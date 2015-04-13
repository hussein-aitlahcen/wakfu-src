package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item.action;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.jetbrains.annotations.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class ItemActionPlayScriptMessage extends InputOnlyProxyMessage
{
    private long m_concernedPlayerId;
    private int m_itemRefId;
    private long m_scriptId;
    @Nullable
    private String m_varName;
    private long m_varValue;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_concernedPlayerId = buffer.getLong();
        this.m_itemRefId = buffer.getInt();
        this.m_scriptId = buffer.getLong();
        final int varLength = buffer.getInt();
        if (varLength > 0) {
            final byte[] bytes = new byte[varLength];
            buffer.get(bytes);
            this.m_varName = StringUtils.fromUTF8(bytes);
            this.m_varValue = buffer.getLong();
        }
        else {
            this.m_varName = null;
            this.m_varValue = 0L;
        }
        return true;
    }
    
    public long getConcernedPlayerId() {
        return this.m_concernedPlayerId;
    }
    
    public int getItemRefId() {
        return this.m_itemRefId;
    }
    
    public long getScriptId() {
        return this.m_scriptId;
    }
    
    public long getVarValue() {
        return this.m_varValue;
    }
    
    @Nullable
    public String getVarName() {
        return this.m_varName;
    }
    
    @Override
    public int getId() {
        return 5302;
    }
}
