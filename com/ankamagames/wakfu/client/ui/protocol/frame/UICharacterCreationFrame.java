package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.decorator.*;
import gnu.trove.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.protocol.message.character.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.clientToServer.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.name.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.baseImpl.graphics.ui.progress.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

public class UICharacterCreationFrame implements MessageFrame
{
    private static Logger m_logger;
    private static final UICharacterCreationFrame m_instance;
    private boolean m_lockControls;
    private boolean m_sexForced;
    private ArrayList<CharacterCreationDefinition> m_displayedBreeds;
    private int m_flippingImageCurrentIndex;
    private int m_mainContainerY;
    private long m_characterId;
    private String m_characterName;
    private NetCharacterCreationFrame.CreationType m_creationType;
    private PlayerCharacter m_model;
    private short m_recustomType;
    private byte m_source;
    public static final String[] FULL_EMOTES;
    public static final String ANIM_CHARACTER_HELLO = "AnimEmote-Coucou";
    public static final String ANIM_CHARACTER_UNSELECTED = "AnimStatique";
    public static final String ANIM_CHARACTER_SELECTED = "AnimStatique02";
    public static final int TIME_ANIM_ALEA = 7500;
    public static final int TIME_ANIM_MIN = 5000;
    private static final Random m_dice;
    private ParticleDecorator m_particleDecorator;
    private AnimatedElementViewer animatedElementViewer;
    private List m_breedList;
    private short m_colorSave;
    private Image m_breedIllustration;
    private Image m_maleImage;
    private Image m_femaleImage;
    private static final TObjectIntHashMap<AvatarBreed> BREED_ORDER;
    private final Collection<Texture> m_texturesToClean;
    
