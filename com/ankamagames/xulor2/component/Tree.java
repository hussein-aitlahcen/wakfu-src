package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.appearance.*;
import java.util.*;
import com.ankamagames.xulor2.decorator.mesh.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.component.tree.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.awt.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.event.*;

public class Tree extends Container implements ContentClient, ColorClient
{
    public static final String TAG = "tree";
    public static final String BUTTON_TAG = "button";
    public static final String CELL_TAG = "cell";
    public static final String OPENED_CELL_TAG = "openedCell";
    public static final String LEAF_CELL_TAG = "leafCell";
    public static final String SELECTED_CELL_TAG = "selectedCell";
    public static final String ODD_CELL_TAG = "oddCell";
    public static final String EVEN_CELL_TAG = "evenCell";
    public static final String SCROLLBAR_TAG = "scrollBar";
    private ItemRendererManager m_rendererManager;
    private TreeNode m_content;
    private ArrayList<TreeNode> m_computedContent;
    private ArrayList<RenderableContainer> m_renderables;
    private ScrollBar m_scrollBar;
    private boolean m_displayScrollbar;
    private boolean m_beingLayouted;
    private int m_offset;
    private String m_contentProperty;
    private ElementMap m_contentPropertyElementMap;
    private boolean m_valuesDirty;
    private boolean m_treeDirty;
    private boolean m_contentDirty;
    private ArrayList<ListOverMesh> m_backgroundMeshes;
    private boolean m_selectOnlyOne;
    private boolean m_openOnlyOne;
    private TreeNode m_selectedNode;
    private ArrayList<TreeNode> m_openedNodes;
    private boolean m_displayRoot;
    private boolean m_noClosingOnClick;
    private boolean m_noUnselectingOnClick;
    private int m_cellHeight;
    private int m_minRows;
    private int m_maxRows;
    private boolean m_enableDND;
    private Color m_evenBGColor;
    private Color m_oddBGColor;
    public static final int CONTENT_HASH;
    public static final int CELL_HEIGHT_HASH;
    public static final int ENABLE_DND_HASH;
    public static final int MIN_ROWS_HASH;
    public static final int MAX_ROWS_HASH;
    public static final int OPEN_ONLY_ONE_HASH;
    public static final int SELECT_ONLY_ONE_HASH;
    public static final int DISPLAY_ROOT_HASH;
    public static final int NO_CLOSING_ON_CLICK_HASH;
    public static final int NO_UNSELECTING_ON_CLICK_HASH;
    
