# Contribution plan

CAT 2 requires every member to make a meaningful, visible contribution, and the
lecturer may open the Contributors graph to check. That graph reads commit
author emails, so two rules matter:

1. Each member commits with **their own** name and email:
   `git config user.name "<Full Name>"` and `git config user.email "<strathmore email>"`.
2. That email must be **added and verified on their GitHub account**
   (Settings, Emails). A commit whose email is not linked to an account shows a
   grey avatar and never appears in Contributors.

Check your last commit before pushing:

```bash
git log -1 --format='%an <%ae>'
```

## Branch rules

- Branch from `develop`: `git checkout -b feature/<slice> develop`
- Commit in small pieces with Conventional Commit messages (`feat:`, `fix:`, `docs:`)
- Open a pull request into `develop`, ask another member to review, merge after the review
- Never push straight to `main` or `develop`

## Slices

| Member | Slice | Branch | What lands |
|---|---|---|---|
| Allan Ngugi (191250) | Lead: scaffold, data layer, navigation shell | `feature/android-app` | Gradle setup, Room entities/DAOs/database, repository, ViewModel, MainActivity and NavHost, Home screen |
| Ziki Mtula (192992) | Practice screen | `feature/practice-screen` | `fragment_practice.xml` + `PracticeFragment`, question counter, answer field, submit, session summary, nav action from Home |
| Michael Asingwire (192483) | Result screen | `feature/result-screen` | `fragment_result.xml` + `ResultFragment`, correct/close/incorrect feedback, matched keywords, meaning, next-question action |
| Kelly Wambui (190500) | Archive list | `feature/archive` | `fragment_archive.xml`, `ArchiveAdapter` (RecyclerView), empty state, swipe or button delete, clear archive |
| Daniella Thande (152581) | Proverb editor (CRUD) | `feature/proverb-editor` | `fragment_proverb_editor.xml` + `ProverbEditorFragment`, create and update, delete with confirmation, validation messages |
| Evelyn Mwangi (190051) | Daily proverb, API and WorkManager | `feature/daily-proverb` | `DailyProverbFragment`, MyMemory translation display with loading and error states, `DailyProverbWorker` plus notification channel |
| Daniel Karanja (180761) | Documentation and release | `docs/readme-and-release` | README architecture and data flow sections, screenshots, signed release, `v1.0` tag and GitHub Release with the APK |

Each slice adds its own destination to `res/navigation/nav_graph.xml` and its
own strings, so branches touch different files and merge cleanly.

## Definition of done for a slice

- `./gradlew assembleDebug testDebugUnitTest` passes locally
- The screen is reachable from Home and back navigation works
- Empty, loading and error states are visible where the screen can have them
- A reviewer has approved the pull request
