# Git Workflow Enforcer — Implementační plán

**Verze:** 2026.2.1
**Status:** Připraveno k implementaci

---

## Fáze 0: Plugin Ikona & Branding

- [ ] Navrhnout plugin ikonu (SVG + PNG)
  - [ ] Koncept: Git branch icon se štítem / checkmark
  - [ ] Hlavní ikona: 40×40 SVG pro Marketplace listing
  - [ ] Toolbar ikona: 16×16 + 13×13 SVG (light + dark varianta)
  - [ ] Tool window ikona: 13×13 SVG
  - [ ] `src/main/resources/META-INF/pluginIcon.svg` (40×40, light theme)
  - [ ] `src/main/resources/META-INF/pluginIcon_dark.svg` (40×40, dark theme)
  - [ ] Action ikony pro toolbar/menu (16×16 SVG, light + dark)
- [ ] Barevná paleta (zelená = compliant, červená = violation, žlutá = warning)
- [ ] Marketplace banner obrázek (1280×800)
- [ ] Screenshoty pro Marketplace listing (min 3, 1280×800 PNG)

## Fáze 1: Projekt scaffolding

- [ ] Inicializovat IntelliJ Platform Plugin projekt (Gradle Kotlin DSL)
  - [ ] `build.gradle.kts` — IntelliJ Platform 2024.3+, JVM 17, Kotlin 2.1.0
  - [ ] `settings.gradle.kts`, `gradle.properties` (verze `2026.2.1`)
  - [ ] Gradle wrapper (8.x)
  - [ ] Dependency: SnakeYAML 2.3 (config parsing)
- [ ] `src/main/resources/META-INF/plugin.xml`
  - [ ] Plugin ID: `com.jakubjirak.gitworkflow`
  - [ ] Závislosti: `com.intellij.modules.platform`, `Git4Idea`
- [ ] Balíčková struktura: `com.jakubjirak.gitworkflow.*`
  - [ ] `.model` — datové třídy
  - [ ] `.config` — config loader, preset manager
  - [ ] `.validators` — branch name, commit message, merge policy
  - [ ] `.reviewers` — reviewer requirements engine
  - [ ] `.hooks` — pre-push validation
  - [ ] `.ui` — status bar, tool window, settings
  - [ ] `.licensing` — freemium gate, license checker
- [ ] `src/main/resources/messages/GitWorkflowBundle.properties`
- [ ] `.gitignore` (build/, .idea/, .gradle/, .kotlin/, out/)
- [ ] `gradle/libs.versions.toml` — version catalog
- [ ] `LICENSE` (Apache-2.0)
- [ ] `.editorconfig` (Kotlin style conventions)
- [ ] Detekt konfigurace (`detekt.yml`)
- [ ] GitHub Actions CI workflow (`.github/workflows/build.yml`)
- [ ] Ověřit `./gradlew build`

## Fáze 2: Datový model

- [ ] `WorkflowRule.kt`
  - [ ] `name: String`, `type: RuleType`, `severity: Severity`, `enabled: Boolean`
  - [ ] `config: Map<String, Any>`
- [ ] `WorkflowPreset.kt`
  - [ ] `name: String` (GITFLOW, TRUNK_BASED, GITHUB_FLOW, CUSTOM)
  - [ ] `branchPatterns: List<BranchPattern>`
  - [ ] `protectedBranches: List<ProtectedBranch>`
  - [ ] `commitMessageConfig: CommitMessageConfig`
  - [ ] `reviewerRules: List<ReviewerRule>`
- [ ] `BranchPattern.kt`
  - [ ] `name: String`, `regex: String`, `baseBranch: String`, `maxLifetime: Duration?`
- [ ] `ProtectedBranch.kt`
  - [ ] `name: String`, `allowPush: Boolean`, `mergeFrom: List<String>`, `requirePr: Boolean`
- [ ] `CommitMessageConfig.kt`
  - [ ] `format: CommitFormat`, `allowedTypes: List<String>`, `requireJiraKey: Boolean`
  - [ ] `jiraPattern: String`, `maxSubjectLength: Int`, `maxBodyLineLength: Int`
- [ ] `ReviewerRule.kt`
  - [ ] `pathPattern: String`, `required: List<String>`, `minApprovals: Int`
- [ ] `Violation.kt`
  - [ ] `rule: WorkflowRule`, `severity: Severity`, `message: String`
  - [ ] `suggestion: String?`, `quickFix: QuickFixAction?`, `timestamp: Instant`
- [ ] `PolicyConfig.kt`
  - [ ] `version: Int`, `preset: WorkflowPreset`
  - [ ] `branchNaming: List<BranchPattern>`, `protectedBranches: List<ProtectedBranch>`
  - [ ] `commitMessage: CommitMessageConfig`, `reviewers: List<ReviewerRule>`
