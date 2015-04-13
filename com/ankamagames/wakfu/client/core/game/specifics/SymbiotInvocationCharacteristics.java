package com.ankamagames.wakfu.client.core.game.specifics;

import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.colors.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.characteristics.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;

public class SymbiotInvocationCharacteristics extends BasicInvocationCharacteristics implements FieldProvider
{
    protected static final Logger m_logger;
    public static final String NAME_FIELD = "name";
    public static final String LEVEL_FIELD = "level";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String BREED_NAME_FIELD = "breedName";
    public static final String IS_AVAILABLE_FIELD = "isAvailable";
    public static final String UNAVAILABLE_REASONS_FIELD = "unavailableReasons";
    public static final String LEADER_SHIP_COST_FIELD = "leaderShipCost";
    public static final String HP_FIELD = "hp";
    public static final String MP_FIELD = "mp";
    public static final String AP_FIELD = "ap";
    public static final String WP_FIELD = "wp";
    public static final String INIT_FIELD = "init";
    public static final String TACKLE_FIELD = "tackle";
    public static final String DODGE_FIELD = "dodge";
    public static final String TACKLE_ICON_FIELD = "tackleIcon";
    public static final String DODGE_ICON_FIELD = "dodgeIcon";
    public static final String DMG_FIRE_PERCENT_FIELD = "dmgFirePercent";
    public static final String DMG_WATER_PERCENT_FIELD = "dmgWaterPercent";
    public static final String DMG_EARTH_PERCENT_FIELD = "dmgEarthPercent";
    public static final String DMG_WIND_PERCENT_FIELD = "dmgWindPercent";
    public static final String RES_FIRE_PERCENT_FIELD = "resFirePercent";
    public static final String RES_WATER_PERCENT_FIELD = "resWaterPercent";
    public static final String RES_EARTH_PERCENT_FIELD = "resEarthPercent";
    public static final String RES_WIND_PERCENT_FIELD = "resWindPercent";
    public static final String DEBUFF_AP_VISIBLE_FIELD = "debuffApVisible";
    public static final String DEBUFF_MP_VISIBLE_FIELD = "debuffMpVisible";
    public static final String BUFF_AP_PERCENT_FIELD = "buffApPercent";
    public static final String BUFF_MP_PERCENT_FIELD = "buffMpPercent";
    public static final String RES_AP_PERCENT_FIELD = "resApPercent";
    public static final String RES_MP_PERCENT_FIELD = "resMpPercent";
    public static final String ACTOR_DESCRIPTOR_LIBRARY_FIELD = "actorDescriptorLibrary";
    public static final String ACTOR_STANDARD_SCALE = "actorStandardScale";
    public static final String CAN_BE_RENAMED_FIELD = "canBeRenamed";
    static final String[] FIELDS;
    private boolean m_available;
    private static final short MIN_PLAYER_LEVEL_FOR_INVOCATION = 3;
    
