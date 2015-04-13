package com.ankamagames.wakfu.common.game.personalSpace.impl;

import com.ankamagames.wakfu.common.game.personalSpace.impl.rights.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.compressedCellData.*;

public abstract class AbstractDimensionalBag extends PersonalSpace
{
    public static final int NUMBER_OF_DISPLAY_INFOS = 3;
    public static final int DIMENSIONL_BAG_ENTRY_POINT_X = -1;
    public static final int DIMENSIONL_BAG_ENTRY_POINT_Y = 2;
    public static final short DIMENSIONAL_BAG_INSTANCE_ID = 9;
    protected final Wallet m_wallet;
    private boolean m_isLocked;
    private final DimBagRights m_rights;
    private int m_customViewModelId;
    
    public AbstractDimensionalBag(final long ownerId, final String ownerName, final long guildId, final RoomFactory roomFactory, final RoomContentFactory roomContentFactory, final PersonalSpaceLayout personalSpaceLayout) {
        super(ownerId, ownerName, guildId, roomFactory, roomContentFactory, personalSpaceLayout);
        this.m_wallet = new Wallet();
        this.m_rights = new DimBagRights();
        this.setCustomViewModelId(408);
    }
    
    public Wallet getWallet() {
        return this.m_wallet;
    }
    
    public DimBagRights getRights() {
        return this.m_rights;
    }
    
    public int getCustomViewModelId() {
        return this.m_customViewModelId;
    }
    
    public void setCustomViewModelId(final int customViewModelId) {
        this.m_customViewModelId = ((customViewModelId <= 0) ? 408 : customViewModelId);
        this.onCustomViewChanged();
    }
    
    @Override
    public boolean toRaw(final RawDimensionalBagForSave raw) {
        super.toRaw(raw);
        raw.cash = this.m_wallet.getAmountOfCash();
        raw.customViewModelId = this.m_customViewModelId;
        raw.permissions.clear();
        raw.permissions.dimensionalBagLocked = this.m_isLocked;
        try {
            this.m_rights.toRaw(raw.permissions);
        }
        catch (Exception e) {
            AbstractDimensionalBag.m_logger.error((Object)("Erreur \u00e0 la serialisation des permissions" + this), (Throwable)e);
        }
        return true;
    }
    
    @Override
    public boolean toRaw(final RawDimensionalBagForClient raw, final boolean forOwner) {
        if (!super.toRaw(raw, forOwner)) {
            return false;
        }
        if (forOwner) {
            raw.wallet = new RawDimensionalBagForClient.Wallet();
            raw.wallet.cash = this.m_wallet.getAmountOfCash();
        }
        else {
            raw.wallet = null;
        }
        raw.customViewModelId = this.m_customViewModelId;
        raw.permissions.clear();
        raw.permissions.dimensionalBagLocked = this.m_isLocked;
        this.m_rights.toRaw(raw.permissions);
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawDimensionalBagForClient raw) {
        if (!super.fromRaw(raw)) {
            return false;
        }
        if (raw.wallet != null) {
            this.m_wallet.setAmountOfCash(raw.wallet.cash);
        }
        this.setCustomViewModelId(raw.customViewModelId);
        this.m_isLocked = raw.permissions.dimensionalBagLocked;
        return this.fromRawPermissions(raw.permissions);
    }
    
    @Override
    public boolean fromRaw(final RawDimensionalBagForSave raw) {
        this.m_wallet.setAmountOfCash(raw.cash);
        this.setCustomViewModelId(raw.customViewModelId);
        super.fromRaw(raw);
        this.m_isLocked = raw.permissions.dimensionalBagLocked;
        try {
            this.fromRawPermissions(raw.permissions);
        }
        catch (Exception e) {
            AbstractDimensionalBag.m_logger.error((Object)("Exception \u00e0 la d\u00e9-serialisation des permissions du Havre-sac du joueur " + this.m_ownerId), (Throwable)e);
        }
        return true;
    }
    
    public boolean fromRawPermissions(final RawDimensionalBagPermissions raw) {
        this.m_rights.fromRaw(raw);
        return true;
    }
    
