package com.ankamagames.framework.graphics.engine.text;

import com.sun.opengl.util.j2d.*;
import com.ankamagames.framework.graphics.engine.text.characterPacker.*;
import java.awt.geom.*;
import javax.media.opengl.glu.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.sun.opengl.impl.packrect.*;
import java.util.*;
import javax.media.opengl.*;
import java.awt.event.*;
import com.sun.opengl.impl.*;
import java.awt.image.*;
import java.awt.font.*;
import gnu.trove.*;
import com.sun.opengl.util.*;
import java.nio.*;
import com.sun.opengl.util.texture.*;
import java.awt.*;

public class CustomTextRenderer2 extends TextRenderer
{
    private static final boolean DEBUG;
    private static final boolean DISABLE_GLYPH_CACHE = false;
    private static final boolean DRAW_BBOXES = false;
    private static final long FONT_LIFE_DURATION_IN_MS = 10000L;
    static final int kSizeH = 256;
    static final int kSizeV = 128;
    private static final int CYCLES_PER_FLUSH = 1000;
    private static final float MAX_VERTICAL_FRAGMENTATION = 0.7f;
    static final int kQuadsPerBuffer = 100;
    static final int kCoordsPerVertVerts = 3;
    static final int kCoordsPerVertTex = 2;
    static final int kVertsPerQuad = 4;
    static final int kTotalBufferSizeVerts = 400;
    static final int kTotalBufferSizeCoordsVerts = 1200;
    static final int kTotalBufferSizeCoordsTex = 800;
    static final int kTotalBufferSizeBytesVerts = 4800;
    static final int kTotalBufferSizeBytesTex = 3200;
    static final int kSizeInBytes_OneVertices_VertexData = 12;
    static final int kSizeInBytes_OneVertices_TexData = 8;
    private Font font;
    private boolean antialiased;
    private boolean useFractionalMetrics;
    private int m_deltaX;
    private int m_deltaY;
    private long m_lastRenderTime;
    private AWTFont m_awtFont;
    private boolean mipmap;
    private RectanglePacker packer;
    private boolean haveMaxSize;
    private RenderDelegate renderDelegate;
    private TextureRenderer cachedBackingStore;
    private Graphics2D cachedGraphics;
    private FontRenderContext cachedFontRenderContext;
    private GlyphProducer mGlyphProducer;
    private int numRenderCycles;
    float m_offsetX;
    float m_offsetY;
    private Frame dbgFrame;
    private boolean debugged;
    Pipelined_QuadRenderer mPipelinedQuadRenderer;
    private boolean useVertexArrays;
    private boolean isExtensionAvailable_GL_VERSION_1_5;
    private boolean checkFor_isExtensionAvailable_GL_VERSION_1_5;
    private boolean smoothing;
    private final CharacterPacker m_characterPacker;
    private static VertexBufferPCT m_vertexBuffer;
    private static float[] m_colors;
    
    public CustomTextRenderer2(final Font font, final boolean antialiased, final boolean useFractionalMetrics, final int deltaX, final int deltaY) {
        this(font, antialiased, useFractionalMetrics, null, false, deltaX, deltaY);
    }
    
    public CustomTextRenderer2(final Font font, final boolean antialiased, final boolean useFractionalMetrics, final RenderDelegate renderDelegate, final int deltaX, final int deltaY) {
        this(font, antialiased, useFractionalMetrics, renderDelegate, false, deltaX, deltaY);
    }
    
    public CustomTextRenderer2(final Font font, final boolean antialiased, final boolean useFractionalMetrics, RenderDelegate renderDelegate, final boolean mipmap, final int deltaX, final int deltaY) {
        super();
        this.m_lastRenderTime = 0L;
        this.useVertexArrays = true;
        this.smoothing = true;
        this.font = font;
        this.antialiased = antialiased;
        this.useFractionalMetrics = useFractionalMetrics;
        this.mipmap = mipmap;
        this.m_deltaX = deltaX;
        this.m_deltaY = deltaY;
        this.packer = new RectanglePacker((BackingStoreManager)new Manager(), 256, 128);
        if (renderDelegate == null) {
            renderDelegate = new DefaultRenderDelegate();
        }
        this.renderDelegate = renderDelegate;
        this.mGlyphProducer = new GlyphProducer();
        this.m_characterPacker = CharacterPacker.getCharacterPacker();
    }
    
    public Rectangle2D getBounds(final String str) {
        return this.getBounds((CharSequence)str);
    }
    
