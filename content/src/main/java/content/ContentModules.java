package content;

import com.google.inject.AbstractModule;
import content.commands.ContentCommandsGuiceModule;
import content.fishing.ContentSkillFishingGuiceModule;
import content.food.ContentFoodGuiceModule;
import content.interfaces.ContentInterfacesGuiceModule;
import content.mining.ContentSkillMiningGuiceModule;
import content.startingzone.ContentStartingZoneGuiceModule;
import content.weapons.ContentWeaponsGuiceModule;
import content.woodcutting.ContentSkillWoodcuttingGuiceModule;

import java.util.Set;

public class ContentModules {

    public static Set<AbstractModule> getContentModules() {
        return Set.of(
            new ContentCommandsGuiceModule(),
            new ContentStartingZoneGuiceModule(),
            new ContentInterfacesGuiceModule(),
            new ContentFoodGuiceModule(),
            new ContentWeaponsGuiceModule(),
            new ContentSkillFishingGuiceModule(),
            new ContentSkillMiningGuiceModule(),
            new ContentSkillWoodcuttingGuiceModule()
        );
    }
}