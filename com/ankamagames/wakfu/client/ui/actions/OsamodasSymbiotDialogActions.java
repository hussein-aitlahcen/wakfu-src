package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.form.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.xulor2.property.*;

@XulorActionsTag
public class OsamodasSymbiotDialogActions
{
    public static final String PACKAGE = "wakfu.osamodasSymbiot";
    private static final Logger m_logger;
    
    public static void selectCreature(final Event event) {
        if (event != null && Events.ITEM_CLICK.equals(event.getType())) {
            final ItemEvent mEvent = (ItemEvent)event;
            if (mEvent.getItemValue() instanceof SymbiotInvocationCharacteristics) {
                final LocalPlayerCharacter lpc = WakfuGameEntity.getInstance().getLocalPlayer();
                final SymbiotInvocationCharacteristics invocationCharacteristics = (SymbiotInvocationCharacteristics)mEvent.getItemValue();
                if (mEvent.getButton() == 1) {
                    final UIMessage message = new UIMessage();
                    message.setId(16814);
                    message.setByteValue(lpc.getSymbiot().getIndexByCreature(invocationCharacteristics));
                    Worker.getInstance().pushMessage(message);
                }
                else if (mEvent.getButton() == 3) {
                    UIOsamodasSymbiotFrame.getInstance().openCloseCreatureDescription(invocationCharacteristics);
                }
                else if (mEvent.getButton() == 507) {
                    final UIMessage message = new UIMessage();
                    message.setId(16815);
                    message.setByteValue(lpc.getSymbiot().getIndexByCreature(invocationCharacteristics));
                    Worker.getInstance().pushMessage(message);
                }
            }
        }
    }
    
    public static void selectNextCreature(final Event event) {
        if (event != null && Events.MOUSE_WHEELED.equals(event.getType())) {
            final MouseEvent mEvent = (MouseEvent)event;
            UIMessage.send((short)16815);
        }
    }
    
    public static boolean validateChangeNameForm(final Form form) {
        form.synchronizeProperties();
        final Property creatureProperty = form.getProperty("osamodasSymbiot.describedCreature");
        if (creatureProperty != null) {
            final String creatureName = creatureProperty.getFieldStringValue("name");
            final String capitalizedCreatureName = NameChecker.doNameCorrection(creatureName);
            if (!capitalizedCreatureName.equals(creatureName)) {
                creatureProperty.setFieldValue("name", capitalizedCreatureName);
            }
            return Actions.checkNameValidity(creatureName);
        }
        return true;
    }
    
    public static void setCreatureName(final Event event, final SymbiotInvocationCharacteristics creature, final TextEditor textEditor) {
        if (event.getType() == Events.MOUSE_CLICKED || (event.getType() == Events.KEY_PRESSED && ((KeyEvent)event).getKeyCode() == 10)) {
            final LocalPlayerCharacter lpc = WakfuGameEntity.getInstance().getLocalPlayer();
            if (textEditor.isValid()) {
                final UIMessage message = new UIMessage();
                message.setStringValue(textEditor.getText());
                message.setByteValue(lpc.getSymbiot().getIndexByCreature(creature));
                message.setId(16817);
                Worker.getInstance().pushMessage(message);
            }
            else {
                OsamodasSymbiotDialogActions.m_logger.warn((Object)"Formulaire invalide");
            }
        }
    }
    
    public static void freeCreature(final Event event, final SymbiotInvocationCharacteristics creature) {
        final LocalPlayerCharacter lpc = WakfuGameEntity.getInstance().getLocalPlayer();
        final UIMessage message = new UIMessage();
        message.setByteValue(lpc.getSymbiot().getIndexByCreature(creature));
        message.setId(16816);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void monsterPopup(final Event e) {
        final SpellLevel spellLevel = WakfuGameEntity.getInstance().getLocalPlayer().getSpellInventory().getFirstWithReferenceId(787);
        if (spellLevel != null) {
            PropertiesProvider.getInstance().setPropertyValue("describedSpell", spellLevel);
        }
    }
    
    public static void closeMonsterPopup(final Event e) {
        PropertiesProvider.getInstance().setPropertyValue("describedSpell", null);
    }
    
    static {
        m_logger = Logger.getLogger((Class)OsamodasSymbiotDialogActions.class);
    }
}
