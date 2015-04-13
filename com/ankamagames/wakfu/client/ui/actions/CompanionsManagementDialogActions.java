package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.companion.*;

@XulorActionsTag
public class CompanionsManagementDialogActions extends CompanionsEmbeddedActions
{
    public static final String PACKAGE = "wakfu.companionsManagement";
    
    public static void selectCompanion(final ItemEvent e) {
        PropertiesProvider.getInstance().setLocalPropertyValue("characterSheet", e.getItemValue(), "companionsManagementDialog");
    }
    
    public static void selectAlphabeticalSorter(final SelectionChangedEvent e) {
        UICompanionsManagementFrame.INSTANCE.getCompanionsSavedSearch().setAlphabeticalSorted(e.isSelected());
        UICompanionsManagementFrame.INSTANCE.reflowCompanionsList();
    }
    
    public static void selectLevelSorter(final SelectionChangedEvent e) {
        UICompanionsManagementFrame.INSTANCE.getCompanionsSavedSearch().setLevelSorted(e.isSelected());
        UICompanionsManagementFrame.INSTANCE.reflowCompanionsList();
    }
    
    public static void selectFilter(final SelectionChangedEvent e) {
        if (!e.isSelected()) {
            return;
        }
        final RadioButton r = e.getTarget();
        final int filter = Integer.parseInt(r.getValue());
        final CompanionsSavedSearch.CompanionFilterType fromId = CompanionsSavedSearch.CompanionFilterType.getFromId(filter);
        UICompanionsManagementFrame.INSTANCE.getCompanionsSavedSearch().setCompanionFilterType(fromId);
        UICompanionsManagementFrame.INSTANCE.reflowCompanionsList();
    }
    
    public static void setPage(final SelectionChangedEvent e) {
        if (!e.isSelected()) {
            return;
        }
        final RadioButton r = e.getTarget();
        final byte page = Byte.parseByte(r.getValue());
        PropertiesProvider.getInstance().setPropertyValue("companionManagementPage", page);
    }
    
