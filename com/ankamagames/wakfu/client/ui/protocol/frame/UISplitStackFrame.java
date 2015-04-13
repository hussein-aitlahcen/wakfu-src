package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UISplitStackFrame implements MessageFrame
{
    private static UISplitStackFrame m_instance;
    
    public static UISplitStackFrame getInstance() {
        return UISplitStackFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16821: {
                if (WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight() != null) {
                    return false;
                }
                if (Xulor.getInstance().isLoaded("splitStackDialog")) {
                    Xulor.getInstance().unload("splitStackDialog");
                }
                final UIItemMessage msg = (UIItemMessage)message;
                short tooltipX = msg.getX();
                short tooltipY = msg.getY();
                tooltipX = (short)Math.max(0.0f, Math.min(tooltipX, Xulor.getInstance().getScene().getFrustumWidth() - 100.0f));
                tooltipY = (short)Math.max(0.0f, Math.min(tooltipY, Xulor.getInstance().getScene().getFrustumHeight() - 50.0f));
                Xulor.getInstance().loadAsTooltip("splitStackDialog", Dialogs.getDialogPath("splitStackDialog"), null, false, tooltipX, tooltipY, 257L, (short)20000);
                PropertiesProvider.getInstance().setPropertyValue("itemToSplit", (short)((msg.getItem() != null) ? msg.getItem().getQuantity() : 0));
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
            Xulor.getInstance().putActionClass("wakfu.split", SplitStackDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeActionClass("wakfu.split");
            Xulor.getInstance().unload("splitStackDialog");
            PropertiesProvider.getInstance().removeProperty("itemToSplit");
        }
    }
    
    static {
        UISplitStackFrame.m_instance = new UISplitStackFrame();
    }
}
