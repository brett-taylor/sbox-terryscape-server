package content.weapons;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.entity.VisualEffectFactory;
import com.terryscape.game.combat.special.WeaponSpecialAttackHandler;

public class ContentWeaponsGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(VisualEffectFactory.class).asEagerSingleton();
        var weaponSpecialAttackHandlerMultibinder = Multibinder.newSetBinder(binder(), WeaponSpecialAttackHandler.class);
        weaponSpecialAttackHandlerMultibinder.addBinding().to(BasicSwordWeaponSpecialAttackHandler.class);
        weaponSpecialAttackHandlerMultibinder.addBinding().to(BasicScimitarWeaponSpecialAttackHandler.class);
    }

}
