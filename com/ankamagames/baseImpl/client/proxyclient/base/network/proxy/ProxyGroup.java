package com.ankamagames.baseImpl.client.proxyclient.base.network.proxy;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class ProxyGroup
{
    protected static final Logger m_logger;
    private final int m_index;
    private final String m_name;
    private final Community m_community;
    private final ArrayList<ProxyAddress> m_proxyAddresses;
    private final ArrayList<ProxyAddress> m_usableProxyAddresses;
    
    public ProxyGroup() {
        this(-1, null, null);
    }
    
    public ProxyGroup(final int index, final String name, final Community community) {
        super();
        this.m_index = index;
        this.m_name = name;
        this.m_community = community;
        this.m_proxyAddresses = new ArrayList<ProxyAddress>();
        this.m_usableProxyAddresses = new ArrayList<ProxyAddress>();
    }
    
    public int getIndex() {
        return this.m_index;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public Community getCommunity() {
        return this.m_community;
    }
    
    public ArrayList<ProxyAddress> getProxyAddresses() {
        return this.m_proxyAddresses;
    }
    
    public void addProxy(final ProxyAddress proxyAddress) {
        this.m_proxyAddresses.add(proxyAddress);
        this.m_usableProxyAddresses.add(proxyAddress);
    }
    
    @Nullable
    public ProxyAddress getFirstProxyAddress() {
        if (!this.m_usableProxyAddresses.isEmpty()) {
            return this.m_usableProxyAddresses.remove(0);
        }
        return null;
    }
    
    public boolean hasUsableProxyAdresses() {
        return !this.m_usableProxyAddresses.isEmpty();
    }
    
    public void clearRandomIterator() {
        this.m_usableProxyAddresses.clear();
        for (final ProxyAddress proxyAddress : this.m_proxyAddresses) {
            this.m_usableProxyAddresses.add(proxyAddress);
        }
    }
    
    public static ProxyGroup extractProxyGroupFromProperties(final PropertiesReaderWriter properties, final String addressesKey) {
        final ProxyGroup proxyGroup = new ProxyGroup();
        try {
            final String proxyAddresses = properties.getString(addressesKey);
            final String[] values = proxyAddresses.split(":");
            if (values.length == 2) {
                final String proxyHost = values[0];
                final String[] proxyPorts = values[1].split(";");
                for (int k = 0; k < proxyPorts.length; ++k) {
                    proxyGroup.addProxy(new ProxyAddress(proxyHost, Integer.parseInt(proxyPorts[k])));
                }
            }
        }
        catch (PropertyException e) {
            ProxyGroup.m_logger.error((Object)"Exception", (Throwable)e);
        }
        return proxyGroup;
    }
    
    @Override
    public String toString() {
        return this.m_name;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ProxyGroup.class);
    }
}
