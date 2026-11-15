# Slice patches handoff

Each file here is a complete slice of the Android app, prepared so the member
who owns it can review it, run it, and commit it under their own name. The
commit has to be theirs: Contributors reads commit author emails, and the
lecturer uses that graph plus the individual defence.

## Before your first slice

Set your identity once per clone:

```bash
git config user.name "Your Full Name"
git config user.email "your.name@strathmore.edu"
```

Then add that same email to GitHub (Settings, Emails, Add email, verify). A
commit whose email is not on your account shows a grey avatar and never appears
in Contributors.

## Applying your slice

From a clean `develop`:

```bash
git checkout develop
git pull
git checkout -b feature/<your-slice>
git apply docs/patches/<your-slice>.patch
./gradlew assembleDebug testDebugUnitTest     # JAVA_HOME must point at a JDK 17 or newer
```

Run the app on an emulator, walk the screen you own, fix anything that feels
wrong, then:

```bash
git add -A
git commit -m "feat: add the <your-slice> screen"
git push -u origin feature/<your-slice>
```

Open the pull request into `develop`, ask another member to review, merge after
the review. Check the commit author before pushing:

```bash
git log -1 --format='%an <%ae>'
```

## Available patches

| File | Slice | Owner |
|---|---|---|
| `02-practice-screen.patch` | Practice screen and the nav action from Home | Ziki Mtula |
| `03-result-screen.patch` | Result screen with Safe Args, machine translation card, and the "See full feedback" button on the practice card | Michael Asingwire |
| `04-archive.patch` | Archive screen: RecyclerView with diffing adapter, empty state, delete with confirmation, clear all | Kelly Wambui |
| `05-proverb-editor.patch` | Proverb editor: create and update in one form, delete with confirmation, inline validation | Daniella Thande |
| `06-daily-proverb.patch` | Daily proverb screen, MyMemory translation card, WorkManager daily reminder with notification channel and POST_NOTIFICATIONS handling | Evelyn Mwangi |

Patches are numbered in the order they should land. Each one adds a destination
to `res/navigation/nav_graph.xml` and touches `values/strings.xml`, and 03 and
05 also edit the practice and home screens. Apply yours only after the earlier
numbers have merged, and if git reports a conflict on `nav_graph.xml` or
`strings.xml`, keep both sides: they are separate destinations and strings.
