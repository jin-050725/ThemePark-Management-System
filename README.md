# PROG2004 Assignment 2 — Theme Park Management System

A Java console application that simulates a small theme park: visitors queue for attractions,
rides and shows run operating cycles, staff inspect facilities, park data can be backed up to
and restored from a text file, and several attractions are operated concurrently by a thread pool.

Author: Jin Chengpeng

## Project structure

| File | Part | Purpose |
| --- | --- | --- |
| `src/Visitor.java` | 1 | Visitor entity, `Comparable` by age, equality by ID |
| `src/Staff.java` | 1, 2 | Staff entity, can inspect any `Inspectable` facility |
| `src/Inspectable.java` | 2 | Interface shared by unrelated inspectable facilities |
| `src/Toilet.java` | 2 | Facility that implements `Inspectable` but is not an attraction |
| `src/Attraction.java` | 2–5 | Abstract base class: waiting line (Part 3), visit history (Part 4), cycle logic (Part 5) |
| `src/Ride.java` | 2–5 | Ride subclass (`Inspectable`), runs only with an operator, a queue and no open inspection |
| `src/Show.java` | 2–5 | Show subclass, runs on schedule as long as a performer is assigned |
| `src/ParkManager.java` | 6 | `HashMap` based registry, O(1) lookup by ID, park-wide statistics |
| `src/ParkFileManager.java` | 7 | Save / load park state to a text file, with graceful error handling |
| `src/AssignmentTwo.java` | 8 | `main` method: scripted simulation of all 8 parts, concurrent operation via `ExecutorService` |

## How to compile and run

From the project root:

```bash
javac -d bin src/*.java
java -cp bin AssignmentTwo
```

The program prints a section for each part (Part 1 to Part 8) and ends with
`===== End of Simulation =====`. Running Part 7 also creates `park_data.txt` in the
working directory; that generated file is excluded by `.gitignore`.

## Design notes

- The waiting line is a `Queue` (`LinkedList`) so visitors are served FIFO; visit history is an
`ArrayList` that supports the two required sort orders (by age via natural ordering, and by
name then ID via a separate `Comparator`).
- `Attraction` is abstract: each subclass decides when a cycle may run through `canRunCycle()`,
which keeps ride-specific constraints (operator, queue, safety inspection) out of the base class.
- The park registry uses `HashMap<String, Attraction>`, so `getAttractionById` is O(1) and
distinct park-wide visitors are counted with a `HashSet` that deduplicates by visitor ID.
- Part 8 uses a fixed thread pool with two tasks and an `AtomicInteger` counter, so the total
number of visits is accumulated safely without a race condition.
