package com.terryscape.game.combat;

import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class ParticlePacketComponent extends BaseEntityComponent implements NetworkedEntityComponent {

    public String getComponentIdentifier() {
        return "particle_added";
    }
    private EntityIdentifier target;
    private String imgUrl = "";
    private int duration = 0;
    public void setTarget(EntityIdentifier target){
        this.target = target;
    }
    public void setImageUrl(String img){
        imgUrl = img;
    }
    public void setDuration(int duration){
        this.duration = duration;
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {

        if(imgUrl.isEmpty()) return;

        target.writeToPacket(packet);
        OutgoingPacket.writeString(packet, imgUrl);
        OutgoingPacket.writeInt32(packet, duration);

        imgUrl = "";
    }

    public ParticlePacketComponent(Entity entity) {
        super(entity);
    }

}
