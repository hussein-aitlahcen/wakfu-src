package com.ankamagames.wakfu.client.core.game.companion;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class CompanionItemManager
{
    private static final Logger m_logger;
    public static final CompanionItemManager INSTANCE;
    private final TIntIntHashMap m_breedIdByItemId;
    
    public CompanionItemManager() {
        super();
        this.m_breedIdByItemId = new TIntIntHashMap();
    }
    
    public void addEntry(final int itemId) {
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(itemId);
        if (!(referenceItem.getItemAction() instanceof CompanionActivationItemAction)) {
            CompanionItemManager.m_logger.warn((Object)"Tentative d'ajout d'un item avec la mauvaise action");
            return;
        }
        final CompanionActivationItemAction action = (CompanionActivationItemAction)referenceItem.getItemAction();
        this.m_breedIdByItemId.put(itemId, action.getCompanionBreedId());
    }
    
    public int getBreedId(final int itemId) {
        return this.m_breedIdByItemId.get(itemId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionItemManager.class);
        INSTANCE = new CompanionItemManager();
    }
}
