package com.ankamagames.wakfu.common.datas.specific.symbiot;

import org.apache.log4j.*;
import gnu.trove.*;

final class SymbiotFightData
{
    private static final Logger m_logger;
    private final TByteIntHashMap m_nbTurnPlayedByCreatureId;
    private int m_nbTurnsPlayedBySummoner;
    
    SymbiotFightData() {
        super();
        this.m_nbTurnPlayedByCreatureId = new TByteIntHashMap();
        this.m_nbTurnsPlayedBySummoner = 0;
    }
    
    public int getNbTurnsPlayedByCreature(final byte index) {
        return this.m_nbTurnPlayedByCreatureId.get(index);
    }
    
    public int getNbTurnsPlayedBySummoner() {
        return this.m_nbTurnsPlayedBySummoner;
    }
    
    public void addTurnForCreature(final byte index) {
        this.m_nbTurnPlayedByCreatureId.adjustOrPutValue(index, 1, 1);
    }
    
    public void incrementTurnPlayedBySummoner() {
        ++this.m_nbTurnsPlayedBySummoner;
    }
    
    public SymbiotXpModification getCreatureXp(final long summonerXp) {
        final SymbiotXpModification res = new SymbiotXpModification();
        if (this.m_nbTurnsPlayedBySummoner == 0) {
            SymbiotFightData.m_logger.warn((Object)"[SYMBIOT] On cherche a recuperer l'xp des creatures d'un symbiote, mais le summoner a jou\u00e9 0 tour");
            return res;
        }
        final TByteIntIterator it = this.m_nbTurnPlayedByCreatureId.iterator();
        while (it.hasNext()) {
            it.advance();
            res.setCreatureXp(it.key(), summonerXp * it.value() / this.m_nbTurnsPlayedBySummoner);
        }
        return res;
    }
    
    public void reset() {
        this.m_nbTurnPlayedByCreatureId.clear();
        this.m_nbTurnsPlayedBySummoner = 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SymbiotFightData.class);
    }
}
