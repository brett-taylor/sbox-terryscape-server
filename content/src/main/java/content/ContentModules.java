package content;

import com.google.inject.AbstractModule;
import content.commands.ContentCommandsGuiceModule;
import content.interfaces.ContentInterfacesGuiceModule;
import content.startingzone.ContentStartingZoneGuiceModule;
import content.weapons.ContentWeaponsGuiceModule;

import java.util.Set;

public class ContentModules {

    public static Set<AbstractModule> getContentModules() {
        return Set.of(
            new ContentCommandsGuiceModule(),
            new ContentStartingZoneGuiceModule(),
            new ContentInterfacesGuiceModule(),
            new ContentWeaponsGuiceModule()
        );
    }
}