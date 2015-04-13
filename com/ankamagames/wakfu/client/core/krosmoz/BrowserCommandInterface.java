package com.ankamagames.wakfu.client.core.krosmoz;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.auth.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class BrowserCommandInterface extends KrosmozGameFrame
{
    private static final Logger m_logger;
    
    public BrowserCommandInterface() {
        super(new MessageRunner[] { new AuthentificationTokenMessageRunner() });
    }
    
    @Override
    public void onCommandReceived(final String command, final Object... params) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)BrowserCommandInterface.class);
    }
    
    public static class AuthentificationTokenMessageRunner extends KrozmozGameMessageRunner<AuthentificationTokenResultMessage>
    {
        private static final String RECEIVE_ARENA_SSO = "receiveArenaSSO";
        
        @Override
        public boolean run(final AuthentificationTokenResultMessage msg) {
            final SWFWrapper swfWrapper = this.m_frame.getSwfWrapper();
            final String name = WakfuGameEntity.getInstance().getLocalAccount().getAccountNickName();
            final String token = msg.getToken();
            final Object[] params = { name, token, KrosmozGameFrame.GAME_ID };
            swfWrapper.invokeFunction("receiveArenaSSO", params);
            swfWrapper.displayComponent();
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 2079;
        }
    }
}
