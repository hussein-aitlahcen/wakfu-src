package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.decorator.mesh.*;
import com.ankamagames.xulor2.component.table.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.*;

public class Table extends Container implements EditableRenderableCollection, ContentClient, ColorClient, TableColumnVisibilityListener
{
    public static final String TAG = "table";
    public static final String BUTTON_TAG = "button";
    public static final String DIRECT_SORT_BUTTON_TAG = "directSortButton";
    public static final String INDIRECT_SORT_BUTTON_TAG = "indirectSortButton";
    public static final String ODD_CELL_TAG = "oddCell";
    public static final String EVEN_CELL_TAG = "evenCell";
    public static final String SCROLLBAR_TAG = "scrollBar";
    public static final String MOUSE_OVER_COLOR_TAG = "mouseOver";
    public static final String MOUSE_OVER_CELL_COLOR_TAG = "mouseOverCell";
    public static final String SELECTED_COLOR_TAG = "selected";
    private ArrayList<Button> m_buttons;
    private ArrayList<TableColumn> m_columns;
    private ArrayList<FieldProvider> m_items;
    private ArrayList<RenderableContainer> m_renderables;
    private ScrollBar m_scrollBar;
    private boolean m_displayScrollbar;
    private boolean m_beingLayouted;
    private int m_offset;
    private int m_selectedOffset;
    private int m_mouseOverIndex;
    private RenderableContainer m_selectedRenderable;
    private RenderableContainer m_mouseOverRenderable;
    private String m_contentProperty;
    private ElementMap m_contentPropertyElementMap;
    private boolean m_valuesDirty;
    private boolean m_tableDirty;
    private int[] m_indexes;
    private ArrayList<ListOverMesh> m_backgroundMeshes;
    private ListOverMesh m_mouseOverMesh;
    private ListOverMesh m_mouseOverCellMesh;
    private ListOverMesh m_selectedMesh;
    private ScrollBar.ScrollBarBehaviour m_scrollBarBehaviour;
    private int m_cellHeight;
    private int m_minRows;
    private int m_maxRows;
    private boolean m_enableDND;
    private TableModel m_tableModel;
    private Color m_evenBGColor;
    private Color m_oddBGColor;
    private boolean m_selectionable;
    private boolean m_selectionTogglable;
    public static final int CONTENT_HASH;
    public static final int CELL_HEIGHT_HASH;
    public static final int ENABLE_DND_HASH;
    public static final int MIN_ROWS_HASH;
    public static final int MAX_ROWS_HASH;
    public static final int SCROLL_BAR_BEHAVIOUR_HASH;
    public static final int SELECTIONABLE_HASH;
    
    public Table() {
        super();
        this.m_displayScrollbar = false;
        this.m_selectedOffset = -1;
        this.m_mouseOverIndex = -1;
        this.m_contentProperty = null;
        this.m_contentPropertyElementMap = null;
        this.m_indexes = null;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        super.add(e);
        if (e instanceof TableColumn) {
            final TableColumn tableColumn = (TableColumn)e;
            tableColumn.setVisibilityListener(this);
            this.addColumn(tableColumn);
        }
    }
    
