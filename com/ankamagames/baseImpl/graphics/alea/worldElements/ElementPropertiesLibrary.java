package com.ankamagames.baseImpl.graphics.alea.worldElements;

import java.nio.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import gnu.trove.*;

public class ElementPropertiesLibrary
{
    private static final TIntObjectHashMap<ElementProperties> m_elements;
    
    public static void add(final ElementProperties properties) {
        assert !ElementPropertiesLibrary.m_elements.containsKey(properties.getId()) : "Un \u00e9l\u00e9ment avec l'id " + properties.getId() + " existe d\u00e9j\u00e0";
        ElementPropertiesLibrary.m_elements.put(properties.getId(), properties);
    }
    
    public static void load(final String filename) throws IOException {
        final ByteBuffer buffer = ByteBuffer.wrap(ContentFileHelper.readFile(filename));
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int numElements = buffer.getInt(), i = 0; i < numElements; ++i) {
            add(new ElementProperties(buffer));
        }
        ElementPropertiesLibrary.m_elements.trimToSize();
    }
    
    public static void save(final String filename) throws IOException {
        final FileOutputStream fos = FileHelper.createFileOutputStream(filename);
        final OutputBitStream ostream = new OutputBitStream(fos);
        ostream.writeInt(ElementPropertiesLibrary.m_elements.size());
        final TIntObjectIterator<ElementProperties> iter = ElementPropertiesLibrary.m_elements.iterator();
        for (int i = ElementPropertiesLibrary.m_elements.size(); i > 0; --i) {
            iter.advance();
            iter.value().save(ostream);
        }
        ostream.close();
    }
    
    public static ElementProperties getElement(final int elementId) {
        return ElementPropertiesLibrary.m_elements.get(elementId);
    }
    
    public static void clear() {
        ElementPropertiesLibrary.m_elements.clear();
    }
    
    public static int size() {
        return ElementPropertiesLibrary.m_elements.size();
    }
    
    static {
        m_elements = new TIntObjectHashMap<ElementProperties>(20000, 1.0f);
    }
}
