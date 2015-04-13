package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;

public class IsSeason extends FunctionCriterion
{
    private final Season m_season;
    private static final ArrayList<ParserType[]> m_signatures;
    
    public IsSeason(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        final String string = args.get(0).getValue();
        this.m_season = Season.valueOf(string);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsSeason.m_signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (WakfuGameCalendar.getInstance().isSynchronized() && WakfuGameCalendar.getInstance().getSeason() == this.m_season) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IsSeason;
    }
    
    static {
        m_signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.STRING };
        IsSeason.m_signatures.add(sig);
    }
}
