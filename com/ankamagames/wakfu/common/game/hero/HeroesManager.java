package com.ankamagames.wakfu.common.game.hero;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import gnu.trove.*;

public class HeroesManager
{
    private static final Logger m_logger;
    public static final HeroesManager INSTANCE;
    private final TLongObjectHashMap<TLongHashSet> m_heroes;
    private final TLongObjectHashMap<TLongHashSet> m_heroesInParty;
    private final TLongObjectHashMap<BasicCharacterInfo> m_heroesById;
    private final List<HeroesManagerListener> m_listeners;
    
    private HeroesManager() {
        super();
        this.m_heroes = new TLongObjectHashMap<TLongHashSet>();
        this.m_heroesInParty = new TLongObjectHashMap<TLongHashSet>();
        this.m_heroesById = new TLongObjectHashMap<BasicCharacterInfo>();
        this.m_listeners = new ArrayList<HeroesManagerListener>();
    }
    
    public void addHero(final long ownerId, final BasicCharacterInfo info) {
        TLongHashSet map = this.m_heroes.get(ownerId);
        if (map == null) {
            this.m_heroes.put(ownerId, map = new TLongHashSet());
        }
        map.add(info.getId());
        this.m_heroesById.put(info.getId(), info);
        this.notifyHeroAdded(info);
    }
    
    public void removeHero(final long characterId) {
        final BasicCharacterInfo hero = this.m_heroesById.get(characterId);
        this.removeHero(hero);
    }
    
    public void removeHero(final BasicCharacterInfo info) {
        if (info == null) {
            return;
        }
        this.m_heroesById.remove(info.getId());
        this.removeHeroFromParty(info.getOwnerId(), info.getId());
        if (!this.removeFromOwnerHeroes(info)) {
            return;
        }
        this.notifyHeroRemoved(info);
    }
    
    public void removeHeroes(final long ownerId) {
        final TLongHashSet heroesIds = this.m_heroes.get(ownerId);
        this.m_heroesInParty.remove(ownerId);
        if (heroesIds == null) {
            return;
        }
        final TLongIterator it = heroesIds.iterator();
        while (it.hasNext()) {
            this.m_heroesById.remove(it.next());
        }
    }
    
    private boolean removeFromOwnerHeroes(final BasicCharacterInfo info) {
        final TLongHashSet heroes = this.m_heroes.get(info.getOwnerId());
        if (heroes == null) {
            return false;
        }
        heroes.remove(info.getId());
        if (heroes.isEmpty()) {
            this.m_heroes.remove(info.getOwnerId());
        }
        return true;
    }
    
    public boolean addHeroToParty(final long ownerId, final long characterId) {
        TLongHashSet map = this.m_heroesInParty.get(ownerId);
        if (map == null) {
            this.m_heroesInParty.put(ownerId, map = new TLongHashSet());
        }
        final boolean added = map.add(characterId);
        if (added) {
            this.notifyHeroAddedToParty(characterId);
        }
        return added;
    }
    
    public void removeHeroFromParty(final long ownerId, final long characterId) {
        final TLongHashSet map = this.m_heroesInParty.get(ownerId);
        if (map == null) {
            return;
        }
        final boolean removed = map.remove(characterId);
        if (removed) {
            this.notifyHeroRemovedFromParty(characterId);
        }
        if (map.isEmpty()) {
            this.m_heroesInParty.remove(ownerId);
        }
    }
    
    public TLongHashSet getHeroes(final long ownerId) {
        return this.m_heroes.get(ownerId);
    }
    
    public <T extends BasicCharacterInfo> boolean hasHero(final T characterInfo) {
        return this.m_heroesById.containsKey(characterInfo.getId());
    }
    
    public <T extends BasicCharacterInfo> T getHero(final long characterId) {
        return (T)this.m_heroesById.get(characterId);
    }
    
    public <T extends BasicCharacterInfo> BasicCharacterInfo setHeroInfo(final T characterInfo) {
        return this.m_heroesById.put(characterInfo.getId(), characterInfo);
    }
    
    public boolean forEachHeroId(final TLongProcedure procedure) {
        return this.m_heroesById.forEach(procedure);
    }
    
