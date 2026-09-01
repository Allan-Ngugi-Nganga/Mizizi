# Mizizi 🌱

**Keeping Kenyan languages usable for the next generation.**

Mizizi is a language-practice application that lets users actively practise
translating words, phrases and proverbs in their mother tongue — and get
meaning-based feedback on their attempts, instead of a blind literal translation.

## Current status

- **Lab 1 (this repo phase):** a console-based Kotlin prototype of the core
  feature — a proverb practice engine with meaning-based grading, a session
  score, an archive of attempts, and a daily proverb.
- **Endgame (later labs):** a native Android app (Kotlin + Jetpack Compose)
  with an LLM-backed feedback engine, daily proverb notifications and a
  personal proverb archive.

## Repository structure

```
Mizizi/
├── Main.kt              # Lab 1 console prototype (single-file submission)
├── README.md            # This file — overview + repo rules
├── CONTRIBUTING.md      # Branch, commit and PR conventions for the team
├── docs/
│   └── concept-note.md  # Concept note (mirror of the Google Doc)
└── .gitignore
```

## Branching rules

| Branch                | Purpose                                                      | Who pushes |
|-----------------------|--------------------------------------------------------------|------------|
| `main`                | Demo/submission-ready code. Protected — no direct pushes.    | No one     |
| `develop`             | Integration branch. Features merge here via PR.              | No one     |
| `feature/<name>`      | New work, e.g. `feature/proverb-practice`                    | Everyone   |
| `hotfix/<name>`       | Urgent fixes straight to `main` (rare)                       | Everyone   |

## Commit rules (Conventional Commits)

- `feat:` — new functionality
- `fix:` — bug fix
- `docs:` — documentation only
- `refactor:` — code change with no behaviour change
- `chore:` — tooling, config, housekeeping

Keep commits **small and frequent** (one commit = one logical change).
Push at least once a day — it is your backup AND your teamwork evidence.

## How to run (Lab 1)

Command line (needs the Kotlin compiler):

```bash
kotlinc Main.kt -include-runtime -d mizizi.jar
java -jar mizizi.jar
```

Or simply open the project in IntelliJ IDEA and run `main()`.

## Team

- Allan Ngugi — 191250
- Ziki Mtula — 192992
- Michael Asingwire — 192483
- Kelly Wambui — 190500
- Daniella Thande — 152581
- Evelyn Mwangi — 190051
- Daniel Karanja — 180761

## Concept note

See [`docs/concept-note.md`](docs/concept-note.md). The Google Docs version
is linked at the top of `Main.kt` (required by the assignment).
