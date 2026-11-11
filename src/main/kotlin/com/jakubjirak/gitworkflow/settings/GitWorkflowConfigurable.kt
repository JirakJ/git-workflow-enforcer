package com.jakubjirak.gitworkflow.settings

import com.jakubjirak.gitworkflow.model.WorkflowPreset
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel

/**
 * Settings UI for Git Workflow Enforcer under Tools.
 */
class GitWorkflowConfigurable(private val project: Project) : BoundConfigurable("Git Workflow Enforcer") {

    override fun createPanel() = panel {
        val settings = GitWorkflowSettings.getInstance(project)

        group("General") {
            row {
                checkBox("Enable Git Workflow Enforcer")
                    .bindSelected(settings::enabled)
                    .comment("Toggle all workflow enforcement on or off")
            }
            row("Workflow preset:") {
                comboBox(WorkflowPreset.entries.toList())
                    .bindItem(
                        { settings.getPreset() },
                        { settings.presetName = (it ?: WorkflowPreset.GITFLOW).name }
                    )
                    .comment("Select a predefined workflow or use Custom for full control")
            }
        }

        group("Validation Rules") {
            row {
                checkBox("Enable branch name validation")
                    .bindSelected(settings::enableBranchValidation)
                    .comment("Validate branch names against the selected workflow naming conventions")
            }
            row {
                checkBox("Enable commit message validation")
                    .bindSelected(settings::enableCommitValidation)
                    .comment("Validate commit messages for format and content requirements")
            }
        }

        group("Team Configuration") {
            row {
                comment(
                    "Commit message rules (Conventional Commits, issue reference, subject length), branch " +
                        "patterns and protected branches come from the selected preset. Override them for the whole team in a " +
                        "<code>.git-workflow.yaml</code> file in the project root.",
                )
            }
        }
    }
}
