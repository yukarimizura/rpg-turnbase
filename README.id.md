<p align="center">
  <a href="README.md">English</a> | <b>Bahasa Indonesia</b>
</p>

---

# ⚔️ Cyber-Chrono ARPG Turn-Based Engine

Engine Action RPG 2D berbasis Java murni (Swing & Graphics2D) yang mengintegrasikan sistem pertempuran real-time hibrida: **Vanguard Arena Fighter** dan **Off-Field Support Resonance**. Proyek ini dirancang mengikuti prinsip modularitas Object-Oriented Programming (OOP) yang ketat (seluruh file berukuran di bawah 100 baris kode).

---

## 🧠 Analisis & Implementasi Algoritma

Untuk memenuhi standar evaluasi teknis, proyek ini mengimplementasikan algoritma komputasi mandiri tanpa library eksternal:

### 1. Collision Detection & Proximity Targeting (Euclidean Geometry)
* **Pendeteksian Tabrakan Lingkaran (Bounding Circle Collision):**
  Deteksi kontak antara entitas sirkular (Vanguard, Creep, dan Boss) dihitung menggunakan jarak Euclidean berbasis teorema Pythagoras:
  * **Rumus Jarak:** `Distance = sqrt((x2 - x1)^2 + (y2 - y1)^2)`
  * Tabrakan terjadi apabila `Distance < (radius1 + radius2)`.
* **Nearest-Neighbor Heuristic Targeting:**
  Digunakan oleh subperan *Ranger* untuk mendeteksi dan mengunci target musuh terdekat dengan kompleksitas waktu $O(N)$ sebelum melepaskan tembakan presisi.

### 2. Finite State Machine (Manajemen Siklus Permainan)
Transisi siklus permainan dikelola melalui FSM terpusat via `GameState` (`PROFILE_SELECT`, `TITLE`, `LOBBY`, `GACHA`, `PARTY`, `BATTLE`, `SETTINGS`, `VICTORY`, `GAME_OVER`). FSM menjamin transisi state bersifat deterministik dan mencegah tumpang tindih eksekusi antara pertempuran dan menu antarmuka.

### 3. Gacha Drop Rate & Dynamic Pity Counter Algorithm
Sistem roll gacha menggunakan pembangkit bilangan acak seragam (*Uniform Distribution Sampling*) dengan mekanisme proteksi bersyarat:
* **Base Pull Rates:** SSR = 2%, SR = 13%, R = 85%.
* **Pity Mechanism:**
  * **Soft Pity:** Setiap kelipatan 10 tarikan tanpa SSR dijamin memberikan minimal rank SR.
  * **Hard Pity:** Jika mencapai 50 tarikan (`pityCounter >= 50`), sistem mengunci probabilitas 100% untuk drop hero SSR dan melakukan reset penghitung ke 0.

### 4. Boss AI State Loop: Telegraphing & Burst Cycle
AI Boss mengadopsi mesin waktu bersiklus (*Time-driven State Cycle*):
1. **Chasing Phase:** Bos bergerak mengejar posisi Vanguard dengan vektor translasi ternormalisasi.
2. **Telegraph Charge Phase:** Kecepatan bos menjadi 0, dan lingkaran radius peringatan (*telegraph zone*) dirender di lantai arena selama durasi tertentu.
3. **AoE Burst Execution:** Saat waktu charge habis, kalkulasi area efek (AoE) dieksekusi secara instan. Entitas yang berada di dalam area menerima damage berlipat.

### 5. Flat-File Serialization Parsing (`.save`)
Mekanisme penyimpanan data lokal mandiri tanpa ketergantungan JSON eksternal menggunakan pemindaian stream berbasis pemisah (*delimiter* `;`):
* Membaca baris secara berurutan menggunakan `BufferedReader`.
* Memisahkan baris *header* (profil akun, mata uang, progres stage, penghitung pity) dari baris entitas dinamis (`HERO;<id>;<role>;...`).
* Mengonstruksi ulang objek hero secara dinamis via polimorfisme saat proses pemuatan file.

### 6. Dynamic Scaling Matrix (Virtual Screen Mapping)
Kanvas beroperasi pada resolusi virtual native `950 x 600`. Algoritma penyekalaan memetakan koordinat fisik monitor/jendela ke ruang koordinat virtual secara real-time:
* **X_virtual** = `X_mouse / Scale_X`
* **Y_virtual** = `Y_mouse / Scale_Y`

Perhitungan ini menjamin koordinat tombol dan rendering grafik tetap presisi saat jendela di-drag, di-maximize, atau diatur melalui menu resolusi.

---

## 🎮 Fitur Mekanik Permainan

1. **Sistem Vanguard & Resonance:**
   * **Vanguard (Slot 1):** Khusus untuk tipe **Attacker**. Merupakan karakter utama yang dikontrol langsung di arena.
   * **Resonance Squad (Slot 2–5):** Diisi oleh **Defender**, **Healer**, dan **Support**. Mereka berada di luar arena (*off-field*) dan memicu pasif secara berkala:
     * **Guardian:** Memberikan perisai flat penahan damage.
     * **Paladin:** Memberikan perisai duri (*thorns*) yang memantulkan damage saat musuh menabrak.
     * **Cleric:** Memberikan pemulihan HP burst instan saat darah Vanguard berada di bawah 80%.
     * **Druid:** Memberikan regenerasi HP bertahap (HoT) secara berkelanjutan.
     * **Commander:** Memberikan teriakan perang (+ATK boost tim).
     * **Chronomancer:** Memotong waktu cooldown skill aktif Vanguard.

