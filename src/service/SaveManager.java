package service;

import model.characters.Character;
import service.save.HeroSaveSerializer;

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
                list.add(f.getName().replace(".save", ""));
            }
        }
        return list;
    }

    public static boolean saveGame(PlayerProfile profile, PartyManager party, StageManager stage, GachaService gacha) {
        ensureDirectory();
        File file = new File(SAVE_DIR, profile.getPlayerName() + ".save");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Header: PlayerName, Gems, Credits, Stage, Pity
            writer.println(profile.getPlayerName() + ";" + profile.getCyberGems() + ";"
                    + profile.getCredits() + ";" + stage.getCurrentStage() + ";" + gacha.getPityCounter());

            // Owned Heroes List
            for (Character c : profile.getOwnedHeroes()) {
                writer.println(HeroSaveSerializer.serializeHero(c));
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
                    Character c = HeroSaveSerializer.deserializeHero(line);
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