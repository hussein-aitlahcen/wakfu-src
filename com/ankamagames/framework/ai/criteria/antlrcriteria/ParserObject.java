package com.ankamagames.framework.ai.criteria.antlrcriteria;

import org.apache.commons.lang3.*;

public abstract class ParserObject
{
    private boolean m_displayable;
    private String m_invalidCriterionTradKey;
    
    public ParserObject() {
        super();
        this.m_displayable = true;
    }
    
    public boolean isDisplayable() {
        return this.m_displayable;
    }
    
    public void setDisplayable(final boolean displayable) {
        this.m_displayable = displayable;
    }
    
    public String getInvalidCriterionTradKey() {
        return this.m_invalidCriterionTradKey;
    }
    
    public void setInvalidCriterionTradKey(final String invalidCriterionTradKey) {
        this.m_invalidCriterionTradKey = StringUtils.substring(invalidCriterionTradKey, 1, invalidCriterionTradKey.length() - 1);
    }
    
    public abstract ParserType getType();
    
    public abstract Enum getEnum();
}
