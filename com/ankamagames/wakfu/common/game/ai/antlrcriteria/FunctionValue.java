package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public abstract class FunctionValue extends NumericalValue
{
    protected static final Logger m_logger;
    private boolean m_opposite;
    
    protected abstract List<ParserType[]> getSignatures();
    
    @Override
    public boolean isConstant() {
        return false;
    }
    
    @Override
    public void setOpposite() {
        this.m_opposite = !this.m_opposite;
    }
    
    public long getSign() {
        return this.m_opposite ? -1L : 1L;
    }
    
    public byte checkType(final List<ParserObject> args) throws ParseException {
        return FunctionUtils.checkType(this.getClass().getName(), this.getSignatures(), args);
    }
    
    static {
        m_logger = Logger.getLogger((Class)FunctionValue.class);
    }
}
