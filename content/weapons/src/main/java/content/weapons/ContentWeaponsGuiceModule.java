package content.weapons;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.game.specialattack.SpecialAttackHandler;

public class ContentWeaponsGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        var specialAttackHandlerMultibinder = Multibinder.newSetBinder(binder(), SpecialAttackHandler.class);
        specialAttackHandlerMultibinder.addBinding().to(GodswordEvilSpecialAttackHandler.class);
        specialAttackHandlerMultibinder.addBinding().to(GodswordRighteousSpecialAttackHandler.class);
    }

}
