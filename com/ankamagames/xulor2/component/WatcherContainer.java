package com.ankamagames.xulor2.component;

import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import java.awt.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public class WatcherContainer extends Container implements ScreenTargetWatcher, AdviserContainerComponent, EventListener
{
    public static final String TAG = "WatcherContainer";
    protected ScreenTarget m_target;
    protected boolean m_targetIsVisible;
    private Alignment9 m_align;
    private int m_xOffset;
    private int m_yOffset;
    private boolean m_useTargetPositionning;
    protected WatcherContainerAdviser m_watcherContainerAdviser;
    public static final int ALIGN_HASH;
    public static final int TARGET_HASH;
    public static final int USE_TARGET_POSITIONNING_HASH;
    public static final int X_OFFSET_HASH;
    public static final int Y_OFFSET_HASH;
    
    public WatcherContainer() {
        super();
        this.m_targetIsVisible = true;
        this.m_align = null;
        this.m_xOffset = 0;
        this.m_yOffset = 0;
        this.m_useTargetPositionning = true;
    }
    
    public WatcherContainerAdviser getWatcherContainerAdviser() {
        return this.m_watcherContainerAdviser;
    }
    
    protected void process(final int deltaTime, final float zoomFactor) {
    }
    
    @Override
    public int validateAdviser() {
        return AdviserManager.getInstance().addAdviser(this.m_watcherContainerAdviser);
    }
    
    @Override
    public void onAdviserCleanUp() {
    }
    
    @Override
    public float getWorldX() {
        if (this.m_target != null) {
            return this.m_target.getWorldX();
        }
        return 0.0f;
    }
    
    @Override
    public float getWorldY() {
        if (this.m_target != null) {
            return this.m_target.getWorldY();
        }
        return 0.0f;
    }
    
    @Override
    public float getAltitude() {
        if (this.m_target != null) {
            return this.m_target.getAltitude();
        }
        return 0.0f;
    }
    
    @Override
    public String getTag() {
        return "WatcherContainer";
    }
    
    public Alignment9 getAlign() {
        return this.m_align;
    }
    
    public void setAlign(final Alignment9 align) {
        this.m_align = align;
    }
    
    public ScreenTarget getTarget() {
        return this.m_target;
    }
    
    public void setTarget(final ScreenTarget target) {
        if (this.m_target != target) {
            if (this.m_target != null) {
                this.m_target.removeWatcher(this);
            }
            this.m_target = target;
            if (this.m_target != null) {
                this.m_target.addWatcher(this);
            }
        }
        if (this.m_target != null) {
            this.screenTargetMoved(null, this.m_target.getScreenX(), this.m_target.getScreenY(), this.m_target.getScreenTargetHeight());
        }
    }
    
    public void setTarget(final ScreenTarget target, final int offsetX, final int offsetY) {
        this.m_xOffset = offsetX;
        this.m_yOffset = offsetY;
        if (this.m_target != null && this.m_target != target) {
            this.m_target.removeWatcher(this);
        }
        this.m_target = target;
        if (this.m_target != null) {
            this.m_target.addWatcher(this);
            this.screenTargetMoved(null, this.m_target.getScreenX(), this.m_target.getScreenY(), this.m_target.getScreenTargetHeight());
        }
    }
    
    public boolean isUseTargetPositionning() {
        return this.m_useTargetPositionning;
    }
    
    public void setUseTargetPositionning(final boolean onlyTargetPositionning) {
        this.m_useTargetPositionning = onlyTargetPositionning;
    }
    
    @Override
    public int getXOffset() {
        return this.m_xOffset;
    }
    
    public void setXOffset(final int offset) {
        this.m_xOffset = offset;
        if (this.m_target != null) {
            this.screenTargetMoved(null, this.m_target.getScreenX(), this.m_target.getScreenY(), this.m_target.getScreenTargetHeight());
        }
    }
    
    @Override
    public int getYOffset() {
        return this.m_yOffset;
    }
    
    public void setYOffset(final int offset) {
        this.m_yOffset = offset;
        if (this.m_target != null) {
            this.screenTargetMoved(null, this.m_target.getScreenX(), this.m_target.getScreenY(), this.m_target.getScreenTargetHeight());
        }
    }
    
    public void setOffset(final int offsetX, final int offsetY) {
        this.m_xOffset = offsetX;
        this.m_yOffset = offsetY;
        if (this.m_target != null && this.m_target.isPositionComputed()) {
            this.screenTargetMoved(null, this.m_target.getScreenX(), this.m_target.getScreenY(), this.m_target.getScreenTargetHeight());
        }
    }
    
    @Override
    public void setPosition(final int x, final int y) {
        if (!this.m_useTargetPositionning) {
            super.setPosition(x, y);
        }
    }
    
    @Override
    public void setPosition(final int x, final int y, final boolean b) {
        if (!this.m_useTargetPositionning) {
            super.setPosition(x, y, b);
        }
    }
    
    @Override
    public void setPosition(final Point position) {
        if (!this.m_useTargetPositionning) {
            super.setPosition(position);
        }
    }
    
    @Override
    public void setX(final int x) {
        if (!this.m_useTargetPositionning) {
            super.setX(x);
        }
    }
    
    @Override
    public void setY(final int y) {
        if (!this.m_useTargetPositionning) {
            super.setY(y);
        }
    }
    
    @Override
    public void setTargetIsVisible(final boolean visible) {
        this.m_targetIsVisible = visible;
    }
    
    protected void updateVisibility() {
        this.setVisible(this.m_targetIsVisible);
    }
    
    @Override
    public void removedFromWidgetTree() {
        super.removedFromWidgetTree();
        if (this.m_target != null) {
            this.m_target.removeWatcher(this);
        }
    }
    
    @Override
    public void screenTargetMoved(final ScreenTarget target, final int x, final int y, final int height) {
        if (this.m_useTargetPositionning) {
            final Point2i position = this.getComputedPosition(x, y, height);
            super.setPosition(position.getX(), position.getY(), false);
        }
    }
    
    protected final int getHalfDisplayWidth() {
        return MasterRootContainer.getInstance().getWidth() / 2;
    }
    
    protected final int getHalfDisplayHeight() {
        return MasterRootContainer.getInstance().getHeight() / 2;
    }
    
    public Point2i getComputedPosition(final int x, final int y, final int height) {
        return new Point2i(x + this.getHalfDisplayWidth() + (int)(this.m_xOffset * this.m_watcherContainerAdviser.getZoomFactor()) - this.m_align.getX(this.getWidth()), y + this.getHalfDisplayHeight() + (int)(this.m_yOffset * this.m_watcherContainerAdviser.getZoomFactor()) + this.m_align.getY(height));
    }
    
    @Override
    public void validate() {
        super.validate();
        if (this.m_target != null) {
            this.screenTargetMoved(null, this.m_target.getScreenX(), this.m_target.getScreenY(), this.m_target.getScreenTargetHeight());
        }
    }
    
    @Override
    public boolean run(final Event event) {
        if (event.getType() != Events.RESIZED) {
            return false;
        }
        if (this.m_target != null) {
            this.screenTargetMoved(null, this.m_target.getScreenX(), this.m_target.getScreenY(), this.m_target.getScreenTargetHeight());
        }
        return false;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        AdviserManager.getInstance().removeAdviser(this.m_watcherContainerAdviser);
        this.m_align = null;
        this.m_target = null;
        this.m_watcherContainerAdviser = null;
        MasterRootContainer.getInstance().removeEventListener(Events.RESIZED, this, false);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setVisible(true);
        this.m_useTargetPositionning = true;
        this.m_align = Alignment9.NORTH;
        this.m_watcherContainerAdviser = new WatcherContainerAdviser();
        MasterRootContainer.getInstance().addEventListener(Events.RESIZED, this, false);
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        super.copyElement(c);
        final WatcherContainer wc = (WatcherContainer)c;
        wc.setUseTargetPositionning(this.isUseTargetPositionning());
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == WatcherContainer.ALIGN_HASH) {
            this.setAlign(Alignment9.value(value));
        }
        else if (hash == WatcherContainer.USE_TARGET_POSITIONNING_HASH) {
            this.setUseTargetPositionning(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == WatcherContainer.X_OFFSET_HASH) {
            this.setXOffset(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != WatcherContainer.Y_OFFSET_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setYOffset(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == WatcherContainer.ALIGN_HASH) {
            this.setAlign((Alignment9)value);
        }
        else if (hash == WatcherContainer.USE_TARGET_POSITIONNING_HASH) {
            this.setUseTargetPositionning(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == WatcherContainer.X_OFFSET_HASH) {
            this.setXOffset(PrimitiveConverter.getInteger(value));
        }
        else if (hash == WatcherContainer.Y_OFFSET_HASH) {
            this.setYOffset(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != WatcherContainer.TARGET_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setTarget((ScreenTarget)value);
        }
        return true;
    }
    
    static {
        ALIGN_HASH = "align".hashCode();
        TARGET_HASH = "target".hashCode();
        USE_TARGET_POSITIONNING_HASH = "useTargetPositionning".hashCode();
        X_OFFSET_HASH = "xOffset".hashCode();
        Y_OFFSET_HASH = "yOffset".hashCode();
    }
    
    protected class WatcherContainerAdviser implements Adviser
    {
        private int m_typeId;
        public final int INFINITE_DURATION = -1;
        private int m_duration;
        private int m_elapsedLifeTime;
        private int m_id;
        private float m_zoomFactor;
        
        private WatcherContainerAdviser() {
            super();
            this.m_duration = -1;
            this.m_elapsedLifeTime = 0;
            this.m_zoomFactor = 1.0f;
        }
        
        @Override
        public WorldPositionable getTarget() {
            return WatcherContainer.this.m_target;
        }
        
        @Override
        public void setTarget(final WorldPositionable target) {
        }
        
        @Override
        public int getXOffset() {
            return WatcherContainer.this.getXOffset();
        }
        
        @Override
        public void setXOffset(final int offset) {
            WatcherContainer.this.m_xOffset = offset;
        }
        
        @Override
        public int getYOffset() {
            return WatcherContainer.this.getYOffset();
        }
        
        @Override
        public void setYOffset(final int offset) {
            WatcherContainer.this.m_yOffset = offset;
        }
        
        @Override
        public float getWorldX() {
            return WatcherContainer.this.getWorldX();
        }
        
        @Override
        public float getWorldY() {
            return WatcherContainer.this.getWorldY();
        }
        
        @Override
        public float getAltitude() {
            if (WatcherContainer.this.m_target != null) {
                return WatcherContainer.this.m_target.getAltitude();
            }
            return 0.0f;
        }
        
        @Override
        public void setPosition(final float x, final float y, final float deltaX, final float deltaY) {
            WatcherContainer.this.screenTargetMoved(null, WatcherContainer.this.m_target.getScreenX(), WatcherContainer.this.m_target.getScreenY(), 0);
        }
        
        @Override
        public int getDuration() {
            return this.m_duration;
        }
        
        @Override
        public boolean isAlive() {
            return this.m_duration == -1 || this.m_elapsedLifeTime <= this.m_duration;
        }
        
        @Override
        public void process(final AleaWorldScene scene, final int deltaTime) {
            this.m_zoomFactor = scene.getIsoCamera().getZoomFactor();
            this.process(deltaTime);
            WatcherContainer.this.process(deltaTime, this.m_zoomFactor);
        }
        
        @Override
        public void process(final int deltaTime) {
            this.m_elapsedLifeTime += deltaTime;
        }
        
        @Override
        public int getId() {
            return this.m_id;
        }
        
        @Override
        public void setId(final int id) {
            this.m_id = id;
        }
        
        @Override
        public int getTypeId() {
            return this.m_typeId;
        }
        
        @Override
        public void setTypeId(final int typeId) {
            this.m_typeId = typeId;
        }
        
        @Override
        public Entity getEntity() {
            return null;
        }
        
        @Override
        public void cleanUp() {
            WatcherContainer.this.onAdviserCleanUp();
        }
        
        @Override
        public boolean needsToPrecomputeZoom() {
            return false;
        }
        
        public void setElapsedLifeTime(final int elapsedLifeTime) {
            this.m_elapsedLifeTime = elapsedLifeTime;
        }
        
        public int getElapsedLifeTime() {
            return this.m_elapsedLifeTime;
        }
        
        public void setDuration(final int duration) {
            this.m_duration = duration;
        }
        
        public float getZoomFactor() {
            return this.m_zoomFactor;
        }
    }
}
