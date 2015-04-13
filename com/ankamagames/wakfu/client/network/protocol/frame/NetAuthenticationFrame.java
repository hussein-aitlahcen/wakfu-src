package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import java.text.*;
import com.ankamagames.baseImpl.graphics.opengl.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.security.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.*;
import java.io.*;
import com.ankamagames.baseImpl.graphics.ui.shortcuts.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.companion.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.steam.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import org.apache.commons.lang3.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.proxyGroup.*;
import com.ankamagames.wakfu.common.dispatch.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.google.common.base.*;
import com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient.*;
import com.ankamagames.wakfu.client.core.game.steam.*;
import com.ankamagames.steam.common.*;
import com.ankamagames.steam.wrapper.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.framework.preferences.*;
import com.ankamagames.wakfu.common.constants.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;
import com.ankamagames.wakfu.client.core.webBrowser.*;
import com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.framework.kernel.*;
import gnu.trove.*;

public class NetAuthenticationFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static NetAuthenticationFrame m_instance;
    private static final DateFormat TIME_FORMAT;
    private static final DateFormat DATE_FORMAT;
    private static String m_wakfuToken;
    private ContainerListener m_closeBigPointBrowserListener;
    
    public NetAuthenticationFrame() {
        super();
        this.m_closeBigPointBrowserListener = new ContainerListener() {
            @Override
            public void onClose() {
                WakfuProgressMonitorManager.getInstance().done();
                WakfuGameEntity.getInstance().disconnectFromServer("Cancel Bigpoint Login");
                WakfuGameEntity.getInstance().pushFrame(UIAuthentificationFrame.getInstance());
                SWFWrapper.INSTANCE.removeDelegateContainerListener(this);
            }
        };
    }
    
    public static NetAuthenticationFrame getInstance() {
        return NetAuthenticationFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 1034: {
                final ClientPublicKeyMessage msg = (ClientPublicKeyMessage)message;
                onClientPublicKey(msg);
                return false;
            }
            case 1037: {
                final ClientSecurityCardRequestMessage msg2 = (ClientSecurityCardRequestMessage)message;
                final String cardQuestion = msg2.getQuestion();
                final String securityQestion = msg2.getQuestion();
                final MessageBoxData data = new MessageBoxData(102, 0, WakfuTranslator.getInstance().getString("securityCard.question", securityQestion), 65542L);
                final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type != 2) {
                            WakfuGameEntity.getInstance().setLogin(null);
                            WakfuGameEntity.getInstance().setPassword(null);
                            WakfuGameEntity.getInstance().setSecurityCardQuestion(null);
                            WakfuGameEntity.getInstance().setSecurityCardAnswer(null);
                            WakfuGameEntity.getInstance().disconnectFromServer("Login Error");
                            return;
                        }
                        WakfuGameEntity.getInstance().setSecurityCardQuestion(cardQuestion);
                        WakfuGameEntity.getInstance().setSecurityCardAnswer(userEntry);
                        final long salt = msg2.getSalt();
                        final byte[] encodedPublicKey = msg2.getPublicKey();
                        ConnectionEncryptionManager.INSTANCE.initialize(encodedPublicKey);
                        final byte[] encryptedLoginAndPassword = WakfuGameEntity.getEncryptedLoginAndPassword(salt);
                        final byte[] encryptedCardQuestionAnswer = NetAuthenticationFrame.this.getEncryptedCardQuestionAnswer(salt, cardQuestion, userEntry);
                        final ClientDispatchAuthenticationCardMessage authMsg = new ClientDispatchAuthenticationCardMessage();
                        authMsg.setEncryptedLoginAndPassword(encryptedLoginAndPassword);
                        authMsg.setEncryptedCardQuestionAnswer(encryptedCardQuestionAnswer);
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(authMsg);
                    }
                });
                return false;
            }
            case 1039: {
                final ClientNickNameRequestMessage msg3 = (ClientNickNameRequestMessage)message;
                final byte errorCode = msg3.getErrorCode();
                this.closeBigPointBrowserIfOpenned();
                String errorTranslationKey = "error.connection.nicknameNotSet";
                switch (errorCode) {
                    case 13: {
                        errorTranslationKey = "error.connection.nicknameNotSet";
                        break;
                    }
                    case 14: {
                        errorTranslationKey = "error.connection.nicknameInvalidContent";
                        break;
                    }
                    case 15: {
                        errorTranslationKey = "error.connection.nicknameDuplicate";
                        break;
                    }
                    case 16: {
                        errorTranslationKey = "error.connection.nicknameTooLong";
                        break;
                    }
                    case 17: {
                        errorTranslationKey = "error.connection.nicknameTooShort";
                        break;
                    }
                    case 18: {
                        errorTranslationKey = "error.connection.nicknameEqualsLogin";
                        break;
                    }
                    case 19: {
                        errorTranslationKey = "error.connection.nicknameInvalidFormat";
                        break;
                    }
                }
                Xulor.getInstance().load("nicknameChoiceDialog", Dialogs.getDialogPath("nicknameChoiceDialog"), 8448L, (short)10000);
                Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString(errorTranslationKey), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 1, 2);
                return false;
            }
            case 1025: {
                final ClientAuthenticationResultsMessage msg4 = (ClientAuthenticationResultsMessage)message;
                WakfuGameEntity.getInstance().setDisconnectionReason((byte)0);
                WakfuGameEntity.getInstance().getProxyGroup().clearRandomIterator();
                WakfuGameEntity.getInstance().setCurrentLoginPhase(LoginPhase.CONNECTED);
                if (msg4.isSuccessfull()) {
                    WakfuGameEntity.getInstance().setLogged(true);
                    final byte[] accountInformations = msg4.getSerializedAccountInformations();
                    if (accountInformations != null) {
                        final LocalAccountInformations localAccountInformations = new LocalAccountInformations();
                        localAccountInformations.fromBuild(accountInformations);
                        WakfuGameEntity.getInstance().setLocalAccount(localAccountInformations);
                        WakfuClientConfigurationManager.getInstance().setAccountName(PropertiesProvider.getInstance().getStringProperty("account.name"));
                        final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
                        final PreferenceStore store = new AccountPreferenceStore(localAccountInformations.getAccountId());
                        wakfuGamePreferences.setAccountPreferenceStore(store);
                        try {
                            store.load();
                        }
                        catch (IOException e) {
                            NetAuthenticationFrame.m_logger.warn((Object)"Probl\u00ef¿½me au chargement du PreferenceStore", (Throwable)e);
                        }
                        ShortcutManager.getInstance().enableGroup("admin", !AdminRightHelper.checkRights(localAccountInformations.getAdminRights(), AdminRightHelper.NO_RIGHT));
                        PropertiesProvider.getInstance().setPropertyValue("isAdmin", !AdminRightHelper.checkRights(localAccountInformations.getAdminRights(), AdminRightHelper.NO_RIGHT));
                    }
                    WakfuGameEntity.getInstance().removeFrame(this);
                    WakfuGameEntity.getInstance().removeFrame(UIAuthentificationFrame.getInstance());
                    WakfuGameEntity.getInstance().pushFrame(NetSystemNotificationsAndPingFrame.getInstance());
                    NetCharacterChoiceFrame.getInstance().enableLoadUI(false);
                    WakfuGameEntity.getInstance().pushFrame(NetCharacterChoiceFrame.getInstance());
                    WakfuGameEntity.getInstance().pushFrame(NetChatFrame.getInstance());
                    WakfuGameEntity.getInstance().pushFrame(NetGroupFrame.getInstance());
                    WakfuGameEntity.getInstance().pushFrame(NetCalendarFrame.INSTANCE);
                    WakfuGameEntity.getInstance().pushFrame(NetSystemConfigurationFrame.INSTANCE);
                    WakfuGameEntity.getInstance().pushFrame(NetEventsCalendarFrame.getInstance());
                    WakfuGameEntity.getInstance().pushFrame(NetCompanionFrame.INSTANCE);
                }
                else {
                    WakfuGameEntity.getInstance().setSecurityCardQuestion(null);
                    WakfuGameEntity.getInstance().setSecurityCardAnswer(null);
                    Object[] errorStringParams = new Object[0];
                    final byte errorCode2 = (byte)(msg4.getErrorCode() & 0xFF);
                    if (errorCode2 == 13 || errorCode2 == 15 || errorCode2 == 18 || errorCode2 == 14 || errorCode2 == 19 || errorCode2 == 16 || errorCode2 == 17) {
                        WakfuGameEntity.getInstance().pushFrame(UIAuthentificationFrame.getInstance());
                        onLoginNicknameError(errorCode2);
                        return false;
                    }
                    if (errorCode2 == 26) {
                        WakfuGameEntity.getInstance().pushFrame(UIAuthentificationFrame.getInstance());
                        return false;
                    }
                    String errorString = null;
                    switch (errorCode2) {
                        case 2: {
                            errorString = "error.connection.invalidLogin";
                            break;
                        }
                        case 3: {
                            errorString = "error.connection.alreadyConnected";
                            break;
                        }
                        case 4: {
                            errorString = "error.connection.saveInProgress";
                            break;
                        }
                        case Byte.MAX_VALUE: {
                            errorString = "error.connection.closedBeta";
                            break;
                        }
                        case 9: {
                            errorString = "error.connection.locked";
                            break;
                        }
                        case 10: {
                            errorString = "error.connection.loginServerDown";
                            break;
                        }
                        case 11: {
                            errorString = "error.connection.tooManyConnection";
                            break;
                        }
                        case 12: {
                            errorString = "error.connection.invalidPartner";
                            break;
                        }
                        case 5: {
                            errorString = "error.connection.banned";
                            int banMinutes = msg4.getBanDuration();
                            final int banDays = banMinutes / 1440;
                            banMinutes -= banDays * 60 * 24;
                            final int banHours = banMinutes / 60;
                            banMinutes -= banHours * 60;
                            if (banDays > 0) {
                                errorStringParams = new Object[] { banDays, banHours, 0 };
                                break;
                            }
                            errorStringParams = new Object[] { 0, banHours, banMinutes };
                            break;
                        }
                        case 20: {
                            errorString = "error.connection.invalidEmail";
                            try {
                                final String validationURL = WakfuConfiguration.getInstance().getString("accountValidationUrl");
                                final String url = String.format(validationURL, WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage());
                                errorStringParams = new Object[] { url };
                            }
                            catch (PropertyException e3) {
                                NetAuthenticationFrame.m_logger.error((Object)"Impossible de r\u00ef¿½cup\u00ef¿½rer la propri\u00ef¿½t\u00ef¿½ de validation d'e-mail");
                            }
                            break;
                        }
                        case 22: {
                            errorString = "error.connection.OTPAuthFailed";
                            try {
                                final String validationURL = WakfuConfiguration.getInstance().getString("accountValidationUrl");
                                final String url = String.format(validationURL, WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage());
                                errorStringParams = new Object[] { url };
                            }
                            catch (PropertyException e3) {
                                NetAuthenticationFrame.m_logger.error((Object)"Impossible de r\u00ef¿½cup\u00ef¿½rer la propri\u00ef¿½t\u00ef¿½ de validation d'e-mail");
                            }
                            break;
                        }
                        case 21: {
                            errorString = "error.connection.accountModeration";
                            break;
                        }
                        case 40: {
                            errorString = "error.connection.invalidLogin";
                            break;
                        }
                        case 42: {
                            errorString = "error.connection.invalidToken";
                            break;
                        }
                        case 27:
                        case 28:
                        case 29:
                        case 30:
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                        case 43: {
                            errorString = "steam.genericError";
                            errorStringParams = new Object[] { String.valueOf(errorCode2) };
                            break;
                        }
                        default: {
                            errorString = "error.connection.invalidLogin";
                            break;
                        }
                    }
                    this.loginError(message, errorString, errorStringParams);
                }
                return false;
            }
            case 1030: {
                this.loginError(message, "error.connection.worldLoading", new Object[0]);
                return false;
            }
            case 9: {
                final ProxyRelayErrorMessage proxyRelayErrorMessage = (ProxyRelayErrorMessage)message;
                this.loginError(message, "error.connection.loginServerDown", proxyRelayErrorMessage.getReason());
                return false;
            }
            case 1212: {
                final WakfuAuthenticationTokenResultMessage msg5 = (WakfuAuthenticationTokenResultMessage)message;
                final byte errorCode = msg5.getErrorCode();
                final String token = msg5.getToken();
                NetAuthenticationFrame.m_logger.info((Object)("Authentication token received from dispatch server : " + token + " errorCode=" + errorCode));
                switch (errorCode) {
                    case 0: {
                        this.onWakfuTokenReceived(token);
                        return false;
                    }
                    default: {
                        return false;
                    }
                }
                break;
            }
            case 1210: {
                final OAuthAuthenticationTokenResultMessage msg6 = (OAuthAuthenticationTokenResultMessage)message;
                final byte errorCode = msg6.getErrorCode();
                final String token = msg6.getToken();
                NetAuthenticationFrame.m_logger.info((Object)("Authentication token received from dispatch server : " + token + " errorCode=" + errorCode));
                switch (errorCode) {
                    case 0: {
                        this.onTokenReceived(token);
                        return false;
                    }
                    default: {
                        return false;
                    }
                }
                break;
            }
            case 1027: {
                final ClientDispatchAuthenticationResultMessage msg7 = (ClientDispatchAuthenticationResultMessage)message;
                final byte errorCode = msg7.getResultCode();
                SteamDisplayer.INSTANCE.setHintActivated(msg7.isActivateSteamLinkHint());
                this.closeBigPointBrowserIfOpenned();
                NetAuthenticationFrame.m_logger.info((Object)("Authentication result " + errorCode));
                String[] errorStringParams2 = null;
                switch (errorCode) {
                    case 0: {
                        DispatchAccountInformation.set(msg7.getCommunity(), msg7.getAdmin());
                        onLoginSuccess();
                        return false;
                    }
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19: {
                        onLoginNicknameError(errorCode);
                        return false;
                    }
                    case 20: {
                        try {
                            final String validationURL2 = WakfuConfiguration.getInstance().getString("accountValidationUrl");
                            errorStringParams2 = new String[] { String.format(validationURL2, WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage()) };
                        }
                        catch (PropertyException e2) {
                            NetAuthenticationFrame.m_logger.error((Object)"Impossible de r\u00c3©cup\u00c3©rer la propri\u00c3©t\u00c3© de validation d'e-mail", (Throwable)e2);
                        }
                        NetEnabledWidgetManager.INSTANCE.setGroupEnabled("loginLock", true);
                        WakfuProgressMonitorManager.getInstance().done();
                        this.loginError(message, LoginError.fromError(errorCode).m_key, (Object[])(ArrayUtils.isEmpty(errorStringParams2) ? ArrayUtils.EMPTY_STRING_ARRAY : errorStringParams2));
                        return false;
                    }
                    default: {
                        NetEnabledWidgetManager.INSTANCE.setGroupEnabled("loginLock", true);
                        WakfuProgressMonitorManager.getInstance().done();
                        this.loginError(message, LoginError.fromError(errorCode).m_key, new Object[0]);
                        return false;
                    }
                }
                break;
            }
            case 1036: {
                final ClientProxiesResultMessage msg8 = (ClientProxiesResultMessage)message;
                final TIntObjectHashMap<Proxy> proxies = msg8.getProxies();
                final TIntObjectHashMap<WorldInfo> worldInfos = msg8.getWorldInfos();
                final ArrayList<WakfuServerView> serverViews = new ArrayList<WakfuServerView>();
                final TIntObjectIterator<Proxy> it = proxies.iterator();
                while (it.hasNext()) {
                    it.advance();
                    final Proxy proxy = it.value();
                    final WorldInfo info = worldInfos.get(proxy.getId());
                    if (info != null) {
                        serverViews.add(new WakfuServerView(proxy, info));
                    }
                }
                UIServerChoiceFrame.INSTANCE.reloadServerList((Optional<ArrayList<WakfuServerView>>)Optional.of((Object)serverViews));
                WakfuProgressMonitorManager.getInstance().done();
                WakfuGameEntity.getInstance().pushFrame(UIServerChoiceFrame.INSTANCE);
                return false;
            }
            case 1042: {
                final SteamLoginResultMessage steamLoginResultMessage = (SteamLoginResultMessage)message;
                final long salt = steamLoginResultMessage.getSalt();
                final byte[] encodedPublicKey = steamLoginResultMessage.getPublicKey();
                UIAuthentificationFrame.getInstance().setTemporaryLoginEncryptionInfo(salt, encodedPublicKey);
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("steamLinkAccountQuestion"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2077L, 102, 2);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            WakfuGameEntity.getInstance().setCurrentLoginPhase(LoginPhase.STEAM_LINK);
                            Xulor.getInstance().load("steamLinkAccountDialog", Dialogs.getDialogPath("steamLinkAccountDialog"), 8448L, (short)10000);
                        }
                        else if (type == 16) {
                            final CSteamID steamID = SteamClientContext.INSTANCE.getSteamID();
                            final ClientCreateSteamAccountMessage accountMessage = new ClientCreateSteamAccountMessage();
                            accountMessage.setLanguage(WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage());
                            accountMessage.setSteamId(SteamUtils.serializeSteamId(steamID));
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(accountMessage);
                        }
                        else {
                            WakfuProgressMonitorManager.getInstance().done();
                            WakfuGameEntity.getInstance().disconnectFromServer("Cancel Steam Link");
                            WakfuGameEntity.getInstance().pushFrame(UIAuthentificationFrame.getInstance());
                        }
                    }
                });
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void closeBigPointBrowserIfOpenned() {
        if (SWFWrapper.INSTANCE.isOpened()) {
            SWFWrapper.INSTANCE.removeDelegateContainerListener(this.m_closeBigPointBrowserListener);
            SWFWrapper.INSTANCE.unload();
        }
    }
    
    private void onWakfuTokenReceived(final String token) {
        NetAuthenticationFrame.m_wakfuToken = token;
        UIServerChoiceFrame.connectToServer();
    }
    
    private void onTokenReceived(final String token) {
        SWFWrapper.INSTANCE.toggleDisplay(KrosmozGame.FULL_SCREEN_BROWSER_WITHOUT_CONTROLS);
        final SWFBrowser swfBrowser = SWFWrapper.INSTANCE.getBrowser();
        final Browser browser = swfBrowser.getBrowser();
        final Partner currentPartner = Partner.getCurrentPartner();
        String string = null;
        try {
            switch (currentPartner) {
                case BIGPOINT: {
                    string = WakfuConfiguration.getInstance().getString("bigpointLoginWebsiteUrl");
                    break;
                }
                case LIKEVN: {
                    string = WakfuConfiguration.getInstance().getString("likevnLoginWebsiteUrl");
                    break;
                }
                default: {
                    throw new UnsupportedOperationException("Le partner courant : " + currentPartner + " ne correspond \u00c3  aucun service d'OAuth");
                }
            }
        }
        catch (PropertyException e) {
            NetAuthenticationFrame.m_logger.error((Object)("Le partner courant : " + currentPartner + " ne correspond \u00c3  aucun service d'OAuth"), (Throwable)e);
            return;
        }
        Display.getDefault().syncExec((Runnable)new Runnable() {
            @Override
            public void run() {
                browser.setUrl(String.format(string, token, WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage()));
                SWFWrapper.INSTANCE.addDelegateContainerListener(NetAuthenticationFrame.this.m_closeBigPointBrowserListener);
            }
        });
    }
    
    private static void onLoginSuccess() {
        if (WakfuGameEntity.getInstance().getCurrentLoginPhase() == LoginPhase.STEAM_LINK) {
            WakfuGameEntity.getInstance().setCurrentLoginPhase(LoginPhase.DISPATCH);
        }
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new ClientProxiesRequestMessage());
        WakfuGameEntity.getInstance().removeFrame(UIAuthentificationFrame.getInstance());
        PropertiesProvider.getInstance().removeProperty("account.password");
    }
    
    private static void onClientPublicKey(final ClientPublicKeyMessage message) {
        final long salt = message.getSalt();
        final byte[] encodedPublicKey = message.getPublicKey();
        ConnectionEncryptionManager.INSTANCE.initialize(encodedPublicKey);
        final byte[] encryptedLoginAndPassword = WakfuGameEntity.getEncryptedLoginAndPassword(salt);
        switch (WakfuGameEntity.getInstance().getCurrentLoginPhase()) {
            case CONNECTION: {
                final ClientAuthenticationTokenMessage msg = new ClientAuthenticationTokenMessage();
                msg.setToken(NetAuthenticationFrame.m_wakfuToken);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
                break;
            }
            case STEAM_LINK: {
                NetAuthenticationFrame.m_logger.error((Object)"Pouet public key receive through steam link !!!");
                break;
            }
            case DISPATCH: {
                final Partner currentPartner = Partner.getCurrentPartner();
                final boolean useSteam = currentPartner.isEnableSteam();
                final boolean useOAuth = currentPartner.isUseOAuth() && PropertiesProvider.getInstance().getBooleanProperty("useOAuth");
                if (useSteam) {
                    final CSteamID steamID = SteamClientContext.INSTANCE.getSteamID();
                    final ClientDispatchAuthenticationSteamMessage msg2 = new ClientDispatchAuthenticationSteamMessage();
                    msg2.setSteamId(SteamUtils.serializeSteamId(steamID));
                    msg2.setTicket(SteamClientContext.INSTANCE.getClient().getAuthTicket());
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg2);
                    break;
                }
                if (useOAuth) {
                    final OAuthAuthenticationTokenRequestMessage dispatchTokenRequest = new OAuthAuthenticationTokenRequestMessage();
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(dispatchTokenRequest);
                    break;
                }
                final ClientDispatchAuthenticationMessage msg3 = new ClientDispatchAuthenticationMessage();
                msg3.setEncryptedLoginAndPassword(encryptedLoginAndPassword);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg3);
                break;
            }
        }
    }
    
    private byte[] getEncryptedCardQuestionAnswer(final long salt, final String cardQuestion, final String cardAnswer) {
        final byte[] question = StringUtils.toUTF8(cardQuestion);
        final byte questionLength = (byte)question.length;
        final byte[] answer = StringUtils.toUTF8(cardAnswer);
        final byte answerLength = (byte)answer.length;
        final ByteArray bb = new ByteArray();
        bb.putLong(salt);
        bb.put(questionLength);
        bb.put(question);
        bb.put(answerLength);
        bb.put(answer);
        final byte[] rawData = bb.toArray();
        return ConnectionEncryptionManager.INSTANCE.crypt(rawData);
    }
    
    private static byte[] getEncryptedSecurityQuestionAnswer(final long salt) {
        final byte[] question = StringUtils.toUTF8(WakfuGameEntity.getInstance().getSecurityCardQuestion());
        final byte questionLength = (byte)question.length;
        final byte[] answer = StringUtils.toUTF8(WakfuGameEntity.getInstance().getSecurityCardAnswer());
        final byte answerLength = (byte)answer.length;
        final ByteBuffer bb = ByteBuffer.allocate(9 + questionLength + 1 + answerLength);
        bb.putLong(salt);
        bb.put(questionLength);
        bb.put(question);
        bb.put(answerLength);
        bb.put(answer);
        final byte[] rawData = bb.array();
        return ConnectionEncryptionManager.INSTANCE.crypt(rawData);
    }
    
    private static void onLoginNicknameError(final byte errorCode) {
        String errorTranslationKey = "error.connection.nicknameNotSet";
        switch (errorCode) {
            case 13: {
                errorTranslationKey = "error.connection.nicknameNotSet";
                break;
            }
            case 14: {
                errorTranslationKey = "error.connection.nicknameInvalidContent";
                break;
            }
            case 15: {
                errorTranslationKey = "error.connection.nicknameDuplicate";
                break;
            }
            case 16: {
                errorTranslationKey = "error.connection.nicknameTooLong";
                break;
            }
            case 17: {
                errorTranslationKey = "error.connection.nicknameTooShort";
                break;
            }
            case 18: {
                errorTranslationKey = "error.connection.nicknameEqualsLogin";
                break;
            }
            case 19: {
                errorTranslationKey = "error.connection.nicknameInvalidFormat";
                break;
            }
        }
        Xulor.getInstance().load("nicknameChoiceDialog", Dialogs.getDialogPath("nicknameChoiceDialog"), 8448L, (short)10000);
        Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString(errorTranslationKey), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 1, 2);
    }
    
    protected final void loginError(final Message message, final String errorStringKey, final Object... errorStringParams) {
        WakfuGameEntity.getInstance().setLogin(null);
        WakfuGameEntity.getInstance().setPassword(null);
        ((FrameHandler)message.getHandler()).removeFrame(this);
        Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString(errorStringKey, errorStringParams), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 1, 2);
        WakfuGameEntity.getInstance().disconnectFromServer("Login Error");
        WakfuGameEntity.getInstance().pushFrame(UIAuthentificationFrame.getInstance());
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            NetAuthenticationFrame.m_wakfuToken = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetAuthenticationFrame.class);
        NetAuthenticationFrame.m_instance = new NetAuthenticationFrame();
        TIME_FORMAT = DateFormat.getTimeInstance(3);
        DATE_FORMAT = DateFormat.getDateInstance(3);
    }
}
