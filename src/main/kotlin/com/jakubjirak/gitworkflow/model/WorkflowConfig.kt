package com.jakubjirak.gitworkflow.model

/**
 * Complete workflow configuration combining all rule types.
 *
 * @param preset The workflow preset being used
 * @param branchRules Allowed branch naming patterns
 * @param commitRules Commit message validation rules
 * @param mergeRules Merge and push policy rules
 */
data class WorkflowConfig(
    val preset: WorkflowPreset = WorkflowPreset.GITFLOW,
    val branchRules: List<BranchRule> = emptyList(),
    val commitRules: CommitRule = CommitRule(),
    val mergeRules: MergeRule = MergeRule(),
)
