package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.alea.animation.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.gameAction.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.common.game.mount.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;

public class RideOccupation extends AbstractOccupation
{
    private static final Logger m_logger;
    private final CharacterInfo m_user;
    private Pet m_mount;
    private Anm m_anm;
    
    public RideOccupation(final CharacterInfo user) {
        super();
        this.m_user = user;
    }
    
    @Override
    public boolean onPlayerMove() {
        return true;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 14;
    }
    
    @Override
    public boolean isAllowed() {
        final PlayerCharacter playerCharacter = (PlayerCharacter)this.m_user;
        final CharacterActor actor = playerCharacter.getActor();
        if (actor.getCurrentPath() != null) {
            return false;
        }
        final PetMobileView mountMobile = playerCharacter.getMountMobile();
        if (mountMobile == null) {
            return false;
        }
        final Pet pet = mountMobile.getPet();
        if (!pet.isActive()) {
            return false;
        }
        final HMIHelper hmiHelper = actor.getHmiHelper();
        return !hmiHelper.hasMonsterSkin() && !hmiHelper.hasAppearanceChanges();
    }
    
    @Override
    public void begin() {
        if (this.m_user == this.m_localPlayer) {
            UIMRUFrame.getInstance().closeCurrentMRU();
            applyDezoom();
        }
        this.m_user.setCurrentOccupation(this);
        this.applyParticleSystem();
        this.rideOnPet();
    }
    
    private void applyParticleSystem() {
        final FreeParticleSystem particle = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800450);
        if (particle == null) {
            RideOccupation.m_logger.warn((Object)"le system de particule 800450 n'exsite pas");
            return;
        }
        particle.setTarget(this.m_user.getActor());
        IsoParticleSystemManager.getInstance().addParticleSystem(particle);
    }
    
    private static void applyDezoom() {
        final IsoWorldScene scene = CameraFunctionsLibrary.getInstance().getWorldScene();
        if (scene != null) {
            scene.setDesiredZoomFactor(scene.getIsoCamera().getZoomFactorMin());
        }
    }
    
    @Override
    public boolean cancel(final boolean fromServer, final boolean sendMessage) {
        this.resetActor(this.m_user.getActor());
        this.applyParticleSystem();
        if (this.m_user == this.m_localPlayer) {
            sendMessage((byte)3);
        }
        return true;
    }
    
    @Override
    public boolean finish() {
        final CharacterActor actor = this.m_user.getActor();
        this.resetActor(actor);
        this.applyParticleSystem();
        this.m_user.setCurrentOccupation(null);
        return true;
    }
    
    public void rideOnPet() {
        final PlayerCharacter playerCharacter = (PlayerCharacter)this.m_user;
        final CharacterActor actor = playerCharacter.getActor();
        actor.stopMoving();
        final PetMobileView mountMobile = playerCharacter.getMountMobile();
        this.m_mount = mountMobile.getPet();
        this.applyRideOnActor();
    }
    
    public Anm getAnm() {
        return this.m_anm;
    }
    
    public void applyRideOnActor() {
        final CharacterActor actor = this.m_user.getActor();
        this.applyAnimation();
        final MovementSelector mountMovementSelector = new MountMovementSelector(new MountMovementStyle(this.getMountType()));
        actor.setMovementSelector(mountMovementSelector);
        actor.reloadHmiActions();
    }
    
    public void applyAnimation() {
        final CharacterActor actor = this.m_user.getActor();
        try {
            final String pathForGfx = "petGfxPath";
            final String gfxFile = WakfuConfiguration.getInstance().getString("petGfxPath");
            final String anmFile = String.format(gfxFile, PetHelper.getPetGfx(this.m_user, this.m_mount));
            actor.onAnmLoaded(this.m_anm = actor.applyAllPartsFrom(anmFile), new Runnable() {
                @Override
                public void run() {
                    actor.setAnimation(RideOccupation.this.getMountType().getAnimPlayerStatic());
                }
            });
        }
        catch (PropertyException e) {
            RideOccupation.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    MountType getMountType() {
        return this.m_mount.getDefinition().getMountType();
    }
    
    private static void sendMessage(final byte modificationType) {
        final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
        netMsg.setModificationType(modificationType);
        netMsg.setOccupationType((short)14);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
    }
    
    public void resetActor(final CharacterActor actor) {
        if (this.m_anm != null) {
            actor.unapplyAllPartsFrom(this.m_anm);
            this.m_anm = null;
        }
        actor.setAnimation("AnimStatique");
        actor.setStaticAnimationKey("AnimStatique");
        actor.setMovementSelector(SimpleMovementSelector.getInstance());
        actor.reloadHmiActions();
    }
    
    @Override
    public String toString() {
        return "RideOccupation{m_user=" + this.m_user + ", m_mount=" + this.m_mount + ", m_anm=" + this.m_anm.getFileName() + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)RideOccupation.class);
    }
}