    public boolean isCorridorCell(final short x, final short y) {
        if ((y == 1 || y == 2 || y == 7 || y == 8 || y == 13 || y == 14) && (x == 4 || x == 5 || x == 10 || x == 11)) {
            final int rx = x / 6 * 6;
            final int ry = y / 6 * 6;
            final GemControlledRoom left = (GemControlledRoom)this.m_personalSpaceLayout.getRoomFromUnit(rx, ry);
            final GemControlledRoom right = (GemControlledRoom)this.m_personalSpaceLayout.getRoomFromUnit(rx + 6, ry);
            if (left == null || right == null) {
                AbstractDimensionalBag.m_logger.error((Object)"Probl\u00e8me de r\u00e9cup\u00e9ration des salles.");
                return false;
            }
            if (left.getGem(true) != null && right.getGem(true) != null) {
                return true;
            }
        }
        if ((x == 1 || x == 2 || x == 7 || x == 8 || x == 13 || x == 14) && (y == 4 || y == 5 || y == 10 || y == 11)) {
            final int rx = x / 6 * 6;
            final int ry = y / 6 * 6;
            final GemControlledRoom top = (GemControlledRoom)this.m_personalSpaceLayout.getRoomFromUnit(rx, ry);
            final GemControlledRoom bottom = (GemControlledRoom)this.m_personalSpaceLayout.getRoomFromUnit(rx, ry + 6);
            if (top == null || bottom == null) {
                AbstractDimensionalBag.m_logger.error((Object)"Probl\u00e8me de r\u00e9cup\u00e9ration des salles.");
                return false;
            }
            if (top.getGem(true) != null && bottom.getGem(true) != null) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean isCellWalkable(final short x, final short y) {
        if (this.isCorridorCell(x, y)) {
            return true;
        }
        boolean walkable = false;
        final GemControlledRoom room = (GemControlledRoom)this.m_personalSpaceLayout.getRoomFromUnit(x, y);
        if (room != null) {
            final short xo = room.getOriginX();
            final short yo = room.getOriginY();
            final byte w = room.getWidth();
            final byte h = room.getHeight();
            walkable = (x >= xo && x < xo + w && y >= yo && y < yo + h && room.getGem(true) != null);
        }
        return walkable;
    }
    
    @Override
    public void updateTopology(final TopologyMapInstance mapInstance) {
        final TopologyMap topologyMap = mapInstance.getTopologyMap();
        if (topologyMap == null) {
            AbstractDimensionalBag.m_logger.error((Object)"Topology null !!! (ne devrait pas arriver)");
            return;
        }
        for (int y = 0; y < 18; ++y) {
            for (int x = 0; x < 18; ++x) {
                mapInstance.setCellBlocked(x, y, !this.isCellWalkable((short)x, (short)y));
            }
        }
        for (final Room room : this.m_personalSpaceLayout) {
            for (final RoomContent content : room.getContents()) {
                if (!topologyMap.isInMap(content.getWorldCellX(), content.getWorldCellY())) {
                    AbstractDimensionalBag.m_logger.error((Object)("\u00c9l\u00e9ment dans le sac dimensionnel plac\u00e9 au mauvais endroit " + content + " (" + content.getWorldCellX() + ";" + content.getWorldCellY() + ") de type=" + content.getContentType()));
                }
                else {
                    mapInstance.setBlocked(content.getWorldCellX(), content.getWorldCellY(), content.getWorldCellAltitude(), content.isBlockingMovements());
                }
            }
        }
    }
    
    public GemControlledRoom.ModResult putGem(final byte roomLayoutPosition, final Item gem, final boolean primary, final boolean commit) {
        final Room room = this.m_personalSpaceLayout.getRoom(roomLayoutPosition);
        if (room instanceof GemControlledRoom) {
            final GemControlledRoom.ModResult results = ((GemControlledRoom)room).putGem(gem, primary, commit);
            return results;
        }
        return null;
    }
    
    public GemControlledRoom.ModResult removeGem(final byte roomLayoutPosition, final boolean primary, final boolean commit) {
        final Room room = this.m_personalSpaceLayout.getRoom(roomLayoutPosition);
        if (room instanceof GemControlledRoom) {
            return ((GemControlledRoom)room).removeGem(primary, commit);
        }
        return null;
    }
    
    public Item getGem(final byte roomLayoutPosition, final boolean primary) {
        final Room room = this.m_personalSpaceLayout.getRoom(roomLayoutPosition);
        if (room instanceof GemControlledRoom) {
            return ((GemControlledRoom)room).getGem(primary);
        }
        return null;
    }
    
    public GemControlledRoom.ModResult exchangeGem(final byte sourceRoomLayoutPosition, final boolean sourceIsPrimary, final byte destRoomLayoutPosition, final boolean destIsPrimary, final boolean commit) {
        if (sourceRoomLayoutPosition == destRoomLayoutPosition) {
            return GemControlledRoom.ModResult.INVALID_ARGUMENT;
        }
        final Room sourceRoom = this.m_personalSpaceLayout.getRoom(sourceRoomLayoutPosition);
        final Room destRoom = this.m_personalSpaceLayout.getRoom(destRoomLayoutPosition);
        if (!(sourceRoom instanceof GemControlledRoom) || !(destRoom instanceof GemControlledRoom)) {
            return GemControlledRoom.ModResult.INVALID_ARGUMENT;
        }
        final GemControlledRoom s = (GemControlledRoom)sourceRoom;
        final GemControlledRoom d = (GemControlledRoom)destRoom;
        final Item sourceGem = s.getGem(sourceIsPrimary);
        if (sourceGem == null) {
            return GemControlledRoom.ModResult.INVALID_ARGUMENT;
        }
        final Item destGem = d.getGem(destIsPrimary);
        if (destGem == null) {
            final GemControlledRoom.ModResult ms = s.removeGem(sourceIsPrimary, false);
            if (ms != GemControlledRoom.ModResult.OK) {
                return ms;
            }
            final GemControlledRoom.ModResult md = d.putGem(sourceGem, destIsPrimary, false);
            if (md != GemControlledRoom.ModResult.OK) {
                return md;
            }
            if (commit) {
                s.removeGem(sourceIsPrimary, true);
                d.putGem(sourceGem, destIsPrimary, true);
            }
            return GemControlledRoom.ModResult.OK;
        }
        else {
            GemControlledRoom.ModResult ms = s.removeGem(sourceIsPrimary, false);
            if (ms != GemControlledRoom.ModResult.OK) {
                return ms;
            }
            GemControlledRoom.ModResult md = d.removeGem(destIsPrimary, false);
            if (md != GemControlledRoom.ModResult.OK) {
                return md;
            }
            ms = s.replaceGem(sourceIsPrimary, destGem, false);
            if (ms != GemControlledRoom.ModResult.OK) {
                return ms;
            }
            md = d.replaceGem(destIsPrimary, sourceGem, false);
            if (md != GemControlledRoom.ModResult.OK) {
                return md;
            }
            if (commit) {
                s.replaceGem(sourceIsPrimary, destGem, true);
                d.replaceGem(destIsPrimary, sourceGem, true);
            }
            return GemControlledRoom.ModResult.OK;
        }
    }
    
    protected CompressedCellData buildGroundTypeData(final int mapX, final int mapY) {
        final short[] groundTypes = new short[324];
        final short mapOriginX = (short)(mapX * 18);
        final short mapOriginY = (short)(mapY * 18);
        for (final Room room : this.m_personalSpaceLayout) {
            final GemControlledRoom gemRoom = (GemControlledRoom)room;
            final Item gem = gemRoom.getGem(true);
            short roomGroundType;
            if (gem != null && GemType.getFromItemReferenceId(gem.getReferenceId()) == GemType.GEM_ID_RESOURCES) {
                roomGroundType = 28;
            }
            else {
                roomGroundType = 0;
            }
            final short originX = room.getOriginX();
            final short originY = room.getOriginY();
            for (int x = originX, width = originX + room.getWidth(); x < width; ++x) {
                for (int y = originY, height = originY + room.getHeight(); y < height; ++y) {
                    groundTypes[x - mapOriginX + (y - mapOriginY) * 18] = roomGroundType;
                }
            }
        }
        try {
            final CompressedCellData environmentData = EnvironmentConstants.forPartition().optimize(groundTypes);
            return environmentData;
        }
        catch (CompressedDataException e) {
            AbstractDimensionalBag.m_logger.error((Object)"Exception pendant l'update des types de sol du havre-sac ", (Throwable)e);
            return null;
        }
    }
    
    public boolean isLocked() {
        return this.m_isLocked;
    }
    
    public void setLocked(final boolean locked) {
        this.m_isLocked = locked;
    }
    
    protected abstract void onCustomViewChanged();
}
