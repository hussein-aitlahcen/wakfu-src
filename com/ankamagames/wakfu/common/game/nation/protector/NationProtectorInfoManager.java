package com.ankamagames.wakfu.common.game.nation.protector;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.tax.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.nation.*;
import gnu.trove.*;

public class NationProtectorInfoManager
{
    private static final Logger m_logger;
    private static final TIntIntHashMap PROTECTOR_NATIONS;
    private final TIntObjectHashMap<NationProtectorInfo> m_protectors;
    private final NationEconomyHandler m_economyHandler;
    
    public NationProtectorInfoManager(final Nation nation) {
        super();
        this.m_protectors = new TIntObjectHashMap<NationProtectorInfo>();
        this.m_economyHandler = new NationEconomyHandler(nation);
    }
    
    private void registerProtector(final NationProtectorInfo protector) {
        this.m_protectors.put(protector.getId(), protector);
        this.m_economyHandler.adjustTotalCash(protector.getCash());
        NationProtectorInfoManager.PROTECTOR_NATIONS.put(protector.getId(), protector.getNationId());
        protector.getNation().onProtectorAcquired(protector.getId());
    }
    
    private void removeProtector(final NationProtectorInfo protector) {
        this.m_protectors.remove(protector.getId());
        this.m_economyHandler.adjustTotalCash(-protector.getCash());
        NationProtectorInfoManager.PROTECTOR_NATIONS.remove(protector.getId());
    }
    
    private void onMerchantItemSold(final int protectorId, final ProtectorMerchantItemType itemType, final int itemPrice) {
        this.m_economyHandler.onMerchantItemSold(itemType, itemPrice);
    }
    
    private void onCheckInTax(final int protectorId, final TaxContext taxContext, final int taxAmount) {
        this.m_economyHandler.onCheckInTax(taxContext, taxAmount);
    }
    
    private void onWalletUpdate(final int protectorId, final int deltaAmount) {
        this.m_protectors.get(protectorId).adjustCash(deltaAmount);
        this.m_economyHandler.adjustTotalCash(deltaAmount);
    }
    
    private void onChaosUpdate(final int protectorId, final boolean inChaos) {
        this.m_protectors.get(protectorId).setInChaos(inChaos);
    }
    
    private void onSatisfactionChanged(final int protectorId, final int satisfaction) {
        final NationProtectorInfo protectorInfo = this.m_protectors.get(protectorId);
        if (protectorInfo == null) {
            NationProtectorInfoManager.m_logger.error((Object)("Mise a jour de la satisfaction d'un protecteur inconnu " + protectorId));
            return;
        }
        protectorInfo.setSatisfaction(satisfaction);
    }
    
    private void onTaxChanged(final int protectorId, final TaxContext taxContext, final float taxValue) {
        this.m_protectors.get(protectorId).setTaxValue(taxContext, taxValue);
    }
    
    private void onNationChanged(final int protectorId, final Nation newNation) {
        final NationProtectorInfo protector = this.m_protectors.get(protectorId);
        this.removeProtector(protector);
        protector.setNation(newNation);
        newNation.getProtectorInfoManager().registerProtector(protector);
    }
    
    public boolean hasProtector(final int protectorId) {
        return this.m_protectors.containsKey(protectorId);
    }
    
    public NationProtectorInfo getProtectorInfo(final int protectorId) {
        return this.m_protectors.get(protectorId);
    }
    
    public TIntObjectIterator<NationProtectorInfo> getIterator() {
        return this.m_protectors.iterator();
    }
    
    public int size() {
        return this.m_protectors.size();
    }
    
    public NationEconomyHandler getEconomyHandler() {
        return this.m_economyHandler;
    }
    
    public int getProtectorsSize() {
        return this.m_protectors.size();
    }
    
    public void clearProtectors() {
        final Object[] protectors = this.m_protectors.getValues();
        for (int i = 0; i < protectors.length; ++i) {
            this.removeProtector((NationProtectorInfo)protectors[i]);
        }
    }
    
