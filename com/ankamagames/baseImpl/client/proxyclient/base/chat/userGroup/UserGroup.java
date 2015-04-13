package com.ankamagames.baseImpl.client.proxyclient.base.chat.userGroup;

import java.util.*;

public class UserGroup
{
    private HashMap<String, User> m_users;
    
    public UserGroup() {
        super();
        this.m_users = new HashMap<String, User>();
    }
    
    public HashMap<String, User> getUsers() {
        return this.m_users;
    }
    
    public void addUser(final User user) {
        this.m_users.put(user.getName(), user);
    }
    
    public void addUsers(final Iterable<User> users) {
        for (final User user : users) {
            this.addUser(user);
        }
    }
    
    public boolean removeUser(final User user) {
        return this.m_users.remove(user.getName()) != null;
    }
    
    public boolean removeUser(final String userName) {
        if (this.m_users.containsKey(userName)) {
            this.m_users.remove(userName);
            return true;
        }
        return false;
    }
    
    public User getUser(final String userName) {
        return this.m_users.get(userName);
    }
    
    public String format() {
        final StringBuilder s = new StringBuilder("");
        for (final User u : this.getUsers().values()) {
            s.append(" +").append(u.getName()).append(" (");
            if (u.isOnline()) {
                s.append("onLine");
            }
            else {
                s.append("offLine");
            }
            s.append(")\n");
        }
        return s.toString();
    }
    
    public boolean contains(final String name) {
        return this.m_users.containsKey(name);
    }
}
