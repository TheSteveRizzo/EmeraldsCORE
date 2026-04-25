# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build

```bash
mvn clean install   # Build the shaded plugin JAR
mvn compile         # Compile only
```

Output JAR is in `target/`. The Maven Shade plugin bundles HikariCP into the JAR; Vault, DiscordSRV, ProtocolLib, and PlaceholderAPI are excluded (provided by the server at runtime).

No automated tests are configured in this project.

## Project Overview

**EmeraldsCore v6.0** — A Spigot 1.21.11 plugin (Java 21) for a multi-server Minecraft network. Main class: `com.steve_rizzo.emeraldscore.Main`.

Servers are identified by `serverID.yml` (`hub`, `smp`, `sky`). Plugin messaging channel `emeraldscore:chat` carries cross-server chat with the format `chat|<serverID>|<prefix>|<playerName>|<message>`; `PrefixSender` deduplicates via an in-memory `HashSet` flushed every 5 minutes. Discord relay goes through DiscordSRV to a hardcoded channel ID.

## Architecture

### Initialization (`Main.onEnable`)
1. Vault hooks (economy, permissions, chat)
2. YML configs saved/loaded (`emeralds.yml` for DB creds, `serverID.yml`, `cooldownNPC.yml`)
3. HikariCP pool initialized → three MySQL tables auto-created (`Ranks`, `EmeraldsCash`, `EmeraldsTokens`)
4. All listeners registered via `Bukkit.getPluginManager().registerEvents(...)`
5. All commands registered via `this.getCommand(...).setExecutor(...)`
6. Plugin messaging channels registered

### Data Persistence
Three MySQL tables share the same schema pattern (`UUID`, `name`, `balance`/`rank`, `date`):

- **Ranks** — managed by `utils/Ranks.java`; in-memory cache, synced with LuckPerms via `lp user <name> parent set <rank>`. Loaded/saved on join/quit.
- **EmeraldsCash** — primary currency; `commands/economy/api/EmeraldsCashAPI.java`; implements Vault `Economy` via `commands/economy/vault/EconomyImplement.java`.
- **EmeraldsTokens** — secondary currency; `commands/tokens/TokensAPI.java`.

All DB calls return `CompletableFuture<T>` and use `ON DUPLICATE KEY UPDATE` for upsert.

### Command & Listener Pattern
Commands implement `CommandExecutor` (single `onCommand` method). Listeners implement `Listener` with `@EventHandler` methods. Both are registered centrally in `Main.onEnable`; all must also be declared in `plugin.yml`.

### Key Subsystems

**Economy (`commands/economy/`)** — `BalanceCommand`, `PayCommand`, `GiveCommand`, `TakeCommand`, `SetCommand`, `BaltopCommand`. All delegate to `EmeraldsCashAPI` for async DB access.

**Tokens (`commands/tokens/`)** — Mirrors economy structure. `BuyTokensCommand` converts EmeraldsCash → EmeraldsTokens.

**Casino (`casino/`)** — Blackjack (`BlackjackManager` holds `Map<UUID, BlackjackGame>` sessions), Roulette (`RouletteGame` instantiated in `Main.onEnable`), and rank income collection (`CollectCommand` + `Income`).

**Mining Pouch (`features/miningpouch/`)** — ItemStack-based storage using custom lore to track quantities of 17 mining materials. `Pouch` is the static utility class. Five listeners handle pickup, GUI clicks, interaction, player movement, and crafting prevention.

**Villager Saver (`features/villagersave/`)** — Prevents despawn; world blacklist persisted to `VillagerBlacklistWorld.yml`.

**Ranks** — Hierarchy (owner → admin → mod → helper → youtuber/builder → platinum → elite → donor4–1 → member → default/guest). Rank prefix fetched via Vault chat API; affects income amounts, shop access, and staff commands.

## Configuration Files
| File | Purpose |
|------|---------|
| `emeralds.yml` | MySQL host/port/user/pass/dbname |
| `serverID.yml` | Server identifier (`hub`, `smp`, `sky`) |
| `cooldownNPC.yml` | NPC interaction cooldown tracking |
| `VillagerBlacklistWorld.yml` | Worlds where villager saving is disabled |
