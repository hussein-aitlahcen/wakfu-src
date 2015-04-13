package com.ankamagames.xulor2.appearance;

import java.awt.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.util.alignment.*;

public class ProgressBarAppearance extends DecoratorAppearance
{
    public static final String COLOR_NAME = "progressBar";
    public static final String BORDER_COLOR_NAME = "progressBarBorder";
    public static final String TAG = "progressBarAppearance";
    private ColorElement m_color;
    private ColorElement m_borderColor;
    private Insets m_innerBorder;
    private Alignment9 m_position;
    private PixmapElement[] m_pixmaps;
    public static final int INNER_BORDER_HASH;
    public static final int POSITION_HASH;
    
    public ProgressBarAppearance() {
        super();
        this.m_color = null;
        this.m_borderColor = null;
        this.m_innerBorder = null;
        this.m_position = Alignment9.CENTER;
        this.m_pixmaps = new PixmapElement[9];
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof PixmapElement) {
            final PixmapElement pe = (PixmapElement)e;
            switch (pe.getPosition()) {
                case NORTH_WEST: {
                    this.m_pixmaps[0] = pe;
                    break;
                }
                case NORTH: {
                    this.m_pixmaps[1] = pe;
                    break;
                }
                case NORTH_EAST: {
                    this.m_pixmaps[2] = pe;
                    break;
                }
                case WEST: {
                    this.m_pixmaps[3] = pe;
                    break;
                }
                case CENTER: {
                    this.m_pixmaps[4] = pe;
                    break;
                }
                case EAST: {
                    this.m_pixmaps[5] = pe;
                    break;
                }
                case SOUTH_WEST: {
                    this.m_pixmaps[6] = pe;
                    break;
                }
                case SOUTH: {
                    this.m_pixmaps[7] = pe;
                    break;
                }
                case SOUTH_EAST: {
                    this.m_pixmaps[8] = pe;
                    break;
                }
            }
            this.applyPixmaps();
        }
        else if (e instanceof ColorElement) {
            final ColorElement ce = (ColorElement)e;
            if (ce.getName() == null || ce.getName().equalsIgnoreCase("progressBar")) {
                ce.addEventListener(Events.COLOR_CHANGED, new EventListener() {
                    @Override
                    public boolean run(final Event event) {
                        ProgressBarAppearance.this.applyColor();
                        return false;
                    }
                }, false);
                this.m_color = (ColorElement)e;
                this.applyColor();
            }
            else if (ce.getName().equals("progressBarBorder")) {
                ce.addEventListener(Events.COLOR_CHANGED, new EventListener() {
                    @Override
                    public boolean run(final Event event) {
                        ProgressBarAppearance.this.applyColor();
                        return false;
                    }
                }, false);
                this.m_borderColor = (ColorElement)e;
                this.applyColor();
            }
        }
        super.add(e);
    }
    
    @Override
    public String getTag() {
        return "progressBarAppearance";
    }
    
    @Override
    public void setWidget(final Widget w) {
        super.setWidget(w);
        if (this.m_color != null) {
            this.applyColor();
        }
        else {
            this.applyPixmaps();
        }
        this.applyInnerBorder();
        this.applyPosition();
    }
    
    public void setPosition(final Alignment9 position) {
        this.m_position = position;
        this.applyPosition();
    }
    
    public void setInnerBorder(final Insets insets) {
        if (insets == null) {
            this.m_innerBorder = null;
        }
        else {
            this.m_innerBorder = new Insets(insets.top, insets.left, insets.bottom, insets.right);
        }
        this.applyInnerBorder();
    }
    
    public Insets getInnerBorder() {
        return this.m_innerBorder;
    }
    
    @Override
    public void destroyAllRemovableDecorators() {
        super.destroyAllRemovableDecorators();
        this.destroyColor();
        this.destroyPixmaps();
    }
    
    public void applyPixmaps() {
        if (this.m_widget == null || !(this.m_widget instanceof ProgressBar)) {
            return;
        }
        if (this.m_pixmaps[4] == null) {
            return;
        }
        if (this.m_pixmaps[0] != null && this.m_pixmaps[8] != null) {
            ((ProgressBar)this.m_widget).setPixmaps(this.m_pixmaps[0], this.m_pixmaps[1], this.m_pixmaps[2], this.m_pixmaps[3], this.m_pixmaps[4], this.m_pixmaps[5], this.m_pixmaps[6], this.m_pixmaps[7], this.m_pixmaps[8]);
        }
        else if (this.m_pixmaps[0] == null && this.m_pixmaps[8] == null) {
            ((ProgressBar)this.m_widget).setPixmaps(this.m_pixmaps[4], this.m_pixmaps[4], this.m_pixmaps[4], this.m_pixmaps[4], this.m_pixmaps[4], this.m_pixmaps[4], this.m_pixmaps[4], this.m_pixmaps[4], this.m_pixmaps[4]);
        }
    }
    
    private void applyInnerBorder() {
        if (this.m_innerBorder == null || this.m_widget == null || !(this.m_widget instanceof ProgressBar)) {
            return;
        }
        final ProgressBar pb = (ProgressBar)this.m_widget;
        pb.setInnerBorder(this.m_innerBorder);
    }
    
    private void applyPosition() {
        if (!(this.m_widget instanceof ProgressBar)) {
            return;
        }
        final ProgressBar pb = (ProgressBar)this.m_widget;
        pb.setInnerPosition(this.m_position);
    }
    
    private void applyColor() {
        if (this.m_widget == null || !(this.m_widget instanceof ProgressBar)) {
            return;
        }
        final ProgressBar pb = (ProgressBar)this.m_widget;
        if (this.m_color != null) {
            pb.setColor(this.m_color.getColor(), "progressBar");
        }
        if (this.m_borderColor != null) {
            pb.setColor(this.m_borderColor.getColor(), "progressBarBorder");
        }
    }
    
    private void destroyColor() {
        if (this.m_color != null) {
            this.destroy(this.m_color);
            this.m_color = null;
        }
        if (this.m_borderColor != null) {
            this.destroy(this.m_borderColor);
            this.m_borderColor = null;
        }
    }
    
    private void destroyPixmaps() {
        for (int i = 0; i < this.m_pixmaps.length; ++i) {
            if (this.m_pixmaps[i] != null) {
                this.destroy(this.m_pixmaps[i]);
                this.m_pixmaps[i] = null;
            }
        }
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final ProgressBarAppearance pba = (ProgressBarAppearance)source;
        if (this.m_innerBorder != null) {
            pba.setInnerBorder(this.m_innerBorder);
        }
        pba.setPosition(this.m_position);
        super.copyElement(source);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_position = Alignment9.CENTER;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_color = null;
        this.m_borderColor = null;
        for (int i = 0; i < this.m_pixmaps.length; ++i) {
            this.m_pixmaps[i] = null;
        }
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ProgressBarAppearance.INNER_BORDER_HASH) {
            this.setInnerBorder(cl.convertToInsets(value));
        }
        else {
            if (hash != ProgressBarAppearance.POSITION_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setPosition(Alignment9.value(value));
        }
        return true;
    }
    
    static {
        INNER_BORDER_HASH = "innerBorder".hashCode();
        POSITION_HASH = "position".hashCode();
    }
}
