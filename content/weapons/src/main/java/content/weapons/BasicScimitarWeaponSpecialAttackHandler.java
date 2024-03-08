package content.weapons;

import com.google.inject.Singleton;
import com.terryscape.entity.VisualEffectFactory;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.SpecialBar;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.DamageType;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.combat.special.WeaponSpecialAttackHandler;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;

@Singleton
public class BasicScimitarWeaponSpecialAttackHandler implements WeaponSpecialAttackHandler {
    private String animationName = "Unarmed_Attack_Kick_R1";
    private int energyCost = 5;
    private DamageType damageType = DamageType.WATER;

    @Override
    public String getItemId() {
        return "basic_scimitar";
    }

    @Override
    public boolean attack(CombatComponent attacker, CombatComponent victim) {
        var chatOptional = attacker.getEntity().getComponent(PlayerChatComponent.class);
        chatOptional.ifPresent(playerChatComponent -> playerChatComponent.sendGameMessage("YOO SCIMMY ATTACK"));

        var special = attacker.getEntity().getComponentOrThrow(SpecialBar.class);

        var hasSpecial = special.canUse(energyCost);
        if(!hasSpecial)
            return false;

        special.use(energyCost);

        var damageInformation = new DamageInformation()
            .setType(damageType)
            .setIsUsingMainHand(true)
            .setAmount(50)
            .setHit(true);

        victim.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(damageInformation);
        attacker.getEntity().getComponentOrThrow(AnimationComponent.class).playAnimation(animationName);

        var imgUrl = "box.vmdl";

        /*
        var attackerParticle = VisualEffectFactory.CreateParticle();
        var imgUrl = "https://www.pngall.com/wp-content/uploads/14/Blue-Circle-Transparent.png";
        attackerParticle.setTarget(victim.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate());
        attackerParticle.setImageUrl(imgUrl);
        attackerParticle.setDuration(2);
*/
        var attackerProjectile = VisualEffectFactory.CreateProjectile();
        var attackerPosition = attacker.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();
        var victimPosition = victim.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();
        var distanceBetweenThem = attackerPosition.distance(victimPosition);

        attackerProjectile.setSource(attackerPosition);
        attackerProjectile.setTarget(victimPosition);
        attackerProjectile.setImageUrl(imgUrl);
        attackerProjectile.setDuration((int)distanceBetweenThem); //Assuming a projectile moves 2 tiles a second.

        return true;
    }

}
