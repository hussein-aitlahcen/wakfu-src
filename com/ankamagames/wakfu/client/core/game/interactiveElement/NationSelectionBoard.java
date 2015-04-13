package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class NationSelectionBoard extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        if (action == InteractiveElementAction.READ) {
            WakfuGameEntity.getInstance().pushFrame(new Frame(this));
        }
        this.sendActionMessage(action);
        return true;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.READ;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.READ };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final MRUGenericInteractiveAction read = MRUActions.GENERIC_INTERACTIVE_ACTION.getMRUAction();
        read.setGfxId(MRUGfxConstants.BOOK.m_id);
        read.setName("nationSelectionBoard");
        read.setActionToExecute(InteractiveElementAction.READ);
        final AbstractMRUAction[] mru = { read };
        return mru;
    }
    
    @Override
    public String getName() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(WakfuTranslator.getInstance().getString("nationSelectionBoard"));
        return sb.finishAndToString();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setOverHeadable(true);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationSelectionBoard.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static final MonitoredPool POOL;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            NationSelectionBoard ie;
            try {
                ie = (NationSelectionBoard)Factory.POOL.borrowObject();
                ie.setPool(Factory.POOL);
            }
            catch (Exception e) {
                NationSelectionBoard.m_logger.error((Object)"Erreur lors de l'extraction d'une NationSelectionBoard du pool", (Throwable)e);
                ie = new NationSelectionBoard();
            }
            return ie;
        }
        
        static {
            POOL = new MonitoredPool(new ObjectFactory<NationSelectionBoard>() {
                @Override
                public NationSelectionBoard makeObject() {
                    return new NationSelectionBoard();
                }
            });
        }
    }
    
    private static class Frame implements MessageFrame
    {
        private final NationSelectionBoard m_board;
        
        Frame(final NationSelectionBoard board) {
            super();
            this.m_board = board;
        }
        
        @Override
        public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        }
        
        @Override
        public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        }
        
        @Override
        public boolean onMessage(final Message message) {
            switch (message.getId()) {
                case 20300: {
                    this.nationSelectionBoardResult((NationSelectionInfoResult)message);
                    return false;
                }
                default: {
                    return true;
                }
            }
        }
        
        private void nationSelectionBoardResult(final NationSelectionInfoResult message) {
            final UINationSelectionPanelFrame uiFrame = UINationSelectionPanelFrame.getInstance();
            uiFrame.setNationInfos(message.getNationSelectionInfos());
            WakfuGameEntity.getInstance().pushFrame(uiFrame);
            WakfuGameEntity.getInstance().removeFrame(this);
        }
        
        @Override
        public long getId() {
            return 1L;
        }
        
        @Override
        public void setId(final long id) {
        }
        
        @Override
        public String toString() {
            return "Frame{m_board=" + this.m_board + '}';
        }
    }
}
