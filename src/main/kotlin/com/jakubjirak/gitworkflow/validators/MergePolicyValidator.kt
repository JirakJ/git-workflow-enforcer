package com.jakubjirak.gitworkflow.validators

import com.jakubjirak.gitworkflow.model.Severity
import com.jakubjirak.gitworkflow.model.Violation
import com.jakubjirak.gitworkflow.model.ViolationType
import com.jakubjirak.gitworkflow.model.WorkflowConfig

/**
 * Applies the `merge:` policy of the workflow to commits: committing directly on a
 * protected branch is reported before the commit happens.
 *
 * - `block_direct_push: true`  → ERROR (the pre-commit dialog defaults to Cancel)
 * - `require_pr: true` only    → WARNING
 * - both false                 → no check
 */
object MergePolicyValidator {

    fun validateCommitOnBranch(branchName: String, config: WorkflowConfig): List<Violation> {
        val rules = config.mergeRules
        if (!isProtected(branchName, rules.protectedBranches)) return emptyList()

        val suggestion = "Create a feature branch and merge it into $branchName through a pull request"
        val violation = when {
            rules.blockDirectPush -> Violation(
                type = ViolationType.PROTECTED_BRANCH,
                severity = Severity.ERROR,
                message = "Committing directly on protected branch \"$branchName\" is not allowed",
                suggestion = suggestion,
            )
            rules.requirePR -> Violation(
                type = ViolationType.PROTECTED_BRANCH,
                severity = Severity.WARNING,
                message = "Changes to protected branch \"$branchName\" should go through a pull request",
                suggestion = suggestion,
            )
            else -> null
        }
        return listOfNotNull(violation)
    }

    /** A branch is protected when it equals, or fully matches the regex of, any entry. */
    fun isProtected(branchName: String, protectedBranches: List<String>): Boolean {
        return protectedBranches.any { protected ->
            branchName == protected || try {
                branchName.matches(Regex(protected))
            } catch (_: Exception) {
                false
            }
        }
    }
}
