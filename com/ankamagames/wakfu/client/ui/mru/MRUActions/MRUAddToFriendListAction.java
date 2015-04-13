package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUAddToFriendListAction extends AbstractMRUAction
{
    @Override
    public MRUActions tag() {
        return MRUActions.ADD_TO_FRIEND_LIST;
    }
    
    @Override
    public void run() {
        final PlayerCharacter target = (PlayerCharacter)this.m_source;
        final AddFriendMessage addFriendMessage = new AddFriendMessage();
        addFriendMessage.setFriendName(target.getName());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(addFriendMessage);
    }
    
    @Override
    public boolean isEnabled() {
        if (!super.isEnabled()) {
            return false;
        }
        if (!(this.m_source instanceof PlayerCharacter)) {
            return false;
        }
        final PlayerCharacter cible = (PlayerCharacter)this.m_source;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return cible != null && localPlayer != null;
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof PlayerCharacter)) {
            return false;
        }
        final PlayerCharacter cible = (PlayerCharacter)this.m_source;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuUserGroupManager.getInstance().getFriendGroup().getUserById(cible.getOwnerId()) == null;
    }
    
    @Override
    public String getTranslatorKey() {
        return "addToFriendList";
    }
    
    @Override
    public String getLabel() {
        if (!(this.m_source instanceof PlayerCharacter)) {
            return null;
        }
        return WakfuTranslator.getInstance().getString("desc.mru." + this.getTranslatorKey(), ((PlayerCharacter)this.m_source).getName());
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUAddToFriendListAction();
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.FRIENDS.m_id;
    }
}
