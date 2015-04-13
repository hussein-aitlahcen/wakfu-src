package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUDimensionalBagChangeRoomAction extends AbstractMRUAction
{
    @Override
    public MRUActions tag() {
        return MRUActions.DIMENSIONAL_BAG_CHANGE_ROOM_ACTION;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUDimensionalBagChangeRoomAction();
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        if (Xulor.getInstance().isLoaded("dimensionalBagRoomManagerDialog")) {
            WakfuGameEntity.getInstance().removeFrame(UIDimensionalBagRoomManagerFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIDimensionalBagRoomManagerFrame.getInstance());
        }
    }
    
    @Override
    public boolean isRunnable() {
        return false;
    }
    
    @Override
    public String getTranslatorKey() {
        return "bagManage";
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.PADLOCK.m_id;
    }
}
