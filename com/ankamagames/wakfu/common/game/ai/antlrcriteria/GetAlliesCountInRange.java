package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fight.protagonists.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public final class GetAlliesCountInRange extends GetFightersCountInRange
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_onlyInPlay;
    
    public GetAlliesCountInRange(final ArrayList<ParserObject> args) {
        super(args);
        this.m_onlyInPlay = true;
        if (args.size() == 6) {
            this.m_minRange = args.get(0);
            this.m_maxRange = args.get(1);
            this.m_target = args.get(2).getValue().equalsIgnoreCase("target");
            this.m_inLine = args.get(3);
            this.m_testLoS = args.get(4);
            this.m_onlyInPlay = args.get(5).isValid(null, null, null, null);
        }
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetAlliesCountInRange.signatures;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_ALLIES_COUNT_IN_RANGE;
    }
    
    @Override
    protected Collection<? extends BasicCharacterInfo> getFighters(final AbstractFight<?> fight, final CriterionUser user) {
        if (fight == null || user == null) {
            return (Collection<? extends BasicCharacterInfo>)Collections.emptyList();
        }
        if (this.m_onlyInPlay) {
            return fight.getFightersPresentInTimelineInPlayInTeam(user.getTeamId());
        }
        return fight.getFighters(ProtagonistFilter.inTeam(user.getTeamId()), ProtagonistFilter.not(ProtagonistFilter.hasProperty(WorldPropertyType.NOT_PRESENT_IN_TIMELINE)));
    }
    
    static {
        GetAlliesCountInRange.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        GetAlliesCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER };
        GetAlliesCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER, ParserType.STRING };
        GetAlliesCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER, ParserType.NUMBER, ParserType.STRING };
        GetAlliesCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER, ParserType.NUMBER, ParserType.STRING, ParserType.BOOLEAN };
        GetAlliesCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER, ParserType.NUMBER, ParserType.STRING, ParserType.BOOLEAN, ParserType.BOOLEAN };
        GetAlliesCountInRange.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER, ParserType.NUMBER, ParserType.STRING, ParserType.BOOLEAN, ParserType.BOOLEAN, ParserType.BOOLEAN };
        GetAlliesCountInRange.signatures.add(sig);
    }
}
