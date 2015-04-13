package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.wakfu.client.core.game.protector.inventory.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.climate.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.tax.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.loot.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.protector.*;
import java.util.*;

public final class Protector extends ProtectorBase implements TaxHandler
{
    private long m_npcId;
    private ProtectorWalletHandler m_walletHandler;
    private ProtectorMerchantInventory m_challengeMerchantInventory;
    private ProtectorMerchantInventory m_buffMerchantInventory;
    private ProtectorMerchantInventory m_climateMerchantInventory;
    private ProtectorChallengeMerchantInventoryView m_challengeMerchantInventoryView;
    private final ArrayList<WeatherModifier> m_currentWeatherModifiers;
    private final TIntArrayList m_baseChallengeList;
    private final TIntArrayList m_knownChallengeList;
    private final TIntArrayList m_selectedChallengeList;
    private final TIntArrayList m_knownBuffsList;
    private final ArrayList<ProtectorSecret> m_secrets;
    private final EnumMap<TaxContext, Tax> m_taxes;
    private final ProtectorSatisfactionManager m_satisfactionManager;
    private final ProtectorEcosystemHandler m_ecosystemHandler;
    private Point3 m_position;
    private int[] m_craft;
    private DropTable<ChallengeDrop> m_baseChallengeDropTable;
    
    Protector(final int id) {
        super(id);
        this.m_challengeMerchantInventoryView = null;
        this.m_position = new Point3();
        this.m_craft = null;
        this.m_taxes = new EnumMap<TaxContext, Tax>(TaxContext.class);
        this.m_currentWeatherModifiers = new ArrayList<WeatherModifier>();
        this.m_knownChallengeList = new TIntArrayList();
        this.m_selectedChallengeList = new TIntArrayList();
        this.m_satisfactionManager = new ProtectorSatisfactionManager(this);
        this.m_secrets = new ArrayList<ProtectorSecret>();
        this.m_knownBuffsList = new TIntArrayList();
        this.m_baseChallengeList = new TIntArrayList();
        this.m_ecosystemHandler = new ProtectorEcosystemHandler(this);
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(48, this.getId(), new Object[0]);
    }
    
    public long getNpcId() {
        return this.m_npcId;
    }
    
    public void setNpcId(final long npcId) {
        this.m_npcId = npcId;
    }
    
    public Point3 getPositionConst() {
        return this.m_position;
    }
    
    public void setPosition(final int x, final int y, final short z) {
        this.m_position.set(x, y, z);
    }
    
    public int[] getCraft() {
        return this.m_craft;
    }
    
    public void setCraft(final int[] craft) {
        this.m_craft = craft;
    }
    
    public ProtectorBuff[] getNationBuffs() {
        final Nation currentNation = this.getCurrentNation();
        if (currentNation != null) {
            final IntArray protectorBuffs = currentNation.getProtectorBuffs(this.getId());
            if (protectorBuffs != null) {
                final ProtectorBuff[] buffs = new ProtectorBuff[protectorBuffs.size()];
                for (int i = 0, size = protectorBuffs.size(); i < size; ++i) {
                    final int buffId = protectorBuffs.getQuick(i);
                    buffs[i] = (ProtectorBuff)ProtectorBuffManager.INSTANCE.getBuff(buffId);
                }
                return buffs;
            }
        }
        return new ProtectorBuff[0];
    }
    
    public ProtectorMerchantInventory getChallengeMerchantInventory() {
        return this.m_challengeMerchantInventory;
    }
    
    public void setChallengeMerchantInventory(final ProtectorMerchantInventory challengeMerchantInventory) {
        this.m_challengeMerchantInventory = challengeMerchantInventory;
        this.m_challengeMerchantInventoryView = new ProtectorChallengeMerchantInventoryView(challengeMerchantInventory);
        this.m_challengeMerchantInventory.addMerchantEventListener(new MerchantInventoryEventListener<MerchantClient>() {
            @Override
            public void onMerchantItemSold(final MerchantClient merchantClient, final AbstractMerchantInventoryItem merchantItem) {
                Protector.this.m_challengeMerchantInventoryView.update();
            }
        });
    }
    
    public void updateChallengeMerchantInventoryView() {
        if (this.m_challengeMerchantInventoryView != null) {
            this.m_challengeMerchantInventoryView.update();
        }
    }
    
    public void updateChallengeMerchantInventoryTime() {
        if (this.m_challengeMerchantInventoryView != null) {
            this.m_challengeMerchantInventoryView.updateTime();
        }
    }
    
