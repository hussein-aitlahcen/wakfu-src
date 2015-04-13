package com.ankamagames.xulor2.util.xmlToJava.init;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.xmlToJava.*;

public class AnimatedCursorInitLoader implements InitLoader
{
    private static final Logger m_logger;
    private int m_x;
    private int m_y;
    private CursorFactory.CursorType m_type;
    private int m_delay;
    private ArrayList<String> m_pathList;
    private boolean m_initialized;
    
    public AnimatedCursorInitLoader(final DocumentEntry entry) {
        super();
        this.m_initialized = false;
        if (!entry.getName().equalsIgnoreCase("animatedCursor")) {
            return;
        }
        try {
            final DocumentEntry xEntry = entry.getParameterByName("x");
            final DocumentEntry yEntry = entry.getParameterByName("y");
            final DocumentEntry delayEntry = entry.getParameterByName("delay");
            final DocumentEntry cursorTypeEntry = entry.getParameterByName("type");
            this.m_x = ((xEntry == null) ? 0 : xEntry.getIntValue());
            this.m_y = ((yEntry == null) ? 0 : yEntry.getIntValue());
            this.m_delay = ((delayEntry == null) ? 500 : delayEntry.getIntValue());
            this.m_type = ((cursorTypeEntry == null) ? CursorFactory.CursorType.DEFAULT : CursorFactory.CursorType.valueOf(cursorTypeEntry.getStringValue().toUpperCase()));
            final ArrayList<DocumentEntry> cursorFrames = entry.getChildrenByName("cursorFrame");
            this.m_pathList = new ArrayList<String>(cursorFrames.size());
            for (int i = 0, size = cursorFrames.size(); i < size; ++i) {
                final DocumentEntry cursorFrameEntry = cursorFrames.get(i);
                final DocumentEntry pathEntry = cursorFrameEntry.getParameterByName("path");
                this.m_pathList.add(pathEntry.getStringValue());
            }
            this.m_initialized = true;
        }
        catch (Exception e) {
            AnimatedCursorInitLoader.m_logger.warn((Object)"Probl\u00e8me \u00e0 la lecture d'un AnimatedCursor");
        }
    }
    
    public AnimatedCursorInitLoader(final int x, final int y, final CursorFactory.CursorType type, final int delay, final int cycleDelay, final ArrayList<String> path) {
        super();
        this.m_initialized = false;
        this.m_x = x;
        this.m_y = y;
        this.m_type = type;
        this.m_delay = delay;
        this.m_pathList = path;
    }
    
    @Override
    public void init(final DocumentParser doc) {
        if (this.m_initialized) {
            doc.loadAnimatedCursor(this.m_type, this.m_x, this.m_y, this.m_delay, this.m_pathList);
        }
    }
    
    public String getCommand(final DocumentVariableAccessor accessor) {
        if (!this.m_initialized) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final String listVarName = accessor.getUnusedVarName();
        sb.append(new ClassVariable(ArrayList.class, listVarName, "new ArrayList<String>()").getCommand(accessor));
        sb.append("\n");
        for (int i = 0, size = this.m_pathList.size(); i < size; ++i) {
            sb.append(new ClassMethodCall(null, "add", listVarName, new String[] { "\"" + this.m_pathList.get(i) + "\"" }).getCommand(accessor)).append("\n");
        }
        sb.append("\n");
        sb.append("InitLoaderManager.getInstance().addLoader(new AnimatedCursorInitLoader(").append(this.m_x).append(", ").append(this.m_y).append(", ").append("CursorFactory.CursorType.").append(this.m_type.name()).append(", ").append(this.m_delay).append(", ").append(listVarName).append("));");
        return sb.toString();
    }
    
    @Override
    public void addToDocument(final ThemeInitClassDocument doc) {
        if (!this.m_initialized) {
            return;
        }
        doc.addImport(ArrayList.class);
        doc.addImport(CursorFactory.CursorType.class);
        final String docVarName = doc.getDocumentParserVarName();
        final String varName = doc.getUnusedVarName();
        doc.addGeneratedCommandLine(new ClassVariable(ArrayList.class, varName, "new ArrayList<String>()"));
        for (int i = 0, size = this.m_pathList.size(); i < size; ++i) {
            doc.addGeneratedCommandLine(new ClassMethodCall(null, "add", varName, new String[] { "\"" + this.m_pathList.get(i) + "\"" }));
        }
        doc.addGeneratedCommandLine(new ClassMethodCall(null, "loadAnimatedCursor", docVarName, new String[] { CursorFactory.CursorType.class.getSimpleName() + "." + this.m_type.name(), String.valueOf(this.m_x), String.valueOf(this.m_y), String.valueOf(this.m_delay), varName }));
    }
    
    @Override
    public boolean isInitialized() {
        return this.m_initialized;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnimatedCursorInitLoader.class);
    }
}
