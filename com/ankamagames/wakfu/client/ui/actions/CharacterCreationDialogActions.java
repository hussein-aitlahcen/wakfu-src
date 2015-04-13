package com.ankamagames.wakfu.client.ui.actions;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.name.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.ui.protocol.message.character.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

@XulorActionsTag
public class CharacterCreationDialogActions
{
    public static final String PACKAGE = "wakfu.characterCreation";
    private static boolean m_createCharacterFlag;
    protected static final Logger m_logger;
    private static PlayerCharacter m_playerCharacter;
    public static final int DIRECTION_INDEX_MIN = 0;
    public static final int DIRECTION_INDEX_MAX = 7;
    
    public static void setCreateCharacterFlag(final boolean createCharacterFlag) {
        CharacterCreationDialogActions.m_createCharacterFlag = createCharacterFlag;
    }
    
    public static void cancelCharacterCreation(final Event event) {
        UIMessage.send((short)16513);
    }
    
    public static boolean validateCharacterCreationForm(final TextEditor textEditor) {
        if (textEditor != null) {
            final String playerInfoName = textEditor.getText();
            final String capitalizedPlayerInfoName = NameChecker.doNameCorrection(playerInfoName);
            if (!capitalizedPlayerInfoName.equals(playerInfoName)) {
                textEditor.setTextImmediate(capitalizedPlayerInfoName);
            }
            return Actions.checkNameValidity(capitalizedPlayerInfoName);
        }
        return false;
    }
    
    public static void stopCharacterNameTween(final Event e) {
        UICharacterCreationFrame.getInstance().stopCharacterNameTextTween();
    }
    
    public static void createCharacter(final Event event, final TextEditor textEditor) {
        stopCharacterNameTween(event);
        if (event == null || event.getType() == Events.MOUSE_CLICKED || (event instanceof KeyEvent && ((KeyEvent)event).getKeyCode() == 10)) {
            if (validateCharacterCreationForm(textEditor)) {
                if (!CharacterCreationDialogActions.m_createCharacterFlag) {
                    final UIPlayerInfoMessage message = new UIPlayerInfoMessage();
                    message.setStringValue(textEditor.getText());
                    message.setId(16512);
                    Worker.getInstance().pushMessage(message);
                    CharacterCreationDialogActions.m_createCharacterFlag = true;
                }
            }
            else {
                CharacterCreationDialogActions.m_logger.info((Object)"Formulaire invalide");
            }
        }
    }
    
