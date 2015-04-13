package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.security.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractWarpEffectArea extends AbstractEffectArea
{
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    protected Point3 m_firstPoint;
    protected Point3 m_secondPoint;
    protected boolean m_doNotInit;
    private final long[] m_target;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AbstractWarpEffectArea.PARAMETER_LIST_SET;
    }
    
    protected AbstractWarpEffectArea() {
        super();
        this.m_target = new long[1];
    }
    
    protected AbstractWarpEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBetargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBetargetted, canBeDestroyed, maxLevel);
        this.m_target = new long[1];
    }
    
    @Override
    public AbstractEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final AbstractWarpEffectArea copy = (AbstractWarpEffectArea)super.instanceAnother(parameters);
        if (this.m_firstPoint != null) {
            copy.m_firstPoint = new Point3(this.m_firstPoint);
        }
        if (this.m_secondPoint != null) {
            copy.m_secondPoint = new Point3(this.m_secondPoint);
        }
        return copy;
    }
    
    @Override
    public void initialize() {
        if (this.m_doNotInit) {
            return;
        }
        Iterable<int[]> pattern;
        if (this.m_owner != null) {
            pattern = this.getArea().getCells(this.m_position.getX(), this.m_position.getY(), this.m_position.getZ(), this.m_owner.getWorldCellX(), this.m_owner.getWorldCellY(), this.m_owner.getWorldCellAltitude(), this.m_owner.getDirection());
        }
        else {
            pattern = this.getArea().getCells(this.m_position.getX(), this.m_position.getY(), this.m_position.getZ(), this.m_position.getX(), this.m_position.getY(), this.m_position.getZ(), this.getDirection());
        }
        final Iterator<int[]> it = pattern.iterator();
        if (!it.hasNext()) {
            throw new InvalidParameterException("Un warp doit avoir une zone d'effet de deux poins uniquement");
        }
        this.m_firstPoint = new Point3(it.next());
        if (!it.hasNext()) {
            throw new InvalidParameterException("Un warp doit avoir une zone d'effet de deux poins uniquement");
        }
        this.m_secondPoint = new Point3(it.next());
        if (it.hasNext()) {
            throw new InvalidParameterException("Un warp doit avoir une zone d'effet de deux poins uniquement");
        }
    }
    
    @Override
    public int getType() {
        return EffectAreaType.WARP.getTypeId();
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        if (triggerer != null) {
            this.m_target[0] = PositionValue.toLong(triggerer.getWorldCellX(), triggerer.getWorldCellY(), triggerer.getWorldCellAltitude());
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
        return true;
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
        this.m_doNotInit = true;
    }
    
    public Iterable<int[]> getPattern() {
        final Collection<int[]> pattern = new ArrayList<int[]>();
        pattern.add(this.m_firstPoint.toIntArray());
        pattern.add(this.m_secondPoint.toIntArray());
        return pattern;
    }
    
    static {
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Warp standard", new Parameter[0]) });
    }
}
