# ⚔️ Cyber-Chrono ARPG Turn-Based Engine

Engine RPG berbasis Java murni (Swing/Java 2D) yang mengusung mekanik aksi real-time hibrida dengan kombinasi **Vanguard Arena Fighter** dan **Off-Field Support Resonance**. Dibangun dengan arsitektur berorientasi objek (OOP) modular yang mematuhi standar *clean architecture* (setiap file berada di bawah 100 baris kode).

---

## 🎮 Fitur Utama Game

### 1. Sistem Vanguard & Off-Field Resonance
* **Vanguard (Active Arena Player):** Karakter utama di arena yang digerakkan secara manual (WASD + Mouse/Skill/Ult). Hanya karakter ber-tipe **Attacker** yang dapat mengisi slot ini.
* **Resonance Squad (Off-Field Auto-Trigger):** 4 slot pendukung diisi oleh **Defender**, **Healer**, dan **Support**. Mereka tidak turun langsung ke arena, melainkan memicu skill pasif berjangka (*auto-trigger resonance*) ke Vanguard:
  * **Defender:** Memicu *Guardian Shield* (penyerapan damage flat) atau *Paladin Thorns Aegis* (pantulan damage kontak musuh).
  * **Healer:** Memicu *Cleric Grace* (pemulihan burst instan) atau *Druid Renewal* (regenerasi berkelanjutan / HoT) saat HP Vanguard menurun.
  * **Support:** Memicu *Commander War Cry* (+ATK boost tim) atau *Chronomancer Recharge* (akselerasi pemotongan cooldown skill).

### 2. Variasi Subrole Attacker Unik
Attacker terbagi ke dalam 5 subrole dengan gaya tempur yang berbeda:
1. **Blade Dancer:** Kecepatan tinggi dengan skill cooldown instan untuk spam serangan tebasan cepat.
2. **Ranger:** Serangan tembakan terarah dengan damage single-target tinggi ke musuh terdekat.
3. **Berserker:** Damage serangan meningkat secara drastis seiring menipisnya sisa HP karakter.
4. **Spellcaster:** Serangan sihir area (AoE burst) melingkar dengan radius jangkauan luas.
5. **Artillery:** Serangan meriam yang memberikan efek knockback masif untuk menjauhkan kerumunan musuh.

### 3. Guaranteed Starter Hero & Gacha System
* **Guaranteed Starter:** Pemain baru dijamin langsung mendapatkan 1 Vanguard Attacker (Blade Dancer) secara gratis agar dapat langsung berburu monster.
* **Gacha Portal:** Sistem roll hero 1x (160 Gems) dan 10x (1600 Gems) dengan mekanisme **Pity System** (garansi SSR pada roll ke-50 dan rate-up SR pada kelipatan ke-10).

### 4. Roster & Squad Management
* Mengelola susunan 5 hero aktif (1 Vanguard + 4 Resonance Supports).
* Inventaris lengkap menampilkan seluruh koleksi hero hasil gacha dengan sistem tukar slot instan (*select slot & equip*).

### 5. Multi-Profile & Local Persistence (Save/Load)
* **Profile Select:** Pemilihan profil sebelum masuk ke permainan utama (`GameState.PROFILE_SELECT`).
* **Auto-Save & Manual Save:** Seluruh mata uang (Gems & Credits), stage progress, status pity, serta inventaris hero tersimpan ke berkas lokal di folder `saves/<nama_commander>.save`.

### 6. Dynamic Scaling Engine & Settings
* Tampilan antarmuka menggunakan **`AffineTransform` Scaling** (rasio kanvas dasar 950x600).
* Ukuran jendela dapat di-drag resize secara bebas atau dimaksimalkan tanpa membuat resolusi gambar pecah atau koordinat tombol meleset.
* Pengaturan resolusi preset (Default 950x600 & HD 1280x720) tersedia di menu **Settings**.

---

## 🏗️ Struktur Arsitektur Direktori

