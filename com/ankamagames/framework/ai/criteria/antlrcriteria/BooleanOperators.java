package com.ankamagames.framework.ai.criteria.antlrcriteria;

public abstract class BooleanOperators extends SimpleCriterion
{
    public static void checkType(final ParserObject... args) {
        if (args.length != 2) {
            throw new ParseException("On essaie d'appliquer un op\u00e9rateur bool\u00e9en au mauvais nombre d'arguments ");
        }
        for (final ParserObject object : args) {
            if (object.getType() != ParserType.BOOLEAN) {
                throw new ParseException("On essaie d'appliquer un op\u00e9rateur bool\u00e9en \u00e0 autre chose que des bool\u00e9ens...");
            }
        }
    }
}
