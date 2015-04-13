package com.ankamagames.wakfu.common.game.interactiveElements.param.type;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.external.*;

public interface GenericActionParameters
{
    public static final ActionListSet GIVE_KAMAS = new ActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("NB Kamas", ParserType.NUMBER) }) });
    public static final ActionListSet RESET_ACHIEVEMENT = new ActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("id achievement", ParserType.NUMBER) }) });
    public static final ActionListSet GIVE_EMOTE = new ActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("id emote", ParserType.NUMBER) }) });
    public static final ActionListSet KROSMOZ_GAME_PLAY = new ActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("id jeu Krosmoz", ParserType.NUMBER) }) });
    public static final ActionListSet RECUSTOM_CHARACTER = new ActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[0]) });
    public static final ActionListSet LAUNCH_SCENARIO = new ActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("id scenario", ParserType.NUMBER) }) });
    public static final ActionListSet PLAY_LUA = new ActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[0]), new EffectAreaParameterList("Broadcast", new Parameter[] { new ParserObjectParameter("BroadCaster autour?", ParserType.BOOLEAN) }), new EffectAreaParameterList("Variable", new Parameter[] { new ParserObjectParameter("BroadCaster autour?", ParserType.BOOLEAN), new ParserObjectParameter("Nom de variable", ParserType.STRING), new ParserObjectParameter("Valeur de variable", ParserType.NUMBER) }) });
    public static final ActionListSet TELEPORT = new ActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[] { new ParserObjectParameter("Position", ParserType.POSITION), new ParserObjectParameter("Instance ID", ParserType.NUMBER), new ParserObjectParameter("Direction", ParserType.NUMBER) }), new EffectAreaParameterList("Diff\u00e9r\u00e9", new Parameter[] { new ParserObjectParameter("Position", ParserType.POSITION), new ParserObjectParameter("Instance ID", ParserType.NUMBER), new ParserObjectParameter("Direction", ParserType.NUMBER), new ParserObjectParameter("D\u00e9lai (ms)", ParserType.NUMBER) }) });
    public static final ActionListSet GIVE_RANDOM_ITEM_IN_LIST = new ActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("id de la table de drop", ParserType.NUMBER) }) });
    public static final ActionListSet APPLY_STATE = new ActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("Id de l'\u00e9tat", ParserType.NUMBER) }) });
    public static final ActionListSet MERCENARY_DIALOG = new ActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("Id de la cat\u00e9gorie", ParserType.NUMBER) }) });
    
    public static class ActionListSet extends ParameterListSet
    {
        public ActionListSet(ParameterList... lists) {
            super(lists);
        }
        
        @Override
        public final boolean mapValueCount(int count) {
            return this.mapParameterCount(count);
        }
    }
}
