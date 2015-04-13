package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.game.title.*;
import com.ankamagames.wakfu.client.core.effect.*;
import com.ankamagames.wakfu.common.game.lock.*;

class WakfuAccountInformationListener implements com.ankamagames.wakfu.common.account.WakfuAccountInformationListener
{
    private LocalPlayerCharacter m_localPlayerCharacter;
    
    WakfuAccountInformationListener(final LocalPlayerCharacter localPlayerCharacter) {
        super();
        this.m_localPlayerCharacter = localPlayerCharacter;
    }
    
    @Override
    public void onSubscriptionRightChanged(final SubscriptionLevel previousActiveLevel, final SubscriptionLevel newActiveLevel) {
        final LockContext locks = this.m_localPlayerCharacter.getLockContext();
        if (locks != null) {
            if (this.m_localPlayerCharacter.hasSubscriptionRight(SubscriptionRight.NO_DUNGEON_DAILY_LIMITATION)) {
                locks.removeLock(-1);
            }
            else if (!locks.containsLock(-1)) {
                locks.addLock(new LockData(LockConstants.DAILY_LOCK_DEFINITION));
            }
        }
        SubscriptionEmoteAndTitleLimitations.resetCurrentTitleIfNecessary(this.m_localPlayerCharacter, this.m_localPlayerCharacter.getAccountInformationHandler().getActiveSubscriptionLevel());
        this.m_localPlayerCharacter.getShortcutBarManager().updateShortcutBars();
        SubscriptionEndPopupDisplayer.INSTANCE.displayPopupIfNecessary(previousActiveLevel, newActiveLevel);
    }
}
