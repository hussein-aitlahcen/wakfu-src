package com.ankamagames.wakfu.client.core.auth;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.net.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.auth.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.auth.*;
import com.ankamagames.framework.kernel.utils.*;

public class ICEAuthFrame extends MessageRunnerFrame
{
    private static final Logger m_logger;
    private final Object m_mutex;
    public static final ICEAuthFrame INSTANCE;
    private final ArrayList<ICEAuthListener> m_listeners;
    
    private ICEAuthFrame() {
        super(new MessageRunner[] { new AuthentificationTokenMessageRunner() });
        this.m_mutex = new Object();
        this.m_listeners = new ArrayList<ICEAuthListener>();
    }
    
    public void getToken(final ICEAuthListener listener) {
        synchronized (this.m_mutex) {
            ICEAuthFrame.m_logger.info((Object)("[" + this.getClass().getSimpleName() + "] Demande de token ICE"));
            this.m_listeners.add(listener);
            if (!WakfuGameEntity.getInstance().hasFrame(this)) {
                WakfuGameEntity.getInstance().pushFrame(this);
            }
            sendSSOrequest();
        }
    }
    
    void onTokenReceived(final String token) {
        synchronized (this.m_mutex) {
            ICEAuthFrame.m_logger.info((Object)("[" + this.getClass().getSimpleName() + "] R\u00e9ception de token ICE : " + token));
            for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
                final ICEAuthListener listener = this.m_listeners.get(i);
                listener.onToken(token);
            }
            this.m_listeners.clear();
        }
    }
    
    void onError() {
        synchronized (this.m_mutex) {
            ICEAuthFrame.m_logger.info((Object)("[" + this.getClass().getSimpleName() + "] R\u00e9ception d'un token ICE vide."));
            for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
                final ICEAuthListener listener = this.m_listeners.get(i);
                listener.onError();
            }
            this.m_listeners.clear();
        }
    }
    
    private static void sendSSOrequest() {
        final long address = NetHelper.ip2long(WakfuGameEntity.getInstance().getClientIp());
        final String lang = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage();
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new AuthentificationTokenRequestMessage(address, lang));
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.forEachRunner((TObjectProcedure<MessageRunner>)new TObjectProcedure<AuthentificationTokenMessageRunner>() {
                @Override
                public boolean execute(final AuthentificationTokenMessageRunner object) {
                    object.setFrame(ICEAuthFrame.this);
                    return true;
                }
            });
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            synchronized (this.m_mutex) {
                this.m_listeners.clear();
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public String toString() {
        return "ICEAuthFrame{}";
    }
    
    static {
        m_logger = Logger.getLogger((Class)ICEAuthFrame.class);
        INSTANCE = new ICEAuthFrame();
    }
    
    private static class AuthentificationTokenMessageRunner implements MessageRunner<AuthentificationTokenResultMessage>
    {
        protected ICEAuthFrame m_frame;
        
        @Override
        public String toString() {
            return "AuthentificationTokenMessageRunner{}";
        }
        
        protected void setFrame(final ICEAuthFrame frame) {
            this.m_frame = frame;
        }
        
        @Override
        public boolean run(final AuthentificationTokenResultMessage msg) {
            final String token = msg.getToken();
            if (StringUtils.isEmptyOrNull(token)) {
                this.m_frame.onError();
            }
            else {
                this.m_frame.onTokenReceived(token);
            }
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 2079;
        }
    }
}
