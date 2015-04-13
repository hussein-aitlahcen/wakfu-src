package com.ankamagames.wakfu.client.ui.actions;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.shortcut.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.client.ui.protocol.message.spells.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class SpellsPageDialogActions extends CompanionsEmbeddedActions
{
    public static final String PACKAGE = "wakfu.characterBook.spellsPage";
    protected static Logger m_logger;
    
    public static void showSpellDetailPopup(final ItemEvent itemEvent, final Container container) {
        SpellLevel spellLevel = null;
        if (itemEvent.getItemValue() instanceof SpellLevel) {
            spellLevel = (SpellLevel)itemEvent.getItemValue();
        }
        else if (itemEvent.getItemValue() instanceof Spell) {
            spellLevel = new SpellLevel((Spell)itemEvent.getItemValue(), (short)0, -1L);
        }
        if (spellLevel != null) {
            PropertiesProvider.getInstance().setPropertyValue("describedSpell", spellLevel);
            final PopupElement popup = (PopupElement)container.getElementMap().getElement("spellDetailPopup");
            if (itemEvent.getType() == Events.ITEM_OVER && !MasterRootContainer.getInstance().isDragging()) {
                XulorActions.popup(itemEvent, popup);
            }
            else if (itemEvent.getType() == Events.ITEM_OUT) {
                XulorActions.closePopup(itemEvent, popup);
            }
        }
    }
    
    public static void addToShortcuts(final ItemEvent event) {
        if (event.getType() == Events.ITEM_CLICK && event.getButton() == 1 && event.hasAlt()) {
            final SpellLevel spellLevel = (SpellLevel)event.getItemValue();
            if (spellLevel.getSpell().isPassive()) {
                return;
            }
            final UIShortcutMessage message = new UIShortcutMessage();
            message.setItem(spellLevel);
            message.setShorcutBarNumber(-1);
            message.setPosition(-1);
            message.setId(16700);
            message.setBooleanValue(true);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void closeDialog(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UISpellsPageFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UISpellsPageFrame.getInstance());
        }
    }
    
    public static void dropOutLockedSpell(final DropOutEvent dropOutEvent) {
        if (!SpellXpComputer.canPlayerUnlockSpell(WakfuGameEntity.getInstance().getLocalPlayer())) {
            final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("error.spellCantBeUnlocked"));
            chatMessage.setPipeDestination(3);
            ChatManager.getInstance().pushMessage(chatMessage);
            return;
        }
        UIMessage.send((short)16435);
    }
    
    public static void dropLockedSpell(final DropEvent dropEvent) {
        final SpellLevel spellLevel = (SpellLevel)dropEvent.getValue();
        final SpellLockValidity playerSpellLockValidity = SpellXpComputer.getPlayerSpellLockValidity(WakfuGameEntity.getInstance().getLocalPlayer(), spellLevel.getSpell().getId());
        if (playerSpellLockValidity != SpellLockValidity.VALID) {
            final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("error.spellCantBeLocked"));
            chatMessage.setPipeDestination(3);
            ChatManager.getInstance().pushMessage(chatMessage);
            return;
        }
        final UISpellLevelMessage uiSpellLevelMessage = new UISpellLevelMessage();
        uiSpellLevelMessage.setSpell(spellLevel);
        uiSpellLevelMessage.setId(16436);
        Worker.getInstance().pushMessage(uiSpellLevelMessage);
    }
    
    public static void dropView(final DropOutEvent e) {
        final ShortCharacterView shortCharacterView = (ShortCharacterView)e.getSourceValue();
        if (shortCharacterView.isPlayer()) {
            return;
        }
        final int screenX = e.getScreenX();
        final int screenY = e.getScreenY();
        final String dialogId = UISpellsPageFrame.getInstance().loadSecondaryDialog(UICompanionsEmbeddedFrame.getCharacterSheetView(shortCharacterView.getBreedId()), "characterBookSpellPageContainer", screenX, screenY);
        if (dialogId == null) {
            return;
        }
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setId(19270);
        uiMessage.setStringValue(dialogId);
        uiMessage.setIntValue(shortCharacterView.getBreedId());
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    static {
        SpellsPageDialogActions.m_logger = Logger.getLogger((Class)SpellsPageDialogActions.class);
    }
}
