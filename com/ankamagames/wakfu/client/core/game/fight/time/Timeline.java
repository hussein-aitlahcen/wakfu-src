package com.ankamagames.wakfu.client.core.game.fight.time;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.fight.time.timescore.*;
import com.ankamagames.wakfu.common.game.fight.time.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.nodes.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.ui.protocol.message.character.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class Timeline extends AbstractTimeline<CharacterInfo> implements FieldProvider
{
    public static final String CURRENT_FIGHTER_FIELD = "currentFighter";
    public static final String CURRENT_FIGHTER_POSITION_FIELD = "currentFighterPosition";
    public static final String FIGHTERS_FIELD = "fighters";
    public static final String SECOND_TIMELINE_FIGHTERS_FIELD = "secondTimelineFighters";
    public static final String CURRENT_TABLE_TURN_FIELD = "currentTableTurn";
    public static final String AVAILABLE_BONUSES_FIELD = "availableBonuses";
    public static final String[] FIELDS;
    private TimelineWidget m_widget;
    private TimePointBarWidget m_timePointBarWidget;
    
    public Timeline(@NotNull final Fight owner, @NotNull final TimeScoreGauges timeScoreGauges, @NotNull final InitProvider initProvider, @NotNull final FighterSortingStrategy sortingStrategy, @NotNull final TimelineNodes nodes) {
        super(owner, timeScoreGauges, initProvider, sortingStrategy, nodes);
    }
    
    @Override
    public int getFighterPosition(final long fighterId) {
        final Fight f = (Fight)this.getGlobalListener();
        if (f.getStatus() == AbstractFight.FightStatus.CREATION || f.getStatus() == AbstractFight.FightStatus.PLACEMENT) {
            return this.getFighterUniquePosition(this.getDynamicOrder(), fighterId);
        }
        return this.getFighterUniquePosition(this.getTurnOrder(), fighterId);
    }
    
    private int getFighterUniquePosition(final TLongArrayList list, final long fighterId) {
        int index = 0;
        long prevId = 0L;
        for (int i = 0, size = list.size(); i < size; ++i) {
            if (fighterId == list.get(i)) {
                return index;
            }
            if (list.get(i) == prevId) {
                --index;
            }
            prevId = list.get(i);
            ++index;
        }
        return -1;
    }
    
    @Override
    public int getCurrentFighterPosition() {
        final byte offset = this.m_nodes.currentPosition();
        final TLongArrayList fighterOrder = this.m_nodes.getOrderThisTurn();
        if (offset == -1 || offset >= fighterOrder.size()) {
            return -1;
        }
        return this.getFighterUniquePosition(fighterOrder, fighterOrder.get(offset)) + (this.m_wasCurrentFighterRemoved ? 1 : 0);
    }
    
    public CharacterInfo getFighter(final long fighterId) {
        return (CharacterInfo)this.m_fightersInformationProvider.getFighterFromId(fighterId);
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("currentFighter")) {
            return this.getCurrentFighter();
        }
        if (fieldName.equals("currentFighterPosition")) {
            return this.getCurrentFighterPositionField();
        }
        if (fieldName.equals("fighters")) {
            return this.getOrderedFightersThisTurn();
        }
        if (fieldName.equals("secondTimelineFighters")) {
            return this.getOrderedFightersNextTurn();
        }
        if (fieldName.equals("currentTableTurn")) {
            return WakfuTranslator.getInstance().getString("fight.turn", this.getCurrentTableturn());
        }
        if (fieldName.equals("availableBonuses")) {
            final long lpcId = WakfuGameEntity.getInstance().getLocalPlayer().getId();
            return this.getAvailableBonusesFor(lpcId);
        }
        return null;
    }
    
    public TimePointEffect[] getAvailableBonusesFor(final long fighterId) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Fight fight = localPlayer.getCurrentOrObservedFight();
        final CharacterInfo fighter = fight.getFighterFromId(fighterId);
        final byte originalTeamId = fight.getOriginalTeamId(fighter);
        final List<WakfuEffect> allEffects = this.getTimeScoreGauges().getTeamEffects(originalTeamId);
        final List<WakfuEffect> availableEffects = this.getTimeScoreGauges().getEffectsAvailableForSelection(fighterId, originalTeamId);
        final TIntObjectHashMap<TimePointEffect> timePointEffects = new TIntObjectHashMap<TimePointEffect>();
        for (int i = 0, size = allEffects.size(); i < size; ++i) {
            final WakfuEffect effect = allEffects.get(i);
            final TimePointEffect timePointEffect = timePointEffects.get(effect.getEffectId());
            if (timePointEffect == null) {
                timePointEffects.put(effect.getEffectId(), new TimePointEffect(effect));
            }
        }
        for (int i = 0, size = availableEffects.size(); i < size; ++i) {
            final WakfuEffect effect = availableEffects.get(i);
            final TimePointEffect timePointEffect = timePointEffects.get(effect.getEffectId());
            if (timePointEffect != null) {
                timePointEffect.incrementStack();
            }
        }
        final TimePointEffect[] effects = timePointEffects.getValues(new TimePointEffect[timePointEffects.size()]);
        for (int j = 0, size2 = effects.length; j < size2; ++j) {
            if (j == size2 - 1) {
                effects[j].setDimension(48, 48);
            }
            else if (j == size2 - 2) {
                effects[j].setDimension(40, 40);
            }
            else {
                effects[j].setDimension(32, 32);
            }
        }
        return effects;
    }
    
    private Object getCurrentFighterPositionField() {
        final int currentFighterPos = this.getCurrentFighterPosition();
        if (currentFighterPos == 0) {
            return currentFighterPos;
        }
        final TLongArrayList turnOrder = this.getTurnOrder();
        if (turnOrder.get(currentFighterPos - 1) == turnOrder.get(currentFighterPos)) {
            return currentFighterPos - 1;
        }
        return currentFighterPos;
    }
    
    @Override
    public String[] getFields() {
        return Timeline.FIELDS;
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    public void setTimelineWidget(final TimelineWidget w) {
        this.m_widget = w;
    }
    
    public void setTimePointBarWidget(final TimePointBarWidget timePointBarWidget) {
        this.m_timePointBarWidget = timePointBarWidget;
    }
    
    public CharacterInfo getCurrentFighter() {
        return this.hasCurrentFighter() ? this.getFighter(this.getCurrentFighterId()) : null;
    }
    
    public boolean nextTurnHasADifferentOrder() {
        final TLongArrayList thisTurn = this.getTurnOrder();
        final TLongArrayList nextTurn = this.getDynamicOrder();
        if (nextTurn.size() != thisTurn.size()) {
            return true;
        }
        for (int i = 0, size = nextTurn.size(); i < size; ++i) {
            if (thisTurn.getQuick(i) != nextTurn.getQuick(i)) {
                return true;
            }
        }
        return false;
    }
    
    public int getTimePointGap() {
        return this.getTimeScoreGauges().getTimePointGap();
    }
    
    public int getTimeScoreGauge(final long fighterId) {
        return this.getTimeScoreGauges().getTimeScore(fighterId);
    }
    
    @Override
    public void startAction() {
        if (this.m_timePointBarWidget != null) {
            this.m_timePointBarWidget.onStartAction();
        }
    }
    
    public List<CharacterInfo> getOrderedFightersThisTurn() {
        return this.mapIdsToFighters(this.getTurnOrder());
    }
    
    public List<CharacterInfo> getOrderedFightersNextTurn() {
        return this.mapIdsToFighters(this.getDynamicOrder());
    }
    
    private List<CharacterInfo> mapIdsToFighters(final TLongArrayList idTrove) {
        final List<CharacterInfo> orderedFighters = new ArrayList<CharacterInfo>();
        for (int i = 0; i < idTrove.size(); ++i) {
            final long fighterId = idTrove.get(i);
            final CharacterInfo f = this.getFighter(fighterId);
            if (f != null) {
                final FighterFieldProvider ffp = f.getFighterFieldProvider();
                if (ffp == null) {
                    f.setFighterFieldProvider(new FighterFieldProvider(f, this));
                }
                else if (ffp.getTimeline() != this) {
                    Timeline.m_logger.warn((Object)('(' + f.getName() + " - " + fighterId + ") a un lien vers une autre timeline que la sienne"));
                    ffp.setTimeline(this);
                }
                if (!orderedFighters.contains(f)) {
                    orderedFighters.add(f);
                }
            }
        }
        return orderedFighters;
    }
    
    public void updateFirstTimelineFighterListProperty() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "fighters");
    }
    
    @Override
    protected void onCurrentFighterChange() {
        if (this.consernLocalPlayer()) {
            if (this.m_widget != null) {
                this.m_widget.onCurrentFighterChange();
            }
            if (this.m_timePointBarWidget != null) {
                this.m_timePointBarWidget.onCurrentFighterChange();
            }
            this.displayCurrentFighter();
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentFighter", "currentFighterPosition", "fighters");
        }
    }
    
    public void displayCurrentFighter() {
        PropertiesProvider.getInstance().setPropertyValue("selectedFighter", this.getCurrentOrFirstFighter());
    }
    
    public void displayCurrentFighter(final CharacterInfo character) {
        PropertiesProvider.getInstance().setPropertyValue("selectedFighter", character);
    }
    
    public CharacterInfo getCurrentOrFirstFighter() {
        CharacterInfo fighter = this.getCurrentFighter();
        if (fighter == null) {
            final TLongArrayList dynamicOrder = this.getDynamicOrder();
            if (dynamicOrder.size() > 0) {
                fighter = this.getFighter(dynamicOrder.get(0));
            }
        }
        return fighter;
    }
    
    private void sendTimelineCellDisplayMessage(final CharacterInfo info, final boolean display) {
        final UICharacterInfoMessage msg = new UICharacterInfoMessage();
        msg.setId(18103);
        msg.setBooleanValue(display);
        msg.setCharacterInfo(info);
        Worker.getInstance().pushMessage(msg);
    }
    
    void updateUIForAddedFighter(final CharacterInfo fighter) {
        if (fighter.getFighterFieldProvider() == null) {
            fighter.setFighterFieldProvider(new FighterFieldProvider(fighter, this));
        }
        else {
            Timeline.m_logger.warn((Object)("Le fighter ajout\u00e9 n'a pas \u00e9t\u00e9 bien nettoy\u00e9 \u00e0 la fin du combat pr\u00e9c\u00e9dent ! (" + fighter.getName() + " - " + fighter.getId() + ')'));
            if (fighter.getFighterFieldProvider().getTimeline() != this) {
                fighter.getFighterFieldProvider().setTimeline(this);
            }
        }
        if (this.m_widget != null) {
            this.m_widget.onFighterAdded(fighter);
        }
        if (this.m_timePointBarWidget != null) {
            this.m_timePointBarWidget.onFighterAdded(fighter);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "fighters", "secondTimelineFighters");
        this.sendTimelineCellDisplayMessage(fighter, true);
        PropertiesProvider.getInstance().setPropertyValue("selectedFighter", fighter);
    }
    
    void updateUIForRemovedFighter(final CharacterInfo fighter) {
        if (this.m_widget != null) {
            this.m_widget.onFighterRemoved(fighter);
        }
        if (this.m_timePointBarWidget != null) {
            this.m_timePointBarWidget.onFighterRemoved(fighter);
        }
        CharacterActorSelectionChangeListener.unselect(fighter.getActor());
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "fighters", "secondTimelineFighters");
        this.sendTimelineCellDisplayMessage(fighter, false);
        UITimelineFrame.getInstance().closeFighterDescription(fighter);
        final Property highlightedFighterProp = PropertiesProvider.getInstance().getProperty("fight.describedFighter");
        if (highlightedFighterProp != null && highlightedFighterProp.getValue() == fighter) {
            TimelineDialogActions.unhighlightFighterInTimeline(null, fighter);
        }
    }
    
    @Override
    protected void onTimePointGapChanged() {
        final List<CharacterInfo> fighters = this.getOrderedFightersThisTurn();
        for (int i = fighters.size() - 1; i >= 0; --i) {
            final FighterFieldProvider fieldProvider = fighters.get(i).getFighterFieldProvider();
            if (fieldProvider != null) {
                fieldProvider.updateExtraTurnScore();
            }
        }
    }
    
    @Override
    protected void onFighterAdded(final long fighterId) {
    }
    
    @Override
    protected void onFighterRemoved(final long fighterId) {
    }
    
    @Override
    protected void onNewTableTurn() {
        super.onNewTableTurn();
        if (this.consernLocalPlayer()) {
            if (this.m_widget != null) {
                this.m_widget.onNewTableTurn();
            }
            if (this.m_timePointBarWidget != null) {
                this.m_timePointBarWidget.onNewTableTurn();
            }
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentTableTurn", "secondTimelineFighters");
        }
    }
    
    @Override
    protected void onTurnEnded(final long fighterId) {
        if (this.consernLocalPlayer()) {
            final CharacterInfo fighter = this.getFighter(fighterId);
            if (fighter == null) {
                return;
            }
            if (this.m_widget != null) {
                this.m_widget.onTurnEnded(fighter);
            }
            if (this.m_timePointBarWidget != null) {
                this.m_timePointBarWidget.onTurnEnded(fighter);
            }
            final FighterFieldProvider ffp = fighter.getFighterFieldProvider();
            if (ffp != null) {
                ffp.onTurnEnded();
            }
        }
    }
    
    @Override
    protected void onTurnStarted(final long fighterId) {
        if (this.consernLocalPlayer()) {
            final CharacterInfo fighter = this.getFighter(fighterId);
            if (this.m_widget != null) {
                this.m_widget.onTurnStarted(fighter);
            }
            if (this.m_timePointBarWidget != null) {
                this.m_timePointBarWidget.onTurnStarted(fighter);
            }
            fighter.getFighterFieldProvider().onTurnStarted();
        }
    }
    
    private boolean consernLocalPlayer() {
        if (this.getGlobalListener() != null) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer != null) {
                final BasicFight<CharacterInfo> fight = localPlayer.getCurrentOrObservedFight();
                if (fight != null) {
                    return fight.equals(this.getGlobalListener());
                }
            }
        }
        return false;
    }
    
    @Override
    public void updateDynamicOrder() {
        super.updateDynamicOrder();
        if (this.consernLocalPlayer()) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "secondTimelineFighters");
        }
    }
    
    public void setTimePointGap(final int timePointGap) {
        this.getTimeScoreGauges().setTimePointGap(timePointGap);
    }
    
    public void highlightFighter(final CharacterInfo info, final boolean highlight) {
        final FighterFieldProvider fieldProvider = info.getFighterFieldProvider();
        if (fieldProvider != null) {
            fieldProvider.setHighlighted(highlight);
        }
    }
    
    static {
        FIELDS = new String[] { "currentFighter", "currentFighterPosition", "fighters", "secondTimelineFighters", "currentTableTurn", "availableBonuses" };
    }
}
