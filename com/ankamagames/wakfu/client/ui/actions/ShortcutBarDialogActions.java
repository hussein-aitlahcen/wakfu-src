package com.ankamagames.wakfu.client.ui.actions;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.shortcut.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import com.ankamagames.wakfu.client.ui.protocol.message.spells.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.message.fight.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.mount.*;
import com.ankamagames.wakfu.client.core.emote.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.ui.protocol.message.resources.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

@XulorActionsTag
public class ShortcutBarDialogActions
{
    public static final String PACKAGE = "wakfu.shortcutBar";
    private static final Logger m_logger;
    public static boolean m_shortCutsActive;
    private static long SECURITY_DELAY;
    private static long m_startTime;
    private static byte m_previousBar;
    private static int m_previousPosition;
    
    public static void closeShortcutBarDialog(final Event event) {
        UIMessage.send((short)16405);
    }
    
    public static void openCloseAdditionalShortcutBar(final Event e, final Byte index) {
        final byte realIndex = (byte)(index - WakfuGameEntity.getInstance().getLocalPlayer().getShortcutBarManager().getCurrentBarType().getFirstIndex());
        UIShortcutBarFrame.getInstance().openCloseAdditionalShortcutBar(realIndex);
    }
    
    public static void setSouthEastDirection(final Event event) {
        final Fight fight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
        if (fight != null) {
            if (!PropertiesProvider.getInstance().getBooleanProperty("isInFightPlacement")) {
                if (fight.getTimeline().getCurrentFighter() == WakfuGameEntity.getInstance().getLocalPlayer() || fight.getTimeline().getCurrentFighter().isControlledByLocalPlayer()) {
                    UIMessage.send((short)18004);
                }
            }
            else if (!fight.getTimeline().hasCurrentFighter()) {
                ShortcutBarDialogActions.m_logger.info((Object)"Changement d'orientation inutile en phase de placement. Aucune action effecut\u00e9e");
            }
        }
        else {
            UIMessage.send((short)16708);
        }
    }
    
    public static void setSouthWestDirection(final Event event) {
        final Fight fight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
        if (fight != null) {
            if (!PropertiesProvider.getInstance().getBooleanProperty("isInFightPlacement")) {
                if (fight.getTimeline().getCurrentFighter() == WakfuGameEntity.getInstance().getLocalPlayer() || fight.getTimeline().getCurrentFighter().isControlledByLocalPlayer()) {
                    UIMessage.send((short)18005);
                }
            }
            else if (!fight.getTimeline().hasCurrentFighter()) {
                ShortcutBarDialogActions.m_logger.info((Object)"Changement d'orientation inutile en phase de placement. Aucune action effecut\u00e9e");
            }
        }
        else {
            UIMessage.send((short)16709);
        }
    }
    
    public static void setNorthWestDirection(final Event event) {
        final Fight fight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
        if (fight != null) {
            if (!PropertiesProvider.getInstance().getBooleanProperty("isInFightPlacement")) {
                if (fight.getTimeline().getCurrentFighter() == WakfuGameEntity.getInstance().getLocalPlayer() || fight.getTimeline().getCurrentFighter().isControlledByLocalPlayer()) {
                    UIMessage.send((short)18003);
                }
            }
            else if (!fight.getTimeline().hasCurrentFighter()) {
                ShortcutBarDialogActions.m_logger.info((Object)"Changement d'orientation inutile en phase de placement. Aucune action effecut\u00e9e");
            }
        }
        else {
            UIMessage.send((short)16707);
        }
    }
    
    public static void setNorthEastDirection(final Event event) {
        final Fight fight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
        if (fight != null) {
            if (!PropertiesProvider.getInstance().getBooleanProperty("isInFightPlacement")) {
                if (fight.getTimeline().getCurrentFighter() == WakfuGameEntity.getInstance().getLocalPlayer() || fight.getTimeline().getCurrentFighter().isControlledByLocalPlayer()) {
                    UIMessage.send((short)18006);
                }
            }
            else if (!fight.getTimeline().hasCurrentFighter()) {
                ShortcutBarDialogActions.m_logger.info((Object)"Changement d'orientation inutile en phase de placement. Aucune action effecut\u00e9e");
            }
        }
        else {
            UIMessage.send((short)16710);
        }
    }
    
