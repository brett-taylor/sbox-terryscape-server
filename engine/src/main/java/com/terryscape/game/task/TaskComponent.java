package com.terryscape.game.task;

import com.terryscape.entity.component.EntityComponent;
import com.terryscape.game.task.step.Step;

import java.util.Optional;

public interface TaskComponent extends EntityComponent {

    Optional<Task> setPrimaryTask(Step... steps);

    Optional<Task> setCancellablePrimaryTask(Step... steps);

    boolean cancelPrimaryTask();

    boolean hasPrimaryTask();
}
