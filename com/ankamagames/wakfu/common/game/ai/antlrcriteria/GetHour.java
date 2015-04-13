package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class GetHour extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_minutes;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetHour.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetHour(final ArrayList<ParserObject> args) {
        super();
        final byte type = this.checkType(args);
        if (type == 1) {
            if (args.get(0).getValue().equalsIgnoreCase("hhmm")) {
                this.m_minutes = true;
            }
            else {
                this.m_minutes = false;
            }
        }
        else {
            this.m_minutes = false;
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final GameDateConst date = WakfuGameCalendar.getInstance().getDate();
        if (this.m_minutes) {
            return date.getHours() * 100 + date.getMinutes();
        }
        return date.getHours();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETHOUR;
    }
    
    static {
        GetHour.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        GetHour.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING };
        GetHour.signatures.add(sig);
    }
}
