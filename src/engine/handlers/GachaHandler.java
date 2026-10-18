package engine.handlers;

import model.characters.Character;
import service.GachaService;
import service.PartyManager;
import service.PlayerProfile;
import view.screen.GachaScreen;

public class GachaHandler {
    private String lastNotice = "";

    public void handlePull(int count, int cost, PlayerProfile profile, GachaService gacha, PartyManager party) {
        if (!profile.spendGems(cost)) {
            lastNotice = "Cyber Gems tidak mencukupi! (Butuh " + cost + " Gems)";
            return;
        }
        gacha.startNewBatch();
        Character last = null;
        int ssrCount = 0, srCount = 0;
        for (int i = 0; i < count; i++) {
            last = gacha.roll();
            if ("SSR".equals(last.getRarity())) ssrCount++;
            else if ("SR".equals(last.getRarity())) srCount++;
            profile.addHero(last);
            party.addHeroToParty(last);
        }
        GachaScreen.triggerSummonAnimation();
        if (count == 1 && last != null) {
            lastNotice = "Berhasil Summon 1x: " + last.getName() + " [" + last.getRarity() + " - " + last.getRole() + "]!";
        } else if (count > 1) {
            lastNotice = String.format("Berhasil Summon %dx! (SSR: %d | SR: %d | R: %d)", count, ssrCount, srCount, count - ssrCount - srCount);
        }
    }

    public String getLastNotice() {
        return lastNotice;
    }
}