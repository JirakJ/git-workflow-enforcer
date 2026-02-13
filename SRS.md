# Git Workflow Enforcer — Software Requirements Specification (SRS)

**Verze:** 2026.2.1
**Autor:** Jakub Jirák
**Datum:** 12. února 2026
**Model:** Freemium JetBrains Plugin

---

## 1. Úvod

### 1.1 Účel
Git Workflow Enforcer vynucuje týmová Git workflow pravidla přímo v IDE: pojmenování větví, commit message formát, merge politiky, povinní revieweři dle cest a pre-push validace. Cílem je standardizace Git workflow v týmu bez závislosti na server-side řešeních.

### 1.2 Rozsah
Plugin načítá konfiguraci z `.git-workflow.yaml` v projektu, v reálném čase validuje akce vývojáře a zobrazuje compliance status ve status baru. Podporuje workflow presety (Gitflow, trunk-based, GitHub Flow) i plně custom konfigurace.

### 1.3 Reference
- Konfigurační soubor: `.git-workflow.yaml` (sdílený přes Git)
- Závislosti: Git4Idea plugin, SnakeYAML 2.3

---

## 2. Celkový popis

### 2.1 Perspektiva produktu
Enterprise-focused plugin pro týmy potřebující Git workflow standardizaci. IDE-native řešení poskytující okamžitou zpětnou vazbu bez nutnosti server-side konfigurace. Žádný přímý konkurent na JetBrains Marketplace — existující řešení jsou buď client-side git hooks (těžko sdílitelné), nebo server-side (GitHub branch protection, GitLab rules) bez IDE integrace.

### 2.2 Uživatelské skupiny
- **Vývojové týmy** — potřebují konzistentní workflow pravidla
- **Tech leads / Engineering managers** — definují a vynucují standardy
- **DevOps / Release managers** — řídí release proces, branch politiky
- **Jednotliví vývojáři** — chtějí rychlou zpětnou vazbu na compliance

---

## 3. Technická specifikace

### 3.1 Plugin metadata
- **Package:** `com.jakubjirak.gitworkflow`
- **Plugin ID:** `com.jakubjirak.gitworkflow`
- **Verze:** `2026.2.1`
- **Target:** IntelliJ Platform 2024.3+ (`sinceBuild=243`, `untilBuild=252.*`)
- **JVM:** 17
- **Kotlin:** 2.1.0
- **Závislosti:** `com.intellij.modules.platform`, `Git4Idea`
- **Extra lib:** SnakeYAML 2.3 (parsování `.git-workflow.yaml`)

---

## 4. Funkční požadavky

### FR-1: Workflow Presets
- **FR-1.1:** Vestavěné presety: Gitflow, trunk-based, GitHub Flow
- **FR-1.2:** Custom preset — plně konfigurovatelný
- **FR-1.3:** Preset definuje: branch naming rules, merge policies, commit message format
- **FR-1.4:** Quick-start: výběr presetu při prvním spuštění → generuje `.git-workflow.yaml`

#### FR-1.5: Gitflow preset (default rules)
```yaml
preset: gitflow
branches:
  main: { protected: true, merge_from: [release/*, hotfix/*] }
  develop: { protected: true, merge_from: [feature/*, bugfix/*, release/*] }
  patterns:
    - { pattern: "feature/*", base: develop }
    - { pattern: "bugfix/*", base: develop }
    - { pattern: "release/*", base: develop }
    - { pattern: "hotfix/*", base: main }
```

#### FR-1.6: Trunk-based preset (default rules)
```yaml
preset: trunk-based
branches:
  main: { protected: true, merge_from: [feature/*, fix/*] }
  patterns:
    - { pattern: "feature/*", base: main, max_lifetime: 2d }
    - { pattern: "fix/*", base: main, max_lifetime: 1d }
```

### FR-2: Branch Naming Conventions
- **FR-2.1:** Regex pattern validace pro názvy větví
- **FR-2.2:** Default patterny:
  - `feature/[A-Z]+-[0-9]+-.*` (vyžaduje JIRA key)
  - `bugfix/[A-Z]+-[0-9]+-.*`
  - `release/[0-9]+\.[0-9]+\.[0-9]+`
  - `hotfix/[A-Z]+-[0-9]+-.*`
- **FR-2.3:** Custom regex patterny v konfiguraci
- **FR-2.4:** Validace při checkout/branch creation v IDE
- **FR-2.5:** Suggestion: automatický návrh správného názvu při violation

