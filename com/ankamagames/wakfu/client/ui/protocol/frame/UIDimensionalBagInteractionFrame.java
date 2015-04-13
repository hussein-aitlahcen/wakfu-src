package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.room.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.world.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public class UIDimensionalBagInteractionFrame implements MessageFrame, UIInteractionFrame
{
    private static final String LAYER_NAME = "destinationPosition";
    private static final float[] OK_COLOR;
    private static final float[] INVALID_COLOR;
    private static final Logger m_logger;
    private static final UIDimensionalBagInteractionFrame m_instance;
    private final TIntObjectHashMap<RoomContentResult> m_validCells;
    private DisplayedScreenElement m_oldElem;
    private RoomContentResult m_cellResult;
    private ItemizableInfo m_element;
    private Runnable m_onValidate;
    private Runnable m_onCancel;
    
    public static UIDimensionalBagInteractionFrame getInstance() {
        return UIDimensionalBagInteractionFrame.m_instance;
    }
    
    private UIDimensionalBagInteractionFrame() {
        super();
        this.m_validCells = new TIntObjectHashMap<RoomContentResult>();
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
            case 19994: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                final ArrayList<DisplayedScreenElement> hitElements = WakfuClientInstance.getInstance().getWorldScene().getDisplayedElementsUnderMousePoint(msg.getMouseX(), msg.getMouseY(), 0.0f, DisplayedScreenElementComparator.CELL_CENTER_DISTANCE);
                final DisplayedScreenElement displayedElement = (hitElements != null && !hitElements.isEmpty()) ? hitElements.get(0) : null;
                if (displayedElement != this.m_oldElem) {
                    this.selectCell(displayedElement);
                }
                return false;
            }
            case 19992: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                if (!msg.isButtonLeft()) {
                    this.m_cellResult = RoomContentResult.USER_CANCEL;
                    UIDimensionalBagInteractionFrame.m_logger.info((Object)"Bag-action cancelled");
                    this.m_onCancel.run();
                }
                else if (this.m_cellResult == RoomContentResult.OK) {
                    WakfuSoundManager.getInstance().playGUISound(600071L);
                    UIDimensionalBagInteractionFrame.m_logger.info((Object)("Bag-action to " + this.m_element.getDragInfo().getDragPoint() + " validated"));
                    this.m_onValidate.run();
                }
                else {
                    UIDimensionalBagInteractionFrame.m_logger.info((Object)"Bag-action cancelled");
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
            final short z = element.getCellZ();
            this.m_cellResult = this.m_validCells.get(getCellId(x, y));
            final float[] color = (this.m_cellResult == RoomContentResult.OK) ? UIDimensionalBagInteractionFrame.OK_COLOR : UIDimensionalBagInteractionFrame.INVALID_COLOR;
            this.m_element.getDragInfo().setDragPoint(x, y, z);
            final HighLightLayer highLightLayer = HighLightManager.getInstance().getLayer("destinationPosition");
            assert highLightLayer != null;
            highLightLayer.clear();
            highLightLayer.add(x, y, z);
            highLightLayer.setColor(color);
        }
        this.m_oldElem = displayedElement;
        return false;
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
            this.m_element.getDragInfo().startDrag();
            this.addValidCells();
        }
        try {
            HighLightManager.getInstance().createLayer("destinationPosition");
        }
        catch (Exception e) {
            UIDimensionalBagInteractionFrame.m_logger.error((Object)("Exception raised while pushing frame " + this.getClass().getName()), (Throwable)e);
        }
    }
    
    private void addValidCells() {
        this.m_validCells.clear();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final PersonalSpaceLayout layout = localPlayer.getVisitingDimentionalBag().getLayout();
        for (final Room room : layout) {
            for (int x = room.getOriginX(); x < room.getOriginX() + room.getWidth(); ++x) {
                for (int y = room.getOriginY(); y < room.getOriginY() + room.getHeight(); ++y) {
                    this.addValidCell(room, x, y);
                }
            }
        }
        final List<LocalPartition> partitions = ((AbstractLocalPartitionManager<LocalPartition>)LocalPartitionManager.getInstance()).getAllLocalPartitions();
        for (final LocalPartition partition : partitions) {
            partition.foreachInteractiveElement(new TObjectProcedure<ClientMapInteractiveElement>() {
                @Override
                public boolean execute(final ClientMapInteractiveElement object) {
                    final Point3 position = object.getPosition();
                    UIDimensionalBagInteractionFrame.this.m_validCells.put(getCellId(position.getX(), position.getY()), RoomContentResult.BLOCKED);
                    return true;
                }
            });
        }
    }
    
    private void addValidCell(final Room room, final int x, final int y) {
        if (room instanceof GemControlledRoom && ((GemControlledRoom)room).maxContentReached(this.m_element)) {
            this.m_validCells.put(getCellId(x, y), RoomContentResult.MAX_CONTENT_REACHED);
            return;
        }
        final Resource resource = ResourceManager.getInstance().getResource(x, y);
        if (resource != null) {
            this.m_validCells.put(getCellId(x, y), RoomContentResult.BLOCKED);
            return;
        }
        if (!room.canPutContentAt(this.m_element, x, y)) {
            this.m_validCells.put(getCellId(x, y), RoomContentResult.CHECKER_RETURN_FALSE);
            return;
        }
        final TopologyMapInstance map = TopologyMapManager.getMapFromCell(x, y);
        if (map == null) {
            return;
        }
        this.m_validCells.put(getCellId(x, y), map.isCellBlocked(x, y) ? RoomContentResult.BLOCKED : RoomContentResult.OK);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        this.m_validCells.clear();
        if (this.m_element != null) {
            UIDimensionalBagInteractionFrame.m_logger.info((Object)"Bag-action cancelled");
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
            UIDimensionalBagInteractionFrame.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    private static int getCellId(final int x, final int y) {
        return MathHelper.getIntFromTwoInt(x, y);
    }
    
    static {
        OK_COLOR = new float[] { 0.0f, 1.0f, 0.0f, 0.5f };
        INVALID_COLOR = new float[] { 1.0f, 0.0f, 0.0f, 0.5f };
        m_logger = Logger.getLogger((Class)UIDimensionalBagInteractionFrame.class);
        m_instance = new UIDimensionalBagInteractionFrame();
    }
}
