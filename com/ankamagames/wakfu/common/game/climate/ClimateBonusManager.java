package com.ankamagames.wakfu.common.game.climate;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.climate.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;

public final class ClimateBonusManager
{
    public static final ClimateBonusManager INSTANCE;
    private static final Logger m_logger;
    private final TIntObjectHashMap<ClimateBonus> m_bonus;
    private final TIntObjectHashMap<ClimateBonusList> m_bonusList;
    
    private ClimateBonusManager() {
        super();
        this.m_bonus = new TIntObjectHashMap<ClimateBonus>();
        this.m_bonusList = new TIntObjectHashMap<ClimateBonusList>();
    }
    
    public void addBonus(final int id, final float temperature, final float wind, final float precipitations, final SimpleCriterion criterion, final int duration, final short price) {
        final ClimateBonus bonus = new ClimateBonus(id, temperature, wind, precipitations, criterion, duration, price);
        this.m_bonus.put(id, bonus);
    }
    
    public ClimateBonus getBonus(final int id) {
        return this.m_bonus.get(id);
    }
    
    public void addBonusList(final int id, final int[] bonusList) {
        final ArrayList<ClimateBonus> bonus = new ArrayList<ClimateBonus>();
        for (int i = 0, size = bonusList.length; i < size; ++i) {
            final ClimateBonus b = this.m_bonus.get(bonusList[i]);
            if (b != null) {
                bonus.add(b);
            }
            else {
                ClimateBonusManager.m_logger.error((Object)("Impossible de trouver le bonus correspondant, ID=" + bonusList[i]));
            }
        }
        final ClimateBonusList list = new ClimateBonusList(id, bonus.toArray(new ClimateBonus[bonus.size()]));
        this.m_bonusList.put(id, list);
    }
    
    public ClimateBonusList getBonusList(final int bonusListId) {
        return this.m_bonusList.get(bonusListId);
    }
    
    static {
        INSTANCE = new ClimateBonusManager();
        m_logger = Logger.getLogger((Class)ClimateBonusManager.class);
    }
}
