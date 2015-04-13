package com.ankamagames.wakfu.client.core.effect;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ClientEffectApplier
{
    public static final ClientEffectApplier INSTANCE;
    
    public void reloadAntiAddictionEffects(final LocalPlayerCharacter player) {
        this.reloadEffects(player, 34, EffectContainerConstants.ANTI_ADDICTION_CONTAINER, player.getAntiAddictionEffects());
    }
    
    public void reloadGuildEffects(final LocalPlayerCharacter player) {
        this.reloadEffects(player, 32, EffectContainerConstants.GUILD_CONTAINER, player.getGuildEffects());
    }
    
    public void reloadHavenWorldEffects(final LocalPlayerCharacter player) {
        this.reloadEffects(player, 28, EffectContainerConstants.HAVEN_WORLD_CONTAINER, player.getHavenWorldEffects());
    }
    
    private void reloadEffects(final LocalPlayerCharacter player, final int type, final EffectContainer container, final TIntHashSet effects) {
        player.getRunningEffectManager().removeLinkedToContainerType(type);
        if (effects == null || effects.isEmpty()) {
            return;
        }
        final int[] effectsIds = effects.toArray();
        for (int i = 0; i < effectsIds.length; ++i) {
            final int effectsId = effectsIds[i];
            final WakfuEffect effect = EffectManager.getInstance().getEffect(effectsId);
            if (effect != null) {
                effect.execute(container, player, player.getEffectContext(), RunningEffectConstants.getInstance(), player.getWorldCellX(), player.getWorldCellY(), player.getWorldCellAltitude(), player, null, false);
            }
        }
    }
    
    public void reloadSubscriptionState(final LocalPlayerCharacter player) {
        player.getRunningEffectManager().removeLinkedToContainerType(35, true, false);
        final LocalAccountInformations localAccount = WakfuGameEntity.getInstance().getLocalAccount();
        final SubscriptionLevel subscriptionLevel = SubscriptionLevel.fromId(localAccount.getSubscriptionLevel());
        final short stateId = subscriptionLevel.getStateId();
        final GameInterval remainingDuration = this.getRemainingDuration(player, localAccount, stateId);
        if (stateId <= 0) {
            return;
        }
        if (!remainingDuration.isPositive()) {
            return;
        }
        this.applyState(player, stateId, remainingDuration);
    }
    
    private void applyState(final LocalPlayerCharacter player, final short stateId, final GameInterval remainingDuration) {
        final int stateUid = State.getUniqueIdFromBasicInformation(stateId, (short)1);
        final StateRunningEffect stateRunningEffect = StateRunningEffect.checkOut(player.getAppropriateContext(), player, EffectContainerConstants.SUBSCRIPTION_STATE_CONTAINER, stateUid);
        ((RunningEffect<FX, WakfuEffectContainer>)stateRunningEffect).setEffectContainer(EffectContainerConstants.SUBSCRIPTION_STATE_CONTAINER);
        stateRunningEffect.setUid(-stateUid);
        stateRunningEffect.setBaseUid(-stateUid);
        stateRunningEffect.setRemainingTimeInMs(remainingDuration.toLong());
        player.getRunningEffectManager().storeEffect(stateRunningEffect);
        player.applyStateEffects(stateId, (short)1, player.getAppropriateContext());
    }
    
    private GameInterval getRemainingDuration(final LocalPlayerCharacter player, final LocalAccountInformations localAccount, final short stateId) {
        final GameDate expirationDate = GameDate.fromLong(localAccount.getAccountExpirationDate());
        final GameDate now = WakfuGameCalendar.getInstance().getNewDate();
        final GameInterval remainingDuration = now.timeTo(expirationDate);
        if (!player.getCitizenComportment().hasRank(NationRank.GOVERNOR) || stateId == 0) {
            return remainingDuration;
        }
        final Nation nation = NationManager.INSTANCE.getNationById(player.getNationId());
        if (nation == null) {
            return remainingDuration;
        }
        final GameDate mandateEndDate = this.getMandateEndDate(nation);
        if (mandateEndDate.before(expirationDate)) {
            return remainingDuration;
        }
        return now.timeTo(mandateEndDate);
    }
    
    private GameDate getMandateEndDate(final Nation nation) {
        final GameDate endDate = new GameDate(nation.getVoteStartDate());
        endDate.add(nation.getVoteDuration());
        return endDate;
    }
    
    static {
        INSTANCE = new ClientEffectApplier();
    }
}
