package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;

public final class HasSurroundingCellWithOwnSummon extends HasSurroundingCell
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private long m_summonId;
    
    public HasSurroundingCellWithOwnSummon(final ArrayList<ParserObject> args) {
        super();
        final byte sigType = this.checkType(args);
        this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
        switch (sigType) {
            case 1: {
                this.m_summonId = args.get(1).getLongValue(null, null, null, null);
                this.m_neighbourhoodType = NeighbourhoodType.VON_NEUMANN;
                break;
            }
            case 2: {
                this.m_summonId = -1L;
                this.extractNeighbourhoodType(args.get(1).getValue());
                break;
            }
            case 3: {
                this.m_summonId = args.get(1).getLongValue(null, null, null, null);
                this.extractNeighbourhoodType(args.get(2).getValue());
                break;
            }
        }
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasSurroundingCellWithOwnSummon.SIGNATURES;
    }
    
    @Override
    protected boolean cellIsInNeighbourhood(final BasicCharacterInfo user, final AbstractFight fight, final Point3 cell) {
        final BasicFight<?> basicFight = (BasicFight<?>)user.getCurrentFight();
        if (basicFight == null) {
            return false;
        }
        final Collection<? extends BasicCharacterInfo> fighters = (Collection<? extends BasicCharacterInfo>)basicFight.getFightersInPlay();
        for (final BasicCharacterInfo info : fighters) {
            if (info == user) {
                continue;
            }
            if (info.getController() != user) {
                continue;
            }
            if (info.getBreedId() != this.m_summonId && this.m_summonId != -1L) {
                continue;
            }
            switch (this.m_neighbourhoodType) {
                case MOORE: {
                    if (cell.isInMooreNeighborhood(info.getWorldCellX(), info.getWorldCellY())) {
                        return true;
                    }
                    continue;
                }
                case VON_NEUMANN: {
                    if (cell.isInVonNeumannNeighbourhood(info.getWorldCellX(), info.getWorldCellY())) {
                        return true;
                    }
                    continue;
                }
            }
        }
        return false;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_SURROUNDING_CELL_WITH_OWN_SUMMON;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_STRING_SIGNATURE);
        HasSurroundingCellWithOwnSummon.SIGNATURES.add(new ParserType[] { ParserType.STRING, ParserType.NUMBER });
        HasSurroundingCellWithOwnSummon.SIGNATURES.add(new ParserType[] { ParserType.STRING, ParserType.STRING });
        HasSurroundingCellWithOwnSummon.SIGNATURES.add(new ParserType[] { ParserType.STRING, ParserType.NUMBER, ParserType.STRING });
    }
}
