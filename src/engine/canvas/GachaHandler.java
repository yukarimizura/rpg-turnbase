package engine.canvas;

import model.Character;
import service.GachaService;
import service.PartyManager;
import service.PlayerProfile;

public class GachaHandler {
    private String lastNotice = "";

    public void handlePull(int count, int cost, PlayerProfile profile, GachaService gacha, PartyManager party) {
        if (!profile.spendGems(cost)) {
            lastNotice = "Cyber Gems tidak mencukupi!";
            return;
        }
        Character last = null;
        for (int i = 0; i < count; i++) {
            last = gacha.roll();
            profile.addHero(last);
            if (!party.isPartyFull()) {
                party.addHeroToParty(last);
            }
        }
        if (last != null) {
            lastNotice = "Berhasil Summon: " + last.getName() + " [" + last.getRarity() + " " + last.getRole() + "]!";
        }
    }

    public String getLastNotice() {
        return lastNotice;
    }
}