package com.ankamagames.baseImpl.client.proxyclient.base.network.proxy;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.net.netty.decoder.*;
import com.ankamagames.framework.kernel.core.net.netty.connection.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.*;

public class ProxyClient
{
    private static final Logger LOG;
    private WakfuMessageDecoder m_proxyDecoder;
    private SimpleObjectFactory<NetworkEntity> m_proxyEntityFactory;
    private NetworkEventsHandler m_proxyEventsHandler;
    
    public boolean connectToProxy(final String host, final int port) {
        final ClientConnection client = new ClientConnection(host, port, ProtocolAdapter.CLIENT_SERVER, this.m_proxyEntityFactory, this.m_proxyEventsHandler);
        client.addDecoder(this.m_proxyDecoder);
        try {
            return client.start(true, false);
        }
        catch (InterruptedException e) {
            ProxyClient.LOG.warn((Object)("Uable to connect to " + host + ':' + port), (Throwable)e);
            return false;
        }
    }
    
    public void setProxyEntityFactory(final SimpleObjectFactory<NetworkEntity> entityFactory) {
        this.m_proxyEntityFactory = entityFactory;
    }
    
    public void setProxyDecoder(final AbstractClientMessageDecoder messageDecoder) {
        this.m_proxyDecoder = messageDecoder;
    }
    
    public NetworkEventsHandler getProxyEventsHandler() {
        return this.m_proxyEventsHandler;
    }
    
    public void setProxyEventsHandler(final NetworkEventsHandler eventHandler) {
        this.m_proxyEventsHandler = eventHandler;
    }
    
    @Override
    public String toString() {
        return "ProxyNettyClient{m_proxyDecoder=" + this.m_proxyDecoder + ", m_proxyEntityFactory=" + this.m_proxyEntityFactory.getClass() + ", m_proxyEventsHandler=" + this.m_proxyEventsHandler + '}';
    }
    
    static {
        LOG = Logger.getLogger((Class)ProxyClient.class);
    }
}
