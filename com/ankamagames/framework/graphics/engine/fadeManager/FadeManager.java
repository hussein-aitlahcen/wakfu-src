package com.ankamagames.framework.graphics.engine.fadeManager;

import com.ankamagames.framework.graphics.opengl.base.render.*;
import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.opengl.base.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.image.*;
import javax.media.opengl.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.opengl.*;

public class FadeManager implements GLRenderable
{
    private static final Logger m_logger;
    public static final int DEFAULT_FADING_IN_TIME = 1000;
    public static final int DEFAULT_FADING_OUT_TIME = 1000;
    private State m_state;
    private int m_elapsedTime;
    private int m_timeToFade;
    private int m_fadingOutDuration;
    private float[] m_destinationColor;
    private float[] m_sourceColor;
    private float[] m_color;
    private final EntitySprite m_fade;
    private Scene m_scene;
    private final ArrayList<FadeListener> m_listeners;
    private final ArrayList<FadeOutCondition> m_conditions;
    private static final FadeManager m_instance;
    int debug_blackScreenTime;
    
    private FadeManager() {
        super();
        this.m_fadingOutDuration = 1000;
        this.m_listeners = new ArrayList<FadeListener>();
        this.m_conditions = new ArrayList<FadeOutCondition>();
        this.debug_blackScreenTime = 0;
        this.m_sourceColor = new float[4];
        this.m_destinationColor = new float[4];
        this.m_color = new float[4];
        this.m_sourceColor[3] = 0.0f;
        this.m_destinationColor[3] = 1.0f;
        this.m_elapsedTime = 0;
        this.m_timeToFade = 1000;
        this.m_state = State.DisabledOut;
        this.m_fade = EntitySprite.Factory.newInstance();
        this.m_fade.m_owner = this;
        final GLGeometrySprite geometry = GLGeometrySprite.Factory.newInstance();
        this.m_fade.setGeometry(geometry);
        geometry.removeReference();
        this.m_fade.setColor(this.m_destinationColor[0], this.m_destinationColor[1], this.m_destinationColor[2], this.m_destinationColor[3]);
        this.m_fade.setTexture(null);
        this.m_fade.setVisible(false);
        this.m_fade.getGeometry().setBlendFunc(BlendModes.SrcAlpha, BlendModes.InvSrcAlpha);
    }
    
    public static FadeManager getInstance() {
        return FadeManager.m_instance;
    }
    
    public final boolean isFadeIn() {
        return this.m_state == State.FadeIn;
    }
    
    public final boolean isFadeOut() {
        return this.m_state == State.FadeOut;
    }
    
    public final boolean isDisabledIn() {
        return this.m_state == State.DisabledIn;
    }
    
    public final boolean isDisabledOut() {
        return this.m_state == State.DisabledOut;
    }
    
    public final void setFadeOutDuration(final int fadeOutDuration) {
        this.m_fadingOutDuration = fadeOutDuration;
    }
    
    public final void fadeIn(final int fadeTime, final Scene scene) {
        if (this.m_state == State.DisabledIn) {
            FadeManager.m_logger.error((Object)"probl\u00e8me de fade ", (Throwable)new Exception());
        }
        this.m_scene = scene;
        this.fade(fadeTime, Color.BLACK);
        this.setState(State.FadeIn);
    }
    
    @Override
    public final void process(final int timeIncrement) {
        if (this.m_state == State.DisabledIn) {
            this.debug_blackScreenTime += timeIncrement;
            if (this.debug_blackScreenTime > 10000) {
                this.debug_blackScreenTime = 0;
            }
            for (int i = this.m_conditions.size() - 1; i >= 0; --i) {
                if (!this.m_conditions.get(i).isValid()) {
                    return;
                }
            }
            while (true) {
                if (this.debug_blackScreenTime == 0) {
                    this.debug_blackScreenTime = 0;
                    this.fadeOut(this.m_fadingOutDuration);
                    return;
                }
                continue;
            }
        }
        else {
            if (this.m_state == State.DisabledOut) {
                return;
            }
            this.m_elapsedTime += timeIncrement;
            if (this.m_elapsedTime > this.m_timeToFade) {
                this.m_elapsedTime = this.m_timeToFade;
                this.m_color[0] = this.m_destinationColor[0];
                this.m_color[1] = this.m_destinationColor[1];
                this.m_color[2] = this.m_destinationColor[2];
                this.m_color[3] = this.m_destinationColor[3];
                if (this.m_state == State.FadeIn) {
                    this.setState(State.DisabledIn);
                    this.m_scene = null;
                }
                else {
                    this.setState(State.DisabledOut);
                }
            }
            else {
                final float f = this.m_elapsedTime / this.m_timeToFade;
                this.m_color[0] = MathHelper.lerp(this.m_sourceColor[0], this.m_destinationColor[0], f);
                this.m_color[1] = MathHelper.lerp(this.m_sourceColor[1], this.m_destinationColor[1], f);
                this.m_color[2] = MathHelper.lerp(this.m_sourceColor[2], this.m_destinationColor[2], f);
                this.m_color[3] = MathHelper.lerp(this.m_sourceColor[3], this.m_destinationColor[3], f);
            }
            this.m_fade.setColor(this.m_color[0], this.m_color[1], this.m_color[2], this.m_color[3]);
            this.m_fade.setVisible(this.m_color[3] > 0.004f);
        }
    }
    
