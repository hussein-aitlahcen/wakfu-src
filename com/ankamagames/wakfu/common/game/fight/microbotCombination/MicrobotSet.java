package com.ankamagames.wakfu.common.game.fight.microbotCombination;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.google.common.collect.*;
import com.google.common.base.*;
import com.ankamagames.wakfu.common.game.fight.microbotCombination.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import gnu.trove.*;

public class MicrobotSet
{
    static final Logger m_logger;
    private final ArrayList<AbstractFakeFighterEffectArea> m_microbotsList;
    private final ArrayList<MicrobotCombination> m_combinations;
    private final HashSet<Point3> m_combinationsCells;
    private final long m_ownerId;
    private final int m_maxSpaceBetweenBots;
    
    MicrobotSet(final long ownerId, final int maxSpaceBetweenBots) {
        super();
        this.m_microbotsList = new ArrayList<AbstractFakeFighterEffectArea>();
        this.m_combinations = new ArrayList<MicrobotCombination>();
        this.m_combinationsCells = new HashSet<Point3>();
        this.m_ownerId = ownerId;
        this.m_maxSpaceBetweenBots = maxSpaceBetweenBots;
    }
    
    public long getOwnerId() {
        return this.m_ownerId;
    }
    
    public boolean isEmpty() {
        return this.m_combinations.isEmpty();
    }
    
    public int getCellsCount() {
        return this.m_combinationsCells.size();
    }
    
    public boolean contains(final Point3 cellPos) {
        return this.m_combinationsCells.contains(cellPos);
    }
    
    public boolean contains(final int x, final int y) {
        for (final Point3 p : this.m_combinationsCells) {
            if (p.equals(x, y)) {
                return true;
            }
        }
        return false;
    }
    
    public List<MicrobotCombination> getCombinations() {
        return Collections.unmodifiableList((List<? extends MicrobotCombination>)this.m_combinations);
    }
    
    boolean add(final AbstractFakeFighterEffectArea microbot, @Nullable final FightMap fightMap, final MicrobotCombinationEventListener listener) {
        if (this.m_microbotsList.contains(microbot)) {
            MicrobotSet.m_logger.error((Object)("Trying to register a microbot already registered : " + microbot), (Throwable)new RuntimeException());
            return false;
        }
        this.m_microbotsList.add(microbot);
        this.updateCombinations(fightMap, listener);
        return true;
    }
    
    boolean remove(final AbstractFakeFighterEffectArea microbot, @Nullable final FightMap fightMap, final MicrobotCombinationEventListener listener) {
        if (!this.m_microbotsList.remove(microbot)) {
            return false;
        }
        this.updateCombinations(fightMap, listener);
        return true;
    }
    
    protected void updateCombinations(@Nullable final FightMap fightMap, final MicrobotCombinationEventListener listener) {
        final ArrayList<MicrobotCombination> newCombinations = this.computeAllCombinations(fightMap);
        ArrayList<MicrobotCombination> combinationsAdded;
        ArrayList<MicrobotCombination> combinationsRemoved;
        ArrayList<Point3> cellsBefore;
        if (listener == null) {
            combinationsAdded = null;
            combinationsRemoved = null;
            cellsBefore = null;
        }
        else {
            combinationsAdded = new ArrayList<MicrobotCombination>();
            for (final MicrobotCombination combination : newCombinations) {
                if (!this.m_combinations.remove(combination)) {
                    combinationsAdded.add(combination);
                }
            }
            combinationsRemoved = new ArrayList<MicrobotCombination>(this.m_combinations);
            cellsBefore = new ArrayList<Point3>(this.m_combinationsCells);
        }
        this.m_combinations.clear();
        this.m_combinations.addAll(newCombinations);
        this.m_combinationsCells.clear();
        for (final MicrobotCombination combi : this.m_combinations) {
            for (Point3 p : combi) {
                if (!this.m_combinationsCells.contains(p)) {
                    if (fightMap != null) {
                        p = new Point3(p.getX(), p.getY(), fightMap.getCellHeight(p.getX(), p.getY()));
                    }
                    this.m_combinationsCells.add(p);
                }
            }
        }
        if (listener != null && (!combinationsAdded.isEmpty() || !combinationsRemoved.isEmpty())) {
            final ArrayList<Point3> cellsNow = new ArrayList<Point3>(this.m_combinationsCells);
            final List<Point3> cellsAdded = (List<Point3>)FluentIterable.from((Iterable)cellsNow).filter(Predicates.not(Predicates.in((Collection)cellsBefore))).toList();
            final List<Point3> cellsRemoved = (List<Point3>)FluentIterable.from((Iterable)cellsBefore).filter(Predicates.not(Predicates.in((Collection)cellsNow))).toList();
            listener.onCombinationsModification(this, combinationsAdded, combinationsRemoved, cellsAdded, cellsRemoved);
        }
    }
    
