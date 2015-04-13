package com.ankamagames.baseImpl.graphics.game.worldPositionManager;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.interpolation.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;

public class ValuePoint
{
    private static final float INTERPOLATION_MOVEMENT_LIMIT = 0.01f;
    private static final float DEFAULT_MOVE_SPEED = 2.1f;
    private int m_startX;
    private int m_startY;
    private int m_startZ;
    private int m_watcherStartX;
    private int m_watcherStartY;
    private int m_watcherStartZ;
    private boolean m_init;
    private Object m_value;
    private IsoWorldTarget m_target;
    private int m_x;
    private int m_y;
    private int m_z;
    private String m_iconUrl;
    private int m_arrowApsId;
    private FreeParticleSystem m_arrow;
    private int m_screenX;
    private int m_screenY;
    private boolean m_onScreen;
    private Interpolator m_screenXI;
    private Interpolator m_screenYI;
    private Interpolator m_xI;
    private Interpolator m_yI;
    private Interpolator m_zI;
    private ValuePointManager m_manager;
    private ValuePointDeleteListener m_deleteListener;
    private final boolean m_forceZ;
    
    public ValuePoint(final ValuePointManager manager, final Object value, final int arrowApsId, final int startX, final int startY, final int startZ, final boolean forceZ) {
        super();
        this.m_watcherStartX = Integer.MIN_VALUE;
        this.m_watcherStartY = Integer.MIN_VALUE;
        this.m_watcherStartZ = Integer.MIN_VALUE;
        this.m_init = false;
        this.m_arrow = null;
        this.m_onScreen = false;
        this.m_manager = manager;
        this.m_value = value;
        this.m_forceZ = forceZ;
        (this.m_xI = new Interpolator()).setSpeed(2.1f);
        this.m_xI.setDelta(0.01f);
        (this.m_yI = new Interpolator()).setSpeed(2.1f);
        this.m_yI.setDelta(0.01f);
        (this.m_zI = new Interpolator()).setSpeed(2.1f);
        this.m_zI.setDelta(0.01f);
        (this.m_screenXI = new Interpolator()).setSpeed(2.1f);
        this.m_screenXI.setDelta(0.01f);
        (this.m_screenYI = new Interpolator()).setSpeed(2.1f);
        this.m_screenYI.setDelta(0.01f);
        this.m_arrowApsId = arrowApsId;
        this.m_startX = startX;
        this.m_startY = startY;
        this.m_startZ = startZ;
    }
    
    public void addToManager() {
        this.m_manager.addPoint(this);
    }
    
    public void setScreenPosition(final int screenX, final int screenY) {
        this.m_screenXI.set(screenX);
        this.m_screenYI.set(screenY);
    }
    
    public void updateScreenPosition(final int screenX, final int screenY) {
        this.m_screenXI.setEnd(screenX);
        this.m_screenYI.setEnd(screenY);
    }
    
    public int getScreenX() {
        return this.m_screenX;
    }
    
    public int getScreenY() {
        return this.m_screenY;
    }
    
    public int getArrowApsId() {
        return this.m_arrowApsId;
    }
    
    public String getIconUrl() {
        return this.m_iconUrl;
    }
    
    public void setIconUrl(final String iconUrl) {
        this.m_iconUrl = iconUrl;
    }
    
    public void setValue(final Object value) {
        this.m_value = value;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public void setOnScreen(final boolean onScreen, final boolean force) {
        if (this.m_init && !force && this.m_onScreen == onScreen) {
            return;
        }
        this.m_init = true;
        this.m_onScreen = onScreen;
        if (this.m_onScreen) {
            this.addMarkerWorldTarget();
        }
        else {
            this.removeMarketWorldTarget();
        }
    }
    
    public void setPosition(final int x, final int y, final int z) {
        this.m_xI.set(x);
        this.m_yI.set(y);
        this.m_zI.set(z);
    }
    
    public void updatePosition(final int x, final int y, final int z) {
        this.m_xI.setEnd(x);
        this.m_yI.setEnd(y);
        this.m_zI.setEnd(z);
    }
    
    public boolean isForceZ() {
        return this.m_forceZ;
    }
    
    public int getX() {
        return (int)this.m_xI.getEnd();
    }
    
    public int getY() {
        return (int)this.m_yI.getEnd();
    }
    
    public int getZ() {
        return (int)this.m_zI.getEnd();
    }
    
    public int getStartX() {
        return this.m_startX;
    }
    
    public int getStartY() {
        return this.m_startY;
    }
    
    public int getStartZ() {
        return this.m_startZ;
    }
    
    public int getWatcherStartX() {
        return this.m_watcherStartX;
    }
    
    public int getWatcherStartY() {
        return this.m_watcherStartY;
    }
    
    public int getWatcherStartZ() {
        return this.m_watcherStartZ;
    }
    
    public boolean isWatcherInit() {
        return this.m_watcherStartX != Integer.MIN_VALUE;
    }
    
    public void setWatcherStart(final int watcherStartX, final int watcherStartY, final int watcherStartZ) {
        this.m_watcherStartX = watcherStartX;
        this.m_watcherStartY = watcherStartY;
        this.m_watcherStartZ = watcherStartZ;
    }
    
    public IsoWorldTarget getTarget() {
        return this.m_target;
    }
    
    public void setTarget(final IsoWorldTarget target) {
        if (this.m_target == target) {
            return;
        }
        this.m_target = target;
        if (this.m_arrow != null) {
            this.m_arrow.setTarget(target);
        }
    }
    
    public void process(final int deltaTime) {
        this.m_screenX = (int)this.m_screenXI.process(deltaTime);
        this.m_screenY = (int)this.m_screenYI.process(deltaTime);
        this.m_x = (int)this.m_xI.process(deltaTime);
        this.m_y = (int)this.m_yI.process(deltaTime);
        this.m_z = (int)this.m_zI.process(deltaTime);
        if (this.m_arrow != null) {
            this.m_arrow.setWorldPosition(this.m_x, this.m_y, this.m_z);
        }
    }
    
    private void addMarkerWorldTarget() {
        if (this.m_arrow != null) {
            return;
        }
        this.m_arrow = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(this.m_arrowApsId, 0);
        if (this.m_arrow != null) {
            if (this.m_target != null) {
                this.m_arrow.setTarget(this.m_target);
            }
            else {
                this.m_arrow.setWorldPosition(this.m_x, this.m_y, this.m_z);
            }
            IsoParticleSystemManager.getInstance().addParticleSystem(this.m_arrow);
        }
    }
    
    private void removeMarketWorldTarget() {
        if (this.m_arrow == null) {
            return;
        }
        IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_arrow.getId());
        this.m_arrow = null;
    }
    
    private void onDelete() {
        if (this.m_deleteListener != null) {
            final ValuePointDeleteListener l = this.m_deleteListener;
            this.m_deleteListener = null;
            l.onValuePointDelete();
        }
    }
    
    public ValuePointDeleteListener getDeleteListener() {
        return this.m_deleteListener;
    }
    
    public void setDeleteListener(final ValuePointDeleteListener deleteListener) {
        this.m_deleteListener = deleteListener;
    }
    
    public void clear() {
        this.onDelete();
        this.m_manager.removePoint(this);
        this.m_target = null;
        this.removeMarketWorldTarget();
    }
}
