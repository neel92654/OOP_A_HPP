# Practical 12 — Part A Solutions

This folder contains the **Part A practice-program source code**, separately from MiniBank. These are the main exercise solutions from the lab manual; MiniBank remains in the project root `src/` tree.

## Compile Part A and MiniBank together
Run these commands from the project root:
```bash
rm -rf out
mkdir out
javac -d out $(find src PartA_Solutions/src -name "*.java")
```

## Problems covered

### 1. Word frequency
Counts words with HashMap and sorts results by frequency using a Comparator.
**Source:** `PartA_Solutions/src/practice12/` — WordFrequencyDemo.java

### 2. Unique visitors
Uses HashSet to count distinct visitors and LinkedHashSet to preserve first-seen order.
**Source:** `PartA_Solutions/src/practice12/` — UniqueVisitorsDemo.java

### 3. Leaderboard (advanced)
Sorts score entries by descending score and then by player name; accepts optional top-N.
**Source:** `PartA_Solutions/src/practice12/` — LeaderboardDemo.java
