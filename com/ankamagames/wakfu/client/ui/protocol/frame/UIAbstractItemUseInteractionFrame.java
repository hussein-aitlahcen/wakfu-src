package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.cellSelector.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.baseImpl.graphics.alea.utils.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.xulor2.util.alignment.*;
import org.apache.commons.lang3.*;

public abstract class UIAbstractItemUseInteractionFrame implements MessageFrame, Runnable, ResourceCreationListener, ResourceDestructionListener
{
    private static final float[] m_excellentColor;
    private static final float[] m_veryBadColor;
    private static final float[] m_badColor;
    private static final float[] m_goodColor;
    private static final float[] m_invalidColor;
    private static final float[] m_selectedColor;
    private static final int[] CANCELING_RESULTS_IDS;
    protected static final int PLANT_DISTANCE_MAX = 8;
    private static final Logger m_logger;
    protected Item m_item;
    protected final DefaultIsoWorldTarget m_isoWorldTarget;
    protected Point3 m_lastTarget;
    private Point3 m_currentPos;
    protected ElementSelection m_elementSelection;
    protected ElementSelection m_elementsSelectedSelection;
    protected final LinkedList<SeedAction> m_seedActions;
    protected SeedAction m_runningSeedAction;
    private MobileEndPathListener m_listener;
    private final MouseClickedListener m_layerMouseListener;
    
    protected UIAbstractItemUseInteractionFrame() {
        super();
        this.m_isoWorldTarget = new DefaultIsoWorldTarget();
        this.m_seedActions = new LinkedList<SeedAction>();
        this.m_layerMouseListener = new MouseClickedListener() {
            @Override
            public boolean run(final Event event) {
                if (event.getTarget() == null || !(event.getTarget() instanceof Widget)) {
                    return true;
                }
                final Widget target = event.getTarget();
                if (target == MasterRootContainer.getInstance() || (target != null && target.isNonBlocking())) {
                    return true;
                }
                WakfuGameEntity.getInstance().removeFrame(UIAbstractItemUseInteractionFrame.this);
                return false;
            }
        };
        this.m_elementSelection = new ElementSelection("seedVeryBadRange", UIAbstractItemUseInteractionFrame.m_excellentColor);
        this.m_elementsSelectedSelection = new ElementSelection("seedSelectedCells", UIAbstractItemUseInteractionFrame.m_selectedColor);
    }
    
