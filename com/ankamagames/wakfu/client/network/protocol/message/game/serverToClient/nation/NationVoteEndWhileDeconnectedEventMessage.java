package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.constants.*;

public class NationVoteEndWhileDeconnectedEventMessage extends InputOnlyProxyMessage
{
    private static final Logger m_logger;
    private String m_governorName;
    int m_resultOrdinal;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_resultOrdinal = buff.getInt();
        final byte[] mayorNameData = new byte[buff.get()];
        buff.get(mayorNameData);
        this.m_governorName = StringUtils.fromUTF8(mayorNameData);
        return true;
    }
    
    public NationVoteEndWhileDisconnectedResult getResult() {
        final NationVoteEndWhileDisconnectedResult[] values = NationVoteEndWhileDisconnectedResult.values();
        for (int i = 0; i < values.length; ++i) {
            if (values[i].ordinal() == this.m_resultOrdinal) {
                return values[i];
            }
        }
        NationVoteEndWhileDeconnectedEventMessage.m_logger.error((Object)("Erreur \u00e0 lad\u00e9s\u00e9rialisation dun r\u00e9sultat de vote (alors qu'on \u00e9tait d\u00e9connect\u00e9) : r\u00e9sultat de type " + this.m_resultOrdinal + " inconnu"));
        return NationVoteEndWhileDisconnectedResult.ELECTION_MISSED;
    }
    
    public String getGovernorName() {
        return this.m_governorName;
    }
    
    @Override
    public int getId() {
        return 20010;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationVoteEndWhileDeconnectedEventMessage.class);
    }
}