Proyek ini menggunakan modularisasi MVC ketat dengan pemisahan tanggung jawab yang jelas:

```text
src/
├── App.java                                # Titik masuk aplikasi (Window launcher)
│
├── engine/                                 # Inti eksekusi & loop permainan
│   ├── GameCanvas.java                     # Canvas Swing & dynamic resolution scaler
│   ├── GameLoop.java                       # Fixed 60 FPS update-render loop
│   ├── GameState.java                      # State Machine enum permainan
│   ├── InputHandler.java                   # Adapter kontrol keyboard & mouse
│   │
│   ├── canvas/
│   │   ├── CombatEngine.java               # Orkestrasi siklus tempur & deteksi tabrakan
│   │   └── SupportResonanceEngine.java     # Pengatur interval auto-trigger off-field support
│   │
│   └── handlers/
│       ├── CanvasButtonManager.java        # Instansiasi & layouting tombol antarmuka
│       ├── CanvasClickHandler.java         # Router pendelegasian event klik mouse
│       ├── CanvasRenderer.java             # Router render canvas berdasarkan GameState
│       └── GachaHandler.java               # Handler transaksi gacha & konsumsi gems
│
├── model/                                  # Entitas data & model peran
│   ├── npc/
│   │   └── Enemy.java                      # AI musuh biasa & Boss telegraph burst
│   │
│   └── characters/
│       ├── Character.java                  # Abstract base class seluruh hero
│       ├── Attacker.java                   # Entitas hero vanguard aktif
│       ├── Defender.java                   # Entitas hero pertahanan off-field
│       ├── Healer.java                     # Entitas hero pemulihan HP off-field
│       ├── Support.java                    # Entitas hero buffer off-field
│       │
│       └── skills/                         # Pemisahan logika skill setiap subrole
│           ├── SkillAction.java            # Interface kontrak skill aktif
│           ├── AttackerSkillExecutor.java  # Eksekutor skill 5 subrole attacker
│           ├── DefenderSkillExecutor.java  # Eksekutor shield & thorns resonance
│           ├── HealerSkillExecutor.java    # Eksekutor burst heal & renewal resonance
│           └── SupportSkillExecutor.java   # Eksekutor atk buff & cooldown reduction
│
├── service/                                # Logika bisnis, ekonomi, & penyimpanan
│   ├── GachaService.java                   # Algoritma probabilitas & perhitungan pity
│   ├── PartyManager.java                   # Manajemen komposisi squad & cooldown global
│   ├── PlayerProfile.java                  # Model data akun pemain (Gems, Credits, Heroes)
│   ├── SaveManager.java                    # Modul baca/tulis file simpanan (.save)
│   └── StageManager.java                   # Progres creep count & pembuka stage boss
│
└── view/                                   # Lapisan presentasi & visual (Frontend)
    ├── component/                          # Komponen UI modular & efek visual
    │   ├── DamageText.java                 # Floating text animasi angka damage/heal
    │   ├── MainHUD.java                    # HUD bar status hero & cooldown skill
    │   ├── UIButton.java                   # Komponen tombol interaktif dengan efek hover
    │   └── VFXEffect.java                  # Partikel efek lingkaran sihir & ledakan
    │
    └── screen/                             # Layar tampilan antarmuka
        ├── TitleScreen.java                # Layar judul pembuka
        ├── ProfileSelectScreen.java        # Layar pemilihan & pembuatan profil
        ├── LobbyScreen.java                # Menu utama hub komandan
        ├── GachaScreen.java                # Portal roll hero banner
        ├── PartyScreen.java                # Layar formasi vanguard & tim
        ├── PartyInventoryRenderer.java     # Perender grid inventaris hero cadangan
        ├── BattleScreen.java               # Arena arena tempur utama
        ├── SettingsScreen.java             # Menu pengaturan grafis & simpan manual
        └── ResultScreen.java               # Layar ringkasan Victory & Game Over
