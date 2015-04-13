package com.ankamagames.wakfu.common.game.travel.infos;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.kernel.utils.*;

public class BoatLink
{
    private static final Logger m_logger;
    private final long m_id;
    private final int m_startBoatId;
    private final int m_endBoatId;
    private final int m_cost;
    private final SimpleCriterion m_criterion;
    private final SimpleCriterion m_criterionDisplay;
    private final boolean m_needsToPayEverytime;
    private TravelLoadingInfo m_loading;
    
    public BoatLink(final long id, final int startBoatId, final int endBoatId, final int cost, final String criteria, final String criteriaDisplay, final boolean needsToPayEverytime) {
        super();
        this.m_id = id;
        this.m_startBoatId = startBoatId;
        this.m_endBoatId = endBoatId;
        this.m_cost = cost;
        this.m_needsToPayEverytime = needsToPayEverytime;
        SimpleCriterion criterion = null;
        try {
            criterion = CriteriaCompiler.compileBoolean(criteria);
        }
        catch (Exception e) {
            BoatLink.m_logger.error((Object)("[LD] Erreur au chargement du crit\u00e8re " + criteria + " du BoatLink " + id), (Throwable)e);
        }
        this.m_criterion = criterion;
        SimpleCriterion criterionDisplay = null;
        try {
            criterionDisplay = CriteriaCompiler.compileBoolean(criteriaDisplay);
        }
        catch (Exception e2) {
            BoatLink.m_logger.error((Object)("[LD] Erreur au chargement du crit\u00e8re " + criteriaDisplay + " du BoatLink " + id), (Throwable)e2);
        }
        this.m_criterionDisplay = criterionDisplay;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public int getStartBoatId() {
        return this.m_startBoatId;
    }
    
    public int getEndBoatId() {
        return this.m_endBoatId;
    }
    
    public int getCost() {
        return this.m_cost;
    }
    
    public boolean isNeedsToPayEverytime() {
        return this.m_needsToPayEverytime;
    }
    
    public SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
    
    public boolean isCriterionValid(final BasicCharacterInfo user, final MapInteractiveElement machine) {
        return this.m_criterion == null || this.m_criterion.isValid(user, machine, this, user.getAppropriateContext());
    }
    
    public boolean isCriterionDisplayValid(final BasicCharacterInfo user, final MapInteractiveElement machine) {
        return this.m_criterionDisplay == null || this.m_criterionDisplay.isValid(user, machine, this, user.getAppropriateContext());
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
        m_logger = Logger.getLogger((Class)BoatLink.class);
    }
}
