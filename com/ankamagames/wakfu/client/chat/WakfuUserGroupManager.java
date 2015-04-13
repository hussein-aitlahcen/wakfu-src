package com.ankamagames.wakfu.client.chat;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;

public class WakfuUserGroupManager implements FieldProvider
{
    private static final Logger m_logger;
    public static final String CATEGORIES_LIST = "categories";
    public static final String FRIENDS_LIST = "friendsList";
    public static final String IGNORE_LIST = "ignoreList";
    public static final String[] FIELDS;
    private EnumMap<ContactListCategoryType, ContactListCategory> m_categories;
    private static WakfuUserGroupManager m_instance;
    private HashMap<String, WakfuUser> m_list;
    private boolean m_displayDisconnectedPlayers;
    
    private WakfuUserGroupManager() {
        super();
        this.m_categories = new EnumMap<ContactListCategoryType, ContactListCategory>(ContactListCategoryType.class);
        this.m_list = new HashMap<String, WakfuUser>();
        this.m_displayDisconnectedPlayers = true;
        this.init();
    }
    
    public static WakfuUserGroupManager getInstance() {
        return WakfuUserGroupManager.m_instance;
    }
    
    private void init() {
        this.m_categories.put(ContactListCategoryType.FRIEND, new ContactListCategory(ContactListCategoryType.FRIEND));
        this.m_categories.put(ContactListCategoryType.IGNORE, new ContactListCategory(ContactListCategoryType.IGNORE));
    }
    
    public void clear() {
        this.m_list.clear();
        for (final ContactListCategory cat : this.m_categories.values()) {
            cat.clear();
        }
        this.m_displayDisconnectedPlayers = true;
    }
    
    public void addUser(final short type, WakfuUser user) {
        if (!this.m_list.containsKey(user.getName().toLowerCase())) {
            user.EnableType(type);
            this.m_list.put(user.getName().toLowerCase(), user);
        }
        else {
            user = this.m_list.get(user.getName().toLowerCase());
            user.EnableType(type);
        }
        final ContactListCategory cat = this.m_categories.get(ContactListCategoryType.getById(type));
        if (cat != null) {
            cat.addUser(user);
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "categories");
        }
    }
    
    public void addUsers(final short type, final Iterable<WakfuUser> users) {
        for (final WakfuUser user : users) {
            this.addUser(type, user);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "categories");
    }
    
    public boolean removeUser(final short type, final String userName) {
        final ContactListCategory cat = this.m_categories.get(ContactListCategoryType.getById(type));
        if (cat != null) {
            cat.removeUser(userName);
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "categories");
        }
        if (this.m_list.containsKey(userName.toLowerCase())) {
            final WakfuUser user = this.m_list.get(userName.toLowerCase());
            user.DisableType(type);
            if (type == 1) {
                user.setOnline(false);
            }
            if (user.hasNoType()) {
                this.m_list.remove(userName.toLowerCase());
            }
            return true;
        }
        return false;
    }
    
    public void removeUsers(final short type, final Iterable<WakfuUser> users) {
        for (final WakfuUser user : users) {
            this.removeUser(type, user.getName());
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "categories");
    }
    
    public void switchUser(final short type, final WakfuUser user) {
        if (this.m_list.containsKey(user.getName().toLowerCase())) {
            if (user.isType(type)) {
                this.sendRemoveMessage(type, user.getName());
            }
            else {
                this.sendAddMessage(type, user.getName());
            }
        }
        else {
            this.sendAddMessage(type, user.getName());
        }
    }
    
    public void switchUsers(final short type, final Iterable<WakfuUser> users) {
        for (final WakfuUser user : users) {
            this.switchUser(type, user);
        }
    }
    
    public Collection<WakfuUser> getUsers() {
        return this.m_list.values();
    }
    
    public ContactListCategory getFriendGroup() {
        return this.m_categories.get(ContactListCategoryType.FRIEND);
    }
    
    public ContactListCategory getIgnoreGroup() {
        return this.m_categories.get(ContactListCategoryType.IGNORE);
    }
    
    public WakfuUser getUser(final String name) {
        return this.m_list.get(name.toLowerCase());
    }
    
    public WakfuUser getUserByCharacterName(final String characterName) {
        if (characterName == null) {
            return null;
        }
        for (final WakfuUser wakfuUser : this.m_list.values()) {
            if (characterName.equals(wakfuUser.getCharacterName())) {
                return wakfuUser;
            }
        }
        return null;
    }
    
    public boolean isDisplayDisconnectedPlayers() {
        return this.m_displayDisconnectedPlayers;
    }
    
    public void setDisplayDisconnectedPlayers(final boolean displayDisconnectedPlayers) {
        this.m_displayDisconnectedPlayers = displayDisconnectedPlayers;
        PropertiesProvider.getInstance().firePropertyValueChanged(this.getFriendGroup(), "contentList");
        PropertiesProvider.getInstance().firePropertyValueChanged(this.getIgnoreGroup(), "contentList");
    }
    
    @Override
    public String[] getFields() {
        return WakfuUserGroupManager.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("categories")) {
            final ArrayList<ContactListCategory> contactListCategories = this.getNotEmptyCategories();
            return (contactListCategories.size() > 0) ? contactListCategories : null;
        }
        if (fieldName.equals("friendsList")) {
            return this.getFriendGroup();
        }
        if (fieldName.equals("ignoreList")) {
            return this.getIgnoreGroup();
        }
        return null;
    }
    
    private ArrayList<ContactListCategory> getNotEmptyCategories() {
        final ArrayList<ContactListCategory> categories = new ArrayList<ContactListCategory>();
        for (final ContactListCategory contactListCategory : this.m_categories.values()) {
            if (contactListCategory != null && contactListCategory.getUsersCount() > 0) {
                categories.add(contactListCategory);
            }
        }
        return categories;
    }
    
    public void sendAddMessage(final short type, final String name) {
        switch (type) {
            case 1: {
                this.sendAddFriendMessage(name);
                break;
            }
            case 2: {
                this.sendAddIgnoreMessage(name);
                break;
            }
        }
    }
    
    public void sendRemoveMessage(final short type, final String name) {
        switch (type) {
            case 1: {
                this.sendRemoveFriendMessage(name);
                break;
            }
            case 2: {
                this.sendRemoveIgnoreMessage(name);
                break;
            }
        }
    }
    
    public void sendAddFriendMessage(final String name) {
        final AddFriendMessage privateMessage = new AddFriendMessage();
        privateMessage.setFriendName(name);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(privateMessage);
    }
    
    public void sendRemoveFriendMessage(final String name) {
        final RemoveFriendMessage privateMessage = new RemoveFriendMessage();
        privateMessage.setFriendName(name);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(privateMessage);
    }
    
    public void sendAddIgnoreMessage(final String name) {
        final AddIgnoreMessage privateMessage = new AddIgnoreMessage();
        privateMessage.setIgnoreName(name);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(privateMessage);
    }
    
    public void sendRemoveIgnoreMessage(final String name) {
        final RemoveIgnoreMessage privateMessage = new RemoveIgnoreMessage();
        privateMessage.setIgnoreName(name);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(privateMessage);
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
    
    static {
        m_logger = Logger.getLogger((Class)WakfuUserGroupManager.class);
        FIELDS = new String[] { "categories", "friendsList", "ignoreList" };
        WakfuUserGroupManager.m_instance = new WakfuUserGroupManager();
    }
}
