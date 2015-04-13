package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.nation.crime.data.*;

public class CitizenScoreFieldProvider extends ImmutableFieldProvider
{
    public static final String SCORE = "score";
    public static final String CITIZEN_RANK_DESCRIPTION_FIELD = "rankDescription";
    public static final String CITIZEN_RANK_LONG_DESCRIPTION_FIELD = "rankLongDescription";
    public static final String COLOR_FIELD = "color";
    private int m_nationId;
    private int m_citizenScore;
    
    public CitizenScoreFieldProvider(final int nationId) {
        super();
        this.m_nationId = nationId;
    }
    
    public int getNationId() {
        return this.m_nationId;
    }
    
    public int getCitizenScore() {
        return this.m_citizenScore;
    }
    
    public void setCitizenScore(final int citizenScore) {
        this.m_citizenScore = citizenScore;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "score", "rankDescription", "rankLongDescription", "color");
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("score")) {
            try {
                return new TextWidgetFormater().append(this.m_citizenScore).append(" ").openText().addImage(WakfuTextImageProvider._getIconUrl((byte)9), -1, -1, null).finishAndToString();
            }
            catch (PropertyException e) {
                return this.m_citizenScore;
            }
        }
        if (fieldName.equals("rankLongDescription")) {
            if (this.m_nationId == 0) {
                return WakfuTranslator.getInstance().getString("map.territories.noNation");
            }
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.b().append(WakfuTranslator.getInstance().getString("citizen.score"))._b().append(this.m_citizenScore).newLine();
            sb.b().append(this.getCitizenRankName())._b();
            return sb.finishAndToString();
        }
        else if (fieldName.equals("rankDescription")) {
            if (this.m_nationId == 0) {
                return WakfuTranslator.getInstance().getString("map.territories.noNation");
            }
            return this.getCitizenRankName();
        }
        else {
            if (fieldName.equals("color")) {
                final float percentage = this.getCitizenPercentage();
                final float absValue = Math.abs(percentage);
                final CitizenRank rank = this.getPlayerRank();
                final Color color1 = Color.getRGBAFromHex(rank.getColor());
                CitizenRank referenceRank = (percentage > 0.0f) ? CitizenRankManager.getInstance().getRankAfter(rank) : CitizenRankManager.getInstance().getRankBefore(rank);
                if (referenceRank == null) {
                    referenceRank = rank;
                }
                final Color color2 = Color.getRGBAFromHex(referenceRank.getColor());
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(MathHelper.lerp(color1.getRed(), color2.getRed(), absValue)).append(",");
                sb2.append(MathHelper.lerp(color1.getGreen(), color2.getGreen(), absValue)).append(",");
                sb2.append(MathHelper.lerp(color1.getBlue(), color2.getBlue(), absValue));
                return sb2.toString();
            }
            return null;
        }
    }
    
    private float getCitizenPercentage() {
        final int score = this.getCitizenScore();
        float result = 0.0f;
        final CitizenRank rank = this.getPlayerRank();
        if (score > 0) {
            result = score / rank.getCap();
        }
        else if (score < 0) {
            final CitizenRank rankBefore = CitizenRankManager.getInstance().getRankBefore(rank);
            result = -(score / ((rankBefore == null) ? rank.getCap() : rankBefore.getCap()));
        }
        return result;
    }
    
    private CitizenRank getPlayerRank() {
        return CitizenRankManager.getInstance().getRankFromCitizenScore(this.getCitizenScore());
    }
    
    private String getCitizenRankName() {
        return WakfuTranslator.getInstance().getString(CitizenRankManager.getInstance().getRankFromCitizenScore(this.m_citizenScore).getTranslationKey());
    }
}
