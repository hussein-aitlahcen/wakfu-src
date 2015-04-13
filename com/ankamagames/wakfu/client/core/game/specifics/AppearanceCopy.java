package com.ankamagames.wakfu.client.core.game.specifics;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.colors.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public final class AppearanceCopy
{
    private static final Logger m_logger;
    
    public static void copySummonerAppearance(final NonPlayerCharacter summonning, final CharacterInfo summoner) {
        copySummonerAppearance(summonning, summoner, summoner.getSex(), summoner.getBreedId());
    }
    
    public static void copySummonerAppearance(final NonPlayerCharacter summonning, final CharacterInfo summoner, final byte sex, final int typeId) {
        if (summoner == null) {
            return;
        }
        summonning.setGfxId(summoner.getGfxId());
        summonning.setForcedGfxId(summoner.getGfxId());
        if (summoner instanceof PlayerCharacter) {
            final PlayerCharacter pcSummoner = (PlayerCharacter)summoner;
            final MonsterSpecialGfx customGfx = new MonsterSpecialGfx();
            final CharacterColor hairColor = BreedColorsManager.getInstance().getHairColor(typeId, pcSummoner.getHairColorIndex(), pcSummoner.getHairColorFactor(), pcSummoner.getSex());
            if (hairColor != null) {
                customGfx.addColor(new MonsterSpecialGfx.Colors(2, hairColor.getColor()));
            }
            final CharacterColor skinColor = BreedColorsManager.getInstance().getSkinColor(typeId, pcSummoner.getSkinColorIndex(), pcSummoner.getSkinColorFactor(), pcSummoner.getSex());
            if (skinColor != null) {
                customGfx.addColor(new MonsterSpecialGfx.Colors(1, skinColor.getColor()));
            }
            final CharacterColor pupilColor = BreedColorsManager.getInstance().getPupilColor(typeId, pcSummoner.getPupilColorIndex(), sex);
            if (pupilColor != null) {
                customGfx.addColor(new MonsterSpecialGfx.Colors(8, pupilColor.getColor()));
            }
            customGfx.addEquipement(new MonsterSpecialGfx.Equipment(summoner.getCurrentDressStyle(), AnmPartHelper.getParts("VETEMENTCUSTOM")));
            customGfx.addEquipement(new MonsterSpecialGfx.Equipment(summoner.getCurrentHairStyle(), AnmPartHelper.getParts("CHEVEUXCUSTOM")));
            summonning.setSpecificSpecialGfx(customGfx);
            final CharacterAdditionalAppearance additionalAppearence = pcSummoner.getAdditionalAppearance();
            if (additionalAppearence != null) {
                summonning.copyAdditionalAppearance(additionalAppearence);
            }
        }
        else {
            if (!(summoner instanceof NonPlayerCharacter)) {
                AppearanceCopy.m_logger.error((Object)("Summoner type not handled : " + summoner + " : " + summoner.getClass().getSimpleName()));
                return;
            }
            final MonsterSpecialGfx customGfx2 = ((NonPlayerCharacter)summoner).getBreedSpecialGfx();
            summonning.setSpecificSpecialGfx(customGfx2);
        }
        summonning.forceEquipmentAppearance(summoner.getEquipmentAppearance());
        final CharacterActor summonActor = summonning.getActor();
        final CharacterActor summonerActor = summoner.getActor();
        if (summonerActor != null) {
            summonerActor.getHmiHelper().copyAndApplyPartsChangesTo(summonActor);
            summonerActor.getHmiHelper().copyAndApplyLightsTo(summonActor);
            summonerActor.getHmiHelper().copyAndApplyColorsTo(summonActor);
            summonerActor.getHmiHelper().copyAndApplyPartsVisibilityTo(summonActor);
        }
        summonning.refreshDisplayEquipment();
    }
    
    private static int getTypeId() {
        return 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AppearanceCopy.class);
    }
}
