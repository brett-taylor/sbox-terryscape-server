package com.terryscape.game.task;

import com.terryscape.entity.component.EntityComponent;
import com.terryscape.game.task.step.Step;

public interface TaskComponent extends EntityComponent {

    Task setTask(Step... steps);

}
