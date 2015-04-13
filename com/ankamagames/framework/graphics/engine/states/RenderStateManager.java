package com.ankamagames.framework.graphics.engine.states;

import com.ankamagames.framework.graphics.opengl.base.render.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.material.*;

public final class RenderStateManager
{
    private static final RenderStateManager m_instance;
    private final int[] m_pushedIntStates;
    private final short[] m_pushedShortStates;
    private final float[] m_pushedFloatStates;
    private final boolean[] m_pushedStatesValidity;
    private final int[] m_intStates;
    private final short[] m_shortStates;
    private final float[] m_floatStates;
    private final boolean[] m_statesValidity;
    private GLTexture m_texture;
    private final ViewPort m_viewport;
    private final Matrix44[] m_matrix;
    private boolean m_pushed;
    private final float[] m_diffuse;
    private final float[] m_specular;
    
    private RenderStateManager() {
        super();
        this.m_viewport = new ViewPort(0, 0, 0, 0);
        this.m_matrix = new Matrix44[MatrixModes.values().length];
        this.m_diffuse = new float[] { Float.NaN, Float.NaN, Float.NaN, Float.NaN };
        this.m_specular = new float[] { Float.NaN, Float.NaN, Float.NaN, Float.NaN };
        final RenderStateTypes[] renderStateTypes = RenderStateTypes.values();
        final int renderStatesCount = renderStateTypes.length;
        this.m_intStates = new int[renderStatesCount];
        this.m_floatStates = new float[renderStatesCount];
        this.m_shortStates = new short[renderStatesCount];
        this.m_statesValidity = new boolean[renderStatesCount];
        this.m_pushedIntStates = new int[renderStatesCount];
        this.m_pushedFloatStates = new float[renderStatesCount];
        this.m_pushedShortStates = new short[renderStatesCount];
        this.m_pushedStatesValidity = new boolean[renderStatesCount];
        for (final RenderStateTypes type : renderStateTypes) {
            this.m_intStates[type.ordinal()] = 0;
            this.m_floatStates[type.ordinal()] = 0.0f;
            this.m_shortStates[type.ordinal()] = 0;
            this.m_statesValidity[type.ordinal()] = false;
        }
        for (int i = 0; i < this.m_matrix.length; ++i) {
            this.m_matrix[i] = Matrix44.Factory.newInstance();
        }
        this.m_pushed = false;
    }
    
    public static RenderStateManager getInstance() {
        return RenderStateManager.m_instance;
    }
    
    public void setValuesToDefault() {
        this.enableBlend(true);
        this.setBlendFunc(BlendModes.One, BlendModes.Zero);
        this.setColorScale(1.0f);
        this.enablePointSmooth(false);
        this.setPointSize(1.0f);
        this.enableLineSmooth(false);
        this.setLineWidth(1.0f);
        this.setLineStippleFactor(1);
        this.setLineStipplePattern((short)0);
        this.enableScissor(false);
        this.setScissorRect(0, 0, 0, 0);
        this.enableTextures(true);
        this.setTextureBlend(TextureBlendModes.Blend);
        this.enableDepthTest(false);
    }
    
    private void setParam(final int stateId, final boolean booleanState) {
        this.setParam(stateId, booleanState ? 1 : 0);
    }
    
    private void setParam(final int stateId, final int state) {
        if (this.m_intStates[stateId] == state) {
            return;
        }
        if (this.m_pushed) {
            this.m_pushedStatesValidity[stateId] = (state == this.m_pushedIntStates[stateId]);
        }
        this.m_intStates[stateId] = state;
        this.m_statesValidity[stateId] = false;
    }
    
    private void setParam(final int stateId, final short state) {
        if (this.m_shortStates[stateId] == state) {
            return;
        }
        if (this.m_pushed) {
            this.m_pushedStatesValidity[stateId] = (state == this.m_pushedShortStates[stateId]);
        }
        this.m_shortStates[stateId] = state;
        this.m_statesValidity[stateId] = false;
    }
    
    private void setParam(final int stateId, final float state) {
        if (this.m_floatStates[stateId] == state) {
            return;
        }
        if (this.m_pushed) {
            this.m_pushedStatesValidity[stateId] = (state == this.m_pushedFloatStates[stateId]);
        }
        this.m_floatStates[stateId] = state;
        this.m_statesValidity[stateId] = false;
    }
    
    public void enableBlend(final boolean blendEnabled) {
        this.setParam(RenderStateTypes.BLEND.ordinal(), blendEnabled);
    }
    
    public void setBlendFunc(final BlendModes source, final BlendModes destination) {
        this.setParam(RenderStateTypes.BLEND_FUNC_SRC.ordinal(), source.m_oglCode);
        this.setParam(RenderStateTypes.BLEND_FUNC_DST.ordinal(), destination.m_oglCode);
    }
    
