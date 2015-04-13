package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;

public class GetDate extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private GameDate m_date;
    
    public GetDate(final ArrayList<ParserObject> args) {
        super();
        if (this.checkType(args) == 1) {
            this.m_date = new GameDate((int)args.get(5).getLongValue(null, null, null, null), (int)args.get(4).getLongValue(null, null, null, null), (int)args.get(3).getLongValue(null, null, null, null), (int)args.get(0).getLongValue(null, null, null, null), (int)args.get(1).getLongValue(null, null, null, null), (int)args.get(2).getLongValue(null, null, null, null));
        }
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetDate.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (this.m_date != null) {
            return this.m_date.toLong();
        }
        return WakfuGameCalendar.getInstance().getDate().toLong();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETDATE;
    }
    
    static {
        GetDate.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        GetDate.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER, ParserType.NUMBER, ParserType.NUMBER, ParserType.NUMBER, ParserType.NUMBER, ParserType.NUMBER };
        GetDate.signatures.add(sig);
    }
}
