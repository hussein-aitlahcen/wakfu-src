package com.ankamagames.wakfu.common.game.nation;

import org.apache.log4j.*;
import java.util.*;
import com.google.common.collect.*;
import gnu.trove.*;

public class NationManager
{
    public static final NationManager INSTANCE;
    private static final Logger m_logger;
    private final TIntObjectHashMap<Nation> m_nations;
    private final ArrayList<NationManagerObserver> m_observers;
    public static final int NATION_SELECTION_PLAYER_MIN_LEVEL = 15;
    
    private NationManager() {
        super();
        this.m_nations = new TIntObjectHashMap<Nation>();
        this.m_observers = new ArrayList<NationManagerObserver>();
    }
    
    public void addObserver(final NationManagerObserver observer) {
        if (this.m_observers.contains(observer)) {
            return;
        }
        this.m_observers.add(observer);
        final TIntObjectIterator<Nation> it = this.nationsIterator();
        while (it.hasNext()) {
            it.advance();
            final Nation nation = it.value();
            observer.onNationRegistered(nation);
        }
    }
    
    private void onNationRegistered(final Nation nation) {
        for (int i = 0; i < this.m_observers.size(); ++i) {
            this.m_observers.get(i).onNationRegistered(nation);
        }
    }
    
    public boolean registerNation(final Nation nation) {
        if (nation.getNationId() == 0) {
            NationManager.m_logger.info((Object)("Enregistrement d'une VOID_NATION : " + nation));
        }
        else {
            NationManager.m_logger.info((Object)("Enregistrement d'une nation  : " + nation));
        }
        final int id = nation.getNationId();
        final Nation old = this.m_nations.get(id);
        if (old != null && old != nation) {
            NationManager.m_logger.error((Object)("Tentative d'\u00e9crasement de r\u00e9f\u00e9rence de nation. ID=" + id));
            return false;
        }
        this.m_nations.put(id, nation);
        this.onNationRegistered(nation);
        return true;
    }
    
    public Nation getNationById(final int nationId) {
        return this.m_nations.get(nationId);
    }
    
    public TIntObjectIterator<Nation> nationsIterator() {
        return this.m_nations.iterator();
    }
    
    public TIntObjectIterator<Nation> realNationIterator(final Integer... additionalExclude) {
        return new FilteredNationIterator(this.m_nations, (ImmutableSet<Integer>)ImmutableSet.builder().add((Object[])additionalExclude).add((Object)0).build());
    }
    
    public int[] getNationsId() {
        return this.m_nations.keys();
    }
    
    public int getMemberNationId(final long characterId) {
        final TIntObjectIterator<Nation> it = this.realNationIterator(new Integer[0]);
        while (it.hasNext()) {
            it.advance();
            final Nation nation = it.value();
            if (nation.getCitizen(characterId) != null) {
                return nation.getNationId();
            }
        }
        return 0;
    }
    
    static {
        INSTANCE = new NationManager();
        m_logger = Logger.getLogger((Class)NationManager.class);
    }
    
    private static class FilteredNationIterator extends TIntObjectIterator<Nation>
    {
        private final ImmutableSet<Integer> m_excludedNationIds;
        private boolean m_hasAdvanced;
        private int m_nationId;
        private Nation m_nation;
        
        FilteredNationIterator(final TIntObjectHashMap<Nation> map, final ImmutableSet<Integer> excludedNationIds) {
            super(map);
            this.m_hasAdvanced = false;
            this.m_excludedNationIds = excludedNationIds;
        }
        
        @Override
        public void advance() {
            this.m_hasAdvanced = false;
            this.m_nationId = super.key();
            this.m_nation = super.value();
        }
        
        @Override
        public boolean hasNext() {
            if (super.hasNext() && !this.m_hasAdvanced) {
                super.advance();
                while (this.m_excludedNationIds.contains((Object)super.key()) && super.hasNext()) {
                    super.advance();
                }
                this.m_hasAdvanced = true;
            }
            return super.hasNext();
        }
        
        @Override
        public int key() {
            return this.m_nationId;
        }
        
        @Override
        public Nation value() {
            return this.m_nation;
        }
        
        @Override
        public String toString() {
            return "FilteredNationIterator{m_excludedNationIds=" + this.m_excludedNationIds + '}';
        }
    }
}
