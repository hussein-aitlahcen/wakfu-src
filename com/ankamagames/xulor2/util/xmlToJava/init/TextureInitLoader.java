package com.ankamagames.xulor2.util.xmlToJava.init;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.xmlToJava.*;

public class TextureInitLoader implements InitLoader
{
    private static final Logger m_logger;
    private String m_id;
    private String m_path;
    private boolean m_permanent;
    private boolean m_init;
    
    public TextureInitLoader(final String id, final String path) {
        super();
        this.m_init = false;
        this.m_id = id;
        this.m_path = path;
        this.m_permanent = false;
        this.m_init = true;
    }
    
    public TextureInitLoader(final String id, final String path, final boolean permanent) {
        super();
        this.m_init = false;
        this.m_id = id;
        this.m_path = path;
        this.m_permanent = permanent;
        this.m_init = true;
    }
    
    public TextureInitLoader(final DocumentEntry entry) {
        super();
        this.m_init = false;
        if (!entry.getName().equalsIgnoreCase("texture") || entry.getParameterByName("path") == null || entry.getParameterByName("id") == null) {
            return;
        }
        try {
            if (entry.getParameterByName("path") != null) {
                this.m_path = entry.getParameterByName("path").getStringValue();
                final DocumentEntry permanentEntry = entry.getParameterByName("permanent");
                if (permanentEntry != null) {
                    this.m_permanent = permanentEntry.getBooleanValue();
                }
                this.m_id = entry.getParameterByName("id").getStringValue();
                this.m_init = true;
            }
        }
        catch (Exception e) {
            TextureInitLoader.m_logger.error((Object)"Impossible de cr\u00e9er l'instance de texture", (Throwable)e);
        }
    }
    
    @Override
    public void init(final DocumentParser doc) {
        if (this.m_init) {
            doc.loadTexture(this.m_id, this.m_path, this.m_permanent);
        }
    }
    
    @Override
    public void addToDocument(final ThemeInitClassDocument doc) {
        if (!this.m_init) {
            return;
        }
        final String docVarName = doc.getDocumentParserVarName();
        doc.addGeneratedCommandLine(new ClassMethodCall(null, "loadTexture", docVarName, new String[] { "\"" + this.m_id + "\"", "\"" + this.m_path + "\"", String.valueOf(this.m_permanent) }));
    }
    
    @Override
    public boolean isInitialized() {
        return this.m_init;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TextureInitLoader.class);
    }
}
