package com.ankamagames.wakfu.client.alea;

import com.ankamagames.baseImpl.graphics.game.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.baseImpl.graphics.isometric.interpolation.process.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.core.world.river.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import java.util.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import org.jetbrains.annotations.*;
import java.awt.event.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.alea.ambiance.*;

public class WakfuWorldScene extends GameWorldScene
{
    private static final Logger m_logger;
    private final Stack<CustomTextureHighlightingProvider> m_highlightCellProvidersToUpdate;
    private boolean m_dispatchKeyPressedMessage;
    private boolean m_dispatchKeyReleasedMessage;
    private boolean m_dispatchMouseMovedMessage;
    private boolean m_dispatchMouseMovedExtendedMessage;
    private int m_pressedX;
    private int m_pressedY;
    private final TIntHashSet m_mouseButtonDown;
    private final boolean[] m_keyDown;
    private AnimatedInteractiveElement m_nearestElement;
    private final ArrayList<AnimatedInteractiveElement> m_elements;
    private static final Comparator<AnimatedInteractiveElement> ELEMENT_Z_ORDER_COMPARATOR;
    
    public WakfuWorldScene(final AbstractGameClientInstance gameClientInstance, final float minZoom, final float maxZoom) {
        super(gameClientInstance, minZoom, maxZoom);
        this.m_highlightCellProvidersToUpdate = new Stack<CustomTextureHighlightingProvider>();
        this.m_mouseButtonDown = new TIntHashSet();
        this.m_keyDown = new boolean[256];
        this.m_elements = new ArrayList<AnimatedInteractiveElement>();
        this.getIsoCamera().setCameraMovementMode(InterpolationType.SMOOTH_LINEAR);
        this.addRenderProcessHandler(ResourceManager.getInstance());
        this.addRenderProcessHandler(AnimatedElementSceneViewManager.getInstance());
        this.addRenderProcessHandler(SimpleAnimatedElementManager.getInstance());
        this.addRenderProcessHandler(CompassWidget.INSTANCE);
        this.addRenderProcessHandler(WorldPositionMarkerManager.getInstance());
        this.addRenderProcessHandler(SoundPartitionManager.INSTANCE);
        this.getIsoCamera().addClipPlanesUpdatedListener(new ClipPlanesUpdatedListener() {
            @Override
            public void onClipPlanesUpdated() {
                WakfuWorldScene.this.refreshSelectElementUnderMouse(WakfuWorldScene.this.getMouseX(), WakfuWorldScene.this.getMouseY());
            }
        });
    }
    
    public void setDispatchMouseMovedMessage(final boolean dispatchMouseMovedMessage) {
        this.m_dispatchMouseMovedMessage = dispatchMouseMovedMessage;
    }
    
    public void setDispatchKeyPressedMessage(final boolean dispatchKeyPressedMessage) {
        this.m_dispatchKeyPressedMessage = dispatchKeyPressedMessage;
    }
    
    public void setDispatchKeyReleasedMessage(final boolean dispatchKeyReleasedMessage) {
        this.m_dispatchKeyReleasedMessage = dispatchKeyReleasedMessage;
    }
    
    @Override
    public void clean(final boolean forceUpdate) {
        super.clean(forceUpdate);
        this.setMaskHeight(0.0f);
        this.setMaskShow(false);
        StartPointDisplayer.getInstance().clear();
        StaticEffectAreaDisplayer.getInstance().clear();
        ResourceManager.getInstance().clear();
        WakfuSoundManager.getInstance().reset();
        SimpleAnimatedElementManager.getInstance().clear();
        TextureManager.getInstance().releaseTextures();
    }
    
    @Override
    public void process(final int deltaTime) {
        HiddenElementManager.getInstance().update(deltaTime);
        AmbianceManager.INSTANCE.update(deltaTime);
        super.process(deltaTime);
        this.checkProvidersToUpdate();
    }
    
    private static boolean isValidKeyChar(final char keyChar) {
        return keyChar >= '\0' && keyChar < '\u0100';
    }
    
    @Override
    public boolean keyPressed(final KeyEvent keyEvent) {
        if (this.m_dispatchKeyPressedMessage) {
            UIWorldSceneKeyMessage.sendPressed(keyEvent);
        }
        final char keyChar = keyEvent.getKeyChar();
        if (isValidKeyChar(keyChar) && !this.m_keyDown[keyChar]) {
            this.m_keyDown[keyChar] = true;
        }
        return false;
    }
    
    @Override
    public boolean keyReleased(final KeyEvent keyEvent) {
        if (this.m_dispatchKeyReleasedMessage) {
            UIWorldSceneKeyMessage.sendReleased(keyEvent);
        }
        final char keyChar = keyEvent.getKeyChar();
        if (isValidKeyChar(keyChar)) {
            this.m_keyDown[keyChar] = false;
        }
        return false;
    }
    
