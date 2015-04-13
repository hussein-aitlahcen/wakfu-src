package com.ankamagames.baseImpl.graphics.alea.mobile;

import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.tween.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;

public abstract class Mobile extends AnimatedInteractiveElement
{
    protected static final Logger m_logger;
    protected final ArrayList<TargetPositionListener<PathMobile>> m_positionListeners;
    private Mobile m_carriedMobile;
    private Mobile m_carrierMobile;
    private ArrayList<Mobile> m_linkedChildrenMobile;
    private Mobile m_linkedParentMobile;
    private boolean m_useLinkedParentAnimation;
    private boolean m_useLinkedParentDirection;
    private boolean m_parentDisposeLinked;
    private byte m_status;
    public static final byte NORMAL = 0;
    public static final byte KO = 1;
    public static final byte DEAD = 2;
    private static final int UNCARRY_TRAJECTORY_DURATION = 500;
    private static final int UNCARRY_INITIAL_Z_VELOCITY = 1;
    private static final int UNCARRY_FINAL_Z_VELOCITY = 1;
    private int m_fightId;
    
    public Mobile(final long id) {
        super(id);
        this.m_positionListeners = new ArrayList<TargetPositionListener<PathMobile>>(2);
        this.m_fightId = -1;
    }
    
    public Mobile(final long id, final float worldX, final float worldY, final float altitude) {
        super(id, worldX, worldY, altitude);
        this.m_positionListeners = new ArrayList<TargetPositionListener<PathMobile>>(2);
        this.m_fightId = -1;
    }
    
    public Mobile(final long id, final float worldX, final float worldY) {
        super(id, worldX, worldY);
        this.m_positionListeners = new ArrayList<TargetPositionListener<PathMobile>>(2);
        this.m_fightId = -1;
    }
    
    public boolean isParentDisposeLinked() {
        return this.m_parentDisposeLinked;
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
        if (direction == null) {
            Mobile.m_logger.error((Object)"Unable to set a Direction8 to null", (Throwable)new Exception());
            return;
        }
        if (this.m_direction == direction) {
            return;
        }
        final int delta = direction.m_index - this.m_direction.m_index;
        this.m_direction = direction;
        this.m_directionChanged = true;
        if (this.m_linkedChildrenMobile != null) {
            for (int i = 0, size = this.m_linkedChildrenMobile.size(); i < size; ++i) {
                final Mobile child = this.m_linkedChildrenMobile.get(i);
                if (child.m_useLinkedParentDirection) {
                    child.setDirection(this.m_direction);
                }
            }
        }
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY, final float altitude) {
        final int previousCellX = this.getWorldCellX();
        final int previousCellY = this.getWorldCellY();
        final int previousCellAltitude = this.getWorldCellAltitude();
        super.setWorldPosition(worldX, worldY, altitude);
        final int worldCellX = this.getWorldCellX();
        final int worldCellY = this.getWorldCellY();
        if (worldCellX != previousCellX || worldCellY != previousCellY) {
            this.onCellTransition(new int[] { this.getWorldCellX(), this.getWorldCellY(), Math.round(this.getAltitude()) }, new int[] { previousCellX, previousCellY, previousCellAltitude });
        }
        if (this.m_carriedMobile != null) {
            this.m_carriedMobile.setWorldPosition(worldX, worldY, altitude + this.getHeight());
        }
        if (this.m_linkedChildrenMobile != null) {
            for (int i = 0, size = this.m_linkedChildrenMobile.size(); i < size; ++i) {
                this.m_linkedChildrenMobile.get(i).setWorldPosition(worldX, worldY, altitude + this.getHeight());
            }
        }
    }
    
    protected void setCarriedMobile(final Mobile carriedMobile) {
        this.m_carriedMobile = carriedMobile;
    }
    
    public Mobile getCarriedMobile() {
        return this.m_carriedMobile;
    }
    
    protected void setCarrierMobile(final Mobile carrierMobile) {
        this.m_carrierMobile = carrierMobile;
    }
    
    public Mobile getCarrierMobile() {
        return this.m_carrierMobile;
    }
    
    public boolean isCarried() {
        return this.m_carrierMobile != null;
    }
    
    public boolean isCarrier() {
        return this.m_carriedMobile != null;
    }
    
    public void carry(final Mobile carriedMobile) {
        if (carriedMobile == this || carriedMobile == null) {
            return;
        }
        carriedMobile.setCarrierMobile(this);
        this.setCarriedMobile(carriedMobile);
        carriedMobile.forceReloadAnimation();
    }
    
