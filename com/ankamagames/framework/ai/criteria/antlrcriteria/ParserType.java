package com.ankamagames.framework.ai.criteria.antlrcriteria;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.parsing.*;

public enum ParserType
{
    STRING((StringParser)new StringValueStringParser()), 
    NUMBER((StringParser)new NumericalValueStringParser()), 
    BOOLEAN((StringParser)new BooleanValueStringParser()), 
    NUMBERLIST((StringParser)new NumericalListStringParser()), 
    STRINGLIST((StringParser)null), 
    POSITION((StringParser)null), 
    POSITIONLIST((StringParser)null), 
    PLAYERRANKING((StringParser)null);
    
    private static final Logger m_logger;
    public static final ParserType[] EMPTY_ARRAY;
    private final StringParser m_stringParser;
    
    private ParserType(final StringParser stringParser) {
        this.m_stringParser = stringParser;
    }
    
    public StringParser getStringParser() {
        if (this.m_stringParser == null) {
            ParserType.m_logger.error((Object)("Acc\u00e8s \u00e0 un parser non impl\u00e9ment\u00e9 dans ParserType." + this.name()));
        }
        return this.m_stringParser;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ParserType.class);
        EMPTY_ARRAY = new ParserType[0];
    }
}
