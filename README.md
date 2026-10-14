# Cyber-Chrono: 2D ARPG Gacha Engine

Proyek game aksi 2D *real-time* berbasis Java SE murni yang mengimplementasikan prinsip-prinsip Pemrograman Berorientasi Objek (OOP) secara komprehensif. Game ini memadukan mekanik *gacha summoning*, manajemen formasi tim 5 karakter, *leveling/grinding progression*, serta pertarungan arena real-time berkecepatan 60 FPS menggunakan Java Swing dan Java 2D Engine bawaan[cite: 6, 7, 10, 17].

---


## Penerapan Pilar OOP

1. **Abstraction:**
   - `Character.java`: Abstract base class yang mendefinisikan atribut dasar hero (HP, ATK, DEF, EXP, posisi koordinat) dan mewajibkan implementasi visualisasi `draw()`[cite: 12].
   - `SkillAction.java`: Interface yang menentukan kontrak aksi tempur `castSkill()` dan `castUltimate()`[cite: 16].
2. **Inheritance:**
   - Subclass `Attacker`, `Defender`, dan `Healer` mewarisi seluruh kapabilitas gerak, status, dan pertahanan dari kelas induk `Character`[cite: 11, 12, 13, 15].
3. **Polymorphism:**
   - Setiap subclass memiliki implementasi unik pada efek visual dan jurus tempur:
     - `Attacker`: Jurus area tebasan jarak dekat ber-multiplier tinggi (*Crimson Slash*) dan serangan global (*Blade Storm*)[cite: 11].
     - `Defender`: Pertahanan fisik tebal dengan efek knockback musuh (*Shield Slam*) dan pemulihan perisai (*Fortress Aegis*)[cite: 13].
     - `Healer`: Pemulihan HP parsial dan penuh bagi unit tempur (*Holy Pulse* & *Resurrection Prayer*)[cite: 15].
4. **Encapsulation:**
   - Seluruh atribut status (`id`, `name`, `rarity`, `maxHp`, `attack`, `defense`) dienkapsulasi dengan akses *private* dan hanya dapat diakses melalui *getter/setter* serta method fungsional (`takeDamage`, `heal`, `gainExp`)[cite: 12].

---
