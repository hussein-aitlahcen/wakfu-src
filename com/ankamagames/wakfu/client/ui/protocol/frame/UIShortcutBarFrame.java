package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.message.shortcut.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.emote.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.windowStick.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class UIShortcutBarFrame implements MessageFrame
{
    private static UIShortcutBarFrame m_instance;
    private static final Logger m_logger;
    private ShortcutBarManager m_shortcutBarManager;
    private final TIntObjectHashMap<Window> m_opennedShortcutBars;
    private boolean m_pushed;
    
    public UIShortcutBarFrame() {
        super();
        this.m_opennedShortcutBars = new TIntObjectHashMap<Window>();
        this.m_pushed = false;
    }
    
    public static UIShortcutBarFrame getInstance() {
        return UIShortcutBarFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16405: {
                WakfuGameEntity.getInstance().removeFrame(this);
                return false;
            }
            case 16710: {
                this.sendDirectionChangeRequest(Direction8.NORTH_EAST);
                return false;
            }
            case 16707: {
                this.sendDirectionChangeRequest(Direction8.NORTH_WEST);
                return false;
            }
            case 16708: {
                this.sendDirectionChangeRequest(Direction8.SOUTH_EAST);
                return false;
            }
            case 16709: {
                this.sendDirectionChangeRequest(Direction8.SOUTH_WEST);
                return false;
            }
            case 16406: {
                if (PropertiesProvider.getInstance().getBooleanProperty("spellInventoryDisplay")) {
                    PropertiesProvider.getInstance().setPropertyValue("spellInventoryDisplay", false);
                }
                else {
                    this.hideAllWidgets();
                    PropertiesProvider.getInstance().setPropertyValue("spellInventoryDisplay", true);
                }
                return false;
            }
            case 16419: {
                if (PropertiesProvider.getInstance().getBooleanProperty("itemInventoryDisplay")) {
                    PropertiesProvider.getInstance().setPropertyValue("itemInventoryDisplay", false);
                }
                else {
                    this.hideAllWidgets();
                    PropertiesProvider.getInstance().setPropertyValue("itemInventoryDisplay", true);
                }
                return false;
            }
            case 16422: {
                if (Xulor.getInstance().isLoaded("osamodasSymbiotDialog")) {
                    WakfuGameEntity.getInstance().removeFrame(UIOsamodasSymbiotFrame.getInstance());
                }
                else {
                    this.hideAllWidgets();
                    WakfuGameEntity.getInstance().pushFrame(UIOsamodasSymbiotFrame.getInstance());
                }
                return false;
            }
            case 16434: {
                if (PropertiesProvider.getInstance().getBooleanProperty("fightActionsDisplay")) {
                    PropertiesProvider.getInstance().setPropertyValue("fightActionsDisplay", false);
                }
                else {
                    this.hideAllWidgets();
                    PropertiesProvider.getInstance().setPropertyValue("fightActionsDisplay", true);
                }
                return false;
            }
            case 16423: {
                if (PropertiesProvider.getInstance().getBooleanProperty("kardsDisplay")) {
                    PropertiesProvider.getInstance().setPropertyValue("kardsDisplay", false);
                }
                else {
                    this.hideAllWidgets();
                    PropertiesProvider.getInstance().setPropertyValue("kardsDisplay", true);
                }
                return false;
            }
            case 16410: {
                if (PropertiesProvider.getInstance().getBooleanProperty("spellInventoryDisplay")) {
                    PropertiesProvider.getInstance().setPropertyValue("spellInventoryDisplay", false);
                }
                return false;
            }
            case 16700: {
                final UIShortcutMessage shortcutMessage = (UIShortcutMessage)message;
                final Object item = shortcutMessage.getItem();
                final boolean playSound = shortcutMessage.getBooleanValue();
                final boolean force = shortcutMessage.isForce();
                final boolean cantModify = WakfuGameEntity.getInstance().getLocalPlayer().hasProperty(WorldPropertyType.CANT_MODIFY_SHORTCUT_BARS);
                if (cantModify && !force) {
                    return false;
                }
                if (item == null) {
                    return false;
                }
                int shortcutBarNumber = shortcutMessage.getShorcutBarNumber();
                final byte shortcutBarNumber2 = shortcutMessage.getPreviousBar();
                int position = shortcutMessage.getPosition();
                ShortCutItem item2 = null;
                final ShortCutBarType currentBarType = this.m_shortcutBarManager.getCurrentBarType();
                if (position != -1 && shortcutBarNumber != -1) {
                    item2 = this.m_shortcutBarManager.getShortcutBar((byte)shortcutBarNumber).getFromPosition((short)position);
                    if (item2 != null) {
                        item2 = (ShortCutItem)item2.getClone();
                    }
                }
                final int previousPosition = shortcutMessage.getPreviousPosition();
                ShortCutBarType shortCutBarType = null;
                ShortCutItem shortcut = null;
                if (item instanceof SpellLevel) {
                    shortCutBarType = ShortCutBarType.FIGHT;
                    final SpellLevel spell = (SpellLevel)item;
                    shortcut = ShortCutItem.checkOut(ShortCutType.SPELL_LEVEL, spell.getUniqueId(), spell.getReferenceId(), spell.getGfxId());
                }
                else if (item instanceof ShortCutItem) {
                    shortcut = (ShortCutItem)item;
                    if (shortcut.getType() == ShortCutType.EQUIPMENT_SLOT) {
                        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(2145);
                        if (shortcut.getReferenceId() != referenceItem.getId()) {
                            final Item itemObject = ((ArrayInventoryWithoutCheck<Item, R>)WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory()).getFromPosition((short)(-(shortcut.getUniqueId() + 1L)));
                            shortCutBarType = (itemObject.isUsableInWorld() ? ShortCutBarType.WORLD : ShortCutBarType.FIGHT);
                            if (itemObject.isActive()) {
                                shortcut = ShortCutItem.checkOut(ShortCutType.ITEM, itemObject.getUniqueId(), itemObject.getReferenceId(), itemObject.getGfxId());
                                if (itemObject.isUsable()) {
                                    shortcut.setState((byte)2);
                                }
                                else {
                                    shortcut.setState((byte)3);
                                }
                            }
                        }
                    }
                }
                else if (item instanceof Item) {
                    final Item itemObject2 = (Item)item;
                    shortCutBarType = ShortCutBarType.WORLD;
                    if (WakfuGameEntity.getInstance().getLocalPlayer().getBags().contains(itemObject2) != null) {
                        if (itemObject2.getReferenceItem().getItemType().getEquipmentPositions().length > 0) {
                            shortcut = ShortCutItem.checkOut(ShortCutType.ITEM, itemObject2.getUniqueId(), itemObject2.getReferenceId(), itemObject2.getGfxId());
                            shortcut.setState((byte)0);
                        }
                        else if (itemObject2.isUsable()) {
                            shortcut = ShortCutItem.checkOut(ShortCutType.USABLE_REFERENCE_ITEM, -1L, itemObject2.getReferenceId(), itemObject2.getGfxId());
                            shortcut.setState((byte)4);
                        }
                        else {
                            shortcut = ShortCutItem.checkOut(ShortCutType.ITEM, itemObject2.getUniqueId(), itemObject2.getReferenceId(), itemObject2.getGfxId());
                            shortcut.setState((byte)0);
                        }
                    }
                    else if (((ArrayInventoryWithoutCheck<Item, R>)WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory()).contains(itemObject2)) {
                        shortcut = ShortCutItem.checkOut(ShortCutType.ITEM, itemObject2.getUniqueId(), itemObject2.getReferenceId(), itemObject2.getGfxId());
                        if (itemObject2.isUsable()) {
                            shortcut.setState((byte)2);
                        }
                        else {
                            shortcut.setState((byte)3);
                        }
                    }
                    else {
                        shortcut = null;
                    }
                }
                else if (item instanceof EmoteSmileyFieldProvider) {
                    shortCutBarType = ShortCutBarType.WORLD;
                    final EmoteSmileyFieldProvider emoteSmileyFieldProvider = (EmoteSmileyFieldProvider)item;
                    shortcut = ShortCutItem.checkOut(ShortCutType.EMOTE, emoteSmileyFieldProvider.getId(), emoteSmileyFieldProvider.getId(), emoteSmileyFieldProvider.getId());
                }
                if (shortCutBarType != null && this.m_shortcutBarManager.getCurrentBarType() != shortCutBarType) {
                    this.m_shortcutBarManager.setCurrentBarType(shortCutBarType);
                    position = -1;
                    shortcutBarNumber = -1;
                }
                if (!this.m_shortcutBarManager.setShortcutItemAt(shortcut, shortcutBarNumber, (short)position)) {
                    if (item2 != null && previousPosition != -1) {
                        this.m_shortcutBarManager.setShortcutItemAt(item2, shortcutBarNumber, (short)position);
                    }
                    if (shortcut != null) {
                        shortcut.release();
                    }
                    if (cantModify) {
                        this.m_shortcutBarManager.setCurrentBarType(currentBarType);
                    }
                    return false;
                }
                if (item2 != null && previousPosition != -1) {
                    final ShortCutItem shortcut2 = ShortCutItem.checkOut(item2.getType(), item2.getUniqueId(), item2.getReferenceId(), item2.getTargetGfxId());
                    this.m_shortcutBarManager.setShortcutItemAt(shortcut2, shortcutBarNumber2, (short)previousPosition);
                }
                if (playSound) {
                    WakfuSoundManager.getInstance().playGUISound(600053L);
                }
                if (cantModify) {
                    this.m_shortcutBarManager.setCurrentBarType(currentBarType);
                }
                return false;
            }
            case 16701: {
                final UIShortcutMessage shortcutMessage = (UIShortcutMessage)message;
                final int shortcutBarNumber3 = shortcutMessage.getShorcutBarNumber();
                final int position2 = shortcutMessage.getPosition();
                final boolean force = shortcutMessage.isForce();
                final boolean cantModify = WakfuGameEntity.getInstance().getLocalPlayer().hasProperty(WorldPropertyType.CANT_MODIFY_SHORTCUT_BARS);
                if (cantModify && !force) {
                    return false;
                }
                this.m_shortcutBarManager.removeShortcutItemAt(shortcutBarNumber3, (short)position2, false);
                WakfuSoundManager.getInstance().playGUISound(600059L);
                return false;
            }
            case 16702: {
                final UIShortcutMessage shortcutMessage = (UIShortcutMessage)message;
                final Object item = shortcutMessage.getItem();
                final boolean force2 = shortcutMessage.isForce();
                final boolean cantModify2 = WakfuGameEntity.getInstance().getLocalPlayer().hasProperty(WorldPropertyType.CANT_MODIFY_SHORTCUT_BARS);
                if (cantModify2 && !force2) {
                    return false;
                }
                if (item instanceof ShortCutItem) {
                    this.m_shortcutBarManager.setupRemovedShortcutToDestroy((ShortCutItem)item, shortcutMessage.getShorcutBarNumber(), (short)shortcutMessage.getPosition());
                }
                WakfuSoundManager.getInstance().dropItem();
                return false;
            }
            case 16703: {
                this.m_shortcutBarManager.selectNextShortcutBar();
                this.updateAdditionalShortcutBars();
                return false;
            }
            case 16704: {
                this.m_shortcutBarManager.selectPreviousShortcutBar();
                this.updateAdditionalShortcutBars();
                return false;
            }
            case 16411: {
                PropertiesProvider.getInstance().setPropertyValue("spellDescriptionDisplay", true);
                return false;
            }
            case 16412: {
                if (PropertiesProvider.getInstance().getBooleanProperty("spellDescriptionDisplay")) {
                    PropertiesProvider.getInstance().setPropertyValue("spellDescriptionDisplay", false);
                }
                return false;
            }
            case 16433: {
                this.m_shortcutBarManager.setCurrentBarType((this.m_shortcutBarManager.getCurrentBarType() == ShortCutBarType.FIGHT) ? ShortCutBarType.WORLD : ShortCutBarType.FIGHT, true);
                return false;
            }
            case 16432: {
                this.m_shortcutBarManager.setCurrentBarType((this.m_shortcutBarManager.getCurrentBarType() == ShortCutBarType.WORLD) ? ShortCutBarType.FIGHT : ShortCutBarType.WORLD, true);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void sendDirectionChangeRequest(final Direction8 dir) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!localPlayer.isOnFight()) {
            return;
        }
        final ActorDirectionChangeRequestMessage netMessage = new ActorDirectionChangeRequestMessage(dir);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void hideAllWidgets() {
        PropertiesProvider.getInstance().setPropertyValue("throwableInventoryDisplay", false);
        if (!WakfuGameEntity.getInstance().getLocalPlayer().isOnFight()) {
            PropertiesProvider.getInstance().setPropertyValue("fightActionsDisplay", false);
        }
    }
    
    public void openCloseAllAdditionalShortcutBars() {
        if (!this.m_shortcutBarManager.getCurrentBar((byte)0).isOpened()) {
            this.openAllAdditionalShortcutBars();
        }
        else {
            this.closeAllAdditionalShortcutBars();
        }
    }
    
    public void openCloseAdditionalShortcutBar(final byte index) {
        if (!this.m_shortcutBarManager.getCurrentBar(index).isOpened()) {
            this.openAdditionalShortcutBar(index);
        }
        else {
            this.closeAdditionalShortcutBar(index, true);
        }
    }
    
    public void openAllAdditionalShortcutBars() {
        for (byte i = 0, size = this.m_shortcutBarManager.getCurrentBarType().getCount(); i < size; ++i) {
            this.openAdditionalShortcutBar(i);
        }
    }
    
    public void closeAllAdditionalShortcutBars() {
        for (byte i = 0, size = this.m_shortcutBarManager.getCurrentBarType().getCount(); i < size; ++i) {
            this.closeAdditionalShortcutBar(i, false);
        }
    }
    
    public void openAdditionalShortcutBar(final byte shortcutBarIndex) {
        final ShortcutBar bar = this.m_shortcutBarManager.getCurrentBar(shortcutBarIndex);
        if (bar.isOpened()) {
            return;
        }
        final String baseId = bar.isVertical() ? "verticalSecondaryShortcutBarDialog" : "secondaryShortcutBarDialog";
        final String id = baseId + shortcutBarIndex;
        if (Xulor.getInstance().isLoaded(id)) {
            return;
        }
        String secondDialogId = null;
        try {
            if (!this.m_opennedShortcutBars.isEmpty()) {
                byte index = 0;
                do {
                    final Window win = this.m_opennedShortcutBars.get(index);
                    if (win != null && !win.isUnloading() && !win.getUserDefinedManager().hasRecord()) {
                        secondDialogId = win.getElementMap().getId();
                        break;
                    }
                    ++index;
                } while (index < this.m_opennedShortcutBars.size());
            }
        }
        catch (Exception e) {
            UIShortcutBarFrame.m_logger.error((Object)"Erreur au contr\u00f4le du placement d'une barre de raccourci secondaire");
            return;
        }
        Window w;
        if (secondDialogId != null) {
            w = (Window)Xulor.getInstance().loadAsMultiple(id, Dialogs.getDialogPath(baseId), null, secondDialogId, baseId, 40960L, (short)10005);
        }
        else {
            w = (Window)Xulor.getInstance().load(id, Dialogs.getDialogPath(baseId), 40960L, (short)10005);
        }
        w.setHorizontalDialog("secondaryShortcutBarDialog");
        w.setVerticalDialog("verticalSecondaryShortcutBarDialog");
        if (!this.m_opennedShortcutBars.contains(shortcutBarIndex)) {
            this.m_opennedShortcutBars.put(shortcutBarIndex, w);
        }
        WindowStickManager.getInstance().addWindow(w, false);
        PropertiesProvider.getInstance().setLocalPropertyValue("shortcutBar", this.m_shortcutBarManager.getCurrentBar(shortcutBarIndex), w.getElementMap());
        this.setShortcutBarOpened(shortcutBarIndex, true, true);
    }
    
    public void closeAdditionalShortcutBar(final byte shortcutBarIndex, final boolean save) {
        final ShortcutBar bar = this.m_shortcutBarManager.getCurrentBar(shortcutBarIndex);
        if (!bar.isOpened()) {
            return;
        }
        final String id = (bar.isVertical() ? "verticalSecondaryShortcutBarDialog" : "secondaryShortcutBarDialog") + shortcutBarIndex;
        if (Xulor.getInstance().isLoaded(id)) {
            Xulor.getInstance().unload(id);
        }
        this.setShortcutBarOpened(shortcutBarIndex, false, save);
    }
    
    public void setShortcutBarOpened(final byte barIndex, final boolean opened, final boolean save) {
        for (final ShortCutBarType type : ShortCutBarType.serializedValues()) {
            this.m_shortcutBarManager.getBar(type, barIndex).setOpened(opened);
        }
        if (save) {
            this.saveOpenedShortcutBarsToPreferences();
        }
    }
    
    public void setShortcutBarVertical(final byte barIndex, final boolean vertical) {
        for (final ShortCutBarType type : ShortCutBarType.serializedValues()) {
            this.m_shortcutBarManager.getBar(type, barIndex).setVertical(vertical);
        }
        this.saveOpenedShortcutBarsToPreferences();
    }
    
    public void updateAdditionalShortcutBars() {
        if (this.m_shortcutBarManager != null) {
            final ShortCutBarType barType = this.m_shortcutBarManager.getCurrentBarType();
            if (barType != null) {
                for (byte i = 0, size = barType.getCount(); i < size; ++i) {
                    this.updateAdditionalShortcutBar(i);
                }
            }
        }
    }
    
    public void updateAdditionalShortcutBar(final byte index) {
        final ShortcutBar bar = this.m_shortcutBarManager.getCurrentBar(index);
        final String id = (bar.isVertical() ? "verticalSecondaryShortcutBarDialog" : "secondaryShortcutBarDialog") + index;
        if (Xulor.getInstance().isLoaded(id)) {
            PropertiesProvider.getInstance().setLocalPropertyValue("shortcutBar", this.m_shortcutBarManager.getCurrentBar(index), id);
        }
    }
    
    public void saveOpenedShortcutBarsToPreferences() {
        final boolean[] openedBars = new boolean[this.m_shortcutBarManager.getCurrentBarType().getCount() * 2];
        for (byte i = 0, size = this.m_shortcutBarManager.getCurrentBarType().getCount(); i < size; ++i) {
            final ShortcutBar bar = this.m_shortcutBarManager.getCurrentBar(i);
            openedBars[i * 2] = bar.isOpened();
            openedBars[i * 2 + 1] = bar.isVertical();
        }
        int openedValue = 0;
        for (int count = Math.min(32, openedBars.length), j = 0; j < count; ++j) {
            if (openedBars[j]) {
                openedValue += 1 << j;
            }
        }
        WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.OPENED_SHORTCUT_BARS_KEY, openedValue);
    }
    
    public void changeShortcutBarManager(final ShortcutBarManager shortcutBarManager) {
        this.m_shortcutBarManager = shortcutBarManager;
        PropertiesProvider.getInstance().setPropertyValue("shortcutBarManager", this.m_shortcutBarManager);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!this.m_pushed) {
            final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
            this.changeShortcutBarManager(WakfuGameEntity.getInstance().getLocalPlayer().getShortcutBarManager());
            final ShortCutBarType shortCutBarType = ShortCutBarType.valueOf(wakfuGamePreferences.getStringValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SHORTCUT_BAR_TYPE));
            final WakfuKeyPreferenceStoreEnum key = (shortCutBarType == ShortCutBarType.FIGHT) ? WakfuKeyPreferenceStoreEnum.CURRENT_SPELL_SHORTCUT_BAR_INDEX : WakfuKeyPreferenceStoreEnum.CURRENT_ITEM_SHORTCUT_BAR_INDEX;
            final byte index = (byte)wakfuGamePreferences.getIntValue(key);
            if (shortCutBarType != null) {
                if (this.m_shortcutBarManager.getCurrentBarType() != shortCutBarType) {
                    this.m_shortcutBarManager.setCurrentBarType(shortCutBarType, index);
                }
                else {
                    this.m_shortcutBarManager.setSelectedShortcutBarNumber(index, true);
                }
            }
            this.hideAllWidgets();
            final Fight fight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
            if (fight != null && fight.getTimeline().getCurrentFighter() == WakfuGameEntity.getInstance().getLocalPlayer()) {
                WakfuGameEntity.getInstance().pushFrame(UIFightEndTurnFrame.getInstance());
            }
            Xulor.getInstance().putActionClass("wakfu.shortcutBar", ShortcutBarDialogActions.class);
            Xulor.getInstance().putActionClass("wakfu.osamodasSymbiot", OsamodasSymbiotDialogActions.class);
            this.m_pushed = true;
            final int opened = wakfuGamePreferences.getIntValue(WakfuKeyPreferenceStoreEnum.OPENED_SHORTCUT_BARS_KEY);
            final int count = Math.min(32, ShortCutBarType.serializedValues()[0].getCount() * 2);
            final boolean[] ret = new boolean[count];
            for (int i = 0; i < count; ++i) {
                ret[i] = ((opened >> i & 0x1) == 0x1);
            }
            final boolean[] openedBars = ret;
            for (final ShortCutBarType type : ShortCutBarType.serializedValues()) {
                for (byte j = 0, size = type.getCount(); j < size; ++j) {
                    final ShortcutBar bar = this.m_shortcutBarManager.getBar(type, j);
                    bar.setVertical(openedBars[j * 2 + 1]);
                }
            }
            for (byte k = 0, size2 = this.m_shortcutBarManager.getCurrentBarType().getCount(); k < size2; ++k) {
                if (openedBars[k * 2]) {
                    this.openAdditionalShortcutBar(k);
                }
            }
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (this.m_pushed) {
            this.m_opennedShortcutBars.clear();
            ChallengeManager.getInstance().clean();
            this.closeAllAdditionalShortcutBars();
            Xulor.getInstance().removeActionClass("wakfu.shortcutBar");
            Xulor.getInstance().removeActionClass("wakfu.osamodasSymbiot");
            WakfuGameEntity.getInstance().removeFrame(UIFightEndTurnFrame.getInstance());
            this.m_shortcutBarManager = null;
            this.m_pushed = false;
        }
    }
    
    static {
        UIShortcutBarFrame.m_instance = new UIShortcutBarFrame();
        m_logger = Logger.getLogger((Class)UIShortcutBarFrame.class);
    }
}
