package com.ankamagames.framework.ai.criteria.antlrcriteria;

import gnu.trove.*;
import java.util.*;

public class ConstantNumberList extends NumberList
{
    private ArrayList<NumericalValue> m_values;
    
    @Override
    public ParserType getType() {
        return ParserType.NUMBERLIST;
    }
    
    @Override
    public Enum getEnum() {
        return CriterionIds.NUMBERLIST;
    }
    
    @Override
    public TLongArrayList getValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final TLongArrayList value = new TLongArrayList();
        for (final NumericalValue member : this.m_values) {
            value.add(member.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext));
        }
        return value;
    }
    
    public ConstantNumberList(final ArrayList<? extends ParserObject> args) {
        this(args, false);
    }
    
    public ConstantNumberList(final ArrayList<? extends ParserObject> args, final boolean ignoreCheck) {
        super();
        if (ignoreCheck) {
            this.m_values = (ArrayList<NumericalValue>)args;
        }
        else {
            this.m_values = new ArrayList<NumericalValue>();
            for (final ParserObject arg : args) {
                if (arg.getType() != ParserType.NUMBER) {
                    throw new ParseException("On essaie d'ajouter " + arg + " de type " + arg.getType().name() + " \u00e0 une liste de valeur num\u00e9rique");
                }
                this.m_values.add((NumericalValue)arg);
            }
        }
    }
    
    @Override
    public int getSize() {
        return this.m_values.size();
    }
}
