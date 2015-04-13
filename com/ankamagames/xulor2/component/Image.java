package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class Image extends Widget implements PixmapClient, PixmapChangeClient
{
    public static final String TAG = "Image";
    private static final double EPSILON = 0.001;
    protected ImageMesh m_imageMesh;
    protected boolean m_scaled;
    protected boolean m_keepAspectRatio;
    protected boolean m_imageParametersDirty;
    protected boolean m_imageIsWaitingForFade;
    protected Alignment17 m_align;
    protected Dimension m_displayedSize;
    private boolean m_fadeOnPixmapLoaded;
    public static final int ALIGN_HASH;
    public static final int FADE_ON_PIXMAP_LOADED_HASH;
    public static final int DISPLAY_SHAPE_HASH;
    public static final int DISPLAY_SIZE_HASH;
    public static final int FLIP_HORIZONTALY_HASH;
    public static final int FLIP_VERTICALY_HASH;
    public static final int KEEP_ASPECT_RATIO_HASH;
    public static final int MODULATION_COLOR_HASH;
    public static final int PIXMAP_HASH;
    public static final int SCALED_HASH;
    
    public Image() {
        super();
        this.m_scaled = false;
        this.m_keepAspectRatio = false;
        this.m_imageParametersDirty = true;
        this.m_imageIsWaitingForFade = false;
        this.m_align = Alignment17.CENTER;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof PixmapElement) {
            this.setPixmap((PixmapElement)e);
        }
        super.add(e);
    }
    
    @Override
    protected void addInnerMeshes() {
        super.addInnerMeshes();
        if (this.m_entity != null && this.m_imageMesh.getEntity() != null) {
            this.m_entity.addChild(this.m_imageMesh.getEntity());
        }
    }
    
    @Override
    public String getTag() {
        return "Image";
    }
    
    public Pixmap getPixmap() {
        return (this.m_imageMesh != null) ? this.m_imageMesh.getPixmap() : null;
    }
    
    public void setPixmap(final Pixmap pixmap) {
        if (this.m_imageMesh != null) {
            final Pixmap p = this.m_imageMesh.getPixmap();
            if (p != null) {
                p.removeClient(this);
            }
            this.m_imageMesh.setPixmap(pixmap);
            pixmap.addClient(this);
            this.m_imageParametersDirty = true;
            this.setNeedsToPreProcess();
        }
    }
    
    public boolean getFadeOnPixmapLoaded() {
        return this.m_fadeOnPixmapLoaded;
    }
    
    public void setFadeOnPixmapLoaded(final boolean fadeOnPixmapLoaded) {
        if (this.m_fadeOnPixmapLoaded == fadeOnPixmapLoaded) {
            return;
        }
        this.m_fadeOnPixmapLoaded = fadeOnPixmapLoaded;
        this.m_imageIsWaitingForFade = fadeOnPixmapLoaded;
        if (this.m_fadeOnPixmapLoaded && this.m_imageMesh != null) {
            this.m_imageMesh.setModulationColor(new Color(Color.WHITE_ALPHA));
        }
        this.setNeedsToPreProcess();
    }
    
    private void checkPixmapFade() {
        if (this.m_imageMesh == null) {
            return;
        }
        if (!this.m_fadeOnPixmapLoaded) {
            return;
        }
        if (!this.m_imageIsWaitingForFade) {
            return;
        }
        final Pixmap pixmap = this.m_imageMesh.getPixmap();
        if (pixmap == null) {
            return;
        }
        if (pixmap.getTexture() == null) {
            return;
        }
        if (!pixmap.getTexture().isReady()) {
            return;
        }
        this.removeTweensOfType(ModulationColorTween.class);
        this.addTween(new ModulationColorTween(Color.WHITE_ALPHA, Color.WHITE, this, 0, 250, 1, false, TweenFunction.PROGRESSIVE));
        this.m_imageIsWaitingForFade = false;
    }
    
    @Override
    public void setPixmap(final PixmapElement pixmap) {
        if (this.m_imageMesh != null) {
            Pixmap p = this.m_imageMesh.getPixmap();
            if (p != null) {
                p.removeClient(this);
            }
            p = pixmap.getPixmap();
            if (p != null) {
                p.addClient(this);
            }
            this.m_imageMesh.setPixmap(pixmap.getPixmap());
            this.m_imageParametersDirty = true;
            this.setNeedsToPreProcess();
        }
    }
    
    public boolean getScaled() {
        return this.m_scaled;
    }
    
    public void setScaled(final boolean scaled) {
        if (this.m_scaled != scaled) {
            this.m_scaled = scaled;
            this.m_imageParametersDirty = true;
            this.setNeedsToPreProcess();
        }
    }
    
    public void setDisplayShape(final WidgetShape shape) {
        if (this.m_imageMesh != null) {
            this.m_imageMesh.setShape(shape);
        }
    }
    
    public WidgetShape getDisplayShape() {
        return (this.m_imageMesh != null) ? this.m_imageMesh.getShape() : null;
    }
    
    public boolean getKeepAspectRatio() {
        return this.m_keepAspectRatio;
    }
    
    public void setKeepAspectRatio(final boolean keepAspectRatio) {
        if (this.m_keepAspectRatio != keepAspectRatio) {
            this.m_keepAspectRatio = keepAspectRatio;
            this.m_imageParametersDirty = true;
            this.setNeedsToPreProcess();
        }
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
    
    public void setDisplaySize(final Dimension size) {
        this.m_displayedSize = size;
        this.m_imageParametersDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public Dimension getDisplaySize() {
        return this.m_displayedSize;
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
    
    public void setFlipHorizontaly(final boolean flip) {
        if (this.m_imageMesh != null) {
            this.m_imageMesh.setFlipHorizontaly(flip);
            this.m_imageParametersDirty = true;
            this.setNeedsToPreProcess();
        }
    }
    
    public boolean getFlipHorizontaly() {
        return this.m_imageMesh != null && this.m_imageMesh.isFlipHorizontaly();
    }
    
    public void setFlipVerticaly(final boolean flip) {
        if (this.m_imageMesh != null) {
            this.m_imageMesh.setFlipVerticaly(flip);
            this.m_imageParametersDirty = true;
            this.setNeedsToPreProcess();
        }
    }
    
    public boolean getFlipVerticaly() {
        return this.m_imageMesh != null && this.m_imageMesh.isFlipVerticaly();
    }
    
    public ImageMesh getImageMesh() {
        return this.m_imageMesh;
    }
    
    public String getShader() {
        return this.m_imageMesh.getShader();
    }
    
    public void setShader(final String shader) {
        this.m_imageMesh.setShader(shader);
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return appearance instanceof ImageAppearance;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_align = null;
        this.m_displayedSize = null;
        if (this.m_imageMesh != null) {
            final Pixmap p = this.m_imageMesh.getPixmap();
            if (p != null) {
                p.removeClient(this);
            }
            this.m_imageMesh.onCheckIn();
            this.m_imageMesh = null;
        }
    }
    
    @Override
    public void onCheckOut() {
        this.m_align = Alignment17.CENTER;
        super.onCheckOut();
        (this.m_imageMesh = new ImageMesh()).onCheckOut();
        final ImageAppearance app = ImageAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
        this.m_fadeOnPixmapLoaded = false;
    }
    
    @Override
    public void validate() {
        if (this.m_imageMesh != null) {
            this.computeImageParameters();
            this.m_imageMesh.updateVertex(this.m_size, this.m_appearance.getMargin(), this.m_appearance.getBorder(), this.m_appearance.getPadding());
        }
        super.validate();
    }
    
    private void computeImageParameters() {
        if (this.m_imageMesh == null) {
            return;
        }
        final Pixmap pixmap = this.m_imageMesh.getPixmap();
        if (pixmap == null) {
            return;
        }
        if (this.m_displayedSize != null) {
            this.applyDisplayedSize(pixmap, this.m_appearance.getContentWidth(), this.m_appearance.getContentHeight());
            return;
        }
        if (this.m_scaled) {
            this.applyScaledSize(pixmap, this.m_appearance.getContentWidth(), this.m_appearance.getContentHeight());
            return;
        }
        this.setMeshBoundsFromComponent(pixmap.getWidth(), pixmap.getHeight(), this.m_appearance.getContentWidth(), this.m_appearance.getContentHeight());
    }
    
    private void applyDisplayedSize(final Pixmap pixmap, final int contentWidth, final int contentHeight) {
        if (this.m_keepAspectRatio) {
            final Point2i size = this.computeSize(pixmap, this.m_displayedSize.width, this.m_displayedSize.height);
            this.setMeshBoundsFromComponent(size.getX(), size.getY(), contentWidth, contentHeight);
            return;
        }
        this.setMeshBoundsFromComponent(this.m_displayedSize.width, this.m_displayedSize.height, contentWidth, contentHeight);
    }
    
    private void applyScaledSize(final Pixmap pixmap, final int contentWidth, final int contentHeight) {
        if (this.m_keepAspectRatio) {
            final Point2i size = this.computeSize(pixmap, contentWidth, contentHeight);
            this.setMeshBoundsFromComponent(size.getX(), size.getY(), contentWidth, contentHeight);
            return;
        }
        this.setMeshBounds(0, 0, contentWidth, contentHeight);
    }
    
    private Point2i computeSize(final Pixmap pixmap, final int contentWidth, final int contentHeight) {
        if (contentWidth == 0 || pixmap.getWidth() == 0 || contentHeight == 0 || pixmap.getHeight() == 0) {
            return new Point2i(contentWidth, contentHeight);
        }
        final float pixmapRatio = pixmap.getWidth() / pixmap.getHeight();
        final float widgetRatio = contentWidth / contentHeight;
        if (MathHelper.isEqual(pixmapRatio, widgetRatio)) {
            return new Point2i(contentWidth, contentHeight);
        }
        if (pixmapRatio > widgetRatio) {
            return new Point2i(contentWidth, (int)(contentWidth / pixmapRatio));
        }
        return new Point2i((int)(contentHeight * pixmapRatio), contentHeight);
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
        if (this.m_imageMesh == null) {
            return false;
        }
        if (this.m_imageMesh.getPixmap() != null) {
            int width;
            int height;
            if (this.m_displayedSize != null) {
                width = this.m_displayedSize.width;
                height = this.m_displayedSize.height;
            }
            else if (this.m_shrinkable) {
                width = 0;
                height = 0;
            }
            else {
                width = this.m_imageMesh.getPixmap().getWidth();
                height = this.m_imageMesh.getPixmap().getHeight();
            }
            if (this.m_minSize == null || width != this.m_minSize.width || height != this.m_minSize.height) {
                this.setMinSize(new Dimension(width, height));
                minSizeChanged = true;
            }
        }
        else if (this.m_minSize == null || this.m_minSize.width != 0 || this.m_minSize.height != 0) {
            this.setMinSize(new Dimension(0, 0));
            minSizeChanged = true;
        }
        return minSizeChanged;
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        boolean ret = super.preProcess(deltaTime);
        if (this.m_imageMesh != null && (this.m_imageParametersDirty || (this.m_imageMesh.getPixmap() != null && this.m_imageMesh.getPixmap().needsCompute()))) {
            final boolean minSizeChanged = this.computeMinSize();
            this.computeImageParameters();
            try {
                if (this.m_appearance != null) {
                    this.m_imageMesh.updateVertex(this.m_size, this.m_appearance.getMargin(), this.m_appearance.getBorder(), this.m_appearance.getPadding());
                }
            }
            catch (NullPointerException e) {
                Image.m_logger.error((Object)("imageMesh = " + this.m_imageMesh + ", appearance = " + this.m_appearance), (Throwable)e);
            }
            if (minSizeChanged && this.m_containerParent != null) {
                this.m_containerParent.invalidateMinSize();
            }
            this.m_imageParametersDirty = false;
        }
        if (this.m_imageIsWaitingForFade) {
            this.checkPixmapFade();
            ret |= this.m_imageIsWaitingForFade;
        }
        return ret;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final Image image = (Image)source;
        super.copyElement(source);
        image.setAlign(this.m_align);
        image.setDisplaySize((this.m_displayedSize != null) ? ((Dimension)this.m_displayedSize.clone()) : null);
        image.setKeepAspectRatio(this.m_keepAspectRatio);
        image.setScaled(this.m_scaled);
        image.setModulationColor(image.getModulationColor());
        image.setFlipHorizontaly(this.getFlipHorizontaly());
        image.setFlipVerticaly(this.getFlipVerticaly());
        image.setDisplayShape(this.getDisplayShape());
        image.setFadeOnPixmapLoaded(this.m_fadeOnPixmapLoaded);
    }
    
    @Override
    public void pixmapChanged(final Pixmap p) {
        this.m_imageParametersDirty = true;
        if (this.m_imageMesh != null && this.m_fadeOnPixmapLoaded) {
            this.m_imageMesh.setModulationColor(new Color(Color.WHITE_ALPHA));
            this.m_imageIsWaitingForFade = true;
        }
        this.setNeedsToPreProcess();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Image.ALIGN_HASH) {
            this.setAlign(Alignment17.value(value));
        }
        else if (hash == Image.DISPLAY_SHAPE_HASH) {
            this.setDisplayShape(WidgetShape.value(value));
        }
        else if (hash == Image.FADE_ON_PIXMAP_LOADED_HASH) {
            this.setFadeOnPixmapLoaded(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Image.DISPLAY_SIZE_HASH) {
            this.setDisplaySize(cl.convertToDimension(value));
        }
        else if (hash == Image.FLIP_HORIZONTALY_HASH) {
            this.setFlipHorizontaly(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Image.FLIP_VERTICALY_HASH) {
            this.setFlipVerticaly(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Image.KEEP_ASPECT_RATIO_HASH) {
            this.setKeepAspectRatio(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Image.MODULATION_COLOR_HASH) {
            this.setModulationColor(cl.convertToColor(value));
        }
        else {
            if (hash != Image.SCALED_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setScaled(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Image.ALIGN_HASH) {
            this.setAlign((Alignment17)value);
        }
        else if (hash == Image.DISPLAY_SHAPE_HASH) {
            this.setDisplayShape((WidgetShape)value);
        }
        else if (hash == Image.DISPLAY_SIZE_HASH) {
            this.setDisplaySize((Dimension)value);
        }
        else if (hash == Image.FLIP_HORIZONTALY_HASH) {
            this.setFlipHorizontaly(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Image.FLIP_VERTICALY_HASH) {
            this.setFlipVerticaly(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Image.KEEP_ASPECT_RATIO_HASH) {
            this.setKeepAspectRatio(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Image.MODULATION_COLOR_HASH) {
            this.setModulationColor((Color)value);
        }
        else if (hash == Image.PIXMAP_HASH) {
            this.setPixmap((Pixmap)value);
        }
        else {
            if (hash != Image.SCALED_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setScaled(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        ALIGN_HASH = "align".hashCode();
        FADE_ON_PIXMAP_LOADED_HASH = "fadeOnPixmapLoaded".hashCode();
        DISPLAY_SHAPE_HASH = "displayShape".hashCode();
        DISPLAY_SIZE_HASH = "displaySize".hashCode();
        FLIP_HORIZONTALY_HASH = "flipHorizontaly".hashCode();
        FLIP_VERTICALY_HASH = "flipVerticaly".hashCode();
        KEEP_ASPECT_RATIO_HASH = "keepAspectRatio".hashCode();
        MODULATION_COLOR_HASH = "modulationColor".hashCode();
        PIXMAP_HASH = "pixmap".hashCode();
        SCALED_HASH = "scaled".hashCode();
    }
}
