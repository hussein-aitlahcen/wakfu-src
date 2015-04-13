package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import java.util.*;

public class BlindBoxView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String LOOT_LIST_FIELD = "lootList";
    public static final String FINIHED_FIELD = "finished";
    public static final String SELECTED_ITEM_FIELD = "selectedItem";
    public static final String MAX_COLUMNS_FIELD = "maxColumns";
    public static final String[] FIELDS;
    private boolean m_finished;
    private ReferenceItem m_selectedItem;
    private int m_maxColumns;
    private final ArrayList<FakeItem> m_lootList;
    private final String m_name;
    
    @Override
    public String[] getFields() {
        return BlindBoxView.FIELDS;
    }
    
    public BlindBoxView(final String name, final ArrayList<FakeItem> lootList) {
        super();
        this.m_lootList = lootList;
        this.m_name = name;
        final int size = this.m_lootList.size();
        final float sqrt = MathHelper.sqrt(size);
        final int round = Math.round(sqrt);
        int tempMaxCol = -1;
        if (size % 10 == 0 && size / 10 > 1) {
            tempMaxCol = 10;
        }
        else if (size % 5 == 0 && size / 5 > 1) {
            tempMaxCol = 5;
        }
        else if (size % 3 == 0 && size / 3 > 1) {
            tempMaxCol = 3;
        }
        if (tempMaxCol == -1 || Math.abs(tempMaxCol - round) > 2) {
            tempMaxCol = round;
        }
        this.m_maxColumns = tempMaxCol;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("lootList")) {
            return this.m_lootList;
        }
        if (fieldName.equals("finished")) {
            return this.m_finished;
        }
        if (fieldName.equals("selectedItem")) {
            return this.m_selectedItem;
        }
        if (fieldName.equals("maxColumns")) {
            return this.m_maxColumns;
        }
        return null;
    }
    
    public void setFinished(final boolean finished) {
        this.m_finished = finished;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "finished");
    }
    
    public int indexOf(final int referenceId) {
        for (final FakeItem fi : this.m_lootList) {
            if (fi.getReferenceId() == referenceId) {
                return this.m_lootList.indexOf(fi);
            }
        }
        return -1;
    }
    
    public int lootListSize() {
        return this.m_lootList.size();
    }
    
    public void setCurrentOffset(final float offset) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedItem");
    }
    
    static {
        FIELDS = new String[] { "name", "lootList", "finished", "selectedItem" };
    }
}
