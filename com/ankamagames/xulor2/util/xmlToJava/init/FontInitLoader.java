package com.ankamagames.xulor2.util.xmlToJava.init;

import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.xmlToJava.*;

public class FontInitLoader implements InitLoader
{
    private String m_id;
    private String m_definition;
    private String m_font;
    private boolean m_bordered;
    private boolean m_init;
    
    public FontInitLoader(final DocumentEntry entry) {
        super();
        this.m_init = false;
        if (!entry.getName().equalsIgnoreCase("font") || entry.getParameterByName("font") == null || entry.getParameterByName("id") == null) {
            return;
        }
        this.m_bordered = false;
        if (entry.getParameterByName("bordered") != null) {
            this.m_bordered = entry.getParameterByName("bordered").getBooleanValue();
        }
        this.m_definition = entry.getParameterByName("definition").getStringValue();
        this.m_font = entry.getParameterByName("font").getStringValue();
        this.m_id = entry.getParameterByName("id").getStringValue();
        this.m_init = true;
    }
    
    @Override
    public void init(final DocumentParser doc) {
        if (this.m_init) {
            doc.loadFont(this.m_id, this.m_definition, this.m_font, this.m_bordered);
        }
    }
    
    @Override
    public void addToDocument(final ThemeInitClassDocument doc) {
        if (!this.m_init) {
            return;
        }
        final String docVarName = doc.getDocumentParserVarName();
        doc.addGeneratedCommandLine(new ClassMethodCall(null, "loadFont", docVarName, new String[] { "\"" + this.m_id + "\"", "\"" + this.m_definition + "\"", "\"" + this.m_font + "\"", String.valueOf(this.m_bordered) }));
    }
    
    @Override
    public boolean isInitialized() {
        return true;
    }
}
