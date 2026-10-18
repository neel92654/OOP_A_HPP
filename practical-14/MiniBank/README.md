# Practical 14 — MiniBank + Part A Solutions

This ZIP contains the complete MiniBank project progressed through Practical 14, plus the Part A practice programs for Practical 14. It includes the previous MiniBank work (classes, encapsulation, equality, validation, command parsing, inheritance, interfaces, annotations, exceptions) and the new work for this practical.

## Requirements
- JDK 17 or later
- Run commands from this project folder in macOS Terminal / zsh (also works in most Linux terminals)

## Compile all Java files
```bash
rm -rf out
mkdir out
javac -d out $(find src -name "*.java")
```

## Run the Practical 14 Part A programs
```bash
java -cp out practice14.Practice14Demo
```

## Run MiniBank

```bash
java -cp out service.MiniBank
```

## Run JUnit tests and build the runnable JAR

Install Maven if it is not installed (`brew install maven`), then run:

```bash
mvn test
mvn package
java -jar target/minibank-practical14-1.0.jar
```

The JUnit 5 tests cover Part A adding/counting, invalid actions, and save/reload; MiniBank tests cover deposits, rejected withdrawals, and concurrent deposits. To build and run the Part A app as a separate JAR after the `javac` compile, run `jar cfe ItemDemo.jar practice14.Practice14Demo -C out .` and then `java -jar ItemDemo.jar`.

## Notes

Final capstone: layered BankService, repository, validation, checked exceptions, persistence, transaction log/report, concurrent batch deposits, interactive menu, JUnit 5 tests and runnable JAR.

## Suggested menu test

For Practical 14, open an account first, note its account number, then try deposit, withdrawal, transfer, statement, list, report, and batch deposit. Exit normally so the latest account state is saved.
