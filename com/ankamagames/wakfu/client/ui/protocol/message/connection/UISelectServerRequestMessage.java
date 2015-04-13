package com.ankamagames.wakfu.client.ui.protocol.message.connection;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UISelectServerRequestMessage extends UIMessage
{
    private String m_login;
    private String m_password;
    protected boolean m_remember;
    
    @Override
    public int getId() {
        return 16383;
    }
    
    public void setLogin(final String login) {
        this.m_login = login;
    }
    
    public void setPassword(final String password) {
        this.m_password = password;
    }
    
    public void setRemember(final Boolean remember) {
        this.m_remember = remember;
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
