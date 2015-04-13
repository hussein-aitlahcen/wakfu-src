package com.ankamagames.wakfu.common.game.companion;

import com.ankamagames.wakfu.common.datas.Breed.*;

public class CompanionManagerController
{
    protected final long m_clientId;
    
    public CompanionManagerController(final long clientId) {
        super();
        this.m_clientId = clientId;
    }
    
    public CompanionModel addCompanion(final CompanionModel companion) throws CompanionException {
        final short breedId = companion.getBreedId();
        if (CompanionManager.INSTANCE.hasUnlockedCompanionWithBreed(this.m_clientId, breedId)) {
            throw new CompanionException("Le joueur poss\u00e8de d\u00e9j\u00e0 un compagnon de ce type " + breedId);
        }
        final Breed breed = MonsterBreedManagerProvider.getMonsterBreedManager().getBreedFromId(breedId);
        if (breed == null) {
            throw new CompanionException("On ne peut pas ajouter de compagnon de breed inconnue " + breedId);
        }
        CompanionManager.INSTANCE.addCompanion(this.m_clientId, companion);
        return companion;
    }
    
    public boolean removeCompanion(final long companionId) {
        return CompanionManager.INSTANCE.removeCompanion(this.m_clientId, companionId);
    }
    
    public void clearCompanionList() {
        CompanionManager.INSTANCE.clearCompanions(this.m_clientId);
    }
    
    @Override
    public String toString() {
        return "CompanionManagerController{m_clientId=" + this.m_clientId + '}';
    }
}
