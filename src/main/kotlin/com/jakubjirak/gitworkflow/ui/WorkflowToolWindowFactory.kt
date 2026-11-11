package com.jakubjirak.gitworkflow.ui

import com.jakubjirak.gitworkflow.config.WorkflowConfigLoader
import com.jakubjirak.gitworkflow.engine.WorkflowEnforcerEngine
import com.jakubjirak.gitworkflow.model.Severity
import com.jakubjirak.gitworkflow.model.Violation
import com.jakubjirak.gitworkflow.model.ViolationType
import com.jakubjirak.gitworkflow.model.WorkflowConfig
import com.jakubjirak.gitworkflow.settings.GitWorkflowSettings
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import git4idea.repo.GitRepositoryManager
import java.awt.BorderLayout
import java.io.File
import javax.swing.DefaultListModel
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.SwingConstants

/**
 * Tool window showing current workflow violations with fix suggestions.
 */
class WorkflowToolWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = WorkflowToolWindowPanel(project)
        val content = ContentFactory.getInstance().createContent(panel, "Violations", false)
        toolWindow.contentManager.addContent(content)
    }
}

class WorkflowToolWindowPanel(private val project: Project) : JPanel(BorderLayout()) {

    private val LOG = Logger.getInstance(WorkflowToolWindowPanel::class.java)
    private val listModel = DefaultListModel<String>()
    private val violationList = JList(listModel)

    init {
        val toolbar = JPanel()
        val refreshButton = JButton("Refresh")
        refreshButton.addActionListener { refreshViolations() }
        toolbar.add(refreshButton)
        add(toolbar, BorderLayout.NORTH)

        add(JBScrollPane(violationList), BorderLayout.CENTER)

        refreshViolations()
    }

    private fun refreshViolations() {
        listModel.clear()
        try {
            val violations = collectViolations()
            displayViolations(violations)
        } catch (e: Exception) {
            LOG.warn("Error refreshing violations", e)
            listModel.addElement("Error loading violations: ${e.message}")
        }
    }

    private fun collectViolations(): List<Violation>? {
        val config = loadConfig()
        if (config == null) {
            listModel.addElement("No workflow configuration found")
            return null
        }

        val branchName = getCurrentBranch()
        if (branchName == null) {
            listModel.addElement("No Git repository detected")
            return null
        }

        return WorkflowEnforcerEngine.validateBranch(branchName, config)
    }

    private fun displayViolations(violations: List<Violation>?) {
        if (violations == null) return

        if (violations.isEmpty()) {
            listModel.addElement("✓ No violations detected — branch is compliant")
            return
        }

        groupViolations(violations).forEach { (type, typeViolations) ->
            listModel.addElement("── ${getTypeLabel(type)} ──")
            typeViolations.forEach { v -> addViolationEntry(v) }
        }
    }

    private fun addViolationEntry(violation: Violation) {
        val icon = if (violation.severity == Severity.ERROR) "✗" else "⚠"
        listModel.addElement("  $icon ${violation.message}")
        if (violation.suggestion.isNotEmpty()) {
            listModel.addElement("    → ${violation.suggestion}")
        }
    }

    private fun groupViolations(violations: List<Violation>): Map<ViolationType, List<Violation>> {
        return violations.groupBy { it.type }
    }

    private fun getTypeLabel(type: ViolationType): String {
        return when (type) {
            ViolationType.BRANCH_NAME -> "Branch Naming"
            ViolationType.COMMIT_FORMAT -> "Commit Format"
            ViolationType.PROTECTED_BRANCH -> "Protected Branch"
        }
    }

    private fun loadConfig(): WorkflowConfig? {
        val basePath = project.basePath ?: return null
        val config = WorkflowConfigLoader.loadFromDirectory(File(basePath))
        if (config != null) return config
        val settings = GitWorkflowSettings.getInstance(project)
        return WorkflowConfigLoader.getDefaultConfig(settings.getPreset())
    }

    private fun getCurrentBranch(): String? {
        return try {
            val repoManager = GitRepositoryManager.getInstance(project)
            repoManager.repositories.firstOrNull()?.currentBranch?.name
        } catch (e: Exception) {
            LOG.debug("Could not determine current branch", e)
            null
        }
    }
}
