package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class Dummy extends WakfuClientMapInteractiveElement
{
    protected static final Logger m_logger;
    private final BinarSerialPart SHARED_DATAS;
    
    protected Dummy() {
        super();
        this.SHARED_DATAS = new BinarSerialPart(3) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => client par de s\u00e9rialisation");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                Dummy.this.m_state = buffer.getShort();
                final boolean visible = buffer.get() == 1;
                Dummy.this.setVisible(visible);
                Dummy.this.setBlockingMovements(visible);
            }
        };
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SHARED_DATAS;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_state = 1;
        this.setVisible(true);
        this.setBlockingLineOfSight(false);
        this.setBlockingMovements(true);
        this.m_selectable = false;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        return false;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.NONE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return InteractiveElementAction.EMPTY_ACTIONS;
    }
    
    @Override
    public byte getHeight() {
        return 4;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Dummy.class);
    }
    
    public static class DummyFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            Dummy dummy;
            try {
                dummy = (Dummy)DummyFactory.m_pool.borrowObject();
                dummy.setPool(DummyFactory.m_pool);
            }
            catch (Exception e) {
                Dummy.m_logger.error((Object)"Erreur lors de l'extraction d'un Lever du pool", (Throwable)e);
                dummy = new Dummy();
            }
            return dummy;
        }
        
        static {
            DummyFactory.m_pool = new MonitoredPool(new ObjectFactory<Dummy>() {
                @Override
                public Dummy makeObject() {
                    return new Dummy();
                }
            });
        }
    }
}
