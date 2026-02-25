# Android Development & Refactoring Agent Guide

**Mission**: Safely refactor existing code and implement new features robustly without breaking current behavior.
**Conflict Resolution Priority**: 1) This file > 2) Repo docs > 3) Build logic > 4) AI preferences.

## 1. Core Directives
- **Refactoring**: Preserve existing behavior. Prefer small, reviewable changes. No massive "format everything" commits.
- **Feature Addition**: Build strictly to the provided spec. Integrate seamlessly with the existing architecture (e.g., MVI, MVVM, Clean Architecture). Reuse existing base classes, UI components, and utilities.
- **No Silent Changes**: Do not migrate languages (Java to Kotlin), UI frameworks (XML to Compose), or architectures without explicit orders.
- **Dependencies**: Do not bump versions or add new libraries unless strictly required for a new feature and explicitly approved.

## 2. Standard Workflow
1. **Analyze**: Map relevant modules, entry points, and dependencies before modifying or adding code. Identify potential impacts on existing features.
2. **Propose**: Output a brief plan containing: Goals, Scope, Architectural approach (for new features), Risk Assessment, and Validation strategy. Wait for user confirmation.
3. **Execute**: Implement in isolated, logical phases.
4. **Validate**: Ensure the project compiles, new features integrate correctly, and tests pass.

## 3. Architecture & Android Constraints
- **Lifecycle & Memory**: Respect Android lifecycles. Prevent Context leaks. Handle configuration changes and process death correctly.
- **State Management**: For new UI features, handle states (Loading, Success, Error) explicitly and safely.
- **Concurrency**: Do not block the main thread. Use existing Coroutine/Flow setups. Do not introduce new thread pools casually.
- **Error Handling**: Never swallow exceptions silently. Validate external inputs, IO, and Network boundaries defensively.
- **Logging**: Keep existing log formats. Do not add noisy logs in production loops.

## 4. Required Output Format
For every completed task (feature or refactoring batch), output:
* **Summary**: High-level explanation of changes or additions.
* **Scope**: List of files/modules touched or created.
* **Risk & Mitigation**: Potential side effects on existing code and how they were mitigated.
* **Validation**: Commands to run (e.g., `./gradlew test`) or specific UI flows to manually verify.

## 5. Fallback Protocol (If Stuck)
If blocked by an error, constraint, or ambiguous feature requirement, stop and report:
1. The exact error or missing information.
2. What you tried or analyzed.
3. Provide options to proceed (always list the safest, most architecturally consistent option first).