package com.ankamagames.wakfu.client.ui.swt;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

public class SWTStaticLayout extends Layout
{
    private Point m_prefSize;
    
    protected Point computeSize(final Composite composite, final int widthHint, final int heightHint, final boolean flushCache) {
        if (this.m_prefSize == null || flushCache) {
            int width = Integer.MIN_VALUE;
            int height = Integer.MIN_VALUE;
            for (final Control child : composite.getChildren()) {
                if (child.isVisible()) {
                    final Point prefSize = child.computeSize(widthHint, heightHint, flushCache);
                    width = Math.max(prefSize.x, width);
                    height = Math.max(prefSize.y, height);
                }
            }
            this.m_prefSize = new Point(width, height);
        }
        return this.m_prefSize;
    }
    
    protected void layout(final Composite composite, final boolean flush) {
        final Rectangle bounds = composite.getBounds();
        final int contentWidth = bounds.width;
        final int contentHeight = bounds.height;
        for (final Control child : composite.getChildren()) {
            final Object layoutData = child.getLayoutData();
            if (layoutData instanceof SWTStaticLayoutData) {
                final SWTStaticLayoutData data = (SWTStaticLayoutData)layoutData;
                final Point prefSize = child.computeSize(contentWidth, contentHeight, false);
                final int width = data.getWidth(contentWidth, prefSize.x);
                final int height = data.getHeight(contentHeight, prefSize.y);
                final int x = data.getPositionX(width, contentWidth);
                final int y = data.getPositionY(height, contentHeight);
                child.setBounds(x, y, width, height);
            }
        }
    }
}
