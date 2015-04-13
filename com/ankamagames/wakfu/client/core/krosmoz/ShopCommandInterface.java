package com.ankamagames.wakfu.client.core.krosmoz;

import com.ankamagames.wakfu.client.core.webBrowser.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.auth.*;
import com.ankamagames.wakfu.client.*;

public class ShopCommandInterface extends KrosmozGameFrame
{
    private ShopBrowserHandler m_handler;
    
    public ShopCommandInterface(final ShopBrowserHandler handler) {
        super(new MessageRunner[0]);
        this.m_handler = handler;
    }
    
    public ShopBrowserHandler getHandler() {
        return this.m_handler;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        super.onFrameAdd(frameHandler, isAboutToBeAdded);
        if (!isAboutToBeAdded) {
            ICEAuthFrame.INSTANCE.getToken(new ICEAuthListener() {
                @Override
                public void onToken(final String token) {
                    final SWFWrapper swfWrapper = ShopCommandInterface.this.getSwfWrapper();
                    final ShopBrowserHandler handler = ShopCommandInterface.this.getHandler();
                    handler.onTokenReceived(token, swfWrapper.getBrowser());
                }
                
                @Override
                public void onError() {
                }
            });
        }
    }
    
    @Override
    public void onCommandReceived(final String command, final Object... params) {
    }
}
