package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.seedSpreader.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class SeedSpreader extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private Frame m_netFrame;
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("Ne devrait pas passer par ici");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                ((SeedSpreaderItemizableInfo)SeedSpreader.this.getOrCreateItemizableInfo()).setSeedQuantity(buffer.getShort());
            }
        };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final MRUGenericInteractiveAction open = MRUActions.GENERIC_INTERACTIVE_ACTION.getMRUAction();
        open.setGfxId(MRUGfxConstants.BAG.m_id);
        open.setName("desc.mru.openSeedSpreader");
        open.setActionToExecute(InteractiveElementAction.OPEN);
        final AbstractMRUAction[] mru = { open };
        return mru;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        SeedSpreader.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        SeedSpreader.m_logger.info((Object)("Action performed on interactive element : " + action.toString()));
        this.runScript(action);
        if (action == InteractiveElementAction.OPEN) {
            this.m_netFrame = new Frame(this);
            WakfuGameEntity.getInstance().pushFrame(this.m_netFrame);
            this.sendActionMessage(action);
        }
        return true;
    }
    
    public Frame getNetFrame() {
        return this.m_netFrame;
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
        return 8;
    }
    
    @Override
    public ItemizableInfo getOrCreateItemizableInfo() {
        if (this.m_itemizableInfo == null) {
            this.m_itemizableInfo = new SeedSpreaderItemizableInfo(this);
        }
        return this.m_itemizableInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SeedSpreader.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            SeedSpreader trigger;
            try {
                trigger = (SeedSpreader)Factory.m_pool.borrowObject();
                trigger.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                SeedSpreader.m_logger.error((Object)"Erreur lors de l'extraction d'un SeedSpreader du pool", (Throwable)e);
                trigger = new SeedSpreader();
            }
            return trigger;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<SeedSpreader>() {
                @Override
                public SeedSpreader makeObject() {
                    return new SeedSpreader();
                }
            });
        }
    }
    
    private static class Frame implements MessageFrame
    {
        private final SeedSpreader m_seedSpreader;
        
        Frame(final SeedSpreader seedSpreader) {
            super();
            this.m_seedSpreader = seedSpreader;
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
                case 15942: {
                    this.seedSpreaderResult((OpenSeedSpreaderResultMessage)message);
                    return false;
                }
                default: {
                    return true;
                }
            }
        }
        
        private void seedSpreaderResult(final OpenSeedSpreaderResultMessage message) {
            final UISeedSpreaderFrame uiFrame = UISeedSpreaderFrame.getInstance();
            if (!WakfuGameEntity.getInstance().hasFrame(uiFrame)) {
                WakfuGameEntity.getInstance().pushFrame(uiFrame);
                uiFrame.setSpreader(this.m_seedSpreader);
            }
            uiFrame.setCurrentItem(message.getReferenceId(), message.getQuantity());
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
            return "Frame{m_seedSpreader=" + this.m_seedSpreader + '}';
        }
    }
}