    @Override
    public boolean keyTyped(final KeyEvent keyEvent) {
        return false;
    }
    
    @Override
    public boolean mouseEntered(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseExited(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mousePressed(final MouseEvent mouseEvent) {
        if (!this.m_mouseButtonDown.contains(mouseEvent.getButton())) {
            this.m_mouseButtonDown.add(mouseEvent.getButton());
        }
        this.m_pressedX = mouseEvent.getX();
        this.m_pressedY = mouseEvent.getY();
        UIWorldSceneMouseMessage.sendPressed(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getButton());
        return false;
    }
    
    @Override
    public boolean mouseReleased(final MouseEvent mouseEvent) {
        if (this.m_mouseButtonDown.contains(mouseEvent.getButton())) {
            this.m_mouseButtonDown.remove(mouseEvent.getButton());
        }
        UIWorldSceneMouseMessage.sendReleased(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getButton());
        final AleaIsoCamera cam = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
        return true;
    }
    
    @Override
    public boolean mouseClicked(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseWheelMoved(final MouseWheelEvent mouseEvent) {
        if (!UIChatFrame.getInstance().isOnChatWindow()) {
            this.getIsoCamera().modifyDesiredZoomFactor(mouseEvent.getWheelRotation() * 0.1f);
        }
        return false;
    }
    
    @Override
    public boolean mouseDragged(final MouseEvent mouseEvent) {
        super.mouseDragged(mouseEvent);
        if (!this.m_dispatchMouseMovedExtendedMessage) {
            return false;
        }
        final int dx = mouseEvent.getX() - this.m_pressedX;
        final int dy = mouseEvent.getY() - this.m_pressedY;
        if (dx * dx + dy * dy < 200) {
            return true;
        }
        UIWorldSceneMouseMessage.sendMoveExtended(mouseEvent.getX(), mouseEvent.getY());
        return false;
    }
    
    @Override
    public boolean mouseMoved(final MouseEvent mouseEvent) {
        super.mouseMoved(mouseEvent);
        this.refreshSelectElementUnderMouse(mouseEvent.getX(), mouseEvent.getY());
        return false;
    }
    
    private boolean hasButtonDown() {
        return !this.m_mouseButtonDown.isEmpty();
    }
    
    private void refreshSelectElementUnderMouse(final int mouseX, final int mouseY) {
        this.m_nearestElement = this.selectNearestElement(mouseX, mouseY);
        if (!this.hasButtonDown()) {
            if (this.m_dispatchMouseMovedMessage) {
                UIWorldSceneMouseMessage.sendMove(mouseX, mouseY);
            }
        }
        else if (this.m_dispatchMouseMovedExtendedMessage) {
            UIWorldSceneMouseMessage.sendMoveExtended(mouseX, mouseY);
        }
    }
    
    public AnimatedInteractiveElement selectNearestElement(final int mouseX, final int mouseY) {
        this.m_elements.clear();
        final float adjustedMouseX = this.getAdjustedMouseX(mouseX);
        final float adjustedMouseY = this.getAdjustedMouseY(mouseY);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final int currentFightId = (localPlayer == null) ? -1 : localPlayer.getCurrentFightId();
        final ArrayList<Mobile> mobiles = MobileManager.getInstance().getElementUnderMousePoint(adjustedMouseX, adjustedMouseY);
        for (final Mobile m : mobiles) {
            if (m != null) {
                final boolean fightCase = m.getCurrentFightId() == currentFightId;
                if (!fightCase || !m.isHighlightable()) {
                    continue;
                }
                this.m_elements.add(m);
            }
        }
        final ClientInteractiveAnimatedElementSceneView nearestinteractiveElement = AnimatedElementSceneViewManager.getInstance().getNearestElement(adjustedMouseX, adjustedMouseY);
        if (nearestinteractiveElement != null) {
            this.m_elements.add(nearestinteractiveElement);
        }
        final AnimatedElement nearestSimpleAnimatedElement = SimpleAnimatedElementManager.getInstance().getNearestElement(adjustedMouseX, adjustedMouseY);
        if (nearestSimpleAnimatedElement != null && nearestSimpleAnimatedElement instanceof AnimatedInteractiveElement) {
            this.m_elements.add((AnimatedInteractiveElement)nearestSimpleAnimatedElement);
        }
        if (currentFightId == -1) {
            final Resource nearestResource = ResourceManager.getInstance().getNearestElement(adjustedMouseX, adjustedMouseY);
            if (nearestResource != null) {
                this.m_elements.add(nearestResource);
            }
        }
        Collections.sort(this.m_elements, WakfuWorldScene.ELEMENT_Z_ORDER_COMPARATOR);
        AnimatedInteractiveElement elementSelected = null;
        if (!this.m_elements.isEmpty()) {
            elementSelected = this.m_elements.get(0);
        }
        MobileManager.getInstance().unselectAllElementExceptThis(elementSelected);
        ResourceManager.getInstance().unselectAllElementExceptThis(elementSelected);
        AnimatedElementSceneViewManager.getInstance().unselectAllElementExceptThis(elementSelected);
        SimpleAnimatedElementManager.getInstance().unselectAllElementExceptThis(elementSelected);
        if (elementSelected != null && elementSelected.isHighlightable()) {
            elementSelected.setSelected(true);
        }
        return elementSelected;
    }
    
    @NotNull
    public ArrayList<AnimatedInteractiveElement> selectAllNearestElement(final int mouseX, final int mouseY) {
        final ArrayList<AnimatedInteractiveElement> elements = new ArrayList<AnimatedInteractiveElement>();
        final float adjustedMouseX = this.getAdjustedMouseX(mouseX);
        final float adjustedMouseY = this.getAdjustedMouseY(mouseY);
        boolean localPlayerInFight = false;
        if (WakfuGameEntity.getInstance().getLocalPlayer() != null) {
            localPlayerInFight = (WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight() != null);
        }
        final ArrayList<Mobile> nearestMobile = MobileManager.getInstance().getElementUnderMousePoint(adjustedMouseX, adjustedMouseY);
        if (nearestMobile != null) {
            elements.addAll(nearestMobile);
        }
        if (!localPlayerInFight) {
            final ArrayList<ClientInteractiveAnimatedElementSceneView> nearestAnimatedElement = AnimatedElementSceneViewManager.getInstance().getElementUnderMousePoint(adjustedMouseX, adjustedMouseY);
            if (nearestAnimatedElement != null) {
                elements.addAll(nearestAnimatedElement);
            }
            final ArrayList<Resource> nearestResource = ResourceManager.getInstance().getElementUnderMousePoint(adjustedMouseX, adjustedMouseY);
            if (nearestResource != null) {
                elements.addAll(nearestResource);
            }
            final ArrayList<AnimatedInteractiveElement> fightRepresentation = FightVisibilityManager.getInstance().getFightRepresentationsUnderMousePoint(adjustedMouseX, adjustedMouseY);
            if (fightRepresentation != null) {
                elements.addAll(fightRepresentation);
            }
        }
        Collections.sort(elements, WakfuWorldScene.ELEMENT_Z_ORDER_COMPARATOR);
        return elements;
    }
    
    @Override
    public boolean focusGained(final FocusEvent e) {
        return false;
    }
    
    @Override
    public boolean focusLost(final FocusEvent e) {
        return false;
    }
    
    public void addHighlightCellProvidersToUpdate(final CustomTextureHighlightingProvider provider) {
        this.m_highlightCellProvidersToUpdate.push(provider);
    }
    
    public void checkProvidersToUpdate() {
        while (!this.m_highlightCellProvidersToUpdate.empty()) {
            final CustomTextureHighlightingProvider provider = this.m_highlightCellProvidersToUpdate.pop();
            provider.update();
        }
    }
    
    public AnimatedInteractiveElement getNearestElement() {
        return this.m_nearestElement;
    }
    
    public boolean isKeyDown(final int index) {
        assert isValidKeyChar((char)index);
        return this.m_keyDown[index];
    }
    
    public boolean isButtonDown(final int index) {
        return this.m_mouseButtonDown.contains(index);
    }
    
    public void setDispatchMouseMovedExtendedMessage(final boolean dispatchMouseMovedExtendedMessage) {
        this.m_dispatchMouseMovedExtendedMessage = dispatchMouseMovedExtendedMessage;
    }
    
    @Override
    protected void onInGroupChanged(final boolean inGroup) {
        super.onInGroupChanged(inGroup);
        WakfuAmbianceListener.getInstance().displayGroupName(inGroup);
    }
    
    @Override
    protected AleaWorldScene createNew(final RenderTreeInterface renderTreeCopy, final float minZoom, final float maxZoom) {
        final WakfuWorldScene scene = new WakfuWorldScene(this.m_gameClientInstance, minZoom, maxZoom);
        scene.m_renderTree = renderTreeCopy;
        return scene;
    }
    
    @Override
    protected void cleanManagers() {
        super.cleanManagers();
        AmbianceManagerFake.INSTANCE.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuWorldScene.class);
        ELEMENT_Z_ORDER_COMPARATOR = new Comparator<AnimatedInteractiveElement>() {
            @Override
            public int compare(final AnimatedInteractiveElement o1, final AnimatedInteractiveElement o2) {
                if (o1.getZOrder() < o2.getZOrder()) {
                    return 1;
                }
                if (o1.getZOrder() > o2.getZOrder()) {
                    return -1;
                }
                return 1;
            }
        };
    }
}
