package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.steam.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.proxy.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.security.*;
import com.ankamagames.steam.common.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.connection.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.news.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.steam.wrapper.*;
import com.ankamagames.wakfu.client.core.news.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.util.rss.*;
import java.net.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.rss.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.game.embeddedTutorial.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.framework.fileFormat.news.*;
import com.ankamagames.xulor2.core.*;

public class UIAuthentificationFrame implements MessageFrame, Runnable
{
    private static final Logger m_logger;
    private static final UIAuthentificationFrame m_instance;
    private NewsDisplayer m_newsDisplayer;
    private Container m_newsContainer;
    private Container m_blinkContainer;
    private static final long PING_NEWS_TIMEOUT = 5000L;
    private final Runnable m_pauseCycleRunnable;
    private final Runnable m_pingLoopRunnable;
    private long m_salt;
    private byte[] m_encodedPublicKey;
    private Thread m_rssLoadingThread;
    
    public UIAuthentificationFrame() {
        super();
        this.m_pauseCycleRunnable = new Runnable() {
            @Override
            public void run() {
                UIAuthentificationFrame.this.launchNewsCycle();
            }
        };
        this.m_pingLoopRunnable = new Runnable() {
            @Override
            public void run() {
                UIAuthentificationFrame.this.checkNewsLoading();
            }
        };
    }
    
