package com.ankamagames.xulor2.layout;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class TableLayout extends AbstractLayoutManager
{
    public static final String TAG = "tl";
    private int m_rows;
    private int m_columns;
    private Alignment9 m_align;
    private boolean m_dimensionsChanged;
    private Widget[][] m_widgets;
    private int[] m_columnDimensions;
    private int[] m_rowDimensions;
    private boolean[] m_expandableHorizontaly;
    private boolean[] m_expandableVerticaly;
    private int m_numExpandableHorizontaly;
    private int m_numExpandableVerticaly;
    private int m_width;
    private int m_height;
    public static final int ROWS_HASH;
    public static final int COLUMNS_HASH;
    public static final int ALIGN_HASH;
    
    public TableLayout() {
        super();
        this.m_dimensionsChanged = true;
        this.m_numExpandableHorizontaly = 0;
        this.m_numExpandableVerticaly = 0;
        this.m_width = 0;
        this.m_height = 0;
    }
    
    @Override
    public Dimension getContentPreferedSize(final Container container) {
        this.checkForCreation(container);
        for (int x = 0; x < this.m_columns; ++x) {
            for (int y = 0; y < this.m_rows; ++y) {
                final Widget widget = this.m_widgets[x][y];
                if (widget != null) {
                    if (widget.isVisible()) {
                        final Dimension prefSize = widget.getPrefSize();
                        this.m_columnDimensions[x] = Math.max(this.m_columnDimensions[x], prefSize.width);
                        this.m_rowDimensions[y] = Math.max(this.m_rowDimensions[y], prefSize.height);
                        if (widget.isExpandable()) {
                            if (!this.m_expandableHorizontaly[x]) {
                                ++this.m_numExpandableHorizontaly;
                            }
                            if (!this.m_expandableVerticaly[y]) {
                                ++this.m_numExpandableVerticaly;
                            }
                            this.m_expandableHorizontaly[x] = true;
                            this.m_expandableVerticaly[y] = true;
                        }
                    }
                }
            }
        }
        this.m_width = 0;
        this.m_height = 0;
        for (int x = 0; x < this.m_columns; ++x) {
            this.m_width += this.m_columnDimensions[x];
        }
        for (int y2 = 0; y2 < this.m_rows; ++y2) {
            this.m_height += this.m_rowDimensions[y2];
        }
        return new Dimension(this.m_width, this.m_height);
    }
    
    @Override
    public Dimension getContentMinSize(final Container container) {
        return this.getContentPreferedSize(container);
    }
    
    @Override
    public void layoutContainer(final Container parent) {
        if (this.m_columnDimensions == null || this.m_rowDimensions == null) {
            return;
        }
        final WidgetSize[] widgetsSize = new WidgetSize[this.m_columns * this.m_rows];
        for (int col = 0; col < this.m_columns; ++col) {
            for (int row = 0; row < this.m_rows; ++row) {
                final Widget widget = this.m_widgets[col][row];
                if (widget != null) {
                    if (widget.isVisible()) {
                        widgetsSize[col * this.m_rows + row] = new WidgetSize((TableLayoutData)widget.getLayoutData(), widget.getPrefSize());
                    }
                }
            }
        }
        this.calculateWidth(parent, widgetsSize);
        this.calculateHeight(parent, widgetsSize);
        for (int col = 0; col < this.m_columns; ++col) {
            for (int row = 0; row < this.m_rows; ++row) {
                final WidgetSize widgetSize = widgetsSize[col * this.m_rows + row];
                if (widgetSize != null) {
                    final Widget widget2 = this.m_widgets[col][row];
                    widget2.setPosition(widgetSize.m_x, widgetSize.m_y);
                    widget2.setSize(widgetSize.m_width, widgetSize.m_height);
                }
            }
        }
    }
    
    private void calculateWidth(final Container parent, final WidgetSize[] widgetsSize) {
        final int availableWidth = parent.getAppearance().getContentWidth();
        int extraWidth = availableWidth - this.m_width;
        final int bonusWidth = extraWidth / MathHelper.max(this.m_numExpandableHorizontaly, 1, new int[0]);
        int numExpandableH = 0;
        int x = 0;
        for (int col = 0; col < this.m_columns; ++col) {
            int colWidth = this.m_columnDimensions[col];
            if (this.m_expandableHorizontaly[col]) {
                if (++numExpandableH == this.m_numExpandableHorizontaly) {
                    colWidth += extraWidth;
                    extraWidth = 0;
                }
                else {
                    colWidth += bonusWidth;
                    extraWidth -= bonusWidth;
                }
            }
            for (int row = 0; row < this.m_rows; ++row) {
                final WidgetSize widgetSize = widgetsSize[col * this.m_rows + row];
                if (widgetSize != null) {
                    final Alignment9 align = widgetSize.m_layoutData.getHorizontalAlign();
                    if (align != null) {
                        final Dimension prefSize = widgetSize.m_prefSize;
                        widgetSize.m_x = x + align.getX(prefSize.width, colWidth);
                        widgetSize.m_width = prefSize.width;
                    }
                    else {
                        widgetSize.m_x = x;
                        widgetSize.m_width = colWidth;
                    }
                }
            }
            x += colWidth;
        }
    }
    
    private void calculateHeight(final Container parent, final WidgetSize[] widgetsSize) {
        final int availableHeight = parent.getAppearance().getContentHeight();
        int extraHeight = availableHeight - this.m_height;
        final int bonusHeight = extraHeight / MathHelper.max(this.m_numExpandableVerticaly, 1, new int[0]);
        int numExpandableV = 0;
        int y = availableHeight;
        for (int row = 0; row < this.m_rows; ++row) {
            int rowHeight = this.m_rowDimensions[row];
            if (this.m_expandableVerticaly[row]) {
                if (++numExpandableV == this.m_numExpandableVerticaly) {
                    rowHeight += extraHeight;
                    extraHeight = 0;
                }
                else {
                    rowHeight += bonusHeight;
                    extraHeight -= bonusHeight;
                }
            }
            y -= rowHeight;
            for (int col = 0; col < this.m_columns; ++col) {
                final WidgetSize widgetSize = widgetsSize[col * this.m_rows + row];
                if (widgetSize != null) {
                    final Alignment9 align = widgetSize.m_layoutData.getVerticalAlign();
                    if (align != null) {
                        final Dimension prefSize = widgetSize.m_prefSize;
                        widgetSize.m_y = y + align.getY(prefSize.height, rowHeight);
                        widgetSize.m_height = prefSize.height;
                    }
                    else {
                        widgetSize.m_y = y;
                        widgetSize.m_height = rowHeight;
                    }
                }
            }
        }
    }
    
    private void checkForCreation(final Container parent) {
        if (this.m_dimensionsChanged) {
            this.m_widgets = new Widget[this.m_columns][];
            for (int i = 0; i < this.m_columns; ++i) {
                this.m_widgets[i] = new Widget[this.m_rows];
            }
            this.m_columnDimensions = new int[this.m_columns];
            this.m_rowDimensions = new int[this.m_rows];
            this.m_expandableHorizontaly = new boolean[this.m_columns];
            this.m_expandableVerticaly = new boolean[this.m_rows];
            this.m_dimensionsChanged = false;
        }
        this.cleanWidgetList();
        this.cleanCache();
        final ArrayList<Widget> children = parent.getWidgetChildren();
        for (int j = children.size() - 1; j >= 0; --j) {
            final Widget widget = children.get(j);
            if (widget.getLayoutData() instanceof TableLayoutData) {
                final TableLayoutData data = (TableLayoutData)widget.getLayoutData();
                if (data.getRow() < this.m_rows) {
                    if (data.getColumn() < this.m_columns) {
                        this.m_widgets[data.getColumn()][data.getRow()] = widget;
                    }
                }
            }
        }
    }
    
    private void cleanWidgetList() {
        if (this.m_widgets == null) {
            return;
        }
        for (int x = 0; x < this.m_columns; ++x) {
            for (int y = 0; y < this.m_rows; ++y) {
                this.m_widgets[x][y] = null;
            }
        }
    }
    
    private void cleanCache() {
        if (this.m_columnDimensions != null) {
            Arrays.fill(this.m_columnDimensions, 0);
        }
        if (this.m_rowDimensions != null) {
            Arrays.fill(this.m_rowDimensions, 0);
        }
        if (this.m_expandableHorizontaly != null) {
            Arrays.fill(this.m_expandableHorizontaly, false);
        }
        if (this.m_expandableVerticaly != null) {
            Arrays.fill(this.m_expandableVerticaly, false);
        }
        this.m_numExpandableHorizontaly = 0;
        this.m_numExpandableVerticaly = 0;
    }
    
    public int getRows() {
        return this.m_rows;
    }
    
    public void setRows(final int rows) {
        this.m_rows = rows;
        this.m_dimensionsChanged = true;
    }
    
    public int getColumns() {
        return this.m_columns;
    }
    
    public void setColumns(final int columns) {
        this.m_columns = columns;
        this.m_dimensionsChanged = true;
    }
    
    public Alignment9 getAlign() {
        return this.m_align;
    }
    
    public void setAlign(final Alignment9 align) {
        this.m_align = align;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        final TableLayout l = (TableLayout)source;
        l.setAlign(this.m_align);
        l.setRows(this.m_rows);
        l.setColumns(this.m_columns);
    }
    
    @Override
    public TableLayout clone() {
        final TableLayout l = new TableLayout();
        l.onCheckOut();
        this.copyElement(l);
        return l;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_dimensionsChanged = true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_widgets = null;
        this.m_columnDimensions = null;
        this.m_rowDimensions = null;
        this.m_expandableHorizontaly = null;
        this.m_expandableVerticaly = null;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == TableLayout.ROWS_HASH) {
            this.setRows(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TableLayout.COLUMNS_HASH) {
            this.setColumns(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != TableLayout.ALIGN_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setAlign(Alignment9.value(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        ROWS_HASH = "rows".hashCode();
        COLUMNS_HASH = "columns".hashCode();
        ALIGN_HASH = "align".hashCode();
    }
    
    private static class WidgetSize
    {
        final TableLayoutData m_layoutData;
        final Dimension m_prefSize;
        int m_x;
        int m_y;
        int m_width;
        int m_height;
        
        private WidgetSize(final TableLayoutData layoutData, final Dimension prefSize) {
            super();
            this.m_layoutData = layoutData;
            this.m_prefSize = prefSize;
        }
    }
}