    public void serializeProtectors(final ByteBuffer buffer) {
        buffer.putInt(this.m_protectors.size());
        final TIntObjectIterator<NationProtectorInfo> it = this.m_protectors.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().serialize(buffer);
        }
    }
    
    public void unserializeProtectors(final ByteBuffer buffer) {
        for (int size = buffer.getInt(), i = 0; i < size; ++i) {
            this.registerProtector(NationProtectorInfo.fromBuild(buffer));
        }
    }
    
    public int serializedProtectorSize() {
        return 4 + this.m_protectors.size() * NationProtectorInfo.serializedSize();
    }
    
    public static void registerNewProtector(final NationProtectorInfo info) {
        final int nationId = info.getNationId();
        final int protectorId = info.getId();
        final Nation nation = NationManager.INSTANCE.getNationById(nationId);
        if (nation == null) {
            NationProtectorInfoManager.m_logger.error((Object)("Nation " + nationId + " inconnue pour le protecteur " + protectorId));
            return;
        }
        final NationProtectorInfoManager manager = nation.getProtectorInfoManager();
        final NationProtectorInfo oldProtector = manager.m_protectors.get(protectorId);
        if (oldProtector != null) {
            NationProtectorInfoManager.m_logger.warn((Object)("Demande de remplacement des informations du protecteur " + oldProtector));
            manager.removeProtector(oldProtector);
            return;
        }
        NationProtectorInfoManager.m_logger.info((Object)("Enregistrement du protecteur " + info));
        manager.registerProtector(info);
    }
    
    public static void registerNewProtector(final int protectorId, final int nationId, final boolean inChaos, final int cash, final float fleaTaxValue, final float marketTaxValue, final int currentSatisfaction, final int totalSatisfaction, final String idString) {
        final Nation nation = NationManager.INSTANCE.getNationById(nationId);
        if (nation == null) {
            NationProtectorInfoManager.m_logger.error((Object)("Nation " + nationId + " inconnue pour le protecteur " + protectorId));
            return;
        }
        final NationProtectorInfoManager manager = nation.getProtectorInfoManager();
        final NationProtectorInfo oldProtector = manager.m_protectors.get(protectorId);
        if (oldProtector != null) {
            NationProtectorInfoManager.m_logger.warn((Object)("Demande de remplacement des informations du protecteur " + oldProtector));
            manager.removeProtector(oldProtector);
            return;
        }
        final NationProtectorInfo protector = new NationProtectorInfo(protectorId, nation, inChaos, cash, fleaTaxValue, marketTaxValue, currentSatisfaction, totalSatisfaction, idString);
        NationProtectorInfoManager.m_logger.info((Object)("Enregistrement du protecteur " + protector));
        manager.registerProtector(protector);
    }
    
    public static void updateProtectorMerchantItemSold(final int protectorId, final ProtectorMerchantItemType itemType, final int itemPrice) {
        final int nationId = NationProtectorInfoManager.PROTECTOR_NATIONS.get(protectorId);
        final Nation nation = NationManager.INSTANCE.getNationById(nationId);
        if (nation == null) {
            NationProtectorInfoManager.m_logger.error((Object)("Nation " + nationId + " inconnue pour le protecteur " + protectorId));
            return;
        }
        final NationProtectorInfoManager manager = nation.getProtectorInfoManager();
        NationProtectorInfoManager.m_logger.info((Object)("Achat d'un item de type " + itemType + " au prix de " + itemPrice + " sur le protecteur " + protectorId));
        manager.onMerchantItemSold(protectorId, itemType, itemPrice);
    }
    
    public static void updateProtectorCheckInTax(final int protectorId, final TaxContext taxContext, final int taxAmount) {
        final int nationId = NationProtectorInfoManager.PROTECTOR_NATIONS.get(protectorId);
        final Nation nation = NationManager.INSTANCE.getNationById(nationId);
        if (nation == null) {
            NationProtectorInfoManager.m_logger.error((Object)("Nation " + nationId + " inconnue pour le protecteur " + protectorId));
            return;
        }
        final NationProtectorInfoManager manager = nation.getProtectorInfoManager();
        NationProtectorInfoManager.m_logger.info((Object)("Taxes de type " + taxContext + " d'un montant de " + taxAmount + " pr\u00e9lev\u00e9es pour le protecteur " + protectorId));
        manager.onCheckInTax(protectorId, taxContext, taxAmount);
    }
    
    public static void updateProtectorWallet(final int protectorId, final int deltaAmount) {
        final int nationId = NationProtectorInfoManager.PROTECTOR_NATIONS.get(protectorId);
        final Nation nation = NationManager.INSTANCE.getNationById(nationId);
        if (nation == null) {
            NationProtectorInfoManager.m_logger.error((Object)("Nation " + nationId + " inconnue pour le protecteur " + protectorId));
            return;
        }
        final NationProtectorInfoManager manager = nation.getProtectorInfoManager();
        NationProtectorInfoManager.m_logger.info((Object)("Update de wallet du protecteur " + protectorId + " pour un montant de " + deltaAmount));
        manager.onWalletUpdate(protectorId, deltaAmount);
    }
    
    public static void updateProtectorChaos(final int protectorId, final boolean inChaos) {
        final int nationId = NationProtectorInfoManager.PROTECTOR_NATIONS.get(protectorId);
        final Nation nation = NationManager.INSTANCE.getNationById(nationId);
        if (nation == null) {
            NationProtectorInfoManager.m_logger.error((Object)("Nation " + nationId + " inconnue pour le protecteur " + protectorId));
            return;
        }
        final NationProtectorInfoManager manager = nation.getProtectorInfoManager();
        NationProtectorInfoManager.m_logger.info((Object)("Update de chaos du protecteur " + protectorId + ", chaos = " + inChaos));
        manager.onChaosUpdate(protectorId, inChaos);
    }
    
    public static void updateProtectorSatisfaction(final int protectorId, final int satisfaction) {
        final int nationId = NationProtectorInfoManager.PROTECTOR_NATIONS.get(protectorId);
        final Nation nation = NationManager.INSTANCE.getNationById(nationId);
        if (nation == null) {
            NationProtectorInfoManager.m_logger.error((Object)("Nation " + nationId + " inconnue pour le protecteur " + protectorId));
            return;
        }
        final NationProtectorInfoManager manager = nation.getProtectorInfoManager();
        NationProtectorInfoManager.m_logger.info((Object)("Update de la satisfaction du protecteur " + protectorId + ". Nouvelle satisfaction : " + satisfaction));
        manager.onSatisfactionChanged(protectorId, satisfaction);
    }
    
    public static void updateProtectorTax(final int protectorId, final TaxContext taxContext, final float taxValue) {
        final int nationId = NationProtectorInfoManager.PROTECTOR_NATIONS.get(protectorId);
        final Nation nation = NationManager.INSTANCE.getNationById(nationId);
        if (nation == null) {
            NationProtectorInfoManager.m_logger.error((Object)("Nation " + nationId + " inconnue pour le protecteur " + protectorId));
            return;
        }
        final NationProtectorInfoManager manager = nation.getProtectorInfoManager();
        NationProtectorInfoManager.m_logger.info((Object)("Update de la valeur de taxe " + taxContext + " \u00e0 une valeur de " + taxValue + " pour le protecteur " + protectorId));
        manager.onTaxChanged(protectorId, taxContext, taxValue);
    }
    
    public static void updateProtectorNation(final int protectorId, final int nationId) {
        final int oldNationId = NationProtectorInfoManager.PROTECTOR_NATIONS.get(protectorId);
        final Nation nation = NationManager.INSTANCE.getNationById(oldNationId);
        if (nation == null) {
            NationProtectorInfoManager.m_logger.error((Object)("Nation " + oldNationId + " inconnue pour le protecteur " + protectorId));
            return;
        }
        final NationProtectorInfoManager manager = nation.getProtectorInfoManager();
        NationProtectorInfoManager.m_logger.info((Object)("Update de la nation du protecteur " + protectorId + ". Nouvelle nation : " + nationId));
        manager.onNationChanged(protectorId, NationManager.INSTANCE.getNationById(nationId));
    }
    
    public static int getProtectorNation(final int protectorId) {
        return NationProtectorInfoManager.PROTECTOR_NATIONS.get(protectorId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationProtectorInfoManager.class);
        PROTECTOR_NATIONS = new TIntIntHashMap();
    }
}
