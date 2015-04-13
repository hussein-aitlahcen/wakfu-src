package com.ankamagames.wakfu.common.game.characterInfo.action;

import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.external.*;

public class MonsterActionParameters
{
    public static final ParameterListSet DO_NOTHING;
    public static final ParameterListSet SET_COMPORTMENT;
    public static final ParameterListSet START_DIALOG;
    public static final ParameterListSet DESTROY;
    public static final ParameterListSet MANAGE_HAVEN_WORLD;
    
    static {
        DO_NOTHING = new MonsterActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[0]) });
        SET_COMPORTMENT = new MonsterActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[] { new ParserObjectParameter("Id du comportement du monstre", ParserType.NUMBER) }) });
        START_DIALOG = new MonsterActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[] { new ParserObjectParameter("Id des dialogues \u00e0 d\u00e9marrer (test\u00e9s dans l'ordre de la liste)", ParserType.NUMBERLIST) }) });
        DESTROY = new MonsterActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[0]) });
        MANAGE_HAVEN_WORLD = new MonsterActionListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[0]) });
    }
    
    private static class MonsterActionListSet extends ParameterListSet
    {
        MonsterActionListSet(final ParameterList... lists) {
            super(lists);
        }
        
        @Override
        public final boolean mapValueCount(final int count) {
            return this.mapParameterCount(count);
        }
    }
}
