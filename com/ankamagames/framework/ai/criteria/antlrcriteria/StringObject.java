package com.ankamagames.framework.ai.criteria.antlrcriteria;

public class StringObject extends ParserObject
{
    private String m_value;
    
    @Override
    public ParserType getType() {
        return ParserType.STRING;
    }
    
    public StringObject(final String value) {
        super();
        this.m_value = value.replace('\"', ' ');
        this.m_value = this.m_value.trim().intern();
    }
    
    public String getValue() {
        return this.m_value;
    }
    
    @Override
    public Enum getEnum() {
        return CriterionIds.STRING;
    }
}
