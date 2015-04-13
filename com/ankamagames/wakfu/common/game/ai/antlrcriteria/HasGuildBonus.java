package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.guild.*;

public class HasGuildBonus extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> signatures;
    private NumericalValue m_bonusId;
    
    public HasGuildBonus() {
        super();
    }
    
    public HasGuildBonus(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_bonusId = args.get(0);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasGuildBonus.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser user = CriteriaUtils.getTargetCriterionUserFromParameters(criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user == null) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        if (!(user instanceof GuildUser)) {
            throw new CriteriaExecutionException("Mauvais utilisateur du crit\u00e8re");
        }
        final GuildUser player = (GuildUser)user;
        final GuildInformationHandler handler = player.getGuildHandler();
        final int bonusId = (int)this.m_bonusId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        return (handler.getGuildId() > 0L && handler.getActiveBonuses().contains(bonusId)) ? 0 : -1;
    }
    
    public int getBonusId() {
        if (this.m_bonusId.isInteger()) {
            return (int)this.m_bonusId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_GUILD_BONUS;
    }
    
    @Override
    public String toString() {
        return "HasGuildBonus{m_bonusId=" + this.m_bonusId + '}';
    }
    
    static {
        (signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.NUMBER });
    }
}
