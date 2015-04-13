package com.ankamagames.wakfu.client.core.game.dimensionalBag;

import com.ankamagames.wakfu.client.core.game.dimensionalBag.room.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.client.alea.environment.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.compressedCellData.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.rights.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import java.util.*;

public class DimensionalBag extends AbstractDimensionalBag
{
    DimensionalBag() {
        super(-1L, "<undefined>", -1L, DimensionalBagRoomFactory.INSTANCE, DimensionalBagRoomContentFactory.INSTANCE, new DimensionalBagLayout());
        this.getLayout().setPersonalSpace(this);
    }
    
    public final boolean isCellUsable(final short x, final short y) {
        return this.isCellWalkable(x, y);
    }
    
    @Override
    public void updateTopology(final TopologyMapInstance mapInstance) {
        super.updateTopology(mapInstance);
    }
    
    public void updateGroundTypes(final WakfuClientEnvironmentMap environmentMap) {
        final CompressedCellData groundTypeData = this.buildGroundTypeData(environmentMap.getX(), environmentMap.getY());
        environmentMap.setGroundType(groundTypeData);
    }
    
    boolean canCharacterAccessRoom(final long visiterId, final int roomTypeId) {
        if (visiterId == this.m_ownerId) {
            return true;
        }
        final GemType gemType = GemType.getFromItemReferenceId(roomTypeId);
        final DimBagRights rights = this.getRights();
        final DimBagIndividualRight individualRight = rights.getIndividualRight(visiterId);
        if (individualRight != null) {
            return individualRight.hasRight(gemType);
        }
        final DimBagGroupRight guildRight = rights.getGroupRight(GroupType.GUILD);
        if (WakfuGameEntity.getInstance().getLocalPlayer().getGuildId() == this.getGuildId() && guildRight != null) {
            return guildRight.hasRight(gemType);
        }
        final DimBagGroupRight allRight = rights.getGroupRight(GroupType.ALL);
        return allRight != null && allRight.hasRight(gemType);
    }
    
    public void release() {
        for (final Room genericRoom : this.m_personalSpaceLayout) {
            if (genericRoom instanceof GemControlledRoom) {
                final GemControlledRoom room = (GemControlledRoom)genericRoom;
                room.release();
            }
        }
    }
    
    @Override
    protected void onCustomViewChanged() {
    }
}
