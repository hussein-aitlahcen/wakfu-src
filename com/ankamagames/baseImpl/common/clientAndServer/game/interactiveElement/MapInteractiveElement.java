package com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;

public abstract class MapInteractiveElement extends BinarSerial implements OccupationUsedElement
{
    private static final Logger m_logger;
    public static final byte DEFAULT_VISIBLE_CONTENT = 0;
    protected long m_id;
    private short m_modelId;
    protected final Point3 m_position;
    protected short m_world;
    protected short m_state;
    private boolean m_visible;
    protected boolean m_usable;
    protected short m_activationPattern;
    protected final HashSet<Point3> m_positionTriggers;
    private Direction8 m_direction;
    protected String m_parameter;
    protected boolean m_blockingMovements;
    protected boolean m_blockingLineOfSight;
    protected byte m_visibleContent;
    private boolean m_isDummy;
    private final HashSet<InteractiveElementProperty> m_properties;
    protected MonitoredPool m_pool;
    private ArrayList<InteractiveElementChangesListener> m_changesListeners;
    
    protected MapInteractiveElement() {
        super();
        this.m_position = new Point3();
        this.m_positionTriggers = new HashSet<Point3>();
        this.m_visibleContent = 0;
        this.m_properties = new HashSet<InteractiveElementProperty>();
    }
    
    public final short getActivationPattern() {
        return this.m_activationPattern;
    }
    
    public Direction8 getDirection() {
        return this.m_direction;
    }
    
    public abstract int getWorldCellX();
    
    public abstract int getWorldCellY();
    
    public abstract short getWorldCellAltitude();
    
