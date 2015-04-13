package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.gameAction;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.gameActions.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class GameActionPlayScriptMessage extends InputOnlyProxyMessage
{
    private long m_userId;
    private int m_actionId;
    private PlayScriptSource m_source;
    private final HashMap<String, Object> m_variables;
    
    public GameActionPlayScriptMessage() {
        super();
        this.m_variables = new HashMap<String, Object>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_userId = bb.getLong();
        this.m_actionId = bb.getInt();
        this.m_source = PlayScriptSource.fromId(bb.get());
        for (int varSize = bb.getInt(), i = 0; i < varSize; ++i) {
            final byte[] data = new byte[bb.getInt()];
            bb.get(data);
            this.m_variables.put(StringUtils.fromUTF8(data), bb.getLong());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 15800;
    }
    
    public long getUserId() {
        return this.m_userId;
    }
    
    public int getActionId() {
        return this.m_actionId;
    }
    
    public PlayScriptSource getSource() {
        return this.m_source;
    }
    
    public HashMap<String, Object> getVariables() {
        return this.m_variables;
    }
}
