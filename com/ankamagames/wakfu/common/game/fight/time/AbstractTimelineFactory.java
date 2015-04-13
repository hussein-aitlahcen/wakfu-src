package com.ankamagames.wakfu.common.game.fight.time;

import com.ankamagames.wakfu.common.datas.*;
import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.nodes.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.time.timescore.*;

public abstract class AbstractTimelineFactory<F extends BasicCharacterInfo, TFight extends TimeEventHandler, TTimeline extends AbstractTimeline<F>>
{
    private static final Logger m_logger;
    @NotNull
    protected final TFight m_fight;
    
    protected AbstractTimelineFactory(@NotNull final TFight fight) {
        super();
        this.m_fight = (TimeEventHandler)fight;
    }
    
    @NotNull
    protected TimelineNodes createNodes(final FighterSortingStrategy sortingStrategy) {
        return new TimelineNodesImpl(sortingStrategy);
    }
    
    @NotNull
    protected FighterSortingStrategy createSortingStrategy(final InitProvider initProvider) {
        return new AlternateTeamSortingStrategy((BasicFightInfo<? extends BasicCharacterInfo>)this.m_fight, initProvider);
    }
    
    @NotNull
    protected InitProvider createInitProvider() {
        return new FightInitProvider((FightersInformationProvider<? extends BasicCharacterInfo>)this.m_fight);
    }
    
    @NotNull
    protected TimeScoreGauges createTimeScoreGauges() {
        return new TimeScoreGaugesImpl();
    }
    
    @NotNull
    public TTimeline createTimeline() {
        final TTimeline timeline = this.createTimelineCore();
        timeline.setCurrentTurnDurationInMillisecond(((BasicFightInfo)this.m_fight).getModel().getTurnDurationInMillisecond());
        return timeline;
    }
    
    @NotNull
    protected abstract TTimeline createTimelineCore();
    
    static {
        m_logger = Logger.getLogger((Class)AbstractTimelineFactory.class);
    }
}
