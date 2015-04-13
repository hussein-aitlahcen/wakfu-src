package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.tutorial.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UITutorialBookFrame implements MessageFrame
{
    public static final UITutorialBookFrame INSTANCE;
    protected static final Logger m_logger;
    private TutorialBook m_tutorialBook;
    private DialogUnloadListener m_dialogUnloadListener;
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19370: {
                final UIMessage msg = (UIMessage)message;
                final String search = msg.getStringValue();
                this.m_tutorialBook.setSearch(search);
                return false;
            }
            case 19371: {
                final UIMessage msg = (UIMessage)message;
                this.m_tutorialBook.setPreviousPage();
                return false;
            }
            case 19372: {
                final UIMessage msg = (UIMessage)message;
                this.m_tutorialBook.setNextPage();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("tutorialBookDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UITutorialBookFrame.INSTANCE);
                    }
                }
            };
            this.m_tutorialBook = new TutorialBook();
            PropertiesProvider.getInstance().setPropertyValue("tutorialBook", this.m_tutorialBook);
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("tutorialBookDialog", Dialogs.getDialogPath("tutorialBookDialog"), 32768L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.tutorialBook", TutorialBookDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_tutorialBook = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("tutorialBookDialog");
            Xulor.getInstance().removeActionClass("wakfu.tutorialBook");
            PropertiesProvider.getInstance().removeProperty("tutorialBook");
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
        INSTANCE = new UITutorialBookFrame();
        m_logger = Logger.getLogger((Class)UITutorialBookFrame.class);
    }
}
