package com.ankamagames.baseImpl.graphics.alea.utils;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.awt.geom.*;
import javax.imageio.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.image.*;
import java.awt.image.*;
import com.ankamagames.framework.graphics.opengl.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.opengl.base.render.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import java.nio.*;
import gnu.trove.*;

public abstract class AnmRastorize
{
    private static final Logger m_logger;
    public static final String FORMAT = "png";
    public static final float DEFAULT_ZOOM = 1.5f;
    protected final Entity3D m_entity3D;
    protected final int m_imageWidth;
    protected final int m_imageHeight;
    protected final float m_zoom;
    protected final float m_offsetX;
    protected final float m_offsetY;
    protected AnmInstance m_anmInstance;
    protected String m_animName;
    
    protected AnmRastorize(final int imageWidth, final int imageHeight, final float zoomFactor, final float offsetX, final float offsetY) {
        super();
        this.m_entity3D = Entity3D.Factory.newPooledInstance();
        this.m_imageWidth = imageWidth;
        this.m_imageHeight = imageHeight;
        this.m_zoom = zoomFactor;
        this.m_offsetX = offsetX;
        this.m_offsetY = offsetY;
    }
    
    public final void prepare(final AnmInstance anm, final String animName, final String... itemsToHide) {
        if (this.m_anmInstance != null) {
            this.m_anmInstance.reset();
        }
        (this.m_anmInstance = new AnmInstance(anm)).setMaterial(Material.WHITE_NO_SPECULAR);
        this.m_animName = animName;
        for (int i = 0; i < itemsToHide.length; ++i) {
            this.m_anmInstance.hideSprite(Engine.getPartName(itemsToHide[i]));
        }
    }
    
    protected boolean checkAnimation() {
        this.m_anmInstance.updateEquipment();
        if (!this.m_anmInstance.isReady() || this.m_anmInstance.hasPartsToSet()) {
            return false;
        }
        this.m_anmInstance.setAnimation(this.m_animName);
        if (this.m_anmInstance.isAnimationRequested()) {
            return false;
        }
        this.m_anmInstance.updateFrame(0, this.m_entity3D, 0);
        return true;
    }
    
    public abstract void run(final ByteArrayOutputStream p0, final String p1);
    
    public abstract void run(final String p0, final String p1);
    
    public abstract void run(final String p0, final OnTerminated p1);
    
    public final void cleanup() {
        this.m_entity3D.removeReference();
        if (this.m_anmInstance != null) {
            this.m_anmInstance.reset();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnmRastorize.class);
    }
    
    public static class Simple extends AnmRastorize
    {
        private final BufferedImage m_bufferedImage;
        
        public Simple() {
            this(96, 192, 1.5f, 0.5f, 0.1f);
        }
        
        public Simple(final int imageWidth, final int imageHeight, final float zoomFactor, final float offsetX, final float offsetY) {
            super(imageWidth, imageHeight, zoomFactor, offsetX, offsetY);
            this.m_bufferedImage = new BufferedImage(this.m_imageWidth, this.m_imageHeight, 2);
        }
        
        @Override
        public void run(final ByteArrayOutputStream ostream, final String imgFormat) {
            this.rasterize(ostream, imgFormat);
        }
        
        @Override
        public void run(final String fileName, final String imgFormat) {
            try {
                this.rasterize(FileHelper.createFileOutputStream(fileName), imgFormat);
            }
            catch (IOException e) {
                AnmRastorize.m_logger.error((Object)"", (Throwable)e);
            }
        }
        
        @Override
        public void run(final String imgFormat, final OnTerminated onDone) {
            final ByteArrayOutputStream data = new ByteArrayOutputStream();
            if (this.checkAnimation()) {
                this.rasterize(data, imgFormat);
                onDone.run(data.toByteArray());
            }
        }
        
        private void rasterize(final OutputStream ostream, final String imgFormat) {
            this.rasterizeEntity();
            try {
                final AffineTransform tx = new AffineTransform();
                tx.scale(1.0, -1.0);
                tx.translate(0.0, -this.m_bufferedImage.getHeight(null));
                final AffineTransformOp flipVertically = new AffineTransformOp(tx, 1);
                final BufferedImage result = flipVertically.filter(this.m_bufferedImage, null);
                ImageIO.write(result, imgFormat, ostream);
            }
            catch (IOException e) {
                AnmRastorize.m_logger.error((Object)"Erreur ici", (Throwable)e);
            }
        }
        
        private void rasterizeEntity() {
            final Graphics2D g = this.m_bufferedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            this.m_anmInstance.updateFrame(0, this.m_entity3D, 0);
            final int numGeometries = this.m_entity3D.getNumGeometries();
            final TLongObjectHashMap<Texture> used = new TLongObjectHashMap<Texture>(numGeometries);
            for (int i = 0; i < numGeometries; ++i) {
                final GeometryMesh geometryMesh = (GeometryMesh)this.m_entity3D.getGeometry(i);
                Texture texture = this.m_entity3D.getTexture(i);
                final String filename = texture.getFileName();
                final long name = Engine.getTextureName(filename);
                texture = used.get(name);
                if (texture == null) {
                    texture = RendererType.OpenGL.getRenderer().createTexture(name, filename, true);
                    texture.load(false);
                    used.put(name, texture);
                }
                this.rasterizeGeometry(geometryMesh, texture, g);
            }
            final TLongObjectIterator<Texture> iter = used.iterator();
            while (iter.hasNext()) {
                iter.advance();
                final Texture texture2 = iter.value();
                while (texture2.getNumReferences() >= 0) {
                    texture2.removeReference();
                }
            }
            g.dispose();
            this.cleanup();
        }
        
