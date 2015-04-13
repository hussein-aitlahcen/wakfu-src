package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import org.jetbrains.annotations.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;

public final class GetGuildPartnerCountInFight extends FunctionValue
{
    private static final ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetGuildPartnerCountInFight.signatures;
    }
    
    public GetGuildPartnerCountInFight(final ArrayList<ParserObject> args) {
        super();
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final CriterionUser character = CriteriaUtils.getTargetCriterionUserFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        final AbstractFight<?> fight = CriteriaUtils.getFight(criterionUser, criterionContext);
        if (fight == null) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer le combat");
        }
        if (!(character instanceof GuildUser)) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        final GuildUser user = (GuildUser)character;
        final long guildId = user.getGuildHandler().getGuildId();
        if (guildId == 0L) {
            return 0L;
        }
        final Collection<? extends BasicCharacterInfo> fightersInTeam = fight.getFightersInTeam(character.getTeamId());
        int count = -1;
        for (final BasicCharacterInfo c : fightersInTeam) {
            if (c instanceof GuildUser) {
                final GuildUser partner = (GuildUser)c;
                if (partner.getGuildHandler().getGuildId() != guildId) {
                    continue;
                }
                ++count;
            }
        }
        assert count >= 0;
        return count;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_GUILD_PARTNER_COUNT_IN_FIGHT;
    }
    
    static {
        (signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
