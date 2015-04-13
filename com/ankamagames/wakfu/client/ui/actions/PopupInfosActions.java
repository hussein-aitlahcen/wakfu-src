package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.fight.history.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.wakfu.client.core.game.protector.inventory.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.popupInfos.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

@XulorActionsTag
public class PopupInfosActions
{
    public static final String PACKAGE = "wakfu.popupInfos";
    
    public static void showPopup(final Event event, final PopupElement popup) {
        showPopup(event, popup, null);
    }
    
    public static void showPopup(final Event event, final PopupElement popup, final Widget target) {
        if (event instanceof ItemEvent) {
            final Object value = ((ItemEvent)event).getItemValue();
            if (value instanceof FieldProvider) {
                showPopup(event, (FieldProvider)value, popup, target);
                return;
            }
        }
        showPopup(event, null, popup, target);
    }
    
    public static void showPopup(final Event event, final FieldProvider value, final PopupElement popup) {
        showPopup(event, value, popup, null);
    }
    
    public static void showPopup(final Event event, final FieldProvider value, final PopupElement popup, final Widget target) {
        if (value instanceof ShortCutItem) {
            getItemValue((ShortCutItem)value);
        }
        else if (value instanceof SymbiotInvocationCharacteristics) {
            PropertiesProvider.getInstance().setPropertyValue("osamodasSymbiot.describedCreature", value);
        }
        else if (value instanceof ItemSet || value instanceof Item || value instanceof ReferenceItem || value instanceof ReferenceItemFieldProvider) {
            PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", value);
        }
        else if (value instanceof ItemDisplayerImpl.DisabledItem) {
            final ItemDisplayerImpl.DisabledItem disabledItem = (ItemDisplayerImpl.DisabledItem)value;
            if (disabledItem.getItem() instanceof Item) {
                PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", disabledItem.getItem());
            }
        }
        else if (value instanceof RunningEffectFieldProvider) {
            PropertiesProvider.getInstance().setLocalPropertyValue("describedRunningEffect", value, event.getTarget().getElementMap());
        }
        else if (value instanceof ChallengeView) {
            final ChallengeView challengeItemView = (ChallengeView)value;
            PropertiesProvider.getInstance().setLocalPropertyValue("displayedAchievement", challengeItemView, event.getTarget().getElementMap());
        }
        else if (value instanceof ProtectorChallengeItemView) {
            final ProtectorChallengeItemView challengeItemView2 = (ProtectorChallengeItemView)value;
            PropertiesProvider.getInstance().setLocalPropertyValue("displayedAchievement", ChallengeViewManager.INSTANCE.getChallengeView(challengeItemView2.getChallengeId()), event.getTarget().getElementMap());
        }
        else if (value instanceof ProtectorClimateItemView) {
            PropertiesProvider.getInstance().setPropertyValue("climateDetail", value);
        }
        else if (value instanceof RecipeView) {
            PropertiesProvider.getInstance().setPropertyValue("overRecipe", value);
        }
        if (target == null) {
            XulorActions.popup(event, popup);
        }
        else {
            XulorActions.popup(event, popup, target);
        }
    }
    
    public static void closePopup(final Event event) {
        MasterRootContainer.getInstance().getPopupContainer().hide();
        PropertiesProvider.getInstance().removeProperty("describedSpell");
        PropertiesProvider.getInstance().removeProperty("currentDescribedContainer");
        PropertiesProvider.getInstance().removeProperty("handsOfWeapon");
        PropertiesProvider.getInstance().removeProperty("itemPopupDetail");
        PropertiesProvider.getInstance().removeProperty("isFromEquipedShortcut");
        PropertiesProvider.getInstance().removeProperty("isFromShortcut");
        PropertiesProvider.getInstance().removeProperty("currentDescribedChallenge");
        MasterRootContainer.getInstance().getPopupContainer().hide();
    }
    
