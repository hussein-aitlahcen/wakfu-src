package com.ankamagames.xulor2.layout;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class TableLayoutData extends AbstractLayoutData
{
    public static final String TAG = "tld";
    private int m_row;
    private int m_column;
    private Alignment9 m_horizontalAlign;
    private Alignment9 m_verticalAlign;
    public static final int ROW_HASH;
    public static final int COLUMN_HASH;
    public static final int HORIZONTAL_ALIGN_HASH;
    public static final int VERTICAL_ALIGN_HASH;
    
    public TableLayoutData() {
        super();
        this.m_horizontalAlign = null;
        this.m_verticalAlign = null;
    }
    
    public int getRow() {
        return this.m_row;
    }
    
    public void setRow(final int row) {
        this.m_row = row;
    }
    
    public int getColumn() {
        return this.m_column;
    }
    
    public void setColumn(final int column) {
        this.m_column = column;
    }
    
    public Alignment9 getHorizontalAlign() {
        return this.m_horizontalAlign;
    }
    
    public void setHorizontalAlign(final Alignment9 horizontalAlign) {
        this.m_horizontalAlign = horizontalAlign;
    }
    
    public Alignment9 getVerticalAlign() {
        return this.m_verticalAlign;
    }
    
    public void setVerticalAlign(final Alignment9 verticalAlign) {
        this.m_verticalAlign = verticalAlign;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        final TableLayoutData data = (TableLayoutData)source;
        data.setRow(this.m_row);
        data.setColumn(this.m_column);
        data.setHorizontalAlign(this.m_horizontalAlign);
        data.setVerticalAlign(this.m_verticalAlign);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_horizontalAlign = null;
        this.m_verticalAlign = null;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == TableLayoutData.ROW_HASH) {
            this.setRow(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TableLayoutData.COLUMN_HASH) {
            this.setColumn(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TableLayoutData.HORIZONTAL_ALIGN_HASH) {
            this.setHorizontalAlign(Alignment9.value(value));
        }
        else {
            if (hash != TableLayoutData.VERTICAL_ALIGN_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setVerticalAlign(Alignment9.value(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        ROW_HASH = "row".hashCode();
        COLUMN_HASH = "column".hashCode();
        HORIZONTAL_ALIGN_HASH = "horizontalAlign".hashCode();
        VERTICAL_ALIGN_HASH = "verticalAlign".hashCode();
    }
}
