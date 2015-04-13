package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;

public class UIAdminCharacterEditorFrame implements MessageFrame
{
    private static final UIAdminCharacterEditorFrame m_instance;
    private static final Logger m_logger;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIAdminCharacterEditorFrame getInstance() {
        return UIAdminCharacterEditorFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17720: {
                WakfuGameEntity.getInstance().removeFrame(this);
                return false;
            }
            default: {
                return true;
            }
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
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if ((id.equals("adminCharacterEditorDialog") && !Xulor.getInstance().isLoaded("adminCharacterColorEditorDialog")) || (id.equals("adminCharacterColorEditorDialog") && !Xulor.getInstance().isLoaded("adminCharacterEditorDialog"))) {
                        WakfuGameEntity.getInstance().removeFrame(UIAdminCharacterEditorFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("spellsList", null);
            PropertiesProvider.getInstance().setPropertyValue("craftSkillsList", null);
            updateSpellsList();
            updateSkillsList();
            Xulor.getInstance().load("adminCharacterEditorDialog", Dialogs.getDialogPath("adminCharacterEditorDialog"), 1L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.adminCharacterEditor", AdminCharacterEditorDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            PropertiesProvider.getInstance().removeProperty("spellsList");
            PropertiesProvider.getInstance().removeProperty("craftSkillsList");
            Xulor.getInstance().unload("adminCharacterEditorDialog");
            Xulor.getInstance().unload("adminCharacterColorEditorDialog");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().removeActionClass("wakfu.adminCharacterEditor");
        }
    }
    
    public static void updateSpellsList() {
        final ArrayList<SpellLevel> spellsList = new ArrayList<SpellLevel>();
        for (final SpellLevel spellLevel : WakfuGameEntity.getInstance().getLocalPlayer().getSpellInventory()) {
            spellsList.add(spellLevel);
        }
        PropertiesProvider.getInstance().setPropertyValue("spellsList", spellsList);
    }
    
    public static void updateSkillsList() {
        final ArrayList<CraftView> craftToLearn = new ArrayList<CraftView>();
        CraftManager.INSTANCE.foreachCraft(new TObjectProcedure<ReferenceCraft>() {
            @Override
            public boolean execute(final ReferenceCraft object) {
                final int refId = object.getId();
                if (CraftManager.INSTANCE.getCraft(refId).isConceptualCraft()) {
                    return true;
                }
                if (WakfuGameEntity.getInstance().getLocalPlayer().getCraftHandler().contains(refId)) {
                    return true;
                }
                craftToLearn.add(new CraftView(refId));
                return true;
            }
        });
        PropertiesProvider.getInstance().setPropertyValue("craftSkillsList", craftToLearn);
    }
    
    static {
        m_instance = new UIAdminCharacterEditorFrame();
        m_logger = Logger.getLogger((Class)UIAdminCharacterEditorFrame.class);
    }
}