    public static void setCharacterSex(final SelectionChangedEvent event) {
        if (event.isSelected()) {
            final RadioButton button = event.getTarget();
            final UIPlayerInfoMessage message = new UIPlayerInfoMessage();
            message.setByteValue(Byte.valueOf(button.getValue()));
            message.setId(16516);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void setCharacterHairColorIndex(final Event event, final Integer hairColorIndex, final Integer hairColorFactor) {
        final UIPlayerInfoMessage message = new UIPlayerInfoMessage();
        message.setShortValue(MathHelper.getShortFromTwoBytes((byte)(Object)hairColorIndex, (byte)(Object)hairColorFactor));
        message.setId(16517);
        Worker.getInstance().pushMessage(message);
        final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap("characterCreationDialog");
        final Container eastContainer = (Container)elementMap.getElement("eastContainer");
        final Container colorFactorContainer = (Container)elementMap.getElement("colorFactorContainer");
        final List list = (List)elementMap.getElement("hairColorsList");
        colorFactorContainer.setY(list.getY(eastContainer) + list.getHeight() + 5);
        colorFactorContainer.setX(list.getX(eastContainer) - 10);
    }
    
    public static void setCharacterSkinColorIndex(final Event event, final Integer skinColorIndex, final Integer skinColorFactor) {
        final UIPlayerInfoMessage message = new UIPlayerInfoMessage();
        message.setShortValue(MathHelper.getShortFromTwoBytes((byte)(Object)skinColorIndex, (byte)(Object)skinColorFactor));
        message.setId(16518);
        Worker.getInstance().pushMessage(message);
        final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap("characterCreationDialog");
        final Container eastContainer = (Container)elementMap.getElement("eastContainer");
        final Container colorFactorContainer = (Container)elementMap.getElement("colorFactorContainer");
        final List list = (List)elementMap.getElement("skinColorsList");
        colorFactorContainer.setY(list.getY(eastContainer) + list.getHeight() + 5);
        colorFactorContainer.setX(list.getX(eastContainer) - 10);
    }
    
    public static void setCharacterPupilColorIndex(final Event event, final Integer pupilColorIndex, final Integer pupilColorFactor) {
        final UIPlayerInfoMessage message = new UIPlayerInfoMessage();
        message.setShortValue(MathHelper.getShortFromTwoBytes((byte)(Object)pupilColorIndex, (byte)(Object)pupilColorFactor));
        message.setId(16524);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void setRandomCharacterName(final Event event, final TextEditor textEditor) {
        final PlayerCharacter playerCharacter = (PlayerCharacter)PropertiesProvider.getInstance().getObjectProperty("characterCreation.editablePlayerInfo");
        playerCharacter.setName(WakfuNameGenerator.getInstance().getRandomName());
        textEditor.setText(playerCharacter.getName());
        UICharacterCreationFrame.getInstance().stopCharacterNameTextTween();
    }
    
    public static void openMainSpellDescription(final ItemEvent event, final List list, final PopupElement popup) {
        if (event.getItemValue() != null && event.getItemValue() instanceof Spell) {
            final PropertiesProvider propertiesProvider = Xulor.getInstance().getEnvironment().getPropertiesProvider();
            propertiesProvider.setPropertyValue("characterCreation.describedMainSpell", event.getItemValue());
            XulorActions.popup(popup, list);
        }
    }
    
    public static void validDetailedColorChoice(final Event event) {
        final PropertiesProvider propertiesProvider = PropertiesProvider.getInstance();
        propertiesProvider.setPropertyValue("characterCreation.skinColorChosen", null);
        propertiesProvider.setPropertyValue("characterCreation.hairColorChosen", null);
        propertiesProvider.setPropertyValue("characterCreation.pupilColorChosen", null);
    }
    
    public static void changeDirectionClockWise(final MouseEvent mouseEvent, final AnimatedElementViewer objViewer) {
        if (mouseEvent.getButton() != 1) {
            return;
        }
        if (UICharacterCreationFrame.getInstance().isLockControls()) {
            return;
        }
        final int direction = (objViewer.getDirection() + 1 > 7) ? 0 : (objViewer.getDirection() + 1);
        objViewer.setAnimName("AnimStatique");
        objViewer.setDirection(direction);
        final PlayerCharacter playerCharacter = (PlayerCharacter)PropertiesProvider.getInstance().getObjectProperty("characterCreation.editablePlayerInfo");
        playerCharacter.setDirection(Direction8.getDirectionFromIndex(direction));
    }
    
    public static void changeDirectionCounterClockwise(final MouseEvent mouseEvent, final AnimatedElementViewer objViewer) {
        if (mouseEvent.getButton() != 1) {
            return;
        }
        if (UICharacterCreationFrame.getInstance().isLockControls()) {
            return;
        }
        final int direction = (objViewer.getDirection() - 1 < 0) ? 7 : (objViewer.getDirection() - 1);
        objViewer.setAnimName("AnimStatique");
        objViewer.setDirection(direction);
        final PlayerCharacter playerCharacter = (PlayerCharacter)PropertiesProvider.getInstance().getObjectProperty("characterCreation.editablePlayerInfo");
        playerCharacter.setDirection(Direction8.getDirectionFromIndex(direction));
    }
    
    public static void cancelDetailedColorChoice(final Event event) {
        UICharacterCreationFrame.getInstance().cancelColorChoices();
        validDetailedColorChoice(event);
    }
    
    public static void selectBreed(final ItemEvent itemEvent) {
        final UISelectBreedMessage message = new UISelectBreedMessage();
        message.setId(16515);
        message.setDefinition((CharacterCreationDefinition)itemEvent.getItemValue());
        Worker.getInstance().pushMessage(message);
    }
    
    public static void overBreed(final ItemEvent itemEvent) {
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.breedOver", itemEvent.getItemValue());
    }
    
    public static void outBreed(final ItemEvent itemEvent) {
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.breedOver", null);
    }
    
    public static void setNextHairStyle(final Event event) {
        UIMessage.send((short)16525);
    }
    
    public static void setPreviousHairStyle(final Event event) {
        UIMessage.send((short)16526);
    }
    
    public static void setNextDressStyle(final Event event) {
        UIMessage.send((short)16527);
    }
    
    public static void setPreviousDressStyle(final Event event) {
        UIMessage.send((short)16528);
    }
    
    public static void setRandomCharacterColors(final Event event) {
        UIMessage.send((short)16529);
    }
    
    static {
        CharacterCreationDialogActions.m_createCharacterFlag = false;
        m_logger = Logger.getLogger((Class)CharacterCreationDialogActions.class);
    }
}
