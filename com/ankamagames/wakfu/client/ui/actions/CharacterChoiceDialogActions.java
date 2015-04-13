package com.ankamagames.wakfu.client.ui.actions;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.protocol.message.character.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.clientToServer.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.characterChoiceSlot.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.tween.*;

@XulorActionsTag
public class CharacterChoiceDialogActions
{
    public static final String PACKAGE = "wakfu.characterChoice";
    private static Image m_image;
    protected static final Logger m_logger;
    private static Image m_fadingInImage;
    private static Image m_fadingOutImage;
    private static final TweenEventListener m_tweenEventListener;
    
    public static void selectCharacter(final Event event, final PlayerCharacter playerInfo, final Image image) {
        if (playerInfo.equals(LocalCharacterInfosManager.getInstance().getSelectedCharacterInfo())) {
            return;
        }
        final UIPlayerInfoMessage message = new UIPlayerInfoMessage();
        message.setPlayerInfo(playerInfo);
        message.setId(16510);
        Worker.getInstance().pushMessage(message);
        if (CharacterChoiceDialogActions.m_image != null && !CharacterChoiceDialogActions.m_image.isUnloading()) {
            CharacterChoiceDialogActions.m_image.getAppearance().setModulationColor(Color.WHITE_ALPHA);
        }
        CharacterChoiceDialogActions.m_image = image;
    }
    
    public static void setImage(final Image image) {
        CharacterChoiceDialogActions.m_image = image;
    }
    
