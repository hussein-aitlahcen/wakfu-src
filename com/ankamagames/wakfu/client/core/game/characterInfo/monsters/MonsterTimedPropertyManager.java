package com.ankamagames.wakfu.client.core.game.characterInfo.monsters;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MonsterTimedPropertyManager extends AbstractMonsterTimedPropertyManager
{
    private static final MonsterTimedPropertyManager m_instance;
    
    public static MonsterTimedPropertyManager getInstance() {
        return MonsterTimedPropertyManager.m_instance;
    }
    
    public long addPropertyAndUpdate(final NonPlayerCharacter npc, final WorldPropertyType state) {
        if (state == WorldPropertyType.BUSY) {
            closeMRU(npc);
        }
        return super.addProperty(npc, state);
    }
    
    private static void closeMRU(final NonPlayerCharacter npc) {
        final MRU mru = UIMRUFrame.getInstance().getCurrentMRU();
        if (mru == null) {
            return;
        }
        for (int i = 0, size = mru.getSourcesCount(); i < size; ++i) {
            final MRUable source = mru.getSource(i);
            if (source instanceof NonPlayerCharacter && ((NonPlayerCharacter)source).getId() == npc.getId()) {
                UIMRUFrame.getInstance().closeCurrentMRU();
                break;
            }
        }
    }
    
    static {
        m_instance = new MonsterTimedPropertyManager();
    }
}
