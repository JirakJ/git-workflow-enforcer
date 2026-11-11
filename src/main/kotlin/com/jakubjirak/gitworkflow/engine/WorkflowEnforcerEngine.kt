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

    fun validateProtectedBranch(branchName: String, config: WorkflowConfig): List<Violation> {
        return try {
            MergePolicyValidator.validateCommitOnBranch(branchName, config)
        } catch (e: Exception) {
            LOG.error("Error checking protected branch: $branchName", e)
            emptyList()
        }
    }

    /**
     * Everything the pre-commit check reports for a commit of [commitMessage] on [branchName]
     * (null when HEAD is detached / no repository).
     *
     * - commit message rules run when [checkMessage] (setting "Enable commit message validation")
     * - branch naming rules run when [checkBranchName] (setting "Enable branch name validation")
     * - the protected-branch policy (`merge:` section) always runs; it is configured, or switched
     *   off, through `protected_branches`, `block_direct_push` and `require_pr`
     */
    fun validateCommit(
        commitMessage: String?,
        branchName: String?,
        config: WorkflowConfig,
        checkMessage: Boolean = true,
        checkBranchName: Boolean = true,
    ): List<Violation> {
        val violations = mutableListOf<Violation>()

        if (checkMessage && commitMessage != null) {
            violations.addAll(validateCommitMessage(commitMessage, config))
        }
        if (branchName != null) {
            if (checkBranchName) violations.addAll(validateBranch(branchName, config))
            violations.addAll(validateProtectedBranch(branchName, config))
        }

        return violations
    }
}
