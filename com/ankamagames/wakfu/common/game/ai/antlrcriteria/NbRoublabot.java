package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class NbRoublabot extends NbSummons
{
    private static final int ROUBLABOT_1 = 1236;
    private static final int ROUBLABOT_2 = 1251;
    private static final int ROUBLABOT_3 = 1252;
    private static final int ROUBLABOT_4 = 1253;
    private static final int ROUBLABOT_5 = 1254;
    
    public NbRoublabot(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    protected int getSummoningCount(final CriterionUser targetCharacter) {
        return targetCharacter.getSummoningsCount(1236) + targetCharacter.getSummoningsCount(1251) + targetCharacter.getSummoningsCount(1252) + targetCharacter.getSummoningsCount(1253) + targetCharacter.getSummoningsCount(1254);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.NB_ROUBLABOT;
    }
}
