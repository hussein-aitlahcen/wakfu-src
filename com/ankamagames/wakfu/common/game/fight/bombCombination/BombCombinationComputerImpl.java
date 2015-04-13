package com.ankamagames.wakfu.common.game.fight.bombCombination;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import java.util.*;

public final class BombCombinationComputerImpl implements BombCombinationComputer, BombPositionChangedListener
{
    private static final Logger m_logger;
    public static final int COMBINATION_DISTANCE_MAX = 2;
    private final List<List<AbstractBombEffectArea>> m_bombCombinations;
    private BombCombinationModificationListener m_listener;
    private boolean m_notifyUpdate;
    
    public BombCombinationComputerImpl() {
        super();
        this.m_bombCombinations = new ArrayList<List<AbstractBombEffectArea>>();
        this.m_notifyUpdate = false;
    }
    
    @Override
    public void computeSpecialZoneAndExecuteEffectsIfNecessary(final AbstractBombEffectArea bomb) {
        this.addBombToCombination(bomb);
    }
    
    void addBombToCombination(final AbstractBombEffectArea bomb) {
        if (bomb == null) {
            return;
        }
        if (this.m_bombCombinations.isEmpty()) {
            this.addNewCombination(bomb);
            return;
        }
        if (this.contains(bomb)) {
            return;
        }
        final List<AbstractBombEffectArea> combination = this.addBombToExistingCombination(bomb);
        if (combination == null) {
            this.addNewCombination(bomb);
        }
        else {
            this.recomputeCombinations(combination);
        }
    }
    
    private void recomputeCombinations(final List<AbstractBombEffectArea> combination) {
        final Iterator<List<AbstractBombEffectArea>> it = this.m_bombCombinations.iterator();
        while (it.hasNext()) {
            final List<AbstractBombEffectArea> combinationToTest = it.next();
            if (combinationToTest == combination) {
                continue;
            }
            if (!this.fusionCombinationsIfNeeded(combination, combinationToTest)) {
                continue;
            }
            it.remove();
        }
    }
    
    private boolean fusionCombinationsIfNeeded(final List<AbstractBombEffectArea> firstCombination, final List<AbstractBombEffectArea> secondCombination) {
        for (final AbstractBombEffectArea bombToTest : secondCombination) {
            for (final AbstractBombEffectArea bomb : firstCombination) {
                if (this.isCombining(bomb, bombToTest)) {
                    firstCombination.addAll(secondCombination);
                    this.setForNotification(firstCombination);
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean contains(final AbstractBombEffectArea bomb) {
        for (final List<AbstractBombEffectArea> bombCombination : this.m_bombCombinations) {
            if (bombCombination.contains(bomb)) {
                return true;
            }
        }
        return false;
    }
    
    private List<AbstractBombEffectArea> addBombToExistingCombination(final AbstractBombEffectArea bomb) {
        for (final List<AbstractBombEffectArea> bombCombination : this.m_bombCombinations) {
            for (final AbstractBombEffectArea bombInCombination : bombCombination) {
                if (this.isCombining(bomb, bombInCombination)) {
                    bombCombination.add(bomb);
                    this.setForNotification(bombCombination);
                    return bombCombination;
                }
            }
        }
        return null;
    }
    
    boolean isCombining(final AbstractBombEffectArea bomb, final AbstractBombEffectArea bombInCombination) {
        return bomb.getOwner() == bombInCombination.getOwner() && (bomb.getWorldCellX() == bombInCombination.getWorldCellX() || bomb.getWorldCellY() == bombInCombination.getWorldCellY()) && bomb.getPosition().getDistance(bombInCombination.getPosition()) <= 2;
    }
    
    private void addNewCombination(final AbstractBombEffectArea bomb) {
        final List<AbstractBombEffectArea> newCombination = new ArrayList<AbstractBombEffectArea>();
        newCombination.add(bomb);
        this.m_bombCombinations.add(newCombination);
    }
    
    @Override
    public void onBombPositionChanged(final AbstractBombEffectArea bomb) {
        this.removeBombFromCombination(bomb);
        this.addBombToCombination(bomb);
        this.notifyListener();
    }
    
    void removeBombFromCombination(final AbstractBombEffectArea bomb) {
        final Iterator<List<AbstractBombEffectArea>> it = this.m_bombCombinations.iterator();
        List<AbstractBombEffectArea> modifiedCombination = null;
        while (it.hasNext()) {
            final List<AbstractBombEffectArea> bombCombination = it.next();
            if (!bombCombination.contains(bomb)) {
                continue;
            }
            modifiedCombination = bombCombination;
            this.setForNotification(bombCombination);
            bombCombination.remove(bomb);
            it.remove();
        }
        if (modifiedCombination == null) {
            return;
        }
        for (final AbstractBombEffectArea bombEffectArea : modifiedCombination) {
            this.addBombToCombination(bombEffectArea);
        }
        this.notifyListener();
    }
    
    private void setForNotification(final List<AbstractBombEffectArea> bombCombination) {
        if (bombCombination.size() >= 3) {
            this.m_notifyUpdate = true;
        }
    }
    
    @Override
    public List<List<AbstractBombEffectArea>> getBombCombinations() {
        return this.m_bombCombinations;
    }
    
    @Override
    public void addBombToCombinationAndNotifyListener(final AbstractBombEffectArea bomb) {
        this.addBombToCombination(bomb);
        this.notifyListener();
    }
    
    @Override
    public void removeBombFromCombinationAndNotifyListener(final AbstractBombEffectArea bomb) {
        this.removeBombFromCombination(bomb);
        this.notifyListener();
    }
    
    @Override
    public void setListener(final BombCombinationModificationListener listener) {
        this.m_listener = listener;
    }
    
    private void notifyListener() {
        try {
            if (this.m_listener != null && this.m_notifyUpdate) {
                this.m_listener.onCombinationUpdated(this);
            }
        }
        catch (Exception e) {
            BombCombinationComputerImpl.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        finally {
            this.m_notifyUpdate = false;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)BombCombinationComputerImpl.class);
    }
}
