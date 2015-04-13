package com.ankamagames.wakfu.common.game.aptitude;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;

public interface AptitudeInventory extends Iterable<Aptitude>, RawConvertible<RawAptitudeInventory>
{
    int getAvailablePoints(AptitudeType p0);
    
    void setAvailablePoints(AptitudeType p0, int p1);
    
    int getWonPoints(AptitudeType p0);
    
    void setWonPoints(AptitudeType p0, int p1);
    
    Iterator<Aptitude> iterator();
    
    List<Aptitude> getAptitudes(AptitudeType p0);
    
    List<Aptitude> getCommonAptitudes();
    
    List<Aptitude> getBreedAptitudes();
    
    boolean toRaw(RawAptitudeInventory p0);
    
    boolean fromRaw(RawAptitudeInventory p0);
    
    Aptitude getWithUniqueId(short p0);
    
    boolean destroyWithUniqueId(long p0);
    
    void cleanup();
    
    List<Aptitude> getAllWithReferenceId(int p0);
    
    boolean add(Aptitude p0) throws InventoryCapacityReachedException, ContentAlreadyPresentException;
    
    int destroyAll();
}
