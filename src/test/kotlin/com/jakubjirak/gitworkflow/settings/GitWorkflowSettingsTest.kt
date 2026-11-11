package com.jakubjirak.gitworkflow.settings

import com.jakubjirak.gitworkflow.model.WorkflowPreset
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GitWorkflowSettingsTest {

    @Test
    fun `defaults`() {
        val settings = GitWorkflowSettings()
        assertTrue(settings.enabled)
        assertTrue(settings.enableBranchValidation)
        assertTrue(settings.enableCommitValidation)
        assertEquals(WorkflowPreset.GITFLOW, settings.getPreset())
    }

    @Test
    fun `unknown preset name falls back to gitflow`() {
        val settings = GitWorkflowSettings().apply { presetName = "NOPE" }
        assertEquals(WorkflowPreset.GITFLOW, settings.getPreset())
        settings.presetName = WorkflowPreset.GITHUB_FLOW.name
        assertEquals(WorkflowPreset.GITHUB_FLOW, settings.getPreset())
    }

    @Test
    fun `loadState copies values`() {
        val source = GitWorkflowSettings().apply {
            enabled = false
            enableCommitValidation = false
            presetName = WorkflowPreset.TRUNK_BASED.name
        }
        val target = GitWorkflowSettings()
        target.loadState(source)
        assertFalse(target.enabled)
        assertFalse(target.enableCommitValidation)
        assertEquals(WorkflowPreset.TRUNK_BASED, target.getPreset())
    }
}
