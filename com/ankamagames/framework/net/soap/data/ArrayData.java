package com.ankamagames.framework.net.soap.data;

import com.google.common.collect.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.fileFormat.xml.*;

public class ArrayData implements Data
{
    private final List<Data> m_data;
    
    public ArrayData() {
        super();
        this.m_data = new ArrayList<Data>();
    }
    
    @Override
    public DataType getDataType() {
        return DataType.ARRAY;
    }
    
    @Override
    public Object getValue() {
        return ImmutableList.copyOf((Collection)this.m_data);
    }
    
    public Data getValue(final int index) {
        return this.m_data.get(index);
    }
    
    public void addValue(final Data value) {
        this.m_data.add(value);
    }
    
    @Override
    public float getFloatValue() {
        throw new UnsupportedOperationException("getFloatValue sur ArrayData");
    }
    
    @Override
    public int getIntValue() {
        throw new UnsupportedOperationException("getIntValue sur ArrayData");
    }
    
    @Override
    public long getLongValue() {
        throw new UnsupportedOperationException("LongValue sur ArrayData");
    }
    
    @Override
    public String getStringValue() {
        throw new UnsupportedOperationException("getStringValue sur ArrayData");
    }
    
    @Override
    public boolean getBooleanValue() {
        throw new UnsupportedOperationException("getBooleanValue sur ArrayData");
    }
    
    public float getFloatValue(final int index) {
        final Data data = this.m_data.get(index);
        return (data != null) ? data.getFloatValue() : 0.0f;
    }
    
    public int getIntValue(final int index) {
        final Data data = this.m_data.get(index);
        return (data != null) ? data.getIntValue() : 0;
    }
    
    @Nullable
    public String getStringValue(final int index) {
        final Data data = this.m_data.get(index);
        return (data != null) ? data.getStringValue() : null;
    }
    
    public boolean getBooleanValue(final int index) {
        final Data data = this.m_data.get(index);
        return data != null && data.getBooleanValue();
    }
    
    public int size() {
        return this.m_data.size();
    }
    
    private static DocumentEntry createItem(final Data data) {
        final DocumentEntry item = data.generateNode();
        item.setName("item");
        return item;
    }
    
    @Override
    public DocumentEntry generateNode() {
        final DocumentEntry arrayEntry = new XMLDocumentNode(null, null);
        arrayEntry.addParameter(new XMLNodeAttribute("xsi:type", this.getDataType().getTag()));
        for (int i = 0, size = this.m_data.size(); i < size; ++i) {
            final Data data = this.m_data.get(i);
            arrayEntry.addChild(createItem(data));
        }
        return arrayEntry;
    }
    
    @Override
    public String toString() {
        return "ArrayData{m_data=" + this.m_data + '}';
    }
}
