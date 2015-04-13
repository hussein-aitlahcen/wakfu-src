package com.ankamagames.wakfu.client.core.protector;

import com.ankamagames.wakfu.client.core.game.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.client.core.game.protector.event.*;
import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.constants.*;
import org.apache.commons.lang3.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.buff.*;
import com.ankamagames.framework.kernel.utils.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.loot.*;
import com.ankamagames.wakfu.client.core.game.protector.inventory.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.events.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.tax.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.protector.wallet.*;
import org.jetbrains.annotations.*;

public class ProtectorView extends AbstractProtectorView implements WalletEventListener, ProtectorNationChangedListener
{
    private static final ProtectorView m_instance;
    private Protector m_currentProtector;
    public static TaxView m_fleaTaxView;
    public static TaxView m_marketTaxView;
    private AnimatedElement m_animation;
    private NationFieldProvider m_nationFieldProvider;
    private Protector m_protectorCacheForDimensionalBag;
    private final ArrayList<ProtectorMerchantItemView> m_boughtChallenges;
    
    public static ProtectorView getInstance() {
        return ProtectorView.m_instance;
    }
    
    protected ProtectorView() {
        super();
        this.m_nationFieldProvider = null;
        this.m_boughtChallenges = new ArrayList<ProtectorMerchantItemView>();
    }
    
