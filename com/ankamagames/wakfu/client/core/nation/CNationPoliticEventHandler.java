package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.nation.*;

public class CNationPoliticEventHandler implements NationPoliticEventHandler
{
    private final Nation m_nation;
    
    public CNationPoliticEventHandler(final Nation nation) {
        super();
        this.m_nation = nation;
    }
    
    @Override
    public void onCandidateRegistered(final long citizenId) {
        final Citizen citizen = this.m_nation.getCitizen(citizenId);
        if (citizen == null) {
            return;
        }
        ((ClientCitizenComportment)citizen.getCitizenComportment()).updateCandidateInfo();
        final ClientCitizenComportment comportment = (ClientCitizenComportment)WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment();
        comportment.updateCandidateInfo();
        NationDisplayer.getInstance().updateUI();
    }
    
    @Override
    public void onCitizenHasVoted(final long citizenId, final long candidateId) {
    }
    
    @Override
    public void onCandidateWithdraw(final long citizenId) {
    }
    
    @Override
    public void onCandidateRevalidate(final long citizenId) {
    }
    
    @Override
    public void onForbiddenCandidatesChanged() {
    }
}
