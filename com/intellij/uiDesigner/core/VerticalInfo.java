package com.intellij.uiDesigner.core;

final class VerticalInfo extends DimensionInfo
{
    public VerticalInfo(final LayoutState layoutState, final int gap) {
        super(layoutState, gap);
    }
    
    protected int getOriginalCell(final GridConstraints constraints) {
        return constraints.getRow();
    }
    
    protected int getOriginalSpan(final GridConstraints constraints) {
        return constraints.getRowSpan();
    }
    
    int getSizePolicy(final int componentIndex) {
        return this.myLayoutState.getConstraints(componentIndex).getVSizePolicy();
    }
    
    int getChildLayoutCellCount(final GridLayoutManager childLayout) {
        return childLayout.getRowCount();
    }
    
    public int getMinimumWidth(final int componentIndex) {
        return this.getMinimumSize(componentIndex).height;
    }
    
    public DimensionInfo getDimensionInfo(final GridLayoutManager grid) {
        return grid.myVerticalInfo;
    }
    
    public int getCellCount() {
        return this.myLayoutState.getRowCount();
    }
    
    public int getPreferredWidth(final int componentIndex) {
        return this.getPreferredSize(componentIndex).height;
    }
}
