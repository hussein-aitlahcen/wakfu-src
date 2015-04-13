package com.ankamagames.wakfu.client.core.game.miniMap;

import com.ankamagames.xulor2.component.map.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.graphics.image.*;

public abstract class WakfuMapZoneDescription extends DefaultMapZoneDescription
{
    protected WakfuMapZoneDescription(final PartitionList list, final int id, final Color color, final String textDescription, final String iconUrl, final byte maskIndex, final String anim1, final String anim2, final long highlightSoundId, final boolean interactive) {
        super(list, id, color, textDescription, iconUrl, maskIndex, anim1, anim2, highlightSoundId, interactive);
    }
    
    public abstract void sendZoneToMap(final MapManager p0);
}
