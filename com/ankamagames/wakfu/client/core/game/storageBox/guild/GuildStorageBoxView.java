package com.ankamagames.wakfu.client.core.game.storageBox.guild;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.guild.storage.*;
import com.ankamagames.wakfu.client.core.game.storageBox.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.guildStorage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;

public class GuildStorageBoxView extends AbstractStorageBoxView implements GuildListener
{
    private GuildStorageHistoryView m_guildStorageHistoryView;
    private final TIntObjectHashMap<Boolean> m_compartments;
    
    public GuildStorageBoxView(final TIntObjectHashMap<Boolean> compartments, final GuildStorageHistory history, final int money) {
        super();
        this.setGuildHistory(history);
        this.m_compartments = compartments;
        this.m_moneyAmount = money;
        for (final GuildStorageCompartmentLinkType guildStorageCompartmentType : GuildStorageCompartmentLinkType.values()) {
            this.m_compartmentViewImpls.add(new GuildAggregatedCompartmentView(guildStorageCompartmentType));
        }
        this.m_selectedCompartmentImpl = (this.m_compartmentViewImpls.isEmpty() ? null : this.m_compartmentViewImpls.get(0));
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Guild guild = ((GuildLocalInformationHandler)localPlayer.getGuildHandler()).getGuild();
        if (guild != null) {
            guild.addListener(this);
        }
    }
    
    public boolean isSerialized(final int compartmentId) {
        return this.m_compartments.containsKey(compartmentId);
    }
    
    public boolean isUnlocked(final int compartmentId) {
        final Boolean unlocked = this.m_compartments.get(compartmentId);
        return unlocked != null && unlocked;
    }
    
    @Override
    protected String getName() {
        return WakfuTranslator.getInstance().getString("guild.storageBox");
    }
    
    @Override
    protected Dimension getPrefSize() {
        return new Dimension(400, 160);
    }
    
    @Override
    protected int getIdealSizeMaxColumns() {
        return 10;
    }
    
    @Override
    protected int getIdealSizeMaxRows() {
        return 4;
    }
    
    @Override
    protected boolean canManageMoney() {
        return true;
    }
    
    @Override
    protected boolean canPutMoney() {
        return true;
    }
    
    @Override
    protected boolean canTakeMoney() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
        final GuildMember guildMember = guildHandler.getMember(localPlayer.getId());
        if (guildMember == null) {
            return false;
        }
        final GuildRank guildRank = guildHandler.getRank(guildMember.getRank());
        return guildRank != null && guildRank.hasAuthorisation(GuildRankAuthorisation.REMOVE_CHEST_MONEY);
    }
    
    @Override
    protected FieldProvider getHistory() {
        return this.m_guildStorageHistoryView;
    }
    
    public final void setGuildHistory(final GuildStorageHistory history) {
        this.m_guildStorageHistoryView = new GuildStorageHistoryView(history);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "history");
    }
    
    @Override
    public int getSize() {
        return this.m_compartments.size();
    }
    
    @Override
    public void depositMoney(final int amount) {
        if (!this.canPutMoney()) {
            final String errorMsg = WakfuTranslator.getInstance().getString("error.guild.cannotDepositMoney");
            final MessageBoxData data = new MessageBoxData(102, 1, errorMsg, 2L);
            Xulor.getInstance().msgBox(data);
            return;
        }
        final Message msg = new GuildStorageMoneyActionRequestMessage(Math.abs(amount));
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    @Override
    public void withdrawMoney(final int amount) {
        if (!this.canPutMoney()) {
            final String errorMsg = WakfuTranslator.getInstance().getString("error.guild.cannotWithdrawMoney");
            final MessageBoxData data = new MessageBoxData(102, 1, errorMsg, 2L);
            Xulor.getInstance().msgBox(data);
            return;
        }
        final Message msg = new GuildStorageMoneyActionRequestMessage(-Math.abs(amount));
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    @Override
    public void nameChanged() {
    }
    
    @Override
    public void blazonChanged() {
    }
    
    @Override
    public void descriptionChanged() {
    }
    
    @Override
    public void messageChanged() {
    }
    
    @Override
    public void levelChanged(final short level) {
    }
    
    @Override
    public void currentGuildPointsChanged(final int deltaPoints) {
    }
    
    @Override
    public void totalGuildPointsChanged(final int deltaPoints) {
    }
    
    @Override
    public void rankAdded(final GuildRank rank) {
    }
    
    @Override
    public void rankMoved(final GuildRank rank) {
    }
    
    @Override
    public void rankRemoved(final GuildRank rank) {
    }
    
    @Override
    public void memberAdded(final GuildMember member) {
    }
    
    @Override
    public void memberRemoved(final GuildMember member) {
    }
    
    @Override
    public void bonusRemoved(final GuildBonus bonus) {
    }
    
    @Override
    public void bonusAdded(final GuildBonus bonus) {
    }
    
    @Override
    public void nationIdChanged(final int nationId) {
    }
    
    @Override
    public void rankChanged(final GuildRank rank) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "canTakeMoney");
    }
    
    @Override
    public void memberChanged(final GuildMember member) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "canTakeMoney");
    }
    
    @Override
    public void bonusActivated(final GuildBonus bonus) {
    }
    
    @Override
    public void earnedPointsWeeklyChanged(final int earnedPoints) {
    }
    
    @Override
    public void lastEarningPointWeekChanged(final int week) {
    }
    
    @Override
    public void clear() {
        super.clear();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Guild guild = ((GuildLocalInformationHandler)localPlayer.getGuildHandler()).getGuild();
        if (guild != null) {
            guild.removeListener(this);
        }
    }
}
