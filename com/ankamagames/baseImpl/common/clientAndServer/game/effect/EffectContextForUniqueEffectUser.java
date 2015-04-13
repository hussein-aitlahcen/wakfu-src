package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import com.ankamagames.framework.ai.dataProvider.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.*;

public abstract class EffectContextForUniqueEffectUser<FX extends Effect> implements EffectContext<FX>, EffectUserInformationProvider, TargetInformationProvider<EffectUser>
{
    private EffectExecutionListener m_listener;
    protected EffectUser m_effectUser;
    private AbstractEffectManager<FX> m_effectManager;
    private List<EffectUser> m_effectUserList;
    private long m_nextSummoningId;
    
    public EffectContextForUniqueEffectUser(final EffectUser effectUser) {
        super();
        this.m_effectUserList = new ArrayList<EffectUser>(1);
        this.m_nextSummoningId = -1L;
        this.m_effectUser = effectUser;
        this.m_effectUserList.add(effectUser);
    }
    
    @Override
    public EffectUserInformationProvider getEffectUserInformationProvider() {
        return this;
    }
    
    @Override
    public TargetInformationProvider<EffectUser> getTargetInformationProvider() {
        return this;
    }
    
    @Override
    public AbstractEffectManager<FX> getEffectManager() {
        return this.m_effectManager;
    }
    
    public void setEffectManager(final AbstractEffectManager<FX> effectManager) {
        this.m_effectManager = effectManager;
    }
    
    public void setListener(final EffectExecutionListener listener) {
        this.m_listener = listener;
    }
    
    @Override
    public EffectExecutionListener getEffectExecutionListener() {
        return this.m_listener;
    }
    
    @Override
    public TurnBasedTimeline getTimeline() {
        return null;
    }
    
    @Override
    public BasicEffectAreaManager getEffectAreaManager() {
        return null;
    }
    
    @Override
    public EffectUser getEffectUserFromId(final long effectId) {
        if (effectId == this.m_effectUser.getId()) {
            return this.m_effectUser;
        }
        return null;
    }
    
    @Override
    public long getNextFreeEffectUserId(final byte type) {
        return this.m_nextSummoningId++;
    }
    
    @Override
    public Iterator<EffectUser> getAllPossibleTargets() {
        return this.m_effectUserList.iterator();
    }
    
    @Override
    public List<EffectUser> getPossibleTargetsAtPosition(final Point3 pos) {
        return this.getPossibleTargetsAtPosition(pos.getX(), pos.getY(), pos.getZ());
    }
    
    @Override
    public List<EffectUser> getPossibleTargetsAtPosition(final int x, final int y, final int z) {
        final ArrayList<EffectUser> list = new ArrayList<EffectUser>();
        final Iterator<EffectUser> it = this.getAllPossibleTargets();
        while (it.hasNext()) {
            final EffectUser eu = it.next();
            if (DistanceUtils.getIntersectionDistance(eu, x, y) == 0) {
                list.add(eu);
            }
        }
        return list;
    }
    
    public byte getType() {
        return 0;
    }
}
