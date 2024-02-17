package content.startingzone;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.game.worldobject.WorldObjectInteractionHandler;
import content.startingzone.spawnnpcs.SpawnGoblins;
import content.startingzone.spawnnpcs.SpawnHumans;
import content.startingzone.worldobjecthandlers.CoinTableWorldObjectInteractionHandler;
import content.startingzone.worldobjecthandlers.TreeWorldObjectInteractionHandler;

public class ContentStartingZoneGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(SpawnGoblins.class).asEagerSingleton();
        binder().bind(SpawnHumans.class).asEagerSingleton();

        var worldObjectInteractionHandler = Multibinder.newSetBinder(binder(), WorldObjectInteractionHandler.class);
        worldObjectInteractionHandler.addBinding().to(CoinTableWorldObjectInteractionHandler.class);
        worldObjectInteractionHandler.addBinding().to(TreeWorldObjectInteractionHandler.class);
    }

}
