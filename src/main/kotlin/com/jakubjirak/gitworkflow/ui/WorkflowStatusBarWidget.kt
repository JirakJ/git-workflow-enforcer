package com.jakubjirak.gitworkflow.ui

import com.jakubjirak.gitworkflow.config.WorkflowConfigLoader
import com.jakubjirak.gitworkflow.engine.WorkflowEnforcerEngine
import com.jakubjirak.gitworkflow.model.Violation
import com.jakubjirak.gitworkflow.model.WorkflowConfig
import com.jakubjirak.gitworkflow.settings.GitWorkflowSettings
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.util.Consumer
import git4idea.repo.GitRepositoryManager
import java.awt.Component
import java.awt.event.MouseEvent
import java.io.File

class WorkflowStatusBarWidgetFactory : StatusBarWidgetFactory {

    override fun getId(): String = WIDGET_ID

    override fun getDisplayName(): String = "Git Workflow Enforcer"

    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget = WorkflowStatusBarWidget(project)

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true

    companion object {
        const val WIDGET_ID = "com.jakubjirak.gitworkflow.statusBarWidget"
    }
}

class WorkflowStatusBarWidget(private val project: Project) : StatusBarWidget, StatusBarWidget.TextPresentation {

    private val LOG = Logger.getInstance(WorkflowStatusBarWidget::class.java)
    private var statusBar: StatusBar? = null
    private var currentViolations: List<Violation> = emptyList()

    override fun ID(): String = WorkflowStatusBarWidgetFactory.WIDGET_ID

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
        refresh()
    }

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

    @NlsContexts.Label
    override fun getText(): String {
        val settings = GitWorkflowSettings.getInstance(project)
        if (!settings.enabled) return ""

        return if (currentViolations.isEmpty()) {
            "✓ Compliant"
        } else {
            "⚠ ${currentViolations.size} Violation(s)"
        }
    }

    override fun getTooltipText(): String {
        val branchName = getCurrentBranch() ?: "unknown"
        return buildString {
            appendLine("Git Workflow Enforcer")
            appendLine("Branch: $branchName")
            if (currentViolations.isEmpty()) {
                appendLine("Status: Compliant")
            } else {
                appendLine("Status: ${currentViolations.size} violation(s)")
                currentViolations.take(3).forEach { v ->
                    appendLine("  • ${v.message}")
                }
                if (currentViolations.size > 3) {
                    appendLine("  ... and ${currentViolations.size - 3} more")
                }
            }
        }.trim()
    }

    override fun getAlignment(): Float = Component.CENTER_ALIGNMENT

    override fun getClickConsumer(): Consumer<MouseEvent> = Consumer {
        refresh()
    }

    fun refresh() {
        try {
            val config = loadConfig() ?: return
            val branchName = getCurrentBranch() ?: return
            currentViolations = WorkflowEnforcerEngine.validateBranch(branchName, config)
            statusBar?.updateWidget(ID())
        } catch (e: Exception) {
            LOG.warn("Error refreshing workflow status", e)
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

    override fun dispose() {
        statusBar = null
    }
}
