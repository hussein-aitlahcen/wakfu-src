package com.ankamagames.framework.net.soap.data;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.document.*;
import org.jetbrains.annotations.*;
import java.util.*;

public final class DataFactory
{
    private static final Logger m_logger;
    private static final String XSI_NIL = "xsi:nil";
    private static final String XSI_TYPE = "xsi:type";
    static final String KEY_CHILD_TAG = "key";
    static final String VALUE_CHILD_TAG = "value";
    static final String ITEM_CHILD_TAG = "item";
    
    public static Data parseData(final DocumentEntry node) throws IllegalArgumentException {
        final DocumentEntry nilParam = node.getParameterByName("xsi:nil");
        if (nilParam != null && nilParam.getBooleanValue()) {
            return NilData.VALUE;
        }
        final DocumentEntry typeEntry = node.getParameterByName("xsi:type");
        if (typeEntry != null) {
            final DataType type = DataType.getType(typeEntry.getStringValue());
            if (type == null) {
                throw new IllegalArgumentException("La r\u00e9ponse contient un type invalide");
            }
            switch (type) {
                case BOOLEAN: {
                    return parseBooleanNode(node);
                }
                case INT: {
                    return parseIntNode(node);
                }
                case LONG: {
                    return parseLongNode(node);
                }
                case STRING: {
                    return parseStringNode(node);
                }
                case MAP: {
                    return parseMapNode(node);
                }
                case ARRAY: {
                    return parseArrayNode(node);
                }
                case FLOAT: {
                    return parseFloatNode(node);
                }
            }
        }
        throw new IllegalArgumentException("La r\u00e9ponse ne contient pas de type");
    }
    
    @Nullable
    public static BooleanData parseBooleanNode(final DocumentEntry node) {
        final String value = node.getStringValue();
        if (value != null) {
            return booleanFromString(value);
        }
        final DocumentEntry textEntry = node.getChildByName("#text");
        if (textEntry != null) {
            return booleanFromString(textEntry.getStringValue());
        }
        return null;
    }
    
    private static BooleanData booleanFromString(final String value) {
        return new BooleanData(Boolean.parseBoolean(value));
    }
    
    @Nullable
    public static IntData parseIntNode(final DocumentEntry node) {
        final String value = node.getStringValue();
        if (value != null) {
            return intFromString(value);
        }
        final DocumentEntry textEntry = node.getChildByName("#text");
        if (textEntry != null) {
            return intFromString(textEntry.getStringValue());
        }
        return null;
    }
    
    @Nullable
    private static IntData intFromString(final String value) {
        try {
            return new IntData(Integer.parseInt(value));
        }
        catch (NumberFormatException e) {
            DataFactory.m_logger.warn((Object)("Probl\u00e8me au parse de la valeur : " + value + ". " + e.getMessage()));
            return null;
        }
    }
    
    @Nullable
    public static LongData parseLongNode(final DocumentEntry node) {
        final String value = node.getStringValue();
        if (value != null) {
            return longFromString(value);
        }
        final DocumentEntry textEntry = node.getChildByName("#text");
        if (textEntry != null) {
            return longFromString(textEntry.getStringValue());
        }
        return null;
    }
    
    @Nullable
    private static LongData longFromString(final String value) {
        try {
            return new LongData(Long.parseLong(value));
        }
        catch (NumberFormatException e) {
            DataFactory.m_logger.warn((Object)("Probl\u00e8me au parse de la valeur : " + value + ". " + e.getMessage()));
            return null;
        }
    }
    
    @Nullable
    public static FloatData parseFloatNode(final DocumentEntry node) {
        final String value = node.getStringValue();
        if (value != null) {
            return floatFromString(value);
        }
        final DocumentEntry textEntry = node.getChildByName("#text");
        if (textEntry != null) {
            return floatFromString(textEntry.getStringValue());
        }
        return null;
    }
    
    @Nullable
    private static FloatData floatFromString(final String value) {
        try {
            return new FloatData(Float.parseFloat(value));
        }
        catch (NumberFormatException e) {
            DataFactory.m_logger.warn((Object)("Probl\u00e8me au parse de la valeur : " + value + ". " + e.getMessage()));
            return null;
        }
    }
    
    @Nullable
    public static StringData parseStringNode(final DocumentEntry node) {
        final String value = node.getStringValue();
        if (value != null) {
            return new StringData(value);
        }
        final DocumentEntry textEntry = node.getChildByName("#text");
        if (textEntry != null) {
            return new StringData(textEntry.getStringValue());
        }
        return null;
    }
    
    public static MapData parseMapNode(final DocumentEntry node) {
        final MapData mapData = new MapData();
        final ArrayList<? extends DocumentEntry> children = node.getChildren();
        for (int i = 0, size = children.size(); i < size; ++i) {
            final DocumentEntry child = (DocumentEntry)children.get(i);
            if ("item".equalsIgnoreCase(child.getName())) {
                parseItem(child, mapData);
            }
        }
        return mapData;
    }
    
    private static void parseItem(final DocumentEntry child, final MapData mapData) {
        final DocumentEntry keyChild = child.getChildByName("key");
        final DocumentEntry valueChild = child.getChildByName("value");
        if (keyChild != null && valueChild != null) {
            final Data keyData = parseData(keyChild);
            final Data valueData = parseData(valueChild);
            if (keyData != null && keyData.getDataType() != DataType.NIL) {
                mapData.putValue(keyData.getStringValue(), valueData);
            }
        }
    }
    
    public static ArrayData parseArrayNode(final DocumentEntry node) {
        final ArrayData arrayData = new ArrayData();
        final ArrayList<? extends DocumentEntry> children = node.getChildren();
        for (int i = 0, size = children.size(); i < size; ++i) {
            final DocumentEntry child = (DocumentEntry)children.get(i);
            if ("item".equalsIgnoreCase(child.getName())) {
                parseItem(child, arrayData);
            }
        }
        return arrayData;
    }
    
    private static void parseItem(final DocumentEntry child, final ArrayData arrayData) {
        final Data valueData = parseData(child);
        if (valueData != null && valueData.getDataType() != DataType.NIL) {
            arrayData.addValue(valueData);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)DataFactory.class);
    }
}
