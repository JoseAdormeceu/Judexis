# Judexis

Judexis is a modular anti-cheat foundation designed for high-frequency game server environments.

## Architectural philosophy

The project enforces strict separation between detection runtime and platform glue:

- `judexis-core` contains snapshot dispatching, check registry/manager, player runtime data store, evidence routing, rolling violation policy, and decision events.
- `judexis-adapter-1_8` captures Bukkit/Paper 1.8 events and translates them into core snapshots.
- Core remains independent of Bukkit/Spigot/Paper/NMS/ProtocolLib.

## Module layout

```text
judexis/
 ├─ settings.gradle
 ├─ build.gradle
 ├─ README.md
 ├─ judexis-core/
 │    ├─ build.gradle
 │    ├─ libs/ (optional local junit jars)
 │    └─ src/main/java/io/judexis/core/
 │         ├─ check/
 │         ├─ context/
 │         ├─ data/
 │         ├─ decision/
 │         ├─ domain/
 │         ├─ pipeline/
 │         ├─ snapshot/
 │         ├─ violation/
 │         └─ debug/
 └─ judexis-adapter-1_8/
      ├─ build.gradle
      ├─ libs/ (optional local Paper API jar)
      └─ src/main/java/io/judexis/adapter/v1_8/
```

## Check System v1

Core now includes:

- `CheckRegistry` and `CheckManager` for id/category registration and lifecycle.
- `CheckConfiguration` for in-memory runtime enable/disable toggles.
- `PlayerDataStore` with per-player `ContextState`, `ViolationAccumulator`, and per-check state slots.
- `EvidenceRouter` + `ViolationPolicy` to record evidence and compute rolling check/global scores.
- `DecisionEventBus` stream with `DecisionEventListener` subscription.

## Adapter runtime surface

`judexis-adapter-1_8` provides:

- Event capture for `PlayerJoinEvent`, `PlayerQuitEvent`, `PlayerMoveEvent`, `EntityDamageByEntityEvent`.
- Tick heartbeat updating ping/TPS context.
- `/judexis debug <player>` for runtime telemetry and last evidence entries.
- `/judexis toggle <checkId> on|off` for in-memory check toggling (op only).

## Offline-friendly build setup

If repository access is blocked:

1. **Core tests**: place `junit-platform-console-standalone-1.10.2.jar` in:
   - `judexis-core/libs/junit-platform-console-standalone-1.10.2.jar`
2. **Adapter compile**: place Paper API jar in:
   - `judexis-adapter-1_8/libs/paperspigot-api-1.8.8-R0.1-SNAPSHOT.jar`

Both modules are configured to use local jars first, then remote repositories.

## Build commands

```bash
./gradlew :judexis-core:compileJava
./gradlew :judexis-core:test
./gradlew :judexis-adapter-1_8:compileJava
```
