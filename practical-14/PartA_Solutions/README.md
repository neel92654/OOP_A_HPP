# Practical 14 — Part A Solutions

This folder contains the **Part A practice-program source code**, separately from MiniBank. These are the main exercise solutions from the lab manual; MiniBank remains in the project root `src/` tree.

## Compile Part A and MiniBank together
Run these commands from the project root:
```bash
rm -rf out
mkdir out
javac -d out $(find src PartA_Solutions/src -name "*.java")
```

## Problems covered

### 1. Tiny layered app and JUnit tests
Model, ArrayList-backed service, menu, tests for adding/count and invalid actions.
**Source:** `PartA_Solutions/src/practice14/` — Practice14Demo.java

### 2. Packages, runnable JAR, save/load test (advanced)
The Part A app is organised into packages, supports save/load, and has a test verifying the data round-trips unchanged.
**Source:** `PartA_Solutions/src/practice14/` — ItemService.java + ItemServiceTest.java

## Earlier Part A solutions also carried in this snapshot

- `practice12/` contains the Part A programs from that earlier practical. Run them with the matching `java -cp out practice12.ClassName` command in the main README / run guide.
- `practice13/` contains the Part A programs from that earlier practical. Run them with the matching `java -cp out practice13.ClassName` command in the main README / run guide.

## Part A tests
`test/practice14/service/ItemServiceTest.java` tests that adding an item increases the count, invalid actions throw, and save/reload preserves the data. The Maven build is configured to include `PartA_Solutions/src` as an additional source root.

Build and run the standalone Part A JAR after compiling:
```bash
jar cfe ItemDemo.jar practice14.Practice14Demo -C out .
java -jar ItemDemo.jar
```
