package com.ankamagames.wakfu.client.console;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;

public class ShortcutCategoryFieldProvider extends ImmutableFieldProvider
{
    public final String NAME_FIELD = "name";
    public final String SHORTCUTS_FIELD = "shorctuts";
    private final String[] FIELDS;
    private ArrayList<ShortcutFieldProvider> m_shortcuts;
    private final String m_name;
    
    public ShortcutCategoryFieldProvider(final String name) {
        super();
        this.FIELDS = new String[] { "name", "shorctuts" };
        this.m_shortcuts = new ArrayList<ShortcutFieldProvider>();
        this.m_name = name;
    }
    
    public void setShortcuts(final ArrayList<ShortcutFieldProvider> shortcuts) {
        this.m_shortcuts = shortcuts;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString(this.m_name);
        }
        if (fieldName.equals("shorctuts")) {
            return this.m_shortcuts;
        }
        return null;
    }
}