        private void rasterizeGeometry(final GeometryMesh geometryMesh, final Texture texture, final Graphics2D g) {
            final Layer layer = texture.getLayer(0);
            final VertexBufferPCT vertexBuffer = geometryMesh.getVertexBuffer();
            for (int numTriangles = vertexBuffer.getNumVertices() / 4, i = 0; i < numTriangles; ++i) {
                this.rasterizeTriangle(g, i, vertexBuffer, layer);
            }
        }
        
        private void rasterizeTriangle(final Graphics2D g, final int triangleIndex, final VertexBufferPCT vertexBuffer, final Layer layer) {
            final FloatBuffer positionBuffer = vertexBuffer.getPositionBuffer();
            final FloatBuffer texCoordBuffer = vertexBuffer.getTexCoord0Buffer();
            final FloatBuffer colorBuffer = vertexBuffer.getColorBuffer();
            final int i0 = triangleIndex * 4;
            final short index0 = (short)i0;
            final short index = (short)(i0 + 1);
            final short index2 = (short)(i0 + 2);
            final float x0 = positionBuffer.get(index0 * 2) * this.m_zoom + this.m_imageWidth * this.m_offsetX;
            final float x = positionBuffer.get(index * 2) * this.m_zoom + this.m_imageWidth * this.m_offsetX;
            final float x2 = positionBuffer.get(index2 * 2) * this.m_zoom + this.m_imageWidth * this.m_offsetX;
            final float y0 = positionBuffer.get(index0 * 2 + 1) * this.m_zoom + this.m_imageHeight * this.m_offsetY;
            final float y = positionBuffer.get(index * 2 + 1) * this.m_zoom + this.m_imageHeight * this.m_offsetY;
            final float y2 = positionBuffer.get(index2 * 2 + 1) * this.m_zoom + this.m_imageHeight * this.m_offsetY;
            final float u0 = texCoordBuffer.get(index0 * 2);
            final float u = texCoordBuffer.get(index2 * 2);
            final float v0 = texCoordBuffer.get(index0 * 2 + 1);
            final float v = texCoordBuffer.get(index * 2 + 1);
            final float r0 = colorBuffer.get(index0 * 4) * 1.25f + 0.5f;
            final float g2 = colorBuffer.get(index0 * 4 + 1) * 1.25f + 0.5f;
            final float b0 = colorBuffer.get(index0 * 4 + 2) * 1.25f + 0.5f;
            final float a0 = colorBuffer.get(index0 * 4 + 3);
            final Vector2 vecV = new Vector2(x0 - x, y0 - y);
            final Vector2 vecH = new Vector2(x2 - x, y2 - y);
            final int bottom = Math.round(v0 * layer.getHeight());
            final int left = Math.round(u0 * layer.getWidth());
            final int top = Math.round(v * layer.getHeight());
            final int right = Math.round(u * layer.getWidth());
            final Layer cropped = Image.crop(layer, top, left, bottom, right);
            final int texW = cropped.getWidth();
            final int texH = cropped.getHeight();
            BufferedImage img = ImageUtilities.toImage(texW, texH, cropped.getData(), ImageUtilities.PixelFormat.ARGB);
            ImageUtilities.alphaDemultiply(img);
            cropped.removeReference();
            final RescaleOp colorFilter = new RescaleOp(new float[] { r0, g2, b0, a0 }, new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, null);
            img = colorFilter.filter(img, null);
            final float m00 = vecH.m_x / texW;
            final float m = vecH.m_y / texW;
            final float m2 = vecV.m_x / texH;
            final float m3 = vecV.m_y / texH;
            final AffineTransform xform = new AffineTransform(m00, m, m2, m3, x, y);
            g.drawImage(img, xform, null);
        }
    }
    
    public static class OpenGL extends AnmRastorize
    {
        boolean m_canceled;
        final Renderer m_renderer;
        
        public OpenGL(final Renderer renderer) {
            this(96, 192, 1.5f, 0.5f, 0.1f, renderer);
        }
        
        public OpenGL(final int imageWidth, final int imageHeight, final float zoomFactor, final float offsetX, final float offsetY, final Renderer renderer) {
            super(imageWidth, imageHeight, zoomFactor, offsetX, offsetY);
            this.m_canceled = false;
            this.m_renderer = renderer;
        }
        
