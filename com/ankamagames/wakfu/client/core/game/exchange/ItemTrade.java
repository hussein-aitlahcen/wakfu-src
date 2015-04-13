package com.ankamagames.wakfu.client.core.game.exchange;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.core.messagebox.*;
import java.text.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.common.game.exchange.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import gnu.trove.*;

public class ItemTrade extends ItemExchanger<Item, WakfuExchangerUser> implements FieldProvider
{
    public static final String EXCHANGE_ID_FIELD = "exchangeId";
    public static final String LOCAL_ITEMS_EXCHANGE_FIELD = "localItemsExchange";
    public static final String REMOTE_ITEMS_EXCHANGE_FIELD = "remoteItemsExchange";
    public static final String LOCAL_MONEY_EXCHANGE_FIELD = "localMoneyExchange";
    public static final String REMOTE_MONEY_EXCHANGE_FIELD = "remoteMoneyExchange";
    public static final String REMOTE_USER_STATE = "remoteUserState";
    public static final String LOCAL_USER_STATE = "localUserState";
    public static final String[] FIELDS;
    private MessageBoxControler m_invitationMessageBoxControler;
    private static final NumberFormat m_currencyFormatter;
    private byte m_state;
    protected long m_currentDragUniqueId;
    
    public ItemTrade(final long id, final WakfuExchangerUser requester, final WakfuExchangerUser target) {
        super(id, requester, target);
        this.m_invitationMessageBoxControler = null;
        this.m_state = 0;
    }
    
    @Override
    protected boolean initUser(final WakfuExchangerUser user) {
        user.getPlayer().cancelCurrentOccupation(true, false);
        final ExchangeOccupation occupation = new ExchangeOccupation(this);
        if (occupation.isAllowed()) {
            user.setOccupation(occupation);
            occupation.begin();
            return true;
        }
        return false;
    }
    
    @Override
    protected void cleanUser(final WakfuExchangerUser user) {
        final ExchangeOccupation occupation = user.getOccupation();
        if (occupation != null) {
            user.clearOccupation();
        }
    }
    
    public void setCurrentDragUniqueId(final long itemDraguedId) {
        this.m_currentDragUniqueId = itemDraguedId;
    }
    
    public long getCurrentDragUniqueId() {
        return this.m_currentDragUniqueId;
    }
    
    public void setInvitationMessageBoxControler(final MessageBoxControler invitationMessageBoxControler) {
        this.m_invitationMessageBoxControler = invitationMessageBoxControler;
    }
    
    public MessageBoxControler getInvitationMessageBoxControler() {
        return this.m_invitationMessageBoxControler;
    }
    
    public boolean isRequesterLocal() {
        return ((WakfuExchangerUser)this.m_requester).isLocalPlayer();
    }
    
    @Override
    protected boolean needsInvitationStep() {
        return true;
    }
    
    @Override
    protected boolean needsWaitingUsersReady() {
        return true;
    }
    
    @Override
    protected boolean isExchangeValid() {
        return this.m_state == 0;
    }
    
    @Override
    protected boolean isCheckQuantityNeeded() {
        return false;
    }
    
    public void setCashInExchange(final long userId, final int amountOfCash) {
        final WakfuExchangerUser user = ((ItemExchanger<ContentType, WakfuExchangerUser>)this).getUserById(userId);
        if (user == null) {
            ItemTrade.m_logger.error((Object)"[Trade] On essaye de modifier les kamas d'un echange pour un utilisateur qui n'y est pas");
            return;
        }
        if (amountOfCash < 0) {
            ItemTrade.m_logger.error((Object)("[Trade] Impossible de placer une quantit\u00e9 n\u00e9gative de kamas dans l'\u00e9change. La quantit\u00e9 de monnaie donn\u00e9e par " + user.getName() + " n'a pas \u00e9t\u00e9 mise a jour"));
            return;
        }
        ((WakfuExchangerUser)this.m_requester).setReady(false);
        ((WakfuExchangerUser)this.m_target).setReady(false);
        int cashModification = amountOfCash;
        if (user.isLocalPlayer()) {
            cashModification = user.getMoneyExchanged() - amountOfCash;
        }
        user.setMoneyExchanged(amountOfCash);
        ItemTrade.m_logger.info((Object)("[Trade] Le joueur " + user.getName() + " mets la somme de kamas \u00e0 " + amountOfCash));
        this.notifyUsers(WakfuItemExchangerModifiedEvent.checkOut(this, ItemExchangerModifiedEvent.Modification.CASH_MODIFIED, userId, cashModification));
    }
    
    @Override
    public void addItemToExchange(final WakfuExchangerUser wakfuExchangerUser, final Item referenceContent, final short quantity) {
        super.addItemToExchange(wakfuExchangerUser, referenceContent, quantity);
        ItemTrade.m_logger.info((Object)("[Trade] Le joueur " + wakfuExchangerUser.getName() + " ajoute " + quantity + "x" + referenceContent.getName() + " (refId=" + referenceContent.getReferenceId() + ")"));
    }
    