    public static void removeCompanion(final Event e, final CharacteristicCompanionView characteristicCompanionView) {
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.unlinkCompanion", 70.0f), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    final ItemizeCompanionRequestMessage itemizeCompanionRequestMessage = new ItemizeCompanionRequestMessage(characteristicCompanionView.getCompanionId());
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(itemizeCompanionRequestMessage);
                }
            }
        });
    }
    
    public static void openCompanionInventory(final Event e, final CharacteristicCompanionView characteristicCompanionView) {
        if (!WakfuGameEntity.getInstance().hasFrame(UIEquipmentFrame.getInstance())) {
            Xulor.getInstance().addDialogLoadListener(new DialogLoadListener() {
                @Override
                public void dialogLoaded(final String id) {
                    if (!"equipmentDialog".equals(id)) {
                        return;
                    }
                    loadInventory(characteristicCompanionView);
                    Xulor.getInstance().removeDialogLoadListener(this);
                }
            });
            WakfuGameEntity.getInstance().pushFrame(UIEquipmentFrame.getInstance());
        }
        else {
            loadInventory(characteristicCompanionView);
        }
    }
    
    private static void loadInventory(final CharacteristicCompanionView characteristicCompanionView) {
        if (!characteristicCompanionView.isCompanion()) {
            return;
        }
        final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap("equipmentDialog");
        final String windowId = "equipInventWindow";
        final Window equipWindow = (Window)elementMap.getElement("equipInventWindow");
        equipWindow.addWindowPostProcessedListener(new WindowPostProcessedListener() {
            @Override
            public void windowPostProcessed() {
                final CharacterView characterSheetView = UICompanionsEmbeddedFrame.getCharacterSheetView(characteristicCompanionView.getCharacterInfo().getBreedId());
                final String mapId = UIEquipmentFrame.getInstance().loadSecondaryDialog((characterSheetView == null) ? characteristicCompanionView : characterSheetView, "equipInventWindow", equipWindow.getX() + equipWindow.getWidth(), equipWindow.getY());
                final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap(mapId);
                final Window w = (Window)elementMap.getElement("equipInventWindow");
                w.addWindowPostProcessedListener(new WindowPostProcessedListener() {
                    @Override
                    public void windowPostProcessed() {
                        final int wX = equipWindow.getX() + equipWindow.getWidth();
                        if (wX + w.getWidth() <= Xulor.getInstance().getScene().getFrustumWidth()) {
                            w.setPosition(wX, equipWindow.getY());
                        }
                        else {
                            w.setPosition(equipWindow.getX(), equipWindow.getY());
                        }
                        w.removeWindowPostProcessedListener(this);
                    }
                });
                equipWindow.removeWindowPostProcessedListener(this);
            }
        });
        equipWindow.setNeedsToPostProcess();
    }
    
    public static void validRenameCompanion(final Event e, final TextEditor texteditor) {
        if (e.getType() == Events.KEY_PRESSED && ((KeyEvent)e).getKeyCode() != 10) {
            return;
        }
        final String value = texteditor.getText();
        final CharacteristicCompanionView characteristicCompanionView = (CharacteristicCompanionView)PropertiesProvider.getInstance().getObjectProperty("characterSheet", "companionsManagementDialog");
        if (value != null && value.length() > 0 && !value.equals(characteristicCompanionView.getName())) {
            if (!WordsModerator.getInstance().validateName(value)) {
                ChatHelper.pushErrorMessage("error.connection.nicknameInvalidContent", new Object[0]);
            }
            else {
                final RenameCompanionRequestMessage msg = new RenameCompanionRequestMessage(characteristicCompanionView.getCompanionId(), value);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
            }
        }
    }
    
    public static void openSpellPopup(final ItemEvent e, final Window w) {
        final SpellLevel spell = (SpellLevel)e.getItemValue();
        PropertiesProvider.getInstance().setPropertyValue("describedSpell", spell);
        final PopupElement popup = (PopupElement)w.getElementMap().getElement("spellDetailPopup");
        if (e.getType() == Events.ITEM_OVER && !MasterRootContainer.getInstance().isDragging()) {
            XulorActions.popup(popup, w);
        }
        else if (e.getType() == Events.ITEM_OUT) {
            XulorActions.closePopup(e, popup);
        }
    }
    
    public static void buyCompanion(final Event e, final CharacteristicCompanionView view) {
        final Article article = view.getShopArticle();
        UIWebShopFrame.getInstance().openArticleDialog(article);
    }
    
    public static void displayPage(final Event e, final String value) {
        final Byte pageIndex = Byte.valueOf(value);
        final int currentPageIndex = PropertiesProvider.getInstance().getIntProperty("companionCurrentPageIndex");
        if (pageIndex == currentPageIndex) {
            return;
        }
        PropertiesProvider.getInstance().setPropertyValue("companionCurrentPageIndex", pageIndex);
    }
    
    public static void addCompanionToParty(final Event e, final CharacteristicCompanionView characteristicCompanionView) {
        final long companionId = characteristicCompanionView.getCompanionId();
        final Message msg = new AddCompanionToGroupRequestMessage(companionId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventCompanionJointGroup());
    }
    
    public static void addCharacterToParty(final MouseEvent e, final CharacterView characterView, final AnimatedElementViewer animatedElementViewer) {
        final ToggleButton tb = e.getCurrentTarget();
        final boolean add = tb.getSelected();
        if (add) {
            if (characterView.isCompanion()) {
                addCompanionToParty(e, (CharacteristicCompanionView)characterView);
            }
            else {
                addHeroToParty(characterView);
            }
        }
        else {
            final PartyComportment partyComportment = WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment();
            if (!partyComportment.isInParty()) {
                return;
            }
            long characterId = characterView.getCharacterInfo().getId();
            if (characterView.isCompanion()) {
                characterId = -((CharacteristicCompanionView)characterView).getCompanionId();
            }
            final UIMessage msg = new UIMessage();
            msg.setId(19030);
            msg.setLongValue(characterId);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void addHeroToParty(final CharacterView characterView) {
        final long characterId = characterView.getCharacterInfo().getId();
        final Message msg = new AddHeroToGroupRequestMessage(characterId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    public static void mouseOverHeroList(final ItemEvent e) {
        PropertiesProvider.getInstance().setPropertyValue("overHero", e.getItemValue());
    }
    
    public static void mouseOutHeroList(final ItemEvent e) {
        PropertiesProvider.getInstance().setPropertyValue("overHero", null);
    }
}
