# Practical 10 — MiniBank + Part A Solutions

This ZIP contains the complete MiniBank project progressed through Practical 10, plus the Part A practice programs for Practical 10. It includes the previous MiniBank work (classes, encapsulation, equality, validation, command parsing, inheritance, interfaces, annotations, exceptions) and the new work for this practical.

## Requirements
- JDK 17 or later
- Run commands from this project folder in macOS Terminal / zsh (also works in most Linux terminals)

## Compile all Java files
```bash
rm -rf out
mkdir out
javac -d out $(find src -name "*.java")
```

## Run the Practical 10 Part A programs
```bash
java -cp out practice10.ThreadPoolDemo
```

```bash
java -cp out practice10.ProducerConsumerDemo
```

```bash
java -cp out practice10.DeadlockDemo
```

```bash
java -cp out service.TransactionProcessorDemo
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

Includes a 3-thread pool, wait/notify producer–consumer, a bounded deadlock demonstration using daemon threads, and a consistent-lock-order fix. MiniBank transfer uses account-number lock ordering.

## Suggested menu test

Choose `Open account`, note the account number printed, then test deposit, withdrawal and transfer. For Practical 11 onward, exit normally and start the app again to confirm saved balances reload.
