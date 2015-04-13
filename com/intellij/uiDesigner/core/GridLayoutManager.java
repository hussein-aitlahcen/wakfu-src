package com.intellij.uiDesigner.core;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public final class GridLayoutManager extends AbstractLayout
{
    private int myMinCellSize;
    private final int[] myRowStretches;
    private final int[] myColumnStretches;
    private final int[] myYs;
    private final int[] myHeights;
    private final int[] myXs;
    private final int[] myWidths;
    private LayoutState myLayoutState;
    DimensionInfo myHorizontalInfo;
    DimensionInfo myVerticalInfo;
    private boolean mySameSizeHorizontally;
    private boolean mySameSizeVertically;
    public static Object DESIGN_TIME_INSETS;
    private static final int SKIP_ROW = 1;
    private static final int SKIP_COL = 2;
    
    public GridLayoutManager(final int rowCount, final int columnCount) {
        super();
        this.myMinCellSize = 20;
        if (columnCount < 1) {
            throw new IllegalArgumentException("wrong columnCount: " + columnCount);
        }
        if (rowCount < 1) {
            throw new IllegalArgumentException("wrong rowCount: " + rowCount);
        }
        this.myRowStretches = new int[rowCount];
        for (int i = 0; i < rowCount; ++i) {
            this.myRowStretches[i] = 1;
        }
        this.myColumnStretches = new int[columnCount];
        for (int j = 0; j < columnCount; ++j) {
            this.myColumnStretches[j] = 1;
        }
        this.myXs = new int[columnCount];
        this.myWidths = new int[columnCount];
        this.myYs = new int[rowCount];
        this.myHeights = new int[rowCount];
    }
    
    public GridLayoutManager(final int rowCount, final int columnCount, final Insets margin, final int hGap, final int vGap) {
        this(rowCount, columnCount);
        this.setMargin(margin);
        this.setHGap(hGap);
        this.setVGap(vGap);
        this.myMinCellSize = 0;
    }
    
    public GridLayoutManager(final int rowCount, final int columnCount, final Insets margin, final int hGap, final int vGap, final boolean sameSizeHorizontally, final boolean sameSizeVertically) {
        this(rowCount, columnCount, margin, hGap, vGap);
        this.mySameSizeHorizontally = sameSizeHorizontally;
        this.mySameSizeVertically = sameSizeVertically;
    }
    
    public void addLayoutComponent(final Component comp, final Object constraints) {
        final GridConstraints c = (GridConstraints)constraints;
        final int row = c.getRow();
        final int rowSpan = c.getRowSpan();
        final int rowCount = this.getRowCount();
        if (row < 0 || row >= rowCount) {
            throw new IllegalArgumentException("wrong row: " + row);
        }
        if (row + rowSpan - 1 >= rowCount) {
            throw new IllegalArgumentException("wrong row span: " + rowSpan + "; row=" + row + " rowCount=" + rowCount);
        }
        final int column = c.getColumn();
        final int colSpan = c.getColSpan();
        final int columnCount = this.getColumnCount();
        if (column < 0 || column >= columnCount) {
            throw new IllegalArgumentException("wrong column: " + column);
        }
        if (column + colSpan - 1 >= columnCount) {
            throw new IllegalArgumentException("wrong col span: " + colSpan + "; column=" + column + " columnCount=" + columnCount);
        }
        super.addLayoutComponent(comp, constraints);
    }
    
    public int getRowCount() {
        return this.myRowStretches.length;
    }
    
    public int getColumnCount() {
        return this.myColumnStretches.length;
    }
    
    public int getRowStretch(final int rowIndex) {
        return this.myRowStretches[rowIndex];
    }
    
    public void setRowStretch(final int rowIndex, final int stretch) {
        if (stretch < 1) {
            throw new IllegalArgumentException("wrong stretch: " + stretch);
        }
        this.myRowStretches[rowIndex] = stretch;
    }
    
    public int getColumnStretch(final int columnIndex) {
        return this.myColumnStretches[columnIndex];
    }
    
    public void setColumnStretch(final int columnIndex, final int stretch) {
        if (stretch < 1) {
            throw new IllegalArgumentException("wrong stretch: " + stretch);
        }
        this.myColumnStretches[columnIndex] = stretch;
    }
    
    public Dimension maximumLayoutSize(final Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    public Dimension minimumLayoutSize(final Container container) {
        this.validateInfos(container);
        final DimensionInfo horizontalInfo = this.myHorizontalInfo;
        final DimensionInfo verticalInfo = this.myVerticalInfo;
        final Dimension result = this.getTotalGap(container, horizontalInfo, verticalInfo);
        final int[] widths = this.getMinSizes(horizontalInfo);
        if (this.mySameSizeHorizontally) {
            makeSameSizes(widths);
        }
        final Dimension dimension = result;
        dimension.width += sum(widths);
        final int[] heights = this.getMinSizes(verticalInfo);
        if (this.mySameSizeVertically) {
            makeSameSizes(heights);
        }
        final Dimension dimension2 = result;
        dimension2.height += sum(heights);
        return result;
    }
    
    private static void makeSameSizes(final int[] widths) {
        int max = widths[0];
        for (int i = 0; i < widths.length; ++i) {
            final int width = widths[i];
            max = Math.max(width, max);
        }
        for (int j = 0; j < widths.length; ++j) {
            widths[j] = max;
        }
    }
    
    private static int[] getSameSizes(final DimensionInfo info, final int totalWidth) {
        final int[] widths = new int[info.getCellCount()];
        final int average = totalWidth / widths.length;
        int rest = totalWidth % widths.length;
        for (int i = 0; i < widths.length; ++i) {
            widths[i] = average;
            if (rest > 0) {
                final int[] array = widths;
                final int n = i;
                ++array[n];
                --rest;
            }
        }
        return widths;
    }
    
    public Dimension preferredLayoutSize(final Container container) {
        this.validateInfos(container);
        final DimensionInfo horizontalInfo = this.myHorizontalInfo;
        final DimensionInfo verticalInfo = this.myVerticalInfo;
        final Dimension result = this.getTotalGap(container, horizontalInfo, verticalInfo);
        final int[] widths = this.getPrefSizes(horizontalInfo);
        if (this.mySameSizeHorizontally) {
            makeSameSizes(widths);
        }
        final Dimension dimension = result;
        dimension.width += sum(widths);
        final int[] heights = this.getPrefSizes(verticalInfo);
        if (this.mySameSizeVertically) {
            makeSameSizes(heights);
        }
        final Dimension dimension2 = result;
        dimension2.height += sum(heights);
        return result;
    }
    
    private static int sum(final int[] ints) {
        int result = 0;
        for (int i = ints.length - 1; i >= 0; --i) {
            result += ints[i];
        }
        return result;
    }
    
    private Dimension getTotalGap(final Container container, final DimensionInfo hInfo, final DimensionInfo vInfo) {
        final Insets insets = getInsets(container);
        return new Dimension(insets.left + insets.right + countGap(hInfo, 0, hInfo.getCellCount()) + this.myMargin.left + this.myMargin.right, insets.top + insets.bottom + countGap(vInfo, 0, vInfo.getCellCount()) + this.myMargin.top + this.myMargin.bottom);
    }
    
    private static int getDesignTimeInsets(Container container) {
        while (container != null) {
            if (container instanceof JComponent) {
                final Integer designTimeInsets = (Integer)((JComponent)container).getClientProperty(GridLayoutManager.DESIGN_TIME_INSETS);
                if (designTimeInsets != null) {
                    return designTimeInsets;
                }
            }
            container = container.getParent();
        }
        return 0;
    }
    
    private static Insets getInsets(final Container container) {
        final Insets insets = container.getInsets();
        final int insetsValue = getDesignTimeInsets(container);
        if (insetsValue != 0) {
            return new Insets(insets.top + insetsValue, insets.left + insetsValue, insets.bottom + insetsValue, insets.right + insetsValue);
        }
        return insets;
    }
    
    private static int countGap(final DimensionInfo info, final int startCell, final int cellCount) {
        int counter = 0;
        for (int cellIndex = startCell + cellCount - 2; cellIndex >= startCell; --cellIndex) {
            if (shouldAddGapAfterCell(info, cellIndex)) {
                ++counter;
            }
        }
        return counter * info.getGap();
    }
    
    private static boolean shouldAddGapAfterCell(final DimensionInfo info, final int cellIndex) {
        if (cellIndex < 0 || cellIndex >= info.getCellCount()) {
            throw new IllegalArgumentException("wrong cellIndex: " + cellIndex + "; cellCount=" + info.getCellCount());
        }
        boolean endsInThis = false;
        boolean startsInNext = false;
        int indexOfNextNotEmpty = -1;
        for (int i = cellIndex + 1; i < info.getCellCount(); ++i) {
            if (!isCellEmpty(info, i)) {
                indexOfNextNotEmpty = i;
                break;
            }
        }
        for (int j = 0; j < info.getComponentCount(); ++j) {
            final Component component = info.getComponent(j);
            if (!(component instanceof Spacer)) {
                if (info.componentBelongsCell(j, cellIndex) && DimensionInfo.findAlignedChild(component, info.getConstraints(j)) != null) {
                    return true;
                }
                if (info.getCell(j) == indexOfNextNotEmpty) {
                    startsInNext = true;
                }
                if (info.getCell(j) + info.getSpan(j) - 1 == cellIndex) {
                    endsInThis = true;
                }
            }
        }
        return startsInNext && endsInThis;
    }
    
    private static boolean isCellEmpty(final DimensionInfo info, final int cellIndex) {
        if (cellIndex < 0 || cellIndex >= info.getCellCount()) {
            throw new IllegalArgumentException("wrong cellIndex: " + cellIndex + "; cellCount=" + info.getCellCount());
        }
        for (int i = 0; i < info.getComponentCount(); ++i) {
            final Component component = info.getComponent(i);
            if (info.getCell(i) == cellIndex && !(component instanceof Spacer)) {
                return false;
            }
        }
        return true;
    }
    
    public void layoutContainer(final Container container) {
        this.validateInfos(container);
        final LayoutState layoutState = this.myLayoutState;
        final DimensionInfo horizontalInfo = this.myHorizontalInfo;
        final DimensionInfo verticalInfo = this.myVerticalInfo;
        final Insets insets = getInsets(container);
        final int skipLayout = this.checkSetSizesFromParent(container, insets);
        final Dimension gap = this.getTotalGap(container, horizontalInfo, verticalInfo);
        final Dimension size2;
        final Dimension size = size2 = container.getSize();
        size2.width -= gap.width;
        final Dimension dimension = size;
        dimension.height -= gap.height;
        final Dimension preferredLayoutSize;
        final Dimension prefSize = preferredLayoutSize = this.preferredLayoutSize(container);
        preferredLayoutSize.width -= gap.width;
        final Dimension dimension2 = prefSize;
        dimension2.height -= gap.height;
        final Dimension minimumLayoutSize;
        final Dimension minSize = minimumLayoutSize = this.minimumLayoutSize(container);
        minimumLayoutSize.width -= gap.width;
        final Dimension dimension3 = minSize;
        dimension3.height -= gap.height;
        if ((skipLayout & 0x1) == 0x0) {
            int[] heights;
            if (this.mySameSizeVertically) {
                heights = getSameSizes(verticalInfo, Math.max(size.height, minSize.height));
            }
            else if (size.height < prefSize.height) {
                heights = this.getMinSizes(verticalInfo);
                this.new_doIt(heights, 0, verticalInfo.getCellCount(), size.height, verticalInfo, true);
            }
            else {
                heights = this.getPrefSizes(verticalInfo);
                this.new_doIt(heights, 0, verticalInfo.getCellCount(), size.height, verticalInfo, false);
            }
            int y = insets.top + this.myMargin.top;
            for (int i = 0; i < heights.length; ++i) {
                this.myYs[i] = y;
                this.myHeights[i] = heights[i];
                y += heights[i];
                if (shouldAddGapAfterCell(verticalInfo, i)) {
                    y += verticalInfo.getGap();
                }
            }
        }
        if ((skipLayout & 0x2) == 0x0) {
            int[] widths;
            if (this.mySameSizeHorizontally) {
                widths = getSameSizes(horizontalInfo, Math.max(size.width, minSize.width));
            }
            else if (size.width < prefSize.width) {
                widths = this.getMinSizes(horizontalInfo);
                this.new_doIt(widths, 0, horizontalInfo.getCellCount(), size.width, horizontalInfo, true);
            }
            else {
                widths = this.getPrefSizes(horizontalInfo);
                this.new_doIt(widths, 0, horizontalInfo.getCellCount(), size.width, horizontalInfo, false);
            }
            int x = insets.left + this.myMargin.left;
            for (int i = 0; i < widths.length; ++i) {
                this.myXs[i] = x;
                this.myWidths[i] = widths[i];
                x += widths[i];
                if (shouldAddGapAfterCell(horizontalInfo, i)) {
                    x += horizontalInfo.getGap();
                }
            }
        }
        for (int j = 0; j < layoutState.getComponentCount(); ++j) {
            final GridConstraints c = layoutState.getConstraints(j);
            final Component component = layoutState.getComponent(j);
            final int column = horizontalInfo.getCell(j);
            final int colSpan = horizontalInfo.getSpan(j);
            final int row = verticalInfo.getCell(j);
            final int rowSpan = verticalInfo.getSpan(j);
            final int cellWidth = this.myXs[column + colSpan - 1] + this.myWidths[column + colSpan - 1] - this.myXs[column];
            final int cellHeight = this.myYs[row + rowSpan - 1] + this.myHeights[row + rowSpan - 1] - this.myYs[row];
            final Dimension componentSize = new Dimension(cellWidth, cellHeight);
            if ((c.getFill() & 0x1) == 0x0) {
                componentSize.width = Math.min(componentSize.width, horizontalInfo.getPreferredWidth(j));
            }
            if ((c.getFill() & 0x2) == 0x0) {
                componentSize.height = Math.min(componentSize.height, verticalInfo.getPreferredWidth(j));
            }
            Util.adjustSize(component, c, componentSize);
            int dx = 0;
            int dy = 0;
            if ((c.getAnchor() & 0x4) != 0x0) {
                dx = cellWidth - componentSize.width;
            }
            else if ((c.getAnchor() & 0x8) == 0x0) {
                dx = (cellWidth - componentSize.width) / 2;
            }
            if ((c.getAnchor() & 0x2) != 0x0) {
                dy = cellHeight - componentSize.height;
            }
            else if ((c.getAnchor() & 0x1) == 0x0) {
                dy = (cellHeight - componentSize.height) / 2;
            }
            final int indent = 10 * c.getIndent();
            final Dimension dimension4 = componentSize;
            dimension4.width -= indent;
            dx += indent;
            component.setBounds(this.myXs[column] + dx, this.myYs[row] + dy, componentSize.width, componentSize.height);
        }
    }
    
    private int checkSetSizesFromParent(final Container container, final Insets insets) {
        int skipLayout = 0;
        GridLayoutManager parentGridLayout = null;
        GridConstraints parentGridConstraints = null;
        final Container parent = container.getParent();
        if (parent != null) {
            if (parent.getLayout() instanceof GridLayoutManager) {
                parentGridLayout = (GridLayoutManager)parent.getLayout();
                parentGridConstraints = parentGridLayout.getConstraintsForComponent(container);
            }
            else {
                final Container parent2 = parent.getParent();
                if (parent2 != null && parent2.getLayout() instanceof GridLayoutManager) {
                    parentGridLayout = (GridLayoutManager)parent2.getLayout();
                    parentGridConstraints = parentGridLayout.getConstraintsForComponent(parent);
                }
            }
        }
        if (parentGridLayout != null && parentGridConstraints.isUseParentLayout()) {
            if (this.myRowStretches.length == parentGridConstraints.getRowSpan()) {
                final int row = parentGridConstraints.getRow();
                this.myYs[0] = insets.top + this.myMargin.top;
                this.myHeights[0] = parentGridLayout.myHeights[row] - this.myYs[0];
                for (int i = 1; i < this.myRowStretches.length; ++i) {
                    this.myYs[i] = parentGridLayout.myYs[i + row] - parentGridLayout.myYs[row];
                    this.myHeights[i] = parentGridLayout.myHeights[i + row];
                }
                final int[] myHeights = this.myHeights;
                final int n = this.myRowStretches.length - 1;
                myHeights[n] -= insets.bottom + this.myMargin.bottom;
                skipLayout |= 0x1;
            }
            if (this.myColumnStretches.length == parentGridConstraints.getColSpan()) {
                final int col = parentGridConstraints.getColumn();
                this.myXs[0] = insets.left + this.myMargin.left;
                this.myWidths[0] = parentGridLayout.myWidths[col] - this.myXs[0];
                for (int i = 1; i < this.myColumnStretches.length; ++i) {
                    this.myXs[i] = parentGridLayout.myXs[i + col] - parentGridLayout.myXs[col];
                    this.myWidths[i] = parentGridLayout.myWidths[i + col];
                }
                final int[] myWidths = this.myWidths;
                final int n2 = this.myColumnStretches.length - 1;
                myWidths[n2] -= insets.right + this.myMargin.right;
                skipLayout |= 0x2;
            }
        }
        return skipLayout;
    }
    
    public void invalidateLayout(final Container container) {
        this.myLayoutState = null;
        this.myHorizontalInfo = null;
        this.myVerticalInfo = null;
    }
    
    void validateInfos(final Container container) {
        if (this.myLayoutState == null) {
            this.myLayoutState = new LayoutState(this, getDesignTimeInsets(container) == 0);
            this.myHorizontalInfo = new HorizontalInfo(this.myLayoutState, AbstractLayout.getHGapImpl(container));
            this.myVerticalInfo = new VerticalInfo(this.myLayoutState, AbstractLayout.getVGapImpl(container));
        }
    }
    
    public int[] getXs() {
        return this.myXs;
    }
    
    public int[] getWidths() {
        return this.myWidths;
    }
    
    public int[] getYs() {
        return this.myYs;
    }
    
    public int[] getHeights() {
        return this.myHeights;
    }
    
    public int[] getCoords(final boolean isRow) {
        return isRow ? this.myYs : this.myXs;
    }
    
    public int[] getSizes(final boolean isRow) {
        return isRow ? this.myHeights : this.myWidths;
    }
    
    private int[] getMinSizes(final DimensionInfo info) {
        return this.getMinOrPrefSizes(info, true);
    }
    
    private int[] getPrefSizes(final DimensionInfo info) {
        return this.getMinOrPrefSizes(info, false);
    }
    
    private int[] getMinOrPrefSizes(final DimensionInfo info, final boolean min) {
        final int[] widths = new int[info.getCellCount()];
        for (int i = 0; i < widths.length; ++i) {
            widths[i] = this.myMinCellSize;
        }
        for (int j = info.getComponentCount() - 1; j >= 0; --j) {
            if (info.getSpan(j) == 1) {
                int size = min ? getMin2(info, j) : Math.max(info.getMinimumWidth(j), info.getPreferredWidth(j));
                final int gap = countGap(info, info.getCell(j), info.getSpan(j));
                size = Math.max(size - gap, 0);
                widths[info.getCell(j)] = Math.max(widths[info.getCell(j)], size);
            }
        }
        updateSizesFromChildren(info, min, widths);
        final boolean[] toProcess = new boolean[info.getCellCount()];
        for (int k = info.getComponentCount() - 1; k >= 0; --k) {
            int size2 = min ? getMin2(info, k) : Math.max(info.getMinimumWidth(k), info.getPreferredWidth(k));
            final int span = info.getSpan(k);
            final int cell = info.getCell(k);
            final int gap2 = countGap(info, cell, span);
            size2 = Math.max(size2 - gap2, 0);
            Arrays.fill(toProcess, false);
            int curSize = 0;
            for (int l = 0; l < span; ++l) {
                curSize += widths[l + cell];
                toProcess[l + cell] = true;
            }
            if (curSize < size2) {
                final boolean[] higherPriorityCells = new boolean[toProcess.length];
                this.getCellsWithHigherPriorities(info, toProcess, higherPriorityCells, false, widths);
                distribute(higherPriorityCells, info, size2 - curSize, widths);
            }
        }
        return widths;
    }
    
    private static void updateSizesFromChildren(final DimensionInfo info, final boolean min, final int[] widths) {
        for (int i = info.getComponentCount() - 1; i >= 0; --i) {
            final Component child = info.getComponent(i);
            final GridConstraints c = info.getConstraints(i);
            if (c.isUseParentLayout() && child instanceof Container) {
                final Container container = (Container)child;
                if (container.getLayout() instanceof GridLayoutManager) {
                    updateSizesFromChild(info, min, widths, container, i);
                }
                else if (container.getComponentCount() == 1 && container.getComponent(0) instanceof Container) {
                    final Container childContainer = (Container)container.getComponent(0);
                    if (childContainer.getLayout() instanceof GridLayoutManager) {
                        updateSizesFromChild(info, min, widths, childContainer, i);
                    }
                }
            }
        }
    }
    
    private static void updateSizesFromChild(final DimensionInfo info, final boolean min, final int[] widths, final Container container, final int childIndex) {
        final GridLayoutManager childLayout = (GridLayoutManager)container.getLayout();
        if (info.getSpan(childIndex) == info.getChildLayoutCellCount(childLayout)) {
            childLayout.validateInfos(container);
            final DimensionInfo childInfo = (info instanceof HorizontalInfo) ? childLayout.myHorizontalInfo : childLayout.myVerticalInfo;
            final int[] sizes = childLayout.getMinOrPrefSizes(childInfo, min);
            final int cell = info.getCell(childIndex);
            for (int j = 0; j < sizes.length; ++j) {
                widths[cell + j] = Math.max(widths[cell + j], sizes[j]);
            }
        }
    }
    
    private static int getMin2(final DimensionInfo info, final int componentIndex) {
        int s;
        if ((info.getSizePolicy(componentIndex) & 0x1) != 0x0) {
            s = info.getMinimumWidth(componentIndex);
        }
        else {
            s = Math.max(info.getMinimumWidth(componentIndex), info.getPreferredWidth(componentIndex));
        }
        return s;
    }
    
    private void new_doIt(final int[] widths, final int cell, final int span, final int minWidth, final DimensionInfo info, final boolean checkPrefs) {
        int toDistribute = minWidth;
        for (int i = cell; i < cell + span; ++i) {
            toDistribute -= widths[i];
        }
        if (toDistribute <= 0) {
            return;
        }
        final boolean[] allowedCells = new boolean[info.getCellCount()];
        for (int j = cell; j < cell + span; ++j) {
            allowedCells[j] = true;
        }
        final boolean[] higherPriorityCells = new boolean[info.getCellCount()];
        this.getCellsWithHigherPriorities(info, allowedCells, higherPriorityCells, checkPrefs, widths);
        distribute(higherPriorityCells, info, toDistribute, widths);
    }
    
    private static void distribute(final boolean[] higherPriorityCells, final DimensionInfo info, int toDistribute, final int[] widths) {
        int stretches = 0;
        for (int i = 0; i < info.getCellCount(); ++i) {
            if (higherPriorityCells[i]) {
                stretches += info.getStretch(i);
            }
        }
        final int toDistributeFrozen = toDistribute;
        for (int j = 0; j < info.getCellCount(); ++j) {
            if (higherPriorityCells[j]) {
                final int addon = toDistributeFrozen * info.getStretch(j) / stretches;
                final int n = j;
                widths[n] += addon;
                toDistribute -= addon;
            }
        }
        if (toDistribute != 0) {
            for (int k = 0; k < info.getCellCount(); ++k) {
                if (higherPriorityCells[k]) {
                    final int n2 = k;
                    ++widths[n2];
                    if (--toDistribute == 0) {
                        break;
                    }
                }
            }
        }
        if (toDistribute != 0) {
            throw new IllegalStateException("toDistribute = " + toDistribute);
        }
    }
    
    private void getCellsWithHigherPriorities(final DimensionInfo info, final boolean[] allowedCells, final boolean[] higherPriorityCells, final boolean checkPrefs, final int[] widths) {
        Arrays.fill(higherPriorityCells, false);
        int foundCells = 0;
        if (checkPrefs) {
            final int[] prefs = this.getMinOrPrefSizes(info, false);
            for (int cell = 0; cell < allowedCells.length; ++cell) {
                if (allowedCells[cell]) {
                    if (!isCellEmpty(info, cell) && prefs[cell] > widths[cell]) {
                        higherPriorityCells[cell] = true;
                        ++foundCells;
                    }
                }
            }
            if (foundCells > 0) {
                return;
            }
        }
        for (int cell2 = 0; cell2 < allowedCells.length; ++cell2) {
            if (allowedCells[cell2]) {
                if ((info.getCellSizePolicy(cell2) & 0x4) != 0x0) {
                    higherPriorityCells[cell2] = true;
                    ++foundCells;
                }
            }
        }
        if (foundCells > 0) {
            return;
        }
        for (int cell = 0; cell < allowedCells.length; ++cell) {
            if (allowedCells[cell]) {
                if ((info.getCellSizePolicy(cell) & 0x2) != 0x0) {
                    higherPriorityCells[cell] = true;
                    ++foundCells;
                }
            }
        }
        if (foundCells > 0) {
            return;
        }
        for (int cell3 = 0; cell3 < allowedCells.length; ++cell3) {
            if (allowedCells[cell3]) {
                if (!isCellEmpty(info, cell3)) {
                    higherPriorityCells[cell3] = true;
                    ++foundCells;
                }
            }
        }
        if (foundCells > 0) {
            return;
        }
        for (int cell4 = 0; cell4 < allowedCells.length; ++cell4) {
            if (allowedCells[cell4]) {
                higherPriorityCells[cell4] = true;
            }
        }
    }
    
    public boolean isSameSizeHorizontally() {
        return this.mySameSizeHorizontally;
    }
    
    public boolean isSameSizeVertically() {
        return this.mySameSizeVertically;
    }
    
    public void setSameSizeHorizontally(final boolean sameSizeHorizontally) {
        this.mySameSizeHorizontally = sameSizeHorizontally;
    }
    
    public void setSameSizeVertically(final boolean sameSizeVertically) {
        this.mySameSizeVertically = sameSizeVertically;
    }
    
    public int[] getHorizontalGridLines() {
        final int[] result = new int[this.myYs.length + 1];
        result[0] = this.myYs[0];
        for (int i = 0; i < this.myYs.length - 1; ++i) {
            result[i + 1] = (this.myYs[i] + this.myHeights[i] + this.myYs[i + 1]) / 2;
        }
        result[this.myYs.length] = this.myYs[this.myYs.length - 1] + this.myHeights[this.myYs.length - 1];
        return result;
    }
    
    public int[] getVerticalGridLines() {
        final int[] result = new int[this.myXs.length + 1];
        result[0] = this.myXs[0];
        for (int i = 0; i < this.myXs.length - 1; ++i) {
            result[i + 1] = (this.myXs[i] + this.myWidths[i] + this.myXs[i + 1]) / 2;
        }
        result[this.myXs.length] = this.myXs[this.myXs.length - 1] + this.myWidths[this.myXs.length - 1];
        return result;
    }
    
    public int getCellCount(final boolean isRow) {
        return isRow ? this.getRowCount() : this.getColumnCount();
    }
    
    public int getCellSizePolicy(final boolean isRow, final int cellIndex) {
        final DimensionInfo info = isRow ? this.myVerticalInfo : this.myHorizontalInfo;
        if (info == null) {
            return 0;
        }
        return info.getCellSizePolicy(cellIndex);
    }
    
    static {
        GridLayoutManager.DESIGN_TIME_INSETS = new Object();
    }
}
