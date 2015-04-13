package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.fight.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.*;

public class UIStateDetailFrame extends UIAbstractEffectDetailFrame
{
    private static UIStateDetailFrame m_instance;
    
    public static UIStateDetailFrame getInstance() {
        return UIStateDetailFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16151: {
                final UIStateMessage msg = (UIStateMessage)message;
                final int value = msg.getIntValue();
                this.openDetailDialog(msg.getState(), msg.getStringValue(), (value == 0) ? -1 : value);
                return false;
            }
            default: {
                return super.onMessage(message);
            }
        }
    }
    
    @Override
    protected String getDialogId() {
        return "stateDetailDialog";
    }
    
    @Override
    public void updateEffectForSpell(final int spellId, final int effectId, final short level) {
        final TIntObjectHashMap<EffectFieldProvider> map = this.m_effectClientsCache.get(spellId);
        if (map == null) {
            return;
        }
        final StateClient client = map.get(effectId);
        if (client == null) {
            return;
        }
        client.forceLevel(level);
        PropertiesProvider.getInstance().setLocalPropertyValue("describedState", client, this.createDialogId(client.getStateBaseId()));
        PropertiesProvider.getInstance().setLocalPropertyValue("describedStateLevel", client.getLevel(), this.createDialogId(client.getStateBaseId()));
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            super.onFrameAdd(frameHandler, isAboutToBeAdded);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            super.onFrameRemove(frameHandler, isAboutToBeRemoved);
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
        UIStateDetailFrame.m_instance = new UIStateDetailFrame();
    }
}
