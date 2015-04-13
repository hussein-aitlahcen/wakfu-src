package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.interactiveElements.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import org.apache.commons.lang3.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.core.script.*;
import com.ankamagames.framework.script.events.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.wakfu.client.core.game.chaos.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import java.awt.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import java.io.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.world.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public abstract class WakfuClientMapInteractiveElement extends ClientMapInteractiveElement implements MRUable
{
    private static final Logger m_logger;
    protected short m_oldState;
    protected boolean m_usingSpecificOldState;
    private TransitionModel m_transitionModel;
    protected boolean m_useSpecificAnimTransition;
    protected boolean m_selectable;
    protected boolean m_overHeadable;
    protected int m_overheadOffset;
    protected boolean m_hasToFinishOnIE;
    protected boolean m_usingDespawnAnimation;
    private AbstractChaosInteractiveElementHandler m_chaosElementHandler;
    protected final DragInfo m_dragInfo;
    protected ItemizableInfo m_itemizableInfo;
    private String m_name;
    private ArrayList<Point3> m_approachPoints;
    protected int m_currentFightId;
    private final BinarSerialPart SPECIFIC_DATA;
    private final BinarSerialPart SHARED_DATAS;
    private final BinarSerialPart ITEMIZABLE_SYNCHRO_DATA;
    
    public WakfuClientMapInteractiveElement() {
        super();
        this.m_transitionModel = TransitionModel.NORMAL;
        this.m_selectable = true;
        this.m_chaosElementHandler = AbstractChaosInteractiveElementHandler.NO_CHAOS;
        this.m_dragInfo = new DragInfoImpl(this);
        this.m_currentFightId = -1;
        this.SPECIFIC_DATA = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("Les \u00e9l\u00e9ments interactifs client ne peuvent pas \u00eatre s\u00e9rialis\u00e9s");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                WakfuClientMapInteractiveElement$1.m_logger.trace((Object)("D\u00e9codage des donn\u00e9es sp\u00e9cifiques (len=" + buffer.remaining() + ")"));
                if (buffer.remaining() < 1) {
                    WakfuClientMapInteractiveElement$1.m_logger.error((Object)"Impossible de d\u00e9s\u00e9rialiser un \u00e9l\u00e9ment interactif vide");
                    return;
                }
                if (buffer.remaining() < 23) {
                    WakfuClientMapInteractiveElement$1.m_logger.error((Object)("Taille de donn\u00e9es restantes dans le buffer invalide : " + buffer.remaining()));
                    return;
                }
                WakfuClientMapInteractiveElement.this.m_world = buffer.getShort();
                WakfuClientMapInteractiveElement.this.m_position.setX(buffer.getInt());
                WakfuClientMapInteractiveElement.this.m_position.setY(buffer.getInt());
                WakfuClientMapInteractiveElement.this.m_position.setZ(buffer.getShort());
                WakfuClientMapInteractiveElement.this.m_state = buffer.getShort();
                WakfuClientMapInteractiveElement.this.setVisible(buffer.get() != 0);
                WakfuClientMapInteractiveElement.this.m_usable = (buffer.get() != 0);
                WakfuClientMapInteractiveElement.this.setDirection(Direction8.getDirectionFromIndex(buffer.get()));
                WakfuClientMapInteractiveElement.this.m_activationPattern = buffer.getShort();
                final short numberOfPositionTrigger = buffer.getShort();
                for (int i = numberOfPositionTrigger - 1; i >= 0; --i) {
                    final Point3 pos = new Point3();
                    pos.setX(buffer.getInt());
                    pos.setY(buffer.getInt());
                    pos.setZ(buffer.getShort());
                    WakfuClientMapInteractiveElement.this.m_positionTriggers.add(pos);
                }
                final byte[] parameters = new byte[buffer.getShort() & 0xFFFF];
                buffer.get(parameters);
                WakfuClientMapInteractiveElement.this.m_parameter = StringUtils.fromUTF8(parameters);
                WakfuClientMapInteractiveElement.this.setProperties(WakfuInteractiveElementProperty.readProperties(buffer));
                buffer.getInt();
                WakfuClientMapInteractiveElement.this.initializeWithParameter();
                WakfuClientMapInteractiveElement.this.initialiseChaos();
                if (buffer.remaining() > 0) {
                    WakfuClientMapInteractiveElement$1.m_logger.error((Object)("Il reste des donn\u00e9es non trait\u00e9es dans le buffer : " + buffer.remaining()));
                }
            }
        };
        this.SHARED_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => par de s\u00e9rialisation");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                WakfuClientMapInteractiveElement.this.setState(buffer.getShort());
                WakfuClientMapInteractiveElement.this.setVisible(buffer.get() == 1);
                WakfuClientMapInteractiveElement.this.setUsable(buffer.get() == 1);
                WakfuClientMapInteractiveElement.this.setBlockingMovements(buffer.get() == 1);
                WakfuClientMapInteractiveElement.this.setBlockingLineOfSight(buffer.get() == 1);
                WakfuClientMapInteractiveElement.this.setVisibleContent(buffer.get());
                for (int propertySize = buffer.getInt(), i = 0; i < propertySize; ++i) {
                    WakfuClientMapInteractiveElement.this.addProperty(WakfuInteractiveElementProperty.getProperty(buffer.get()));
                }
            }
        };
        this.ITEMIZABLE_SYNCHRO_DATA = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => par de s\u00e9rialisation");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final RawItemizableSynchronisationData rawData = new RawItemizableSynchronisationData();
                rawData.unserialize(buffer);
                WakfuClientMapInteractiveElement.this.getOrCreateItemizableInfo().fromRawSynchronisationData(rawData);
            }
        };
    }
    
    public static WakfuClientMapInteractiveElement spawnNewElement(final long elementId, final byte[] elementDatas) {
        final WakfuClientMapInteractiveElement element = ((InteractiveElementFactory<WakfuClientMapInteractiveElement, C>)WakfuClientInteractiveElementFactory.getInstance()).createInteractiveElement(elementId);
        if (element == null) {
            WakfuClientMapInteractiveElement.m_logger.error((Object)("Impossible de spawner l'\u00e9l\u00e9ment interactif instanceId=" + elementId));
            return null;
        }
        if (elementDatas != null) {
            element.fromBuild(elementDatas);
        }
        element.notifyViews();
        for (final ClientInteractiveElementView view : element.getViews()) {
            if (view instanceof ClientInteractiveAnimatedElementSceneView) {
                final ClientInteractiveAnimatedElementSceneView clientView = (ClientInteractiveAnimatedElementSceneView)view;
                AnimatedElementSceneViewManager.getInstance().addElement(clientView);
                clientView.fadeIfOnScreen();
                MaskableHelper.setUndefined(clientView);
            }
        }
        return element;
    }
    
    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        if (!visible) {
            this.closeMRU();
        }
        for (final ClientInteractiveElementView view : this.getViews()) {
            ((WakfuClientInteractiveAnimatedElementSceneView)view).setVisible(visible);
        }
    }
    
    @Override
    public boolean isVisible() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (player == null) {
            return super.isVisible();
        }
        if (!this.hasProperty(WakfuInteractiveElementProperty.CHALLENGE_IE)) {
            return super.isVisible();
        }
        final Fight fight = player.getCurrentOrObservedFight();
        final Point3 pos = this.getPosition();
        return super.isVisible() && (fight == null || !fight.getFightMap().isInsideOrBorder(pos.getX(), pos.getY()));
    }
    
    private void closeMRU() {
        final MRU mru = UIMRUFrame.getInstance().getCurrentMRU();
        if (mru != null) {
            for (int i = 0, size = mru.getSourcesCount(); i < size; ++i) {
                final MRUable source = mru.getSource(i);
                if (source == this) {
                    UIMRUFrame.getInstance().closeCurrentMRU();
                    break;
                }
            }
        }
    }
    
    @Override
    public int getWorldCellX() {
        return this.m_position.getX();
    }
    
    @Override
    public int getWorldCellY() {
        return this.m_position.getY();
    }
    
    @Override
    public short getWorldCellAltitude() {
        return this.m_position.getZ();
    }
    
    public float getWorldX() {
        return this.getWorldCellX();
    }
    
    public float getWorldY() {
        return this.getWorldCellY();
    }
    
    public float getAltitude() {
        return this.getWorldCellAltitude();
    }
    
    @Override
    public List<Point3> getApproachPoints() {
        if (this.m_approachPoints == null) {
            this.initializeActivationPattern();
        }
        return Collections.unmodifiableList((List<? extends Point3>)this.m_approachPoints);
    }
    
    public boolean isInApproachPoint(final Point3 pos) {
        return this.getApproachPoints().contains(pos);
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public short getOldState() {
        return this.m_oldState;
    }
    
    public void setOldState(final short oldState) {
        this.m_oldState = oldState;
    }
    
    public int getOverheadOffset() {
        return this.m_overheadOffset;
    }
    
    public final TransitionModel getTransitionModel() {
        return this.m_transitionModel;
    }
    
    protected final void setTransitionModel(final TransitionModel transitionModel) {
        this.m_transitionModel = transitionModel;
    }
    
    public boolean isOverHeadable() {
        return this.m_overHeadable;
    }
    
    public void setOverHeadable(final boolean overHeadable) {
        this.m_overHeadable = overHeadable;
    }
    
    public boolean isSelectable() {
        return this.m_selectable;
    }
    
    public void setSelectable(final boolean selectable) {
        this.m_selectable = selectable;
    }
    
    public boolean isUseSpecificAnimTransition() {
        return this.m_useSpecificAnimTransition;
    }
    
    public void setUseSpecificAnimTransition(final boolean useSpecificAnimTransition) {
        this.m_useSpecificAnimTransition = useSpecificAnimTransition;
    }
    
    public boolean isUsingDespawnAnimation() {
        return this.m_usingDespawnAnimation;
    }
    
    public void setUsingDespawnAnimation(final boolean usingDespawnAnimation) {
        this.m_usingDespawnAnimation = usingDespawnAnimation;
    }
    
    public boolean isUsingSpecificOldState() {
        return this.m_usingSpecificOldState;
    }
    
    public void setUsingSpecificOldState(final boolean usingSpecificOldState) {
        this.m_usingSpecificOldState = usingSpecificOldState;
    }
    
    public void setPosition(final Point3 position) {
        this.m_position.set(position);
    }
    
    @Override
    public boolean fireAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (!ArrayUtils.contains(this.getUsableActions(), action)) {
            WakfuClientMapInteractiveElement.m_logger.warn((Object)("Tentative de fire de l'action " + action + " sur l'\u00e9l\u00e9ment interactif " + this + " alors que celle-ci n'est pas g\u00e9r\u00e9e"), (Throwable)new UnsupportedOperationException());
            return false;
        }
        boolean executed = false;
        if (this.m_chaosElementHandler.isDestroyed()) {
            executed = this.m_chaosElementHandler.fireAction(action, user);
        }
        else if (this.m_itemizableInfo != null) {
            executed = this.m_itemizableInfo.onAction(action, user);
        }
        if (!executed) {
            executed = super.fireAction(action, user);
        }
        if (executed) {
            ClientGameEventManager.INSTANCE.fireEvent(new ClientEventInteractiveElementActivation(this, action.getActionId()));
        }
        if (user.getId() == WakfuGameEntity.getInstance().getLocalPlayer().getId()) {
            ScriptEventManager.getInstance().fireEvent(new InteractiveElementActivated(this.getId(), action.getActionId()));
        }
        return executed;
    }
    
    @Override
    public final InteractiveElementAction[] getUsableActions() {
        if (this.m_chaosElementHandler.isDestroyed()) {
            return this.m_chaosElementHandler.getUsableActions();
        }
        if (this.m_itemizableInfo != null) {
            return this.m_itemizableInfo.getInteractiveUsableActions();
        }
        return this.getInteractiveUsableActions();
    }
    
    public abstract InteractiveElementAction[] getInteractiveUsableActions();
    
    protected void initialiseChaos() {
        if (!(this instanceof ChaosInteractiveElement)) {
            return;
        }
        final ChaosInteractiveElement chaosIe = (ChaosInteractiveElement)this;
        if (chaosIe.getChaosIEParameter() == null) {
            return;
        }
        this.m_chaosElementHandler = new ChaosInteractiveElementHandler(chaosIe);
    }
    
    @Override
    public final void setState(final short state) {
        if (state == this.m_state) {
            return;
        }
        super.setState(state);
        this.m_chaosElementHandler.changeState(state);
        this.onStateChanged();
    }
    
    public AbstractChaosInteractiveElementHandler getChaosElementHandler() {
        return this.m_chaosElementHandler;
    }
    
    protected void onStateChanged() {
        this.notifyViews();
    }
    
    @Override
    public final InteractiveElementAction getDefaultAction() {
        return this.m_chaosElementHandler.isDestroyed() ? this.m_chaosElementHandler.getDefaultAction() : this.getInteractiveDefaultAction();
    }
    
    protected abstract InteractiveElementAction getInteractiveDefaultAction();
    
    @Override
    public void sendActionMessage(final InteractiveElementAction action) {
        WakfuGameEntity.getInstance().getLocalPlayer().getActor().sendBufferedPathToServer();
        final InteractiveElementActionMessage actionMessage = new InteractiveElementActionMessage();
        actionMessage.setActionId(action.getActionId());
        actionMessage.setElementId(this.getId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(actionMessage);
    }
    
    @Override
    public AbstractMRUAction[] getMRUActions() {
        if (this.m_chaosElementHandler.isDestroyed()) {
            return ((ChaosInteractiveElementHandler)this.m_chaosElementHandler).getMRUActions();
        }
        if (this.m_itemizableInfo != null) {
            return this.m_itemizableInfo.getInteractiveMRUActions();
        }
        return this.getInteractiveMRUActions();
    }
    
    public AbstractMRUAction[] getInteractiveMRUActions() {
        return AbstractMRUAction.EMPTY_ARRAY;
    }
    
    @Override
    public boolean isMRUPositionable() {
        return true;
    }
    
    @Override
    public final Point getMRUScreenPosition() {
        final AleaWorldScene scene = WakfuClientInstance.getInstance().getWorldScene();
        final Point2 p = IsoCameraFunc.getScreenPositionFromBottomLeft(scene, this.getWorldX(), this.getWorldY(), this.getAltitude() + this.getHeight());
        final int x = MathHelper.fastRound(p.m_x);
        final int y = MathHelper.fastRound(p.m_y);
        return new Point(x, y);
    }
    
    @Override
    public short getMRUHeight() {
        return 0;
    }
    
    public byte getHeight() {
        final Iterator i$ = this.getViews().iterator();
        if (i$.hasNext()) {
            final ClientInteractiveElementView view = i$.next();
            return view.getViewHeight();
        }
        return 4;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_oldState = 0;
        this.m_usingSpecificOldState = false;
        this.m_transitionModel = TransitionModel.NORMAL;
        this.m_useSpecificAnimTransition = false;
        this.m_selectable = true;
        this.m_overHeadable = false;
        this.m_overheadOffset = 0;
        this.m_hasToFinishOnIE = false;
        this.m_usingDespawnAnimation = false;
        assert this.m_chaosElementHandler == AbstractChaosInteractiveElementHandler.NO_CHAOS;
        this.m_name = "";
        assert this.m_approachPoints == null;
        this.m_currentFightId = -1;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_name = null;
        this.m_chaosElementHandler = AbstractChaosInteractiveElementHandler.NO_CHAOS;
        this.m_approachPoints = null;
        this.m_itemizableInfo = null;
        this.m_dragInfo.clear();
    }
    
    @Override
    protected final BinarSerialPart getSpecificDataPart() {
        return this.SPECIFIC_DATA;
    }
    
    @Override
    protected final BinarSerialPart getSynchronizationPart() {
        return this.SHARED_DATAS;
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return BinarSerialPart.EMPTY;
    }
    
    protected BinarSerialPart getItemizableSynchronisationPart() {
        return this.ITEMIZABLE_SYNCHRO_DATA;
    }
    
    @Override
    public BinarSerialPart[] partsEnumeration() {
        return new BinarSerialPart[] { this.getGlobalDataPart(), this.getSpecificDataPart(), this.getSynchronizationPart(), this.getSynchronizationSpecificPart(), this.getPersistancePart(), this.getAdditionalPersistancePart(), this.getItemizableSynchronisationPart() };
    }
    
    public String getOverHeadText() {
        return null;
    }
    
    public boolean hasMobileInRange(final Actor actor) {
        return !this.hasToFinishOnIE() && this.isInApproachPoint(actor.getWorldCoordinates());
    }
    
    @Override
    public boolean hasToFinishOnIE() {
        return this.m_hasToFinishOnIE;
    }
    
    public void initializeActivationPattern() {
        if (this.isDummy()) {
            return;
        }
        final ArrayList<InteractiveElementActivationPattern> patterns = InteractiveElementActivationPattern.getPatterns(this.m_activationPattern);
        this.m_hasToFinishOnIE = false;
        if (patterns.contains(InteractiveElementActivationPattern.CENTER)) {
            if (this.isBlockingMovements()) {
                patterns.remove(InteractiveElementActivationPattern.CENTER);
                if (patterns.isEmpty()) {
                    WakfuClientMapInteractiveElement.m_logger.error((Object)("[Level Design] Element interactif bloquant, et pas de pattern d'activation valide : " + this.m_position));
                    this.m_hasToFinishOnIE = true;
                }
            }
            else if (patterns.size() == 1) {
                this.m_hasToFinishOnIE = true;
            }
        }
        final Point3 pos = new Point3();
        this.m_approachPoints = new ArrayList<Point3>();
        TopologyMapManager.setMoverCaracteristics((short)6, (byte)0, (short)4);
        if (patterns.contains(InteractiveElementActivationPattern.CENTER)) {
            this.m_approachPoints.add(new Point3(this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude()));
        }
        for (int i = patterns.size() - 1; i >= 0; --i) {
            final InteractiveElementActivationPattern pat = patterns.get(i);
            if (pat != InteractiveElementActivationPattern.CENTER) {
                final Direction8 patternDir = Direction8.getDirectionFromIndex((this.getDirection().m_index + pat.getPositionOffset()) % 8);
                pos.set(this.m_position.getX() + patternDir.m_x, this.m_position.getY() + patternDir.m_y, this.m_position.getZ());
                final short mapX = (short)MapConstants.getMapCoordFromCellX(pos.getX());
                final short mapY = (short)MapConstants.getMapCoordFromCellY(pos.getY());
                try {
                    if (MapManagerHelper.isValidTopology(mapX, mapY)) {
                        TopologyMapManager.loadMap(mapX, mapY);
                        final short nearestWalkableZ = TopologyMapManager.getPossibleNearestWalkableZ(pos.getX(), pos.getY(), pos.getZ());
                        if (nearestWalkableZ != -32768) {
                            pos.setZ(nearestWalkableZ);
                            this.m_approachPoints.add(new Point3(pos));
                        }
                    }
                    else {
                        WakfuClientMapInteractiveElement.m_logger.error((Object)("On veut charger une cellule qui n'appartient \u00e0 aucune map " + pos + " world=" + MapManagerHelper.getWorldId() + " IE=" + this.getId()));
                    }
                }
                catch (IOException e) {
                    WakfuClientMapInteractiveElement.m_logger.error((Object)("Impossible de charger la map [" + mapX + ", " + mapY + "]"), (Throwable)e);
                }
            }
        }
    }
    
    public void initializeWithParameter() {
    }
    
    public void runDespawnAnimation() {
    }
    
    private void setBlockingMovementsInMap(final TopologyMapInstance map, final boolean blocked) {
        if (this.isBlockingWholeCell()) {
            map.setCellBlocked(this.getWorldCellX(), this.getWorldCellY(), blocked);
        }
        else {
            map.setBlocked(this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude(), blocked);
        }
    }
    
    @Override
    public void onSpawn() {
        super.onSpawn();
        this.addToTopologyMap();
    }
    
    public void addToTopologyMap() {
        if (!this.isBlockingMovements()) {
            return;
        }
        final Point3 pos = this.getPosition();
        final short x = (short)MapConstants.getMapCoordFromCellX(pos.getX());
        final short y = (short)MapConstants.getMapCoordFromCellY(pos.getY());
        try {
            TopologyMapManager.loadMap(x, y);
        }
        catch (IOException e) {
            WakfuClientMapInteractiveElement.m_logger.warn((Object)"Probl\u00e8me au chargement de la topologie de la map");
        }
        final TopologyMapInstance topologyMap = this.getTopologyMap();
        if (topologyMap != null) {
            this.setBlockingMovementsInMap(topologyMap, true);
        }
        else {
            WakfuClientMapInteractiveElement.m_logger.error((Object)"Impossible de marquer bloquant un \u00e9l\u00e9ment sans topologie");
        }
    }
    
    @Override
    public void onDeSpawn() {
        super.onDeSpawn();
        if (this.hasProperty(WakfuInteractiveElementProperty.CHALLENGE_IE)) {
            final IsoParticleSystemFactory factory = IsoParticleSystemFactory.getInstance();
            final Collection<ClientInteractiveElementView> views = this.getViews();
            for (final ClientInteractiveElementView view : views) {
                if (!(view instanceof ClientInteractiveAnimatedElementSceneView)) {
                    continue;
                }
                final FreeParticleSystem particle = factory.getFreeParticleSystem(800030);
                particle.setTarget((IsoWorldTarget)view);
                IsoParticleSystemManager.getInstance().addParticleSystem(particle);
            }
        }
        final TopologyMapInstance topologyMap = this.getTopologyMap();
        if (topologyMap != null) {
            this.setBlockingMovementsInMap(topologyMap, false);
        }
        final AnimatedElementSceneViewManager manager = AnimatedElementSceneViewManager.getInstance();
        for (final ClientInteractiveElementView view : this.getViews()) {
            if (view instanceof ClientInteractiveAnimatedElementSceneView) {
                manager.removeElement((ClientInteractiveAnimatedElementSceneView)view);
            }
        }
        this.release();
    }
    
    @Override
    public void setBlockingMovements(final boolean blockingMovements) {
        super.setBlockingMovements(blockingMovements);
        final TopologyMapInstance map = this.getTopologyMap();
        if (this.m_isSpawned && map != null) {
            final int worldCellX = this.getWorldCellX();
            final int worldCellY = this.getWorldCellY();
            final short worldCellZ = this.getWorldCellAltitude();
            final LocalPartition partition = ((AbstractLocalPartitionManager<LocalPartition>)LocalPartitionManager.getInstance()).getLocalPartition(worldCellX, worldCellY);
            final CheckCellBlockedProcedure procedure = new CheckCellBlockedProcedure(worldCellX, worldCellY, worldCellZ);
            partition.foreachInteractiveElement(procedure);
            this.setBlockingMovementsInMap(map, procedure.isCellBlocked());
        }
    }
    
    public int getOverheadDelayPreference() {
        return -1;
    }
    
    public void setCurrentFightId(final int fightId) {
        this.m_currentFightId = fightId;
    }
    
    public boolean isConcernedByFight(final int fightId) {
        return this.m_currentFightId == fightId;
    }
    
    public int getCurrentFightId() {
        return this.m_currentFightId;
    }
    
    public boolean checkSubscription() {
        return true;
    }
    
    public DragInfo getDragInfo() {
        return this.m_dragInfo;
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
        super.setDirection(direction);
        this.m_approachPoints = null;
    }
    
    public ItemizableInfo getOrCreateItemizableInfo() {
        throw new UnsupportedOperationException("Il faut implementer cette methode pour rendre l'ie deployable");
    }
    
    public ItemizableInfo getItemizableInfo() {
        return this.m_itemizableInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuClientMapInteractiveElement.class);
    }
    
    private static class CheckCellBlockedProcedure implements TObjectProcedure<ClientMapInteractiveElement>
    {
        private final int m_x;
        private final int m_y;
        private final short m_z;
        private boolean cellBlocked;
        
        CheckCellBlockedProcedure(final int worldCellX, final int worldCellY, final short worldCellZ) {
            super();
            this.m_x = worldCellX;
            this.m_y = worldCellY;
            this.m_z = worldCellZ;
        }
        
        @Override
        public boolean execute(final ClientMapInteractiveElement object) {
            if (object.getWorldCellX() != this.m_x || object.getWorldCellY() != this.m_y || object.getWorldCellAltitude() != this.m_z) {
                return true;
            }
            if (!object.isBlockingMovements()) {
                return true;
            }
            this.cellBlocked = true;
            return false;
        }
        
        public boolean isCellBlocked() {
            return this.cellBlocked;
        }
        
        @Override
        public String toString() {
            return "CheckCellBlockedProcedure{cellBlocked=" + this.cellBlocked + '}';
        }
    }
}
