# Practical 12 — MiniBank + Part A Solutions

This ZIP contains the complete MiniBank project progressed through Practical 12, plus the Part A practice programs for Practical 12. It includes the previous MiniBank work (classes, encapsulation, equality, validation, command parsing, inheritance, interfaces, annotations, exceptions) and the new work for this practical.

## Requirements
- JDK 17 or later
- Run commands from this project folder in macOS Terminal / zsh (also works in most Linux terminals)

## Compile all Java files
```bash
rm -rf out
mkdir out
javac -d out $(find src -name "*.java")
```

## Run the Practical 12 Part A programs
```bash
java -cp out practice12.WordFrequencyDemo
```

```bash
java -cp out practice12.UniqueVisitorsDemo
```

```bash
java -cp out practice12.LeaderboardDemo
```

## Run MiniBank

```bash
java -cp out service.MiniBank
```

## Build and run a JAR

```bash
jar cfe MiniBank.jar service.MiniBank -C out .
java -jar MiniBank.jar
```

## Notes

Includes HashMap-backed AccountStore, ArrayList transaction history, balance sorting via Comparator, account natural order via Comparable, and a ConcurrentHashMap in TransactionProcessor.

## Suggested menu test

Choose `Open account`, note the account number printed, then test deposit, withdrawal and transfer. For Practical 11 onward, exit normally and start the app again to confirm saved balances reload.
