package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.core.dialogclose.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.character.*;
import com.ankama.wakfu.utils.injection.*;
import com.ankamagames.wakfu.client.updater.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.clientToServer.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.characterChoiceSlot.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.reflect.*;

public class UICharacterChoiceFrame implements MessageFrame, DialogCloseRequestListener
{
    private static final Logger m_logger;
    private static UICharacterChoiceFrame m_instance;
    public static final String ANIM_CHARACTER_OUT = "AnimStatique";
    public static final String ANIM_CHARACTER_OVER = "AnimEmote-Coucou";
    public static final String ANIM_CHARACTER_DELETION = "AnimEmote-Defaite";
    public static final String ANIM_CHARACTER_DELETION_CONFIRMATION = "AnimEmote-Defaite";
    public static final String ANIM_CHARACTER_UNSELECTED = "AnimStatique";
    public static final String ANIM_CHARACTER_SELECTED = "AnimStatique02";
    private boolean m_askDelete;
    private MessageBoxControler m_confirmationMessageBoxControler;
    private boolean m_characterListLoaded;
    private ListScroller m_scroller;
    private List m_mini;
    
    private UICharacterChoiceFrame() {
        super();
        this.m_confirmationMessageBoxControler = null;
    }
    
    public static UICharacterChoiceFrame getInstance() {
        return UICharacterChoiceFrame.m_instance;
    }
    
