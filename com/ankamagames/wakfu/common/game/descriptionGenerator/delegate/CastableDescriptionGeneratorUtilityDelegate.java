package com.ankamagames.wakfu.common.game.descriptionGenerator.delegate;

import com.ankamagames.baseImpl.common.clientAndServer.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.datas.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.pvp.*;

public interface CastableDescriptionGeneratorUtilityDelegate
{
    void initialize(@NotNull GameContentTranslator p0, @NotNull TextWidgetFormatterFactory p1);
    
    void formatGlyphName(TextWidgetFormater p0, AbstractEffectArea p1, short p2);
    
    void formatStateName(TextWidgetFormater p0, State p1, short p2);
    
    void formatSpellName(TextWidgetFormater p0, AbstractSpell p1);
    
    void formatSpellPropertyName(TextWidgetFormater p0, int p1);
    
    AbstractSpellLevel getSpell(short p0);
    
    String getAchievementName(int p0);
    
    String getItemTypeName(int p0);
    
    String getItemName(int p0);
    
    String getSkillName(short p0);
    
    String getCraftName(short p0);
    
    String getBonusName(int p0);
    
    String getMonsterName(short p0);
    
    String getMonsterTypeName(short p0);
    
    String getAvatarBreedName(int p0);
    
    String getNationName(int p0);
    
    String getInstanceName(int p0);
    
    int getNationId();
    
    TextWidgetFormater formatInvalidCriterion(TextWidgetFormater p0, String p1);
    
    TextWidgetFormater getCraftIcon(TextWidgetFormater p0);
    
    TextWidgetFormater getElementIcon(TextWidgetFormater p0, Elements p1);
    
    TextWidgetFormater getSpellTargetIcon(TextWidgetFormater p0, String p1);
    
    String getChrageIcon();
    
    String getCitizenPointsIcon();
    
    String getAreaOfEffectIcon(String p0);
    
    String getAreaOfEffectIcon(AreaOfEffect p0);
    
    String getInfiniteDurationString(GameContentTranslator p0);
    
    String getRemainingDurationString(GameContentTranslator p0, int p1);
    
    boolean useAutomaticDescription(AbstractSpellLevel p0);
    
    @Nullable
    BasicCharacterInfo getCaster();
    
    boolean forceDisplayState();
    
    @Nullable
    Object getCasterEffectContext();
    
    String getAlignmentName(byte p0);
    
    String getSubscriptionLevelName(int p0);
    
    String getPvpRankName(NationPvpRanks p0);
}
