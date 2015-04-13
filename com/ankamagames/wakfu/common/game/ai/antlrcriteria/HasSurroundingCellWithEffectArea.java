package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;

public class HasSurroundingCellWithEffectArea extends HasSurroundingCell
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private NumericalValue m_effectAreaTypeId;
    private StringObject m_effectAreaTypeName;
    private boolean m_effectAreaSpecificIdInsteadOfTypeId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasSurroundingCellWithEffectArea.SIGNATURES;
    }
    
    public HasSurroundingCellWithEffectArea(final ArrayList<ParserObject> args) {
        super();
        final byte sigType = this.checkType(args);
        if (sigType == 0) {
            this.m_effectAreaTypeId = args.get(0);
            this.m_effectAreaTypeName = null;
            this.m_neighbourhoodType = NeighbourhoodType.VON_NEUMANN;
            this.m_effectAreaSpecificIdInsteadOfTypeId = false;
        }
        else if (sigType == 1) {
            this.m_effectAreaTypeName = args.get(0);
            this.m_effectAreaTypeId = null;
            this.m_neighbourhoodType = NeighbourhoodType.VON_NEUMANN;
            this.m_effectAreaSpecificIdInsteadOfTypeId = false;
        }
        else if (sigType == 2) {
            this.m_effectAreaTypeId = args.get(0);
            this.m_effectAreaTypeName = null;
            this.extractNeighbourhoodType(args.get(2).getValue());
            this.m_effectAreaSpecificIdInsteadOfTypeId = false;
        }
        else if (sigType == 3) {
            this.m_effectAreaTypeName = args.get(0);
            this.m_effectAreaTypeId = null;
            this.extractNeighbourhoodType(args.get(2).getValue());
            this.m_effectAreaSpecificIdInsteadOfTypeId = false;
        }
        else if (sigType == 4) {
            this.m_effectAreaTypeName = null;
            this.m_effectAreaTypeId = args.get(0);
            this.extractNeighbourhoodType(args.get(2).getValue());
            this.m_effectAreaSpecificIdInsteadOfTypeId = true;
        }
        this.m_target = args.get(1).getValue().equalsIgnoreCase("target");
    }
    
    private EffectAreaType getEffectAreaType() {
        if (this.m_effectAreaTypeId != null) {
            final int value = (int)this.m_effectAreaTypeId.getLongValue(null, null, null, null);
            return EffectAreaType.getTypeFromId(value);
        }
        return EffectAreaType.valueOf(this.m_effectAreaTypeName.getValue().toUpperCase());
    }
    
    @Override
    protected boolean cellIsInNeighbourhood(final BasicCharacterInfo user, final AbstractFight fight, final Point3 cell) {
        boolean hasAreaInNeighbourhood = false;
        int effectAreaSpecificId;
        int effectAreaTypeId;
        if (this.m_effectAreaSpecificIdInsteadOfTypeId) {
            effectAreaSpecificId = (int)this.m_effectAreaTypeId.getLongValue(null, null, null, null);
            effectAreaTypeId = -1;
        }
        else {
            effectAreaSpecificId = -1;
            if (this.m_effectAreaTypeId != null) {
                effectAreaTypeId = (int)this.m_effectAreaTypeId.getLongValue(null, null, null, null);
            }
            else {
                final EffectAreaType effectAreaType = EffectAreaType.valueOf(this.m_effectAreaTypeName.getValue().toUpperCase());
                if (effectAreaType != null) {
                    effectAreaTypeId = effectAreaType.getTypeId();
                }
                else {
                    effectAreaTypeId = -1;
                }
            }
        }
        final Collection<BasicEffectArea> areas = fight.getEffectAreaManager().getActiveEffectAreas();
        for (final BasicEffectArea area : areas) {
            if (!this.isConcernedArea(effectAreaTypeId, effectAreaSpecificId, area, user)) {
                continue;
            }
            switch (this.m_neighbourhoodType) {
                case MOORE: {
                    if (cell.isInMooreNeighborhood(area.getWorldCellX(), area.getWorldCellY())) {
                        hasAreaInNeighbourhood = true;
                        continue;
                    }
                    continue;
                }
                case VON_NEUMANN: {
                    if (cell.isInVonNeumannNeighbourhood(area.getWorldCellX(), area.getWorldCellY())) {
                        hasAreaInNeighbourhood = true;
                        continue;
                    }
                    continue;
                }
            }
        }
        return hasAreaInNeighbourhood;
    }
    
    protected boolean isConcernedArea(final int effectAreaTypeId, final int effectAreaSpecificId, final BasicEffectArea area, final EffectUser user) {
        return area.getType() == effectAreaTypeId || area.getBaseId() == effectAreaSpecificId;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_SURROUNDING_CELL_WITH_EFFECT_AREA;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.NUMBER, ParserType.STRING });
        HasSurroundingCellWithEffectArea.SIGNATURES.add(new ParserType[] { ParserType.STRING, ParserType.STRING });
        HasSurroundingCellWithEffectArea.SIGNATURES.add(new ParserType[] { ParserType.NUMBER, ParserType.STRING, ParserType.STRING });
        HasSurroundingCellWithEffectArea.SIGNATURES.add(new ParserType[] { ParserType.STRING, ParserType.STRING, ParserType.STRING });
        HasSurroundingCellWithEffectArea.SIGNATURES.add(new ParserType[] { ParserType.NUMBER, ParserType.STRING, ParserType.STRING, ParserType.BOOLEAN });
    }
}
