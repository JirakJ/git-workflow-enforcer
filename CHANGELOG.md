# Changelog

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
- Freemium licensing gate with Free and Enterprise ($15/dev/month) tiers
