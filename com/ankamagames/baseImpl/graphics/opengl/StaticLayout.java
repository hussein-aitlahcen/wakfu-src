package com.ankamagames.baseImpl.graphics.opengl;

import java.util.*;
import java.awt.*;

public class StaticLayout implements LayoutManager2
{
    private final HashMap<Component, StaticLayoutData> m_constraints;
    
    public StaticLayout() {
        super();
        this.m_constraints = new HashMap<Component, StaticLayoutData>();
    }
    
    @Override
    public void addLayoutComponent(final Component comp, final Object constraints) {
        this.m_constraints.put(comp, (StaticLayoutData)constraints);
    }
    
    @Override
    public Dimension maximumLayoutSize(final Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    @Override
    public float getLayoutAlignmentX(final Container target) {
        return 0.5f;
    }
    
    @Override
    public float getLayoutAlignmentY(final Container target) {
        return 0.5f;
    }
    
    @Override
    public void invalidateLayout(final Container target) {
    }
    
    @Override
    public void addLayoutComponent(final String name, final Component comp) {
    }
    
    @Override
    public void removeLayoutComponent(final Component comp) {
        this.m_constraints.remove(comp);
    }
    
    @Override
    public Dimension preferredLayoutSize(final Container parent) {
        synchronized (parent.getTreeLock()) {
            int width = Integer.MIN_VALUE;
            int height = Integer.MIN_VALUE;
            for (int i = 0, size = parent.getComponentCount(); i < size; ++i) {
                final Component child = parent.getComponent(i);
                if (child.isVisible()) {
                    final Dimension prefSize = child.getPreferredSize();
                    width = Math.max((int)prefSize.getWidth(), width);
                    height = Math.max((int)prefSize.getHeight(), height);
                }
            }
            final Insets insets = parent.getInsets();
            width += insets.left + insets.right;
            height += insets.top + insets.bottom;
            return new Dimension(width, height);
        }
    }
    
    @Override
    public Dimension minimumLayoutSize(final Container parent) {
        synchronized (parent.getTreeLock()) {
            int width = Integer.MIN_VALUE;
            int height = Integer.MIN_VALUE;
            for (int i = 0, size = parent.getComponentCount(); i < size; ++i) {
                final Component child = parent.getComponent(i);
                if (child.isVisible()) {
                    final Dimension prefSize = child.getMinimumSize();
                    width = Math.max((int)prefSize.getWidth(), width);
                    height = Math.max((int)prefSize.getHeight(), height);
                }
            }
            final Insets insets = parent.getInsets();
            width += insets.left + insets.right;
            height += insets.top + insets.bottom;
            return new Dimension(width, height);
        }
    }
    
    @Override
    public void layoutContainer(final Container parent) {
        final Insets insets = parent.getInsets();
        final int contentWidth = parent.getWidth() - insets.left - insets.right;
        final int contentHeight = parent.getHeight() - insets.top - insets.bottom;
        for (int i = 0, size = parent.getComponentCount(); i < size; ++i) {
            final Component child = parent.getComponent(i);
            if (child.isVisible()) {
                final StaticLayoutData data = this.m_constraints.get(child);
                if (data == null) {
                    child.setBounds(insets.left, insets.top, contentWidth, contentHeight);
                }
                else {
                    final Dimension prefSize = child.getPreferredSize();
                    int width = data.getWidth(contentWidth, prefSize.width);
                    int height = data.getHeight(contentHeight, prefSize.height);
                    if (data.isEnsurePairDimension()) {
                        width += width % 2;
                        height += height % 2;
                    }
                    final int x = data.getPositionX(width, contentWidth) + insets.top;
                    final int y = data.getPositionY(height, contentHeight) + insets.left;
                    child.setBounds(x, y, width, height);
                }
            }
        }
    }
}
