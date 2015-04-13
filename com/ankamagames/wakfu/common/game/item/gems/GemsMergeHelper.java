package com.ankamagames.wakfu.common.game.item.gems;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;

public class GemsMergeHelper
{
    private static final Logger m_logger;
    
    public static AbstractReferenceItem computeMergedItem(final AbstractReferenceItem refItem, final GemMergeType mergeType) {
        final GemElementType type = mergeType.computeElementType(refItem.getGemElementType());
        final short level = mergeType.computeLevel(refItem.getLevel());
        final ItemRarity rarity = mergeType.computeRarity(refItem.getRarity());
        return BasicGemsDefinitionManager.INSTANCE.getGem(type, rarity, level);
    }
    
    public static boolean canMerge(final BasicCharacterInfo player, final int refId, final GemMergeType mergeType, final boolean withLogs) {
        final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(refId);
        if (refItem.getGemElementType() == GemElementType.NONE) {
            if (withLogs) {
                GemsMergeHelper.m_logger.warn((Object)("Le joueur " + player + " demande une fusion de rune de type " + refItem.getGemElementType()));
            }
            return false;
        }
        final Collection<Item> gems = player.getBags().getAllWithReferenceId(refId);
        if (gems.isEmpty()) {
            if (withLogs) {
                GemsMergeHelper.m_logger.warn((Object)("Le joueur " + player + " essaye de fusionner comme rune l'item de refId" + refId + " qu'il ne poss\u00e8de pas"));
            }
            return false;
        }
        if (!mergeType.canMergeItem(refItem)) {
            if (withLogs) {
                GemsMergeHelper.m_logger.warn((Object)("Le joueur " + player + " essaye d'effectuer la fusion de type " + mergeType + " sur un objet d\u00e9j\u00e0 max\u00e9."));
            }
            return false;
        }
        int totalQty = 0;
        for (final Item gem : gems) {
            totalQty += gem.getQuantity();
        }
        if (totalQty < mergeType.getQuantityNeeded()) {
            if (withLogs) {
                GemsMergeHelper.m_logger.warn((Object)("Le joueur " + player + " essayer de fusionner comme rune l'item de refId " + refId + " mais n'en poss\u00e8de pas 2"));
            }
            return false;
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GemsMergeHelper.class);
    }
}