### FR-3: Branch Creation Policy
- **FR-3.1:** Vynucení vytvoření větve ze správné base branch
- **FR-3.2:** Kontrola: `feature/*` musí vycházet z `develop` (Gitflow)
- **FR-3.3:** Kontrola: `hotfix/*` musí vycházet z `main`
- **FR-3.4:** Warning/error při porušení politiky
- **FR-3.5:** Konfigurovatelné base branch pravidla

### FR-4: Merge Policy Enforcement
- **FR-4.1:** Blokování přímých pushů na chráněné větve (main, develop)
- **FR-4.2:** Vyžadování pull requestu pro merge do chráněných větví
- **FR-4.3:** Validace merge direction (feature → develop, nikdy feature → main)
- **FR-4.4:** Warning před merge do nesprávné cílové větve
- **FR-4.5:** Konfigurovatelné chráněné větve a merge pravidla

### FR-5: Commit Message Validation
- **FR-5.1:** Conventional Commits format:
  ```
  <type>(<scope>): <description>

  [optional body]

  [optional footer(s)]
  ```
- **FR-5.2:** Povolené typy: `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `build`, `ci`, `chore`, `revert`
- **FR-5.3:** JIRA key validace: `[A-Z]+-[0-9]+` v commit message (konfigurovatelné)
- **FR-5.4:** Max délka subject line (default 72 znaků)
- **FR-5.5:** Max délka body line (default 100 znaků)
- **FR-5.6:** Real-time validace v commit dialogu
- **FR-5.7:** Auto-complete: JIRA key ze jména větve do commit message

### FR-6: Required Reviewers per Path
- **FR-6.1:** CODEOWNERS-like pravidla:
  ```yaml
  reviewers:
    - path: "src/main/kotlin/com/*/security/**"
      required: ["@security-team"]
      min_approvals: 2
    - path: "*.gradle.kts"
      required: ["@build-team"]
      min_approvals: 1
    - path: "src/main/resources/META-INF/**"
      required: ["@platform-team"]
  ```
- **FR-6.2:** Glob pattern matching na změněné soubory
- **FR-6.3:** Notifikace: kdo musí udělat review před merge
- **FR-6.4:** Integrace s Git diff — detekce změněných cest

### FR-7: Pre-push Validation
- **FR-7.1:** Validace před push:
  - [ ] Branch name compliance
  - [ ] Commit message format (všechny commity v push)
  - [ ] Merge policy (push na chráněnou větev)
- **FR-7.2:** Blokování push při critical violations
- **FR-7.3:** Warning s možností override při non-critical violations
- **FR-7.4:** Integrace s Git4Idea pre-push hook extension point

### FR-8: Team Config File
- **FR-8.1:** `.git-workflow.yaml` v project root
- **FR-8.2:** Sdílení přes Git (verzovaný konfigurační soubor)
- **FR-8.3:** Schema validace konfiguračního souboru
- **FR-8.4:** File watcher — automatický reload při změně
- **FR-8.5:** Merge s project-level settings (override hierarchy)

#### FR-8.6: Ukázkový `.git-workflow.yaml`
```yaml
version: 1
preset: gitflow

branch_naming:
  patterns:
    - name: feature
      regex: "feature/[A-Z]+-[0-9]+-[a-z0-9-]+"
      base: develop
    - name: bugfix
      regex: "bugfix/[A-Z]+-[0-9]+-[a-z0-9-]+"
      base: develop
    - name: release
      regex: "release/[0-9]+\\.[0-9]+\\.[0-9]+"
      base: develop
    - name: hotfix
      regex: "hotfix/[A-Z]+-[0-9]+-[a-z0-9-]+"
      base: main