    public ProtectorChallengeMerchantInventoryView getChallengeMerchantInventoryView() {
        return this.m_challengeMerchantInventoryView;
    }
    
    public ProtectorWalletHandler getWallet() {
        return this.m_walletHandler;
    }
    
    public void setWallet(final ProtectorWalletHandler walletHandler) {
        this.m_walletHandler = walletHandler;
    }
    
    public ProtectorMerchantInventory getBuffMerchantInventory() {
        return this.m_buffMerchantInventory;
    }
    
    public void setBuffMerchantInventory(final ProtectorMerchantInventory buffMerchantInventory) {
        this.m_buffMerchantInventory = buffMerchantInventory;
    }
    
    public NonPlayerCharacter getNpc() {
        final CharacterInfo characterInfo = CharacterInfoManager.getInstance().getCharacter(this.m_npcId);
        if (characterInfo instanceof NonPlayerCharacter) {
            return (NonPlayerCharacter)characterInfo;
        }
        if (characterInfo != null) {
            Protector.m_logger.error((Object)("Un protecteur a un uid qui ne correspond pas \u00e0 celui d'un NPC uid=" + this.m_npcId));
        }
        return null;
    }
    
    public String getTerritoryName() {
        final Territory territory = (Territory)this.getTerritory();
        if (territory == null) {
            return null;
        }
        if (WakfuTranslator.getInstance().containsContentKey(66, territory.getId())) {
            final String territoryName = WakfuTranslator.getInstance().getString(66, territory.getId(), new Object[0]);
            if (territoryName != null && territoryName.length() > 0) {
                return territoryName;
            }
        }
        return null;
    }
    
    public ProtectorMerchantInventory getClimateMerchantInventory() {
        return this.m_climateMerchantInventory;
    }
    
    public void setClimateMerchantInventory(final ProtectorMerchantInventory climateMerchantInventory) {
        this.m_climateMerchantInventory = climateMerchantInventory;
    }
    
    public ArrayList<WeatherModifier> getCurrentWeatherModifiers() {
        return this.m_currentWeatherModifiers;
    }
    
    public TIntArrayList getKnownChallengeList() {
        return this.m_knownChallengeList;
    }
    
    public TIntArrayList getBaseChallengeList() {
        return this.m_baseChallengeList;
    }
    
    public TIntArrayList getSelectedChallengeList() {
        return this.m_selectedChallengeList;
    }
    
    public TIntArrayList getKnownBuffsList() {
        return this.m_knownBuffsList;
    }
    
    public EnumMap<TaxContext, Tax> getTaxes() {
        return this.m_taxes;
    }
    
    public ProtectorSatisfactionManager getSatisfactionManager() {
        return this.m_satisfactionManager;
    }
    
    @Override
    public ProtectorSatisfactionLevel getSatisfactionLevel() {
        return this.m_satisfactionManager.getGlobalSatisfaction();
    }
    
    public void addSecret(final ProtectorSecret secret) {
        this.m_secrets.add(secret);
    }
    
    public Iterator<ProtectorSecret> getSecretsIterator() {
        return this.m_secrets.iterator();
    }
    
    public ProtectorEcosystemHandler getEcosystemHandler() {
        return this.m_ecosystemHandler;
    }
    
    @Override
    public float getTaxValue(final TaxContext context) {
        return this.m_taxes.get(context).getValue();
    }
    
    @Override
    public void setTaxes(final TaxContext context, final float value) {
        this.m_taxes.get(context).setValue(value);
    }
    
    @Override
    public int getTaxAmount(final MerchantClient taxSource, final TaxContext taxContext, final int kamasCount) {
        if (!this.mustTax(taxSource)) {
            return 0;
        }
        final Tax tax = this.m_taxes.get(taxContext);
        return (tax != null) ? tax.getTaxAmount(kamasCount) : 0;
    }
    
    @Override
    public void checkInTax(final TaxContext context, final int taxAmount) {
        throw new UnsupportedOperationException("Non support\u00e9 dnas le client");
    }
    
    @Override
    public boolean mustTax(final MerchantClient taxSource) {
        return taxSource instanceof CharacterInfo;
    }
    
    @Override
    public Point3 getPosition() {
        return this.m_position;
    }
    
    public void setBaseChallengeDropTable(final DropTable<ChallengeDrop> baseChallengeDropTable) {
        this.m_baseChallengeDropTable = baseChallengeDropTable;
    }
    
    public DropTable<ChallengeDrop> getBaseChallengeDropTable() {
        return this.m_baseChallengeDropTable;
    }
}
