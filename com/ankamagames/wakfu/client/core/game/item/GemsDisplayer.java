package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;

public class GemsDisplayer extends ImmutableFieldProvider
{
    private static final String GEMS_LIST = "gemsList";
    private static final String GEMS_EFFECTS = "gemsEffects";
    private static final String HAS_GEMS = "hasGems";
    private static final String IS_EDITABLE = "isEditable";
    private static final String[] FIELDS;
    private final Item m_holder;
    private final Gems m_gems;
    private final List<GemSlotDisplayer> m_gemSlotDisplayers;
    
    public GemsDisplayer(final Item holder, final Gems gems) {
        super();
        this.m_gemSlotDisplayers = new ArrayList<GemSlotDisplayer>();
        this.m_holder = holder;
        this.m_gems = gems;
        for (byte i = 0, size = this.m_gems.getSlotCount(); i < size; ++i) {
            this.m_gemSlotDisplayers.add(new GemSlotDisplayer(this.m_holder, this.m_gems.getGem(i), i));
        }
    }
    
    @Override
    public String[] getFields() {
        return GemsDisplayer.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("gemsList")) {
            return this.m_gemSlotDisplayers;
        }
        if (fieldName.equals("gemsEffects")) {
            return this.getEffectsDescription();
        }
        if (fieldName.equals("hasGems")) {
            return this.m_gems.getSlotCount() != 0;
        }
        if (fieldName.equals("isEditable")) {
            return this.m_gems.isEditable();
        }
        return null;
    }
    
    private ArrayList<String> getEffectsDescription() {
        final ArrayList<String> effects = new ArrayList<String>();
        for (int i = 0, size = this.m_gemSlotDisplayers.size(); i < size; ++i) {
            effects.addAll(this.m_gemSlotDisplayers.get(i).getEffectDescription());
        }
        if (!effects.isEmpty()) {
            effects.add(0, WakfuTranslator.getInstance().getString("effectOnEquip"));
        }
        return effects;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GemsDisplayer");
        sb.append("{m_gems=").append(this.m_gems);
        sb.append(", m_gemSlotDisplayers=").append(this.m_gemSlotDisplayers);
        sb.append('}');
        return sb.toString();
    }
    
    static {
        FIELDS = new String[] { "gemsList", "gemsEffects", "hasGems", "isEditable" };
    }
}
