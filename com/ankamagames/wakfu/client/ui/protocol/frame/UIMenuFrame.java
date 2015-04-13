package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.system.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.common.account.antiAddiction.*;
import com.ankamagames.wakfu.client.core.news.*;
import com.ankamagames.wakfu.common.account.*;

public class UIMenuFrame implements MessageFrame
{
    private static UIMenuFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIMenuFrame getInstance() {
        return UIMenuFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16393: {
                final String preMsgText = WakfuTranslator.getInstance().getString("contactModerator.ask");
                final MessageBoxData preMessage = new MessageBoxData(102, 0, preMsgText, 65542L);
                final MessageBoxControler preMessageControler = Xulor.getInstance().msgBox(preMessage);
                preMessageControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 2) {
                            if (!userEntry.isEmpty()) {
                                if (userEntry.length() <= 220) {
                                    final Message msg = new ModeratorHelpRequestMessage(userEntry);
                                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
                                    final String msgText = WakfuTranslator.getInstance().getString("contactModerator.waiting");
                                    final MessageBoxData data = new MessageBoxData(102, 0, msgText, 2L);
                                    Xulor.getInstance().msgBox(data);
                                }
                                else {
                                    final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("contactModerator.tooLong"));
                                    chatMessage.setPipeDestination(3);
                                    ChatManager.getInstance().pushMessage(chatMessage);
                                }
                            }
                            else {
                                final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("contactModerator.tooShort"));
                                chatMessage.setPipeDestination(3);
                                ChatManager.getInstance().pushMessage(chatMessage);
                            }
                        }
                    }
                });
                WakfuGameEntity.getInstance().removeFrame(this);
                return false;
            }
            case 16402: {
                WakfuGameEntity.getInstance().pushFrame(UIOptionsFrame.getInstance());
                return false;
            }
            case 16386: {
                WakfuGameEntity.getInstance().logoff();
                return false;
            }
            case 16387: {
                WakfuGameEntity.getInstance().quit();
                return false;
            }
            case 16401: {
                WakfuGameEntity.getInstance().removeFrame(this);
                return false;
            }
            case 16388: {
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.changeCharacter"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            WakfuGameEntity.getInstance().removeFrame(UIMenuFrame.this);
                            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                            if (localPlayer != null && (localPlayer.getCurrentOccupation() == null || localPlayer.getCurrentOccupation().getOccupationTypeId() != 4)) {
                                final AnimationEndedListener listener = new AnimationEndedListener() {
                                    @Override
                                    public void animationEnded(final AnimatedElement element) {
                                        element.removeAnimationEndedListener(this);
                                        UIMenuFrame.goToCharacterList();
                                    }
                                };
                                WakfuGameEntity.getInstance().playByeEmote(localPlayer, listener);
                            }
                            else {
                                UIMenuFrame.goToCharacterList();
                            }
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
    
    public static void goToCharacterList() {
        final LocalPlayerCharacter lpc = WakfuGameEntity.getInstance().getLocalPlayer();
        if (lpc != null) {
            lpc.getShortcutBarManager().sendShortcutsUpdateMessageIfNeeded();
        }
        WakfuClientInstance.getInstance().partialCleanUp();
        WakfuGameEntity.getInstance().pushFrame(NetSystemNotificationsAndPingFrame.getInstance());
        WakfuGameEntity.getInstance().pushFrame(UISystemBarFrame.getInstance());
        WakfuGameEntity.getInstance().pushFrame(NetChatFrame.getInstance());
        WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).beginTask(WakfuTranslator.getInstance().getString("charactersLoading.progress"), 0);
        NetCharacterChoiceFrame.getInstance().enableLoadUI(true);
        WakfuGameEntity.getInstance().pushFrame(NetCharacterChoiceFrame.getInstance());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new GoToCharacterSelectionRequestMessage());
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
        if (!isAboutToBeAdded) {
            if (WakfuGameEntity.getInstance().hasFrame(UIAuthentificationFrame.getInstance())) {
                final NewsDisplayer newsDisplayer = UIAuthentificationFrame.getInstance().getNewsDisplayer();
                if (newsDisplayer != null) {
                    final NewsItemView currentNew = newsDisplayer.getCurrentNew();
                    if (currentNew != null && currentNew.hasVideo()) {
                        final NewsVideoElementView videoElement = currentNew.getVideoElement();
                        if (videoElement != null && videoElement.isVideoPlaying()) {
                            UIMessage.send((short)16111);
                        }
                    }
                }
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("menuDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIMenuFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("isConnected", WakfuGameEntity.getInstance().isLogged());
            Xulor.getInstance().load("menuDialog", Dialogs.getDialogPath("menuDialog"), 256L, (short)26000);
            Xulor.getInstance().putActionClass("wakfu.menu", MenuDialogActions.class);
            WakfuSoundManager.getInstance().playGUISound(600023L);
            OptionsDialogActions.setSelectedTabIndex(0);
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            boolean antiAddictionEnabled = false;
            if (localPlayer != null) {
                final WakfuAccountInformationHandler accountInformationHandler = localPlayer.getAccountInformationHandler();
                antiAddictionEnabled = (SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.ANTI_ADDICTION_ENABLE) && accountInformationHandler.getAntiAddictionLevel() == AntiAddictionLevel.UNKNOWN);
            }
            PropertiesProvider.getInstance().setPropertyValue("antiAddictionEnabled", antiAddictionEnabled);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            PropertiesProvider.getInstance().removeProperty("isConnected");
            Xulor.getInstance().unload("menuDialog");
            Xulor.getInstance().removeActionClass("wakfu.menu");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            WakfuSoundManager.getInstance().playGUISound(600024L);
            OptionsDialogActions.setSelectedTabIndex(2);
        }
    }
    
    static {
        UIMenuFrame.m_instance = new UIMenuFrame();
    }
}
