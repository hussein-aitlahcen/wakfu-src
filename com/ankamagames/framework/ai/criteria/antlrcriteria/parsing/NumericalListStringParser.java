package com.ankamagames.framework.ai.criteria.antlrcriteria.parsing;

import java.util.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class NumericalListStringParser implements StringParser
{
    @Override
    public ParserObject fromString(final String string) {
        final ArrayList<ParserObject> numberList = new ArrayList<ParserObject>();
        final String s = string.substring(1, string.length() - 1);
        final String[] arr$;
        final String[] listParamsAsString = arr$ = StringUtils.split(s, ',');
        for (final String str : arr$) {
            numberList.add(new ConstantIntegerValue(Integer.parseInt(str)));
        }
        return new ConstantNumberList(numberList);
    }
}
