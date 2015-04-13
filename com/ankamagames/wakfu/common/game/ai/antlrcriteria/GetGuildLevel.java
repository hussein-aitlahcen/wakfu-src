package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class GetGuildLevel extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    public GetGuildLevel() {
        super();
    }
    
    public GetGuildLevel(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetGuildLevel.signatures;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_GUILD_LEVEL;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser character = CriteriaUtils.getTargetCriterionUserFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (!(character instanceof GuildUser)) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        final GuildUser user = (GuildUser)character;
        return user.getGuildHandler().getLevel();
    }
    
    static {
        (GetGuildLevel.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
