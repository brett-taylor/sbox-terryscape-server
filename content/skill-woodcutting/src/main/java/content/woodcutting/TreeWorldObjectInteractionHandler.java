package content.woodcutting;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.world.WorldObjectDefinition;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.sound.SoundManager;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.LookAtTaskStep;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.game.worldobject.WorldObjectInteractionHandler;
import com.terryscape.net.Client;
import com.terryscape.util.EnumValueRetriever;

import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class TreeWorldObjectInteractionHandler implements WorldObjectInteractionHandler {

    private final EnumValueRetriever enumValueRetriever;

    private final CacheLoader cacheLoader;

    private final SoundManager soundManager;

    @Inject
    public TreeWorldObjectInteractionHandler(EnumValueRetriever enumValueRetriever, CacheLoader cacheLoader, SoundManager soundManager) {
        this.enumValueRetriever = enumValueRetriever;
        this.cacheLoader = cacheLoader;
        this.soundManager = soundManager;
    }

    @Override
    public Set<String> getObjectIds() {
        return enumValueRetriever
            .getEnumValues(Tree.class).stream()
            .map(Tree::getTreeObjectId)
            .collect(Collectors.toSet());
    }

    @Override
    public void invoke(Client client, WorldObjectDefinition worldObjectDefinition) {
        var player = client.getPlayer().orElseThrow();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);

        var destination = worldObjectDefinition.getWorldCoordinate().getClosestCardinalNeighbourFrom(playerMovement.getWorldCoordinate());

        var tree = enumValueRetriever.getEnumValues(Tree.class).stream()
            .filter(t -> t.getTreeObjectId().equals(worldObjectDefinition.getObjectDefinition().getId()))
            .findFirst()
            .orElseThrow();

        playerTask.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(playerMovement, destination),

            LookAtTaskStep.worldCoordinate(playerMovement, worldObjectDefinition.getWorldCoordinate()),

            new CutWoodTaskStep(player, tree, cacheLoader, soundManager)
        );
    }
}