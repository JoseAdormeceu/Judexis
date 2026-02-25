# Judexis

Judexis is a modular anti-cheat foundation designed for high-frequency game server environments.

## Architectural philosophy

The project enforces a strict separation between **detection logic** and **platform adapters**:

- `judexis-core` owns domain state, snapshot processing, check lifecycle orchestration, evidence aggregation, and decision projection.
- No code in the core module references Bukkit, Spigot, Paper, NMS, ProtocolLib, or any game API.
- The core receives normalized snapshots and outputs neutral decisions so downstream integrations can decide what to do with results.

This approach keeps the detection system deterministic, testable, and portable across platforms.

## Module layout

```text
judexis/
 ├─ settings.gradle
 ├─ build.gradle
 ├─ README.md
 └─ judexis-core/
      ├─ build.gradle
      ├─ src/main/java/io/judexis/core/
      │    ├─ domain/
      │    ├─ snapshot/
      │    ├─ context/
      │    ├─ pipeline/
      │    ├─ check/
      │    ├─ violation/
      │    └─ decision/
      └─ src/test/java/io/judexis/core/
```

## Adapter integration model

Adapters are expected to live in separate modules (for example `judexis-paper-adapter`, `judexis-velocity-adapter`, etc.) and follow this pipeline:

1. Capture platform events/packets.
2. Normalize data into core snapshots (`MovementSnapshot`, `NetworkSnapshot`, `WorldSnapshot`).
3. Resolve or create a `PlayerProfile`.
4. Feed snapshots into `JudexisCoreEngine#ingest(...)`.
5. Consume the returned `Decision` and apply server-specific policies outside the core.

## Why separation is enforced

- **Performance**: hot-path code remains focused on primitive state updates and predictable iteration.
- **Reliability**: no classpath coupling to volatile Minecraft internals.
- **Testability**: core behavior can be validated with pure JVM unit tests.
- **Portability**: one core implementation can power many runtime adapters.

## Build and test

```bash
./gradlew test
```

