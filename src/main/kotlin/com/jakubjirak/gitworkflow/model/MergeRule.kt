package com.jakubjirak.gitworkflow.model

/**
 * Rules for merge and push policies on protected branches.
 *
 * @param protectedBranches Branch names that are protected from direct push
 * @param requirePR Whether changes to protected branches require a pull request
 * @param blockDirectPush Whether direct pushes to protected branches are blocked
 */
data class MergeRule(
    val protectedBranches: List<String> = listOf("main", "master"),
    val requirePR: Boolean = true,
    val blockDirectPush: Boolean = true,
)
