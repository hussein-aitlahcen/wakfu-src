package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.component.list.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.decorator.mesh.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import java.awt.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.appearance.*;

public class List extends Container implements EditableRenderableCollection, ContentClient
{
    public static final String TAG = "List";
    public static final String THEME_HORIZONTAL_SCROLLBAR = "horizontalScrollBar";
    public static final String THEME_VERTICAL_SCROLLBAR = "verticalScrollBar";
    private ListFilter m_filter;
    private ScrollBar m_scrollBar;
    private float m_currentColumnCount;
    private float m_currentRowCount;
    private Dimension m_cellSize;
    private boolean m_adaptCellSizeToContentSize;
    private boolean m_enableDND;
    protected float m_offset;
    private int m_minDisplayedCells;
    private boolean m_beingLayouted;
    private boolean m_horizontal;
    private boolean m_oppositeScrollBarPosition;
    private int m_showOneMore;
    private boolean m_displayScrollbar;
    private boolean m_autoIdealSize;
    private int m_idealSizeMaxRows;
    private int m_idealSizeMaxColumns;
    private int m_idealSizeMinRows;
    private int m_idealSizeMinColumns;
    private ListLayoutMode m_listLayoutMode;
    private int m_isoColumnCount;
    private boolean m_isoPositiveFactor;
    private boolean m_selectionTogglable;
    private boolean m_selectionable;
    private boolean m_canSelectNull;
    private ScrollBar.ScrollBarBehaviour m_scrollBarBehaviour;
    private ArrayList<Point> m_renderablePositions;
    protected ArrayList<RenderableContainer> m_renderables;
    private RenderableContainer m_mouseOverRenderable;
    private RenderableContainer m_selectedRenderable;
    private int m_selectedOffset;
    private ItemRendererManager m_rendererManager;
    protected ArrayList<Object> m_items;
    private ArrayList<Object> m_newItems;
    private Alignment9 m_alignment;
    private String m_contentProperty;
    private ElementMap m_contentPropertyElementMap;
    private boolean m_listIsDirty;
    private boolean m_needToUpdateValues;
    private ListOverMesh m_mouseOverMesh;
    private ListOverMesh m_selectedMesh;
    private ListScrollMode m_scrollMode;
    private boolean m_scrollOnMouseWheel;
    private final ArrayList<CollectionContentLoadedListener> m_collectionContentLoadedListeners;
    private final ArrayList<CollectionOffsetListener> m_collectionOffsetListeners;
    private boolean m_listContentLoaded;
    public static long LAYOUT_TIME;
    public static final int ALIGN_HASH;
    public static final int ADAPT_CELL_SIZE_TO_CONTENT_SIZE_HASH;
    public static final int AUTO_IDEAL_SIZE_HASH;
    public static final int CELL_SIZE_HASH;
    public static final int CONTENT_HASH;
    public static final int HORIZONTAL_HASH;
    public static final int IDEAL_SIZE_MAX_COLUMNS_HASH;
    public static final int IDEAL_SIZE_MAX_ROWS_HASH;
    public static final int IDEAL_SIZE_MIN_COLUMNS_HASH;
    public static final int IDEAL_SIZE_MIN_ROWS_HASH;
    public static final int LIST_FILTER_HASH;
    public static final int LIST_OFFSET_HASH;
    public static final int MIN_DISPLAYED_CELLS_HASH;
    public static final int MOUSE_OVER_COLOR_HASH;
    public static final int SELECTED_COLOR_HASH;
    public static final int OFFSET_HASH;
    public static final int OPPOSITE_SCROLL_BAR_POSITION_HASH;
    public static final int SCROLL_BAR_HASH;
    public static final int CAN_SELECT_NULL_HASH;
    public static final int SCROLL_BAR_BEHAVIOUR_HASH;
    public static final int SELECTED_HASH;
    public static final int SELECTED_VALUE_HASH;
    public static final int SELECTIONABLE_HASH;
    public static final int SELECTION_TOGGLABLE_HASH;
    public static final int SHOW_ONE_MORE_HASH;
    public static final int WISHED_MIN_SIZE_HASH;
    public static final int ENABLE_DND_HASH;
    public static final int LIST_LAYOUT_MODE_HASH;
    public static final int ISO_COLUMN_COUNT_HASH;
    public static final int ISO_POSITIVE_FACTOR_HASH;
    public static final int SCROLL_MODE_HASH;
    public static final int SCROLL_ON_MOUSE_WHEEL_HASH;
    
    public List() {
        this(false);
    }
    
    public List(final boolean horizontal) {
        super();
        this.m_currentColumnCount = -1.0f;
        this.m_currentRowCount = -1.0f;
        this.m_cellSize = new Dimension();
        this.m_enableDND = true;
        this.m_minDisplayedCells = 1;
        this.m_idealSizeMaxRows = -1;
        this.m_idealSizeMaxColumns = -1;
        this.m_idealSizeMinRows = -1;
        this.m_idealSizeMinColumns = -1;
        this.m_listLayoutMode = ListLayoutMode.DEFAULT;
        this.m_isoColumnCount = 1;
        this.m_isoPositiveFactor = true;
        this.m_selectionable = true;
        this.m_canSelectNull = true;
        this.m_scrollBarBehaviour = ScrollBar.ScrollBarBehaviour.WHEN_NEEDED;
        this.m_selectedOffset = -1;
        this.m_rendererManager = new ItemRendererManager();
        this.m_scrollOnMouseWheel = true;
        this.m_collectionContentLoadedListeners = new ArrayList<CollectionContentLoadedListener>();
        this.m_collectionOffsetListeners = new ArrayList<CollectionOffsetListener>();
        this.m_horizontal = horizontal;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        super.add(e);
        if (e instanceof ItemRenderer) {
            this.m_rendererManager.addRenderer((ItemRenderer)e);
        }
    }
    
    @Override
    protected void addInnerMeshes() {
        if (this.m_selectedMesh != null && this.m_selectedRenderable != null) {
            this.m_entity.addChild(this.m_selectedMesh.getEntity());
        }
        if (this.m_mouseOverRenderable != null && this.m_mouseOverMesh != null) {
            this.m_entity.addChild(this.m_mouseOverMesh.getEntity());
        }
        super.addInnerMeshes();
    }
    
