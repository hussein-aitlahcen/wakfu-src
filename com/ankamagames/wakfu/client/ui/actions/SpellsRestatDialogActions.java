package com.ankamagames.wakfu.client.ui.actions;

import org.apache.log4j.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.ui.protocol.message.spells.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

@XulorActionsTag
public class SpellsRestatDialogActions
{
    public static final String PACKAGE = "wakfu.spellsRestat";
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
    
    public static void openSpellDescription(final ItemEvent itemEvent) {
        final int button = itemEvent.getButton();
        if (button == 3) {
            return;
        }
        SpellLevel spellLevel = (SpellLevel)itemEvent.getItemValue();
        if (spellLevel instanceof RestatSpellLevel) {
            spellLevel = ((RestatSpellLevel)spellLevel).getRealSpellLevel();
        }
        final SpellLevel currentDescribedSpellLevel = (SpellLevel)PropertiesProvider.getInstance().getObjectProperty("describedSpell", "spellsRestatDialog");
        if (currentDescribedSpellLevel != null && currentDescribedSpellLevel.getSpellId() == spellLevel.getSpellId()) {
            return;
        }
        final UISpellLevelSelectionMessage msg = new UISpellLevelSelectionMessage();
        msg.setSpell(spellLevel.getCopy(false, true));
        msg.setButton(button);
        msg.setStringValue("spellsRestatDialog");
        msg.setId(16409);
        Worker.getInstance().pushMessage(msg);
        PropertiesProvider.getInstance().setLocalPropertyValue("describedSpell", spellLevel, "spellsRestatDialog");
    }
    
    private static void setSelectedModulationColor(final Widget w) {
        w.getAppearance().setModulationColor(new Color(0.4f, 0.5f, 0.62f, 1.0f));
    }
    
    public static void onMouseOverSpell(final Event event, final Container container) {
        setSelectedModulationColor(container);
    }
    
    public static void onMouseOutSpell(final Event event, final Container container, final Object spell) {
        final String mapId = container.getRenderableParent().getElementMap().getId();
        final SpellLevel selectedSpellLevel = (SpellLevel)PropertiesProvider.getInstance().getObjectProperty("describedSpell", mapId);
        if (spell instanceof Spell && selectedSpellLevel != null && ((Spell)spell).getId() == selectedSpellLevel.getReferenceId()) {
            return;
        }
        if (spell instanceof SpellLevel && selectedSpellLevel != null && ((SpellLevel)spell).getReferenceId() == selectedSpellLevel.getReferenceId()) {
            return;
        }
        container.getAppearance().setModulationColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
    }
    
    public static void improveSpell(final Event e, final RestatSpellLevel restatSpellLevel) {
        launchSpellLevelModification(e, restatSpellLevel, true);
    }
    
    public static void decreaseSpell(final Event e, final RestatSpellLevel restatSpellLevel) {
        launchSpellLevelModification(e, restatSpellLevel, false);
    }
    
    public static void launchSpellLevelModification(final Event e, final RestatSpellLevel restatSpellLevel, final boolean up) {
        if (ModificationRunnable.INSTANCE.isRunning()) {
            return;
        }
        final UIRestatSpellModificationMessage msg = new UIRestatSpellModificationMessage();
        msg.setRestatSpellLevel(restatSpellLevel);
        msg.setId(up ? 19351 : 19352);
        ModificationRunnable.INSTANCE.initAndStart(msg, e.getTarget());
    }
    
    public static void valid(final Event e) {
        UIMessage.send((short)19353);
    }
    
    public static void reset(final Event e) {
        UIMessage.send((short)19354);
    }
    
    static {
        SpellsRestatDialogActions.m_logger = Logger.getLogger((Class)SpellsRestatDialogActions.class);
    }
}
