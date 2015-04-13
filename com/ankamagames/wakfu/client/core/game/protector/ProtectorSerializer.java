package com.ankamagames.wakfu.client.core.game.protector;

import org.apache.log4j.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.protector.inventory.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.common.game.tax.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.climate.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.climate.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.loot.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.common.game.protector.*;

public class ProtectorSerializer extends com.ankamagames.wakfu.common.game.protector.ProtectorSerializer<Protector>
{
    public static final ProtectorSerializer INSTANCE;
    protected static final Logger m_logger;
    
    @Override
    public void prepareNationalityPart(final Protector protector, final RawProtector.Nationality nationality) {
    }
    
    @Override
    public void onNationalityPartChanged(final Protector protector, final RawProtector.Nationality nationality) {
    }
    
    @Override
    public void prepareChallengesPart(final Protector protector, final RawProtector.Challenges challenges) {
    }
    
    @Override
    public void onChallengePartChanged(final Protector protector, final RawProtector.Challenges challenges) {
    }
    
    @Override
    public void prepareAppearancePart(final Protector protector, final RawProtector.Appearance appearance) {
    }
    
    @Override
    public void onAppearancePartChanged(final Protector protector, final RawProtector.Appearance appearance) {
        protector.setNpcId(appearance.monterId);
    }
    
    @Override
    public void prepareReferenceInventoriesPart(final Protector protector, final RawProtector.ReferenceMerchantInventories inventories) {
    }
    
