package com.ankamagames.wakfu.client.chat;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;

public class ContactListCategory implements FieldProvider, Iterable<WakfuUser>
{
    public static final String NAME_FIELD = "name";
    public static final String TYPE_FIELD = "type";
    public static final String CONTENT_LIST_FIELD = "contentList";
    public static final String ONLINE_CONTENT_LIST_FIELD = "onlineContentList";
    public static final String OFFLINE_CONTENT_LIST_FIELD = "offlineContentList";
    public static final String[] FIELDS;
    private final ContactListCategoryType m_type;
    private ArrayList<WakfuUser> m_users;
    
    public ContactListCategory(final ContactListCategoryType type) {
        super();
        this.m_users = new ArrayList<WakfuUser>();
        this.m_type = type;
    }
    
    public void addUser(final WakfuUser user) {
        if (!this.m_users.contains(user)) {
            this.m_users.add(user);
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "contentList", "onlineContentList", "offlineContentList");
        }
    }
    
    public boolean removeUser(final WakfuUser user) {
        if (user != null) {
            if (this.m_type == ContactListCategoryType.FRIEND) {
                user.setOnline(false);
            }
            this.m_users.remove(user);
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "contentList", "onlineContentList", "offlineContentList");
            return true;
        }
        return false;
    }
    
    public boolean removeUser(final String userName) {
        return this.removeUser(this.getUser(userName));
    }
    
    public boolean contains(final WakfuUser user) {
        return this.m_users.contains(user);
    }
    
    public WakfuUser getUserById(final long clientId) {
        for (int size = this.m_users.size(), i = 0; i < size; ++i) {
            final WakfuUser user = this.m_users.get(i);
            if (user.getId() == clientId) {
                return user;
            }
        }
        return null;
    }
    
    public WakfuUser getUser(final WakfuUser user) {
        if (user == null) {
            return null;
        }
        for (int size = this.m_users.size(), i = 0; i < size; ++i) {
            final WakfuUser userToCheck = this.m_users.get(i);
            if (user.getName().equalsIgnoreCase(userToCheck.getName())) {
                return userToCheck;
            }
            if (user.getCharacterName().equalsIgnoreCase(userToCheck.getCharacterName())) {
                return userToCheck;
            }
            if (user.getId() != -1L && user.getId() == userToCheck.getId()) {
                return userToCheck;
            }
        }
        return null;
    }
    
    public WakfuUser getUser(final String name) {
        for (int size = this.m_users.size(), i = 0; i < size; ++i) {
            final WakfuUser user = this.m_users.get(i);
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }
    
    public WakfuUser getUserByCharacterName(final String name) {
        for (int size = this.m_users.size(), i = 0; i < size; ++i) {
            final WakfuUser user = this.m_users.get(i);
            if (user.getCharacterName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }
    
    @Override
    public String[] getFields() {
        return ContactListCategory.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_type.getName();
        }
        if (fieldName.equals("type")) {
            return this.m_type.getId();
        }
        if (fieldName.equals("contentList")) {
            ArrayList<WakfuUser> users = new ArrayList<WakfuUser>();
            if (WakfuUserGroupManager.getInstance().isDisplayDisconnectedPlayers()) {
                users = this.m_users;
            }
            else {
                users = this.getOnlineUsers(true);
            }
            Collections.sort(users, new Comparator() {
                @Override
                public int compare(final Object o1, final Object o2) {
                    if (o1 != null && o2 != null && o1 instanceof WakfuUser && o2 instanceof WakfuUser) {
                        final WakfuUser wakfuUser1 = (WakfuUser)o1;
                        final WakfuUser wakfuUser2 = (WakfuUser)o2;
                        if (wakfuUser1.isOnline() && !wakfuUser2.isOnline()) {
                            return -1;
                        }
                        if (!wakfuUser1.isOnline() && wakfuUser2.isOnline()) {
                            return 1;
                        }
                        if (wakfuUser1.getName() != null && wakfuUser2.getName() != null) {
                            return wakfuUser1.getName().compareTo(wakfuUser2.getName());
                        }
                    }
                    return 0;
                }
            });
            return users;
        }
        if (fieldName.equals("onlineContentList")) {
            return this.getOnlineUsers(true);
        }
        if (fieldName.equals("offlineContentList")) {
            return this.getOnlineUsers(false);
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
    
    public void clear() {
        this.m_users.clear();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "contentList", "onlineContentList", "offlineContentList");
    }
    
    @Override
    public Iterator<WakfuUser> iterator() {
        return this.m_users.iterator();
    }
    
    public int getUsersCount() {
        return this.m_users.size();
    }
    
    public void cancelEditions() {
        for (final WakfuUser user : this.m_users) {
            user.setComentaryEdition(false);
        }
    }
    
    public ArrayList<WakfuUser> getOnlineUsers(final boolean online) {
        final ArrayList users = new ArrayList();
        for (final WakfuUser wakfuUser : this.m_users) {
            if ((online && wakfuUser.isOnline()) || (!online && !wakfuUser.isOnline())) {
                users.add(wakfuUser);
            }
        }
        Collections.sort((List<Object>)users, new Comparator() {
            @Override
            public int compare(final Object o1, final Object o2) {
                if (o1 != null && o2 != null && o1 instanceof WakfuUser && o2 instanceof WakfuUser) {
                    final WakfuUser wakfuUser1 = (WakfuUser)o1;
                    final WakfuUser wakfuUser2 = (WakfuUser)o2;
                    if (wakfuUser1.getName() != null && wakfuUser2.getName() != null) {
                        return wakfuUser1.getName().compareTo(wakfuUser2.getName());
                    }
                }
                return 0;
            }
        });
        return (ArrayList<WakfuUser>)users;
    }
    
    static {
        FIELDS = new String[] { "name", "type", "contentList", "onlineContentList", "offlineContentList" };
    }
}
