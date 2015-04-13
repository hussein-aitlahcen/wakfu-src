package com.ankamagames.wakfu.client.core.game.shortcut;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.console.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;

public class ShortcutBar implements FieldProvider
{
    private static final Logger m_logger;
    public static final String SHORTCUTS_FIELD = "shortcuts";
    public static final String NAME_FIELD = "name";
    public static final String TYPE_FIELD = "type";
    public static final String INDEX_FIELD = "index";
    public static final String INDEX_TEXT_FIELD = "indexText";
    public static final String KEY_LIST_FIELD = "keyList";
    public static final String OPENED_FIELD = "opened";
    public static final String[] FIELDS;
    private ShortcutInventory<ShortCutItem> m_shortCutItems;
    private TByteObjectHashMap<ShortCutItem> m_toAdd;
    private TByteObjectHashMap<ShortCutItem> m_toRemove;
    private TByteObjectHashMap<ShortCutItem> m_toDestroy;
    private boolean m_opened;
    private boolean m_vertical;
    private byte m_index;
    
    public ShortcutBar(final ShortCutBarType type, final byte index) {
        super();
        this.m_toAdd = new TByteObjectHashMap<ShortCutItem>();
        this.m_toRemove = new TByteObjectHashMap<ShortCutItem>();
        this.m_toDestroy = new TByteObjectHashMap<ShortCutItem>();
        this.m_opened = false;
        this.m_vertical = false;
        this.m_shortCutItems = new ShortcutInventory<ShortCutItem>(type, ShortCutItemProvider.getInstance(), type.getChecker(), type.getMaxSize(), false);
        this.m_index = index;
    }
    
    public ShortCutBarType getType() {
        return this.m_shortCutItems.getType();
    }
    
    public boolean isPositionFree(final short position) {
        return this.m_shortCutItems != null && this.m_shortCutItems.isPositionFree(position);
    }
    
    public short getPosition(final ShortCutItem item) {
        if (this.m_shortCutItems == null) {
            return -1;
        }
        return this.m_shortCutItems.getPosition(item);
    }
    
    public short getFirstFreeIndex() {
        if (this.m_shortCutItems == null) {
            return -1;
        }
        return this.m_shortCutItems.getFirstFreeIndex();
    }
    
    public ShortCutItem getFromPosition(final short position) {
        if (this.m_shortCutItems == null) {
            return null;
        }
        return this.m_shortCutItems.getFromPosition(position);
    }
    
    public ArrayList<ShortCutItem> getAllWithReferenceId(final int refId) {
        return this.m_shortCutItems.getAllWithReferenceId(refId);
    }
    
    public ShortCutItem getFirstWithReferenceId(final int id) {
        if (this.m_shortCutItems == null) {
            return null;
        }
        return this.m_shortCutItems.getFirstWithReferenceId(id);
    }
    
    public ShortCutItem getWithUniqueId(final long uid) {
        if (this.m_shortCutItems == null) {
            return null;
        }
        return this.m_shortCutItems.getWithUniqueId(uid);
    }
    
