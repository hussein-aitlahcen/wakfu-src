package com.ankamagames.framework.ai.criteria.antlrcriteria;

import gnu.trove.*;
import java.util.*;

public class ConcatenateList extends NumberList
{
    private NumberList m_left;
    private NumberList m_right;
    
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
        final TLongArrayList value = this.m_left.getValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        value.add(this.m_right.getValue(criterionUser, criterionTarget, criterionContent, criterionContext).toNativeArray());
        return value;
    }
    
    public ConcatenateList(final ParserObject left, final ParserObject right) {
        super();
        if (left.getType() == ParserType.NUMBERLIST) {
            this.m_left = (NumberList)left;
        }
        else {
            if (left.getType() != ParserType.NUMBER) {
                throw new ParseException("On essaie de concatener deux objets qui ne sont pas des listes");
            }
            final ArrayList<NumericalValue> test = new ArrayList<NumericalValue>();
            test.add((NumericalValue)left);
            this.m_left = new ConstantNumberList(test);
        }
        if (right.getType() == ParserType.NUMBERLIST) {
            this.m_right = (NumberList)right;
        }
        else {
            if (right.getType() != ParserType.NUMBER) {
                throw new ParseException("On essaie de concatener deux objets qui ne sont pas des listes");
            }
            final ArrayList<NumericalValue> test = new ArrayList<NumericalValue>();
            test.add((NumericalValue)right);
            this.m_right = new ConstantNumberList(test);
        }
    }
    
    @Override
    public int getSize() {
        final int size = this.m_left.getSize();
        if (size == Integer.MIN_VALUE) {
            return size;
        }
        final int rightSize = this.m_right.getSize();
        if (rightSize == Integer.MIN_VALUE) {
            return rightSize;
        }
        return size + rightSize;
    }
}
