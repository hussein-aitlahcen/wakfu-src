package com.ankamagames.wakfu.common.game.personalSpace;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;

public abstract class PersonalSpace
{
    protected static final Logger m_logger;
    protected long m_ownerId;
    protected String m_ownerName;
    protected long m_guildId;
    protected RoomFactory m_roomFactory;
    protected RoomContentFactory m_roomContentFactory;
    protected PersonalSpaceLayout m_personalSpaceLayout;
    
    protected PersonalSpace(final long ownerId, final String ownerName, final long guildId, final RoomFactory roomFactory, final RoomContentFactory roomContentFactory, final PersonalSpaceLayout personalSpaceLayout) {
        super();
        this.m_ownerId = ownerId;
        this.m_ownerName = ownerName;
        this.m_guildId = guildId;
        this.m_roomFactory = roomFactory;
        this.m_roomContentFactory = roomContentFactory;
        this.m_personalSpaceLayout = personalSpaceLayout;
    }
    
    public long getOwnerId() {
        return this.m_ownerId;
    }
    
    public void setOwnerId(final long ownerId) {
        this.m_ownerId = ownerId;
    }
    
    public String getOwnerName() {
        return this.m_ownerName;
    }
    
    public void setOwnerName(final String ownerName) {
        this.m_ownerName = ownerName;
    }
    
    public long getGuildId() {
        return this.m_guildId;
    }
    
    public void setGuildId(final long guildId) {
        this.m_guildId = guildId;
    }
    
    public RoomFactory getRoomFactory() {
        return this.m_roomFactory;
    }
    
    public RoomContentFactory getRoomContentFactory() {
        return this.m_roomContentFactory;
    }
    
    public PersonalSpaceLayout getLayout() {
        return this.m_personalSpaceLayout;
    }
    
    public byte getEntryRoomId() {
        return 0;
    }
    
    public Room putRoomContent(final RoomContent content) {
        final Room room = this.m_personalSpaceLayout.getRoomFromUnit(content.getWorldCellX(), content.getWorldCellY());
        if (room != null && room.putContent(content)) {
            this.onContentAdded(content, room);
            return room;
        }
        return null;
    }
    
    public boolean canPutRoomContentAt(final RoomContent content, final int x, final int y) {
        final Room room = this.m_personalSpaceLayout.getRoomFromUnit(x, y);
        return room != null && room.canPutContentAt(content, x, y);
    }
    
    public boolean removeRoomContent(final RoomContent content) {
        final Room room = this.m_personalSpaceLayout.getRoomFromUnit(content.getWorldCellX(), content.getWorldCellY());
        if (room != null && room.removeContent(content)) {
            this.onContentRemoved(content, room);
            return true;
        }
        return false;
    }
    
    public boolean toRaw(final RawDimensionalBagForSave raw) {
        for (final Room room : this.m_personalSpaceLayout) {
            final RawDimensionalBagForSave.Room rawRoom = new RawDimensionalBagForSave.Room();
            room.toRaw(rawRoom.room, true);
            raw.rooms.add(rawRoom);
        }
        return true;
    }
    
    public boolean toRaw(final RawDimensionalBagForClient raw, final boolean forOwner) {
        raw.ownerId = this.m_ownerId;
        raw.ownerName = this.m_ownerName;
        raw.guildId = this.m_guildId;
        for (final Room room : this.m_personalSpaceLayout) {
            final RawDimensionalBagForClient.Room rawRoom = new RawDimensionalBagForClient.Room();
            if (!room.toRaw(rawRoom.room, false)) {
                PersonalSpace.m_logger.error((Object)("Erreur durant la s\u00e9rialisation de la salle " + room.getLayoutPosition() + " du sac " + this));
                return false;
            }
            raw.rooms.add(rawRoom);
        }
        return true;
    }
    
