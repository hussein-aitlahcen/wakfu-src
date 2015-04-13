package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fightChallenge.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.game.fightChallenge.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;

public class FightChallengeView extends AbstractFightChallengeView
{
    public FightChallengeView(final int challengeId) {
        super(challengeId);
    }
    
    @Override
    protected boolean isFailed() {
        return WakfuGameEntity.getInstance().getLocalPlayer().getFightChallengesContext().getChallengeState(this.m_challengeId) == FightChallengeState.FAILURE;
    }
    
    @Override
    protected boolean isCompleted() {
        return WakfuGameEntity.getInstance().getLocalPlayer().getFightChallengesContext().getChallengeState(this.m_challengeId) == FightChallengeState.SUCCESS;
    }
    
    @Override
    public String getRewards() {
        final ClientFightChallengeContext context = WakfuGameEntity.getInstance().getLocalPlayer().getFightChallengesContext();
        final State dropState = StateManager.getInstance().getState(2428);
        final State xpState = StateManager.getInstance().getState(2214);
        final StateWriter writer = new StateWriter(dropState, CastableDescriptionGenerator.DescriptionMode.ALL, (short)context.getChallengeDropLevel(this.m_challengeId));
        final ArrayList<String> dropDesc = writer.writeContainer();
        final StateWriter writer2 = new StateWriter(xpState, CastableDescriptionGenerator.DescriptionMode.ALL, (short)context.getChallengeXpLevel(this.m_challengeId));
        final ArrayList<String> xpDesc = writer2.writeContainer();
        final TextWidgetFormater sb = new TextWidgetFormater();
        for (int i = 0, size = dropDesc.size(); i < size; ++i) {
            final String desc = dropDesc.get(i);
            if (sb.length() != 0) {
                sb.newLine();
            }
            sb.append(desc);
        }
        for (int i = 0, size = xpDesc.size(); i < size; ++i) {
            final String desc = xpDesc.get(i);
            if (sb.length() != 0) {
                sb.newLine();
            }
            sb.append(desc);
        }
        return sb.finishAndToString();
    }
    
    public FightChallengeStaticView getStaticCopy() {
        return new FightChallengeStaticView(this.m_challengeId, WakfuGameEntity.getInstance().getLocalPlayer().getFightChallengesContext().getChallengeState(this.m_challengeId), this.getRewards());
    }
}
