package com.ankamagames.framework.graphics.engine.opengl;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.test.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.states.*;

public final class GLRenderer extends Renderer<GL>
{
    public final GLStateManager m_stateManager;
    public static final GLU m_glu;
    private static final Logger m_logger;
    
    public GLRenderer() {
        super();
        this.m_stateManager = new GLStateManager();
        this.m_worldMatrix = Matrix44.Factory.newInstance();
        this.m_cameraMatrix = Matrix44.Factory.newInstance();
        this.m_worldMatrix.setIdentity();
        this.m_cameraMatrix.setIdentity();
    }
    
    @Override
    public RendererType getType() {
        return RendererType.OpenGL;
    }
    
    @Override
    public Texture createTexture(final long name, final String fileName, final boolean keepData) {
        return new GLTexture(name, fileName, keepData);
    }
    
    @Override
    public Texture createTexture(final long name, final Image image, final boolean keepData) {
        return new GLTexture(name, image, keepData);
    }
    
    @Override
    public Texture createRenderTarget(final long name, final int width, final int height, final boolean generateMipMaps) {
        return new GLTexture(name, width, height, false);
    }
    
    @Override
    public void setWorldMatrix(final Matrix44 matrix) {
        if (!this.m_worldMatrix.equals(matrix)) {
            this.m_worldMatrix.set(matrix);
            this.updateMatrix();
        }
    }
    
    @Override
    public void setCameraMatrix(final Matrix44 matrix) {
        if (!this.m_cameraMatrix.equals(matrix)) {
            this.m_cameraMatrix.set(matrix);
            this.updateMatrix();
        }
    }
    
    @Override
    public void setRenderTarget(final Texture texture) {
        this.m_renderTarget = texture;
        if (texture == null) {
            ((GL)this.m_device).glBindFramebufferEXT(36160, 0);
        }
        else {
            assert texture.getClass() == GLTexture.class : "GLRenderer can't apply Texture but GLTexture";
            final GLTexture glTexture = (GLTexture)texture;
            ((GL)this.m_device).glBindFramebufferEXT(36160, glTexture.getRenderTargetID());
        }
    }
    
    @Override
    public void createDevice(final GL gl) {
        super.createDevice(gl);
        this.m_stateManager.setDevice(gl);
        final int[] value = { 0 };
        gl.glGetIntegerv(34018, value, 0);
        this.m_maxMultiTextures = value[0];
    }
    
    @Override
    public void drawRect(final float x, final float y, final float width, final float height, final int ARGB) {
        RenderStateManager.getInstance().enableTextures(false);
        RenderStateManager.getInstance().applyStates(this);
        this.setWorldMatrix(Matrix44.IDENTITY);
        this.setCameraMatrix(Matrix44.IDENTITY);
        ((GL)this.m_device).glBegin(7);
        ((GL)this.m_device).glColor4f(Color.getRedFromARGB(ARGB), Color.getGreenFromARGB(ARGB), Color.getBlueFromARGB(ARGB), Color.getAlphaFromARGB(ARGB));
        ((GL)this.m_device).glVertex2f(x, y);
        ((GL)this.m_device).glVertex2f(x, y + height);
        ((GL)this.m_device).glVertex2f(x + width, y + height);
        ((GL)this.m_device).glVertex2f(x + width, y);
        ((GL)this.m_device).glEnd();
    }
    
    @Override
    public boolean supportRenderTarget() {
        return HardwareFeatureManager.INSTANCE.isFeatureSupported(HardwareFeature.GL_RENDER_TARGET);
    }
    
    public void dumpRenderStates() {
        this.logBooleanState(3042);
        this.logIntegerState(3041);
        this.logIntegerState(3040);
        this.logBooleanState(32886);
        this.logIntegerState(32897);
        this.logIntegerState(32899);
        this.logIntegerState(32898);
        this.logBooleanState(32888);
        this.logIntegerState(32904);
        this.logIntegerState(32906);
        this.logIntegerState(32905);
        this.logBooleanState(32884);
        this.logIntegerState(32890);
        this.logIntegerState(32892);
        this.logIntegerState(32891);
        this.logBooleanState(2884);
        this.logIntegerState(2885);
        this.logIntegerState(3415);
        this.logIntegerState(2961);
        this.logIntegerState(2964);
        this.logIntegerState(2962);
        this.logIntegerState(2965);
        this.logIntegerState(2966);
        this.logIntegerState(2967);
        this.logBooleanState(2960);
        this.logIntegerState(2963);
        this.logIntegerState(2968);
        this.logBooleanState(3552);
        this.logBooleanState(3553);
        this.logBooleanState(2977);
        this.logBooleanState(3089);
        this.logColorState(4609);
    }
    
    private void logBooleanState(final int state) {
        final byte[] b = { 0 };
        ((GL)this.m_device).glGetBooleanv(state, b, 0);
        GLRenderer.m_logger.info((Object)(state + "\t" + b[0]));
    }
    
    private void logIntegerState(final int state) {
        final int[] i = { 0 };
        ((GL)this.m_device).glGetIntegerv(state, i, 0);
        GLRenderer.m_logger.info((Object)(state + "\t" + i[0]));
    }
    
    private void logColorState(final int state) {
        final float[] f = new float[4];
        ((GL)this.m_device).glGetFloatv(state, f, 0);
        GLRenderer.m_logger.info((Object)(state + "\t" + Arrays.toString(f)));
    }
    
    @Override
    public void updateMatrix() {
        if (this.m_worldMatrix.isIdentity() && this.m_cameraMatrix.isIdentity()) {
            this.m_worldViewProjMatrix.setIdentity();
        }
        else {
            this.m_worldViewProjMatrix.setMultiply(this.m_worldMatrix, this.m_cameraMatrix);
        }
        RenderStateManager.getInstance().applyMatrixMode((GL)this.m_device, MatrixModes.MODEL_VIEW, this.m_worldViewProjMatrix);
    }
    
    static {
        m_glu = new GLU();
        m_logger = Logger.getLogger((Class)GLRenderer.class);
    }
}