    @Override
    public void init(final GLAutoDrawable glAutoDrawable) {
    }
    
    @Override
    public final void setFrustumSize(final int frustumWidth, final int frustumHeight) {
        this.m_fade.setBounds(frustumHeight / 2, -frustumWidth / 2, frustumWidth, frustumHeight);
        if (this.m_scene != null) {
            this.m_scene.setFrustumSize(frustumWidth, frustumHeight);
        }
    }
    
    @Override
    public void display(final GL gl) {
        if (this.m_state == State.DisabledOut) {
            return;
        }
        if (this.m_scene != null) {
            this.m_scene.display(gl);
        }
        final GLRenderer renderer = RendererType.OpenGL.getRenderer();
        renderer.setCameraMatrix(Matrix44.IDENTITY);
        this.m_fade.render(renderer);
    }
    
    public final void addListener(final FadeListener l) {
        this.m_listeners.add(l);
    }
    
    public final void removeListener(final FadeListener l) {
        this.m_listeners.remove(l);
    }
    
    public final void addFadeOutCondition(final FadeOutCondition condition) {
        this.m_conditions.add(condition);
    }
    
    public final void removeFadeOutCondition(final FadeOutCondition condition) {
        this.m_conditions.remove(condition);
    }
    
    private void fadeOut(final int fadeTime) {
        if (this.m_scene != null) {
            this.m_scene.uninitialize();
        }
        this.m_scene = null;
        this.fade(fadeTime, Color.ALPHA);
        this.setState(State.FadeOut);
    }
    
    private void fade(final int fadeTime, final Color color) {
        this.m_sourceColor[0] = this.m_color[0];
        this.m_sourceColor[1] = this.m_color[1];
        this.m_sourceColor[2] = this.m_color[2];
        this.m_sourceColor[3] = this.m_color[3];
        this.m_destinationColor[0] = color.getRed();
        this.m_destinationColor[1] = color.getGreen();
        this.m_destinationColor[2] = color.getBlue();
        this.m_destinationColor[3] = color.getAlpha();
        this.m_timeToFade = fadeTime;
        this.m_elapsedTime = 0;
    }
    
    public final void reset() {
        this.setState(State.DisabledIn);
    }
    
    private void setState(final State state) {
        if (this.m_state == state) {
            return;
        }
        final State current = this.m_state;
        this.m_state = state;
        for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
            this.m_state.triggerListener(this.m_listeners.get(i));
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)FadeManager.class);
        m_instance = new FadeManager();
    }
    
    public enum State
    {
        FadeIn {
            @Override
            void triggerListener(final FadeListener listener) {
                listener.onFadeInStart();
            }
        }, 
        DisabledIn {
            @Override
            void triggerListener(final FadeListener listener) {
                listener.onFadeInEnd();
            }
        }, 
        FadeOut {
            @Override
            void triggerListener(final FadeListener listener) {
                listener.onFadeOutStart();
            }
        }, 
        DisabledOut {
            @Override
            void triggerListener(final FadeListener listener) {
                listener.onFadeOutEnd();
            }
        };
        
        abstract void triggerListener(final FadeListener p0);
        
        State getNext() {
            final int ordinal = this.ordinal();
            final State[] values = values();
            if (ordinal == values.length - 1) {
                return values[0];
            }
            return values[ordinal + 1];
        }
    }
}
