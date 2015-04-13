package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class NetCharacterCreationFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final NetCharacterCreationFrame INSTANCE;
    private long m_characterId;
    private String m_characterName;
    private PlayerCharacter m_model;
    private CreationType m_creationType;
    private short m_recustomType;
    private byte m_source;
    
    public NetCharacterCreationFrame() {
        super();
        this.m_creationType = CreationType.DEFAULT;
    }
    
    public static NetCharacterCreationFrame getInstance() {
        return NetCharacterCreationFrame.INSTANCE;
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    public void setCharacterName(final String characterName) {
        this.m_characterName = characterName;
    }
    
    public void setCreationType(final CreationType creationType) {
        this.m_creationType = creationType;
    }
    
    public void setModel(final PlayerCharacter model) {
        this.m_model = model;
    }
    
    public void setRecustomType(final short recustomType) {
        this.m_recustomType = recustomType;
    }
    
    public void setSource(final byte source) {
        this.m_source = source;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 2050: {
                final boolean success = NetCharacterChoiceFrame.onCharacterSelectionResult((CharacterSelectionResultMessage)message, this);
                if (!success) {
                    WakfuProgressMonitorManager.getInstance().done();
                    WakfuGameEntity.getInstance().removeFrame(this);
                    NetCharacterChoiceFrame.getInstance().enableLoadUI(true);
                    WakfuGameEntity.getInstance().pushFrame(NetCharacterChoiceFrame.getInstance());
                }
                return false;
            }
            case 2054: {
                final CharacterCreationResultMessage msg = (CharacterCreationResultMessage)message;
                NetCharacterCreationFrame.m_logger.info((Object)("R\u00e9sultat de la cr\u00e9ation de perso : succes=" + msg.isCreationSuccessful() + ", code=" + msg.getCreationCode()));
                if (msg.isCreationSuccessful()) {
                    WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).beginTask(WakfuTranslator.getInstance().getString("loadingWorld.progress"), 0);
                }
                else {
                    WakfuProgressMonitorManager.getInstance().done();
                    displayNameError(msg.getCreationCode());
                    CharacterCreationDialogActions.setCreateCharacterFlag(false);
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public static void displayNameError(final byte errorCode) {
        String errorString = null;
        switch (errorCode) {
            case 10: {
                errorString = "error.characterCreation.existingName";
                break;
            }
            case 11: {
                errorString = "error.characterCreation.invalidName";
                break;
            }
            case 12: {
                errorString = "error.characterCreation.tooManyCharacters";
                break;
            }
            default: {
                errorString = "error.characterCreation";
                break;
            }
        }
        Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString(errorString), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 8, 1);
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
            UICharacterCreationFrame.getInstance().setCharacterId(this.m_characterId);
            UICharacterCreationFrame.getInstance().setCharacterName(this.m_characterName);
            UICharacterCreationFrame.getInstance().setCreationType(this.m_creationType);
            UICharacterCreationFrame.getInstance().setRecustomType(this.m_recustomType);
            UICharacterCreationFrame.getInstance().setSource(this.m_source);
            UICharacterCreationFrame.getInstance().setModel(this.m_model);
            WakfuGameEntity.getInstance().pushFrame(UICharacterCreationFrame.getInstance());
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            WakfuGameEntity.getInstance().removeFrame(UICharacterCreationFrame.getInstance());
            this.m_characterId = -1L;
            this.m_characterName = null;
            this.m_creationType = CreationType.DEFAULT;
            this.m_model = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetCharacterCreationFrame.class);
        INSTANCE = new NetCharacterCreationFrame();
    }
    
    public enum CreationType
    {
        DEFAULT(false, true, false), 
        RECUSTOM(false, false, true), 
        TUTORIAL(true, true, false);
        
        private final boolean m_forceSoulBreed;
        private final boolean m_canCancelCreation;
        private final boolean m_useModel;
        
        private CreationType(final boolean forceSoulBreed, final boolean canCancelCreation, final boolean useModel) {
            this.m_forceSoulBreed = forceSoulBreed;
            this.m_canCancelCreation = canCancelCreation;
            this.m_useModel = useModel;
        }
        
        public boolean isForceSoulBreed() {
            return this.m_forceSoulBreed;
        }
        
        public boolean isCanCancelCreation() {
            return this.m_canCancelCreation;
        }
        
        public boolean isUseModel() {
            return this.m_useModel;
        }
    }
}
