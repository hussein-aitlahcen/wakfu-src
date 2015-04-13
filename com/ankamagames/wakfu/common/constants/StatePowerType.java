package com.ankamagames.wakfu.common.constants;

import com.ankamagames.framework.external.*;

public enum StatePowerType implements ExportableEnum
{
    SPECIAL(0, "Sp\u00e9cial", "Etat sp\u00e9cial, utilis\u00e9 pour le Gameplay. Son application ne peut \u00eatre ni boost\u00e9e, ni r\u00e9duite", new Parameters().withNonModifiableProbability()), 
    NORMAL(1, "Normal", "Etat classique, infligeant principalement des d\u00e9g\u00e2ts", new Parameters().withApplicationProbabilityFactors(1.0f, 1.0f)), 
    HALF_GAME_BREAKER(2, "Semi Game Breaker", "Etat bloquant partiellement le joueur", new Parameters().withApplicationProbabilityFactors(0.75f, 1.0f)), 
    GAME_BREAKER(3, "Game Breaker", "Etat bloquant totalement le joueur", new Parameters().withApplicationProbabilityFactors(0.25f, 1.0f));
    
    private final int m_id;
    private final String m_label;
    private final String m_comment;
    private final Parameters m_parameters;
    
    private StatePowerType(final int id, final String label, final String comment, final Parameters parameters) {
        this.m_id = id;
        this.m_label = label;
        this.m_comment = comment;
        this.m_parameters = parameters;
    }
    
    public static StatePowerType getFromId(final int statePopwerTyeId) {
        for (final StatePowerType type : values()) {
            if (type.m_id == statePopwerTyeId) {
                return type;
            }
        }
        return StatePowerType.SPECIAL;
    }
    
    public float getApplicationProbabilityBonusFactor() {
        return this.m_parameters.m_applicationBonusFactor;
    }
    
    public float getApplicationProbabilityResistanceFactor() {
        return this.m_parameters.m_applicationResistanceFactor;
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return this.m_comment;
    }
    
    private static class Parameters
    {
        float m_applicationBonusFactor;
        float m_applicationResistanceFactor;
        
        private Parameters() {
            super();
            this.m_applicationBonusFactor = 1.0f;
            this.m_applicationResistanceFactor = 1.0f;
        }
        
        private Parameters withApplicationProbabilityFactors(final float bonusFactor, final float resistanceFactor) {
            this.m_applicationBonusFactor = bonusFactor;
            this.m_applicationResistanceFactor = resistanceFactor;
            return this;
        }
        
        private Parameters withNonModifiableProbability() {
            this.m_applicationBonusFactor = 0.0f;
            this.m_applicationResistanceFactor = 0.0f;
            return this;
        }
    }
}
