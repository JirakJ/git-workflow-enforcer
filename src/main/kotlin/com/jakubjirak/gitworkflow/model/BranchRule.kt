package com.jakubjirak.gitworkflow.model

/**
 * Rule defining an allowed branch naming pattern.
 *
 * @param pattern Regex pattern the branch name must match
 * @param allowedBase Branches from which this branch type can be created (empty = any)
 * @param description Human-readable description of the rule
 */
data class BranchRule(
    val pattern: String,
    val allowedBase: List<String> = emptyList(),
    val description: String = "",
)
