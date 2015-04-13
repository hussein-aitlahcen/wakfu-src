package com.intellij.uiDesigner.core;

import java.util.*;
import java.awt.*;

public abstract class DimensionInfo
{
    private final int[] myCell;
    private final int[] mySpan;
    protected final LayoutState myLayoutState;
    private final int[] myStretches;
    private final int[] mySpansAfterElimination;
    private final int[] myCellSizePolicies;
    private final int myGap;
    
    public DimensionInfo(final LayoutState layoutState, final int gap) {
        super();
        if (layoutState == null) {
            throw new IllegalArgumentException("layoutState cannot be null");
        }
        if (gap < 0) {
            throw new IllegalArgumentException("invalid gap: " + gap);
        }
        this.myLayoutState = layoutState;
        this.myGap = gap;
        this.myCell = new int[layoutState.getComponentCount()];
        this.mySpan = new int[layoutState.getComponentCount()];
        for (int i = 0; i < layoutState.getComponentCount(); ++i) {
            final GridConstraints c = layoutState.getConstraints(i);
            this.myCell[i] = this.getOriginalCell(c);
            this.mySpan[i] = this.getOriginalSpan(c);
        }
        this.myStretches = new int[this.getCellCount()];
        for (int j = 0; j < this.myStretches.length; ++j) {
            this.myStretches[j] = 1;
        }
        final ArrayList elimitated = new ArrayList();
        this.mySpansAfterElimination = this.mySpan.clone();
        Util.eliminate(this.myCell.clone(), this.mySpansAfterElimination, elimitated);
        this.myCellSizePolicies = new int[this.getCellCount()];
        for (int k = 0; k < this.myCellSizePolicies.length; ++k) {
            this.myCellSizePolicies[k] = this.getCellSizePolicyImpl(k, elimitated);
        }
    }
    
    public final int getComponentCount() {
        return this.myLayoutState.getComponentCount();
    }
    
    public final Component getComponent(final int componentIndex) {
        return this.myLayoutState.getComponent(componentIndex);
    }
    
    public final GridConstraints getConstraints(final int componentIndex) {
        return this.myLayoutState.getConstraints(componentIndex);
    }
    
    public abstract int getCellCount();
    
    public abstract int getPreferredWidth(final int p0);
    
    public abstract int getMinimumWidth(final int p0);
    
    public abstract DimensionInfo getDimensionInfo(final GridLayoutManager p0);
    
    public final int getCell(final int componentIndex) {
        return this.myCell[componentIndex];
    }
    
    public final int getSpan(final int componentIndex) {
        return this.mySpan[componentIndex];
    }
    
    public final int getStretch(final int cellIndex) {
        return this.myStretches[cellIndex];
    }
    
    protected abstract int getOriginalCell(final GridConstraints p0);
    
    protected abstract int getOriginalSpan(final GridConstraints p0);
    
    abstract int getSizePolicy(final int p0);
    
    abstract int getChildLayoutCellCount(final GridLayoutManager p0);
    
    public final int getGap() {
        return this.myGap;
    }
    
    public boolean componentBelongsCell(final int componentIndex, final int cellIndex) {
        final int componentStartCell = this.getCell(componentIndex);
        final int span = this.getSpan(componentIndex);
        return componentStartCell <= cellIndex && cellIndex < componentStartCell + span;
    }
    
    public final int getCellSizePolicy(final int cellIndex) {
        return this.myCellSizePolicies[cellIndex];
    }
    
    private int getCellSizePolicyImpl(final int cellIndex, final ArrayList eliminatedCells) {
        final int policyFromChild = this.getCellSizePolicyFromInheriting(cellIndex);
        if (policyFromChild != -1) {
            return policyFromChild;
        }
        for (int i = eliminatedCells.size() - 1; i >= 0; --i) {
            if (cellIndex == eliminatedCells.get(i)) {
                return 1;
            }
        }
        return this.calcCellSizePolicy(cellIndex);
    }
    
