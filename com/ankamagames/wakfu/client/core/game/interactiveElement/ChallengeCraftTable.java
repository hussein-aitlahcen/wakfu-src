package com.ankamagames.wakfu.client.core.game.interactiveElement;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class ChallengeCraftTable extends ChallengeGenericInteractiveElement
{
    private CraftTable m_craftTable;
    private final BinarSerialPart SHARED_DATAS;
    
    private ChallengeCraftTable() {
        super();
        this.SHARED_DATAS = new BinarSerialPart(22) {
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                ChallengeCraftTable.this.m_position.set(buffer.getInt(), buffer.getInt(), buffer.getShort());
                ChallengeCraftTable.this.m_gfxId = buffer.getInt();
                final byte[] parameters = new byte[buffer.getShort() & 0xFFFF];
                buffer.get(parameters);
                final String p = StringUtils.fromUTF8(parameters);
                ChallengeCraftTable.this.m_parameter = ((p != null) ? p.intern() : null);
                ChallengeCraftTable.this.initialize();
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => pas de s\u00e9rialisation");
            }
        };
    }
    
    @Override
    public InteractiveElementAction getInteractiveDefaultAction() {
        return this.m_craftTable.getDefaultAction();
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        return this.m_craftTable.getMRUActions();
    }
    
    @Override
    public short getMRUHeight() {
        return this.m_craftTable.getMRUHeight();
    }
    
    @Override
    public String getName() {
        return this.m_craftTable.getName();
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return this.m_craftTable.getInteractiveUsableActions();
    }
    
    @Override
    public void initializeWithParameter() {
        this.m_craftTable.setParameter(this.m_parameter);
        this.m_craftTable.initializeWithParameter();
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        return this.m_craftTable.onAction(action, user);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_craftTable.release();
        this.m_craftTable = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        assert this.m_craftTable == null;
        this.m_craftTable = WakfuClientInteractiveElementTypes.CraftTable.getFactory().makeObject();
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        this.m_craftTable.onViewUpdated(view);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        this.initializeWithParameter();
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SHARED_DATAS;
    }
    
    public static class ChallengeCraftTableFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public ChallengeCraftTable makeObject() {
            ChallengeCraftTable element;
            try {
                element = (ChallengeCraftTable)ChallengeCraftTableFactory.m_pool.borrowObject();
                element.setPool(ChallengeCraftTableFactory.m_pool);
            }
            catch (Exception e) {
                ChallengeGenericInteractiveElement.m_logger.error((Object)"Erreur lors de l'extraction d'un Lever du pool", (Throwable)e);
                element = new ChallengeCraftTable(null);
            }
            final ObjectFactory<ClientInteractiveElementView> viewFactory = new WakfuClientInteractiveAnimatedElementSceneView.WakfuInteractiveAnimatedElementSceneViewFactory();
            final ClientInteractiveElementView view = viewFactory.makeObject();
            view.setViewModelId(1);
            view.setViewGfxId(0);
            view.setViewHeight((byte)3);
            element.addView(view);
            return element;
        }
        
        static {
            ChallengeCraftTableFactory.m_pool = new MonitoredPool(new ObjectFactory<ChallengeCraftTable>() {
                @Override
                public ChallengeCraftTable makeObject() {
                    return new ChallengeCraftTable(null);
                }
            });
        }
    }
}
