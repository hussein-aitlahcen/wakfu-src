package com.ankamagames.wakfu.client.ui.actions;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.framework.kernel.core.translator.*;
import com.ankamagames.wakfu.client.ui.protocol.message.connection.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.game.fight.history.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.util.rss.*;
import com.ankamagames.xulor2.component.text.builder.content.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.xulor2.component.text.document.part.*;
import com.ankamagames.wakfu.client.core.game.steam.*;
import com.ankamagames.steam.wrapper.*;
import com.ankamagames.framework.net.http.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.auth.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.wakfu.client.ui.protocol.message.news.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.news.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.subscription.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class Actions
{
    protected static final Logger m_logger;
    public static final String PACKAGE = "wakfu";
    private static Container m_container;
    
    public static void quit(final Event event) {
        UIMessage.send((short)16387);
    }
    
    public static void toggleLikevnAnkamaAuthVisibility(final Event event) {
        final boolean visible = PropertiesProvider.getInstance().getBooleanProperty("likevnAnkamaAuthVisible");
        PropertiesProvider.getInstance().setPropertyValue("likevnAnkamaAuthVisible", !visible);
    }
    
    public static void cancelSteamLink(final Event event) {
        Xulor.getInstance().unload("steamLinkAccountDialog");
        WakfuProgressMonitorManager.getInstance().done();
        WakfuGameEntity.getInstance().disconnectFromServer("Cancel Steam Link");
        WakfuGameEntity.getInstance().pushFrame(UIAuthentificationFrame.getInstance());
    }
    
    public static void moderatorRequestUserRequest(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UIModeratorChatFrame.getInstance())) {
            final String msgText = WakfuTranslator.getInstance().getString("moderator.error.chatStillOpened");
            final MessageBoxData data = new MessageBoxData(102, 0, msgText, 2L);
            Xulor.getInstance().msgBox(data);
        }
        else {
            final Message message = new ModeratorRequestUserRequestMessage();
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        }
    }
    
    public static void onLanguageSelected(final ListSelectionChangedEvent event) {
        if (event.getSelected()) {
            final WakfuTranslator.LanguageView view = (WakfuTranslator.LanguageView)event.getValue();
            final UIChangeLanguageRequestMessage message = new UIChangeLanguageRequestMessage();
            message.setLanguage(view.getLanguage());
            Worker.getInstance().pushMessage(message);
            WakfuClientInstance.getInstance().getGamePreferences().setValue(KeyPreferenceStoreEnum.LANGUAGE_PREFERENCE_KEY, view.getLanguage().getActualLocale().getLanguage());
        }
    }
    
    public static void setLanguage(final Event event, final String languageCode) {
        final UIChangeLanguageRequestMessage message = new UIChangeLanguageRequestMessage();
        message.setLanguage(Language.getLanguage(languageCode));
        Worker.getInstance().pushMessage(message);
        WakfuClientInstance.getInstance().getGamePreferences().setValue(KeyPreferenceStoreEnum.LANGUAGE_PREFERENCE_KEY, languageCode);
    }
    
    public static void selectServer(final Event event, final TextEditor accountTextEditor, final TextEditor passwordTextEditor) {
        selectServer(event, accountTextEditor, passwordTextEditor, null);
    }
    
    public static void selectServer(final Event event, final TextEditor accountTextEditor, final TextEditor passwordTextEditor, final ToggleButton rememberCheckBox) {
        if (event instanceof KeyEvent && ((KeyEvent)event).getKeyCode() != 10) {
            return;
        }
        final String login = accountTextEditor.getText();
        final String password = passwordTextEditor.getText();
        final boolean remember = rememberCheckBox != null && rememberCheckBox.getSelected();
        if (login != null && password != null && !login.isEmpty() && !password.isEmpty()) {
            final UISelectServerRequestMessage message = new UISelectServerRequestMessage();
            message.setLogin(login);
            message.setPassword(password);
            message.setRemember(remember);
            Worker.getInstance().pushMessage(message);
            WakfuSoundManager.getInstance().playGUISound(600002L);
        }
        else {
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("logon.invalidForm"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 1, 2);
        }
    }
    
    public static void openMenuDialog(final Event event) {
        UIMessage.send((short)16400);
    }
    
    public static void openReportBugDialog(final Event event) {
        UIMessage.send((short)16427);
    }
    
    public static void openAdminMonitorPanel(final Event e) {
        UIMessage.send((short)17721);
    }
    
    public static void openAdminCharacterEditor(final Event e) {
        UIMessage.send((short)17724);
    }
    
    public static void hackClientRights(final Event e) {
        final WakfuAccountInformationHandler accountInformationHandler = WakfuGameEntity.getInstance().getLocalPlayer().getAccountInformationHandler();
        final boolean debugginghackMode = !accountInformationHandler.isDebuggingHackMode();
        accountInformationHandler.setDebuggingHackMode(debugginghackMode);
        PropertiesProvider.getInstance().setPropertyValue("rightsHackMode", debugginghackMode);
    }
    
    public static void moderation(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UIModerationPanelFrame.INSTANCE)) {
            WakfuGameEntity.getInstance().removeFrame(UIModerationPanelFrame.INSTANCE);
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIModerationPanelFrame.INSTANCE);
        }
    }
    
    public static void closePrivateMessageDialog(final Event event) {
        UIMessage.send((short)19004);
    }
    
    public static boolean checkNameValidity(final String name) {
        final int[] localAdminRights = WakfuGameEntity.getInstance().getLocalAccount().getAdminRights();
        if (AdminRightHelper.checkRight(localAdminRights, AdminRightsEnum.PASS_THROUGH_NAME_CHECK) && name.length() <= 25 && name.length() >= 3) {
            return true;
        }
        final NameCheckerResult result = NameChecker.nameValidity(name);
        final char character = result.getCharacter();
        String errorText = null;
        switch (result.getResult()) {
            case ERROR_FORBIDDEN_NAME: {
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.forbiddenName");
                break;
            }
            case ERROR_INVALID_DASH_POSITION: {
                if (character != -1) {
                    errorText = WakfuTranslator.getInstance().getString("error.characterCreation.invalidDashPosition2", character);
                    break;
                }
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.invalidDashPosition");
                break;
            }
            case ERROR_TOO_MANY_CONSECUTIVE_CONSONANT: {
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooManyConsonant");
                break;
            }
            case ERROR_TOO_MANY_CONSECUTIVE_VOWEL: {
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooManyVowel");
                break;
            }
            case ERROR_NAME_TOO_LONG: {
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.nameTooLong");
                break;
            }
            case ERROR_NAME_TOO_SHORT: {
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.nameTooShort");
                break;
            }
            case ERROR_BAD_CHAR: {
                if (character != -1) {
                    errorText = WakfuTranslator.getInstance().getString("error.characterCreation.badChar2", String.valueOf(character));
                    break;
                }
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.badChar");
                break;
            }
            case ERROR_TOO_MANY_CONSECUTIVE_IDENTICAL: {
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooManyConsecutiveIdentical");
                break;
            }
            case ERROR_TOO_FEW_VOWEL_IN_PART: {
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooFewVowelInPart");
                break;
            }
            case ERROR_TOO_FEW_CONSONANT_IN_PART: {
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooFewConsonantInPart");
                break;
            }
            case ERROR_TOO_MANY_SPECIAL_IN_PART: {
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooManySpecialInPart");
                break;
            }
            case ERROR_PART_TOO_LONG: {
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.parTooLong");
                break;
            }
            case ERROR_TOO_MANY_SPECIAL: {
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooManySpecial");
                break;
            }
            case ERROR_NAME_WITH_BAD_CASE: {
                errorText = WakfuTranslator.getInstance().getString("error.characterCreation.nameWithBadCase");
                break;
            }
            default: {
                return true;
            }
        }
        Xulor.getInstance().msgBox(errorText, WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 1, 2);
        return false;
    }
    
    public static String formatNumber(final String number) {
        String result = "";
        if (number != null && number.length() > 0) {
            int count;
            for (count = 1; number.length() > 3 * count; ++count) {
                result = "." + number.substring(number.length() - 3 * count, number.length() - 3 * (count - 1)) + result;
            }
            result = number.substring(0, number.length() - 3 * (count - 1)) + result;
        }
        return result;
    }
    
    public static int parseStringNumber(String formatedNumber) {
        formatedNumber = formatedNumber.replaceAll("\\.", "");
        try {
            return Integer.parseInt(formatedNumber);
        }
        catch (Exception e) {
            Actions.m_logger.error((Object)"Exception", (Throwable)e);
            return Integer.MAX_VALUE;
        }
    }
    
    public static void openItemDetailWindow(final ItemEvent itemClickEvent, final Window window) {
        if (itemClickEvent.getButton() == 3) {
            Item item = null;
            final Object itemValue = itemClickEvent.getItemValue();
            if (itemValue instanceof MerchantInventoryItem) {
                item = ((MerchantInventoryItem)itemValue).getItem();
            }
            else if (itemValue instanceof ReferenceItemFieldProvider) {
                item = ReferenceItemManager.getInstance().getDefaultItem(((ReferenceItemFieldProvider)itemValue).getReferenceItem().getId());
            }
            sendOpenCloseItemDetailMessage((window == null) ? null : window.getElementMap().getId(), item);
        }
    }
    
    public static void sendOpenCloseItemDetailMessage(final String parentId, final ReferenceItem refItem) {
        final Item item = new Item(refItem.getId());
        item.initializeWithReferenceItem(refItem);
        item.setQuantity((short)1);
        sendOpenCloseItemDetailMessage(parentId, item);
    }
    
    public static void sendOpenCloseItemDetailMessage(final String parentId, final Item item) {
        AbstractUIDetailMessage msg;
        if (item.hasPet()) {
            msg = new UIPetDetailMessage(new PetDetailDialogView(item));
            msg.setId(16430);
        }
        else {
            msg = new UIItemDetailMessage();
            msg.setId(16415);
            msg.setItem(item);
        }
        msg.setParentWindowId(parentId);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void openRefItemDetailWindow(final Event e, final ReferenceItem item) {
        sendOpenCloseItemDetailMessage(e.getTarget().getElementMap().getRootId(), item);
    }
    
    public static void openItemDetailWindow(final ItemEvent itemClickEvent) {
        openItemDetailWindow(itemClickEvent, null);
    }
    
    public static boolean openLinkInBrowser(final Event e, final RSSChannelFieldProvider channel) {
        if (!openLinkInBrowser(e)) {
            openInBrowser(channel.getChannel().getLink());
        }
        return true;
    }
    
    public static boolean openLinkInBrowser(final Event e, final RSSImageFieldProvider image) {
        if (!openLinkInBrowser(e)) {
            openInBrowser(image.getImage().getLink());
        }
        return true;
    }
    
    public static boolean openLinkInBrowser(final Event e, final RSSItemFieldProvider item) {
        if (!openLinkInBrowser(e)) {
            openInBrowser(item.getItem().getLink());
        }
        return true;
    }
    
    public static boolean openLinkInBrowser(final Event event) {
        if (event.getTarget() instanceof TextView) {
            final TextView textView = event.getTarget();
            final AbstractContentBlock block = textView.getBlockUnderMouse();
            if (block != null && block.getType() == AbstractContentBlock.BlockType.TEXT) {
                final AbstractDocumentPart part = block.getDocumentPart();
                if (part == null) {
                    return false;
                }
                if (part.getType() == DocumentPartType.TEXT) {
                    final String partId = ((TextDocumentPart)part).getId();
                    if (partId != null) {
                        openInBrowser(partId);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private static void openInBrowser(final String url) {
        if (SteamClientContext.INSTANCE.isInit()) {
            SteamApi.SteamFriends().ActivateGameOverlayToWebPage(url);
        }
        else {
            BrowserManager.openUrlInBrowser(url);
        }
    }
    
    public static void OAuthAuthentification(final Event e) {
        final UIMessage message = new UIMessage();
        message.setId(19411);
        Worker.getInstance().pushMessage(message);
        WakfuSoundManager.getInstance().playGUISound(600002L);
    }
    
    public static void createAccount(final Event e) {
        try {
            final String accountCreationUrl = WakfuConfiguration.getInstance().getString("accountCreationUrl");
            final String url = String.format(accountCreationUrl, WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage().toLowerCase());
            openInBrowser(url);
        }
        catch (PropertyException e2) {
            e2.printStackTrace();
        }
    }
    
    public static void linkSteamAccount(final Event e) {
        try {
            final String linkSteamAccountUrl = WakfuConfiguration.getInstance().getString("linkSteamAccountUrl");
            ICEAuthFrame.INSTANCE.getToken(new ICEAuthListener() {
                @Override
                public void onToken(final String token) {
                    final String url = String.format(linkSteamAccountUrl, WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage().toLowerCase(), token);
                    openInBrowser(url);
                }
                
                @Override
                public void onError() {
                    Actions.m_logger.error((Object)"Impossible de linker le compte, on n'a pas pu r\u00e9cup\u00e9rer de token ICE");
                }
            });
        }
        catch (PropertyException e2) {
            e2.printStackTrace();
        }
    }
    
    public static void checkAntiAddictionAccount(final Event e) {
        try {
            final String antiAddictionAccountCheckUrl = WakfuConfiguration.getInstance().getString("antiAddictionAccountCheckURL");
            final String url = String.format(antiAddictionAccountCheckUrl, WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage());
            openInBrowser(url);
        }
        catch (PropertyException e2) {
            e2.printStackTrace();
        }
    }
    
    public static void validateNickname(final Event e, final TextEditor nickNameEditor) {
        final String nickName = nickNameEditor.getText();
        if (nickName.length() < 3) {
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.connection.nicknameTooShort"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 1, 2);
        }
        else if (nickName.equalsIgnoreCase(WakfuGameEntity.getInstance().getLogin())) {
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.connection.nicknameEqualsLogin"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 1, 2);
        }
        else {
            final UIMessage msg = new UIMessage();
            msg.setId(16392);
            msg.setStringValue(nickName);
            Worker.getInstance().pushMessage(msg);
            Xulor.getInstance().unload("nicknameChoiceDialog");
        }
    }
    
    public static void setLeftArrowVisible(final Event e) {
        if (!UIAuthentificationFrame.getInstance().getNewsDisplayer().hasPreviousNew()) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("logonDialog");
        final Widget cont = (Widget)map.getElement("leftArrowContainer");
        final Widget img = (Widget)map.getElement("leftArrowImage");
        cont.setVisible(true);
        img.setVisible(true);
        fade(true, cont);
        fade(true, img);
    }
    
    public static void setLeftArrowInvisible(final Event e) {
        setLeftArrowInvisible(e, false);
    }
    
    public static void setLeftArrowInvisible(final Event e, final boolean force) {
        if (!force && !UIAuthentificationFrame.getInstance().getNewsDisplayer().hasPreviousNew()) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("logonDialog");
        final Widget cont = (Widget)map.getElement("leftArrowContainer");
        final Widget img = (Widget)map.getElement("leftArrowImage");
        cont.setVisible(true);
        img.setVisible(true);
        fade(false, cont);
        fade(false, img);
    }
    
    public static void setRightArrowVisible(final Event e) {
        if (!UIAuthentificationFrame.getInstance().getNewsDisplayer().hasNextNew()) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("logonDialog");
        final Widget cont = (Widget)map.getElement("rightArrowContainer");
        final Widget img = (Widget)map.getElement("rightArrowImage");
        cont.setVisible(true);
        img.setVisible(true);
        fade(true, cont);
        fade(true, img);
    }
    
    public static void setRightArrowInvisible(final Event e) {
        setRightArrowInvisible(e, false);
    }
    
    public static void setRightArrowInvisible(final Event e, final boolean force) {
        if (!force && !UIAuthentificationFrame.getInstance().getNewsDisplayer().hasNextNew()) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("logonDialog");
        final Widget cont = (Widget)map.getElement("rightArrowContainer");
        final Widget img = (Widget)map.getElement("rightArrowImage");
        cont.setVisible(true);
        img.setVisible(true);
        fade(false, cont);
        fade(false, img);
    }
    
    private static void fade(final boolean fadeIn, final Widget m) {
        int aValue;
        int bValue;
        if (fadeIn) {
            aValue = Color.WHITE_ALPHA.get();
            bValue = Color.WHITE.get();
        }
        else {
            aValue = Color.WHITE.get();
            bValue = Color.WHITE_ALPHA.get();
        }
        if (fadeIn) {
            WakfuSoundManager.getInstance().windowFadeIn();
        }
        else {
            WakfuSoundManager.getInstance().windowFadeOut();
        }
        final Color c = new Color(aValue);
        final Color c2 = new Color(bValue);
        m.getAppearance().removeTweensOfType(ModulationColorTween.class);
        final AbstractTween t = new ModulationColorTween(c, c2, m.getAppearance(), 0, 500, 1, false, TweenFunction.PROGRESSIVE);
        m.getAppearance().addTween(t);
    }
    
    public static void togglePlayPauseVideo(final Event e) {
        UIMessage.send((short)16111);
    }
    
    public static void selectNew(final Event e, final NewsItemView newsItemView) {
        final UISelectNewMessage uiMessage = new UISelectNewMessage(newsItemView);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void selectNextNew(final Event e) {
        UIMessage.send((short)16114);
    }
    
    public static void selectPreviousNew(final Event e) {
        UIMessage.send((short)16113);
    }
    
    public static void setVideoVolume(final SliderMovedEvent sliderMovedEvent, final Drawer d) {
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setId(16115);
        uiMessage.setFloatValue(sliderMovedEvent.getValue());
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void setVideoProgression(final MouseEvent clickEvent) {
        final UIMessage uiMessage = new UIMessage();
        if (!(clickEvent.getTarget() instanceof ProgressBar)) {
            return;
        }
        final ProgressBar progressBar = clickEvent.getTarget();
        final int width = progressBar.getWidth();
        if (width == 0) {
            return;
        }
        final float percent = clickEvent.getX(progressBar) / width;
        uiMessage.setId(16119);
        uiMessage.setFloatValue(percent);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void seek(final Event event, final ProgressBar progressBar) {
        final MouseEvent e = (MouseEvent)event;
        final int x = e.getX(progressBar);
        final int y = e.getY(progressBar);
        progressBar.setValue(x / progressBar.getWidth());
    }
    
    public static void mouseOverQuality(final Event e, final Container container) {
        container.setVisible(true);
        Actions.m_container = container;
    }
    
    public static void mouseOutQuality(final Event e, final Container container) {
        container.setVisible(false);
    }
    
    public static void mouseOverNew(final ItemEvent e) {
        PropertiesProvider.getInstance().setPropertyValue("overNew", e.getItemValue());
    }
    
    public static void mouseOutNew(final ItemEvent e) {
        PropertiesProvider.getInstance().setPropertyValue("overNew", null);
    }
    
    public static void selectQuality(final ItemEvent itemEvent) {
        if (Actions.m_container != null) {
            Actions.m_container.setVisible(false);
            Actions.m_container = null;
        }
        final VideoQualityView videoQuality = (VideoQualityView)itemEvent.getItemValue();
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setId(16116);
        uiMessage.setIntValue(videoQuality.getQuality());
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void muteVideo(final Event e) {
        UIMessage.send((short)16117);
    }
    
    public static void onMouseOutDrawer(final MouseEvent e, final Drawer d, final Widget source, final Container popupContainer) {
        if (!d.isPopupIsBeingDisplayed()) {
            return;
        }
        final Widget widget = MasterRootContainer.getInstance().getWidget(e.getScreenX(), e.getScreenY());
        if (source.equals(widget)) {
            return;
        }
        if (widget == popupContainer || widget.hasInParentHierarchy(popupContainer)) {
            return;
        }
        XulorActions.switchDrawer(e, d);
    }
    
    public static void displayQuests(final Event e) {
        ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("verticalFollowedAchievementsDialog");
        if (map == null) {
            map = Xulor.getInstance().getEnvironment().getElementMap("followedAchievementsDialog");
        }
        if (map == null) {
            return;
        }
        final Window w = (Window)map.getElement("window");
        final int northernBorderY = w.getY() + w.getHeight();
        w.setVisible(false);
        final WakfuGamePreferences pref = WakfuClientInstance.getInstance().getGamePreferences();
        final boolean displayQuests = !PropertiesProvider.getInstance().getBooleanProperty("followedQuestsDisplay");
        pref.setValue(WakfuKeyPreferenceStoreEnum.FOLLOWED_QUESTS_DISPLAY, displayQuests);
        AchievementUIHelper.displayFollowedAchievements();
        w.addWindowPostProcessedListener(new WindowPostProcessedListener() {
            boolean m_positionChanged = false;
            
            @Override
            public void windowPostProcessed() {
                if (this.m_positionChanged) {
                    w.setVisible(true);
                    w.removeWindowPostProcessedListener(this);
                }
                else {
                    this.m_positionChanged = true;
                    w.setY(northernBorderY - w.getHeight());
                }
            }
        });
    }
    
    public static void setVideoToFullScreen(final Event e) {
        UIMessage.send((short)16118);
    }
    
    public static void browser(final Event e) {
        SWFWrapper.INSTANCE.toggleDisplay(KrosmozGame.BROWSER);
    }
    
    public static void shop(final Event e) {
        UIWebShopFrame.getInstance().openCloseShopDialog();
    }
    
    public static void subscriptionEndTeleport(final Event e) {
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new TeleportOutOfInstanceRequestMessage());
        Xulor.getInstance().unload("subscriptionEndedDialog");
    }
    
    public static void connectWithSteam(final Event e) {
        final UIMessage message = new UIMessage();
        message.setId(19410);
        Worker.getInstance().pushMessage(message);
        WakfuSoundManager.getInstance().playGUISound(600002L);
    }
    
    public static void pvp(final Event e) {
        UIMessage.send((short)19420);
    }
    
    public static void nationPvpLadderDialog(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UINationPvpLadderFrame.INSTANCE)) {
            WakfuGameEntity.getInstance().removeFrame(UINationPvpLadderFrame.INSTANCE);
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UINationPvpLadderFrame.INSTANCE);
        }
    }
    
    public static void aptitudesBonusDialog(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UIAptitudeBonusFrame.INSTANCE)) {
            WakfuGameEntity.getInstance().removeFrame(UIAptitudeBonusFrame.INSTANCE);
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIAptitudeBonusFrame.INSTANCE);
        }
    }
    
    public static void partySearch(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UIPartySearchFrame.INSTANCE)) {
            WakfuGameEntity.getInstance().removeFrame(UIPartySearchFrame.INSTANCE);
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIPartySearchFrame.INSTANCE);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)Actions.class);
    }
}
