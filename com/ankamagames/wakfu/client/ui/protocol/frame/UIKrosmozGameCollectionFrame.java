package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.krosmoz.collection.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.krosmoz.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.krozmoz.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UIKrosmozGameCollectionFrame implements MessageFrame
{
    private static UIKrosmozGameCollectionFrame m_instance;
    protected static Logger m_logger;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIKrosmozGameCollectionFrame getInstance() {
        return UIKrosmozGameCollectionFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17351: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final int figureId = msg.getIntValue();
                final KrosmozFigure krosmozFigure = KrosmozCollectionView.INSTANCE.getFirstWithId(figureId);
                if (krosmozFigure != null) {
                    if (krosmozFigure == KrosmozFigureHelper.INVALID_KROSMOZ_FIGURE) {
                        final String errorMessage = WakfuTranslator.getInstance().getString("error.chat.krosmozFigureBound");
                        final ChatMessage chatMessage = new ChatMessage(errorMessage);
                        chatMessage.setPipeDestination(3);
                        ChatManager.getInstance().pushMessage(chatMessage);
                    }
                    else {
                        final Message deleteMsg = new KrosmozFigureDeleteRequestMessage(krosmozFigure.getGuid());
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(deleteMsg);
                    }
                }
                return false;
            }
            case 17352: {
                KrosmozCollectionView.INSTANCE.previousSeason();
                return false;
            }
            case 17353: {
                KrosmozCollectionView.INSTANCE.nextSeason();
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
                    if (id.equals("krosmozCollectionDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIKrosmozGameCollectionFrame.getInstance());
                    }
                }
            };
            PropertiesProvider.getInstance().setPropertyValue("krosmozCollection", KrosmozCollectionView.INSTANCE);
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("krosmozCollectionDialog", Dialogs.getDialogPath("krosmozCollectionDialog"), 0L, (short)30000);
            Xulor.getInstance().putActionClass("wakfu.krosmozCollection", KrosmozCollectionDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("krosmozCollectionDialog");
            Xulor.getInstance().removeActionClass("wakfu.krosmozCollection");
        }
    }
    
    static {
        UIKrosmozGameCollectionFrame.m_instance = new UIKrosmozGameCollectionFrame();
        UIKrosmozGameCollectionFrame.m_logger = Logger.getLogger((Class)UIKrosmozGameCollectionFrame.class);
    }
}
