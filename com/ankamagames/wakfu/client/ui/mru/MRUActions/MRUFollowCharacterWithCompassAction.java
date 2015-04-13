package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUFollowCharacterWithCompassAction extends AbstractMRUAction
{
    protected AbstractMRUAction constructeurVirtuel() {
        return new MRUFollowCharacterWithCompassAction();
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUFollowCharacterWithCompassAction();
    }
    
    @Override
    public boolean isRunnable() {
        if (this.m_source == null) {
            return false;
        }
        if (!(this.m_source instanceof PlayerCharacter)) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return !localPlayer.isWaitingForResult() && !localPlayer.isOnFight() && !ClientTradeHelper.INSTANCE.isTradeRunning();
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUFollowCharacterWithCompassAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        final PlayerCharacter player = (PlayerCharacter)this.m_source;
        MapManagerHelper.addCharacterCompass(player, MapManager.getInstance());
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.FOLLOW_CHARACTER_WITH_COMPASS;
    }
    
    @Override
    public String getTranslatorKey() {
        return "followCharacterWithCompass";
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.PIN.m_id;
    }
}
