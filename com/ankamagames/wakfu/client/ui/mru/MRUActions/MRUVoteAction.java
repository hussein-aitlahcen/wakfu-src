package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.common.game.nation.government.*;

public class MRUVoteAction extends AbstractLawMRUAction implements MobileEndPathListener
{
    protected Point3 m_pathDestination;
    
    @Override
    public MRUActions tag() {
        return MRUActions.VOTE_ACTION;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        final NonPlayerCharacter npc = (NonPlayerCharacter)this.m_source;
        final Protector protector = npc.getProtector();
        if (protector != null) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (!localPlayer.cancelCurrentOccupation(false, true)) {
                return;
            }
            PropertiesProvider.getInstance().setPropertyValue("voteList", false);
            WakfuGameEntity.getInstance().pushFrame(UIVoteInformationFrame.getInstance());
        }
    }
    
    @Override
    public boolean isRunnable() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.isOnFight()) {
            return false;
        }
        if (ClientTradeHelper.INSTANCE.isTradeRunning()) {
            return false;
        }
        if (!(this.m_source instanceof NonPlayerCharacter)) {
            return false;
        }
        final Protector protector = ((NonPlayerCharacter)this.m_source).getProtector();
        if (protector == null) {
            return false;
        }
        final CitizenComportment citizen = localPlayer.getCitizenComportment();
        if (protector.getCurrentNationId() != citizen.getNationId()) {
            return false;
        }
        if (protector.getCurrentNationId() == 34) {
            return false;
        }
        final Nation nation = protector.getCurrentNation();
        if (nation == null) {
            return false;
        }
        if (!nation.isVoteRunning()) {
            return false;
        }
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        return player.hasSubscriptionRight(SubscriptionRight.ACCESS_TO_ELECTION);
    }
    
    @Override
    public String getLabel() {
        return super.getLabel();
    }
    
    @Override
    public String getTranslatorKey() {
        return "vote";
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUVoteAction();
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        WakfuGameEntity.getInstance().pushFrame(UIVoteInformationFrame.getInstance());
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.HAND.m_id;
    }
    
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }
    
    @Nullable
    @Override
    public List<NationLaw> getTriggeredLaws() {
        return null;
    }
    
    @Nullable
    @Override
    public List<NationLaw> getProbablyTriggeredLaws() {
        if (AbstractLawMRUAction.getCurrentNationAlignment() != NationAlignement.ALLIED) {
            return null;
        }
        final LocalPlayerCharacter citizen = WakfuGameEntity.getInstance().getLocalPlayer();
        final NationLawEvent event = new RegisterCandidateLawEvent(citizen);
        final List<NationLaw> triggeredLaws = event.getTriggeringLaws();
        final NationLawEvent voteLawEvent = new VoteLawEvent(citizen, -1L);
        triggeredLaws.addAll(voteLawEvent.getTriggeringLaws());
        final Nation nation = citizen.getCitizenComportment().getNation();
        final GovernmentInfo governor = nation.getGovernment().getGovernor();
        if (governor != null) {
            final NationLawEvent voteLawEvent2 = new VoteLawEvent(citizen, governor.getId());
            triggeredLaws.addAll(voteLawEvent2.getTriggeringLaws());
        }
        return triggeredLaws;
    }
    
    @Override
    public String toString() {
        return "MRUVoteAction{m_pathDestination=" + this.m_pathDestination + '}';
    }
}
