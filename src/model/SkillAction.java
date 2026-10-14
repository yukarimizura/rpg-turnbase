package model;

import java.util.List;

public interface SkillAction {
    void castSkill(List<Enemy> enemies);
    void castUltimate(List<Enemy> enemies);
}