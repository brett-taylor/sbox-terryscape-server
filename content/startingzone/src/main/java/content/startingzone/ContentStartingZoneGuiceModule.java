package content.startingzone;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.game.chat.command.Command;

public class ContentStartingZoneGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(SpawnSomeTestNpcs.class).asEagerSingleton();
    }

}
