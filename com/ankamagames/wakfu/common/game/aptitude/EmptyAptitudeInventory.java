package com.ankamagames.wakfu.common.game.aptitude;

import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;

public final class EmptyAptitudeInventory implements AptitudeInventory
{
    public static EmptyAptitudeInventory INSTANCE;
    
    @Override
    public int getWonPoints(final AptitudeType type) {
        return 0;
    }
    
    @Override
    public void setWonPoints(final AptitudeType type, final int availablePoints) {
    }
    
    @Override
    public int getAvailablePoints(final AptitudeType type) {
        return 0;
    }
    
    @Override
    public void setAvailablePoints(final AptitudeType type, final int availablePoints) {
    }
    
    @Override
    public Iterator<Aptitude> iterator() {
        return null;
    }
    
    @Override
    public List<Aptitude> getAptitudes(final AptitudeType type) {
        return Collections.emptyList();
    }
    
    @Override
    public List<Aptitude> getCommonAptitudes() {
        return Collections.emptyList();
    }
    
    @Override
    public List<Aptitude> getBreedAptitudes() {
        return Collections.emptyList();
    }
    
    @Override
    public boolean toRaw(final RawAptitudeInventory raw) {
        return false;
    }
    
    @Override
    public boolean fromRaw(final RawAptitudeInventory raw) {
        return false;
    }
    
    @Override
    public Aptitude getWithUniqueId(final short aptitudeId) {
        return null;
    }
    
    @Override
    public boolean destroyWithUniqueId(final long uniqueId) {
        return false;
    }
    
    @Override
    public void cleanup() {
    }
    
    @Override
    public List<Aptitude> getAllWithReferenceId(final int i) {
        return Collections.emptyList();
    }
    
    @Override
    public boolean add(final Aptitude aptitude) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        return false;
    }
    
    @Override
    public int destroyAll() {
        return 0;
    }
    
    static {
        EmptyAptitudeInventory.INSTANCE = new EmptyAptitudeInventory();
    }
}
