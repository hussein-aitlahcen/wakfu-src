package com.ankamagames.framework.net.soap;

import com.ankamagames.framework.fileFormat.xml.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.net.soap.data.*;
import java.util.regex.*;

public class SOAPElement
{
    private static final String ELEMENT_ATTRIBUTE_PREFIX = "ns1:";
    private static final Pattern NAME_PATTERN;
    private final String m_name;
    private final ArrayList<Data> m_data;
    private final Map<String, Data> m_parameters;
    
    public SOAPElement(final String name) {
        super();
        this.m_data = new ArrayList<Data>();
        this.m_parameters = new HashMap<String, Data>();
        this.m_name = name;
    }
    
    public void putParameter(final String name, final String value) {
        this.m_parameters.put(name, new StringData(value));
    }
    
    public void putParameter(final String name, final int value) {
        this.m_parameters.put(name, new IntData(value));
    }
    
    public void putParameter(final String name, final long value) {
        this.m_parameters.put(name, new LongData(value));
    }
    
    public void putParameter(final String name, final boolean value) {
        this.m_parameters.put(name, new BooleanData(value));
    }
    
    public void putParameter(final String name, final Data value) {
        this.m_parameters.put(name, value);
    }
    
    public Data getFirstData() {
        return this.m_data.isEmpty() ? null : this.m_data.get(0);
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public XMLDocumentNode toXMLDocumentNode() {
        final XMLDocumentNode node = new XMLDocumentNode("ns1:" + this.m_name, "");
        for (final Map.Entry<String, Data> entry : this.m_parameters.entrySet()) {
            final DocumentEntry child = entry.getValue().generateNode();
            child.setName(entry.getKey());
            node.addChild(child);
        }
        return node;
    }
    
    public static SOAPElement parseSOAPXMLEntry(final DocumentEntry node) throws IllegalArgumentException {
        final String attributeName = node.getName();
        final Matcher matcher = SOAPElement.NAME_PATTERN.matcher(attributeName);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Le nom de l'\u00e9l\u00e9ment ne respecte pas la norme : " + attributeName);
        }
        final SOAPElement element = new SOAPElement(matcher.group(1));
        for (final DocumentEntry child : node.getChildren()) {
            element.m_data.add(DataFactory.parseData(child));
        }
        return element;
    }
    
    @Override
    public String toString() {
        return "SOAPElement{m_name='" + this.m_name + '\'' + ", m_data=" + this.m_data + ", m_parameters=" + this.m_parameters + '}';
    }
    
    static {
        NAME_PATTERN = Pattern.compile("ns1:([A-Za-z]+)");
    }
}
