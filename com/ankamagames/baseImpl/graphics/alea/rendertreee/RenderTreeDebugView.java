package com.ankamagames.baseImpl.graphics.alea.rendertreee;

import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.tree.*;
import java.util.*;

public class RenderTreeDebugView
{
    private JPanel m_rootPanel;
    private JTree m_renderTree;
    private DefaultMutableTreeNode m_rootNode;
    
    public RenderTreeDebugView() {
        super();
        this.initializeView();
    }
    
    public void updateData(final RenderTreeStencil renderTree) {
        this.m_rootNode.removeAllChildren();
        DefaultMutableTreeNode currentNode = this.m_rootNode;
        DefaultMutableTreeNode newNode;
        for (RenderTreeStencil currentRenderTree = renderTree; currentRenderTree != null; currentRenderTree = currentRenderTree.getChild(), currentNode = newNode) {
            final Entity entity = currentRenderTree.getEntity();
            final ArrayList<Entity> after = currentRenderTree.getEntitiesAfter();
            final ArrayList<RenderTreeStencil> mask = currentRenderTree.getMask();
            final ArrayList<RenderTreeStencil> over = currentRenderTree.getOver();
            final String entityClass = entity.getClass().getSimpleName();
            final StringBuffer sb = new StringBuffer();
            sb.append(entityClass).append(" [ ");
            sb.append("radius=").append(entity.m_renderRadius);
            sb.append(", afterCount=").append(after.size());
            sb.append(", maskCount=").append(mask.size());
            sb.append(", overCount=").append(over.size());
            sb.append(" ]");
            newNode = new DefaultMutableTreeNode(sb.toString());
            currentNode.add(newNode);
        }
        this.expandAll(this.m_renderTree, true);
        this.m_renderTree.repaint();
    }
    
    private void initializeView() {
        (this.m_rootPanel = new JPanel()).setLayout(new BorderLayout(0, 0));
        final JScrollPane scrollPane1 = new JScrollPane();
        this.m_rootPanel.add(scrollPane1, "Center");
        this.m_rootNode = new DefaultMutableTreeNode("Root");
        scrollPane1.setViewportView(this.m_renderTree = new JTree(this.m_rootNode));
    }
    
    public void expandAll(final JTree tree, final boolean expand) {
        final TreeNode root = (TreeNode)tree.getModel().getRoot();
        this.expandAll(tree, new TreePath(root), expand);
    }
    
    private void expandAll(final JTree tree, final TreePath parent, final boolean expand) {
        final TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            final Enumeration e = node.children();
            while (e.hasMoreElements()) {
                final TreeNode n = e.nextElement();
                final TreePath path = parent.pathByAddingChild(n);
                this.expandAll(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        }
        else {
            tree.collapsePath(parent);
        }
    }
    
    public JPanel getRootPanel() {
        return this.m_rootPanel;
    }
}