    public static UIAuthentificationFrame getInstance() {
        return UIAuthentificationFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19410: {
                if (!SteamClientContext.INSTANCE.isInit()) {
                    return false;
                }
                PropertiesProvider.getInstance().setPropertyValue("useOAuth", false);
                this.connectToDispatch();
                return false;
            }
            case 19411: {
                PropertiesProvider.getInstance().setPropertyValue("useOAuth", true);
                this.connectToDispatch();
                return false;
            }
            case 16383: {
                final UISelectServerRequestMessage msg = (UISelectServerRequestMessage)message;
                final WakfuGamePreferences gamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
                gamePreferences.setValue(WakfuKeyPreferenceStoreEnum.REMEMBER_LAST_LOGIN_PREFERENCE_KEY, msg.getRemember());
                gamePreferences.setValue(WakfuKeyPreferenceStoreEnum.LAST_LOGIN_PREFERENCE_KEY, ((boolean)msg.getRemember()) ? msg.getLogin() : "");
                final WakfuGameEntity gameEntity = WakfuGameEntity.getInstance();
                gameEntity.setLogin(msg.getLogin());
                gameEntity.setPassword(msg.getPassword());
                gameEntity.setLogged(false);
                NetEnabledWidgetManager.INSTANCE.setGroupEnabled("loginLock", false);
                final ProxyGroup proxyGroup = ProxyGroup.extractProxyGroupFromProperties(WakfuConfiguration.getInstance(), "dispatchAddresses");
                WakfuGameEntity.getInstance().setProxyGroup(proxyGroup);
                if (WakfuGameEntity.getInstance().getCurrentLoginPhase() != LoginPhase.STEAM_LINK) {
                    WakfuGameEntity.getInstance().setCurrentLoginPhase(LoginPhase.DISPATCH);
                    WakfuGameEntity.getInstance().connect();
                }
                else {
                    Xulor.getInstance().unload("steamLinkAccountDialog");
                    ConnectionEncryptionManager.INSTANCE.initialize(this.m_encodedPublicKey);
                    final byte[] encryptedLoginAndPassword = WakfuGameEntity.getEncryptedLoginAndPassword(this.m_salt);
                    final ClientLinkAccountToSteamMessage netMsg = new ClientLinkAccountToSteamMessage();
                    netMsg.setEncryptedLoginAndPassword(encryptedLoginAndPassword);
                    final CSteamID steamID = SteamClientContext.INSTANCE.getSteamID();
                    netMsg.setSteamId(SteamUtils.serializeSteamId(steamID));
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
                }
                return false;
            }
            case 16385: {
                WakfuProgressMonitorManager.getInstance().getProgressMonitor(false).beginTask(WakfuTranslator.getInstance().getString("logon.progress"), 0);
                final UILogonRequestMessage msg2 = (UILogonRequestMessage)message;
                final WakfuGamePreferences gamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
                gamePreferences.setValue(WakfuKeyPreferenceStoreEnum.REMEMBER_LAST_LOGIN_PREFERENCE_KEY, msg2.getRemember());
                gamePreferences.setValue(WakfuKeyPreferenceStoreEnum.LAST_LOGIN_PREFERENCE_KEY, ((boolean)msg2.getRemember()) ? msg2.getLogin() : "");
                final WakfuGameEntity gameEntity = WakfuGameEntity.getInstance();
                gameEntity.setLogin(msg2.getLogin());
                gameEntity.setPassword(msg2.getPassword());
                gameEntity.setLogged(false);
                final ProxyGroup proxyGroup = msg2.getProxyGroup();
                gameEntity.setProxyGroup(proxyGroup);
                if (proxyGroup != null) {
                    gamePreferences.setValue(WakfuKeyPreferenceStoreEnum.LAST_PROXY_GROUP_INDEX_PREFERENCE_KEY, ((boolean)msg2.getRemember()) ? proxyGroup.getIndex() : 0);
                    proxyGroup.clearRandomIterator();
                }
                NetEnabledWidgetManager.INSTANCE.setGroupEnabled("loginLock", false);
                PropertiesProvider.getInstance().setPropertyValue("useOAuth", false);
                gameEntity.connect();
                return false;
            }
            case 16392: {
                final AbstractUIMessage msg3 = (AbstractUIMessage)message;
                final String nickname = msg3.getStringValue();
                final ClientDispatchNickNameMessage authMsg = new ClientDispatchNickNameMessage();
                authMsg.setNickName(nickname);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(authMsg);
                return false;
            }
            case 16384: {
                final UIChangeLanguageRequestMessage msg4 = (UIChangeLanguageRequestMessage)message;
                WakfuTranslator.getInstance().setLanguage(msg4.getLanguage());
                WakfuClientInstance.getInstance().cleanUp();
                WakfuClientInstance.getInstance().start();
                UIMessage.send((short)16382);
                return false;
            }
            case 16382: {
                return false;
            }
            case 16111: {
                this.togglePlayPauseVideo();
                return false;
            }
            case 16112: {
                final UISelectNewMessage uiSelectNewMessage = (UISelectNewMessage)message;
                final NewsItemView newsItemView = uiSelectNewMessage.getNewsItemView();
                if (!uiSelectNewMessage.getBooleanValue() && newsItemView.equals(this.m_newsDisplayer.getCurrentNew())) {
                    return false;
                }
                this.setCurrentDisplayedNew(newsItemView);
                this.stopNewsCycle();
                return false;
            }
            case 16113: {
                this.setCurrentDisplayedNew(this.m_newsDisplayer.getPreviousNew());
                if (!this.m_newsDisplayer.hasPreviousNew()) {
                    Actions.setLeftArrowInvisible(null, true);
                }
                this.stopNewsCycle();
                return false;
            }
            case 16114: {
                this.setCurrentDisplayedNew(this.m_newsDisplayer.getNextNew());
                if (!this.m_newsDisplayer.hasNextNew()) {
                    Actions.setRightArrowInvisible(null, true);
                }
                this.stopNewsCycle();
                return false;
            }
            case 16115: {
                final float floatValue = ((AbstractUIMessage)message).getFloatValue();
                final NewsVideoElementView videoElement = this.m_newsDisplayer.getCurrentNew().getVideoElement();
                videoElement.setVideoVolume(floatValue);
                PropertiesProvider.getInstance().firePropertyValueChanged(videoElement, "videoSoundVolumeValue", "videoSoundVolumeValue", "videoMuted");
                return false;
            }
            case 16119: {
                final float floatValue = ((AbstractUIMessage)message).getFloatValue();
                final NewsVideoElementView videoElement = this.m_newsDisplayer.getCurrentNew().getVideoElement();
                videoElement.seek(floatValue);
                PropertiesProvider.getInstance().firePropertyValueChanged(videoElement, "videoCurrentTimeText", "videoProgressionValue");
                return false;
            }
            case 16116: {
                final int value = ((AbstractUIMessage)message).getIntValue();
                final NewsVideoElementView videoElement = this.m_newsDisplayer.getCurrentNew().getVideoElement();
                videoElement.setSelectedQuality(this.m_newsContainer, value);
                PropertiesProvider.getInstance().firePropertyValueChanged(videoElement, "selectedQuality");
                return false;
            }
            case 16117: {
                final NewsItemView currentNew = this.m_newsDisplayer.getCurrentNew();
                if (currentNew == null) {
                    return false;
                }
                if (!currentNew.hasVideo()) {
                    return false;
                }
                final NewsVideoElementView videoElement = currentNew.getVideoElement();
                videoElement.setMuted(!videoElement.isMuted());
                PropertiesProvider.getInstance().firePropertyValueChanged(videoElement, "videoSoundVolumeValue", "videoSoundVolumeValue", "videoMuted");
                return false;
            }
            case 16394: {
                final CSteamID steamID2 = SteamClientContext.INSTANCE.getSteamID();
                final ClientCreateSteamAccountMessage accountMessage = new ClientCreateSteamAccountMessage();
                accountMessage.setLanguage(WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage());
                accountMessage.setSteamId(SteamUtils.serializeSteamId(steamID2));
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(accountMessage);
                return false;
            }
            case 16395: {
                WakfuProgressMonitorManager.getInstance().done();
                WakfuGameEntity.getInstance().disconnectFromServer("Cancel Steam Link");
                WakfuGameEntity.getInstance().pushFrame(getInstance());
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void connectToDispatch() {
        NetEnabledWidgetManager.INSTANCE.setGroupEnabled("loginLock", false);
        final ProxyGroup proxyGroup = ProxyGroup.extractProxyGroupFromProperties(WakfuConfiguration.getInstance(), "dispatchAddresses");
        WakfuGameEntity.getInstance().setProxyGroup(proxyGroup);
        WakfuGameEntity.getInstance().setCurrentLoginPhase(LoginPhase.DISPATCH);
        WakfuGameEntity.getInstance().connect();
    }
    
    private void togglePlayPauseVideo() {
        final NewsItemView currentNew = this.m_newsDisplayer.getCurrentNew();
        if (currentNew == null) {
            return;
        }
        if (!currentNew.hasVideo()) {
            return;
        }
        final NewsVideoElementView videoElement = currentNew.getVideoElement();
        videoElement.togglePlayPauseVideo();
        PropertiesProvider.getInstance().firePropertyValueChanged(videoElement, "videoPlaying");
        this.clearNewsCycle();
        WakfuSoundManager.getInstance().fadeMusic(videoElement.getVideoWidget().isPaused() ? 1.0f : 0.0f, 1000);
    }
    
    private void setCurrentDisplayedNew(final NewsItemView newsItemView) {
        if (newsItemView == null) {
            return;
        }
        if (this.m_newsDisplayer.getCurrentNew() != null && this.m_newsDisplayer.getCurrentNew().hasVideo()) {
            final NewsVideoElementView videoElement = this.m_newsDisplayer.getCurrentNew().getVideoElement();
            if (videoElement.isVideoPlaying()) {
                this.togglePlayPauseVideo();
            }
        }
        this.blinkNewsContainer(this.m_newsDisplayer.getCurrentNew() == null);
        this.m_newsDisplayer.setCurrentNew(newsItemView);
        this.m_newsDisplayer.populateContainerWithNews(this.m_newsContainer);
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_newsDisplayer, this.m_newsDisplayer.getFields());
        if (newsItemView.hasVideo()) {
            PropertiesProvider.getInstance().firePropertyValueChanged(newsItemView.getVideoElement(), NewsVideoElementView.FIELDS);
        }
    }
    
    private void blinkNewsContainer(final boolean first) {
        final int aValue = this.m_blinkContainer.getAppearance().getModulationColor().get();
        final int bValue = Color.WHITE.get();
        if (aValue == bValue) {
            return;
        }
        final Color c = new Color(aValue);
        final Color c2 = new Color(bValue);
        this.m_blinkContainer.getAppearance().removeTweensOfType(ModulationColorTween.class);
        final AbstractTween t = new ModulationColorTween(c, c2, this.m_blinkContainer.getAppearance(), 0, 75, 1, false, TweenFunction.PROGRESSIVE);
        t.addTweenEventListener(new TweenEventListener() {
            @Override
            public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                switch (e) {
                    case TWEEN_ENDED: {
                        final Color c = new Color(bValue);
                        final Color c2 = new Color(aValue);
                        UIAuthentificationFrame.this.m_blinkContainer.getAppearance().removeTweensOfType(ModulationColorTween.class);
                        final AbstractTween t = new ModulationColorTween(c, c2, UIAuthentificationFrame.this.m_blinkContainer.getAppearance(), 0, first ? 2500 : 75, 1, false, TweenFunction.PROGRESSIVE);
                        UIAuthentificationFrame.this.m_blinkContainer.getAppearance().addTween(t);
                        t.removeTweenEventListener(this);
                        break;
                    }
                }
            }
        });
        this.m_blinkContainer.getAppearance().addTween(t);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Nullable
    private static String generateWakfuWebsiteURLLink() {
        try {
            final String wakfuWebsiteUrl = WakfuConfiguration.getInstance().getString("wakfuWebsiteUrl");
            final String wakfuWebsiteUrlDisplay = WakfuConfiguration.getInstance().getString("wakfuWebsiteUrlDisplay");
            return RSSUtils.HTMLLinkToTextViewLink(wakfuWebsiteUrl, wakfuWebsiteUrlDisplay);
        }
        catch (PropertyException e) {
            UIAuthentificationFrame.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (isAboutToBeAdded) {
            return;
        }
        PropertiesProvider.getInstance().setPropertyValue("overNew", null);
        PropertiesProvider.getInstance().setPropertyValue("wakfuWebsiteURL", generateWakfuWebsiteURLLink());
        PropertiesProvider.getInstance().setPropertyValue("rssFeed", null);
        PropertiesProvider.getInstance().setPropertyValue("likevnAnkamaAuthVisible", false);
        if (this.m_rssLoadingThread == null) {
            (this.m_rssLoadingThread = new Thread("RSS Loading") {
                @Override
                public void run() {
                    final XMLDocumentContainer container = new XMLDocumentContainer();
                    final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
                    try {
                        final String language = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage().toLowerCase();
                        String baseURL = WakfuConfiguration.getInstance().getString("rssURL");
                        if (baseURL == null) {
                            baseURL = "http://www.wakfu.com/";
                        }
                        final URL url = ContentFileHelper.getURL(baseURL + language + "/mmorpg/game.xml");
                        final InputStream stream = url.openStream();
                        accessor.open(stream);
                        accessor.read(container, new DocumentEntryParser[0]);
                        accessor.close();
                        stream.close();
                    }
                    catch (Exception e) {
                        UIAuthentificationFrame.m_logger.warn((Object)"Probl\u00e8me au chargement du flux rss", (Throwable)e);
                        return;
                    }
                    final RSSDocumentReader rssDocumentReader = new RSSDocumentReader();
                    try {
                        final RSSChannel rssChannel = rssDocumentReader.read(container, true);
                        if (!rssChannel.getItems().isEmpty()) {
                            final RSSChannelFieldProvider view = new RSSChannelFieldProvider(rssChannel);
                            PropertiesProvider.getInstance().setPropertyValue("rssFeed", view);
                        }
                        else {
                            PropertiesProvider.getInstance().setPropertyValue("rssFeed", null);
                        }
                    }
                    catch (MalformedRSSException e2) {
                        UIAuthentificationFrame.m_logger.warn((Object)"Probl\u00e8me lors du chargement du flux rss");
                    }
                    UIAuthentificationFrame.this.m_rssLoadingThread = null;
                }
            }).start();
        }
        PropertiesProvider.getInstance().setPropertyValue("languages", WakfuTranslator.getInstance().getLanguageViews());
        PropertiesProvider.getInstance().setPropertyValue("currentLanguage", WakfuTranslator.getInstance().getCurrentLanguageView());
        NetEnabledWidgetManager.INSTANCE.createGroup("loginLock");
        final String logonDialogId = "logonDialog";
        Xulor.getInstance().load("logonDialog", Dialogs.getDialogPath("logonDialog"), 8192L, (short)10000).getElementMap();
        PropertiesProvider.getInstance().setPropertyValue("news", null);
        try {
            this.checkNewsLoading();
        }
        catch (Throwable e) {
            UIAuthentificationFrame.m_logger.error((Object)"Erreur au chargement du carousel !");
            e.printStackTrace();
        }
        WakfuSoundManager.getInstance().onBackToLogin();
        EmbeddedTutorialManager.getInstance().setEnabled(false);
        if (WakfuGameEntity.getInstance().hasFrame(UISystemBarFrame.getInstance())) {
            UISystemBarFrame.getInstance().reloadMenuBarDialog();
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UISystemBarFrame.getInstance());
        }
    }
    
    void checkNewsLoading() {
        switch (NewsManager.INSTANCE.getState()) {
            case ERROR: {
                UIAuthentificationFrame.m_logger.error((Object)"[NEWS] Erreur lors du chargement");
                this.stopPingLoopRunnable();
                break;
            }
            case FULLY_LOADED: {
                UIAuthentificationFrame.m_logger.info((Object)"[NEWS] Chargement effectu\u00e9");
                this.stopPingLoopRunnable();
                this.loadNews();
                break;
            }
            case LOADING_IMAGES:
            case LOADING_STREAM: {
                UIAuthentificationFrame.m_logger.warn((Object)"[NEWS] Chargement...");
                ProcessScheduler.getInstance().schedule(this.m_pingLoopRunnable, 5000L, 1);
                break;
            }
        }
    }
    
    private void stopPingLoopRunnable() {
        ProcessScheduler.getInstance().remove(this.m_pingLoopRunnable);
    }
    
    private void loadNews() {
        final NewsChannel channel = NewsManager.INSTANCE.getChannel();
        if (channel == null || channel.getItems().isEmpty()) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("logonDialog");
        this.m_newsContainer = (Container)map.getElement("newsContainer");
        this.m_blinkContainer = (Container)map.getElement("blinkContainer");
        this.m_newsDisplayer = new NewsDisplayer(channel);
        this.setCurrentDisplayedNew(this.m_newsDisplayer.getFirstNew());
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_newsDisplayer, this.m_newsDisplayer.getFields());
        PropertiesProvider.getInstance().setPropertyValue("news", this.m_newsDisplayer);
        this.launchNewsCycle();
    }
    