    @Override
    public void onReferenceInventoriesPartChanged(final Protector protector, final RawProtector.ReferenceMerchantInventories inventories) {
        final TIntArrayList knownChallengeList = protector.getKnownChallengeList();
        knownChallengeList.clear();
        final ArrayList<RawProtectorReferenceInventory.Content> knownChallenges = inventories.challengeReferenceInventory.contents;
        for (int i = 0; i < knownChallenges.size(); ++i) {
            final RawProtectorReferenceInventory.Content content = knownChallenges.get(i);
            if (content.remainingDuration != Integer.MAX_VALUE) {
                knownChallengeList.add(content.referenceId);
            }
        }
        final RawProtectorReferenceInventory.ContentsSelection contentsSelection = inventories.challengeReferenceInventory.contentsSelection;
        final ArrayList<RawProtectorReferenceInventory.ContentsSelection.ContentSelection> selectedChallenges = (contentsSelection != null) ? contentsSelection.contentsSelection : null;
        protector.getSelectedChallengeList().clear();
        if (selectedChallenges == null) {
            protector.getSelectedChallengeList().ensureCapacity(knownChallengeList.size());
            for (int j = 0, size = knownChallengeList.size(); j < size; ++j) {
                protector.getSelectedChallengeList().add(knownChallengeList.get(j));
            }
        }
        else {
            for (int j = 0, size = selectedChallenges.size(); j < size; ++j) {
                protector.getSelectedChallengeList().add(selectedChallenges.get(j).referenceId);
            }
        }
        final ArrayList<RawProtectorReferenceInventory.Content> knownBuffs = inventories.buffsReferenceInventory.contents;
        final TIntArrayList buffs = protector.getKnownBuffsList();
        buffs.clear();
        for (int k = 0, size2 = knownBuffs.size(); k < size2; ++k) {
            buffs.add(knownBuffs.get(k).referenceId);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(ProtectorView.getInstance(), "boughtChallenges");
    }
    
    @Override
    public void prepareMerchantInventoriesPart(final Protector protector, final RawProtector.NationMerchantInventories inventories) {
    }
    
    @Override
    public void onMerchantInventoriesPartChanged(final Protector protector, final RawProtector.NationMerchantInventories inventories) {
        ProtectorMerchantInventory challengeMerchantInventory = protector.getChallengeMerchantInventory();
        if (challengeMerchantInventory == null) {
            challengeMerchantInventory = new ProtectorMerchantInventory(inventories.challengeMerchantInventory.uid, ProtectorMerchantInventoryItemProvider.INSTANCE, MerchantItemType.PROTECTOR_CHALLENGES, (short)99, (byte)1, false, ProtectorWalletContext.CHALLENGE);
            protector.setChallengeMerchantInventory(challengeMerchantInventory);
        }
        challengeMerchantInventory.fromRaw(inventories.challengeMerchantInventory);
        protector.updateChallengeMerchantInventoryView();
        ProtectorMerchantInventory buffMerchantInventory = protector.getBuffMerchantInventory();
        if (buffMerchantInventory == null) {
            buffMerchantInventory = new ProtectorMerchantInventory(inventories.buffsMerchantInventory.uid, ProtectorMerchantInventoryItemProvider.INSTANCE, MerchantItemType.PROTECTOR_BUFFS, (short)99, (byte)1, false, ProtectorWalletContext.BUFF);
            protector.setBuffMerchantInventory(buffMerchantInventory);
        }
        buffMerchantInventory.fromRaw(inventories.buffsMerchantInventory);
        ProtectorMerchantInventory climateMerchantInventory = protector.getClimateMerchantInventory();
        if (climateMerchantInventory == null) {
            climateMerchantInventory = new ProtectorMerchantInventory(inventories.climateMerchantInventory.uid, ProtectorMerchantInventoryItemProvider.INSTANCE, MerchantItemType.PROTECTOR_CLIMATE_BUFFS, (short)99, (byte)1, true, ProtectorWalletContext.CLIMATE);
            protector.setClimateMerchantInventory(climateMerchantInventory);
        }
        climateMerchantInventory.fromRaw(inventories.climateMerchantInventory);
        ProtectorSerializer.m_logger.info((Object)("#### MerchantInventory (challenges) du protecteur id=" + protector.getId()));
        for (final ProtectorMerchantInventoryItem item : challengeMerchantInventory) {
            ProtectorSerializer.m_logger.info((Object)("####   refId=" + item.getReferenceId() + " price=" + item.getPrice() + " type=" + item.getType().name() + " featureReferenceId=" + item.getFeatureReferenceId()));
        }
        ProtectorSerializer.m_logger.info((Object)("#### MerchantInventory (buffs) du protecteur id=" + protector.getId()));
        for (final ProtectorMerchantInventoryItem item : buffMerchantInventory) {
            ProtectorSerializer.m_logger.info((Object)("####   refId=" + item.getReferenceId() + " price=" + item.getPrice() + " type=" + item.getType().name() + " featureReferenceId=" + item.getFeatureReferenceId()));
        }
        ProtectorSerializer.m_logger.info((Object)("#### MerchantInventory (buffs de climat) du protecteur id=" + protector.getId()));
        for (final ProtectorMerchantInventoryItem item : climateMerchantInventory) {
            ProtectorSerializer.m_logger.info((Object)("####   refId=" + item.getReferenceId() + " price=" + item.getPrice() + " type=" + item.getType().name() + " featureReferenceId=" + item.getFeatureReferenceId()));
        }
    }
    
    @Override
    public void prepareWalletPart(final Protector protector, final RawProtector.Wallet wallet) {
    }
    
    @Override
    public void onWalletPartChanged(final Protector protector, final RawProtector.Wallet walletPart) {
        ProtectorWalletHandler walletHandler = (ProtectorWalletHandler)protector.getWallet();
        if (walletHandler == null) {
            protector.setWallet(walletHandler = new ProtectorWalletHandler(protector, ProtectorMerchantClient.FACTORY));
        }
        walletHandler.onWalletPartChanged(walletPart);
        ProtectorSerializer.m_logger.info((Object)("#### Wallet du protecteur id=" + protector.getId() + " : cashAmount=" + walletHandler.getAmountOfCash()));
    }
    
    @Override
    public void prepareStakePart(final Protector protector, final RawProtector.Stake stake) {
    }
    
    @Override
    public void onStakePartChange(final Protector protector, final RawProtector.Stake stake) {
    }
    
    @Override
    public void prepareTaxesPart(final Protector protector, final RawProtector.Taxes taxes) {
    }
    
    @Override
    public void onTaxesPartChanged(final Protector protector, final RawProtector.Taxes taxes) {
        final EnumMap<TaxContext, Tax> protectorTaxes = protector.getTaxes();
        final ArrayList<RawProtector.Taxes.TaxValue> taxValues = taxes.taxValues;
        for (int i = 0, size = taxValues.size(); i < size; ++i) {
            final RawTax tax = taxValues.get(i).tax;
            final TaxContext taxContext = TaxContext.getById(tax.taxContext);
            protectorTaxes.put(taxContext, new Tax(taxContext, tax.taxValue));
        }
    }
    
    @Override
    public void prepareWeatherModifiersPart(final Protector protector, final RawProtector.WeatherModifiers modifiers) {
    }
    
    @Override
    public void onWeatherModifiersChanged(final Protector protector, final RawProtector.WeatherModifiers weatherModifiers) {
        final ArrayList<RawProtector.WeatherModifiers.WeatherModifier> wMods = weatherModifiers.modifiers;
        protector.getCurrentWeatherModifiers().clear();
        for (int i = 0, size = wMods.size(); i < size; ++i) {
            final RawProtector.WeatherModifiers.WeatherModifier wMod = wMods.get(i);
            final GameDateConst date = GameDate.fromLong(wMod.applicationDate);
            final ClimateBonus bonus = ClimateBonusManager.INSTANCE.getBonus(wMod.climateBonusId);
            if (bonus != null) {
                final WeatherModifier mod = new WeatherModifier(bonus, date);
                protector.getCurrentWeatherModifiers().add(mod);
            }
            else {
                ProtectorSerializer.m_logger.error((Object)("Modificateur m\u00e9t\u00e9o ignor\u00e9 -- pas/plus de bonus de climat d'ID=" + wMod.climateBonusId + ", pour le protecteur ID=" + protector.getId()));
            }
        }
    }
    
    @Override
    protected void prepareResourceTargetsPart(final Protector protector, final RawProtector.ResourceTargets resourceTargets) {
    }
    
    @Override
    protected void onResourceTargetsChanged(final Protector protector, final RawProtector.ResourceTargets resourceTargets) {
        final ProtectorSatisfactionManager satisfactionManager = protector.getSatisfactionManager();
        final ArrayList<RawProtector.ResourceTargets.ResourceTarget> targets = resourceTargets.targets;
        for (int i = 0, size = targets.size(); i < size; ++i) {
            final RawProtector.ResourceTargets.ResourceTarget target = targets.get(i);
            satisfactionManager.addResourceTarget(target.target.referenceId, new Interval(target.target.min, target.target.max));
        }
    }
    
    @Override
    protected void prepareMonsterTargetsPart(final Protector protector, final RawProtector.MonsterTargets monsterTargets) {
    }
    
    @Override
    protected void onMonsterTargetsChanged(final Protector protector, final RawProtector.MonsterTargets monsterTargets) {
        final ProtectorSatisfactionManager satisfactionManager = protector.getSatisfactionManager();
        final ArrayList<RawProtector.MonsterTargets.MonsterTarget> targets = monsterTargets.targets;
        for (int i = 0, size = targets.size(); i < size; ++i) {
            final RawProtector.MonsterTargets.MonsterTarget target = targets.get(i);
            satisfactionManager.addMonsterTarget(target.target.referenceId, new Interval(target.target.min, target.target.max));
        }
    }
    
    @Override
    protected void onSatisfactionChanged(final Protector protector, final RawProtector.Satisfaction satisfaction) {
        final ProtectorSatisfactionLevel protectorSatisfactionLevel = ProtectorSatisfactionLevel.fromId(satisfaction.satisfactionLevel);
        if (protectorSatisfactionLevel != null) {
            protector.getSatisfactionManager().setGlobalSatisfaction(protectorSatisfactionLevel);
        }
        else {
            ProtectorSerializer.m_logger.error((Object)("Erreur \u00e0 la d\u00e9serialisation de la satisfaction du protecteur id=" + protector.getId() + " niveau de satisfaction id=" + satisfaction.satisfactionLevel + " inexistant"));
        }
    }
    
    @Override
    protected void prepareSatisfactionPart(final Protector protector, final RawProtector.Satisfaction satisfaction) {
    }
    
    @Override
    protected void onEcosystemPartChanged(final Protector protector, final RawProtector.Ecosystem ecosystem) {
        final ProtectorEcosystemHandler ecosystemHandler = protector.getEcosystemHandler();
        final ArrayList<RawProtector.Ecosystem.MonsterFamily> protectedMonsterFamilies = ecosystem.protectedMonsters;
        for (int i = 0, size = protectedMonsterFamilies.size(); i < size; ++i) {
            ecosystemHandler.protectMonsterFamily(protectedMonsterFamilies.get(i).familyId);
        }
        final ArrayList<RawProtector.Ecosystem.ResourceFamily> protectedResourceFamilies = ecosystem.protectedResources;
        for (int j = 0, size2 = protectedResourceFamilies.size(); j < size2; ++j) {
            ecosystemHandler.protectResourceFamily(protectedResourceFamilies.get(j).familyId);
        }
    }
    
    @Override
    protected void prepareEcosystemPart(final Protector protector, final RawProtector.Ecosystem ecosystem) {
    }
    
    @Override
    protected void onProtectorUnserialized(final Protector protector) {
        try {
            final ProtectorBinaryData protectorData = new ProtectorBinaryData();
            if (!BinaryDocumentManager.getInstance().getId(protector.getId(), protectorData)) {
                ProtectorSerializer.m_logger.error((Object)("le protector " + protector.getId() + " non trouv\u00e9"));
                return;
            }
            final TIntArrayList baseChallengeList = protector.getBaseChallengeList();
            baseChallengeList.clear();
            final ChallengeLootListBinaryData challengeLootListData = new ChallengeLootListBinaryData();
            if (!BinaryDocumentManager.getInstance().getId(protectorData.getScenarioLootListId(), challengeLootListData)) {
                return;
            }
            final DropTable<ChallengeDrop> dropTable = new DropTable<ChallengeDrop>();
            for (final ChallengeLootListBinaryData.ChallengeLootEntry entry : challengeLootListData.getEntries()) {
                final ChallengeDrop drop = new ChallengeDrop(entry.getChallengeId(), (short)1, CriteriaCompiler.compileBoolean(entry.getCriteria()));
                dropTable.addDrop(drop);
                baseChallengeList.add(entry.getChallengeId());
            }
            protector.setBaseChallengeDropTable(dropTable);
        }
        catch (Exception e) {
            ProtectorSerializer.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    static {
        INSTANCE = new ProtectorSerializer();
        m_logger = Logger.getLogger((Class)ProtectorSerializer.class);
    }
}