    public void setScrollMode(final ListScroller.ScrollMode mode) {
        this.m_scroller.setScrollMode(mode);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16514: {
                final UIPlayerInfoMessage msg = (UIPlayerInfoMessage)message;
                final PlayerCharacter playerCharacter = msg.getPlayerInfo();
                if (playerCharacter != null) {
                    final boolean loading = !Injection.getInstance().getInstance(IComponentManager.class).hasComponentsCompleted(Component.FULL);
                    if (loading && playerCharacter.getBreedId() != AvatarBreed.SOUL.getBreedId()) {
                        return false;
                    }
                    WakfuSoundManager.getInstance().playGUISound(600003L);
                    if (playerCharacter.isNeedsRecustom()) {
                        if (!Injection.getInstance().getInstance(IComponentManager.class).hasComponentsCompleted(Component.FULL)) {
                            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.characterSelection.updating"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 7, 1);
                            return false;
                        }
                        this.goToCharacterCreation(NetCharacterCreationFrame.CreationType.RECUSTOM, playerCharacter.getId(), playerCharacter.getName(), playerCharacter.getRecustomType(), playerCharacter);
                    }
                    else {
                        WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).beginTask(WakfuTranslator.getInstance().getString("loadingWorld.progress"), 0);
                        WakfuSceneFader.getInstance().prepareFadeIn();
                        final CharacterSelectionMessage netMessage = new CharacterSelectionMessage();
                        netMessage.setCharacterId(playerCharacter.getId());
                        NetWorldFrame.getInstance().setWaitForFirstCharacterInfoMessage(true);
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                        WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.LAST_SELECTED_CHARACTER_NAME, playerCharacter.getName());
                    }
                }
                return false;
            }
            case 16511: {
                final UIPlayerInfoMessage msg = (UIPlayerInfoMessage)message;
                final PlayerCharacter playerCharacter = msg.getPlayerInfo();
                if (playerCharacter != null) {
                    this.m_askDelete = true;
                    final CharacterActor actor = playerCharacter.getActor();
                    actor.setAnimation("AnimEmote-Defaite");
                    final MessageBoxData data = new MessageBoxData(102, 0, WakfuTranslator.getInstance().getString("question.removeCharacter", playerCharacter.getName()), 65542L);
                    final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
                    controler.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type != 2) {
                                return;
                            }
                            if (userEntry.equalsIgnoreCase(playerCharacter.getName())) {
                                final CharacterDeletionMessage netMessage = new CharacterDeletionMessage();
                                netMessage.setCharacterId(playerCharacter.getId());
                                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                            }
                        }
                    });
                }
                return false;
            }
            case 16413: {
                final boolean loading2 = !Injection.getInstance().getInstance(IComponentManager.class).hasComponentsCompleted(Component.FULL);
                if (loading2) {
                    this.goToCharacterCreation(NetCharacterCreationFrame.CreationType.TUTORIAL, -1L, null, (short)0, null);
                    return false;
                }
                final boolean forceTuto = WakfuConfiguration.getInstance().getBoolean("characterCreation.tuto.force", false);
                if (forceTuto && LocalCharacterInfosManager.getInstance().getNumbersOfCharacterInfos() <= 0) {
                    this.goToCharacterCreation(NetCharacterCreationFrame.CreationType.TUTORIAL, -1L, null, (short)0, null);
                    return false;
                }
                final String msgText = WakfuTranslator.getInstance().getString("question.characterCreation.tutorial");
                final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 24L);
                final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            UICharacterChoiceFrame.this.goToCharacterCreation(NetCharacterCreationFrame.CreationType.TUTORIAL, -1L, null, (short)0, null);
                        }
                        else {
                            UICharacterChoiceFrame.this.goToCharacterCreation(NetCharacterCreationFrame.CreationType.DEFAULT, -1L, null, (short)0, null);
                        }
                    }
                });
                return false;
            }
            case 16414: {
                final CharacterChoiceLeaveRequestMessage choiceLeaveRequestMessage = new CharacterChoiceLeaveRequestMessage();
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(choiceLeaveRequestMessage);
                return false;
            }
            case 16510: {
                final UIPlayerInfoMessage msg = (UIPlayerInfoMessage)message;
                this.selectPlayerInfo(msg.getPlayerInfo());
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void goToCharacterCreation(final NetCharacterCreationFrame.CreationType creationType, final long id, final String name, final short recustomType, final PlayerCharacter model) {
        WakfuGameEntity.getInstance().removeFrame(NetCharacterChoiceFrame.getInstance());
        NetCharacterCreationFrame.getInstance().setCreationType(creationType);
        NetCharacterCreationFrame.getInstance().setCharacterId(id);
        NetCharacterCreationFrame.getInstance().setCharacterName(name);
        NetCharacterCreationFrame.getInstance().setModel(model);
        NetCharacterCreationFrame.getInstance().setRecustomType(recustomType);
        WakfuGameEntity.getInstance().pushFrame(NetCharacterCreationFrame.getInstance());
    }
    
    public void selectPlayerInfo(final CharacterInfo playerCharacter) {
        final CharacterInfo oldSelected = LocalCharacterInfosManager.getInstance().getSelectedCharacterInfo();
        if (playerCharacter != oldSelected) {
            this.setAnimUnselected(oldSelected);
            this.setAnimSelected(playerCharacter);
        }
        LocalCharacterInfosManager.getInstance().selectCharacterInfo(playerCharacter);
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            final LocalCharacterInfosManager characterInfoManager = LocalCharacterInfosManager.getInstance();
            PropertiesProvider.getInstance().setPropertyValue("characterChoice.characterInfosList", CharacterChoiceSlots.INSTANCE);
            final EventDispatcher dialog = Xulor.getInstance().load("characterChoiceDialog", Dialogs.getDialogPath("characterChoiceDialog"), 8192L, (short)10000);
            final List list = (List)dialog.getElementMap().getElement("characterList");
            list.addListContentListener(new EditableRenderableCollection.CollectionContentLoadedListener() {
                @Override
                public void onContentLoaded() {
                    if (!UICharacterChoiceFrame.this.m_characterListLoaded) {
                        return;
                    }
                    if (characterInfoManager.getSelectedCharacterInfo() == null) {
                        final String lastCharacterName = WakfuClientInstance.getInstance().getGamePreferences().getStringValue(WakfuKeyPreferenceStoreEnum.LAST_SELECTED_CHARACTER_NAME);
                        if (lastCharacterName != null) {
                            final CharacterInfo characterByName = characterInfoManager.getCharacterInfoByName(lastCharacterName);
                            if (characterByName != null) {
                                characterInfoManager.selectCharacterInfo(characterByName);
                            }
                        }
                    }
                    final int characterOffset = CharacterChoiceSlots.INSTANCE.getCharacterOffset(characterInfoManager.getSelectedCharacterInfo());
                    final int index = list.getSelectedOffsetByValue(characterInfoManager.getSelectedCharacterInfo());
                    if (characterOffset != -1) {
                        final int offset = MathHelper.clamp(characterOffset - 5, 0, list.size() - 5);
                        list.setListOffset(offset);
                        return;
                    }
                    final RenderableContainer renderableByOffset = list.getRenderableByOffset(index);
                    if (renderableByOffset == null) {
                        list.removeListContentLoadListener(this);
                        return;
                    }
                    final Image image = (Image)renderableByOffset.getInnerElementMap().getElement("illustration");
                    image.setVisible(true);
                    CharacterChoiceDialogActions.setImage(image);
                    list.removeListContentLoadListener(this);
                }
            });
            this.m_mini = (List)dialog.getElementMap().getElement("miniList");
            list.addOffsetListener(new EditableRenderableCollection.CollectionOffsetListener() {
                @Override
                public void onOffsetChanged(final float offset) {
                    UICharacterChoiceFrame.this.repositionMiniList(offset);
                }
            });
            this.m_mini.addListContentListener(new EditableRenderableCollection.CollectionContentLoadedListener() {
                @Override
                public void onContentLoaded() {
                    UICharacterChoiceFrame.this.repositionMiniList(list.getOffset());
                }
            });
            DialogClosesManager.getInstance().addDialogCloseRequestListener(this);
            Xulor.getInstance().putActionClass("wakfu.characterChoice", CharacterChoiceDialogActions.class);
            WakfuSoundManager.getInstance().onBackToLogin();
            this.refreshSelectPlayerInfo();
            UIWebShopFrame.getInstance().requestLockForUI("characterChoiceDialog");
            (this.m_scroller = new ListScroller(list)).start();
            UISystemBarFrame.getInstance().reloadMenuBarDialog();
        }
    }
    
    private void repositionMiniList(final float offset) {
        if (this.m_mini != null) {
            final int width = this.m_mini.getContainer().getAppearance().getContentWidth();
            final float x = Alignment9.CENTER.getX(152, width) - 25.0f * offset + 13.0f;
            this.m_mini.setX((int)x);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_characterListLoaded = false;
            LocalCharacterInfosManager.getInstance().setLastCreatedCharacter(null);
            LocalCharacterInfosManager.getInstance().selectCharacterInfo(null);
            PropertiesProvider.getInstance().removeProperty("characterChoice.characterInfosList");
            DialogClosesManager.getInstance().removeDialogCloseRequestListener(this);
            PropertiesProvider.getInstance().removeProperty("characterChoice.selectedCharacter");
            Xulor.getInstance().unload("characterChoiceDialog");
            Xulor.getInstance().removeActionClass("wakfu.characterChoice");
            this.m_scroller.stop();
        }
    }
    
    @Override
    public int onDialogCloseRequest(final String id) {
        if (WakfuProgressMonitorManager.getInstance().isRunning()) {
            WakfuGameEntity.getInstance().logoff();
            WakfuProgressMonitorManager.getInstance().done();
        }
        return 0;
    }
    
    public final boolean isAskForDelete() {
        return this.m_askDelete;
    }
    
    private void setAnimSelected(final CharacterInfo character) {
        if (character != null) {
            final CharacterActor actor = character.getActor();
            assert actor != null;
            actor.setAnimation("AnimStatique02");
            actor.setStaticAnimationKey("AnimStatique02");
            PropertiesProvider.getInstance().firePropertyValueChanged(character, "actorDescriptorLibrary", "actorAnimation");
        }
    }
    
    private void setAnimUnselected(final CharacterInfo character) {
        if (character != null) {
            final CharacterActor actor = character.getActor();
            assert actor != null;
            actor.setAnimation("AnimStatique");
            actor.setStaticAnimationKey("AnimStatique");
            PropertiesProvider.getInstance().firePropertyValueChanged(character, "actorDescriptorLibrary");
            PropertiesProvider.getInstance().firePropertyValueChanged(character, "actorAnimation");
        }
    }
    
    public void refreshSelectPlayerInfo() {
        final CharacterInfo selectedCharacter = LocalCharacterInfosManager.getInstance().getSelectedCharacterInfo();
        this.setAnimSelected(selectedCharacter);
        PropertiesProvider.getInstance().setPropertyValue("characterChoice.selectedCharacter", selectedCharacter);
    }
    
    public void setCharacterListLoaded(final boolean characterListLoaded) {
        this.m_characterListLoaded = characterListLoaded;
    }
    
    public void openRenameDialog(final long characterId) {
        if (characterId != LocalCharacterInfosManager.getInstance().getSelectedCharacterInfo().getId()) {
            LocalCharacterInfosManager.getInstance().selectCharacterInfo(LocalCharacterInfosManager.getInstance().getCharacterInfoById(characterId));
        }
        Xulor.getInstance().load("renameCharacterDialog", Dialogs.getDialogPath("renameCharacterDialog"), 8448L, (short)10000);
        PropertiesProvider.getInstance().setPropertyValue("renameCharater.dirty", false);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UICharacterChoiceFrame.class);
        UICharacterChoiceFrame.m_instance = new UICharacterChoiceFrame();
    }
}