    private void clearNewsCycle() {
        ProcessScheduler.getInstance().remove(this);
        ProcessScheduler.getInstance().remove(this.m_pauseCycleRunnable);
    }
    
    private void stopNewsCycle() {
        this.clearNewsCycle();
        ProcessScheduler.getInstance().schedule(this.m_pauseCycleRunnable, 180000L, 1);
    }
    
    void launchNewsCycle() {
        ProcessScheduler.getInstance().schedule(this, 30000L, -1);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.clearNewsCycle();
            this.stopPingLoopRunnable();
            WakfuSoundManager.getInstance().fadeMusic(1.0f, 1000);
            if (this.m_newsDisplayer != null) {
                this.m_newsDisplayer.clean();
            }
            Xulor.getInstance().unload("logonDialog");
            NetEnabledWidgetManager.INSTANCE.destroyGroup("loginLock");
            if (this.m_rssLoadingThread != null) {
                this.m_rssLoadingThread.interrupt();
                this.m_rssLoadingThread = null;
            }
        }
    }
    
    @Override
    public void run() {
        if (!this.m_newsDisplayer.hasNextNew() && !this.m_newsDisplayer.hasPreviousNew()) {
            return;
        }
        if (!this.m_newsDisplayer.hasNextNew()) {
            this.setCurrentDisplayedNew(this.m_newsDisplayer.getFirstNew());
            return;
        }
        this.setCurrentDisplayedNew(this.m_newsDisplayer.getNextNew());
    }
    
    public NewsDisplayer getNewsDisplayer() {
        return this.m_newsDisplayer;
    }
    
    public void setTemporaryLoginEncryptionInfo(final long salt, final byte[] encodedPublicKey) {
        this.m_salt = salt;
        this.m_encodedPublicKey = encodedPublicKey;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIAuthentificationFrame.class);
        m_instance = new UIAuthentificationFrame();
    }
}
