package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.almanach.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.core.*;

public class UIAlmanachFrame implements MessageFrame
{
    private static final UIAlmanachFrame INSTANCE;
    protected static final Logger m_logger;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIAlmanachFrame getInstance() {
        return UIAlmanachFrame.INSTANCE;
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
                    if (id.equals("almanachDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIAlmanachFrame.getInstance());
                    }
                }
            };
            PropertiesProvider.getInstance().setPropertyValue("almanach", AlmanachView.INSTANCE);
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("almanachDialog", Dialogs.getDialogPath("almanachDialog"), 32768L, (short)10000);
            AlmanachView.INSTANCE.setDisplayedDate(WakfuGameCalendar.getInstance().getDate());
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("almanachDialog");
            final CalendarWidget calendar = (CalendarWidget)map.getElement("calendar");
            calendar.setCalendar(WakfuGameCalendar.getInstance());
            Xulor.getInstance().putActionClass("wakfu.almanach", AlmanachDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("almanachDialog");
            Xulor.getInstance().removeActionClass("wakfu.almanach");
            PropertiesProvider.getInstance().removeProperty("almanach");
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
        INSTANCE = new UIAlmanachFrame();
        m_logger = Logger.getLogger((Class)UIAlmanachFrame.class);
    }
}