    public static void openCloseSpellInventoryDialog(final Event event) {
        UIMessage.send((short)16406);
    }
    
    public static void openCloseFightActions(final Event event) {
        UIMessage.send((short)16434);
    }
    
    public static void openCloseItemInventoryDialog(final Event event) {
        UIMessage.send((short)16419);
    }
    
    public static void openCloseKardsInventoryDialog(final Event event) {
        UIMessage.send((short)16423);
    }
    
    public static void openCloseSecondaryShortcutBar(final Event event, final Integer shortcutBar) {
        final UIMessage message = new UIMessage();
        message.setId(16408);
        message.setIntValue(shortcutBar);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void dropItem(final Event event, final Byte shortcutBar) {
        if (event instanceof DropEvent) {
            EquipmentDialogActions.onDropItem();
            final DropEvent dropEvent = (DropEvent)event;
            final int position = dropEvent.getTarget().getRenderableParent().getCollection().getTableIndex(dropEvent.getTarget().getRenderableParent());
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final ShortcutBarManager shortcutBarManager = localPlayer.getShortcutBarManager();
            final ShortCutBarType shortCutBarType = shortcutBarManager.getShortcutBar(shortcutBar).getType();
            if (dropEvent.getValue() instanceof Item) {
                final Item item = (Item)dropEvent.getValue();
                final ReferenceItem referenceItem = (ReferenceItem)item.getReferenceItem();
                EquipmentDialogActions.onDropItem();
                final boolean isMount = item.hasPet() && item.getPet().getDefinition().hasMount();
                final boolean canAddInWorldBar = (isMount || item.isUsableInWorld()) && shortCutBarType == ShortCutBarType.WORLD;
                final boolean canAddInFightBar = shortCutBarType == ShortCutBarType.FIGHT && item.isUsableInFight();
                if (!canAddInWorldBar && !canAddInFightBar) {
                    return;
                }
                final UIShortcutMessage message = new UIShortcutMessage();
                message.setItem(item);
                message.setShorcutBarNumber(shortcutBar);
                message.setPosition(position);
                message.setId(16700);
                message.setBooleanValue(true);
                Worker.getInstance().pushMessage(message);
                return;
            }
            else {
                if (dropEvent.getValue() instanceof SpellLevel) {
                    final SpellLevel spellLevel = (SpellLevel)dropEvent.getValue();
                    if (spellLevel.getSpell().isPassive()) {
                        return;
                    }
                    dropEvent.setValue(localPlayer.getSpellLevelById(((SpellLevel)dropEvent.getValue()).getUniqueId()));
                }
                if (dropEvent.getValue() != null) {
                    final UIShortcutMessage message2 = new UIShortcutMessage();
                    message2.setItem(dropEvent.getValue());
                    message2.setShorcutBarNumber(shortcutBar);
                    message2.setPosition(position);
                    message2.setPreviousBar(ShortcutBarDialogActions.m_previousBar);
                    message2.setPreviousPosition(ShortcutBarDialogActions.m_previousPosition);
                    message2.setId(16700);
                    message2.setBooleanValue(true);
                    Worker.getInstance().pushMessage(message2);
                }
            }
        }
        ShortcutBarDialogActions.m_previousBar = -1;
        ShortcutBarDialogActions.m_previousPosition = -1;
    }
    
    public static void dragItem(final Event event, final Byte shortcutBar) {
        if (event instanceof DragEvent) {
            final DragEvent dragEvent = (DragEvent)event;
            final int position = dragEvent.getTarget().getRenderableParent().getCollection().getItemIndex(dragEvent.getValue());
            if (dragEvent.getValue() != null) {
                final UIShortcutMessage message = new UIShortcutMessage();
                message.setItem(dragEvent.getValue());
                message.setShorcutBarNumber(shortcutBar);
                ShortcutBarDialogActions.m_previousBar = shortcutBar;
                message.setPosition(ShortcutBarDialogActions.m_previousPosition = position);
                message.setId(16701);
                Worker.getInstance().pushMessage(message);
            }
        }
    }
    
    public static void dropOutItem(final Event event, final Byte shortcutBar) {
        if (event instanceof DropOutEvent) {
            final DropOutEvent dropOutEvent = (DropOutEvent)event;
            if (dropOutEvent.getValue() != null) {
                final UIShortcutMessage message = new UIShortcutMessage();
                message.setItem(dropOutEvent.getValue());
                message.setShorcutBarNumber(shortcutBar);
                message.setPosition(ShortcutBarDialogActions.m_previousPosition);
                message.setId(16702);
                Worker.getInstance().pushMessage(message);
            }
        }
        ShortcutBarDialogActions.m_previousBar = -1;
        ShortcutBarDialogActions.m_previousPosition = -1;
    }
    
    public static void addItem(final ItemEvent event, final Integer spellBar) {
        final UIShortcutMessage message = new UIShortcutMessage();
        message.setItem(event.getItemValue());
        message.setShorcutBarNumber(spellBar);
        message.setPosition(-1);
        message.setId(16700);
        message.setBooleanValue(true);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void selectPreviousShortcutBar(final MouseEvent event) {
        if (event.getButton() == 1) {
            UIMessage.send((short)16704);
        }
    }
    
    public static void selectNextShortcutBar(final MouseEvent event) {
        if (event.getButton() == 1) {
            UIMessage.send((short)16703);
        }
    }
    
    public static void useItem(final ItemEvent event) {
        if (event.getItemValue() != null && event.getItemValue() instanceof ShortCutItem) {
            final ShortCutItem shortcut = (ShortCutItem)event.getItemValue();
            useItem(event, shortcut, false);
        }
    }
    
    public static void doubleClickItem(final ItemEvent event) {
        if (event.getItemValue() != null && event.getItemValue() instanceof ShortCutItem) {
            final ShortCutItem shortcut = (ShortCutItem)event.getItemValue();
            useItem(event, shortcut, true);
        }
    }
    
    public static void selectSpell(final ItemEvent event, final SpellLevel spellLevel) {
        CharacterInfo concernedPlayer;
        final CharacterInfo player = concernedPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (event != null && event.getButton() == 3) {
            if (spellLevel == null) {
                ShortcutBarDialogActions.m_logger.error((Object)"On tente d'utiliser un sort null dans les barres raccourcis !!! ");
                return;
            }
            final ElementMap map = event.getCurrentTarget().getElementMap();
            if (map != null) {
                CompanionsEmbeddedActions.openSpellDescription(3, spellLevel, map.getId());
            }
        }
        else if (player.isOnFight() && player.getCurrentFight() != null) {
            if (spellLevel == null) {
                ShortcutBarDialogActions.m_logger.error((Object)"On tente d'utiliser un sort null dans les barres raccourcis !!! ");
                return;
            }
            concernedPlayer = player.getCurrentFight().getTimeline().getCurrentFighter();
            if (concernedPlayer == null) {
                concernedPlayer = player;
            }
            final Spell spell = spellLevel.getSpell();
            if (spell == null) {
                ShortcutBarDialogActions.m_logger.error((Object)("spell null pour le spellLevel de type " + spellLevel.getType() + "(uniqueId=" + spellLevel.getUniqueId() + ")"));
                return;
            }
            if (spell.isAssociatedWithItemUse()) {
                final UIFighterSelectSpellOverItemMessage message = new UIFighterSelectSpellOverItemMessage();
                message.setSpell(concernedPlayer.getSpellLevelById(spellLevel.getUniqueId()));
                final byte itemPos = EquipmentPosition.FIRST_WEAPON.m_id;
                final Item item = ((ArrayInventoryWithoutCheck<Item, R>)concernedPlayer.getEquipmentInventory()).getFromPosition(itemPos);
                message.setEquipmentPos(itemPos);
                message.setItem(item);
                message.setId(18012);
                Worker.getInstance().pushMessage(message);
            }
            else {
                final UISpellLevelSelectionMessage selectionMessage = new UISpellLevelSelectionMessage();
                selectionMessage.setSpell(concernedPlayer.getSpellLevelById(spellLevel.getUniqueId()));
                selectionMessage.setId(18002);
                if (spell.getId() == 787) {
                    if (!Xulor.getInstance().isLoaded("osamodasSymbiotDialog")) {
                        WakfuGameEntity.getInstance().pushFrame(UIOsamodasSymbiotFrame.getInstance());
                    }
                    else {
                        Worker.getInstance().pushMessage(selectionMessage);
                    }
                }
                else {
                    Worker.getInstance().pushMessage(selectionMessage);
                }
            }
        }
    }
    
    public static void useLeftHandWeapon(final ItemEvent e) {
        final ShortCutItem shortcut = (ShortCutItem)e.getItemValue();
        if (shortcut == null || !shortcut.isUsable()) {
            return;
        }
        UIMessage.send((short)16441);
    }
    
    public static void useRightHandWeapon(final ItemEvent e) {
        final ShortCutItem shortcut = (ShortCutItem)e.getItemValue();
        if (shortcut == null || !shortcut.isUsable()) {
            return;
        }
        UIMessage.send((short)16442);
    }
    
    public static void useItem(final ItemEvent event, final ShortCutItem shortcut, final boolean wasDoubleClick) {
        Label_0612: {
            if (shortcut != null && shortcut.isUsable()) {
                if (ShortcutBarDialogActions.m_startTime == -1L || System.currentTimeMillis() - ShortcutBarDialogActions.m_startTime >= ShortcutBarDialogActions.SECURITY_DELAY) {
                    ShortcutBarDialogActions.m_startTime = System.currentTimeMillis();
                }
                else if (System.currentTimeMillis() - ShortcutBarDialogActions.m_startTime < ShortcutBarDialogActions.SECURITY_DELAY) {
                    return;
                }
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                switch (shortcut.getType()) {
                    case EMOTE: {
                        if (wasDoubleClick) {
                            return;
                        }
                        final EmoteSmileyFieldProvider emote = ReferenceEmoteManager.INSTANCE.getEmoteOrSmiley(shortcut.getReferenceId());
                        final UIChatContentMessage msg = new UIChatContentMessage();
                        msg.setMessage(emote.getCommandText());
                        Worker.getInstance().pushMessage(msg);
                        break;
                    }
                    case SPELL_LEVEL: {
                        if (wasDoubleClick) {
                            return;
                        }
                        final Fight f = localPlayer.getCurrentFight();
                        CharacterInfo c = localPlayer;
                        if (f != null) {
                            c = f.getTimeline().getCurrentFighter();
                            if (c == null || !c.isControlledByLocalPlayer()) {
                                c = localPlayer;
                            }
                        }
                        selectSpell(event, c.getSpellLevelById(shortcut.getUniqueId()));
                        break;
                    }
                    case EQUIPMENT_SLOT: {
                        if (wasDoubleClick) {
                            return;
                        }
                        final UIFighterSelectAttackMessage message = new UIFighterSelectAttackMessage();
                        final ReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(2145);
                        final Item item = new Item(-1L);
                        item.initializeWithReferenceItem(refItem);
                        item.setQuantity((short)1);
                        message.setItem(item, refItem.getItemType().getEquipmentPositions()[0].m_id);
                        message.setId(18009);
                        Worker.getInstance().pushMessage(message);
                        break;
                    }
                    case USABLE_REFERENCE_ITEM: {
                        if (event != null && event.getButton() != 1) {
                            break;
                        }
                        final Item item2 = (Item)localPlayer.getBags().getFirstItemFromInventory(shortcut.getReferenceId());
                        if (item2 == null) {
                            return;
                        }
                        useUsableItem(item2, wasDoubleClick);
                        break;
                    }
                    case ITEM: {
                        if (wasDoubleClick) {
                            return;
                        }
                        switch (shortcut.getState()) {
                            case 2:
                            case 4:
                            case 5: {
                                if (event == null || event.getButton() == 1) {
                                    Item item2 = (Item)localPlayer.getBags().getFirstItemFromInventory(shortcut.getReferenceId());
                                    if (item2 == null) {
                                        item2 = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFirstWithReferenceId(shortcut.getReferenceId());
                                    }
                                    if (item2 != null && item2.isActive()) {
                                        useItem(shortcut, item2, localPlayer, wasDoubleClick);
                                    }
                                    break Label_0612;
                                }
                                break Label_0612;
                            }
                            case 3: {
                                final PetHolder item3 = ((ArrayInventoryWithoutCheck<PetHolder, R>)localPlayer.getEquipmentInventory()).getFirstWithReferenceId(shortcut.getReferenceId());
                                if (item3 != null && item3.hasPet() && item3.getPet().getDefinition().hasMount()) {
                                    final AbstractOccupation occ = localPlayer.getCurrentOccupation();
                                    if (occ != null && occ.getOccupationTypeId() == 14) {
                                        final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
                                        netMsg.setModificationType((byte)2);
                                        netMsg.setOccupationType((short)14);
                                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
                                    }
                                    else {
                                        final StartRidingRequestMessage msg2 = new StartRidingRequestMessage();
                                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg2);
                                    }
                                    break Label_0612;
                                }
                                break Label_0612;
                            }
                            case 0: {
                                addItemToEquipmentFromShortcut(shortcut);
                                break Label_0612;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
    
    private static boolean hasCriteria(final Item item, final ActionsOnItem use) {
        return item.getReferenceItem().getCriterion(use) != null;
    }
    
    private static boolean criteriaIsValid(final Item item, final LocalPlayerCharacter localPlayer, final ActionsOnItem use) {
        final SimpleCriterion criterion = item.getReferenceItem().getCriterion(use);
        return criterion == null || criterion.isValid(localPlayer, localPlayer.getPosition(), item, localPlayer.getEffectContext());
    }
    
    private static void useItem(final ShortCutItem shortcut, final Item item, final LocalPlayerCharacter localPlayer, final boolean wasDoubleClick) {
        final AbstractItemAction itemAction = item.getReferenceItem().getItemAction();
        if (itemAction != null && !localPlayer.isOnFight()) {
            if (criteriaIsValid(item, localPlayer, ActionsOnItem.USE)) {
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventItemUsed(item.getReferenceItem()));
                ((AbstractClientItemAction)itemAction).run(item);
            }
            return;
        }
        if (localPlayer.isOnFight()) {
            if ((item.isUsableInFight() && !hasCriteria(item, ActionsOnItem.USE_IN_FIGHT)) || criteriaIsValid(item, localPlayer, ActionsOnItem.USE_IN_FIGHT)) {
                final UIFighterSelectAttackMessage message = new UIFighterSelectAttackMessage();
                if (shortcut.getState() == 2) {
                    try {
                        message.setItem(item, item.getReferenceItem().getItemType().getEquipmentPositions()[0].getId());
                    }
                    catch (Exception e) {
                        message.setItem(item, (byte)(-1));
                    }
                }
                else {
                    message.setItem(item, (byte)(-1));
                }
                message.setId(18009);
                Worker.getInstance().pushMessage(message);
            }
            return;
        }
        if ((item.isUsableInWorld() && !hasCriteria(item, ActionsOnItem.USE)) || criteriaIsValid(item, localPlayer, ActionsOnItem.USE)) {
            final UIUseItemMessage message2 = new UIUseItemMessage(item, (byte)shortcut.getReferenceId(), wasDoubleClick);
            message2.setId(16712);
            Worker.getInstance().pushMessage(message2);
            return;
        }
        if (shortcut.getState() == 2) {
            removeItemFromeEquipmentFromShortcut(shortcut);
            return;
        }
        addItemToEquipmentFromShortcut(shortcut);
    }
    
    private static void useItem(final Item item, final LocalPlayerCharacter localPlayer, final boolean wasDoubleClick) {
        if (localPlayer.isOnFight()) {
            if (((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getWithUniqueId(item.getUniqueId()) != null) {
                final UIFighterSelectAttackMessage message = new UIFighterSelectAttackMessage();
                try {
                    final EquipmentPosition[] equipmentPositions = item.getReferenceItem().getItemType().getEquipmentPositions();
                    message.setItem(item, (byte)((equipmentPositions.length > 0) ? equipmentPositions[0].m_id : -1));
                }
                catch (Exception e) {
                    message.setItem(item, (byte)(-1));
                }
                message.setId(18009);
                Worker.getInstance().pushMessage(message);
            }
            else {
                final UIUseItemMessage message2 = new UIUseItemMessage(item, (byte)item.getReferenceId(), wasDoubleClick);
                message2.setId(16712);
                Worker.getInstance().pushMessage(message2);
            }
            return;
        }
        final UIUseItemMessage message2 = new UIUseItemMessage(item, (byte)item.getReferenceId(), wasDoubleClick);
        message2.setId(16712);
        Worker.getInstance().pushMessage(message2);
    }
    
    private static void removeItemFromeEquipmentFromShortcut(final ShortCutItem shortcut) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.isOnFight()) {
            return;
        }
        final Item item = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFirstWithReferenceId(shortcut.getReferenceId());
        if (item != null && item.isActive()) {
            final UIItemMessage message = new UIItemMessage();
            message.setItem(item);
            message.setPosition((short)(-1));
            message.setId(16803);
            Worker.getInstance().pushMessage(message);
            EquipmentDialogActions.onChangeEquipement(localPlayer);
        }
    }
    
    private static void addItemToEquipmentFromShortcut(final ShortCutItem shortcut) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractBag container = localPlayer.getBags().getFirstInventoryFromItem(shortcut.getReferenceId());
        if (container != null) {
            final Item item = container.getFirstWithReferenceId(shortcut.getReferenceId());
            if (((ReferenceItem)item.getReferenceItem()).getReferenceItemDisplayer().isEquipable() && container instanceof Bag) {
                EquipmentDialogActions.addToEquipment(item, (Bag)container);
                EquipmentDialogActions.onChangeEquipement(localPlayer);
            }
        }
    }
    
    public static boolean useUsableItem(final Item item, final boolean wasDoubleClick) {
        final LocalPlayerCharacter localPlayer = UIEquipmentFrame.getCharacter();
        if (localPlayer.isDead()) {
            ErrorsMessageTranslator.getInstance().pushMessage(6, 3, new Object[0]);
            return false;
        }
        if (item != null && item.isActive() && ItemDisplayerImpl.isItemUsable(item)) {
            useItem(item, localPlayer, wasDoubleClick);
            return true;
        }
        if (PetDefinitionManager.INSTANCE.isPetEquipment(item.getReferenceId())) {
            ErrorsMessageTranslator.getInstance().pushMessage(64, 3, new Object[0]);
        }
        final AbstractOccupation occupation = localPlayer.getCurrentOccupation();
        if (occupation != null && occupation.getOccupationTypeId() == 14) {
            useItem(item, localPlayer, wasDoubleClick);
            return true;
        }
        ErrorsMessageTranslator.getInstance().pushMessage(61, 3, new Object[0]);
        return false;
    }
    
    public static void openCloseShortcutDescription(final ItemEvent event, Container parent) {
        if (event.getItemValue() != null && event.getItemValue() instanceof AbstractShortCutItem) {
            parent = event.getTarget();
            CharacterInfo concernedPlayer;
            final LocalPlayerCharacter player = (LocalPlayerCharacter)(concernedPlayer = WakfuGameEntity.getInstance().getLocalPlayer());
            if (player.isOnFight() && player.getCurrentFight() != null) {
                if (player.getCurrentFight().getTimeline().hasCurrentFighter()) {
                    final CharacterInfo currentFighter = player.getCurrentFight().getTimeline().getCurrentFighter();
                    final boolean controlledByLocalPlayer = !currentFighter.isControlledByAI() && currentFighter.isControlledByLocalPlayer();
                    if (currentFighter.getId() == player.getId() || controlledByLocalPlayer) {
                        concernedPlayer = currentFighter;
                    }
                }
                if (concernedPlayer == null) {
                    concernedPlayer = player;
                }
            }
            final AbstractShortCutItem shortcut = (AbstractShortCutItem)event.getItemValue();
            if (shortcut.getType() == ShortCutType.SPELL_LEVEL) {
                final SpellLevel spell = concernedPlayer.getSpellLevelById(shortcut.getUniqueId());
                PropertiesProvider.getInstance().setPropertyValue("describedSpell", spell);
                final PopupElement popup = (PopupElement)parent.getElementMap().getElement("spellDetailPopup");
                if (event.getType() == Events.ITEM_OVER && !MasterRootContainer.getInstance().isDragging()) {
                    XulorActions.popup(popup, parent);
                }
                else if (event.getType() == Events.ITEM_OUT) {
                    XulorActions.closePopup(event, popup);
                }
            }
            if (shortcut.getType() == ShortCutType.EQUIPMENT_SLOT) {
                final PopupElement popup2 = (PopupElement)parent.getElementMap().getElement("itemDetailPopup");
                if (event.getType() == Events.ITEM_OVER && !MasterRootContainer.getInstance().isDragging()) {
                    Item item;
                    if (shortcut.getReferenceId() == 2145) {
                        item = new Item(-1L);
                        final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(2145);
                        item.initializeWithReferenceItem(refItem);
                        item.setQuantity((short)1);
                    }
                    else {
                        item = ((ArrayInventoryWithoutCheck<Item, R>)player.getEquipmentInventory()).getWithUniqueId(shortcut.getUniqueId());
                        if (item == null) {
                            item = player.getBags().getItemFromInventories(shortcut.getUniqueId());
                        }
                    }
                    PropertiesProvider.getInstance().setPropertyValue("handsOfWeapon", "twoHands");
                    PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", item);
                    PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", true);
                    XulorActions.popup(popup2, parent);
                }
                else if (event.getType() == Events.ITEM_OUT) {
                    PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", null);
                    PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", false);
                    PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
                    PropertiesProvider.getInstance().setPropertyValue("handsOfWeapon", "none");
                    XulorActions.closePopup(event, popup2);
                }
            }
            if (shortcut.getType() == ShortCutType.EMOTE) {
                final EmoteSmileyFieldProvider emote = ReferenceEmoteManager.INSTANCE.getEmoteOrSmiley(shortcut.getReferenceId());
                PropertiesProvider.getInstance().setPropertyValue("currentDescribedEmote", emote);
                final PopupElement popup = (PopupElement)parent.getElementMap().getElement("emotePopup");
                if (event.getType() == Events.ITEM_OVER && !MasterRootContainer.getInstance().isDragging()) {
                    XulorActions.popup(popup, parent);
                }
                else if (event.getType() == Events.ITEM_OUT) {
                    XulorActions.closePopup(event, popup);
                }
            }
            if (shortcut.getType() == ShortCutType.ITEM) {
                Item item2 = ((ArrayInventoryWithoutCheck<Item, R>)WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory()).getFirstWithReferenceId(shortcut.getReferenceId());
                if (item2 != null) {
                    final short position = WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory().getPosition(item2.getUniqueId());
                    if (position == 15 || position == 16) {
                        if (item2.getType().getId() == 121) {
                            ShortcutBarDialogActions.m_logger.info((Object)"Lampe");
                        }
                        else {
                            final PopupElement popup3 = (PopupElement)parent.getElementMap().getElement("itemDetailPopup");
                            if (event.getType() == Events.ITEM_OVER && !MasterRootContainer.getInstance().isDragging()) {
                                if (position == 15) {
                                    if (item2.getReferenceItem().isTwoHandedWeapon()) {
                                        PropertiesProvider.getInstance().setPropertyValue("handsOfWeapon", "twoHands");
                                    }
                                    else {
                                        PropertiesProvider.getInstance().setPropertyValue("handsOfWeapon", "rightHand");
                                    }
                                }
                                else if (position == 16) {
                                    PropertiesProvider.getInstance().setPropertyValue("handsOfWeapon", "leftHand");
                                }
                                PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", item2);
                                PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", true);
                                XulorActions.popup(popup3, parent);
                            }
                            else if (event.getType() == Events.ITEM_OUT) {
                                PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", null);
                                PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", false);
                                PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
                                PropertiesProvider.getInstance().setPropertyValue("handsOfWeapon", "none");
                                XulorActions.closePopup(event, popup3);
                            }
                        }
                    }
                    else {
                        PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
                        if (item2.isUsable()) {
                            PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", true);
                        }
                        else {
                            PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", false);
                        }
                        final PopupElement popup3 = (PopupElement)parent.getElementMap().getElement("itemDetailPopup");
                        if (event.getType() == Events.ITEM_OVER && !MasterRootContainer.getInstance().isDragging()) {
                            PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", item2);
                            XulorActions.popup(popup3, parent);
                        }
                        else if (event.getType() == Events.ITEM_OUT) {
                            PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", null);
                            XulorActions.closePopup(event, popup3);
                        }
                    }
                }
                else {
                    item2 = (Item)WakfuGameEntity.getInstance().getLocalPlayer().getBags().getFirstItemFromInventory(shortcut.getReferenceId());
                    if (item2 != null) {
                        final PopupElement popup = (PopupElement)parent.getElementMap().getElement("itemDetailPopup");
                        if (event.getType() == Events.ITEM_OVER && !MasterRootContainer.getInstance().isDragging()) {
                            PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", item2);
                            PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", true);
                            XulorActions.popup(popup, parent);
                        }
                        else if (event.getType() == Events.ITEM_OUT) {
                            PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", null);
                            PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
                            PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", false);
                            XulorActions.closePopup(event, popup);
                        }
                    }
                }
            }
            else if (ShortCutType.USABLE_REFERENCE_ITEM == shortcut.getType()) {
                final ReferenceItem refItem2 = ReferenceItemManager.getInstance().getReferenceItem(shortcut.getReferenceId());
                if (refItem2 == null) {
                    return;
                }
                final PopupElement popup = (PopupElement)parent.getElementMap().getElement("itemDetailPopup");
                if (event.getType() == Events.ITEM_OVER && !MasterRootContainer.getInstance().isDragging()) {
                    PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", refItem2);
                    PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", true);
                    XulorActions.popup(popup, parent);
                }
                else if (event.getType() == Events.ITEM_OUT) {
                    PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", null);
                    PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
                    PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", false);
                    XulorActions.closePopup(event, popup);
                }
            }
        }
    }
    
    public static void toggleShortcutBarMode(final Event event) {
        final UIMessage message = new UIMessage();
        if (WakfuGameEntity.getInstance().getLocalPlayer().getShortcutBarManager().getCurrentBarType() == ShortCutBarType.FIGHT) {
            message.setId(16432);
        }
        else {
            message.setId(16433);
        }
        Worker.getInstance().pushMessage(message);
    }
    
    public static void openEmotesInventory(final SelectionChangedEvent event) {
        final boolean selected = event.isSelected();
        if (!selected && WakfuGameEntity.getInstance().hasFrame(UIEmotesDialogFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIEmotesDialogFrame.getInstance());
        }
        else if (selected && !WakfuGameEntity.getInstance().hasFrame(UIEmotesDialogFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIEmotesDialogFrame.getInstance());
        }
        UIControlCenterContainerFrame.getInstance().selectEmoteButton(selected);
    }
    
    public static void displaySpellDescription(final Event e, final SpellLevel spell) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)ShortcutBarDialogActions.class);
        ShortcutBarDialogActions.m_shortCutsActive = true;
        ShortcutBarDialogActions.SECURITY_DELAY = 500L;
        ShortcutBarDialogActions.m_startTime = -1L;
    }
}
