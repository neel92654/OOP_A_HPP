# Practical 9 — MiniBank + Part A Solutions

This ZIP contains the complete MiniBank project progressed through Practical 9, plus the Part A practice programs for Practical 9. It includes the previous MiniBank work (classes, encapsulation, equality, validation, command parsing, inheritance, interfaces, annotations, exceptions) and the new work for this practical.

## Requirements
- JDK 17 or later
- Run commands from this project folder in macOS Terminal / zsh (also works in most Linux terminals)

## Compile all Java files
```bash
rm -rf out
mkdir out
javac -d out $(find src -name "*.java")
```

## Run the Practical 9 Part A programs
```bash
java -cp out practice09.CounterRaceDemo
```

```bash
java -cp out practice09.SeatBookingDemo
```

```bash
java -cp out practice09.ParallelSumDemo
```

```bash
java -cp out service.ConcurrencyDemo
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

Thread/race demos: the unsynchronized examples are intentionally unsafe and their totals may vary; synchronized versions demonstrate the correct totals. MiniBank deposit and withdrawal are synchronized.

## Suggested menu test

Choose `Open account`, note the account number printed, then test deposit, withdrawal and transfer. For Practical 11 onward, exit normally and start the app again to confirm saved balances reload.
