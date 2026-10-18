# Practical 13 — Part A Solutions

This folder contains the **Part A practice-program source code**, separately from MiniBank. These are the main exercise solutions from the lab manual; MiniBank remains in the project root `src/` tree.

## Compile Part A and MiniBank together
Run these commands from the project root:
```bash
rm -rf out
mkdir out
javac -d out $(find src PartA_Solutions/src -name "*.java")
```

## Problems covered

### 1. Generic containers
Demonstrates Box<T>, Pair<A,B>, and a generic first-element method without casts.
**Source:** `PartA_Solutions/src/practice13/` — GenericExamples.java

### 2. Bounded statistics
Computes averages for Integer[], Double[], and Long[] using N extends Number.
**Source:** `PartA_Solutions/src/practice13/` — BoundedStatsDemo.java

### 3. Generic array-backed stack (advanced)
Uses a reusable generic stack for two types and handles empty/full conditions.
**Source:** `PartA_Solutions/src/practice13/` — GenericStackDemo.java

## Earlier Part A solutions also carried in this snapshot

- `practice12/` contains the Part A programs from that earlier practical. Run them with the matching `java -cp out practice12.ClassName` command in the main README / run guide.
