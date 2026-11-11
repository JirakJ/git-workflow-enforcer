package com.jakubjirak.gitworkflow.validators

import com.jakubjirak.gitworkflow.model.BranchRule
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
        branchRules = listOf(BranchRule("feature/.+", listOf("develop"))),
        mergeRules = MergeRule(protectedBranches = listOf("main", "release/.*"), blockDirectPush = true),
    )

    @Test
    fun `protected branch target is an error`() {
        val violation = MergePolicyValidator.validateMergeTarget("main", config).single()
        assertEquals(ViolationType.MERGE_POLICY, violation.type)
        assertEquals(Severity.ERROR, violation.severity)
    }

    @Test
    fun `regex protected branches and unprotected targets`() {
        assertEquals(1, MergePolicyValidator.validateMergeTarget("release/1.0", config).size)
        assertTrue(MergePolicyValidator.validateMergeTarget("develop", config).isEmpty())
    }

    @Test
    fun `direct push allowed when blocking is disabled`() {
        val lenient = config.copy(mergeRules = config.mergeRules.copy(blockDirectPush = false))
        assertTrue(MergePolicyValidator.validateMergeTarget("main", lenient).isEmpty())
    }

    @Test
    fun `isProtected tolerates invalid regex`() {
        assertTrue(MergePolicyValidator.isProtected("[", listOf("[")))
        assertFalse(MergePolicyValidator.isProtected("x", listOf("[")))
    }

    @Test
    fun `merge direction follows the source rule's allowed bases`() {
        assertTrue(MergePolicyValidator.validateMergeDirection("feature/a", "develop", config).isEmpty())
        val violation = MergePolicyValidator.validateMergeDirection("feature/a", "main", config).single()
        assertEquals(Severity.WARNING, violation.severity)
        assertEquals("Merge into develop instead", violation.suggestion)
        assertTrue(MergePolicyValidator.validateMergeDirection("unmatched", "main", config).isEmpty())
    }
}