    @Override
    public boolean setAnimation(final String animation) {
        final boolean changed = super.setAnimation(animation);
        if (changed && this.m_linkedChildrenMobile != null) {
            for (int i = 0, size = this.m_linkedChildrenMobile.size(); i < size; ++i) {
                final Mobile child = this.m_linkedChildrenMobile.get(i);
                if (child.m_useLinkedParentAnimation) {
                    child.setAnimation(animation);
                }
            }
        }
        return changed;
    }
    
    @Override
    public boolean addToScene(final AleaWorldScene scene) {
        if (!super.addToScene(scene)) {
            return false;
        }
        if (this.m_carrierMobile != null) {
            final Matrix44 childMatrix = this.m_carrierMobile.getAnmInstance().getCarrierMatrix();
            if (childMatrix == null) {
                return false;
            }
            this.applyLinkToMobile(scene, childMatrix, this.m_carrierMobile);
            this.getEntity().m_zOrder = this.m_carrierMobile.getEntity().m_zOrder + 1L;
            this.setMaskKey(this.m_carrierMobile.getMaskKey(), this.m_carrierMobile.getLayerId());
        }
        else if (this.m_linkedParentMobile != null && this.m_linkedParentMobile.getAnmInstance() != null) {
            final Matrix44 parentMatrix = this.m_linkedParentMobile.getAnmInstance().getLinkMatrix();
            this.applyLinkToMobile(scene, parentMatrix, this.m_linkedParentMobile);
        }
        return true;
    }
    
    public Mobile uncarry() {
        if (this.m_carriedMobile != null) {
            this.setAnimationSuffix(null);
            this.m_carriedMobile.setCarrierMobile(null);
            final Mobile carriedMobile = this.m_carriedMobile;
            this.setCarriedMobile(null);
            carriedMobile.forceReloadAnimation();
            return carriedMobile;
        }
        return null;
    }
    
    public Mobile uncarry(final boolean playUncarryTrajectory, final Point3 position) {
        if (this.m_carriedMobile != null) {
            if (playUncarryTrajectory) {
                this.setAnimationSuffix(null);
                this.setUncarryTrajectory(position);
            }
            this.m_carriedMobile.setCarrierMobile(null);
            final Mobile carriedMobile = this.m_carriedMobile;
            this.setCarriedMobile(null);
            if (playUncarryTrajectory) {
                carriedMobile.forceReloadAnimation();
            }
            return carriedMobile;
        }
        return null;
    }
    
    private void setUncarryTrajectory(final Point3 position) {
        final CubicSplineTween trajectory = new CubicSplineTween(this.m_carriedMobile);
        trajectory.setFinalPosition(new Vector3(position.getX(), position.getY(), position.getZ()));
        trajectory.setInitialPosition(new Vector3(this.m_carriedMobile.getWorldCellX(), this.m_carriedMobile.getWorldCellY(), this.m_carriedMobile.getWorldCellAltitude() + this.getHeight()));
        trajectory.setDuration(500L);
        trajectory.setInitialVelocity(new Vector3(0.0f, 0.0f, 1.0f));
        trajectory.setFinalVelocity(new Vector3(0.0f, 0.0f, 1.0f));
        TweenManager.getInstance().addTween(trajectory);
    }
    
    public void linkMobile(final Mobile toLink) {
        this.linkMobile(toLink, false, false, true);
    }
    
    public void linkMobile(final Mobile toLink, final boolean useParentAnimation, final boolean parentDisposeLinked, final boolean useParentDirection) {
        assert this.m_linkedParentMobile == null : "A mobile is already linked to this mobile";
        assert toLink != null : "You can't link a null mobile";
        if (this.m_linkedChildrenMobile == null) {
            this.m_linkedChildrenMobile = new ArrayList<Mobile>(1);
        }
        this.m_linkedChildrenMobile.add(toLink);
        toLink.m_linkedParentMobile = this;
        toLink.m_useLinkedParentAnimation = useParentAnimation;
        toLink.m_parentDisposeLinked = parentDisposeLinked;
        toLink.m_useLinkedParentDirection = useParentDirection;
        this.forceReloadAnimation();
        toLink.forceReloadAnimation();
        toLink.applyParentLightning();
    }
    
