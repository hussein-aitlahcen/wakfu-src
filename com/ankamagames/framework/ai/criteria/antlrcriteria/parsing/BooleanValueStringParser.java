package com.ankamagames.framework.ai.criteria.antlrcriteria.parsing;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class BooleanValueStringParser implements StringParser
{
    @Override
    public ParserObject fromString(final String string) {
        return new ConstantBooleanCriterion(Boolean.parseBoolean(string));
    }
}
