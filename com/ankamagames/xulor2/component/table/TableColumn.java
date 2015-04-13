package com.ankamagames.xulor2.component.table;

import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class TableColumn extends EventDispatcher
{
    public static final String TAG = "tableColumn";
    private String m_name;
    private String m_field;
    private String m_columnId;
    private int m_cellWidth;
    private int m_columnIndex;
    private boolean m_sortable;
    private ItemRendererManager m_rendererManager;
    private boolean m_visible;
    private TableColumnVisibilityListener m_visibilityListener;
    public static final int NAME_HASH;
    public static final int FIELD_HASH;
    public static final int SORTABLE_HASH;
    public static final int COLUMN_INDEX_HASH;
    public static final int COLUMN_ID_HASH;
    public static final int CELL_WIDTH_HASH;
    public static final int VISIBLE_HASH;
    
    public TableColumn() {
        super();
        this.m_rendererManager = new ItemRendererManager();
        this.m_visible = true;
    }
    
    @Override
    public void add(final EventDispatcher element) {
        super.add(element);
        if (element instanceof ItemRenderer) {
            this.m_rendererManager.addRenderer((ItemRenderer)element);
        }
    }
    
    @Override
    public String getTag() {
        return "tableColumn";
    }
    
    public ItemRendererManager getRendererManager() {
        return this.m_rendererManager;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public String getField() {
        return this.m_field;
    }
    
    public void setField(final String field) {
        this.m_field = field;
    }
    
    public boolean getSortable() {
        return this.m_sortable;
    }
    
    public void setSortable(final boolean sortable) {
        this.m_sortable = sortable;
    }
    
    public int getColumnIndex() {
        return this.m_columnIndex;
    }
    
    public void setColumnIndex(final int columnIndex) {
        this.m_columnIndex = columnIndex;
    }
    
    public String getColumnId() {
        return this.m_columnId;
    }
    
    public void setColumnId(final String columnId) {
        this.m_columnId = columnId;
    }
    
    public int getCellWidth() {
        return this.m_cellWidth;
    }
    
    public void setCellWidth(final int cellWidth) {
        this.m_cellWidth = cellWidth;
    }
    
    public void setVisible(final boolean visible) {
        final boolean changed = this.m_visible != visible;
        this.m_visible = visible;
        if (changed) {
            this.m_visibilityListener.onVisibilityChange(this, visible);
        }
    }
    
    public boolean isVisible() {
        return this.m_visible;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        final TableColumn c = (TableColumn)source;
        c.setName(this.m_name);
        c.setField(this.m_field);
        c.setSortable(this.m_sortable);
        c.setColumnIndex(this.m_columnIndex);
        c.setColumnId(this.m_columnId);
        c.setCellWidth(this.m_cellWidth);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_name = null;
        this.m_field = null;
        this.m_columnId = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_columnIndex = -1;
        this.m_cellWidth = 30;
        this.m_sortable = true;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == TableColumn.NAME_HASH) {
            this.setName(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == TableColumn.FIELD_HASH) {
            this.setField(value);
        }
        else if (hash == TableColumn.SORTABLE_HASH) {
            this.setSortable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TableColumn.COLUMN_INDEX_HASH) {
            this.setColumnIndex(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TableColumn.COLUMN_ID_HASH) {
            this.setColumnId(value);
        }
        else if (hash == TableColumn.CELL_WIDTH_HASH) {
            this.setCellWidth(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != TableColumn.VISIBLE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setVisible(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == TableColumn.NAME_HASH) {
            this.setName((String)value);
        }
        else if (hash == TableColumn.FIELD_HASH) {
            this.setField((String)value);
        }
        else if (hash == TableColumn.SORTABLE_HASH) {
            this.setSortable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TableColumn.COLUMN_INDEX_HASH) {
            this.setColumnIndex(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TableColumn.COLUMN_ID_HASH) {
            this.setColumnId((String)value);
        }
        else {
            if (hash != TableColumn.VISIBLE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setVisible(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    public void setVisibilityListener(final TableColumnVisibilityListener visibilityListener) {
        this.m_visibilityListener = visibilityListener;
    }
    
    static {
        NAME_HASH = "name".hashCode();
        FIELD_HASH = "field".hashCode();
        SORTABLE_HASH = "sortable".hashCode();
        COLUMN_INDEX_HASH = "columnIndex".hashCode();
        COLUMN_ID_HASH = "columnId".hashCode();
        CELL_WIDTH_HASH = "cellWidth".hashCode();
        VISIBLE_HASH = "visible".hashCode();
    }
}
