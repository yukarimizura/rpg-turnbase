<p align="center">
  <b>English</b> | <a href="README.id.md">Bahasa Indonesia</a>
</p>

---

# ⚔️ Cyber-Chrono ARPG Turn-Based Engine

A lightweight, pure Java 2D Action RPG engine built with native Swing and `Graphics2D`. It implements a hybrid combat system combining real-time **Vanguard Arena Combat** with automated **Off-Field Support Resonance**. The project adheres strictly to modular Object-Oriented Programming (OOP) clean architecture guidelines, maintaining every single file under 100 lines of code.

---

## 🧠 Algorithmic Foundation & Technical Implementation

To satisfy technical academic evaluation, this project implements core computer science algorithms from scratch without external dependencies:

### 1. Collision Detection & Proximity Targeting (Euclidean Geometry)
* **Bounding Circle Collision Detection:**
  Contact between circular hitboxes (Vanguard, Creeps, and Bosses) is evaluated using the Euclidean distance metric:
  * **Distance Formula:** `Distance = sqrt((x2 - x1)^2 + (y2 - y1)^2)`
  * A collision occurs whenever `Distance < (radius1 + radius2)`.
* **Nearest-Neighbor Heuristic Targeting:**
  Used by the *Ranger* subrole to locate and lock onto the single closest enemy entity with $O(N)$ linear time complexity before firing targeted projectiles.

### 2. Finite State Machine (Game Lifecycle Management)
The game loop transitions are managed through a centralized FSM via `GameState` (`PROFILE_SELECT`, `TITLE`, `LOBBY`, `GACHA`, `PARTY`, `BATTLE`, `SETTINGS`, `VICTORY`, `GAME_OVER`). This ensures deterministic rendering and execution flow, completely preventing state bleeding between arena combat and menu routing.

### 3. Probability Distribution & Gacha Pity Counter Algorithm
The hero recruitment portal utilizes pseudo-random uniform distribution sampling with conditional guarantee logic:
* **Base Pull Rates:** SSR = 2%, SR = 13%, R = 85%.
* **Pity Mechanism:**
  * **Soft Pity:** Guaranteed minimum of SR rarity every 10 consecutive pulls.
  * **Hard Pity:** Accumulating 50 pulls (`pityCounter >= 50`) forces a 100% guaranteed SSR outcome, resetting the counter back to 0.

### 4. Boss AI State Loop: Telegraphing & Burst Cycle
The stage boss runs on a cyclic time-driven AI state machine:
1. **Chasing State:** The boss paths toward the Vanguard's coordinates using normalized directional velocity.
2. **Telegraph Charge State:** Velocity drops to 0, and a circular warning indicator is rendered over the floor for a fixed duration.
3. **AoE Burst Execution:** When the timer expires, an area-of-effect check runs instantaneously. Targets caught inside receive amplified multiplier damage.

### 5. Flat-File Serialization Parsing (`.save`)
Lightweight local persistence without heavyweight external JSON dependencies using stream-based delimiter parsing (`;`):
* Reads lines sequentially through `BufferedReader`.
* Differentiates header records (account profile, currencies, stage progress, pity status) from dynamic entity records (`HERO;<id>;<role>;...`).
* Reconstructs runtime hero instances via object polymorphism during loading.

### 6. Dynamic Scaling Matrix (Virtual Screen Mapping)
The engine operates on a fixed virtual canvas of `950 x 600`. The scaling algorithm maps window and monitor drag transformations back to the virtual plane in real time:
* **X_virtual** = `X_mouse / Scale_X`
* **Y_virtual** = `Y_mouse / Scale_Y`

This preserves button click bounds and aspect ratios across resizing, maximizing, and custom resolution presets.

---

## 🎮 Core Game Mechanics

1. **Vanguard & Resonance System:**
   * **Vanguard (Slot 1):** Reserved exclusively for **Attacker** units. The primary hero controlled in the combat arena.
   * **Resonance Squad (Slots 2–5):** Filled by **Defender**, **Healer**, and **Support** units. They remain off-field and automatically trigger support skills periodically:
     * **Guardian:** Deploys a high-capacity flat shield damage buffer.
     * **Paladin:** Provides an Aegis thorns shield reflecting damage upon enemy contact.
     * **Cleric:** Fires an emergency burst heal when Vanguard HP drops below 80%.
     * **Druid:** Applies consistent Heal-over-Time (HoT) regeneration.
     * **Commander:** Triggers a War Cry empowering Vanguard attack power.
     * **Chronomancer:** Directly shaves active cooldown seconds off Vanguard skills.

