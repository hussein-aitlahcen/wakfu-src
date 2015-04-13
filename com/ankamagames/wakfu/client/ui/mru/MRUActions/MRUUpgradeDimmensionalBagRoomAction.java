package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUUpgradeDimmensionalBagRoomAction extends AbstractMRUAction
{
    private DimensionalBagView m_dimensionalBag;
    private byte m_roomId;
    
    public MRUUpgradeDimmensionalBagRoomAction(final DimensionalBagView dimensionalBag, final byte roomId) {
        super();
        this.m_dimensionalBag = dimensionalBag;
        this.m_roomId = roomId;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUUpgradeDimmensionalBagRoomAction(this.m_dimensionalBag, this.m_roomId);
    }
    
    public MRUUpgradeDimmensionalBagRoomAction() {
        super();
    }
    
    @Override
    public boolean isRunnable() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return !localPlayer.isWaitingForResult() && !localPlayer.isOnFight() && !ClientTradeHelper.INSTANCE.isTradeRunning();
    }
    
    @Override
    public void run() {
        MRUUpgradeDimmensionalBagRoomAction.m_logger.error((Object)"MRUUpgradeDimmensionalBagRoomAction.run()");
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.DIMENSIONAL_BAG_UPDATE_ROOM_ACTION;
    }
    
    @Override
    public String getTranslatorKey() {
        return "bagUpgrade";
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.UPGRADE.m_id;
    }
}