    @Override
    public void onProtectorEvent(final ProtectorEvent e) {
        final ProtectorEvents event = ProtectorEvents.getFromId(e.getId());
        if (event == ProtectorEvents.PROTECTOR_WELCOME) {
            StaticProtectorView.INSTANCE.setProtectorId(-1);
            final Protector protector = e.getProtector();
            if (this.m_currentProtector != protector) {
                this.setCurrentProtector(protector);
            }
        }
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("animation")) {
            if (this.m_currentProtector == null) {
                return null;
            }
            return this.getAnimation(this.getProtectorId(), ProtectorMood.NEUTRAL);
        }
        else {
            if (fieldName.equals("animName")) {
                return ProtectorMood.NEUTRAL.getAnimation();
            }
            if (fieldName.equals("currentProtector")) {
                return this.m_currentProtector;
            }
            if (fieldName.equals("challengeInventory")) {
                return this.m_currentProtector.getChallengeMerchantInventoryView();
            }
            if (fieldName.equals("buffInventory")) {
                return this.getMerchantInventoryItems(this.m_currentProtector.getBuffMerchantInventory(), false);
            }
            if (fieldName.equals("climateInventory")) {
                return this.getMerchantInventoryItems(this.m_currentProtector.getClimateMerchantInventory(), false);
            }
            if (fieldName.equals("challengeObjInventory")) {
                return this.m_currentProtector.getChallengeMerchantInventory();
            }
            if (fieldName.equals("buffObjInventory")) {
                return this.m_currentProtector.getBuffMerchantInventory();
            }
            if (fieldName.equals("climateObjInventory")) {
                return this.m_currentProtector.getClimateMerchantInventory();
            }
            if (fieldName.equals("baseChallenges")) {
                final TIntArrayList baseChallengeList = this.m_currentProtector.getBaseChallengeList();
                if (baseChallengeList.size() == 0) {
                    return null;
                }
                final ArrayList<FieldProvider> views = new ArrayList<FieldProvider>();
                for (int i = 0, size = baseChallengeList.size(); i < size; ++i) {
                    final AbstractChallengeView view = ChallengeViewManager.INSTANCE.getChallengeView(baseChallengeList.get(i));
                    if (view != null) {
                        views.add(view);
                    }
                }
                return views;
            }
            else {
                if (fieldName.equals("hasMoneyBaseChallenges")) {
                    final TIntArrayList list = this.m_currentProtector.getBaseChallengeList();
                    for (int j = 0, size2 = list.size(); j < size2; ++j) {
                        final int baseChallengeId = list.get(j);
                        if (ArrayUtils.contains(ChallengeConstants.MONEY_CHALLENGES, baseChallengeId)) {
                            return true;
                        }
                    }
                    return false;
                }
                if (fieldName.equals("availableChallenges")) {
                    final TIntArrayList baseChallengeList = this.m_currentProtector.getBaseChallengeList();
                    final DropTable<ChallengeDrop> dropTable = this.m_currentProtector.getBaseChallengeDropTable();
                    final TIntArrayList knownChallengeList = this.m_currentProtector.getKnownChallengeList();
                    if (baseChallengeList.size() + knownChallengeList.size() == 0) {
                        return null;
                    }
                    final ArrayList<FieldProvider> views2 = new ArrayList<FieldProvider>();
                    for (int k = 0, size3 = baseChallengeList.size(); k < size3; ++k) {
                        final AbstractChallengeView view2 = ChallengeViewManager.INSTANCE.getChallengeView(baseChallengeList.get(k));
                        final ChallengeDrop drop = dropTable.getDrop(baseChallengeList.get(k));
                        if (view2 != null && drop != null && (drop.getCriterion() == null || drop.getCriterion().isValid(this.m_currentProtector, null, null, null))) {
                            views2.add(view2);
                        }
                    }
                    for (int k = 0, size3 = knownChallengeList.size(); k < size3; ++k) {
                        final AbstractChallengeView view2 = ChallengeViewManager.INSTANCE.getChallengeView(knownChallengeList.get(k));
                        if (view2 != null) {
                            views2.add(view2);
                        }
                    }
                    return views2;
                }
                else {
                    if (fieldName.equals("numAvailableChallenges")) {
                        return this.m_currentProtector.getBaseChallengeList().size() + this.m_currentProtector.getKnownChallengeList().size();
                    }
                    if (fieldName.equals("numBoughtChallenges")) {
                        return this.m_currentProtector.getKnownChallengeList().size();
                    }
                    if (fieldName.equals("boughtChallenges")) {
                        this.m_boughtChallenges.clear();
                        final TIntArrayList list = this.m_currentProtector.getKnownChallengeList();
                        if (list.size() == 0) {
                            return null;
                        }
                        final ProtectorMerchantInventory inv = this.m_currentProtector.getChallengeMerchantInventory();
                        if (inv == null) {
                            return null;
                        }
                        for (int i = 0, size = list.size(); i < size; ++i) {
                            final int challengeId = list.getQuick(i);
                            final ProtectorMerchantInventoryItem item = inv.getFromFeatureId(challengeId);
                            if (item != null) {
                                this.m_boughtChallenges.add(item.getView());
                            }
                        }
                        return this.m_boughtChallenges;
                    }
                    else {
                        if (fieldName.equals("boughtBuffs")) {
                            return this.m_currentProtector.getNationBuffs();
                        }
                        if (fieldName.equals("boughtClimates")) {
                            return this.getMerchantInventoryItems(this.m_currentProtector.getClimateMerchantInventory(), true);
                        }
                        if (fieldName.equals("walletHandler")) {
                            if (this.m_currentProtector == null || this.m_currentProtector.getWallet() == null) {
                                return null;
                            }
                            final ProtectorWalletHandler walletHandler = (ProtectorWalletHandler)this.m_currentProtector.getWallet();
                            return walletHandler.getView(this.getProtector());
                        }
                        else {
                            if (fieldName.equals("tax")) {
                                return this.getCurrentTax(TaxContext.FLEA_ADD_ITEM_CONTEXT);
                            }
                            if (fieldName.equals("nation")) {
                                return this.m_nationFieldProvider;
                            }
                            if (fieldName.equals("description")) {
                                if (this.m_currentProtector == null) {
                                    return null;
                                }
                                final TextWidgetFormater sb = new TextWidgetFormater();
                                ProtectorDescriptionHelper.writeBuffs(sb, this.m_currentProtector, false);
                                final String s = sb.finishAndToString().replaceFirst("\n\n", "");
                                return (s.length() > 0) ? s : null;
                            }
                            else {
                                if (fieldName.equals("job")) {
                                    return WakfuTranslator.getInstance().getString(90, this.getProtectorId(), new Object[0]);
                                }
                                if (fieldName.equals("sex")) {
                                    return WakfuTranslator.getInstance().getString(91, this.getProtectorId(), new Object[0]);
                                }
                                if (fieldName.equals("height")) {
                                    return WakfuTranslator.getInstance().getString(92, this.getProtectorId(), new Object[0]);
                                }
                                if (fieldName.equals("weight")) {
                                    return WakfuTranslator.getInstance().getString(93, this.getProtectorId(), new Object[0]);
                                }
                                if (fieldName.equals("customDescription")) {
                                    if (!WakfuTranslator.getInstance().containsContentKey(94, this.getProtectorId())) {
                                        return null;
                                    }
                                    final String customDescription = WakfuTranslator.getInstance().getString(94, this.getProtectorId(), new Object[0]);
                                    return customDescription.split("\n");
                                }
                                else if (fieldName.equals("numSecrets")) {
                                    if (this.m_currentProtector == null) {
                                        return "";
                                    }
                                    int numTotal = 0;
                                    final Iterator<ProtectorSecret> it = this.m_currentProtector.getSecretsIterator();
                                    while (it.hasNext()) {
                                        it.next();
                                        ++numTotal;
                                    }
                                    return numTotal;
                                }
                                else if (fieldName.equals("secrets")) {
                                    if (this.m_currentProtector == null) {
                                        return null;
                                    }
                                    final ArrayList<ProtectorSecretView> views3 = new ArrayList<ProtectorSecretView>();
                                    final Iterator<ProtectorSecret> it = this.m_currentProtector.getSecretsIterator();
                                    while (it.hasNext()) {
                                        final ProtectorSecret secret = it.next();
                                        views3.add(new ProtectorSecretView(secret));
                                    }
                                    return views3;
                                }
                                else if (fieldName.equals("unlockedSecrets")) {
                                    if (this.m_currentProtector == null) {
                                        return "";
                                    }
                                    int numUnlocked = 0;
                                    int numTotal2 = 0;
                                    final ClientAchievementsContext context = WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext();
                                    final Iterator<ProtectorSecret> it2 = this.m_currentProtector.getSecretsIterator();
                                    while (it2.hasNext()) {
                                        final ProtectorSecret secret2 = it2.next();
                                        ++numTotal2;
                                        if (!context.hasObjective(secret2.getAchievementGoalId()) || context.isObjectiveCompleted(secret2.getAchievementGoalId())) {
                                            ++numUnlocked;
                                        }
                                    }
                                    return numUnlocked + "/" + numTotal2;
                                }
                                else {
                                    if (fieldName.equals("territoryName")) {
                                        final Territory territory = (Territory)this.m_currentProtector.getTerritory();
                                        if (WakfuTranslator.getInstance().containsContentKey(66, territory.getId())) {
                                            final String territoryName = WakfuTranslator.getInstance().getString(66, territory.getId(), new Object[0]);
                                            if (territoryName != null && territoryName.length() > 0) {
                                                return territoryName;
                                            }
                                        }
                                        return null;
                                    }
                                    if (fieldName.equals("buffTitle")) {
                                        switch (this.getProtectorBuffOrigin()) {
                                            case MDC: {
                                                return WakfuTranslator.getInstance().getString("protector.buffs.mdc.title");
                                            }
                                            case SHUKRUTE: {
                                                return WakfuTranslator.getInstance().getString("protector.buffs.shukrute.title");
                                            }
                                        }
                                    }
                                    else if (fieldName.equals("buffTooltip")) {
                                        switch (this.getProtectorBuffOrigin()) {
                                            case MDC: {
                                                return WakfuTranslator.getInstance().getString("protector.buffs.mdc.explaination");
                                            }
                                            case SHUKRUTE: {
                                                return WakfuTranslator.getInstance().getString("protector.buffs.shukrute.explaination");
                                            }
                                        }
                                    }
                                    else if (fieldName.equals("numBuffList")) {
                                        final BuffOrigin origin = this.getProtectorBuffOrigin();
                                        switch (origin) {
                                            case MDC: {
                                                if (this.m_currentProtector.getSatisfactionLevel() == ProtectorSatisfactionLevel.UNDEFINED) {
                                                    return this.m_currentProtector.getNationBuffs().length;
                                                }
                                                return this.m_currentProtector.getKnownBuffsList().size() + 1;
                                            }
                                            case SHUKRUTE: {
                                                return this.m_currentProtector.getNationBuffs().length;
                                            }
                                            default: {
                                                return 0;
                                            }
                                        }
                                    }
                                    else if (fieldName.equals("unsatisfiedRegionalState")) {
                                        if (WakfuGlobalZoneManager.getInstance().getWillCount() < 1) {
                                            return null;
                                        }
                                        return this.getSatisfactionRegionalBonusDescription(ProtectorSatisfactionLevel.UNSATISFIED);
                                    }
                                    else if (fieldName.equals("halfSatisfiedRegionalState")) {
                                        if (WakfuGlobalZoneManager.getInstance().getWillCount() < 2) {
                                            return null;
                                        }
                                        return this.getSatisfactionRegionalBonusDescription(ProtectorSatisfactionLevel.HALF_SATISFIED);
                                    }
                                    else if (fieldName.equals("satisfiedRegionalState")) {
                                        if (WakfuGlobalZoneManager.getInstance().getWillCount() < 1) {
                                            return null;
                                        }
                                        return this.getSatisfactionRegionalBonusDescription(ProtectorSatisfactionLevel.SATISFIED);
                                    }
                                    else if (fieldName.equals("unsatisfiedGlobalState")) {
                                        if (WakfuGlobalZoneManager.getInstance().getWillCount() < 1) {
                                            return null;
                                        }
                                        return this.getSatisfactionGlobalBonusDescription(ProtectorSatisfactionLevel.UNSATISFIED);
                                    }
                                    else if (fieldName.equals("halfSatisfiedGlobalState")) {
                                        if (WakfuGlobalZoneManager.getInstance().getWillCount() < 2) {
                                            return null;
                                        }
                                        return this.getSatisfactionGlobalBonusDescription(ProtectorSatisfactionLevel.HALF_SATISFIED);
                                    }
                                    else if (fieldName.equals("satisfiedGlobalState")) {
                                        if (WakfuGlobalZoneManager.getInstance().getWillCount() < 1) {
                                            return null;
                                        }
                                        return this.getSatisfactionGlobalBonusDescription(ProtectorSatisfactionLevel.SATISFIED);
                                    }
                                    else if (fieldName.equals("unsatisfiedStateIcon")) {
                                        final int stateId = this.getSatisfactionRegionalBonusStateId(ProtectorSatisfactionLevel.UNSATISFIED);
                                        if (stateId == -1) {
                                            return null;
                                        }
                                        final StateClient state = (StateClient)StateManager.getInstance().getState(stateId);
                                        return state.getIconUrl();
                                    }
                                    else if (fieldName.equals("halfSatisfiedStateIcon")) {
                                        final int stateId = this.getSatisfactionRegionalBonusStateId(ProtectorSatisfactionLevel.HALF_SATISFIED);
                                        if (stateId == -1) {
                                            return null;
                                        }
                                        final StateClient state = (StateClient)StateManager.getInstance().getState(stateId);
                                        return state.getIconUrl();
                                    }
                                    else if (fieldName.equals("satisfiedStateIcon")) {
                                        final int stateId = this.getSatisfactionRegionalBonusStateId(ProtectorSatisfactionLevel.SATISFIED);
                                        if (stateId == -1) {
                                            return null;
                                        }
                                        final StateClient state = (StateClient)StateManager.getInstance().getState(stateId);
                                        return state.getIconUrl();
                                    }
                                    else {
                                        if (fieldName.equals("globalStateIcon")) {
                                            return WakfuConfiguration.getInstance().getIconUrl("protectorBuffsIconsPath", "defaultIconPath", -1);
                                        }
                                        if (fieldName.equals("buffList")) {
                                            final BuffOrigin origin = this.getProtectorBuffOrigin();
                                            final TIntArrayList knownBuffs = this.m_currentProtector.getKnownBuffsList();
                                            final ArrayList<ProtectorBuffView> views4 = new ArrayList<ProtectorBuffView>();
                                            switch (origin) {
                                                case MDC: {
                                                    if (this.m_currentProtector.getSatisfactionLevel() != ProtectorSatisfactionLevel.UNDEFINED) {
                                                        final ArrayList<ProtectorBuff> buffs = new ArrayList<ProtectorBuff>();
                                                        for (int k = 0, size3 = knownBuffs.size(); k < size3; ++k) {
                                                            final ProtectorBuff buff = ProtectorBuffManager.INSTANCE.getBuff(knownBuffs.get(k));
                                                            if (buff != null && buff.getOrigin() == BuffOrigin.MDC) {
                                                                buffs.add(buff);
                                                            }
                                                        }
                                                        views4.add(new ProtectorBuffView(null, this.m_currentProtector, buffs.size() != 0));
                                                        for (int k = 0, size3 = buffs.size(); k < size3; ++k) {
                                                            final ProtectorBuff buff = buffs.get(k);
                                                            views4.add(new ProtectorBuffView(buff, this.m_currentProtector));
                                                        }
                                                        Collections.sort(views4, ProtectorBuffView.ProtectorBuffViewComparator.INSTANCE);
                                                        break;
                                                    }
                                                    if (this.m_currentProtector.getNationBuffs() == null) {
                                                        return null;
                                                    }
                                                    final com.ankamagames.wakfu.client.core.game.protector.ProtectorBuff[] arr$ = this.m_currentProtector.getNationBuffs();
                                                    for (int len$ = arr$.length, i$ = 0; i$ < len$; ++i$) {
                                                        final ProtectorBuff buff = arr$[i$];
                                                        if (buff.getOrigin() == BuffOrigin.MDC) {
                                                            views4.add(new ProtectorBuffView(buff, this.m_currentProtector, false));
                                                        }
                                                    }
                                                    break;
                                                }
                                                case SHUKRUTE: {
                                                    if (this.m_currentProtector.getNationBuffs() == null) {
                                                        return null;
                                                    }
                                                    for (final ProtectorBuff buff : this.m_currentProtector.getNationBuffs()) {
                                                        if (buff.getOrigin() == BuffOrigin.SHUKRUTE) {
                                                            views4.add(new ProtectorBuffView(buff, this.m_currentProtector, false));
                                                        }
                                                    }
                                                    break;
                                                }
                                            }
                                            return views4;
                                        }
                                        if (fieldName.equals("zoology")) {
                                            return this.m_currentProtector.getEcosystemHandler().getView();
                                        }
                                        if (fieldName.equals("territoryRecommendedLevel")) {
                                            final Territory territory = this.getTerritory();
                                            if (territory == null) {
                                                return null;
                                            }
                                            final short minLevel = territory.getMinLevel();
                                            final short maxLevel = territory.getMaxLevel();
                                            if (minLevel != -1 && maxLevel != -1) {
                                                return StringFormatter.format(WakfuTranslator.getInstance().getString("recommended.level", minLevel, maxLevel), new Object[0]);
                                            }
                                            return StringFormatter.format(WakfuTranslator.getInstance().getString("recommended.level", 1, 5), new Object[0]);
                                        }
                                    }
                                    return super.getFieldValue(fieldName);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private int getSatisfactionRegionalBonusStateId(final ProtectorSatisfactionLevel satisfactionLevel) {
        switch (satisfactionLevel) {
            case UNSATISFIED: {
                return 1917;
            }
            case SATISFIED: {
                return 1916;
            }
            case HALF_SATISFIED: {
                return 1915;
            }
            default: {
                return -1;
            }
        }
    }
    
    private String getSatisfactionGlobalBonusDescription(final ProtectorSatisfactionLevel satisfactionLevel) {
        final ArrayList<String> strings = new ArrayList<String>();
        final Color c = (satisfactionLevel.getId() <= this.m_currentProtector.getSatisfactionLevel().getId()) ? Color.WHITE : Color.LIGHT_GRAY;
        final TIntArrayList knownBuffs = this.m_currentProtector.getKnownBuffsList();
        for (int i = 0, size = knownBuffs.size(); i < size; ++i) {
            final ProtectorBuff buff = ProtectorBuffManager.INSTANCE.getBuff(knownBuffs.get(i));
            if (buff != null && buff.getOrigin() == BuffOrigin.MDC) {
                final SatisfactionManager satisfactionManager = new SatisfactionManager() {
                    @Override
                    public int getMonsterSatisfaction() {
                        return 0;
                    }
                    
                    @Override
                    public int getResourceSatisfaction() {
                        return 0;
                    }
                    
                    @Override
                    public ProtectorSatisfactionLevel getGlobalSatisfaction() {
                        return satisfactionLevel;
                    }
                };
                if (buff.getCriterion() == null || buff.getCriterion().isValid(this.m_currentProtector, this.m_currentProtector, satisfactionManager, null)) {
                    strings.add(new TextWidgetFormater().openText().addColor(c.getRGBtoHex()).append(ProtectorBuffView.getProtectorBuffs(buff, false)).closeText().finishAndToString());
                }
            }
        }
        if (strings.isEmpty()) {
            strings.add(WakfuTranslator.getInstance().getString("protector.noBuff"));
        }
        final TextWidgetFormater sb = new TextWidgetFormater();
        for (int j = 0, size2 = strings.size(); j < size2; ++j) {
            final String s = strings.get(j);
            if (j != 0) {
                sb.newLine();
            }
            sb.append(s);
        }
        return sb.finishAndToString();
    }
    
    private Object getSatisfactionRegionalBonusDescription(final ProtectorSatisfactionLevel satisfactionLevel) {
        final int stateId = this.getSatisfactionRegionalBonusStateId(satisfactionLevel);
        if (stateId == -1) {
            return null;
        }
        final Color c = (satisfactionLevel.getId() <= this.m_currentProtector.getSatisfactionLevel().getId()) ? Color.WHITE : Color.LIGHT_GRAY;
        final StateClient state = (StateClient)StateManager.getInstance().getState(stateId);
        final ArrayList<String> strings = new ArrayList<String>();
        for (final String s : CastableDescriptionGenerator.generateDescription(new StateWriter(state, CastableDescriptionGenerator.DescriptionMode.ALL))) {
            strings.add(new TextWidgetFormater().openText().addColor(c.getRGBtoHex()).append(s).closeText().finishAndToString());
        }
        if (strings.isEmpty()) {
            strings.add(WakfuTranslator.getInstance().getString("protector.noBuff"));
        }
        return strings;
    }
    
    public void updateTime() {
        for (int i = 0, size = this.m_boughtChallenges.size(); i < size; ++i) {
            this.m_boughtChallenges.get(i).updateTime();
        }
    }
    
    private BuffOrigin getProtectorBuffOrigin() {
        final TIntArrayList knownBuffs = this.m_currentProtector.getKnownBuffsList();
        if (knownBuffs.size() == 0) {
            return BuffOrigin.MDC;
        }
        final ProtectorBuff firstBuff = ProtectorBuffManager.INSTANCE.getBuff(knownBuffs.get(0));
        return firstBuff.getOrigin();
    }
    
    @Override
    public int getProtectorId() {
        return (this.m_currentProtector != null) ? this.m_currentProtector.getId() : -1;
    }
    
    @Override
    public void onNationChanged(final ProtectorBase protector, final Nation nation) {
        if (protector != this.m_currentProtector) {
            return;
        }
        this.m_nationFieldProvider = new NationFieldProvider(nation.getNationId());
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "nation");
        if (WakfuGameEntity.getInstance().hasFrame(UIProtectorManagementFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIProtectorManagementFrame.getInstance());
        }
    }
    
    private static TaxView getTax(final Protector protector, final TaxContext context) {
        final EnumMap<TaxContext, Tax> taxes = protector.getTaxes();
        return new TaxView(taxes.get(context));
    }
    
    public TaxView getCurrentTax(final TaxContext context) {
        switch (context) {
            case MARKET_ADD_ITEM_CONTEXT: {
                if (ProtectorView.m_marketTaxView == null) {
                    ProtectorView.m_marketTaxView = getTax(this.m_currentProtector, context);
                }
                return ProtectorView.m_marketTaxView;
            }
            case FLEA_ADD_ITEM_CONTEXT: {
                if (ProtectorView.m_fleaTaxView == null) {
                    ProtectorView.m_fleaTaxView = getTax(this.m_currentProtector, context);
                }
                return ProtectorView.m_fleaTaxView;
            }
            default: {
                return null;
            }
        }
    }
    
    public Protector getProtector() {
        return this.m_currentProtector;
    }
    
    public Protector getProtectorCacheForDimensionalBag() {
        return this.m_protectorCacheForDimensionalBag;
    }
    
    public void clean() {
        if (this.m_animation != null) {
            this.m_animation.dispose();
            this.m_animation = null;
        }
    }
    
    public void setCurrentProtector(final Protector currentProtector) {
        if (this.m_protectorCacheForDimensionalBag != null) {
            TaxManager.INSTANCE.removeTaxHandler(this.m_protectorCacheForDimensionalBag);
            this.m_protectorCacheForDimensionalBag = null;
        }
        if (this.m_currentProtector != null) {
            final Wallet wallet = this.m_currentProtector.getWallet();
            if (wallet != null) {
                wallet.setListener(null);
            }
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (this.m_currentProtector == null || (localPlayer != null && !localPlayer.getOwnedDimensionalBag().equals(localPlayer.getVisitingDimentionalBag()))) {
                TaxManager.INSTANCE.removeTaxHandler(this.m_currentProtector);
            }
            else {
                this.m_protectorCacheForDimensionalBag = this.m_currentProtector;
            }
            this.clean();
        }
        PropertiesProvider.getInstance().setPropertyValue("wakfuEcosystemEnabled", false);
        this.m_currentProtector = currentProtector;
        if (this.m_animation != null) {
            this.m_animation.dispose();
            this.m_animation = null;
        }
        ProtectorView.m_fleaTaxView = null;
        ProtectorView.m_marketTaxView = null;
        if (this.m_currentProtector != null) {
            TaxManager.INSTANCE.addTaxHandler(this.m_currentProtector);
            final Wallet wallet = this.m_currentProtector.getWallet();
            if (wallet != null) {
                wallet.setListener(this);
            }
            this.m_nationFieldProvider = new NationFieldProvider(this.m_currentProtector.getCurrentNation().getNationId());
            PropertiesProvider.getInstance().setPropertyValue("protector", this);
        }
        else {
            UIProtectorFrame.getInstance().unloadLinkedDialogs();
            this.m_nationFieldProvider = null;
        }
        this.updateProtectorFields();
    }
    
    public static String generateTalkText(final int type, final int subtype, final int protectorId, final Object... params) {
        final int subKey = type * 10000000 + subtype * 10000 + protectorId;
        final String s = WakfuTranslator.getInstance().getString(49, subKey, (params == null) ? new String[0] : params);
        return WakfuTranslator.getInstance().containsContentKey(49, subKey) ? s : null;
    }
    
    private ArrayList<ProtectorMerchantItemView> getMerchantInventoryItems(final ProtectorMerchantInventory merchantInventory, final boolean bought) {
        if (merchantInventory == null || merchantInventory.size() == 0) {
            return null;
        }
        final ArrayList<ProtectorMerchantItemView> result = new ArrayList<ProtectorMerchantItemView>(merchantInventory.size());
        for (final AbstractMerchantInventoryItem merchantInventoryItem : merchantInventory) {
            final ProtectorMerchantInventoryItem item = (ProtectorMerchantInventoryItem)merchantInventoryItem;
            if (item != null && item.isActivated() == bought) {
                result.add(((ProtectorMerchantInventoryItem)merchantInventoryItem).getView());
            }
        }
        if (result.size() == 0) {
            return null;
        }
        return result;
    }
    
    @Override
    public void onWalletUpdated(final Wallet wallet, final int delta) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "walletHandler");
        if (this.m_currentProtector == null || this.m_currentProtector.getWallet() == null) {
            return;
        }
        final ProtectorWalletHandler walletHandler = (ProtectorWalletHandler)this.m_currentProtector.getWallet();
        final WalletHandlerView view = walletHandler.getView(this.getProtector());
        view.updateView();
    }
    
    @Nullable
    @Override
    public AnimatedElement getAnimation(final int protectorId, @NotNull final ProtectorMood protectorMood) {
        if (this.m_animation == null) {
            this.m_animation = ProtectorDisplayHelper.createProtectorAnimation(protectorId);
        }
        if (this.m_animation != null) {
            ProtectorDisplayHelper.setAnimation(this.m_animation, protectorMood);
        }
        return this.m_animation;
    }
    
    @Nullable
    @Override
    public Territory getTerritory() {
        if (this.m_currentProtector == null) {
            return null;
        }
        return (Territory)this.m_currentProtector.getTerritory();
    }
    
    @Override
    public boolean isStaticProtector() {
        return false;
    }
    
    static {
        m_instance = new ProtectorView();
    }
}