    public boolean forEachHero(final long ownerId, final TObjectProcedure<BasicCharacterInfo> procedure) {
        final TLongHashSet heroesIds = this.m_heroes.get(ownerId);
        return heroesIds == null || this.executeProcedureOnHeroes(heroesIds, procedure);
    }
    
    public <T extends BasicCharacterInfo> boolean forEachHeroInParty(final long ownerId, final TObjectProcedure<T> procedure) {
        final TLongHashSet heroesInPartyIds = this.m_heroesInParty.get(ownerId);
        return heroesInPartyIds == null || this.executeProcedureOnHeroes(heroesInPartyIds, procedure);
    }
    
    private <T extends BasicCharacterInfo> boolean executeProcedureOnHeroes(final TLongHashSet heroesIds, final TObjectProcedure<T> procedure) {
        final TLongIterator it = heroesIds.iterator();
        while (it.hasNext()) {
            final BasicCharacterInfo hero = this.m_heroesById.get(it.next());
            if (hero == null) {
                continue;
            }
            boolean stopProcedure;
            try {
                stopProcedure = !procedure.execute((T)hero);
            }
            catch (Exception e) {
                HeroesManager.m_logger.error((Object)"Exception levee", (Throwable)e);
                return false;
            }
            if (stopProcedure) {
                return false;
            }
        }
        return true;
    }
    
    public TLongHashSet getHeroesInParty(final long ownerId) {
        final TLongHashSet value = this.m_heroesInParty.get(ownerId);
        if (value != null) {
            return new TLongHashSet(value.toArray());
        }
        return null;
    }
    
    public int getHeroesInPartyQuantity(final long ownerId) {
        final TLongHashSet party = this.m_heroesInParty.get(ownerId);
        return (party != null) ? party.size() : 0;
    }
    
    public <T extends BasicCharacterInfo> void addListener(final HeroesManagerListener<T> listener) {
        this.m_listeners.add(listener);
    }
    
    public <T extends BasicCharacterInfo> void removeListener(final HeroesManagerListener<T> listener) {
        this.m_listeners.remove(listener);
    }
    
    private void notifyHeroAdded(final BasicCharacterInfo info) {
        final List<HeroesManagerListener> listeners = new ArrayList<HeroesManagerListener>(this.m_listeners);
        for (int i = 0, size = listeners.size(); i < size; ++i) {
            try {
                listeners.get(i).heroAdded(info);
            }
            catch (Exception e) {
                HeroesManager.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    private void notifyHeroAddedToParty(final long characterId) {
        final List<HeroesManagerListener> listeners = new ArrayList<HeroesManagerListener>(this.m_listeners);
        for (int i = 0, size = listeners.size(); i < size; ++i) {
            try {
                listeners.get(i).heroAddedToParty(this.getHero(characterId));
            }
            catch (Exception e) {
                HeroesManager.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    private void notifyHeroRemoved(final BasicCharacterInfo info) {
        final List<HeroesManagerListener> listeners = new ArrayList<HeroesManagerListener>(this.m_listeners);
        for (int i = 0, size = listeners.size(); i < size; ++i) {
            try {
                listeners.get(i).heroRemoved(info);
            }
            catch (Exception e) {
                HeroesManager.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    private void notifyHeroRemovedFromParty(final long characterId) {
        final List<HeroesManagerListener> listeners = new ArrayList<HeroesManagerListener>(this.m_listeners);
        for (int i = 0, size = listeners.size(); i < size; ++i) {
            try {
                listeners.get(i).heroRemovedFromParty(this.getHero(characterId));
            }
            catch (Exception e) {
                HeroesManager.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    public void clear() {
        this.m_heroesInParty.clear();
        this.m_heroesById.clear();
        this.m_heroes.clear();
    }
    
    @Override
    public String toString() {
        return "HeroesManager{m_heroes=" + this.m_heroes + ", m_heroesById=" + this.m_heroesById + ", m_listeners=" + this.m_listeners + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)HeroesManager.class);
        INSTANCE = new HeroesManager();
    }
}
