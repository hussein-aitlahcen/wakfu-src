package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;

public abstract class ItemExchanger<ContentType extends InventoryContent, User extends ExchangerUser<ContentType>>
{
    protected static final Logger m_logger;
    protected User m_requester;
    protected User m_target;
    protected ItemExchangeState m_currentState;
    protected final long m_id;
    protected final long m_requesterId;
    protected final long m_targetId;
    
    protected ItemExchanger(final long id, final User requester, final User target) {
        super();
        this.m_id = id;
        this.m_requesterId = requester.getId();
        this.m_targetId = target.getId();
        this.init(requester, target);
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public long getRequesterId() {
        return this.m_requesterId;
    }
    
    public long getTargetId() {
        return this.m_targetId;
    }
    
    protected void init(final User user1, final User user2) {
        ItemExchanger.m_logger.info((Object)("[Trade] Initialisation d'un \u00e9change entre " + user1.getName() + "(id=" + user1.getId() + ") et " + user2.getName() + " (id=" + user2.getId() + ")"));
        if (user1 == null) {
            throw new NullPointerException("[Trade] First user of an ItemExchanger can't be null");
        }
        if (user2 == null) {
            throw new NullPointerException("[Trade] Second user of an ItemExchanger can't be null");
        }
        this.m_currentState = ItemExchangeState.INITIALIZING;
        (this.m_requester = user1).setReady(false);
        (this.m_target = user2).setReady(false);
    }
    
    public void start() {
        if (this.m_currentState != ItemExchangeState.INITIALIZING) {
            throw new IllegalStateException("[Trade] Only an Initializing exchanger can be started. Current State: " + this.m_currentState);
        }
        if (!ItemExchangerManager.getInstance().addExchanger(this)) {
            this.m_currentState = ItemExchangeState.FINISHED;
            this.notifyUser(this.m_requester, ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.INVITATION_IMPOSSIBLE_USER_BUSY));
            this.cleanUser(this.m_requester);
            this.cleanUser(this.m_target);
            return;
        }
        if (this.needsInvitationStep()) {
            this.doInvitationStep();
        }
        else {
            this.doStartStep();
        }
    }
    
    protected abstract boolean initUser(final User p0);
    
    protected abstract void cleanUser(final User p0);
    
    protected boolean isUserConcerned(final ExchangerUser user) {
        return user == this.m_requester || user == this.m_target;
    }
    
    public User getRequester() {
        return this.m_requester;
    }
    
    public User getTarget() {
        return this.m_target;
    }
    
    @Nullable
    public User getOtherUser(final User user) {
        if (user == this.m_requester) {
            return this.m_target;
        }
        if (user == this.m_target) {
            return this.m_requester;
        }
        return null;
    }
    
    private void doInvitationStep() {
        this.m_currentState = ItemExchangeState.AWAITING_INVITATION_ANSWER;
        this.notifyUser(this.m_requester, ItemExchangerEvent.checkOut(this, ItemExchangerEvent.Action.EXCHANGE_PROPOSED));
        this.notifyUser(this.m_target, ItemExchangerEvent.checkOut(this, ItemExchangerEvent.Action.EXCHANGE_REQUESTED));
    }
    
