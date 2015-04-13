package com.ankamagames.wakfu.client.core.world;

import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import gnu.trove.*;

public final class InteractiveElementDefinitionsManager
{
    private static final int DEFINITION_CACHE_SIZE = 80;
    private static final LRUCache<Long, TLongHashSet> m_definitions;
    
    static void loadDefinitionsForPartition(final LocalPartition partition, final InteractiveElementDef[] definitions) {
        final long mapId = MathHelper.getLongFromTwoInt(partition.getX(), partition.getY());
        final TLongHashSet infos = new TLongHashSet();
        for (int i = 0; i < definitions.length; ++i) {
            final InteractiveElementDef currentDef = definitions[i];
            WakfuClientInteractiveElementFactory.getInstance().addInfo(new InteractiveElementInfo(currentDef.m_id, currentDef.m_type, currentDef.m_data), currentDef.m_views);
            infos.add(currentDef.m_id);
        }
        InteractiveElementDefinitionsManager.m_definitions.put(mapId, infos);
    }
    
    static void spawnClientInteractiveElements(final LocalPartition partition, final InteractiveElementDef[] defs) {
        for (int i = 0; i < defs.length; ++i) {
            final InteractiveElementDef currentDef = defs[i];
            if (currentDef.m_clientOnly) {
                final WakfuClientMapInteractiveElement element = WakfuClientMapInteractiveElement.spawnNewElement(currentDef.m_id, null);
                partition.addInteractiveElement(element);
            }
        }
    }
    
    public static void unloadDefinitionsForPartition(final int mapX, final int mapY) {
        final long mapId = MathHelper.getLongFromTwoInt(mapX, mapY);
        final TLongHashSet set = InteractiveElementDefinitionsManager.m_definitions.get(mapId);
        if (set != null) {
            for (final long id : set) {
                WakfuClientInteractiveElementFactory.getInstance().removeInfo(id);
            }
        }
    }
    
    static {
        m_definitions = new LRUCache<Long, TLongHashSet>(80);
    }
}