    public final void unlinkChildrenMobile() {
        if (this.m_linkedChildrenMobile == null) {
            return;
        }
        for (int i = 0, size = this.m_linkedChildrenMobile.size(); i < size; ++i) {
            final Mobile child = this.m_linkedChildrenMobile.get(i);
            child.m_linkedParentMobile = null;
            child.forceReloadAnimation();
        }
        this.m_linkedChildrenMobile.clear();
        this.m_linkedChildrenMobile = null;
    }
    
    public final void unlinkChildMobile(final Mobile child) {
        if (this.m_linkedChildrenMobile == null) {
            return;
        }
        this.m_linkedChildrenMobile.remove(child);
        child.m_linkedParentMobile = null;
        child.forceReloadAnimation();
    }
    
    public ArrayList<Mobile> getLinkedChildrenMobile() {
        return this.m_linkedChildrenMobile;
    }
    
    public Mobile getLinkedParentMobile() {
        return this.m_linkedParentMobile;
    }
    
    public final void addPositionListener(final TargetPositionListener<PathMobile> listener) {
        if (listener != null && !this.m_positionListeners.contains(listener)) {
            this.m_positionListeners.add(listener);
        }
    }
    
    public final boolean containsPositionListener(final TargetPositionListener<PathMobile> listener) {
        return this.m_positionListeners.contains(listener);
    }
    
    public final boolean removePositionListener(final TargetPositionListener<PathMobile> listener) {
        return listener != null && this.m_positionListeners.remove(listener);
    }
    
    public final void removeAllPositionListeners() {
        this.m_positionListeners.clear();
    }
    
    protected void onCellTransition(final int[] nextCell, final int[] previousCell) {
        if (this.m_positionListeners != null) {
            final TargetPositionListener[] arr$;
            final TargetPositionListener[] listeners = arr$ = this.m_positionListeners.toArray(new TargetPositionListener[this.m_positionListeners.size()]);
            for (final TargetPositionListener listener : arr$) {
                listener.cellPositionChanged(this, nextCell[0], nextCell[1], (short)nextCell[2]);
            }
        }
        if (this.getCarriedMobile() != null) {
            this.getCarriedMobile().onCellTransition(nextCell, previousCell);
        }
    }
    
    public void setStatus(final byte status) {
        this.m_status = status;
    }
    
    public byte getStatus() {
        return this.m_status;
    }
    
    public void setFightId(final int fightId) {
        this.m_fightId = fightId;
    }
    
    @Override
    public int getCurrentFightId() {
        return this.m_fightId;
    }
    
    private void applyLinkToMobile(final AleaWorldScene scene, final Matrix44 parentMatrix, final Mobile mobile) {
        final TransformerMatrix carriedMatrix = new TransformerMatrix();
        final Matrix44 m = carriedMatrix.getMatrix();
        m.set(parentMatrix);
        m.translate(mobile.getPosition(scene));
        final float defScale = mobile.getAnmInstance().getScale();
        final float[] buffer2;
        final float[] buffer = buffer2 = m.getBuffer();
        final int n = 0;
        buffer2[n] /= defScale;
        final float[] array = buffer;
        final int n2 = 5;
        array[n2] /= defScale;
        final BatchTransformer batchTransformer = this.getEntity().getTransformer();
        batchTransformer.setTransformer(0, carriedMatrix);
        m.removeReference();
    }
    
    @Override
    public void applyLighting(final float[] colors) {
        if (this.m_linkedParentMobile != null) {
            this.applyParentLightning();
        }
        else {
            super.applyLighting(colors);
            if (this.m_linkedChildrenMobile != null) {
                for (int i = 0, size = this.m_linkedChildrenMobile.size(); i < size; ++i) {
                    this.m_linkedChildrenMobile.get(i).applyParentLightning();
                }
            }
        }
    }
    
    protected void applyParentLightning() {
        if (this.m_linkedParentMobile == null) {
            return;
        }
        final Material parentMaterial = this.m_linkedParentMobile.getMaterial();
        this.m_material.setDiffuseColor(parentMaterial.getDiffuseColor());
        this.getAnmEntity().updateMaterial(this.m_material);
    }
    
    public void setCustomColorFrom(final Mobile fromMobile) {
        final AnmInstance anmInstance = this.getAnmInstance();
        if (anmInstance == null) {
            return;
        }
        final AnmInstance from = fromMobile.getAnmInstance();
        if (from == null) {
            return;
        }
        anmInstance.copyCustomColor(from);
    }
    
    public void hideDirectionParticleSystem() {
    }
    
    public void showDirectionParticleSystem() {
    }
    
    static {
        m_logger = Logger.getLogger((Class)Mobile.class);
    }
}
