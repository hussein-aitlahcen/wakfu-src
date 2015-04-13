package com.ankamagames.framework.ai.criteria.antlrcriteria;

public abstract class NumericalOperator extends NumericalValue
{
    private boolean m_opposite;
    
    public NumericalOperator() {
        super();
        this.m_opposite = false;
    }
    
    @Override
    public void setOpposite() {
        this.m_opposite = !this.m_opposite;
    }
    
    protected boolean isOpposite() {
        return this.m_opposite;
    }
    
    public static void checkType(final ParserObject... args) {
        if (args.length != 2) {
            throw new ParseException("On essaie d'appliquer un op\u00e9rateur num\u00e9rique au mauvais nombre d'arguments ");
        }
        for (final ParserObject object : args) {
            if (object.getType() != ParserType.NUMBER) {
                throw new ParseException("On essaie d'appliquer un op\u00e9rateur num\u00e9rique \u00e0 autre chose que des nombres...");
            }
        }
    }
}