- [ ] Unit testy pro datový model

## Fáze 3: Config Loader (.git-workflow.yaml)

- [ ] `WorkflowConfigLoader.kt`
  - [ ] `fun load(configFile: File): PolicyConfig`
  - [ ] Parsování `.git-workflow.yaml` z project root (SnakeYAML 2.3)
  - [ ] Schema validace konfiguračního souboru
  - [ ] Default hodnoty pro chybějící pole
  - [ ] Error handling: malformed YAML, neznámé klíče
- [ ] `WorkflowPresetManager.kt`
  - [ ] `fun getPreset(name: String): WorkflowPreset`
  - [ ] Vestavěné presety: Gitflow, trunk-based, GitHub Flow
  - [ ] Merge: preset defaults + user overrides
- [ ] `WorkflowConfigService.kt` — project-level service
  - [ ] Singleton per project: aktuální PolicyConfig
  - [ ] File watcher na `.git-workflow.yaml` — automatický reload
  - [ ] Registrace jako `ProjectService` v `plugin.xml`
- [ ] `WorkflowConfigGenerator.kt`
  - [ ] `fun generate(preset: WorkflowPreset): String` — generuje YAML
  - [ ] Quick-start: výběr presetu → zápis `.git-workflow.yaml`
- [ ] Unit testy (fixture YAML soubory — gitflow, trunk-based, custom, malformed)

## Fáze 4: Branch Name Validator

- [ ] `BranchNameValidator.kt`
  - [ ] `fun validate(branchName: String, config: PolicyConfig): List<Violation>`
  - [ ] Regex pattern matching proti konfigurovaným pravidlům
  - [ ] Suggestion: návrh správného názvu při violation
