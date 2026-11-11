package com.jakubjirak.gitworkflow.settings

import com.jakubjirak.gitworkflow.model.WorkflowPreset
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.RoamingType
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Persistent project-level settings for Git Workflow Enforcer.
 */
@State(
    name = "GitWorkflowSettings",
    storages = [Storage(value = "gitWorkflowSettings.xml", roamingType = RoamingType.DEFAULT)],
)
class GitWorkflowSettings : PersistentStateComponent<GitWorkflowSettings> {

    var enabled: Boolean = true
    var enableBranchValidation: Boolean = true
    var enableCommitValidation: Boolean = true
    var presetName: String = WorkflowPreset.GITFLOW.name

    override fun getState(): GitWorkflowSettings = this

    override fun loadState(state: GitWorkflowSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    fun getPreset(): WorkflowPreset {
        return try {
            WorkflowPreset.valueOf(presetName)
        } catch (_: IllegalArgumentException) {
            WorkflowPreset.GITFLOW
        }
    }

    companion object {
        fun getInstance(project: Project): GitWorkflowSettings =
            project.getService(GitWorkflowSettings::class.java)
    }
}
