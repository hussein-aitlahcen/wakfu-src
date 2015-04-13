package com.ankamagames.wakfu.client.core.effectArea.graphics;

import com.ankamagames.wakfu.common.game.fight.bombCombination.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import gnu.trove.*;

public final class BombCombinationDisplay implements BombCombinationModificationListener
{
    private final int m_fightId;
    private final TIntHashSet m_particleIds;
    private final TLongHashSet m_particleDisplayedPositions;
    
    public BombCombinationDisplay(final int id) {
        super();
        this.m_particleIds = new TIntHashSet();
        this.m_particleDisplayedPositions = new TLongHashSet();
        this.m_fightId = id;
    }
    
    @Override
    public void onCombinationUpdated(final BombCombinationComputer bombCombinationComputer) {
        this.removeParticles();
        final List<List<AbstractBombEffectArea>> combinations = bombCombinationComputer.getBombCombinations();
        for (final List<AbstractBombEffectArea> combination : combinations) {
            if (combination.size() < 3) {
                continue;
            }
            this.displayCombination(combination);
        }
    }
    
    private void displayCombination(final List<AbstractBombEffectArea> combination) {
        for (final AbstractBombEffectArea bomb : combination) {
            final int x = bomb.getWorldCellX();
            final int y = bomb.getWorldCellY();
            this.addParticlesAllAroundPosition(x, y);
        }
    }
    
    private void addParticlesAllAroundPosition(final int x, final int y) {
        this.addParticle(x, y);
        this.addParticle(x + 1, y);
        this.addParticle(x, y + 1);
        this.addParticle(x + 1, y + 1);
        this.addParticle(x - 1, y);
        this.addParticle(x, y - 1);
        this.addParticle(x - 1, y - 1);
        this.addParticle(x - 1, y + 1);
        this.addParticle(x + 1, y - 1);
    }
    
    private void addParticle(final int x, final int y) {
        final long posToLong = MathHelper.getLongFromTwoInt(x, y);
        if (this.m_particleDisplayedPositions.contains(posToLong)) {
            return;
        }
        final FightInfo fight = FightManager.getInstance().getFightById(this.m_fightId);
        if (fight == null) {
            return;
        }
        final short z = fight.getFightMap().getCellHeight(x, y);
        final FreeParticleSystem particle = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(1013002);
        particle.setWorldPosition(x, y, z);
        particle.setFightId(this.m_fightId);
        this.m_particleIds.add(particle.getId());
        this.m_particleDisplayedPositions.add(posToLong);
        IsoParticleSystemManager.getInstance().addParticleSystem(particle);
    }
    
    public void removeParticles() {
        for (final int particleId : this.m_particleIds) {
            IsoParticleSystemManager.getInstance().removeParticleSystem(particleId, true);
        }
        this.m_particleIds.clear();
        this.m_particleDisplayedPositions.clear();
    }
}
