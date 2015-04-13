package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;

public class HavenWorldResourcesCollector extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private Frame m_netFrame;
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final MRUGenericInteractiveAction open = MRUActions.GENERIC_INTERACTIVE_ACTION.getMRUAction();
        open.setGfxId(MRUGfxConstants.BAG.m_id);
        open.setName("desc.mru.openResourcesCollector");
        open.setActionToExecute(InteractiveElementAction.OPEN);
        final AbstractMRUAction[] mru = { open };
        return mru;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)1);
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
    }
    
    @Override
    public void setPosition(final Point3 position) {
        super.setPosition(position);
        this.getPositionTriggers().add(position);
    }
    
    @Override
    public boolean isTrigger(final Point3 cell) {
        return cell.equalsIgnoringAltitude(this.getPosition());
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        HavenWorldResourcesCollector.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        HavenWorldResourcesCollector.m_logger.info((Object)("Action performed on interactive element : " + action.toString()));
        this.runScript(action);
        if (action == InteractiveElementAction.OPEN) {
            this.m_netFrame = new Frame(this);
            WakfuGameEntity.getInstance().pushFrame(this.m_netFrame);
            this.sendActionMessage(action);
        }
        return false;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.OPEN;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.OPEN };
    }
    
    @Override
    public byte getHeight() {
        return 0;
    }
    
    public Frame getNetFrame() {
        return this.m_netFrame;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldResourcesCollector.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            HavenWorldResourcesCollector trigger;
            try {
                trigger = (HavenWorldResourcesCollector)Factory.m_pool.borrowObject();
                trigger.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                HavenWorldResourcesCollector.m_logger.error((Object)"Erreur lors de l'extraction d'un HavenWorldResourcesCollector du pool", (Throwable)e);
                trigger = new HavenWorldResourcesCollector();
            }
            return trigger;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<HavenWorldResourcesCollector>() {
                @Override
                public HavenWorldResourcesCollector makeObject() {
                    return new HavenWorldResourcesCollector();
                }
            });
        }
    }
    
    private static class Frame implements MessageFrame
    {
        private final HavenWorldResourcesCollector m_collector;
        
        Frame(final HavenWorldResourcesCollector collector) {
            super();
            this.m_collector = collector;
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
                case 20096: {
                    this.havenWorldOpenResourcesCollectorResult((HavenWorldOpenResourcesCollectorResultMessage)message);
                    return false;
                }
                default: {
                    return true;
                }
            }
        }
        
        private void havenWorldOpenResourcesCollectorResult(final HavenWorldOpenResourcesCollectorResultMessage message) {
            final UIHavenWorldResourcesCollectorFrame uiFrame = UIHavenWorldResourcesCollectorFrame.getInstance();
            if (WakfuGameEntity.getInstance().hasFrame(uiFrame)) {
                uiFrame.setHavenWorldResourcesCollectorView(new HavenWorldResourcesCollectorView(message.getResources()));
                uiFrame.resetView();
            }
            else {
                uiFrame.setResourcesCollector(this.m_collector);
                uiFrame.setHavenWorldResourcesCollectorView(new HavenWorldResourcesCollectorView(message.getResources()));
                WakfuGameEntity.getInstance().pushFrame(uiFrame);
            }
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
            return "Frame{m_collector=" + this.m_collector + '}';
        }
    }
}
