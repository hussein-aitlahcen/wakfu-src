package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public interface MonsterSpawner
{
    boolean spawnNpcAndAddItToFight(int p0, int p1);
    
    boolean spawnNpcAndAddItToFight(int p0, int p1, Point3 p2);
    
    boolean spawnNpcAndAddItToFight(int p0, int p1, Point3 p2, Collection<? extends PropertyType> p3);
}
