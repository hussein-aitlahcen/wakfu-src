package com.ankamagames.xulor2.component;

import java.util.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class RepeatableImage extends Container implements PixmapClient
{
    public static final String TAG = "RepeatableImage";
    private ArrayList<Image> m_images;
    private PixmapElement m_pixmap;
    private Color m_modulationColor;
    private int m_repeatNumber;
    private boolean m_repeatableNumberIsDirty;
    private boolean m_pixmapIsDirty;
    public static final int HORIZONTAL_HASH;
    public static final int MODULATION_COLOR_HASH;
    public static final int REPEAT_NUMBER_HASH;
    
    public RepeatableImage() {
        super();
        this.m_images = new ArrayList<Image>();
        this.m_pixmap = null;
        this.m_modulationColor = null;
        this.m_repeatNumber = 0;
        this.m_repeatableNumberIsDirty = false;
        this.m_pixmapIsDirty = false;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof PixmapElement) {
            this.setPixmap((PixmapElement)e);
        }
        super.add(e);
    }
    
    @Override
    public void add(final DataElement e) {
        if (e instanceof LayoutManager && (this.m_layout != null || !(e instanceof RowLayout))) {
            e.release();
            return;
        }
        super.add(e);
    }
    
    @Override
    public String getTag() {
        return "RepeatableImage";
    }
    
    public void setHorizontal(final boolean horizontal) {
        if (this.m_layout instanceof RowLayout) {
            ((RowLayout)this.m_layout).setHorizontal(horizontal);
        }
    }
    
    public boolean getHorizontal() {
        return this.m_layout instanceof RowLayout && ((RowLayout)this.m_layout).isHorizontal();
    }
    
    public void setRepeatNumber(final int repeat) {
        if (repeat != this.m_repeatNumber) {
            this.m_repeatNumber = repeat;
            this.m_repeatableNumberIsDirty = true;
            this.setNeedsToPreProcess();
        }
    }
    
    public int getRepeatNumber() {
        return this.m_repeatNumber;
    }
    
    @Override
    public void setPixmap(final PixmapElement pixmap) {
        if (pixmap != this.m_pixmap) {
            this.m_pixmap = pixmap;
            this.m_pixmapIsDirty = true;
            this.setNeedsToPreProcess();
        }
    }
    
    @Override
    public void setModulationColor(final Color c) {
        if (this.m_modulationColor == c) {
            return;
        }
        this.m_modulationColor = c;
        for (int i = this.m_images.size() - 1; i >= 0; --i) {
            this.m_images.get(i).setModulationColor(c);
        }
    }
    
    @Override
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        final RepeatableImage i = (RepeatableImage)c;
        super.copyElement(i);
        while (i.m_widgetChildren.size() > 0) {
            i.m_widgetChildren.get(0).destroySelfFromParent();
        }
        i.setHorizontal(this.getHorizontal());
        i.setRepeatNumber(this.m_repeatNumber);
        i.setModulationColor(this.m_modulationColor);
    }
    
    public void computeChanges() {
        if (this.m_repeatableNumberIsDirty) {
            while (this.m_repeatNumber < this.m_images.size()) {
                this.m_images.remove(this.m_repeatNumber).destroySelfFromParent();
            }
            if (this.m_repeatNumber > this.m_images.size()) {
                if (this.m_images.size() == 0) {
                    final Image image = new Image();
                    image.onCheckOut();
                    image.setNonBlocking(true);
                    image.setModulationColor(this.m_modulationColor);
                    this.add(image);
                    image.add(this.m_pixmap.cloneElementStructure());
                    this.m_images.add(image);
                }
                while (this.m_repeatNumber > this.m_images.size()) {
                    final Image image = (Image)this.m_images.get(0).cloneElementStructure();
                    this.add(image);
                    this.m_images.add(image);
                }
            }
            this.m_repeatableNumberIsDirty = false;
        }
        if (this.m_pixmapIsDirty) {
            for (int i = this.m_images.size() - 1; i >= 0; --i) {
                this.m_images.get(i).add(this.m_pixmap.cloneElementStructure());
            }
            this.m_pixmapIsDirty = false;
        }
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_pixmapIsDirty || this.m_repeatableNumberIsDirty) {
            this.computeChanges();
            this.invalidateMinSize();
        }
        return ret;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_images.clear();
        this.m_repeatNumber = 0;
        this.m_pixmap = null;
        this.m_modulationColor = null;
        this.m_repeatableNumberIsDirty = false;
        this.m_pixmapIsDirty = false;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == RepeatableImage.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == RepeatableImage.MODULATION_COLOR_HASH) {
            this.setModulationColor(cl.convertToColor(value));
        }
        else {
            if (hash != RepeatableImage.REPEAT_NUMBER_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setRepeatNumber(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == RepeatableImage.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == RepeatableImage.MODULATION_COLOR_HASH) {
            this.setModulationColor((Color)value);
        }
        else {
            if (hash != RepeatableImage.REPEAT_NUMBER_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setRepeatNumber(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    static {
        HORIZONTAL_HASH = "horizontal".hashCode();
        MODULATION_COLOR_HASH = "modulationColor".hashCode();
        REPEAT_NUMBER_HASH = "repeatNumber".hashCode();
    }
}
