package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class GenericStaticInteractiveElement extends WakfuClientMapInteractiveElement
{
    protected static final Logger m_logger;
    private int m_gfxId;
    private int m_translatorId;
    private int m_mruGfx;
    private final BinarSerialPart SHARED_DATAS;
    
    public GenericStaticInteractiveElement() {
        super();
        this.SHARED_DATAS = new BinarSerialPart(22) {
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                GenericStaticInteractiveElement.this.m_position.set(buffer.getInt(), buffer.getInt(), buffer.getShort());
                final int oldGfx = GenericStaticInteractiveElement.this.m_gfxId;
                GenericStaticInteractiveElement.this.m_gfxId = buffer.getInt();
                GenericStaticInteractiveElement.this.m_mruGfx = buffer.getInt();
                GenericStaticInteractiveElement.this.m_translatorId = buffer.getInt();
                if (GenericStaticInteractiveElement.this.m_translatorId == -1) {
                    GenericStaticInteractiveElement.this.setOverHeadable(false);
                }
                else {
                    GenericStaticInteractiveElement.this.setOverHeadable(true);
                }
                if (oldGfx != GenericStaticInteractiveElement.this.m_gfxId) {
                    GenericStaticInteractiveElement.this.initialize();
                }
                GenericStaticInteractiveElement.this.notifyViews();
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => pas de s\u00e9rialisation");
            }
        };
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        GenericStaticInteractiveElement.m_logger.info((Object)("Action performed on interactive element : " + action.toString()));
        this.sendActionMessage(action);
        this.runScript(action);
        return true;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.CHALLENGE_ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.CHALLENGE_ACTIVATE };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        if (this.m_mruGfx == -1) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        final AbstractMRUAction[] mRUActions = { null };
        final MRUGenericInteractiveAction action = MRUActions.GENERIC_INTERACTIVE_ACTION.getMRUAction();
        action.setGfxId(this.m_mruGfx);
        action.setName("ie.scenario.mru." + this.m_translatorId);
        action.setActionToExecute(InteractiveElementAction.CHALLENGE_ACTIVATE);
        mRUActions[0] = action;
        return mRUActions;
    }
    
    @Override
    public short getMRUHeight() {
        return (short)(this.getHeight() * 10.0f);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
        this.m_gfxId = 0;
        this.m_translatorId = -1;
        this.m_mruGfx = -1;
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SHARED_DATAS;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("ie.scenario.name." + this.m_translatorId);
    }
    
    void initialize() {
        if (this.m_gfxId != -1) {
            if (this.getViews().isEmpty()) {
                final ObjectFactory<ClientInteractiveElementView> viewFactory = new WakfuClientInteractiveAnimatedElementSceneView.WakfuInteractiveAnimatedElementSceneViewFactory();
                final ClientInteractiveElementView view = viewFactory.makeObject();
                view.setViewModelId(1);
                view.setViewGfxId(0);
                view.setViewHeight((byte)4);
                this.addView(view);
            }
            final Iterator i$ = this.getViews().iterator();
            while (i$.hasNext()) {
                final ClientInteractiveElementView view = i$.next();
                if (view instanceof WakfuClientInteractiveAnimatedElementSceneView) {
                    view.setViewGfxId(this.m_gfxId);
                }
            }
        }
    }
    
    @Override
    public void initializeWithParameter() {
        super.initializeWithParameter();
        final String[] params = this.m_parameter.split(";");
        if (params.length != 1 && params.length != 2) {
            GenericStaticInteractiveElement.m_logger.error((Object)"[LevelDesign] Un \u00e9l\u00e9ment de sc\u00e9nario g\u00e9n\u00e9rique doit avoir 1 ou 2 param\u00e8tres : id dans le translator et id du gfx du MRU (-1 si cet \u00e9l\u00e9ment ne doit pas avoir de MRU");
            return;
        }
        this.m_translatorId = Integer.valueOf(params[0].trim());
        this.setDirection(Direction8.SOUTH_WEST);
    }
    
    @Override
    public void onDeSpawn() {
        super.onDeSpawn();
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public Direction8 getDirection() {
        return Direction8.SOUTH_WEST;
    }
    
    @Override
    public boolean checkSubscription() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
    }
    
    static {
        m_logger = Logger.getLogger((Class)GenericStaticInteractiveElement.class);
    }
    
    public static class GenericStaticInteractiveElementFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static final MonitoredPool m_pool;
        
        @Override
        public GenericStaticInteractiveElement makeObject() {
            GenericStaticInteractiveElement element;
            try {
                element = (GenericStaticInteractiveElement)GenericStaticInteractiveElementFactory.m_pool.borrowObject();
                element.setPool(GenericStaticInteractiveElementFactory.m_pool);
            }
            catch (Exception e) {
                GenericStaticInteractiveElement.m_logger.error((Object)"Erreur lors de l'extraction d'un LootChest du pool", (Throwable)e);
                element = new GenericStaticInteractiveElement();
            }
            return element;
        }
        
        static {
            m_pool = new MonitoredPool(new ObjectFactory<GenericStaticInteractiveElement>() {
                @Override
                public GenericStaticInteractiveElement makeObject() {
                    return new GenericStaticInteractiveElement();
                }
            });
        }
    }
}
