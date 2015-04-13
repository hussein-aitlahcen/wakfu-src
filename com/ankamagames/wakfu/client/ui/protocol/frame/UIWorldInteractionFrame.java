package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.game.embeddedTutorial.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.constraint.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class UIWorldInteractionFrame implements MessageFrame
{
    private static final boolean DEBUG_MODE = false;
    private long m_lastMovementTime;
    private static final Logger m_logger;
    private static final UIWorldInteractionFrame m_instance;
    private final CameraMouseMove m_cameraMouseMove;
    private int m_absoluteMousePositionX;
    private int m_absoluteMousePositionY;
    private final List<MouseReleasedListener> m_mouseReleasedListener;
    private FreeParticleSystem m_feedbackAPS;
    
    private UIWorldInteractionFrame() {
        super();
        this.m_cameraMouseMove = CameraMouseMove.INSTANCE;
        this.m_mouseReleasedListener = new ArrayList<MouseReleasedListener>();
    }
    
    public static UIWorldInteractionFrame getInstance() {
        return UIWorldInteractionFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final WakfuWorldScene worldScene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 19994: {
                return this.onMouseMove((UIWorldSceneMouseMessage)message, worldScene);
            }
            case 19995: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                return this.onMouseDragged(msg, localPlayer);
            }
            case 19998: {
                this.m_lastMovementTime = System.currentTimeMillis();
                return false;
            }
            case 19992: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                return this.onMouseReleased(msg, worldScene, localPlayer);
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean onMouseReleased(final UIWorldSceneMouseMessage message, final WakfuWorldScene worldScene, final LocalPlayerCharacter localPlayer) {
        if (localPlayer.isWaitingForResult()) {
            return false;
        }
        UIShortcutBarFrame.getInstance().hideAllWidgets();
        final int x = message.getMouseX();
        final int y = message.getMouseY();
        for (int i = this.m_mouseReleasedListener.size() - 1; i >= 0; --i) {
            this.m_mouseReleasedListener.get(i).run(null);
        }
        if (!localPlayer.canMoveAndInteract()) {
            return false;
        }
        if (message.isButtonLeft() && MRUDirectRunnerManager.INSTANCE.isRunning()) {
            MRUDirectRunnerManager.INSTANCE.onMouseClick(worldScene, x, y);
            return false;
        }
        MRUDirectRunnerManager.INSTANCE.stop();
        if (message.isButtonLeft()) {
            final ArrayList<AnimatedInteractiveElement> displayedElementsMouseOver = worldScene.selectAllNearestElement(x, y);
            AnimatedInteractiveElement firstElement = null;
            if (!displayedElementsMouseOver.isEmpty()) {
                firstElement = displayedElementsMouseOver.get(0);
            }
            if (firstElement == null) {
                this.moveLocalPlayer(localPlayer, x, y);
                return false;
            }
            if (firstElement instanceof Resource) {
                this.moveLocalPlayer(localPlayer, message.getMouseX(), message.getMouseY());
                return false;
            }
            if (!(firstElement instanceof MRUable) && !(firstElement instanceof CharacterActor)) {
                localPlayer.moveTo(firstElement.getWorldCoordinates(), false, true);
                return false;
            }
            EmbeddedTutorialManager.getInstance().launchTutorial(TutorialEvent.RIGHT_CLICK, null);
        }
        if (localPlayer.isTemporaryTransferInventoryActive()) {
            return false;
        }
        if (message.isButtonRight() || (WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.INTERACTION_ON_LEFT_CLICK_KEY) && message.isButtonLeft())) {
            UIMRUFrame.getInstance().checkForMRUable(worldScene, x, y);
        }
        return false;
    }
    
    private boolean onMouseDragged(final UIWorldSceneMouseMessage message, final LocalPlayerCharacter localPlayer) {
        if (UIWorldSceneMouseMessage.isLeftButtonDown()) {
            if (localPlayer.isWaitingForResult() || localPlayer.isTemporaryTransferInventoryActive() || !localPlayer.canMoveAndInteract()) {
                return true;
            }
            final long time = System.currentTimeMillis();
            if (time - this.m_lastMovementTime > 500L) {
                this.moveLocalPlayer(localPlayer, message.getMouseX(), message.getMouseY());
                this.m_lastMovementTime = time;
            }
        }
        return false;
    }
    
    private boolean onMouseMove(final UIWorldSceneMouseMessage message, final WakfuWorldScene worldScene) {
        final Widget widget = MasterRootContainer.getInstance().getMouseOver();
        if (widget != null && widget != MasterRootContainer.getInstance()) {
            return true;
        }
        final int mouseX = message.getMouseX();
        final int mouseY = message.getMouseY();
        if (MRUDirectRunnerManager.INSTANCE.isRunning()) {
            MRUDirectRunnerManager.INSTANCE.onMouseMove(worldScene, mouseX, mouseY);
        }
        final ArrayList<AnimatedInteractiveElement> elements = worldScene.selectAllNearestElement(mouseX, mouseY);
        if (elements.isEmpty()) {
            CursorFactory.getInstance().show(CursorFactory.CursorType.DEFAULT, false);
            return true;
        }
        boolean found = false;
        for (int i = 0; i < elements.size() && !found; ++i) {
            final AnimatedInteractiveElement elem = elements.get(i);
            final CursorFactory.CursorType rightClickCursorType = CursorFactory.CursorType.CUSTOM7;
            if (elem instanceof CharacterActor) {
                final CharacterActor characterActor = (CharacterActor)elem;
                final CharacterInfo character = characterActor.getCharacterInfo();
                if (character != null) {
                    final AbstractMRUAction[] mruActions = character.getMRUActions();
                    if (mruActions != null && mruActions.length > 0) {
                        for (int j = 0; j < mruActions.length; ++j) {
                            final AbstractMRUAction mruAction = mruActions[j];
                            if (mruAction != null) {
                                mruAction.initFromSource(character);
                                if (mruAction != null && mruAction.isUsable() && mruAction.isRunnable()) {
                                    if (CursorFactory.getInstance().getType() != rightClickCursorType) {
                                        CursorFactory.getInstance().show(rightClickCursorType, false);
                                    }
                                    found = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else if (elem instanceof MRUable) {
                CursorFactory.getInstance().show(rightClickCursorType, false);
                found = true;
            }
            else if (elem instanceof Resource) {
                if (((Resource)elem).getMRUSkillActions().length > 0) {
                    CursorFactory.getInstance().show(rightClickCursorType, false);
                }
                else {
                    CursorFactory.getInstance().show(CursorFactory.CursorType.DEFAULT);
                }
                found = true;
            }
            else if (elem instanceof WakfuClientInteractiveAnimatedElementSceneView) {
                final ClientInteractiveElementView element = (ClientInteractiveElementView)elem;
                final ClientMapInteractiveElement mie = element.getInteractiveElement();
                if (mie instanceof MRUable) {
                    final AbstractMRUAction[] actions = ((MRUable)mie).getMRUActions();
                    if (mie.isUsable() && actions != null && actions.length != 0) {
                        CursorFactory.getInstance().show(rightClickCursorType, false);
                    }
                    else {
                        CursorFactory.getInstance().show(CursorFactory.CursorType.DEFAULT, false);
                    }
                }
                found = true;
            }
        }
        if (!found) {
            CursorFactory.getInstance().show(CursorFactory.CursorType.DEFAULT, false);
        }
        return false;
    }
    
    private void moveLocalPlayer(final LocalPlayerCharacter localPlayer, final int mouseX, final int mouseY) {
        final AbstractOccupation currentOccupation = localPlayer.getCurrentOccupation();
        if (!localPlayer.canMoveAndInteract() || (currentOccupation != null && !currentOccupation.onPlayerMove())) {
            return;
        }
        if (localPlayer.isTemporaryTransferInventoryActive()) {
            UITemporaryInventoryFrame.getInstance().askForTemporaryInventoryDestruction();
            return;
        }
        this.m_absoluteMousePositionX = mouseX;
        this.m_absoluteMousePositionY = mouseY;
        this.moveToFistElement(localPlayer, true);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        final WakfuWorldScene scene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
        if (!isAboutToBeAdded) {
            scene.setDispatchMouseMovedMessage(true);
            scene.setDispatchMouseMovedExtendedMessage(true);
        }
        final AnimatedElement target = getCenterCameraOnCharacter();
        this.m_cameraMouseMove.removeAllListener();
        this.m_cameraMouseMove.setCameraConstraint(new WorldCameraConstraint(target, scene.getIsoCamera()));
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            CursorFactory.getInstance().unlock();
        }
        this.m_cameraMouseMove.forceStop();
    }
    
    private void moveToFistElement(final LocalPlayerCharacter localPlayer, final boolean movementFeedBack) {
        final ArrayList<DisplayedScreenElement> hitElements = WakfuClientInstance.getInstance().getWorldScene().getDisplayedElementsUnderMousePoint(this.m_absoluteMousePositionX, this.m_absoluteMousePositionY, localPlayer.getAltitude(), DisplayedScreenElementComparator.Z_ORDER, 0.0f, 0.0f);
        if (hitElements == null || hitElements.isEmpty()) {
            return;
        }
        for (int i = 0; i < hitElements.size(); ++i) {
            final DisplayedScreenElement element = hitElements.get(i);
            final int x = element.getWorldCellX();
            final int y = element.getWorldCellY();
            final short z = element.getWorldCellAltitude();
            if (localPlayer.getPositionConst().equals(x, y, z)) {
                if (movementFeedBack) {
                    this.displayMovementFeedback(x, y, z);
                }
                return;
            }
            if (localPlayer.moveTo(x, y, z, false, true)) {
                if (movementFeedBack) {
                    this.displayMovementFeedback(x, y, z);
                }
                return;
            }
        }
    }
    
    private void displayMovementFeedback(final int x, final int y, final short z) {
        if (this.m_feedbackAPS != null && this.m_feedbackAPS.isAlive() && this.m_feedbackAPS.getWorldCellX() == x && this.m_feedbackAPS.getWorldCellY() == y && this.m_feedbackAPS.getWorldCellAltitude() == z) {
            return;
        }
        final FreeParticleSystem particleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800185);
        particleSystem.setPosition(x, y, z);
        IsoParticleSystemManager.getInstance().addParticleSystem(particleSystem);
        this.m_feedbackAPS = particleSystem;
    }
    
    private static AnimatedElement getCenterCameraOnCharacter() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        assert localPlayer != null : "Local player null !!! WTF ?";
        return localPlayer.getActor();
    }
    
    public static PathFindResult getPathForModel(final Actor localActor, final ClientMapInteractiveElement model) {
        final List<Point3> approchPoint = model.getApproachPoints();
        if (approchPoint.isEmpty()) {
            return localActor.getPathResult(model.getWorldCellX(), model.getWorldCellY(), model.getWorldCellAltitude(), false, true);
        }
        final ArrayList<Point3> approchPointOrdered = new ArrayList<Point3>();
        final ArrayList<Byte> distances = new ArrayList<Byte>();
        final ArrayList<PathFindResult> paths = new ArrayList<PathFindResult>();
        for (final Point3 anApprochPoint : approchPoint) {
            int k = 0;
            final Point3 worldCoordinates = localActor.getWorldCoordinates();
            byte distance;
            for (distance = (byte)Math.max(Math.abs(worldCoordinates.getX() - anApprochPoint.getX()), Math.abs(worldCoordinates.getY() - anApprochPoint.getY())); k < distances.size() && distances.get(k) < distance; ++k) {}
            approchPointOrdered.add(k, anApprochPoint);
            distances.add(k, distance);
        }
        PathFindResult resultPath = null;
        for (int j = 0; j < approchPointOrdered.size(); ++j) {
            paths.add(j, localActor.getPathResult(approchPointOrdered.get(j), false, true));
            if (paths.get(j).isPathFound()) {
                if (paths.get(j).getPathLength() <= 1.6 * distances.get(j)) {
                    resultPath = paths.get(j);
                    break;
                }
                if (resultPath == null || resultPath.getPathLength() >= paths.get(j).getPathLength()) {
                    resultPath = paths.get(j);
                }
            }
        }
        if (resultPath != null && model.hasToFinishOnIE() && !model.isBlockingMovements()) {
            final PathFindResult tempPath = new PathFindResult(resultPath.getPathLength() + 1);
            for (int i = 0; i < resultPath.getPathLength(); ++i) {
                tempPath.setStep(i, resultPath.getPathStep(i));
            }
            tempPath.setStep(resultPath.getPathLength(), model.getWorldCellX(), model.getWorldCellY(), model.getWorldCellAltitude());
            resultPath = tempPath;
        }
        return resultPath;
    }
    
    public void addMouseReleasedListener(final MouseReleasedListener mouseListener) {
        this.m_mouseReleasedListener.add(mouseListener);
    }
    
    public void removeMouseReleasedListener(final MouseReleasedListener mouseReleasedListener) {
        this.m_mouseReleasedListener.remove(mouseReleasedListener);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIWorldInteractionFrame.class);
        m_instance = new UIWorldInteractionFrame();
    }
}
