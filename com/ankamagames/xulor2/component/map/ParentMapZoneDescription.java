package com.ankamagames.xulor2.component.map;

import org.jetbrains.annotations.*;
import java.util.*;

public interface ParentMapZoneDescription extends MapZoneDescription
{
    List<PartitionListMapZoneDescription> getChildren();
    
    short getAltitudeAt00();
    
    @Nullable
    String getMapUrl();
    
    Collection<? extends ParentMapZoneDescription> getDisplayedZones();
}
