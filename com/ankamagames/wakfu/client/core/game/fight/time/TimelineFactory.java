package com.ankamagames.wakfu.client.core.game.fight.time;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.fight.time.timescore.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.nodes.*;
import com.ankamagames.wakfu.common.game.fight.time.*;

public class TimelineFactory extends AbstractTimelineFactory<CharacterInfo, Fight, Timeline>
{
    public TimelineFactory(@NotNull final Fight fight) {
        super(fight);
    }
    
    @NotNull
    @Override
    protected Timeline createTimelineCore() {
        final TimeScoreGauges timeScoreGauges = this.createTimeScoreGauges();
        final InitProvider initProvider = this.createInitProvider();
        final FighterSortingStrategy sortingStrategy = this.createSortingStrategy(initProvider);
        final TimelineNodes nodes = this.createNodes(sortingStrategy);
        return new Timeline((Fight)this.m_fight, timeScoreGauges, initProvider, sortingStrategy, nodes);
    }
}