    private void doStartStep() {
        if (!this.initUser(this.m_requester)) {
            this.m_currentState = ItemExchangeState.FINISHED;
            this.notifyUser(this.m_requester, ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.INVITATION_LOCALLY_CANCELED));
            this.notifyUser(this.m_target, ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.INVITATION_IMPOSSIBLE_USER_BUSY));
            ItemExchangerManager.getInstance().removeExchanger(this);
            return;
        }
        if (!this.initUser(this.m_target)) {
            this.m_currentState = ItemExchangeState.FINISHED;
            this.notifyUser(this.m_requester, ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.INVITATION_IMPOSSIBLE_USER_BUSY));
            this.notifyUser(this.m_target, ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.INVITATION_LOCALLY_CANCELED));
            this.cleanUser(this.m_requester);
            ItemExchangerManager.getInstance().removeExchanger(this);
            return;
        }
        this.m_currentState = ItemExchangeState.RUNNING;
        this.notifyUsers(ItemExchangerEvent.checkOut(this, ItemExchangerEvent.Action.EXCHANGE_STARTED));
    }
    
    protected void finishExchange() {
        ItemExchanger.m_logger.info((Object)("[Trade] Fin de l'\u00e9change entre " + this.m_requester.getName() + " (id=" + this.m_requester.getId() + " et " + this.m_target.getName() + " (id=" + this.m_target.getId() + ")"));
        this.m_currentState = ItemExchangeState.FINISHED;
        this.m_requester.clear();
        this.m_target.clear();
        ItemExchangerManager.getInstance().removeExchanger(this);
    }
    
    @Nullable
    public User getUserById(final long id) {
        if (id == this.m_requester.getId()) {
            return this.m_requester;
        }
        if (id == this.m_target.getId()) {
            return this.m_target;
        }
        return null;
    }
    
    protected void notifyUsers(final ItemExchangerEvent event) {
        this.notifyUsers(event, true);
    }
    
    protected void notifyUsers(final ItemExchangerEvent event, final boolean releaseEvent) {
        if (this.m_requester != null) {
            this.m_requester.onItemExchangerEvent(event);
        }
        if (this.m_target != null) {
            this.m_target.onItemExchangerEvent(event);
        }
        if (releaseEvent) {
            try {
                event.release();
            }
            catch (Exception e) {
                ItemExchanger.m_logger.error((Object)"[Trade] Exception lors de la notification d'un \u00e9v\u00e8nement aux utilisateurs d'un ItemExchanger: ", (Throwable)e);
            }
        }
    }
    
    protected void notifyUser(final long userId, final ItemExchangerEvent event) {
        this.notifyUser(userId, event, true);
    }
    
    protected void notifyUser(final User user, final ItemExchangerEvent event) {
        this.notifyUser(user, event, true);
    }
    
    protected void notifyUser(final long userId, final ItemExchangerEvent event, final boolean releaseEvent) {
        final User user = this.getUserById(userId);
        if (user == null) {
            ItemExchanger.m_logger.error((Object)("[Trade] Impossible d'envoyer un \u00e9v\u00e8nement \u00e0 l'utilisateur d'id " + userId));
            return;
        }
        this.notifyUser(user, event, releaseEvent);
    }
    
    protected void notifyUser(final User user, final ItemExchangerEvent event, final boolean releaseEvent) {
        if (user != null) {
            user.onItemExchangerEvent(event);
        }
        if (releaseEvent) {
            try {
                event.release();
            }
            catch (Exception e) {
                ItemExchanger.m_logger.error((Object)"[Trade] Exception lors de la notification d'un \u00e9v\u00e8nement \u00e0 un user d'un ItemExchanger: ", (Throwable)e);
            }
        }
    }
    
    public ItemExchangeState getCurrentState() {
        return this.m_currentState;
    }
    
    protected abstract boolean needsInvitationStep();
    
    protected abstract boolean needsWaitingUsersReady();
    
    public void acceptInvitation(final long userId) {
        this.acceptInvitation(this.getUserById(userId));
    }
    
    public void acceptInvitation(final User user) {
        if (!this.isUserConcerned(user)) {
            return;
        }
        this.doStartStep();
    }
    
    public void declineInvitation(final User user, final ItemExchangerEndEvent.Reason reason) {
        if (!this.isUserConcerned(user)) {
            return;
        }
        final User otherUser = this.getOtherUser(user);
        assert otherUser != null;
        final ItemExchangerEndEvent eventLocal = ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.INVITATION_LOCALLY_CANCELED);
        eventLocal.setUserId(user.getId());
        final ItemExchangerEndEvent eventRemote = ItemExchangerEndEvent.checkOut(this, reason);
        eventRemote.setUserId(user.getId());
        this.m_currentState = ItemExchangeState.FINISHED;
        this.notifyUser(otherUser.getId(), eventRemote);
        this.notifyUser(user.getId(), eventLocal);
        this.finishExchange();
    }
    
    public void cancelExchangeUserBusy(final long userId) {
        this.cancelExchangeUserBusy(this.getUserById(userId));
    }
    
    public void cancelExchangeUserBusy(final User userBusy) {
        if (!this.isUserConcerned(userBusy)) {
            ItemExchanger.m_logger.warn((Object)("[Trade] Impossible d'annuler l'\u00e9change, " + userBusy.getName() + " n'est pas concern\u00e9"));
            return;
        }
        this.m_currentState = ItemExchangeState.FINISHED;
        this.notifyUser(this.m_requester, ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.INVITATION_IMPOSSIBLE_USER_BUSY));
        this.notifyUser(this.m_target, ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.INVITATION_LOCALLY_CANCELED));
        this.finishExchange();
    }
    
    public void failExchange() {
        this.m_currentState = ItemExchangeState.FINISHED;
        this.notifyUser(this.m_requester, ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.EXCHANGE_FAILED));
        this.notifyUser(this.m_target, ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.EXCHANGE_FAILED));
        this.finishExchange();
    }
    
    public void cancelExchange(final User user) {
        if (!this.isUserConcerned(user)) {
            ItemExchanger.m_logger.error((Object)("[Trade] Impossible de retirer l'utilisateur (\"" + user.getName() + "\", " + user.getId() + ") de l'ExchangerUser : Il n'est pas concern\u00e9 par cet \u00e9change (\"" + this.m_requester.getName() + "\", " + this.m_requester.getId() + "/\"" + this.m_target.getName() + "\", " + this.m_target.getId() + ")."));
            return;
        }
        final User otherUser = this.getOtherUser(user);
        this.m_currentState = ItemExchangeState.FINISHED;
        this.notifyUser(user, ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.LOCALLY_CANCELED));
        this.notifyUser(otherUser, ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.REMOTELY_CANCELED));
        this.finishExchange();
    }
    
    public void acceptExchange(final User user) {
        if (!this.isUserConcerned(user)) {
            ItemExchanger.m_logger.error((Object)("[Trade] Impossible de finir l'\u00e9change " + user + " de l'ExchangerUser n'est pas concern\u00e9 par cet \u00e9change"));
            return;
        }
        if (this.isExchangeValid()) {
            this.doExchange();
            this.m_currentState = ItemExchangeState.FINISHED;
            this.notifyUsers(ItemExchangerEndEvent.checkOut(this, ItemExchangerEndEvent.Reason.EXCHANGE_DONE));
            this.finishExchange();
        }
        else {
            this.cancelExchange(user);
        }
    }
    
    public void setUserReady(final long id, final boolean ready) {
        this.setUserReady(this.getUserById(id), ready);
    }
    
    public void setUserReady(final User user, final boolean ready) {
        if (!this.isUserConcerned(user)) {
            ItemExchanger.m_logger.error((Object)("[Trade] Impossible de continuer l'\u00e9change " + user + " de l'ExchangerUser n'est pas concern\u00e9 par cet \u00e9change"));
            return;
        }
        if (this.needsWaitingUsersReady()) {
            user.setReady(ready);
        }
    }
    
    public void addItemToExchange(final long userId, final ContentType referenceContent, final short quantity) {
        this.addItemToExchange(this.getUserById(userId), referenceContent, quantity);
    }
    
    public void addItemToExchange(final User user, final ContentType referenceContent, final short quantity) {
        if (!this.isUserConcerned(user)) {
            return;
        }
        if (quantity < 1) {
            ItemExchanger.m_logger.error((Object)"[Trade] On essaye d'ajouter une quantit\u00e9 n\u00e9gative ou nulle d'objets \u00e0 l'\u00e9change");
            return;
        }
        this.m_requester.setReady(false);
        this.m_target.setReady(false);
        final ContentType exchangeContent = user.getInExchangeList(referenceContent.getUniqueId());
        if (exchangeContent != null) {
            if (this.isCheckQuantityNeeded() && exchangeContent.getQuantity() + quantity > referenceContent.getQuantity()) {
                ItemExchanger.m_logger.info((Object)"[Trade] On essaye d'ajouter plus de carte qu'il n'en a de disponible dans un \u00e9change");
                return;
            }
            exchangeContent.updateQuantity(quantity);
        }
        else {
            if (this.isCheckQuantityNeeded() && quantity > referenceContent.getQuantity()) {
                ItemExchanger.m_logger.error((Object)"[Trade] On essaye d'\u00e9changer plus de carte qu'il n'en a de disponible");
                return;
            }
            final ContentType clonedContent = (ContentType)referenceContent.getClone();
            clonedContent.setQuantity(quantity);
            user.putInExchangeList(clonedContent.getUniqueId(), clonedContent);
        }
        this.notifyUsers(ItemExchangerModifiedEvent.checkOut(this, ItemExchangerModifiedEvent.Modification.CONTENT_ADDED, user.getId(), referenceContent, quantity));
    }
    
    public void removeItemToExchange(final long userId, final ContentType referenceContent, final short quantity) {
        this.removeItemToExchange(this.getUserById(userId), referenceContent, quantity);
    }
    
    public void removeItemToExchange(final User user, final ContentType referenceContent, final short quantity) {
        if (!this.isUserConcerned(user)) {
            ItemExchanger.m_logger.error((Object)"[Trade] On essaye de retirer un objet de la liste d'un utilisateur non valide");
            return;
        }
        if (quantity < 1) {
            ItemExchanger.m_logger.error((Object)"[Trade] On essaye de retirer une quantit\u00e9 n\u00e9gative ou nulle d'objets \u00e0 l'\u00e9change");
            return;
        }
        this.m_requester.setReady(false);
        this.m_target.setReady(false);
        final ContentType exchangeContent = user.getInExchangeList(referenceContent.getUniqueId());
        if (exchangeContent == null) {
            ItemExchanger.m_logger.error((Object)"[Trade] On essaye de retirer un objet de l'\u00e9change qui n'existe pas");
            return;
        }
        if (exchangeContent.getQuantity() < quantity) {
            ItemExchanger.m_logger.error((Object)"[Trade] On essaie de retirer plus d'objets qu'il n'y en a dans l'\u00e9change");
        }
        else if (exchangeContent.getQuantity() == quantity) {
            exchangeContent.release();
            user.removeFromExchangeList(referenceContent.getUniqueId());
        }
        else {
            exchangeContent.updateQuantity((short)(-quantity));
        }
        this.notifyUsers(ItemExchangerModifiedEvent.checkOut(this, ItemExchangerModifiedEvent.Modification.CONTENT_REMOVED, user.getId(), referenceContent, quantity));
    }
    
    protected boolean isCheckQuantityNeeded() {
        return true;
    }
    
    protected abstract boolean isExchangeValid();
    
    protected abstract void doExchange();
    
    public void cancel(final long userId) {
        this.cancel(this.getUserById(userId));
    }
    
    public void cancel(final User user) {
        if (!this.isUserConcerned(user)) {
            ItemExchanger.m_logger.warn((Object)("[Trade] Impossible d'annuler l'\u00e9change, " + user.getName() + " n'est pas concern\u00e9"));
            return;
        }
        switch (this.m_currentState) {
            case RUNNING: {
                this.cancelExchange(user);
                break;
            }
            case AWAITING_INVITATION_ANSWER: {
                this.declineInvitation(user, ItemExchangerEndEvent.Reason.INVITATION_REMOTELY_CANCELED);
                break;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemExchanger.class);
    }
}
