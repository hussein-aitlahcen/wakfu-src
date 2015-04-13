package com.ankamagames.xulor2.component.tree;

import java.util.*;

public interface TreeNode
{
    boolean hasChildren();
    
    ArrayList<TreeNode> getChildren();
    
    TreeNode getParent();
    
    Object getValue();
    
    void setValue(Object p0);
    
    void setOpened(boolean p0);
    
    boolean isOpened();
    
    void setSelected(boolean p0);
    
    boolean isSelected();
    
    int getDepth();
    
    void setDepth(int p0);
}