    private void addColumn(final TableColumn column) {
        this.m_columns.add(column);
        final Button button = new Button();
        button.onCheckOut();
        this.add(button);
        button.setElementMap(this.m_elementMap);
        button.setChildrenAdded(true);
        button.setCanBeCloned(false);
        final String style = this.getStyle();
        final StringBuilder sb = new StringBuilder("table");
        if (style != null) {
            sb.append(style);
        }
        sb.append("$").append("button");
        final String newStyle = sb.toString();
        button.setStyle(newStyle, true);
        button.setText(column.getName());
        button.setEnabled(this.m_enabled && column.getSortable());
        button.setNetEnabled(this.m_netEnabled);
        button.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                for (int i = 0, size = Table.this.m_buttons.size(); i < size; ++i) {
                    final Button b = Table.this.m_buttons.get(i);
                    if (button != b) {
                        b.setPixmap(null);
                        b.setStyle(newStyle, false);
                    }
                }
                if (Table.this.m_tableModel != null) {
                    Table.this.m_indexes = Table.this.m_tableModel.getIndexes(Table.this.m_items, column.getColumnId());
                    final StringBuilder sb = new StringBuilder("table");
                    if (style != null) {
                        sb.append(style);
                    }
                    sb.append("$");
                    if (Table.this.m_tableModel.isDirect()) {
                        sb.append("directSortButton");
                    }
                    else {
                        sb.append("indirectSortButton");
                    }
                    button.setStyle(sb.toString(), true);
                    Table.this.setValuesDirty();
                }
                return false;
            }
        }, false);
        this.m_buttons.add(button);
        this.setTableDirty();
    }
    
    @Override
    protected void addInnerMeshes() {
        for (int i = this.m_backgroundMeshes.size() - 1; i >= 0; --i) {
            this.m_entity.addChild(this.m_backgroundMeshes.get(i).getEntity());
        }
        if (this.m_selectedMesh != null && this.m_selectedRenderable != null) {
            this.m_entity.addChild(this.m_selectedMesh.getEntity());
        }
        if (this.m_mouseOverMesh != null && this.m_mouseOverRenderable != null) {
            this.m_entity.addChild(this.m_mouseOverMesh.getEntity());
        }
        if (this.m_mouseOverCellMesh != null && this.m_mouseOverRenderable != null) {
            this.m_entity.addChild(this.m_mouseOverCellMesh.getEntity());
        }
        super.addInnerMeshes();
    }
    
    @Override
    public String getTag() {
        return "table";
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
        if (name == null || name.equals("mouseOver")) {
            if (c != null) {
                if (this.m_mouseOverMesh == null) {
                    (this.m_mouseOverMesh = new ListOverMesh()).onCheckOut();
                    this.setNeedsToResetMeshes();
                }
                this.m_mouseOverMesh.setColor(c);
            }
            else {
                if (this.m_mouseOverMesh != null) {
                    this.m_mouseOverMesh.onCheckIn();
                }
                this.m_mouseOverMesh = null;
                this.setNeedsToResetMeshes();
            }
        }
        else if (name.equals("mouseOverCell")) {
            if (c != null) {
                if (this.m_mouseOverCellMesh == null) {
                    (this.m_mouseOverCellMesh = new ListOverMesh()).onCheckOut();
                    this.setNeedsToResetMeshes();
                }
                this.m_mouseOverCellMesh.setColor(c);
            }
            else {
                if (this.m_mouseOverCellMesh != null) {
                    this.m_mouseOverCellMesh.onCheckIn();
                }
                this.m_mouseOverCellMesh = null;
                this.setNeedsToResetMeshes();
            }
        }
        else if (name.equals("selected")) {
            if (c != null) {
                if (this.m_selectedMesh == null) {
                    (this.m_selectedMesh = new ListOverMesh()).onCheckOut();
                    this.setNeedsToResetMeshes();
                }
                this.m_selectedMesh.setColor(c);
            }
            else {
                if (this.m_selectedMesh != null) {
                    this.m_selectedMesh.onCheckIn();
                }
                this.m_selectedMesh = null;
                this.setNeedsToResetMeshes();
            }
        }
        else if (name.equals("evenCell")) {
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
    
    public void setSelectionable(final boolean selectionable) {
        this.m_selectionable = selectionable;
    }
    
    public ScrollBar.ScrollBarBehaviour getScrollBarBehaviour() {
        return this.m_scrollBarBehaviour;
    }
    
    public void setScrollBarBehaviour(final ScrollBar.ScrollBarBehaviour scrollBarBehaviour) {
        this.m_scrollBarBehaviour = scrollBarBehaviour;
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
        for (int i = 0, size = this.m_buttons.size(); i < size; ++i) {
            this.m_buttons.get(i).setEnabled(enabled && this.m_columns.get(i).getSortable());
        }
    }
    
    @Override
    public void setNetEnabled(final boolean enabled) {
        super.setNetEnabled(enabled);
        for (int i = 0, size = this.m_renderables.size(); i < size; ++i) {
            this.m_renderables.get(i).setNetEnabled(enabled);
        }
        for (int i = 0, size = this.m_buttons.size(); i < size; ++i) {
            this.m_buttons.get(i).setNetEnabled(enabled);
        }
    }
    
    public void setContent(final Iterable<FieldProvider> items) {
        assert this.m_items != null;
        Object oldSelectedValue = null;
        if (this.m_selectedOffset != -1) {
            oldSelectedValue = this.m_items.get(this.m_indexes[this.m_selectedOffset]);
        }
        this.m_items.clear();
        if (items != null) {
            final Iterator<FieldProvider> it = items.iterator();
            while (it.hasNext()) {
                this.m_items.add(it.next());
            }
        }
        this.m_indexes = null;
        final int newSelectedOffset = this.getSelectedOffsetByValue(oldSelectedValue);
        if (this.m_offset == 0 || this.getRenderableByOffset(this.m_offset) == null) {
            this.setOffset(0);
        }
        if (newSelectedOffset != -1) {
            this.setSelectedOffset(newSelectedOffset, false);
        }
        else {
            this.m_selectedOffset = MathHelper.clamp(this.m_selectedOffset, -1, this.m_items.size() - 1);
            this.m_selectedRenderable = this.getRenderableByOffset(this.m_selectedOffset);
            if (this.m_indexes == null) {
                this.regenerateIndexes();
            }
            Object newValue = null;
            if (this.m_selectedOffset != -1) {
                newValue = this.m_items.get(this.m_indexes[this.m_selectedOffset]);
            }
            if (oldSelectedValue != newValue) {
                final ValueChangedEvent e = new ValueChangedEvent(this);
                e.setOldValue(oldSelectedValue);
                e.setValue(newValue);
                this.dispatchEvent(e);
            }
            this.updateSelectedAppearance();
        }
        this.m_mouseOverRenderable = null;
        this.setTableDirty();
        this.setValuesDirty();
    }
    
    public void setContent(final Object[] items) {
        assert this.m_items != null;
        Object oldSelectedValue = null;
        if (this.m_selectedOffset != -1) {
            oldSelectedValue = this.m_items.get(this.m_indexes[this.m_selectedOffset]);
        }
        this.m_items.clear();
        if (items != null) {
            for (final Object fp : items) {
                this.m_items.add((FieldProvider)fp);
            }
        }
        this.m_indexes = null;
        this.setOffset(0);
        final int newSelectedOffset = this.getSelectedOffsetByValue(oldSelectedValue);
        if (newSelectedOffset != -1) {
            this.setSelectedOffset(newSelectedOffset, false);
        }
        else {
            this.m_selectedOffset = MathHelper.clamp(this.m_selectedOffset, -1, this.m_items.size() - 1);
            this.m_selectedRenderable = this.getRenderableByOffset(this.m_selectedOffset);
            if (this.m_indexes == null) {
                this.regenerateIndexes();
            }
            Object newValue = null;
            if (this.m_selectedOffset != -1) {
                newValue = this.m_items.get(this.m_indexes[this.m_selectedOffset]);
            }
            if (oldSelectedValue != newValue) {
                final ValueChangedEvent e = new ValueChangedEvent(this);
                e.setOldValue(oldSelectedValue);
                e.setValue(newValue);
                this.dispatchEvent(e);
            }
            this.updateSelectedAppearance();
        }
        this.m_mouseOverRenderable = null;
        this.setTableDirty();
        this.setValuesDirty();
    }
    
    private RenderableContainer getRenderableByPosition(final int row, final int col) {
        final int index = row * this.m_columns.size() + col;
        if (index < 0 || index >= this.m_renderables.size()) {
            return null;
        }
        return this.m_renderables.get(index);
    }
    
    private void setTableDirty() {
        this.m_tableDirty = true;
        this.setNeedsToPreProcess();
    }
    
    private void setValuesDirty() {
        this.m_valuesDirty = true;
        this.setNeedsToPostProcess();
    }
    
    @Override
    public Object getValue(final int i) {
        if (i < 0 || i >= this.m_items.size()) {
            return null;
        }
        return this.m_items.get(i);
    }
    
    @Override
    public RenderableContainer getSelected() {
        return null;
    }
    
    @Override
    public int getTableIndex(final RenderableContainer container) {
        return this.m_renderables.indexOf(container);
    }
    
    @Override
    public int getItemIndex(final Object value) {
        return this.m_items.indexOf(value);
    }
    
    @Override
    public Widget getWidget(final String type, final int index) {
        if (index >= 0 && index < this.m_renderables.size()) {
            return this.m_renderables.get(index);
        }
        return null;
    }
    
    @Override
    public ArrayList<RenderableContainer> getRenderables() {
        return this.m_renderables;
    }
    
    public void setTableModel(final TableModel model) {
        this.m_tableModel = model;
    }
    
    public TableModel getTableModel() {
        return this.m_tableModel;
    }
    
    public int getOffset() {
        return this.m_offset;
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
    
    public int getSelectedOffsetByValue(final Object value) {
        if (this.m_items == null) {
            return -1;
        }
        final int valueIndex = this.m_items.indexOf(value);
        if (valueIndex == -1) {
            return -1;
        }
        if (this.m_indexes == null) {
            this.regenerateIndexes();
        }
        for (int i = 0; i < this.m_indexes.length; ++i) {
            if (this.m_indexes[i] == valueIndex) {
                return i;
            }
        }
        return -1;
    }
    
    public int getOffsetByRenderable(final RenderableContainer selected) {
        if (selected == null || this.m_items == null) {
            return -1;
        }
        int offset = this.m_offset + this.m_renderables.indexOf(selected) / this.m_columns.size();
        if (offset >= this.m_items.size()) {
            offset = -1;
        }
        return offset;
    }
    
    public RenderableContainer getRenderableByOffset(final int offset) {
        if (offset == -1 || this.m_items == null) {
            return null;
        }
        final int renderableIndex = (offset - this.m_offset) * this.m_columns.size();
        if (renderableIndex < 0 || renderableIndex >= this.m_renderables.size()) {
            return null;
        }
        return this.m_renderables.get(renderableIndex);
    }
    
    public void setSelectedOffset(final int offset, final boolean force) {
        if (offset == this.m_selectedOffset && !force) {
            return;
        }
        final int oldOffset = this.m_selectedOffset;
        this.m_selectedOffset = offset;
        this.m_selectedRenderable = this.getRenderableByOffset(offset);
        if (this.m_indexes == null) {
            this.regenerateIndexes();
        }
        Object oldValue = null;
        Object newValue = null;
        if (oldOffset != -1) {
            oldValue = this.m_items.get(this.m_indexes[oldOffset]);
        }
        if (this.m_selectedOffset != -1) {
            newValue = this.m_items.get(this.m_indexes[this.m_selectedOffset]);
        }
        if (oldValue != newValue) {
            final ValueChangedEvent e = new ValueChangedEvent(this);
            e.setOldValue(oldValue);
            e.setValue(newValue);
            this.dispatchEvent(e);
        }
        this.updateSelectedAppearance();
    }
    
    private void updateSelectedAppearance() {
        if (this.m_selectedRenderable != null && this.m_selectedMesh != null) {
            this.m_selectedMesh.setPositionSize(0, this.m_selectedRenderable.getY(), this.m_appearance.getContentWidth() - this.m_scrollBar.getWidth(), this.m_selectedRenderable.getHeight(), this.m_appearance.getTopInset(), this.m_appearance.getBottomInset(), this.m_appearance.getLeftInset(), this.m_appearance.getRightInset());
        }
        this.setNeedsToResetMeshes();
    }
    
    private void fireSelectionChanged(final RenderableContainer selected) {
        if (selected == this.m_selectedRenderable) {
            return;
        }
        if (selected != null) {
            this.setSelectedOffset(this.getOffsetByRenderable(selected), false);
        }
        else {
            this.m_selectedOffset = -1;
        }
    }
    
    private float offsetToSliderValue(int offset) {
        if (offset < 0) {
            offset = 0;
        }
        final int count = this.m_items.size() - this.m_renderables.size() / this.m_columns.size();
        if (count == 0) {
            return 1.0f;
        }
        if (count > 0 && offset > count + 1) {
            offset = count + 1;
        }
        return 1.0f - offset / count;
    }
    
    private int sliderValueToOffset(final float value) {
        final float count = this.m_items.size() - this.m_renderables.size() / this.m_columns.size();
        float offset = count - Math.round(count * value);
        if (count < 0.0f || offset < 0.0f) {
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
        this.regenerateIndexes();
        this.m_selectedRenderable = null;
        final int realOffset = this.m_offset;
        for (int j = 0; j < this.m_renderables.size(); j += this.m_columns.size()) {
            final int cellOffset = j / this.m_columns.size() + realOffset;
            final RenderableContainer renderable = this.m_renderables.get(j);
            if (j >= 0) {
                if (j < this.m_renderables.size()) {
                    if (this.m_items != null && cellOffset >= 0 && cellOffset < this.m_items.size() && cellOffset == this.m_selectedOffset) {
                        this.m_selectedRenderable = renderable;
                        break;
                    }
                }
            }
        }
        for (int numRows = this.m_renderables.size() / this.m_columns.size(), row = 0; row < numRows; ++row) {
            final int offset = row + realOffset;
            final int itemOffset = (offset >= this.m_indexes.length) ? offset : this.m_indexes[offset];
            this.m_backgroundMeshes.get(row).setColor((offset % 2 == 0) ? this.m_evenBGColor : this.m_oddBGColor);
            for (int col = this.m_columns.size() - 1; col >= 0; --col) {
                final TableColumn currentCol = this.m_columns.get(col);
                final RenderableContainer renderable2 = this.getRenderableByPosition(row, col);
                final String field = currentCol.getField();
                final StringBuilder propertyName = new StringBuilder();
                propertyName.append(this.m_contentProperty);
                propertyName.append("#").append(itemOffset);
                if (field != null) {
                    propertyName.append("/").append(currentCol.getField());
                }
                renderable2.setContentProperty(propertyName.toString(), this.m_contentPropertyElementMap);
                if (this.m_items != null && itemOffset >= 0 && itemOffset < this.m_items.size()) {
                    final FieldProvider item = this.m_items.get(itemOffset);
                    if (item != null) {
                        if (field != null) {
                            renderable2.setContent(item.getFieldValue(currentCol.getField()));
                        }
                        else {
                            renderable2.setContent(item);
                        }
                    }
                    else {
                        renderable2.setContent(null);
                    }
                }
                else {
                    renderable2.setContent(null);
                }
            }
        }
        this.updateSelectedAppearance();
    }
    
    private void regenerateIndexes() {
        if (this.m_indexes == null && this.m_tableModel != null) {
            this.m_indexes = this.m_tableModel.regenerateIndexes(this.m_items);
        }
        if (this.m_indexes == null) {
            this.m_indexes = TableModel.getSimpleOrder(this.m_items.size());
        }
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_tableDirty) {
            super.invalidateMinSize();
            this.invalidate();
            this.m_tableDirty = false;
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
        final Table t = (Table)c;
        t.setCellHeight(this.m_cellHeight);
        t.setMinRows(this.m_minRows);
        t.setMaxRows(this.m_maxRows);
        t.setEnableDND(this.m_enableDND);
        t.setTableModel(this.m_tableModel);
        t.setScrollBarBehaviour(this.m_scrollBarBehaviour);
        t.setSelectionable(this.m_selectionable);
    }
    
    @Override
    public void addedToWidgetTree() {
        this.m_scrollBar.addedToWidgetTree();
        super.addedToWidgetTree();
        this.addEventListener(Events.SLIDER_MOVED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final SliderMovedEvent e = (SliderMovedEvent)event;
                Table.this.setListOffset(Table.this.sliderValueToOffset(e.getValue()));
                return false;
            }
        }, false);
        this.addEventListener(Events.MOUSE_WHEELED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final MouseEvent e = (MouseEvent)event;
                Table.this.setOffset(Table.this.m_offset + e.getRotations());
                return false;
            }
        }, false);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_evenBGColor = null;
        this.m_oddBGColor = null;
        if (this.m_mouseOverMesh != null) {
            this.m_mouseOverMesh.onCheckIn();
            this.m_mouseOverMesh = null;
        }
        if (this.m_mouseOverCellMesh != null) {
            this.m_mouseOverCellMesh.onCheckIn();
            this.m_mouseOverCellMesh = null;
        }
        if (this.m_selectedMesh != null) {
            this.m_selectedMesh.onCheckIn();
            this.m_selectedMesh = null;
        }
        if (this.m_backgroundMeshes != null) {
            for (int i = this.m_backgroundMeshes.size() - 1; i >= 0; --i) {
                this.m_backgroundMeshes.get(i).onCheckIn();
            }
            this.m_backgroundMeshes = null;
        }
        this.m_selectedRenderable = null;
        this.m_mouseOverRenderable = null;
        this.m_buttons = null;
        this.m_scrollBar = null;
        this.m_columns = null;
        this.m_items = null;
        this.m_tableModel = null;
        this.m_renderables = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final TableLayout tl = new TableLayout();
        tl.onCheckOut();
        this.add(tl);
        (this.m_scrollBar = new ScrollBar()).onCheckOut();
        this.m_scrollBar.setHorizontal(false);
        this.m_scrollBar.setValue(1.0f);
        this.m_scrollBar.setCanBeCloned(false);
        this.add(this.m_scrollBar);
        this.m_minRows = -1;
        this.m_maxRows = -1;
        this.m_cellHeight = 30;
        this.m_enableDND = true;
        this.m_offset = 0;
        this.m_beingLayouted = false;
        this.m_tableDirty = false;
        this.m_nonBlocking = false;
        this.m_selectionable = true;
        this.m_selectionTogglable = false;
        this.m_selectedOffset = -1;
        this.m_mouseOverIndex = -1;
        this.m_renderables = new ArrayList<RenderableContainer>();
        this.m_buttons = new ArrayList<Button>();
        this.m_columns = new ArrayList<TableColumn>();
        this.m_items = new ArrayList<FieldProvider>();
        this.m_backgroundMeshes = new ArrayList<ListOverMesh>();
        this.m_scrollBarBehaviour = ScrollBar.ScrollBarBehaviour.WHEN_NEEDED;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Table.CELL_HEIGHT_HASH) {
            this.setCellHeight(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Table.ENABLE_DND_HASH) {
            this.setEnableDND(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Table.MIN_ROWS_HASH) {
            this.setMinRows(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Table.MAX_ROWS_HASH) {
            this.setMaxRows(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Table.SELECTIONABLE_HASH) {
            this.setSelectionable(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != Table.SCROLL_BAR_BEHAVIOUR_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setScrollBarBehaviour(ScrollBar.ScrollBarBehaviour.value(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Table.CONTENT_HASH) {
            if (value == null || value.getClass().isArray()) {
                this.setContent((Object[])value);
            }
            else {
                if (!(value instanceof Iterable)) {
                    return false;
                }
                this.setContent((Iterable<FieldProvider>)value);
            }
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    @Override
    public void removeValue(final Object value) {
    }
    
    @Override
    public void addValue(final Object value) {
    }
    
    @Override
    public boolean addValue(final int position, final Object value) {
        return false;
    }
    
    @Override
    public void addValue(final Object oldItem, final Object newItem) {
    }
    
    @Override
    public boolean replaceValue(final Object oldItem, final Object newItem) {
        return false;
    }
    
    @Override
    public int size() {
        return this.m_items.size();
    }
    
    @Override
    public void onVisibilityChange(final TableColumn tableColumn, final boolean visible) {
        if (visible) {
            this.addColumn(tableColumn);
        }
        else {
            final int index = this.m_columns.indexOf(tableColumn);
            this.m_columns.remove(tableColumn);
            final Button remove = this.m_buttons.remove(index);
            if (remove != null) {
                remove.setVisible(false);
            }
            this.setTableDirty();
            this.setNeedsToPreProcess();
        }
    }
    
    static {
        CONTENT_HASH = "content".hashCode();
        CELL_HEIGHT_HASH = "cellHeight".hashCode();
        ENABLE_DND_HASH = "enableDND".hashCode();
        MIN_ROWS_HASH = "minRows".hashCode();
        MAX_ROWS_HASH = "maxRows".hashCode();
        SCROLL_BAR_BEHAVIOUR_HASH = "scrollBarBehaviour".hashCode();
        SELECTIONABLE_HASH = "selectionable".hashCode();
    }
    
    public class TableLayout extends AbstractLayoutManager
    {
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            int height = 0;
            int width = 0;
            final int size = (Table.this.m_items == null) ? 0 : Table.this.m_items.size();
            int rowCount = 1;
            if (Table.this.m_maxRows >= 0 || Table.this.m_minRows >= 0) {
                rowCount = MathHelper.clamp(size, Table.this.m_minRows, Table.this.m_maxRows);
            }
            for (int i = Table.this.m_columns.size() - 1; i >= 0; --i) {
                width += Table.this.m_columns.get(i).getCellWidth();
                final Button button = Table.this.m_buttons.get(i);
                if (button != null) {
                    height = Math.max(height, button.getPrefSize().height);
                }
                else {
                    Table.m_logger.warn((Object)"Un bouton de colonne n'a pas \u00e9t\u00e9 initialis\u00e9 correctement");
                }
            }
            final Dimension prefSize = Table.this.m_scrollBar.getPrefSize();
            switch (Table.this.m_scrollBarBehaviour) {
                case FORCE_DISPLAY: {
                    width += prefSize.width;
                    break;
                }
                case WHEN_NEEDED: {
                    if (rowCount < size) {
                        width += prefSize.width;
                        break;
                    }
                    break;
                }
            }
            height += Table.this.m_cellHeight * rowCount;
            return new Dimension(width, height);
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            return this.getContentPreferedSize(container);
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            Table.this.m_beingLayouted = true;
            final int contentHeight = parent.getAppearance().getContentHeight();
            final int numCols = Table.this.m_columns.size();
            if (numCols == 0) {
                return;
            }
            final int numRows = contentHeight / Table.this.m_cellHeight;
            final int actualNumRows = Table.this.m_renderables.size() / numCols;
            if (numRows > actualNumRows) {
                Table.this.m_renderables.ensureCapacity(numCols * numRows);
                Table.this.m_backgroundMeshes.ensureCapacity(numRows);
                for (int i = actualNumRows; i < numRows; ++i) {
                    final ListOverMesh background = new ListOverMesh();
                    background.onCheckOut();
                    Table.this.m_backgroundMeshes.add(background);
                    for (int j = 0; j < numCols; ++j) {
                        final TableColumn currentColumn = Table.this.m_columns.get(j);
                        final RenderableContainer container = new RenderableContainer();
                        container.onCheckOut();
                        container.setCollection(Table.this);
                        container.setNonBlocking(Table.this.m_nonBlocking);
                        container.setRendererManager(currentColumn.getRendererManager());
                        container.setEnableDND(Table.this.m_enableDND);
                        container.setEnabled(Table.this.m_enabled);
                        container.setNetEnabled(Table.this.m_netEnabled);
                        container.setCanBeCloned(false);
                        container.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
                            @Override
                            public boolean run(final Event event) {
                                if (Table.this.m_selectionable) {
                                    RenderableContainer selected;
                                    if (Table.this.m_selectionTogglable && Table.this.m_selectedRenderable == event.getCurrentTarget()) {
                                        selected = null;
                                    }
                                    else {
                                        selected = event.getCurrentTarget();
                                    }
                                    Table.this.fireSelectionChanged(selected);
                                }
                                return false;
                            }
                        }, false);
                        container.addEventListener(Events.MOUSE_ENTERED, new EventListener() {
                            @Override
                            public boolean run(final Event event) {
                                final RenderableContainer container = event.getCurrentTarget();
                                if (container.getItemValue() != null) {
                                    Table.this.m_mouseOverRenderable = container;
                                    if (Table.this.m_mouseOverMesh != null) {
                                        Table.this.m_mouseOverMesh.setPositionSize(0, Table.this.m_mouseOverRenderable.getY(), Table.this.m_appearance.getContentWidth() - Table.this.m_scrollBar.getWidth(), Table.this.m_mouseOverRenderable.getHeight(), Table.this.m_appearance.getTopInset(), Table.this.m_appearance.getBottomInset(), Table.this.m_appearance.getLeftInset(), Table.this.m_appearance.getRightInset());
                                        Table.this.setNeedsToResetMeshes();
                                    }
                                    if (Table.this.m_mouseOverCellMesh != null) {
                                        Table.this.m_mouseOverCellMesh.setPositionSize(Table.this.m_mouseOverRenderable.getPosition(), Table.this.m_mouseOverRenderable.getSize(), Table.this.m_appearance.getTotalInsets());
                                    }
                                }
                                return false;
                            }
                        }, false);
                        container.addEventListener(Events.MOUSE_EXITED, new EventListener() {
                            @Override
                            public boolean run(final Event event) {
                                Table.this.m_mouseOverRenderable = null;
                                Table.this.setNeedsToResetMeshes();
                                return false;
                            }
                        }, false);
                        Table.this.m_renderables.add(container);
                        this.add(container);
                    }
                }
            }
            else if (numRows < actualNumRows) {
                final int toDelete = numCols * (actualNumRows - numRows);
                for (int k = toDelete - 1; k >= 0; --k) {
                    final RenderableContainer container2 = Table.this.m_renderables.remove(Table.this.m_renderables.size() - 1);
                    Table.this.destroy(container2);
                }
                for (int l = actualNumRows - numRows - 1; l >= 0; --l) {
                    final ListOverMesh bg = Table.this.m_backgroundMeshes.remove(Table.this.m_backgroundMeshes.size() - 1);
                    bg.onCheckIn();
                }
            }
            int buttonHeight = 0;
            for (int l = Table.this.m_columns.size() - 1; l >= 0; --l) {
                final Button button = Table.this.m_buttons.get(l);
                if (button != null) {
                    buttonHeight = Math.max(buttonHeight, button.getPrefSize().height);
                }
                else {
                    Table.m_logger.warn((Object)"Un bouton de colonne n'a pas \u00e9t\u00e9 initialis\u00e9 correctement");
                }
            }
            int x = 0;
            for (int col = 0; col < numCols; ++col) {
                final TableColumn currentColumn = Table.this.m_columns.get(col);
                final int cellWidth = currentColumn.getCellWidth();
                int y = contentHeight - buttonHeight;
                final Button button2 = Table.this.m_buttons.get(col);
                button2.setSize(cellWidth, buttonHeight);
                button2.setPosition(x, y);
                y -= Table.this.m_cellHeight;
                for (int row = 0; row < numRows; ++row) {
                    final RenderableContainer rc = Table.this.getRenderableByPosition(row, col);
                    if (rc == null) {
                        Table.m_logger.warn((Object)("Impossible de trouver un renderableContainer \u00e0 la ligne " + row + " et \u00e0 la colonne " + col));
                    }
                    else {
                        rc.setSize(cellWidth, Table.this.m_cellHeight);
                        rc.setPosition(x, y);
                        y -= Table.this.m_cellHeight;
                    }
                }
                x += cellWidth;
            }
            int y2 = contentHeight - buttonHeight - Table.this.m_cellHeight;
            for (int row2 = 0; row2 < numRows; ++row2) {
                Table.this.m_backgroundMeshes.get(row2).setPositionSize(0, y2, x, Table.this.m_cellHeight, 0, 0, 0, 0);
                y2 -= Table.this.m_cellHeight;
            }
            switch (Table.this.m_scrollBarBehaviour) {
                case FORCE_HIDE: {
                    Table.this.m_displayScrollbar = false;
                    break;
                }
                case FORCE_DISPLAY: {
                    Table.this.m_displayScrollbar = true;
                    break;
                }
                case WHEN_NEEDED: {
                    Table.this.m_displayScrollbar = (Table.this.m_items.size() > Table.this.m_renderables.size() / Table.this.m_columns.size());
                    break;
                }
            }
            if (Table.this.m_displayScrollbar) {
                Table.this.m_scrollBar.setVisible(true);
                final int scrollBarWidth = Table.this.m_scrollBar.getPrefSize().width;
                Table.this.m_scrollBar.setSize(scrollBarWidth, contentHeight);
                Table.this.m_scrollBar.setPosition(x, 0);
            }
            else {
                Table.this.m_scrollBar.setVisible(false);
            }
            this.updateScrollBarLayout();
            final int delta = Table.this.m_items.size() - numRows;
            final int offset = (delta < 0) ? 0 : MathHelper.clamp(Table.this.m_offset, 0, delta);
            if (offset != Table.this.m_offset) {
                Table.this.setOffset(offset);
            }
            if (numRows != actualNumRows) {
                Table.this.setValuesDirty();
            }
            Table.this.m_beingLayouted = false;
        }
        
        private void updateScrollBarLayout() {
            if (Table.this.m_displayScrollbar) {
                final int count = Table.this.m_items.size() - Table.this.m_renderables.size() / Table.this.m_columns.size();
                if (count > 0) {
                    Table.this.m_scrollBar.setEnabled(true);
                    Table.this.m_scrollBar.setButtonJump(1.0f / count);
                    Table.this.m_scrollBar.setSliderSize(Table.this.m_renderables.size() / Table.this.m_columns.size() / Table.this.m_items.size());
                }
                else {
                    Table.this.m_scrollBar.setButtonJump(0.0f);
                    Table.this.m_scrollBar.setEnabled(false);
                }
            }
        }
    }
}
