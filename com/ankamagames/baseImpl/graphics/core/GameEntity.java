package com.ankamagames.baseImpl.graphics.core;

import com.ankamagames.baseImpl.client.proxyclient.base.core.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.framework.kernel.core.net.netty.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.proxy.*;

public abstract class GameEntity extends ProxyClientEntity
{
    private String m_login;
    private String m_password;
    private boolean m_logged;
    private String m_desiredNickname;
    private String m_securityCardQuestion;
    private String m_securityCardAnswer;
    private ProxyGroup m_proxyGroup;
    private int m_serverId;
    
    public String getLogin() {
        return this.m_login;
    }
    
    public void setLogin(final String login) {
        this.m_login = login;
    }
    
    public String getPassword() {
        return this.m_password;
    }
    
    public void setPassword(final String password) {
        this.m_password = password;
    }
    
    public String getDesiredNickname() {
        return this.m_desiredNickname;
    }
    
    public String getSecurityCardQuestion() {
        return this.m_securityCardQuestion;
    }
    
    public void setSecurityCardQuestion(final String cardQuestion) {
        this.m_securityCardQuestion = cardQuestion;
    }
    
    public String getSecurityCardAnswer() {
        return this.m_securityCardAnswer;
    }
    
    public void setSecurityCardAnswer(final String cardAnswer) {
        this.m_securityCardAnswer = cardAnswer;
    }
    
    public boolean isLogged() {
        return this.m_logged;
    }
    
    public void setLogged(final boolean logged) {
        this.m_logged = logged;
    }
    
    public ProxyGroup getProxyGroup() {
        return this.m_proxyGroup;
    }
    
    public void setProxyGroup(final ProxyGroup proxyGroup) {
        this.m_proxyGroup = proxyGroup;
    }
    
    public int getServerId() {
        return this.m_serverId;
    }
    
    public void setServerId(final int serverId) {
        this.m_serverId = serverId;
    }
    
    @Override
    public void partialCleanUp() {
        super.partialCleanUp();
        PaperMapManager.getInstance().reset();
    }
    
    @Override
    public void cleanUp() {
        super.cleanUp();
        this.m_login = null;
        this.m_password = null;
        this.m_logged = false;
        this.m_proxyGroup = null;
        this.m_desiredNickname = null;
        this.m_securityCardQuestion = null;
        this.m_securityCardAnswer = null;
    }
    
    public boolean connect(final ProxyClient proxyClient) {
        final ProxyGroup proxyGroup = this.getProxyGroup();
        if (proxyGroup != null) {
            while (proxyGroup.hasUsableProxyAdresses()) {
                final ProxyAddress proxyAddress = proxyGroup.getFirstProxyAddress();
                final boolean isLast = !proxyGroup.hasUsableProxyAdresses();
                if (proxyAddress != null) {
                    try {
                        this.setDisconnectionReason((byte)20);
                        GameEntity.m_logger.info((Object)("Connexion au proxy :" + proxyAddress.getHost() + ":" + proxyAddress.getPort()));
                        final boolean connectionEstablished = proxyClient.connectToProxy(proxyAddress.getHost(), proxyAddress.getPort());
                        if (connectionEstablished) {
                            return connectionEstablished;
                        }
                        continue;
                    }
                    catch (Exception e) {
                        GameEntity.m_logger.error((Object)"connect :", (Throwable)e);
                    }
                }
            }
        }
        this.setDisconnectionReason((byte)0);
        proxyClient.getProxyEventsHandler().onConnectionClose(null);
        this.onConnectionToProxyFaild();
        GameEntity.m_logger.error((Object)"Aucun proxy n'est disponible");
        this.setDisconnectionReason((byte)0);
        return false;
    }
    
    public void logon() {
        if (!this.m_logged && this.getNetworkEntity() != null && this.getNetworkEntity().getConnection().isConnected()) {
            this.onLogonRequest();
        }
    }
    
    public void logoff() {
        if (this.m_logged && this.getNetworkEntity() != null && this.getNetworkEntity().getConnection().isConnected()) {
            this.onLogoffRequest();
        }
    }
    
    @Deprecated
    public void gotoWorldSelection() {
        this.onGotoWorldSelectionRequest();
        GameEntity.m_logger.error((Object)"La m\u00e9thode gotoWorldSelection() ne devrait plus \u00eatre utilis\u00e9e.");
    }
    
    public void quit() {
        this.onQuitRequest();
    }
    
    protected abstract void onConnectionToProxyFaild();
    
    protected abstract void onLogonRequest();
    
    protected abstract void onLogoffRequest();
    
    protected abstract void onGotoWorldSelectionRequest();
    
    protected abstract void onQuitRequest();
}
