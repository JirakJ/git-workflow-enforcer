package com.jakubjirak.gitworkflow.validators

import com.jakubjirak.gitworkflow.model.MergeRule
import com.jakubjirak.gitworkflow.model.Severity
import com.jakubjirak.gitworkflow.model.ViolationType
import com.jakubjirak.gitworkflow.model.WorkflowConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MergePolicyValidatorTest {

    private val config = WorkflowConfig(
        mergeRules = MergeRule(protectedBranches = listOf("main", "release/.*"), requirePR = true, blockDirectPush = true),
    )

    @Test
    fun `committing on a protected branch is an error when direct push is blocked`() {
        val violation = MergePolicyValidator.validateCommitOnBranch("main", config).single()
        assertEquals(ViolationType.PROTECTED_BRANCH, violation.type)
        assertEquals(Severity.ERROR, violation.severity)
        assertTrue(violation.message.contains("\"main\""))
        assertTrue(violation.suggestion.contains("pull request"))
    }

    @Test
    fun `regex protected branches match, other branches do not`() {
        assertEquals(1, MergePolicyValidator.validateCommitOnBranch("release/1.0", config).size)
        assertTrue(MergePolicyValidator.validateCommitOnBranch("develop", config).isEmpty())
        assertTrue(MergePolicyValidator.validateCommitOnBranch("feature/main", config).isEmpty())
    }

    @Test
    fun `require_pr without block_direct_push only warns`() {
        val warnOnly = config.copy(mergeRules = config.mergeRules.copy(blockDirectPush = false))
        val violation = MergePolicyValidator.validateCommitOnBranch("main", warnOnly).single()
        assertEquals(Severity.WARNING, violation.severity)
    }

    @Test
    fun `no check when both block_direct_push and require_pr are off`() {
        val off = config.copy(mergeRules = config.mergeRules.copy(blockDirectPush = false, requirePR = false))
        assertTrue(MergePolicyValidator.validateCommitOnBranch("main", off).isEmpty())
    }

    @Test
    fun `no check when the protected list is empty`() {
        val none = config.copy(mergeRules = config.mergeRules.copy(protectedBranches = emptyList()))
        assertTrue(MergePolicyValidator.validateCommitOnBranch("main", none).isEmpty())
    }

    @Test
    fun `isProtected tolerates invalid regex`() {
        assertTrue(MergePolicyValidator.isProtected("[", listOf("[")))
        assertFalse(MergePolicyValidator.isProtected("x", listOf("[")))
    }
}
