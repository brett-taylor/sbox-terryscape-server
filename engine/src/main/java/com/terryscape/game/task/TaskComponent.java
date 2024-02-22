package com.terryscape.game.task;

import com.terryscape.entity.component.EntityComponent;
import com.terryscape.game.task.step.TaskStep;

import java.util.Optional;

public interface TaskComponent extends EntityComponent {

    Optional<Task> setPrimaryTask(TaskStep... taskSteps);

    Optional<Task> setCancellablePrimaryTask(TaskStep... taskSteps);

    boolean cancelPrimaryTask();

    boolean hasPrimaryTask();

    Optional<Task> getPrimaryTask();
}
