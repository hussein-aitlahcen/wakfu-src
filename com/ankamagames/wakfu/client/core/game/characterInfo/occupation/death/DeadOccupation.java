package com.ankamagames.wakfu.client.core.game.characterInfo.occupation.death;

import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.alea.ambiance.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.graphics.engine.fadeManager.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class DeadOccupation extends AbstractOccupation
{
    private static final Logger m_logger;
    private AnimHelper m_animHelper;
    private final PlayerCharacter m_owner;
    private int m_shaderEffectId;
    private boolean m_canBeRaised;
    
    public boolean canBeRaised() {
        return this.m_canBeRaised;
    }
    
    public DeadOccupation(final PlayerCharacter owner) {
        super();
        this.m_owner = owner;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 4;
    }
    
    @Override
    public boolean isAllowed() {
        return true;
    }
    
    @Override
    public void begin() {
        DeadOccupation.m_logger.info((Object)("[DEATH] Lancement de l'occupation pour le joueur " + this.m_owner));
        this.m_owner.cancelCurrentOccupation(false, true);
        assert this.m_animHelper == null;
        (this.m_animHelper = new AnimHelper()).apply(this.m_owner);
        if (this.m_owner == this.m_localPlayer) {
            this.m_localPlayer.setCanMoveAndInteract(false);
            this.enableUI(false);
            final WakfuGameEntity wakfuGameEntity = WakfuGameEntity.getInstance();
            wakfuGameEntity.pushFrame(CancelFrame.INSTANCE);
            if (!wakfuGameEntity.hasFrame(UIFightEndFrame.getInstance()) && !wakfuGameEntity.hasFrame(UIArcadeDungeonFrame.getInstance())) {
                this.displayMessageBox();
            }
            ClientGameEventManager.INSTANCE.fireEvent(new ClientEventPlayerDeath());
            this.m_shaderEffectId = Shaders.applyDeath();
        }
        this.m_owner.setDead(true);
        this.m_owner.setCurrentOccupation(this);
    }
    
    private void enableUI(final boolean enable) {
        PropertiesProvider.getInstance().setPropertyValue("isAlive", enable);
    }
    
    public void displayMessageBox() {
        CancelFrame.INSTANCE.displayMessageBox();
    }
    
    @Override
    public boolean cancel(final boolean fromServeur, final boolean sendMessage) {
        DeadOccupation.m_logger.info((Object)("[DEATH] Annulation l'occupation pour le joueur " + this.m_owner + " (fromServer=" + fromServeur + ", sendMessage=" + sendMessage + ")"));
        return false;
    }
    
    @Override
    public boolean finish() {
        DeadOccupation.m_logger.info((Object)("[DEATH] Fin de l'occupation pour le joueur " + this.m_owner));
        if (this.m_owner == this.m_localPlayer && CancelFrame.INSTANCE.isChooseToReturnToPhoenix()) {
            this.addListenerToFadeManager();
        }
        else {
            this.addEndAnimationListener();
        }
        this.m_owner.getActor().setAnimation("AnimTombe-Fin");
        if (this.m_owner == this.m_localPlayer) {
            this.m_localPlayer.setCanMoveAndInteract(true);
            this.enableUI(true);
            WakfuGameEntity.getInstance().removeFrame(CancelFrame.INSTANCE);
            Shaders.removeEffect(this.m_shaderEffectId);
        }
        this.m_owner.setDead(false);
        return true;
    }
    
    private void addEndAnimationListener() {
        this.m_owner.getActor().addAnimationEndedListener(new AnimationEndedListener() {
            @Override
            public void animationEnded(final AnimatedElement element) {
                DeadOccupation.this.diplayRaiseEffects();
                element.removeAnimationEndedListener(this);
            }
        });
    }
    
    private void addListenerToFadeManager() {
        FadeManager.getInstance().addListener(new FadeListener() {
            @Override
            public void onFadeInStart() {
                if (DeadOccupation.this.m_animHelper != null) {
                    DeadOccupation.this.m_animHelper.cancel(DeadOccupation.this.m_owner);
                    DeadOccupation.this.m_animHelper = null;
                }
            }
            
            @Override
            public void onFadeOutStart() {
            }
            
            @Override
            public void onFadeInEnd() {
                DeadOccupation.this.m_owner.getActor().setVisible(false);
            }
            
            @Override
            public void onFadeOutEnd() {
                DeadOccupation.this.diplayRaiseEffects();
                FadeManager.getInstance().removeListener(this);
            }
        });
    }
    
    private void diplayRaiseEffects() {
        final CharacterActor actor = this.m_owner.getActor();
        actor.setVisible(true);
        actor.setAnimation("AnimResurection");
        final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(this.m_owner.getResurrectionParticleSystemId());
        system.setTarget(actor);
        IsoParticleSystemManager.getInstance().addParticleSystem(system);
    }
    
    public void build(final byte[] rawData) {
        final ByteBuffer bb = ByteBuffer.wrap(rawData);
        this.m_canBeRaised = (bb.get() == 1);
    }
    
    static {
        m_logger = Logger.getLogger((Class)DeadOccupation.class);
    }
    
    private static class AnimHelper
    {
        static final AnimationEndedListener m_animListener;
        int m_apsId;
        
        void apply(final PlayerCharacter owner) {
            final CharacterActor actor = owner.getActor();
            actor.setDirection(Direction8.SOUTH_EAST);
            actor.addAnimationEndedListener(AnimHelper.m_animListener);
            actor.setAnimation("AnimDesincarnation");
            final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(owner.getDeathParticleSystemId());
            this.m_apsId = system.getId();
            system.setTarget(actor);
            IsoParticleSystemManager.getInstance().addParticleSystem(system);
        }
        
        void cancel(final PlayerCharacter owner) {
            assert this.m_apsId != 0;
            IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_apsId);
            owner.getActor().removeAnimationEndedListener(AnimHelper.m_animListener);
            this.m_apsId = 0;
        }
        
        static {
            m_animListener = new AnimationEndedListener() {
                @Override
                public void animationEnded(final AnimatedElement element) {
                    element.setAnimation("AnimTombe");
                    element.removeAnimationEndedListener(this);
                }
            };
        }
    }
}
