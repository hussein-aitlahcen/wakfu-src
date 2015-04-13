package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.cellSelector.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.utils.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class UIKillMonsterInteractionFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static final UIKillMonsterInteractionFrame m_instance;
    private static final float[] m_excellentColor;
    private ElementSelection m_elementSelection;
    private Item m_item;
    private Point3 m_lastTarget;
    
    public static UIKillMonsterInteractionFrame getInstance() {
        return UIKillMonsterInteractionFrame.m_instance;
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    public void setItem(final Item item) {
        this.m_item = item;
    }
    
    public void selectRange() {
        CursorFactory.getInstance().show(CursorFactory.CursorType.CUSTOM16, true);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_elementSelection = new ElementSelection("actionRange", UIKillMonsterInteractionFrame.m_excellentColor);
            final AleaWorldSceneWithParallax scene = WakfuClientInstance.getInstance().getWorldScene();
            final Point3 target = getNearestPoint3(scene.getMouseX(), scene.getMouseY());
            this.refreshCursorDisplay(target, false);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_elementSelection.clear();
            CursorFactory.getInstance().unlock();
        }
        this.m_item = null;
        this.m_lastTarget = null;
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19994: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                final Point3 target = getNearestPoint3(msg.getMouseX(), msg.getMouseY());
                this.refreshCursorDisplay(target, false);
                return false;
            }
            case 19992: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                if (msg.isButtonLeft()) {
                    final Point3 target = getNearestPoint3(msg.getMouseX(), msg.getMouseY());
                    this.sendItemActionRequest(target.getX(), target.getY());
                }
                else {
                    WakfuGameEntity.getInstance().removeFrame(this);
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    protected void refreshCursorDisplay(final Point3 target, final boolean force) {
        if (target == null) {
            this.m_elementSelection.clear();
            this.m_lastTarget = null;
            return;
        }
        if (target.equals(this.m_lastTarget) && !force) {
            return;
        }
        this.m_elementSelection.clear();
        this.m_lastTarget = target;
        final int validity = this.checkValidity(target);
        this.m_elementSelection.setColor(UIKillMonsterInteractionFrame.m_excellentColor);
        this.addToSelection(target);
    }
    
    private static Point3 getNearestPoint3(final int mouseX, final int mouseY) {
        final Point3 target = WorldSceneInteractionUtils.getNearestPoint3(WakfuClientInstance.getInstance().getWorldScene(), mouseX, mouseY, false);
        if (target == null) {
            return null;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        TopologyMapManager.setMoverCaracteristics(localPlayer.getHeight(), localPlayer.getPhysicalRadius(), localPlayer.getJumpCapacity());
        final short nearestWalkableZ = TopologyMapManager.getPossibleNearestWalkableZ(target.getX(), target.getY(), target.getZ());
        if (nearestWalkableZ == -32768 || Math.abs(target.getZ() - nearestWalkableZ) > 1) {
            return null;
        }
        target.setZ(nearestWalkableZ);
        return target;
    }
    
    protected void addToSelection(final Point3 target) {
        final KillMonstersInRadiusItemAction action = (KillMonstersInRadiusItemAction)this.m_item.getReferenceItem().getItemAction();
        final int radius = action.getRadius();
        this.m_elementSelection.add(target.getX(), target.getY(), target.getZ());
        for (int x = target.getX() - radius, endX = target.getX() + radius; x <= endX; ++x) {
            for (int y = target.getY() - radius, endY = target.getY() + radius; y <= endY; ++y) {
                if (MathHelper.pow2(x - target.getX()) + MathHelper.pow2(y - target.getY()) <= MathHelper.pow2(radius)) {
                    this.m_elementSelection.add(x, y, target.getZ());
                }
            }
        }
    }
    
    protected void refreshParticles() {
    }
    
    protected void sendItemActionRequest(final int seedPositionX, final int seedPositionY) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final KillMonstersInRadiusItemAction itemAction = (KillMonstersInRadiusItemAction)this.m_item.getReferenceItem().getItemAction();
        itemAction.setCastPosition(new Point3(seedPositionX, seedPositionY, localPlayer.getWorldCellAltitude()));
        itemAction.sendRequest(this.m_item, seedPositionX, seedPositionY);
    }
    
    protected int checkValidity(final Point3 target) {
        if (this.m_item == null) {
            return -1;
        }
        return 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIKillMonsterInteractionFrame.class);
        m_instance = new UIKillMonsterInteractionFrame();
        m_excellentColor = new float[] { 0.0f, 1.0f, 0.0f, 0.6f };
    }
}
