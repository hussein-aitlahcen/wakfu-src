package com.ankamagames.framework.external;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class ParserObjectParameter extends Parameter
{
    private ParserType m_type;
    
    public ParserObjectParameter(final String name, final ParserType type) {
        super(name);
        this.m_type = type;
    }
    
    public ParserType getType() {
        return this.m_type;
    }
}
