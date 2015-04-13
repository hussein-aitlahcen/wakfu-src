package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.util.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.xulor2.graphics.*;
import java.net.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class FlippingImage extends Widget implements PixmapChangeClient, ModulationColorClient
{
    public static final String TAG = "FlippingImage";
    private final ArrayList<Pixmap> m_pixmaps;
    private ImageMesh m_imageMesh;
    protected Alignment17 m_align;
    private boolean m_imageParametersDirty;
    private int m_duration;
    private int m_currentIndex;
    private int m_flipCount;
    private boolean m_running;
    private FlippingImageTween m_flippingImageTween;
    private boolean m_vertical;
    private boolean m_needsToLoadTextures;
    public static final int TEXTURES_HASH;
    public static final int DURATION_HASH;
    public static final int FLIP_COUNT_HASH;
    public static final int VERTICAL_HASH;
    public static final int SHADER_HASH;
    
    public FlippingImage() {
        super();
        this.m_pixmaps = new ArrayList<Pixmap>();
        this.m_align = Alignment17.CENTER;
    }
    
    @Override
    public String getTag() {
        return "FlippingImage";
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return true;
    }
    
    @Override
    protected void addInnerMeshes() {
        super.addInnerMeshes();
        if (this.m_entity != null && this.m_imageMesh.getEntity() != null) {
            this.m_entity.addChild(this.m_imageMesh.getEntity());
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_align = null;
        this.clearPixmaps();
        if (this.m_imageMesh != null) {
            final Pixmap p = this.m_imageMesh.getPixmap();
            if (p != null) {
                p.removeClient(this);
            }
            this.m_imageMesh.onCheckIn();
            this.m_imageMesh = null;
        }
    }
    
    private void clearPixmaps() {
        for (int i = 0, size = this.m_pixmaps.size(); i < size; ++i) {
            this.m_pixmaps.get(i).setTexture(null);
        }
        this.m_pixmaps.clear();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        (this.m_imageMesh = new ImageMesh()).onCheckOut();
        final DecoratorAppearance app = DecoratorAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
    }
    
    @Override
    public void validate() {
        if (this.m_imageMesh != null) {
            this.computeImageParameters();
            this.m_imageMesh.updateVertex(this.m_size, this.m_appearance.getMargin(), this.m_appearance.getBorder(), this.m_appearance.getPadding());
        }
        super.validate();
    }
    
    public Alignment17 getAlign() {
        return this.m_align;
    }
    
    public void setAlign(final Alignment17 align) {
        if (!align.equals(this.m_align)) {
            this.m_align = align;
            this.m_imageParametersDirty = true;
            this.setNeedsToPreProcess();
        }
    }
    
    private void computeImageParameters() {
        if (this.m_imageMesh == null) {
            return;
        }
        final Pixmap pixmap = this.m_imageMesh.getPixmap();
        if (pixmap == null) {
            return;
        }
        this.setMeshBoundsFromComponent(pixmap.getWidth(), pixmap.getHeight(), this.m_appearance.getContentWidth(), this.m_appearance.getContentHeight());
    }
    
    private void setMeshBoundsFromComponent(final int width, final int height, final int contentWidth, final int contentHeight) {
        final int x = this.m_align.getX(width, contentWidth);
        final int y = this.m_align.getY(height, contentHeight);
        this.setMeshBounds(x, y, width, height);
    }
    
    private void setMeshBounds(final int x, final int y, final int width, final int height) {
        this.m_imageMesh.setX(x);
        this.m_imageMesh.setY(y);
        this.m_imageMesh.setWidth(width);
        this.m_imageMesh.setHeight(height);
    }
    
    public boolean computeMinSize() {
        boolean minSizeChanged = false;
        final int biggestWidth = this.getBiggestWidth();
        final int biggestHeight = this.getBiggestHeight();
        if (this.m_minSize == null || this.m_minSize.width != biggestWidth || this.m_minSize.height != biggestHeight) {
            this.setMinSize(new Dimension(biggestWidth, biggestHeight));
            minSizeChanged = true;
        }
        return minSizeChanged;
    }
    
    private int getBiggestWidth() {
        int max = 0;
        for (final Pixmap t : this.m_pixmaps) {
            final int width = t.getWidth();
            if (width > max) {
                max = width;
            }
        }
        return max;
    }
    
    private int getBiggestHeight() {
        int max = 0;
        for (final Pixmap t : this.m_pixmaps) {
            final int height = t.getHeight();
            if (height > max) {
                max = height;
            }
        }
        return max;
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_needsToLoadTextures) {
            for (final Pixmap t : this.m_pixmaps) {
                if (!t.getTexture().isReady()) {
                    return true;
                }
            }
            this.computeMinSize();
            this.m_needsToLoadTextures = false;
        }
        if (this.m_imageMesh != null && (this.m_imageParametersDirty || (this.m_imageMesh.getPixmap() != null && this.m_imageMesh.getPixmap().needsCompute()))) {
            final boolean minSizeChanged = this.computeMinSize();
            this.computeImageParameters();
            try {
                if (this.m_appearance != null) {
                    this.m_imageMesh.updateVertex(this.m_size, this.m_appearance.getMargin(), this.m_appearance.getBorder(), this.m_appearance.getPadding());
                }
            }
            catch (NullPointerException e) {
                FlippingImage.m_logger.error((Object)("imageMesh = " + this.m_imageMesh + ", appearance = " + this.m_appearance), (Throwable)e);
            }
            if (minSizeChanged && this.m_containerParent != null) {
                this.m_containerParent.invalidateMinSize();
            }
            this.m_imageParametersDirty = false;
        }
        return ret;
    }
    
    public String getShader() {
        return this.m_imageMesh.getShader();
    }
    
    public void setShader(final String shader) {
        this.m_imageMesh.setShader(shader);
    }
    
    @Override
    public void setModulationColor(final Color c) {
        if (this.m_imageMesh != null) {
            this.m_imageMesh.setModulationColor(c);
        }
    }
    
    @Override
    public Color getModulationColor() {
        return (this.m_imageMesh != null) ? this.m_imageMesh.getModulationColor() : null;
    }
    
    @Override
    public void pixmapChanged(final Pixmap p) {
        this.m_imageParametersDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public void setPixmaps(final String[] pixmaps) {
        this.clearPixmaps();
        if (pixmaps.length == 0) {
            return;
        }
        for (final String textureUrl : pixmaps) {
            this.addTexture(textureUrl);
        }
        this.setTextureIndex(0);
        this.computeMinSize();
        this.m_needsToLoadTextures = true;
        this.setNeedsToPreProcess();
    }
    
    public void setTextures(final Iterable<String> value) {
        this.m_pixmaps.clear();
        for (final String textureUrl : value) {
            this.addTexture(textureUrl);
        }
        this.setTextureIndex(0);
        this.computeMinSize();
        this.m_needsToLoadTextures = true;
        this.setNeedsToPreProcess();
    }
    
    private void addTexture(final String textureUrl) {
        try {
            final URL url = ContentFileHelper.getURL(textureUrl);
            if (!URLUtils.urlExists(url)) {
                FlippingImage.m_logger.error((Object)("Impossible de charger la texture=" + textureUrl));
                return;
            }
            final Texture texture = TextureLoader.getInstance().loadTexture(url);
            this.m_pixmaps.add(new Pixmap(texture));
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    
    public void setTextureIndex(final int index) {
        this.m_currentIndex = index;
        if (index < 0 || index >= this.m_pixmaps.size()) {
            FlippingImage.m_logger.error((Object)("Index de texture inconnu=" + index));
            return;
        }
        this.m_imageMesh.setPixmap(this.m_pixmaps.get(index));
        this.m_imageParametersDirty = true;
        this.setNeedsToPreProcess();
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final FlippingImage flippingImage = (FlippingImage)source;
        super.copyElement(source);
        flippingImage.setAlign(this.m_align);
        flippingImage.setModulationColor(this.getModulationColor());
        flippingImage.setShader(this.getShader());
        flippingImage.setDuration(this.m_duration);
        flippingImage.setFlipCount(this.m_flipCount);
        flippingImage.setVertical(this.m_vertical);
        flippingImage.m_styleIsDirty = true;
    }
    
    public void setDuration(final int duration) {
        this.m_duration = duration;
    }
    
    public void start() {
        if (this.m_pixmaps.isEmpty()) {
            FlippingImage.m_logger.error((Object)"Impossible de lancer le flip sans texture !!!");
            return;
        }
        this.m_running = true;
        this.removeTweensOfType(FlippingImageTween.class);
        this.addTween(this.m_flippingImageTween = new FlippingImageTween(0.0f, 6.2831855f, 0, this.m_duration, TweenFunction.LINEAR, this.m_flipCount));
    }
    
    public void stop() {
        this.m_running = false;
        this.m_flippingImageTween.onEnd();
        this.removeTweensOfType(FlippingImageTween.class);
    }
    
    public void setFlipCount(final int flipCount) {
        this.m_flipCount = flipCount;
        if (this.m_running) {
            this.stop();
        }
    }
    
    public void setVertical(final boolean vertical) {
        this.m_vertical = vertical;
    }
    
    private void onTextureChange() {
        int newIndex = this.m_currentIndex + 1;
        if (newIndex >= this.m_pixmaps.size()) {
            newIndex = 0;
        }
        this.setTextureIndex(newIndex);
    }
    
    public void displayNextTexture() {
        this.onTextureChange();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == FlippingImage.DURATION_HASH) {
            this.setDuration(PrimitiveConverter.getInteger(value));
        }
        else if (hash == FlippingImage.FLIP_COUNT_HASH) {
            this.setFlipCount(PrimitiveConverter.getInteger(value));
        }
        else if (hash == FlippingImage.VERTICAL_HASH) {
            this.setVertical(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != FlippingImage.SHADER_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setShader(value);
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == FlippingImage.TEXTURES_HASH) {
            if (value == null || value instanceof String[]) {
                this.setPixmaps((String[])value);
            }
            else {
                if (!(value instanceof Iterable)) {
                    return false;
                }
                this.setTextures((Iterable<String>)value);
            }
        }
        else if (hash == FlippingImage.DURATION_HASH) {
            this.setDuration(PrimitiveConverter.getInteger(value));
        }
        else if (hash == FlippingImage.FLIP_COUNT_HASH) {
            this.setFlipCount(PrimitiveConverter.getInteger(value));
        }
        else if (hash == FlippingImage.VERTICAL_HASH) {
            this.setVertical(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != FlippingImage.SHADER_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setShader(PrimitiveConverter.getString(value));
        }
        return true;
    }
    
    static {
        TEXTURES_HASH = "textures".hashCode();
        DURATION_HASH = "duration".hashCode();
        FLIP_COUNT_HASH = "flipCount".hashCode();
        VERTICAL_HASH = "vertical".hashCode();
        SHADER_HASH = "shader".hashCode();
    }
    
    private class FlippingImageTween extends AbstractWidgetTween<Float>
    {
        private int m_textureChangeCount;
        
        public FlippingImageTween(final Float a, final Float b, final int delay, final int duration, final TweenFunction function, final int repeatCount) {
            super(a, b, FlippingImage.this, delay, duration, function);
            this.setRepeat(repeatCount);
            this.setReverseOnRepeat(false);
        }
        
        @Override
        public boolean process(final int deltaTime) {
            if (!super.process(deltaTime)) {
                return false;
            }
            if (this.m_function == null) {
                return true;
            }
            final float angle = this.m_function.compute((float)this.m_a, (float)this.m_b, this.m_elapsedTime, this.m_duration);
            final double secondFlipAngle = 4.71238898038469;
            final double firstFlipAngle = 1.5707963267948966;
            if (this.m_textureChangeCount == 0 && angle >= 1.5707963267948966) {
                ++this.m_textureChangeCount;
                FlippingImage.this.onTextureChange();
            }
            else if (this.m_textureChangeCount == 1 && angle < 1.5707963267948966) {
                this.m_textureChangeCount = 0;
            }
            FlippingImage.this.m_imageMesh.setRotation(FlippingImage.this.m_vertical ? Vector3.AXIS_X : Vector3.AXIS_Y, angle);
            return true;
        }
        
        @Override
        public void onEnd() {
            super.onEnd();
            FlippingImage.this.m_imageMesh.setRotation(FlippingImage.this.m_vertical ? Vector3.AXIS_X : Vector3.AXIS_Y, (float)this.m_b);
        }
    }
}
