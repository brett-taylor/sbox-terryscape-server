package content.food;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.game.item.ItemInteractionHandler;

public class ContentFoodGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        var itemInteractionHandlerMultibinder = Multibinder.newSetBinder(binder(), ItemInteractionHandler.class);
        itemInteractionHandlerMultibinder.addBinding().to(FoodItemInteractionHandler.class);
    }

}
