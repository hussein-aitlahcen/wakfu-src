package com.ankamagames.wakfu.common.game.travel.infos;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.datas.*;

public class DragoInfo extends TravelInfo
{
    private final int m_exitX;
    private final int m_exitY;
    private TravelLoadingInfo m_loading;
    private SimpleCriterion m_criterion;
    private static final Logger m_logger;
    
    public DragoInfo(final long id, final int visualId, final int exitX, final int exitY, final String criterion) {
        super(id, visualId);
        this.m_exitX = exitX;
        this.m_exitY = exitY;
        this.m_criterion = this.loadCriterion(id, criterion);
    }
    
    public DragoInfo(final long id, final int visualId, final int exitX, final int exitY, final int uiGfxId, final TravelType landmarkTravelType, final String criterion) {
        super(id, visualId, uiGfxId, landmarkTravelType);
        this.m_exitX = exitX;
        this.m_exitY = exitY;
        this.m_criterion = this.loadCriterion(id, criterion);
    }
    
    private SimpleCriterion loadCriterion(final long id, final String criterion) {
        SimpleCriterion simpleCriterion = null;
        try {
            simpleCriterion = CriteriaCompiler.compileBoolean(criterion);
        }
        catch (Exception e) {
            DragoInfo.m_logger.warn((Object)("Unable to load DragoInfo for drago id " + id));
        }
        if (simpleCriterion == null) {
            simpleCriterion = ConstantBooleanCriterion.TRUE;
        }
        return simpleCriterion;
    }
    
    public int getExitX() {
        return this.m_exitX;
    }
    
    public int getExitY() {
        return this.m_exitY;
    }
    
    public void setLoading(final TravelLoadingInfo loading) {
        this.m_loading = loading;
    }
    
    public boolean hasLoading() {
        return this.m_loading != null && !StringUtils.isEmptyOrNull(this.m_loading.getAnimationName());
    }
    
    public boolean isDestinationValid(final BasicCharacterInfo user) {
        return this.m_criterion == null || this.m_criterion.isValid(user, null, this, user.getAppropriateContext());
    }
    
    public TravelLoadingInfo getLoading() {
        return this.m_loading;
    }
    
    @Override
    public String toString() {
        return "DragoInfo{m_exitX=" + this.m_exitX + ", m_exitY=" + this.m_exitY + ", m_loading=" + this.m_loading + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)DragoInfo.class);
    }
}
