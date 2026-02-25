# Judexis

Judexis is a modular anti-cheat foundation designed for high-frequency game server environments.

## Architectural philosophy

The project enforces a strict separation between **detection logic** and **platform adapters**:

- `judexis-core` owns domain state, snapshot processing, check lifecycle orchestration, evidence aggregation, and decision projection.
- `judexis-adapter-1_8` is a Bukkit/Paper 1.8.8 bridge that only captures platform events and maps them into core snapshots.
- No code in `judexis-core` references Bukkit, Spigot, Paper, NMS, ProtocolLib, or any game API.
- No code in the core module references Bukkit, Spigot, Paper, NMS, ProtocolLib, or any game API.
- The core receives normalized snapshots and outputs neutral decisions so downstream integrations can decide what to do with results.

This approach keeps the detection system deterministic, testable, and portable across platforms.

## Module layout

```text
judexis/
 ├─ settings.gradle
 ├─ build.gradle
 ├─ README.md
 ├─ judexis-core/
 │    ├─ build.gradle
 │    └─ src/main/java/io/judexis/core/
 │         ├─ domain/
 │         ├─ snapshot/
 │         ├─ context/
 │         ├─ pipeline/
 │         ├─ check/
 │         ├─ violation/
 │         └─ decision/
 └─ judexis-adapter-1_8/
      ├─ build.gradle
      └─ src/main/java/io/judexis/adapter/v1_8/
           ├─ listener/
           ├─ session/
           ├─ tracker/
           └─ bridge/
```

## Adapter integration model

Adapters follow this pipeline:

1. Capture platform events/packets.
2. Convert them to core snapshots (`MovementSnapshot`, `NetworkSnapshot`, `WorldSnapshot`).
3. Resolve/create `PlayerProfile` in a session registry.
4. Publish snapshots through `JudexisCoreEngine#ingest(...)`.
5. Consume the returned `Decision` externally if policy actions are desired.

## Why separation is enforced

- **Performance**: hot-path code remains focused on primitive state updates and predictable iteration.
- **Reliability**: no classpath coupling to volatile Minecraft internals in the core.
- **Testability**: core behavior can be validated with pure JVM unit tests.
- **Portability**: one core implementation can power many runtime adapters.

## Build and test

```bash
./gradlew :judexis-core:compileJava
./gradlew :judexis-adapter-1_8:compileJava
./gradlew test
```
