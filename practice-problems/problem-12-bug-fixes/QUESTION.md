# Problem 12 — Spot and Fix the Bugs

**Source:** CSE 2218 Advanced OOP Lab — Section 5, Question 12

## The Question

Each code snippet below contains a common mistake. **Identify the bug and write the corrected version.**

| Bug | Description |
|---|---|
| Bug 1 | The POST endpoint returns **200** instead of **201** |
| Bug 2 | The service crashes with `NoSuchElementException` instead of returning **404** |
| Bug 3 | The entity fails to load from DB with **"No default constructor found"** |
| Bug 4 | The seeder **inserts duplicates** every time the app restarts |
| Bug 5 | The exception handler is **never triggered** — Spring still returns 500 |

## How to Use This Folder

- `buggy/` — contains the 5 buggy code files (one per bug)
- `fixed/` — contains the 5 corrected code files (one per bug)
- `DIFF.md` — explains exactly what changed and why

## Exam Strategy

For each bug, ask yourself:
1. What symptom does this bug cause? (wrong status code, crash, duplicate data, etc.)
2. Which LINE causes the bug?
3. What is the minimal fix?

These bugs are **explicitly tested** in CSE 2218. Memorise all 5 bugs and their fixes.