    public Rectangle2D getBounds(final CharSequence str) {
        return this.normalize(this.renderDelegate.getBounds(str, this.font, this.getFontRenderContext()));
    }
    
    public FontRenderContext getFontRenderContext() {
        if (this.cachedFontRenderContext == null) {
            this.cachedFontRenderContext = this.getGraphics2D().getFontRenderContext();
        }
        return this.cachedFontRenderContext;
    }
    
    @Override
    public void beginRendering(final int width, final int height) throws GLException {
        this._beginRendering(-width / 2, -height / 2);
    }
    
    @Override
    public void begin3DRendering() throws GLException {
        this._beginRendering(0.0f, 0.0f);
    }
    
    @Override
    public void setColor(final float r, final float g, final float b, final float a) throws GLException {
        this.getBackingStore().setColor(1.0f, 1.0f, 1.0f, 1.0f);
        CustomTextRenderer2.m_colors[0] = r * a;
        CustomTextRenderer2.m_colors[1] = g * a;
        CustomTextRenderer2.m_colors[2] = b * a;
        CustomTextRenderer2.m_colors[3] = a;
    }
    
    public void flush() {
        this.flushGlyphPipeline();
    }
    
    @Override
    public void endRendering() throws GLException {
        this._endRendering();
    }
    
    @Override
    public void end3DRendering() throws GLException {
        this._endRendering();
    }
    
    public void dispose() throws GLException {
        this.packer.dispose();
        this.packer = null;
        this.cachedBackingStore = null;
        this.cachedGraphics = null;
        this.cachedFontRenderContext = null;
        if (this.dbgFrame != null) {
            this.dbgFrame.dispose();
        }
    }
    
    private static Rectangle2D preNormalize(final Rectangle2D src) {
        final int minX = (int)Math.floor(src.getMinX()) - 1;
        final int minY = (int)Math.floor(src.getMinY()) - 1;
        final int maxX = (int)Math.ceil(src.getMaxX()) + 1;
        final int maxY = (int)Math.ceil(src.getMaxY()) + 1;
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }
    
    private Rectangle2D normalize(final Rectangle2D src) {
        final int boundary = (int)Math.max(1.0, 0.015 * this.font.getSize());
        return new Rectangle2D.Double((int)Math.floor(src.getMinX() - boundary), (int)Math.floor(src.getMinY() - boundary), (int)Math.ceil(src.getWidth() + 2 * boundary), (int)Math.ceil(src.getHeight()) + 2 * boundary);
    }
    
    private TextureRenderer getBackingStore() {
        final TextureRenderer renderer = (TextureRenderer)this.packer.getBackingStore();
        if (renderer != this.cachedBackingStore) {
            if (this.cachedGraphics != null) {
                this.cachedGraphics.dispose();
                this.cachedGraphics = null;
                this.cachedFontRenderContext = null;
            }
            this.cachedBackingStore = renderer;
        }
        return this.cachedBackingStore;
    }
    
    private Graphics2D getGraphics2D() {
        final TextureRenderer renderer = this.getBackingStore();
        if (this.cachedGraphics == null) {
            (this.cachedGraphics = renderer.createGraphics()).setComposite(AlphaComposite.Src);
            this.cachedGraphics.setColor(Color.WHITE);
            this.cachedGraphics.setFont(this.font);
            this.cachedGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, this.antialiased ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            this.cachedGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.useFractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        }
        return this.cachedGraphics;
    }
    
    private void _beginRendering(final float offsetX, final float offsetY) {
        final GL gl = GLU.getCurrentGL();
        this.m_lastRenderTime = System.currentTimeMillis();
        if (CustomTextRenderer2.DEBUG && !this.debugged) {
            this.debug(gl);
        }
        this.m_offsetX = offsetX;
        this.m_offsetY = offsetY;
        final GLRenderer glRenderer = RendererType.OpenGL.getRenderer();
        glRenderer.m_stateManager.setVertexArrayComponents(13);
        RenderStateManager.getInstance().setBlendFunc(BlendModes.SrcAlpha, BlendModes.InvSrcAlpha);
        RenderStateManager.getInstance().applyStates(glRenderer);
        this.getBackingStore().begin3DRendering();
        if (!this.haveMaxSize) {
            final int[] sz = { 0 };
            gl.glGetIntegerv(3379, sz, 0);
            this.packer.setMaxSize(sz[0], sz[0]);
            this.haveMaxSize = true;
        }
        if (this.mipmap && !this.getBackingStore().isUsingAutoMipmapGeneration()) {
            if (CustomTextRenderer2.DEBUG) {
                System.err.println("Disabled mipmapping in TextRenderer");
            }
            this.mipmap = false;
        }
    }
    
