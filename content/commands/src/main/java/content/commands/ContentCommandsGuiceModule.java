package content.commands;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.game.chat.command.Command;

public class ContentCommandsGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        var commandMultibinder = Multibinder.newSetBinder(binder(), Command.class);
        commandMultibinder.addBinding().to(GenderCommand.class);
        commandMultibinder.addBinding().to(SpawnItemCommand.class);
        commandMultibinder.addBinding().to(SetHealthCommand.class);
        commandMultibinder.addBinding().to(CoordinatesCommand.class);
        commandMultibinder.addBinding().to(AnimationCommand.class);
        commandMultibinder.addBinding().to(SetRunCommand.class);
        commandMultibinder.addBinding().to(ItemStatsCommand.class);
        commandMultibinder.addBinding().to(NpcStatsCommand.class);
        commandMultibinder.addBinding().to(SkillsMaxCommand.class);
        commandMultibinder.addBinding().to(SkillsNoobCommand.class);
        commandMultibinder.addBinding().to(SkillsPureCommand.class);
        commandMultibinder.addBinding().to(MaxHitCommand.class);
    }

}
