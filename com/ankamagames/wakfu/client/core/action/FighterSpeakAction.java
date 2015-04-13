package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class FighterSpeakAction extends AbstractFightTimedAction
{
    private final int m_sentenceId;
    private final boolean m_blocking;
    
    public FighterSpeakAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final int sentenceId, final boolean blocking) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_sentenceId = sentenceId;
        this.m_blocking = blocking;
    }
    
    @Override
    protected long onRun() {
        if (this.getFight() == null) {
            FighterSpeakAction.m_logger.error((Object)"[SPEAK] On d\u00e9clenche une action de combat alors qu'il n'y a plus de combat");
            return 0L;
        }
        final long fighterId = this.getInstigatorId();
        final CharacterInfo fighter = this.getFight().getFighterFromId(fighterId);
        final String name = fighter.getName();
        final String sentence = WakfuTranslator.getInstance().getString(47, this.m_sentenceId, new Object[0]);
        final ChatMessage chatMessage = new ChatMessage(name, fighterId, sentence);
        chatMessage.setPipeDestination(1);
        ChatManager.getInstance().pushMessage(chatMessage);
        return this.m_blocking ? 750L : 0L;
    }
}
