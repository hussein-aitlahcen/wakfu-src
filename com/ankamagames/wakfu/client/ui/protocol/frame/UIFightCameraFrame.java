package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.constraint.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public final class UIFightCameraFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static final boolean DEBUG_MODE = false;
    private static final UIFightCameraFrame m_instance;
    private CameraMode m_cameraMode;
    private CharacterInfo m_currentFighter;
    private IsoWorldTarget m_targetBeforeFight;
    private final CameraMouseMove m_cameraMouseMove;
    
    public static UIFightCameraFrame getInstance() {
        return UIFightCameraFrame.m_instance;
    }
    
    private UIFightCameraFrame() {
        super();
        this.m_cameraMode = CameraMode.CENTER_ON_ACTIVE_CHARACTER;
        this.m_cameraMouseMove = CameraMouseMove.INSTANCE;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        this.reset();
        final AleaIsoCamera isoCamera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
        this.m_targetBeforeFight = isoCamera.getTrackingTarget();
        isoCamera.lockMaskKey(true);
        this.setCameraMode(CameraMode.PLACEMENT);
        this.m_cameraMouseMove.setCameraConstraint(this.createCameraConstraint(isoCamera));
        this.m_cameraMouseMove.addListener(new CameraMouseMove.Listener() {
            @Override
            public void onStart() {
                UIFightCameraFrame.this.setCameraMode(CameraMode.DRAGGING);
                UIFightMovementFrame.getInstance().disable();
            }
            
            @Override
            public void onStop() {
                UIFightCameraFrame.this.resetCameraModeToDefault();
                UIFightCameraFrame.this.recenterCamera();
                UIFightMovementFrame.getInstance().enable();
            }
        });
    }
    
    private CameraConstraint createCameraConstraint(final AleaIsoCamera isoCamera) {
        final Fight fight = UIFightFrame.m_fight;
        if (fight == null || fight.getFightMap() == null) {
            return new WorldCameraConstraint(WakfuGameEntity.getInstance().getLocalPlayer().getActor(), isoCamera);
        }
        return new FightCameraConstraint(fight.getFightMap(), isoCamera);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        this.reset();
        final AleaIsoCamera isoCamera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
        isoCamera.lockMaskKey(false);
        if (this.m_targetBeforeFight == null) {
            isoCamera.setTrackingTarget(WakfuGameEntity.getInstance().getLocalPlayer().getActor());
        }
        else {
            isoCamera.setTrackingTarget(this.m_targetBeforeFight);
        }
        this.m_cameraMouseMove.forceStop();
    }
    
    private void reset() {
        this.m_currentFighter = null;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (!this.m_cameraMouseMove.onMessage(message)) {
            return false;
        }
        UIFightMovementFrame.getInstance().enable();
        return true;
    }
    
    private void setCameraMode(final CameraMode mode) {
        assert mode != null;
        if (this.m_cameraMode != mode) {
            this.m_cameraMode = mode;
            this.recenterCamera();
        }
    }
    
    private void resetCameraModeToDefault() {
        this.setCameraMode(CameraMode.INTELLIGENT);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    private MultipleSubTargetsCamera createFightCameraTarget() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Fight fight = localPlayer.getCurrentOrObservedFight();
        final MultipleSubTargetsCamera cameraTarget = new MultipleSubTargetsCamera(localPlayer.getActor(), (fight == null) ? null : fight.getFightMap());
        if (fight == null) {
            cameraTarget.addTarget(localPlayer.getActor(), 1);
            return cameraTarget;
        }
        final int fightersCount = fight.getFighters().size();
        for (final CharacterInfo currentFighter : localPlayer.getCurrentOrObservedFight().getFighters()) {
            if (currentFighter.isInvisibleForLocalPlayer()) {
                continue;
            }
            final int p = (currentFighter instanceof NonPlayerCharacter) ? 10 : 12;
            if (this.m_currentFighter != null && currentFighter == this.m_currentFighter) {
                cameraTarget.addTarget(currentFighter.getActor(), Math.max(p, p * fightersCount));
            }
            else {
                cameraTarget.addTarget(currentFighter.getActor(), p);
            }
        }
        if (cameraTarget.targetCount() < 1) {
            UIFightCameraFrame.m_logger.error((Object)"Il devrait y avoir au moins 1 target (le localPlayer");
        }
        return cameraTarget;
    }
    
    public void onFighterToPlay(final CharacterInfo fighter) {
        if (this.m_cameraMode != CameraMode.DRAGGING) {
            this.resetCameraModeToDefault();
        }
        this.m_currentFighter = fighter;
        switch (this.m_cameraMode) {
            case INTELLIGENT: {
                this.recenterCamera();
                break;
            }
            case CENTER_ON_ACTIVE_CHARACTER: {
                this.recenterCamera();
                break;
            }
        }
    }
    
    public void onFighterMoved(final CharacterInfo fighter) {
        if (this.m_cameraMode == CameraMode.INTELLIGENT) {
            this.onFighterToPlay(fighter);
        }
    }
    
    public void onNewTableTurn() {
        this.recenterCamera();
    }
    
    private void recenterCamera() {
        final AleaWorldScene scene = WakfuClientInstance.getInstance().getWorldScene();
        switch (this.m_cameraMode) {
            case INTELLIGENT: {
                scene.setCameraTarget(this.createFightCameraTarget());
                break;
            }
            case CENTER_ON_ACTIVE_CHARACTER: {
                if (this.m_currentFighter != null) {
                    scene.setCameraTarget(this.m_currentFighter.getActor());
                    break;
                }
                break;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIFightCameraFrame.class);
        m_instance = new UIFightCameraFrame();
    }
    
    public enum CameraMode
    {
        PLACEMENT, 
        INTELLIGENT, 
        CENTER_ON_ACTIVE_CHARACTER, 
        DRAGGING;
    }
}
