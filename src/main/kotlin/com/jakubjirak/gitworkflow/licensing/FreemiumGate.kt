package com.jakubjirak.gitworkflow.licensing

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service

/**
 * Central gate for freemium feature access.
 * Free tier: basic branch naming rules, 1 preset, status bar widget.
 * Paid tier: full policy config, audit log, team config, all presets, merge policy enforcement.
 */
@Service(Service.Level.APP)
class FreemiumGate {

    enum class LicenseStatus {
        FREE, TRIAL, PAID, EXPIRED
    }

    /**
     * Check if a paid feature is available for the current license.
     */
    @Suppress("UNUSED_PARAMETER")
    fun isPaidFeatureAvailable(featureId: String): Boolean {
        val status = getLicenseStatus()
        return status == LicenseStatus.PAID || status == LicenseStatus.TRIAL
    }

    fun getLicenseStatus(): LicenseStatus {
        // TODO: Integrate with JetBrains Marketplace Licensing API
        return LicenseStatus.FREE
    }

    fun getMaxPresets(): Int {
        return when (getLicenseStatus()) {
            LicenseStatus.FREE -> 1
            LicenseStatus.EXPIRED -> 1
            LicenseStatus.TRIAL, LicenseStatus.PAID -> Int.MAX_VALUE
        }
    }

    companion object {
        // Feature IDs
        const val FEATURE_MERGE_POLICY = "merge_policy"
        const val FEATURE_AUDIT_LOG = "audit_log"
        const val FEATURE_TEAM_CONFIG = "team_config"
        const val FEATURE_ALL_PRESETS = "all_presets"
        const val FEATURE_CUSTOM_RULES = "custom_rules"
        const val FEATURE_PRE_PUSH_HOOK = "pre_push_hook"

        fun getInstance(): FreemiumGate =
            ApplicationManager.getApplication().getService(FreemiumGate::class.java)
    }
}
