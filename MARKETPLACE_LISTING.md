# JetBrains Marketplace Listing — Git Workflow Enforcer

---

## 📝 Plugin Information

| Field       | Value                           |
|-------------|---------------------------------|
| **Name**    | Git Workflow Enforcer           |
| **Plugin ID** | com.jakubjirak.gitworkflow   |
| **Version** | 2026.2.8                        |
| **Category** | Version Control                |
| **Tags**    | git, workflow, conventional-commits, branch-naming, code-review |

---

## 🎯 Short Description

> Validate branch names, commit messages, and merge policies before commits happen. Enforce Gitflow, Trunk-Based, or GitHub Flow with real-time compliance feedback in the status bar.

*(197 characters)*

---

## 📖 Full Description

### 🛡️ Consistent Git Workflows Shouldn't Rely on Code Review

**The Problem:** Git naming conventions break down when they depend on team discipline alone. Bad branch names, sloppy commit messages, and direct pushes to protected branches slip through — causing CI failures, messy histories, and merge conflicts that waste hours.

**The Solution:** Git Workflow Enforcer validates branch names, commit messages, and merge policies **before** commits happen. A real-time status bar widget shows compliance at a glance, and pre-commit hooks block violations before they reach your repository.

---

### ✨ Key Features

#### 📊 Real-Time Status Bar
- **Compliance indicator:** `✓ Compliant` (green) or `✗ N Violation(s)` (red/yellow) — always visible
- Instant feedback as you work — no waiting for CI

#### 🌿 Branch Name Validation
- **3 built-in workflow presets:** Gitflow, Trunk-Based Development, GitHub Flow
- Custom regex patterns for any naming convention
- Allowed base branch enforcement (e.g., features must branch from `develop`)

#### 💬 Commit Message Enforcement
- **Conventional Commits** format: `<type>(<scope>): <description>`
- 11 allowed types: `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `build`, `ci`, `chore`, `revert`
- Configurable max subject length (default: 72 characters)
- Optional JIRA issue key requirement (`[A-Z]+-[0-9]+`)

#### 🔒 Merge Policy
- Block direct pushes to protected branches (`main`, `master`, `develop`)
- Require pull requests for all protected branch changes
- Merge direction validation (e.g., feature → develop, not feature → main)
- Required reviewers per path (CODEOWNERS-like rules)

#### ⚙️ Workflow Presets

| Preset            | Branch Patterns                                      | Protected Branches    |
|-------------------|------------------------------------------------------|-----------------------|
| **Gitflow**       | `feature/.+`, `bugfix/.+`, `release/.+`, `hotfix/.+` | main, master, develop |
| **Trunk-Based**   | `[a-z0-9-]+/[a-z0-9-]+`                              | main                  |
| **GitHub Flow**   | `[a-z0-9]+(-[a-z0-9]+)*`                              | main                  |
| **Custom**        | User-defined regex                                    | User-defined          |

#### 📋 Violations Tool Window
- All violations grouped by type: Branch Naming, Commit Format, Merge Policy, Base Branch
- Severity levels: ERROR (blocks commit) and WARNING (allows with notice)
- Fix suggestions for every violation

#### 👥 Team Configuration
- **Shared `.git-workflow.yaml`** — version-controlled team rules with auto-reload
- **Audit log** — track all violations and overrides across the team
- **Compliance dashboard** — team-wide metrics and trends

---

### 💰 Pricing Model

Free & open source (Apache-2.0) — every feature is available to everyone, no license needed.

---

### 🎯 Perfect For

- **Development teams** enforcing consistent Git conventions
- **Tech leads** tired of reviewing branch names and commit messages
- **Regulated environments** requiring audit trails for Git operations
- **Organizations** using Gitflow, Trunk-Based, or GitHub Flow workflows
- Works with **any Git server**: GitHub, GitLab, Bitbucket, Azure DevOps, self-hosted

---

### ⚙️ Requirements

| Requirement       | Value                            |
|-------------------|----------------------------------|
| **IDE Version**   | IntelliJ IDEA 2024.3+ (build 232+) |
| **Java Version**  | 17+                             |
| **Dependencies**  | Git4Idea (bundled with all JetBrains IDEs) |

---

### 🚀 Quick Start

1. **Install** the plugin from JetBrains Marketplace
2. **Open** any Git-tracked project
3. Check the **status bar** for your compliance status
4. Go to **Settings → Tools → Git Workflow Enforcer** to select a workflow preset
5. Try committing with a non-conventional message — the pre-commit hook catches it!
6. Open the **Git Workflow** tool window to review all violations

---

## 📸 Screenshots & Media

| # | Screenshot Description                                                     |
|---|---------------------------------------------------------------------------|
| 1 | **Status Bar Widget** — Real-time compliance indicator showing `✓ Compliant` or `✗ Violations` |
| 2 | **Pre-Commit Validation** — Commit dialog with violation warning blocking a bad commit message |
| 3 | **Violations Tool Window** — Grouped violations by type with severity and fix suggestions |
| 4 | **Settings Panel** — Workflow preset selector (Gitflow/Trunk/GitHub Flow/Custom) with branch patterns |
| 5 | **Conventional Commits** — Commit message editor with type dropdown and validation feedback |
| 6 | **Merge Policy Configuration** — Protected branches and required reviewers setup |
| 7 | **Team Config File** — `.git-workflow.yaml` editor with auto-reload notification |
| 8 | **Compliance Dashboard** — Team-wide compliance metrics and violation trends |

---

## 🏷️ Marketplace Categories

| Type          | Category                 |
|---------------|--------------------------|
| **Primary**   | Version Control          |
| **Secondary** | Code Quality, Team Tools |

---

## 🔍 Keywords (SEO)

`git workflow`, `branch naming`, `commit message validation`, `conventional commits`, `gitflow`, `trunk-based development`, `github flow`, `merge policy`, `protected branches`, `JIRA integration`, `git compliance`, `branch validation`, `pre-commit hook`, `code review automation`, `git conventions`

---

## 📋 Compatibility

| IDE               | Compatible |
|-------------------|------------|
| IntelliJ IDEA     | ✅          |
| WebStorm          | ✅          |
| PyCharm           | ✅          |
| GoLand            | ✅          |
| Rider             | ✅          |
| PhpStorm          | ✅          |
| CLion             | ✅          |
| RubyMine          | ✅          |
| Android Studio    | ✅          |

**Minimum Version:** IntelliJ Platform 2024.3+ (build 232)

---

## 📞 Vendor Information

| Field        | Value                          |
|--------------|--------------------------------|
| **Vendor**   | Jakub Jirak                    |
| **Email**    | jirak@jakubjirak.com           |
| **Website**  | https://jakubjirak.com         |
| **Support**  | support@jakubjirak.com         |

---

## ✅ Pre-Launch Checklist

- [ ] Plugin icon (SVG, 40×40px) — designed and included
- [ ] Plugin description finalized (< 10,000 characters)
- [ ] Short description finalized (< 255 characters)
- [ ] Screenshots captured (minimum 5, 1280×800px recommended)
- [ ] Plugin signed with JetBrains Marketplace certificate
- [ ] Changelog written for initial release
- [ ] Vendor verified on JetBrains Marketplace
- [ ] Compatibility range tested (2024.3+)
- [ ] License agreement uploaded (MIT)
- [ ] Support URL / email configured
- [ ] README and documentation published
- [ ] Landing page live at vendor website
- [ ] Social media announcements prepared
