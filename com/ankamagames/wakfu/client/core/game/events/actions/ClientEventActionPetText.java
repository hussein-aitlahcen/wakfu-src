package com.ankamagames.wakfu.client.core.game.events.actions;

import com.ankamagames.wakfu.client.core.game.pet.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;

public class ClientEventActionPetText extends ClientEventAction
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private String m_text;
    private boolean m_forced;
    private long m_duration;
    private PetEmotion m_emotion;
    
    public ClientEventActionPetText(final int id) {
        super(id);
    }
    
    @Override
    public void execute() {
        UITutorialFrame.getInstance().pushPetMessage(WakfuTranslator.getInstance().getString(this.m_text), this.m_forced, this.m_duration, this.m_emotion);
    }
    
    @Override
    protected void parseParameters(final ArrayList<ParserObject> params) {
        this.m_emotion = PetEmotion.NEUTRAL;
        this.m_forced = false;
        this.m_duration = 0L;
        final int paramCount = params.size();
        if (paramCount < 1 || paramCount > 4) {
            ClientEventActionPetText.m_logger.error((Object)("Nombre de param\u00e8tres invalide pour une action de type PetText id=" + this.getId() + " paramCount=" + paramCount));
            return;
        }
        this.m_text = params.get(0).getValue();
        if (paramCount > 1) {
            this.m_forced = (params.get(1).getValidity(null, null, null, null) == 0);
        }
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventActionPetText.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("", new Parameter[] { new ParserObjectParameter("text", ParserType.STRING) }), new ClientGameEventParameterList("avec for\u00e7age", new Parameter[] { new ParserObjectParameter("text", ParserType.STRING), new ParserObjectParameter("forced", ParserType.BOOLEAN) }) });
    }
}
