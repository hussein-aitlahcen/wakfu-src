package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class MRUScriptAction extends AbstractMRUAction
{
    private int m_eventId;
    private int m_gfxId;
    
    @Override
    public MRUActions tag() {
        return MRUActions.SCRIPT_ACTION;
    }
    
    @Override
    public void run() {
        final TriggerServerEvent netMessage = new TriggerServerEvent();
        netMessage.setEventId(this.m_eventId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
    }
    
    @Override
    public boolean isRunnable() {
        return true;
    }
    
    @Override
    public String getTranslatorKey() {
        return "";
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUScriptAction();
    }
    
    public void setEventId(final int eventId) {
        this.m_eventId = eventId;
    }
    
    @Override
    protected int getGFXId() {
        return this.m_gfxId;
    }
    
    public void setGfxId(final int gfxId) {
        this.m_gfxId = gfxId;
    }
}
