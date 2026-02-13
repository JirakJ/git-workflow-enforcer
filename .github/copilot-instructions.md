# Copilot Instructions — Git Workflow Enforcer

## Projekt
JetBrains IDE plugin — vynucuje týmová Git workflow pravidla: pojmenování větví, merge politiky, formát commit zpráv, povinní revieweři dle cest. Real-time IDE-native enforcement bez závislosti na serveru.

## Technický stack
- **Jazyk:** Kotlin (JVM 17+)
- **Build:** Gradle (Kotlin DSL) s `org.jetbrains.intellij.platform` plugin 2.x
- **Platform:** IntelliJ Platform SDK 2024.3+
- **Testování:** JUnit 5 + `BasePlatformTestCase`
- **Extra:** SnakeYAML 2.3 pro parsování `.git-workflow.yaml`

## Build a testy
```bash
./gradlew build
./gradlew test
./gradlew test --tests "*.BranchNameValidatorTest.testGitflowNaming"
./gradlew runIde
./gradlew verifyPlugin
```

## Architektura
- **Config:** `WorkflowConfigLoader` — parsuje `.git-workflow.yaml` z project root (SnakeYAML)
- **Model:** `WorkflowRule`, `WorkflowPreset`, `Violation`, `PolicyConfig`
- **Validators:**
  - `BranchNameValidator` — regex pattern matching pro pojmenování větví (feature/*, bugfix/*, release/*, hotfix/*)
  - `CommitMessageValidator` — conventional commits, JIRA key, max délka
  - `MergePolicyEnforcer` — blokování přímých pushů na main/develop, vyžadování PR
  - `ReviewerRequirements` — CODEOWNERS-like pravidla pro povinné reviewery dle cest
- **Hook:** `PrePushValidator` — validace před push (branch name, commit format)
- **UI:** `WorkflowStatusBarWidget` (StatusBarWidgetFactory), `ViolationsToolWindow`, `WorkflowSettingsConfigurable`
- **Presets:** `WorkflowPresetManager` — Gitflow, trunk-based, GitHub Flow, custom

## Konvence
- Balíčky: `com.jakubjirak.gitworkflow.*`
  - `.model` — datové třídy (WorkflowRule, Violation, PolicyConfig, WorkflowPreset)
  - `.config` — config loader, preset manager
  - `.validators` — branch name, commit message, merge policy
  - `.reviewers` — reviewer requirements engine
  - `.hooks` — pre-push validation
  - `.ui` — status bar widget, tool window, settings
  - `.licensing` — freemium gate, license checker
- Config soubor: `.git-workflow.yaml` v project root (sdílený přes Git)
- Texty v `messages/GitWorkflowBundle.properties`
- Verzování: `2026.2.X`
- Plugin ID: `com.jakubjirak.gitworkflow`
- Závislosti: `com.intellij.modules.platform`, `Git4Idea`

## Production-grade požadavky
- Všechny I/O operace na background threadech (`Task.Backgroundable`, coroutines)
- Read/Write actions správně (`ReadAction.run {}`, `WriteAction.run {}`)
- `Disposable` pattern pro listeners a services
- Exception handling: žádné unhandled exceptions, graceful degradation
- Logging: `Logger.getInstance(MyClass::class.java)` v každé třídě
- EDT nesmí být blokován — žádné síťové/git/file operace na EDT
- Smart mode check (`DumbService.isDumb()`) kde potřeba

## Freemium architektura
- `FreemiumGate` centrální guard: `FreemiumGate.isPaidFeatureAvailable("feature.id")`
- JetBrains Marketplace Licensing API přes `<product-descriptor>` v `plugin.xml`
- Paid UI elementy: skryté nebo disabled ve free tieru (ne crash/error)
- Trial: 30 dní plný přístup, pak downgrade na free tier
- Upgrade prompt: max 1× denně, non-intrusive balloon

## Freemium hranice
- **Free:** Základní pravidla pojmenování větví (regex), commit message validace (max délka), status bar widget
- **Enterprise ($15/dev/měsíc):** Plná policy konfigurace, merge policy enforcement, reviewer requirements, pre-push hook, audit log, team config sdílení, custom workflow presets

## CI/CD
- GitHub Actions: `./gradlew check`, `verifyPlugin`, `runPluginVerifier`
- Detekt pro statickou analýzu Kotlin kódu
- Tag-based release → `publishPlugin` na Marketplace
- Kotlin coding conventions (JetBrains official style)

## Ikona
- `src/main/resources/META-INF/pluginIcon.svg` (40×40, light)
- `src/main/resources/META-INF/pluginIcon_dark.svg` (40×40, dark)
- Action ikony: 16×16 + 13×13 SVG (light + dark varianty)
