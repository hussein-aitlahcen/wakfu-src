package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UIJailInfoFrame implements MessageFrame
{
    private static UIJailInfoFrame m_instance;
    private static final Logger m_logger;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIJailInfoFrame getInstance() {
        return UIJailInfoFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        message.getId();
        return true;
    }
    
    public void highlightTimer(final int duration) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("jailInfoDialog");
        if (map == null) {
            return;
        }
        final Image image = (Image)map.getElement("timerImage");
        if (image == null) {
            return;
        }
        final int tweenDuration = 250;
        int repeat = duration / 250;
        if (repeat % 2 != 0) {
            ++repeat;
        }
        final DecoratorAppearance app = image.getAppearance();
        app.addTween(new ModulationColorTween(Color.WHITE, Color.GRAY, app, 0, 250, repeat, true, TweenFunction.PROGRESSIVE));
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
                    if (id.equals("jailInfoDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIJailInfoFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("jailInfoDialog", Dialogs.getDialogPath("jailInfoDialog"), 8193L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.jailInfo", JailInfoDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeActionClass("wakfu.jailInfo");
            Xulor.getInstance().unload("jailInfoDialog");
        }
    }
    
    static {
        UIJailInfoFrame.m_instance = new UIJailInfoFrame();
        m_logger = Logger.getLogger((Class)UIJailInfoFrame.class);
    }
}
