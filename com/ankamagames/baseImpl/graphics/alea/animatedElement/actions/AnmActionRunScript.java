package com.ankamagames.baseImpl.graphics.alea.animatedElement.actions;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.sound.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class AnmActionRunScript extends AnmAction
{
    private static final Logger m_logger;
    private long m_scriptId;
    
    public final long getScriptId() {
        return this.m_scriptId;
    }
    
    @Override
    public final boolean run(final AnimatedObject animatedElement) {
        if (this.m_scriptId == -1L) {
            return false;
        }
        AnmActionRunScriptManager.getInstance().playAnmAction(animatedElement, this.m_scriptId);
        return false;
    }
    
    @Override
    public final void load(final byte parametersCount, final ExtendedDataInputStream bitStream) throws Exception {
        final String scriptId = bitStream.readString();
        try {
            this.m_scriptId = Long.parseLong(scriptId);
        }
        catch (NumberFormatException e) {
            AnmActionRunScript.m_logger.error((Object)("Impossible d'interpr\u00e9ter le parametre pour runScript param=" + scriptId));
            this.m_scriptId = -1L;
        }
    }
    
    @Override
    public void write(final OutputBitStream ostream) throws IOException {
        super.write(ostream);
        ostream.writeByte((byte)1);
        ostream.writeString(Long.toString(this.m_scriptId));
    }
    
    @Override
    public final AnmActionTypes getType() {
        return AnmActionTypes.RUN_SCRIPT;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnmActionRunScript)) {
            return false;
        }
        final AnmActionRunScript that = (AnmActionRunScript)o;
        return this.m_scriptId == that.m_scriptId;
    }
    
    @Override
    public final int hashCode() {
        int result = 31 * this.getType().getId();
        result = 31 * result + (int)this.m_scriptId;
        return result;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnmActionRunScript.class);
    }
}
