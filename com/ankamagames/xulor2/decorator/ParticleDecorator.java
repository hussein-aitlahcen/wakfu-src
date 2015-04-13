package com.ankamagames.xulor2.decorator;

import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.decorator.mesh.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import java.awt.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.xulor2.component.*;

public class ParticleDecorator extends AbstractDecorator implements ModulationColorClient
{
    public static final String TAG = "Particle";
    private int m_x;
    private int m_y;
    private Alignment9 m_alignment;
    private boolean m_useParentScissor;
    private XulorParticleSystem m_particleSystem;
    private EntityGroup m_entity;
    private XulorParticleSystemRenderStates m_renderStates;
    private Vector4 m_position;
    private boolean m_followBorders;
    private float m_speed;
    private int m_timeToLive;
    private boolean m_moveClockWise;
    private Vector4 m_direction;
    private String m_fileName;
    private int m_level;
    private float m_zoom;
    private Color m_modulationColor;
    private boolean m_positionInit;
    private int m_turnNumber;
    private int m_currentTurnNumber;
    public static final int ALIGNMENT_HASH;
    public static final int FILE_HASH;
    public static final int FOLLOW_BORDERS_HASH;
    public static final int LEVEL_HASH;
    public static final int MOVE_CLOCK_WISE_HASH;
    public static final int SPEED_HASH;
    public static final int TIME_TO_LIVE_HASH;
    public static final int USE_PARENT_SCISSOR_HASH;
    public static final int X_HASH;
    public static final int Y_HASH;
    public static final int ZOOM_HASH;
    public static final int TURN_NUMBER_HASH;
    
    public ParticleDecorator() {
        super();
        this.m_useParentScissor = false;
        this.m_followBorders = false;
        this.m_speed = 200.0f;
        this.m_position = new Vector4(0.0f, 0.0f, 0.0f);
        this.m_moveClockWise = true;
    }
    
    @Override
    public final AbstractDecoratorMesh getMesh() {
        return null;
    }
    
    @Override
    public String getTag() {
        return "Particle";
    }
    
    @Override
    public final Entity getEntity() {
        return null;
    }
    
    public final void setFile(final String fileName) {
        this.m_fileName = fileName;
        this.setNeedsToPreProcess();
    }
    
    public final void setLevel(final int level) {
        this.m_level = level;
        if (this.m_fileName != null) {
            this.setNeedsToPreProcess();
        }
    }
    
