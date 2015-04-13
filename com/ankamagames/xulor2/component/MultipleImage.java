package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.component.mesh.*;
import java.awt.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;
import java.net.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.tween.*;

public class MultipleImage extends Container implements PixmapClient, PixmapChangeClient
{
    public static final String TAG = "multipleImage";
    public static final String INTERNAL_POPUP = "internalPopup";
    public static final String INTERNAL_POPUP_TEXT_VIEW = "internalPopupTextView";
    protected MultipleImageMesh m_imageMesh;
    private boolean m_hasBeenDragged;
    private Point m_oldPosition;
    protected boolean m_pixmapParametersDirty;
    protected boolean m_imageParametersDirty;
    protected boolean m_imageDataListDirty;
    private boolean m_shrinkToImageHeight;
    private boolean m_shrinkToImageWidth;
    protected int m_deltaX;
    protected int m_deltaY;
    private int m_lastDragDeltaX;
    private int m_lastDragDeltaY;
    protected int m_chunkWidth;
    protected int m_chunkHeight;
    private int m_totalWidth;
    private int m_totalHeight;
    private boolean m_manualInnerMove;
    private boolean m_toggleInnerMoveOnClick;
    private final ArrayList<ImageData> m_imageDataList;
    private final ArrayList<TextData> m_textDataList;
    private Pixmap m_pixmap;
    private TextData m_selectedTextData;
    private EventListener m_mousePressedListener;
    private EventListener m_mouseReleasedListener;
    private EventListener m_mouseDraggedListener;
    private EventListener m_mouseMovedListener;
    private Widget m_internalPopup;
    private TextView m_internalPopupTextView;
    private InnerMoveTween m_moveTween;
    private String m_particlePath;
    private MultipleImageListener m_imageListener;
    public static final int MODULATION_COLOR_HASH;
    public static final int IMAGE_PATH_HASH;
    public static final int MANUAL_INNER_MOVE_HASH;
    public static final int USE_INNER_MOVE_TWEEN_HASH;
    public static final int SHRINK_TO_IMAGE_WIDTH_HASH;
    public static final int SHRINK_TO_IMAGE_HEIGHT_HASH;
    public static final int TOGGLE_INNER_MOVE_ON_CLICK_HASH;
    
