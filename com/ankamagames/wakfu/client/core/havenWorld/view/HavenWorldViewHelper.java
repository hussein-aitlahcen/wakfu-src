package com.ankamagames.wakfu.client.core.havenWorld.view;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class HavenWorldViewHelper
{
    private static final Logger m_logger;
    
    public static String getCatalogEntryName(final HavenWorldCatalogEntry havenWorldCatalogEntry) {
        final WakfuTranslator translator = WakfuTranslator.getInstance();
        final short contentId = havenWorldCatalogEntry.getId();
        if (havenWorldCatalogEntry instanceof BuildingCatalogEntry) {
            return translator.getString(122, contentId, new Object[0]);
        }
        if (havenWorldCatalogEntry instanceof PatchCatalogEntry) {
            return translator.getString(124, contentId, new Object[0]);
        }
        if (havenWorldCatalogEntry instanceof PartitionCatalogEntry) {
            return translator.getString("haven.world.partition");
        }
        HavenWorldViewHelper.m_logger.error((Object)("Type de catalog entry inconnu " + havenWorldCatalogEntry.getClass()));
        return "<unknow>";
    }
    
    public static String getCatalogEntryName(final ModificationItem modificationItem) {
        return getCatalogEntryName(modificationItem.getCatalogEntry());
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldViewHelper.class);
    }
}
