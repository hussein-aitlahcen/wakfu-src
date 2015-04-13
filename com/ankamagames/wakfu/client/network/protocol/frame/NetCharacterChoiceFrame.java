package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient.*;
import com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.characterChoiceSlot.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.network.protocol.frame.secretQuestion.*;
import com.ankamagames.baseImpl.graphics.ui.shortcuts.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.game.embeddedTutorial.*;

public class NetCharacterChoiceFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static NetCharacterChoiceFrame m_instance;
    private boolean m_loadUI;
    
    public static NetCharacterChoiceFrame getInstance() {
        return NetCharacterChoiceFrame.m_instance;
    }
    
    public void enableLoadUI(final boolean enable) {
        this.m_loadUI = enable;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 2069: {
                final ClientAdditionalCharacterSlotsUpdateMessage msg = (ClientAdditionalCharacterSlotsUpdateMessage)message;
                this.onAdditionalAccountInformationMessage(msg);
                return false;
            }
            case 2048: {
                final CharactersListMessage msg2 = (CharactersListMessage)message;
                this.onCharacterListMessage(msg2);
                return false;
            }
            case 2052: {
                final CharacterDeletionResultMessage msg3 = (CharacterDeletionResultMessage)message;
                this.onCharacterDeletionResultMessage(msg3);
                return false;
            }
            case 2050: {
                onCharacterSelectionResult((CharacterSelectionResultMessage)message, this);
                return false;
            }
            case 2056: {
                this.onCharacterChoiceLeaveResultMessage();
                return false;
            }
            case 2070: {
                final CharacterRenameOrderMessage msg4 = (CharacterRenameOrderMessage)message;
                this.onCharacterRenameOrderMessage(msg4);
                return false;
            }
            case 2072: {
                final CharacterRenameResultMessage msg5 = (CharacterRenameResultMessage)message;
                this.onCharacterRenameResultMessage(msg5);
                return false;
            }
            case 1202: {
                final WorldSelectionResultMessage msg6 = (WorldSelectionResultMessage)message;
                if (msg6.getErrorCode() != 0) {
                    if (msg6.getErrorCode() != 9) {
                        final String errorString = "error.worldSelection";
                        Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.worldSelection"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 3, 2);
                        WakfuProgressMonitorManager.getInstance().done();
                    }
                }
                return false;
            }
            case 2080: {
                final String wakfuTranslatorString = "error.connection.deleteUnderModeration";
                Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.connection.deleteUnderModeration"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 6, 2);
                return false;
            }
            case 9: {
                final ProxyRelayErrorMessage msg7 = (ProxyRelayErrorMessage)message;
                if (msg7.getServerGroup() == 1) {
                    NetAuthenticationFrame.getInstance().loginError(message, "error.connection.loginServerDown", new Object[0]);
                    WakfuGameEntity.getInstance().removeFrame(this);
                    return false;
                }
                break;
            }
        }
        return true;
    }
    
    private void onAdditionalAccountInformationMessage(final ClientAdditionalCharacterSlotsUpdateMessage msg) {
        LocalCharacterInfosManager.getInstance().setAdditionalCharacterSlots(msg.getAdditionalSlots());
    }
    
    private void onCharacterRenameResultMessage(final CharacterRenameResultMessage msg) {
        final byte renameErrorCode = msg.getErrorCode();
        NetCharacterChoiceFrame.m_logger.info((Object)("R\u00e9sultat du renommage de personnage : errorCode=" + renameErrorCode));
        if (renameErrorCode == 0) {
            Xulor.getInstance().unload("renameCharacterDialog");
            PropertiesProvider.getInstance().removeProperty("renameCharater.dirty");
        }
        else {
            PropertiesProvider.getInstance().setPropertyValue("renameCharater.dirty", false);
            NetCharacterCreationFrame.displayNameError(renameErrorCode);
        }
    }
    
    private void onCharacterListMessage(final CharactersListMessage msg) {
        final ArrayList<byte[]> serializedCharacters = msg.getSerializedCharacters();
        if (!WakfuGameEntity.getInstance().hasFrame(UICharacterChoiceFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UICharacterChoiceFrame.getInstance());
        }
        WakfuGameEntity.getInstance().removeFrame(NetConnectionQueueFrame.INSTANCE);
        try {
            LocalCharacterInfosManager.getInstance().setSerializedCharacters(serializedCharacters);
        }
        catch (Exception ex) {
            NetCharacterChoiceFrame.m_logger.error((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation de la liste des personnages", (Throwable)ex);
        }
        if (LocalCharacterInfosManager.getInstance().getNumbersOfCharacterInfos() > 0) {
            final CharacterInfo createdCharacterInfo = LocalCharacterInfosManager.getInstance().getLastCreatedCharacterInfo();
            UICharacterChoiceFrame.getInstance().selectPlayerInfo(createdCharacterInfo);
        }
        LocalCharacterInfosManager.getInstance().checkCharactersConfigDirectory();
        CharacterChoiceSlots.INSTANCE.requestUpdateShop(new Runnable() {
            @Override
            public void run() {
                NetCharacterChoiceFrame.this.endLoadingOnDoneProcessing();
            }
        });
    }
    
    private void onCharacterDeletionResultMessage(final CharacterDeletionResultMessage msg) {
        if (msg.isCharacterDeletionSuccessful()) {
            final long characterId = msg.getCharacterId();
            LocalCharacterInfosManager.getInstance().removeCharacterInfo(characterId);
            HeroesManager.INSTANCE.removeHero(characterId);
            UICharacterChoiceFrame.getInstance().refreshSelectPlayerInfo();
            LocalCharacterInfosManager.getInstance().checkCharactersConfigDirectory();
        }
        else {
            final String errorString = "error.characterDeletion";
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.characterDeletion"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 6, 1);
        }
        if (WakfuGameEntity.getInstance().hasFrame(NetSecretQuestionFrame.INSTANCE)) {
            WakfuGameEntity.getInstance().removeFrame(NetSecretQuestionFrame.INSTANCE);
        }
    }
    
    private void onCharacterChoiceLeaveResultMessage() {
        WakfuGameEntity.getInstance().removeFrame(NetCharacterChoiceFrame.m_instance);
        WakfuGameEntity.getInstance().pushFrame(NetAuthenticationFrame.getInstance());
        LocalCharacterInfosManager.getInstance().removeAllCharacterInfos();
        this.endLoadingOnDoneProcessing();
    }
    
    private void onCharacterRenameOrderMessage(final CharacterRenameOrderMessage msg) {
        final long characterId = msg.getCharacterId();
        UICharacterChoiceFrame.getInstance().openRenameDialog(characterId);
    }
    
    public static boolean onCharacterSelectionResult(final CharacterSelectionResultMessage msg, final MessageFrame frame) {
        if (msg.getErrorCode() == 0) {
            WakfuGameEntity.getInstance().removeFrame(frame);
            WakfuGameEntity.getInstance().pushFrame(NetHavenWorldFrame.INSTANCE);
            WakfuGameEntity.getInstance().pushFrame(NetWorldFrame.getInstance());
            WakfuGameEntity.getInstance().pushFrame(NetNationFrame.INSTANCE);
            WakfuGameEntity.getInstance().pushFrame(NetPvpFrame.INSTANCE);
            WakfuGameEntity.getInstance().pushFrame(NetSpellsRestatFrame.INSTANCE);
            ShortcutManager.getInstance().enableGroup("world", true);
            ShortcutManager.getInstance().enableGroup("common", true);
            ShortcutManager.getInstance().enableGroup("binding", true);
            LocalCharacterInfosManager.getInstance().removeAllCharacterInfos();
            return true;
        }
        final String errorString = "error.characterSelection";
        Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.characterSelection"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 7, 1);
        return false;
    }
    
    private void endLoadingOnDoneProcessing() {
        RenderableContainerManager.getInstance().addListener(new RenderableContainerManager.RenderableContainerManagerListener() {
            @Override
            public void onDoneProcessing() {
                WakfuProgressMonitorManager.getInstance().done();
                RenderableContainerManager.getInstance().removeListener(this);
            }
        });
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
            if (this.m_loadUI) {
                WakfuGameEntity.getInstance().pushFrame(UICharacterChoiceFrame.getInstance());
            }
            EmbeddedTutorialManager.getInstance().setEnabled(false);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().unload("renameCharacterDialog");
            WakfuGameEntity.getInstance().removeFrame(UICharacterChoiceFrame.getInstance());
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetCharacterChoiceFrame.class);
        NetCharacterChoiceFrame.m_instance = new NetCharacterChoiceFrame();
    }
}
