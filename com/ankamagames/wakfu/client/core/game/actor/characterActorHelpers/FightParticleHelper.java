package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers;

import com.ankamagames.baseImpl.graphics.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class FightParticleHelper implements VisibleChangedListener
{
    private static final Logger m_logger;
    private FreeParticleSystem m_fightActiveParticleSystem;
    private FreeParticleSystem m_fightSelectedParticleSystem;
    private FreeParticleSystem m_fightTeamParticleSystem;
    private FreeParticleSystem m_fightDirectionParticleSystem;
    private FreeParticleSystem m_fightCrossSwordParticleSystem;
    private FreeParticleSystem m_extraTourParticleSystem;
    private final CharacterActor m_actor;
    
    public FightParticleHelper(final CharacterActor actor) {
        super();
        assert actor != null;
        (this.m_actor = actor).addVisibleChangedListener(this);
    }
    
    @Override
    public void onVisibleChanged(final boolean visible, final VisibleChangedCause cause) {
        if (this.m_actor == null) {
            return;
        }
        if (visible) {
            this.m_actor.showAllParticleSystems();
        }
        else {
            this.m_actor.hideAllParticleSystems();
        }
    }
    
    public void clearAllParticleSystems() {
        this.clearActiveParticleSystem();
        this.clearTeamParticleSystem();
        this.clearCrossSwordParticleSystem();
        this.clearExtraTourParticleSystem();
        this.clearSelectedParticleSystem();
        this.clearDirectionParticleSystem();
    }
    
    public void showActiveParticleSystem() {
        if (this.m_fightActiveParticleSystem != null) {
            this.m_fightActiveParticleSystem.setVisible(true);
        }
    }
    
    public void hideActiveParticleSystem() {
        if (this.m_fightActiveParticleSystem != null) {
            this.m_fightActiveParticleSystem.setVisible(false);
        }
    }
    
    public void clearActiveParticleSystem() {
        if (this.m_fightActiveParticleSystem != null) {
            IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_fightActiveParticleSystem.getId());
            this.m_fightActiveParticleSystem = null;
        }
    }
    
    public void clearSelectedParticleSystem() {
        if (this.m_fightSelectedParticleSystem != null) {
            IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_fightSelectedParticleSystem.getId());
            this.m_fightSelectedParticleSystem = null;
        }
    }
    
    public void showTeamParticleSystem() {
        if (this.m_fightTeamParticleSystem != null) {
            this.m_fightTeamParticleSystem.setVisible(true);
        }
    }
    
    public void hideTeamParticleSystem() {
        if (this.m_fightTeamParticleSystem != null) {
            this.m_fightTeamParticleSystem.setVisible(false);
        }
    }
    
    public void showDirectionParticleSystem() {
        if (this.m_fightDirectionParticleSystem != null) {
            this.m_fightDirectionParticleSystem.setVisible(true);
        }
    }
    
    public void hideDirectionParticleSystem() {
        if (this.m_fightDirectionParticleSystem != null) {
            this.m_fightDirectionParticleSystem.setVisible(false);
        }
    }
    
    public void addPassiveTeamParticleSystem(final byte teamId) {
        this.clearTeamParticleSystem();
        this.addTeamParticleSystem(teamId, WakfuClientConstants.FIGHTER_PASSIVE_TEAM_PARTICLE_SYSTEM_FILE_ID_PER_RADIUS);
    }
    
    public void addActiveTeamParticleSystem(final byte teamId) {
        this.clearTeamParticleSystem();
        this.addTeamParticleSystem(teamId, WakfuClientConstants.FIGHTER_ACTIVE_TEAM_PARTICLE_SYSTEM_FILE_ID_PER_RADIUS);
    }
    
    private void addTeamParticleSystem(final byte teamId, final int[][] apsArray) {
        final CharacterInfo characterInfo = this.m_actor.getCharacterInfo();
        if (characterInfo == null || this.m_fightTeamParticleSystem != null) {
            FightParticleHelper.m_logger.error((Object)String.format("Cercle d'\u00e9quipe d\u00e9j\u00e0 pr\u00e9sent sur #%d %s - %s", this.m_actor.getId(), characterInfo, ExceptionFormatter.currentStackTrace(6)));
            return;
        }
        final int fightId = characterInfo.getCurrentFightId();
        byte physicalRadius = characterInfo.getPhysicalRadius();
        if (physicalRadius < 0 || physicalRadius >= apsArray.length) {
            physicalRadius = 0;
        }
        if (teamId < 0 || teamId >= apsArray[physicalRadius].length) {
            FightParticleHelper.m_logger.error((Object)String.format("Id de team invalide pour afficher le cercle d'\u00e9quipe sur #%d %s %s", this.m_actor.getId(), characterInfo, ExceptionFormatter.currentStackTrace(6)));
            return;
        }
        assert this.m_fightTeamParticleSystem == null;
        this.clearTeamParticleSystem();
        this.m_fightTeamParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(apsArray[physicalRadius][teamId]);
        if (this.m_fightTeamParticleSystem != null) {
            this.m_fightTeamParticleSystem.setTarget(this.m_actor);
            this.m_fightTeamParticleSystem.setFightId(fightId);
            this.m_fightTeamParticleSystem.setVisible(this.m_actor.isVisible());
            IsoParticleSystemManager.getInstance().addParticleSystem(this.m_fightTeamParticleSystem);
        }
        else {
            FightParticleHelper.m_logger.error((Object)("Impossible d'afficher le cercle d'\u00e9quipe sur l'acteur id=" + this.m_actor.getId() + " : APS invalide (" + apsArray[physicalRadius][teamId] + ")"));
        }
    }
    
    public void clearTeamParticleSystem() {
        if (this.m_fightTeamParticleSystem == null) {
            return;
        }
        IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_fightTeamParticleSystem.getId());
        this.m_fightTeamParticleSystem = null;
    }
    
    public void addDirectionParticleSystem(final Direction8 direction) {
        final CharacterInfo characterInfo = this.m_actor.getCharacterInfo();
        final int fightId = characterInfo.getCurrentFightId();
        final byte teamId = characterInfo.getTeamId();
        if (teamId < 0 || teamId >= WakfuClientConstants.FIGHTER_DIRECTION_PARTICLE_SYSTEM_FILE_ID.length) {
            return;
        }
        final int[] particles = WakfuClientConstants.FIGHTER_DIRECTION_PARTICLE_SYSTEM_FILE_ID[teamId];
        final int index = direction.m_index;
        int particuleId = 0;
        switch (index) {
            case 1: {
                particuleId = particles[0];
                break;
            }
            case 3: {
                particuleId = particles[1];
                break;
            }
            case 5: {
                particuleId = particles[2];
                break;
            }
            case 7: {
                particuleId = particles[3];
                break;
            }
            default: {
                particuleId = particles[0];
                break;
            }
        }
        final byte physicalRadius = characterInfo.getPhysicalRadius();
        Point3 apsTarget = null;
        if (physicalRadius == 1) {
            apsTarget = characterInfo.getPosition();
            apsTarget.shift(direction);
        }
        this.clearDirectionParticleSystem();
        this.m_fightDirectionParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(particuleId);
        if (this.m_fightDirectionParticleSystem != null) {
            if (apsTarget != null) {
                this.m_fightDirectionParticleSystem.setWorldPosition(apsTarget.getX(), apsTarget.getY(), apsTarget.getZ());
            }
            else {
                this.m_fightDirectionParticleSystem.setTarget(this.m_actor);
            }
            this.m_fightDirectionParticleSystem.setFightId(fightId);
            this.m_fightDirectionParticleSystem.setVisible(this.m_actor.isVisible());
            IsoParticleSystemManager.getInstance().addParticleSystem(this.m_fightDirectionParticleSystem);
        }
    }
    
    public void clearDirectionParticleSystem() {
        if (this.m_fightDirectionParticleSystem == null) {
            return;
        }
        IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_fightDirectionParticleSystem.getId());
        this.m_fightDirectionParticleSystem = null;
    }
    
    public void addActiveParticleSystem() {
        final CharacterInfo characterInfo = this.m_actor.getCharacterInfo();
        if (characterInfo == null) {
            return;
        }
        if (!characterInfo.canBeFoundByUI()) {
            return;
        }
        final int fightId = characterInfo.getCurrentFightId();
        final byte teamId = characterInfo.getTeamId();
        if (teamId < 0 || teamId >= WakfuClientConstants.FIGHTER_ACTIVE_PARTICLE_SYSTEM_FILE_ID.length) {
            return;
        }
        this.m_fightActiveParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(WakfuClientConstants.FIGHTER_ACTIVE_PARTICLE_SYSTEM_FILE_ID[teamId]);
        if (this.m_fightActiveParticleSystem != null) {
            this.m_fightActiveParticleSystem.setTarget(this.m_actor);
            this.m_fightActiveParticleSystem.setFightId(fightId);
            IsoParticleSystemManager.getInstance().addParticleSystem(this.m_fightActiveParticleSystem);
        }
    }
    
    public void addSelectedParticleSystem() {
        final CharacterInfo characterInfo = this.m_actor.getCharacterInfo();
        if (characterInfo == null) {
            return;
        }
        if (!characterInfo.canBeFoundByUI()) {
            return;
        }
        final int fightId = characterInfo.getCurrentFightId();
        final byte teamId = characterInfo.getTeamId();
        if (teamId < 0 || teamId >= WakfuClientConstants.FIGHTER_SELECTED_PARTICLE_SYSTEM_FILE_ID.length) {
            return;
        }
        this.m_fightSelectedParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(WakfuClientConstants.FIGHTER_SELECTED_PARTICLE_SYSTEM_FILE_ID[teamId]);
        if (this.m_fightSelectedParticleSystem != null) {
            this.m_fightSelectedParticleSystem.setTarget(this.m_actor);
            this.m_fightSelectedParticleSystem.setFightId(fightId);
            IsoParticleSystemManager.getInstance().addParticleSystem(this.m_fightSelectedParticleSystem);
        }
    }
    
    public void addCrossSwordParticleSystem(final byte elementId) {
        final CharacterInfo characterInfo = this.m_actor.getCharacterInfo();
        if (characterInfo == null) {
            return;
        }
        final int fightid = characterInfo.getCurrentFightId();
        if (this.m_fightCrossSwordParticleSystem != null) {
            IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_fightCrossSwordParticleSystem.getId());
            this.m_fightCrossSwordParticleSystem = null;
        }
        if (elementId >= 0) {
            this.m_fightCrossSwordParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(WakfuClientConstants.FIGHTER_ELEMENT_SWORD_APS_ID[elementId]);
        }
        else if (characterInfo instanceof PlayerCharacter) {
            this.m_fightCrossSwordParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(WakfuClientConstants.FIGHTER_SWORD_APS_ID[0]);
        }
        else {
            this.m_fightCrossSwordParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(WakfuClientConstants.FIGHTER_SWORD_APS_ID[1]);
        }
        if (this.m_fightCrossSwordParticleSystem != null) {
            this.m_fightCrossSwordParticleSystem.setTarget(this.m_actor);
            this.m_fightCrossSwordParticleSystem.setFightId(fightid);
            IsoParticleSystemManager.getInstance().addParticleSystem(this.m_fightCrossSwordParticleSystem);
        }
    }
    
    public void addTauntParticleSystem() {
        final CharacterInfo characterInfo = this.m_actor.getCharacterInfo();
        if (characterInfo == null) {
            return;
        }
        final int fightid = characterInfo.getCurrentFightId();
        if (this.m_fightCrossSwordParticleSystem != null) {
            IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_fightCrossSwordParticleSystem.getId());
            this.m_fightCrossSwordParticleSystem = null;
        }
        this.m_fightCrossSwordParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(WakfuClientConstants.FIGHTER_SWORD_APS_ID[1]);
        if (this.m_fightCrossSwordParticleSystem != null) {
            this.m_fightCrossSwordParticleSystem.setTarget(this.m_actor);
            this.m_fightCrossSwordParticleSystem.setFightId(fightid);
            IsoParticleSystemManager.getInstance().addParticleSystem(this.m_fightCrossSwordParticleSystem);
        }
    }
    
    public void clearCrossSwordParticleSystem() {
        if (this.m_fightCrossSwordParticleSystem != null) {
            IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_fightCrossSwordParticleSystem.getId());
            this.m_fightCrossSwordParticleSystem = null;
        }
    }
    
    public void addExtraTourParticleSystem() {
        final CharacterInfo characterInfo = this.m_actor.getCharacterInfo();
        if (characterInfo == null) {
            return;
        }
        final int fightid = characterInfo.getCurrentFightId();
        final byte teamId = characterInfo.getTeamId();
        if (this.m_extraTourParticleSystem != null) {
            IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_extraTourParticleSystem.getId());
            this.m_extraTourParticleSystem = null;
        }
        if (teamId >= 0 && teamId < WakfuClientConstants.FIGHTER_EXTRA_TURN_PARTICLE_SYSTEM_FILE_ID.length) {
            this.m_extraTourParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(WakfuClientConstants.FIGHTER_EXTRA_TURN_PARTICLE_SYSTEM_FILE_ID[teamId]);
        }
        if (this.m_extraTourParticleSystem != null) {
            this.m_extraTourParticleSystem.setTarget(this.m_actor);
            this.m_extraTourParticleSystem.setFightId(fightid);
            IsoParticleSystemManager.getInstance().addParticleSystem(this.m_extraTourParticleSystem);
        }
    }
    
    public void clearExtraTourParticleSystem() {
        if (this.m_extraTourParticleSystem != null) {
            IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_extraTourParticleSystem.getId());
            this.m_extraTourParticleSystem = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightParticleHelper.class);
    }
}
