package com.ankamagames.wakfu.common.game.protector;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.nation.*;

public abstract class ProtectorSerializer<P extends ProtectorBase>
{
    protected static final Logger m_logger;
    protected final RawProtector m_rawProtector;
    private ProtectorFactory<P> m_protectorFactory;
    
    protected ProtectorSerializer() {
        super();
        this.m_rawProtector = new RawProtector();
    }
    
    public RawProtector getRawProtector() {
        return this.m_rawProtector;
    }
    
    public void setProtectorFactory(final ProtectorFactory<P> protectorFactory) {
        this.m_protectorFactory = protectorFactory;
    }
    
    private void prepareCommonPart(final P protector) {
        this.m_rawProtector.clear();
        this.m_rawProtector.protectorId = protector.getId();
    }
    
    private void prepareNationalityPart(final P protector) {
        final Nation nativeNation = protector.getNativeNation();
        final RawProtector.Nationality nationality = new RawProtector.Nationality();
        this.m_rawProtector.nationality = nationality;
        if (nativeNation != null) {
            nationality.nativeNationId = nativeNation.getNationId();
        }
        else {
            nationality.nativeNationId = -1;
        }
        final Nation currentNation = protector.getCurrentNation();
        if (currentNation != null) {
            nationality.currentNationId = currentNation.getNationId();
        }
        else {
            nationality.currentNationId = -1;
        }
        final AbstractTerritory territory = protector.getTerritory();
        if (territory != null) {
            nationality.territoryId = territory.getId();
        }
        else {
            nationality.territoryId = -1;
        }
        this.prepareNationalityPart(protector, nationality);
    }
    
    private byte[] serializeCurrent() {
        return this.serializeRawProtector(this.m_rawProtector);
    }
    
    public byte[] serializeRawProtector(final RawProtector rawProtector) {
        final int size = rawProtector.serializedSize();
        final ByteBuffer buffer = ByteBuffer.allocate(size);
        rawProtector.serialize(buffer);
        return buffer.array();
    }
    
    public byte[] serializeForOuterTerritoryEntrance(final P protector) {
        this.prepareCommonPart(protector);
        this.prepareNationalityPart(protector);
        this.prepareAppearancePart(protector, this.m_rawProtector.appearance = new RawProtector.Appearance());
        this.prepareTaxesPart(protector, this.m_rawProtector.taxes = new RawProtector.Taxes());
        this.prepareSatisfactionPart(protector, this.m_rawProtector.satisfaction = new RawProtector.Satisfaction());
        this.prepareMonsterTargetsPart(protector, this.m_rawProtector.monsterTargets = new RawProtector.MonsterTargets());
        this.prepareResourceTargetsPart(protector, this.m_rawProtector.resourceTargets = new RawProtector.ResourceTargets());
        this.prepareReferenceInventoriesPart(protector, this.m_rawProtector.referenceMerchantInventories = new RawProtector.ReferenceMerchantInventories());
        this.prepareEcosystemPart(protector, this.m_rawProtector.ecosystem = new RawProtector.Ecosystem());
        return this.serializeCurrent();
    }
    
    public byte[] serializeForTerritoryEntrance(final P protector) {
        this.prepareCommonPart(protector);
        this.prepareNationalityPart(protector);
        this.prepareAppearancePart(protector, this.m_rawProtector.appearance = new RawProtector.Appearance());
        this.prepareTaxesPart(protector, this.m_rawProtector.taxes = new RawProtector.Taxes());
        this.prepareSatisfactionPart(protector, this.m_rawProtector.satisfaction = new RawProtector.Satisfaction());
        this.prepareMonsterTargetsPart(protector, this.m_rawProtector.monsterTargets = new RawProtector.MonsterTargets());
        this.prepareResourceTargetsPart(protector, this.m_rawProtector.resourceTargets = new RawProtector.ResourceTargets());
        this.prepareReferenceInventoriesPart(protector, this.m_rawProtector.referenceMerchantInventories = new RawProtector.ReferenceMerchantInventories());
        this.prepareEcosystemPart(protector, this.m_rawProtector.ecosystem = new RawProtector.Ecosystem());
        return this.serializeCurrent();
    }
    
    public byte[] serializeForTaxUpdate(final P protector) {
        this.prepareCommonPart(protector);
        this.prepareTaxesPart(protector, this.m_rawProtector.taxes = new RawProtector.Taxes());
        return this.serializeCurrent();
    }
    
