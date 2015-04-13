package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUCreatePrivateChatAction extends AbstractMRUAction
{
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUCreatePrivateChatAction();
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof PlayerCharacter)) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.isWaitingForResult()) {
            return false;
        }
        if (localPlayer.isActiveProperty(WorldPropertyType.PRIVATE_CHAT_MRU_DISABLED)) {
            return false;
        }
        final PlayerCharacter source = (PlayerCharacter)this.m_source;
        return !source.isActiveProperty(WorldPropertyType.PRIVATE_CHAT_MRU_DISABLED);
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUCreatePrivateChatAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        final PlayerCharacter player = (PlayerCharacter)this.m_source;
        final UIMessage msg = new UIMessage();
        msg.setId(19061);
        msg.setStringValue(player.getName());
        Worker.getInstance().pushMessage(msg);
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.CREATE_PRIVATE_CHAT_ACTION;
    }
    
    @Override
    public String getTranslatorKey() {
        return "createPrivateChat";
    }
    
    @Override
    public String getLabel() {
        if (!(this.m_source instanceof PlayerCharacter)) {
            return null;
        }
        return WakfuTranslator.getInstance().getString("desc.mru." + this.getTranslatorKey(), ((PlayerCharacter)this.m_source).getName());
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.BUBBLE.m_id;
    }
}