- [ ] `BranchCreationPolicyChecker.kt`
  - [ ] `fun validateBaseBranch(newBranch: String, baseBranch: String, config: PolicyConfig): List<Violation>`
  - [ ] Kontrola: feature/* z develop, hotfix/* z main (dle presetu)
- [ ] `BranchNameInspection.kt`
  - [ ] Integrace s Git4Idea — listener na branch checkout/creation
  - [ ] Real-time validace, notification při porušení
- [ ] Unit testy (valid/invalid branch names, base branch validation)

## Fáze 5: Commit Message Validator

- [ ] `CommitMessageValidator.kt`
  - [ ] `fun validate(message: String, config: CommitMessageConfig): List<Violation>`
  - [ ] Conventional Commits format parsing
  - [ ] JIRA key validace (regex z config)
  - [ ] Subject line length check
  - [ ] Body line length check
  - [ ] Type validace (feat, fix, docs, ...)
- [ ] `CommitMessageInspection.kt`
  - [ ] Integrace s VCS commit dialog
  - [ ] Real-time validace při psaní commit message
  - [ ] Highlighting chyb v commit message textu
- [ ] `JiraKeyAutoCompleter.kt`
  - [ ] Extrakce JIRA key z branch name
  - [ ] Auto-insert do commit message
- [ ] Unit testy (valid/invalid commit messages, JIRA key extraction)

## Fáze 6: Merge Policy Enforcer

- [ ] `MergePolicyEnforcer.kt`
  - [ ] `fun validateMerge(sourceBranch: String, targetBranch: String, config: PolicyConfig): List<Violation>`
  - [ ] Kontrola protected branches — blokování přímých pushů
  - [ ] Merge direction validace (feature → develop, ne feature → main)
  - [ ] Require PR check
- [ ] `MergePolicyListener.kt`
  - [ ] Listener na Git merge/push akce v IDE
  - [ ] Warning/error dialog před zakázanou akcí
- [ ] Unit testy (merge direction, protected branches, PR requirement)

## Fáze 7: Reviewer Requirements

- [ ] `ReviewerRequirementsEngine.kt`
  - [ ] `fun getRequiredReviewers(changedFiles: List<String>, config: PolicyConfig): List<ReviewerRequirement>`
  - [ ] Glob pattern matching na změněné soubory
  - [ ] Agregace reviewerů ze všech matchujících pravidel
- [ ] `ReviewerNotificationService.kt`
  - [ ] Notification: seznam povinných reviewerů pro aktuální změny
  - [ ] Integrace s Git diff — detekce změněných cest
- [ ] Unit testy (path matching, reviewer aggregation)

## Fáze 8: Pre-push Hook Integration

- [ ] `PrePushValidator.kt`
  - [ ] Integrace s Git4Idea `GitPushHandler` / pre-push extension point
  - [ ] Spuštění všech validací před push:
    - [ ] Branch name compliance
    - [ ] Commit message format (všechny commity v push)
    - [ ] Merge policy (push na chráněnou větev)
  - [ ] Blokování push při critical violations
  - [ ] Warning dialog s možností override při non-critical
- [ ] `PrePushResultDialog.kt`
  - [ ] Souhrn violations před push
  - [ ] „Fix & Retry" / „Push Anyway" / „Cancel" akce
- [ ] Unit testy (pre-push validation scenarios)

## Fáze 9: Status Bar Widget

- [ ] `WorkflowStatusBarWidget.kt` implements `StatusBarWidget.MultipleTextValuesPresentation`
  - [ ] Registrace: `StatusBarWidgetFactory` v `plugin.xml`
  - [ ] Zobrazení: `✓ Compliant` / `✗ 3 Violations`
  - [ ] Barva: 🟢 compliant, 🟡 warnings, 🔴 errors
  - [ ] Update při změně stavu (branch switch, commit, config change)
- [ ] Click action → otevření Violations tool window
- [ ] Tooltip: souhrn aktuálních violations

## Fáze 10: Tool Window (violations, compliance)

- [ ] `ViolationsToolWindow.kt`
  - [ ] Registrace: `ToolWindowFactory` v `plugin.xml`
  - [ ] Seznam aktuálních violations (tree view)
  - [ ] Grouping: dle typu (branch naming, commit, merge, reviewer)
  - [ ] Severity ikony: ⚠️ warning, ❌ error
  - [ ] Quick-fix akce per violation
- [ ] `ComplianceDashboard.kt` (Enterprise)
  - [ ] Přehled compliance metriky
  - [ ] Violation history (timeline)
  - [ ] Team compliance rate

## Fáze 11: Settings

- [ ] `WorkflowSettingsConfigurable.kt` — Settings → Tools → Git Workflow Enforcer
  - [ ] Workflow preset výběr (dropdown)
  - [ ] Branch naming rules tabulka (pattern, base branch, regex)
  - [ ] Protected branches tabulka (name, allow push, merge from)
  - [ ] Commit message konfigurace (format, types, JIRA, max length)
  - [ ] Reviewer rules tabulka (path, required, min approvals)
  - [ ] Enable/disable toggle per rule type
- [ ] `WorkflowSettings.kt` implements `PersistentStateComponent`
  - [ ] Projekt-level settings (override `.git-workflow.yaml`)
  - [ ] Global enable/disable
- [ ] Quick-start wizard: výběr presetu → generace `.git-workflow.yaml`
- [ ] Unit testy

## Fáze 12: Freemium & Licensing Infrastructure

- [ ] Integrace JetBrains Marketplace Licensing API
  - [ ] `LicenseChecker.kt` — ověření licence přes Marketplace API
  - [ ] `LicenseStatus.kt` — enum: FREE, TRIAL, ENTERPRISE, EXPIRED
  - [ ] `FreemiumGate.kt` — centrální guard pro paid features
    - [ ] `fun isPaidFeatureAvailable(featureId: String): Boolean`
    - [ ] Graceful degradation — paid UI elementy skryté/disabled ve free tieru
  - [ ] Trial period: 30 dní plný přístup
- [ ] `plugin.xml` — `<product-descriptor>` element pro Marketplace licensing
  - [ ] `code` atribut (unique product code)
  - [ ] `release-date`, `release-version`
- [ ] Nag screen / upgrade prompt
  - [ ] Non-intrusive balloon notification při pokusu o Enterprise feature
  - [ ] „Upgrade to Enterprise" action → Marketplace stránka
  - [ ] Max 1× denně (ne spam)
- [ ] Feature flags
  - [ ] `FeatureRegistry.kt` — definice free vs. enterprise features
  - [ ] Každý enterprise feature má guard check
- [ ] Unit testy pro licensing logic (mock Marketplace API)

## Fáze 13: Production-Grade Quality

- [ ] Error Handling & Resilience
  - [ ] Globální exception handler (`ErrorReportSubmitter` implementace)
  - [ ] Graceful degradation — plugin nesmí crashnout IDE
  - [ ] Try-catch na všech EP entry pointech
  - [ ] Timeout na všech I/O operacích
- [ ] Logging
  - [ ] `Logger` instance v každé třídě (IntelliJ `Logger.getInstance()`)
  - [ ] Structured logging: DEBUG pro vývoj, INFO pro operace, WARN/ERROR pro problémy
  - [ ] Žádné citlivé údaje v logech
- [ ] Telemetrie (opt-in, anonymní)
  - [ ] `PluginUsageTracker.kt` — FUS (Feature Usage Statistics) integrace
  - [ ] Metriky: violations prevented, compliance rate, preset usage
  - [ ] Registrace FUS group v `plugin.xml`
- [ ] Performance
  - [ ] Profiling na velkých projektech (10k+ souborů)
  - [ ] Background thread pro všechny I/O operace (nesmí blokovat EDT)
  - [ ] Read/write action správně (ReadAction, WriteAction, smart mode check)
  - [ ] Disposal pattern: `Disposable` pro všechny listeners a services
  - [ ] Memory leak check (WeakReference kde potřeba)
- [ ] Kódová kvalita
  - [ ] Kotlin coding conventions (official JetBrains style)
  - [ ] Detekt nebo ktlint pro statickou analýzu
  - [ ] Žádné deprecated API (IntelliJ Platform compatibility)
  - [ ] `@ApiStatus.Experimental` / `@ApiStatus.Internal` anotace
- [ ] Dokumentace
  - [ ] KDoc pro všechny public API
  - [ ] README.md: instalace, konfigurace, screenshoty, FAQ
  - [ ] CHANGELOG.md (Keep a Changelog format)

## Fáze 14: Tests

- [ ] Unit testy: ≥80 % coverage core logiky
  - [ ] `BranchNameValidatorTest.kt` — valid/invalid branch names, regex patterns
  - [ ] `CommitMessageValidatorTest.kt` — conventional commits, JIRA key, length
  - [ ] `MergePolicyEnforcerTest.kt` — protected branches, merge direction
  - [ ] `ReviewerRequirementsEngineTest.kt` — path matching, aggregation
  - [ ] `WorkflowConfigLoaderTest.kt` — YAML parsing, schema validation
  - [ ] `WorkflowPresetManagerTest.kt` — preset loading, merge overrides
  - [ ] `PrePushValidatorTest.kt` — pre-push validation scenarios
- [ ] Integrační testy: `BasePlatformTestCase` pro PSI, inspections, EP
  - [ ] `BranchNameInspectionTest.kt`
  - [ ] `CommitMessageInspectionTest.kt`
  - [ ] `WorkflowConfigServiceTest.kt`
- [ ] UI testy: `HeavyPlatformTestCase` pro tool windows, dialogy
  - [ ] `ViolationsToolWindowTest.kt`
  - [ ] `WorkflowStatusBarWidgetTest.kt`
- [ ] Performance testy: benchmark na velkých Git repozitářích
- [ ] CI/CD Pipeline
  - [ ] GitHub Actions workflow:
    - [ ] `./gradlew check` — kompilace + testy
    - [ ] `./gradlew verifyPlugin` — plugin strukturální validace
    - [ ] `./gradlew verifyPluginProjectConfiguration` — metadata validace
    - [ ] `./gradlew runPluginVerifier` — binární kompatibilita s target IDE verzemi
    - [ ] Qodana scan (JetBrains code quality)
  - [ ] Release workflow:
    - [ ] Tag-based release (push tag → build → publish)
    - [ ] `./gradlew publishPlugin` — automatický upload na Marketplace
    - [ ] Changelog generace z git commitů
  - [ ] Dependabot / Renovate pro dependency updates

## Fáze 15: Release

- [ ] Ukázkový `.git-workflow.yaml` (Gitflow, trunk-based)
- [ ] README.md s screenshoty a instalačními instrukcemi
- [ ] `./gradlew verifyPlugin`, `./gradlew buildPlugin`
- [ ] Manuální testování s reálným Git repozitářem
- [ ] Cross-IDE test (IntelliJ IDEA, PyCharm, WebStorm, GoLand)
- [ ] Cross-OS test (macOS, Linux, Windows)
- [ ] Marketplace listing:
  - [ ] Popis, screenshoty, ikona
  - [ ] Kategorie: VCS Integration, Team Tools
  - [ ] Tagy: git, workflow, branch, commit, policy, enforcement
- [ ] Git tag `v2026.2.1`
- [ ] Marketing launch: Reddit, Hacker News, JetBrains blog
- [ ] Sbírání zpětné vazby od early adopters

---

## Budoucí fáze (Post-release)

- [ ] Integrace s JIRA API — automatické stahování issue keys
- [ ] GitHub / GitLab PR integrace — reviewer status z platformy
- [ ] Multi-repo policy — sdílená konfigurace across repozitáře
- [ ] Policy-as-code — import/export politik
- [ ] Compliance reporting — PDF/HTML reporty pro management
- [ ] Slack / Teams notifikace — violation alerts do team channels
- [ ] Custom workflow presets marketplace — community-contributed presets
- [ ] AI-powered commit message suggestions
