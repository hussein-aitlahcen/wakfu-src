package com.ankamagames.xulor2.util.xmlToJava.init;

import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.xmlToJava.*;

public class CursorInitLoader implements InitLoader
{
    private int m_x;
    private int m_y;
    private CursorFactory.CursorType m_type;
    private String m_path;
    private boolean m_initialized;
    
    public CursorInitLoader(final int x, final int y, final CursorFactory.CursorType type, final String path) {
        super();
        this.m_initialized = false;
        this.m_x = x;
        this.m_y = y;
        this.m_type = type;
        this.m_path = path;
        this.m_initialized = true;
    }
    
    public CursorInitLoader(final DocumentEntry entry) throws IllegalArgumentException {
        super();
        this.m_initialized = false;
        if (!entry.getName().equalsIgnoreCase("cursor") || entry.getParameterByName("path") == null) {
            return;
        }
        final DocumentEntry pathEntry = entry.getParameterByName("path");
        if (pathEntry != null) {
            final DocumentEntry xEntry = entry.getParameterByName("x");
            final DocumentEntry yEntry = entry.getParameterByName("y");
            final DocumentEntry cursorTypeEntry = entry.getParameterByName("type");
            this.m_x = ((xEntry == null) ? 0 : xEntry.getIntValue());
            this.m_y = ((yEntry == null) ? 0 : yEntry.getIntValue());
            this.m_type = ((cursorTypeEntry == null) ? CursorFactory.CursorType.DEFAULT : CursorFactory.CursorType.valueOf(cursorTypeEntry.getStringValue().toUpperCase()));
            this.m_path = pathEntry.getStringValue();
            this.m_initialized = true;
        }
    }
    
    @Override
    public void init(final DocumentParser doc) {
        if (this.m_initialized) {
            doc.loadCursor(this.m_path, this.m_type, this.m_x, this.m_y);
        }
    }
    
    @Override
    public void addToDocument(final ThemeInitClassDocument doc) {
        if (!this.m_initialized) {
            return;
        }
        doc.addImport(CursorFactory.CursorType.class);
        final String docVarName = doc.getDocumentParserVarName();
        doc.addGeneratedCommandLine(new ClassMethodCall(null, "loadCursor", docVarName, new String[] { "\"" + this.m_path + "\"", CursorFactory.CursorType.class.getSimpleName() + "." + this.m_type.name(), String.valueOf(this.m_x), String.valueOf(this.m_y) }));
    }
    
    @Override
    public boolean isInitialized() {
        return this.m_initialized;
    }
}
