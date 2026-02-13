package com.jakubjirak.gitworkflow.settings

import com.jakubjirak.gitworkflow.model.WorkflowPreset
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.BoxLayout
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

/**
 * Settings UI for Git Workflow Enforcer under Tools.
 */
class GitWorkflowConfigurable(private val project: Project) : Configurable {

    private var enabledCheckBox: JCheckBox? = null
    private var branchValidationCheckBox: JCheckBox? = null
    private var commitValidationCheckBox: JCheckBox? = null
    private var mergeValidationCheckBox: JCheckBox? = null
    private var presetComboBox: JComboBox<String>? = null
    private var maxSubjectLengthField: JTextField? = null
    private var requireIssueRefCheckBox: JCheckBox? = null
    private var conventionalCommitsCheckBox: JCheckBox? = null
    private var protectedBranchesField: JTextField? = null

    override fun getDisplayName(): String = "Git Workflow Enforcer"

    override fun createComponent(): JComponent {
        val settings = GitWorkflowSettings.getInstance(project)

        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        enabledCheckBox = JCheckBox("Enable Git Workflow Enforcer", settings.enabled)
        panel.add(enabledCheckBox)

        val presetPanel = JPanel()
        presetPanel.add(JLabel("Workflow Preset:"))
        presetComboBox = JComboBox(WorkflowPreset.entries.map { it.displayName }.toTypedArray())
        presetComboBox?.selectedIndex = WorkflowPreset.entries.indexOf(settings.getPreset())
        presetPanel.add(presetComboBox)
        panel.add(presetPanel)

        branchValidationCheckBox = JCheckBox("Enable branch name validation", settings.enableBranchValidation)
        panel.add(branchValidationCheckBox)

        commitValidationCheckBox = JCheckBox("Enable commit message validation", settings.enableCommitValidation)
        panel.add(commitValidationCheckBox)

        mergeValidationCheckBox = JCheckBox("Enable merge policy validation", settings.enableMergeValidation)
        panel.add(mergeValidationCheckBox)

        conventionalCommitsCheckBox = JCheckBox("Enforce Conventional Commits format", settings.conventionalCommits)
        panel.add(conventionalCommitsCheckBox)

        requireIssueRefCheckBox = JCheckBox("Require issue reference in commits", settings.requireIssueRef)
        panel.add(requireIssueRefCheckBox)

        val subjectPanel = JPanel()
        subjectPanel.add(JLabel("Max commit subject length:"))
        maxSubjectLengthField = JTextField(settings.maxSubjectLength.toString(), 5)
        subjectPanel.add(maxSubjectLengthField)
        panel.add(subjectPanel)

        val branchPanel = JPanel()
        branchPanel.add(JLabel("Protected branches (comma-separated):"))
        protectedBranchesField = JTextField(settings.protectedBranches, 30)
        branchPanel.add(protectedBranchesField)
        panel.add(branchPanel)

        return panel
    }

    override fun isModified(): Boolean {
        val settings = GitWorkflowSettings.getInstance(project)
        return enabledCheckBox?.isSelected != settings.enabled ||
            branchValidationCheckBox?.isSelected != settings.enableBranchValidation ||
            commitValidationCheckBox?.isSelected != settings.enableCommitValidation ||
            mergeValidationCheckBox?.isSelected != settings.enableMergeValidation ||
            getSelectedPreset().name != settings.presetName ||
            maxSubjectLengthField?.text != settings.maxSubjectLength.toString() ||
            requireIssueRefCheckBox?.isSelected != settings.requireIssueRef ||
            conventionalCommitsCheckBox?.isSelected != settings.conventionalCommits ||
            protectedBranchesField?.text != settings.protectedBranches
    }

    override fun apply() {
        val settings = GitWorkflowSettings.getInstance(project)
        settings.enabled = enabledCheckBox?.isSelected ?: true
        settings.enableBranchValidation = branchValidationCheckBox?.isSelected ?: true
        settings.enableCommitValidation = commitValidationCheckBox?.isSelected ?: true
        settings.enableMergeValidation = mergeValidationCheckBox?.isSelected ?: true
        settings.presetName = getSelectedPreset().name
        settings.maxSubjectLength = maxSubjectLengthField?.text?.toIntOrNull() ?: 72
        settings.requireIssueRef = requireIssueRefCheckBox?.isSelected ?: false
        settings.conventionalCommits = conventionalCommitsCheckBox?.isSelected ?: true
        settings.protectedBranches = protectedBranchesField?.text ?: "main,master,develop"
    }

    override fun reset() {
        val settings = GitWorkflowSettings.getInstance(project)
        enabledCheckBox?.isSelected = settings.enabled
        branchValidationCheckBox?.isSelected = settings.enableBranchValidation
        commitValidationCheckBox?.isSelected = settings.enableCommitValidation
        mergeValidationCheckBox?.isSelected = settings.enableMergeValidation
        presetComboBox?.selectedIndex = WorkflowPreset.entries.indexOf(settings.getPreset())
        maxSubjectLengthField?.text = settings.maxSubjectLength.toString()
        requireIssueRefCheckBox?.isSelected = settings.requireIssueRef
        conventionalCommitsCheckBox?.isSelected = settings.conventionalCommits
        protectedBranchesField?.text = settings.protectedBranches
    }

    private fun getSelectedPreset(): WorkflowPreset {
        val index = presetComboBox?.selectedIndex ?: 0
        return WorkflowPreset.entries.getOrElse(index) { WorkflowPreset.GITFLOW }
    }
}