        @Override
        public void run(final ByteArrayOutputStream ostream, final String imgFormat) {
            this.run(imgFormat, new OnTerminated() {
                @Override
                public void run(final byte[] imgData) {
                    try {
                        ostream.write(imgData);
                    }
                    catch (IOException e) {
                        AnmRastorize.m_logger.error((Object)"", (Throwable)e);
                    }
                }
            });
        }
        
        @Override
        public void run(final String fileName, final String imgFormat) {
            this.run(imgFormat, new OnTerminated() {
                @Override
                public void run(final byte[] imgData) {
                    try {
                        FileHelper.write(fileName, imgData);
                    }
                    catch (IOException e) {
                        AnmRastorize.m_logger.error((Object)"", (Throwable)e);
                    }
                }
            });
        }
        
        @Override
        public void run(final String imgFormat, final OnTerminated onDone) {
            this.m_renderer.addRenderToTextureTask(new Renderer.RenderToTextureTask() {
                @Override
                public boolean render() {
                    if (OpenGL.this.m_canceled) {
                        OpenGL.this.cleanup();
                        return false;
                    }
                    if (!OpenGL.this.checkAnimation()) {
                        return true;
                    }
                    final GLRenderer renderer = RendererType.OpenGL.getRenderer();
                    final GL gl = renderer.getDevice();
                    final int texId = OpenGL.this.emptyTexture(gl);
                    final byte[] img = OpenGL.this.renderToTexture(texId, renderer, imgFormat);
                    assert onDone != null;
                    onDone.run(img);
                    gl.glDeleteTextures(1, new int[] { texId }, 0);
                    OpenGL.this.cleanup();
                    return false;
                }
            });
        }
        
        public void cancel() {
            this.m_canceled = true;
        }
        
        private int emptyTexture(final GL gl) {
            final int[] tex = { 0 };
            gl.glEnable(3553);
            GLTexture.generateRenderTexture(gl, this.m_imageWidth, this.m_imageHeight, tex, true);
            return tex[0];
        }
        
        private byte[] renderToTexture(final int texId, final GLRenderer glRenderer, final String imgFormat) {
            final GL gl = glRenderer.getDevice();
            final int imgWidth = this.m_imageWidth;
            final int imgHeight = this.m_imageHeight;
            final float[] previousClearColor = new float[4];
            gl.glGetFloatv(3106, previousClearColor, 0);
            RenderStateManager.getInstance().applyStates(glRenderer);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glRenderer.m_stateManager.setVertexArrayComponents(0);
            final RenderStateManager stateManager = RenderStateManager.getInstance();
            stateManager.applyMatrixMode(gl, MatrixModes.TEXTURE, Matrix44.IDENTITY);
            stateManager.applyMatrixMode(gl, MatrixModes.PROJECTION, Matrix44.IDENTITY);
            final ViewPort vp = new ViewPort(0, 0, imgWidth, imgHeight);
            stateManager.applyViewportAndOrtho2D(gl, vp);
            stateManager.reset();
            EffectManager.getInstance().resetAllEffects();
            StencilStateManager.getInstance().clear(gl);
            stateManager.applyMatrixMode(gl, MatrixModes.MODEL_VIEW, Matrix44.IDENTITY);
            glRenderer.setCameraMatrix(Matrix44.IDENTITY);
            final float offsetX = vp.getHalfResX() * (this.m_offsetX - 0.5f) / this.m_zoom;
            final float offsetY = vp.getHalfResY() * (this.m_offsetY - 0.5f) / this.m_zoom;
            this.m_entity3D.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            final TransformerSRT transformer = new TransformerSRT();
            transformer.setScale(this.m_zoom, this.m_zoom, 1.0f);
            transformer.setTranslation(offsetX, offsetY, 0.0f);
            this.m_entity3D.setPreRenderStates(RenderStates.m_instance);
            this.m_entity3D.setPostRenderStates(RenderStates.m_instance);
            this.m_anmInstance.updateFrame(0, this.m_entity3D, 0);
            stateManager.setColorScale(2.0f);
            this.m_entity3D.getTransformer().addTransformer(transformer);
            this.m_entity3D.renderWithoutEffect(glRenderer);
            stateManager.enableTextures(true);
            gl.glBindTexture(3553, texId);
            gl.glCopyTexImage2D(3553, 0, 6408, 0, 0, imgWidth, imgHeight, 0);
            final byte[] data = new byte[imgWidth * imgHeight * 4];
            final ByteBuffer buff = ByteBuffer.wrap(data);
            gl.glGetTexImage(3553, 0, 6408, 5121, (Buffer)buff);
            final BufferedImage img = ImageUtilities.toImage(imgWidth, imgHeight, data, ImageUtilities.PixelFormat.ARGB);
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                final BufferedImage flippedImg = ImageUtilities.copy(img, true);
                ImageIO.write(flippedImg, imgFormat, stream);
            }
            catch (IOException e) {
                AnmRastorize.m_logger.error((Object)"", (Throwable)e);
            }
            stateManager.reset();
            gl.glClearColor(previousClearColor[0], previousClearColor[1], previousClearColor[2], previousClearColor[3]);
            return stream.toByteArray();
        }
    }
    
    public interface OnTerminated
    {
        void run(byte[] p0);
    }
}
