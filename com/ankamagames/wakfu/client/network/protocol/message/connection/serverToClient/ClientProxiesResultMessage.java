package com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.dispatch.*;
import java.nio.*;

public class ClientProxiesResultMessage extends InputOnlyProxyMessage
{
    private final TIntObjectHashMap<Proxy> m_proxies;
    private final TIntObjectHashMap<WorldInfo> m_worldInfos;
    
    public ClientProxiesResultMessage() {
        super();
        this.m_proxies = new TIntObjectHashMap<Proxy>();
        this.m_worldInfos = new TIntObjectHashMap<WorldInfo>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 1, false)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        for (int proxySize = bb.getInt(), i = 0; i < proxySize; ++i) {
            final Proxy proxy = Proxy.fromBuild(bb);
            this.m_proxies.put(proxy.getId(), proxy);
        }
        for (int infoSize = bb.getInt(), j = 0; j < infoSize; ++j) {
            final WorldInfo info = WorldInfo.fromBuild(bb);
            this.m_worldInfos.put(info.getServerId(), info);
        }
        return true;
    }
    
    public TIntObjectHashMap<Proxy> getProxies() {
        return this.m_proxies;
    }
    
    public TIntObjectHashMap<WorldInfo> getWorldInfos() {
        return this.m_worldInfos;
    }
    
    @Override
    public int getId() {
        return 1036;
    }
}
