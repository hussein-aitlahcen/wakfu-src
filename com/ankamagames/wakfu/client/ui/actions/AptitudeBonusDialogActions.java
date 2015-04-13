package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.aptitudenew.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.aptitude.*;
import com.ankamagames.wakfu.client.ui.protocol.message.aptitude.*;
import com.ankamagames.framework.java.util.*;

@XulorActionsTag
public class AptitudeBonusDialogActions
{
    public static final String PACKAGE = "wakfu.aptitudeBonus";
    
    public static void overAptitude(final ItemEvent e) {
        overAptitude(e, (FieldProvider)e.getItemValue());
    }
    
    public static void overAptitude(final Event e, final FieldProvider aptitude) {
        PropertiesProvider.getInstance().setPropertyValue("describedAptitude", aptitude);
    }
    
    public static void increaseBonus(final Event e, final AptitudeBonusView bonus, final Container buttonContainer, final Button validateButton) {
        launchAptitudeModification(e, bonus, buttonContainer, validateButton, true);
    }
    
    public static void decreaseBonus(final Event e, final AptitudeBonusView bonus, final Container buttonContainer, final Button validateButton) {
        launchAptitudeModification(e, bonus, buttonContainer, validateButton, false);
    }
    
    public static void selectCategory(final Event e, final AptitudeBonusCategoryView category) {
        final AbstractUIMessage message = new UIMessage((short)17693);
        message.setObjectValue(category);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void validateCommonAptitudePoints(final Event e, final Container buttonContainer, final Button validateButton) {
        final UIAptitudeCommonMessage msg = new UIAptitudeCommonMessage();
        msg.setId(17694);
        msg.setButtonContainer(buttonContainer);
        msg.setValidateButton(validateButton);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void cancelCommonAptitudePoints(final Event e, final Container buttonContainer, final Button validateButton) {
        final UIAptitudeCommonMessage msg = new UIAptitudeCommonMessage();
        msg.setId(17695);
        msg.setButtonContainer(buttonContainer);
        msg.setValidateButton(validateButton);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void selectAptitude(final Event e, final Aptitude aptitude) {
        if (e instanceof MouseEvent && ((MouseEvent)e).getButton() == 3) {
            final int linkedSpellId = aptitude.getReferenceAptitude().getLinkedSpellId();
            if (linkedSpellId == 0) {
                return;
            }
            SpellLevel spellLevel = WakfuGameEntity.getInstance().getLocalPlayer().getSpellInventory().getFirstWithReferenceId(linkedSpellId);
            if (spellLevel == null) {
                final Spell spell = SpellManager.getInstance().getSpell(linkedSpellId);
                if (spell != null) {
                    spellLevel = new SpellLevel(spell, (short)0, -1L);
                }
            }
            if (spellLevel != null) {
                CompanionsEmbeddedActions.openSpellDescription(3, spellLevel, "characterSheetAptitudesDialog");
            }
        }
    }
    
    public static void validateSpellAptitudePoints(final Event e, final Container buttonContainer, final Button validateButton) {
        final UIAptitudeCommonMessage msg = new UIAptitudeCommonMessage();
        msg.setId(17688);
        msg.setButtonContainer(buttonContainer);
        msg.setValidateButton(validateButton);
        msg.setByteValue(AptitudeType.SPELL.getId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void cancelSpellAptitudePoints(final Event e, final Container buttonContainer, final Button validateButton) {
        final UIAptitudeCommonMessage msg = new UIAptitudeCommonMessage();
        msg.setId(17689);
        msg.setButtonContainer(buttonContainer);
        msg.setValidateButton(validateButton);
        msg.setByteValue(AptitudeType.SPELL.getId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void openCloseAptitudes(final Event e) {
        UIMessage.send((short)17690);
    }
    
    public static void improveAptitude(final Event e, final Aptitude aptitude, final Container buttonContainer, final Button validateButton) {
        launchAptitudeModification(e, aptitude, buttonContainer, validateButton, true);
    }
    
    public static void decreaseAptitude(final Event e, final Aptitude aptitude, final Container buttonContainer, final Button validateButton) {
        launchAptitudeModification(e, aptitude, buttonContainer, validateButton, false);
    }
    
    public static void launchAptitudeModification(final Event e, final Aptitude aptitude, final Container buttonContainer, final Button validateButton, final boolean up) {
        if (ModificationRunnable.INSTANCE.isRunning()) {
            return;
        }
        final UIAptitudeMessage msg = new UIAptitudeMessage();
        msg.setAptitude(aptitude);
        msg.setButtonContainer(buttonContainer);
        msg.setValidateButton(validateButton);
        msg.setId(up ? 17686 : 17687);
        ModificationRunnable.INSTANCE.initAndStart(msg, e.getTarget());
    }
    
    private static void launchAptitudeModification(final Event e, final AptitudeBonusView aptitude, final Container buttonContainer, final Button validateButton, final boolean up) {
        if (ModificationRunnable.INSTANCE.isRunning()) {
            return;
        }
        final UIAptitudeBonusMessage msg = new UIAptitudeBonusMessage(aptitude);
        msg.setButtonContainer(buttonContainer);
        msg.setValidateButton(validateButton);
        msg.setId(up ? 17691 : 17692);
        ModificationRunnable.INSTANCE.initAndStart(msg, e.getTarget());
    }
    
    public static void displayPage(final Event e, final String page) {
        PropertiesProvider.getInstance().setLocalPropertyValue("currentPage", PrimitiveConverter.getInteger(page), "aptitudeBonusDialog");
    }
}
