package com.ankamagames.wakfu.client.core.game.fight;

import com.ankamagames.wakfu.client.core.game.group.party.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class FighterFieldProvider implements CharacterImageGenerator.Listener
{
    private static final Logger m_logger;
    public static final String EXTRA_TURN_SCORE_PERCENTAGE_FIELD = "extraTurnScorePercentage";
    public static final String EXTRA_TURN_SCORE_PERCENTAGE_NEXT_FIELD = "extraTurnScorePercentageNext";
    public static final String EXTRA_TURN_SCORE_DESCRIPTION_FIELD = "extraTurnScoreDescription";
    public static final String EXTRA_TURN_SCORE_VALUE_FIELD = "extraTurnScoreValue";
    public static final String TEAM_FIELD = "team";
    public static final String TIMELINE_ILLUSTRATION_FIELD = "timelineIllustrationUrl";
    public static final String TIMELINE_SMALL_ILLUSTRATION_FIELD = "timelineSmallIllustrationUrl";
    public static final String IS_MONSTER_FIELD = "isMonster";
    public static final String SELECTED_BONUS_FIELD = "selectedBonus";
    public static final String ACTOR_STANDARD_SCALE = "actorStandardScale";
    public static final String TIMELINE_INDEX = "timelineIndex";
    public static final String COLOR_POINT = "colorPoint";
    private CharacterInfo m_fighter;
    private Timeline m_timeline;
    private boolean m_isHighlighted;
    private float m_previousTimeScore;
    private TimePointEffect m_selectedTimePoint;
    private long m_characterImageListenedId;
    final String DEFAULT_COLOR = "#777777";
    final String[] COLOR_POINTS;
    
    public FighterFieldProvider(final CharacterInfo fighter, final Timeline timeline) {
        super();
        this.m_characterImageListenedId = -1L;
        this.COLOR_POINTS = new String[] { "#fc0097", "#ff0000", "#ff7f00", "#fec700", "#e2fc00", "#6fff00", "#00ff88", "#00e3fc", "#006cef", "#0008fe", "#7b00ff", "#b300ff", "#ed00fe", "#400000", "#cfcfcf", "#fefefe", "#777777" };
        this.m_fighter = fighter;
        this.m_timeline = timeline;
        if (this.m_fighter.getBreedId() == 1550) {
            final CharacterInfo info = this.m_fighter.getController();
            this.m_characterImageListenedId = info.getId();
        }
        else if (!(this.m_fighter.getBreed() instanceof MonsterBreed) || !ActorUtils.isNpcGfx(this.m_fighter.getGfxId()) || HoodedMonsterFightEventListener.isVisuallyHooded(this.m_fighter)) {
            this.m_characterImageListenedId = this.m_fighter.getId();
        }
        if (this.m_characterImageListenedId != -1L) {
            CharacterImageGenerator.getInstance().addListener(this.m_characterImageListenedId, this);
        }
    }
    
    public void cleanUp() {
        if (this.m_characterImageListenedId != -1L) {
            CharacterImageGenerator.getInstance().removeListener(this.m_characterImageListenedId, this);
        }
    }
    
    public CharacterInfo getFighter() {
        return this.m_fighter;
    }
    
    public Timeline getTimeline() {
        return this.m_timeline;
    }
    
    public void setTimeline(final Timeline timeline) {
        this.m_timeline = timeline;
    }
    
    private float getScorePercentageValue() {
        final int gap = this.m_timeline.getTimePointGap();
        final float timeScore = this.m_timeline.getTimeScoreGauge(this.m_fighter.getId()) % gap;
        return timeScore / gap;
    }
    
    private float getScorePercentageValueNext() {
        final int gap = this.m_timeline.getTimePointGap();
        float timeScore = this.m_timeline.getTimeScoreGauge(this.m_fighter.getId()) % gap;
        timeScore = Math.min(gap, timeScore + 100.0f + this.m_fighter.getCharacteristicValue(FighterCharacteristicType.INIT));
        return timeScore / gap;
    }
    
    public String[] getFields() {
        return null;
    }
    
    @Override
    public void onDone(final Texture texture, final String errorMsg) {
        if (texture != null) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this.m_fighter, "timelineSmallIllustrationUrl");
        }
        else {
            FighterFieldProvider.m_logger.warn((Object)errorMsg);
        }
    }
    
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("timelineElementType")) {
            return 1;
        }
        if (fieldName.equals("extraTurnScoreDescription")) {
            final int gap = this.m_timeline.getTimePointGap();
            final int timeScore = this.m_timeline.getTimeScoreGauge(this.m_fighter.getId()) % gap;
            return WakfuTranslator.getInstance().getString("fight.extraTurn.score", timeScore, gap);
        }
        if (fieldName.equals("extraTurnScorePercentage")) {
            return this.getScorePercentageValue();
        }
        if (fieldName.equals("extraTurnScorePercentageNext")) {
            return this.getScorePercentageValueNext();
        }
        if (fieldName.equals("extraTurnScoreValue")) {
            final int gap = this.m_timeline.getTimePointGap();
            return this.m_timeline.getTimeScoreGauge(this.m_fighter.getId()) % gap;
        }
        if (fieldName.equals("actorStandardScale")) {
            final CharacterActor characterActor = this.m_fighter.getActor();
            final short height = characterActor.getHeight();
            final float anmScale = characterActor.getAnmInstance().getScale();
            return 6.0f / Math.max(height, 6.0f) * 1.4f * anmScale;
        }
        if (fieldName.equals("team")) {
            return (byte)(this.isRedTeam() ? 0 : 1);
        }
        if (fieldName.equals("timelineIllustrationUrl")) {
            return this.getTimelineIllustrationUrl();
        }
        if (fieldName.equals("timelineSmallIllustrationUrl")) {
            if (this.m_characterImageListenedId != -1L) {
                return CharacterImageGenerator.getInstance().getCharacterImage(this.m_characterImageListenedId);
            }
            return this.getTimelineIllustrationUrl();
        }
        else {
            if (fieldName.equals("isMonster")) {
                return this.m_fighter.getBreedId() != 1550 && this.m_fighter.getBreed() instanceof MonsterBreed && ActorUtils.isNpcGfx(this.m_fighter.getGfxId()) && !HoodedMonsterFightEventListener.isVisuallyHooded(this.m_fighter);
            }
            if (fieldName.equals("selectedBonus")) {
                return this.m_selectedTimePoint;
            }
            if (fieldName.equals("timelineIndex")) {
                return this.m_timeline.getFighterPosition(this.m_fighter.getId()) + 1;
            }
            if (!fieldName.equals("colorPoint")) {
                return null;
            }
            final int fighterPosition = this.m_timeline.getFighterPosition(this.m_fighter.getId());
            if (fighterPosition >= this.COLOR_POINTS.length) {
                return "#777777";
            }
            return this.COLOR_POINTS[fighterPosition];
        }
    }
    
    public String getTimelineIllustrationUrl() {
        if (this.m_fighter.getBreedId() == 1550) {
            final CharacterInfo info = this.m_fighter.getController();
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedContactListIllustrationPath"), info.getBreedId(), info.getSex());
            }
            catch (PropertyException e) {
                FighterFieldProvider.m_logger.error((Object)"Exception", (Throwable)e);
                return null;
            }
        }
        if (this.m_fighter instanceof PlayerCharacter) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedContactListIllustrationPath"), this.m_fighter.getBreedId(), this.m_fighter.getSex());
            }
            catch (PropertyException e2) {
                FighterFieldProvider.m_logger.error((Object)"Exception", (Throwable)e2);
                return null;
            }
        }
        if (this.m_fighter.getType() == 5) {
            final MonsterBreed monsterBreed = (MonsterBreed)this.m_fighter.getBreed();
            final int gfxId = (monsterBreed.getTimelineGfx() == -1) ? this.m_fighter.getGfxId() : monsterBreed.getTimelineGfx();
            if (ActorUtils.isNpcGfx(gfxId)) {
                return WakfuConfiguration.getInstance().getMonsterIllustrationIconUrl(gfxId);
            }
            try {
                return String.format(WakfuConfiguration.getInstance().getString("companionCharacterSheetIllustrationPath"), monsterBreed.getBreedId());
            }
            catch (PropertyException e3) {
                FighterFieldProvider.m_logger.error((Object)"Exception", (Throwable)e3);
                return null;
            }
        }
        if (this.m_fighter.getBreed() instanceof MonsterBreed) {
            final MonsterBreed monsterBreed = (MonsterBreed)this.m_fighter.getBreed();
            final int gfxId = (monsterBreed.getTimelineGfx() == -1) ? this.m_fighter.getGfxId() : monsterBreed.getTimelineGfx();
            if (ActorUtils.isNpcGfx(gfxId)) {
                return WakfuConfiguration.getInstance().getMonsterIllustrationIconUrl(gfxId);
            }
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedContactListIllustrationPath"), ActorUtils.extractBreedFromGfxLol(gfxId), ActorUtils.extractSexFromGfxLol(gfxId));
            }
            catch (PropertyException e3) {
                FighterFieldProvider.m_logger.error((Object)"Exception", (Throwable)e3);
            }
        }
        return null;
    }
    
    private boolean isRedTeam() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        boolean isRedTeam;
        if (this.m_fighter.getCurrentFightId() == localPlayer.getCurrentFightId()) {
            isRedTeam = (this.m_fighter.getTeamId() == 0);
        }
        else {
            isRedTeam = (this.m_fighter.getTeamId() == -1);
        }
        return isRedTeam;
    }
    
    public void onTurnStarted() {
        this.updateExtraTurnScore();
    }
    
    public void onTurnEnded() {
        this.testForBonusSelection();
    }
    
    public void testForBonusSelection() {
        final int gap = this.m_timeline.getTimePointGap();
        final float timeScore = this.m_timeline.getTimeScoreGauge(this.m_fighter.getId()) % gap;
        if (timeScore < this.m_previousTimeScore) {
            this.setSelectedBonus(new TimePointEffect(true));
        }
        else if (timeScore > this.m_previousTimeScore && this.m_selectedTimePoint != null) {
            this.setSelectedBonus(null);
        }
        this.m_previousTimeScore = timeScore;
        if (this.m_fighter.isLocalPlayer()) {
            FighterFieldProvider.m_logger.warn((Object)("TimeScore = " + timeScore));
            PropertiesProvider.getInstance().firePropertyValueChanged(this.m_fighter, "extraTurnScoreDescription", "extraTurnScorePercentage", "extraTurnScoreValue");
            UITimelineFrame.getInstance().highlightTimeScoreBar((int)timeScore + 1);
        }
    }
    
    public void setSelectedBonus(final TimePointEffect effect) {
        this.m_selectedTimePoint = effect;
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_fighter, "selectedBonus");
    }
    
    public void updateExtraTurnScore() {
        if (this.m_fighter.getCurrentFight() != null && !this.m_fighter.getCurrentFight().getModel().isUseTimeScoreGauge()) {
            return;
        }
        UITimelineFrame.getInstance().refreshTimeLineBonusParticle((int)MathHelper.clamp(this.getScorePercentageValue() * 100.0f, 1.0f, 100.0f));
    }
    
    public void setHighlighted(boolean highlighted) {
        final CharacterActor actor = this.m_fighter.getActor();
        if (!actor.isVisible()) {
            highlighted = false;
        }
        if (!this.m_fighter.canBeFoundByUI()) {
            highlighted = false;
        }
        if (this.m_isHighlighted == highlighted) {
            return;
        }
        this.m_isHighlighted = highlighted;
        if (this.m_isHighlighted) {
            actor.highlightCharacter();
            actor.addSelectedParticleSystem();
        }
        else {
            actor.unHighlightCharacter();
            actor.clearSelectedParticleSystem();
        }
    }
    
    public int getTimeScore() {
        final int timePointGap = this.m_timeline.getTimePointGap();
        return this.m_timeline.getTimeScoreGauge(this.m_fighter.getId()) % ((timePointGap == 0) ? 1 : timePointGap);
    }
    
    public int getTimePointTurn() {
        final int timePointGap = this.m_timeline.getTimePointGap();
        return this.m_timeline.getTimeScoreGauge(this.m_fighter.getId()) / ((timePointGap == 0) ? 1 : timePointGap);
    }
    
    public void updateTeamField() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_fighter, "team");
    }
    
    static {
        m_logger = Logger.getLogger((Class)FighterFieldProvider.class);
    }
}