    public void setDirection(final Direction8 direction) {
        this.m_direction = direction;
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    public void setId(final long id) {
        this.m_id = id;
    }
    
    public void addProperty(final InteractiveElementProperty prop) {
        this.m_properties.add(prop);
    }
    
    public void addProperties(final Collection<InteractiveElementProperty> props) {
        this.m_properties.addAll((Collection<?>)props);
    }
    
    public final void setProperties(final Collection<InteractiveElementProperty> props) {
        this.m_properties.clear();
        if (props != null) {
            this.m_properties.addAll((Collection<?>)props);
        }
    }
    
    public void removeProperty(final InteractiveElementProperty prop) {
        this.m_properties.remove(prop);
    }
    
    public void removeProperties(final Collection<InteractiveElementProperty> props) {
        this.m_properties.removeAll(props);
    }
    
    public boolean hasProperty(final InteractiveElementProperty prop) {
        return this.m_properties.contains(prop);
    }
    
    public Iterator<InteractiveElementProperty> getProperties() {
        return this.m_properties.iterator();
    }
    
    public int getPropertySize() {
        return this.m_properties.size();
    }
    
    public final short getState() {
        return this.m_state;
    }
    
    public void setState(final short state) {
        this.m_state = state;
    }
    
    public final short getWorld() {
        return this.m_world;
    }
    
    public void setWorld(final short world) {
        this.m_world = world;
    }
    
    @Override
    public final Point3 getPosition() {
        return this.m_position;
    }
    
    public final boolean isBlockingLineOfSight() {
        return this.m_blockingLineOfSight;
    }
    
    public void setBlockingLineOfSight(final boolean blockingLineOfSight) {
        this.m_blockingLineOfSight = blockingLineOfSight;
    }
    
    public boolean isBlockingWholeCell() {
        return false;
    }
    
    public boolean isBlockingMovements() {
        return this.m_blockingMovements && this.m_visible;
    }
    
    public boolean isUsable() {
        return this.m_usable;
    }
    
    public void setUsable(final boolean usable) {
        this.m_usable = usable;
    }
    
    public boolean isVisible() {
        return this.m_visible;
    }
    
    public void setVisible(final boolean visible) {
        if (!(this.m_visible = visible)) {
            this.setBlockingMovements(false);
        }
    }
    
    public void setParameter(final String parameter) {
        this.m_parameter = parameter.intern();
    }
    
    protected void setPool(final MonitoredPool pool) {
        this.m_pool = pool;
    }
    
    public BinarSerialPart[] getSharedDatasParts() {
        return new BinarSerialPart[] { this.getSynchronizationPart(), this.getSynchronizationSpecificPart() };
    }
    
    @Override
    public void onCheckOut() {
        this.m_id = 0L;
        this.m_modelId = 0;
        this.m_position.reset();
        this.m_world = 0;
        this.m_state = 32767;
        this.m_visible = false;
        this.m_usable = false;
        this.m_activationPattern = 0;
        this.m_direction = Direction8.NORTH_EAST;
        this.m_parameter = null;
        this.m_blockingMovements = false;
        this.m_blockingLineOfSight = false;
        this.m_visibleContent = 0;
        this.m_isDummy = false;
    }
    
    @Override
    public void onCheckIn() {
        this.m_id = Long.MAX_VALUE;
        this.m_positionTriggers.clear();
        this.m_properties.clear();
        if (this.m_changesListeners != null) {
            this.m_changesListeners.clear();
            this.m_changesListeners = null;
        }
    }
    
    @Override
    public final void release() {
        if (this.m_pool != null) {
            try {
                this.m_pool.returnObject(this);
            }
            catch (Exception e) {
                MapInteractiveElement.m_logger.error((Object)"Erreur lors du retour au pool", (Throwable)e);
            }
            this.m_pool = null;
        }
        else {
            MapInteractiveElement.m_logger.error((Object)("Double release de " + this.getClass()), (Throwable)new Exception());
            this.onCheckIn();
        }
    }
    
    public final void addChangesListener(final InteractiveElementChangesListener changesListener) {
        if (this.m_changesListeners == null) {
            this.m_changesListeners = new ArrayList<InteractiveElementChangesListener>(1);
        }
        if (this.m_changesListeners.contains(changesListener)) {
            return;
        }
        this.m_changesListeners.add(changesListener);
    }
    
    public final void clearChangesListeners() {
        this.m_changesListeners.clear();
        this.m_changesListeners = null;
    }
    
    public boolean isTrigger(final Point3 cell) {
        return !this.m_positionTriggers.isEmpty() && this.m_positionTriggers.contains(cell);
    }
    
    public HashSet<Point3> getPositionTriggers() {
        return this.m_positionTriggers;
    }
    
    public final void notifyChangesListeners() {
        if (this.m_changesListeners != null) {
            for (final InteractiveElementChangesListener changesListener : this.m_changesListeners) {
                changesListener.onInteractiveElementChanges(this);
            }
        }
    }
    
    @Override
    public BinarSerialPart[] partsEnumeration() {
        return new BinarSerialPart[] { this.getGlobalDataPart(), this.getSpecificDataPart(), this.getSynchronizationPart(), this.getSynchronizationSpecificPart(), this.getPersistancePart(), this.getAdditionalPersistancePart() };
    }
    
    protected abstract BinarSerialPart getGlobalDataPart();
    
    protected abstract BinarSerialPart getSpecificDataPart();
    
    protected abstract BinarSerialPart getSynchronizationPart();
    
    protected abstract BinarSerialPart getSynchronizationSpecificPart();
    
    protected abstract BinarSerialPart getPersistancePart();
    
    protected abstract BinarSerialPart getAdditionalPersistancePart();
    
    public final void removeChangesListener(final InteractiveElementChangesListener changesListener) {
        if (this.m_changesListeners != null) {
            this.m_changesListeners.remove(changesListener);
        }
    }
    
    public void setBlockingMovements(final boolean blockingMovements) {
        this.m_blockingMovements = blockingMovements;
    }
    
    @Override
    public String toString() {
        return '[' + this.getClass().getSimpleName() + " id=" + this.m_id + ']' + this.getPosition();
    }
    
    protected abstract TopologyMapInstance getTopologyMap();
    
    public void setVisibleContent(final byte visibleContent) {
        this.m_visibleContent = visibleContent;
    }
    
    public byte getVisibleContent() {
        return this.m_visibleContent;
    }
    
    public void setModelId(final short modelId) {
        this.m_modelId = modelId;
    }
    
    public short getModelId() {
        return this.m_modelId;
    }
    
    public boolean isUsableInFight() {
        return false;
    }
    
    public void setIsDummy() {
        this.m_isDummy = true;
    }
    
    public void unsetDummy() {
        this.m_isDummy = false;
    }
    
    public boolean isDummy() {
        return this.m_isDummy;
    }
    
    public void onLoaded() {
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapInteractiveElement.class);
    }
}
