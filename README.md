# SpearElytra

A Paper plugin prototype that lets a player hit another elytra-gliding player by swinging a spear while aiming at them.

## Behavior
- Both attacker and target must be gliding with elytra.
- The attacker must hold an item whose material is `SPEAR`; `TRIDENT` is accepted as a fallback for older APIs.
- Range: 4.5 blocks.
- Damage: 8 points (4 hearts) before armor/enchantments and other damage modifiers.
- 500 ms per-attacker cooldown.

## Build
Requires Java 21+ and Maven, plus a Paper API artifact available for your exact server version.

Run from this folder:
```bash
mvn package
```
The plugin JAR will be in `target/spear-elytra-1.0.0.jar`. Put it in the server's `plugins` folder and restart.

## Important compatibility note
`26.3-R0.1-SNAPSHOT` is a version-specific API coordinate and may not exist in the Paper Maven repository for your server build. If Maven cannot resolve it, change the `paper-api` version in `pom.xml` to the exact API version listed for your Paper 26.3 server. This is source code, not a precompiled JAR.
