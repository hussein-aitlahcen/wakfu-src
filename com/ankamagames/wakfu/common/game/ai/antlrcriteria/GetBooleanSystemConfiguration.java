package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.configuration.*;

public class GetBooleanSystemConfiguration extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private String m_key;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetBooleanSystemConfiguration.signatures;
    }
    
    public GetBooleanSystemConfiguration(final ArrayList<ParserObject> args) {
        super();
        this.m_key = null;
        this.checkType(args);
        this.m_key = args.get(0).getValue();
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final SystemConfigurationType type = SystemConfigurationType.getByKey(this.m_key);
        if (type == null) {
            return -1;
        }
        return SystemConfiguration.INSTANCE.getBooleanValue(type) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_BOOLEAN_SYSTEM_CONFIGURATION;
    }
    
    static {
        GetBooleanSystemConfiguration.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.STRING };
        GetBooleanSystemConfiguration.signatures.add(sig);
    }
}
