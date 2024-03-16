package content.startingzone;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.game.loottable.LootTableProvider;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.worldobject.WorldObjectInteractionHandler;
import content.startingzone.npchandlers.CombatExpertNpcInteractionHandler;
import content.startingzone.npchandlers.GuideNpcInteractionHandler;
import content.startingzone.npchandlers.PlayerOnlineCounterNpcInteractionHandler;
import content.startingzone.npchandlers.ShopKeeperNpcInteractionHandler;
import content.startingzone.npcs.GoblinLootTableProvider;
import content.startingzone.npcs.SpawnGoblins;
import content.startingzone.npcs.SpawnHumans;
import content.startingzone.worldobjecthandlers.CoinTableWorldObjectInteractionHandler;

public class ContentStartingZoneGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(SpawnGoblins.class).asEagerSingleton();
        binder().bind(SpawnHumans.class).asEagerSingleton();

        var worldObjectInteractionHandlerMultibinder = Multibinder.newSetBinder(binder(), WorldObjectInteractionHandler.class);
        worldObjectInteractionHandlerMultibinder.addBinding().to(CoinTableWorldObjectInteractionHandler.class);

        var npcInteractionHandlerMultibinder = Multibinder.newSetBinder(binder(), NpcInteractionHandler.class);
        npcInteractionHandlerMultibinder.addBinding().to(ShopKeeperNpcInteractionHandler.class);
        npcInteractionHandlerMultibinder.addBinding().to(GuideNpcInteractionHandler.class);
        npcInteractionHandlerMultibinder.addBinding().to(PlayerOnlineCounterNpcInteractionHandler.class);
        npcInteractionHandlerMultibinder.addBinding().to(CombatExpertNpcInteractionHandler.class);

        var lootTableProviderMultibinder = Multibinder.newSetBinder(binder(), LootTableProvider.class);
        lootTableProviderMultibinder.addBinding().to(GoblinLootTableProvider.class);
    }

}
