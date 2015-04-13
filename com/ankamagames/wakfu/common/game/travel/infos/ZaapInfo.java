package com.ankamagames.wakfu.common.game.travel.infos;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.utils.*;

public class ZaapInfo extends TravelInfo
{
    private static final Logger m_logger;
    private final SimpleCriterion m_destinationCriterion;
    private final int m_exitX;
    private final int m_exitY;
    private final int m_exitWorldId;
    private TravelLoadingInfo m_loading;
    
    public ZaapInfo(final long id, final int visualId, final int exitX, final int exitY, final int exitWorldId, final String criterion) {
        super(id, visualId);
        this.m_exitX = exitX;
        this.m_exitY = exitY;
        this.m_exitWorldId = exitWorldId;
        SimpleCriterion destinationCriterion = null;
        try {
            destinationCriterion = CriteriaCompiler.compileBoolean(criterion);
        }
        catch (Exception e) {
            ZaapInfo.m_logger.error((Object)("Impossible de compiler le crit\u00e8re " + criterion + " sur le zaap " + id), (Throwable)e);
        }
        this.m_destinationCriterion = destinationCriterion;
    }
    
    public ZaapInfo(final long id, final int visualId, final int exitX, final int exitY, final int exitWorldId, final String criterion, final int uiGfxId, final TravelType landmarkTravelType) {
        super(id, visualId, uiGfxId, landmarkTravelType);
        this.m_exitX = exitX;
        this.m_exitY = exitY;
        this.m_exitWorldId = exitWorldId;
        SimpleCriterion destinationCriterion = null;
        try {
            destinationCriterion = CriteriaCompiler.compileBoolean(criterion);
        }
        catch (Exception e) {
            ZaapInfo.m_logger.error((Object)("Impossible de compiler le crit\u00e8re " + criterion + " sur le zaap " + id), (Throwable)e);
        }
        this.m_destinationCriterion = destinationCriterion;
    }
    
    public int getExitX() {
        return this.m_exitX;
    }
    
    public int getExitY() {
        return this.m_exitY;
    }
    
    public int getExitWorldId() {
        return this.m_exitWorldId;
    }
    
    public boolean isDestinationValid(final BasicCharacterInfo user) {
        return this.m_destinationCriterion == null || this.m_destinationCriterion.isValid(user, null, this, user.getAppropriateContext());
    }
    
    public void setLoading(final TravelLoadingInfo loading) {
        this.m_loading = loading;
    }
    
    public boolean hasLoading() {
        return this.m_loading != null && !StringUtils.isEmptyOrNull(this.m_loading.getAnimationName());
    }
    
    public TravelLoadingInfo getLoading() {
        return this.m_loading;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ZaapInfo.class);
    }
}
