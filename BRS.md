# Git Workflow Enforcer — Business Requirements Specification (BRS)

**Verze:** 2026.2.1
**Autor:** Jakub Jirák
**Datum:** 12. února 2026
**Model:** Freemium JetBrains Plugin

---

## 1. Executive Summary

Git Workflow Enforcer je IDE-native plugin pro vynucování týmových Git workflow pravidel v reálném čase. Řeší problém nekonzistentního workflow v týmech — špatné pojmenování větví, nekvalitní commit zprávy, přímé pushe na chráněné větve, chybějící revieweři. Enterprise-focused monetizace s $15/dev/měsíc pro plnou funkčnost.

---

## 2. Obchodní cíle

### 2.1 Primární role
**Enterprise productivity tool** — IDE-native Git workflow standardizace bez server-side závislosti. Okamžitá zpětná vazba vývojářům, vynucování týmových pravidel, snížení code review friction.

### 2.2 KPI
| Metrika | Cíl (6 měsíců) | Cíl (12 měsíců) |
|---------|:---:|:---:|
| Celkové instalace | 5 000 | 20 000 |
| MAU | 2 500 | 10 000 |
| Enterprise konverze | 5 % | 10 % |
| MRR | $5 000 | $25 000 |
| Violations prevented / měsíc | 50 000 | 500 000 |
| Team compliance rate | 75 % | 90 % |
| Marketplace rating | ≥ 4.3 | ≥ 4.5 |
| Churn rate | < 10 % | < 5 % |

---

## 3. Cílový trh

### 3.1 Primární segment
- **Vývojové týmy (5-50 vývojářů)** používající JetBrains IDE s Git
- Týmy přecházející na strukturovaný workflow (Gitflow, trunk-based)
- Organizace s compliance požadavky na Git historii

### 3.2 Sekundární segment
- **Enterprise (50+ vývojářů)** — centralizované policy management
- **Open source projekty** — CONTRIBUTING.md enforcement
- **Konzultanti / Tech leads** — standardizace across projekty

### 3.3 Velikost trhu
- JetBrains IDE uživatelé: ~10M+
- Git uživatelé v JetBrains: ~8M+
- Týmy s workflow problémy: ~60 % (odhadem ~4.8M)

---

## 4. Konkurenční prostředí

### 4.1 Existující řešení
| Řešení | Typ | Nevýhody |
|--------|-----|----------|
| Git hooks (client-side) | `.git/hooks/` scripty | Těžko sdílitelné, per-repo setup, žádná IDE integrace |
| Husky / lint-staged | npm package | Jen JS ekosystém, CLI-only, žádná IDE zpětná vazba |
| GitHub Branch Protection | Server-side | Jen GitHub, žádná IDE integrace, feedback až po push |
| GitLab Push Rules | Server-side | Jen GitLab, žádná IDE integrace, feedback až po push |
| Bitbucket Branch Permissions | Server-side | Jen Bitbucket, omezené pravidla |
| commitlint | CLI tool | Jen commit messages, žádná IDE integrace |
| Conventional Commits plugin (VS Code) | IDE plugin | Jen VS Code, jen commit messages |

### 4.2 Konkurenční výhoda
1. **IDE-native** — okamžitá zpětná vazba v reálném čase, ne až po push
2. **Server-agnostic** — funguje s GitHub, GitLab, Bitbucket, self-hosted
3. **Sdílená konfigurace** — `.git-workflow.yaml` verzovaný v Gitu
4. **Komplexní** — branch naming + commit format + merge policy + reviewers v jednom nástroji
5. **Workflow presets** — Gitflow/trunk-based/GitHub Flow out-of-the-box
6. **Žádná závislost na npm/node** — nativní IDE plugin

---

## 5. Revenue model

| Tier | Cena | Funkce |
|------|------|--------|
| **Free** | $0 | Základní branch naming (3 patterny), commit message max délka, status bar widget |
| **Enterprise** | $15/dev/měsíc nebo $150/dev/rok | Plná policy konfigurace, merge enforcement, reviewer requirements, pre-push hook, audit, team config |

### 5.1 Revenue projekce
| Období | Odhad Enterprise uživatelů | MRR |
|--------|:---:|:---:|
| 6 měsíců | 350 | $5 250 |
| 12 měsíců | 1 500 | $22 500 |
| 18 měsíců | 3 000 | $45 000 |

### 5.2 Cenová strategie
- Enterprise pricing: $15/dev/měsíc reflektuje B2B hodnotu (prevence chyb, compliance, produktivita)
- Volume discount: 10+ devs → 20 % sleva, 50+ → 30 %
- Annual billing: 2 měsíce zdarma ($150/dev/rok místo $180)

---

## 6. Go-to-Market

### 6.1 Launch strategie
1. **Content marketing** — blog posts: „Why Your Team Needs Git Workflow Standards"
2. **Developer communities** — Reddit r/git, r/IntelliJIDEA, r/programming, Hacker News
3. **Conference talks** — „IDE-Native Git Workflow Enforcement" na JetBrains events
4. **Free tier virální efekt** — `.git-workflow.yaml` sdílený v repo → nový team member nainstaluje plugin

### 6.2 Growth loops
1. **Config file virality** — `.git-workflow.yaml` v repo → team members install plugin
2. **Violation notifications** — „Install Git Workflow Enforcer to see policy compliance"
3. **Cross-team adoption** — tech lead standardizuje → celá organizace adoptuje

### 6.3 Partnerships
- JetBrains Marketplace featured listing
- Integrace s Atlassian JIRA (JIRA key validace)
- Dokumentace pro GitFlow / trunk-based adopci

---

## 7. Milníky

| Milestone | Popis | Časový rámec |
|-----------|-------|:---:|
| **M1: MVP** | Branch naming validace, commit message check, status bar widget | 4 týdny |
| **M2: Config** | `.git-workflow.yaml` loader, workflow presets, file watcher | 2 týdny |
| **M3: Merge Policy** | Protected branches, merge direction enforcement | 2 týdny |
| **M4: Reviewers** | Required reviewers per path, CODEOWNERS-like rules | 2 týdny |
| **M5: Pre-push** | Pre-push hook integrace, blokování violations | 2 týdny |
| **M6: Enterprise** | Audit log, compliance dashboard, team config | 3 týdny |
| **M7: Release** | Marketplace listing, dokumentace, marketing | 2 týdny |

---

## 8. Rizika

| Riziko | Pravděpodobnost | Dopad | Mitigace |
|--------|:---:|:---:|---------|
| Nízká adopce free tieru | Střední | Vysoký | Virální config file strategy, content marketing |
| Konkurence od JetBrains (built-in) | Nízká | Vysoký | Rychlý time-to-market, community building |
| Enterprise pricing příliš vysoká | Střední | Střední | A/B testing cen, freemium funkce rozšíření |
| Git4Idea API breaking changes | Střední | Střední | Abstrakční vrstva, testing na multiple IDE verzích |

---

## 9. Success Criteria

1. 20 000+ instalací do 12 měsíců
2. Enterprise konverzní rate ≥ 10 %
3. MRR $25 000 do 12 měsíců
4. 500 000+ violations prevented měsíčně
5. Marketplace rating ≥ 4.5
6. Team compliance rate ≥ 90 % u Enterprise zákazníků
