package com.ankamagames.xulor2.component.map;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;

public interface PartitionListMapZoneDescription extends MapZoneDescription
{
    PartitionList getPartitionList();
    
    boolean canZoomIn();
}