    private int calcCellSizePolicy(final int cellIndex) {
        boolean canShrink = true;
        boolean canGrow = false;
        boolean wantGrow = false;
        boolean weakCanGrow = true;
        boolean weakWantGrow = true;
        int countOfBelongingComponents = 0;
        for (int i = 0; i < this.getComponentCount(); ++i) {
            if (this.componentBelongsCell(i, cellIndex)) {
                ++countOfBelongingComponents;
                final int p = this.getSizePolicy(i);
                final boolean thisCanShrink = (p & 0x1) != 0x0;
                final boolean thisCanGrow = (p & 0x2) != 0x0;
                final boolean thisWantGrow = (p & 0x4) != 0x0;
                if (this.getCell(i) == cellIndex && this.mySpansAfterElimination[i] == 1) {
                    canShrink &= thisCanShrink;
                    canGrow |= thisCanGrow;
                    wantGrow |= thisWantGrow;
                }
                if (!thisCanGrow) {
                    weakCanGrow = false;
                }
                if (!thisWantGrow) {
                    weakWantGrow = false;
                }
            }
        }
        return (canShrink ? 1 : 0) | ((canGrow || (countOfBelongingComponents > 0 && weakCanGrow)) ? 2 : 0) | ((wantGrow || (countOfBelongingComponents > 0 && weakWantGrow)) ? 4 : 0);
    }
    
    private int getCellSizePolicyFromInheriting(final int cellIndex) {
        int nonInheritingComponentsInCell = 0;
        int policyFromInheriting = -1;
        for (int i = this.getComponentCount() - 1; i >= 0; --i) {
            if (this.componentBelongsCell(i, cellIndex)) {
                final Component child = this.getComponent(i);
                final GridConstraints c = this.getConstraints(i);
                final Container container = findAlignedChild(child, c);
                if (container != null) {
                    final GridLayoutManager grid = (GridLayoutManager)container.getLayout();
                    grid.validateInfos(container);
                    final DimensionInfo info = this.getDimensionInfo(grid);
                    final int policy = info.calcCellSizePolicy(cellIndex - this.getOriginalCell(c));
                    if (policyFromInheriting == -1) {
                        policyFromInheriting = policy;
                    }
                    else {
                        policyFromInheriting |= policy;
                    }
                }
                else if (this.getOriginalCell(c) == cellIndex && this.getOriginalSpan(c) == 1 && !(child instanceof Spacer)) {
                    ++nonInheritingComponentsInCell;
                }
            }
        }
        if (nonInheritingComponentsInCell > 0) {
            return -1;
        }
        return policyFromInheriting;
    }
    
    public static Container findAlignedChild(final Component child, final GridConstraints c) {
        if (c.isUseParentLayout() && child instanceof Container) {
            final Container container = (Container)child;
            if (container.getLayout() instanceof GridLayoutManager) {
                return container;
            }
            if (container.getComponentCount() == 1 && container.getComponent(0) instanceof Container) {
                final Container childContainer = (Container)container.getComponent(0);
                if (childContainer.getLayout() instanceof GridLayoutManager) {
                    return childContainer;
                }
            }
        }
        return null;
    }
    
    protected final Dimension getPreferredSize(final int componentIndex) {
        Dimension size = this.myLayoutState.myPreferredSizes[componentIndex];
        if (size == null) {
            size = Util.getPreferredSize(this.myLayoutState.getComponent(componentIndex), this.myLayoutState.getConstraints(componentIndex), true);
            this.myLayoutState.myPreferredSizes[componentIndex] = size;
        }
        return size;
    }
    
    protected final Dimension getMinimumSize(final int componentIndex) {
        Dimension size = this.myLayoutState.myMinimumSizes[componentIndex];
        if (size == null) {
            size = Util.getMinimumSize(this.myLayoutState.getComponent(componentIndex), this.myLayoutState.getConstraints(componentIndex), true);
            this.myLayoutState.myMinimumSizes[componentIndex] = size;
        }
        return size;
    }
}