    public SymbiotInvocationCharacteristics(final short id, final String name, final int hp, final long xp, final short cappedLevel) {
        super(id, name, hp, xp, cappedLevel);
        this.m_available = true;
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public String[] getFields() {
        return SymbiotInvocationCharacteristics.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("hp")) {
            if (MonsterBreedManager.getInstance().getBreedFromId(this.getTypeId()) != null) {
                return this.getCharacteristic(FighterCharacteristicType.HP);
            }
            return null;
        }
        else {
            if (fieldName.equals("level")) {
                return this.getBreedMaxLevel();
            }
            if (fieldName.equals("iconUrl")) {
                try {
                    final GroupDifficultyColor color = GroupDifficultyColor.getInvocationLevelColor(this.getLevel(), this.getCappedLevel());
                    final MonsterBreed monsterBreed = MonsterBreedManager.getInstance().getBreedFromId(this.getTypeId());
                    final MonsterFamily family = MonsterFamilyManager.getInstance().getMonsterFamily(monsterBreed.getFamilyId());
                    final int parentFamilyId = family.getParentFamilyId();
                    String iconUrl = null;
                    if (parentFamilyId != 0) {
                        iconUrl = color.getIconURL(parentFamilyId);
                        if (!URLUtils.urlExists(iconUrl)) {
                            iconUrl = color.getIconURL(0);
                        }
                    }
                    return iconUrl;
                }
                catch (Exception ex) {}
            }
            if (fieldName.equals("breedName")) {
                if (MonsterBreedManager.getInstance().getBreedFromId(this.getTypeId()) != null) {
                    return '(' + MonsterBreedManager.getInstance().getBreedFromId(this.getTypeId()).getName() + ')';
                }
                return null;
            }
            else {
                if (fieldName.equals("isAvailable")) {
                    return this.m_available;
                }
                if (fieldName.equals("unavailableReasons")) {
                    String result = null;
                    final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                    if (localPlayer.getCurrentFight() == null) {
                        return result;
                    }
                    if (this.hasGobGobInvoked()) {
                        return WakfuTranslator.getInstance().getString("desc.gobgobInvoke");
                    }
                    if (localPlayer.getLevel() < 3) {
                        result = WakfuTranslator.getInstance().getString("desc.osamodasBadLevelToInvoke", (short)3);
                    }
                    else if (!this.isCumuledLevelEndurabled()) {
                        result = WakfuTranslator.getInstance().getString("desc.osamodasInsufferableCreaturesLevel");
                    }
                    return result;
                }
                else {
                    if (fieldName.equals("ap")) {
                        return this.getCharacteristic(FighterCharacteristicType.AP);
                    }
                    if (fieldName.equals("mp")) {
                        return this.getCharacteristic(FighterCharacteristicType.MP);
                    }
                    if (fieldName.equals("wp")) {
                        return this.getCharacteristic(FighterCharacteristicType.WP);
                    }
                    if (fieldName.equals("init")) {
                        return this.getCharacteristic(FighterCharacteristicType.INIT);
                    }
                    if (fieldName.equals("init")) {
                        return this.getCharacteristic(FighterCharacteristicType.INIT);
                    }
                    if (fieldName.equals("tackle")) {
                        return this.getCharacteristic(FighterCharacteristicType.TACKLE);
                    }
                    if (fieldName.equals("dodge")) {
                        return this.getCharacteristic(FighterCharacteristicType.DODGE);
                    }
                    if (fieldName.equals("tackle")) {
                        return this.getCharacteristic(FighterCharacteristicType.TACKLE);
                    }
                    if (fieldName.equals("dmgFirePercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.DMG_FIRE_PERCENT);
                    }
                    if (fieldName.equals("dmgEarthPercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.DMG_EARTH_PERCENT);
                    }
                    if (fieldName.equals("dmgWaterPercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.DMG_WATER_PERCENT);
                    }
                    if (fieldName.equals("dmgWindPercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.DMG_AIR_PERCENT);
                    }
                    if (fieldName.equals("resEarthPercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.RES_EARTH_PERCENT);
                    }
                    if (fieldName.equals("resFirePercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.RES_FIRE_PERCENT);
                    }
                    if (fieldName.equals("resWaterPercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.RES_WATER_PERCENT);
                    }
                    if (fieldName.equals("resWindPercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.RES_AIR_PERCENT);
                    }
                    if (fieldName.equals("resWindPercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.RES_AIR_PERCENT);
                    }
                    if (fieldName.equals("debuffApVisible")) {
                        final Object pow = this.getCharacteristic(FighterCharacteristicType.AP_DEBUFF_POWER);
                        final Object res = this.getCharacteristic(FighterCharacteristicType.AP_DEBUFF_RES);
                        return pow != null && res != null && (int)pow != 0 && (int)res != 0;
                    }
                    if (fieldName.equals("debuffMpVisible")) {
                        final Object pow = this.getCharacteristic(FighterCharacteristicType.MP_DEBUFF_POWER);
                        final Object res = this.getCharacteristic(FighterCharacteristicType.MP_DEBUFF_RES);
                        return pow != null && res != null && (int)pow != 0 && (int)res != 0;
                    }
                    if (fieldName.equals("buffApPercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.AP_DEBUFF_POWER);
                    }
                    if (fieldName.equals("buffMpPercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.MP_DEBUFF_POWER);
                    }
                    if (fieldName.equals("resApPercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.AP_DEBUFF_RES);
                    }
                    if (fieldName.equals("resMpPercent")) {
                        return this.getDmgResDesc(FighterCharacteristicType.MP_DEBUFF_RES);
                    }
                    if (fieldName.equals("tackleIcon")) {
                        return WakfuConfiguration.getInstance().getAptitudeIcon(4);
                    }
                    if (fieldName.equals("dodgeIcon")) {
                        return WakfuConfiguration.getInstance().getAptitudeIcon(3);
                    }
                    if (fieldName.equals("actorDescriptorLibrary")) {
                        return this.getAnimation();
                    }
                    if (fieldName.equals("actorStandardScale")) {
                        final short height = MonsterBreedManager.getInstance().getBreedFromId(this.getTypeId()).getHeight();
                        final float anmScale = this.getAnimation().getAnmInstance().getScale();
                        return 6.0f / Math.max(height, 6.0f) * 1.4f * anmScale;
                    }
                    if (fieldName.equals("canBeRenamed")) {
                        return this.canBeRenamed();
                    }
                    if (fieldName.equals("leaderShipCost")) {
                        return MonsterBreedManager.getInstance().getBreedFromId(this.getTypeId()).getRequiredLeadershipPoints();
                    }
                    return null;
                }
            }
        }
    }
    
    private boolean hasGobGobInvoked() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer.hasGobgobInvoked();
    }
    
    private boolean isCumuledLevelEndurabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final int localPlayerTotalLeadership = localPlayer.getLevel() + localPlayer.getCharacteristicValue(FighterCharacteristicType.LEADERSHIP) * localPlayer.getCharacteristicValue(FighterCharacteristicType.OSA_INVOCATION_KNOWLEDGE);
        return localPlayer.getTotalLeadershipNeededForCreature(this) <= localPlayerTotalLeadership;
    }
    
    public boolean isCappedByLevelMax() {
        return this.getLevel() == this.getCappedLevel();
    }
    
    public boolean canBeRenamed() {
        return this.getLevel() == this.getCappedLevel();
    }
    
    private AnimatedElementWithDirection getAnimation() {
        final AnimatedElementWithDirection element = new AnimatedElementWithDirection(GUIDGenerator.getGUID(), 0.0f, 0.0f, 0.0f);
        final int gfxId = MonsterBreedManager.getInstance().getBreedFromId(this.getTypeId()).getGfx();
        try {
            element.load(String.format(WakfuConfiguration.getInstance().getString("npcGfxPath"), gfxId), true);
        }
        catch (IOException e) {
            SymbiotInvocationCharacteristics.m_logger.error((Object)"", (Throwable)e);
            return null;
        }
        catch (PropertyException e2) {
            SymbiotInvocationCharacteristics.m_logger.error((Object)"", (Throwable)e2);
            return null;
        }
        element.setGfxId(String.valueOf(gfxId));
        element.setDirection(Direction8.SOUTH);
        element.setAnimation("AnimStatique");
        return element;
    }
    
    public String getDmgResDesc(final FighterCharacteristicType fighterCharacteristicType) {
        final Object val = this.getCharacteristic(fighterCharacteristicType);
        return CharacteristicsUtil.displayPercentage((val == null) ? 0 : ((int)val), -1, true);
    }
    
    public Object getCharacteristic(final FighterCharacteristicType fighterCharacteristicType) {
        final MonsterBreed monsterBreed = MonsterBreedManager.getInstance().getBreedFromId(this.getTypeId());
        if (monsterBreed == null) {
            return null;
        }
        float primaryFactor = 1.0f;
        float secondaryFactor = 1.0f;
        final SpellLevel spell = WakfuGameEntity.getInstance().getLocalPlayer().getSpellInventory().getFirstWithReferenceId(787);
        if (spell == null) {
            return null;
        }
        for (final WakfuEffect effect : spell) {
            if (effect.getActionId() == RunningEffectConstants.SUMMON_FROM_SYMBIOT.getId()) {
                primaryFactor = effect.getParam(0, spell.getLevel()) / 100.0f;
                secondaryFactor = effect.getParam(1, spell.getLevel()) / 100.0f;
                break;
            }
        }
        return OsamodasCreatureCharacModifier.INSTANCE.getModifiedValue(fighterCharacteristicType, this.getLevel(), monsterBreed.getCharacteristicManager(), primaryFactor, secondaryFactor);
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return fieldName.equals("name");
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
        if (fieldName.equals("name")) {
            this.setName(value.toString());
        }
    }
    
    @Override
    public void setSummonId(final long summoned) {
        super.setSummonId(summoned);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isAvailable");
    }
    
    @Override
    public void setName(final String name) {
        super.setName(name);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "name");
    }
    
    public void setAvailability(final boolean available) {
        this.m_available = available;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isAvailable");
    }
    
    public String getShortDescription() {
        return this.getName();
    }
    
    @Override
    public boolean comeFromSymbiot() {
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SymbiotInvocationCharacteristics.class);
        FIELDS = new String[] { "name", "level", "iconUrl", "breedName", "isAvailable", "unavailableReasons", "hp", "ap", "mp", "wp", "init", "tackle", "dodge", "tackleIcon", "dodgeIcon", "dmgFirePercent", "dmgWaterPercent", "dmgEarthPercent", "dmgWindPercent", "resFirePercent", "resWaterPercent", "resEarthPercent", "resWindPercent", "debuffApVisible", "debuffMpVisible", "buffApPercent", "buffMpPercent", "resApPercent", "resMpPercent", "canBeRenamed" };
    }
}