    public void enablePointSmooth(final boolean enabled) {
        this.setParam(RenderStateTypes.POINT_SMOOTH.ordinal(), enabled);
    }
    
    public void enableLineSmooth(final boolean enabled) {
        this.setParam(RenderStateTypes.LINE_SMOOTH.ordinal(), enabled);
    }
    
    public void setTextureBlend(final TextureBlendModes blendMode) {
        this.setParam(RenderStateTypes.TEXTURE_BLEND.ordinal(), blendMode.m_oglCode);
    }
    
    public void enableTextures(final boolean textureEnabled) {
        this.enableTextures(textureEnabled, null);
    }
    
    private void enableTextures(final boolean textureEnabled, final GLTexture texture) {
        this.setParam(RenderStateTypes.TEXTURE_ENABLE.ordinal(), textureEnabled ? 1 : 0);
        this.m_texture = texture;
    }
    
    public void enableScissor(final boolean enabled) {
        this.setParam(RenderStateTypes.SCISSOR_ENABLE.ordinal(), enabled);
    }
    
    public void enableLineStipple(final boolean enabled) {
        this.setParam(RenderStateTypes.LINE_STIPPLE.ordinal(), enabled);
    }
    
    public void enableDepthTest(final boolean enabled) {
        this.setParam(RenderStateTypes.DEPTH_TEST.ordinal(), enabled);
    }
    
    public void setDepthMask(final boolean value) {
        this.setParam(RenderStateTypes.DEPTH_MASK.ordinal(), value);
    }
    
    public void setScissorRect(final int left, final int bottom, final int width, final int height) {
        this.setParam(RenderStateTypes.SCISSOR_LEFT.ordinal(), left);
        this.setParam(RenderStateTypes.SCISSOR_BOTTOM.ordinal(), bottom);
        this.setParam(RenderStateTypes.SCISSOR_WIDTH.ordinal(), width);
        this.setParam(RenderStateTypes.SCISSOR_HEIGHT.ordinal(), height);
    }
    
    public void setColorScale(final float scale) {
        this.setParam(RenderStateTypes.COLOR_SCALE.ordinal(), scale);
    }
    
    public void setPointSize(final float pointSize) {
        this.setParam(RenderStateTypes.POINT_SIZE.ordinal(), pointSize);
    }
    
    public void setLineWidth(final float lineWidth) {
        this.setParam(RenderStateTypes.LINE_WIDTH.ordinal(), lineWidth);
    }
    
    public void setLineStippleFactor(final int factor) {
        this.setParam(RenderStateTypes.LINE_STIPPLE_FACTOR.ordinal(), factor);
    }
    
    public void setLineStipplePattern(final short pattern) {
        this.setParam(RenderStateTypes.LINE_STIPPLE_PATTERN.ordinal(), pattern);
    }
    