    public MultipleImage() {
        super();
        this.m_hasBeenDragged = false;
        this.m_oldPosition = null;
        this.m_pixmapParametersDirty = false;
        this.m_imageParametersDirty = true;
        this.m_imageDataListDirty = false;
        this.m_shrinkToImageHeight = false;
        this.m_shrinkToImageWidth = false;
        this.m_chunkWidth = 0;
        this.m_chunkHeight = 0;
        this.m_totalWidth = 0;
        this.m_totalHeight = 0;
        this.m_manualInnerMove = false;
        this.m_toggleInnerMoveOnClick = false;
        this.m_imageDataList = new ArrayList<ImageData>();
        this.m_textDataList = new ArrayList<TextData>();
        this.m_pixmap = null;
        this.m_selectedTextData = null;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof PixmapElement) {
            this.setPixmap((PixmapElement)e);
        }
        else if (e instanceof Widget && e.getId().equals("internalPopup")) {
            (this.m_internalPopup = (Widget)e).setVisible(false);
        }
        super.add(e);
    }
    
    @Override
    protected void addInnerMeshes() {
        if (this.m_entity != null && this.m_imageMesh.getEntity() != null) {
            this.m_entity.addChild(this.m_imageMesh.getEntity());
        }
        super.addInnerMeshes();
    }
    
    public void setImageListener(final MultipleImageListener imageListener) {
        this.m_imageListener = imageListener;
    }
    
    @Override
    public String getTag() {
        return "multipleImage";
    }
    
    @Override
    public void setModulationColor(final Color c) {
        if (this.m_imageMesh != null) {
            this.m_imageMesh.setModulationColor(c);
        }
        for (int i = 0, size = this.m_textDataList.size(); i < size; ++i) {
            final XulorParticleSystem particle = this.m_textDataList.get(i).getParticle();
            if (particle != null) {
                particle.setGlobalColor(c.getRed() * c.getAlpha(), c.getGreen() * c.getAlpha(), c.getBlue() * c.getAlpha(), c.getAlpha());
            }
        }
    }
    
    public boolean getManualInnerMove() {
        return this.m_manualInnerMove;
    }
    
    public void setManualInnerMove(final boolean manualInnerMove) {
        this.m_manualInnerMove = manualInnerMove;
        this.reflowCursorType();
    }
    
    private void reflowCursorType() {
        CursorFactory.CursorType cursorType = CursorFactory.CursorType.DEFAULT;
        if (this.m_manualInnerMove) {
            if (this.m_shrinkToImageHeight && !this.m_shrinkToImageWidth) {
                cursorType = CursorFactory.CursorType.HORIZONTAL_RESIZE;
            }
            else if (this.m_shrinkToImageWidth && !this.m_shrinkToImageHeight) {
                cursorType = CursorFactory.CursorType.VERTICAL_RESIZE;
            }
            else {
                cursorType = CursorFactory.CursorType.MOVE;
            }
        }
        this.setCursorType(cursorType);
    }
    
    public void setUseInnerMoveTween(final boolean start) {
        this.stopInnerMoveTween();
        if (start) {
            this.startInnerMoveTween(0.0f, 1.0f, 0.0f, 1.0f, 5000);
        }
    }
    
    public boolean getShrinkToImageWidth() {
        return this.m_shrinkToImageWidth;
    }
    
    public void setShrinkToImageWidth(final boolean shrinkToImageWidth) {
        this.m_shrinkToImageWidth = shrinkToImageWidth;
        this.reflowCursorType();
    }
    
    public boolean getShrinkToImageHeight() {
        return this.m_shrinkToImageHeight;
    }
    
    public void setShrinkToImageHeight(final boolean shrinkToImageHeight) {
        this.m_shrinkToImageHeight = shrinkToImageHeight;
        this.reflowCursorType();
    }
    
    public boolean getToggleInnerMoveOnClick() {
        return this.m_toggleInnerMoveOnClick;
    }
    
    public void setToggleInnerMoveOnClick(final boolean toggleInnerMoveOnClick) {
        this.m_toggleInnerMoveOnClick = toggleInnerMoveOnClick;
    }
    
    @Override
    public void setPixmap(final PixmapElement p) {
        this.m_imageDataList.clear();
        if (this.m_imageMesh != null) {
            this.m_imageMesh.clear();
        }
        (this.m_pixmap = p.getPixmap()).addClient(this);
        final ImageData data = new ImageData(this.m_pixmap, 0, 0);
        this.m_imageDataList.add(data);
    }
    
    @Override
    public Color getModulationColor() {
        return (this.m_imageMesh != null) ? this.m_imageMesh.getModulationColor() : null;
    }
    
    public int getDeltaX() {
        return this.m_deltaX;
    }
    
    public void setDeltaX(final int deltaX) {
        final int min = -(this.m_totalWidth - this.m_appearance.getContentWidth());
        final int max = 0;
        final int clampedDeltaX = MathHelper.clamp(deltaX, min, 0);
        if (clampedDeltaX != this.m_deltaX) {
            this.m_deltaX = clampedDeltaX;
            this.m_imageParametersDirty = true;
            this.setNeedsToPreProcess();
            if (this.m_imageListener != null) {
                if (this.m_deltaX == min) {
                    this.m_imageListener.onDeltaXBounded((byte)2);
                }
                else if (this.m_deltaX == 0) {
                    this.m_imageListener.onDeltaXBounded((byte)0);
                }
                else {
                    this.m_imageListener.onDeltaXBounded((byte)1);
                }
            }
        }
    }
    
    public int getDeltaY() {
        return this.m_deltaY;
    }
    
    public void setDeltaY(final int deltaY) {
        final int min = 0;
        final int max = this.m_totalHeight - this.m_appearance.getContentHeight();
        final int clampedDeltaY = MathHelper.clamp(deltaY, 0, max);
        if (clampedDeltaY != this.m_deltaY) {
            this.m_deltaY = clampedDeltaY;
            this.m_imageParametersDirty = true;
            this.setNeedsToPreProcess();
            if (this.m_imageListener != null) {
                if (this.m_deltaY == 0) {
                    this.m_imageListener.onDeltaYBounded((byte)2);
                }
                else if (this.m_deltaY == max) {
                    this.m_imageListener.onDeltaYBounded((byte)0);
                }
                else {
                    this.m_imageListener.onDeltaYBounded((byte)1);
                }
            }
        }
    }
    
    public String getParticlePath() {
        return this.m_particlePath;
    }
    
    public void setParticlePath(final String particlePath) {
        this.m_particlePath = particlePath;
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return true;
    }
    
    public void setImagePath(final String path) {
        if (path == null) {
            return;
        }
        URL url;
        try {
            url = ContentFileHelper.getURL(path);
        }
        catch (MalformedURLException e1) {
            MultipleImage.m_logger.error((Object)("URL invalide : " + path));
            return;
        }
        XMLDocumentContainer doc;
        try {
            doc = Xulor.loadDoc(url);
        }
        catch (Exception e2) {
            MultipleImage.m_logger.error((Object)("Probl\u00e8me lors de la lecture du fichier de map d'url : " + url));
            return;
        }
        this.m_imageDataList.clear();
        if (this.m_imageMesh != null) {
            this.m_imageMesh.clear();
        }
        this.m_deltaX = 0;
        this.m_deltaY = 0;
        this.m_chunkWidth = 0;
        this.m_chunkHeight = 0;
        final ArrayList<? extends DocumentEntry> children = doc.getRootNode().getChildren();
        for (int size = children.size(), i = 0; i < size; ++i) {
            final DocumentEntry child = (DocumentEntry)children.get(i);
            if (!child.getName().equals("#text")) {
                if (!child.getName().equals("#comment")) {
                    if (child.getName().equalsIgnoreCase("parameters")) {
                        DocumentEntry entry = child.getParameterByName("maxWidth");
                        if (entry != null) {
                            this.m_chunkWidth = entry.getIntValue();
                        }
                        entry = child.getParameterByName("maxHeight");
                        if (entry != null) {
                            this.m_chunkHeight = entry.getIntValue();
                        }
                        entry = child.getParameterByName("totalWidth");
                        if (entry != null) {
                            this.m_totalWidth = entry.getIntValue();
                        }
                        entry = child.getParameterByName("totalHeight");
                        if (entry != null) {
                            this.m_totalHeight = entry.getIntValue();
                        }
                    }
                    else if (child.getName().equalsIgnoreCase("image")) {
                        int x = 0;
                        int y = 0;
                        Texture texture = null;
                        DocumentEntry entry2 = child.getParameterByName("x");
                        if (entry2 != null) {
                            x = entry2.getIntValue();
                        }
                        entry2 = child.getParameterByName("y");
                        if (entry2 != null) {
                            y = entry2.getIntValue();
                        }
                        entry2 = child.getParameterByName("texture");
                        if (entry2 != null) {
                            final String texturePath = entry2.getStringValue();
                            try {
                                final URL texURL = URLUtils.urlCompound(url, texturePath);
                                final String textureName = texURL.toString();
                                texture = this.createTexturePowerOfTwo(textureName);
                            }
                            catch (Exception e3) {
                                MultipleImage.m_logger.error((Object)"Probl\u00e8me lors de la r\u00e9cup\u00e9ration de la texture de la map");
                            }
                        }
                        final ImageData data = new ImageData(new Pixmap(texture), x, y);
                        this.m_imageDataList.add(data);
                    }
                    else if (child.getName().equalsIgnoreCase("text")) {
                        int x = 0;
                        int y = 0;
                        int width = 40;
                        int height = 40;
                        String key = null;
                        DocumentEntry entry3 = child.getParameterByName("x");
                        if (entry3 != null) {
                            x = entry3.getIntValue();
                        }
                        entry3 = child.getParameterByName("y");
                        if (entry3 != null) {
                            y = entry3.getIntValue();
                        }
                        entry3 = child.getParameterByName("width");
                        if (entry3 != null) {
                            width = entry3.getIntValue();
                        }
                        entry3 = child.getParameterByName("height");
                        if (entry3 != null) {
                            height = entry3.getIntValue();
                        }
                        entry3 = child.getParameterByName("key");
                        if (entry3 != null) {
                            key = entry3.getStringValue();
                        }
                        XulorParticleSystem particleSystem = null;
                        if (this.m_particlePath != null) {
                            particleSystem = XulorParticleSystemFactory.getInstance().getFreeParticleSystem(this.m_particlePath);
                            if (particleSystem != null) {
                                XulorParticleSystemManager.INSTANCE.addParticleSystem(particleSystem);
                                particleSystem.addReference();
                                particleSystem.setPosition(x + width / 2, y + height / 2);
                                particleSystem.prepareParticlesBeforeRendering(this.m_imageMesh.getParticleEntity());
                                final Color modulationColor = this.m_imageMesh.getModulationColor();
                                if (modulationColor != null) {
                                    particleSystem.setGlobalColor(modulationColor.getRed() * modulationColor.getAlpha(), modulationColor.getGreen() * modulationColor.getAlpha(), modulationColor.getBlue() * modulationColor.getAlpha(), modulationColor.getAlpha());
                                }
                            }
                        }
                        final TextData textData = new TextData(key, x, y, width, height, particleSystem);
                        this.m_textDataList.add(textData);
                    }
                }
            }
        }
        this.m_imageDataListDirty = true;
        this.m_imageParametersDirty = true;
        this.setNeedsToPreProcess();
    }
    
    private boolean computeMinSize() {
        boolean minSizeChanged = false;
        int minWidth = 0;
        int minHeight = 0;
        if (this.m_shrinkToImageHeight) {
            minHeight = this.m_totalHeight;
        }
        if (this.m_shrinkToImageWidth) {
            minWidth = this.m_totalWidth;
        }
        if (this.m_minSize == null || minWidth != this.m_minSize.width || minHeight != this.m_minSize.height) {
            this.setMinSize(new Dimension(minWidth, minHeight));
            minSizeChanged = true;
        }
        return minSizeChanged;
    }
    
    public void startInnerMoveTween(final float minXPercentage, final float maxXPercentage, final float minYPercentage, final float maxYPercentage, final int duration) {
        this.removeTweensOfType(InnerMoveTween.class);
        (this.m_moveTween = new InnerMoveTween(minXPercentage, maxXPercentage, minYPercentage, maxYPercentage, this, 0, duration, TweenFunction.PROGRESSIVE)).setRepeat(-1);
        this.addTween(this.m_moveTween);
    }
    
    public void stopInnerMoveTween() {
        this.removeTweensOfType(InnerMoveTween.class);
    }
    
    public void setTweenPaused(final boolean paused) {
        if (this.m_moveTween != null) {
            this.m_moveTween.setPaused(paused);
        }
    }
    
    public boolean hasMoveTween() {
        return this.m_moveTween != null;
    }
    
    private Texture createTexturePowerOfTwo(final String path) {
        return TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), Engine.getTextureName(path), path, false);
    }
    
    @Override
    public void onChildrenAdded() {
        super.onChildrenAdded();
        this.m_internalPopupTextView = (TextView)this.getElementMap().getElement("internalPopupTextView");
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_moveTween = null;
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
        this.m_mouseDraggedListener = null;
        this.m_mouseMovedListener = null;
        this.m_mouseReleasedListener = null;
        this.m_mousePressedListener = null;
        this.m_imageListener = null;
        if (this.m_imageMesh != null) {
            this.m_imageMesh.onCheckIn();
            this.m_imageMesh = null;
        }
        if (this.m_pixmap != null) {
            this.m_pixmap.removeClient(this);
            this.m_pixmap = null;
        }
        this.m_imageDataList.clear();
        this.m_particlePath = null;
        for (int i = this.m_textDataList.size() - 1; i >= 0; --i) {
            final XulorParticleSystem particle = this.m_textDataList.get(i).getParticle();
            if (particle != null) {
                particle.removeReference();
            }
        }
        this.m_textDataList.clear();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setNonBlocking(false);
        this.setLayoutManager(null);
        final DecoratorAppearance app = DecoratorAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
        this.setNeedsToPreProcess();
        (this.m_imageMesh = new MultipleImageMesh()).onCheckOut();
        this.addListener();
    }
    
    @Override
    public void addedToWidgetTree() {
        super.addedToWidgetTree();
        this.m_containerParent.setNeedsScissor(true);
    }
    
    @Override
    public void validate() {
        if (this.m_imageMesh != null) {
            this.m_imageMesh.updateVertex(this.m_size, this.m_appearance.getMargin(), this.m_appearance.getBorder(), this.m_appearance.getPadding());
        }
        if (this.m_internalPopup != null) {
            if (this.m_selectedTextData != null) {
                this.m_internalPopup.setVisible(true);
                this.m_internalPopup.setSizeToPrefSize();
                final int x = -Alignment9.CENTER.getX(this.m_selectedTextData.getWidth(), this.m_internalPopup.getWidth()) + this.m_selectedTextData.getX() + this.m_deltaX;
                final int y = this.m_selectedTextData.getY() + this.m_deltaY + this.m_selectedTextData.getHeight();
                this.m_internalPopup.setPosition(x, y);
            }
            else {
                this.m_internalPopup.setVisible(false);
            }
        }
        super.validate();
    }
    
    private void addListener() {
        this.m_mousePressedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final MouseEvent m = (MouseEvent)event;
                MultipleImage.this.m_oldPosition = new Point(m.getX(MultipleImage.this), m.getY(MultipleImage.this));
                MultipleImage.this.removeTweensOfType(InnerDragMoveTween.class);
                MultipleImage.this.m_hasBeenDragged = false;
                return false;
            }
        };
        this.addEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
        this.m_mouseReleasedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                MultipleImage.this.m_oldPosition = null;
                if (MultipleImage.this.m_hasBeenDragged && MultipleImage.this.m_lastDragDeltaX != Integer.MIN_VALUE && MultipleImage.this.m_lastDragDeltaY != Integer.MIN_VALUE) {
                    final int deltaX = MultipleImage.this.m_deltaX + MultipleImage.this.m_lastDragDeltaX * 20;
                    final int deltaY = MultipleImage.this.m_deltaY + MultipleImage.this.m_lastDragDeltaY * 20;
                    MultipleImage.this.addTween(new InnerDragMoveTween(deltaX, deltaY, 1000));
                    MultipleImage.this.m_lastDragDeltaX = Integer.MIN_VALUE;
                    MultipleImage.this.m_lastDragDeltaY = Integer.MIN_VALUE;
                }
                return false;
            }
        };
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_DRAGGED_OUT, this.m_mouseReleasedListener, false);
        this.m_mouseDraggedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final MouseEvent m = (MouseEvent)event;
                if (!MultipleImage.this.m_manualInnerMove) {
                    return false;
                }
                MultipleImage.this.m_hasBeenDragged = true;
                final int mouseDeltaX = m.getX(MultipleImage.this) - MultipleImage.this.m_oldPosition.x;
                final int mouseDeltaY = m.getY(MultipleImage.this) - MultipleImage.this.m_oldPosition.y;
                final int deltaX = MultipleImage.this.m_deltaX + mouseDeltaX;
                final int deltaY = MultipleImage.this.m_deltaY + mouseDeltaY;
                MultipleImage.this.m_lastDragDeltaX = mouseDeltaX;
                MultipleImage.this.m_lastDragDeltaY = mouseDeltaY;
                MultipleImage.this.setDeltaX(deltaX);
                MultipleImage.this.setDeltaY(deltaY);
                MultipleImage.this.m_oldPosition = new Point(m.getX(MultipleImage.this), m.getY(MultipleImage.this));
                return false;
            }
        };
        this.addEventListener(Events.MOUSE_DRAGGED, this.m_mouseDraggedListener, false);
        this.m_mouseMovedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final MouseEvent me = (MouseEvent)event;
                final int mouseX = me.getX(MultipleImage.this) - MultipleImage.this.m_deltaX;
                final int mouseY = me.getY(MultipleImage.this) - MultipleImage.this.m_deltaY;
                TextData data = null;
                for (int i = 0, size = MultipleImage.this.m_textDataList.size(); i < size; ++i) {
                    final TextData textData = MultipleImage.this.m_textDataList.get(i);
                    if (MultipleImage.this.isInside(textData, mouseX, mouseY)) {
                        data = textData;
                        break;
                    }
                }
                if (MultipleImage.this.m_selectedTextData == data) {
                    return false;
                }
                MultipleImage.this.m_selectedTextData = data;
                MultipleImage.this.m_internalPopup.setVisible(MultipleImage.this.m_selectedTextData != null);
                if (MultipleImage.this.m_selectedTextData != null) {
                    MultipleImage.this.m_internalPopupTextView.setText(MultipleImage.this.m_selectedTextData.getTranslatedString());
                }
                return false;
            }
        };
        this.addEventListener(Events.MOUSE_MOVED, this.m_mouseMovedListener, false);
        this.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (MultipleImage.this.m_toggleInnerMoveOnClick && !MultipleImage.this.m_hasBeenDragged) {
                    MultipleImage.this.setManualInnerMove(!MultipleImage.this.m_manualInnerMove);
                }
                return false;
            }
        }, false);
    }
    
    private boolean isInside(final TextData data, final int x, final int y) {
        return data.getX() <= x && data.getX() + data.getWidth() >= x && data.getY() <= y && data.getY() + data.getHeight() >= y;
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        boolean needUpdate = false;
        final boolean minSizeChanged = this.computeMinSize();
        if (this.m_pixmapParametersDirty) {
            final int height = this.m_pixmap.getHeight();
            this.m_chunkHeight = height;
            this.m_totalHeight = height;
            final int width = this.m_pixmap.getWidth();
            this.m_chunkWidth = width;
            this.m_totalWidth = width;
            this.setDeltaX(this.m_deltaX);
            this.setDeltaY(this.m_deltaY);
            this.m_pixmapParametersDirty = false;
            this.m_imageParametersDirty = true;
            this.m_imageDataListDirty = true;
        }
        if (this.m_imageMesh != null && this.m_imageParametersDirty) {
            this.m_imageMesh.setX(this.m_deltaX);
            this.m_imageMesh.setY(this.m_deltaY);
            this.m_imageMesh.setHeight(this.m_chunkHeight);
            this.m_imageMesh.setWidth(this.m_chunkWidth);
            this.m_imageParametersDirty = false;
            needUpdate = true;
        }
        if (this.m_imageMesh != null && this.m_imageDataListDirty) {
            this.m_imageMesh.clear();
            for (int i = 0, size = this.m_imageDataList.size(); i < size; ++i) {
                this.m_imageMesh.addImageData(this.m_imageDataList.get(i));
            }
            this.m_imageDataListDirty = false;
            needUpdate = true;
        }
        for (int i = 0, size = this.m_textDataList.size(); i < size; ++i) {
            this.m_textDataList.get(i).recomputeParticlePosition(this.m_deltaX, this.m_deltaY);
        }
        if (needUpdate) {
            try {
                if (this.m_appearance != null) {
                    this.m_imageMesh.updateVertex(this.m_size, this.m_appearance.getMargin(), this.m_appearance.getBorder(), this.m_appearance.getPadding());
                }
            }
            catch (NullPointerException e) {
                MultipleImage.m_logger.error((Object)("imageMesh = " + this.m_imageMesh + ", appearance = " + this.m_appearance), (Throwable)e);
            }
        }
        if (minSizeChanged) {
            this.invalidateMinSize();
        }
        return ret;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        super.postProcess(deltaTime);
        this.m_imageMesh.getParticleEntity().removeAllChildren();
        for (int i = 0, size = this.m_textDataList.size(); i < size; ++i) {
            final XulorParticleSystem particle = this.m_textDataList.get(i).getParticle();
            if (particle != null) {
                particle.prepareParticlesBeforeRendering(this.m_imageMesh.getParticleEntity());
            }
        }
        return true;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final MultipleImage image = (MultipleImage)source;
        super.copyElement(source);
        image.removeEventListener(Events.MOUSE_DRAGGED_OUT, this.m_mouseReleasedListener, false);
        image.removeEventListener(Events.MOUSE_DRAGGED, this.m_mouseDraggedListener, false);
        image.removeEventListener(Events.MOUSE_MOVED, this.m_mouseMovedListener, false);
        image.removeEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
        image.setModulationColor(image.getModulationColor());
        image.setManualInnerMove(this.m_manualInnerMove);
        image.setShrinkToImageWidth(this.m_shrinkToImageWidth);
        image.setShrinkToImageHeight(this.m_shrinkToImageHeight);
        image.setToggleInnerMoveOnClick(this.m_toggleInnerMoveOnClick);
    }
    
    @Override
    public void pixmapChanged(final Pixmap p) {
        this.m_pixmapParametersDirty = true;
        this.setNeedsToPreProcess();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == MultipleImage.SHRINK_TO_IMAGE_WIDTH_HASH) {
            this.setShrinkToImageWidth(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == MultipleImage.SHRINK_TO_IMAGE_HEIGHT_HASH) {
            this.setShrinkToImageHeight(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == MultipleImage.TOGGLE_INNER_MOVE_ON_CLICK_HASH) {
            this.setToggleInnerMoveOnClick(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == MultipleImage.USE_INNER_MOVE_TWEEN_HASH) {
            this.setUseInnerMoveTween(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != MultipleImage.MANUAL_INNER_MOVE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setManualInnerMove(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == MultipleImage.SHRINK_TO_IMAGE_WIDTH_HASH) {
            this.setShrinkToImageWidth(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == MultipleImage.SHRINK_TO_IMAGE_HEIGHT_HASH) {
            this.setShrinkToImageHeight(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == MultipleImage.MODULATION_COLOR_HASH) {
            this.setModulationColor((Color)value);
        }
        else {
            if (hash != MultipleImage.IMAGE_PATH_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setImagePath((String)value);
        }
        return true;
    }
    
    static {
        MODULATION_COLOR_HASH = "modulationColor".hashCode();
        IMAGE_PATH_HASH = "imagePath".hashCode();
        MANUAL_INNER_MOVE_HASH = "manualInnerMove".hashCode();
        USE_INNER_MOVE_TWEEN_HASH = "useInnerMoveTween".hashCode();
        SHRINK_TO_IMAGE_WIDTH_HASH = "shrinkToImageWidth".hashCode();
        SHRINK_TO_IMAGE_HEIGHT_HASH = "shrinkToImageHeight".hashCode();
        TOGGLE_INNER_MOVE_ON_CLICK_HASH = "toggleInnerMoveOnClick".hashCode();
    }
    
    public class InnerMoveTween extends AbstractWidgetTween<Boolean>
    {
        private float m_minXPercentage;
        private float m_maxXPercentage;
        private float m_minYPercentage;
        private float m_maxYPercentage;
        private float m_currentMinXPerc;
        private float m_currentMinYPerc;
        
        public InnerMoveTween(final float minX, final float maxX, final float minY, final float maxY, final Widget w, final int delay, final int duration, final TweenFunction function) {
            super(true, false, w, delay, duration, function);
            this.m_minXPercentage = minX;
            this.m_minYPercentage = minY;
            this.m_maxXPercentage = maxX;
            this.m_maxYPercentage = maxY;
            final int deltaWidth = -(MultipleImage.this.m_totalWidth - MultipleImage.this.m_appearance.getContentWidth());
            this.m_currentMinXPerc = MultipleImage.this.m_deltaX / ((deltaWidth == 0) ? 1 : deltaWidth);
            final int deltaHeight = MultipleImage.this.m_totalHeight - MultipleImage.this.m_appearance.getContentHeight();
            this.m_currentMinYPerc = MultipleImage.this.m_deltaY / ((deltaHeight == 0) ? 1 : deltaHeight);
        }
        
        @Override
        public boolean process(final int deltaTime) {
            super.process(deltaTime);
            if (this.m_function != null) {
                final boolean ascending = (boolean)this.m_a;
                if (!ascending) {
                    this.m_currentMinXPerc = this.m_minXPercentage;
                    this.m_currentMinYPerc = this.m_minYPercentage;
                }
                final float minXPerc = ascending ? this.m_currentMinXPerc : this.m_maxXPercentage;
                final float maxXPerc = ascending ? this.m_maxXPercentage : this.m_currentMinXPerc;
                final float minYPerc = ascending ? this.m_currentMinYPerc : this.m_maxYPercentage;
                final float maxYPerc = ascending ? this.m_maxYPercentage : this.m_currentMinYPerc;
                final float percentageX = this.m_function.compute(minXPerc, maxXPerc, this.m_elapsedTime, this.m_duration);
                final float percentageY = this.m_function.compute(minYPerc, maxYPerc, this.m_elapsedTime, this.m_duration);
                MultipleImage.this.setDeltaX((int)(-(MultipleImage.this.m_totalWidth - MultipleImage.this.m_appearance.getContentWidth()) * percentageX));
                MultipleImage.this.setDeltaY((int)((MultipleImage.this.m_totalHeight - MultipleImage.this.m_appearance.getContentHeight()) * percentageY));
            }
            return true;
        }
        
        @Override
        public void onEnd() {
            super.onEnd();
            MultipleImage.this.m_moveTween = null;
        }
    }
    
    public class InnerDragMoveTween extends AbstractWidgetTween<Boolean>
    {
        private int m_deltaXA;
        private int m_deltaXB;
        private int m_deltaYA;
        private int m_deltaYB;
        
        public InnerDragMoveTween(final int deltaXB, final int deltaYB, final int duration) {
            super(true, false, MultipleImage.this, 0, duration, TweenFunction.FULL_TO_NULL);
            this.m_deltaXA = MultipleImage.this.m_deltaX;
            this.m_deltaYA = MultipleImage.this.m_deltaY;
            this.m_deltaXB = deltaXB;
            this.m_deltaYB = deltaYB;
        }
        
        @Override
        public boolean process(final int deltaTime) {
            super.process(deltaTime);
            if (this.m_function != null) {
                final int deltaX = (int)this.m_function.compute(this.m_deltaXA, this.m_deltaXB, this.m_elapsedTime, this.m_duration);
                final int deltaY = (int)this.m_function.compute(this.m_deltaYA, this.m_deltaYB, this.m_elapsedTime, this.m_duration);
                MultipleImage.this.setDeltaX(deltaX);
                MultipleImage.this.setDeltaY(deltaY);
            }
            return true;
        }
        
        @Override
        public void onEnd() {
            super.onEnd();
        }
    }
    
    public interface MultipleImageListener
    {
        public static final byte MIN = 0;
        public static final byte MIDDLE = 1;
        public static final byte MAX = 2;
        
        void onDeltaXBounded(byte p0);
        
        void onDeltaYBounded(byte p0);
    }
}
