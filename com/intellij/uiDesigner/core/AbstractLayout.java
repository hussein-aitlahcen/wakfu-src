package com.intellij.uiDesigner.core;

import java.awt.*;

public abstract class AbstractLayout implements LayoutManager2
{
    public static final int DEFAULT_HGAP = 10;
    public static final int DEFAULT_VGAP = 5;
    protected Component[] myComponents;
    protected GridConstraints[] myConstraints;
    protected Insets myMargin;
    private int myHGap;
    private int myVGap;
    
    public AbstractLayout() {
        super();
        this.myComponents = new Component[0];
        this.myConstraints = new GridConstraints[0];
        this.myMargin = new Insets(0, 0, 0, 0);
        this.myHGap = -1;
        this.myVGap = -1;
    }
    
    public final Insets getMargin() {
        return (Insets)this.myMargin.clone();
    }
    
    public final int getHGap() {
        return this.myHGap;
    }
    
    protected static int getHGapImpl(Container container) {
        if (container == null) {
            throw new IllegalArgumentException("container cannot be null");
        }
        while (container != null) {
            if (container.getLayout() instanceof AbstractLayout) {
                final AbstractLayout layout = (AbstractLayout)container.getLayout();
                if (layout.getHGap() != -1) {
                    return layout.getHGap();
                }
            }
            container = container.getParent();
        }
        return 10;
    }
    
    public final void setHGap(final int hGap) {
        if (hGap < -1) {
            throw new IllegalArgumentException("wrong hGap: " + hGap);
        }
        this.myHGap = hGap;
    }
    
    public final int getVGap() {
        return this.myVGap;
    }
    
    protected static int getVGapImpl(Container container) {
        if (container == null) {
            throw new IllegalArgumentException("container cannot be null");
        }
        while (container != null) {
            if (container.getLayout() instanceof AbstractLayout) {
                final AbstractLayout layout = (AbstractLayout)container.getLayout();
                if (layout.getVGap() != -1) {
                    return layout.getVGap();
                }
            }
            container = container.getParent();
        }
        return 5;
    }
    
    public final void setVGap(final int vGap) {
        if (vGap < -1) {
            throw new IllegalArgumentException("wrong vGap: " + vGap);
        }
        this.myVGap = vGap;
    }
    
    public final void setMargin(final Insets margin) {
        if (margin == null) {
            throw new IllegalArgumentException("margin cannot be null");
        }
        this.myMargin = (Insets)margin.clone();
    }
    
    final int getComponentCount() {
        return this.myComponents.length;
    }
    
    final Component getComponent(final int index) {
        return this.myComponents[index];
    }
    
    final GridConstraints getConstraints(final int index) {
        return this.myConstraints[index];
    }
    
    public void addLayoutComponent(final Component comp, final Object constraints) {
        if (!(constraints instanceof GridConstraints)) {
            throw new IllegalArgumentException("constraints: " + constraints);
        }
        final Component[] newComponents = new Component[this.myComponents.length + 1];
        System.arraycopy(this.myComponents, 0, newComponents, 0, this.myComponents.length);
        newComponents[this.myComponents.length] = comp;
        this.myComponents = newComponents;
        final GridConstraints[] newConstraints = new GridConstraints[this.myConstraints.length + 1];
        System.arraycopy(this.myConstraints, 0, newConstraints, 0, this.myConstraints.length);
        newConstraints[this.myConstraints.length] = (GridConstraints)constraints;
        this.myConstraints = newConstraints;
    }
    
    public final void addLayoutComponent(final String name, final Component comp) {
        throw new UnsupportedOperationException();
    }
    
    public final void removeLayoutComponent(final Component comp) {
        final int i = this.getComponentIndex(comp);
        if (i == -1) {
            throw new IllegalArgumentException("component was not added: " + comp);
        }
        final Component[] newComponents = new Component[this.myComponents.length - 1];
        System.arraycopy(this.myComponents, 0, newComponents, 0, i);
        System.arraycopy(this.myComponents, i + 1, newComponents, i, this.myComponents.length - i - 1);
        this.myComponents = newComponents;
        final GridConstraints[] newConstraints = new GridConstraints[this.myConstraints.length - 1];
        System.arraycopy(this.myConstraints, 0, newConstraints, 0, i);
        System.arraycopy(this.myConstraints, i + 1, newConstraints, i, this.myConstraints.length - i - 1);
        this.myConstraints = newConstraints;
    }
    
    public GridConstraints getConstraintsForComponent(final Component comp) {
        final int i = this.getComponentIndex(comp);
        if (i == -1) {
            throw new IllegalArgumentException("component was not added: " + comp);
        }
        return this.myConstraints[i];
    }
    
    private int getComponentIndex(final Component comp) {
        for (int i = 0; i < this.myComponents.length; ++i) {
            final Component component = this.myComponents[i];
            if (component == comp) {
                return i;
            }
        }
        return -1;
    }
    
    public final float getLayoutAlignmentX(final Container container) {
        return 0.5f;
    }
    
    public final float getLayoutAlignmentY(final Container container) {
        return 0.5f;
    }
    
    public abstract Dimension maximumLayoutSize(final Container p0);
    
    public abstract void invalidateLayout(final Container p0);
    
    public abstract Dimension preferredLayoutSize(final Container p0);
    
    public abstract Dimension minimumLayoutSize(final Container p0);
    
    public abstract void layoutContainer(final Container p0);
}
