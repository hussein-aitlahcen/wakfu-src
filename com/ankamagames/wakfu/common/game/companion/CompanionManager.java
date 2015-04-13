package com.ankamagames.wakfu.common.game.companion;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.companion.freeCompanion.*;
import java.util.*;
import gnu.trove.*;

public final class CompanionManager
{
    private static final Logger m_logger;
    public static final CompanionManager INSTANCE;
    private final TLongObjectHashMap<TLongObjectHashMap<CompanionModel>> m_companionLists;
    private final TLongObjectHashMap<CompanionModel> m_companionsById;
    private final List<CompanionManagerListener> m_listeners;
    
    private CompanionManager() {
        super();
        this.m_companionLists = new TLongObjectHashMap<TLongObjectHashMap<CompanionModel>>();
        this.m_companionsById = new TLongObjectHashMap<CompanionModel>();
        this.m_listeners = new ArrayList<CompanionManagerListener>();
    }
    
    public void addCompanion(final long clientId, final CompanionModel companion) {
        if (companion.getOwnerId() != clientId) {
            throw new IllegalArgumentException("le companion " + companion + " doit avoir le bon ownerId : " + clientId);
        }
        TLongObjectHashMap<CompanionModel> playerCompanions = this.m_companionLists.get(clientId);
        if (playerCompanions == null) {
            playerCompanions = new TLongObjectHashMap<CompanionModel>();
            this.m_companionLists.put(clientId, playerCompanions);
        }
        playerCompanions.put(companion.getId(), companion);
        this.m_companionsById.put(companion.getId(), companion);
        this.fireCompanionAdded(companion);
    }
    
    public List<CompanionModel> getCompanions(final long clientId) {
        final TLongObjectHashMap<CompanionModel> companions = this.m_companionLists.get(clientId);
        if (companions == null) {
            return (List<CompanionModel>)Collections.EMPTY_LIST;
        }
        final List<CompanionModel> res = new ArrayList<CompanionModel>();
        companions.forEachValue(new TObjectProcedure<CompanionModel>() {
            @Override
            public boolean execute(final CompanionModel companion) {
                res.add(companion);
                return true;
            }
        });
        return res;
    }
    
    public CompanionModel getCompanion(final long clientId, final long companionId) {
        final TLongObjectHashMap<CompanionModel> companions = this.m_companionLists.get(clientId);
        if (companions == null) {
            return null;
        }
        return companions.get(companionId);
    }
    
    public CompanionModel getCompanion(final long companionId) {
        return this.m_companionsById.get(companionId);
    }
    
    public CompanionModel getCompanionWithBreed(final long clientId, final int breedId) {
        final TLongObjectHashMap<CompanionModel> companions = this.m_companionLists.get(clientId);
        if (companions == null) {
            return null;
        }
        final TLongObjectIterator<CompanionModel> it = companions.iterator();
        while (it.hasNext()) {
            it.advance();
            final CompanionModel companion = it.value();
            if (companion.getBreedId() == breedId) {
                return companion;
            }
        }
        return null;
    }
    
    public boolean hasUnlockedCompanionWithBreed(final long clientId, final int breedId) {
        final CompanionModel companionWithBreed = this.getCompanionWithBreed(clientId, breedId);
        return companionWithBreed != null && companionWithBreed.isUnlocked();
    }
    
    public void clearCompanions(final long clientId) {
        final TLongObjectHashMap<CompanionModel> map = this.m_companionLists.remove(clientId);
        if (map != null) {
            final TLongObjectIterator<CompanionModel> it = map.iterator();
            while (it.hasNext()) {
                it.advance();
                this.m_companionsById.remove(it.key());
            }
        }
    }
    
    public boolean removeCompanion(final long clientId, final long companionId) {
        this.m_companionsById.remove(companionId);
        final TLongObjectHashMap<CompanionModel> companions = this.m_companionLists.get(clientId);
        if (companions == null) {
            return false;
        }
        final CompanionModel companionModel = companions.remove(companionId);
        final boolean removed = companionModel != null;
        if (removed) {
            this.fireCompanionRemoved(companionModel);
        }
        return removed;
    }
    
    public List<CompanionModel> getActiveCompanions(final long clientId) {
        final TLongObjectHashMap<CompanionModel> companions = this.m_companionLists.get(clientId);
        if (companions == null) {
            return (List<CompanionModel>)Collections.EMPTY_LIST;
        }
        final List<CompanionModel> res = new ArrayList<CompanionModel>();
        companions.forEachValue(new TObjectProcedure<CompanionModel>() {
            @Override
            public boolean execute(final CompanionModel companion) {
                if (companion.isUnlocked() || FreeCompanionManager.INSTANCE.isFreeCompanion(companion.getBreedId()) || companion.hasEquipment(companion)) {
                    res.add(companion);
                }
                return true;
            }
        });
        return res;
    }
    
    private void fireCompanionRemoved(final CompanionModel companion) {
        for (final CompanionManagerListener listener : this.m_listeners) {
            try {
                listener.companionRemoved(companion);
            }
            catch (Exception e) {
                CompanionManager.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    private void fireCompanionAdded(final CompanionModel companion) {
        for (final CompanionManagerListener listener : this.m_listeners) {
            try {
                listener.companionAdded(companion);
            }
            catch (Exception e) {
                CompanionManager.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    public void removeListener(final CompanionManagerListener listener) {
        if (!this.m_listeners.contains(listener)) {
            return;
        }
        this.m_listeners.remove(listener);
    }
    
    public void addListener(final CompanionManagerListener listener) {
        if (this.m_listeners.contains(listener)) {
            return;
        }
        this.m_listeners.add(listener);
    }
    
    public CompanionModel getCompanionHoldingItem(final long clientId, final long itemId) {
        final List<CompanionModel> companions = this.getCompanions(clientId);
        for (final CompanionModel companion : companions) {
            if (companion.getItemEquipment().containsUniqueId(itemId)) {
                return companion;
            }
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionManager.class);
        INSTANCE = new CompanionManager();
    }
}
