package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;

public class UIDimensionalBagSaleHistoryFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static UIDimensionalBagSaleHistoryFrame m_instance;
    private DimensionalBagSaleHistoryView m_view;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIDimensionalBagSaleHistoryFrame getInstance() {
        return UIDimensionalBagSaleHistoryFrame.m_instance;
    }
    
    public void setView(final DimensionalBagSaleHistoryView view) {
        this.m_view = view;
        PropertiesProvider.getInstance().setPropertyValue("saleHistory", view);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17010: {
                this.m_view.previousPage();
                return false;
            }
            case 17011: {
                this.m_view.nextPage();
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
                    if (id.equals("dimensionalBagFleaHistoryDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIDimensionalBagSaleHistoryFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("dimensionalBagFleaHistoryDialog", Dialogs.getDialogPath("dimensionalBagFleaHistoryDialog"), 32769L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.dimensionalBagFleaHistory", DimensionalBagFleaHistoryDialogActions.class);
            PropertiesProvider.getInstance().setPropertyValue("saleHistory", this.m_view);
            WakfuSoundManager.getInstance().windowFadeIn();
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().unload("dimensionalBagFleaHistoryDialog");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().removeActionClass("wakfu.dimensionalBagFleaHistory");
            PropertiesProvider.getInstance().removeProperty("saleHistory");
            WakfuSoundManager.getInstance().windowFadeOut();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIDimensionalBagSaleHistoryFrame.class);
        UIDimensionalBagSaleHistoryFrame.m_instance = new UIDimensionalBagSaleHistoryFrame();
    }
}