    public Tree() {
        super();
        this.m_displayScrollbar = false;
        this.m_contentProperty = null;
        this.m_contentPropertyElementMap = null;
        this.m_selectOnlyOne = true;
        this.m_openOnlyOne = true;
        this.m_selectedNode = null;
        this.m_displayRoot = false;
        this.m_noClosingOnClick = false;
        this.m_noUnselectingOnClick = false;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof ItemRenderer) {
            this.m_rendererManager.addRenderer((ItemRenderer)e);
        }
        super.add(e);
    }
    
    @Override
    protected void addInnerMeshes() {
        for (int i = this.m_backgroundMeshes.size() - 1; i >= 0; --i) {
            this.m_entity.addChild(this.m_backgroundMeshes.get(i).getEntity());
        }
        super.addInnerMeshes();
    }
    
    @Override
    public String getTag() {
        return "tree";
    }
    
    @Override
    public Widget getWidgetByThemeElementName(final String themeElementName, final boolean compilationMode) {
        if (themeElementName.equals("scrollBar")) {
            return this.m_scrollBar;
        }
        return super.getWidgetByThemeElementName(themeElementName, compilationMode);
    }
    
    @Override
    public void setColor(final Color c, final String name) {
        if (name == null || name.equals("evenCell")) {
            this.m_evenBGColor = c;
        }
        else if (name.equals("oddCell")) {
            this.m_oddBGColor = c;
        }
    }
    
    @Override
    public void setContentProperty(final String propertyName, final ElementMap map) {
        this.m_contentProperty = propertyName;
        this.m_contentPropertyElementMap = map;
    }
    
    public int getCellHeight() {
        return this.m_cellHeight;
    }
    
    public void setCellHeight(final int cellHeight) {
        this.m_cellHeight = cellHeight;
    }
    
    public int getMinRows() {
        return this.m_minRows;
    }
    
    public void setMinRows(final int minRows) {
        this.m_minRows = minRows;
    }
    
    public int getMaxRows() {
        return this.m_maxRows;
    }
    
    public void setMaxRows(final int maxRows) {
        this.m_maxRows = maxRows;
    }
    
    public boolean isEnableDND() {
        return this.m_enableDND;
    }
    
    public void setEnableDND(final boolean enableDND) {
        this.m_enableDND = enableDND;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0, size = this.m_renderables.size(); i < size; ++i) {
            this.m_renderables.get(i).setEnabled(enabled);
        }
    }
    
    @Override
    public void setNetEnabled(final boolean enabled) {
        super.setNetEnabled(enabled);
        for (int i = 0, size = this.m_renderables.size(); i < size; ++i) {
            this.m_renderables.get(i).setNetEnabled(enabled);
        }
    }
    
    public boolean getSelectOnlyOne() {
        return this.m_selectOnlyOne;
    }
    
    public void setSelectOnlyOne(final boolean selectOnlyOne) {
        this.m_selectOnlyOne = selectOnlyOne;
    }
    
    public TreeNode getSelected() {
        return this.m_selectedNode;
    }
    
    public boolean getOpenOnlyOne() {
        return this.m_openOnlyOne;
    }
    
    public void setOpenOnlyOne(final boolean openOnlyOne) {
        this.m_openOnlyOne = openOnlyOne;
    }
    
    public TreeNode getTopOpened() {
        if (!this.m_openedNodes.isEmpty()) {
            return this.m_openedNodes.get(this.m_openedNodes.size() - 1);
        }
        return null;
    }
    
    public void setContent(final TreeNode content) {
        this.m_content = content;
        if (!this.m_displayRoot) {
            this.m_content.setOpened(true);
        }
        this.setContentDirty();
    }
    
    private void setContentDirty() {
        this.m_contentDirty = true;
        this.setNeedsToPreProcess();
    }
    
    private void setTreeDirty() {
        this.m_treeDirty = true;
        this.setNeedsToPreProcess();
    }
    
    private void setValuesDirty() {
        this.m_valuesDirty = true;
        this.setNeedsToPostProcess();
    }
    
    public void setOffset(final int offset) {
        if (this.m_offset == offset) {
            return;
        }
        final float value = this.offsetToSliderValue(offset);
        this.m_scrollBar.getSlider().setValue(value);
    }
    
    private void setListOffset(final int offset) {
        if (this.m_offset == offset) {
            return;
        }
        this.m_offset = offset;
        this.setValuesDirty();
    }
    
    public void setDisplayRoot(final boolean displayRoot) {
        this.m_displayRoot = displayRoot;
    }
    
    public void setNoClosingOnClick(final boolean noClosingOnClick) {
        this.m_noClosingOnClick = noClosingOnClick;
    }
    
    public void setNoUnselectingOnClick(final boolean noUnselectingOnClick) {
        this.m_noUnselectingOnClick = noUnselectingOnClick;
    }
    
    private void computeContent() {
        final TreeNode topOpenedNode = this.getTopOpened();
        this.m_computedContent.clear();
        final TreeNodeIterator it = new TreeNodeIterator(this.m_content);
        boolean first = !this.m_displayRoot;
        while (it.hasNext()) {
            if (first) {
                it.next();
                first = false;
            }
            else {
                this.m_computedContent.add(it.next());
            }
        }
        int offset = this.m_offset;
        final int index = this.m_computedContent.indexOf(topOpenedNode);
        if (this.m_computedContent.size() <= this.m_renderables.size()) {
            offset = 0;
        }
        else if (index == -1 || index < this.m_offset || index >= this.m_offset + this.m_renderables.size()) {
            if (index != -1 && index + this.m_renderables.size() <= this.m_computedContent.size()) {
                offset = index;
            }
        }
        offset = MathHelper.clamp(offset, 0, this.m_computedContent.size() - this.m_renderables.size());
        if (offset != this.m_offset) {
            this.setOffset(this.m_offset = offset);
        }
    }
    
    private int computeVisibleTreeSize() {
        return this.computeVisibleTreeSize(this.m_content) - (this.m_displayRoot ? 0 : 1);
    }
    
    private int computeVisibleTreeSize(final TreeNode node) {
        if (node == null) {
            return 0;
        }
        int openedSize = 1;
        if (node.hasChildren() && node.isOpened()) {
            final ArrayList<TreeNode> children = node.getChildren();
            for (int i = 0, size = children.size(); i < size; ++i) {
                openedSize += this.computeVisibleTreeSize(children.get(i));
            }
        }
        return openedSize;
    }
    
    private float offsetToSliderValue(int offset) {
        if (offset < 0) {
            offset = 0;
        }
        final int itemSize = this.computeVisibleTreeSize();
        final int count = itemSize - this.m_renderables.size();
        if (offset > count + 1) {
            offset = count + 1;
        }
        return 1.0f - offset / count;
    }
    
    private int sliderValueToOffset(final float value) {
        final int itemSize = this.computeVisibleTreeSize();
        final float count = itemSize - this.m_renderables.size();
        float offset = count - Math.round(count * value);
        if (offset < 0.0f) {
            offset = 0.0f;
        }
        else if (offset > count + 1.0f) {
            offset = count + 1.0f;
        }
        return Math.round(offset);
    }
    
    public void updateValues() {
        if (this.m_beingLayouted || this.m_renderables == null) {
            return;
        }
        int numNodeToPass = this.m_offset;
        int renderableIndex = 0;
        for (int i = 0, size = this.m_computedContent.size(); i < size; ++i) {
            final TreeNode node = this.m_computedContent.get(i);
            if (numNodeToPass != 0) {
                --numNodeToPass;
            }
            else {
                if (renderableIndex == this.m_renderables.size()) {
                    break;
                }
                final RenderableContainer renderable = this.m_renderables.get(renderableIndex);
                final StringBuilder propertyName = new StringBuilder();
                propertyName.append(this.m_contentProperty);
                propertyName.append("#").append(renderableIndex + this.m_offset);
                renderable.setContentProperty(propertyName.toString(), this.m_contentPropertyElementMap);
                renderable.setContent(node.getValue());
                final String style = this.getStyle();
                final StringBuilder sb = new StringBuilder("tree");
                if (style != null) {
                    sb.append(style);
                }
                sb.append("$");
                if (node.isOpened()) {
                    sb.append("openedCell");
                }
                else if (node.isSelected()) {
                    sb.append("selectedCell");
                }
                else if (node.hasChildren()) {
                    sb.append("cell");
                }
                else {
                    sb.append("leafCell");
                }
                renderable.getAppearance().setMargin(new Insets(0, (node.getDepth() - (this.m_displayRoot ? 0 : 1)) * 10 + 5, 0, 0));
                renderable.setStyle(sb.toString(), true);
                ++renderableIndex;
            }
        }
        for (int i = renderableIndex, size = this.m_renderables.size(); i < size; ++i) {
            final RenderableContainer renderable2 = this.m_renderables.get(renderableIndex);
            final StringBuilder propertyName2 = new StringBuilder();
            propertyName2.append(this.m_contentProperty);
            propertyName2.append("#").append(i + this.m_offset);
            renderable2.setContentProperty(propertyName2.toString(), this.m_contentPropertyElementMap);
            renderable2.setContent(null);
        }
    }
    
    @Override
    public void addedToWidgetTree() {
        this.m_scrollBar.addedToWidgetTree();
        super.addedToWidgetTree();
        this.addEventListener(Events.SLIDER_MOVED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final SliderMovedEvent e = (SliderMovedEvent)event;
                Tree.this.setListOffset(Tree.this.sliderValueToOffset(e.getValue()));
                return false;
            }
        }, false);
        this.addEventListener(Events.MOUSE_WHEELED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final MouseEvent e = (MouseEvent)event;
                Tree.this.setOffset(Tree.this.m_offset + e.getRotations());
                return false;
            }
        }, false);
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_contentDirty) {
            this.computeContent();
            this.setValuesDirty();
            this.m_treeDirty = true;
            this.m_contentDirty = false;
        }
        if (this.m_treeDirty) {
            super.invalidateMinSize();
            this.invalidate();
            this.m_treeDirty = false;
        }
        return ret;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        final boolean ret = super.postProcess(deltaTime);
        if (this.m_valuesDirty) {
            this.updateValues();
            this.m_valuesDirty = false;
        }
        return ret;
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        super.copyElement(c);
        final Tree t = (Tree)c;
        t.setCellHeight(this.m_cellHeight);
        t.setMinRows(this.m_minRows);
        t.setMaxRows(this.m_maxRows);
        t.setEnableDND(this.m_enableDND);
        t.setSelectOnlyOne(this.m_selectOnlyOne);
        t.setOpenOnlyOne(this.m_openOnlyOne);
        for (int i = t.m_widgetChildren.size() - 1; i >= 0; --i) {
            final Widget w = t.m_widgetChildren.get(i);
            if (w != t.m_scrollBar) {
                w.destroySelfFromParent();
            }
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_evenBGColor = null;
        this.m_oddBGColor = null;
        this.m_scrollBar = null;
        this.m_content = null;
        this.m_computedContent = null;
        this.m_backgroundMeshes = null;
        this.m_renderables = null;
        this.m_selectedNode = null;
        this.m_openedNodes = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final TreeLayout tl = new TreeLayout();
        tl.onCheckOut();
        this.add(tl);
        (this.m_scrollBar = new ScrollBar()).onCheckOut();
        this.m_scrollBar.setHorizontal(false);
        this.m_scrollBar.setValue(1.0f);
        this.add(this.m_scrollBar);
        this.m_minRows = -1;
        this.m_maxRows = -1;
        this.m_cellHeight = 30;
        this.m_enableDND = true;
        this.m_offset = 0;
        this.m_beingLayouted = false;
        this.m_treeDirty = false;
        this.m_rendererManager = new ItemRendererManager();
        this.m_renderables = new ArrayList<RenderableContainer>();
        this.m_backgroundMeshes = new ArrayList<ListOverMesh>();
        this.m_computedContent = new ArrayList<TreeNode>();
        this.m_openedNodes = new ArrayList<TreeNode>();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Tree.CELL_HEIGHT_HASH) {
            this.setCellHeight(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Tree.ENABLE_DND_HASH) {
            this.setEnableDND(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Tree.MIN_ROWS_HASH) {
            this.setMinRows(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Tree.MAX_ROWS_HASH) {
            this.setMaxRows(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Tree.OPEN_ONLY_ONE_HASH) {
            this.setOpenOnlyOne(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Tree.SELECT_ONLY_ONE_HASH) {
            this.setSelectOnlyOne(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Tree.DISPLAY_ROOT_HASH) {
            this.setDisplayRoot(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Tree.NO_CLOSING_ON_CLICK_HASH) {
            this.setNoClosingOnClick(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != Tree.NO_UNSELECTING_ON_CLICK_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setNoUnselectingOnClick(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Tree.CONTENT_HASH) {
            this.setContent((TreeNode)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        CONTENT_HASH = "content".hashCode();
        CELL_HEIGHT_HASH = "cellHeight".hashCode();
        ENABLE_DND_HASH = "enableDND".hashCode();
        MIN_ROWS_HASH = "minRows".hashCode();
        MAX_ROWS_HASH = "maxRows".hashCode();
        OPEN_ONLY_ONE_HASH = "openOnlyOne".hashCode();
        SELECT_ONLY_ONE_HASH = "selectOnlyOne".hashCode();
        DISPLAY_ROOT_HASH = "displayRoot".hashCode();
        NO_CLOSING_ON_CLICK_HASH = "noClosingOnClick".hashCode();
        NO_UNSELECTING_ON_CLICK_HASH = "noUnselectingOnClick".hashCode();
    }
    
    public class TreeLayout extends AbstractLayoutManager
    {
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            int height = 0;
            int width = 0;
            final int size = Tree.this.computeVisibleTreeSize();
            int rowCount = 1;
            if (Tree.this.m_maxRows >= 0 || Tree.this.m_minRows >= 0) {
                rowCount = Math.min(Math.max(Tree.this.m_maxRows, Tree.this.m_minRows), size);
            }
            if (rowCount < size) {
                final Dimension prefSize = Tree.this.m_scrollBar.getPrefSize();
                width += prefSize.width;
            }
            height += Tree.this.m_cellHeight * rowCount;
            return new Dimension(width, height);
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            return this.getContentPreferedSize(container);
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            Tree.this.m_beingLayouted = true;
            final int availableHeight = parent.getAppearance().getContentHeight();
            int availableWidth = parent.getAppearance().getContentWidth();
            final int numRows = Math.min(Tree.this.m_computedContent.size(), availableHeight / Tree.this.m_cellHeight);
            final int actualNumRows = Tree.this.m_renderables.size();
            if (numRows > actualNumRows) {
                Tree.this.m_renderables.ensureCapacity(numRows);
                Tree.this.m_backgroundMeshes.ensureCapacity(numRows);
                for (int i = actualNumRows; i < numRows; ++i) {
                    final ListOverMesh background = new ListOverMesh();
                    background.onCheckOut();
                    Tree.this.m_backgroundMeshes.add(background);
                    final RenderableContainer container = new RenderableContainer();
                    container.onCheckOut();
                    container.setNonBlocking(Tree.this.m_nonBlocking);
                    container.setRendererManager(Tree.this.m_rendererManager);
                    container.setEnableDND(Tree.this.m_enableDND);
                    container.setEnabled(Tree.this.m_enabled);
                    container.setNetEnabled(Tree.this.m_netEnabled);
                    container.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
                        @Override
                        public boolean run(final Event event) {
                            final int index = Tree.this.m_renderables.indexOf(container);
                            final TreeNode node = Tree.this.m_computedContent.get(index + Tree.this.m_offset);
                            boolean open = true;
                            if (node.hasChildren()) {
                                open = !node.isOpened();
                                if (open || !Tree.this.m_noClosingOnClick) {
                                    node.setOpened(open);
                                    if (Tree.this.m_openOnlyOne) {
                                        if (open) {
                                            TreeNode openedNode = Tree.this.m_openedNodes.isEmpty() ? null : Tree.this.m_openedNodes.get(Tree.this.m_openedNodes.size() - 1);
                                            if (openedNode != node.getParent()) {
                                                do {
                                                    openedNode = (Tree.this.m_openedNodes.isEmpty() ? null : Tree.this.m_openedNodes.remove(Tree.this.m_openedNodes.size() - 1));
                                                    if (openedNode != null) {
                                                        openedNode.setOpened(false);
                                                    }
                                                } while (openedNode != null && openedNode.getParent() != node.getParent());
                                            }
                                            Tree.this.m_openedNodes.add(node);
                                        }
                                        else {
                                            TreeNode openedNode;
                                            do {
                                                openedNode = (Tree.this.m_openedNodes.isEmpty() ? null : Tree.this.m_openedNodes.remove(Tree.this.m_openedNodes.size() - 1));
                                                if (openedNode != null) {
                                                    openedNode.setOpened(false);
                                                }
                                            } while (openedNode != null && openedNode != node);
                                        }
                                    }
                                }
                            }
                            final boolean selected = (!node.isSelected() && (open || Tree.this.m_noClosingOnClick)) || Tree.this.m_noUnselectingOnClick;
                            node.setSelected(selected);
                            if (Tree.this.m_selectOnlyOne && (!Tree.this.m_noUnselectingOnClick || node != Tree.this.m_selectedNode)) {
                                if (selected) {
                                    if (Tree.this.m_selectedNode != null) {
                                        Tree.this.m_selectedNode.setSelected(false);
                                    }
                                    Tree.this.m_selectedNode = node;
                                }
                                else {
                                    if (Tree.this.m_selectedNode != null) {
                                        Tree.this.m_selectedNode.setSelected(false);
                                    }
                                    Tree.this.m_selectedNode = null;
                                }
                                Tree.this.dispatchEvent(new ValueChangedEvent(Tree.this));
                            }
                            Tree.this.setContentDirty();
                            return false;
                        }
                    }, false);
                    Tree.this.m_renderables.add(container);
                    this.add(container);
                    container.setChildrenAdded(true);
                    final String style = Tree.this.getStyle();
                    final StringBuilder sb = new StringBuilder("tree");
                    if (style != null) {
                        sb.append(style);
                    }
                    sb.append("$").append("cell");
                    container.setStyle(sb.toString(), true);
                }
            }
            else if (numRows < actualNumRows) {
                final int toDelete = actualNumRows - numRows;
                for (int k = toDelete - 1; k >= 0; --k) {
                    final int lastIndex = Tree.this.m_renderables.size() - 1;
                    final RenderableContainer container2 = Tree.this.m_renderables.remove(lastIndex);
                    Tree.this.destroy(container2);
                    final ListOverMesh bg = Tree.this.m_backgroundMeshes.remove(lastIndex);
                    bg.onCheckIn();
                }
            }
            final int itemSize = Tree.this.computeVisibleTreeSize();
            Tree.this.m_displayScrollbar = (itemSize > Tree.this.m_renderables.size());
            if (Tree.this.m_displayScrollbar) {
                Tree.this.m_scrollBar.setVisible(true);
                final int scrollBarWidth = Tree.this.m_scrollBar.getPrefSize().width;
                availableWidth -= scrollBarWidth;
                Tree.this.m_scrollBar.setSize(scrollBarWidth, availableHeight);
                Tree.this.m_scrollBar.setPosition(availableWidth, 0);
            }
            else {
                Tree.this.m_scrollBar.setVisible(false);
            }
            final int x = 0;
            int y = availableHeight - Tree.this.m_cellHeight;
            for (int row = 0; row < numRows; ++row) {
                final RenderableContainer rc = Tree.this.m_renderables.get(row);
                if (rc == null) {
                    Tree.m_logger.warn((Object)("Impossible de trouver un renderableContainer \u00e0 la ligne " + row));
                }
                else {
                    rc.setSize(availableWidth, Tree.this.m_cellHeight);
                    rc.setPosition(x, y);
                    Tree.this.m_backgroundMeshes.get(row).setPositionSize(0, y, x, Tree.this.m_cellHeight, 0, 0, 0, 0);
                    y -= Tree.this.m_cellHeight;
                }
            }
            this.updateScrollBarLayout();
            Tree.this.m_beingLayouted = false;
        }
        
        private void updateScrollBarLayout() {
            if (Tree.this.m_displayScrollbar) {
                final int itemSize = Tree.this.computeVisibleTreeSize();
                final int count = itemSize - Tree.this.m_renderables.size();
                if (count > 0) {
                    Tree.this.m_scrollBar.setEnabled(true);
                    Tree.this.m_scrollBar.setButtonJump(1.0f / count);
                    Tree.this.m_scrollBar.setSliderSize(Tree.this.m_renderables.size() / itemSize);
                }
                else {
                    Tree.this.m_scrollBar.setButtonJump(0.0f);
                    Tree.this.m_scrollBar.setEnabled(false);
                }
            }
        }
    }
}
