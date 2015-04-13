package com.ankamagames.wakfu.client.ui.protocol.message.connection;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.proxy.*;

public class UILogonRequestMessage extends UIMessage
{
    private ProxyGroup m_proxyGroup;
    private String m_login;
    private String m_password;
    private boolean m_remember;
    
    @Override
    public int getId() {
        return 16385;
    }
    
    public void setLogin(final String login) {
        this.m_login = login;
    }
    
    public void setPassword(final String password) {
        this.m_password = password;
    }
    
    public void setProxyGroup(final ProxyGroup proxyGroup) {
        this.m_proxyGroup = proxyGroup;
    }
    
    public void setRemember(final Boolean remember) {
        this.m_remember = remember;
    }
    
    public ProxyGroup getProxyGroup() {
        return this.m_proxyGroup;
    }
    
    public String getLogin() {
        return this.m_login;
    }
    
    public String getPassword() {
        return this.m_password;
    }
    
    public Boolean getRemember() {
        return this.m_remember;
    }
}