protected_branches:
  - name: main
    allow_push: false
    merge_from: [release/*, hotfix/*]
    require_pr: true
  - name: develop
    allow_push: false
    merge_from: [feature/*, bugfix/*, release/*]
    require_pr: true

commit_message:
  format: conventional
  types: [feat, fix, docs, style, refactor, perf, test, build, ci, chore, revert]
  require_jira_key: true
  jira_pattern: "[A-Z]+-[0-9]+"
  max_subject_length: 72
  max_body_line_length: 100

reviewers:
  - path: "src/**/security/**"
    required: ["@security-team"]
    min_approvals: 2
  - path: "*.gradle.kts"
    required: ["@build-team"]
    min_approvals: 1
```

### FR-9: Notification on Violations
- **FR-9.1:** Warning notifikace při non-critical violations
- **FR-9.2:** Error notifikace při critical violations (blokující)
- **FR-9.3:** Fix suggestions: návrh opravy (přejmenování větve, úprava commit message)
- **FR-9.4:** Quick-fix akce: 1-click oprava kde možné
- **FR-9.5:** Konfigurovatelná severita: warning vs. error per pravidlo
- **FR-9.6:** Notifikační skupina: `Git Workflow Enforcer` (seskupení v Event Log)

### FR-10: Status Bar Indicator
- **FR-10.1:** Widget ve status baru: `✓ Workflow Compliant` / `✗ 3 Violations`
- **FR-10.2:** Barevné kódování:
  - 🟢 Vše v pořádku (compliant)
  - 🟡 Non-critical violations (warnings)
  - 🔴 Critical violations (errors)
- **FR-10.3:** Klik na widget → otevření Violations tool window
- **FR-10.4:** Tooltip: souhrn aktuálních violations
- **FR-10.5:** Real-time update při změně stavu

---

## 5. Nefunkční požadavky

### NFR-1: Výkon
- File watcher na `.git-workflow.yaml` — minimální overhead
- Validace branch name < 10ms
- Commit message validace < 50ms
- Status bar update < 100ms
- Pre-push validace < 500ms (všechny kontroly)

### NFR-2: Kompatibilita
- IntelliJ IDEA 2024.3+, všechny JetBrains IDE s Git podporou
- macOS, Linux, Windows
- Git4Idea plugin (bundled ve všech JetBrains IDE)

### NFR-3: Bezpečnost
- Žádné odesílání dat mimo IDE
- Config soubor je lokální / verzovaný v Gitu
- Žádné credentials v konfiguraci

---

## 6. Datový model

### 6.1 WorkflowRule
```
WorkflowRule {
  name: String
  type: RuleType (BRANCH_NAMING, COMMIT_FORMAT, MERGE_POLICY, REVIEWER_REQUIREMENT)
  severity: Severity (WARNING, ERROR)
  enabled: Boolean
  config: Map<String, Any>
}
```

### 6.2 WorkflowPreset
```
WorkflowPreset {
  name: String (GITFLOW, TRUNK_BASED, GITHUB_FLOW, CUSTOM)
  branchPatterns: List<BranchPattern>
  protectedBranches: List<ProtectedBranch>
  commitMessageConfig: CommitMessageConfig
  reviewerRules: List<ReviewerRule>
}
```

### 6.3 BranchPattern
```
BranchPattern {
  name: String
  regex: String
  baseBranch: String
  maxLifetime: Duration?
}
```

### 6.4 ProtectedBranch
```
ProtectedBranch {
  name: String
  allowPush: Boolean
  mergeFrom: List<String>
  requirePr: Boolean
}
```

### 6.5 CommitMessageConfig
```
CommitMessageConfig {
  format: CommitFormat (CONVENTIONAL, CUSTOM, NONE)
  allowedTypes: List<String>
  requireJiraKey: Boolean
  jiraPattern: String
  maxSubjectLength: Int
  maxBodyLineLength: Int
}
```

### 6.6 ReviewerRule
```
ReviewerRule {
  pathPattern: String
  required: List<String>
  minApprovals: Int
}
```

### 6.7 Violation
```
Violation {
  rule: WorkflowRule
  severity: Severity
  message: String
  suggestion: String?
  quickFix: QuickFixAction?
  timestamp: Instant
}
```

### 6.8 PolicyConfig
```
PolicyConfig {
  version: Int
  preset: WorkflowPreset
  branchNaming: List<BranchPattern>
  protectedBranches: List<ProtectedBranch>
  commitMessage: CommitMessageConfig
  reviewers: List<ReviewerRule>
}
```

---

## 7. Freemium model

### Free tier
- Status bar widget (compliance status)
- Základní branch naming validace (3 default patterny)
- Commit message validace (max délka)
- Notification na violations (basic)

### Enterprise tier ($15/dev/měsíc)
- Workflow presety (Gitflow, trunk-based, GitHub Flow)
- Custom regex patterny (neomezené)
- Merge policy enforcement
- Required reviewers per path
- Pre-push hook integrace
- Audit log (violation history, compliance report)
- Team config file (`.git-workflow.yaml`) plná podpora
- Custom workflow presets
- Compliance dashboard