    public static void deleteCharacter(final Event event, final PlayerCharacter playerInfo) {
        final UIPlayerInfoMessage message = new UIPlayerInfoMessage();
        message.setPlayerInfo(playerInfo);
        message.setId(16511);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void createNewCharacter(final Event event) {
        UIMessage.send((short)16413);
    }
    
    public static void enterWorld(final Event event, final PlayerCharacter playerInfo) {
        if (event instanceof KeyEvent) {
            final KeyEvent keyEvent = (KeyEvent)event;
            switch (keyEvent.getKeyCode()) {
                case 10: {
                    break;
                }
                default: {
                    return;
                }
            }
        }
        if (playerInfo != null) {
            final UIPlayerInfoMessage message = new UIPlayerInfoMessage();
            message.setPlayerInfo(playerInfo);
            message.setId(16514);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void enterWorldDoubleClick(final Event event, final PlayerCharacter playerInfo) {
        if (playerInfo != null) {
            final UIPlayerInfoMessage message = new UIPlayerInfoMessage();
            message.setPlayerInfo(playerInfo);
            message.setId(16514);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void cancelCharacterChoice(final Event event) {
        WakfuGameEntity.getInstance().gotoWorldSelection();
    }
    
    public static void onMouseOverDeletionButton(final Event event, final PlayerCharacter playerInfo, final AnimatedElementViewer animatedElementViewer, final PopupElement popupElement) {
        if (playerInfo != null) {
            animatedElementViewer.setAnimName("AnimEmote-Defaite");
            XulorActions.popup(event, popupElement);
        }
    }
    
    public static void onMouseOutDeletionButton(final Event event, final PlayerCharacter playerInfo, final AnimatedElementViewer animatedElementViewer, final PopupElement popupElement) {
        if (playerInfo != null && !UICharacterChoiceFrame.getInstance().isAskForDelete()) {
            animatedElementViewer.setAnimName(playerInfo.getActor().getStaticAnimationKey());
            XulorActions.closePopup(event, popupElement);
        }
    }
    
    public static void onMouseOverCharacter(final Event event, final PlayerCharacter playerInfo, final AnimatedElementViewer animatedElementViewer, final Image image) {
        if (playerInfo != PropertiesProvider.getInstance().getObjectProperty("characterChoice.selectedCharacter")) {
            animatedElementViewer.setAnimName("AnimEmote-Coucou");
        }
        fadeImage(true, image);
    }
    
    public static void onMouseOutCharacter(final Event event, final PlayerCharacter playerInfo, final AnimatedElementViewer animatedElementViewer, final Image image) {
        if (playerInfo != PropertiesProvider.getInstance().getObjectProperty("characterChoice.selectedCharacter")) {
            animatedElementViewer.setAnimName(playerInfo.getActor().getStaticAnimationKey());
        }
        fadeImage(false, image);
    }
    
    private static void fadeImage(final boolean in, final Image image) {
        if (image == CharacterChoiceDialogActions.m_image) {
            return;
        }
        final Color a = new Color(in ? Color.WHITE_ALPHA : Color.WHITE);
        final Color b = new Color(in ? Color.WHITE : Color.WHITE_ALPHA);
        image.setVisible(true);
        image.removeTweensOfType(ModulationColorTween.class);
        final ModulationColorTween tw = new ModulationColorTween(a, b, image, 0, 500, 1, false, TweenFunction.PROGRESSIVE);
        tw.addTweenEventListener(CharacterChoiceDialogActions.m_tweenEventListener);
        image.addTween(tw);
        if (in) {
            if (CharacterChoiceDialogActions.m_fadingInImage != null) {
                fadeImage(false, CharacterChoiceDialogActions.m_fadingInImage);
                cleanFadingInImage();
            }
            CharacterChoiceDialogActions.m_fadingInImage = image;
        }
        else {
            if (CharacterChoiceDialogActions.m_fadingOutImage != null) {
                cleanFadingOutImage();
            }
            CharacterChoiceDialogActions.m_fadingOutImage = image;
            if (CharacterChoiceDialogActions.m_fadingInImage != null && CharacterChoiceDialogActions.m_fadingInImage == CharacterChoiceDialogActions.m_fadingOutImage) {
                cleanFadingInImage();
            }
        }
    }
    
    private static void cleanFadingInImage() {
        if (CharacterChoiceDialogActions.m_fadingInImage == null) {
            return;
        }
        CharacterChoiceDialogActions.m_fadingInImage.removeTweensOfType(ModulationColorTween.class);
        final DecoratorAppearance decoratorAppearance = CharacterChoiceDialogActions.m_fadingInImage.getAppearance();
        if (decoratorAppearance != null) {
            decoratorAppearance.setModulationColor(Color.WHITE);
        }
        CharacterChoiceDialogActions.m_fadingInImage = null;
    }
    
    private static void cleanFadingOutImage() {
        if (CharacterChoiceDialogActions.m_fadingOutImage == null) {
            return;
        }
        CharacterChoiceDialogActions.m_fadingOutImage.removeTweensOfType(ModulationColorTween.class);
        CharacterChoiceDialogActions.m_fadingOutImage.setVisible(false);
        final DecoratorAppearance decoratorAppearance = CharacterChoiceDialogActions.m_fadingOutImage.getAppearance();
        if (decoratorAppearance != null) {
            decoratorAppearance.setModulationColor(Color.WHITE_ALPHA);
        }
        CharacterChoiceDialogActions.m_fadingOutImage = null;
    }
    
    public static void onMouseOverAnimNewCharacter(final Event event, final AnimatedElementViewer animatedElementViewer) {
    }
    
    public static void onMouseOutAnimNewCharacter(final Event event, final AnimatedElementViewer animatedElementViewer) {
    }
    
    public static void validRename(final Event event, final TextEditor textEditor) {
        String newName = textEditor.getText();
        final CharacterInfo characterInfo = (CharacterInfo)PropertiesProvider.getInstance().getObjectProperty("characterChoice.selectedCharacter");
        if (event == null || event.getType() == Events.MOUSE_CLICKED || (event instanceof KeyEvent && ((KeyEvent)event).getKeyCode() == 10)) {
            newName = NameChecker.doNameCorrection(textEditor.getText());
            if (CharacterCreationDialogActions.validateCharacterCreationForm(textEditor)) {
                final long characterId = characterInfo.getId();
                CharacterChoiceDialogActions.m_logger.info((Object)("Demande de renommage du personnage characterId=" + characterId + " en " + newName));
                final CharacterRenameRequestMessage characterRenameRequestMessage = new CharacterRenameRequestMessage();
                characterRenameRequestMessage.setCharacterId(characterId);
                characterRenameRequestMessage.setCharacterName(newName);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(characterRenameRequestMessage);
            }
            else {
                CharacterChoiceDialogActions.m_logger.info((Object)"Formulaire invalide");
            }
        }
        else if (event instanceof KeyEvent) {
            PropertiesProvider.getInstance().setPropertyValue("renameCharater.dirty", !newName.equals(characterInfo.getName()));
        }
    }
    
    public static void buySlot(final Event e, final Article article) {
        UIWebShopFrame.getInstance().openArticleDialog(article);
    }
    
    public static void startScrollLeft(final Event e) {
        UICharacterChoiceFrame.getInstance().setScrollMode(ListScroller.ScrollMode.LEFT);
    }
    
    public static void startScrollRight(final Event e) {
        UICharacterChoiceFrame.getInstance().setScrollMode(ListScroller.ScrollMode.RIGHT);
    }
    
    public static void stopScroll(final Event e) {
        UICharacterChoiceFrame.getInstance().setScrollMode(ListScroller.ScrollMode.STOPPED);
    }
    
    public static void scrollTo(final ItemEvent e, final List list) {
        final AbstractCharacterChoiceSlot slot = (AbstractCharacterChoiceSlot)e.getItemValue();
        list.removeTweensOfType(ListOffsetTween.class);
        list.addTween(new ListOffsetTween(list.getOffset(), MathHelper.clamp(slot.getOffset(), 0, list.size() - 5), list, 0, 500, TweenFunction.PROGRESSIVE));
    }
    
    static {
        m_logger = Logger.getLogger((Class)CharacterChoiceDialogActions.class);
        CharacterChoiceDialogActions.m_fadingInImage = null;
        CharacterChoiceDialogActions.m_fadingOutImage = null;
        m_tweenEventListener = new TweenEventListener() {
            @Override
            public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                switch (e) {
                    case TWEEN_ENDED: {
                        if (tw.getTweenClient() == CharacterChoiceDialogActions.m_fadingInImage) {
                            cleanFadingInImage();
                            break;
                        }
                        if (tw.getTweenClient() == CharacterChoiceDialogActions.m_fadingOutImage) {
                            cleanFadingOutImage();
                            break;
                        }
                        break;
                    }
                }
            }
        };
    }
}
