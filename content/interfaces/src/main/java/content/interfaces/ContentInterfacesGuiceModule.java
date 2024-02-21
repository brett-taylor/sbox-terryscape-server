package content.interfaces;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.game.interfaces.InterfaceActionHandler;

public class ContentInterfacesGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        var interfaceActionHandlerMultibinder = Multibinder.newSetBinder(binder(), InterfaceActionHandler.class);
        interfaceActionHandlerMultibinder.addBinding().to(WelcomeScreenInterfaceActionHandler.class);
    }

}
