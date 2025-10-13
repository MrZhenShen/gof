# GoF Pattern Practice

Hands-on space for exploring Gang of Four (GoF) design patterns through short exercises that start from an intentionally naïve baseline and evolve into an idiomatic implementation of the target pattern.

## Purpose
- Provide bite-sized problem/solution drills that reinforce GoF concepts.
- Capture both the unstructured "raw" starting point and the refactored "solution" that applies the proper pattern.
- Serve as a reference catalogue of creational, structural, and behavioral examples implemented in Java.

## Requirements
- JDK 17 or newer (the exercises were verified with `java 17.x`).
- Gradle Wrapper (included) for building and running tasks: `./gradlew build`.

## Project Structure
- `src/main/java/practice/design/creational` – object-creation patterns such as `abstractfactory`, `factory`, `builderfailed`, `objectpool`, `prototype`, `singleton`.
- `src/main/java/practice/design/structural` – structural patterns (`adapter`, `bridge`, `composite`, `decorator`, `facade`, `flyweight`, `proxy`) that focus on composing objects and managing relationships.
- `src/main/java/practice/design/behavioral` – behavioral patterns (`chainresponsibility`, `command`, `mediator`, `state`) plus an interactive `_pattern` folder that holds paired `raw` and `solution` implementations for the training workflow.
- `src/main/resources` – supplemental assets for examples.
- `src/test/java` – room for automated checks (currently minimal).

Mermaid view of the high-level layout:

``` mermaid
graph TD
    A[gof] --> B[src]
    B --> C[main]
    B --> D[test]
    C --> E[java]
    C --> F[resources]
    D --> G[java]
    E --> H[practice.design._category]
    H --> I[_pattern]
    I --> J[TASK.md]
    I --> K[raw]
    K --> KK[Main.java]
    I --> L[solution]
    L --> L1[Main.java]
    L --> L2[_other packages and .java_]
    L --> L3[FEEDBACK.md]
```

## Practice Workflow
We use a "problem–solution" loop to practice patterns:

1. Choose a GoF category or pattern.
2. Randomly select a specific pattern (keep it hidden).
   - Describe the **problem statement** and initial requirements.
   - Supply starter code under a `raw` package that deliberately misses the target pattern.
3. Implement the expected refactor in a parallel `solution` package.
4. Review the solution:
   - Highlight what aligns with the intended pattern.
   - Point out gaps, misuses, or stylistic issues.
   - Offer refinements or an idealized implementation when helpful.

## Utilities
- Flatten all Java sources into a single file for quick reference:
  - Windows (PowerShell)
    ```powershell
    Get-ChildItem -Recurse -Filter *.java | Get-Content | Out-File all_java.txt
    ```
  - Linux / macOS (bash)
    ```bash
    find . -name "*.java" -type f -exec cat {} + > all_java.txt
    ```
- LLM Prompt for a GoF Pattern Practice Chat:
``` promt
You are a trainer for practicing design patterns (Gang of Four).

We will play in a "problem–solution" format:

1. I give you a list of categories or specific GoF patterns. 
2. You pick one pattern at random (without revealing it). 
   - You describe a **problem/task/requirements** that need to be solved.
   - You provide starter code that does not yet use the correct pattern.
   - The code should be written in such a way that it can be refactored into the chosen pattern.
3. I take your input and try to implement the correct solution using the intended pattern.
4. You then review my result:
   - Compare it with the expected solution.
   - Provide comments: what was done right, what should be improved, and whether the chosen approach matches the essence of the pattern.
   - Optionally, suggest a better implementation if needed.

Goal: to understand and reinforce GoF patterns through hands-on practice.
```
