package com.ankamagames.baseImpl.client.proxyclient.base.chat.userGroup;

public class User implements Comparable<User>
{
    private String m_name;
    private String m_characterName;
    private boolean m_online;
    private long m_id;
    
    public User(final String characterName, final String name, final boolean online, final long id) {
        super();
        this.m_online = false;
        this.m_characterName = characterName;
        this.m_name = name;
        this.m_online = online;
        this.m_id = id;
    }
    
    public User(final long id, final String name, final String characterName) {
        this(characterName, name, false, id);
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public boolean isOnline() {
        return this.m_online;
    }
    
    public void setOnline(final boolean online) {
        this.m_online = online;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public void setId(final long id) {
        this.m_id = id;
    }
    
    public String getCharacterName() {
        return this.m_characterName;
    }
    
    public void setCharacterName(final String characterName) {
        this.m_characterName = characterName;
    }
    
    @Override
    public int compareTo(final User user) {
        return this.getName().toLowerCase().compareTo(user.getName().toLowerCase());
    }
}