    public boolean canAdd(final ShortCutItem item) {
        try {
            return this.m_shortCutItems.canAdd(item);
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public boolean setShortcutItemAt(final ShortCutItem item, final short position) {
        if (this.m_shortCutItems == null) {
            return false;
        }
        if (this.m_shortCutItems.getWithUniqueId(item.getUniqueId()) != null) {
            final String errorMsg = WakfuTranslator.getInstance().getString("error.shortcutItemAlreadyPresent");
            final ChatMessage chatErrorMsg = new ChatMessage(errorMsg);
            chatErrorMsg.setPipeDestination(3);
            ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg);
            return false;
        }
        try {
            short pos;
            if ((pos = position) == -1) {
                for (pos = 0; pos < this.m_shortCutItems.getMaximumSize(); ++pos) {
                    if (this.m_shortCutItems.isPositionFree(pos)) {
                        break;
                    }
                }
            }
            else if (!this.m_shortCutItems.isPositionFree(position)) {
                this.removeShortcutItemAt(position, true);
            }
            if (pos >= this.m_shortCutItems.getMaximumSize()) {
                return false;
            }
            final boolean added = this.m_shortCutItems.addAt(item, pos);
            if (added) {
                final byte offset = (byte)pos;
                boolean add = true;
                if (this.m_toRemove.get(offset) == item) {
                    add = false;
                    this.m_toRemove.remove(offset);
                }
                if (add) {
                    this.m_toAdd.put(offset, item);
                }
            }
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "shortcuts");
            return added;
        }
        catch (InventoryCapacityReachedException e) {
            final String errorMsg2 = WakfuTranslator.getInstance().getString("error.shortcutItemDropNotPossible");
            final ChatMessage chatErrorMsg2 = new ChatMessage(errorMsg2);
            chatErrorMsg2.setPipeDestination(3);
            ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg2);
        }
        catch (ContentAlreadyPresentException e2) {
            final String errorMsg2 = WakfuTranslator.getInstance().getString("error.shortcutItemAlreadyPresent");
            final ChatMessage chatErrorMsg2 = new ChatMessage(errorMsg2);
            chatErrorMsg2.setPipeDestination(3);
            ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg2);
        }
        catch (PositionAlreadyUsedException e3) {
            final String errorMsg2 = WakfuTranslator.getInstance().getString("error.shortcutItemDropNotPossible");
            final ChatMessage chatErrorMsg2 = new ChatMessage(errorMsg2);
            chatErrorMsg2.setPipeDestination(3);
            ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg2);
        }
        return false;
    }
    
    public boolean removeShortcutItemAt(final short position, boolean destroy) {
        if (this.m_shortCutItems == null) {
            return false;
        }
        if (!this.m_shortCutItems.isPositionFree(position)) {
            final ShortCutItem item = this.m_shortCutItems.getFromPosition(position);
            final byte offset = (byte)position;
            boolean remove = true;
            if (this.m_toAdd.get(offset) == item) {
                this.m_toAdd.remove(offset);
                remove = false;
            }
            if (remove) {
                if (destroy) {
                    this.m_toDestroy.put(offset, item);
                    destroy = false;
                }
                else {
                    this.m_toRemove.put(offset, item);
                }
            }
            boolean ret;
            if (destroy) {
                ret = this.m_shortCutItems.destroyAt(position);
            }
            else {
                ret = (this.m_shortCutItems.removeAt(position) != null);
            }
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "shortcuts");
            return ret;
        }
        return false;
    }
    
    public void removeShortcutItemWithId(final int referenceId, final ShortCutType type, final boolean destroy) {
        final ArrayList<ShortCutItem> items = this.m_shortCutItems.getAllWithReferenceId(referenceId);
        if (items != null) {
            for (int j = items.size() - 1; j >= 0; --j) {
                final ShortCutItem item = items.get(j);
                if (item != null && item.getType() == type) {
                    this.removeShortcutItemAt(this.m_shortCutItems.getPosition(item.getUniqueId()), destroy);
                }
            }
        }
    }
    
    public boolean remove(final ShortCutItem shortcutItem) {
        return this.m_shortCutItems != null && this.m_shortCutItems.remove(shortcutItem);
    }
    
    public void setupRemovedShortcutToDestroy(final ShortCutItem item, final short position) {
        final byte offset = (byte)position;
        if (this.m_toRemove.get(offset) != null) {
            this.m_toDestroy.put(offset, item);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "shortcuts");
    }
    
    public boolean needsToSendUpdateToServer() {
        return !this.m_toAdd.isEmpty() || !this.m_toDestroy.isEmpty() || !this.m_toRemove.isEmpty();
    }
    
    public void cleanChangesForServer() {
        this.m_toAdd.clear();
        this.m_toRemove.clear();
        final TByteObjectIterator<ShortCutItem> it = this.m_toDestroy.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().release();
        }
        this.m_toDestroy.clear();
    }
    
    public TByteObjectIterator<ShortCutItem> toAddIterator() {
        return this.m_toAdd.iterator();
    }
    
    public TByteObjectIterator<ShortCutItem> toRemoveIterator() {
        return this.m_toRemove.iterator();
    }
    
    public TByteObjectIterator<ShortCutItem> toDestroyIterator() {
        return this.m_toDestroy.iterator();
    }
    
    public void runProcedure(final ShortcutItemProcedure procedure) {
        final Iterator<ShortCutItem> it = this.m_shortCutItems.iterator();
        while (it.hasNext()) {
            procedure.execute(it.next(), this);
        }
    }
    
    public void clean() {
        this.m_shortCutItems.removeAll();
    }
    
    public void updateShortcuts() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "shortcuts");
    }
    
    public void updateShortcutsUsable() {
        for (final ShortCutItem shortCutItem : this.m_shortCutItems) {
            PropertiesProvider.getInstance().firePropertyValueChanged(shortCutItem, "usable");
        }
    }
    
    @Override
    public String[] getFields() {
        return ShortcutBar.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("shortcuts")) {
            return this.m_shortCutItems.toArray(new ShortCutItem[this.m_shortCutItems.getMaximumSize()]);
        }
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString("desc.shortcut.type." + this.getType().ordinal());
        }
        if (fieldName.equals("type")) {
            return this.getType().ordinal();
        }
        if (fieldName.equals("index")) {
            return this.m_index;
        }
        if (fieldName.equals("indexText")) {
            return (this.getType() == ShortCutBarType.SYMBIOT_BAR) ? 1 : String.valueOf(this.getReadableIndex());
        }
        if (fieldName.equals("keyList")) {
            return this.getKeyList();
        }
        if (fieldName.equals("opened")) {
            return this.m_opened;
        }
        return null;
    }
    
    private int getReadableIndex() {
        return this.m_index - WakfuGameEntity.getInstance().getLocalPlayer().getShortcutBarManager().getShortcutBar(this.m_index).getType().getFirstIndex() + 1;
    }
    
    private ArrayList<String> getKeyList() {
        final ArrayList<String> keys = new ArrayList<String>();
        final short length = this.m_shortCutItems.getMaximumSize();
        final int shortcutBarIndex = this.getReadableIndex();
        for (int i = 0; i < length; ++i) {
            final ShortcutFieldProvider value = (ShortcutFieldProvider)ShortcutsFieldProvider.getInstance().getFieldValue("shortcut" + String.valueOf(shortcutBarIndex - 1) + String.valueOf(i));
            keys.add(value.getKeyText());
        }
        return keys;
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
    
    public boolean onDataChanged(final RawShortcutInventory raw) {
        return this.m_shortCutItems.fromRaw(raw);
    }
    
    public void removeShortcutItemWithUniqueId(final long uniqueId, final boolean destroy) {
        final ShortCutItem item = this.m_shortCutItems.getWithUniqueId(uniqueId);
        if (item != null) {
            this.removeShortcutItemAt(this.m_shortCutItems.getPosition(item.getUniqueId()), destroy);
        }
    }
    
    public boolean isEmpty() {
        return this.m_shortCutItems.isEmpty();
    }
    
    public boolean isFull() {
        return this.m_shortCutItems.isFull();
    }
    
    public byte getIndex() {
        return this.m_index;
    }
    
    public void setIndex(final byte index) {
        this.m_index = index;
    }
    
    public boolean isOpened() {
        return this.m_opened;
    }
    
    public void setOpened(final boolean opened) {
        this.m_opened = opened;
    }
    
    public boolean isVertical() {
        return this.m_vertical;
    }
    
    public void setVertical(final boolean vertical) {
        this.m_vertical = vertical;
    }
    
    public void setShortcutsUsable(final boolean usable) {
        for (final ShortCutItem shortCutItem : this.m_shortCutItems) {
            shortCutItem.setUsable(usable);
            PropertiesProvider.getInstance().firePropertyValueChanged(shortCutItem, "usable");
        }
    }
    
    public void release() {
        this.m_shortCutItems.cleanup();
    }
    
    public ShortcutInventory<ShortCutItem> getShortCutItems() {
        return this.m_shortCutItems;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ShortcutBar.class);
        FIELDS = new String[] { "shortcuts", "name", "type", "index", "indexText", "keyList", "opened" };
    }
}
