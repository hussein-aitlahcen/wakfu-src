package com.ankamagames.baseImpl.client.proxyclient.base.network.proxy;

public class ProxyAddress
{
    private final String m_host;
    private final int m_port;
    
    public ProxyAddress(final String host, final int port) {
        super();
        this.m_host = host;
        this.m_port = port;
    }
    
    public final String getHost() {
        return this.m_host;
    }
    
    public final int getPort() {
        return this.m_port;
    }
    
    @Override
    public String toString() {
        return "ProxyAddress{m_host='" + this.m_host + '\'' + ", m_port=" + this.m_port + '}';
    }
}