    private static void getItemValue(final ShortCutItem shortcut) {
        CharacterInfo concernedPlayer;
        final CharacterInfo player = concernedPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (player.isOnFight() && player.getCurrentFight() != null) {
            if (player.getCurrentFight().getTimeline().hasCurrentFighter() && player.getCurrentFight().getTimeline().getCurrentFighter().getId() == player.getId()) {
                concernedPlayer = player.getCurrentFight().getTimeline().getCurrentFighter();
            }
            if (concernedPlayer == null) {
                concernedPlayer = player;
            }
        }
        if (shortcut.getType() == ShortCutType.SPELL_LEVEL) {
            final SpellLevel spell = concernedPlayer.getSpellLevelById(shortcut.getUniqueId());
            PropertiesProvider.getInstance().setPropertyValue("describedSpell", spell);
        }
        if (shortcut.getType() == ShortCutType.EQUIPMENT_SLOT) {
            if (!MasterRootContainer.getInstance().isDragging()) {
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
            }
            else {
                PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", null);
                PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", false);
                PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
                PropertiesProvider.getInstance().setPropertyValue("handsOfWeapon", "none");
            }
        }
        if (shortcut.getType() == ShortCutType.ITEM) {
            Item item = ((ArrayInventoryWithoutCheck<Item, R>)WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory()).getFirstWithReferenceId(shortcut.getReferenceId());
            if (item != null) {
                final short position = WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory().getPosition(item.getUniqueId());
                if (position == 15 || position == 16) {
                    if (!MasterRootContainer.getInstance().isDragging()) {
                        if (position == 15) {
                            if (item.getReferenceItem().isTwoHandedWeapon()) {
                                PropertiesProvider.getInstance().setPropertyValue("handsOfWeapon", "twoHands");
                            }
                            else {
                                PropertiesProvider.getInstance().setPropertyValue("handsOfWeapon", "rightHand");
                            }
                        }
                        else if (position == 16) {
                            PropertiesProvider.getInstance().setPropertyValue("handsOfWeapon", "leftHand");
                        }
                        PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", item);
                        PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", true);
                    }
                    else {
                        PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", null);
                        PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", false);
                        PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
                        PropertiesProvider.getInstance().setPropertyValue("handsOfWeapon", "none");
                    }
                }
                else {
                    PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
                    if (item.isUsable()) {
                        PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", true);
                    }
                    else {
                        PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", false);
                    }
                    PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", item);
                }
            }
            else {
                item = (Item)WakfuGameEntity.getInstance().getLocalPlayer().getBags().getFirstItemFromInventory(shortcut.getReferenceId());
                if (item != null) {
                    if (!MasterRootContainer.getInstance().isDragging()) {
                        PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", item);
                        PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", true);
                    }
                    else {
                        PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", null);
                        PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
                        PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", false);
                    }
                }
            }
        }
    }
    
    public static void showPopup(final Event event, final FieldProvider content, final String time) {
        showPopup(content, Integer.parseInt(time));
    }
    
    public static void showPopup(final FieldProvider content, final int time) {
        final UIShowPopupInfosMessage uiShowPopupInfosMessage = new UIShowPopupInfosMessage(content);
        uiShowPopupInfosMessage.setIntValue(time);
        uiShowPopupInfosMessage.setId(19300);
        Worker.getInstance().pushMessage(uiShowPopupInfosMessage);
    }
    
    public static void hidePopup(final ItemEvent itemEvent) {
        final Object value = itemEvent.getItemValue();
        if (value instanceof FieldProvider) {
            hidePopup(itemEvent, (FieldProvider)value);
        }
    }
    
    public static void hidePopup(final Event event, final FieldProvider content) {
        final UIShowPopupInfosMessage uiShowPopupInfosMessage = new UIShowPopupInfosMessage(content);
        uiShowPopupInfosMessage.setId(19301);
        Worker.getInstance().pushMessage(uiShowPopupInfosMessage);
    }
}
