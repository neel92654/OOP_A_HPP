# Object Oriented Programming (CEUE203) — Coursework & MiniBank Project

This repository contains the complete Java Object Oriented Programming coursework submissions and the final cumulative **MiniBank** project developed across **Practicals 1 to 14**.

---

## Repository Structure

- [`MiniBank/`](file:///MiniBank/) — Complete, cumulative MiniBank application incorporating all practical implementations (1–14), JUnit 5 tests, Maven configuration, and runnable JAR.
- [`MiniBank_Report.pdf`](file:///MiniBank_Report.pdf) — Complete final MiniBank report following the practical file format with full source code listings and practical attributions.
- `Practical-1` to `Practical-8` — Core Java OOP laboratory exercises.
- `practical-9` to `practical-14` — Advanced topics: multithreading, concurrency, serialization, collections, generics, and service integration.

---

## MiniBank Project Overview (Practicals 1–14)

MiniBank is a robust, modular console-based banking application designed to demonstrate modern Java features and object-oriented principles across 14 practical milestones:

1. **Practical 1 — Menu & Bank Information:**
   - Record `BankInfo(name, branch)` for immutable bank metadata.
   - Enum `MenuOption` for type-safe menu action routing.

2. **Practical 2 — Customer & Account Foundations:**
   - `Customer` model with nested `Address` class.
   - Encapsulation, robust constructors, and basic balance operations.

3. **Practical 3 — Object Contracts & Nested Classes:**
   - Override `equals()`, `hashCode()`, and `toString()`.
   - Implement `Comparable<Account>` for natural ordering by account number.

4. **Practical 4 — Input Validation & Formatting:**
   - `Validator` utilizing regular expressions for mobile numbers, email, PAN, and IFSC codes.
   - `CommandParser` for structured command evaluation.
   - `StatementFormatter` using `StringBuilder` for formatted account statements.

5. **Practical 5 — Inheritance & Runtime Polymorphism:**
   - Abstract base class `Account`.
   - Concrete subclasses: `SavingsAccount` (with minimum balance constraints), `CurrentAccount` (with overdraft limit), and `FixedDepositAccount` (with fixed tenure and compound interest calculation).

6. **Practical 6 — Interfaces & Functional Programming:**
   - Interfaces `Transactable` and `InterestBearing` with default methods.
   - Marker interface `Premium`.
   - Functional interface `WithdrawRule` and lambda expressions for configurable withdrawal authorization.

7. **Practical 7 — Custom Annotations & Reflection:**
   - Declarative annotations `@Id`, `@Positive`, and `@MaxLength`.
   - `AnnotationValidator` reflecting over model fields at runtime to validate constraints.

8. **Practical 8 — Exception Hierarchy & Resource Management:**
   - Checked exception hierarchy: `BankException`, `InvalidAmountException`, `InsufficientFundsException`, and `AccountNotFoundException`.
   - `BankResource` implementing `AutoCloseable` for automatic cleanup via `try-with-resources`.

9. **Practical 9 — Multithreading & Synchronization:**
   - Worker thread `AccountWorker` for background operations.
   - Synchronized account methods ensuring thread-safe balance mutations.

10. **Practical 10 — Thread Pools & Concurrency Coordination:**
    - `ExecutorService` thread pool managing concurrent deposits and withdrawals.
    - `TransactionBuffer` implementing a bounded array queue with `wait()` and `notifyAll()` producer–consumer coordination.
    - Deadlock prevention via deterministic canonical locking order during transfers.

11. **Practical 11 — Serialization, Logging & NIO Reporting:**
    - `StatePersister` implementing binary serialization/deserialization for account persistence across restarts.
    - `TransactionLog` maintaining append-only audit trail logs under `data/logs/`.
    - `ReportGenerator` utilizing Java NIO (`java.nio.file`) for daily financial summary reports.

12. **Practical 12 — Collections Framework:**
    - `AccountStore` utilizing `List`, `Map`, and thread-safe collections (`ConcurrentHashMap`).
    - Multiple sorting criteria using `Comparator` (by balance, owner, or account number).

13. **Practical 13 — Generics & Reflection:**
    - Generic data access layer `Repository<T>`.
    - Dynamic reflection-based lookup by `@Id` annotation.
    - `GenericStatistics` for generic numerical calculations.

14. **Practical 14 — Service Integration & Automated Testing:**
    - `BankService` unifying account management, transactions, batch processing, and reporting.
    - Interactive command-line interface in `MiniBank`.
    - Automated test suite `BankServiceTest` using JUnit 5 Jupiter.

---

## Prerequisites

- **Java Development Kit (JDK):** Version 17 or higher (`java -version`).
- **Maven (optional):** Apache Maven 3.8+ for running automated test suites via `mvn test`.

---

## Build & Execution Instructions

### Option 1: Direct Execution via Runnable JAR

A pre-packaged runnable JAR is included in the project:

```bash
cd MiniBank
java -jar MiniBank.jar
```

### Option 2: Compile and Run from Source

Compile the source files directly using `javac`:

```bash
cd MiniBank
rm -rf out
mkdir out
javac -d out $(find src -name '*.java')
java -cp out service.MiniBank
```

### Option 3: Build and Test with Maven

If Maven is installed on your system:

```bash
cd MiniBank

# Execute JUnit 5 automated test suite
mvn test

# Package into target/minibank-final-1.0.0.jar
mvn package

# Run the packaged JAR
java -jar target/minibank-final-1.0.0.jar
```

---

## Automated Test Suite

The test suite in [`MiniBank/test/service/BankServiceTest.java`](file:///MiniBank/test/service/BankServiceTest.java) validates:
1. `depositIncreasesBalance()`: Valid deposits increase balance correctly.
2. `withdrawalOverSavingsLimitThrows()`: Enforces minimum balance requirements on Savings accounts.
3. `saveAndLoadPreservesAccountState()`: Serializes accounts to disk and reloads them without loss of state.
4. `concurrentDepositsKeepCorrectBalance()`: Thread safety under 1,000 concurrent deposits using thread pools.
5. `oppositeDirectionTransfersRemainConsistent()`: Deadlock-free bidirectional concurrent transfers between accounts.