package com.ankamagames.wakfu.common.game.nation.crime.data;

import com.ankamagames.framework.external.*;

public enum CitizenRankRule implements ExportableEnum
{
    SPECIAL_NATION_BUILDING_ACCESS(1, "Acc\u00e8de aux Infrastructures sp\u00e9ciales de sa nation"), 
    CAN_SPONSOR_NEUTRAL_STRANGER(2, "Peut parrainer un joueur d'une autre nation (Neutre)"), 
    CAN_STAND_FOR_ELECTIONS(3, "Peut se pr\u00e9senter aux \u00e9lections"), 
    CAN_BECOME_SOLDIER_OR_MILITIAMAN(4, "Peut devenir Milicien ou Soldat"), 
    CAN_VOTE(5, "Peut Voter lors des \u00e9lections"), 
    CAN_RECEIVE_MDC_BONUS(6, "Profite des Bonus de MdC"), 
    IS_NATION_ENEMY(7, "Est consid\u00e9r\u00e9 enemi de la nation (flag + aggro)"), 
    MILITIAMAN_RECEIVE_BONUS_AGAINST(8, "Les Miliciens gagnent des bonus contre ce joueur"), 
    CANT_USE_NATION_ZAAP(9, "Ne peut plus utiliser les zaaps de la nation"), 
    CANT_RENT_SELLING_POINT(10, "DEPRECATED : Ne peut plus louer de points de vente de la nation"), 
    CANT_USE_PERSONNAL_SPACE(11, "Ne peut plus acc\u00e9der \u00e0 son Havre-Sac"), 
    CAN_COMPETE_IN_CHALLENGES(12, "Peut participer aux challenges"), 
    CAN_GAIN_PVP_POINTS(13, "Peut gagner des points de pvp");
    
    private final int m_id;
    private final String m_description;
    
    private CitizenRankRule(final int id, final String description) {
        this.m_id = id;
        this.m_description = description;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_description;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static CitizenRankRule getFromId(final int ruleId) {
        final CitizenRankRule[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final CitizenRankRule rule = values[i];
            if (rule.m_id == ruleId) {
                return rule;
            }
        }
        return null;
    }
}
