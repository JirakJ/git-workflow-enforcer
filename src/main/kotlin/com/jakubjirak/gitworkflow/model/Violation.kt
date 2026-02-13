package com.jakubjirak.gitworkflow.model

/**
 * Represents a detected workflow policy violation.
 *
 * @param type The category of the violation
 * @param severity How severe the violation is
 * @param message Human-readable description of the violation
 * @param suggestion Optional suggestion for fixing the violation
 * @param filePath Optional file path related to the violation
 */
data class Violation(
    val type: ViolationType,
    val severity: Severity,
    val message: String,
    val suggestion: String = "",
    val filePath: String = "",
)
