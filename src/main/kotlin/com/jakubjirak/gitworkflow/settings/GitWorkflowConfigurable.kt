package com.jakubjirak.gitworkflow.settings

import com.jakubjirak.gitworkflow.model.WorkflowPreset
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindIntValue
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
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
                        { WorkflowPreset.valueOf(settings.presetName) },
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
            row {
                checkBox("Enable merge policy validation")
                    .bindSelected(settings::enableMergeValidation)
                    .comment("Enforce merge direction rules defined by the workflow")
            }
        }

        group("Commit Messages") {
            row {
                checkBox("Enforce Conventional Commits format")
                    .bindSelected(settings::conventionalCommits)
                    .comment("Require commit messages to follow the Conventional Commits specification")
            }
            row {
                checkBox("Require issue reference in commits")
                    .bindSelected(settings::requireIssueRef)
                    .comment("Require a ticket or issue reference (e.g. JIRA-123) in each commit message")
            }
            row("Max commit subject length:") {
                spinner(20..200)
                    .bindIntValue(settings::maxSubjectLength)
                    .comment("Maximum allowed length for the commit subject line")
            }
        }

        group("Branch Protection") {
            row("Protected branches:") {
                expandableTextField()
                    .bindText(settings::protectedBranches)
                    .comment("Comma-separated list of protected branch names")
            }
        }
    }
}
