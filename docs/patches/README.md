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

Patches are numbered in the order they should land, because each one adds a
destination to `res/navigation/nav_graph.xml`. If git reports a conflict on
that file, keep both destinations: they are separate `<fragment>` blocks.
