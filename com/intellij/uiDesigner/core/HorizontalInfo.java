package com.intellij.uiDesigner.core;

final class HorizontalInfo extends DimensionInfo
{
    public HorizontalInfo(final LayoutState layoutState, final int gap) {
        super(layoutState, gap);
    }
    
    protected int getOriginalCell(final GridConstraints constraints) {
        return constraints.getColumn();
    }
    
    protected int getOriginalSpan(final GridConstraints constraints) {
        return constraints.getColSpan();
    }
    
    int getSizePolicy(final int componentIndex) {
        return this.myLayoutState.getConstraints(componentIndex).getHSizePolicy();
    }
    
    int getChildLayoutCellCount(final GridLayoutManager childLayout) {
        return childLayout.getColumnCount();
    }
    
    public int getMinimumWidth(final int componentIndex) {
        return this.getMinimumSize(componentIndex).width;
    }
    
    public DimensionInfo getDimensionInfo(final GridLayoutManager grid) {
        return grid.myHorizontalInfo;
    }
    
    public int getCellCount() {
        return this.myLayoutState.getColumnCount();
    }
    
    public int getPreferredWidth(final int componentIndex) {
        return this.getPreferredSize(componentIndex).width;
    }
}
