# Practical 11 — Part A Solutions

This folder contains the **Part A practice-program source code**, separately from MiniBank. These are the main exercise solutions from the lab manual; MiniBank remains in the project root `src/` tree.

## Compile Part A and MiniBank together
Run these commands from the project root:
```bash
rm -rf out
mkdir out
javac -d out $(find src PartA_Solutions/src -name "*.java")
```

## Problems covered

### 1. Save/load serialization
Serializes an object array, reloads it, and shows that a transient field is not restored.
**Source:** `PartA_Solutions/src/practice11/` — SerializationDemo.java

### 2. Log analyzer
Reads log lines one by one, counts lines and keyword matches, and displays each file size.
**Source:** `PartA_Solutions/src/practice11/` — LogAnalyzerDemo.java

### 3. NIO folder-tree report (advanced)
Walks a nested folder with NIO and writes file names, sizes, and modification times to a report.
**Source:** `PartA_Solutions/src/practice11/` — FolderReportDemo.java
