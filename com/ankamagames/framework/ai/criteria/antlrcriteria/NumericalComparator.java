package com.ankamagames.framework.ai.criteria.antlrcriteria;

public abstract class NumericalComparator extends SimpleCriterion
{
    public static void checkType(final ParserObject... args) {
        if (args.length != 2) {
            throw new ParseException("On essaie d'appliquer un op\u00e9rateur num\u00e9rique au mauvais nombre d'arguments ");
        }
        if (args[0] == null || args[1] == null) {
            throw new ParseException("Un des 2 param\u00e8tres a \u00e9t\u00e9 impossible \u00e0 parser");
        }
        for (final ParserObject object : args) {
            if (object.getType() != ParserType.NUMBER && object.getType() != ParserType.POSITION) {
                throw new ParseException("On essaie d'appliquer un op\u00e9rateur num\u00e9rique \u00e0 autre chose que des nombres...");
            }
        }
    }
}
