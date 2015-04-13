package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.baseImpl.graphics.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.government.*;

public class CharacterAdditionalAppearance implements VisibleChangedListener, MessageHandler
{
    private static final int DURATION_BEFORE_CYCLE = 3000;
    private final CharacterInfo m_player;
    private FreeParticleSystem m_particle;
    private final TIntArrayList m_particleFileIds;
    private int m_index;
    
    CharacterAdditionalAppearance(final CharacterInfo player) {
        super();
        this.m_particleFileIds = new TIntArrayList();
        this.m_player = player;
        MessageScheduler.getInstance().addClock(this, 3000L, -1);
    }
    
    void copyFrom(final CharacterAdditionalAppearance appearanceToCopy) {
        this.unApplyParticle();
        if (appearanceToCopy.m_particle != null) {
            this.applyParticle(appearanceToCopy.m_particle.m_fileId, appearanceToCopy.m_player.getCurrentFightId());
        }
    }
    
    void updateAdditionalAppearance() {
        this.computeParticleList();
        if (this.m_player.getCurrentFight() == null) {
            this.m_player.applyRankStuff();
        }
    }
    
    void unApplyParticle() {
        if (this.m_particle == null) {
            return;
        }
        if (this.m_player.hasActor()) {
            this.m_player.getActor().removeVisibleChangedListener(this);
        }
        IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_particle.getId());
        this.m_particle = null;
    }
    
    private void applyParticle(final int particleId) throws UnsupportedOperationException {
        final int fightId = this.m_player.getCurrentFightId();
        this.applyParticle(particleId, fightId);
    }
    
    private void applyParticle(final int particleId, final int fightId) {
        if (this.m_particle != null) {
            throw new UnsupportedOperationException("Tentative d'ajout de particule d'apparence sans avoir d\u00e9s-appliqu\u00e9 l'ancienne");
        }
        final CharacterActor actor = this.m_player.getActor();
        (this.m_particle = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(particleId, 0)).setTarget(actor, 1.0f, 2);
        this.m_particle.setFightId(fightId);
        this.m_particle.setVisible(actor.isVisible());
        IsoParticleSystemManager.getInstance().addParticleSystem(this.m_particle);
        actor.addVisibleChangedListener(this);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        this.incrementIndex();
        this.applyCurrentParticle();
        return false;
    }
    
    private void applyCurrentParticle() {
        final int particleFileId = this.getCurrentParticleFileId();
        if (this.m_particle != null && this.m_particle.getFileId() != particleFileId) {
            this.unApplyParticle();
        }
        if (particleFileId == -1) {
            return;
        }
        if (this.m_particle == null) {
            this.applyParticle(particleFileId);
        }
    }
    
    private void computeParticleList() {
        final int currentFileId = this.getCurrentParticleFileId();
        this.m_particleFileIds.clear();
        if (this.m_player.getCurrentFight() == null) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer != null) {
                final PartyComportment partyComportment = localPlayer.getPartyComportment();
                if (partyComportment.isInParty() && partyComportment.getParty().contains(this.m_player.getId())) {
                    this.m_particleFileIds.add(800255);
                }
            }
            final CitizenComportment comportment = this.m_player.getCitizenComportment();
            final NationRank rank = comportment.getRank();
            final int rankParticleId = NationRankEquipmentHelper.getParticleForRank(rank);
            if (rankParticleId != -1) {
                this.m_particleFileIds.add(rankParticleId);
            }
            final int pvpParticle = NationPvpHelper.getPvpParticle(this.m_player);
            if (pvpParticle != -1) {
                this.m_particleFileIds.add(pvpParticle);
            }
        }
        this.m_index = this.m_particleFileIds.indexOf(currentFileId);
    }
    
    private void incrementIndex() {
        if (this.m_particleFileIds.isEmpty()) {
            this.m_index = -1;
            return;
        }
        this.m_index = (this.m_index + 1) % this.m_particleFileIds.size();
    }
    
    private int getCurrentParticleFileId() {
        if (this.m_index >= 0 && this.m_index < this.m_particleFileIds.size()) {
            return this.m_particleFileIds.get(this.m_index);
        }
        return -1;
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onVisibleChanged(final boolean visible, final VisibleChangedCause cause) {
        this.m_particle.setVisible(visible);
    }
    
    public void clean() {
        this.unApplyParticle();
        MessageScheduler.getInstance().removeAllClocks(this);
    }
}
