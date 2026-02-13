package com.jakubjirak.gitworkflow.engine

import com.jakubjirak.gitworkflow.model.Violation
import com.jakubjirak.gitworkflow.model.WorkflowConfig
import com.jakubjirak.gitworkflow.validators.BranchNameValidator
import com.jakubjirak.gitworkflow.validators.CommitMessageValidator
import com.jakubjirak.gitworkflow.validators.MergePolicyValidator
import com.intellij.openapi.diagnostic.Logger

/**
 * Central orchestrator for workflow validation.
 * Runs all configured validators and collects violations.
 */
object WorkflowEnforcerEngine {

    private val LOG = Logger.getInstance(WorkflowEnforcerEngine::class.java)

    fun validateBranch(branchName: String, config: WorkflowConfig): List<Violation> {
        return try {
            BranchNameValidator.validate(branchName, config)
        } catch (e: Exception) {
            LOG.error("Error validating branch name: $branchName", e)
            emptyList()
        }
    }

    fun validateCommitMessage(message: String, config: WorkflowConfig): List<Violation> {
        return try {
            CommitMessageValidator.validate(message, config.commitRules)
        } catch (e: Exception) {
            LOG.error("Error validating commit message", e)
            emptyList()
        }
    }

    fun validateMergeTarget(targetBranch: String, config: WorkflowConfig): List<Violation> {
        return try {
            MergePolicyValidator.validateMergeTarget(targetBranch, config)
        } catch (e: Exception) {
            LOG.error("Error validating merge target: $targetBranch", e)
            emptyList()
        }
    }

    fun validateAll(
        branchName: String,
        commitMessage: String?,
        config: WorkflowConfig,
    ): List<Violation> {
        val violations = mutableListOf<Violation>()

        violations.addAll(validateBranch(branchName, config))

        if (!commitMessage.isNullOrBlank()) {
            violations.addAll(validateCommitMessage(commitMessage, config))
        }

        violations.addAll(validateMergeTarget(branchName, config))

        return violations
    }
}
