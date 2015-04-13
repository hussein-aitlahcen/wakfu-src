package com.ankamagames.wakfu.common.game.havenWorld;

import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import gnu.trove.*;

public interface HavenWorld
{
    short getWorldInstanceId();
    
    HavenWorldDefinition getDefinition();
    
    GuildInfo getGuildInfo();
    
    Building getBuilding(long p0);
    
    boolean forEachBuilding(TObjectProcedure<Building> p0);
    
    Partition getPartition(short p0, short p1);
    
    boolean forEachPartition(TObjectProcedure<Partition> p0);
    
    int getTotalWorkers();
    
    int getAvailableWorkers();
    
    int getBuildingAndEvolutionsCount(short p0);
    
    int getBuildingOfTypeCount(int p0);
    
    int getResources();
    
    boolean addListener(HavenWorldListener p0);
    
    boolean removeListener(HavenWorldListener p0);
    
    int getPartitionCost();
}