    private void _endRendering() throws GLException {
        this.flushGlyphPipeline();
        this.getBackingStore().end3DRendering();
        if (++this.numRenderCycles >= 1000) {
            this.numRenderCycles = 0;
            if (CustomTextRenderer2.DEBUG) {
                System.err.println("Clearing unused entries in endRendering()");
            }
            this.clearUnusedEntries();
        }
    }
    
    private void clearUnusedEntries() {
        final List deadRects = new ArrayList();
        this.packer.visit((RectVisitor)new RectVisitor() {
            public void visit(final Rect rect) {
                final TextData data = (TextData)rect.getUserData();
                if (!data.used()) {
                    deadRects.add(rect);
                }
            }
        });
        for (final Rect r : deadRects) {
            this.packer.remove(r);
            final String string = ((TextData)r.getUserData()).string();
            if (string != null) {
                this.mGlyphProducer.clearCacheEntry(string);
            }
        }
        final float frag = this.packer.verticalFragmentationRatio();
        if (!deadRects.isEmpty() && frag > 0.7f) {
            if (CustomTextRenderer2.DEBUG) {
                System.err.println("Compacting TextRenderer backing store due to vertical fragmentation " + frag);
            }
            this.packer.compact();
        }
        if (CustomTextRenderer2.DEBUG) {
            this.getBackingStore().markDirty(0, 0, this.getBackingStore().getWidth(), this.getBackingStore().getHeight());
        }
    }
    
    private void flushGlyphPipeline() {
        if (this.mPipelinedQuadRenderer != null) {
            this.mPipelinedQuadRenderer.draw();
        }
    }
    
