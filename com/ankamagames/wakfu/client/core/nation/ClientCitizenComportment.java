package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.impl.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.nation.crime.data.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.client.core.contentInitializer.*;

public class ClientCitizenComportment extends GameCitizenComportment
{
    private boolean m_hasVoted;
    private boolean m_candidate;
    private ClientCitizenComportementObserver m_view;
    
    private ClientCitizenComportment(@NotNull final Citizen citizen) {
        super(citizen);
    }
    
    public static ClientCitizenComportment createClientComportment(@NotNull final Citizen citizen) {
        final ClientCitizenComportment res = new ClientCitizenComportment(citizen);
        res.reset();
        res.setView(new ClientCitizenComportementView(res));
        return res;
    }
    
    public void setView(final ClientCitizenComportementObserver view) {
        this.m_view = view;
    }
    
    public ClientCitizenComportementObserver getView() {
        return this.m_view;
    }
    
    @Override
    public void copyFrom(final CitizenComportment comportment) {
        super.copyFrom(comportment);
        this.m_view = new ClientCitizenComportementView(this);
        this.m_hasVoted = comportment.hasVoted();
        this.m_candidate = comportment.isCandidate();
    }
    
    @Override
    public void setJobs(final Collection<NationJob> jobs) {
        for (final NationJob job : jobs) {
            if (job == NationJob.GUARD) {
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventGuardJobLearned());
                break;
            }
        }
        super.setJobs(jobs);
    }
    
    @Override
    public void setPvpState(final NationPvpState state) {
        if (((CharacterInfo)this.m_citizen).isLocalPlayer()) {
            if (state == NationPvpState.PVP_STARTING) {
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventPvpButtonClicked());
            }
            else if (state == NationPvpState.PVP_ON_LOCKED) {
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventPvpActivationFinished());
            }
            else if (state == NationPvpState.PVP_ON) {
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventPvpDisactivationUnlocked());
            }
        }
        super.setPvpState(state);
    }
    
    @Override
    public void setNation(@NotNull final Nation nation) {
        super.setNation(nation);
        final CharacterInfo pc = (CharacterInfo)this.m_citizen;
        if (pc.isLocalPlayer()) {
            NationDisplayer.getInstance().setNation(nation);
            StringFormatter.setNationName((nation == null) ? StringFormatter.DEFAULT_NAME : NationDisplayer.getInstance().getName());
            PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "hasNation");
            ClientCitizenComportment.m_logger.info((Object)("D\u00e9finition de la nation : " + nation));
        }
        if (this.m_view != null) {
            this.m_view.nationSet(nation);
        }
    }
    
    @Override
    public void resetToNation(@NotNull final Nation nation) {
        super.resetToNation(nation);
        this.m_hasVoted = false;
        this.m_candidate = false;
    }
    
    @Override
    public void reset() {
        super.reset();
        this.m_citizenScores.clear();
        this.m_offendedNations.clear();
        this.m_isPasseportActive = false;
        if (this.m_purgationCooldown != null) {
            this.m_purgationCooldown.reset();
            this.stopCrimePurgation();
        }
    }
    
    @Override
    public boolean hasVoted() {
        return this.m_hasVoted || super.hasVoted();
    }
    
    public void setHasVoted(final boolean hasVoted) {
        this.m_hasVoted = hasVoted;
        if (this.m_view != null) {
            this.m_view.hasVoted();
        }
    }
    
    @Override
    public void startCrimePurgation(final int nationId) {
        this.m_crimePurgationNationId = nationId;
        if (this.m_purgationCooldown != null) {
            ClientCitizenComportment.m_logger.info((Object)"Cooldown de Purgation d\u00e9j\u00e0 d\u00e9marr\u00e9");
            return;
        }
        this.m_purgationCooldown = new ClientCrimePurgation(this);
        ProcessScheduler.getInstance().schedule(this.m_purgationCooldown, 1000L, -1);
        this.m_purgationCooldown.onCoolDownLaunched();
    }
    
    @Override
    public void stopCrimePurgation() {
        ProcessScheduler.getInstance().remove(this.m_purgationCooldown);
        this.removePurgationCrimeScore();
        this.m_purgationCooldown = null;
        this.m_crimePurgationNationId = -1;
    }
    
    public void cancelCrimePurgation() {
        ProcessScheduler.getInstance().remove(this.m_purgationCooldown);
        this.m_purgationCooldown.stopCooldown();
        this.m_purgationCooldown = null;
        this.m_crimePurgationNationId = -1;
    }
    
    public void onJailCooldownUpdate(final int oldCrimePoints) {
        if (this.m_view != null) {
            this.m_view.onJailCooldownUpdate(oldCrimePoints);
        }
    }
    
    public void onCitizenScoreChanged(final int nationId, final int oldCrimePoints) {
        final CitizenRank oldRank = CitizenRankManager.getInstance().getRankFromCitizenScore(oldCrimePoints);
        final int deltaScore = this.m_citizenScores.get(nationId) - oldCrimePoints;
        ((PlayerCharacter)this.m_citizen).onCitizenScoreChanged(nationId, this, oldRank, this.getCitizenScoreForNation(nationId), deltaScore);
        if (this.m_crimePurgationNationId == nationId) {
            if (this.m_purgationCooldown != null) {
                this.m_purgationCooldown.addPoints(deltaScore, oldCrimePoints);
            }
            else {
                ClientCitizenComportment.m_logger.warn((Object)"Donn\u00e9es erron\u00e9es dans le client, on ne peut pas purger une peine de prison sans avoir de cooldown");
            }
        }
        if (this.m_view != null) {
            this.m_view.onCitizenScoreChanged(nationId, oldCrimePoints);
        }
    }
    
    public Citizen getCitizen() {
        return this.m_citizen;
    }
    
    public void updateCandidateInfo() {
        if (this.m_view != null) {
            this.m_view.updateCandidateInfo();
        }
    }
    
    public void updatePvp(final boolean full) {
        if (this.m_view != null) {
            this.m_view.updatePvpState(full);
        }
    }
    
    public void updateAppearance() {
        if (this.m_view != null) {
            this.m_view.updateAdditionalAppearance();
        }
    }
    
    public void resetOffendedNations() {
        this.m_offendedNations.clear();
    }
    
    @Override
    public void setPasseportActive(final boolean active) {
        super.setPasseportActive(active);
        if (this.m_view != null) {
            this.m_view.onPassportActiveChanged();
        }
    }
    
    @Override
    public boolean isCandidate() {
        return this.m_candidate;
    }
    
    public void setCandidate(final boolean candidate) {
        this.m_candidate = candidate;
    }
    
    @Override
    public void setRank(final NationRank rank) {
        super.setRank(rank);
        this.updateAppearance();
        if (this.m_citizen == WakfuGameEntity.getInstance().getLocalPlayer()) {
            ChatInitializer.getInstance().refreshPoliticPipes();
        }
    }
}
