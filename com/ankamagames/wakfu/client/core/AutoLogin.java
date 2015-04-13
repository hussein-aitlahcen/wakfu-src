package com.ankamagames.wakfu.client.core;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.message.character.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.proxy.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.ui.protocol.message.connection.*;

public final class AutoLogin extends Thread
{
    protected static final Logger m_logger;
    private static AutoLogin m_instance;
    private static final Object m_mutex;
    private final Queue<Runnable> m_events;
    private volatile boolean m_running;
    public static boolean m_alreadyUse;
    
    private AutoLogin() {
        super();
        this.m_events = new LinkedList<Runnable>();
        this.m_running = false;
    }
    
    public static void startAutoLogin() {
        try {
            if (!AutoLogin.m_alreadyUse && WakfuConfiguration.getInstance().getBoolean("autoLogin")) {
                AutoLogin.m_alreadyUse = true;
                (AutoLogin.m_instance = new AutoLogin()).start();
            }
        }
        catch (PropertyException ex) {}
    }
    
    public static void peekAtMessage(final Message message) {
        if (AutoLogin.m_instance == null) {
            return;
        }
        try {
            switch (message.getId()) {
                case 1025: {
                    synchronized (AutoLogin.m_mutex) {
                        AutoLogin.m_instance.m_events.add(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500L);
                                }
                                catch (Exception e) {
                                    AutoLogin.m_logger.error((Object)"Erreur lors de l'entr\u00e9e dans le monde.");
                                    AutoLogin.m_instance.m_running = false;
                                }
                            }
                        });
                    }
                    break;
                }
                case 2048: {
                    synchronized (AutoLogin.m_mutex) {
                        AutoLogin.m_instance.m_events.add(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    boolean chooseCharacter = false;
                                    try {
                                        chooseCharacter = WakfuConfiguration.getInstance().getBoolean("autoLogin_selectCharacter");
                                    }
                                    catch (PropertyException e) {
                                        AutoLogin.m_logger.warn((Object)"Impossible de trouver la propri\u00e9t\u00e9 'autoLogin_selectCharacter' : initialisation par d\u00e9faut \u00e0 false.");
                                    }
                                    if (chooseCharacter) {
                                        Thread.sleep(500L);
                                        final PlayerCharacter playerCharacter = (PlayerCharacter)LocalCharacterInfosManager.getInstance().getFirstCharacterInfo();
                                        if (playerCharacter != null) {
                                            final UIPlayerInfoMessage msg = new UIPlayerInfoMessage();
                                            msg.setPlayerInfo(playerCharacter);
                                            msg.setId(16514);
                                            Worker.getInstance().pushMessage(msg);
                                        }
                                        else {
                                            Thread.sleep(500L);
                                            AutoLogin.m_instance.m_running = false;
                                        }
                                    }
                                    else {
                                        Thread.sleep(500L);
                                        AutoLogin.m_instance.m_running = false;
                                    }
                                }
                                catch (Exception e2) {
                                    AutoLogin.m_logger.error((Object)"Erreur lors du choix du personnage.");
                                    AutoLogin.m_instance.m_running = false;
                                }
                            }
                        });
                    }
                    break;
                }
                case 4100: {
                    synchronized (AutoLogin.m_mutex) {
                        AutoLogin.m_instance.m_events.add(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000L);
                                    AutoLogin.m_instance.m_running = false;
                                }
                                catch (Exception e) {
                                    AutoLogin.m_logger.error((Object)"Erreur lors de l'arr\u00eat de l'AutoLogin.");
                                }
                            }
                        });
                    }
                    break;
                }
            }
        }
        catch (Exception e) {
            AutoLogin.m_logger.error((Object)"Exception lev\u00e9e dans l'AutoLogin:", (Throwable)e);
        }
    }
    
    @Override
    public void run() {
        this.setName("AutoLogin");
        AutoLogin.m_logger.info((Object)"D\u00e9marrage de l'AutoLogin.");
        synchronized (AutoLogin.m_mutex) {
            this.m_events.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500L);
                        final String login = WakfuConfiguration.getInstance().getString("autoLogin_login");
                        final String password = WakfuConfiguration.getInstance().getString("autoLogin_password");
                        final ProxyGroup selectedProxyGroup = ProxyGroup.extractProxyGroupFromProperties(WakfuConfiguration.getInstance(), "dispatchAddresses");
                        if (selectedProxyGroup != null) {
                            AutoLogin.m_logger.info((Object)("Envoi de la requ\u00eate de login (login=" + login + ", password=" + password + ", proxyGroup=" + selectedProxyGroup + ")"));
                            final UILogonRequestMessage loginMsg = new UILogonRequestMessage();
                            loginMsg.setLogin(login);
                            loginMsg.setPassword(password);
                            loginMsg.setProxyGroup(selectedProxyGroup);
                            loginMsg.setRemember(true);
                            Worker.getInstance().pushMessage(loginMsg);
                        }
                        else {
                            AutoLogin.m_logger.error((Object)"Impossible de trouver un ProxyGroup convenable.");
                            AutoLogin.this.m_running = false;
                        }
                    }
                    catch (Exception e) {
                        AutoLogin.m_logger.error((Object)"Impossible de d\u00e9marrer l'AutoLogin", (Throwable)e);
                        AutoLogin.this.m_running = false;
                    }
                }
            });
        }
        this.m_running = true;
        while (this.m_running) {
            try {
                Thread.sleep(200L);
            }
            catch (InterruptedException e) {
                AutoLogin.m_logger.warn((Object)"Interrupted.");
            }
            if (!this.m_events.isEmpty()) {
                this.m_events.remove().run();
            }
        }
        AutoLogin.m_logger.info((Object)"Fin de l'AutoLogin.");
    }
    
    static {
        m_logger = Logger.getLogger((Class)AutoLogin.class);
        m_mutex = new Object();
        AutoLogin.m_alreadyUse = false;
    }
}
