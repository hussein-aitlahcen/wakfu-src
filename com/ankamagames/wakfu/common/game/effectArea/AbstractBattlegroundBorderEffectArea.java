package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.apache.commons.lang3.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractBattlegroundBorderEffectArea extends AbstractEffectArea
{
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    protected boolean m_blockingMovement;
    protected boolean m_invisible;
    private final long[] m_targets;
    
    @Override
    public EffectAreaParameterListSet getParametersListSet() {
        return AbstractBattlegroundBorderEffectArea.PARAMETER_LIST_SET;
    }
    
    protected AbstractBattlegroundBorderEffectArea() {
        super();
        this.m_blockingMovement = false;
        this.m_invisible = false;
        this.m_targets = new long[1];
    }
    
    public AbstractBattlegroundBorderEffectArea(final int baseId, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, null, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_blockingMovement = false;
        this.m_invisible = false;
        this.m_targets = new long[1];
    }
    
    @Override
    public int getType() {
        return EffectAreaType.BATTLEGROUND_BORDER.getTypeId();
    }
    
    @Override
    public void initialize() {
        if (this.getParamCount() >= 1) {
            this.m_blockingMovement = ((int)this.getParams(0) == 1);
        }
        if (this.getParamCount() >= 2) {
            this.m_invisible = ((int)this.getParams(0) == 1);
        }
    }
    
    @Override
    public boolean isBlockingMovement() {
        return this.m_blockingMovement;
    }
    
    public boolean isInvisible() {
        return this.m_invisible;
    }
    
    public void initialize(final FightMap fightMap) {
        this.m_area = new BattlegroundBorderAreaOfEffect(fightMap);
        if (this.m_blockingMovement) {
            fightMap.setBorderCellBlocked();
        }
    }
    
    @Override
    public void computeZone() {
        if (this.m_area instanceof BattlegroundBorderAreaOfEffect) {
            this.m_zone = ((BattlegroundBorderAreaOfEffect)this.m_area).getCells();
        }
        else {
            super.computeZone();
        }
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        if (triggerer != null) {
            this.m_targets[0] = PositionValue.toLong(triggerer.getWorldCellX(), triggerer.getWorldCellY(), triggerer.getWorldCellAltitude());
            return this.m_targets;
        }
        return null;
    }
    
    @Override
    public ArrayList<Target> determineUnapplicationTarget(final Target triggerer) {
        final ArrayList<Target> targets = new ArrayList<Target>();
        targets.add(triggerer);
        return targets;
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        return true;
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
    }
    
    static {
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Aura de tour de bulle de combat", new Parameter[0]), new EffectAreaParameterList("Bordure bloquante", new Parameter[] { new Parameter("Bloquante (1 = oui, 0 = non (defaut))") }), new EffectAreaParameterList("Bordure invisible", new Parameter[] { new Parameter("Bloquante (1 = oui, 0 = non (defaut))"), new Parameter("Invisible (1 = oui, 0 = non (defaut))") }) });
    }
}
