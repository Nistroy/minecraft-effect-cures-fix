# minecraft-effect-cures-fix — instructions agents

Mod Fabric 1.21.1, serveur seul. Serveur `Nistroy/minecraft-server` (`MODS.md`). Public, GPL-3.0. Docs `.md` = notes
denses pour agents, sauf `README.md`.

## But (nistroy 2026-10-01)
Crash 2026-10-01 : 2 éjections « Internal server error » + serveur arrêté (squelette). Rapport
`server/crash-reports/crash-2026-10-01_17.49.55-server.txt`.

## Cause (Porting Lib `3.1.0-beta.90+1.21.1` embarqué par Twilight Forest `4.8.629`, javap 2026-10-01)
- `MobEffectInstanceMixin.loadEffects` : remèdes décodés NBT = `ImmutableSet` → `porting_lib$cures`.
- `copyEffects` (TAIL `setDetailsFrom`) : `getCures().clear()` → `UnsupportedOperationException`.
- Déclencheur : effet chargé avec `hidden_effect`, effet du dessus expire (`tick` → `setDetailsFrom`). `update()` vers
  un effet plus fort n'appelle pas `setDetailsFrom` → pas de crash.
- Amont corrigé : Porting-Lib#202 (2026-09-27), absent de TF `4.8.629`.

## Carte
- `mixin/MobEffectInstanceMixin` — HEAD `setDetailsFrom` : `porting_lib$cures` ← copie `HashSet`. Champ via
  `@Dynamic @Shadow(remap = false)`, priorité 1100 (après Porting Lib, 1000).
- `depends.porting_lib_entity` exact : nouvelle version → vérifier si #202 incluse → retirer le mod, sinon relire le mixin.

## Tests — TDD obligatoire
- `JAVA_HOME=/opt/homebrew/opt/openjdk@21 ./gradlew build runGameTest` (CI idem). Rouge d'abord.
- Résultat dans `build/run/gameTest/logs/latest.log` (`All N required tests passed`) ; sortie console filtrée par rtk.
- Porting Lib vient du maven `https://mvn.devos.one/snapshots` (`modLocalRuntime`, pas de classe utilisée à la compil).
- `test/mixin/GameTestEntrypointsMixin` : écarte les tests de `porting_lib_gametest` (cassés hors de leur projet).

## Release
Tag `vX.Y.Z` = `version` de `gradle.properties` → workflow `release` → jar sur la release GitHub.
