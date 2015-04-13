package com.ankamagames.xulor2.util.xmlToJava.init;

import java.util.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.xmlToJava.*;

public class FontDefinitionInitLoader implements InitLoader
{
    private String m_name;
    private final ArrayList<FontDefinition> m_definitions;
    private boolean m_init;
    
    public FontDefinitionInitLoader(final DocumentEntry entry) {
        super();
        this.m_definitions = new ArrayList<FontDefinition>();
        this.m_init = false;
        if (!entry.getName().equalsIgnoreCase("fontDefinition") || entry.getParameterByName("name") == null) {
            return;
        }
        final DocumentEntry nameEntry = entry.getParameterByName("name");
        this.m_name = nameEntry.getStringValue();
        this.m_definitions.clear();
        final ArrayList<DocumentEntry> descs = entry.getChildrenByName("desc");
        for (int i = 0, size = descs.size(); i < size; ++i) {
            final DocumentEntry desc = descs.get(i);
            final DocumentEntry pathAttr = desc.getParameterByName("path");
            final DocumentEntry langAttr = desc.getParameterByName("lang");
            final DocumentEntry sizeAttr = desc.getParameterByName("size");
            final DocumentEntry deltaXAttr = desc.getParameterByName("deltaX");
            final DocumentEntry deltaYAttr = desc.getParameterByName("deltaY");
            if (pathAttr != null) {
                if (langAttr != null) {
                    final String path = pathAttr.getStringValue();
                    final String lang = langAttr.getStringValue();
                    final int fontSize = (sizeAttr != null) ? PrimitiveConverter.getInteger(sizeAttr.getStringValue(), 0) : 0;
                    final int deltaX = (deltaXAttr != null) ? PrimitiveConverter.getInteger(deltaXAttr.getStringValue(), 0) : 0;
                    final int deltaY = (deltaYAttr != null) ? PrimitiveConverter.getInteger(deltaYAttr.getStringValue(), 0) : 0;
                    final FontDefinition fontDefinition = new FontDefinition();
                    fontDefinition.m_lang = lang;
                    fontDefinition.m_path = path;
                    fontDefinition.m_size = fontSize;
                    fontDefinition.m_deltaX = deltaX;
                    fontDefinition.m_deltaY = deltaY;
                    this.m_definitions.add(fontDefinition);
                }
            }
        }
        this.m_init = true;
    }
    
    @Override
    public void init(final DocumentParser doc) {
        if (this.m_init) {
            for (int i = 0, size = this.m_definitions.size(); i < size; ++i) {
                final FontDefinition fontDefinition = this.m_definitions.get(i);
                doc.loadFontDefinition(this.m_name, fontDefinition.getPath(), fontDefinition.getLang(), fontDefinition.getSize(), fontDefinition.getDeltaX(), fontDefinition.getDeltaY());
            }
        }
    }
    
    @Override
    public void addToDocument(final ThemeInitClassDocument doc) {
        if (!this.m_init) {
            return;
        }
        final String docVarName = doc.getDocumentParserVarName();
        for (int i = 0, size = this.m_definitions.size(); i < size; ++i) {
            final FontDefinition fontDefinition = this.m_definitions.get(i);
            doc.addGeneratedCommandLine(new ClassMethodCall(null, "loadFontDefinition", docVarName, new String[] { "\"" + this.m_name + "\"", "\"" + fontDefinition.getPath() + "\"", "\"" + fontDefinition.getLang() + "\"", String.valueOf(fontDefinition.getSize()), String.valueOf(fontDefinition.getDeltaX()), String.valueOf(fontDefinition.getDeltaY()) }));
        }
    }
    
    @Override
    public boolean isInitialized() {
        return true;
    }
    
    private static class FontDefinition
    {
        private String m_path;
        private String m_lang;
        private int m_size;
        private int m_deltaX;
        private int m_deltaY;
        
        private String getPath() {
            return this.m_path;
        }
        
        private String getLang() {
            return this.m_lang;
        }
        
        private int getSize() {
            return this.m_size;
        }
        
        private int getDeltaX() {
            return this.m_deltaX;
        }
        
        private int getDeltaY() {
            return this.m_deltaY;
        }
    }
}
