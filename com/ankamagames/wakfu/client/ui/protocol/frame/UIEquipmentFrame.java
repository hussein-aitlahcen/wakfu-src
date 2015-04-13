package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.game.embeddedTutorial.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.core.dragndrop.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.wakfu.client.core.game.item.xp.*;
import com.ankamagames.wakfu.common.game.item.xp.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.reflect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class UIEquipmentFrame extends UICompanionsEmbeddedFrame implements MessageFrame, BigDialogLoadListener
{
    private static UIEquipmentFrame m_instance;
    private DragCancelListener m_dragCancelListener;
    protected static final Logger m_logger;
    private DialogLoadListener m_vaulAccessibilityDialogLoadListener;
    private DialogUnloadListener m_vaultAccessibilityDialogUnloadListener;
    private DialogUnloadListener m_dialogUnloadListener;
    private long m_characterId;
    
    public UIEquipmentFrame() {
        super();
        this.m_vaulAccessibilityDialogLoadListener = new DialogLoadListener() {
            @Override
            public void dialogLoaded(final String id) {
                UIVaultFrame.getInstance().updateVaultAccessibility();
            }
        };
        this.m_vaultAccessibilityDialogUnloadListener = new DialogUnloadListener() {
            @Override
            public void dialogUnloaded(final String id) {
                UIVaultFrame.getInstance().updateVaultAccessibility();
            }
        };
        this.m_dialogUnloadListener = new DialogUnloadListener() {
            @Override
            public void dialogUnloaded(final String id) {
                final TLongObjectIterator<CharacterView> it = UICompanionsEmbeddedFrame.m_characterViews.iterator();
                while (it.hasNext()) {
                    it.advance();
                }
            }
        };
    }
    
    public static UIEquipmentFrame getInstance() {
        return UIEquipmentFrame.m_instance;
    }
    
    @Override
    public void dialogLoaded(final String id) {
        if (id != null && !id.equals("equipmentDialog")) {
            Xulor.getInstance().unload("equipmentDialog");
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public String getBaseDialogId() {
        return "equipmentDialog";
    }
    
    public void setEnableBagTransfer(final boolean enable) {
        PropertiesProvider.getInstance().setPropertyValue("equipment.inventory.enableMove", enable);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19460: {
                final UIMessage msg = (UIMessage)message;
                this.m_characterId = msg.getLongValue();
                final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(this.m_characterId);
                PropertiesProvider.getInstance().setLocalPropertyValue("localPlayer", hero, "equipmentDialog");
                Xulor.getInstance().unload("cosmeticsDialog");
                this.onChangeCharacterSlotsRefresh();
                return false;
            }
            case 17710: {
                final UIItemMessage uiItemMessage = (UIItemMessage)message;
                final LocalPlayerCharacter localPlayer = HeroUtils.getHeroWithBagUid(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), uiItemMessage.getSourceUniqueId());
                final LocalPlayerCharacter hero2 = HeroesManager.INSTANCE.getHero(uiItemMessage.getDestinationCharacter().getCharacterInfo().getId());
                final ClientBagContainer containerBagContainer = hero2.getBags();
                final AbstractBag source = localPlayer.getBags().get(uiItemMessage.getSourceUniqueId());
                final Item item = uiItemMessage.getItem();
                final byte firstFreePlaceIndex = containerBagContainer.getFirstFreePlaceIndex(BagStoringManager.INSTANCE.isTypedBag(item.getReferenceId()));
                if (firstFreePlaceIndex == -1) {
                    final String errorMessage = WakfuTranslator.getInstance().getString("error.bagContainerFull");
                    final ChatMessage chatMessage = new ChatMessage(errorMessage);
                    chatMessage.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(chatMessage);
                    return false;
                }
                if (!AbstractBag.checkCriteria(item, localPlayer)) {
                    final String errorMessage = WakfuTranslator.getInstance().getString("item.error.impossibilityToEquip");
                    final ChatMessage chatMessage = new ChatMessage(errorMessage);
                    chatMessage.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(chatMessage);
                    return false;
                }
                if (source != null) {
                    final byte destinationIndex = (uiItemMessage.getDestinationPosition() != -1) ? uiItemMessage.getDestinationPosition() : firstFreePlaceIndex;
                    final AddBagRequestMessage addBagRequestMessage = new AddBagRequestMessage();
                    addBagRequestMessage.setItemUId(item.getUniqueId());
                    addBagRequestMessage.setSource(source.getUid());
                    addBagRequestMessage.setDestinationCharacterId(hero2.getId());
                    addBagRequestMessage.setDestinationPosition(destinationIndex);
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(addBagRequestMessage);
                }
                else {
                    UIEquipmentFrame.m_logger.warn((Object)("On ne trouve pas la source de l'item que le joueur veut ajouter. Id de la source transmis : " + uiItemMessage.getSourceUniqueId()));
                }
                return false;
            }
            case 17711: {
                final UIItemMessage uiItemMessage = (UIItemMessage)message;
                final LocalPlayerCharacter localPlayer = HeroUtils.getHeroWithBagUid(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), uiItemMessage.getSourceUniqueId());
                final ClientBagContainer containerBagContainer2 = localPlayer.getBags();
                final AbstractBag container = containerBagContainer2.get(uiItemMessage.getSourceUniqueId());
                if (!container.isEmpty()) {
                    ChatHelper.pushErrorMessage("error.bagNotEmpty", new Object[0]);
                    return false;
                }
                final long id = uiItemMessage.getDestinationUniqueId();
                final short positionInBag = uiItemMessage.getDestinationPosition();
                if (id == container.getUid()) {
                    ChatHelper.pushErrorMessage("error.sameBag", new Object[0]);
                    return false;
                }
                final AbstractBag destBag = HeroUtils.getBagFromHero(localPlayer.getOwnerId(), id);
                if (destBag == null) {
                    return false;
                }
                final int refId = container.getReferenceId();
                final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(refId);
                final Item copyItem = Item.newInstance(container.getUid(), refItem);
                copyItem.setQuantity((short)1);
                try {
                    if (!destBag.canAdd(copyItem)) {
                        return false;
                    }
                }
                catch (Exception e) {
                    return false;
                }
                finally {
                    copyItem.release();
                }
                final RemoveBagRequestMessage removeBagRequestMessage = new RemoveBagRequestMessage();
                removeBagRequestMessage.setItemUId(id);
                removeBagRequestMessage.setNewUid(container.getUid());
                removeBagRequestMessage.setSource(uiItemMessage.getSourceUniqueId());
                removeBagRequestMessage.setDestination(uiItemMessage.getDestinationUniqueId());
                removeBagRequestMessage.setDestinationPosition((byte)positionInBag);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(removeBagRequestMessage);
                this.refreshBagDisplay(container.getUid());
                return false;
            }
            case 17712: {
                final UIMessage uiMessage = (UIMessage)message;
                final ChangeBagPositionRequestMessage netMessage = new ChangeBagPositionRequestMessage();
                netMessage.setDestinationPosition(uiMessage.getByteValue());
                netMessage.setBagId(uiMessage.getLongValue());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                return false;
            }
            case 16418: {
                final UIMessage uiMessage = (UIMessage)message;
                if (Xulor.getInstance().isLoaded("splitStackDialog")) {
                    Xulor.getInstance().unload("splitStackDialog");
                }
                WakfuGameEntity.getInstance().getLocalPlayer().getBags().actualizeBagListToXulor();
                Xulor.getInstance().unload(uiMessage.getStringValue());
                return false;
            }
            case 16802: {
                PropertiesProvider.getInstance().setPropertyValue("selectedItemInventoryDescription", null);
                final UIItemMessage msg2 = (UIItemMessage)message;
                PropertiesProvider.getInstance().setPropertyValue("selectedItemInventoryDescription", msg2.getItem());
                return false;
            }
            default: {
                return super.onMessage(message);
            }
        }
    }
    
    public void refreshBagDisplay(final long containerUniqueId) {
        final LocalPlayerCharacter localPlayer = getCharacter();
        final ClientBagContainer containerBagContainer = localPlayer.getBags();
        containerBagContainer.actualizeBagListToXulor();
        localPlayer.updateShortcutBars();
        final Item itemDetail = (Item)PropertiesProvider.getInstance().getObjectProperty("itemDetail", "equipmentDialog");
        if (itemDetail != null && itemDetail.getUniqueId() == containerUniqueId) {
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("equipmentDialog");
            if (map != null) {
                PropertiesProvider.getInstance().firePropertyValueChanged("itemDetail", "isEquiped", map);
            }
        }
    }
    
    public void openEquipment() {
        if (!WakfuGameEntity.getInstance().hasFrame(this)) {
            WakfuGameEntity.getInstance().pushFrame(this);
        }
    }
    
    @Override
    public String loadMainDialog(final CharacterView characterView) {
        if (!Xulor.getInstance().isLoaded("equipmentDialog")) {
            EmbeddedTutorialManager.getInstance().launchTutorial(TutorialEvent.INVENTORY_OPENED, "equipmentDialog");
        }
        final String mapId = super.loadMainDialog(characterView);
        this.applyDragScroll(mapId);
        return mapId;
    }
    
    public void applyDragScroll(final String mapId) {
        final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap(mapId);
        final ScrollContainer scrollContainer = (ScrollContainer)elementMap.getElement("inventoriesScrollContainer");
        scrollContainer.setAutoScrollVertical(true);
        scrollContainer.setDndListenerContentValidator(new DNDListenerContentValidator() {
            @Override
            public boolean validate(final Object content) {
                return content != null && content instanceof Item;
            }
        });
        PropertiesProvider.getInstance().setLocalPropertyValue("editableBag", null, elementMap);
    }
    
    @Override
    public String loadSecondaryDialog(final CharacterView characterView, final String windowId, final int screenX, final int screenY) {
        final String mapId = super.loadSecondaryDialog(characterView, windowId, screenX, screenY);
        if (mapId == null) {
            return null;
        }
        if (!characterView.isCompanion()) {
            this.applyDragScroll(mapId);
        }
        PropertiesProvider.getInstance().setLocalPropertyValue("itemLevelController", null, mapId);
        PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", null, mapId);
        PropertiesProvider.getInstance().setLocalPropertyValue("equipment.showBags", false, mapId);
        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventCompanionOpenEquipment());
        return mapId;
    }
    
    @Override
    public String loadHeroSecondaryDialog(final CharacterView characterView, final String windowId, final int screenX, final int screenY, final Window w) {
        final String mapId = super.loadHeroSecondaryDialog(characterView, windowId, screenX, screenY, w);
        if (mapId == null) {
            return null;
        }
        if (!characterView.isCompanion()) {
            this.applyDragScroll(mapId);
        }
        if (!Xulor.getInstance().isActionClassLoaded("wakfu.equipment")) {
            Xulor.getInstance().putActionClass("wakfu.equipment", EquipmentDialogActions.class);
        }
        PropertiesProvider.getInstance().setLocalPropertyValue("itemLevelController", null, mapId);
        PropertiesProvider.getInstance().setLocalPropertyValue("localPlayer", HeroesManager.INSTANCE.getHero(characterView.getCharacterInfo().getId()), mapId);
        PropertiesProvider.getInstance().setLocalPropertyValue("equipment.showBags", true, mapId);
        return mapId;
    }
    
    private void checkInventoryButton() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("worldAndFightBarDialog");
        if (map == null) {
            return;
        }
        final Widget button = (Widget)map.getElement("inventoryButton");
        if (button == null) {
            return;
        }
        button.getAppearance().removeTweensOfType(ModulationColorTween.class);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        super.onFrameAdd(frameHandler, isAboutToBeAdded);
        if (!isAboutToBeAdded) {
            this.checkInventoryButton();
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final ClientBagContainer bagContainer = localPlayer.getBags();
            this.m_characterId = localPlayer.getId();
            final Item itemDetail = bagContainer.getFirstItemFromInventory();
            this.refreshItemDetail(itemDetail);
            PropertiesProvider.getInstance().setLocalPropertyValue("localPlayer", localPlayer, "equipmentDialog");
            PropertiesProvider.getInstance().setLocalPropertyValue("equipment.showBags", true, "equipmentDialog");
            PropertiesProvider.getInstance().setPropertyValue("formatedKamas", WakfuTranslator.getInstance().formatNumber(localPlayer.getWallet().getAmountOfCash()));
            this.m_dragCancelListener = new DragCancelListener() {
                @Override
                public void cancel() {
                    EquipmentDialogActions.onDropItem();
                }
            };
            DragNDropManager.getInstance().addDragCancelListener(this.m_dragCancelListener);
            bagContainer.actualizeBagListToXulor();
            if (!Xulor.getInstance().isActionClassLoaded("wakfu.equipment")) {
                Xulor.getInstance().putActionClass("wakfu.equipment", EquipmentDialogActions.class);
            }
            WakfuSoundManager.getInstance().playGUISound(600026L);
            UIVaultFrame.getInstance().updateVaultAccessibility();
            PropertiesProvider.getInstance().setPropertyValue("vaultVisibility", SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.VAULT_ENABLED));
            Xulor.getInstance().addDialogLoadListener(this.m_vaulAccessibilityDialogLoadListener);
            Xulor.getInstance().addDialogUnloadListener(this.m_vaultAccessibilityDialogUnloadListener);
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
        }
    }
    
    private void refreshItemDetail(Item itemDetail) {
        if (itemDetail != null && itemDetail.hasPet()) {
            final PetDetailDialogView petDetailDialogView = new PetDetailDialogView(itemDetail);
            PropertiesProvider.getInstance().setPropertyValue("pet", petDetailDialogView);
        }
        else {
            PropertiesProvider.getInstance().setPropertyValue("pet", null);
        }
        if (itemDetail != null && itemDetail.hasXp()) {
            itemDetail = itemDetail.getClone();
            final ItemXpControllerLevelModifier controller = new ItemXpControllerLevelModifier(itemDetail);
            PropertiesProvider.getInstance().setLocalPropertyValue("itemLevelController", controller, "equipmentDialog");
        }
        PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", itemDetail, "equipmentDialog");
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", null, "equipmentDialog");
            PropertiesProvider.getInstance().setPropertyValue("pet", null);
            if (!WakfuGameEntity.getInstance().hasFrame(UICraftTableFrame.getInstance())) {
                Xulor.getInstance().removeActionClass("wakfu.equipment");
            }
            DragNDropManager.getInstance().removeDragCancelListener(this.m_dragCancelListener);
            this.m_dragCancelListener = null;
            WakfuSoundManager.getInstance().playGUISound(600027L);
            Xulor.getInstance().removeDialogLoadListener(this.m_vaulAccessibilityDialogLoadListener);
            Xulor.getInstance().removeDialogUnloadListener(this.m_vaultAccessibilityDialogUnloadListener);
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
        }
        super.onFrameRemove(frameHandler, isAboutToBeRemoved);
    }
    
    public void ensureInventoryLinkedWindowLayout(final Window w) {
        w.addWindowPostProcessedListener(new WindowPostProcessedListener() {
            @Override
            public void windowPostProcessed() {
                final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap("equipmentDialog");
                if (elementMap == null) {
                    return;
                }
                final Window w2 = (Window)elementMap.getElement("equipInventWindow");
                final float frustrumWidth = Xulor.getInstance().getScene().getFrustumWidth();
                final int equipX = w2.getX();
                int x;
                if (equipX + w2.getWidth() + w.getWidth() < frustrumWidth) {
                    x = (int)MathHelper.clamp(equipX + w2.getWidth() + 20, 0.0f, frustrumWidth - w.getWidth());
                }
                else {
                    x = (int)MathHelper.clamp(equipX - w.getWidth() - 20, 0.0f, frustrumWidth - w.getWidth());
                }
                w.setX(x);
                w.removeWindowPostProcessedListener(this);
            }
        });
    }
    
    private void onChangeCharacterSlotsRefresh() {
        for (final String dialogId : this.m_loadedDialogs) {
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogId);
            final LocalPlayerCharacter value = HeroesManager.INSTANCE.getHero(this.m_characterId);
            if (value != null) {
                for (final String fieldName : LocalPlayerCharacter.UPDATE_EQUIPMENT_PROPERTIES) {
                    PropertiesProvider.getInstance().firePropertyValueChanged("characterSheet", fieldName, map);
                }
            }
            PropertiesProvider.getInstance().firePropertyValueChanged(HeroesManager.INSTANCE.getHero(this.m_characterId), LocalPlayerCharacter.UPDATE_EQUIPMENT_PROPERTIES);
        }
    }
    
    public void refreshPlayerEquimentSlots() {
        final TLongObjectIterator<CharacterView> it = UIEquipmentFrame.m_characterViews.iterator();
        while (it.hasNext()) {
            it.advance();
            for (final String dialogId : this.m_loadedDialogs) {
                final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogId);
                final CharacterView value = it.value();
                for (final String fieldName : LocalPlayerCharacter.UPDATE_EQUIPMENT_PROPERTIES) {
                    PropertiesProvider.getInstance().firePropertyValueChanged("characterSheet", fieldName, map);
                }
                if (((ArrayInventoryWithoutCheck<Item, R>)value.getItemEquipment()).getFromPosition(EquipmentPosition.FIRST_WEAPON.m_id) != null) {
                    if (map == null) {
                        continue;
                    }
                    if (map.containsElement("localPlayerDisplay") && value.getCharacterInfo().getId() == this.m_characterId) {
                        final AnimatedElementViewer aev = (AnimatedElementViewer)map.getElement("localPlayerDisplay");
                        aev.setDirection(Direction8.SOUTH_WEST.getIndex());
                    }
                }
                PropertiesProvider.getInstance().firePropertyValueChanged("itemDetail", "isEquiped", map);
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                PropertiesProvider.getInstance().firePropertyValueChanged(localPlayer, LocalPlayerCharacter.UPDATE_EQUIPMENT_PROPERTIES);
            }
        }
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public static LocalPlayerCharacter getCharacter() {
        return (UIEquipmentFrame.m_instance.m_characterId == 0L) ? WakfuGameEntity.getInstance().getLocalPlayer() : HeroesManager.INSTANCE.getHero(UIEquipmentFrame.m_instance.m_characterId);
    }
    
    public void refreshEquipementSlots() {
        final TIntObjectIterator<CharacterView> it = UIEquipmentFrame.m_companionViews.iterator();
        while (it.hasNext()) {
            it.advance();
            for (final String dialogId : this.m_loadedDialogs) {
                final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogId);
                if (map == null) {
                    continue;
                }
                final CharacterView value = it.value();
                if (!value.isCompanion()) {
                    continue;
                }
                for (final String fieldName : CharacteristicCompanionView.UPDATE_EQUIPMENT_PROPERTIES) {
                    PropertiesProvider.getInstance().firePropertyValueChanged("characterSheet", fieldName, map);
                }
                PropertiesProvider.getInstance().firePropertyValueChanged("itemDetail", "isEquiped", map);
            }
        }
    }
    
    static {
        UIEquipmentFrame.m_instance = new UIEquipmentFrame();
        m_logger = Logger.getLogger((Class)UIEquipmentFrame.class);
    }
}
