package com.jakubjirak.gitworkflow.model

/**
 * Rules for commit message validation.
 *
 * @param formatPattern Regex the full commit message must match (empty = no constraint)
 * @param maxSubjectLength Maximum length of the first line
 * @param requireIssueRef Whether an issue reference is required
 * @param issueRefPattern Regex for the issue reference (e.g. JIRA key)
 * @param conventionalCommits Whether Conventional Commits format is enforced
 * @param allowedTypes Allowed commit types for Conventional Commits
 */
data class CommitRule(
    val formatPattern: String = "",
    val maxSubjectLength: Int = 72,
    val requireIssueRef: Boolean = false,
    val issueRefPattern: String = "[A-Z]+-\\d+",
    val conventionalCommits: Boolean = false,
    val allowedTypes: List<String> = listOf("feat", "fix", "docs", "style", "refactor", "perf", "test", "build", "ci", "chore", "revert"),
)
