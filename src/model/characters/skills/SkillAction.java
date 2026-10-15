package model.characters.skills;

import model.npc.Enemy;
import java.util.List;
import model.characters.skills.SkillAction;

public interface SkillAction {
    void castSkill(List<Enemy> enemies);
    void castUltimate(List<Enemy> enemies);
}