    public byte[] serializeForTerritoryManagement(final P protector) {
        this.prepareCommonPart(protector);
        this.prepareChallengesPart(protector, this.m_rawProtector.challenges = new RawProtector.Challenges());
        this.prepareAppearancePart(protector, this.m_rawProtector.appearance = new RawProtector.Appearance());
        this.prepareMerchantInventoriesPart(protector, this.m_rawProtector.nationMerchantInventories = new RawProtector.NationMerchantInventories());
        this.prepareWalletPart(protector, this.m_rawProtector.wallet = new RawProtector.Wallet());
        this.prepareTaxesPart(protector, this.m_rawProtector.taxes = new RawProtector.Taxes());
        this.prepareReferenceInventoriesPart(protector, this.m_rawProtector.referenceMerchantInventories = new RawProtector.ReferenceMerchantInventories());
        this.prepareWeatherModifiersPart(protector, this.m_rawProtector.weatherModifiers = new RawProtector.WeatherModifiers());
        this.prepareMonsterTargetsPart(protector, this.m_rawProtector.monsterTargets = new RawProtector.MonsterTargets());
        this.prepareResourceTargetsPart(protector, this.m_rawProtector.resourceTargets = new RawProtector.ResourceTargets());
        this.prepareEcosystemPart(protector, this.m_rawProtector.ecosystem = new RawProtector.Ecosystem());
        return this.serializeCurrent();
    }
    
    public byte[] serializeForSave(final P protector) {
        this.prepareCommonPart(protector);
        this.prepareNationalityPart(protector);
        this.prepareChallengesPart(protector, this.m_rawProtector.challenges = new RawProtector.Challenges());
        this.prepareAppearancePart(protector, this.m_rawProtector.appearance = new RawProtector.Appearance());
        this.prepareReferenceInventoriesPart(protector, this.m_rawProtector.referenceMerchantInventories = new RawProtector.ReferenceMerchantInventories());
        this.prepareWalletPart(protector, this.m_rawProtector.wallet = new RawProtector.Wallet());
        this.prepareTaxesPart(protector, this.m_rawProtector.taxes = new RawProtector.Taxes());
        this.prepareWeatherModifiersPart(protector, this.m_rawProtector.weatherModifiers = new RawProtector.WeatherModifiers());
        this.prepareMonsterTargetsPart(protector, this.m_rawProtector.monsterTargets = new RawProtector.MonsterTargets());
        this.prepareResourceTargetsPart(protector, this.m_rawProtector.resourceTargets = new RawProtector.ResourceTargets());
        this.prepareEcosystemPart(protector, this.m_rawProtector.ecosystem = new RawProtector.Ecosystem());
        return this.serializeCurrent();
    }
    
    public byte[] serializeForSync(final P protector) {
        this.prepareCommonPart(protector);
        this.prepareNationalityPart(protector);
        this.prepareChallengesPart(protector, this.m_rawProtector.challenges = new RawProtector.Challenges());
        this.prepareAppearancePart(protector, this.m_rawProtector.appearance = new RawProtector.Appearance());
        this.prepareReferenceInventoriesPart(protector, this.m_rawProtector.referenceMerchantInventories = new RawProtector.ReferenceMerchantInventories());
        this.prepareWalletPart(protector, this.m_rawProtector.wallet = new RawProtector.Wallet());
        this.prepareTaxesPart(protector, this.m_rawProtector.taxes = new RawProtector.Taxes());
        this.prepareWeatherModifiersPart(protector, this.m_rawProtector.weatherModifiers = new RawProtector.WeatherModifiers());
        this.prepareMonsterTargetsPart(protector, this.m_rawProtector.monsterTargets = new RawProtector.MonsterTargets());
        this.prepareResourceTargetsPart(protector, this.m_rawProtector.resourceTargets = new RawProtector.ResourceTargets());
        this.prepareStakePart(protector, this.m_rawProtector.stake = new RawProtector.Stake());
        this.prepareEcosystemPart(protector, this.m_rawProtector.ecosystem = new RawProtector.Ecosystem());
        return this.serializeCurrent();
    }
    
