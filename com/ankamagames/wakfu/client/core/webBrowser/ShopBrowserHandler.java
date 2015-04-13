package com.ankamagames.wakfu.client.core.webBrowser;

import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gift.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class ShopBrowserHandler implements BrowserEventHandler
{
    private Object m_lock;
    private final String m_url;
    private String m_token;
    
    public ShopBrowserHandler(final String url) {
        super();
        this.m_lock = new Object();
        this.m_url = url;
    }
    
    public void onTokenReceived(final String token, final SWFBrowser browser) {
        synchronized (this.m_lock) {
            this.m_token = token;
            WakfuSWT.runAsync(new Runnable() {
                @Override
                public void run() {
                    ShopBrowserHandler.this.m_token = token;
                    browser.loadUrl();
                }
            });
        }
    }
    
    @Override
    public void start(final SWFBrowser browser) {
        SWFWrapper.INSTANCE.displayComponent();
    }
    
    @Override
    public void onLoad() {
    }
    
    @Override
    public void clean() {
        final GiftInventoryRequestMessage netMessage = new GiftInventoryRequestMessage();
        netMessage.setLocale(WakfuTranslator.getInstance().getLanguage().getActualLocale());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public String getUrl() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.m_url);
        sb.append('?');
        sb.append("key=").append(this.m_token);
        return sb.toString();
    }
    
    @Override
    public boolean invokeFunction(final String functionName, final Object[] params) {
        return false;
    }
}
