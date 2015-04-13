package com.ankamagames.wakfu.common.game.group.partySearch;

import gnu.trove.*;

public class PartyOccupationManager
{
    public static final PartyOccupationManager INSTANCE;
    private final TLongObjectHashMap<PartyOccupation> m_occupations;
    
    private PartyOccupationManager() {
        super();
        this.m_occupations = new TLongObjectHashMap<PartyOccupation>();
    }
    
    public void registerPartyOccupation(final PartyOccupation occupation) {
        this.m_occupations.put(occupation.getId(), occupation);
    }
    
    public PartyOccupation getPartyOccupation(final long id) {
        return this.m_occupations.get(id);
    }
    
    public boolean forEachOccupation(final TObjectProcedure<PartyOccupation> procedure) {
        return this.m_occupations.forEachValue(procedure);
    }
    
    @Override
    public String toString() {
        return "PartyOccupationManager{m_occupations=" + this.m_occupations.size() + '}';
    }
    
    static {
        INSTANCE = new PartyOccupationManager();
    }
}
