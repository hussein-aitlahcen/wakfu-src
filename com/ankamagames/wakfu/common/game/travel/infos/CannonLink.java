package com.ankamagames.wakfu.common.game.travel.infos;

import com.ankamagames.baseImpl.common.clientAndServer.game.loot.*;
import org.apache.log4j.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.framework.kernel.utils.*;

public class CannonLink implements Dropable
{
    private static final Logger m_logger;
    private final int m_id;
    private final SimpleCriterion m_criterion;
    private final short m_dropWeight;
    private final int m_exitX;
    private final int m_exitY;
    private final int m_exitWorldId;
    private TravelLoadingInfo m_loading;
    
    public CannonLink(final int id, final short dropWeight, final String criteria, final int exitX, final int exitY, final int exitWorldId) {
        super();
        this.m_loading = null;
        this.m_id = id;
        SimpleCriterion criter = null;
        try {
            criter = CriteriaCompiler.compileBoolean(criteria);
        }
        catch (Exception e) {
            CannonLink.m_logger.error((Object)("Impossible de compiler le crit\u00e8re " + criteria + " sur le cannonlink " + id), (Throwable)e);
        }
        this.m_criterion = criter;
        this.m_dropWeight = dropWeight;
        this.m_exitX = exitX;
        this.m_exitY = exitY;
        this.m_exitWorldId = exitWorldId;
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
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
    
    @Override
    public short getDropWeight() {
        return this.m_dropWeight;
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
        m_logger = Logger.getLogger((Class)CannonLink.class);
    }
}
