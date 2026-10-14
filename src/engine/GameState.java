package engine;

public enum GameState {
    TITLE,          // Layar pembuka / Start Game
    LOBBY,          // Hub Markas Pemain (Akses ke Battle, Gacha, Party)
    GACHA,          // Layar khusus Banner Summon & Animasi Kartu
    PARTY,          // Layar Formasi 5 Karakter & Detail Status
    BATTLE,         // Arena Pertarungan Real-Time WASD 60 FPS
    VICTORY,        // Layar Menang Stage
    GAME_OVER       // Layar Kalah
}