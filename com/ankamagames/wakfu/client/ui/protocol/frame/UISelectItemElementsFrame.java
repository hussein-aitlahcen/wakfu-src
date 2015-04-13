package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.*;

public class UISelectItemElementsFrame implements MessageFrame
{
    private static final UISelectItemElementsFrame INSTANCE;
    protected static final Logger m_logger;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UISelectItemElementsFrame getInstance() {
        return UISelectItemElementsFrame.INSTANCE;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        return true;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("selectItemElementsDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UISelectItemElementsFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("selectItemElementsDialog", Dialogs.getDialogPath("selectItemElementsDialog"), 256L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.selectItemElements", SelectItemElementsDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("selectItemElementsDialog");
            Xulor.getInstance().removeActionClass("wakfu.selectItemElements");
            PropertiesProvider.getInstance().removeProperty("selectItemElements");
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void setConcernedItem(final Item concernedItem) {
        SelectItemElementsView.INSTANCE.setConcernedItem(concernedItem);
        PropertiesProvider.getInstance().setPropertyValue("selectItemElements", SelectItemElementsView.INSTANCE);
    }
    
    static {
        INSTANCE = new UISelectItemElementsFrame();
        m_logger = Logger.getLogger((Class)UISelectItemElementsFrame.class);
    }
}
