<h1 align="center">Mizizi</h1>
<p align="center">
  <em>Keeping Kenyan languages usable for the next generation.</em>
  <br>
  <br>
  <a href="#current-status">Current Status</a> ·
  <a href="#repository-structure">Repository Structure</a> ·
  <a href="#branching-rules">Branching Rules</a> ·
  <a href="#ci--checks">CI & Checks</a> ·
  <a href="#how-to-run-lab-1">How to Run</a> ·
  <a href="#team">Team</a> ·
  <a href="#concept-note">Concept Note</a>
  <br>
  <img src="https://img.shields.io/badge/version-1.0.0-16a34a" alt="Version 1.0.0">
  <img src="https://img.shields.io/badge/license-UNLICENSED-16a34a" alt="UNLICENSED">
  <img src="https://img.shields.io/badge/platform-JVM-16a34a" alt="JVM">
</p>

---

Mizizi is a language-practice application that lets users actively practise
translating words, phrases and proverbs in their mother tongue — and get
meaning-based feedback on their attempts, instead of a blind literal translation.

## Current status

- **Lab 1 (this repo phase):** a console-based Kotlin prototype of the core
  feature — a proverb practice engine with meaning-based grading, a session
  score, an archive of attempts, and a daily proverb.

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
Push at least once a day — frequent small commits make bugs easy to trace
and let the whole team follow updates as they happen.

## CI & Checks

Every push and pull request to `main` / `develop` runs GitHub Actions:

- **Compile** — `Main.kt` is built with the official Kotlin compiler.
- **Smoke test** — the app runs through the archive, daily-proverb and exit
  flows and the output is verified.

`main` is protected: code lands only through a pull request with at least one
approving review and a green build. No direct pushes, no force-pushes.
See `.github/workflows/build.yml` and `.github/PULL_REQUEST_TEMPLATE.md`.

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