    private void debug(final GL gl) {
        this.dbgFrame = new Frame("TextRenderer Debug Output");
        final GLCanvas dbgCanvas = new GLCanvas(new GLCapabilities(), (GLCapabilitiesChooser)null, GLContext.getCurrent(), (GraphicsDevice)null);
        dbgCanvas.addGLEventListener((GLEventListener)new DebugListener(gl, this.dbgFrame, dbgCanvas));
        this.dbgFrame.add((Component)dbgCanvas);
        final FPSAnimator anim = new FPSAnimator((GLAutoDrawable)dbgCanvas, 10);
        this.dbgFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        anim.stop();
                    }
                }).start();
            }
        });
        this.dbgFrame.setSize(256, 128);
        this.dbgFrame.setVisible(true);
        anim.start();
        this.debugged = true;
    }
    
    public void setUseVertexArrays(final boolean useVertexArrays) {
        this.useVertexArrays = useVertexArrays;
    }
    
    public final boolean getUseVertexArrays() {
        return this.useVertexArrays;
    }
    
    public void setSmoothing(final boolean smoothing) {
        this.smoothing = smoothing;
        this.getBackingStore().setSmoothing(smoothing);
    }
    
    public boolean getSmoothing() {
        return this.smoothing;
    }
    
    private final boolean is15Available(final GL gl) {
        if (!this.checkFor_isExtensionAvailable_GL_VERSION_1_5) {
            this.isExtensionAvailable_GL_VERSION_1_5 = gl.isExtensionAvailable("GL_VERSION_1_5");
            this.checkFor_isExtensionAvailable_GL_VERSION_1_5 = true;
        }
        return this.isExtensionAvailable_GL_VERSION_1_5;
    }
    
    @Override
    public String getFontName() {
        if (this.font == null) {
            return null;
        }
        String fontStyle;
        if (this.font.isBold()) {
            fontStyle = (this.font.isItalic() ? "bolditalic" : "bold");
        }
        else {
            fontStyle = (this.font.isItalic() ? "italic" : "plain");
        }
        return this.font.getFamily() + '-' + fontStyle + '-' + this.font.getSize();
    }
    
    @Override
    public int getFontStyle() {
        int style = 0;
        if (this.font.isBold()) {
            style |= 0x1;
        }
        if (this.font.isItalic()) {
            style |= 0x2;
        }
        return style;
    }
    
    @Override
    public com.ankamagames.framework.graphics.engine.text.Font getFont() {
        if (this.font == null) {
            return null;
        }
        if (this.m_awtFont == null) {
            (this.m_awtFont = new AWTFont(this.font, true, this.isBlured())).setDeltaXY(this.m_deltaX, this.m_deltaY);
        }
        return this.m_awtFont;
    }
    
    @Override
    public com.ankamagames.framework.graphics.engine.text.Font createDerivedFont(final int fontStyle, final float size) {
        if (this.font == null) {
            return null;
        }
        final Font newFont = this.font.deriveFont(fontStyle, size);
        return new AWTFont(newFont, true, false);
    }
    
    @Override
    public int getCharacterWidth(final char character) {
        final FontRenderContext frc = this.getFontRenderContext();
        final Rectangle2D characterBounds = this.font.getStringBounds(String.valueOf(character), frc);
        return (int)characterBounds.getWidth();
    }
    
    @Override
    public int getVisualCharacterWidth(final char character) {
        return this.getCharacterWidth(character);
    }
    
    @Override
    public int getVisualCharacterHeight(final char character) {
        final FontRenderContext frc = this.getFontRenderContext();
        final Rectangle2D characterBounds = this.font.getStringBounds(String.valueOf(character), frc);
        return (int)characterBounds.getHeight();
    }
    
    @Override
    public int getMaxCharacterWidth() {
        final FontRenderContext frc = this.getFontRenderContext();
        final Rectangle2D bounds = this.font.getMaxCharBounds(frc);
        return (int)bounds.getWidth();
    }
    
    @Override
    public int getMaxCharacterHeight() {
        final FontRenderContext frc = this.getFontRenderContext();
        final Rectangle2D bounds = this.font.getMaxCharBounds(frc);
        return (int)bounds.getHeight();
    }
    
    @Override
    public int getMaxVisibleTextLength(final String line, final int maxCount, final int maxWidth) {
        for (int i = Math.min(line.length() - 1, maxCount - 1); i >= 0; --i) {
            if (this.getLineWidth(line.substring(0, i + 1)) <= maxWidth) {
                return i + 1;
            }
        }
        return 0;
    }
    
    @Override
    public int getLineWidth(final String line) {
        final Rectangle2D bounds = this.font.getStringBounds(line, this.getFontRenderContext());
        return (int)bounds.getWidth();
    }
    
    @Override
    public int getLineHeight(final String line) {
        final Rectangle2D bounds = this.font.getStringBounds(line, this.getFontRenderContext());
        return (int)bounds.getHeight();
    }
    
    @Override
    public int getDescent(final String line) {
        final LineMetrics lineMetrics = this.font.getLineMetrics(line, this.getFontRenderContext());
        return (int)Math.ceil(lineMetrics.getDescent());
    }
    
    @Override
    public boolean isBlured() {
        return false;
    }
    
    @Override
    public void draw(final char[] str, final float x, final float y) throws GLException {
        this.draw(str, x, y, 1.0f);
    }
    
    @Override
    public void draw(final char[] str, final float x, final float y, final float scale) throws GLException {
        this.draw(str, x, str.length, y, scale);
    }
    
    @Override
    public void draw(final char[] line, final float x, final int endX, final float y, final float zoom) {
        this.draw(line, x, endX, y, zoom, 0.0f);
    }
    
    @Override
    public void draw(final char[] chars, final float _x, final int endX, final float _y, final float zoom, final float justificationSpacing) {
        if (this.m_awtFont == null) {
            return;
        }
        float x = _x + (this.m_offsetX - this.renderDelegate.getBorder());
        final float y = _y + this.m_offsetY;
        final GLRenderer glRenderer = RendererType.OpenGL.getRenderer();
        CustomTextRenderer2.m_vertexBuffer.begin();
        for (int length = Math.min(chars.length, endX), index = 0; index < length; ++index) {
            int num;
            for (num = 1; index + num < length && this.m_characterPacker.mustBePacked(chars[index + num]); ++num) {}
            final String code = new String(chars, index, num);
            final Glyph character = this.mGlyphProducer.getGlyph(code);
            if (character != null) {
                if (code.equals(" ")) {
                    x += (character.getAdvance() + justificationSpacing) * zoom;
                }
                else {
                    character.draw3D(x, y, 0.0f, zoom);
                    x += character.getAdvance() * zoom;
                    index += num - 1;
                }
            }
        }
        CustomTextRenderer2.m_vertexBuffer.end();
        final int numVertices = CustomTextRenderer2.m_vertexBuffer.getNumVertices();
        final GL gl = glRenderer.getDevice();
        gl.glVertexPointer(2, 5126, 0, (Buffer)CustomTextRenderer2.m_vertexBuffer.getPositionBuffer());
        gl.glColorPointer(4, 5126, 0, (Buffer)CustomTextRenderer2.m_vertexBuffer.getColorBuffer());
        gl.glTexCoordPointer(2, 5126, 0, (Buffer)CustomTextRenderer2.m_vertexBuffer.getTexCoord0Buffer());
        gl.glDrawArrays(7, 0, numVertices);
    }
    
    static {
        DEBUG = Debug.isPropertyDefined("jogl.debug.TextRenderer");
        CustomTextRenderer2.m_colors = new float[4];
        final int maxVertices = 4096;
        CustomTextRenderer2.m_vertexBuffer = VertexBufferPCT.Factory.newInstance(4096);
    }
    
    class TextData
    {
        private String str;
        private Point origin;
        private Rectangle2D origRect;
        private long lastUsed;
        
        TextData(final String str, final Point origin, final Rectangle2D origRect) {
            super();
            this.str = str;
            this.origin = origin;
            this.origRect = origRect;
            this.lastUsed = CustomTextRenderer2.this.m_lastRenderTime;
        }
        
        String string() {
            return this.str;
        }
        
        Point origin() {
            return this.origin;
        }
        
        int origOriginX() {
            return (int)(-this.origRect.getMinX());
        }
        
        int origOriginY() {
            return (int)(-this.origRect.getMinY());
        }
        
        Rectangle2D origRect() {
            return this.origRect;
        }
        
        boolean used() {
            return CustomTextRenderer2.this.m_lastRenderTime - this.lastUsed < 10000L;
        }
        
        void markUsed() {
            this.lastUsed = CustomTextRenderer2.this.m_lastRenderTime;
        }
        
        void clearUsed() {
            this.lastUsed = 0L;
        }
    }
    
    class Manager implements BackingStoreManager
    {
        private Graphics2D g;
        
        public Object allocateBackingStore(final int w, final int h) {
            TextureRenderer renderer;
            if (CustomTextRenderer2.this.renderDelegate.intensityOnly()) {
                renderer = TextureRenderer.createAlphaOnlyRenderer(w, h, CustomTextRenderer2.this.mipmap);
            }
            else {
                renderer = new TextureRenderer(w, h, true, CustomTextRenderer2.this.mipmap);
            }
            renderer.setSmoothing(CustomTextRenderer2.this.smoothing);
            if (CustomTextRenderer2.DEBUG) {
                System.err.println(" TextRenderer allocating backing store " + w + " x " + h);
            }
            return renderer;
        }
        
        public void deleteBackingStore(final Object backingStore) {
            ((TextureRenderer)backingStore).dispose();
        }
        
        public boolean preExpand(final Rect cause, final int attemptNumber) {
            if (attemptNumber == 0) {
                if (CustomTextRenderer2.DEBUG) {
                    System.err.println("Clearing unused entries in preExpand(): attempt number " + attemptNumber);
                }
                CustomTextRenderer2.this.clearUnusedEntries();
                return true;
            }
            return false;
        }
        
        public void additionFailed(final Rect cause, final int attemptNumber) {
            CustomTextRenderer2.this.packer.clear();
            CustomTextRenderer2.this.mGlyphProducer.clearAllCacheEntries();
            if (CustomTextRenderer2.DEBUG) {
                System.err.println(" *** Cleared all text because addition failed ***");
            }
        }
        
        public boolean canCompact() {
            return true;
        }
        
        public void beginMovement(final Object oldBackingStore, final Object newBackingStore) {
            final TextureRenderer newRenderer = (TextureRenderer)newBackingStore;
            this.g = newRenderer.createGraphics();
        }
        
        public void move(final Object oldBackingStore, final Rect oldLocation, final Object newBackingStore, final Rect newLocation) {
            final TextureRenderer oldRenderer = (TextureRenderer)oldBackingStore;
            final TextureRenderer newRenderer = (TextureRenderer)newBackingStore;
            if (oldRenderer == newRenderer) {
                this.g.setComposite(AlphaComposite.Src);
                this.g.copyArea(oldLocation.x(), oldLocation.y(), oldLocation.w(), oldLocation.h(), newLocation.x() - oldLocation.x(), newLocation.y() - oldLocation.y());
            }
            else {
                final Image img = oldRenderer.getImage();
                this.g.setComposite(AlphaComposite.Clear);
                this.g.drawRect(newLocation.x(), newLocation.y(), newLocation.w(), newLocation.h());
                this.g.setComposite(AlphaComposite.Src);
                this.g.drawImage(img, newLocation.x(), newLocation.y(), newLocation.x() + newLocation.w(), newLocation.y() + newLocation.h(), oldLocation.x(), oldLocation.y(), oldLocation.x() + oldLocation.w(), oldLocation.y() + oldLocation.h(), null);
            }
        }
        
        public void endMovement(final Object oldBackingStore, final Object newBackingStore) {
            this.g.dispose();
            final TextureRenderer newRenderer = (TextureRenderer)newBackingStore;
            newRenderer.markDirty(0, 0, newRenderer.getWidth(), newRenderer.getHeight());
        }
    }
    
    class Glyph
    {
        private String chars;
        private GlyphProducer producer;
        private float advance;
        private GlyphVector singleUnicodeGlyphVector;
        private Rect glyphRectForTextureMapping;
        private boolean needAdvance;
        
        public Glyph(final String chars, final GlyphVector singleUnicodeGlyphVector, final GlyphProducer producer) {
            super();
            this.chars = chars;
            for (int i = 0; i < singleUnicodeGlyphVector.getNumGlyphs(); ++i) {
                this.advance += singleUnicodeGlyphVector.getGlyphMetrics(i).getAdvance();
            }
            this.singleUnicodeGlyphVector = singleUnicodeGlyphVector;
            this.producer = producer;
        }
        
        public String getChars() {
            return this.chars;
        }
        
        public float getAdvance() {
            return this.advance;
        }
        
        public float draw3D(final float inX, final float inY, final float z, final float scaleFactor) {
            if (this.glyphRectForTextureMapping == null) {
                this.upload();
            }
            try {
                final TextureRenderer renderer = CustomTextRenderer2.this.getBackingStore();
                final TextureCoords wholeImageTexCoords = renderer.getTexture().getImageTexCoords();
                final float xScale = wholeImageTexCoords.right();
                final float yScale = wholeImageTexCoords.bottom();
                final Rect rect = this.glyphRectForTextureMapping;
                final TextData data = (TextData)rect.getUserData();
                data.markUsed();
                final Rectangle2D origRect = data.origRect();
                final float x = inX - scaleFactor * (data.origOriginX() - CustomTextRenderer2.this.m_awtFont.getDeltaX());
                final float y = inY - scaleFactor * ((float)origRect.getHeight() - data.origOriginY() - CustomTextRenderer2.this.m_awtFont.getDeltaY());
                final int texturex = rect.x() + (data.origin().x - data.origOriginX());
                final int texturey = renderer.getHeight() - rect.y() - (int)origRect.getHeight() - (data.origin().y - data.origOriginY());
                final int width = (int)origRect.getWidth();
                final int height = (int)origRect.getHeight();
                final float tx1 = xScale * texturex / renderer.getWidth();
                final float ty1 = yScale * (1.0f - texturey / renderer.getHeight());
                final float tx2 = xScale * (texturex + width) / renderer.getWidth();
                final float ty2 = yScale * (1.0f - (texturey + height) / renderer.getHeight());
                CustomTextRenderer2.m_vertexBuffer.pushVertex(x, y, tx1, ty1, CustomTextRenderer2.m_colors);
                CustomTextRenderer2.m_vertexBuffer.pushVertex(x + width * scaleFactor, y, tx2, ty1, CustomTextRenderer2.m_colors);
                CustomTextRenderer2.m_vertexBuffer.pushVertex(x + width * scaleFactor, y + height * scaleFactor, tx2, ty2, CustomTextRenderer2.m_colors);
                CustomTextRenderer2.m_vertexBuffer.pushVertex(x, y + height * scaleFactor, tx1, ty2, CustomTextRenderer2.m_colors);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return this.advance;
        }
        
        public void clear() {
            this.glyphRectForTextureMapping = null;
        }
        
        private void upload() {
            final GlyphVector gv = this.getGlyphVector();
            final Rectangle2D origBBox = preNormalize(CustomTextRenderer2.this.renderDelegate.getBounds(this.chars, CustomTextRenderer2.this.font, CustomTextRenderer2.this.getFontRenderContext()));
            final Rectangle2D bbox = CustomTextRenderer2.this.normalize(origBBox);
            final Point origin = new Point((int)(-bbox.getMinX()), (int)(-bbox.getMinY()));
            final Rect rect = new Rect(0, 0, (int)bbox.getWidth(), (int)bbox.getHeight(), (Object)new TextData(this.chars, origin, origBBox));
            CustomTextRenderer2.this.packer.add(rect);
            this.glyphRectForTextureMapping = rect;
            final Graphics2D g = CustomTextRenderer2.this.getGraphics2D();
            final int strx = rect.x() + origin.x;
            final int stry = rect.y() + origin.y;
            g.setComposite(AlphaComposite.Clear);
            g.fillRect(rect.x(), rect.y(), rect.w(), rect.h());
            g.setComposite(AlphaComposite.Src);
            CustomTextRenderer2.this.renderDelegate.draw(g, this.chars, strx, stry);
            CustomTextRenderer2.this.getBackingStore().markDirty(rect.x(), rect.y(), rect.w(), rect.h());
        }
        
        private GlyphVector getGlyphVector() {
            if (this.singleUnicodeGlyphVector != null) {
                final GlyphVector gv = this.singleUnicodeGlyphVector;
                this.singleUnicodeGlyphVector = null;
                return gv;
            }
            return GlyphVectorCache.INSTANCE.getGlyphVector(this.chars, CustomTextRenderer2.this.font, CustomTextRenderer2.this.getFontRenderContext());
        }
    }
    
    class GlyphProducer
    {
        final THashMap<String, Glyph> m_glyphCache;
        
        GlyphProducer() {
            super();
            this.m_glyphCache = new THashMap<String, Glyph>();
        }
        
        public void clearCacheEntry(final String chars) {
            final Glyph glyph = this.m_glyphCache.remove(chars);
            if (glyph != null) {
                glyph.clear();
            }
        }
        
        public void clearAllCacheEntries() {
            this.m_glyphCache.forEachValue(new TObjectProcedure<Glyph>() {
                @Override
                public boolean execute(final Glyph object) {
                    object.clear();
                    return true;
                }
            });
            this.m_glyphCache.clear();
        }
        
        public Glyph getGlyph(final String chars) {
            Glyph glyph = this.m_glyphCache.get(chars);
            if (glyph == null) {
                final GlyphVector glyphVector = GlyphVectorCache.INSTANCE.getGlyphVector(chars, CustomTextRenderer2.this.font, CustomTextRenderer2.this.getFontRenderContext());
                glyph = new Glyph(chars, glyphVector, this);
                this.m_glyphCache.put(glyph.getChars(), glyph);
            }
            return glyph;
        }
    }
    
    class Pipelined_QuadRenderer
    {
        int mOutstandingGlyphsVerticesPipeline;
        FloatBuffer mTexCoords;
        FloatBuffer mVertCoords;
        boolean usingVBOs;
        int mVBO_For_ResuableTileVertices;
        int mVBO_For_ResuableTileTexCoords;
        
        Pipelined_QuadRenderer() {
            super();
            this.mOutstandingGlyphsVerticesPipeline = 0;
            final GL gl = GLU.getCurrentGL();
            this.mVertCoords = BufferUtil.newFloatBuffer(1200);
            this.mTexCoords = BufferUtil.newFloatBuffer(800);
            this.usingVBOs = (CustomTextRenderer2.this.getUseVertexArrays() && CustomTextRenderer2.this.is15Available(gl));
            if (this.usingVBOs) {
                try {
                    final int[] vbos = new int[2];
                    gl.glGenBuffers(2, IntBuffer.wrap(vbos));
                    this.mVBO_For_ResuableTileVertices = vbos[0];
                    this.mVBO_For_ResuableTileTexCoords = vbos[1];
                    gl.glBindBuffer(34962, this.mVBO_For_ResuableTileVertices);
                    gl.glBufferData(34962, 4800, (Buffer)null, 35040);
                    gl.glBindBuffer(34962, this.mVBO_For_ResuableTileTexCoords);
                    gl.glBufferData(34962, 3200, (Buffer)null, 35040);
                }
                catch (Exception e) {
                    CustomTextRenderer2.this.isExtensionAvailable_GL_VERSION_1_5 = false;
                    this.usingVBOs = false;
                }
            }
        }
        
        public void glTexCoord2f(final float v, final float v1) {
            this.mTexCoords.put(v);
            this.mTexCoords.put(v1);
        }
        
        public void glVertex3f(final float inX, final float inY, final float inZ) {
            this.mVertCoords.put(inX);
            this.mVertCoords.put(inY);
            this.mVertCoords.put(inZ);
            ++this.mOutstandingGlyphsVerticesPipeline;
            if (this.mOutstandingGlyphsVerticesPipeline >= 400) {
                this.draw();
            }
        }
        
        private void draw() {
            if (CustomTextRenderer2.this.useVertexArrays) {
                this.drawVertexArrays();
            }
            else {
                this.drawIMMEDIATE();
            }
        }
        
        private void drawVertexArrays() {
            if (this.mOutstandingGlyphsVerticesPipeline > 0) {
                final GL gl = GLU.getCurrentGL();
                final TextureRenderer renderer = CustomTextRenderer2.this.getBackingStore();
                final Texture texture = renderer.getTexture();
                this.mVertCoords.rewind();
                this.mTexCoords.rewind();
                gl.glEnableClientState(32884);
                if (this.usingVBOs) {
                    gl.glBindBuffer(34962, this.mVBO_For_ResuableTileVertices);
                    gl.glBufferSubData(34962, 0, this.mOutstandingGlyphsVerticesPipeline * 12, (Buffer)this.mVertCoords);
                    gl.glVertexPointer(3, 5126, 0, 0L);
                }
                else {
                    gl.glVertexPointer(3, 5126, 0, (Buffer)this.mVertCoords);
                }
                gl.glEnableClientState(32888);
                if (this.usingVBOs) {
                    gl.glBindBuffer(34962, this.mVBO_For_ResuableTileTexCoords);
                    gl.glBufferSubData(34962, 0, this.mOutstandingGlyphsVerticesPipeline * 8, (Buffer)this.mTexCoords);
                    gl.glTexCoordPointer(2, 5126, 0, 0L);
                }
                else {
                    gl.glTexCoordPointer(2, 5126, 0, (Buffer)this.mTexCoords);
                }
                gl.glDrawArrays(7, 0, this.mOutstandingGlyphsVerticesPipeline);
                this.mVertCoords.rewind();
                this.mTexCoords.rewind();
                this.mOutstandingGlyphsVerticesPipeline = 0;
            }
        }
        
        private void drawIMMEDIATE() {
            if (this.mOutstandingGlyphsVerticesPipeline > 0) {
                final TextureRenderer renderer = CustomTextRenderer2.this.getBackingStore();
                final Texture texture = renderer.getTexture();
                final GL gl = GLU.getCurrentGL();
                gl.glBegin(7);
                try {
                    final int numberOfQuads = this.mOutstandingGlyphsVerticesPipeline / 4;
                    this.mVertCoords.rewind();
                    this.mTexCoords.rewind();
                    for (int i = 0; i < numberOfQuads; ++i) {
                        gl.glTexCoord2f(this.mTexCoords.get(), this.mTexCoords.get());
                        gl.glVertex3f(this.mVertCoords.get(), this.mVertCoords.get(), this.mVertCoords.get());
                        gl.glTexCoord2f(this.mTexCoords.get(), this.mTexCoords.get());
                        gl.glVertex3f(this.mVertCoords.get(), this.mVertCoords.get(), this.mVertCoords.get());
                        gl.glTexCoord2f(this.mTexCoords.get(), this.mTexCoords.get());
                        gl.glVertex3f(this.mVertCoords.get(), this.mVertCoords.get(), this.mVertCoords.get());
                        gl.glTexCoord2f(this.mTexCoords.get(), this.mTexCoords.get());
                        gl.glVertex3f(this.mVertCoords.get(), this.mVertCoords.get(), this.mVertCoords.get());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    gl.glEnd();
                    this.mVertCoords.rewind();
                    this.mTexCoords.rewind();
                    this.mOutstandingGlyphsVerticesPipeline = 0;
                }
            }
        }
    }
    
    class DebugListener implements GLEventListener
    {
        private GLU glu;
        private Frame frame;
        private GLCanvas canvas;
        
        DebugListener(final GL gl, final Frame frame, final GLCanvas dbgCanvas) {
            super();
            this.glu = new GLU();
            this.frame = frame;
            this.canvas = dbgCanvas;
        }
        
        public void display(final GLAutoDrawable drawable) {
            final GL gl = GLU.getCurrentGL();
            gl.glClear(16640);
            if (CustomTextRenderer2.this.packer == null) {
                return;
            }
            final TextureRenderer rend = CustomTextRenderer2.this.getBackingStore();
            final int w = rend.getWidth();
            final int h = rend.getHeight();
            rend.beginOrthoRendering(w, h);
            rend.drawOrthoRect(0, 0);
            rend.endOrthoRendering();
            if (this.canvas.getWidth() != w || this.canvas.getHeight() != h) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        DebugListener.this.canvas.setSize(w, h);
                        DebugListener.this.frame.pack();
                    }
                });
            }
        }
        
        public void dispose(final GLAutoDrawable drawable) {
            this.glu = null;
            this.frame = null;
        }
        
        public void init(final GLAutoDrawable drawable) {
        }
        
        public void reshape(final GLAutoDrawable drawable, final int x, final int y, final int width, final int height) {
        }
        
        public void displayChanged(final GLAutoDrawable drawable, final boolean modeChanged, final boolean deviceChanged) {
        }
    }
}
