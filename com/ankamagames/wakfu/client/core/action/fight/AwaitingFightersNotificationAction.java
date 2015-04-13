package com.ankamagames.wakfu.client.core.action.fight;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import gnu.trove.*;

public final class AwaitingFightersNotificationAction extends AbstractFightTimedAction
{
    private static final FightLogger FIGHT_LOGGER;
    private TLongHashSet m_awaitedFighters;
    
    public AwaitingFightersNotificationAction(final int uniqueId, final int actionType, final int actionId, final int fightId) {
        super(uniqueId, actionType, actionId, fightId);
    }
    
    @Override
    protected long onRun() {
        final TLongIterator it = this.m_awaitedFighters.iterator();
        StringBuilder fightersNameBuilder = null;
        while (it.hasNext()) {
            final CharacterInfo fighter = this.getFighterById(it.next());
            if (fighter == null) {
                continue;
            }
            if (fightersNameBuilder == null) {
                fightersNameBuilder = new StringBuilder();
            }
            fightersNameBuilder.append(fighter.getName());
            if (!it.hasNext()) {
                continue;
            }
            fightersNameBuilder.append(", ");
        }
        if (fightersNameBuilder == null) {
            return 0L;
        }
        final StringBuilder message = new StringBuilder(WakfuTranslator.getInstance().getString("fight.waiting.for"));
        message.append((CharSequence)fightersNameBuilder);
        AwaitingFightersNotificationAction.FIGHT_LOGGER.error(message);
        return 0L;
    }
    
    public void setAwaitedFighters(final TLongHashSet awaitedFighters) {
        this.m_awaitedFighters = awaitedFighters;
    }
    
    static {
        FIGHT_LOGGER = new FightLogger();
    }
}
