package com.ankamagames.wakfu.common.datas.specific;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public abstract class AbstractMonsterTimedPropertyManager
{
    protected static final Logger m_logger;
    private static final TLongObjectHashMap<TByteLongHashMap> m_states;
    
    protected long addProperty(final BasicCharacterInfo character, final WorldPropertyType worldPropertyType) {
        if (character != null) {
            final long characterId = character.getId();
            TByteLongHashMap states = AbstractMonsterTimedPropertyManager.m_states.get(characterId);
            if (states == null) {
                states = new TByteLongHashMap(3);
                AbstractMonsterTimedPropertyManager.m_states.put(characterId, states);
            }
            final long old = states.put(worldPropertyType.getId(), System.currentTimeMillis());
            AbstractMonsterTimedPropertyManager.m_states.put(characterId, states);
            character.addProperty(worldPropertyType);
            return old;
        }
        return 0L;
    }
    
    public byte[] getStateIds(final long monsterId) {
        final TByteLongHashMap states = AbstractMonsterTimedPropertyManager.m_states.get(monsterId);
        if (states != null) {
            return states.keys();
        }
        return null;
    }
    
    public static boolean containState(final long monsterId, final WorldPropertyType state) {
        final TByteLongHashMap states = AbstractMonsterTimedPropertyManager.m_states.get(monsterId);
        return states != null && states.containsKey(state.getId());
    }
    
    public static long getStateStartTime(final long monsterId, final WorldPropertyType state) {
        final TByteLongHashMap infos = AbstractMonsterTimedPropertyManager.m_states.get(monsterId);
        long startTime = 0L;
        if (infos != null) {
            startTime = infos.get(state.getId());
        }
        return startTime;
    }
    
    public void removeAllStates(final long monsterId) {
        final TByteLongHashMap infos = AbstractMonsterTimedPropertyManager.m_states.get(monsterId);
        if (infos != null) {
            infos.clear();
        }
    }
    
    public long removeProperty(final BasicCharacterInfo character, final WorldPropertyType worldPropertyType) {
        if (character != null) {
            final TByteLongHashMap infos = AbstractMonsterTimedPropertyManager.m_states.get(character.getId());
            long startTime = 0L;
            if (infos != null) {
                startTime = infos.remove(worldPropertyType.getId());
            }
            character.removeProperty(worldPropertyType);
            return startTime;
        }
        return 0L;
    }
    
    public TByteLongHashMap removeEntry(final long id) {
        return AbstractMonsterTimedPropertyManager.m_states.remove(id);
    }
    
    public void removeAll() {
        AbstractMonsterTimedPropertyManager.m_states.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractMonsterTimedPropertyManager.class);
        m_states = new TLongObjectHashMap<TByteLongHashMap>();
    }
}