    @Override
    public void removeItemToExchange(final WakfuExchangerUser wakfuExchangerUser, final Item referenceContent, final short quantity) {
        super.removeItemToExchange(wakfuExchangerUser, referenceContent, quantity);
        ItemTrade.m_logger.info((Object)("[Trade] Le joueur " + wakfuExchangerUser.getName() + " retire " + quantity + "x" + referenceContent.getName() + " (refId=" + referenceContent.getReferenceId() + ")"));
    }
    
    @Override
    protected void doExchange() {
        final WakfuExchangerUser localUser = this.getLocalUser();
        final WakfuExchangerUser remoteUser = this.getRemoteUser();
        assert localUser != null && remoteUser != null;
        ItemTrade.m_logger.info((Object)"[Trade] Fin de l'\u00e9change");
        String summary = "[Trade] le joueur " + localUser.getName() + " donne : " + localUser.getMoneyExchanged() + "K ; ";
        if (!localUser.exchangeListIsEmpty()) {
            final TLongObjectIterator<Item> it = localUser.exchangeListIterator();
            while (it.hasNext()) {
                it.advance();
                final Item item = it.value();
                summary = summary + item.getQuantity() + "x" + item.getName() + " (refId=" + item.getReferenceId() + ") ";
            }
        }
        summary = summary + "\nle joueur " + remoteUser.getName() + " donne : " + remoteUser.getMoneyExchanged() + "K ; ";
        if (!remoteUser.exchangeListIsEmpty()) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final TLongObjectIterator<Item> it2 = remoteUser.exchangeListIterator();
            while (it2.hasNext()) {
                it2.advance();
                final Item item2 = it2.value();
                summary = summary + item2.getQuantity() + "x" + item2.getName() + " (refId=" + item2.getReferenceId() + ") ";
                final Item itemClone = item2.getClone();
                final AbstractBag destinationBag = localPlayer.getBags().addItemToBags(itemClone);
                if (destinationBag == null) {
                    ItemTrade.m_logger.error((Object)("[Trade] On a pas pu ajouter les " + item2.getQuantity() + "de " + item2.getName() + " a l'inventaire local"));
                }
            }
        }
        ItemTrade.m_logger.info((Object)summary);
    }
    
    @Override
    public String[] getFields() {
        return ItemTrade.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("exchangeId")) {
            return this.getId();
        }
        if (fieldName.equals("localUserState")) {
            return (this.m_state == 0) ? (this.getLocalUser().isReady() ? 1 : 0) : -1;
        }
        if (fieldName.equals("remoteUserState")) {
            return (this.m_state == 0) ? (this.getRemoteUser().isReady() ? 1 : 0) : -1;
        }
        if (fieldName.equals("localItemsExchange")) {
            return this.createItemExchangeList(this.getLocalUser());
        }
        if (fieldName.equals("remoteItemsExchange")) {
            return this.createItemExchangeList(this.getRemoteUser());
        }
        if (fieldName.equals("localMoneyExchange")) {
            return ItemTrade.m_currencyFormatter.format(this.getLocalUser().getMoneyExchanged());
        }
        if (fieldName.equals("remoteMoneyExchange")) {
            return ItemTrade.m_currencyFormatter.format(this.getRemoteUser().getMoneyExchanged());
        }
        return null;
    }
    
    private Collection<Item> createItemExchangeList(final WakfuExchangerUser localUser) {
        if (!localUser.exchangeListIsEmpty()) {
            final Collection<Item> localList = new ArrayList<Item>();
            final TLongObjectIterator<Item> it = localUser.exchangeListIterator();
            while (it.hasNext()) {
                it.advance();
                final Item item = it.value();
                if (item.getQuantity() > 0) {
                    localList.add(item);
                }
            }
            return localList;
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public void updateLocalItemProperties() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "localItemsExchange");
    }
    
    public void updateRemoteItemProperties() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "remoteItemsExchange");
    }
    
    public void updateLocalCashProperties() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "localMoneyExchange");
    }
    
    public void updateRemoteCashProperties() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "remoteMoneyExchange");
    }
    
    public void updateLocalReadyProperties() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "localUserState");
    }
    
    public void updateRemoteReadyProperties() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "remoteUserState");
    }
    
    public byte getState() {
        return this.m_state;
    }
    
    public void setState(final byte state) {
        this.m_state = state;
    }
    
    public String getErrorSentence() {
        return WakfuTranslator.getInstance().getString("exchange.error." + this.m_state);
    }
    
    public WakfuExchangerUser getLocalUser() {
        return ((ItemExchanger<ContentType, WakfuExchangerUser>)this).getUserById(WakfuGameEntity.getInstance().getLocalPlayer().getId());
    }
    
    public WakfuExchangerUser getRemoteUser() {
        return ((ItemExchanger<ContentType, WakfuExchangerUser>)this).getOtherUser(this.getLocalUser());
    }
    
    static {
        FIELDS = new String[] { "exchangeId", "localItemsExchange", "remoteItemsExchange", "localMoneyExchange", "remoteMoneyExchange", "remoteUserState", "localUserState" };
        m_currencyFormatter = NumberFormat.getIntegerInstance();
    }
}
