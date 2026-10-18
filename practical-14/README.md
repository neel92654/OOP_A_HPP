# Practical 14 — Part A Solutions + MiniBank

This ZIP contains two clearly separated parts:

- **Part A solutions:** full Java source files for the main practice problems in `PartA_Solutions/src/` (with a problem-by-problem map in `PartA_Solutions/README.md`).
- **MiniBank:** the cumulative project implementation in `src/`, including features from earlier practicals and additions for this practical.

## Requirements
- JDK 17 or later
- Run from macOS Terminal / zsh or a Linux terminal

## Compile everything (Part A + MiniBank)
Run in this folder:
```bash
rm -rf out
mkdir out
javac -d out $(find src PartA_Solutions/src -name "*.java")
```

## Run Part A programs

**Part A layered app**
```bash
java -cp out practice14.Practice14Demo
```

**Practical 13 generic containers (carried forward)**
```bash
java -cp out practice13.GenericExamples
```

**Practical 13 bounded stats (carried forward)**
```bash
java -cp out practice13.BoundedStatsDemo
```

**Practical 13 generic stack (carried forward)**
```bash
java -cp out practice13.GenericStackDemo
```

**Practical 12 word frequency (carried forward)**
```bash
java -cp out practice12.WordFrequencyDemo
```

**Practical 12 unique visitors (carried forward)**
```bash
java -cp out practice12.UniqueVisitorsDemo
```

**Practical 12 leaderboard (carried forward)**
```bash
java -cp out practice12.LeaderboardDemo
```

## Run MiniBank
```bash
java -cp out service.MiniBank
```

## Build and run the MiniBank JAR
For Practicals 9–13, after the combined compile:
```bash
jar cfe MiniBank.jar service.MiniBank -C out .
java -jar MiniBank.jar
```

## Part A source locations
The current practical’s main Part A files are under `PartA_Solutions/src/practice14/`. Open `PartA_Solutions/README.md` for what each problem demonstrates.

## Notes
Race-condition and deadlock exercises intentionally demonstrate unsafe approaches alongside safe fixes. Generated runtime files go under `data/` and are not included in the ZIP.

## Part A tests and runnable JAR

The Part A source is in `PartA_Solutions/src/practice14/`, separate from MiniBank. Its model/service/menu are under packages `practice14.model`, `practice14.service`, and `practice14`. JUnit tests check add/count, invalid actions, empty names, and save/reload unchanged.

```bash
mvn test
mvn package
java -jar target/minibank-practical14-1.0.jar
```

To build the Part A menu as its own JAR after the combined `javac` compile:

```bash
jar cfe ItemDemo.jar practice14.Practice14Demo -C out .
java -jar ItemDemo.jar
```

## Persistence test
Practical 14 Part A includes a JUnit test that saves data, reloads it, and verifies that the records are unchanged. Run `mvn test` to execute JUnit tests and `mvn package` to build the final runnable MiniBank JAR.