    public void setSelectedItem(final Item selectedItem) {
        this.m_item = selectedItem;
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    protected EffectContainer getEffectContainer() {
        return this.m_item;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void selectRange() {
        final String iconName = (String)this.m_item.getFieldValue("iconUrl");
        if (iconName != null) {
            this.showCastMouseIcon(iconName);
        }
        else {
            GraphicalMouseManager.getInstance().hide();
            CursorFactory.getInstance().unlock();
        }
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
            case 19995: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                final Point3 target = getNearestPoint3(msg.getMouseX(), msg.getMouseY());
                if (!sameLocation(target, this.m_lastTarget) && UIWorldSceneMouseMessage.isLeftButtonDown()) {
                    this.createAndRegisterSeedAction(target);
                    this.m_lastTarget = target;
                }
                return false;
            }
            case 19998: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                if (msg.isButtonLeft()) {
                    this.m_lastTarget = null;
                }
                return false;
            }
            case 19992: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                if (msg.isButtonLeft()) {
                    final Point3 target = getNearestPoint3(msg.getMouseX(), msg.getMouseY());
                    this.createAndRegisterSeedAction(target);
                }
                else {
                    this.cancelAction(msg);
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private static boolean sameLocation(final Point3 target, final Point3 lastTarget) {
        return (target == null && lastTarget == null) || (target != null && target.equals(lastTarget));
    }
    
    private void cancelAction(final UIWorldSceneMouseMessage msg) {
        final AbstractOccupation currentOccupation = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOccupation();
        final boolean hasPlantOccupation = currentOccupation != null && currentOccupation.getOccupationTypeId() == 2;
        if ((!this.m_seedActions.isEmpty() || hasPlantOccupation) && msg.isButtonMiddle()) {
            final SeedAction seedAction = this.m_seedActions.removeLast();
            final Point3 target = seedAction.getTarget();
            if (!this.hasSeedActionOnTarget(target)) {
                this.m_elementsSelectedSelection.remove(target.getX(), target.getY(), target.getZ());
            }
        }
        else {
            if (hasPlantOccupation) {
                currentOccupation.cancel();
            }
            WakfuGameEntity.getInstance().removeFrame(this);
        }
    }
    
    protected void refreshCursorDisplay(final Point3 target, final boolean force) {
        if (target == null) {
            this.m_elementSelection.clear();
            this.m_lastTarget = null;
            this.clearParticles();
            return;
        }
        if (target.equals(this.m_lastTarget) && !force) {
            return;
        }
        this.m_elementSelection.clear();
        this.m_lastTarget = target;
        GraphicalMouseManager.getInstance().setText(this.getMouseInfoText());
        final int validity = this.checkValidity(target);
        this.refreshColor(validity);
        this.addToSelection(target);
        this.m_isoWorldTarget.setWorldPosition(target.getX(), target.getY(), target.getZ());
        if (validity < 0) {
            this.clearParticles();
            return;
        }
        this.refreshParticles();
    }
    
    protected abstract void addToSelection(final Point3 p0);
    
    protected abstract void refreshParticles();
    
    protected void refreshColor(final int validity) {
        if (validity < 0) {
            this.m_elementSelection.setColor(UIAbstractItemUseInteractionFrame.m_invalidColor);
        }
        else if (this.isNoFeedbackMode()) {
            this.m_elementSelection.setColor(UIAbstractItemUseInteractionFrame.m_excellentColor);
        }
        else if (validity <= 25) {
            this.m_elementSelection.setColor(UIAbstractItemUseInteractionFrame.m_veryBadColor);
        }
        else if (validity <= 50) {
            this.m_elementSelection.setColor(UIAbstractItemUseInteractionFrame.m_badColor);
        }
        else if (validity <= 75) {
            this.m_elementSelection.setColor(UIAbstractItemUseInteractionFrame.m_goodColor);
        }
        else if (validity <= 100) {
            this.m_elementSelection.setColor(UIAbstractItemUseInteractionFrame.m_excellentColor);
        }
        else {
            this.m_elementSelection.setColor(UIAbstractItemUseInteractionFrame.m_invalidColor);
            UIAbstractItemUseInteractionFrame.m_logger.error((Object)("Validit\u00e9 inattendue pour planter sur la case [" + this.m_lastTarget + "] : " + validity));
        }
    }
    
    public boolean isNoFeedbackMode() {
        return this.m_item.getReferenceItem().hasItemProperty(ItemProperty.NO_FEEDBACK);
    }
    
    private boolean isSelectionTooFar(final Point3 target) {
        final int d = Math.abs(target.getX() - WakfuGameEntity.getInstance().getLocalPlayer().getWorldCellX()) + Math.abs(target.getY() - WakfuGameEntity.getInstance().getLocalPlayer().getWorldCellY());
        return d >= 8;
    }
    
    private boolean createAndRegisterSeedAction(final Point3 target) {
        if (target == null || this.checkValidity(target) == -1) {
            return false;
        }
        final SeedAction seedAction = new SeedAction(target);
        final int seedActionsSize = this.m_seedActions.size();
        if (seedActionsSize > 98 || !this.canAffordSeedActionCount(seedActionsSize)) {
            return false;
        }
        if (this.hasSeedActionOnTarget(target)) {
            return false;
        }
        if (this.m_runningSeedAction == null) {
            this.executeSeedAction(seedAction);
        }
        else {
            this.m_seedActions.add(seedAction);
            this.m_elementsSelectedSelection.add(target.getX(), target.getY(), target.getZ());
        }
        return true;
    }
    
    SeedAction getNextSeedAction() {
        this.m_runningSeedAction = null;
        if (this.m_seedActions.isEmpty()) {
            return null;
        }
        return this.m_seedActions.poll();
    }
    
    protected boolean hasSeedActionOnTarget(final Point3 target) {
        for (final SeedAction seedAction : this.m_seedActions) {
            if (seedAction.getTarget().equals(target)) {
                return true;
            }
        }
        return false;
    }
    
    protected void removeSeedActionsOnTarget(final Point3 target) {
        final SeedAction[] seedActions = this.m_seedActions.toArray(new SeedAction[this.m_seedActions.size()]);
        for (int i = seedActions.length - 1; i >= 0; --i) {
            final SeedAction seedAction = seedActions[i];
            if (seedAction.getTarget().equals(target)) {
                this.m_seedActions.remove(seedAction);
            }
        }
    }
    
    private int getSeedActionCountOnTarget(final Point3 target) {
        int count = 0;
        for (final SeedAction seedAction : this.m_seedActions) {
            if (seedAction.getTarget().equals(target)) {
                ++count;
            }
        }
        return count;
    }
    
    public abstract void onSeedSucceed();
    
    public void createTimeOut() {
        this.deleteTimeOut();
        ProcessScheduler.getInstance().schedule(this, 5000L, 1);
    }
    
    public void deleteTimeOut() {
        ProcessScheduler.getInstance().remove(this);
    }
    
    @Override
    public void run() {
        UIAbstractItemUseInteractionFrame.m_logger.warn((Object)"[Plantation] timeOut execut\u00e9");
        this.executeNextSeedAction();
    }
    
    @Override
    public void onResourceCreation(final Resource resource) {
        if (this.isSamePosition(resource)) {
            this.refreshCursorDisplay(this.m_lastTarget, true);
        }
    }
    
    @Override
    public void onResourceDestruction(final Resource resource) {
        if (this.isSamePosition(resource)) {
            this.refreshCursorDisplay(this.m_lastTarget, true);
        }
    }
    
    private boolean isSamePosition(final Resource resource) {
        return this.m_isoWorldTarget != null && this.m_isoWorldTarget.getWorldCellX() == resource.getWorldCellX() && this.m_isoWorldTarget.getWorldCellY() == resource.getWorldCellY();
    }
    
    public void executeNextSeedAction() {
        while (!this.executeSeedAction(this.getNextSeedAction())) {}
    }
    
    boolean executeSeedAction(final SeedAction seedAction) {
        this.m_runningSeedAction = seedAction;
        if (seedAction == null) {
            return true;
        }
        final LocalPlayerCharacter localCharacter = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterActor localActor = localCharacter.getActor();
        final int seedPositionX = seedAction.getSeedPositionX();
        final int seedPositionY = seedAction.getSeedPositionY();
        final short seedPositionZ = seedAction.getSeedPositionZ();
        final Point3 target = seedAction.getTarget();
        final float dz = seedPositionZ - localActor.getAltitude();
        if (Math.abs(dz) > Math.abs(localCharacter.getJumpCapacity())) {
            UIAbstractItemUseInteractionFrame.m_logger.error((Object)("Plantation impossible sur la cellule " + target + " : hauteur maximale de saut de " + localCharacter.getJumpCapacity()));
            ErrorsMessageTranslator.getInstance().pushMessage(5, 3, new Object[0]);
            this.removeTargetFromSelection(target);
            this.m_runningSeedAction = null;
            return false;
        }
        if (this.m_listener != null) {
            localActor.removeEndPositionListener(this.m_listener);
        }
        localActor.addEndPositionListener(this.m_listener = new MobileEndPathListener() {
            @Override
            public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                mobile.removeEndPositionListener(this);
                UIAbstractItemUseInteractionFrame.this.sendItemActionRequest(seedPositionX, seedPositionY);
            }
        });
        final int distance = target.getDistance(localActor.getWorldCellX(), localActor.getWorldCellY());
        if (distance >= 2 || distance == 0) {
            final List<Point3> dests = new ArrayList<Point3>();
            dests.add(new Point3(target.getX() + 1, target.getY(), target.getZ()));
            dests.add(new Point3(target.getX() - 1, target.getY(), target.getZ()));
            dests.add(new Point3(target.getX(), target.getY() + 1, target.getZ()));
            dests.add(new Point3(target.getX(), target.getY() - 1, target.getZ()));
            if (!localActor.moveTo(false, true, dests)) {
                localActor.removeEndPositionListener(this.m_listener);
                ErrorsMessageTranslator.getInstance().pushMessage(2, 3, new Object[0]);
                this.m_runningSeedAction = null;
                return false;
            }
        }
        else {
            localActor.removeEndPositionListener(this.m_listener);
            this.sendItemActionRequest(seedPositionX, seedPositionY);
        }
        if (this.getSeedActionCountOnTarget(target) <= 1) {
            this.removeTargetFromSelection(target);
        }
        if (!this.canAffordSeedActionCount(1)) {
            WakfuGameEntity.getInstance().removeFrame(this);
        }
        return true;
    }
    
    protected abstract void sendItemActionRequest(final int p0, final int p1);
    
    protected boolean canAffordSeedActionCount(final int count) {
        final Item item = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getItemFromInventories(this.m_item.getUniqueId());
        if (item == null) {
            return false;
        }
        final AbstractItemAction itemAction = item.getReferenceItem().getItemAction();
        return !itemAction.isMustConsumeItem() || item.getQuantity() > count;
    }
    
    protected void removeTargetFromSelection(final Point3 target) {
        this.m_elementsSelectedSelection.remove(target.getX(), target.getY(), target.getZ());
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        this.m_runningSeedAction = null;
        ResourceManager.getInstance().addCreationListener(this);
        ResourceManager.getInstance().addDestructionListener(this);
        final WakfuWorldScene scene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
        CursorFactory.getInstance().show(CursorFactory.CursorType.CUSTOM1, true);
        scene.setDispatchKeyReleasedMessage(true);
        scene.setDispatchMouseMovedExtendedMessage(true);
        final Point3 target = getNearestPoint3(scene.getMouseX(), scene.getMouseY());
        this.refreshCursorDisplay(target, false);
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_CLICKED, this.m_layerMouseListener, true);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            ResourceManager.getInstance().removeCreationListener(this);
            ResourceManager.getInstance().removeDestructionListener(this);
            GraphicalMouseManager.getInstance().hide();
            CursorFactory.getInstance().unlock();
            this.m_seedActions.clear();
            this.m_elementSelection.clear();
            this.m_elementsSelectedSelection.clear();
            this.m_lastTarget = null;
            this.clearParticles();
            MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_CLICKED, this.m_layerMouseListener, true);
        }
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
    
    private void showCastMouseIcon(final String iconUrl) {
        if (iconUrl != null) {
            GraphicalMouseManager.getInstance().showMouseInformation(iconUrl, this.getMouseInfoText(), 30, 0, Alignment9.WEST);
        }
    }
    
    protected abstract String getMouseInfoText();
    
    protected abstract int checkValidity(final Point3 p0);
    
    public void onErrorRecieved(final int resultId) {
        if (ArrayUtils.contains(UIAbstractItemUseInteractionFrame.CANCELING_RESULTS_IDS, resultId)) {
            WakfuGameEntity.getInstance().removeFrame(this);
            return;
        }
        this.executeNextSeedAction();
    }
    
    protected abstract void clearParticles();
    
    static {
        m_excellentColor = new float[] { 0.0f, 1.0f, 0.0f, 0.6f };
        m_veryBadColor = new float[] { 1.0f, 0.0f, 0.0f, 0.6f };
        m_badColor = new float[] { 1.0f, 0.5f, 0.0f, 0.6f };
        m_goodColor = new float[] { 1.0f, 1.0f, 0.0f, 0.6f };
        m_invalidColor = new float[] { 0.0f, 0.0f, 0.0f, 0.6f };
        m_selectedColor = new float[] { 1.0f, 1.0f, 1.0f, 0.6f };
        CANCELING_RESULTS_IDS = new int[] { 36, 10, 35 };
        m_logger = Logger.getLogger((Class)UIAbstractItemUseInteractionFrame.class);
    }
    
    protected static class SeedAction
    {
        private final Point3 m_target;
        
        private SeedAction(final Point3 target) {
            super();
            this.m_target = target;
        }
        
        public int getSeedPositionX() {
            return this.m_target.getX();
        }
        
        public int getSeedPositionY() {
            return this.m_target.getY();
        }
        
        public short getSeedPositionZ() {
            return this.m_target.getZ();
        }
        
        public Point3 getTarget() {
            return this.m_target;
        }
    }
}