    public void applyStates(final Renderer renderer) {
        final GLRenderer glRenderer = (GLRenderer)renderer;
        final GL gl = glRenderer.getDevice();
        final int enableTexturing = RenderStateTypes.TEXTURE_ENABLE.ordinal();
        if (!this.m_statesValidity[enableTexturing]) {
            this.m_statesValidity[enableTexturing] = true;
            if (this.m_intStates[enableTexturing] == 1) {
                gl.glEnable(3553);
            }
            else {
                gl.glDisable(3553);
            }
        }
        final int textureBlend = RenderStateTypes.TEXTURE_BLEND.ordinal();
        if (!this.m_statesValidity[textureBlend]) {
            this.m_statesValidity[textureBlend] = true;
            gl.glTexEnvi(8960, 8704, this.m_intStates[textureBlend]);
        }
        final int blend = RenderStateTypes.BLEND.ordinal();
        if (!this.m_statesValidity[blend]) {
            this.m_statesValidity[blend] = true;
            if (this.m_intStates[blend] == 1) {
                gl.glEnable(3042);
            }
            else {
                gl.glDisable(3042);
            }
        }
        final int blendFuncSrc = RenderStateTypes.BLEND_FUNC_SRC.ordinal();
        final int blendFuncDst = RenderStateTypes.BLEND_FUNC_DST.ordinal();
        if (!this.m_statesValidity[blendFuncSrc] || !this.m_statesValidity[blendFuncDst]) {
            this.m_statesValidity[blendFuncSrc] = true;
            this.m_statesValidity[blendFuncDst] = true;
            gl.glBlendFunc(this.m_intStates[blendFuncSrc], this.m_intStates[blendFuncDst]);
        }
        final int pointSmooth = RenderStateTypes.POINT_SMOOTH.ordinal();
        if (!this.m_statesValidity[pointSmooth]) {
            this.m_statesValidity[pointSmooth] = true;
            if (this.m_intStates[pointSmooth] == 1) {
                gl.glEnable(2832);
            }
            else {
                gl.glDisable(2832);
            }
        }
        final int lineSmooth = RenderStateTypes.LINE_SMOOTH.ordinal();
        if (!this.m_statesValidity[lineSmooth]) {
            this.m_statesValidity[lineSmooth] = true;
            if (this.m_intStates[lineSmooth] == 1) {
                gl.glEnable(2848);
            }
            else {
                gl.glDisable(2848);
            }
        }
        final int colorScale = RenderStateTypes.COLOR_SCALE.ordinal();
        if (!this.m_statesValidity[colorScale]) {
            this.m_statesValidity[colorScale] = true;
            this.applyActiveTextureIndex(gl, 0);
            gl.glTexEnvf(8960, 8704, 34160.0f);
            gl.glTexEnvf(8960, 34161, 8448.0f);
            gl.glTexEnvf(8960, 34176, 5890.0f);
            gl.glTexEnvf(8960, 34163, this.m_floatStates[colorScale]);
        }
        final int pointSize = RenderStateTypes.POINT_SIZE.ordinal();
        if (!this.m_statesValidity[pointSize]) {
            this.m_statesValidity[pointSize] = true;
            gl.glPointSize(this.m_floatStates[pointSize]);
        }
        final int lineWidth = RenderStateTypes.LINE_WIDTH.ordinal();
        if (!this.m_statesValidity[lineWidth]) {
            this.m_statesValidity[lineWidth] = true;
            gl.glLineWidth(this.m_floatStates[lineWidth]);
        }
        final int enableLineStipple = RenderStateTypes.LINE_STIPPLE.ordinal();
        if (!this.m_statesValidity[enableLineStipple]) {
            this.m_statesValidity[enableLineStipple] = true;
            if (this.m_intStates[enableLineStipple] == 1) {
                gl.glEnable(2852);
            }
            else {
                gl.glDisable(2852);
            }
        }
        final int lineStippleFactor = RenderStateTypes.LINE_STIPPLE_FACTOR.ordinal();
        final int lineStipplePattern = RenderStateTypes.LINE_STIPPLE_PATTERN.ordinal();
        if (!this.m_statesValidity[lineStippleFactor] || !this.m_statesValidity[lineStipplePattern]) {
            this.m_statesValidity[lineStippleFactor] = true;
            this.m_statesValidity[lineStipplePattern] = true;
            gl.glLineStipple(this.m_intStates[lineStippleFactor], this.m_shortStates[lineStipplePattern]);
        }
        final int enableScissor = RenderStateTypes.SCISSOR_ENABLE.ordinal();
        if (!this.m_statesValidity[enableScissor]) {
            this.m_statesValidity[enableScissor] = true;
            if (this.m_intStates[enableScissor] == 1) {
                gl.glEnable(3089);
            }
            else {
                gl.glDisable(3089);
            }
        }
        final int scissorLeft = RenderStateTypes.SCISSOR_LEFT.ordinal();
        final int scissorBottom = RenderStateTypes.SCISSOR_BOTTOM.ordinal();
        final int scissorWidth = RenderStateTypes.SCISSOR_WIDTH.ordinal();
        final int scissorHeight = RenderStateTypes.SCISSOR_HEIGHT.ordinal();
        if (!this.m_statesValidity[scissorLeft] || !this.m_statesValidity[scissorBottom] || !this.m_statesValidity[scissorWidth] || !this.m_statesValidity[scissorHeight]) {
            this.m_statesValidity[scissorLeft] = true;
            this.m_statesValidity[scissorBottom] = true;
            this.m_statesValidity[scissorWidth] = true;
            this.m_statesValidity[scissorHeight] = true;
            gl.glScissor(this.m_intStates[scissorLeft], this.m_intStates[scissorBottom], this.m_intStates[scissorWidth], this.m_intStates[scissorHeight]);
        }
        final int enableDepthTest = RenderStateTypes.DEPTH_TEST.ordinal();
        if (!this.m_statesValidity[enableDepthTest]) {
            this.m_statesValidity[enableDepthTest] = true;
            if (this.m_intStates[enableDepthTest] == 1) {
                gl.glEnable(2929);
            }
            else {
                gl.glDisable(2929);
            }
        }
        final int depthMask = RenderStateTypes.DEPTH_MASK.ordinal();
        if (!this.m_statesValidity[depthMask]) {
            this.m_statesValidity[depthMask] = true;
            gl.glDepthMask(this.m_intStates[depthMask] == 1);
        }
    }
    
    public void applyActiveTextureIndex(final GL gl, final int i) {
        final int textureIndex = RenderStateTypes.ACTIVE_TEXTURE_INDEX.ordinal();
        if (this.m_intStates[textureIndex] == i) {
            return;
        }
        gl.glActiveTexture(33984 + (this.m_intStates[textureIndex] = i));
    }
    
