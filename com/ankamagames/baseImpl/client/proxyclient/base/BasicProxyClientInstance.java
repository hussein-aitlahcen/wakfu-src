package com.ankamagames.baseImpl.client.proxyclient.base;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.proxy.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.*;
import com.ankamagames.framework.kernel.events.*;

public class BasicProxyClientInstance
{
    private static final Logger m_logger;
    private ProxyClient m_proxy;
    private SimpleObjectFactory<NetworkEntity> m_networkEntityFactory;
    private AbstractClientMessageDecoder m_clientMessageDecoder;
    private NetworkEventsHandler m_networkEventHandler;
    
    public void setClientMessageDecoder(final AbstractClientMessageDecoder clientMessageDecoder) {
        this.m_clientMessageDecoder = clientMessageDecoder;
    }
    
    public void setNetworkEntityFactory(final SimpleObjectFactory<NetworkEntity> networkEntityFactory) {
        this.m_networkEntityFactory = networkEntityFactory;
    }
    
    public void setNetworkEventHandler(final NetworkEventsHandler networkEventHandler) {
        this.m_networkEventHandler = networkEventHandler;
    }
    
    public ProxyClient getProxy() {
        return this.m_proxy;
    }
    
    protected void createProxyClient() {
        if (this.m_networkEntityFactory != null && this.m_clientMessageDecoder != null && this.m_networkEventHandler != null) {
            (this.m_proxy = new ProxyClient()).setProxyEntityFactory(this.m_networkEntityFactory);
            this.m_proxy.setProxyDecoder(this.m_clientMessageDecoder);
            this.m_proxy.setProxyEventsHandler(this.m_networkEventHandler);
            return;
        }
        BasicProxyClientInstance.m_logger.error((Object)"Impossible de cr\u00e9er le ProxyClient : tous ces param\u00e8tres n'ont pas \u00e9t\u00e9 d\u00e9finis");
    }
    
    static {
        m_logger = Logger.getLogger((Class)BasicProxyClientInstance.class);
    }
}
