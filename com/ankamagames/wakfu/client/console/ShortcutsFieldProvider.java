package com.ankamagames.wakfu.client.console;

import com.ankamagames.framework.reflect.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.ui.shortcuts.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import gnu.trove.*;

public class ShortcutsFieldProvider implements FieldProvider
{
    public static final String SHORTCUTS_FIELD = "shortcuts";
    public static final String BIND_NUMBER_LIST_FIELD = "bindNumberList";
    public static final String CURRENT_BIND_FIELD = "currentBind";
    public static final String BINDS_FIELD = "binds";
    private HashMap<String, ShortcutFieldProvider> m_shortcuts;
    private TLongObjectHashMap<String> m_shortcutsKeys;
    private ArrayList<Long> m_reservedKeys;
    private ShortcutFieldProvider m_currentBind;
    private static short m_bindId;
    private TLongObjectHashMap<ShortcutCategoryFieldProvider> m_categories;
    private final String[] FIELDS;
    private static final ShortcutsFieldProvider m_instance;
    
    public ShortcutsFieldProvider() {
        super();
        this.m_shortcuts = new HashMap<String, ShortcutFieldProvider>();
        this.m_shortcutsKeys = new TLongObjectHashMap<String>();
        this.m_reservedKeys = new ArrayList<Long>();
        this.m_categories = new TLongObjectHashMap<ShortcutCategoryFieldProvider>();
        this.FIELDS = new String[] { "shortcuts", "bindNumberList", "currentBind", "binds" };
    }
    
    public static ShortcutsFieldProvider getInstance() {
        return ShortcutsFieldProvider.m_instance;
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("shortcuts")) {
            final ArrayList<ShortcutCategoryFieldProvider> categories = new ArrayList<ShortcutCategoryFieldProvider>();
            final TLongObjectIterator<ShortcutCategoryFieldProvider> it = this.m_categories.iterator();
            while (it.hasNext()) {
                it.advance();
                final ArrayList<ShortcutFieldProvider> shortcuts = new ArrayList<ShortcutFieldProvider>();
                final ShortcutCategoryFieldProvider fieldProvider = it.value();
                for (final ShortcutFieldProvider provider : this.m_shortcuts.values()) {
                    final Shortcut shortcut = provider.getShortcut();
                    if (shortcut != null && shortcut.getCategory() != null) {
                        if (!fieldProvider.getName().equals(shortcut.getCategory())) {
                            continue;
                        }
                        shortcuts.add(provider);
                    }
                }
                Collections.sort(shortcuts, new Comparator<ShortcutFieldProvider>() {
                    @Override
                    public int compare(final ShortcutFieldProvider o1, final ShortcutFieldProvider o2) {
                        return o1.getId().compareTo(o2.getId());
                    }
                });
                fieldProvider.setShortcuts(shortcuts);
                categories.add(fieldProvider);
            }
            return categories;
        }
        if (fieldName.equals("currentBind")) {
            return this.m_currentBind;
        }
        if (fieldName.equals("binds")) {
            return this.getShortcutsOfGroup("binding");
        }
        return this.m_shortcuts.get(fieldName);
    }
    
    private ArrayList<ShortcutFieldProvider> getShortcutsOfGroup(final String group) {
        final ArrayList<ShortcutFieldProvider> shortcuts = new ArrayList<ShortcutFieldProvider>();
        for (final ShortcutFieldProvider shortcutFieldProvider : this.m_shortcuts.values()) {
            if (shortcutFieldProvider.getGroupName().equalsIgnoreCase(group)) {
                shortcuts.add(shortcutFieldProvider);
            }
        }
        return shortcuts;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    private boolean isAllowedGroup(final String groupName) {
        return groupName.equals("common") || groupName.equals("world") || groupName.equals("fight") || groupName.equals("binding") || groupName.equals("default");
    }
    
    public void initialize() {
        this.m_shortcuts.clear();
        this.m_shortcutsKeys.clear();
        this.m_categories.clear();
        ShortcutsFieldProvider.m_bindId = 0;
        final Shortcut[] arr$;
        final Shortcut[] shortcuts = arr$ = ShortcutManager.getInstance().getShortcuts();
        for (final Shortcut shortcut : arr$) {
            final String groupName = shortcut.getGroupName();
            if (shortcut.getId() != null && this.isAllowedGroup(groupName)) {
                if (groupName.equals("binding")) {
                    ++ShortcutsFieldProvider.m_bindId;
                }
                this.m_shortcuts.put(shortcut.getId(), new ShortcutFieldProvider(shortcut));
                this.m_shortcutsKeys.put(MathHelper.getLongFromTwoInt(shortcut.getKeyCode(), shortcut.getModiferMask()), shortcut.getId());
                final String category = shortcut.getCategory();
                if (category != null) {
                    final int key = category.hashCode();
                    if (!this.m_categories.containsKey(key)) {
                        this.m_categories.put(key, new ShortcutCategoryFieldProvider(category));
                    }
                }
                PropertiesProvider.getInstance().firePropertyValueChanged(this, shortcut.getId());
            }
            else {
                this.m_reservedKeys.add(MathHelper.getLongFromTwoInt(shortcut.getKeyCode(), shortcut.getModiferMask()));
            }
        }
        this.setCurrentBind(null);
    }
    
    public boolean isReservedKey(final long keyCode) {
        return this.m_reservedKeys.contains(keyCode);
    }
    
    public boolean shortcutAlreadyExists(final long keyCode) {
        return this.m_shortcutsKeys.containsKey(keyCode) || this.m_reservedKeys.contains(keyCode);
    }
    
    public ShortcutFieldProvider getShortcutFieldProviderFromKeyCode(final long keyCode) {
        final String effect = this.m_shortcutsKeys.get(keyCode);
        if (effect != null) {
            final ShortcutFieldProvider shortcutFieldProvider = this.m_shortcuts.get(effect);
            if (shortcutFieldProvider != null) {
                return shortcutFieldProvider;
            }
        }
        return null;
    }
    
    public void reflow() {
        this.initialize();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer != null) {
            final ShortcutBarManager shortcutBarManager = localPlayer.getShortcutBarManager();
            PropertiesProvider.getInstance().firePropertyValueChanged(shortcutBarManager.getSelectedShortcutBar(), "keyList");
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, this.FIELDS);
    }
    
    public void setCurrentBind(final ShortcutFieldProvider currentBind) {
        this.m_currentBind = currentBind;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentBind");
    }
    
    public ShortcutFieldProvider getCurrentBind() {
        return this.m_currentBind;
    }
    
    public Shortcut generateNewShortcutForBinding(final String text) {
        if (++ShortcutsFieldProvider.m_bindId > 20) {
            return null;
        }
        return new Shortcut("binding" + ShortcutsFieldProvider.m_bindId, -1, "!/common/bind ", false, false, false, false, false, text);
    }
    
    public void activateBindTextEdition(final boolean activate) {
        if (activate) {
            final ArrayList<ShortcutFieldProvider> shortcutsOfGroup = this.getShortcutsOfGroup("binding");
            if (shortcutsOfGroup.size() > 0) {
                this.setCurrentBind(shortcutsOfGroup.get(0));
            }
        }
        else {
            this.setCurrentBind(null);
        }
    }
    
    static {
        ShortcutsFieldProvider.m_bindId = 0;
        m_instance = new ShortcutsFieldProvider();
    }
}
