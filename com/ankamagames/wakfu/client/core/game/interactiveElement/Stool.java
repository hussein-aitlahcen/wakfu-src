package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class Stool extends WakfuClientMapInteractiveElement implements ChaosInteractiveElement
{
    private static final Logger m_logger;
    private long m_characterId;
    protected IEStoolParameter m_stoolParameters;
    private final BinarSerialPart SHARED_DATAS;
    
    public Stool() {
        super();
        this.SHARED_DATAS = new BinarSerialPart(8) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => par de s\u00e9rialisation");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                Stool.this.m_characterId = buffer.getLong();
                Stool.this.setVisible(Stool.this.m_characterId == 0L);
                final CharacterInfo user = CharacterInfoManager.getInstance().getCharacter(Stool.this.m_characterId);
                if (user == null) {
                    return;
                }
                final BasicOccupation occupation = user.getCurrentOccupation();
                if (occupation == null || occupation.getOccupationTypeId() != 16) {
                    return;
                }
                final SitOccupation sit = (SitOccupation)occupation;
                sit.apply(Stool.this);
            }
        };
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        Stool.m_logger.info((Object)("[ON VIEW UPDATED] Stool : " + view));
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setTransitionModel(TransitionModel.FORCE_NO_TRANS);
        this.setBlockingLineOfSight(false);
        this.setBlockingMovements(false);
        this.setOverHeadable(false);
        this.m_characterId = 0L;
        assert this.m_stoolParameters == null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_stoolParameters = null;
    }
    
    @Override
    public void initializeWithParameter() {
        if (StringUtils.isEmptyOrNull(this.m_parameter)) {
            this.m_stoolParameters = null;
            return;
        }
        final String[] params = this.m_parameter.split(";");
        if (params.length != 1) {
            Stool.m_logger.error((Object)("[LD] L'IE de Stool " + this.m_id + " doit avoir 0 ou 1 param\u00e8tre"));
            return;
        }
        final IEStoolParameter param = (IEStoolParameter)IEParametersManager.INSTANCE.getParam(IETypes.STOOL, Integer.valueOf(params[0]));
        if (param == null) {
            Stool.m_logger.error((Object)("[LD] L'IE de Stool " + this.m_id + " \u00e0 un parametre [" + Integer.valueOf(params[0]) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_stoolParameters = param;
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SHARED_DATAS;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        final PlayerCharacter player = (PlayerCharacter)user;
        if (!this.canUse(player)) {
            return true;
        }
        this.runScript(action);
        switch (action) {
            case SITON:
            case STANDUP: {
                this.sendActionMessage(action);
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        if (this.getUserCharacterId() == 0L) {
            return InteractiveElementAction.SITON;
        }
        return InteractiveElementAction.NONE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        if (this.getUserCharacterId() == 0L) {
            return new InteractiveElementAction[] { InteractiveElementAction.SITON };
        }
        return InteractiveElementAction.EMPTY_ACTIONS;
    }
    
    private long getUserCharacterId() {
        if (this.m_characterId == 0L) {
            return 0L;
        }
        final CharacterInfo user = CharacterInfoManager.getInstance().getCharacter(this.m_characterId);
        if (user == null) {
            this.m_characterId = 0L;
        }
        return this.m_characterId;
    }
    
    public void removeCharacter() {
        this.m_characterId = 0L;
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        if (this.getUserCharacterId() != 0L) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        final MRUInteractifMachine action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        final ActionVisual actionVisual = this.getVisualAction();
        if (actionVisual != null) {
            action.setGfxId(actionVisual.getMruGfx());
            action.setTextKey("desc.mru." + actionVisual.getMruLabelKey());
        }
        else {
            action.setGfxId(MRUGfxConstants.SIT.m_id);
            action.setTextKey("desc.mru.sit");
        }
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public boolean hasToFinishOnIE() {
        return this.getUserCharacterId() == 0L && this.m_hasToFinishOnIE;
    }
    
    @Override
    public byte getHeight() {
        return 2;
    }
    
    @Override
    public boolean isUsable() {
        return super.isUsable() && this.canUse(WakfuGameEntity.getInstance().getLocalPlayer());
    }
    
    private boolean canUse(final PlayerCharacter player) {
        final boolean chaosDestroyed = this.getChaosElementHandler().isDestroyed();
        final boolean hasAnimation = this.hasSitAnimation(player);
        if (!hasAnimation) {
            Stool.m_logger.error((Object)("Le perso doit poss\u00e9der l'anim " + this.getSitAnimName()));
        }
        final boolean criterionValid = this.m_stoolParameters == null || this.m_stoolParameters.getCriterion().isValid(player, this, null, player.getAppropriateContext());
        return chaosDestroyed || (hasAnimation && criterionValid);
    }
    
    private boolean hasSitAnimation(final PlayerCharacter player) {
        final AnmInstance anmInstance = player.getActor().getAnmInstance();
        return anmInstance != null && anmInstance.containsAnimation(this.getSitAnimName());
    }
    
    private String getSitAnimName() {
        return AnimatedElementWithDirection.createLinkage(Direction8.SOUTH_EAST.m_index, this.getAnimationName(), "-Assis");
    }
    
    public String getAnimationName() {
        final ActionVisual visual = this.getVisualAction();
        if (visual == null || StringUtils.isEmptyOrNull(visual.getAnimLink())) {
            Stool.m_logger.error((Object)("Visuel sans anim pour le stool " + this.getId()));
            return "";
        }
        return visual.getAnimLink();
    }
    
    private ActionVisual getVisualAction() {
        if (this.m_stoolParameters == null) {
            Stool.m_logger.error((Object)("Pas de visuel pour le stool " + this.getId()));
            return null;
        }
        return ActionVisualManager.getInstance().get(this.m_stoolParameters.getVisualId());
    }
    
    @Override
    public ChaosIEParameter getChaosIEParameter() {
        return this.m_stoolParameters;
    }
    
    public boolean comeFromFront() {
        return this.m_activationPattern == InteractiveElementActivationPattern.FRONT.getPatternId();
    }
    
    @Override
    public void setDirection(Direction8 direction) {
        if (this.comeFromFront() && direction != Direction8.SOUTH_EAST && direction != Direction8.SOUTH_WEST) {
            direction = Direction8.SOUTH_EAST;
        }
        super.setDirection(direction);
    }
    
    @Override
    public ItemizableInfo getOrCreateItemizableInfo() {
        if (this.m_itemizableInfo == null) {
            this.m_itemizableInfo = new StoolItemizableInfo(this);
        }
        return this.m_itemizableInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Stool.class);
    }
    
    public static class StoolFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            Stool stool;
            try {
                stool = (Stool)StoolFactory.m_pool.borrowObject();
                stool.setPool(StoolFactory.m_pool);
            }
            catch (Exception e) {
                Stool.m_logger.error((Object)"Erreur lors de l'extraction d'un Lever du pool", (Throwable)e);
                stool = new Stool();
            }
            return stool;
        }
        
        static {
            StoolFactory.m_pool = new MonitoredPool(new ObjectFactory<Stool>() {
                @Override
                public Stool makeObject() {
                    return new Stool();
                }
            });
        }
    }
}
