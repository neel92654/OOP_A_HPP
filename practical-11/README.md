# Practical 11 — Part A Solutions + MiniBank

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

**Serialization save/load**
```bash
java -cp out practice11.SerializationDemo
```

**Log analyzer**
```bash
java -cp out practice11.LogAnalyzerDemo
```

**NIO folder-tree report (advanced)**
```bash
java -cp out practice11.FolderReportDemo
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
The current practical’s main Part A files are under `PartA_Solutions/src/practice11/`. Open `PartA_Solutions/README.md` for what each problem demonstrates.

## Notes
Race-condition and deadlock exercises intentionally demonstrate unsafe approaches alongside safe fixes. Generated runtime files go under `data/` and are not included in the ZIP.

## Suggested MiniBank test
Choose `Open account`, note the account number, and try deposits, withdrawals and transfers. Practical 11 onward saves accounts on exit; restart the app to confirm reload.
