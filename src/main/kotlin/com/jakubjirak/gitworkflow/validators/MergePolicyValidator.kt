package com.jakubjirak.gitworkflow.validators

import com.jakubjirak.gitworkflow.model.Severity
import com.jakubjirak.gitworkflow.model.Violation
import com.jakubjirak.gitworkflow.model.ViolationType
import com.jakubjirak.gitworkflow.model.WorkflowConfig
import com.intellij.openapi.diagnostic.Logger

/**
 * Validates merge operations against configured merge policies.
 * Checks protected branches, merge direction, and PR requirements.
 */
object MergePolicyValidator {

    private val LOG = Logger.getInstance(MergePolicyValidator::class.java)

    fun validateMergeTarget(
        targetBranch: String,
        config: WorkflowConfig,
    ): List<Violation> {
        val mergeRules = config.mergeRules
        val violations = mutableListOf<Violation>()

        if (mergeRules.blockDirectPush && isProtected(targetBranch, mergeRules.protectedBranches)) {
            violations.add(
                Violation(
                    type = ViolationType.MERGE_POLICY,
                    severity = Severity.ERROR,
                    message = "Direct push to protected branch \"$targetBranch\" is not allowed",
                    suggestion = "Create a pull request to merge changes into $targetBranch",
                )
            )
        }

        return violations
    }

    fun validateMergeDirection(
        sourceBranch: String,
        targetBranch: String,
        config: WorkflowConfig,
    ): List<Violation> {
        val branchRules = config.branchRules
        val violations = mutableListOf<Violation>()

        val sourceRule = branchRules.firstOrNull { rule ->
            try {
                sourceBranch.matches(Regex(rule.pattern))
            } catch (e: Exception) {
                LOG.warn("Invalid regex pattern: ${rule.pattern}", e)
                false
            }
        }

        if (sourceRule != null && sourceRule.allowedBase.isNotEmpty()) {
            val validTargets = sourceRule.allowedBase
            if (targetBranch !in validTargets) {
                violations.add(
                    Violation(
                        type = ViolationType.MERGE_POLICY,
                        severity = Severity.WARNING,
                        message = "Merge from \"$sourceBranch\" to \"$targetBranch\" is not standard. " +
                            "Expected target: ${validTargets.joinToString(", ")}",
                        suggestion = "Merge into ${validTargets.first()} instead",
                    )
                )
            }
        }

        return violations
    }

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
