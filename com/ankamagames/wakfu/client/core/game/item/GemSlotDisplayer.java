package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import java.util.*;

public class GemSlotDisplayer extends ImmutableFieldProvider
{
    private static final String[] FIELDS;
    public static final String STYLE_PATTERN = "gem%sWhite";
    public static final String STYLE_PATTERN_2 = "gem%sBrown";
    public static final String GEM_STYLE = "gemStyle";
    public static final String GEM_STYLE_2 = "gemStyle2";
    public static final String INDEX = "index";
    public static final String HAS_GEM = "hasGem";
    public static final String GEM_TYPE_DESCRIPTION = "gemTypeDescription";
    private Item m_holder;
    private ReferenceItem m_gemItem;
    private final byte m_index;
    
    public GemSlotDisplayer(final Item holder, final int gemItemId, final byte index) {
        super();
        this.m_holder = holder;
        this.setGemItem(gemItemId, false);
        this.m_index = index;
    }
    
    @Override
    public String[] getFields() {
        return GemSlotDisplayer.FIELDS;
    }
    
    public void setGemItem(final int gemItemId) {
        this.setGemItem(gemItemId, true);
    }
    
    public Item getHolder() {
        return this.m_holder;
    }
    
    public byte getIndex() {
        return this.m_index;
    }
    
    public ReferenceItem getGemItem() {
        return this.m_gemItem;
    }
    
    private void setGemItem(final int gemItemId, final boolean updateProperties) {
        this.m_gemItem = ReferenceItemManager.getInstance().getReferenceItem(gemItemId);
        if (updateProperties) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, ReferenceItemDisplayer.FIELDS);
        }
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("gemStyle")) {
            if (this.m_holder == null) {
                return null;
            }
            return String.format("gem%sWhite", this.m_holder.getReferenceItem().getGemType().name());
        }
        else if (fieldName.equals("gemStyle2")) {
            if (this.m_holder == null) {
                return null;
            }
            return String.format("gem%sBrown", this.m_holder.getReferenceItem().getGemType().name());
        }
        else {
            if (fieldName.equals("index")) {
                return this.m_index;
            }
            if (fieldName.equals("hasGem")) {
                return this.m_gemItem != null;
            }
            if (fieldName.equals("gemTypeDescription")) {
                if (this.m_holder == null) {
                    return null;
                }
                return WakfuTranslator.getInstance().getString("gemType.description." + this.m_holder.getReferenceItem().getGemType().name());
            }
            else {
                if (this.m_gemItem == null) {
                    return null;
                }
                if (fieldName.equals("caracteristic")) {
                    final TextWidgetFormater sb = new TextWidgetFormater();
                    final ArrayList<String> characteristics = ReferenceItemDisplayer.getCharacteristicsString(this.m_gemItem, this.m_holder);
                    for (int i = 0, size = characteristics.size(); i < size; ++i) {
                        if (i != 0) {
                            sb.newLine();
                        }
                        sb.append(characteristics.get(i));
                    }
                    return sb.finishAndToString();
                }
                return this.m_gemItem.getFieldValue(fieldName);
            }
        }
    }
    
    List<String> getEffectDescription() {
        return (this.m_gemItem == null) ? Collections.emptyList() : ReferenceItemDisplayer.getCharacteristicsString(this.m_gemItem, this.m_holder);
    }
    
    @Override
    public String toString() {
        return "GemSlotDisplayer{m_gemItem=" + this.m_gemItem + ", m_index=" + this.m_index + '}';
    }
    
    static {
        FIELDS = new String[0];
    }
}
