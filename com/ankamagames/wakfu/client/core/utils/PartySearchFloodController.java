package com.ankamagames.wakfu.client.core.utils;

import gnu.trove.*;

public class PartySearchFloodController
{
    public static final PartySearchFloodController m_instance;
    private static final int MAX_INVITATION_NUMBER = 4;
    private final TLongByteHashMap m_historic;
    
    public PartySearchFloodController() {
        super();
        this.m_historic = new TLongByteHashMap();
    }
    
    public static PartySearchFloodController getInstance() {
        return PartySearchFloodController.m_instance;
    }
    
    public boolean isFlood(final long characterId) {
        final byte actualNb = this.m_historic.get(characterId);
        this.m_historic.put(characterId, (byte)(actualNb + 1));
        return actualNb >= 4;
    }
    
    public void removeEntry(final long characterId) {
        this.m_historic.remove(characterId);
    }
    
    static {
        m_instance = new PartySearchFloodController();
    }
}
