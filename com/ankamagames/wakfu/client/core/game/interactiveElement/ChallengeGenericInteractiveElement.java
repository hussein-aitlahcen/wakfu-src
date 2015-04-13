package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class ChallengeGenericInteractiveElement extends WakfuClientMapInteractiveElement
{
    protected static final Logger m_logger;
    protected int m_gfxId;
    protected int m_translatorId;
    protected int m_mruGfx;
    private final BinarSerialPart SHARED_DATAS;
    
    public ChallengeGenericInteractiveElement() {
        super();
        this.SHARED_DATAS = new BinarSerialPart(22) {
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                ChallengeGenericInteractiveElement.this.m_position.set(buffer.getInt(), buffer.getInt(), buffer.getShort());
                ChallengeGenericInteractiveElement.this.m_gfxId = buffer.getInt();
                ChallengeGenericInteractiveElement.this.m_mruGfx = buffer.getInt();
                ChallengeGenericInteractiveElement.this.m_translatorId = buffer.getInt();
                ChallengeGenericInteractiveElement.this.initialize();
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => pas de s\u00e9rialisation");
            }
        };
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_overHeadable = true;
        this.m_overheadOffset = 40;
        this.m_gfxId = 0;
        this.m_translatorId = 0;
        this.m_mruGfx = 0;
        this.setBlockingMovements(true);
        this.setBlockingLineOfSight(false);
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SHARED_DATAS;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        switch (action) {
            case CHALLENGE_ACTIVATE: {
                this.runScript(action);
                this.notifyViews();
                this.sendActionMessage(action);
                this.runScript(action);
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.CHALLENGE_ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.CHALLENGE_ACTIVATE };
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("ie.challenge." + this.m_translatorId);
    }
    
    @Override
    public short getMRUHeight() {
        return (short)(this.getHeight() * 10.0f);
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final AbstractMRUAction[] mRUActions = { null };
        final MRUGenericInteractiveAction action = MRUActions.GENERIC_INTERACTIVE_ACTION.getMRUAction();
        action.setGfxId(this.m_mruGfx);
        action.setName("ie.challenge.mru." + this.m_translatorId);
        action.setActionToExecute(InteractiveElementAction.CHALLENGE_ACTIVATE);
        mRUActions[0] = action;
        return mRUActions;
    }
    
    @Override
    public void onDeSpawn() {
        super.onDeSpawn();
    }
    
    public void initialize() {
        if (this.m_gfxId == -1) {
            LocalPartitionManager.getInstance().removeInteractiveElement(this);
        }
        else {
            for (final ClientInteractiveElementView view : this.getViews()) {
                if (view instanceof WakfuClientInteractiveAnimatedElementSceneView) {
                    view.setViewGfxId(this.m_gfxId);
                }
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChallengeGenericInteractiveElement.class);
    }
    
    public static class ChallengeGenericInteractiveElementFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            ChallengeGenericInteractiveElement element;
            try {
                element = (ChallengeGenericInteractiveElement)ChallengeGenericInteractiveElementFactory.m_pool.borrowObject();
                element.setPool(ChallengeGenericInteractiveElementFactory.m_pool);
            }
            catch (Exception e) {
                ChallengeGenericInteractiveElement.m_logger.error((Object)"Erreur lors de l'extraction d'un Lever du pool", (Throwable)e);
                element = new ChallengeGenericInteractiveElement();
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
            ChallengeGenericInteractiveElementFactory.m_pool = new MonitoredPool(new ObjectFactory<ChallengeGenericInteractiveElement>() {
                @Override
                public ChallengeGenericInteractiveElement makeObject() {
                    return new ChallengeGenericInteractiveElement();
                }
            });
        }
    }
}
