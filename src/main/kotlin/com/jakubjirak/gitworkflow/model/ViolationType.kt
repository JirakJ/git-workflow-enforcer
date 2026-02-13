package com.jakubjirak.gitworkflow.model

/**
 * Types of workflow violations.
 */
enum class ViolationType {
    BRANCH_NAME,
    COMMIT_FORMAT,
    MERGE_POLICY,
    BASE_BRANCH,
}
