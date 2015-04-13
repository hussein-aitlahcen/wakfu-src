package com.ankamagames.framework.net.soap.data;

import com.google.common.collect.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.fileFormat.xml.*;
import java.util.*;

public class MapData implements Data
{
    private final THashMap<String, Data> m_map;
    
    public MapData() {
        super();
        this.m_map = new THashMap<String, Data>();
    }
    
    @Override
    public DataType getDataType() {
        return DataType.MAP;
    }
    
    @Override
    public Object getValue() {
        return ImmutableMap.copyOf((Map)this.m_map);
    }
    
    public Data getValue(final String key) {
        return this.m_map.get(key);
    }
    
    public boolean forEach(final TObjectObjectProcedure<String, Data> procedure) {
        return this.m_map.forEachEntry(procedure);
    }
    
    public void putValue(final String key, final Data value) {
        this.m_map.put(key, value);
    }
    
    @Override
    public float getFloatValue() {
        throw new UnsupportedOperationException("getFloatValue sur MapData");
    }
    
    @Override
    public int getIntValue() {
        throw new UnsupportedOperationException("getIntValue sur MapData");
    }
    
    @Override
    public long getLongValue() {
        throw new UnsupportedOperationException("getLongValue sur MapData");
    }
    
    @Override
    public String getStringValue() {
        throw new UnsupportedOperationException("getStringValue sur MapData");
    }
    
    @Override
    public boolean getBooleanValue() {
        throw new UnsupportedOperationException("getBooleanValue sur MapData");
    }
    
    public float getFloatValue(final String key) {
        final Data data = this.m_map.get(key);
        return (data != null) ? data.getFloatValue() : 0.0f;
    }
    
    public int getIntValue(final String key) {
        final Data data = this.m_map.get(key);
        return (data != null) ? data.getIntValue() : 0;
    }
    
    @Nullable
    public String getStringValue(final String key) {
        final Data data = this.m_map.get(key);
        return (data != null) ? data.getStringValue() : null;
    }
    
    public boolean getBooleanValue(final String key) {
        final Data data = this.m_map.get(key);
        return data != null && data.getBooleanValue();
    }
    
    private static DocumentEntry createItem(final String name, final Data data) {
        final DocumentEntry item = new XMLDocumentNode("item", null);
        final Data keyData = new StringData(name);
        final DocumentEntry keyChild = keyData.generateNode();
        keyChild.setName("key");
        item.addChild(keyChild);
        final DocumentEntry dataChild = data.generateNode();
        dataChild.setName("value");
        item.addChild(dataChild);
        return item;
    }
    
    @Override
    public DocumentEntry generateNode() {
        final DocumentEntry mapEntry = new XMLDocumentNode(null, null);
        mapEntry.addParameter(new XMLNodeAttribute("xsi:type", this.getDataType().getTag()));
        for (final Map.Entry<String, Data> entry : this.m_map.entrySet()) {
            mapEntry.addChild(createItem(entry.getKey(), entry.getValue()));
        }
        return mapEntry;
    }
    
    @Override
    public String toString() {
        return "MapData{m_map=" + this.m_map + '}';
    }
}
