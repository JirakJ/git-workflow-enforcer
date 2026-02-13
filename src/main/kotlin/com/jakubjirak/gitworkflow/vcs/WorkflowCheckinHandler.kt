package com.jakubjirak.gitworkflow.vcs

import com.jakubjirak.gitworkflow.config.WorkflowConfigLoader
import com.jakubjirak.gitworkflow.engine.WorkflowEnforcerEngine
import com.jakubjirak.gitworkflow.licensing.FreemiumGate
import com.jakubjirak.gitworkflow.model.Severity
import com.jakubjirak.gitworkflow.model.Violation
import com.jakubjirak.gitworkflow.settings.GitWorkflowSettings
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory
import git4idea.repo.GitRepositoryManager
import java.io.File

/**
 * Factory that creates pre-commit checkin handlers for workflow validation.
 */
class WorkflowCheckinHandlerFactory : CheckinHandlerFactory() {

    override fun createHandler(panel: CheckinProjectPanel, commitContext: CommitContext): CheckinHandler {
        return WorkflowCheckinHandler(panel)
    }
}

/**
 * Pre-commit validation handler that checks commit messages against workflow rules.
 */
class WorkflowCheckinHandler(
    private val panel: CheckinProjectPanel,
) : CheckinHandler() {

    private val LOG = Logger.getInstance(WorkflowCheckinHandler::class.java)

    override fun beforeCheckin(): ReturnResult {
        val project = panel.project
        val settings = GitWorkflowSettings.getInstance(project)

        if (!settings.enabled || !settings.enableCommitValidation) {
            return ReturnResult.COMMIT
        }

        val config = loadConfig(project) ?: return ReturnResult.COMMIT

        val violations = collectViolations(project, config, settings)
        if (violations.isEmpty()) return ReturnResult.COMMIT

        return showViolationDialog(project, violations)
    }

    private fun collectViolations(
        project: Project,
        config: com.jakubjirak.gitworkflow.model.WorkflowConfig,
        settings: GitWorkflowSettings,
    ): List<Violation> {
        val violations = mutableListOf<Violation>()
        violations.addAll(WorkflowEnforcerEngine.validateCommitMessage(panel.commitMessage, config))
        val branchName = getCurrentBranch(project)
        if (branchName != null && settings.enableBranchValidation) {
            violations.addAll(WorkflowEnforcerEngine.validateBranch(branchName, config))
        }
        return violations
    }

    private fun showViolationDialog(project: Project, violations: List<Violation>): ReturnResult {
        val errors = violations.filter { it.severity == Severity.ERROR }
        val warnings = violations.filter { it.severity == Severity.WARNING }
        val message = buildViolationMessage(errors, warnings)
        val cancelLabel = if (errors.isNotEmpty()) "Cancel" else "Fix Issues"
        val result = Messages.showYesNoDialog(
            project, message, "Git Workflow Enforcer", "Commit Anyway", cancelLabel, Messages.getWarningIcon(),
        )
        return if (result == Messages.YES) ReturnResult.COMMIT else ReturnResult.CANCEL
    }

    private fun loadConfig(project: Project): com.jakubjirak.gitworkflow.model.WorkflowConfig? {
        val basePath = project.basePath ?: return null
        val config = WorkflowConfigLoader.loadFromDirectory(File(basePath))
        if (config != null) return config

        val settings = GitWorkflowSettings.getInstance(project)
        return WorkflowConfigLoader.getDefaultConfig(settings.getPreset())
    }

    private fun getCurrentBranch(project: Project): String? {
        return try {
            val repoManager = GitRepositoryManager.getInstance(project)
            repoManager.repositories.firstOrNull()?.currentBranch?.name
        } catch (e: Exception) {
            LOG.debug("Could not determine current branch", e)
            null
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun buildViolationMessage(errors: List<Violation>, warnings: List<Violation>): String {
        return buildString {
            if (errors.isNotEmpty()) {
                appendLine("Errors:")
                errors.forEach { v ->
                    appendLine("  ✗ ${v.message}")
                    if (v.suggestion.isNotEmpty()) appendLine("    → ${v.suggestion}")
                }
            }
            if (warnings.isNotEmpty()) {
                if (errors.isNotEmpty()) appendLine()
                appendLine("Warnings:")
                warnings.forEach { v ->
                    appendLine("  ⚠ ${v.message}")
                    if (v.suggestion.isNotEmpty()) appendLine("    → ${v.suggestion}")
                }
            }
        }.trim()
    }
}
