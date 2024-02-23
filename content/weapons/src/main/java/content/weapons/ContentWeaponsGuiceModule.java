package content.weapons;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.game.combat.special.WeaponSpecialAttackHandler;
import com.terryscape.game.interfaces.InterfaceActionHandler;

public class ContentWeaponsGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        var weaponSpecialAttackHandlerMultibinder = Multibinder.newSetBinder(binder(), WeaponSpecialAttackHandler.class);
        weaponSpecialAttackHandlerMultibinder.addBinding().to(BasicSwordWeaponSpecialAttackHandler.class);
        weaponSpecialAttackHandlerMultibinder.addBinding().to(BasicScimitarWeaponSpecialAttackHandler.class);
    }

}
