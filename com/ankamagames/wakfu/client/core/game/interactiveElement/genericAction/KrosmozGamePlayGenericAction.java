package com.ankamagames.wakfu.client.core.game.interactiveElement.genericAction;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.webBrowser.*;
import com.ankamagames.wakfu.client.core.*;

public class KrosmozGamePlayGenericAction extends AbstractClientGenericAction
{
    public KrosmozGamePlayGenericAction(final int id) {
        super(id);
    }
    
    @Override
    protected void runAction() {
        final LocalPlayerCharacter concernedPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final byte gameId = (byte)((NumericalValue)this.getParam(0)).getLongValue(concernedPlayer, null, null, concernedPlayer.getAppropriateContext());
        final KrosmozGame game = KrosmozGame.byId(gameId);
        if (WakfuGameCalendar.getInstance().getDate().before(game.getUnlockDate())) {
            return;
        }
        SWFWrapper.INSTANCE.toggleDisplay(game);
    }
    
    @Override
    public boolean isRunnable(final CharacterInfo concernedPlayer) {
        return SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.KROSMOZ_GAMES_ENABLE);
    }
    
    @Override
    public boolean isEnabled(final CharacterInfo concernedPlayer) {
        final long gameId = ((NumericalValue)this.getParam(0)).getLongValue(concernedPlayer, null, null, concernedPlayer.getAppropriateContext());
        final KrosmozGame game = KrosmozGame.byId((byte)gameId);
        if (WakfuGameCalendar.getInstance().getDate().before(game.getUnlockDate())) {
            return false;
        }
        if (!WakfuSWT.isInit()) {
            this.setErrorMessage(WakfuTranslator.getInstance().getString("krosmoz.gameBoard.systemRequirementsNotMet"));
            return false;
        }
        return true;
    }
}