    private void applyModulationColor() {
        if (this.m_particleSystem != null) {
            if (this.m_modulationColor != null) {
                this.m_particleSystem.setGlobalColor(this.m_modulationColor.getRed() * this.m_modulationColor.getAlpha(), this.m_modulationColor.getGreen() * this.m_modulationColor.getAlpha(), this.m_modulationColor.getBlue() * this.m_modulationColor.getAlpha(), this.m_modulationColor.getAlpha());
            }
            else {
                this.m_particleSystem.setGlobalColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
    
    @Override
    public void setModulationColor(final Color c) {
        this.m_modulationColor = c;
        this.applyModulationColor();
    }
    
    @Override
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    public final void setX(final int x) {
        this.m_x = x;
        this.m_position.setX(this.m_x);
    }
    
    public final void setY(final int y) {
        this.m_y = y;
        this.m_position.setY(this.m_y);
    }
    
    public final void setAlignment(final Alignment9 alignment) {
        this.m_alignment = alignment;
    }
    
    public final void setUseParentScissor(final boolean useParentScissor) {
        this.m_useParentScissor = useParentScissor;
        this.m_renderStates.setUseParentScissor(this.m_useParentScissor);
    }
    
    public void setFollowBorders(final boolean followBorders) {
        this.m_followBorders = followBorders;
    }
    
    public float getZoom() {
        return this.m_zoom;
    }
    
    public void setZoom(final float zoom) {
        this.m_zoom = zoom;
        final TransformerSRT transformer = (TransformerSRT)this.m_entity.getTransformer().getTransformer(0);
        transformer.setScale(this.m_zoom, this.m_zoom, this.m_zoom);
        this.m_entity.getTransformer().setToUpdate();
    }
    
    public void setSpeed(final float speed) {
        this.m_speed = speed;
    }
    
    public void setMoveClockWise(final boolean moveClockWise) {
        this.m_moveClockWise = moveClockWise;
    }
    
    public void setTimeToLive(final int timeToLive) {
        if (this.m_timeToLive == timeToLive) {
            return;
        }
        this.m_timeToLive = timeToLive;
        if (this.m_particleSystem != null) {
            this.m_particleSystem.setDuration(timeToLive);
        }
    }
    
    public void setParticleSystem(final XulorParticleSystem particleSystem) {
        if (this.m_particleSystem != null) {
            this.m_particleSystem.removeReference();
        }
        this.m_particleSystem = particleSystem;
        if (this.m_particleSystem != null) {
            this.m_particleSystem.addReference();
            if (this.m_timeToLive > 0) {
                this.m_particleSystem.setDuration(this.m_timeToLive);
            }
        }
        this.applyModulationColor();
    }
    
    @Override
    public final boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_particleSystem != null) {
            if (this.m_particleSystem.isAlive()) {
                this.m_particleSystem.stopAndKill();
            }
            this.m_particleSystem.removeReference();
            this.m_particleSystem = null;
            this.m_entity.removeAllChildren();
        }
        this.createParticleSystem();
        this.applyModulationColor();
        return ret;
    }
    
    @Override
    public final boolean postProcess(final int deltaTime) {
        super.postProcess(deltaTime);
        this.m_entity.removeAllChildren();
        this.updatePosition(deltaTime);
        if (this.m_particleSystem != null && this.m_particleSystem.getNumReferences() < 0) {
            this.destroySelfFromParent();
            return false;
        }
        if (this.m_particleSystem != null) {
            this.m_particleSystem.prepareParticlesBeforeRendering(this.m_entity);
        }
        if (this.m_entity.getParent() == null) {
            final EntityGroup parentEntity = this.getParentEntity();
            if (parentEntity != null) {
                parentEntity.removeChild(this.m_entity);
                parentEntity.addChild(this.m_entity);
            }
        }
        if (this.m_particleSystem != null && this.m_particleSystem.getNumReferences() < 0) {
            this.destroySelfFromParent();
            return false;
        }
        return true;
    }
    
    @Override
    public final void updateDecorator(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        final ParticleDecorator pd = (ParticleDecorator)source;
        pd.setFile(this.m_fileName);
        pd.setAlignment(this.m_alignment);
        pd.setFollowBorders(this.m_followBorders);
        pd.setLevel(this.m_level);
        pd.setMoveClockWise(this.m_moveClockWise);
        pd.setSpeed(this.m_speed);
        pd.setTimeToLive(this.m_timeToLive);
        pd.setUseParentScissor(this.m_useParentScissor);
        pd.setX(this.m_x);
        pd.setY(this.m_y);
        pd.setParticleSystem(this.m_particleSystem);
        pd.setTurnNumber(this.m_turnNumber);
    }
    
    @Override
    public void onCheckOut() {
        assert this.m_entity == null;
        super.onCheckOut();
        this.m_turnNumber = -1;
        this.m_currentTurnNumber = 0;
        this.m_positionInit = false;
        this.m_timeToLive = -1;
        this.m_level = ParticleSystemFactory.SYSTEM_WITHOUT_LEVEL;
        this.setNeedsToPostProcess();
        this.m_entity = EntityGroup.Factory.newPooledInstance();
        this.m_entity.m_owner = this;
        this.m_renderStates = new XulorParticleSystemRenderStates();
        this.m_entity.setPreRenderStates(this.m_renderStates);
        this.m_entity.setPreRenderStates(this.m_renderStates);
        this.m_entity.getTransformer().addTransformer(new TransformerSRT());
        this.m_renderStates.setUseParentScissor(this.m_useParentScissor);
        if (this.m_moveClockWise) {
            this.m_direction = new Vector4(1.0f, 0.0f, 0.0f);
        }
        else {
            this.m_direction = new Vector4(-1.0f, 0.0f, 0.0f);
        }
        this.m_zoom = 1.0f;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_fileName = null;
        if (this.m_particleSystem != null && this.m_particleSystem.isAlive()) {
            this.m_particleSystem.stopAndKill();
            this.m_particleSystem.removeReference();
            this.m_particleSystem = null;
        }
        final EntityGroup parentEntity = this.getParentEntity();
        if (parentEntity != null) {
            parentEntity.removeChild(this.m_entity);
        }
        this.m_entity.removeAllChildren();
        this.m_entity.removeReference();
        this.m_entity = null;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ParticleDecorator.ALIGNMENT_HASH) {
            this.setAlignment(Alignment9.value(value));
        }
        else if (hash == ParticleDecorator.FILE_HASH) {
            this.setFile(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == ParticleDecorator.FOLLOW_BORDERS_HASH) {
            this.setFollowBorders(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ParticleDecorator.LEVEL_HASH) {
            this.setLevel(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ParticleDecorator.MOVE_CLOCK_WISE_HASH) {
            this.setMoveClockWise(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ParticleDecorator.SPEED_HASH) {
            this.setSpeed(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ParticleDecorator.TIME_TO_LIVE_HASH) {
            this.setTimeToLive(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ParticleDecorator.USE_PARENT_SCISSOR_HASH) {
            this.setUseParentScissor(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ParticleDecorator.X_HASH) {
            this.setX(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ParticleDecorator.Y_HASH) {
            this.setY(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ParticleDecorator.ZOOM_HASH) {
            this.setZoom(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != ParticleDecorator.TURN_NUMBER_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setTurnNumber(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ParticleDecorator.ALIGNMENT_HASH) {
            this.setAlignment((Alignment9)value);
        }
        else if (hash == ParticleDecorator.FILE_HASH) {
            this.setFile((String)value);
        }
        else if (hash == ParticleDecorator.FOLLOW_BORDERS_HASH) {
            this.setFollowBorders(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ParticleDecorator.LEVEL_HASH) {
            this.setLevel(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ParticleDecorator.MOVE_CLOCK_WISE_HASH) {
            this.setMoveClockWise(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ParticleDecorator.SPEED_HASH) {
            this.setSpeed(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ParticleDecorator.TIME_TO_LIVE_HASH) {
            this.setTimeToLive(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ParticleDecorator.USE_PARENT_SCISSOR_HASH) {
            this.setUseParentScissor(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ParticleDecorator.X_HASH) {
            this.setX(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ParticleDecorator.Y_HASH) {
            this.setY(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ParticleDecorator.ZOOM_HASH) {
            this.setZoom(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != ParticleDecorator.TURN_NUMBER_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setTurnNumber(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    private void createParticleSystem() {
        assert this.m_particleSystem == null : "Particle system is already initialized";
        if (this.m_fileName == null) {
            return;
        }
        final String filePath = Xulor.getInstance().m_particlePath + this.m_fileName;
        this.m_particleSystem = XulorParticleSystemFactory.getInstance().getFreeParticleSystem(filePath, this.m_level);
        if (this.m_particleSystem != null) {
            if (this.m_timeToLive > 0) {
                this.m_particleSystem.setDuration(this.m_timeToLive);
            }
            XulorParticleSystemManager.INSTANCE.addParticleSystem(this.m_particleSystem);
        }
    }
    
    private void updatePosition(final int deltaTime) {
        final Widget parentWidget = this.getParentOfType(Widget.class);
        if (parentWidget == null) {
            return;
        }
        final Dimension size = parentWidget.getSize();
        if (this.m_followBorders) {
            if (!this.m_positionInit) {
                this.m_position.set((this.m_x + this.m_alignment.getX(size.width)) / this.m_zoom, (this.m_y + this.m_alignment.getY(size.height)) / this.m_zoom, 0.0f);
                this.m_positionInit = true;
            }
            this.followBorders(deltaTime, size.width, size.height);
        }
        else {
            this.m_position.set((this.m_x + this.m_alignment.getX(size.width)) / this.m_zoom, (this.m_y + this.m_alignment.getY(size.height)) / this.m_zoom, 0.0f);
        }
        if (this.m_particleSystem != null) {
            this.m_particleSystem.setPosition(this.m_position.getX(), this.m_position.getY());
        }
    }
    
    private void followBorders(final int deltaTime, final int width, final int height) {
        this.m_position.setAddMult(this.m_speed * deltaTime / 1000.0f, this.m_direction);
        if (this.m_position.getX() > width) {
            this.m_position.setX(width);
            if (this.m_position.getY() == 0.0f) {
                this.m_direction.set(0.0f, 1.0f, 0.0f);
                this.onAlignmentReached(Alignment9.SOUTH_EAST, false);
            }
            else {
                this.m_direction.set(0.0f, -1.0f, 0.0f);
                this.onAlignmentReached(Alignment9.NORTH_EAST, true);
            }
        }
        else if (this.m_position.getX() < 0.0f) {
            this.m_position.setX(0.0f);
            if (this.m_position.getY() == 0.0f) {
                this.m_direction.set(0.0f, 1.0f, 0.0f);
                this.onAlignmentReached(Alignment9.SOUTH_WEST, true);
            }
            else {
                this.m_direction.set(0.0f, -1.0f, 0.0f);
                this.onAlignmentReached(Alignment9.NORTH_WEST, false);
            }
        }
        if (this.m_position.getY() > height) {
            this.m_position.setY(height);
            if (this.m_position.getX() == 0.0f) {
                this.m_direction.set(1.0f, 0.0f, 0.0f);
                this.onAlignmentReached(Alignment9.NORTH_WEST, true);
            }
            else {
                this.m_direction.set(-1.0f, 0.0f, 0.0f);
                this.onAlignmentReached(Alignment9.NORTH_EAST, false);
            }
        }
        else if (this.m_position.getY() < 0.0f) {
            this.m_position.setY(0.0f);
            if (this.m_position.getX() == 0.0f) {
                this.m_direction.set(1.0f, 0.0f, 0.0f);
                this.onAlignmentReached(Alignment9.SOUTH_WEST, false);
            }
            else {
                this.m_direction.set(-1.0f, 0.0f, 0.0f);
                this.onAlignmentReached(Alignment9.SOUTH_EAST, true);
            }
        }
    }
    
    private void onAlignmentReached(final Alignment9 alignment9, final boolean clockWise) {
        if (this.m_turnNumber < 0) {
            return;
        }
        if (clockWise != this.m_moveClockWise) {
            return;
        }
        if (alignment9 != this.m_alignment) {
            return;
        }
        ++this.m_currentTurnNumber;
        if (this.m_currentTurnNumber >= this.m_turnNumber) {
            this.m_particleSystem.stopAndKill();
        }
    }
    
    public Vector4 getPosition() {
        return this.m_position;
    }
    
    private EntityGroup getParentEntity() {
        final Widget parentWidget = this.getParentOfType(Widget.class);
        return (parentWidget == null) ? null : parentWidget.getEntity();
    }
    
    public void setTurnNumber(final int turnNumber) {
        this.m_turnNumber = turnNumber;
    }
    
    static {
        ALIGNMENT_HASH = "alignment".hashCode();
        FILE_HASH = "file".hashCode();
        FOLLOW_BORDERS_HASH = "followBorders".hashCode();
        LEVEL_HASH = "level".hashCode();
        MOVE_CLOCK_WISE_HASH = "moveClockWise".hashCode();
        SPEED_HASH = "speed".hashCode();
        TIME_TO_LIVE_HASH = "timeToLive".hashCode();
        USE_PARENT_SCISSOR_HASH = "useParentScissor".hashCode();
        X_HASH = "x".hashCode();
        Y_HASH = "y".hashCode();
        ZOOM_HASH = "zoom".hashCode();
        TURN_NUMBER_HASH = "turnNumber".hashCode();
    }
}
