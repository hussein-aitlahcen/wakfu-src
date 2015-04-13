package com.ankamagames.wakfu.client.core.game.fight.history;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.pvp.fight.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public final class PvpValueFieldProvider extends ImmutableFieldProvider
{
    public static final String PREVIOUS_RANK = "previousRank";
    public static final String CURRENT_RANK = "currentRank";
    public static final String PREVIOUS_RANKING = "previousRanking";
    public static final String CURRENT_RANKING = "currentRanking";
    public static final String PREVIOUS_STRENGTH = "previousStrength";
    public static final String CURRENT_STRENGTH = "currentStrength";
    public static final String STRENGTH_DESCRIPTION = "strengthDescription";
    public static final String RANKING_DESCRIPTION = "rankingDescription";
    public static final String RANK_DESCRIPTION = "rankDescription";
    public static final String STREAK = "streakDescription";
    public static final String[] FIELDS;
    private PlayerReportInfo m_reportInfo;
    
    @Override
    public String[] getFields() {
        return PvpValueFieldProvider.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (this.m_reportInfo == null) {
            return null;
        }
        if ("previousRank".equals(fieldName)) {
            return this.m_reportInfo.getPreviousRank();
        }
        if ("currentRank".equals(fieldName)) {
            return this.m_reportInfo.getCurrentRank();
        }
        if ("previousStrength".equals(fieldName)) {
            return this.m_reportInfo.getPreviousStrength();
        }
        if ("currentStrength".equals(fieldName)) {
            return this.m_reportInfo.getCurrentStrength();
        }
        if ("previousRanking".equals(fieldName)) {
            return this.m_reportInfo.getPreviousRanking();
        }
        if ("currentRanking".equals(fieldName)) {
            return this.m_reportInfo.getCurrentRanking();
        }
        if ("strengthDescription".equals(fieldName)) {
            return getScoreAndDeltaDescription(this.m_reportInfo.getCurrentStrength(), this.m_reportInfo.getPreviousStrength(), false);
        }
        if ("rankingDescription".equals(fieldName)) {
            return getScoreAndDeltaDescription(this.m_reportInfo.getCurrentRanking() + 1, this.m_reportInfo.getPreviousRanking() + 1, true);
        }
        if ("rankDescription".equals(fieldName)) {
            return WakfuTranslator.getInstance().getString("nation.pvpRank." + this.m_reportInfo.getCurrentRank());
        }
        if ("streakDescription".equals(fieldName)) {
            return "x " + this.m_reportInfo.getStreak();
        }
        return null;
    }
    
    private static String getScoreAndDeltaDescription(final int currentStrength, final int previousStrength, final boolean invert) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        final int delta = invert ? (previousStrength - currentStrength) : (currentStrength - previousStrength);
        sb.openText().addColor((delta < 0) ? Color.RED : Color.GREEN);
        sb.append((delta >= 0) ? '+' : "").append(delta);
        final String formattedDelta = sb.finishAndToString();
        return WakfuTranslator.getInstance().getString("pvp.fightResult.scoreDeltaDescription", currentStrength, formattedDelta);
    }
    
    public void setReportInfo(final PlayerReportInfo reportInfo) {
        this.m_reportInfo = reportInfo;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, PvpValueFieldProvider.FIELDS);
    }
    
    static {
        FIELDS = new String[] { "previousRank", "currentRank", "previousStrength", "currentStrength", "previousRanking", "currentRanking", "strengthDescription", "rankingDescription", "rankDescription", "streakDescription" };
    }
}
