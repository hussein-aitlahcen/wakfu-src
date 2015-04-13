package com.ankamagames.xulor2.component.tree;

import java.util.*;

public class DefaultTreeNode implements TreeNode
{
    private TreeNode m_parent;
    private ArrayList<TreeNode> m_children;
    protected boolean m_opened;
    private boolean m_selected;
    private int m_depth;
    private Object m_value;
    
    public DefaultTreeNode(final Object value) {
        super();
        this.m_parent = null;
        this.m_opened = false;
        this.m_selected = false;
        this.m_depth = 0;
        this.m_value = value;
    }
    
    public void addChild(final DefaultTreeNode node) {
        if (this.m_children == null) {
            this.m_children = new ArrayList<TreeNode>();
        }
        if (!this.m_children.contains(node)) {
            this.m_children.add(node);
            node.m_parent = this;
            node.setDepth(this.m_depth + 1);
        }
    }
    
    @Override
    public boolean hasChildren() {
        return this.m_children != null && !this.m_children.isEmpty();
    }
    
    @Override
    public ArrayList<TreeNode> getChildren() {
        return this.m_children;
    }
    
    @Override
    public TreeNode getParent() {
        return this.m_parent;
    }
    
    @Override
    public void setValue(final Object value) {
        this.m_value = value;
    }
    
    @Override
    public Object getValue() {
        return this.m_value;
    }
    
    @Override
    public void setOpened(final boolean opened) {
        this.m_opened = opened;
    }
    
    @Override
    public boolean isOpened() {
        return this.m_opened;
    }
    
    @Override
    public void setSelected(final boolean selected) {
        this.m_selected = selected;
    }
    
    @Override
    public boolean isSelected() {
        return this.m_selected;
    }
    
    @Override
    public int getDepth() {
        return this.m_depth;
    }
    
    @Override
    public void setDepth(final int depth) {
        if (this.m_depth == depth) {
            return;
        }
        this.m_depth = depth;
        if (this.m_children != null) {
            for (int i = this.m_children.size() - 1; i >= 0; --i) {
                this.m_children.get(i).setDepth(this.m_depth + 1);
            }
        }
    }
}