    protected ArrayList<MicrobotCombination> computeAllCombinations(@Nullable final FightMap fightMap) {
        final TIntObjectHashMap<AlignedMicrobotList> alignedBotsOnX = new TIntObjectHashMap<AlignedMicrobotList>();
        final TIntObjectHashMap<AlignedMicrobotList> alignedBotsOnY = new TIntObjectHashMap<AlignedMicrobotList>();
        for (final AbstractFakeFighterEffectArea bot : this.m_microbotsList) {
            final Point3 position = bot.getPosition();
            final int x = position.getX();
            if (!alignedBotsOnX.containsKey(x)) {
                alignedBotsOnX.put(x, new AlignedMicrobotList());
            }
            alignedBotsOnX.get(x).add(bot);
            final int y = position.getY();
            if (!alignedBotsOnY.containsKey(y)) {
                alignedBotsOnY.put(y, new AlignedMicrobotList());
            }
            alignedBotsOnY.get(y).add(bot);
        }
        PathFinder pathFinder;
        if (fightMap != null) {
            pathFinder = PathFinder.checkOut();
            pathFinder.setMoverCaracteristics(6, (byte)1, (short)4);
            pathFinder.setTopologyMapInstanceSet(fightMap);
            fightMap.setIgnoreAllMovementObstacles(true);
        }
        else {
            pathFinder = null;
        }
        final ArrayList<MicrobotCombination> newCombinations = new ArrayList<MicrobotCombination>();
        try {
            TIntObjectIterator<AlignedMicrobotList> iterator = alignedBotsOnX.iterator();
            while (iterator.hasNext()) {
                iterator.advance();
                newCombinations.addAll(getCombinationsForAlignedBots(iterator.value(), this.m_maxSpaceBetweenBots, pathFinder));
            }
            iterator = alignedBotsOnY.iterator();
            while (iterator.hasNext()) {
                iterator.advance();
                newCombinations.addAll(getCombinationsForAlignedBots(iterator.value(), this.m_maxSpaceBetweenBots, pathFinder));
            }
        }
        finally {
            if (fightMap != null) {
                fightMap.setIgnoreAllMovementObstacles(false);
                pathFinder.release();
            }
        }
        return newCombinations;
    }
    
    protected static ArrayList<MicrobotCombination> getCombinationsForAlignedBots(final AlignedMicrobotList bots, final int maxSpaceBetweenBots, @Nullable final PathFinder pathFinder) {
        AbstractFakeFighterEffectArea previousBot = null;
        AbstractFakeFighterEffectArea currentBot = null;
        MicrobotCombination currentCombination = null;
        final ArrayList<MicrobotCombination> newCombinations = new ArrayList<MicrobotCombination>();
        final Iterator<AbstractFakeFighterEffectArea> botsIterator = bots.iterator();
        previousBot = null;
        while (botsIterator.hasNext()) {
            currentBot = botsIterator.next();
            if (previousBot != null) {
                if (!MicrobotCombination.canPointsCombine(currentBot.getPosition(), previousBot.getPosition(), maxSpaceBetweenBots)) {
                    currentCombination = null;
                }
                else if (pathFinder != null && !pathFinder.isStraightMovePossible(previousBot.getPosition(), currentBot.getPosition())) {
                    currentCombination = null;
                }
                else if (currentCombination == null) {
                    currentCombination = new MicrobotCombination(previousBot, currentBot);
                    newCombinations.add(currentCombination);
                }
                else if (currentCombination.canCombineWith(currentBot.getPosition(), maxSpaceBetweenBots)) {
                    final boolean hasBeenExpanded = currentCombination.expandWith(currentBot, maxSpaceBetweenBots);
                    if (!hasBeenExpanded) {
                        currentCombination = null;
                    }
                }
                else {
                    currentCombination = new MicrobotCombination(previousBot, currentBot);
                    newCombinations.add(currentCombination);
                }
            }
            previousBot = currentBot;
        }
        return newCombinations;
    }
    
    public static boolean isPositionInMicrobotSets(final Point3 position, final List<MicrobotSet> sets) {
        for (final MicrobotSet set : sets) {
            if (set.contains(position.getX(), position.getY())) {
                return true;
            }
        }
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MicrobotSet.class);
    }
}