    public void applyClientActiveTextureIndex(final GL gl, final int i) {
        final int textureIndex = RenderStateTypes.CLIENT_ACTIVE_TEXTURE_INDEX.ordinal();
        if (this.m_intStates[textureIndex] == i) {
            return;
        }
        gl.glClientActiveTexture(33984 + (this.m_intStates[textureIndex] = i));
    }
    
    public void applyMatrixMode(final GL gl, final MatrixModes matrixMode) {
        final int matrixIndex = RenderStateTypes.MATRIX.ordinal();
        final int value = matrixMode.m_oglCode;
        this.m_matrix[matrixMode.ordinal()].setScale(0.0f, 0.0f, 0.0f);
        if (this.m_intStates[matrixIndex] == value) {
            return;
        }
        gl.glMatrixMode(this.m_intStates[matrixIndex] = value);
    }
    
    public void applyMatrixMode(final GL gl, final MatrixModes matrixMode, final Matrix44 matrix) {
        final int matrixIndex = RenderStateTypes.MATRIX.ordinal();
        final int value = matrixMode.m_oglCode;
        if (this.m_intStates[matrixIndex] != value) {
            gl.glMatrixMode(this.m_intStates[matrixIndex] = value);
        }
        final Matrix44 m = this.m_matrix[matrixMode.ordinal()];
        if (!m.equals(matrix)) {
            m.set(matrix);
            if (matrix.isIdentity()) {
                gl.glLoadIdentity();
            }
            else {
                gl.glLoadMatrixf(matrix.getBuffer(), 0);
            }
        }
    }
    
    public void useTexture(final Renderer renderer, final GLTexture texture) {
        if (this.m_texture == texture) {
            return;
        }
        if (texture == null) {
            this.enableTextures(false, texture);
        }
        else {
            this.enableTextures(true, texture);
            this.setTextureBlend(texture.getBlend());
            this.m_texture.activate2(renderer);
        }
    }
    
    public void push() {
        this.m_pushed = true;
        System.arraycopy(this.m_intStates, 0, this.m_pushedIntStates, 0, this.m_intStates.length);
        System.arraycopy(this.m_floatStates, 0, this.m_pushedFloatStates, 0, this.m_floatStates.length);
        System.arraycopy(this.m_shortStates, 0, this.m_pushedShortStates, 0, this.m_shortStates.length);
        System.arraycopy(this.m_statesValidity, 0, this.m_pushedStatesValidity, 0, this.m_statesValidity.length);
    }
    
    public void pop() {
        if (this.m_pushed) {
            System.arraycopy(this.m_pushedIntStates, 0, this.m_intStates, 0, this.m_intStates.length);
            System.arraycopy(this.m_pushedFloatStates, 0, this.m_floatStates, 0, this.m_floatStates.length);
            System.arraycopy(this.m_pushedShortStates, 0, this.m_shortStates, 0, this.m_shortStates.length);
            System.arraycopy(this.m_pushedStatesValidity, 0, this.m_statesValidity, 0, this.m_statesValidity.length);
            this.m_pushed = false;
        }
    }
    
    public void reset() {
        this.enableTextures(false, null);
    }
    
    public ViewPort getViewport() {
        return this.m_viewport.copy();
    }
    
    public void resetViewport() {
        this.m_viewport.set(0, 0, 0, 0);
    }
    
    public void applyViewport(final GL gl, final int x, final int y, final int width, final int height) {
        if (this.m_viewport.equals(x, y, width, height)) {
            return;
        }
        gl.glViewport(x, y, width, height);
        this.m_viewport.set(x, y, width, height);
    }
    
    public void applyViewportAndOrtho2D(final GL gl, final ViewPort vp) {
        this.applyViewport(gl, vp.getX(), vp.getY(), vp.getWidth(), vp.getHeight());
        this.applyMatrixMode(gl, MatrixModes.PROJECTION);
        gl.glLoadIdentity();
        float halfResX = vp.getHalfResX();
        float halfResY = vp.getHalfResY();
        if (halfResX < 1.0f) {
            halfResX = 1.0f;
        }
        if (halfResY < 1.0f) {
            halfResY = 1.0f;
        }
        gl.glOrtho((double)(-halfResX), (double)halfResX, (double)(-halfResY), (double)halfResY, 0.0, 65535.0);
    }
    
    public void applyMaterial(final GL gl, final Material material) {
        if (!material.diffuseEquals(this.m_diffuse)) {
            material.copyDiffuseTo(this.m_diffuse);
            gl.glMaterialfv(1032, 4609, this.m_diffuse, 0);
        }
        if (!material.specularEquals(this.m_specular)) {
            material.copySpecularTo(this.m_specular);
            gl.glMaterialfv(1032, 4610, this.m_specular, 0);
        }
    }
    
    static {
        m_instance = new RenderStateManager();
    }
}