2. **5 Subperan Attacker:**
   * **Blade Dancer:** Kecepatan gerak tinggi dengan cooldown skill singkat (spam tebasan).
   * **Ranger:** Tembakan terarah dengan damage tinggi ke musuh terdekat.
   * **Berserker:** Pengganda damage meningkat seiring semakin rendahnya sisa HP.
   * **Spellcaster:** Serangan sihir area luas (AoE burst).
   * **Artillery:** Serangan meriam berdaya dorong (*knockback*) menjauhkan musuh.

---

## 🕹️ Skema Kontrol & Input (Keybindings)

| Tombol / Kontrol | Cakupan Mode | Aksi / Fungsi |
| :--- | :--- | :--- |
| **W** | Pertempuran | Bergerak ke Atas |
| **A** | Pertempuran | Bergerak ke Kiri |
| **S** | Pertempuran | Bergerak ke Bawah |
| **D** | Pertempuran | Bergerak ke Kanan |
| **Q** | Pertempuran | Melancarkan Subrole Skill Vanguard |
| **R** | Pertempuran | Melancarkan Ultimate Burst Vanguard |
| **B** | Pertempuran | Menantang Boss Stage (saat creep sudah bersih) |
| **ESC** | Pertempuran | Kembali ke Lobby Hub |
| **1 - 5 (Angka)** | Tim / Pertempuran | Menukar slot pahlawan secara instan |
| **Left Click (Mouse)** | Seluruh Antarmuka | Navigasi menu, eksekusi tombol, & pemilihan slot tim |
| **Window Resize** | Seluruh Antarmuka | Penskalaan otomatis layar secara dinamis |

---

## 🏗️ Struktur Arsitektur Direktori

```text
src/
├── App.java                                # Entry point aplikasi & inisialisasi window
│
├── engine/                                 # Kernel loop & sistem kontrol
│   ├── GameCanvas.java                     # Swing canvas & transformer penskalaan
│   ├── GameLoop.java                       # Fixed 60 FPS update-render thread loop
│   ├── GameState.java                      # Enumerasi State Machine
│   ├── InputHandler.java                   # Keyboard & Mouse event listener adapter
│   │
│   ├── canvas/                             # Logika orkestrasi pertempuran
│   │   ├── CombatEngine.java               # Engine deteksi kontak & siklus bertarung
│   │   └── SupportResonanceEngine.java     # Engine timer auto-trigger off-field support
│   │
│   └── handlers/                           # Pendelegasian event UI
│       ├── CanvasButtonManager.java        # Manajemen tombol & koordinat interaktif
│       ├── CanvasClickHandler.java         # Handler percabangan klik antarmuka
│       ├── CanvasRenderer.java             # Dispatcher perenderan tampilan layar
│       └── GachaHandler.java               # Pengendali transaksi & rolling hero
│
├── model/                                  # Entitas objek & pemodelan peran
│   ├── npc/
│   │   └── Enemy.java                      # Model kecerdasan creep & boss telegraph
│   │
│   └── characters/                         # Entitas karakter pahlawan
│       ├── Character.java                  # Abstract base class entitas hero
│       ├── Attacker.java                   # Karakter Vanguard aktif
│       ├── Defender.java                   # Karakter pertahanan resonansi
│       ├── Healer.java                     # Karakter pemulihan resonansi
│       ├── Support.java                    # Karakter buffer resonansi
│       │
│       └── skills/                         # Logika eksekusi subperan terisolasi
│           ├── SkillAction.java            # Interface kemampuan skill
│           ├── AttackerSkillExecutor.java  # Eksekutor 5 subperan Attacker
│           ├── DefenderSkillExecutor.java  # Eksekutor Guardian & Paladin
│           ├── HealerSkillExecutor.java    # Eksekutor Cleric & Druid
│           └── SupportSkillExecutor.java   # Eksekutor Commander & Chronomancer
│
├── service/                                # Layanan bisnis, state, & persistensi
│   ├── GachaService.java                   # Logika probabilitas acak & pity counter
│   ├── PartyManager.java                   # Validasi komposisi tim & cooldown hero
│   ├── PlayerProfile.java                  # Model akun, saldo koin/gems, & roster
│   ├── SaveManager.java                    # Pembacaan & penulisan stream berkas .save
│   └── StageManager.java                   # Pengatur progres stage & pembuka boss
│
└── view/                                   # Lapisan presentasi & visual UI
    ├── component/                          # Komponen grafis reusable
    │   ├── DamageText.java                 # Animasi angka damage/heal melayang
    │   ├── MainHUD.java                    # Status bar HP/EXP/Cooldown di arena
    │   ├── UIButton.java                   # Komponen tombol interaktif dengan hover
    │   └── VFXEffect.java                  # Animasi visual partikel melingkar
    │
    └── screen/                             # Layar UI permainan
        ├── TitleScreen.java                # Layar judul
        ├── ProfileSelectScreen.java        # Layar seleksi profil & input komandan baru
        ├── LobbyScreen.java                # Hub komandan & arena training
        ├── GachaScreen.java                # Antarmuka pemanggilan pahlawan
        ├── PartyScreen.java                # Antarmuka formasi slot 1-5
        ├── PartyInventoryRenderer.java     # Grid visual koleksi inventaris hero
        ├── BattleScreen.java               # Arena pertarungan real-time
        ├── SettingsScreen.java             # Menu resolusi & tombol simpan manual
        └── ResultScreen.java               # Ringkasan hasil pertarungan
