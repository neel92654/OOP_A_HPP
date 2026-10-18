# Practical 13 — MiniBank + Part A Solutions

This ZIP contains the complete MiniBank project progressed through Practical 13, plus the Part A practice programs for Practical 13. It includes the previous MiniBank work (classes, encapsulation, equality, validation, command parsing, inheritance, interfaces, annotations, exceptions) and the new work for this practical.

## Requirements
- JDK 17 or later
- Run commands from this project folder in macOS Terminal / zsh (also works in most Linux terminals)

## Compile all Java files
```bash
rm -rf out
mkdir out
javac -d out $(find src -name "*.java")
```

## Run the Practical 13 Part A programs
```bash
java -cp out practice13.GenericExamples
```

```bash
java -cp out practice13.BoundedStatsDemo
```

```bash
java -cp out practice13.GenericStackDemo
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

Includes a generic Repository<T> that locates @Id through reflection (including inherited fields), generic saveAll, and bounded numeric average. MiniBank uses Repository<Account>.

## Suggested menu test

Choose `Open account`, note the account number printed, then test deposit, withdrawal and transfer. For Practical 11 onward, exit normally and start the app again to confirm saved balances reload.
