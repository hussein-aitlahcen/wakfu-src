package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;
import gnu.trove.*;

public class RemainingSlotsInBags extends FunctionValue
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return RemainingSlotsInBags.SIGNATURES;
    }
    
    public RemainingSlotsInBags(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    public RemainingSlotsInBags() {
        super();
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        BasicCharacterInfo user;
        if (criterionUser == null) {
            if (!(criterionContent instanceof PlayerTriggered)) {
                throw new CriteriaExecutionException("on cherche l'\u00e9quipement d'un user null");
            }
            user = ((PlayerTriggered)criterionContent).getTriggerer();
        }
        else {
            if (!(criterionUser instanceof BasicCharacterInfo)) {
                throw new CriteriaExecutionException("on cherche l'\u00e9quipement d'autre chose qu'un character");
            }
            user = (BasicCharacterInfo)criterionUser;
        }
        final AbstractBagContainer bags = user.getBags();
        final TLongObjectIterator<AbstractBag> tLongObjectIterator = bags.getBagsIterator();
        long total = 0L;
        while (tLongObjectIterator.hasNext()) {
            tLongObjectIterator.advance();
            final AbstractBag bag = tLongObjectIterator.value();
            total += bag.getMaximumSize() - bag.size();
        }
        return total;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HASEQUIPMENTTYPE;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
