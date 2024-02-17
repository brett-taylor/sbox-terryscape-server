package content.devtools;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.game.chat.command.Command;
import content.devtools.commands.*;

public class ContentDevToolsGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        var commandMultibinder = Multibinder.newSetBinder(binder(), Command.class);
        commandMultibinder.addBinding().to(GenderCommand.class);
        commandMultibinder.addBinding().to(SpawnItemCommand.class);
        commandMultibinder.addBinding().to(SetHealthCommand.class);
        commandMultibinder.addBinding().to(CoordinatesCommand.class);
        commandMultibinder.addBinding().to(AnimationCommand.class);
        commandMultibinder.addBinding().to(SetRunCommand.class);
    }

}
