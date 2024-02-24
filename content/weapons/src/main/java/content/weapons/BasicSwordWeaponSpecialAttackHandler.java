package content.weapons;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.special.WeaponSpecialAttackHandler;

@Singleton
public class BasicSwordWeaponSpecialAttackHandler implements WeaponSpecialAttackHandler {

    @Override
    public String getItemId() {
        return "basic_sword";
    }

    @Override
    public boolean attack(CombatComponent attacker, CombatComponent victim) {
        var chatOptional = attacker.getEntity().getComponent(PlayerChatComponent.class);
        chatOptional.ifPresent(playerChatComponent -> playerChatComponent.sendGameMessage("YOO A LONGBOI ATTACK"));
        return true;
    }
}
