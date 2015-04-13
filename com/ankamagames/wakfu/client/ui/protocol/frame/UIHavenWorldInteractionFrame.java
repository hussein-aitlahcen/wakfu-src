package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.room.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.world.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public class UIHavenWorldInteractionFrame implements MessageFrame, UIInteractionFrame
{
    private static final String LAYER_NAME = "destinationPosition";
    private static final float[] OK_COLOR;
    private static final float[] INVALID_COLOR;
    private static final Logger m_logger;
    private static final UIHavenWorldInteractionFrame m_instance;
    private final LongLightWeightSet m_blockedCellsAltitude;
    private DisplayedScreenElement m_oldElem;
    private RoomContentResult m_cellResult;
    private ItemizableInfo m_element;
    private Runnable m_onValidate;
    private Runnable m_onCancel;
    
    public static UIHavenWorldInteractionFrame getInstance() {
        return UIHavenWorldInteractionFrame.m_instance;
    }
    
    private UIHavenWorldInteractionFrame() {
        super();
        this.m_blockedCellsAltitude = new LongLightWeightSet();
    }
    
    @Override
    public void setElement(final ItemizableInfo element) {
        this.m_element = element;
    }
    
    @Override
    public void setOnCancel(final Runnable onCancel) {
        this.m_onCancel = onCancel;
    }
    
    @Override
    public void setOnValidate(final Runnable onValidate) {
        this.m_onValidate = onValidate;
    }
    
    @Override
    public RoomContentResult getCellResult() {
        return this.m_cellResult;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19994:
            case 19995: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                final DisplayedScreenElement displayedElement = getElementUnderMouse(msg.getMouseX(), msg.getMouseY());
                if (displayedElement != this.m_oldElem) {
                    this.selectCell(displayedElement);
                }
                return false;
            }
            case 19992: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                if (!msg.isButtonLeft()) {
                    this.m_cellResult = RoomContentResult.USER_CANCEL;
                    UIHavenWorldInteractionFrame.m_logger.info((Object)"Bag-action cancelled");
                    this.m_onCancel.run();
                }
                else if (this.m_cellResult == RoomContentResult.OK) {
                    WakfuSoundManager.getInstance().playGUISound(600071L);
                    UIHavenWorldInteractionFrame.m_logger.info((Object)("Bag-action to " + this.m_element.getDragInfo().getDragPoint() + " validated"));
                    this.m_blockedCellsAltitude.add(getCellId(this.m_oldElem.getWorldCellX(), this.m_oldElem.getWorldCellY(), this.m_oldElem.getWorldCellAltitude()));
                    this.m_onValidate.run();
                }
                else {
                    UIHavenWorldInteractionFrame.m_logger.info((Object)"Bag-action cancelled");
                    this.m_onCancel.run();
                }
                this.m_element = null;
                HighLightManager.getInstance().clearLayer("destinationPosition");
                WakfuGameEntity.getInstance().removeFrame(this);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean selectCell(final DisplayedScreenElement displayedElement) {
        if (displayedElement != null) {
            final ScreenElement element = displayedElement.getElement();
            final int x = element.getCellX();
            final int y = element.getCellY();
            final short z = TopologyMapManager.getNearestZ(x, y, element.getCellZ());
            final short playerZ = WakfuGameEntity.getInstance().getLocalPlayer().getWorldCellAltitude();
            final boolean cellBlocked = !this.acceptIE(x, y, z);
            this.m_cellResult = (cellBlocked ? RoomContentResult.BLOCKED : RoomContentResult.OK);
            final float[] color = cellBlocked ? UIHavenWorldInteractionFrame.INVALID_COLOR : UIHavenWorldInteractionFrame.OK_COLOR;
            if (cellBlocked && Math.abs(element.getCellZ() - playerZ) > 6) {
                this.setUnvalid();
                this.m_oldElem = displayedElement;
                return true;
            }
            if (this.m_element != null) {
                this.m_element.getDragInfo().setDragPoint(x, y, z);
            }
            final HighLightLayer highLightLayer = HighLightManager.getInstance().getLayer("destinationPosition");
            assert highLightLayer != null;
            highLightLayer.clear();
            highLightLayer.add(x, y, z);
            highLightLayer.setColor(color);
        }
        else {
            this.setUnvalid();
        }
        this.m_oldElem = displayedElement;
        return false;
    }
    
    private void setUnvalid() {
        this.m_cellResult = RoomContentResult.BLOCKED;
        this.setElementViewVisible(false);
        HighLightManager.getInstance().clearLayer("destinationPosition");
    }
    
    private void setElementViewVisible(final boolean visible) {
        if (this.m_element == null) {
            return;
        }
        for (final ClientInteractiveElementView view : this.m_element.getViews()) {
            ((WakfuClientInteractiveAnimatedElementSceneView)view).setVisible(visible);
        }
    }
    
    private boolean acceptIE(final int x, final int y, final short z) {
        return z != -32768 && !FightManager.getInstance().hasFightOnPosition(x, y) && !this.m_blockedCellsAltitude.contains(getCellId(x, y, z));
    }
    
    private static DisplayedScreenElement getElementUnderMouse(final int mouseX, final int mouseY) {
        final ArrayList<DisplayedScreenElement> hitElements = WakfuClientInstance.getInstance().getWorldScene().getDisplayedElementsUnderMousePoint(mouseX, mouseY, 0.0f, DisplayedScreenElementComparator.Z_ORDER_DIRECT);
        if (hitElements == null || hitElements.isEmpty()) {
            return null;
        }
        return hitElements.get(0);
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
        if (!isAboutToBeAdded) {
            if (this.m_element != null) {
                this.m_element.getDragInfo().startDrag();
            }
            this.refreshBlockedCellsAltitude();
        }
        try {
            HighLightManager.getInstance().createLayer("destinationPosition");
        }
        catch (Exception e) {
            UIHavenWorldInteractionFrame.m_logger.error((Object)("Exception raised while pushing frame " + this.getClass().getName()), (Throwable)e);
        }
    }
    
    private void refreshBlockedCellsAltitude() {
        this.m_blockedCellsAltitude.clear();
        final List<LocalPartition> partitions = ((AbstractLocalPartitionManager<LocalPartition>)LocalPartitionManager.getInstance()).getAllLocalPartitions();
        for (final LocalPartition partition : partitions) {
            final int startX = partition.getX() * 18;
            final int startY = partition.getY() * 18;
            for (int x = startX; x < startX + partition.getWidth(); ++x) {
                for (int y = startY; y < startY + partition.getHeight(); ++y) {
                    final Resource resource = ResourceManager.getInstance().getResource(x, y);
                    if (resource != null) {
                        this.m_blockedCellsAltitude.add(getCellId(x, y, resource.getWorldCellAltitude()));
                    }
                    else {
                        final TopologyMapInstance map = TopologyMapManager.getMapFromCell(x, y);
                        if (map != null) {
                            for (final short z : TopologyMapManager.getZ(x, y)) {
                                if (z != -32768) {
                                    if (map.isBlocked(x, y, z) || TopologyMapManager.isIESterileOrNotWalkable(x, y, z)) {
                                        this.m_blockedCellsAltitude.add(getCellId(x, y, z));
                                    }
                                }
                            }
                            partition.foreachInteractiveElement(new TObjectProcedure<ClientMapInteractiveElement>() {
                                @Override
                                public boolean execute(final ClientMapInteractiveElement object) {
                                    UIHavenWorldInteractionFrame.this.m_blockedCellsAltitude.add(getCellId(object.getWorldCellX(), object.getWorldCellY(), object.getWorldCellAltitude()));
                                    return true;
                                }
                            });
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        this.m_blockedCellsAltitude.clear();
        if (this.m_element != null) {
            UIHavenWorldInteractionFrame.m_logger.info((Object)"Bag-action cancelled");
            this.m_onCancel.run();
            this.m_element = null;
        }
        this.m_oldElem = null;
        this.m_cellResult = RoomContentResult.USER_CANCEL;
        try {
            this.m_element = null;
            HighLightManager.getInstance().clearLayer("destinationPosition");
            HighLightManager.getInstance().removeLayer("destinationPosition");
        }
        catch (Exception e) {
            UIHavenWorldInteractionFrame.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    private static long getCellId(final int x, final int y, final short z) {
        return PositionValue.toLong(x, y, z);
    }
    
    static {
        OK_COLOR = new float[] { 0.0f, 1.0f, 0.0f, 0.5f };
        INVALID_COLOR = new float[] { 1.0f, 0.0f, 0.0f, 0.5f };
        m_logger = Logger.getLogger((Class)UIHavenWorldInteractionFrame.class);
        m_instance = new UIHavenWorldInteractionFrame();
    }
}
