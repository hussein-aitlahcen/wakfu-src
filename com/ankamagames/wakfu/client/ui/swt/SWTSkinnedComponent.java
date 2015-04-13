package com.ankamagames.wakfu.client.ui.swt;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class SWTSkinnedComponent extends Canvas
{
    private Point m_prefSize;
    private Image m_image;
    
    public SWTSkinnedComponent(final Composite parent, final int style, final ImageData image) {
        this(parent, style);
        this.setImage(image);
    }
    
    public SWTSkinnedComponent(final Composite parent, final int style) {
        super(parent, style | 0x100000);
        this.m_prefSize = null;
        this.addPaintListener((PaintListener)new PaintListener() {
            public void paintControl(final PaintEvent e) {
                final Rectangle bounds = SWTSkinnedComponent.this.getBounds();
                final Rectangle imageBounds = SWTSkinnedComponent.this.m_image.getBounds();
                e.gc.drawImage(SWTSkinnedComponent.this.m_image, 0, 0, imageBounds.width, imageBounds.height, 0, 0, bounds.width, bounds.height);
            }
        });
    }
    
    public void setImage(final ImageData image) {
        if (this.m_image != null) {
            this.m_image.dispose();
        }
        this.m_image = new Image((Device)this.getDisplay(), image);
    }
    
    public Point computeSize(final int wHint, final int hHint) {
        return this.computeSize(wHint, hHint, false);
    }
    
    public Point computeSize(final int wHint, final int hHint, final boolean changed) {
        if (!changed && this.m_prefSize != null) {
            return this.m_prefSize;
        }
        if (this.m_image != null) {
            final Rectangle bounds = this.m_image.getBounds();
            this.m_prefSize = new Point(bounds.width, bounds.height);
        }
        else {
            this.m_prefSize = new Point(0, 0);
        }
        return this.m_prefSize;
    }
    
    public void dispose() {
        super.dispose();
        if (this.m_image != null) {
            this.m_image.dispose();
        }
    }
}
