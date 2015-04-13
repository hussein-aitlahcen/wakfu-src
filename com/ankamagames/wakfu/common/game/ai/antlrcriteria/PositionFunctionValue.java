package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public abstract class PositionFunctionValue extends PositionValue
{
    protected static Logger m_logger;
    private boolean is_opposite;
    
    protected abstract List<ParserType[]> getSignatures();
    
    @Override
    public boolean isConstant() {
        return false;
    }
    
    @Override
    public void setOpposite() {
        this.is_opposite = !this.is_opposite;
    }
    
    public long getSign(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (this.is_opposite) {
            return -1L;
        }
        return 1L;
    }
    
    public byte checkType(final ArrayList<ParserObject> args) {
        return FunctionUtils.checkType(this.getClass().getName(), this.getSignatures(), args);
    }
    
    static {
        PositionFunctionValue.m_logger = Logger.getLogger((Class)FunctionValue.class);
    }
}
