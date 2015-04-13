package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public final class IsInGuild extends FunctionCriterion
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsInGuild.SIGNATURES;
    }
    
    public IsInGuild(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo character = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (!(character instanceof GuildUser)) {
            return -1;
        }
        final GuildUser user = (GuildUser)character;
        return (user.getGuildHandler().getGuildId() == 0L) ? -1 : 0;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_IN_GUILD;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
