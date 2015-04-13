package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UINationPvpLadderFrame implements MessageFrame
{
    public static final UINationPvpLadderFrame INSTANCE;
    protected static final Logger m_logger;
    private DialogUnloadListener m_dialogUnloadListener;
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19425: {
                PvpLadderPageView.INSTANCE.first();
                return false;
            }
            case 19426: {
                PvpLadderPageView.INSTANCE.previous();
                return false;
            }
            case 19427: {
                PvpLadderPageView.INSTANCE.next();
                return false;
            }
            case 19428: {
                PvpLadderPageView.INSTANCE.last();
                return false;
            }
            case 19432: {
                final UIMessage msg = (UIMessage)message;
                final PvpLadderPageView.FilterType filter = msg.getObjectValue();
                PvpLadderPageView.INSTANCE.setCurrentFilter(filter);
                return false;
            }
            case 19430: {
                final UIMessage msg = (UIMessage)message;
                final PvpLadderPageView.BreedInfo breed = msg.getObjectValue();
                PvpLadderPageView.INSTANCE.setCurrentBreed(breed);
                return false;
            }
            case 19431: {
                final UIMessage msg = (UIMessage)message;
                final NationFieldProvider nation = msg.getObjectValue();
                PvpLadderPageView.INSTANCE.setCurrentNation(nation);
                return false;
            }
            case 19433: {
                final UIMessage msg = (UIMessage)message;
                final PvpLadderEntryView entry = msg.getObjectValue();
                this.loadPvpLadderEntry(entry);
                return false;
            }
            case 19434: {
                final AbstractUIMessage msg2 = (AbstractUIMessage)message;
                final String name = msg2.getStringValue();
                PvpLadderPageView.INSTANCE.searchByName(name);
                return false;
            }
            case 19435: {
                PvpLadderPageView.INSTANCE.reset();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void loadPvpLadderEntry(final PvpLadderEntryView view) {
        view.requestPvpLadderEntryUpdate();
        final String dialogId = "pvpLadderEntryDialog" + view.getEntry().getId();
        final EventDispatcher e = Xulor.getInstance().load(dialogId, Dialogs.getDialogPath("pvpLadderEntryDialog"), 0L, (short)10000);
        PropertiesProvider.getInstance().setLocalPropertyValue("pvpLadderEntry", view, e.getElementMap());
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("nationPvpLadderDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UINationPvpLadderFrame.INSTANCE);
                    }
                }
            };
            PropertiesProvider.getInstance().setPropertyValue("nationPvpLadder", PvpLadderPageView.INSTANCE);
            PvpLadderPageView.INSTANCE.reset();
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("nationPvpLadderDialog", Dialogs.getDialogPath("nationPvpLadderDialog"), 32768L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.nationPvpLadder", NationPvpLadderDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("nationPvpLadderDialog");
            Xulor.getInstance().removeActionClass("wakfu.nationPvpLadder");
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
        INSTANCE = new UINationPvpLadderFrame();
        m_logger = Logger.getLogger((Class)UINationPvpLadderFrame.class);
    }
}
