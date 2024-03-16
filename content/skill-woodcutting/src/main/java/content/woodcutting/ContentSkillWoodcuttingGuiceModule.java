package content.woodcutting;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.game.worldobject.WorldObjectInteractionHandler;

public class ContentSkillWoodcuttingGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        var worldObjectInteractionHandlerMultibinder = Multibinder.newSetBinder(binder(), WorldObjectInteractionHandler.class);
        worldObjectInteractionHandlerMultibinder.addBinding().to(TreeWorldObjectInteractionHandler.class);
    }

}
