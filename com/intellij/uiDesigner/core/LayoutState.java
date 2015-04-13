package com.intellij.uiDesigner.core;

import java.awt.*;
import java.util.*;

public final class LayoutState
{
    private final Component[] myComponents;
    private final GridConstraints[] myConstraints;
    private final int myColumnCount;
    private final int myRowCount;
    final Dimension[] myPreferredSizes;
    final Dimension[] myMinimumSizes;
    
    public LayoutState(final GridLayoutManager layout, final boolean ignoreInvisibleComponents) {
        super();
        final ArrayList componentsList = new ArrayList(layout.getComponentCount());
        final ArrayList constraintsList = new ArrayList(layout.getComponentCount());
        for (int i = 0; i < layout.getComponentCount(); ++i) {
            final Component component = layout.getComponent(i);
            if (!ignoreInvisibleComponents || component.isVisible()) {
                componentsList.add(component);
                final GridConstraints constraints = layout.getConstraints(i);
                constraintsList.add(constraints);
            }
        }
        this.myComponents = componentsList.toArray(new Component[componentsList.size()]);
        this.myConstraints = constraintsList.toArray(new GridConstraints[constraintsList.size()]);
        this.myMinimumSizes = new Dimension[this.myComponents.length];
        this.myPreferredSizes = new Dimension[this.myComponents.length];
        this.myColumnCount = layout.getColumnCount();
        this.myRowCount = layout.getRowCount();
    }
    
    public int getComponentCount() {
        return this.myComponents.length;
    }
    
    public Component getComponent(final int index) {
        return this.myComponents[index];
    }
    
    public GridConstraints getConstraints(final int index) {
        return this.myConstraints[index];
    }
    
    public int getColumnCount() {
        return this.myColumnCount;
    }
    
    public int getRowCount() {
        return this.myRowCount;
    }
}
