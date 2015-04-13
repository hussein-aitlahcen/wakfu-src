package com.ankamagames.wakfu.client.ui.protocol.frame.helpers;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.constraint.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.targetComputer.*;
import com.ankamagames.baseImpl.graphics.isometric.interpolation.process.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;

public class CameraMouseMove
{
    private static final Logger m_logger;
    private static boolean m_forceActivate;
    private MouseFollower m_mouseFollower;
    private CameraConstraint m_cameraConstraint;
    private final ArrayList<Listener> m_listeners;
    public static final CameraMouseMove INSTANCE;
    
    private CameraMouseMove() {
        super();
        this.m_listeners = new ArrayList<Listener>(1);
    }
    
    public void setCameraConstraint(final CameraConstraint cameraConstraint) {
        this.m_cameraConstraint = cameraConstraint;
        if (this.m_mouseFollower != null) {
            this.m_mouseFollower.setCameraConstraint(cameraConstraint);
        }
    }
    
    public void addListener(final Listener listener) {
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public void removeAllListener() {
        this.m_listeners.clear();
    }
    
    public void removeListener(final Listener listener) {
        this.m_listeners.remove(listener);
    }
    
    public boolean onMessage(final Message message) {
        if (!canMoveCamera()) {
            if (this.m_mouseFollower != null) {
                this.reset();
            }
            return true;
        }
        switch (message.getId()) {
            case 19995: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                if (this.m_mouseFollower != null) {
                    this.m_mouseFollower.setLocation(msg.getMouseX(), msg.getMouseY());
                }
                else {
                    this.start(msg.getMouseX(), msg.getMouseY());
                }
                return false;
            }
            case 19992: {
                this.reset();
                return true;
            }
            default: {
                return true;
            }
        }
    }
    
    private static boolean canMoveCamera() {
        return UIWorldSceneMouseMessage.isRightButtonDown() || CameraMouseMove.m_forceActivate;
    }
    
    public static boolean isForceActivate() {
        return CameraMouseMove.m_forceActivate;
    }
    
    public void forceStop() {
        this.reset();
        this.m_listeners.clear();
    }
    
    private void reset() {
        final AleaIsoCamera isoCamera = this.getAleaIsoCamera();
        isoCamera.setTargetComputer(new TargetTracker());
        isoCamera.setCameraMovementMode(InterpolationType.SMOOTH_LINEAR);
        this.m_mouseFollower = null;
        CameraMouseMove.m_forceActivate = false;
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).onStop();
        }
    }
    
    private void start(final int mouseX, final int mouseY) {
        final AleaIsoCamera isoCamera = this.getAleaIsoCamera();
        (this.m_mouseFollower = new MouseFollower(isoCamera, mouseX, mouseY)).setCameraConstraint(this.m_cameraConstraint);
        isoCamera.setTargetComputer(this.m_mouseFollower);
        isoCamera.setCameraMovementMode(InterpolationType.SMOOTH_LINEAR);
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).onStart();
        }
    }
    
    private AleaIsoCamera getAleaIsoCamera() {
        final WakfuWorldScene worldScene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
        return worldScene.getIsoCamera();
    }
    
    public static void toggleActivation() {
        CameraMouseMove.m_forceActivate = !CameraMouseMove.m_forceActivate;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CameraMouseMove.class);
        CameraMouseMove.m_forceActivate = false;
        INSTANCE = new CameraMouseMove();
    }
    
    public interface Listener
    {
        void onStart();
        
        void onStop();
    }
}