2. **5 Distinct Attacker Subroles:**
   * **Blade Dancer:** High mobility and minimal skill cooldown for rapid slashes.
   * **Ranger:** High-multiplier precision long-range shots against the closest target.
   * **Berserker:** Attack multiplier escalates drastically as current HP depletes.
   * **Spellcaster:** Wide-radius area-of-effect elemental burst circles.
   * **Artillery:** Heavy cannon fire delivering massive target knockback.

---

## 🕹️ Control Scheme & Keybindings

| Key / Input | Context | Action / Function |
| :--- | :--- | :--- |
| **W** | Combat Arena | Move Up |
| **A** | Combat Arena | Move Left |
| **S** | Combat Arena | Move Down |
| **D** | Combat Arena | Move Right |
| **Q** | Combat Arena | Cast Vanguard Subrole Skill |
| **R** | Combat Arena | Cast Vanguard Ultimate Burst |
| **B** | Combat Arena | Trigger Boss Encounter (when creeps cleared) |
| **ESC** | Combat Arena | Retreat to Lobby Hub |
| **1 - 5 (Digits)** | Party / Combat | Direct Hero Slot Swapping |
| **Left Click** | Global UI | Menu navigation, button triggers, squad selection |
| **Window Resize** | Global UI | Dynamic display scaling & responsive viewport |

---

## 🏗️ Clean Modular Architecture

```text
src/
├── App.java                                # Application entry point & frame launcher
│
├── engine/                                 # Runtime loop, physics, & controllers
│   ├── GameCanvas.java                     # Dynamic scaling canvas wrapper
│   ├── GameLoop.java                       # 60 FPS update-render thread loop
│   ├── GameState.java                      # Centralized state machine enum
│   ├── InputHandler.java                   # Keyboard & mouse event listener adapter
│   │
│   ├── canvas/                             # Combat runtime orchestration
│   │   ├── CombatEngine.java               # Collision lifecycle & combat rules
│   │   └── SupportResonanceEngine.java     # Off-field support resonance timers
│   │
│   └── handlers/                           # UI action delegation
│       ├── CanvasButtonManager.java        # Button component layout registry
│       ├── CanvasClickHandler.java         # Click routing by current state
│       ├── CanvasRenderer.java             # Screen renderer state dispatcher
│       └── GachaHandler.java               # Currency deduction & pull dispatcher
│
├── model/                                  # Domain models & entities
│   ├── npc/
│   │   └── Enemy.java                      # Creep & boss telegraph AI behavior
│   │
│   └── characters/                         # Hero entity definitions
│       ├── Character.java                  # Abstract base hero class
│       ├── Attacker.java                   # Vanguard active fighter entity
│       ├── Defender.java                   # Off-field defense resonance entity
│       ├── Healer.java                     # Off-field restoration resonance entity
│       ├── Support.java                    # Off-field utility resonance entity
│       │
│       └── skills/                         # Subrole combat executor classes
│           ├── SkillAction.java            # Active skill interface contract
│           ├── AttackerSkillExecutor.java  # 5 Attacker subrole algorithms
│           ├── DefenderSkillExecutor.java  # Guardian & Paladin resonance logic
│           ├── HealerSkillExecutor.java    # Cleric & Druid resonance logic
│           └── SupportSkillExecutor.java   # Commander & Chronomancer logic
│
├── service/                                # Business logic & persistence services
│   ├── GachaService.java                   # Probability rates & pity calculations
│   ├── PartyManager.java                   # Squad composition & cooldown ticks
│   ├── PlayerProfile.java                  # Currency balances & hero roster storage
│   ├── SaveManager.java                    # Delimiter-based file I/O (.save)
│   └── StageManager.java                   # Stage progression & clear criteria
│
└── view/                                   # Presentation & frontend view layer
    ├── component/                          # Reusable UI widgets & particles
    │   ├── DamageText.java                 # Floating combat text animator
    │   ├── MainHUD.java                    # Hero status, HP, and cooldown bars
    │   ├── UIButton.java                   # Interactive button with hover feedback
    │   └── VFXEffect.java                  # Particle & burst visual rings
    │
    └── screen/                             # Fullscreen rendering views
        ├── TitleScreen.java                # Title screen
        ├── ProfileSelectScreen.java        # Profile selection & creation modal
        ├── LobbyScreen.java                # Command hub & training portal
        ├── GachaScreen.java                # Hero recruitment banner portal
        ├── PartyScreen.java                # Squad slot management view
        ├── PartyInventoryRenderer.java     # Inventory grid visual renderer
        ├── BattleScreen.java               # Active arena combat view
        ├── SettingsScreen.java             # Graphic presets & manual save UI
        └── ResultScreen.java               # Victory & defeat summary screen
