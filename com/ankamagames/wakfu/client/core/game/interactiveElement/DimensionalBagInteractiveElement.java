package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.alea.environment.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DimensionalBagInteractiveElement extends WakfuClientMapInteractiveElement implements AnimationEndedListener
{
    public static final short STATE_CLOSED = 1;
    public static final short STATE_OPENED = 2;
    protected static final Logger m_logger;
    public static final short DBIE_INVISIBLE = 0;
    public static final short DBIE_SPAWNED_NO_FLEA = 1;
    public static final short DBIE_SPAWNED_FLEA_OPENED = 2;
    private DimensionalBagView m_infoProvider;
    private boolean m_locked;
    private final BinarSerialPart SHARED_DATAS;
    
    protected DimensionalBagInteractiveElement() {
        super();
        this.SHARED_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => par de s\u00e9rialisation");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                DimensionalBagInteractiveElement.this.m_world = buffer.getShort();
                final int x = buffer.getInt();
                final int y = buffer.getInt();
                final short z = buffer.getShort();
                DimensionalBagInteractiveElement.this.m_position.set(x, y, z);
                DimensionalBagInteractiveElement.this.m_locked = (buffer.get() == 1);
                final RawDimensionalBagForSpawn rawBag = new RawDimensionalBagForSpawn();
                if (rawBag.unserialize(buffer)) {
                    DimensionalBagInteractiveElement.this.m_infoProvider = new DimensionalBagView();
                    if (!DimensionalBagInteractiveElement.this.m_infoProvider.fromRaw(rawBag)) {
                        DimensionalBagInteractiveElement$1.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer les donn\u00e9es du RawDimensionalBag pour le sac id=" + DimensionalBagInteractiveElement.this.getId()));
                    }
                }
                else {
                    DimensionalBagInteractiveElement$1.m_logger.error((Object)("Impossible de d\u00e9s\u00e9rialiser le RawDimensionalBag pour le sac id=" + DimensionalBagInteractiveElement.this.getId()));
                }
                if ((DimensionalBagInteractiveElement.this.m_state == 2 || DimensionalBagInteractiveElement.this.m_state == 1) && DimensionalBagInteractiveElement.this.m_oldState == 0) {
                    DimensionalBagInteractiveElement.this.synchronizeIEandActor();
                }
                DimensionalBagInteractiveElement.this.m_oldState = DimensionalBagInteractiveElement.this.m_state;
                DimensionalBagInteractiveElement.this.updateDirection();
            }
        };
    }
    
    public DimensionalBagView getInfoProvider() {
        return this.m_infoProvider;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        return false;
    }
    
    private void updateDirection() {
        final int x = this.m_position.getX();
        final int y = this.m_position.getY();
        final short z = this.m_position.getZ();
        Direction8 direction = Direction8.SOUTH_EAST;
        final WakfuClientEnvironmentMap environmentMap = (WakfuClientEnvironmentMap)EnvironmentMapManager.getInstance().getMapFromCell(x, y);
        if (environmentMap != null) {
            final Direction8 forcedBagDirection = environmentMap.getBagDirectionOn(x, y, z);
            if (forcedBagDirection != null && forcedBagDirection != Direction8.NONE) {
                direction = forcedBagDirection;
            }
        }
        this.setDirection(direction);
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SHARED_DATAS;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.ENTER;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ENTER };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        return new AbstractMRUAction[] { MRUActions.DIMENSIONAL_BAG_ENTER_ACTION.getMRUAction(), MRUActions.DIMENSIONAL_BAG_BROWSE_FLEA_ACTION.getMRUAction() };
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("bag.of", this.m_infoProvider.getOwnerName());
    }
    
    @Override
    public boolean isUsingSpecificOldState() {
        if (this.m_oldState == 0 && this.m_state == 1) {
            this.m_oldState = 0;
            return true;
        }
        if (this.m_state == 2) {
            this.m_oldState = 1;
            return true;
        }
        return false;
    }
    
    private void synchronizeIEandActor() {
        final CharacterInfo player = CharacterInfoManager.getInstance().getCharacter(this.m_infoProvider.getOwnerId());
        if (player != null) {
            final Actor playerActor = player.getActor();
            playerActor.setDirection(Direction8.SOUTH_EAST);
            playerActor.addAnimationEndedListener(this);
        }
    }
    
    private void clearAnimationListeners() {
        if (this.m_infoProvider != null) {
            final long id = this.m_infoProvider.getOwnerId();
            final CharacterInfo player = CharacterInfoManager.getInstance().getCharacter(id);
            if (player != null) {
                final Actor playerActor = player.getActor();
                playerActor.setWaitEndAnimation(false);
                playerActor.removeAnimationEndedListener(this);
            }
        }
    }
    
    @Override
    public void animationEnded(final AnimatedElement element) {
        this.clearAnimationListeners();
    }
    
    @Override
    public void runDespawnAnimation() {
        this.m_state = 1;
        this.notifyViews();
        final CharacterInfo player = CharacterInfoManager.getInstance().getCharacter(this.m_infoProvider.getOwnerId());
        if (player != null) {
            final Actor playerActor = player.getActor();
            playerActor.setDirection(Direction8.SOUTH_EAST);
            playerActor.setAnimation("AnimStatique");
        }
    }
    
    @Override
    public boolean isVisible() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (player == null) {
            return false;
        }
        final Fight fight = player.getCurrentOrObservedFight();
        final Point3 pos = this.getPosition();
        return super.isVisible() && (fight == null || !fight.getFightMap().isInsideOrBorder(pos.getX(), pos.getY()));
    }
    
    public boolean isLocked() {
        return this.m_locked;
    }
    
    @Override
    public void onCheckIn() {
        this.clearAnimationListeners();
        this.m_infoProvider = null;
        super.onCheckIn();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_overHeadable = true;
        this.m_oldState = 0;
        this.m_locked = false;
        this.m_state = 0;
        this.m_usingSpecificOldState = true;
        this.m_useSpecificAnimTransition = true;
        this.m_usingDespawnAnimation = true;
        assert this.m_infoProvider == null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagInteractiveElement.class);
    }
    
    public static class DimensionalBagInteractiveElementFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static final MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            DimensionalBagInteractiveElement element;
            try {
                element = (DimensionalBagInteractiveElement)DimensionalBagInteractiveElementFactory.m_pool.borrowObject();
                element.setPool(DimensionalBagInteractiveElementFactory.m_pool);
            }
            catch (Exception e) {
                DimensionalBagInteractiveElement.m_logger.error((Object)("Erreur lors de l'extraction d'un " + DimensionalBagInteractiveElement.class.getName() + " du pool"), (Throwable)e);
                element = new DimensionalBagInteractiveElement();
            }
            return element;
        }
        
        static {
            m_pool = new MonitoredPool(new ObjectFactory<DimensionalBagInteractiveElement>() {
                @Override
                public DimensionalBagInteractiveElement makeObject() {
                    return new DimensionalBagInteractiveElement();
                }
            });
        }
    }
}
