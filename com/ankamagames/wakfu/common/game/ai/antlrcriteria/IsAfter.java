package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class IsAfter extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private final GameDate m_date;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsAfter.signatures;
    }
    
    public IsAfter(final ArrayList<ParserObject> args) {
        super();
        this.m_date = new GameDate(0, 0, 0, 0, 0, 0);
        this.checkType(args);
        final int d = (int)args.get(0).getLongValue(null, null, null, null);
        final int m = (int)args.get(1).getLongValue(null, null, null, null);
        final int y = (int)args.get(2).getLongValue(null, null, null, null);
        final int h = (int)args.get(3).getLongValue(null, null, null, null);
        final int min = (int)args.get(4).getLongValue(null, null, null, null);
        final int s = (int)args.get(5).getLongValue(null, null, null, null);
        this.m_date.set(s, min, h, d, m, y);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (WakfuGameCalendar.getInstance().isSynchronized() && WakfuGameCalendar.getInstance().getDate().compareTo((GameDateConst)this.m_date) >= 0) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.ISAFTER;
    }
    
    static {
        IsAfter.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER, ParserType.NUMBER, ParserType.NUMBER, ParserType.NUMBER, ParserType.NUMBER, ParserType.NUMBER };
        IsAfter.signatures.add(sig);
    }
}
