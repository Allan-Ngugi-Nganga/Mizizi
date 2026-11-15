# Release checklist (v1.0)

Run these in order on `develop` once every slice has merged.

## 1. Prove the build

```bash
./gradlew clean assembleDebug testDebugUnitTest
```

Unit tests must pass. The report lands in
`app/build/reports/tests/testDebugUnitTest/index.html`.

## 2. Run it once on an emulator

```bash
./gradlew installDebug
adb shell am start -n com.example.mizizi/.MainActivity
```

Walk: Home, Practice (answer one proverb, see the grade), See full feedback,
Archive (the attempt is listed), Add a proverb (count goes up), Proverb of the
day (machine translation loads, reminder switches on).

Then close the app completely and reopen it. The proverb count and archive must
still be there, which is the persistence evidence.

## 3. Sign the release

Create a keystore once, outside the repo:

```bash
keytool -genkeypair -v -keystore mizizi-release.jks -keyalg RSA -keysize 2048 \
  -validity 10000 -alias mizizi
```

Then create `keystore.properties` in the project root. It is git ignored:

```
storeFile=/absolute/path/to/mizizi-release.jks
storePassword=...
keyAlias=mizizi
keyPassword=...
```

## 4. Build the signed APK

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`. Verify the signature:

```bash
apksigner verify --print-certs app/build/outputs/apk/release/app-release.apk
```

## 5. Release it

```bash
git checkout develop
git pull
git checkout -b release/v1.0
git push -u origin release/v1.0
```

`main` is protected by the `protect-main` ruleset: it needs a pull request plus one
approving review, and direct pushes are rejected. So open a pull request from
`release/v1.0` into `main`, get a teammate to approve it, and merge on GitHub.

Then tag the merge commit:

```bash
git checkout main
git pull
git tag -a v1.0 -m "Mizizi v1.0"
git push origin v1.0
```

Then create the GitHub release on the `v1.0` tag, attach `app-release.apk`, and
paste the download link into the README header.

## 6. Before submission

- [ ] Every member appears on `Insights > Contributors`
- [ ] Each member can explain their own slice, code included
- [ ] README lists the architecture, the CRUD table and the data flow
- [ ] Screenshots in `docs/screenshots/` are from this build
- [ ] Concept note link still opens
