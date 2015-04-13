package com.ankamagames.xulor2.component.tree;

import gnu.trove.*;
import java.util.*;

public class TreeNodeIterator implements Iterator<TreeNode>
{
    private TreeNode m_next;
    private TreeNode m_parent;
    private TIntArrayList m_childIndexes;
    private int m_depth;
    
    public TreeNodeIterator(final TreeNode node) {
        super();
        this.m_childIndexes = new TIntArrayList();
        this.m_depth = -1;
        this.m_next = node;
        this.m_parent = null;
    }
    
    @Override
    public boolean hasNext() {
        return this.m_next != null;
    }
    
    @Override
    public TreeNode next() {
        final TreeNode node = this.m_next;
        this.m_next = null;
        if (node.hasChildren() && node.isOpened()) {
            ++this.m_depth;
            this.m_parent = node;
            this.m_childIndexes.add(0);
            this.m_next = node.getChildren().get(0);
        }
        else if (this.m_parent != null) {
            while (this.m_parent != null) {
                final ArrayList<TreeNode> children = this.m_parent.getChildren();
                final int childIndex = this.m_childIndexes.get(this.m_depth) + 1;
                if (childIndex != children.size()) {
                    this.m_next = children.get(childIndex);
                    this.m_childIndexes.set(this.m_depth, childIndex);
                    break;
                }
                this.m_parent = this.m_parent.getParent();
                this.m_childIndexes.remove(this.m_depth);
                --this.m_depth;
            }
        }
        return node;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    public static void main(final String[] args) {
        final DefaultTreeNode n1 = new DefaultTreeNode("n1");
        final DefaultTreeNode n1a = new DefaultTreeNode("n1A");
        n1a.addChild(new DefaultTreeNode("n1Aa"));
        n1a.addChild(new DefaultTreeNode("n1Ab"));
        n1a.setOpened(true);
        n1.addChild(n1a);
        final DefaultTreeNode n1B = new DefaultTreeNode("n1B");
        final DefaultTreeNode n1Ba = new DefaultTreeNode("n1Ba");
        final DefaultTreeNode n1Bb = new DefaultTreeNode("n1Bb");
        n1B.addChild(n1Ba);
        n1B.addChild(n1Bb);
        n1B.setOpened(true);
        n1.addChild(n1B);
        final DefaultTreeNode n1C = new DefaultTreeNode("n1C");
        n1.addChild(n1C);
        n1.setOpened(true);
        final TreeNodeIterator it = new TreeNodeIterator(n1);
        while (it.hasNext()) {
            final TreeNode node = it.next();
            for (int i = 0; i < node.getDepth(); ++i) {
                System.out.print("\t");
            }
            System.out.println((String)node.getValue());
        }
    }
}