    public static UICharacterCreationFrame getInstance() {
        return UICharacterCreationFrame.m_instance;
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    public void setCharacterName(final String characterName) {
        this.m_characterName = characterName;
    }
    
    public void setCreationType(final NetCharacterCreationFrame.CreationType creationType) {
        this.m_creationType = creationType;
    }
    
    public void setRecustomType(final short recustomType) {
        this.m_recustomType = recustomType;
    }
    
    public void setModel(final PlayerCharacter model) {
        this.m_model = model;
    }
    
    public void setSource(final byte source) {
        this.m_source = source;
    }
    
    private static void setDefaultCharacterStyle(final PlayerCharacter playerInfo, final boolean updateFields) {
        playerInfo.beginRefreshDisplayEquipment();
        playerInfo.setClothIndex((byte)0, false);
        playerInfo.setFaceIndex((byte)0, false);
        playerInfo.endRefreshDisplayEquipment();
        if (updateFields) {
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.currentDressStyleIndex", playerInfo.getClothIndex() + 1);
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.currentHairStyleIndex", playerInfo.getFaceIndex() + 1);
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.totalDressStyleIndex", BreedColorsManager.getInstance().getDressStylesCount(playerInfo.getBreedId(), playerInfo.getSex() == 0));
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.totalHairStyleIndex", BreedColorsManager.getInstance().getHairStylesCount(playerInfo.getBreedId(), playerInfo.getSex() == 0));
        }
    }
    
    public PlayerCharacter getPlayerInfo() {
        PlayerCharacter o = (PlayerCharacter)PropertiesProvider.getInstance().getObjectProperty("characterCreation.editablePlayerInfo");
        if (o == null) {
            o = new PlayerCharacter();
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.editablePlayerInfo", o);
        }
        return o;
    }
    
    private UICharacterCreationFrame() {
        super();
        this.m_characterId = -1L;
        this.m_creationType = NetCharacterCreationFrame.CreationType.DEFAULT;
        this.m_texturesToClean = new ArrayList<Texture>();
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final PropertiesProvider propertyProvider = PropertiesProvider.getInstance();
        final PlayerCharacter playerInfo = this.getPlayerInfo();
        if (message instanceof ClockMessage) {
            MessageScheduler.getInstance().removeAllClocks(this);
            if (!this.m_lockControls && playerInfo != null && (playerInfo.getDirection() == Direction8.SOUTH_WEST || playerInfo.getDirection() == Direction8.SOUTH_EAST)) {
                final CharacterActor actor = playerInfo.getActor();
                final String currentAnim = actor.getAnimation();
                if (currentAnim.equals(actor.getStaticAnimationKey())) {
                    final String newAnimName = createRandomAnimationName();
                    this.animatedElementViewer.setAnimName(newAnimName);
                    propertyProvider.firePropertyValueChanged("characterCreation.editablePlayerInfo", "actorDescriptorLibrary");
                    propertyProvider.firePropertyValueChanged("characterCreation.editablePlayerInfo", "actorLinkage");
                }
                else {
                    actor.setAnimation(actor.getStaticAnimationKey());
                }
            }
            this.startClock();
            return false;
        }
        switch (message.getId()) {
            case 16513: {
                NetCharacterChoiceFrame.getInstance().enableLoadUI(true);
                WakfuGameEntity.getInstance().pushFrame(NetCharacterChoiceFrame.getInstance());
                WakfuGameEntity.getInstance().removeFrame(NetCharacterCreationFrame.getInstance());
                return false;
            }
            case 16512: {
                final UIPlayerInfoMessage msg = (UIPlayerInfoMessage)message;
                this.createCharacter(playerInfo, msg.getStringValue());
                return false;
            }
            case 16515: {
                final UISelectBreedMessage msg2 = (UISelectBreedMessage)message;
                final CharacterCreationDefinition definition = msg2.getDefinition();
                final boolean useModel = msg2.getBooleanValue();
                return this.selectBreed(playerInfo, definition, useModel) && false;
            }
            case 16516: {
                final UIPlayerInfoMessage msg = (UIPlayerInfoMessage)message;
                final byte sex = msg.getByteValue();
                if (playerInfo.getSex() == sex) {
                    return false;
                }
                if (this.m_lockControls) {
                    return false;
                }
                this.bioumanTransform(sex);
                this.startFlipBreeds(true);
                return false;
            }
            case 16517: {
                final UIPlayerInfoMessage msg = (UIPlayerInfoMessage)message;
                this.cancelSkinColorChoices();
                this.cancelPupilColorChoices();
                final boolean editing = propertyProvider.getObjectProperty("characterCreation.hairColorChosen") != null;
                final byte hairColorIndex = MathHelper.getFirstByteFromShort(msg.getShortValue());
                final byte hairColorFactor = MathHelper.getSecondByteFromShort(msg.getShortValue());
                if (!editing) {
                    this.m_colorSave = MathHelper.getShortFromTwoBytes(playerInfo.getHairColorIndex(), playerInfo.getHairColorFactor());
                }
                propertyProvider.setPropertyValue("characterCreation.hairColorChosen", BreedColorsManager.getInstance().getSecondaryHairColors(playerInfo.getBreedId(), hairColorIndex, playerInfo.getSex()));
                playerInfo.setHairColorIndex(hairColorIndex, hairColorFactor, true);
                propertyProvider.firePropertyValueChanged(playerInfo, "actorDescriptorLibrary", "secondaryHairColors");
                return false;
            }
            case 16518: {
                final UIPlayerInfoMessage msg = (UIPlayerInfoMessage)message;
                this.cancelHairColorChoices();
                this.cancelPupilColorChoices();
                final boolean editing = propertyProvider.getObjectProperty("characterCreation.skinColorChosen") != null;
                final byte skinColorIndex = MathHelper.getFirstByteFromShort(msg.getShortValue());
                final byte skinColorFactor = MathHelper.getSecondByteFromShort(msg.getShortValue());
                if (!editing) {
                    this.m_colorSave = MathHelper.getShortFromTwoBytes(playerInfo.getSkinColorIndex(), playerInfo.getSkinColorFactor());
                }
                propertyProvider.setPropertyValue("characterCreation.skinColorChosen", BreedColorsManager.getInstance().getSecondarySkinColors(playerInfo.getBreedId(), skinColorIndex, playerInfo.getSex()));
                playerInfo.setSkinColorIndex(skinColorIndex, skinColorFactor, true);
                propertyProvider.firePropertyValueChanged(playerInfo, "actorDescriptorLibrary", "secondarySkinColors");
                return false;
            }
            case 16524: {
                this.cancelSkinColorChoices();
                this.cancelHairColorChoices();
                final UIPlayerInfoMessage msg = (UIPlayerInfoMessage)message;
                final byte pupilColorIndex = MathHelper.getFirstByteFromShort(msg.getShortValue());
                playerInfo.setPupilColorIndex(pupilColorIndex, true);
                propertyProvider.firePropertyValueChanged(playerInfo, "actorDescriptorLibrary");
                return false;
            }
            case 16525: {
                final int hairStylesCount = BreedColorsManager.getInstance().getHairStylesCount(playerInfo.getBreedId(), playerInfo.getSex() == 0);
                byte currentHairStyleIndex = (byte)PropertiesProvider.getInstance().getIntProperty("characterCreation.currentHairStyleIndex");
                ++currentHairStyleIndex;
                if (currentHairStyleIndex > hairStylesCount) {
                    currentHairStyleIndex = 1;
                }
                playerInfo.setFaceIndex((byte)(currentHairStyleIndex - 1), true);
                PropertiesProvider.getInstance().setPropertyValue("characterCreation.currentHairStyleIndex", currentHairStyleIndex);
                return false;
            }
            case 16526: {
                final int hairStylesCount = BreedColorsManager.getInstance().getHairStylesCount(playerInfo.getBreedId(), playerInfo.getSex() == 0);
                byte currentHairStyleIndex = (byte)PropertiesProvider.getInstance().getIntProperty("characterCreation.currentHairStyleIndex");
                --currentHairStyleIndex;
                if (currentHairStyleIndex <= 0) {
                    currentHairStyleIndex = (byte)hairStylesCount;
                }
                playerInfo.setFaceIndex((byte)(currentHairStyleIndex - 1), true);
                PropertiesProvider.getInstance().setPropertyValue("characterCreation.currentHairStyleIndex", currentHairStyleIndex);
                return false;
            }
            case 16527: {
                final int dressStylesCount = BreedColorsManager.getInstance().getDressStylesCount(playerInfo.getBreedId(), playerInfo.getSex() == 0);
                byte currentDressStyleIndex = (byte)PropertiesProvider.getInstance().getIntProperty("characterCreation.currentDressStyleIndex");
                ++currentDressStyleIndex;
                if (currentDressStyleIndex > dressStylesCount) {
                    currentDressStyleIndex = 1;
                }
                playerInfo.setClothIndex((byte)(currentDressStyleIndex - 1), true);
                PropertiesProvider.getInstance().setPropertyValue("characterCreation.currentDressStyleIndex", currentDressStyleIndex);
                return false;
            }
            case 16528: {
                final int dressStylesCount = BreedColorsManager.getInstance().getDressStylesCount(playerInfo.getBreedId(), playerInfo.getSex() == 0);
                byte currentDressStyleIndex = (byte)PropertiesProvider.getInstance().getIntProperty("characterCreation.currentDressStyleIndex");
                --currentDressStyleIndex;
                if (currentDressStyleIndex <= 0) {
                    currentDressStyleIndex = (byte)dressStylesCount;
                }
                playerInfo.setClothIndex((byte)(currentDressStyleIndex - 1), true);
                PropertiesProvider.getInstance().setPropertyValue("characterCreation.currentDressStyleIndex", currentDressStyleIndex);
                return false;
            }
            case 16529: {
                boolean modifyAppearance;
                boolean modifyColor;
                if (this.m_creationType == NetCharacterCreationFrame.CreationType.RECUSTOM) {
                    modifyAppearance = RecustomHelper.isAppearanceRecustom(this.m_recustomType);
                    modifyColor = RecustomHelper.isColorRecustom(this.m_recustomType);
                }
                else {
                    modifyAppearance = true;
                    modifyColor = true;
                }
                final int dressStylesCount2 = BreedColorsManager.getInstance().getDressStylesCount(playerInfo.getBreedId(), playerInfo.getSex() == 0);
                final byte clothIndex = modifyAppearance ? ((byte)MathHelper.random(dressStylesCount2)) : playerInfo.getClothIndex();
                final int hairStylesCount2 = BreedColorsManager.getInstance().getHairStylesCount(playerInfo.getBreedId(), playerInfo.getSex() == 0);
                final byte faceIndex = modifyAppearance ? ((byte)MathHelper.random(hairStylesCount2)) : playerInfo.getFaceIndex();
                final int skinColorsCount = BreedColorsManager.getInstance().getSkinColorsCount(playerInfo.getBreedId(), playerInfo.getSex());
                final byte skinColor = modifyColor ? ((byte)MathHelper.random(skinColorsCount)) : playerInfo.getSkinColorIndex();
                final byte skinFactor = modifyColor ? ((byte)MathHelper.random(8)) : playerInfo.getSkinColorFactor();
                final int hairColorsCount = BreedColorsManager.getInstance().getHairColorsCount(playerInfo.getBreedId(), playerInfo.getSex());
                final byte hairColor = modifyColor ? ((byte)MathHelper.random(hairColorsCount)) : playerInfo.getHairColorIndex();
                final byte hairFactor = modifyColor ? ((byte)MathHelper.random(8)) : playerInfo.getHairColorFactor();
                final int pupilColorsCount = BreedColorsManager.getInstance().getPupilColorsCount(playerInfo.getBreedId(), playerInfo.getSex());
                final byte pupilColor = modifyColor ? ((byte)MathHelper.random(pupilColorsCount)) : playerInfo.getPupilColorIndex();
                playerInfo.setClothIndex(clothIndex, true);
                PropertiesProvider.getInstance().setPropertyValue("characterCreation.currentDressStyleIndex", clothIndex + 1);
                playerInfo.setFaceIndex(faceIndex, true);
                PropertiesProvider.getInstance().setPropertyValue("characterCreation.currentHairStyleIndex", faceIndex + 1);
                playerInfo.setSkinColorIndex(skinColor, skinFactor, true);
                propertyProvider.firePropertyValueChanged(playerInfo, "actorDescriptorLibrary", "secondarySkinColors");
                playerInfo.setHairColorIndex(hairColor, hairFactor, true);
                propertyProvider.firePropertyValueChanged(playerInfo, "actorDescriptorLibrary", "secondaryHairColors");
                playerInfo.setPupilColorIndex(pupilColor, true);
                propertyProvider.firePropertyValueChanged(playerInfo, "actorDescriptorLibrary");
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean selectBreed(final PlayerCharacter playerInfo, final CharacterCreationDefinition definition, final boolean useModel) {
        final AvatarBreed breed = definition.getBreed();
        if (playerInfo.getBreed() == breed) {
            return true;
        }
        if (this.m_lockControls) {
            return true;
        }
        if (!definition.isEnabled()) {
            return true;
        }
        if (breed == AvatarBreed.STEAMER && playerInfo.getSex() == 1) {
            return true;
        }
        if (definition instanceof RandomBreed) {
            CharacterCreationDefinition newBreed = null;
            while (newBreed == null || newBreed.getBreed() == playerInfo.getBreed() || (newBreed.getBreed() == AvatarBreed.STEAMER && playerInfo.getSex() == 1)) {
                newBreed = this.m_displayedBreeds.get(this.selectRandomBreed(this.m_creationType));
                UICharacterCreationFrame.m_logger.error((Object)newBreed.getBreed().name());
            }
            this.chooseBreed(newBreed, false);
        }
        else {
            this.chooseBreed(definition, useModel);
        }
        this.selectClass();
        return false;
    }
    
    public NetCharacterCreationFrame.CreationType getCreationType() {
        return this.m_creationType;
    }
    
    private boolean selectClass() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("characterCreationDialog");
        if (map == null) {
            return true;
        }
        final TextEditor textEditor = (TextEditor)map.getElement("nameHelpTextEditor");
        FocusManager.getInstance().setFocused(textEditor);
        final Container mainContainer = (Container)map.getElement("mainContainer");
        final Image backgroundImage = (Image)map.getElement("bigBackground");
        final ArrayList<ModulationColorClient> appL1 = new ArrayList<ModulationColorClient>();
        appL1.add(backgroundImage.getAppearance());
        appL1.add(this.animatedElementViewer);
        mainContainer.removeTweensOfType(PositionTween.class);
        mainContainer.removeTweensOfType(ModulationColorListTween.class);
        this.animatedElementViewer.setModulationColor(Color.WHITE_ALPHA);
        backgroundImage.setModulationColor(Color.WHITE_ALPHA);
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                WakfuSoundManager.getInstance().playGUISound(600012L, false, 600);
                if (UICharacterCreationFrame.this.m_mainContainerY == Integer.MIN_VALUE) {
                    UICharacterCreationFrame.this.m_mainContainerY = mainContainer.getY() - 100;
                }
                mainContainer.setY(UICharacterCreationFrame.this.m_mainContainerY);
                mainContainer.setVisible(true);
                final PositionTween positionTween = new PositionTween(mainContainer.getX(), mainContainer.getY(), mainContainer.getX(), mainContainer.getY() + 100, mainContainer, 0, 750, TweenFunction.PROGRESSIVE);
                final ModulationColorListTween modulationColorListTween1 = new ModulationColorListTween(Color.WHITE_ALPHA, Color.WHITE, appL1, 0, 500, 1, TweenFunction.PROGRESSIVE);
                modulationColorListTween1.addTweenEventListener(new TweenEventListener() {
                    @Override
                    public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                        switch (e) {
                            case TWEEN_ENDED: {
                                mainContainer.removeTweensOfType(ModulationColorListTween.class);
                                modulationColorListTween1.removeTweenEventListener(this);
                                break;
                            }
                        }
                    }
                });
                mainContainer.addTween(positionTween);
                mainContainer.addTween(modulationColorListTween1);
                UICharacterCreationFrame.this.addBreedBackgroundParticle();
                UICharacterCreationFrame.this.startClock(0L);
            }
        }, 100L, 1);
        return false;
    }
    
    private boolean setCharacterSex(final PlayerCharacter playerInfo, byte sex) {
        this.m_sexForced = false;
        if (sex == 1 && !PropertiesProvider.getInstance().getBooleanProperty("characterCreation.femaleEnabled")) {
            sex = 0;
            this.m_sexForced = true;
        }
        if (playerInfo.getSex() == sex) {
            return true;
        }
        this.setPlayerSex(playerInfo, sex, true);
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.currentDressStyleIndex", playerInfo.getClothIndex() + 1);
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.currentHairStyleIndex", playerInfo.getFaceIndex() + 1);
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.totalDressStyleIndex", BreedColorsManager.getInstance().getDressStylesCount(playerInfo.getBreedId(), playerInfo.getSex() == 0));
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.totalHairStyleIndex", BreedColorsManager.getInstance().getHairStylesCount(playerInfo.getBreedId(), playerInfo.getSex() == 0));
        this.fadeImagesChangingSex(sex);
        return false;
    }
    
    private void createCharacter(final PlayerCharacter playerInfo, final String name) {
        WakfuSoundManager.getInstance().playGUISound(600003L);
        playerInfo.setName(name);
        WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).beginTask(WakfuTranslator.getInstance().getString("createCharacter.progress"), 0);
        final CharacterCreationMessage netMessage = new CharacterCreationMessage();
        netMessage.setCharacterId(this.m_characterId);
        netMessage.setBreed(playerInfo.getBreed().getBreedId());
        netMessage.setClothIndex(playerInfo.getClothIndex());
        netMessage.setFaceIndex(playerInfo.getFaceIndex());
        netMessage.setHairColorIndex(playerInfo.getHairColorIndex());
        netMessage.setHairColorFactor(playerInfo.getHairColorFactor());
        netMessage.setSkinColorIndex(playerInfo.getSkinColorIndex());
        netMessage.setSkinColorFactor(playerInfo.getSkinColorFactor());
        netMessage.setPupilColorIndex(playerInfo.getPupilColorIndex());
        netMessage.setSex(playerInfo.getSex());
        netMessage.setName(playerInfo.getName());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
        LocalCharacterInfosManager.getInstance().setLastCreatedCharacter(playerInfo);
    }
    
    private void setPlayerSex(final PlayerCharacter playerCharacter, final byte sex, final boolean updateFields) {
        final PropertiesProvider propertyProvider = PropertiesProvider.getInstance();
        playerCharacter.setSex(sex);
        playerCharacter.adjustGfxFromBreedAndSex();
        playerCharacter.setSet();
        playerCharacter.setDefaultColors();
        this.updateCharacterAppearance(playerCharacter);
        setDefaultCharacterStyle(playerCharacter, updateFields);
        if (updateFields) {
            propertyProvider.setPropertyValue("characterCreation.hairColorChosen", null);
            propertyProvider.setPropertyValue("characterCreation.skinColorChosen", null);
            propertyProvider.setPropertyValue("characterCreation.pupilColorChosen", null);
        }
    }
    
    @Override
    public long getId() {
        return this.hashCode();
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            WakfuProgressMonitorManager.getInstance().getProgressMonitor(true);
            this.m_mainContainerY = Integer.MIN_VALUE;
            this.releaseLock();
            this.m_colorSave = -1;
            if (this.m_creationType != NetCharacterCreationFrame.CreationType.RECUSTOM) {
                this.m_recustomType = 127;
            }
            if (this.m_recustomType == 127) {
                PropertiesProvider.getInstance().setPropertyValue("characterCreation.buttonText", WakfuTranslator.getInstance().getString("create"));
            }
            else {
                PropertiesProvider.getInstance().setPropertyValue("characterCreation.buttonText", WakfuTranslator.getInstance().getString("recustom.change"));
            }
            this.initDisplayedBreeds();
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.describedMainSpell", null);
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.hairColorChosen", null);
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.skinColorChosen", null);
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.pupilColorChosen", null);
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.breedOver", null);
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.randomNameActivated", this.isRandomCharacterCreationEnabled());
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.enableCancel", this.m_creationType.isCanCancelCreation());
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.enableNameChange", RecustomHelper.isNameRecustom(this.m_recustomType));
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.enableSexChange", RecustomHelper.isSexRecustom(this.m_recustomType));
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.enableAppearanceChange", RecustomHelper.isAppearanceRecustom(this.m_recustomType));
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.enableColorChange", RecustomHelper.isColorRecustom(this.m_recustomType));
            final boolean hasAccountRights = AdminRightHelper.checkRight(WakfuGameEntity.getInstance().getLocalAccount().getAdminRights(), AdminRightsEnum.PASS_THROUGH_NAME_CHECK);
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.regex", hasAccountRights ? "[\\p{L} '\\-\\u00b7\\[\\]]+" : "[\\p{L} '\\-\\u00b7]+");
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.maxChars", (short)25);
            final String dialogPath = "characterCreationDialog";
            final EventDispatcher dialog = Xulor.getInstance().load("characterCreationDialog", Dialogs.getDialogPath("characterCreationDialog"), 8192L, (short)10000);
            this.initializeBasicUi(dialog);
            final ElementMap elementMap = dialog.getElementMap();
            this.m_maleImage = (Image)elementMap.getElement("maleImage");
            this.m_femaleImage = (Image)elementMap.getElement("femaleImage");
            this.animatedElementViewer = (AnimatedElementViewer)elementMap.getElement("characterAEV");
            if (this.m_characterName != null) {
                final TextEditor textEditor = (TextEditor)elementMap.getElement("nameHelpTextEditor");
                final Label label = (Label)elementMap.getElement("characterNameEditorText");
                textEditor.setText(this.m_characterName);
                label.setVisible(false);
                this.m_characterName = null;
            }
            else {
                this.startCharacterNameTextTween();
            }
            CharacterCreationDialogActions.setCreateCharacterFlag(false);
            Xulor.getInstance().putActionClass("wakfu.characterCreation", CharacterCreationDialogActions.class);
            WakfuSoundManager.getInstance().playGUISound(600012L);
            WakfuSoundManager.getInstance().onBackToLogin();
            this.preLoad();
            UISystemBarFrame.getInstance().reloadMenuBarDialog();
        }
    }
    
    private boolean isRandomCharacterCreationEnabled() {
        return WakfuConfiguration.getInstance().getBoolean("enableRandomCharacterName", WakfuNameGenerator.getInstance().canGenerateName());
    }
    
    private void initializeBasicUi(final EventDispatcher dialog) {
        this.m_breedList = (List)dialog.getElementMap().getElement("breedList2");
        this.m_breedIllustration = (Image)dialog.getElementMap().getElement("breedIllustration");
        this.m_breedList.addListContentListener(new EditableRenderableCollection.CollectionContentLoadedListener() {
            @Override
            public void onContentLoaded() {
                final UISelectBreedMessage message = new UISelectBreedMessage();
                message.setId(16515);
                if (UICharacterCreationFrame.this.m_creationType.isForceSoulBreed()) {
                    message.setDefinition(UICharacterCreationFrame.this.m_displayedBreeds.get(UICharacterCreationFrame.this.selectBreed(AvatarBreed.SOUL)));
                }
                else {
                    message.setBooleanValue(true);
                    final AvatarBreed breed = (UICharacterCreationFrame.this.m_creationType.isUseModel() && UICharacterCreationFrame.this.m_model != null) ? UICharacterCreationFrame.this.m_model.getBreed() : AvatarBreed.IOP;
                    final CharacterCreationDefinition breedDefinition = UICharacterCreationFrame.this.m_displayedBreeds.get(UICharacterCreationFrame.this.selectBreed(breed));
                    if (breedDefinition.isEnabled()) {
                        message.setDefinition(breedDefinition);
                    }
                    else {
                        message.setDefinition(UICharacterCreationFrame.this.m_displayedBreeds.get(UICharacterCreationFrame.this.selectRandomBreed(UICharacterCreationFrame.this.m_creationType)));
                    }
                }
                Worker.getInstance().pushMessage(message);
                UICharacterCreationFrame.this.m_breedList.removeListContentLoadListener(this);
            }
        });
    }
    
    public void stopCharacterNameTextTween() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("characterCreationDialog");
        final Label l = (Label)map.getElement("characterNameEditorText");
        l.removeTweensOfType(ModulationColorTween.class);
        l.setVisible(false);
    }
    
    private void startCharacterNameTextTween() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("characterCreationDialog");
        final Label l = (Label)map.getElement("characterNameEditorText");
        final Color a = new Color(Color.WHITE);
        final Color b = new Color(Color.WHITE_ALPHA);
        l.removeTweensOfType(ModulationColorTween.class);
        final ModulationColorTween tw = new ModulationColorTween(a, b, l, 0, 1000, -1, true, TweenFunction.PROGRESSIVE);
        l.addTween(tw);
    }
    
    private int selectRandomBreed() {
        return MathHelper.random(this.m_displayedBreeds.size() - 1);
    }
    
    private int selectBreed(final AvatarBreed breed) {
        for (int i = 0, size = this.m_displayedBreeds.size(); i < size; ++i) {
            final CharacterCreationDefinition playerCharacter = this.m_displayedBreeds.get(i);
            if (playerCharacter.getBreed() == breed) {
                return i;
            }
        }
        return 0;
    }
    
    private int selectRandomBreed(final NetCharacterCreationFrame.CreationType type) {
        int numEnabled = 0;
        for (int i = 0, size = this.m_displayedBreeds.size(); i < size; ++i) {
            if (this.m_displayedBreeds.get(i).isEnabled()) {
                ++numEnabled;
            }
        }
        if (numEnabled == 0) {
            return 0;
        }
        int index;
        do {
            index = this.selectRandomBreed();
        } while (!this.m_displayedBreeds.get(index).isEnabled());
        return index;
    }
    
    private void initDisplayedBreeds() {
        this.m_displayedBreeds = new ArrayList<CharacterCreationDefinition>();
        final AvatarBreedInfo[] breedInfos = AvatarBreedInfoManager.getInstance().getBreedInfos();
        for (int i = 0; i < breedInfos.length; ++i) {
            final AvatarBreed breed = breedInfos[i].getBreed();
            final boolean changeBreed = this.m_creationType != NetCharacterCreationFrame.CreationType.RECUSTOM || RecustomHelper.isBreedRecustom(this.m_recustomType) || (this.m_model != null && this.m_model.getBreed() == breed);
            final CharacterCreationDefinition definition = new CharacterCreationDefinition(breed);
            definition.setSortOrder(UICharacterCreationFrame.BREED_ORDER.get(breed));
            final boolean enabled = this.isBreedEnabled(breed, changeBreed);
            definition.setEnabled(enabled);
            this.m_displayedBreeds.add(definition);
        }
        int numEnabled = 0;
        for (int j = 0, size = this.m_displayedBreeds.size(); j < size; ++j) {
            if (this.m_displayedBreeds.get(j).isEnabled()) {
                ++numEnabled;
            }
        }
        if (this.m_displayedBreeds.size() > 1) {
            final CharacterCreationDefinition breed2 = new RandomBreed();
            breed2.setEnabled(numEnabled > 1);
            this.m_displayedBreeds.add(breed2);
        }
        Collections.sort(this.m_displayedBreeds, new Comparator<CharacterCreationDefinition>() {
            @Override
            public int compare(final CharacterCreationDefinition o1, final CharacterCreationDefinition o2) {
                if (o1 == o2) {
                    return 0;
                }
                if (o1 instanceof RandomBreed) {
                    return Integer.MAX_VALUE;
                }
                if (o2 instanceof RandomBreed) {
                    return Integer.MIN_VALUE;
                }
                return o1.getSortOrder() - o2.getSortOrder();
            }
        });
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.breedInfoList", this.m_displayedBreeds);
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.breedsAvailableNumber", breedInfos.length);
    }
    
    private boolean isBreedEnabled(final AvatarBreed breed, final boolean changeBreed) {
        return (this.m_recustomType != 125 || this.m_model.getBreed() == AvatarBreed.SOUL || (breed != AvatarBreed.ELIOTROPE && this.m_model.getBreed() != breed)) && (breed != AvatarBreed.SOUL ^ this.m_creationType.isForceSoulBreed()) && AvatarBreedConstants.isBreedEnabled(breed) && changeBreed;
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_characterId = -1L;
            this.m_characterName = null;
            this.m_creationType = NetCharacterCreationFrame.CreationType.DEFAULT;
            this.m_colorSave = -1;
            this.m_displayedBreeds.clear();
            for (final Texture t : this.m_texturesToClean) {
                t.removeReference();
            }
            this.m_texturesToClean.clear();
            this.getPlayerInfo().getActor().dispose();
            final ArrayList<PlayerCharacter> displayedBreeds = (ArrayList<PlayerCharacter>)PropertiesProvider.getInstance().getObjectProperty("characterCreation.breedInfoList");
            for (final PlayerCharacter c : displayedBreeds) {
                c.release();
            }
            final PlayerCharacter p = (PlayerCharacter)PropertiesProvider.getInstance().getObjectProperty("characterCreation.editablePlayerInfo");
            if (p != null) {
                p.release();
            }
            PropertiesProvider.getInstance().removeProperty("characterCreation.editablePlayerInfo");
            PropertiesProvider.getInstance().removeProperty("characterCreation.overBreed");
            PropertiesProvider.getInstance().removeProperty("characterCreation.breedInfoList");
            PropertiesProvider.getInstance().removeProperty("characterCreation.breedsAvailableNumber");
            PropertiesProvider.getInstance().removeProperty("characterCreation.hairColorChosen");
            PropertiesProvider.getInstance().removeProperty("characterCreation.skinColorChosen");
            PropertiesProvider.getInstance().removeProperty("characterCreation.pupilColorChosen");
            PropertiesProvider.getInstance().removeProperty("characterCreation.currentHairStyleIndex");
            PropertiesProvider.getInstance().removeProperty("characterCreation.totalHairStyleIndex");
            PropertiesProvider.getInstance().removeProperty("characterCreation.currentDressStyleIndex");
            PropertiesProvider.getInstance().removeProperty("characterCreation.totalDressStyleIndex");
            PropertiesProvider.getInstance().removeProperty("characterCreation.breedOver");
            PropertiesProvider.getInstance().removeProperty("characterCreation.randomNameActivated");
            Xulor.getInstance().unload("characterCreationDialog");
            Xulor.getInstance().removeActionClass("wakfu.characterCreation");
            MessageScheduler.getInstance().removeAllClocks(this);
            this.m_particleDecorator = null;
            this.animatedElementViewer = null;
            WakfuNameGenerator.getInstance().clean();
        }
    }
    
    public void startClock() {
        final int rand = UICharacterCreationFrame.m_dice.nextInt(7500);
        this.startClock(rand + 5000);
    }
    
    private void startClock(final long randomDelay) {
        MessageScheduler.getInstance().removeAllClocks(this);
        MessageScheduler.getInstance().addClock(UICharacterCreationFrame.m_instance, randomDelay, 0, 1);
    }
    
    private static String createRandomAnimationName() {
        return UICharacterCreationFrame.FULL_EMOTES[MersenneTwister.getInstance().nextInt(UICharacterCreationFrame.FULL_EMOTES.length)];
    }
    
    private void chooseBreed(final CharacterCreationDefinition newBreed, final boolean useModel) {
        this.stopBreedCornersPulse();
        final short breedId = newBreed.getBreed().getBreedId();
        final boolean isFemaleDisabled = breedId == AvatarBreed.STEAMER.getBreedId() || breedId == AvatarBreed.OUGINAK.getBreedId();
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.femaleEnabled", !isFemaleDisabled);
        final PlayerCharacter playerInfo = this.getPlayerInfo();
        final CharacterCreationDefinition modelBreedDefinition;
        if (useModel && this.m_model != null && (modelBreedDefinition = this.m_displayedBreeds.get(this.selectBreed(this.m_model.getBreed()))) != null && modelBreedDefinition.isEnabled()) {
            playerInfo.setAvatarBreed(this.m_model.getBreed());
            playerInfo.setSkinColorIndex(this.m_model.getSkinColorIndex(), this.m_model.getSkinColorFactor(), true);
            playerInfo.setHairColorIndex(this.m_model.getHairColorIndex(), this.m_model.getHairColorFactor(), true);
            playerInfo.setPupilColorIndex(this.m_model.getPupilColorIndex(), true);
            playerInfo.setSex(this.m_model.getSex());
            final byte faceIndex = this.m_model.getFaceIndex();
            final byte clothIndex = this.m_model.getClothIndex();
            playerInfo.beginRefreshDisplayEquipment();
            playerInfo.setFaceIndex(faceIndex, true);
            playerInfo.setClothIndex(clothIndex, true);
            playerInfo.endRefreshDisplayEquipment();
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.currentHairStyleIndex", faceIndex + 1);
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.currentDressStyleIndex", clothIndex + 1);
            final int dressStylesCount = BreedColorsManager.getInstance().getDressStylesCount(playerInfo.getBreedId(), playerInfo.getSex() == 0);
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.totalDressStyleIndex", dressStylesCount);
            final int hairStylesCount = BreedColorsManager.getInstance().getHairStylesCount(playerInfo.getBreedId(), playerInfo.getSex() == 0);
            PropertiesProvider.getInstance().setPropertyValue("characterCreation.totalHairStyleIndex", hairStylesCount);
            if (this.getPlayerInfo().getSex() == 1) {
                this.startFlipBreeds(false);
            }
        }
        else {
            playerInfo.setAvatarBreed(newBreed.getBreed());
            playerInfo.setSet();
            playerInfo.setDefaultColors();
            this.setCharacterSex(playerInfo, (byte)(this.m_sexForced ? 1 : playerInfo.getSex()));
            setDefaultCharacterStyle(playerInfo, true);
        }
        playerInfo.setDirection(Direction8.SOUTH_WEST);
        this.updateCharacterAppearance(playerInfo);
        this.m_breedList.setSelectedValue(newBreed);
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.hairColorChosen", null);
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.skinColorChosen", null);
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.pupilColorChosen", null);
        this.fadeImagesChangingBreed(playerInfo.getSex());
        this.makeBreedCornersPulse();
    }
    
    private void stopBreedCornersPulse() {
        final RenderableContainer selected = this.m_breedList.getSelected();
        if (selected == null) {
            return;
        }
        final ElementMap innerElementMap = selected.getInnerElementMap();
        this.stopPulseCorner((Image)innerElementMap.getElement("breedSelectionNW"));
        this.stopPulseCorner((Image)innerElementMap.getElement("breedSelectionSW"));
        this.stopPulseCorner((Image)innerElementMap.getElement("breedSelectionNE"));
        this.stopPulseCorner((Image)innerElementMap.getElement("breedSelectionSE"));
    }
    
    private void stopPulseCorner(final Image image) {
        final Color a = new Color(Color.WHITE);
        image.removeTweensOfType(ModulationColorTween.class);
        image.setModulationColor(a);
        image.removeTweensOfType(ImageScaleTween.class);
        image.getImageMesh().setScale(1.0f, 1.0f, 1.0f);
    }
    
    private void makeBreedCornersPulse() {
        final RenderableContainer selected = this.m_breedList.getSelected();
        if (selected == null) {
            return;
        }
        final ElementMap innerElementMap = selected.getInnerElementMap();
        this.pulseCorner((Image)innerElementMap.getElement("breedSelectionNW"));
        this.pulseCorner((Image)innerElementMap.getElement("breedSelectionSW"));
        this.pulseCorner((Image)innerElementMap.getElement("breedSelectionNE"));
        this.pulseCorner((Image)innerElementMap.getElement("breedSelectionSE"));
    }
    
    private void pulseCorner(final Image image) {
        final Color a = new Color(Color.WHITE);
        final Color b = new Color(Color.WHITE_ALPHA);
        image.removeTweensOfType(ImageScaleTween.class);
        final ImageScaleTween ist = new ImageScaleTween(0.8f, 1.0f, image, 0, 300, TweenFunction.FULL_TO_NULL, image.getImageMesh(), -1);
        image.addTween(ist);
    }
    
    private void updateCharacterAppearance(final PlayerCharacter playerCharacter) {
        final PropertiesProvider propertyProvider = PropertiesProvider.getInstance();
        propertyProvider.firePropertyValueChanged(playerCharacter, "sex", "hairColors", "skinColors", "pupilColors", "actorDescriptorLibrary", "actorAnimationPath", "actorLinkage", "hairDescription", "skinDescription", "pupilDescription", "hairAvailability", "skinAvailability", "pupilAvailability", "breedInfo", "secondaryHairColors", "secondarySkinColors");
    }
    
    private void addBreedBackgroundParticle() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("characterCreationDialog");
        if (map == null) {
            return;
        }
        final Image image = (Image)map.getElement("bigBackground");
        if (image == null) {
            return;
        }
        if (this.m_particleDecorator == null) {
            (this.m_particleDecorator = new ParticleDecorator()).onCheckOut();
            this.m_particleDecorator.setAlignment(Alignment9.CENTER);
            this.m_particleDecorator.setX((int)this.m_particleDecorator.getPosition().getX());
            this.m_particleDecorator.setY((int)(this.m_particleDecorator.getPosition().getY() + 20.0f));
            this.m_particleDecorator.setLevel(1);
        }
        this.m_particleDecorator.setFile(BreedColorsManager.getInstance().getApsFileName(this.getPlayerInfo().getBreed().getBreedId()));
        image.getAppearance().add(this.m_particleDecorator);
    }
    
    public void updateBreedBackgroundParticle() {
        if (this.m_particleDecorator != null) {
            this.m_particleDecorator.setFile(BreedColorsManager.getInstance().getApsFileName(this.getPlayerInfo().getBreed().getBreedId()));
        }
    }
    
    public void cancelColorChoices() {
        this.cancelHairColorChoices();
        this.cancelSkinColorChoices();
        this.cancelPupilColorChoices();
    }
    
    private void cancelHairColorChoices() {
        final PropertiesProvider propertiesProvider = PropertiesProvider.getInstance();
        if (propertiesProvider.getObjectProperty("characterCreation.hairColorChosen") == null) {
            return;
        }
        if (this.m_colorSave == -1) {
            return;
        }
        final byte colorIndex = MathHelper.getFirstByteFromShort(this.m_colorSave);
        final byte colorFactor = MathHelper.getSecondByteFromShort(this.m_colorSave);
        final PlayerCharacter playerInfo = this.getPlayerInfo();
        playerInfo.setHairColorIndex(colorIndex, colorFactor, true);
        propertiesProvider.firePropertyValueChanged(playerInfo, "actorDescriptorLibrary");
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.hairColorChosen", null);
    }
    
    private void cancelSkinColorChoices() {
        final PropertiesProvider propertiesProvider = PropertiesProvider.getInstance();
        if (propertiesProvider.getObjectProperty("characterCreation.skinColorChosen") == null) {
            return;
        }
        if (this.m_colorSave == -1) {
            return;
        }
        final byte colorIndex = MathHelper.getFirstByteFromShort(this.m_colorSave);
        final byte colorFactor = MathHelper.getSecondByteFromShort(this.m_colorSave);
        final PlayerCharacter playerInfo = this.getPlayerInfo();
        playerInfo.setSkinColorIndex(colorIndex, colorFactor, true);
        propertiesProvider.firePropertyValueChanged(playerInfo, "actorDescriptorLibrary");
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.skinColorChosen", null);
    }
    
    private void cancelPupilColorChoices() {
        final PropertiesProvider propertiesProvider = PropertiesProvider.getInstance();
        if (propertiesProvider.getObjectProperty("characterCreation.pupilColorChosen") == null) {
            return;
        }
        if (this.m_colorSave == -1) {
            return;
        }
        final byte colorIndex = MathHelper.getFirstByteFromShort(this.m_colorSave);
        final PlayerCharacter playerInfo = this.getPlayerInfo();
        playerInfo.setPupilColorIndex(colorIndex, true);
        propertiesProvider.firePropertyValueChanged(playerInfo, "actorDescriptorLibrary");
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.pupilColorChosen", null);
    }
    
    public void fadeImagesChangingBreed(final byte sex) {
        fadeImage(this.m_breedIllustration, Color.WHITE_ALPHA, Color.WHITE);
        fadeImage(this.m_maleImage, Color.WHITE_ALPHA, this.maleColorFromSex(sex));
        fadeImage(this.m_femaleImage, Color.WHITE_ALPHA, this.femaleColorFromSex(sex));
    }
    
    private Color maleColorFromSex(final byte sex) {
        return (sex == 0) ? Color.WHITE : Color.WHITE_ALPHA;
    }
    
    private Color femaleColorFromSex(final byte sex) {
        return (sex == 1) ? Color.WHITE : Color.WHITE_ALPHA;
    }
    
    public void fadeImagesChangingSex(final byte sex) {
        final Color c0 = (sex == 1) ? Color.WHITE : Color.WHITE_ALPHA;
        final Color c = (sex == 1) ? Color.WHITE_ALPHA : Color.WHITE;
        fadeImage(this.m_maleImage, c0, c);
        fadeImage(this.m_femaleImage, c, c0);
    }
    
    private static void fadeImage(final Image image, final Color fromColor, final Color toColor) {
        if (image == null) {
            return;
        }
        final Color a = new Color(fromColor);
        final Color b = new Color(toColor);
        image.removeTweensOfType(ModulationColorTween.class);
        final ModulationColorTween tw = new ModulationColorTween(a, b, image, 0, 300, 1, false, TweenFunction.PROGRESSIVE);
        image.addTween(tw);
    }
    
    public static short getBreedId(final CharacterInfo character) {
        final ArrayList<PlayerCharacter> breedInfos = (ArrayList<PlayerCharacter>)PropertiesProvider.getInstance().getObjectProperty("characterCreation.breedInfoList");
        return (short)breedInfos.indexOf(character);
    }
    
    public boolean isLockControls() {
        return this.m_lockControls;
    }
    
    public void bioumanTransform(final byte sex) {
        this.getLock();
        final BioumanTransform bioumanTransform = new BioumanTransform(sex);
        bioumanTransform.turn();
    }
    
    private void startFlipBreeds(final boolean withSound) {
        if (withSound) {
            final AudioSource source = WakfuSoundManager.getInstance().playGUISound(600158L);
            if (source != null) {
                source.setGain(0.5f);
            }
        }
        this.m_flippingImageCurrentIndex = 0;
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                final RenderableContainer renderableContainer = UICharacterCreationFrame.this.m_breedList.getRenderables().get(UICharacterCreationFrame.this.m_flippingImageCurrentIndex);
                final FlippingImage fi = (FlippingImage)renderableContainer.getInnerElementMap().getElement("flippingImage");
                fi.start();
                UICharacterCreationFrame.this.m_flippingImageCurrentIndex = (UICharacterCreationFrame.this.m_flippingImageCurrentIndex + 1) % UICharacterCreationFrame.this.m_breedList.size();
            }
        }, 20L, this.m_breedList.size());
    }
    
    private void getLock() {
        this.m_lockControls = true;
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.controlsLocked", this.m_lockControls);
    }
    
    private void releaseLock() {
        this.m_lockControls = false;
        PropertiesProvider.getInstance().setPropertyValue("characterCreation.controlsLocked", this.m_lockControls);
    }
    
    private AvatarBreedInfo getBreedInfo(final AvatarBreed breed, final AvatarBreedInfo[] breedInfos) {
        for (final AvatarBreedInfo info : breedInfos) {
            if (info.getBreed() == breed) {
                return info;
            }
        }
        return null;
    }
    
    public void preLoad() {
        final ProgressMonitor progressMonitor = WakfuProgressMonitorManager.getInstance().getProgressMonitor();
        final AvatarBreedInfo[] breedInfosSrc = AvatarBreedInfoManager.getInstance().getBreedInfos();
        AvatarBreedInfo[] breedInfos;
        if (this.m_creationType.isForceSoulBreed()) {
            breedInfos = new AvatarBreedInfo[] { this.getBreedInfo(AvatarBreed.SOUL, breedInfosSrc) };
        }
        else {
            breedInfos = breedInfosSrc;
        }
        progressMonitor.beginTask(WakfuTranslator.getInstance().getString("loading.preLoad"), breedInfos.length * 4);
        for (final AvatarBreedInfo avatarBreedInfo : breedInfos) {
            final short breedId = avatarBreedInfo.getId();
            this.preloadBreedStyles(breedId, true);
            this.preloadBreedStyles(breedId, false);
        }
        final Thread t = new Thread() {
            public ArrayList<Texture> m_allTextures;
            private int m_totalWorked;
            private int m_totalPastTime;
            
            private void loadTexture(final AvatarBreedInfo avatarBreedInfo, final String field) {
                final Texture texture = TextureLoader.getInstance().loadTexture((String)avatarBreedInfo.getFieldValue(field));
                if (texture != null) {
                    texture.addReference();
                    this.m_allTextures.add(texture);
                    UICharacterCreationFrame.this.m_texturesToClean.add(texture);
                }
            }
            
            @Override
            public void run() {
                this.m_allTextures = new ArrayList<Texture>();
                for (final AvatarBreedInfo avatarBreedInfo : breedInfos) {
                    this.loadTexture(avatarBreedInfo, "maleIllustration");
                    this.loadTexture(avatarBreedInfo, "femaleIllustration");
                }
                for (final AvatarBreedInfo avatarBreedInfo : breedInfosSrc) {
                    this.loadTexture(avatarBreedInfo, "malePortraitIllustration");
                    this.loadTexture(avatarBreedInfo, "femalePortraitIllustration");
                }
                while (!this.m_allTextures.isEmpty() && this.m_totalPastTime < 5000) {
                    for (int i = this.m_allTextures.size() - 1; i >= 0; --i) {
                        final Texture t = this.m_allTextures.get(i);
                        if (t == null) {
                            progressMonitor.worked(this.m_totalWorked++);
                            this.m_allTextures.remove(i);
                        }
                        else if (t.isReady()) {
                            progressMonitor.worked(this.m_totalWorked++);
                            this.m_allTextures.remove(t);
                        }
                    }
                    try {
                        Thread.sleep(50L);
                        this.m_totalPastTime += 50;
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                UICharacterCreationFrame.m_logger.error((Object)"Creation dialog loading finish");
                this.m_allTextures.clear();
                WakfuProgressMonitorManager.getInstance().done();
            }
        };
        t.start();
    }
    
    private void preloadBreedStyles(final short breedId, final boolean male) {
        String equipmentFileName;
        try {
            equipmentFileName = WakfuConfiguration.getInstance().getString("ANMEquipmentPath");
        }
        catch (PropertyException e) {
            UICharacterCreationFrame.m_logger.error((Object)"Erreur au chargement d'une propri\u00e9t\u00e9", (Throwable)e);
            return;
        }
        final int hairStylesCount = BreedColorsManager.getInstance().getHairStylesCount(breedId, male);
        final int dressStylesCount = BreedColorsManager.getInstance().getDressStylesCount(breedId, male);
        for (int i = 0; i < hairStylesCount; ++i) {
            final String dressStyle = BreedColorsManager.getInstance().getHairStyle(breedId, male, i);
            final String file = String.format(equipmentFileName, dressStyle);
            AnimatedElement.loadEquipment(file);
        }
        for (int i = 0; i < dressStylesCount; ++i) {
            final String dressStyle = BreedColorsManager.getInstance().getDressStyle(breedId, male, i);
            final String file = String.format(equipmentFileName, dressStyle);
            AnimatedElement.loadEquipment(file);
        }
    }
    
    static {
        UICharacterCreationFrame.m_logger = Logger.getLogger((Class)UICharacterCreationFrame.class);
        m_instance = new UICharacterCreationFrame();
        FULL_EMOTES = new String[] { "AnimEmote-Repos", "AnimEmote-Victoire", "AnimEmote-Effrayee", "AnimEmote-Bailler", "AnimEmote-Rire", "AnimEmote-Huss-Debut", "AnimEmote-Chercher", "AnimEmote-Huss-Debut" };
        m_dice = new Random(System.currentTimeMillis());
        (BREED_ORDER = new TObjectIntHashMap<AvatarBreed>()).put(AvatarBreed.SOUL, 1);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.IOP, 2);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.CRA, 5);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.ENIRIPSA, 9);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.SRAM, 4);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.ROUBLARD, 6);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.OSAMODAS, 10);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.ECAFLIP, 14);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.SADIDA, 3);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.ZOBAL, 7);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.ENUTROF, 11);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.SACRIER, 15);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.STEAMER, 8);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.PANDAWA, 12);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.FECA, 16);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.XELOR, 13);
        UICharacterCreationFrame.BREED_ORDER.put(AvatarBreed.ELIOTROPE, 17);
    }
    
    private class RandomBreed extends CharacterCreationDefinition
    {
        private AvatarBreedInfo m_breedInfo;
        
        private RandomBreed() {
            super(null);
            this.m_breedInfo = new RandomAvatarBreedInfo();
        }
        
        @Override
        public String[] getFields() {
            return AvatarBreedInfo.FIELDS;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("breedInfo")) {
                return this.m_breedInfo;
            }
            return super.getFieldValue(fieldName);
        }
        
        private class RandomAvatarBreedInfo extends AvatarBreedInfo
        {
            public RandomAvatarBreedInfo() {
                super(null);
            }
            
            @Override
            public Object getFieldValue(final String fieldName) {
                if (fieldName.equals("id")) {
                    return -1;
                }
                if (fieldName.equals("name")) {
                    return WakfuTranslator.getInstance().getString("randomBreed");
                }
                if (fieldName.equals("maleFemalePortraitIllustration")) {
                    try {
                        final String[] textureUrls = new String[2];
                        final String url = String.format(WakfuConfiguration.getInstance().getString("breedPortraitIllustrationPath"), "aleat");
                        textureUrls[1] = (textureUrls[0] = url);
                        return textureUrls;
                    }
                    catch (PropertyException e) {
                        RandomAvatarBreedInfo.m_logger.error((Object)"Exception", (Throwable)e);
                    }
                }
                return null;
            }
        }
    }
    
    private class BioumanTransform
    {
        private static final float INCREMENT = 0.3f;
        private static final int SEX_CHANGE_FRAME = 10;
        private static final int END_FRAME = 20;
        private final Direction8[] m_direction8Values;
        private int m_directionIndex;
        private float m_i;
        private int m_count;
        private byte m_sex;
        
        BioumanTransform(final byte sex) {
            super();
            this.m_direction8Values = Direction8.getDirection8Values();
            this.m_sex = sex;
            this.m_directionIndex = Direction8.SOUTH.getIndex();
            UICharacterCreationFrame.this.animatedElementViewer.setDirection(this.m_directionIndex);
        }
        
        public void start() {
            UICharacterCreationFrame.this.getLock();
            this.turn();
        }
        
        private void turn() {
            ++this.m_count;
            if (this.m_count == 10) {
                UICharacterCreationFrame.this.setCharacterSex(UICharacterCreationFrame.this.getPlayerInfo(), this.m_sex);
            }
            if (this.m_count > 20) {
                UICharacterCreationFrame.this.releaseLock();
                this.m_directionIndex = Direction8.SOUTH.getIndex();
                UICharacterCreationFrame.this.animatedElementViewer.setDirection(this.m_directionIndex);
                UICharacterCreationFrame.this.animatedElementViewer.setAnimName("AnimLevelUp");
                PropertiesProvider.getInstance().firePropertyValueChanged("characterCreation.editablePlayerInfo", "actorDescriptorLibrary");
                PropertiesProvider.getInstance().firePropertyValueChanged("characterCreation.editablePlayerInfo", "actorLinkage");
                return;
            }
            this.m_i += 0.3f;
            final int delay = MathHelper.fastFloor(MathHelper.sin(this.m_i) * 100.0f);
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    if (BioumanTransform.this.m_directionIndex > BioumanTransform.this.m_direction8Values.length - 1) {
                        BioumanTransform.this.m_directionIndex = 0;
                    }
                    UICharacterCreationFrame.this.animatedElementViewer.setDirection(BioumanTransform.this.m_direction8Values[BioumanTransform.this.m_directionIndex].getIndex());
                    BioumanTransform.this.m_directionIndex++;
                    BioumanTransform.this.turn();
                }
            }, delay, 1);
        }
    }
}
