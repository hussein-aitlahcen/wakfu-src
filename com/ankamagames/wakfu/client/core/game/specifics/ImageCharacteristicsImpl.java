package com.ankamagames.wakfu.client.core.game.specifics;

import com.ankamagames.wakfu.common.datas.specific.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.colors.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class ImageCharacteristicsImpl extends ImageCharacteristics
{
    protected static final Logger m_logger;
    
    public ImageCharacteristicsImpl() {
        super();
    }
    
    @Override
    public ImageCharacteristics newInstance() {
        return new ImageCharacteristicsImpl();
    }
    
    public ImageCharacteristicsImpl(final short id, final String name, final int hp, final short level, final BasicCharacterInfo summoner, final SpellInventory<AbstractSpellLevel> spellInventory) {
        super(id, name, hp, level, summoner, spellInventory);
    }
    
    @Override
    public ImageCharacteristicsImpl newInstance(final short id, final String name, final int hp, final short level, final BasicCharacterInfo summoner, final SpellInventory<AbstractSpellLevel> spellInventory) {
        return new ImageCharacteristicsImpl(id, name, hp, level, summoner, spellInventory);
    }
    
    public ImageCharacteristicsImpl(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        super(maximumSpellInventorySize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
    }
    
    @Override
    public ImageCharacteristics newInstance(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        return new ImageCharacteristicsImpl(maximumSpellInventorySize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
    }
    
    @Override
    public void initializeSummoning(final BasicCharacterInfo bciSummoning, final BasicCharacterInfo bciSummoner) {
        super.initializeSummoning(bciSummoning, bciSummoner);
        final NonPlayerCharacter summoned = (NonPlayerCharacter)bciSummoning;
        final CharacterInfo summoner = (CharacterInfo)bciSummoner;
        final CharacterActor summonActor = summoned.getActor();
        if (summoner == null) {
            return;
        }
        summoned.setGfxId(summoner.getGfxId());
        summoned.setForcedGfxId(summoner.getGfxId());
        if (summoner instanceof PlayerCharacter) {
            final PlayerCharacter pcSummoner = (PlayerCharacter)summoner;
            final MonsterSpecialGfx customGfx = new MonsterSpecialGfx();
            final CharacterColor hairColor = BreedColorsManager.getInstance().getHairColor(this.getTypeId(), pcSummoner.getHairColorIndex(), pcSummoner.getHairColorFactor(), pcSummoner.getSex());
            if (hairColor != null) {
                customGfx.addColor(new MonsterSpecialGfx.Colors(2, hairColor.getColor()));
            }
            final CharacterColor skinColor = BreedColorsManager.getInstance().getSkinColor(this.getTypeId(), pcSummoner.getSkinColorIndex(), pcSummoner.getSkinColorFactor(), pcSummoner.getSex());
            if (skinColor != null) {
                customGfx.addColor(new MonsterSpecialGfx.Colors(1, skinColor.getColor()));
            }
            final CharacterColor pupilColor = BreedColorsManager.getInstance().getPupilColor(this.getTypeId(), pcSummoner.getPupilColorIndex(), this.m_sex);
            if (pupilColor != null) {
                customGfx.addColor(new MonsterSpecialGfx.Colors(8, pupilColor.getColor()));
            }
            customGfx.addEquipement(new MonsterSpecialGfx.Equipment(summoner.getCurrentDressStyle(), AnmPartHelper.getParts("VETEMENTCUSTOM")));
            customGfx.addEquipement(new MonsterSpecialGfx.Equipment(summoner.getCurrentHairStyle(), AnmPartHelper.getParts("CHEVEUXCUSTOM")));
            summoned.setSpecificSpecialGfx(customGfx);
            final CharacterAdditionalAppearance additionalAppearence = pcSummoner.getAdditionalAppearance();
            if (additionalAppearence != null) {
                summoned.copyAdditionalAppearance(additionalAppearence);
            }
        }
        else {
            if (!(summoner instanceof NonPlayerCharacter)) {
                ImageCharacteristicsImpl.m_logger.error((Object)("Summoner type not handled : " + summoner + " : " + summoner.getClass().getSimpleName()));
                return;
            }
            final MonsterSpecialGfx customGfx2 = ((NonPlayerCharacter)summoner).getBreedSpecialGfx();
            summoned.setSpecificSpecialGfx(customGfx2);
        }
        summoned.forceEquipmentAppearance(summoner.getEquipmentAppearance());
        final CharacterActor summonerActor = summoner.getActor();
        if (summonerActor != null) {
            summonerActor.getHmiHelper().copyAndApplyAllTo(summonActor);
        }
        summoned.refreshDisplayEquipment();
        summonActor.setVisible(false);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ImageCharacteristicsImpl.class);
    }
}
