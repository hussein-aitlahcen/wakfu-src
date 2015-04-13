package com.ankamagames.wakfu.client.core.game.miniMap;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.graphics.image.*;

public class SplitMapMapzoneDescription extends WakfuMapZoneDescription
{
    public SplitMapMapzoneDescription(final PartitionList list, final int id) {
        super(list, id, null, null, null, (byte)0, null, null, -1L, true);
    }
    
    @Override
    public void sendZoneToMap(final MapManager mapManager) {
    }
}
