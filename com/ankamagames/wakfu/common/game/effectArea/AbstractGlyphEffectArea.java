package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractGlyphEffectArea extends AbstractEffectArea
{
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private final Point3 m_ownerCastPosition;
    private boolean m_mustStayOnCastPosition;
    protected boolean m_glyphOwnerIsCaster;
    private final long[] m_target;
    
    @Override
    public EffectAreaParameterListSet getParametersListSet() {
        return AbstractGlyphEffectArea.PARAMETER_LIST_SET;
    }
    
    protected AbstractGlyphEffectArea() {
        super();
        this.m_ownerCastPosition = new Point3();
        this.m_target = new long[1];
    }
    
    public AbstractGlyphEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_ownerCastPosition = new Point3();
        this.m_target = new long[1];
    }
    
    @Override
    public AbstractGlyphEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final AbstractGlyphEffectArea glyph = (AbstractGlyphEffectArea)super.instanceAnother(parameters);
        if (parameters != null) {
            if (parameters.getOwner() != null) {
                final EffectUser owner = parameters.getOwner();
                glyph.setCasterPosition(owner.getWorldCellX(), owner.getWorldCellY(), owner.getWorldCellAltitude());
            }
            glyph.setDirection(parameters.getDirection());
        }
        return glyph;
    }
    
    @Override
    public void initialize() {
        if (this.m_params == null) {
            this.m_mustStayOnCastPosition = false;
            this.m_glyphOwnerIsCaster = false;
            return;
        }
        this.m_mustStayOnCastPosition = (this.m_params.length > 0 && this.getParams(0) == 1.0f);
        this.m_glyphOwnerIsCaster = (this.m_params.length > 1 && this.getParams(1) == 1.0f);
    }
    
    public void setCasterPosition(final int x, final int y, final short alt) {
        this.m_ownerCastPosition.setX(x);
        this.m_ownerCastPosition.setY(y);
        this.m_ownerCastPosition.setZ(alt);
    }
    
    public boolean checkCasterPosition() {
        return !this.m_mustStayOnCastPosition || (this.m_owner != null && this.m_ownerCastPosition.equals(this.m_owner.getWorldCellX(), this.m_owner.getWorldCellY(), this.m_owner.getWorldCellAltitude()));
    }
    
    @Override
    public int getType() {
        return EffectAreaType.GLYPH.getTypeId();
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
    public ArrayList<Target> determineUnapplicationTarget(final Target triggerer) {
        if (triggerer == null) {
            return null;
        }
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
        final ByteArray ba = new ByteArray();
        if (this.m_mustStayOnCastPosition) {
            ba.put((byte)1);
            ba.putInt(this.m_ownerCastPosition.getX());
            ba.putInt(this.m_ownerCastPosition.getY());
            ba.putShort(this.m_ownerCastPosition.getZ());
        }
        else {
            ba.put((byte)0);
        }
        return ba.toArray();
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
        this.m_mustStayOnCastPosition = (bb.get() == 1);
        if (this.m_mustStayOnCastPosition) {
            this.setCasterPosition(bb.getInt(), bb.getInt(), bb.getShort());
        }
    }
    
    static {
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Glyphe standard", new Parameter[0]), new EffectAreaParameterList("Glyphe param\u00e9tr\u00e9e", new Parameter[] { new Parameter("le casteur doit rester sur sa position de cast (1=oui)") }), new EffectAreaParameterList("Owner is caster", new Parameter[] { new Parameter("le casteur doit rester sur sa position de cast (1=oui)"), new Parameter("le casteur doit etre le caster de la glyphe") }) });
    }
}
