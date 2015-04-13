package com.ankamagames.wakfu.client.core.game.item.gem;

import com.ankamagames.wakfu.common.game.item.gems.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.item.loot.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class GemsDefinitionManager extends FullGemsDefinitionManager<ReferenceItem>
{
    private static final Logger m_logger;
    public static final GemsDefinitionManager INSTANCE;
    
    @Override
    protected ReferenceItem createGem(final int refId, final byte perfectionIndex) {
        final MetaItem metaItem = MetaItemManager.INSTANCE.get(refId);
        if (metaItem == null) {
            GemsDefinitionManager.m_logger.warn((Object)("[GemsDefinitionManager] La m\u00e9ta-gemme d'id " + refId + " n'existe pas !"));
            return null;
        }
        final int[] subIds = metaItem.getSubIds();
        if (perfectionIndex < 0 || perfectionIndex >= subIds.length) {
            GemsDefinitionManager.m_logger.warn((Object)("[GemsDefinitionManager] La m\u00e9ta-gemme d'id " + refId + " n'a pas de sub-meta d'index " + perfectionIndex));
            return null;
        }
        return ReferenceItemManager.getInstance().getReferenceItem(subIds[perfectionIndex]);
    }
    
    public void forEach(final TIntProcedure procedure) {
        this.m_metaGemsLists.forEachValue(new TObjectProcedure<LootList>() {
            @Override
            public boolean execute(final LootList object) {
                for (int i = 0, size = object.size(); i < size; ++i) {
                    final Loot loot = object.get(i);
                    if (!procedure.execute(loot.getReferenceId())) {
                        return true;
                    }
                }
                return true;
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)GemsDefinitionManager.class);
        INSTANCE = new GemsDefinitionManager();
    }
}