    public byte[] serializeForCreation(final P protector) {
        this.prepareCommonPart(protector);
        this.prepareNationalityPart(protector);
        this.prepareChallengesPart(protector, this.m_rawProtector.challenges = new RawProtector.Challenges());
        this.prepareAppearancePart(protector, this.m_rawProtector.appearance = new RawProtector.Appearance());
        this.prepareMonsterTargetsPart(protector, this.m_rawProtector.monsterTargets = new RawProtector.MonsterTargets());
        this.prepareResourceTargetsPart(protector, this.m_rawProtector.resourceTargets = new RawProtector.ResourceTargets());
        return this.serializeCurrent();
    }
    
    public P fromBuild(final ByteBuffer serial) {
        if (this.m_protectorFactory == null) {
            throw new UnsupportedOperationException("La factory de protecteur doit \u00eatre d\u00e9finie");
        }
        final P protector = this.m_protectorFactory.createProtector(-1);
        if (protector != null) {
            this.unserialize(serial, protector);
        }
        return protector;
    }
    
    public int extractProtectorId(final ByteBuffer serial) {
        this.m_rawProtector.clear();
        this.m_rawProtector.unserialize(serial);
        return this.m_rawProtector.protectorId;
    }
    
    public void unserialize(final ByteBuffer serial, final P protector) {
        this.m_rawProtector.clear();
        this.m_rawProtector.unserialize(serial);
        protector.setId(this.m_rawProtector.protectorId);
        final RawProtector.Nationality nationality = this.m_rawProtector.nationality;
        if (nationality != null) {
            final Nation nativeNation = NationManager.INSTANCE.getNationById(nationality.nativeNationId);
            final Nation currentNation = NationManager.INSTANCE.getNationById(nationality.currentNationId);
            final AbstractTerritory territory = TerritoryManager.INSTANCE.getTerritory(nationality.territoryId);
            protector.setNativeNation(nativeNation);
            protector.setCurrentNation(currentNation);
            protector.setTerritory(territory);
            this.onNationalityPartChanged(protector, nationality);
        }
        final RawProtector.Appearance appearance = this.m_rawProtector.appearance;
        if (appearance != null) {
            try {
                this.onAppearancePartChanged(protector, appearance);
            }
            catch (Exception e) {
                ProtectorSerializer.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
        final RawProtector.Challenges challenges = this.m_rawProtector.challenges;
        if (challenges != null) {
            try {
                this.onChallengePartChanged(protector, challenges);
            }
            catch (Exception e2) {
                ProtectorSerializer.m_logger.error((Object)"Exception", (Throwable)e2);
            }
        }
        final RawProtector.ReferenceMerchantInventories referenceMerchantInventories = this.m_rawProtector.referenceMerchantInventories;
        if (referenceMerchantInventories != null) {
            try {
                this.onReferenceInventoriesPartChanged(protector, referenceMerchantInventories);
            }
            catch (Exception e3) {
                ProtectorSerializer.m_logger.error((Object)"Exception", (Throwable)e3);
            }
        }
        final RawProtector.NationMerchantInventories inventories = this.m_rawProtector.nationMerchantInventories;
        if (inventories != null) {
            try {
                this.onMerchantInventoriesPartChanged(protector, inventories);
            }
            catch (Exception e4) {
                ProtectorSerializer.m_logger.error((Object)"Exception", (Throwable)e4);
            }
        }
        final RawProtector.Wallet wallet = this.m_rawProtector.wallet;
        if (wallet != null) {
            try {
                this.onWalletPartChanged(protector, wallet);
            }
            catch (Exception e5) {
                ProtectorSerializer.m_logger.error((Object)"Exception", (Throwable)e5);
            }
        }
        final RawProtector.Stake stake = this.m_rawProtector.stake;
        if (stake != null) {
            try {
                this.onStakePartChange(protector, stake);
            }
            catch (Exception e6) {
                ProtectorSerializer.m_logger.error((Object)"Exception levee", (Throwable)e6);
            }
        }
        final RawProtector.Taxes taxes = this.m_rawProtector.taxes;
        if (taxes != null) {
            try {
                this.onTaxesPartChanged(protector, taxes);
            }
            catch (Exception e7) {
                ProtectorSerializer.m_logger.error((Object)"Exception", (Throwable)e7);
            }
        }
        final RawProtector.WeatherModifiers modifiers = this.m_rawProtector.weatherModifiers;
        if (modifiers != null) {
            try {
                this.onWeatherModifiersChanged(protector, modifiers);
            }
            catch (Exception e8) {
                ProtectorSerializer.m_logger.error((Object)"Exception", (Throwable)e8);
            }
        }
        final RawProtector.Satisfaction satisfaction = this.m_rawProtector.satisfaction;
        if (satisfaction != null) {
            try {
                this.onSatisfactionChanged(protector, satisfaction);
            }
            catch (Exception e9) {
                ProtectorSerializer.m_logger.error((Object)"Exception", (Throwable)e9);
            }
        }
        final RawProtector.MonsterTargets monsterTargets = this.m_rawProtector.monsterTargets;
        if (monsterTargets != null) {
            try {
                this.onMonsterTargetsChanged(protector, monsterTargets);
            }
            catch (Exception e10) {
                ProtectorSerializer.m_logger.error((Object)"Exception", (Throwable)e10);
            }
        }
        final RawProtector.ResourceTargets resourceTargets = this.m_rawProtector.resourceTargets;
        if (resourceTargets != null) {
            try {
                this.onResourceTargetsChanged(protector, resourceTargets);
            }
            catch (Exception e11) {
                ProtectorSerializer.m_logger.error((Object)"Exception", (Throwable)e11);
            }
        }
        final RawProtector.Ecosystem ecosystem = this.m_rawProtector.ecosystem;
        if (ecosystem != null) {
            try {
                this.onEcosystemPartChanged(protector, ecosystem);
            }
            catch (Exception e12) {
                ProtectorSerializer.m_logger.error((Object)"Exception", (Throwable)e12);
            }
        }
        this.onProtectorUnserialized(protector);
    }
    
    public abstract void prepareStakePart(final P p0, final RawProtector.Stake p1);
    
    public abstract void onStakePartChange(final P p0, final RawProtector.Stake p1);
    
    public abstract void prepareNationalityPart(final P p0, final RawProtector.Nationality p1);
    
    public abstract void onNationalityPartChanged(final P p0, final RawProtector.Nationality p1);
    
    public abstract void prepareChallengesPart(final P p0, final RawProtector.Challenges p1);
    
    public abstract void onChallengePartChanged(final P p0, final RawProtector.Challenges p1);
    
    public abstract void prepareAppearancePart(final P p0, final RawProtector.Appearance p1);
    
    public abstract void onAppearancePartChanged(final P p0, final RawProtector.Appearance p1);
    
    public abstract void prepareReferenceInventoriesPart(final P p0, final RawProtector.ReferenceMerchantInventories p1);
    
    public abstract void onReferenceInventoriesPartChanged(final P p0, final RawProtector.ReferenceMerchantInventories p1);
    
    public abstract void prepareMerchantInventoriesPart(final P p0, final RawProtector.NationMerchantInventories p1);
    
    public abstract void onMerchantInventoriesPartChanged(final P p0, final RawProtector.NationMerchantInventories p1);
    
    public abstract void prepareWalletPart(final P p0, final RawProtector.Wallet p1);
    
    public abstract void onWalletPartChanged(final P p0, final RawProtector.Wallet p1);
    
    public abstract void prepareTaxesPart(final P p0, final RawProtector.Taxes p1);
    
    public abstract void onTaxesPartChanged(final P p0, final RawProtector.Taxes p1);
    
    public abstract void prepareWeatherModifiersPart(final P p0, final RawProtector.WeatherModifiers p1);
    
    public abstract void onWeatherModifiersChanged(final P p0, final RawProtector.WeatherModifiers p1);
    
    protected abstract void prepareResourceTargetsPart(final P p0, final RawProtector.ResourceTargets p1);
    
    protected abstract void onResourceTargetsChanged(final P p0, final RawProtector.ResourceTargets p1);
    
    protected abstract void prepareMonsterTargetsPart(final P p0, final RawProtector.MonsterTargets p1);
    
    protected abstract void onMonsterTargetsChanged(final P p0, final RawProtector.MonsterTargets p1);
    
    protected abstract void onSatisfactionChanged(final P p0, final RawProtector.Satisfaction p1);
    
    protected abstract void prepareSatisfactionPart(final P p0, final RawProtector.Satisfaction p1);
    
    protected abstract void onEcosystemPartChanged(final P p0, final RawProtector.Ecosystem p1);
    
    protected abstract void prepareEcosystemPart(final P p0, final RawProtector.Ecosystem p1);
    
    protected abstract void onProtectorUnserialized(final P p0);
    
    static {
        m_logger = Logger.getLogger((Class)ProtectorSerializer.class);
    }
}
