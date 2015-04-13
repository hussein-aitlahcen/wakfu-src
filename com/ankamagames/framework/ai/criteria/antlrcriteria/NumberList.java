package com.ankamagames.framework.ai.criteria.antlrcriteria;

import org.jetbrains.annotations.*;
import gnu.trove.*;

public abstract class NumberList extends ParserObject
{
    public abstract TLongArrayList getValue(@Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3);
    
    @Override
    public ParserType getType() {
        return ParserType.NUMBERLIST;
    }
    
    public abstract int getSize();
}
