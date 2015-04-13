package com.ankamagames.wakfu.client.core.game.shortcut;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.wakfu.client.ui.protocol.message.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ShortcutBarManager implements FieldProvider
{
    private static final Logger m_logger;
    public static final String CURRENT_BAR_TYPE_FIELD = "currentShortcutBarType";
    public static final String SELECTED_SHORTCUT_BAR_FIELD = "selectedShortcutBar";
    public static final String SELECTED_SHORTCUT_BAR_NUMBER_FIELD = "currentShortcutBarNumber";
    public static final String SELECTED_SHORTCUT_BAR_NUMBER_TEXT_FIELD = "currentShortcutBarNumberText";
    public static final String LEFT_HAND_WEAPON_SHORCUT_FIELD = "leftHandWeaponShortcut";
    public static final String RIGHT_HAND_WEAPON_SHORCUT_FIELD = "rightHandWeaponShortcut";
    public static final String CLOSED_SHORTCUT_BARS_FIELD = "closedShortcutBars";
    public static final String DISPLAY_SYMBIOT_BAR_FIELD = "displaySymbiotBar";
    public static final String[] FIELDS;
    private ShortcutBar[] m_shortcutBars;
    private boolean m_displaySymbiot;
    private byte m_selectedItemsShortCutBar;
    private byte m_selectedSpellsShortCutBar;
    private byte[] m_spellsMainShortcutBars;
    private byte[] m_itemsMainShortcutBars;
    private final ShortCutItem m_leftHandWeaponShortcut;
    private final ShortCutItem m_rightHandWeaponShortcut;
    private ShortCutBarType m_currentBarType;
    private final ArrayList<ShortcutBarTypeListener> m_shortcutBarTypeListeners;
    
    public ShortcutBarManager() {
        super();
        this.m_displaySymbiot = false;
        this.m_selectedItemsShortCutBar = 0;
        this.m_selectedSpellsShortCutBar = 0;
        this.m_currentBarType = ShortCutBarType.WORLD;
        this.m_shortcutBarTypeListeners = new ArrayList<ShortcutBarTypeListener>();
        final int totalCount = ShortCutBarType.getTotalBarCount();
        this.m_shortcutBars = new ShortcutBar[totalCount];
        byte idx = 0;
        for (final ShortCutBarType type : ShortCutBarType.values()) {
            final byte count = type.getCount();
            final ShortcutBar[] bars = new ShortcutBar[count];
            for (int i = 0; i < count; ++i) {
                switch (type) {
                    case SYMBIOT_BAR: {
                        bars[i] = new SymbiotShortcutBar(idx);
                        break;
                    }
                    default: {
                        bars[i] = new ShortcutBar(type, idx);
                        break;
                    }
                }
                this.m_shortcutBars[idx] = bars[i];
                ++idx;
            }
        }
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(2145);
        if (referenceItem != null) {
            this.m_rightHandWeaponShortcut = ShortCutItem.checkOut(ShortCutType.EQUIPMENT_SLOT, -1L, referenceItem.getId(), referenceItem.getGfxId());
            this.m_leftHandWeaponShortcut = ShortCutItem.checkOut(ShortCutType.EQUIPMENT_SLOT, -1L, -1, 0);
        }
        else {
            this.m_rightHandWeaponShortcut = null;
            this.m_leftHandWeaponShortcut = null;
        }
    }
    
    public void release() {
        if (this.m_rightHandWeaponShortcut != null) {
            this.m_rightHandWeaponShortcut.release();
        }
        if (this.m_leftHandWeaponShortcut != null) {
            this.m_leftHandWeaponShortcut.release();
        }
        for (int i = 0; i < this.m_shortcutBars.length; ++i) {
            this.m_shortcutBars[i].release();
        }
        this.m_shortcutBars = null;
    }
    
    public void setActive(final boolean active) {
        ShortcutBarDialogActions.m_shortCutsActive = active;
    }
    
    public void notifyShortcutBarTypeChange(final ShortCutBarType shortCutBarType) {
        for (int i = this.m_shortcutBarTypeListeners.size() - 1; i >= 0; --i) {
            this.m_shortcutBarTypeListeners.get(i).onShortcutBarTypeChangeRequested(shortCutBarType);
        }
    }
    
    public void addShortcutBarTypeChangedListener(final ShortcutBarTypeListener shortcutBarTypeListener) {
        if (!this.m_shortcutBarTypeListeners.contains(shortcutBarTypeListener)) {
            this.m_shortcutBarTypeListeners.add(shortcutBarTypeListener);
        }
    }
    
    public void removeShortcutBarTypeChangedListener(final ShortcutBarTypeListener shortcutBarTypeListener) {
        this.m_shortcutBarTypeListeners.remove(shortcutBarTypeListener);
    }
    
    public ShortCutBarType getCurrentBarType() {
        return this.m_currentBarType;
    }
    
    public SymbiotShortcutBar getSymbiotShortcutBar() {
        return (SymbiotShortcutBar)this.m_shortcutBars[ShortCutBarType.SYMBIOT_BAR.getFirstIndex()];
    }
    
    public void showSymbiotBar() {
        if (!this.m_displaySymbiot) {
            this.m_displaySymbiot = true;
            this.setCurrentBarType(ShortCutBarType.SYMBIOT_BAR);
            this.updateMainShortcutBar();
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "displaySymbiotBar");
        }
    }
    
    public void hideSymbiotBar() {
        if (this.m_displaySymbiot) {
            this.m_displaySymbiot = false;
            this.setCurrentBarType(ShortCutBarType.FIGHT);
            this.updateMainShortcutBar();
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "displaySymbiotBar");
        }
    }
    
    public void setCurrentBarType(final ShortCutBarType currentBarType) {
        final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        this.setCurrentBarType(currentBarType, true, (byte)wakfuGamePreferences.getIntValue((currentBarType == ShortCutBarType.FIGHT) ? WakfuKeyPreferenceStoreEnum.CURRENT_SPELL_SHORTCUT_BAR_INDEX : WakfuKeyPreferenceStoreEnum.CURRENT_ITEM_SHORTCUT_BAR_INDEX));
    }
    
    public void setCurrentBarType(final ShortCutBarType currentBarType, final boolean update) {
        final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        this.setCurrentBarType(currentBarType, update, (byte)wakfuGamePreferences.getIntValue((currentBarType == ShortCutBarType.FIGHT) ? WakfuKeyPreferenceStoreEnum.CURRENT_SPELL_SHORTCUT_BAR_INDEX : WakfuKeyPreferenceStoreEnum.CURRENT_ITEM_SHORTCUT_BAR_INDEX));
    }
    
    public void setCurrentBarType(final ShortCutBarType currentBarType, final byte index) {
        this.setCurrentBarType(currentBarType, true, index);
    }
    
    public void setCurrentBarType(final ShortCutBarType currentBarType, final boolean update, final byte index) {
        if (this.m_displaySymbiot && currentBarType != ShortCutBarType.SYMBIOT_BAR) {
            return;
        }
        if (this.m_currentBarType == currentBarType) {
            return;
        }
        this.notifyShortcutBarTypeChange(currentBarType);
        this.m_currentBarType = currentBarType;
        this.setSelectedShortcutBarNumber((index != -1) ? index : ((byte)this.m_currentBarType.getFirstIndex()), false);
        if (update) {
            this.updateMainShortcutBar();
        }
        UIShortcutBarFrame.getInstance().updateAdditionalShortcutBars();
        if (currentBarType.isSerialized()) {
            WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SHORTCUT_BAR_TYPE, this.m_currentBarType.name());
        }
    }
    
    private boolean isItemsBarNumberValid(final byte number) {
        for (int i = 0; i < this.m_itemsMainShortcutBars.length; ++i) {
            if (this.m_itemsMainShortcutBars[i] == number) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isSpellsBarNumberValid(final byte number) {
        for (int i = 0; i < this.m_spellsMainShortcutBars.length; ++i) {
            if (this.m_spellsMainShortcutBars[i] == number) {
                return true;
            }
        }
        return false;
    }
    
    public ShortcutBar getSelectedSpellsShortcutBar() {
        if (this.m_selectedSpellsShortCutBar < 0 || this.m_selectedSpellsShortCutBar >= this.m_shortcutBars.length) {
            return null;
        }
        return this.m_shortcutBars[this.m_selectedSpellsShortCutBar];
    }
    
    public byte getSelectedSpellsShortcutBarNumber() {
        return this.m_selectedSpellsShortCutBar;
    }
    
    public String getSelectedSpellsShortcutBarNumberText() {
        return String.valueOf(this.getSelectedSpellsShortcutBarNumber() - this.getSelectedSpellsShortcutBar().getType().getFirstIndex() + 1);
    }
    
    public void setSelectedSpellsShortcutBarNumber(final byte selectedShortCutBar) {
        this.setSelectedSpellsShortcutBarNumber(selectedShortCutBar, true);
    }
    
    private void setSelectedSpellsShortcutBarNumber(final byte selectedShortCutBar, final boolean update) {
        if (this.m_selectedSpellsShortCutBar != selectedShortCutBar && this.isSpellsBarNumberValid(selectedShortCutBar)) {
            this.m_selectedSpellsShortCutBar = selectedShortCutBar;
            WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.CURRENT_SPELL_SHORTCUT_BAR_INDEX, this.m_selectedSpellsShortCutBar);
            if (update) {
                this.updateMainShortcutBar();
            }
        }
    }
    
    public ShortcutBar getSelectedItemsShortcutBar() {
        if (this.m_selectedItemsShortCutBar < 0 || this.m_selectedItemsShortCutBar >= this.m_shortcutBars.length) {
            return null;
        }
        return this.m_shortcutBars[this.m_selectedItemsShortCutBar];
    }
    
    public byte getSelectedItemsShortcutBarNumber() {
        return this.m_selectedItemsShortCutBar;
    }
    
    public String getSelectedItemsShortcutBarNumberText() {
        return String.valueOf(this.getSelectedItemsShortcutBarNumber() - this.getSelectedItemsShortcutBar().getType().getFirstIndex() + 1);
    }
    
    public void setSelectedItemsShortcutBarNumber(final byte selectedShortCutBar) {
        this.setSelectedItemsShortcutBarNumber(selectedShortCutBar, true);
    }
    
    private void setSelectedItemsShortcutBarNumber(final byte selectedShortCutBar, final boolean update) {
        if (this.m_selectedItemsShortCutBar != selectedShortCutBar && this.isItemsBarNumberValid(selectedShortCutBar)) {
            this.m_selectedItemsShortCutBar = selectedShortCutBar;
            WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.CURRENT_ITEM_SHORTCUT_BAR_INDEX, this.m_selectedItemsShortCutBar);
            if (update) {
                this.updateMainShortcutBar();
            }
        }
    }
    
    public int getShortcutBarNumber(final ShortcutBar bar) {
        for (int i = 0; i < this.m_shortcutBars.length; ++i) {
            if (this.m_shortcutBars[i] == bar) {
                return i;
            }
        }
        return -1;
    }
    
    private void selectNextSpellsShortcutBar() {
        int index = -1;
        for (int i = 0; i < this.m_spellsMainShortcutBars.length; ++i) {
            if (this.m_spellsMainShortcutBars[i] == this.m_selectedSpellsShortCutBar) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            this.setSelectedSpellsShortcutBarNumber(this.m_spellsMainShortcutBars[(index + 1) % this.m_spellsMainShortcutBars.length]);
        }
    }
    
    private void selectPreviousSpellsShortcutBar() {
        int index = -1;
        for (int i = 0; i < this.m_spellsMainShortcutBars.length; ++i) {
            if (this.m_spellsMainShortcutBars[i] == this.m_selectedSpellsShortCutBar) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            this.setSelectedSpellsShortcutBarNumber(this.m_spellsMainShortcutBars[(index - 1 + this.m_spellsMainShortcutBars.length) % this.m_spellsMainShortcutBars.length]);
        }
    }
    
    private void selectNextItemsShortcutBar() {
        int index = -1;
        for (int i = 0; i < this.m_itemsMainShortcutBars.length; ++i) {
            if (this.m_itemsMainShortcutBars[i] == this.m_selectedItemsShortCutBar) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            this.setSelectedItemsShortcutBarNumber(this.m_itemsMainShortcutBars[(index + 1) % this.m_itemsMainShortcutBars.length]);
        }
    }
    
    private void selectPreviousItemsShortcutBar() {
        int index = -1;
        for (int i = 0; i < this.m_itemsMainShortcutBars.length; ++i) {
            if (this.m_itemsMainShortcutBars[i] == this.m_selectedItemsShortCutBar) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            this.setSelectedItemsShortcutBarNumber(this.m_itemsMainShortcutBars[(index - 1 + this.m_itemsMainShortcutBars.length) % this.m_itemsMainShortcutBars.length]);
        }
    }
    
    public void selectNextShortcutBar() {
        if (this.m_currentBarType == ShortCutBarType.FIGHT) {
            this.selectNextSpellsShortcutBar();
        }
        else if (this.m_currentBarType == ShortCutBarType.WORLD) {
            this.selectNextItemsShortcutBar();
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "closedShortcutBars");
    }
    
    public void selectPreviousShortcutBar() {
        if (this.m_currentBarType == ShortCutBarType.FIGHT) {
            this.selectPreviousSpellsShortcutBar();
        }
        else if (this.m_currentBarType == ShortCutBarType.WORLD) {
            this.selectPreviousItemsShortcutBar();
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "closedShortcutBars");
    }
    
    private boolean isOnFight() {
        return WakfuGameEntity.getInstance().getLocalPlayer().isOnFight();
    }
    
    public ShortcutBar getOutFightBar(final byte number) {
        return this.getBar(ShortCutBarType.WORLD, number);
    }
    
    public ShortcutBar getSpellsBar(final byte number) {
        return this.getBar(ShortCutBarType.FIGHT, number);
    }
    
    public ShortcutBar getCurrentBar(final byte number) {
        return this.getBar(this.m_currentBarType, number);
    }
    
    public ShortcutBar getBar(final ShortCutBarType type, final byte number) {
        assert number >= 0 && number < type.getCount() : "On essaye de r\u00e9cup\u00e9rer une ShortCutBar de type " + type + " de num\u00e9ro " + number;
        return this.m_shortcutBars[number + type.getFirstIndex()];
    }
    
    public ArrayList<ShortcutBar> getCurrentBars(final boolean closedOnly) {
        if (this.m_currentBarType == ShortCutBarType.FIGHT) {
            return this.getSpellsBars(closedOnly);
        }
        if (this.m_currentBarType == ShortCutBarType.WORLD) {
            return this.getItemsBars(closedOnly);
        }
        return null;
    }
    
    public ArrayList<ShortcutBar> getSpellsBars(final boolean closedOnly) {
        return this.getBars(ShortCutBarType.FIGHT, this.getSelectedSpellsShortcutBarNumber(), closedOnly);
    }
    
    public ArrayList<ShortcutBar> getItemsBars(final boolean closedOnly) {
        return this.getBars(ShortCutBarType.WORLD, this.getSelectedItemsShortcutBarNumber(), closedOnly);
    }
    
    private ArrayList<ShortcutBar> getBars(final ShortCutBarType type, final byte selectShortcutBarNumber, final boolean closedOnly) {
        final short firstIndex = type.getFirstIndex();
        final ArrayList<ShortcutBar> shortcutBars = new ArrayList<ShortcutBar>();
        for (int i = 0; i < type.getCount(); ++i) {
            final int index = i + firstIndex;
            if (!closedOnly || index != selectShortcutBarNumber) {
                shortcutBars.add(this.m_shortcutBars[index]);
            }
        }
        return shortcutBars;
    }
    
    public ShortcutBar getShortcutBar(final byte number) {
        assert number >= 0 && number < this.m_shortcutBars.length : "On essaye de r\u00e9cup\u00e9rer la shortcutBar de num\u00e9ro " + number;
        return this.m_shortcutBars[number];
    }
    
    private byte getFirstCompatibleShortcutBar(final ShortCutBarType type, final ShortCutItem item) {
        byte selectedShortCutBar = -1;
        byte containingAlreadyItem = -1;
        byte ret = -1;
        for (byte i = 0; i < type.getCount(); ++i) {
            final byte index = (byte)(i + type.getFirstIndex());
            final short indexOf = this.m_shortcutBars[index].getPosition(item);
            if (indexOf != -1) {
                containingAlreadyItem = (selectedShortCutBar = index);
                break;
            }
            if (this.m_shortcutBars[index].canAdd(item)) {
                ret = (selectedShortCutBar = index);
                break;
            }
        }
        if (selectedShortCutBar != -1) {
            switch (type) {
                case WORLD: {
                    this.setSelectedItemsShortcutBarNumber(containingAlreadyItem);
                    break;
                }
                case FIGHT: {
                    this.setSelectedSpellsShortcutBarNumber(containingAlreadyItem);
                    break;
                }
            }
        }
        return ret;
    }
    
    public boolean setShortcutItemAt(final ShortCutItem item, int shortcutBarNumber, final short position) {
        if (shortcutBarNumber == -1) {
            final ShortCutBarType type = this.getCurrentBarType();
            switch (type) {
                case FIGHT: {
                    if (!this.getSelectedSpellsShortcutBar().isFull()) {
                        shortcutBarNumber = this.getSelectedSpellsShortcutBarNumber();
                        break;
                    }
                    break;
                }
                default: {
                    if (!this.getSelectedItemsShortcutBar().isFull()) {
                        shortcutBarNumber = this.getSelectedItemsShortcutBarNumber();
                        break;
                    }
                    break;
                }
            }
            if (shortcutBarNumber == -1) {
                shortcutBarNumber = this.getFirstCompatibleShortcutBar(type, item);
            }
        }
        if (shortcutBarNumber == -1) {
            return false;
        }
        final ShortcutBar bar = this.getShortcutBar((byte)shortcutBarNumber);
        final boolean added = bar.setShortcutItemAt(item, position);
        if (added && position == -1) {
            switch (bar.getType()) {
                case WORLD: {
                    this.setSelectedItemsShortcutBarNumber((byte)shortcutBarNumber);
                    break;
                }
                case FIGHT: {
                    this.setSelectedSpellsShortcutBarNumber((byte)shortcutBarNumber);
                    break;
                }
            }
        }
        return added;
    }
    
    public void removeShortcutItemWithId(final int referenceId, final ShortCutType type, final boolean destroy) {
        for (byte i = 0; i < this.m_shortcutBars.length; ++i) {
            this.m_shortcutBars[i].removeShortcutItemWithId(referenceId, type, destroy);
        }
    }
    
    public void removeShortcutItemWithUniqueId(final long uniqueId, final boolean destroy) {
        for (byte i = 0; i < this.m_shortcutBars.length; ++i) {
            this.m_shortcutBars[i].removeShortcutItemWithUniqueId(uniqueId, destroy);
        }
    }
    
    public boolean removeShortcutItemAt(final int shortcutBarNumber, final short position, final boolean destroy) {
        return this.getShortcutBar((byte)shortcutBarNumber).removeShortcutItemAt(position, destroy);
    }
    
    public void setupRemovedShortcutToDestroy(final ShortCutItem abstractShortCutItem, final int shorcutBarNumber, final short position) {
        this.getShortcutBar((byte)shorcutBarNumber).setupRemovedShortcutToDestroy(abstractShortCutItem, position);
    }
    
    public void sendShortcutsUpdateMessageIfNeeded() {
        if (this.m_shortcutBars == null) {
            return;
        }
        ShortcutsModificationMessage message = null;
        byte barIndex = 0;
        for (final ShortcutBar bar : this.m_shortcutBars) {
            if (bar.needsToSendUpdateToServer()) {
                if (message == null) {
                    message = new ShortcutsModificationMessage();
                }
                final byte localBarIndex = (byte)(barIndex - bar.getType().getFirstIndex());
                TByteObjectIterator<ShortCutItem> it = bar.toAddIterator();
                while (it.hasNext()) {
                    it.advance();
                    message.addToAdds((byte)bar.getType().ordinal(), localBarIndex, it.key(), it.value());
                }
                it = bar.toRemoveIterator();
                while (it.hasNext()) {
                    it.advance();
                    message.addToRemoves((byte)bar.getType().ordinal(), localBarIndex, it.key());
                }
                it = bar.toDestroyIterator();
                while (it.hasNext()) {
                    it.advance();
                    message.addToRemoves((byte)bar.getType().ordinal(), localBarIndex, it.key());
                }
                bar.cleanChangesForServer();
            }
            ++barIndex;
        }
        if (message != null) {
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        }
    }
    
    public void runProcedure(final ShortcutItemProcedure procedure) {
        for (final ShortcutBar bar : this.m_shortcutBars) {
            bar.runProcedure(procedure);
        }
    }
    
    public boolean isInShortcutBarWithReferenceId(final int rid) {
        for (final ShortcutBar bar : this.m_shortcutBars) {
            if (bar.getFirstWithReferenceId(rid) != null) {
                return true;
            }
        }
        return false;
    }
    
    public void useLeftHandWeapon() {
        this.useHandWeapon(this.m_leftHandWeaponShortcut);
    }
    
    public void useRightHandWeapon() {
        this.useHandWeapon(this.m_rightHandWeaponShortcut);
    }
    
    private void useHandWeapon(final ShortCutItem shortcut) {
        Item item = ((ArrayInventoryWithoutCheck<Item, R>)WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory()).getWithUniqueId(shortcut.getUniqueId());
        byte position;
        if (item == null && shortcut.getReferenceId() == 2145) {
            final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(2145);
            final Item realItem = new Item(-1L);
            realItem.initializeWithReferenceItem(refItem);
            realItem.setQuantity((short)1);
            item = realItem;
            position = EquipmentPosition.FIRST_WEAPON.m_id;
        }
        else {
            position = (byte)WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory().getPosition(item.getUniqueId());
        }
        if (item.isActive()) {
            final UIFighterSelectAttackMessage message = new UIFighterSelectAttackMessage();
            message.setItem(item, position);
            message.setId(18009);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public void useShortCut(final byte bar, final short position) {
        ShortcutBarDialogActions.useItem(null, this.getShortcutBar(bar).getFromPosition(position), false);
    }
    
    public void onQuantityChanged(final Item item) {
        final int id = item.getReferenceId();
        for (final ShortcutBar bar : this.m_shortcutBars) {
            final ArrayList<ShortCutItem> items = bar.getAllWithReferenceId(id);
            if (items != null) {
                for (int i = items.size() - 1; i >= 0; --i) {
                    final ShortCutItem sci = items.get(i);
                    PropertiesProvider.getInstance().firePropertyValueChanged(sci, "quantity", "usable");
                }
            }
        }
    }
    
    public void setLeftAndRightHand(final Item firstWeapon, final Item secondWeapon) {
        if (secondWeapon != null) {
            final byte state = (byte)((secondWeapon.isUsable() && secondWeapon.isActive()) ? 2 : 3);
            this.m_leftHandWeaponShortcut.setItem(ShortCutType.ITEM, secondWeapon.getUniqueId(), secondWeapon.getReferenceId(), secondWeapon.getGfxId());
            this.m_leftHandWeaponShortcut.setState(state);
        }
        else {
            this.m_leftHandWeaponShortcut.setItem(ShortCutType.EQUIPMENT_SLOT, -1L, -1, 0);
        }
        if (firstWeapon != null) {
            final byte state = (byte)((firstWeapon.isUsable() && firstWeapon.isActive()) ? 2 : 3);
            this.m_rightHandWeaponShortcut.setItem(ShortCutType.ITEM, firstWeapon.getUniqueId(), firstWeapon.getReferenceId(), firstWeapon.getGfxId());
            this.m_rightHandWeaponShortcut.setState(state);
        }
        else if (secondWeapon != null) {
            final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(2145);
            this.m_rightHandWeaponShortcut.setItem(ShortCutType.EQUIPMENT_SLOT, -1L, referenceItem.getId(), referenceItem.getGfxId());
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "leftHandWeaponShortcut", "rightHandWeaponShortcut");
    }
    
    public void onItemAdded(final Item concernedItem, final InventoryItemModifiedEvent modEvent) {
        try {
            if (modEvent.getPosition() == EquipmentPosition.FIRST_WEAPON.m_id || modEvent.getPosition() == EquipmentPosition.SECOND_WEAPON.getId()) {
                boolean leftChanged = false;
                boolean rightChanged = false;
                final byte state = (byte)((concernedItem.isUsable() && concernedItem.isActive()) ? 2 : 3);
                if (modEvent.getPosition() == EquipmentPosition.SECOND_WEAPON.m_id) {
                    this.m_leftHandWeaponShortcut.setItem(ShortCutType.ITEM, concernedItem.getUniqueId(), concernedItem.getReferenceId(), concernedItem.getGfxId());
                    this.m_leftHandWeaponShortcut.setState(state);
                    leftChanged = true;
                }
                else {
                    this.m_rightHandWeaponShortcut.setItem(ShortCutType.ITEM, concernedItem.getUniqueId(), concernedItem.getReferenceId(), concernedItem.getGfxId());
                    this.m_rightHandWeaponShortcut.setState(state);
                    rightChanged = true;
                }
                if (leftChanged) {
                    PropertiesProvider.getInstance().firePropertyValueChanged(this, "leftHandWeaponShortcut");
                }
                if (rightChanged) {
                    PropertiesProvider.getInstance().firePropertyValueChanged(this, "rightHandWeaponShortcut");
                }
            }
            if (concernedItem.isUsable() && concernedItem.isActive()) {
                for (final ShortcutBar bar : this.m_shortcutBars) {
                    try {
                        final ArrayList<ShortCutItem> list = bar.getAllWithReferenceId(concernedItem.getReferenceId());
                        for (int i = 0, size = list.size(); i < size; ++i) {
                            list.get(i).setState((byte)2);
                        }
                    }
                    catch (Exception e) {
                        ShortcutBarManager.m_logger.error((Object)("Erreur a la modification d'un shortcut " + e));
                    }
                }
            }
            else {
                for (final ShortcutBar bar : this.m_shortcutBars) {
                    try {
                        final ArrayList<ShortCutItem> list = bar.getAllWithReferenceId(concernedItem.getReferenceId());
                        for (int i = 0, size = list.size(); i < size; ++i) {
                            list.get(i).setState((byte)3);
                        }
                    }
                    catch (Exception e) {
                        ShortcutBarManager.m_logger.error((Object)("Erreur a la modification d'un shortcut " + e));
                    }
                }
            }
            this.updateShortcutBars();
        }
        catch (Exception e2) {
            ShortcutBarManager.m_logger.error((Object)("Erreur a l'ajout d'un shortcut " + e2));
        }
    }
    
    public void onItemRemoved(final Item concernedItem) {
        boolean leftChanged = false;
        boolean rightChanged = false;
        if (concernedItem.getUniqueId() == this.m_leftHandWeaponShortcut.getUniqueId()) {
            this.m_leftHandWeaponShortcut.setItem(ShortCutType.EQUIPMENT_SLOT, -1L, -1, 0);
            leftChanged = true;
        }
        else if (concernedItem.getUniqueId() == this.m_rightHandWeaponShortcut.getUniqueId()) {
            this.m_rightHandWeaponShortcut.setItem(ShortCutType.EQUIPMENT_SLOT, -1L, -1, 0);
            rightChanged = true;
        }
        if (this.m_leftHandWeaponShortcut.getReferenceId() == -1 && this.m_rightHandWeaponShortcut.getReferenceId() == -1) {
            final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(2145);
            this.m_rightHandWeaponShortcut.setItem(ShortCutType.EQUIPMENT_SLOT, -1L, referenceItem.getId(), referenceItem.getGfxId());
            rightChanged = true;
        }
        if (leftChanged) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "leftHandWeaponShortcut");
        }
        if (rightChanged) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "rightHandWeaponShortcut");
        }
        for (final ShortcutBar bar : this.m_shortcutBars) {
            try {
                final ArrayList<ShortCutItem> list = bar.getAllWithReferenceId(concernedItem.getReferenceId());
                for (int i = 0, size = list.size(); i < size; ++i) {
                    if (concernedItem.getReferenceItem().isEquipmentPositionValid(EquipmentPosition.FIRST_WEAPON) || concernedItem.getReferenceItem().isEquipmentPositionValid(EquipmentPosition.SECOND_WEAPON)) {
                        list.get(i).setState((byte)5);
                    }
                    else {
                        list.get(i).setState((byte)0);
                    }
                }
            }
            catch (Exception e) {
                ShortcutBarManager.m_logger.error((Object)("Erreur a la modification d'un shortcut " + e));
            }
        }
        this.updateShortcutBars();
    }
    
    @Override
    public String[] getFields() {
        return ShortcutBarManager.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("currentShortcutBarType")) {
            return this.m_currentBarType.ordinal();
        }
        if (fieldName.equals("selectedShortcutBar")) {
            return this.getSelectedShortcutBar();
        }
        if (fieldName.equals("currentShortcutBarNumber")) {
            return this.getSelectedShortcutBarNumber();
        }
        if (fieldName.equals("currentShortcutBarNumberText")) {
            return this.getSelectedShortcutBarNumberText();
        }
        if (fieldName.equals("leftHandWeaponShortcut")) {
            if (this.m_leftHandWeaponShortcut != null && this.m_leftHandWeaponShortcut.getReferenceId() != -1) {
                return this.m_leftHandWeaponShortcut;
            }
            return null;
        }
        else if (fieldName.equals("rightHandWeaponShortcut")) {
            if (this.m_rightHandWeaponShortcut.getReferenceId() != -1) {
                return this.m_rightHandWeaponShortcut;
            }
            return null;
        }
        else {
            if (fieldName.equals("displaySymbiotBar")) {
                return this.m_displaySymbiot;
            }
            return null;
        }
    }
    
    public boolean isDisplaySymbiot() {
        return this.m_displaySymbiot;
    }
    
    public String getSelectedShortcutBarNumberText() {
        if (this.m_currentBarType == ShortCutBarType.FIGHT) {
            return this.getSelectedSpellsShortcutBarNumberText();
        }
        if (this.m_currentBarType == ShortCutBarType.WORLD) {
            return this.getSelectedItemsShortcutBarNumberText();
        }
        return null;
    }
    
    public byte getSelectedShortcutBarNumber() {
        if (this.m_currentBarType == ShortCutBarType.FIGHT) {
            return this.getSelectedSpellsShortcutBarNumber();
        }
        if (this.m_currentBarType == ShortCutBarType.WORLD) {
            return this.getSelectedItemsShortcutBarNumber();
        }
        return -1;
    }
    
    public void setSelectedShortcutBarNumber(final byte index, final boolean update) {
        if (this.m_currentBarType == ShortCutBarType.FIGHT) {
            this.setSelectedSpellsShortcutBarNumber(index, update);
        }
        else if (this.m_currentBarType == ShortCutBarType.WORLD) {
            this.setSelectedItemsShortcutBarNumber(index, update);
        }
    }
    
    public ShortcutBar getSelectedShortcutBar() {
        if (this.m_displaySymbiot) {
            return this.getSymbiotShortcutBar();
        }
        if (this.m_currentBarType == ShortCutBarType.FIGHT) {
            return this.getSelectedSpellsShortcutBar();
        }
        if (this.m_currentBarType == ShortCutBarType.WORLD) {
            return this.getSelectedItemsShortcutBar();
        }
        return null;
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
    
    public void updateMainShortcutBar() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentShortcutBarType", "selectedShortcutBar", "currentShortcutBarNumber", "currentShortcutBarNumberText", "closedShortcutBars");
        this.updateLeftAndRighthands();
    }
    
    public void updateLeftAndRighthands() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "rightHandWeaponShortcut", "leftHandWeaponShortcut");
    }
    
    public void updateShorctutBarUsability() {
        for (final ShortcutBar bar : this.m_shortcutBars) {
            bar.updateShortcutsUsable();
        }
    }
    
    public void updateShortcutBars() {
        for (int i = this.m_currentBarType.getFirstIndex(), size = this.m_currentBarType.getCount() + i; i < size; ++i) {
            this.m_shortcutBars[i].updateShortcuts();
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "leftHandWeaponShortcut", "rightHandWeaponShortcut");
    }
    
    public void onDataChanged(final CharacterSerializedShortcutInventories part) {
        final int inventoriesCount = part.shortcutInventories.size();
        final ShortCutBarType[] types = ShortCutBarType.values();
        final int[] indexes = new int[types.length];
        final boolean[] changed = new boolean[ShortCutBarType.getTotalBarCount()];
        Arrays.fill(indexes, 0);
        Arrays.fill(changed, false);
        for (final ShortCutBarType type : types) {
            indexes[type.ordinal()] = type.getFirstIndex();
        }
        for (int i = 0; i < inventoriesCount; ++i) {
            final int index = indexes[part.shortcutInventories.get(i).inventory.type]++;
            changed[index] = true;
            if (!this.m_shortcutBars[index].onDataChanged(part.shortcutInventories.get(i).inventory)) {
                ShortcutBarManager.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer l'inventaire de shortcut #" + i));
            }
        }
        byte spellsIndex = 0;
        byte itemsIndex = 0;
        this.m_spellsMainShortcutBars = new byte[ShortCutBarType.FIGHT.getCount()];
        this.m_itemsMainShortcutBars = new byte[ShortCutBarType.WORLD.getCount()];
        for (byte j = 0; j < this.m_shortcutBars.length; ++j) {
            if (this.m_shortcutBars[j].getType() == ShortCutBarType.FIGHT) {
                final byte[] spellsMainShortcutBars = this.m_spellsMainShortcutBars;
                final byte b = spellsIndex;
                ++spellsIndex;
                spellsMainShortcutBars[b] = j;
            }
            else if (this.m_shortcutBars[j].getType() == ShortCutBarType.WORLD) {
                final byte[] itemsMainShortcutBars = this.m_itemsMainShortcutBars;
                final byte b2 = itemsIndex;
                ++itemsIndex;
                itemsMainShortcutBars[b2] = j;
            }
        }
        if (this.m_itemsMainShortcutBars.length > 0) {
            this.m_selectedItemsShortCutBar = this.m_itemsMainShortcutBars[0];
        }
        if (this.m_spellsMainShortcutBars.length > 0) {
            this.m_selectedSpellsShortCutBar = this.m_spellsMainShortcutBars[0];
        }
        for (int k = 0; k < changed.length; ++k) {
            if (!changed[k]) {
                this.m_shortcutBars[k].clean();
            }
        }
    }
    
    public ShortCutItem getLeftHandWeaponShortcut() {
        return this.m_leftHandWeaponShortcut;
    }
    
    public ShortCutItem getRightHandWeaponShortcut() {
        return this.m_rightHandWeaponShortcut;
    }
    
    public void initializeItemShortcuts(final ItemEquipment equipmentInventory, final ClientBagContainer bags) {
        this.runProcedure(new ShortcutItemProcedure() {
            @Override
            public void execute(final ShortCutItem shortcutItem, final ShortcutBar bar) {
                if (shortcutItem.getType() == ShortCutType.EQUIPMENT_SLOT) {
                    final Item item = ((ArrayInventoryWithoutCheck<Item, R>)equipmentInventory).getFromPosition((short)(-(shortcutItem.getUniqueId() + 1L)));
                    if (item == null && 2145 != shortcutItem.getReferenceId()) {
                        bar.remove(shortcutItem);
                    }
                }
                if (shortcutItem.getType() == ShortCutType.ITEM) {
                    Item item = ((ArrayInventoryWithoutCheck<Item, R>)equipmentInventory).getFirstWithReferenceId(shortcutItem.getReferenceId());
                    if (item != null && item.isActive()) {
                        if (item.isUsable()) {
                            shortcutItem.setState((byte)2);
                        }
                        else {
                            shortcutItem.setState((byte)3);
                        }
                    }
                    else {
                        item = (Item)bags.getFirstItemFromInventory(shortcutItem.getReferenceId());
                        if (item != null && item.isActive()) {
                            final EquipmentPosition[] positions = item.getReferenceItem().getItemType().getEquipmentPositions();
                            if (positions.length == 0) {
                                if (item.isUsable()) {
                                    shortcutItem.setState((byte)4);
                                }
                                else {
                                    shortcutItem.setState((byte)1);
                                }
                            }
                            else if (item.getReferenceItem().isEquipmentPositionValid(EquipmentPosition.FIRST_WEAPON) || item.getReferenceItem().isEquipmentPositionValid(EquipmentPosition.SECOND_WEAPON)) {
                                shortcutItem.setState((byte)5);
                            }
                            else {
                                shortcutItem.setState((byte)0);
                            }
                        }
                        else {
                            bar.removeShortcutItemAt(bar.getPosition(shortcutItem), true);
                        }
                    }
                }
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)ShortcutBarManager.class);
        FIELDS = new String[] { "currentShortcutBarType", "selectedShortcutBar", "currentShortcutBarNumber", "currentShortcutBarNumberText", "leftHandWeaponShortcut", "rightHandWeaponShortcut", "closedShortcutBars", "displaySymbiotBar" };
    }
    
    private class ShortcutBarIterator implements Iterator<ShortcutBar>
    {
        private int m_current;
        private final int m_size;
        private final int m_firstIndex;
        
        private ShortcutBarIterator(final ShortCutBarType type) {
            super();
            this.m_current = 0;
            this.m_size = type.getCount();
            this.m_firstIndex = type.getFirstIndex();
        }
        
        @Override
        public boolean hasNext() {
            return this.m_current < this.m_size;
        }
        
        @Override
        public ShortcutBar next() {
            final ShortcutBar bar = ShortcutBarManager.this.m_shortcutBars[this.m_current + this.m_firstIndex];
            ++this.m_current;
            return bar;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    public interface ShortcutBarTypeListener
    {
        void onShortcutBarTypeChangeRequested(ShortCutBarType p0);
    }
}
