# Changelog

## [Unreleased]

## [2026.9.0] - 2026-11-11

### Removed
- Settings that had no effect: "merge policy validation", "Conventional Commits", "require issue reference", "max subject length" and "protected branches". These rules always came from the selected preset or `.git-workflow.yaml`; the settings page now says so.
- Documentation of features that do not exist: merge/push policy enforcement, merge direction checks, required reviewers, pre-push hook, config auto-reload, audit log and the compliance dashboard.

### Fixed
- The README `.git-workflow.yaml` example used keys the loader does not read (`branchRules`, `allowedBase`, `commitRules`, …); it now uses the real keys (`branches`, `allowed_base`, `commits`, `max_subject_length`, …).
- An unknown preset name stored in settings no longer breaks the settings page.

## [2026.8.0] - 2026-11-10

### Changed
- The plugin is now completely free — all features unlocked, no license needed.
- Source code is now open source under Apache-2.0: https://github.com/JirakJ/git-workflow-enforcer

## [2026.3.6] - 2026-04-18

### Changed
- Enabled freemium licensing via `<product-descriptor optional="true">`. Plugin remains fully functional without a license; premium features may be introduced in future minor releases.


## [2026.2.5] - 2026-02-14

### Fixed
- Removed deprecated API usage (postStartupActivity → projectActivity, StatusBarWidget.getPresentation)
- No experimental API usage


## [2026.2.4] - 2026-02-13

### Added
- Added ProGuard obfuscation for code protection


## [2026.2.3] - 2026-02-13

### Changed
- Version bump for JetBrains Marketplace publication


## [2026.2.2] - 2026-02-13

### Changed
- Expanded compatibility range: sinceBuild 232.0+ (IntelliJ 2023.2+)
- Removed upper version bound for future IDE compatibility


## [2026.2.1] - 2026-02-13

### Added
- Initial release
- Branch name validation against configurable regex patterns
- Commit message validation with max subject length enforcement (default 72 chars)
- Conventional Commits format enforcement (`type(scope): description`)
- JIRA/issue key validation in commit messages (`[A-Z]+-[0-9]+`)
- Pre-commit checkin handler with violation dialog (Commit Anyway / Cancel)
- Status bar compliance widget (`✓ Compliant` / `✗ N Violation(s)`)
- Violations tool window grouped by type (Branch Naming / Commit Format / Merge Policy / Base Branch)
- Settings panel (Tools → Git Workflow Enforcer)
- Workflow presets: Gitflow, Trunk-Based Development, GitHub Flow, Custom
- **Enterprise:** Merge policy enforcement — block direct pushes to protected branches
- **Enterprise:** Merge direction validation (e.g., feature → develop only)
- **Enterprise:** Custom regex branch naming rules with allowed base branches
- **Enterprise:** Team config via `.git-workflow.yaml` with auto-reload file watcher
- **Enterprise:** Pre-push validation hook
- **Enterprise:** Required reviewers per path (CODEOWNERS-like)
- Balloon notifications for blocked commands
