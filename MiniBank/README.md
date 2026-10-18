# MiniBank - Final Implementation Through Practical 14

This is the cumulative MiniBank console application covering the MiniBank Part B work from Practicals 1 to 14. The Java source is in `src/`; JUnit tests are in `test/`. Runtime data is created in `data/` and is intentionally not included in the project archive.

## Features

- Menu-driven account opening, deposit, withdrawal, transfer, statements, listing, reporting, batch deposits and exit.
- `BankInfo` record and `MenuOption` enum.
- `Customer`, abstract `Account`, account subclasses, encapsulation, constructors, nested `Address`, cloning, `toString`, `equals`, `hashCode` and `Comparable`.
- `Transactable`, `InterestBearing`, `Premium`, and `WithdrawRule` using interface/default-method/marker/functional-interface concepts.
- Regex validation for mobile, email, PAN and IFSC; command parsing; StringBuilder statement formatting.
- Runtime annotations and reflection-based validation and generic `Repository<T>` lookup through `@Id`.
- Checked banking exception hierarchy and try-with-resources via `BankResource`.
- Synchronized balance operations, `AccountWorker`, an `ExecutorService` pool, `ConcurrentHashMap`, an array-buffer producer/consumer using `wait()`/`notifyAll()`, and consistent account-lock ordering during transfer.
- Serialization save/load, append-only transaction logs, NIO report generation and data persistence across restarts.
- JUnit tests for deposits, failed withdrawals, concurrent deposits, save/load and opposite-direction transfers.

## Requirements

- JDK 17 or later.
- Maven for JUnit tests and Maven-built JAR packaging.

## Compile and run on macOS/Linux

From this project root:

```bash
rm -rf out
mkdir out
javac -d out $(find src -name '*.java')
java -cp out service.MiniBank
```

## Run tests and build a runnable JAR

```bash
mvn test
mvn package
java -jar target/minibank-final-1.0.0.jar
```

The runnable JAR manifest names `service.MiniBank` as the main class. Runtime account state, transaction logs and reports are stored in `data/` relative to the working directory.

## Source package map

- `model/`: account types, customer, records, enums and interfaces.
- `model/annotation/`: `@Id`, `@Positive` and `@MaxLength`.
- `exception/`: checked banking exceptions.
- `util/`: regex validation, annotation reflection, generic repository, account store, parsing, persistence, logging, reporting, statement formatting, buffer and resource helpers.
- `service/`: interactive menu, bank service, account worker and transaction processor.
- `test/`: JUnit test cases for the final MiniBank behavior.
