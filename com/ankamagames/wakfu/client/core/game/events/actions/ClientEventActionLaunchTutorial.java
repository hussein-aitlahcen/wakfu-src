package com.ankamagames.wakfu.client.core.game.events.actions;

import com.ankamagames.wakfu.client.ui.protocol.message.tutorial.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;

public class ClientEventActionLaunchTutorial extends ClientEventAction
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private String m_title;
    private String m_desc;
    private String m_iconName;
    private int m_type;
    
    public ClientEventActionLaunchTutorial(final int id) {
        super(id);
    }
    
    @Override
    public void execute() {
        final Message msg = new UITutorialMessage(this.m_title, this.m_desc, this.m_iconName, this.m_type, this.getId());
        Worker.getInstance().pushMessage(msg);
    }
    
    @Override
    protected void parseParameters(final ArrayList<ParserObject> params) {
        final int paramCount = params.size();
        if (paramCount != 3) {
            ClientEventActionLaunchTutorial.m_logger.error((Object)("Nombre de param\u00e8tres invalide pour une action de type ClientEventActionLaunchTutorial id=" + this.getId() + " paramCount=" + paramCount));
            return;
        }
        int currentIndex = 0;
        final String iconName = params.get(currentIndex++).getValue();
        final String title = WakfuTranslator.getInstance().getString(params.get(currentIndex++).getValue());
        final String key = params.get(currentIndex++).getValue();
        int type;
        if (paramCount == currentIndex) {
            type = 0;
        }
        else {
            try {
                type = (int)params.get(currentIndex).getLongValue(null, null, null, null);
                ++currentIndex;
            }
            catch (ClassCastException e) {
                type = 0;
            }
        }
        final String desc = WakfuTranslator.getInstance().getString(key);
        this.m_iconName = iconName;
        this.m_title = title;
        this.m_desc = desc;
        this.m_type = type;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventActionLaunchTutorial.PARAMETERS_LIST_SET;
    }
    
    public String getTitle() {
        return this.m_title;
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("", new Parameter[] { new ParserObjectParameter("iconName", ParserType.STRING), new ParserObjectParameter("titleTranslationKey", ParserType.STRING), new ParserObjectParameter("textTranslationKey", ParserType.STRING) }), new ClientGameEventParameterList("En pr\u00e9cisant le type (tuto sinon)", new Parameter[] { new ParserObjectParameter("iconName", ParserType.STRING), new ParserObjectParameter("titleTranslationKey", ParserType.STRING), new ParserObjectParameter("textTranslationKey", ParserType.STRING), new ParserObjectParameter("type", ParserType.NUMBER) }) });
    }
}
