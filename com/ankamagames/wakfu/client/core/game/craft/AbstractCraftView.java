package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;

public abstract class AbstractCraftView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String HAS_BOOK_FIELD = "hasBook";
    public static final String BOOK_ICON_URL_FIELD = "bookIconUrl";
    public static final String BUTTON_ICON_URL_FIELD = "buttonIconUrl";
    public static final String IS_INNATE_FIELD = "isInnate";
    public static final String IS_CONCEPTUAL_FIELD = "isConceptual";
    public static final String IS_UNKNOWN = "isUnknown";
    public static final String ID_STRING = "idString";
    protected int m_refCraftId;
    private ReferenceItem m_bookItem;
    
    protected AbstractCraftView(final int refCraftId) {
        super();
        this.m_refCraftId = refCraftId;
        this.m_bookItem = ReferenceItemManager.getInstance().getReferenceItem(CraftManager.INSTANCE.getCraft(this.m_refCraftId).getLearningBookId());
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("hasBook")) {
            return this.m_bookItem != null;
        }
        if (fieldName.equals("bookIconUrl")) {
            return (this.m_bookItem != null) ? this.m_bookItem.getFieldValue("iconUrl") : null;
        }
        if (fieldName.equals("buttonIconUrl")) {
            return WakfuConfiguration.getInstance().getSkillSmallIcon(this.m_refCraftId);
        }
        if (fieldName.equals("isUnknown")) {
            return this.isUnknown();
        }
        if (fieldName.equals("isInnate")) {
            return this.isInnate();
        }
        if (fieldName.equals("isConceptual")) {
            return this.isConceptual();
        }
        if (fieldName.equals("idString")) {
            return String.valueOf(this.m_refCraftId);
        }
        return null;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(43, this.m_refCraftId, new Object[0]);
    }
    
    private boolean isInnate() {
        return CraftManager.INSTANCE.getCraft(this.m_refCraftId).isInnate();
    }
    
    private boolean isConceptual() {
        return CraftManager.INSTANCE.getCraft(this.m_refCraftId).isConceptualCraft();
    }
    
    public void displayBook() {
        if (this.m_bookItem != null) {
            ((OpenBackgroundDisplayItemAction)this.m_bookItem.getItemAction()).display();
        }
    }
    
    public int getCraftReferenceId() {
        return this.m_refCraftId;
    }
    
    public abstract short getLevel();
    
    public abstract boolean isUnknown();
    
    public abstract boolean hasRecipes();
    
    public abstract boolean hasHarvests();
}
