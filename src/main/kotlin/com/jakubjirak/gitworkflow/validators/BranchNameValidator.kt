package com.jakubjirak.gitworkflow.validators

import com.jakubjirak.gitworkflow.model.BranchRule
import com.jakubjirak.gitworkflow.model.Severity
import com.jakubjirak.gitworkflow.model.Violation
import com.jakubjirak.gitworkflow.model.ViolationType
import com.jakubjirak.gitworkflow.model.WorkflowConfig
import com.intellij.openapi.diagnostic.Logger

/**
 * Validates branch names against configured naming rules.
 */
object BranchNameValidator {

    private val LOG = Logger.getInstance(BranchNameValidator::class.java)

    fun validate(branchName: String, config: WorkflowConfig): List<Violation> {
        val rules = config.branchRules
        if (rules.isEmpty()) return emptyList()

        val matchingRule = findMatchingRule(branchName, rules)
        if (matchingRule != null) {
            LOG.debug("Branch '$branchName' matches rule: ${matchingRule.pattern}")
            return emptyList()
        }

        val allowedPatterns = rules.joinToString(", ") { it.pattern }
        val suggestion = suggestBranchName(branchName, rules)

        return listOf(
            Violation(
                type = ViolationType.BRANCH_NAME,
                severity = Severity.ERROR,
                message = "Branch name \"$branchName\" does not match any allowed pattern: $allowedPatterns",
                suggestion = suggestion,
            )
        )
    }

    fun findMatchingRule(branchName: String, rules: List<BranchRule>): BranchRule? {
        return rules.firstOrNull { rule ->
            try {
                branchName.matches(Regex(rule.pattern))
            } catch (e: Exception) {
                LOG.warn("Invalid regex pattern in branch rule: ${rule.pattern}", e)
                false
            }
        }
    }

    private fun suggestBranchName(branchName: String, rules: List<BranchRule>): String {
        val prefixRules = rules.filter { it.pattern.contains("/") }
        if (prefixRules.isEmpty()) return "Rename branch to match allowed patterns"

        val firstPrefix = prefixRules.first().pattern.substringBefore("/")
        val cleanName = branchName.replace(Regex("[^a-z0-9-]"), "-").trim('-')
        return "Consider: $firstPrefix/$cleanName"
    }
}
