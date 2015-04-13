package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.dungeon.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UIDungeonFrame implements MessageFrame
{
    private static final UIDungeonFrame INSTANCE;
    protected static final Logger m_logger;
    private DungeonListView m_dungeonList;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIDungeonFrame getInstance() {
        return UIDungeonFrame.INSTANCE;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16540: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                this.m_dungeonList.setCurrentDungeon(msg.getIntValue());
                return false;
            }
            case 16541: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                this.m_dungeonList.setCurrentFilter(msg.getByteValue());
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
                    if (id.equals("dungeonDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIDungeonFrame.getInstance());
                    }
                }
            };
            this.m_dungeonList = new DungeonListView();
            PropertiesProvider.getInstance().setPropertyValue("dungeonList", this.m_dungeonList);
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("dungeonDialog", Dialogs.getDialogPath("dungeonDialog"), 32768L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.dungeon", DungeonDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("dungeonDialog");
            Xulor.getInstance().removeActionClass("wakfu.dungeon");
            PropertiesProvider.getInstance().removeProperty("dungeonList");
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
        INSTANCE = new UIDungeonFrame();
        m_logger = Logger.getLogger((Class)UIDungeonFrame.class);
    }
}
