package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public abstract class FunctionCriterion extends SimpleCriterion
{
    protected abstract List<ParserType[]> getSignatures();
    
    @Override
    public boolean isConstant() {
        return false;
    }
    
    public byte checkType(final ArrayList<ParserObject> args) {
        return FunctionUtils.checkType(this.getClass().getName(), this.getSignatures(), args);
    }
}