    @Override
    public void addedToWidgetTree() {
        this.m_scrollBar.addedToWidgetTree();
        super.addedToWidgetTree();
        this.addEventListener(Events.SLIDER_MOVED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final SliderMovedEvent e = (SliderMovedEvent)event;
                final Slider slider = List.this.m_scrollBar.getSlider();
                if (e.getCurrentTarget() == slider || e.getTarget() == slider) {
                    List.this.setListOffset(List.this.sliderValueToOffset(e.getValue()));
                }
                return false;
            }
        }, false);
        this.addEventListener(Events.MOUSE_WHEELED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final MouseEvent e = (MouseEvent)event;
                return List.this.mouseWheel(e);
            }
        }, false);
    }
    
    @Override
    public void removedFromWidgetTree() {
        for (final RenderableContainer renderable : this.m_renderables) {
            renderable.removedFromWidgetTree();
        }
        this.m_scrollBar.removedFromWidgetTree();
        super.removedFromWidgetTree();
    }
    
    public void addListContentListener(final CollectionContentLoadedListener collectionContentLoadedListener) {
        if (collectionContentLoadedListener != null && !this.m_collectionContentLoadedListeners.contains(collectionContentLoadedListener)) {
            this.m_collectionContentLoadedListeners.add(collectionContentLoadedListener);
        }
    }
    
    public void removeListContentLoadListener(final CollectionContentLoadedListener collectionContentLoadedListener) {
        this.m_collectionContentLoadedListeners.remove(collectionContentLoadedListener);
    }
    
    public void addOffsetListener(final CollectionOffsetListener l) {
        if (!this.m_collectionOffsetListeners.contains(l)) {
            this.m_collectionOffsetListeners.add(l);
        }
    }
    
    public void removeCollectionOffsetListener(final CollectionOffsetListener l) {
        this.m_collectionOffsetListeners.remove(l);
    }
    
    public boolean getEnableDND() {
        return this.m_enableDND;
    }
    
    public void setEnableDND(final boolean enableDND) {
        if (this.m_enableDND != enableDND) {
            this.m_enableDND = enableDND;
            for (int i = this.m_renderables.size() - 1; i >= 0; --i) {
                this.m_renderables.get(i).setEnableDND(enableDND);
            }
        }
    }
    
    public boolean isCanSelectNull() {
        return this.m_canSelectNull;
    }
    
    public void setCanSelectNull(final boolean canSelectNull) {
        this.m_canSelectNull = canSelectNull;
    }
    
    public boolean getScrollOnMouseWheel() {
        return this.m_scrollOnMouseWheel;
    }
    
    public void setScrollOnMouseWheel(final boolean scrollOnMouseWheel) {
        this.m_scrollOnMouseWheel = scrollOnMouseWheel;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        for (int i = this.m_renderables.size() - 1; i >= 0; --i) {
            this.m_renderables.get(i).setEnabled(enabled);
        }
    }
    
    @Override
    public void setNetEnabled(final boolean netEnabled) {
        super.setNetEnabled(netEnabled);
        for (int i = this.m_renderables.size() - 1; i >= 0; --i) {
            this.m_renderables.get(i).setNetEnabled(netEnabled);
        }
    }
    
    @Override
    public Widget getWidget(int x, int y) {
        if (this.m_unloading || !this.m_visible || !this.getAppearance().insideInsets(x, y) || MasterRootContainer.getInstance().isMovePointMode()) {
            return null;
        }
        Widget ret = null;
        Widget found = null;
        x -= this.getAppearance().getLeftInset();
        y -= this.getAppearance().getBottomInset();
        for (final Widget w : this.m_renderables) {
            if (!w.isUnloading()) {
                ret = w.getWidget(x - w.getX(), y - w.getY());
                if (ret == null) {
                    continue;
                }
                found = ret;
            }
        }
        if (this.m_displayScrollbar && !this.m_scrollBar.isUnloading()) {
            ret = this.m_scrollBar.getWidget(x - this.m_scrollBar.getX(), y - this.m_scrollBar.getY());
        }
        if (ret != null) {
            found = ret;
        }
        return found;
    }
    
    public boolean mouseWheel(final MouseEvent e) {
        if (this.m_scrollOnMouseWheel) {
            final float offset = this.m_offset;
            this.setOffset(this.m_offset + e.getRotations());
            return this.m_offset != offset;
        }
        return false;
    }
    
    public void setOffset(final float offset) {
        this.m_scrollBar.getSlider().setValue(this.offsetToSliderValue(offset));
    }
    
    public float getOffset() {
        return this.m_offset;
    }
    
    private float offsetToSliderValue(float offset) {
        if (offset < 0.0f) {
            offset = 0.0f;
        }
        if (this.m_currentRowCount == -1.0f || this.m_currentColumnCount == -1.0f) {
            return 1.0f;
        }
        if (this.m_horizontal) {
            final float count = this.getPotentialColumnCount(this.m_currentRowCount) - this.m_currentColumnCount + this.m_showOneMore;
            if (offset > count + 1.0f) {
                offset = count + 1.0f;
            }
            return (count == 0.0f) ? 0.0f : (offset / count);
        }
        final float count = this.getPotentialRowCount(this.m_currentColumnCount) - this.m_currentRowCount + this.m_showOneMore;
        if (offset > count + 1.0f) {
            offset = count + 1.0f;
        }
        return (count == 0.0f) ? 0.0f : (1.0f - offset / count);
    }
    
    private float sliderValueToOffset(final float value) {
        float count;
        float offset;
        if (this.m_horizontal) {
            count = this.getPotentialColumnCount(this.m_currentRowCount) - this.m_currentColumnCount + this.m_showOneMore;
            offset = count * value;
        }
        else {
            count = this.getPotentialRowCount(this.m_currentColumnCount) - this.m_currentRowCount + this.m_showOneMore;
            offset = count * (1.0f - value);
        }
        if (offset < 0.0f) {
            offset = 0.0f;
        }
        else if (offset > count + 1.0f) {
            offset = count + 1.0f;
        }
        return offset;
    }
    
    private int getPotentialRowCount(final float columnCount) {
        final ArrayList<Object> items = this.getItems();
        if (items == null) {
            return 0;
        }
        return (int)Math.ceil(items.size() / columnCount);
    }
    
    private int getPotentialColumnCount(final float rowCount) {
        final ArrayList<Object> items = this.getItems();
        if (items == null) {
            return 0;
        }
        return (int)Math.ceil(items.size() / rowCount);
    }
    
    public void setListOffset(final float offset) {
        this.setListOffset(offset, true);
    }
    
    public void setListOffset(final float offset, final boolean layout) {
        final float oldOffset = this.m_offset;
        this.m_offset = offset;
        final int valueDiff = (int)((Math.floor(this.m_offset) - Math.floor(oldOffset)) * (this.m_horizontal ? this.m_currentRowCount : this.m_currentColumnCount));
        final boolean valueChanged = valueDiff != 0;
        final float diff = offset - oldOffset;
        if (valueChanged) {
            if (diff > 0.0f) {
                for (int i = 0; i < valueDiff; ++i) {
                    this.m_renderables.add(this.m_renderables.remove(0));
                }
            }
            else if (diff < 0.0f) {
                for (int i = 0; i < -valueDiff; ++i) {
                    this.m_renderables.add(0, this.m_renderables.remove(this.m_renderables.size() - 1));
                }
            }
        }
        if (layout) {
            this.getListLayout().layoutContainer(false);
        }
        if (valueChanged && layout) {
            this.updateValues(valueDiff);
        }
        this.updateSelectedAppearance();
        for (int i = 0, size = this.m_collectionOffsetListeners.size(); i < size; ++i) {
            this.m_collectionOffsetListeners.get(i).onOffsetChanged(offset);
        }
    }
    
    protected void updateValues() {
        this.updateValues(0);
    }
    
    private void updateValues(final int skip) {
        if (this.m_beingLayouted || this.m_renderables == null) {
            return;
        }
        this.m_selectedRenderable = null;
        boolean isSelectedDisplayed = false;
        int offset;
        if (this.m_horizontal) {
            offset = MathHelper.fastFloor(this.m_offset) * MathHelper.fastCeil(this.m_currentRowCount);
        }
        else {
            offset = MathHelper.fastFloor(this.m_offset) * MathHelper.fastCeil(this.m_currentColumnCount);
        }
        for (int j = 0; j < this.m_renderables.size(); ++j) {
            final int realOffset = j + offset;
            final RenderableContainer renderable = this.m_renderables.get(j);
            if (j >= 0) {
                if (j < this.m_renderables.size()) {
                    if (this.m_items != null && realOffset >= 0 && realOffset < this.m_items.size() && realOffset == this.m_selectedOffset && !isSelectedDisplayed) {
                        isSelectedDisplayed = true;
                        this.m_selectedRenderable = renderable;
                        break;
                    }
                }
            }
        }
        int i = 0;
        int size = this.m_renderables.size();
        if (skip > 0) {
            i = size - skip;
        }
        else if (skip < 0) {
            size = -skip;
        }
        while (i < size) {
            if (i >= 0) {
                if (i < this.m_renderables.size()) {
                    final int realOffset2 = i + offset;
                    final RenderableContainer renderable2 = this.m_renderables.get(i);
                    renderable2.setContentProperty(this.m_contentProperty + "#" + realOffset2, this.m_contentPropertyElementMap);
                    this.m_scrollMode.setRenderableContent(renderable2, this.m_items, realOffset2);
                }
            }
            ++i;
        }
        if (!isSelectedDisplayed) {
            this.m_selectedRenderable = null;
        }
        this.updateSelectedAppearance();
    }
    
    public ListLayoutMode getListLayoutMode() {
        return this.m_listLayoutMode;
    }
    
    public void setListLayoutMode(final ListLayoutMode listLayoutMode) {
        if (this.m_listLayoutMode == listLayoutMode) {
            return;
        }
        this.m_listLayoutMode = listLayoutMode;
        AbstractLayoutManager lm = null;
        switch (this.m_listLayoutMode) {
            case DEFAULT: {
                lm = new DefaultListLayout();
                break;
            }
            case ISOMETRIC: {
                lm = new IsometricListLayout();
                break;
            }
            case DIAGONAL: {
                lm = new CharacterCreationBreedListLayout();
                break;
            }
        }
        lm.onCheckOut();
        this.add(lm);
    }
    
    public int getIsoColumnCount() {
        return this.m_isoColumnCount;
    }
    
    public void setIsoColumnCount(final int isoColumnCount) {
        this.m_isoColumnCount = isoColumnCount;
    }
    
    public boolean getIsoPositiveFactor() {
        return this.m_isoPositiveFactor;
    }
    
    public void setIsoPositiveFactor(final boolean isoPositiveFactor) {
        this.m_isoPositiveFactor = isoPositiveFactor;
    }
    
    public void setListFilter(final ListFilter filter) {
        this.m_filter = filter;
    }
    
    public ListFilter getListFilter() {
        return this.m_filter;
    }
    
    public void setSelectionTogglable(final boolean togglable) {
        this.m_selectionTogglable = togglable;
    }
    
    public boolean getSelectionTogglable() {
        return this.m_selectionTogglable;
    }
    
    public boolean isUsePositionTween() {
        return this.m_usePositionTween;
    }
    
    public void setSelectionable(final boolean selectionable) {
        this.m_selectionable = selectionable;
    }
    
    public boolean getSelectionable() {
        return this.m_selectionable;
    }
    
    public void setHorizontal(final boolean horizontal) {
        this.m_horizontal = horizontal;
        this.m_scrollBar.setHorizontal(horizontal);
        this.setOffset(this.m_offset);
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public boolean getHorizontal() {
        return this.m_horizontal;
    }
    
    public void setCellSize(final Dimension cellSize) {
        this.m_cellSize = cellSize;
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    @Override
    public String getTag() {
        return "List";
    }
    
    public boolean getAdaptCellSizeToContentSize() {
        return this.m_adaptCellSizeToContentSize;
    }
    
    public void setAdaptCellSizeToContentSize(final boolean adaptCellSizeToContentSize) {
        this.m_adaptCellSizeToContentSize = adaptCellSizeToContentSize;
    }
    
    @Override
    public void setNeedsScissor(final boolean scissor) {
        super.setNeedsScissor(scissor);
    }
    
    public ScrollBar.ScrollBarBehaviour getScrollbarBehaviour() {
        return this.m_scrollBarBehaviour;
    }
    
    public void setScrollBar(final boolean force) {
        if (force) {
            this.m_scrollBarBehaviour = ScrollBar.ScrollBarBehaviour.FORCE_DISPLAY;
        }
        else {
            this.m_scrollBarBehaviour = ScrollBar.ScrollBarBehaviour.FORCE_HIDE;
        }
    }
    
    public boolean isScrollBarDisplayed() {
        switch (this.m_scrollBarBehaviour) {
            case FORCE_DISPLAY: {
                return true;
            }
            case FORCE_HIDE: {
                return false;
            }
            default: {
                return this.m_displayScrollbar;
            }
        }
    }
    
    public void setScrollBarBehaviour(final ScrollBar.ScrollBarBehaviour behaviour) {
        this.m_scrollBarBehaviour = behaviour;
    }
    
    public Color getSelectedColor() {
        if (this.m_selectedMesh == null) {
            return null;
        }
        return this.m_selectedMesh.getColor();
    }
    
    public void setSelectedColor(final Color c) {
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
    
    public Color getMouseOverColor() {
        if (this.m_mouseOverMesh == null) {
            return null;
        }
        return this.m_mouseOverMesh.getColor();
    }
    
    public void setMouseOverColor(final Color mouseOverColor) {
        if (mouseOverColor != null) {
            if (this.m_mouseOverMesh == null) {
                (this.m_mouseOverMesh = new ListOverMesh()).onCheckOut();
                this.setNeedsToResetMeshes();
            }
            this.m_mouseOverMesh.setColor(mouseOverColor);
        }
        else {
            if (this.m_mouseOverMesh != null) {
                this.m_mouseOverMesh.onCheckIn();
            }
            this.m_mouseOverMesh = null;
            this.setNeedsToResetMeshes();
        }
    }
    
    public int getMinDisplayedCells() {
        return this.m_minDisplayedCells;
    }
    
    public void setMinDisplayedCells(final int minDisplayedCells) {
        this.m_minDisplayedCells = minDisplayedCells;
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public boolean isAutoIdealSize() {
        return this.m_autoIdealSize;
    }
    
    public void setAutoIdealSize(final boolean autoIdealSize) {
        this.m_autoIdealSize = autoIdealSize;
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public void setAutoIdealSize(final boolean autoIdealSize, final int maxRows, final int maxColumns) {
        this.m_autoIdealSize = autoIdealSize;
        this.m_idealSizeMaxRows = maxRows;
        this.m_idealSizeMaxColumns = maxColumns;
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public void setAutoIdealSize(final boolean autoIdealSize, final int maxRows, final int maxColumns, final int minRows, final int minColumns) {
        this.m_autoIdealSize = autoIdealSize;
        this.m_idealSizeMaxRows = maxRows;
        this.m_idealSizeMaxColumns = maxColumns;
        this.m_idealSizeMinRows = minRows;
        this.m_idealSizeMinColumns = minColumns;
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public int getIdealSizeMaxColumns() {
        return this.m_idealSizeMaxColumns;
    }
    
    public void setIdealSizeMaxColumns(final int idealSizeMaxColumns) {
        this.m_idealSizeMaxColumns = idealSizeMaxColumns;
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public int getIdealSizeMaxRows() {
        return this.m_idealSizeMaxRows;
    }
    
    public void setIdealSizeMaxRows(final int idealSizeMaxRows) {
        this.m_idealSizeMaxRows = idealSizeMaxRows;
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public int getIdealSizeMinColumns() {
        return this.m_idealSizeMinColumns;
    }
    
    public void setIdealSizeMinColumns(final int idealSizeMinColumns) {
        this.m_idealSizeMinColumns = idealSizeMinColumns;
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public int getIdealSizeMinRows() {
        return this.m_idealSizeMinRows;
    }
    
    public void setIdealSizeMinRows(final int idealSizeMinRows) {
        this.m_idealSizeMinRows = idealSizeMinRows;
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public void setShowOneMore(final boolean show) {
        if (show) {
            this.m_showOneMore = 1;
        }
        else {
            this.m_showOneMore = 0;
        }
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public ListScrollMode getScrollMode() {
        return this.m_scrollMode;
    }
    
    public void setScrollMode(final ListScrollMode scrollMode) {
        this.m_scrollMode = scrollMode;
    }
    
    public Alignment9 getAlign() {
        return this.m_alignment;
    }
    
    public void setAlign(final Alignment9 alignment) {
        if (this.m_horizontal) {
            if (alignment.equals(Alignment9.NORTH) || alignment.equals(Alignment9.CENTER) || alignment.equals(Alignment9.SOUTH)) {
                this.m_alignment = alignment;
            }
        }
        else if (alignment.equals(Alignment9.WEST) || alignment.equals(Alignment9.CENTER) || alignment.equals(Alignment9.EAST)) {
            this.m_alignment = alignment;
        }
    }
    
    public boolean isOppositeScrollBarPosition() {
        return this.m_oppositeScrollBarPosition;
    }
    
    public void setOppositeScrollBarPosition(final boolean oppositeScrollBarPosition) {
        this.m_oppositeScrollBarPosition = oppositeScrollBarPosition;
    }
    
    @Deprecated
    public Dimension getWishedMinSize() {
        return this.getPrefSize();
    }
    
    @Deprecated
    public void setWishedMinSize(final Dimension wishedMinSize) {
        this.setPrefSize(wishedMinSize);
    }
    
    public Dimension getIdealSize() {
        return this.getIdealSize(-1, -1);
    }
    
    public Dimension getIdealSize(final int maxRows, final int maxColumns) {
        final Dimension dim = this.getContentIdealSize(maxRows, maxColumns, -1, -1);
        int width = dim.width;
        int height = dim.height;
        width += this.m_appearance.getLeftInset() + this.m_appearance.getRightInset();
        height += this.m_appearance.getTopInset() + this.m_appearance.getBottomInset();
        return new Dimension(width, height);
    }
    
    public Dimension getContentIdealSize(final int maxRows, final int maxColumns, final int minRows, final int minColumns) {
        int width = 10;
        int height = 10;
        final int size = (this.m_items == null) ? 0 : this.m_items.size();
        final int realMinColumns = (minColumns == -1) ? Integer.MIN_VALUE : minColumns;
        final int realMaxColumns = (maxColumns == -1) ? Integer.MAX_VALUE : maxColumns;
        final int realMinRows = (minRows == -1) ? Integer.MIN_VALUE : minRows;
        final int realMaxRows = (maxRows == -1) ? Integer.MAX_VALUE : maxRows;
        int rowCount = 0;
        int colCount = 0;
        if (this.m_cellSize.getHeightPercentage() == -1.0f || this.m_cellSize.getWidthPercentage() == -1.0f) {
            if (this.m_cellSize.getWidthPercentage() != -1.0f) {
                final int realColumnCount = (int)(1.0f / this.m_cellSize.getWidthPercentage() * 100.0f);
                colCount = Math.max(realMinColumns, Math.min(realMaxColumns, realColumnCount));
                rowCount = Math.max(realMinRows, Math.min(realMaxRows, this.getPotentialRowCount(colCount)));
            }
            else if (this.m_cellSize.getHeightPercentage() != -1.0f) {
                final int realRowCount = (int)(1.0f / this.m_cellSize.getHeightPercentage() * 100.0f);
                rowCount = Math.max(realMinRows, Math.min(realMaxRows, realRowCount));
                colCount = Math.max(realMinColumns, Math.min(realMaxColumns, this.getPotentialColumnCount(rowCount)));
            }
            else {
                final boolean isColNumberConstrained = maxColumns >= 0 || minColumns >= 0;
                final boolean isRowNumberConstrained = maxRows >= 0 || minRows >= 0;
                if (isColNumberConstrained && !isRowNumberConstrained) {
                    colCount = Math.min(Math.max(maxColumns, minColumns), size);
                    rowCount = this.getPotentialRowCount(colCount);
                }
                else if (!isColNumberConstrained && isRowNumberConstrained) {
                    rowCount = Math.min(Math.max(maxRows, minRows), size);
                    colCount = this.getPotentialColumnCount(rowCount);
                }
                else if (isColNumberConstrained && isRowNumberConstrained) {
                    if (this.m_horizontal) {
                        rowCount = Math.max(realMinRows, Math.min(maxRows, this.getPotentialRowCount(Math.max(1, realMinColumns))));
                        colCount = Math.max(realMinColumns, Math.min(maxColumns, this.getPotentialColumnCount(rowCount)));
                    }
                    else {
                        colCount = Math.max(realMinColumns, Math.min(maxColumns, this.getPotentialColumnCount(Math.max(1, realMinRows))));
                        rowCount = Math.max(realMinRows, Math.min(maxRows, this.getPotentialRowCount(colCount)));
                    }
                }
                else if (this.m_horizontal) {
                    rowCount = 1;
                    colCount = size;
                }
                else {
                    colCount = 1;
                    rowCount = size;
                }
            }
            height = this.m_cellSize.height * rowCount;
            width = this.m_cellSize.width * colCount;
        }
        if (colCount * rowCount < size && this.m_scrollBarBehaviour != ScrollBar.ScrollBarBehaviour.FORCE_HIDE) {
            final Dimension prefSize = this.m_scrollBar.getPrefSize();
            if (this.m_horizontal) {
                height += prefSize.height;
            }
            else {
                width += prefSize.width;
            }
        }
        return new Dimension(width, height);
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return appearance instanceof ListAppearance;
    }
    
    @Override
    public ListAppearance getAppearance() {
        return (ListAppearance)this.m_appearance;
    }
    
    public ScrollBar getScrollBar() {
        return this.m_scrollBar;
    }
    
    @Override
    public Widget getWidgetByThemeElementName(final String themeElementName, final boolean compilationMode) {
        if ((this.m_horizontal && "horizontalScrollBar".equalsIgnoreCase(themeElementName)) || (!this.m_horizontal && "verticalScrollBar".equalsIgnoreCase(themeElementName)) || compilationMode) {
            return this.m_scrollBar;
        }
        return null;
    }
    
    public void setSelected(final Item item) {
        this.setSelectedOffset(this.m_items.indexOf(item));
    }
    
    public int getSelectedOffset() {
        return this.m_selectedOffset;
    }
    
    public void setSelectedOffset(final int offset) {
        if (offset == this.m_selectedOffset) {
            return;
        }
        final ArrayList<Object> items = this.getItems();
        final int oldOffset = this.m_selectedOffset;
        this.m_selectedOffset = offset;
        final RenderableContainer oldRenderable = this.m_selectedRenderable;
        this.m_selectedRenderable = this.getRenderableByOffset(offset);
        if (this.m_items != null && oldOffset != -1) {
            this.dispatchEvent(new ListSelectionChangedEvent(this, oldRenderable, items.get(oldOffset), false));
        }
        if (this.m_items != null && this.m_selectedOffset != -1) {
            this.dispatchEvent(new ListSelectionChangedEvent(this, this.m_selectedRenderable, items.get(this.m_selectedOffset), true));
        }
        this.updateSelectedAppearance();
    }
    
    public void setSelectedValue(final Object value) {
        final ArrayList<Object> items = this.getItems();
        if (items == null) {
            return;
        }
        int offset = -1;
        for (int i = 0; i < items.size(); ++i) {
            final Object o = items.get(i);
            if (o != null && (o == value || o.equals(value))) {
                offset = i;
                break;
            }
        }
        this.setSelectedOffset(offset);
    }
    
    public Object getSelectedValue() {
        return this.getSelectedValue(this.getItems());
    }
    
    public Object getSelectedValue(final ArrayList<Object> items) {
        if (this.m_selectedOffset < 0 || items == null || this.m_selectedOffset >= items.size()) {
            return null;
        }
        return items.get(this.m_selectedOffset);
    }
    
    @Override
    public RenderableContainer getSelected() {
        return this.m_selectedRenderable;
    }
    
    public int getSelectedOffsetByValue(final Object value) {
        int offset;
        for (offset = 0; offset < this.getItems().size() && this.getItems().get(offset) != value; ++offset) {}
        if (offset == this.getItems().size()) {
            return -1;
        }
        return offset;
    }
    
    public RenderableContainer getRenderableByOffset(final int offset) {
        int number;
        if (this.m_horizontal) {
            number = offset - (int)(Math.floor(this.m_offset) * Math.ceil(this.m_currentRowCount));
        }
        else {
            number = offset - (int)(Math.floor(this.m_offset) * Math.ceil(this.m_currentColumnCount));
        }
        if (number < 0 || number >= this.m_renderables.size()) {
            return null;
        }
        return this.m_renderables.get(number);
    }
    
    public int getOffsetByRenderable(final RenderableContainer selected) {
        if (selected == null || this.m_items == null) {
            return -1;
        }
        int offset;
        if (this.m_horizontal) {
            offset = (int)(Math.floor(this.m_offset) * Math.ceil(this.m_currentRowCount)) + this.m_renderables.indexOf(selected);
        }
        else {
            offset = (int)(Math.floor(this.m_offset) * Math.ceil(this.m_currentColumnCount)) + this.m_renderables.indexOf(selected);
        }
        if (offset >= this.m_items.size()) {
            offset = -1;
        }
        return offset;
    }
    
    @Override
    public Widget getWidget(final String type, final int index) {
        if (index >= 0 && index < this.m_renderables.size()) {
            return this.m_renderables.get(index);
        }
        return null;
    }
    
    private void updateSelectedAppearance() {
        if (this.m_selectedRenderable != null && this.m_selectedMesh != null) {
            final int x1 = MathHelper.clamp(this.m_selectedRenderable.getX(), 0, this.getWidth());
            final int y1 = MathHelper.clamp(this.m_selectedRenderable.getY(), 0, this.getHeight());
            final int x2 = MathHelper.clamp(this.m_selectedRenderable.getX() + this.m_selectedRenderable.getWidth(), 0, this.getWidth());
            final int y2 = MathHelper.clamp(this.m_selectedRenderable.getY() + this.m_selectedRenderable.getHeight(), 0, this.getHeight());
            this.m_selectedMesh.setPositionSize(x1, y1, x2 - x1, y2 - y1, this.m_appearance.getTopInset(), this.m_appearance.getBottomInset(), this.m_appearance.getLeftInset(), this.m_appearance.getRightInset());
        }
        this.setNeedsToResetMeshes();
    }
    
    private void fireSelectionChanged(final RenderableContainer selected) {
        if (selected == this.m_selectedRenderable) {
            return;
        }
        if (selected != null) {
            this.setSelectedOffset(this.getOffsetByRenderable(selected));
        }
        else {
            this.m_selectedOffset = -1;
        }
    }
    
    private float computeRowCount(final int availableHeight, final int cellHeight) {
        float rowCount;
        if (this.m_horizontal) {
            if (!this.m_alignment.equals(Alignment9.CENTER)) {
                rowCount = (float)Math.floor(availableHeight / cellHeight);
            }
            else if (this.m_items != null && this.m_items.size() > 0) {
                final int count = (int)Math.floor(availableHeight / cellHeight);
                rowCount = Math.min(this.m_items.size(), count);
            }
            else {
                rowCount = 0.0f;
            }
        }
        else {
            rowCount = availableHeight / cellHeight;
        }
        return rowCount;
    }
    
    private float computeColumnCount(final int availableWidth, final int cellWidth) {
        float columnCount;
        if (this.m_horizontal) {
            columnCount = availableWidth / cellWidth;
        }
        else if (!this.m_alignment.equals(Alignment9.CENTER)) {
            columnCount = (int)Math.floor(availableWidth / cellWidth);
        }
        else if (this.m_items != null && this.m_items.size() > 0) {
            final int count = (int)Math.floor(availableWidth / cellWidth);
            columnCount = Math.min(this.m_items.size(), count);
        }
        else {
            columnCount = 0.0f;
        }
        return columnCount;
    }
    
    private void updateScrollBarLayout() {
        if (this.m_displayScrollbar) {
            if (this.m_horizontal) {
                final float potentialColumnCount = this.getPotentialColumnCount(this.m_currentRowCount);
                if (this.m_currentRowCount + this.m_showOneMore > 0.0f && potentialColumnCount - this.m_currentColumnCount + this.m_showOneMore > 0.0f) {
                    this.m_scrollBar.setEnabled(true);
                    this.m_scrollBar.setButtonJump(1.0f / (potentialColumnCount - this.m_currentColumnCount + this.m_showOneMore));
                    this.m_scrollBar.setSliderSize(this.m_currentColumnCount / (potentialColumnCount + this.m_showOneMore));
                }
                else {
                    this.m_scrollBar.setButtonJump(0.0f);
                    this.m_scrollBar.setEnabled(false);
                }
            }
            else {
                final float potentialRowCount = this.getPotentialRowCount(this.m_currentColumnCount);
                if (this.m_currentColumnCount + this.m_showOneMore > 0.0f && potentialRowCount - this.m_currentRowCount + this.m_showOneMore > 0.0f) {
                    this.m_scrollBar.setEnabled(true);
                    this.m_scrollBar.setButtonJump(1.0f / (potentialRowCount - this.m_currentRowCount + this.m_showOneMore));
                    this.m_scrollBar.setSliderSize(this.m_currentRowCount / (potentialRowCount + this.m_showOneMore));
                }
                else {
                    this.m_scrollBar.setButtonJump(0.0f);
                    this.m_scrollBar.setEnabled(false);
                }
            }
        }
    }
    
    public void computeContent() {
        this.m_items = this.m_newItems;
        this.m_newItems = null;
        this.checkOffsetValidity();
        this.m_listContentLoaded = true;
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public ListLayout getListLayout() {
        return (ListLayout)this.m_layout;
    }
    
    public boolean isHorizontal() {
        return this.m_horizontal;
    }
    
    public ArrayList<Object> getItems() {
        if (this.m_newItems != null) {
            return this.m_newItems;
        }
        return this.m_items;
    }
    
    @Override
    public void setContentProperty(final String propertyName, final ElementMap map) {
        this.m_contentProperty = propertyName;
        this.m_contentPropertyElementMap = map;
    }
    
    public void setContent(final Object[] content) {
        if (this.m_isATemplate) {
            return;
        }
        final int oldSelectedOffset = this.m_selectedOffset;
        final Object oldSelectedValue = this.getSelectedValue();
        this.m_newItems = new ArrayList<Object>();
        if (content != null) {
            for (int i = 0; i < content.length; ++i) {
                if (this.m_filter == null || this.m_filter.accept(content[i])) {
                    this.m_newItems.add(content[i]);
                }
            }
        }
        this.m_selectedOffset = this.getSelectedOffsetByValue(oldSelectedValue);
        if (this.m_selectedOffset == -1 && oldSelectedOffset != -1) {
            this.dispatchEvent(new ListSelectionChangedEvent(this, null, oldSelectedValue, false));
            this.dispatchEvent(new ListSelectionChangedEvent(this, null, null, true));
        }
        this.setOffset(0.0f);
        this.m_mouseOverRenderable = null;
        this.setNeedsToPreProcess();
        this.setNeedsToResetMeshes();
    }
    
    public void setContent(final Iterable content) {
        if (this.m_isATemplate) {
            return;
        }
        final int oldSelectedOffset = this.m_selectedOffset;
        final Object oldSelectedValue = this.getSelectedValue();
        this.m_newItems = new ArrayList<Object>();
        if (content != null) {
            final Iterator it = content.iterator();
            while (it != null && it.hasNext()) {
                final Object o = it.next();
                if (this.m_filter != null && !this.m_filter.accept(o)) {
                    continue;
                }
                this.m_newItems.add(o);
            }
        }
        this.m_selectedOffset = this.getSelectedOffsetByValue(oldSelectedValue);
        if (this.m_selectedOffset == -1 && oldSelectedOffset != -1) {
            this.dispatchEvent(new ListSelectionChangedEvent(this, null, oldSelectedValue, false));
            this.dispatchEvent(new ListSelectionChangedEvent(this, null, null, true));
        }
        this.setOffset(0.0f);
        this.m_mouseOverRenderable = null;
        this.setNeedsToPreProcess();
        this.setNeedsToResetMeshes();
    }
    
    private void checkOffsetValidity() {
        this.m_offset = this.m_scrollMode.validateOffset(this.m_offset, this.m_items, this.m_horizontal, this.m_currentColumnCount, this.m_currentRowCount, this.m_showOneMore);
    }
    
    @Override
    public int getTableIndex(final RenderableContainer container) {
        return this.m_renderables.indexOf(container) + (int)Math.floor(this.m_offset);
    }
    
    @Override
    public int getItemIndex(final Object value) {
        int i = 0;
        if (this.m_items != null) {
            for (final Object v : this.m_items) {
                if (v == value) {
                    return i;
                }
                ++i;
            }
        }
        return -1;
    }
    
    @Override
    public ArrayList<RenderableContainer> getRenderables() {
        return this.m_renderables;
    }
    
    public Dimension getCellSize() {
        return this.m_cellSize;
    }
    
    private void addAndUpdateOffset(final Object item, final int position) {
        this.m_items.add(position, item);
    }
    
    @Override
    public void addValue(final Object value) {
        if (this.m_items == null) {
            this.m_items = new ArrayList<Object>();
        }
        this.addAndUpdateOffset(value, this.m_items.size());
        this.m_selectedOffset = this.m_items.size() - 1;
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
        this.updateSelectedAppearance();
    }
    
    @Override
    public boolean addValue(final int position, final Object value) {
        if (this.m_items == null) {
            this.m_items = new ArrayList<Object>();
        }
        if (position >= 0 || position <= this.m_items.size()) {
            this.addAndUpdateOffset(value, position);
            this.m_selectedOffset = position;
            this.m_listIsDirty = true;
            this.setNeedsToPreProcess();
            this.updateSelectedAppearance();
            return true;
        }
        return false;
    }
    
    @Override
    public void addValue(final Object oldItem, final Object newItem) {
        boolean isSet = false;
        if (this.m_items != null) {
            for (int i = 0; i < this.m_items.size(); ++i) {
                if (oldItem == this.m_items.get(i)) {
                    this.addAndUpdateOffset(newItem, i);
                    this.m_selectedOffset = i;
                    isSet = true;
                    break;
                }
            }
            if (isSet) {
                this.m_listIsDirty = true;
                this.setNeedsToPreProcess();
                this.updateSelectedAppearance();
            }
        }
    }
    
    public void moveValuesToIndices(final ArrayList<ObjectPair<Object, Integer>> list1, final ArrayList<ObjectPair<Object, Integer>> list2) {
        final ArrayList<RenderableContainer> renderables = new ArrayList<RenderableContainer>(this.m_renderables.size());
        final ArrayList<Object> items = new ArrayList<Object>(this.m_items.size());
        renderables.addAll(this.m_renderables);
        items.addAll(this.m_items);
        final int TIME = 400;
        for (int i = 0; i < list1.size(); ++i) {
            final Object from = list1.get(i).getFirst();
            final int indexTo = list1.get(i).getSecond();
            final int indexFrom = this.m_items.indexOf(from);
            final RenderableContainer rcFrom = this.getRenderableByOffset(indexFrom);
            if (rcFrom != null) {
                final int rcFromIndex = this.m_renderables.indexOf(rcFrom);
                final int rcToIndex = rcFromIndex - indexFrom + indexTo;
                if (rcFromIndex != rcToIndex) {
                    rcFrom.removeTweensOfType(ScissorTween.class);
                    rcFrom.removeTweensOfType(PositionTween.class);
                    rcFrom.addTween(new ScissorTween(null, new Rectangle(0, this.m_cellSize.height, this.m_cellSize.width, 0), rcFrom, false, 0, 400, TweenFunction.PROGRESSIVE));
                    final Point newPos = this.m_renderablePositions.get(rcToIndex);
                    rcFrom.addTween(new PositionTween(rcFrom.getX(), rcFrom.getY(), newPos.x, newPos.y, rcFrom, 400, 0, TweenFunction.LINEAR));
                    rcFrom.addTween(new ScissorTween(new Rectangle(0, this.m_cellSize.height, this.m_cellSize.width, 0), new Rectangle(0, 0, this.m_cellSize.width, this.m_cellSize.height), rcFrom, true, 400, 400, TweenFunction.PROGRESSIVE));
                }
                if (rcToIndex >= 0 && rcToIndex < this.m_renderables.size()) {
                    renderables.set(rcToIndex, rcFrom);
                }
            }
            items.set(indexTo, from);
        }
        for (int i = 0; i < list2.size(); ++i) {
            final Object from = list2.get(i).getFirst();
            final int indexTo = list2.get(i).getSecond();
            final int indexFrom = this.m_items.indexOf(from);
            if (indexFrom != indexTo) {
                final RenderableContainer rc = this.getRenderableByOffset(indexFrom);
                if (rc != null) {
                    final int rcFromIndex = this.m_renderables.indexOf(rc);
                    final int rcToIndex = rcFromIndex - indexFrom + indexTo;
                    rc.removeTweensOfType(PositionTween.class);
                    final Point newPos = this.m_renderablePositions.get(rcToIndex);
                    rc.addTween(new PositionTween(rc.getX(), rc.getY(), newPos.x, newPos.y, rc, 400, 400, TweenFunction.PROGRESSIVE));
                    if (rcToIndex >= 0 && rcToIndex < this.m_renderables.size()) {
                        renderables.set(rcToIndex, rc);
                    }
                }
                items.set(indexTo, from);
            }
        }
        this.m_renderables = renderables;
        this.m_items = items;
    }
    
    public void moveValueToIndex(final Object from, final int indexTo) {
        this.moveValueToIndex(from, indexTo, false);
    }
    
    public void moveValueToIndex(final Object from, final int indexTo, final boolean tween) {
        final int indexFrom = this.m_items.indexOf(from);
        if (indexTo < 0 || indexTo >= this.m_items.size() || indexFrom == indexTo) {
            return;
        }
        if (tween) {
            final RenderableContainer rcFrom = this.getRenderableByOffset(indexFrom);
            if (rcFrom != null) {
                final int rcFromIdx = this.m_renderables.indexOf(rcFrom);
                Point lastPosition = rcFrom.m_position;
                final int endIdx = Math.max(0, rcFromIdx - indexFrom + indexTo);
                if (indexFrom < indexTo) {
                    for (int i = rcFromIdx + 1; i <= endIdx; ++i) {
                        final RenderableContainer rc = this.m_renderables.get(i);
                        rc.addTween(new PositionTween(rc.getX(), rc.getY(), lastPosition.x, lastPosition.y, rc, 700, 300, TweenFunction.PROGRESSIVE));
                        lastPosition = rc.m_position;
                    }
                }
                else {
                    for (int i = rcFromIdx - 1; i >= endIdx; --i) {
                        final RenderableContainer rc = this.m_renderables.get(i);
                        rc.addTween(new PositionTween(rc.getX(), rc.getY(), lastPosition.x, lastPosition.y, rc, 700, 300, TweenFunction.PROGRESSIVE));
                        lastPosition = rc.m_position;
                    }
                }
                rcFrom.addTween(new ScissorTween(null, new Rectangle(0, this.m_cellSize.height, this.m_cellSize.width, 0), rcFrom, false, 0, 700, TweenFunction.PROGRESSIVE));
                rcFrom.addTween(new PositionTween(rcFrom.getX(), rcFrom.getY(), lastPosition.x, lastPosition.y, rcFrom, 700, 0, TweenFunction.LINEAR));
                rcFrom.addTween(new ScissorTween(new Rectangle(0, this.m_cellSize.height, this.m_cellSize.width, 0), new Rectangle(0, 0, this.m_cellSize.width, this.m_cellSize.height), rcFrom, true, 700, 700, TweenFunction.PROGRESSIVE));
                this.m_renderables.add(endIdx, this.m_renderables.remove(rcFromIdx));
            }
        }
        this.m_items.add(indexTo, this.m_items.remove(indexFrom));
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
        this.updateSelectedAppearance();
    }
    
    public void moveValue(final Object from, final Object to) {
        final int indexFrom = this.m_items.indexOf(from);
        final int indexTo = this.m_items.indexOf(to);
        if (indexFrom == -1 || indexTo == -1) {
            return;
        }
        final int startIndex = (int)(Math.floor(this.m_offset) * (this.m_horizontal ? this.m_currentRowCount : this.m_currentColumnCount));
        final int endIndex = (int)(Math.floor(this.m_offset + (this.m_horizontal ? this.m_currentColumnCount : this.m_currentRowCount)) * (this.m_horizontal ? this.m_currentRowCount : this.m_currentColumnCount));
        final boolean fromIsVisible = indexFrom >= startIndex && indexFrom <= endIndex;
        final boolean toIsVisible = indexTo >= startIndex && indexTo <= endIndex;
        if (fromIsVisible && toIsVisible) {
            final RenderableContainer rFrom = this.getRenderableByOffset(indexFrom);
            final RenderableContainer rTo = this.getRenderableByOffset(indexTo);
            rFrom.addTween(new ScissorTween(null, new Rectangle(rFrom.getX(), this.m_cellSize.height + rFrom.getY(), this.m_cellSize.width, 0), rFrom, false, 0, 250, TweenFunction.LINEAR));
            rFrom.addTween(new PositionTween(rFrom.getX(), rFrom.getY(), rTo.getX(), rTo.getY(), rFrom, 250, 0, TweenFunction.LINEAR));
            rFrom.addTween(new ScissorTween(new Rectangle(rFrom.getX(), this.m_cellSize.height + rFrom.getY(), this.m_cellSize.width, 0), new Rectangle(rFrom.getX(), rFrom.getY(), this.m_cellSize.width, this.m_cellSize.height), rFrom, true, 250, 250, TweenFunction.LINEAR));
            rTo.addTween(new ScissorTween(null, new Rectangle(rTo.getX(), this.m_cellSize.height + rTo.getY(), this.m_cellSize.width, 0), rTo, false, 0, 250, TweenFunction.LINEAR));
            rTo.addTween(new PositionTween(rTo.getX(), rTo.getY(), rFrom.getX(), rFrom.getY(), rTo, 250, 0, TweenFunction.LINEAR));
            rTo.addTween(new ScissorTween(new Rectangle(rTo.getX(), this.m_cellSize.height + rTo.getY(), this.m_cellSize.width, 0), new Rectangle(rTo.getX(), rTo.getY(), this.m_cellSize.width, this.m_cellSize.height), rTo, true, 250, 250, TweenFunction.LINEAR));
        }
        else if (fromIsVisible || toIsVisible) {
            if (!fromIsVisible) {
                final RenderableContainer rTo2 = this.getRenderableByOffset(indexTo);
                MessageScheduler.getInstance().addClock(new MessageHandler() {
                    @Override
                    public boolean onMessage(final Message message) {
                        rTo2.setContent(from);
                        return false;
                    }
                    
                    @Override
                    public long getId() {
                        return 1L;
                    }
                    
                    @Override
                    public void setId(final long id) {
                    }
                }, 250L, 0, 1);
                rTo2.addTween(new ScissorTween(null, new Rectangle(rTo2.getX(), this.m_cellSize.height + rTo2.getY(), this.m_cellSize.width, 0), rTo2, false, 0, 250, TweenFunction.PROGRESSIVE));
                rTo2.addTween(new ScissorTween(new Rectangle(rTo2.getX(), this.m_cellSize.height + rTo2.getY(), this.m_cellSize.width, 0), new Rectangle(rTo2.getX(), rTo2.getY(), this.m_cellSize.width, this.m_cellSize.height), rTo2, true, 250, 250, TweenFunction.PROGRESSIVE));
            }
            else {
                final RenderableContainer rFrom = this.getRenderableByOffset(indexFrom);
                MessageScheduler.getInstance().addClock(new MessageHandler() {
                    @Override
                    public boolean onMessage(final Message message) {
                        rFrom.setContent(to);
                        return false;
                    }
                    
                    @Override
                    public long getId() {
                        return 1L;
                    }
                    
                    @Override
                    public void setId(final long id) {
                    }
                }, 250L, 0, 1);
                rFrom.addTween(new ScissorTween(null, new Rectangle(rFrom.getX(), this.m_cellSize.height + rFrom.getY(), this.m_cellSize.width, 0), rFrom, false, 0, 250, TweenFunction.PROGRESSIVE));
                rFrom.addTween(new ScissorTween(new Rectangle(rFrom.getX(), this.m_cellSize.height + rFrom.getY(), this.m_cellSize.width, 0), new Rectangle(rFrom.getX(), rFrom.getY(), this.m_cellSize.width, this.m_cellSize.height), rFrom, true, 250, 250, TweenFunction.PROGRESSIVE));
            }
        }
        this.m_items.set(indexFrom, to);
        this.m_items.set(indexTo, from);
    }
    
    @Override
    public void removeValue(final Object value) {
        this.m_items.remove(value);
        this.m_listIsDirty = true;
        this.setNeedsToPreProcess();
        this.updateSelectedAppearance();
    }
    
    @Override
    public boolean replaceValue(final Object oldItem, final Object newItem) {
        boolean isSet = false;
        if (this.m_items != null) {
            int i;
            for (i = 0; i < this.m_items.size(); ++i) {
                if (oldItem == this.m_items.get(i)) {
                    this.m_items.set(i, newItem);
                    isSet = true;
                    break;
                }
            }
            if (isSet) {
                this.m_selectedOffset = i;
                this.m_listIsDirty = true;
                this.setNeedsToPreProcess();
                this.updateSelectedAppearance();
            }
            return isSet;
        }
        return false;
    }
    
    @Override
    public Object getValue(final int i) {
        if (this.m_items != null) {
            return this.m_items.get(i);
        }
        return null;
    }
    
    @Override
    public int size() {
        final ArrayList<Object> items = this.getItems();
        if (items != null) {
            return items.size();
        }
        return 0;
    }
    
    @Override
    protected void processEventForSound(final Event e, final boolean up) {
        if (!e.isSoundConsumed() && (e.getType() == Events.ITEM_CLICK || e.getType() == Events.ITEM_DOUBLE_CLICK)) {
            e.setSoundConsumed(true);
            XulorSoundManager.getInstance().click();
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_selectedRenderable = null;
        this.m_renderables.clear();
        this.m_renderablePositions.clear();
        this.m_items = null;
        this.m_newItems = null;
        this.m_filter = null;
        this.m_contentProperty = null;
        this.m_contentPropertyElementMap = null;
        this.m_rendererManager = null;
        if (this.m_mouseOverMesh != null) {
            this.m_mouseOverMesh.onCheckIn();
            this.m_mouseOverMesh = null;
        }
        if (this.m_selectedMesh != null) {
            this.m_selectedMesh.onCheckIn();
            this.m_selectedMesh = null;
        }
        this.m_collectionContentLoadedListeners.clear();
        this.m_collectionOffsetListeners.clear();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_nonBlocking = false;
        this.m_listContentLoaded = false;
        final ListAppearance app = new ListAppearance();
        app.onCheckOut();
        app.setWidget(this);
        this.add(app);
        final DefaultListLayout ll = new DefaultListLayout();
        ll.onCheckOut();
        this.add(ll);
        (this.m_scrollBar = new ScrollBar()).onCheckOut();
        this.m_scrollBar.setCanBeCloned(false);
        this.m_scrollBar.setHorizontal(this.m_horizontal);
        this.add(this.m_scrollBar);
        this.m_scrollMode = ListScrollMode.DEFAULT;
        if (!this.m_horizontal) {
            this.m_scrollBar.getSlider().setValue(1.0f);
            this.m_alignment = Alignment9.NORTH;
        }
        else {
            this.m_alignment = Alignment9.WEST;
        }
        this.m_selectedOffset = -1;
        this.m_renderables = new ArrayList<RenderableContainer>();
        this.m_renderablePositions = new ArrayList<Point>();
        this.m_enableDND = true;
        this.m_scrollOnMouseWheel = true;
        this.m_needsScissor = true;
        this.m_canSelectNull = true;
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_newItems != null) {
            this.computeContent();
            this.m_needToUpdateValues = true;
            this.setNeedsToPostProcess();
        }
        if (this.m_listIsDirty) {
            super.invalidateMinSize();
            this.invalidate();
            this.m_listIsDirty = false;
        }
        return ret;
    }
    
    @Override
    public void invalidateMinSize() {
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        final boolean ret = super.postProcess(deltaTime);
        if (this.m_needToUpdateValues) {
            this.updateValues();
            this.m_needToUpdateValues = false;
        }
        if (this.m_listContentLoaded) {
            for (int i = this.m_collectionContentLoadedListeners.size() - 1; i >= 0; --i) {
                this.m_collectionContentLoadedListeners.get(i).onContentLoaded();
            }
            this.m_listContentLoaded = false;
        }
        return ret;
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        final List l = (List)c;
        super.copyElement(c);
        l.setAlign(this.m_alignment);
        l.setAutoIdealSize(this.m_autoIdealSize, this.m_idealSizeMaxRows, this.m_idealSizeMaxColumns, this.m_idealSizeMinRows, this.m_idealSizeMinColumns);
        l.setCellSize((Dimension)this.m_cellSize.clone());
        l.setHorizontal(this.m_horizontal);
        l.setMinDisplayedCells(this.m_minDisplayedCells);
        l.setMouseOverColor(this.getMouseOverColor());
        l.setSelectedColor(this.getSelectedColor());
        l.setOffset(this.m_offset);
        l.setOppositeScrollBarPosition(this.m_oppositeScrollBarPosition);
        l.setShowOneMore(this.m_showOneMore > 0);
        l.setScrollBarBehaviour(this.m_scrollBarBehaviour);
        l.setSelectionTogglable(this.m_selectionTogglable);
        l.setSelectionable(this.m_selectionable);
        l.setEnableDND(this.m_enableDND);
        l.setListFilter(this.m_filter);
        l.setScrollMode(this.m_scrollMode);
        l.setListLayoutMode(this.m_listLayoutMode);
        l.setIsoColumnCount(this.m_isoColumnCount);
        l.setIsoPositiveFactor(true);
        l.setScrollOnMouseWheel(this.m_scrollOnMouseWheel);
        l.m_styleIsDirty = true;
        l.m_canSelectNull = this.m_canSelectNull;
        l.setNeedsToPreProcess();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == List.ALIGN_HASH) {
            this.setAlign(Alignment9.value(value));
        }
        else if (hash == List.ADAPT_CELL_SIZE_TO_CONTENT_SIZE_HASH) {
            this.setAdaptCellSizeToContentSize(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == List.AUTO_IDEAL_SIZE_HASH) {
            this.setAutoIdealSize(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == List.CELL_SIZE_HASH) {
            this.setCellSize(cl.convertToDimension(value));
        }
        else if (hash == List.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == List.IDEAL_SIZE_MAX_COLUMNS_HASH) {
            this.setIdealSizeMaxColumns(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.IDEAL_SIZE_MAX_ROWS_HASH) {
            this.setIdealSizeMaxRows(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.IDEAL_SIZE_MIN_COLUMNS_HASH) {
            this.setIdealSizeMinColumns(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.IDEAL_SIZE_MIN_ROWS_HASH) {
            this.setIdealSizeMinRows(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.LIST_OFFSET_HASH) {
            this.setListOffset(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.MIN_DISPLAYED_CELLS_HASH) {
            this.setMinDisplayedCells(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.SELECTED_COLOR_HASH) {
            this.setSelectedColor(cl.convertToColor(value));
        }
        else if (hash == List.MOUSE_OVER_COLOR_HASH) {
            this.setMouseOverColor(cl.convertToColor(value));
        }
        else if (hash == List.OFFSET_HASH) {
            this.setOffset(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.OPPOSITE_SCROLL_BAR_POSITION_HASH) {
            this.setOppositeScrollBarPosition(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == List.SCROLL_BAR_HASH) {
            this.setScrollBar(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == List.SCROLL_BAR_BEHAVIOUR_HASH) {
            this.setScrollBarBehaviour(ScrollBar.ScrollBarBehaviour.value(value));
        }
        else if (hash == List.SELECTIONABLE_HASH) {
            this.setSelectionable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == List.SELECTION_TOGGLABLE_HASH) {
            this.setSelectionTogglable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == List.SHOW_ONE_MORE_HASH) {
            this.setShowOneMore(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == List.WISHED_MIN_SIZE_HASH) {
            this.setPrefSize(cl.convertToDimension(value));
        }
        else if (hash == List.ENABLE_DND_HASH) {
            this.setEnableDND(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == List.CAN_SELECT_NULL_HASH) {
            this.setCanSelectNull(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == List.ISO_POSITIVE_FACTOR_HASH) {
            this.setIsoPositiveFactor(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == List.ISO_COLUMN_COUNT_HASH) {
            this.setIsoColumnCount(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.SCROLL_MODE_HASH) {
            this.setScrollMode(cl.convert(ListScrollMode.class, value));
        }
        else if (hash == List.SCROLL_ON_MOUSE_WHEEL_HASH) {
            this.setScrollOnMouseWheel(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != List.LIST_LAYOUT_MODE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setListLayoutMode(cl.convert(ListLayoutMode.class, value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == List.LIST_OFFSET_HASH) {
            this.setListOffset(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.OFFSET_HASH) {
            this.setOffset(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.SCROLL_BAR_HASH) {
            this.setScrollBar(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == List.CONTENT_HASH) {
            if (value == null || value.getClass().isArray()) {
                this.setContent((Object[])value);
            }
            else {
                if (!(value instanceof Iterable)) {
                    return false;
                }
                this.setContent((Iterable)value);
            }
        }
        else if (hash == List.SELECTED_HASH) {
            this.setSelected((Item)value);
        }
        else if (hash == List.SELECTED_VALUE_HASH) {
            this.setSelectedValue(value);
        }
        else if (hash == List.IDEAL_SIZE_MAX_COLUMNS_HASH) {
            this.setIdealSizeMaxColumns(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.IDEAL_SIZE_MAX_ROWS_HASH) {
            this.setIdealSizeMaxRows(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.IDEAL_SIZE_MIN_COLUMNS_HASH) {
            this.setIdealSizeMinColumns(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.IDEAL_SIZE_MIN_ROWS_HASH) {
            this.setIdealSizeMinRows(PrimitiveConverter.getInteger(value));
        }
        else if (hash == List.LIST_FILTER_HASH) {
            this.setListFilter((ListFilter)value);
        }
        else if (hash == List.ENABLE_DND_HASH) {
            this.setEnableDND(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != List.ISO_POSITIVE_FACTOR_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setIsoPositiveFactor(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static /* synthetic */ boolean access$3676(final List x0, final int x1) {
        return x0.m_needToUpdateValues = ((byte)((x0.m_needToUpdateValues ? 1 : 0) | x1) != 0);
    }
    
    static {
        ALIGN_HASH = "align".hashCode();
        ADAPT_CELL_SIZE_TO_CONTENT_SIZE_HASH = "adaptCellSizeToContentSize".hashCode();
        AUTO_IDEAL_SIZE_HASH = "autoIdealSize".hashCode();
        CELL_SIZE_HASH = "cellSize".hashCode();
        CONTENT_HASH = "content".hashCode();
        HORIZONTAL_HASH = "horizontal".hashCode();
        IDEAL_SIZE_MAX_COLUMNS_HASH = "idealSizeMaxColumns".hashCode();
        IDEAL_SIZE_MAX_ROWS_HASH = "idealSizeMaxRows".hashCode();
        IDEAL_SIZE_MIN_COLUMNS_HASH = "idealSizeMinColumns".hashCode();
        IDEAL_SIZE_MIN_ROWS_HASH = "idealSizeMinRows".hashCode();
        LIST_FILTER_HASH = "listFilter".hashCode();
        LIST_OFFSET_HASH = "listOffset".hashCode();
        MIN_DISPLAYED_CELLS_HASH = "minDisplayedCells".hashCode();
        MOUSE_OVER_COLOR_HASH = "mouseOverColor".hashCode();
        SELECTED_COLOR_HASH = "selectedColor".hashCode();
        OFFSET_HASH = "offset".hashCode();
        OPPOSITE_SCROLL_BAR_POSITION_HASH = "oppositeScrollBarPosition".hashCode();
        SCROLL_BAR_HASH = "scrollBar".hashCode();
        CAN_SELECT_NULL_HASH = "canSelectNull".hashCode();
        SCROLL_BAR_BEHAVIOUR_HASH = "scrollBarBehaviour".hashCode();
        SELECTED_HASH = "selected".hashCode();
        SELECTED_VALUE_HASH = "selectedValue".hashCode();
        SELECTIONABLE_HASH = "selectionable".hashCode();
        SELECTION_TOGGLABLE_HASH = "selectionTogglable".hashCode();
        SHOW_ONE_MORE_HASH = "showOneMore".hashCode();
        WISHED_MIN_SIZE_HASH = "wishedMinSize".hashCode();
        ENABLE_DND_HASH = "enableDND".hashCode();
        LIST_LAYOUT_MODE_HASH = "listLayoutMode".hashCode();
        ISO_COLUMN_COUNT_HASH = "isoColumnCount".hashCode();
        ISO_POSITIVE_FACTOR_HASH = "isoPositiveFactor".hashCode();
        SCROLL_MODE_HASH = "scrollMode".hashCode();
        SCROLL_ON_MOUSE_WHEEL_HASH = "scrollOnMouseWheel".hashCode();
    }
    
    public enum ListScrollMode
    {
        DEFAULT {
            @Override
            public float validateOffset(final float offset, final ArrayList<Object> list, final boolean horizontal, final float currentColumnCount, final float currentRowCount, final int showOneMore) {
                final float displayLength = (horizontal ? currentColumnCount : currentRowCount) - showOneMore;
                final int divider = Math.max(1, (int)Math.floor(horizontal ? ((double)currentRowCount) : ((double)currentColumnCount)));
                if (list != null && list.size() / divider < displayLength) {
                    return 0.0f;
                }
                if (list != null && (int)Math.ceil(list.size() / divider) - offset < displayLength) {
                    return (float)Math.ceil(list.size() / divider) - displayLength;
                }
                if (offset < 0.0f) {
                    return 0.0f;
                }
                return offset;
            }
            
            @Override
            public void setRenderableContent(final RenderableContainer container, final ArrayList<Object> list, final int dataOffset) {
                if (list != null && dataOffset >= 0 && dataOffset < list.size()) {
                    container.setContent(list.get(dataOffset));
                }
                else {
                    container.setContent(null);
                }
            }
        }, 
        CIRCULAR {
            @Override
            public void setRenderableContent(final RenderableContainer container, final ArrayList<Object> list, int dataOffset) {
                if (list == null || list.size() == 0) {
                    container.setContent(null);
                    return;
                }
                dataOffset = (dataOffset % list.size() + list.size()) % list.size();
                container.setContent(list.get(dataOffset));
            }
            
            @Override
            public float validateOffset(final float offset, final ArrayList<Object> list, final boolean horizontal, final float currentColumnCount, final float currentRowCount, final int showOneMore) {
                return offset;
            }
        };
        
        public abstract void setRenderableContent(final RenderableContainer p0, final ArrayList<Object> p1, final int p2);
        
        public abstract float validateOffset(final float p0, final ArrayList<Object> p1, final boolean p2, final float p3, final float p4, final int p5);
    }
    
    public enum ListLayoutMode
    {
        DEFAULT, 
        ISOMETRIC, 
        DIAGONAL;
    }
    
    public class DefaultListLayout extends AbstractLayoutManager implements ListLayout
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            Dimension m_minSize;
            if (List.this.m_autoIdealSize) {
                m_minSize = List.this.getContentIdealSize(List.this.m_idealSizeMaxRows, List.this.m_idealSizeMaxColumns, List.this.m_idealSizeMinRows, List.this.m_idealSizeMinColumns);
            }
            else {
                int minWidth = 30;
                int minHeight = 30;
                final int widthMult = List.this.m_horizontal ? List.this.m_minDisplayedCells : 1;
                final int heightMult = List.this.m_horizontal ? 1 : List.this.m_minDisplayedCells;
                if (List.this.m_cellSize != null) {
                    minWidth = List.this.m_cellSize.width * widthMult;
                    minHeight = List.this.m_cellSize.height * heightMult;
                }
                if (List.this.m_scrollBarBehaviour == ScrollBar.ScrollBarBehaviour.FORCE_DISPLAY || List.this.m_displayScrollbar) {
                    if (List.this.m_horizontal) {
                        minHeight += List.this.m_scrollBar.getMinSize().height;
                    }
                    else {
                        minWidth += List.this.m_scrollBar.getMinSize().width;
                    }
                }
                m_minSize = new Dimension(minWidth, minHeight);
            }
            return m_minSize;
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            Dimension m_prefSize;
            if (List.this.m_autoIdealSize) {
                m_prefSize = List.this.getContentIdealSize(List.this.m_idealSizeMaxRows, List.this.m_idealSizeMaxColumns, List.this.m_idealSizeMinRows, List.this.m_idealSizeMinColumns);
            }
            else {
                int minWidth = 30;
                int minHeight = 30;
                final int widthMult = List.this.m_horizontal ? List.this.m_minDisplayedCells : 1;
                final int heightMult = List.this.m_horizontal ? 1 : List.this.m_minDisplayedCells;
                if (List.this.m_cellSize != null) {
                    minWidth = List.this.m_cellSize.width * widthMult;
                    minHeight = List.this.m_cellSize.height * heightMult;
                }
                if (List.this.m_scrollBarBehaviour == ScrollBar.ScrollBarBehaviour.FORCE_DISPLAY || List.this.m_displayScrollbar) {
                    if (List.this.m_horizontal) {
                        minHeight += List.this.m_scrollBar.getPrefSize().height;
                    }
                    else {
                        minWidth += List.this.m_scrollBar.getPrefSize().width;
                    }
                }
                m_prefSize = new Dimension(minWidth, minHeight);
            }
            return m_prefSize;
        }
        
        @Override
        public void layoutContainer(final boolean full) {
            if (List.this.m_rendererManager == null) {
                return;
            }
            List.this.m_beingLayouted = true;
            int availableWidth = List.this.m_appearance.getContentWidth();
            int availableHeight = List.this.m_appearance.getContentHeight();
            final ArrayList<Object> items = List.this.getItems();
            final int size = (items == null) ? 0 : items.size();
            int x = 0;
            int y = 0;
            int cellWidth;
            int cellHeight;
            if (List.this.m_adaptCellSizeToContentSize) {
                if (size > 0) {
                    if (List.this.m_horizontal) {
                        cellWidth = availableWidth / size;
                        cellHeight = availableHeight;
                    }
                    else {
                        cellHeight = availableHeight / size;
                        cellWidth = availableWidth;
                    }
                }
                else {
                    cellHeight = availableHeight;
                    cellWidth = availableWidth;
                }
            }
            else {
                cellWidth = ((List.this.m_cellSize.getWidthPercentage() != -1.0f) ? MathHelper.fastFloor(availableWidth * List.this.m_cellSize.getWidthPercentage() / 100.0f) : List.this.m_cellSize.width);
                cellHeight = ((List.this.m_cellSize.getHeightPercentage() != -1.0f) ? MathHelper.fastFloor(availableHeight * List.this.m_cellSize.getHeightPercentage() / 100.0f) : List.this.m_cellSize.height);
            }
            if (cellHeight == 0 || cellWidth == 0) {
                return;
            }
            float rowCount = List.this.computeRowCount(availableHeight, cellHeight);
            float columnCount = List.this.computeColumnCount(availableWidth, cellWidth);
            final int hgap = 0;
            final int vgap = 0;
            switch (List.this.m_scrollBarBehaviour) {
                case FORCE_HIDE: {
                    List.this.m_displayScrollbar = false;
                    break;
                }
                case FORCE_DISPLAY: {
                    List.this.m_displayScrollbar = true;
                    break;
                }
                case WHEN_NEEDED: {
                    if ((List.this.m_horizontal && List.this.getPotentialColumnCount(rowCount) + List.this.m_showOneMore > columnCount) || (!List.this.m_horizontal && List.this.getPotentialRowCount(columnCount) + List.this.m_showOneMore > rowCount)) {
                        List.this.m_displayScrollbar = true;
                        break;
                    }
                    List.this.m_displayScrollbar = false;
                    break;
                }
            }
            if (List.this.m_displayScrollbar) {
                List.this.m_scrollBar.setVisible(true);
                if (List.this.m_horizontal) {
                    final int scrollBarHeight = List.this.m_scrollBar.getPrefSize().height;
                    if (full) {
                        List.this.m_scrollBar.setSize(availableWidth, scrollBarHeight);
                    }
                    availableHeight -= scrollBarHeight;
                    if (!List.this.m_oppositeScrollBarPosition) {
                        if (full) {
                            List.this.m_scrollBar.setY(y);
                        }
                        y += scrollBarHeight;
                    }
                    else if (full) {
                        List.this.m_scrollBar.setY(y + availableHeight);
                    }
                    if (full) {
                        List.this.m_scrollBar.setX(x);
                    }
                    cellHeight = ((List.this.m_cellSize.getHeightPercentage() != -1.0f) ? MathHelper.fastRound(availableHeight * List.this.m_cellSize.getHeightPercentage() / 100.0f) : List.this.m_cellSize.height);
                    rowCount = List.this.computeRowCount(availableHeight, cellHeight);
                }
                else {
                    final int scrollBarWidth = List.this.m_scrollBar.getPrefSize().width;
                    if (full) {
                        List.this.m_scrollBar.setSize(scrollBarWidth, availableHeight);
                    }
                    availableWidth -= scrollBarWidth;
                    if (!List.this.m_oppositeScrollBarPosition) {
                        if (full) {
                            List.this.m_scrollBar.setX(x + availableWidth);
                        }
                    }
                    else {
                        if (full) {
                            List.this.m_scrollBar.setX(x);
                        }
                        x += scrollBarWidth;
                    }
                    if (full) {
                        List.this.m_scrollBar.setY(y);
                    }
                    cellWidth = ((List.this.m_cellSize.getWidthPercentage() != -1.0f) ? MathHelper.fastRound(availableWidth * List.this.m_cellSize.getWidthPercentage() / 100.0f) : List.this.m_cellSize.width);
                    columnCount = List.this.computeColumnCount(availableWidth, cellWidth);
                }
            }
            else if (full) {
                List.this.m_scrollBar.setVisible(false);
            }
            final float rem = List.this.m_offset - MathHelper.fastFloor(List.this.m_offset);
            final int hDeltaOffset = List.this.m_horizontal ? ((int)(cellWidth * rem)) : 0;
            final int vDeltaOffset = List.this.m_horizontal ? 0 : ((int)(cellHeight * rem));
            final int oldX = x;
            if (vgap == 0) {
                if (List.this.m_alignment.equals(Alignment9.SOUTH)) {
                    y += (int)(cellHeight * (rowCount - 1.0f));
                }
                else if (List.this.m_alignment.equals(Alignment9.CENTER) && List.this.m_horizontal) {
                    y += (int)(availableHeight - cellHeight - (availableHeight - cellHeight * rowCount) / 2.0f);
                }
                else {
                    y += availableHeight - cellHeight + vDeltaOffset;
                }
            }
            else {
                y += availableHeight - cellHeight + vDeltaOffset - vgap;
            }
            final int ceilRowCount = MathHelper.fastCeil(rowCount) + (List.this.m_horizontal ? 0 : 1);
            final int ceilColumnCount = MathHelper.fastCeil(columnCount) + (List.this.m_horizontal ? 1 : 0);
            final int floorRowCount = MathHelper.fastFloor(rowCount);
            final int floorColumnCount = MathHelper.fastFloor(columnCount);
            final int renderableSize = List.this.m_horizontal ? (ceilRowCount - 1 + (ceilColumnCount - 1) * floorRowCount + 1) : ((ceilRowCount - 1) * floorColumnCount + ceilColumnCount - 1 + 1);
            if (renderableSize > List.this.m_renderables.size()) {
                List.this.m_renderables.ensureCapacity(renderableSize);
                List.this.m_renderablePositions.ensureCapacity(renderableSize);
                for (int k = List.this.m_renderables.size(); k < renderableSize; ++k) {
                    final RenderableContainer container = new RenderableContainer();
                    container.onCheckOut();
                    container.setCollection(List.this);
                    container.setNonBlocking(List.this.m_nonBlocking);
                    container.setRendererManager(List.this.m_rendererManager);
                    container.setEnableDND(List.this.m_enableDND);
                    container.setEnabled(List.this.m_enabled);
                    container.setNetEnabled(List.this.m_netEnabled);
                    container.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
                        @Override
                        public boolean run(final Event event) {
                            if (List.this.m_selectionable) {
                                RenderableContainer selected;
                                if (List.this.m_selectionTogglable && List.this.m_selectedRenderable == event.getCurrentTarget()) {
                                    selected = null;
                                }
                                else {
                                    final RenderableContainer currentTarget = event.getCurrentTarget();
                                    if (List.this.m_canSelectNull || currentTarget.getItemValue() != null) {
                                        selected = currentTarget;
                                    }
                                    else {
                                        selected = null;
                                    }
                                }
                                List.this.fireSelectionChanged(selected);
                            }
                            return false;
                        }
                    }, false);
                    container.addEventListener(Events.MOUSE_ENTERED, new EventListener() {
                        @Override
                        public boolean run(final Event event) {
                            final RenderableContainer container = event.getCurrentTarget();
                            if (container.getItemValue() != null) {
                                List.this.m_mouseOverRenderable = container;
                                if (List.this.m_mouseOverMesh != null) {
                                    List.this.m_mouseOverMesh.setPositionSize(List.this.m_mouseOverRenderable.getPosition(), List.this.m_mouseOverRenderable.getSize(), List.this.m_appearance.getTotalInsets());
                                    List.this.setNeedsToResetMeshes();
                                }
                            }
                            return false;
                        }
                    }, false);
                    container.addEventListener(Events.MOUSE_EXITED, new EventListener() {
                        @Override
                        public boolean run(final Event event) {
                            List.this.m_mouseOverRenderable = null;
                            List.this.setNeedsToResetMeshes();
                            return false;
                        }
                    }, false);
                    List.this.m_renderables.add(container);
                    this.add(container);
                    List.this.m_renderablePositions.add(new Point());
                }
            }
            final int ceilCurrentRowCount = MathHelper.fastCeil(List.this.m_currentRowCount) + (List.this.m_horizontal ? 0 : 1);
            final int ceilCurrentColumnCount = MathHelper.fastCeil(List.this.m_currentColumnCount) + (List.this.m_horizontal ? 1 : 0);
            if (ceilCurrentColumnCount > ceilColumnCount || ceilCurrentRowCount > ceilRowCount) {
                for (int i = List.this.m_renderables.size() - 1; i >= 0; --i) {
                    if (i < ceilColumnCount * ceilRowCount) {
                        break;
                    }
                    final RenderableContainer container2 = List.this.m_renderables.remove(i);
                    if (container2 == List.this.m_mouseOverRenderable) {
                        List.this.m_mouseOverRenderable = null;
                    }
                    List.this.destroy(container2);
                }
            }
            for (int j = 0; j < ceilRowCount; ++j) {
                if (hgap == 0) {
                    if (List.this.m_alignment.equals(Alignment9.EAST)) {
                        x = oldX + availableWidth - (int)(cellWidth * columnCount);
                    }
                    else if (List.this.m_alignment.equals(Alignment9.CENTER)) {
                        x = oldX + (availableWidth - (int)(cellWidth * columnCount)) / 2;
                    }
                    else {
                        x = oldX - hDeltaOffset;
                    }
                }
                else {
                    x = oldX - hDeltaOffset + hgap;
                }
                for (int l = 0; l < ceilColumnCount; ++l) {
                    int offset;
                    if (List.this.m_horizontal) {
                        offset = j + l * floorRowCount;
                    }
                    else {
                        offset = j * floorColumnCount + l;
                    }
                    final RenderableContainer container3 = List.this.m_renderables.get(offset);
                    if (container3 != null) {
                        container3.setSize(cellWidth, cellHeight);
                        List.this.m_renderablePositions.get(offset).setLocation(x, y);
                        container3.setPosition(x, y, !List.this.m_usePositionTween);
                    }
                    x += cellWidth + vgap;
                }
                y -= cellHeight + vgap;
            }
            List.this.m_currentColumnCount = columnCount;
            List.this.m_currentRowCount = rowCount;
            if (full && List.this.m_displayScrollbar) {
                List.this.updateScrollBarLayout();
            }
            List.this.m_beingLayouted = false;
            final float off = List.this.m_offset;
            List.this.checkOffsetValidity();
            if (Math.abs(off - List.this.m_offset) > 1.0E-4) {
                List.this.setOffset(List.this.m_offset);
            }
            List.access$3676(List.this, full ? 1 : 0);
            this.setNeedsToPostProcess();
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            this.layoutContainer(true);
        }
    }
    
    public class IsometricListLayout extends AbstractLayoutManager implements ListLayout
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            final int itemPerColumn = (int)Math.ceil(List.this.m_items.size() / List.this.m_isoColumnCount);
            final int width = (int)Math.ceil(List.this.m_cellSize.getWidth() / 2.0 * (itemPerColumn + List.this.m_isoColumnCount));
            final int height = (int)Math.ceil(List.this.m_cellSize.getHeight() / 2.0 * (itemPerColumn + List.this.m_isoColumnCount));
            return new Dimension(width, height);
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            final int itemPerColumn = (int)Math.ceil(List.this.m_items.size() / List.this.m_isoColumnCount);
            final int width = (int)Math.ceil(List.this.m_cellSize.getWidth() / 2.0 * (itemPerColumn + List.this.m_isoColumnCount));
            final int height = (int)Math.ceil(List.this.m_cellSize.getHeight() / 2.0 * (itemPerColumn + List.this.m_isoColumnCount));
            return new Dimension(width, height);
        }
        
        @Override
        public void layoutContainer(final boolean full) {
            if (List.this.m_rendererManager == null) {
                return;
            }
            List.this.m_beingLayouted = true;
            final int availableWidth = List.this.m_appearance.getContentWidth();
            final int availableHeight = List.this.m_appearance.getContentHeight();
            final int cellWidth = List.this.m_cellSize.width;
            final int cellHeight = List.this.m_cellSize.height;
            List.this.m_scrollBar.setVisible(false);
            final int numRenderablePerColumn = (int)Math.min(Math.floor(availableWidth / cellWidth * 2.0f), Math.floor(availableHeight / cellHeight * 2.0f)) - List.this.m_isoColumnCount + 1;
            final int renderableSize = numRenderablePerColumn * List.this.m_isoColumnCount;
            if (renderableSize > List.this.m_renderables.size()) {
                List.this.m_renderables.ensureCapacity(renderableSize);
                List.this.m_renderablePositions.ensureCapacity(renderableSize);
                for (int k = List.this.m_renderables.size(); k < renderableSize; ++k) {
                    final RenderableContainer container = new RenderableContainer();
                    container.onCheckOut();
                    container.setCollection(List.this);
                    container.setNonBlocking(List.this.m_nonBlocking);
                    container.setRendererManager(List.this.m_rendererManager);
                    container.setEnableDND(List.this.m_enableDND);
                    container.setEnabled(List.this.m_enabled);
                    container.setNetEnabled(List.this.m_netEnabled);
                    container.getAppearance().setShape(WidgetShape.LOSANGE);
                    container.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
                        @Override
                        public boolean run(final Event event) {
                            if (List.this.m_selectionable) {
                                RenderableContainer selected;
                                if (List.this.m_selectionTogglable && List.this.m_selectedRenderable == event.getCurrentTarget()) {
                                    selected = null;
                                }
                                else {
                                    final RenderableContainer currentTarget = event.getCurrentTarget();
                                    if (List.this.m_canSelectNull || currentTarget.getItemValue() != null) {
                                        selected = currentTarget;
                                    }
                                    else {
                                        selected = null;
                                    }
                                }
                                List.this.fireSelectionChanged(selected);
                            }
                            return false;
                        }
                    }, false);
                    container.addEventListener(Events.MOUSE_ENTERED, new EventListener() {
                        @Override
                        public boolean run(final Event event) {
                            final RenderableContainer container = event.getCurrentTarget();
                            if (container.getItemValue() != null) {
                                List.this.m_mouseOverRenderable = container;
                                if (List.this.m_mouseOverMesh != null) {
                                    List.this.m_mouseOverMesh.setPositionSize(List.this.m_mouseOverRenderable.getPosition(), List.this.m_mouseOverRenderable.getSize(), List.this.m_appearance.getTotalInsets());
                                    List.this.setNeedsToResetMeshes();
                                }
                            }
                            return false;
                        }
                    }, false);
                    container.addEventListener(Events.MOUSE_EXITED, new EventListener() {
                        @Override
                        public boolean run(final Event event) {
                            List.this.m_mouseOverRenderable = null;
                            List.this.setNeedsToResetMeshes();
                            return false;
                        }
                    }, false);
                    List.this.m_renderables.add(container);
                    this.add(container);
                    List.this.m_renderablePositions.add(new Point());
                }
            }
            for (int k = List.this.m_renderables.size() - 1; k >= 0 && k >= renderableSize; --k) {
                final RenderableContainer container = List.this.m_renderables.remove(k);
                if (container == List.this.m_mouseOverRenderable) {
                    List.this.m_mouseOverRenderable = null;
                }
                List.this.destroy(container);
            }
            for (int numColumn = 0; numColumn < List.this.m_isoColumnCount; ++numColumn) {
                int y;
                int x;
                if (numColumn < List.this.m_isoColumnCount / 2) {
                    y = 0;
                    if (List.this.m_isoPositiveFactor) {
                        x = -cellHeight * (2 * numColumn - List.this.m_isoColumnCount + 1);
                    }
                    else {
                        x = cellHeight * (2 * numColumn - List.this.m_isoColumnCount) + availableHeight;
                    }
                }
                else {
                    x = 0;
                    if (List.this.m_isoPositiveFactor) {
                        y = (int)(cellHeight / 2.0f * (2 * numColumn - List.this.m_isoColumnCount + 1));
                    }
                    else {
                        y = (int)(cellHeight / 2.0f * (2 * numColumn - List.this.m_isoColumnCount - 1)) + availableHeight;
                    }
                }
                for (int i = 0; i < numRenderablePerColumn; ++i) {
                    final int index = (List.this.m_isoColumnCount - numColumn - 1) * numRenderablePerColumn + i;
                    final RenderableContainer rc = List.this.m_renderables.get(index);
                    rc.setPosition(x, y);
                    rc.setSize(cellWidth, cellHeight);
                    x += cellWidth / 2;
                    y += (List.this.m_isoPositiveFactor ? (cellHeight / 2) : (-cellHeight / 2));
                }
            }
            List.this.m_beingLayouted = false;
            final float off = List.this.m_offset;
            List.this.checkOffsetValidity();
            if (Math.abs(off - List.this.m_offset) > 1.0E-4) {
                List.this.setOffset(List.this.m_offset);
            }
            List.access$3676(List.this, full ? 1 : 0);
            this.setNeedsToPostProcess();
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            this.layoutContainer(true);
        }
    }
    
    public class CharacterCreationBreedListLayout extends AbstractLayoutManager implements ListLayout
    {
        private static final int COLUMN_COUNT = 4;
        private static final int ROW_COUNT = 7;
        
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            final int width = (int)Math.ceil(List.this.m_cellSize.getWidth() * 4.0);
            final int height = (int)Math.ceil(List.this.m_cellSize.getHeight() * 7.0);
            return new Dimension(width, height);
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            final int width = (int)Math.ceil(List.this.m_cellSize.getWidth() * 4.0);
            final int height = (int)Math.ceil(List.this.m_cellSize.getHeight() * 7.0);
            return new Dimension(width, height);
        }
        
        @Override
        public void layoutContainer(final boolean full) {
            if (List.this.m_rendererManager == null) {
                return;
            }
            List.this.m_beingLayouted = true;
            final int availableWidth = List.this.m_appearance.getContentWidth();
            final int availableHeight = List.this.m_appearance.getContentHeight();
            final int cellWidth = List.this.m_cellSize.width;
            final int cellHeight = List.this.m_cellSize.height;
            List.this.m_scrollBar.setVisible(false);
            final int renderableSize = List.this.m_items.size();
            if (renderableSize > List.this.m_renderables.size()) {
                List.this.m_renderables.ensureCapacity(renderableSize);
                List.this.m_renderablePositions.ensureCapacity(renderableSize);
                for (int k = List.this.m_renderables.size(); k < renderableSize; ++k) {
                    final RenderableContainer container = new RenderableContainer();
                    container.onCheckOut();
                    container.setCollection(List.this);
                    container.setNonBlocking(List.this.m_nonBlocking);
                    container.setRendererManager(List.this.m_rendererManager);
                    container.setEnableDND(List.this.m_enableDND);
                    container.setEnabled(List.this.m_enabled);
                    container.setNetEnabled(List.this.m_netEnabled);
                    container.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
                        @Override
                        public boolean run(final Event event) {
                            if (List.this.m_selectionable) {
                                RenderableContainer selected;
                                if (List.this.m_selectionTogglable && List.this.m_selectedRenderable == event.getCurrentTarget()) {
                                    selected = null;
                                }
                                else {
                                    final RenderableContainer currentTarget = event.getCurrentTarget();
                                    if (List.this.m_canSelectNull || currentTarget.getItemValue() != null) {
                                        selected = currentTarget;
                                    }
                                    else {
                                        selected = null;
                                    }
                                }
                                List.this.fireSelectionChanged(selected);
                            }
                            return false;
                        }
                    }, false);
                    container.addEventListener(Events.MOUSE_ENTERED, new EventListener() {
                        @Override
                        public boolean run(final Event event) {
                            final RenderableContainer container = event.getCurrentTarget();
                            if (container.getItemValue() != null) {
                                List.this.m_mouseOverRenderable = container;
                                if (List.this.m_mouseOverMesh != null) {
                                    List.this.m_mouseOverMesh.setPositionSize(List.this.m_mouseOverRenderable.getPosition(), List.this.m_mouseOverRenderable.getSize(), List.this.m_appearance.getTotalInsets());
                                    List.this.setNeedsToResetMeshes();
                                }
                            }
                            return false;
                        }
                    }, false);
                    container.addEventListener(Events.MOUSE_EXITED, new EventListener() {
                        @Override
                        public boolean run(final Event event) {
                            List.this.m_mouseOverRenderable = null;
                            List.this.setNeedsToResetMeshes();
                            return false;
                        }
                    }, false);
                    List.this.m_renderables.add(container);
                    this.add(container);
                    List.this.m_renderablePositions.add(new Point());
                }
            }
            for (int k = List.this.m_renderables.size() - 1; k >= 0 && k >= renderableSize; --k) {
                final RenderableContainer container = List.this.m_renderables.remove(k);
                if (container == List.this.m_mouseOverRenderable) {
                    List.this.m_mouseOverRenderable = null;
                }
                List.this.destroy(container);
            }
            int renderableIndex = 0;
            for (int numColumn = 0; numColumn < 4; ++numColumn) {
                final int x = numColumn * cellWidth;
                int renderablePerColumn;
                int y;
                if (numColumn == 0) {
                    renderablePerColumn = 4;
                    y = cellHeight * 7 - cellHeight;
                }
                else if (numColumn == 3) {
                    renderablePerColumn = 4;
                    y = cellHeight * 7 - cellHeight * 3;
                }
                else {
                    renderablePerColumn = 5;
                    y = cellHeight * 7 - cellHeight * numColumn;
                }
                for (int i = 0; i < renderablePerColumn; ++i) {
                    final RenderableContainer rc = List.this.m_renderables.get(renderableIndex++);
                    rc.setPosition(x, y);
                    rc.setSize(cellWidth, cellHeight);
                    y -= cellHeight;
                }
            }
            List.this.m_beingLayouted = false;
            final float off = List.this.m_offset;
            List.this.checkOffsetValidity();
            if (Math.abs(off - List.this.m_offset) > 1.0E-4) {
                List.this.setOffset(List.this.m_offset);
            }
            List.access$3676(List.this, full ? 1 : 0);
            this.setNeedsToPostProcess();
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            this.layoutContainer(true);
        }
    }
    
    public interface ListLayout
    {
        void layoutContainer(boolean p0);
    }
}
