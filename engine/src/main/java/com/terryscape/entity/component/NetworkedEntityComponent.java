package com.terryscape.entity.component;

import java.io.OutputStream;

public interface NetworkedEntityComponent extends EntityComponent {

    String getComponentIdentifier();

    void writeEntityAddedPacket(OutputStream packet);

    void writeEntityUpdatedPacket(OutputStream packet);

}
