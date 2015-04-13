package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractChangeSpellTargetCellArea extends AbstractEffectArea
{
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    protected Point3 m_firstPoint;
    protected Point3 m_secondPoint;
    private long[] m_target;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AbstractChangeSpellTargetCellArea.PARAMETER_LIST_SET;
    }
    
    protected AbstractChangeSpellTargetCellArea() {
        super();
        this.m_target = new long[1];
    }
    
    protected AbstractChangeSpellTargetCellArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBetargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBetargetted, canBeDestroyed, maxLevel);
        this.m_target = new long[1];
    }
    
    @Override
    public AbstractEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final AbstractChangeSpellTargetCellArea copy = (AbstractChangeSpellTargetCellArea)super.instanceAnother(parameters);
        if (this.m_firstPoint != null) {
            copy.m_firstPoint = new Point3(this.m_firstPoint);
        }
        if (this.m_secondPoint != null) {
            copy.m_secondPoint = new Point3(this.m_secondPoint);
        }
        return copy;
    }
    
    public void setFirstPoint(final Point3 firstPoint) {
        this.m_firstPoint = firstPoint;
    }
    
    public void setSecondPoint(final Point3 secondPoint) {
        this.m_secondPoint = secondPoint;
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public int getType() {
        return EffectAreaType.SPELL_TUNNEL.getTypeId();
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        if (triggerer != null) {
            this.m_target[0] = PositionValue.toLong(this.m_position);
            return this.m_target;
        }
        return null;
    }
    
    @Override
    public List<Target> determineUnapplicationTarget(final Target triggerer) {
        return Collections.emptyList();
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        final SpellCaster spellCaster = this.m_context.getSpellCaster();
        return spellCaster.isSpellTunnelable() && applicant == this.getOwner();
    }
    
    @Override
    public void computeZone() {
        final List<int[]> zone = new ArrayList<int[]>();
        if (this.m_firstPoint == null || this.m_secondPoint == null) {
            return;
        }
        zone.add(this.m_firstPoint.toIntArray());
        zone.add(this.m_secondPoint.toIntArray());
        final Point3 diff = new Point3(this.m_secondPoint);
        diff.sub(this.m_firstPoint);
        (this.m_area = new InvariantPointsAOE()).initialize(new int[] { 0, 0, diff.getX(), diff.getY() });
        this.m_zone = zone;
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        final ByteArray ba = new ByteArray();
        ba.putLong(PositionValue.toLong(this.m_firstPoint.getX(), this.m_firstPoint.getY(), this.m_firstPoint.getZ()));
        ba.putLong(PositionValue.toLong(this.m_secondPoint.getX(), this.m_secondPoint.getY(), this.m_secondPoint.getZ()));
        return ba.toArray();
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
        this.m_firstPoint = PositionValue.fromLong(bb.getLong());
        this.m_secondPoint = PositionValue.fromLong(bb.getLong());
    }
    
    static {
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Tunnel de sort standard", new Parameter[0]) });
    }
}
