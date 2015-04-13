package com.ankamagames.wakfu.client.core.game.item.cosmetic;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.jetbrains.annotations.*;

public class CosmeticsItemView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String NO_COSTUMES = "noCostumes";
    private final ReferenceItem m_item;
    
    public CosmeticsItemView(final int refItemId) {
        super();
        this.m_item = ((refItemId > 0) ? ReferenceItemManager.getInstance().getReferenceItem(refItemId) : null);
    }
    
    public int getRefId() {
        return (this.m_item == null) ? 0 : this.m_item.getId();
    }
    
    @Override
    public String[] getFields() {
        return CosmeticsItemView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("noCostumes")) {
            return this.m_item == null;
        }
        if (this.m_item != null) {
            return this.m_item.getFieldValue(fieldName);
        }
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString("cosmetics.noCostumes");
        }
        if (fieldName.equals("iconUrl")) {
            try {
                return WakfuConfiguration.getContentPath("defaultIconPath");
            }
            catch (PropertyException e) {
                CosmeticsItemView.m_logger.info((Object)e.getMessage(), (Throwable)e);
            }
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CosmeticsItemView.class);
    }
}
