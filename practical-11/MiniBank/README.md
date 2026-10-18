# Practical 11 — MiniBank + Part A Solutions

This ZIP contains the complete MiniBank project progressed through Practical 11, plus the Part A practice programs for Practical 11. It includes the previous MiniBank work (classes, encapsulation, equality, validation, command parsing, inheritance, interfaces, annotations, exceptions) and the new work for this practical.

## Requirements
- JDK 17 or later
- Run commands from this project folder in macOS Terminal / zsh (also works in most Linux terminals)

## Compile all Java files
```bash
rm -rf out
mkdir out
javac -d out $(find src -name "*.java")
```

## Run the Practical 11 Part A programs
```bash
java -cp out practice11.SerializationDemo
```

```bash
java -cp out practice11.LogAnalyzerDemo
```

```bash
java -cp out practice11.FolderReportDemo
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

MiniBank saves accounts to `data/accounts.dat`, appends deposit/withdraw entries in `data/logs/transactions.log`, reloads the saved accounts at startup, and writes `data/report.txt`. Generated data is created only when you run it.

## Suggested menu test

Choose `Open account`, note the account number printed, then test deposit, withdrawal and transfer. For Practical 11 onward, exit normally and start the app again to confirm saved balances reload.
