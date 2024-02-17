package content.startingzone;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.game.worldobject.WorldObjectInteractionHandler;

public class ContentStartingZoneGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(SpawnSomeTestNpcs.class).asEagerSingleton();

        var worldObjectInteractionHandler = Multibinder.newSetBinder(binder(), WorldObjectInteractionHandler.class);
        worldObjectInteractionHandler.addBinding().to(CoinTableWorldObjectInteractionHandler.class);
        worldObjectInteractionHandler.addBinding().to(TreeWorldObjectInteractionHandler.class);
    }

}
