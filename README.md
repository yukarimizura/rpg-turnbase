# ⚔️ Cyber-Chrono ARPG Turn-Based Engine

Engine Action RPG 2D berbasis Java murni (Swing & Graphics2D) yang mengintegrasikan mekanik pertempuran real-time hibrida: **Vanguard Active Fighter** dan **Off-Field Support Resonance**. Proyek ini dirancang mengikuti prinsip modularitas Object-Oriented Programming (OOP) yang ketat (semua kelas dan modul terfragmentasi di bawah 100 baris kode).

---

## 🧠 Analisis & Implementasi Algoritma

Untuk memenuhi standar evaluasi teknis, proyek ini mengimplementasikan beberapa algoritma komputasi mandiri tanpa library eksternal:

### 1. Collision Detection & Proximity Targetting (Euclidean Geometry)
* **Pendeteksian Tabrakan Lingkaran (Bounding Circle Collision):**
  Menggunakan perhitungan jarak Euclidean berbasis teorema Pythagoras untuk mendeteksi kontak antara entitas (Vanguard, Creep, dan Boss Hitbox):
  $$\text{Distance} = \sqrt{(x_2 - x_1)^2 + (y_2 - y_1)^2}$$
  Kondisi tabrakan terpenuhi jika $\text{Distance} < (r_1 + r_2)$.
* **Pencarian Target Terdekat (Nearest-Neighbor Heuristic):**
  Digunakan pada subperan *Ranger* untuk memilih target tunggal musuh secara instan dengan kompleksitas waktu $O(N)$ sebelum melepaskan tembakan presisi.

### 2. State Machine Pattern (Manajemen Siklus Permainan)
Mengimplementasikan *Finite State Machine (FSM)* terpusat via `GameState` (`PROFILE_SELECT`, `TITLE`, `LOBBY`, `GACHA`, `PARTY`, `BATTLE`, `SETTINGS`, `VICTORY`, `GAME_OVER`). FSM menjamin transisi status game bersifat deterministik, mencegah tumpang tindih eksekusi antara logika pertempuran dan navigasi menu antarmuka.

### 3. Gacha Drop Rate & Dynamic Pity Counter Algorithm
Sistem roll gacha menggunakan pembangkit bilangan pseudo-acak (*Uniform Distribution PRNG*) dengan aturan probabilitas bersyarat:
* **Base Rates:** SSR = 2%, SR = 13%, R = 85%.
* **Soft/Hard Pity Mechanism:**
  * Setiap kelipatan 10 tarikan tanpa SSR dijamin memberikan minimal rank SR.
  * Pada tarikan ke-50 (`pityCounter >= 50`), sistem mengunci probabilitas 100% untuk drop hero SSR dan melakukan *reset* penghitung ke 0.

### 4. Boss AI State Loop: Telegraphing & Burst Cycle
AI Boss mengadopsi mesin waktu bersiklus (*Time-driven State Cycle*):
1. **Chasing Phase:** Bos mengejar posisi Vanguard dengan vektor translasi ternormalisasi.
2. **Telegraph Charge Phase:** Pada interval waktu tertentu, kecepatan bos menjadi 0 dan lingkaran radius bahaya (*telegraph zone*) dirender di lantai arena selama durasi *charge*.
3. **AoE Burst Execution:** Begitu waktu *charge* habis, kalkulasi area efek (AoE) dieksekusi secara instan. Pemain yang gagal menjauh dari radius akan menerima damage berlipat.

### 5. Flat-File Serialization Parsing (.save)
Algoritma persistensi data mandiri menggunakan pembacaan sekuensial berbasis *delimiter* (`;`):
* Menghindari overhead pustaka eksternal JSON dengan pemindaian baris stream `BufferedReader`.
* Struktur data memisahkan *header record* (informasi profil) dan *dynamic payload records* (`HERO;<id>;<role>;...`) yang direkonstruksi ulang menjadi instansiasi objek konkret via polimorfisme saat proses pemuatan (*deserialization*).

### 6. Dynamic Scaling Matrix (Virtual Screen Mapping)
Kanvas beroperasi dengan resolusi virtual native `950 x 600`. Algoritma penyekalaan memetakan koordinat fisik monitor/jendela ke ruang koordinat virtual secara real-time:
$$X_{\text{virtual}} = \frac{X_{\text{mouse}}}{\text{Scale}_X}, \quad Y_{\text{virtual}} = \frac{Y_{\text{mouse}}}{\text{Scale}_Y}$$
Menjamin akurasi klik tombol dan rendering antarmuka saat jendela diubah ukurannya (*dynamic resize / maximize*).

---

## 🎮 Fitur Mekanik Permainan

1. **Vanguard (Active) & Resonance (Off-field):**
   * **Vanguard (Slot 1):** Hanya dapat diisi oleh tipe **Attacker**. Karakter utama yang bertarung di arena.
   * **Support Resonance (Slot 2–5):** Diisi oleh **Defender**, **Healer**, dan **Support**. Mereka tidak turun ke lapangan, melainkan memicu kemampuan otomatis berkala (*auto-trigger passive*):
     * **Guardian:** Perisai serap damage (*Flat Shield Reduction*).
     * **Paladin:** Aura duri pelindung (*Thorns Reflect Shield*).
     * **Cleric:** Denyut pemulihan HP masif inst