    public boolean fromRaw(final RawDimensionalBagForSave raw) {
        this.m_personalSpaceLayout.clear();
        if (raw.rooms.isEmpty()) {
            PersonalSpace.m_logger.error((Object)("Pas de pi\u00e8ce ... probl\u00e8me de d\u00e9-serialisation du sac " + this));
        }
        for (final RawDimensionalBagForSave.Room rawRoom : raw.rooms) {
            try {
                final Room room = this.m_roomFactory.newRoom();
                this.m_personalSpaceLayout.putRoom(rawRoom.room.layoutPosition, room);
                if (room.fromRaw(rawRoom.room, false)) {
                    continue;
                }
                PersonalSpace.m_logger.error((Object)("Erreur durant la r\u00e9cup\u00e9ration des donn\u00e9es d'une pi\u00e8ce du sac " + this));
            }
            catch (Exception e) {
                PersonalSpace.m_logger.error((Object)("Erreur durant la r\u00e9cup\u00e9ration des donn\u00e9es d'une pi\u00e8ce du sac " + rawRoom), (Throwable)e);
            }
        }
        try {
            this.m_personalSpaceLayout.checkConsistency(this.m_roomFactory);
            if (!this.m_personalSpaceLayout.update()) {
                PersonalSpace.m_logger.error((Object)("Erreur lors de l'Update des room du Havre-sac " + this));
            }
        }
        catch (Exception e2) {
            PersonalSpace.m_logger.error((Object)("Erreur durant l'update des donn\u00e9es d'une pi\u00e8ce du sac " + this), (Throwable)e2);
        }
        for (final RawDimensionalBagForSave.Room rawRoom : raw.rooms) {
            try {
                final Room room = this.m_personalSpaceLayout.getRoom(rawRoom.room.layoutPosition);
                if (room.fromRaw(rawRoom.room, true)) {
                    continue;
                }
                PersonalSpace.m_logger.error((Object)("Erreur durant la r\u00e9cup\u00e9ration des donn\u00e9es d'une pi\u00e8ce du sac " + this));
            }
            catch (Exception e) {
                PersonalSpace.m_logger.error((Object)("Erreur durant la r\u00e9cup\u00e9ration des donn\u00e9es d'une pi\u00e8ce du sac " + rawRoom), (Throwable)e);
            }
        }
        return true;
    }
    
    public boolean fromRaw(final RawDimensionalBagForClient raw) {
        this.m_ownerId = raw.ownerId;
        this.m_ownerName = raw.ownerName;
        this.m_guildId = raw.guildId;
        this.m_personalSpaceLayout.clear();
        for (final RawDimensionalBagForClient.Room rawRoom : raw.rooms) {
            final Room room = this.m_roomFactory.newRoom();
            this.m_personalSpaceLayout.putRoom(rawRoom.room.layoutPosition, room);
            if (!room.fromRaw(rawRoom.room, false)) {
                PersonalSpace.m_logger.error((Object)("Erreur durant la r\u00e9cup\u00e9ration des donn\u00e9es d'une pi\u00e8ce du sac " + this));
                return false;
            }
        }
        this.m_personalSpaceLayout.checkConsistency(this.m_roomFactory);
        this.m_personalSpaceLayout.update();
        for (final RawDimensionalBagForClient.Room rawRoom : raw.rooms) {
            final Room room = this.m_personalSpaceLayout.getRoom(rawRoom.room.layoutPosition);
            if (!room.fromRaw(rawRoom.room, true)) {
                PersonalSpace.m_logger.error((Object)("Erreur durant la r\u00e9cup\u00e9ration des donn\u00e9es d'une pi\u00e8ce du sac " + this));
                return false;
            }
        }
        return true;
    }
    
    protected void onContentAdded(final RoomContent content, final Room room) {
    }
    
    protected void onContentRemoved(final RoomContent content, final Room room) {
    }
    
    public abstract void updateTopology(final TopologyMapInstance p0);
    
    static {
        m_logger = Logger.getLogger((Class)PersonalSpace.class);
    }
}
