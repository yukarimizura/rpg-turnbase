package service;

import model.characters.Character;
import model.characters.Attacker;
import model.characters.Defender;
import model.characters.Healer;
import model.characters.Support;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    private static final String SAVE_DIR = "saves";

    public static void ensureDirectory() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static List<String> listSavedProfiles() {
        ensureDirectory();
        List<String> list = new ArrayList<>();
        File dir = new File(SAVE_DIR);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".save"));
        if (files != null) {
            for (File f : files) {
                String name = f.getName().replace(".save", "");
                list.add(name);
            }
        }
        return list;
    }

    public static boolean saveGame(PlayerProfile profile, PartyManager party, StageManager stage, GachaService gacha) {
        ensureDirectory();
        File file = new File(SAVE_DIR, profile.getPlayerName() + ".save");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Header: PlayerName, Gems, Credits, Stage, Pity
            writer.println(profile.getPlayerName() + ";" + profile.getCyberGems() + ";" + profile.getCredits() + ";" + stage.getCurrentStage() + ";" + gacha.getPityCounter());
            
            // Owned Heroes List
            for (Character c : profile.getOwnedHeroes()) {
                writer.println("HERO;" + c.getId() + ";" + c.getName() + ";" + c.getRarity() + ";" + c.getSubRole() + ";" + c.getRole() + ";" + c.getLevel() + ";" + c.getMaxHp() + ";" + c.getAttack() + ";" + c.getDefense());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean loadGame(String username, PlayerProfile profile, PartyManager party, StageManager stage, GachaService gacha) {
        File file = new File(SAVE_DIR, username + ".save");
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            if (header == null) return false;
            String[] hData = header.split(";");
            
            // Reset player data
            profile.setPlayerName(hData[0]);
            profile.setGems(Integer.parseInt(hData[1]));
            profile.setCredits(Integer.parseInt(hData[2]));
            stage.setCurrentStage(Integer.parseInt(hData[3]));
            gacha.setPityCounter(Integer.parseInt(hData[4]));

            profile.getOwnedHeroes().clear();
            party.getActiveParty().clear();
            party.getInactiveParty().clear();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("HERO;")) {
                    String[] d = line.split(";");
                    String id = d[1], name = d[2], rarity = d[3], sub = d[4], role = d[5];
                    int level = Integer.parseInt(d[6]), hp = Integer.parseInt(d[7]), atk = Integer.parseInt(d[8]), def = Integer.parseInt(d[9]);

                    Character c;
                    if ("Attacker".equals(role)) {
                        c = new Attacker(id, name, rarity, sub, hp, atk, def);
                    } else if ("Defender".equals(role)) {
                        c = new Defender(id, name, rarity, sub, hp, atk, def);
                    } else if ("Healer".equals(role)) {
                        c = new Healer(id, name, rarity, sub, hp, atk, def);
                    } else {
                        c = new Support(id, name, rarity, sub, hp, atk, def);
                    }
                    
                    // Set level up stats
                    for (int lv = 1; lv < level; lv++) {
                        c.levelUpManual();
                    }

                    profile.addHero(c);
                    party.addHeroToParty(c);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}