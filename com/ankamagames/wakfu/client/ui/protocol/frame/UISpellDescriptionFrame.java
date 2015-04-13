package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.spells.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.common.game.spell.*;

public class UISpellDescriptionFrame implements MessageFrame
{
    private static UISpellDescriptionFrame m_instance;
    private String m_lastSpellDetailDialogId;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UISpellDescriptionFrame getInstance() {
        return UISpellDescriptionFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16409: {
                final UISpellLevelSelectionMessage msg = (UISpellLevelSelectionMessage)message;
                if (msg.getButton() == 3) {
                    this.openSpellDescription(msg.getSpell(), msg.getStringValue());
                }
                else {
                    final SpellLevel describedSpell = (SpellLevel)PropertiesProvider.getInstance().getObjectProperty("editableDescribedSpell", msg.getStringValue());
                    PropertiesProvider.getInstance().setLocalPropertyValue("editableDescribedSpell", msg.getSpell(), msg.getStringValue());
                    if (describedSpell != null) {
                        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                        final ArrayList<SpellLevel> spellLevels = localPlayer.getSpellInventory().getAllWithReferenceId(describedSpell.getSpell().getId());
                        if (spellLevels.size() != 1) {
                            PropertiesProvider.getInstance().firePropertyValueChanged(((AbstractSpellLevel<FieldProvider>)describedSpell).getSpell(), "id");
                            return false;
                        }
                        PropertiesProvider.getInstance().firePropertyValueChanged(((AbstractSpellLevel<FieldProvider>)spellLevels.get(0)).getSpell(), "id");
                    }
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void openSpellDescription(final SpellLevel spell, final String relativePositionDialogId) {
        final String dialogId = "spellDescriptionDialog" + spell.getReferenceId();
        if (!Xulor.getInstance().isLoaded(dialogId)) {
            Xulor.getInstance().loadAsMultiple(dialogId, Dialogs.getDialogPath("spellDescriptionDialog"), (this.m_lastSpellDetailDialogId == null) ? relativePositionDialogId : this.m_lastSpellDetailDialogId, relativePositionDialogId, "spellDescriptionDialog", 129L, (short)10000);
            this.m_lastSpellDetailDialogId = dialogId;
            PropertiesProvider.getInstance().setLocalPropertyValue("describedSpell", spell, dialogId);
            PropertiesProvider.getInstance().setLocalPropertyValue("editableDescribedSpell", spell.getCopy(false, true), dialogId);
        }
        else {
            Xulor.getInstance().unload(dialogId);
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals(UISpellDescriptionFrame.this.m_lastSpellDetailDialogId)) {
                        UISpellDescriptionFrame.this.m_lastSpellDetailDialogId = null;
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().putActionClass("wakfu.spellDetails", SpellDetailsActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().removeActionClass("wakfu.spellDetails");
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        UISpellDescriptionFrame.m_instance = new UISpellDescriptionFrame();
    }
}
