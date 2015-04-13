package com.ankamagames.baseImpl.client.proxyclient.base.game.achievements;

import java.util.*;

public final class Category
{
    private final int m_id;
    private final Category m_parent;
    private final String m_name;
    private ArrayList<Category> m_children;
    private ArrayList<Achievement> m_achievements;
    
    Category(final int id, final Category parent, final String name) {
        super();
        this.m_id = id;
        this.m_name = ((name != null) ? name.intern() : null);
        this.m_parent = parent;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public Category getParent() {
        return this.m_parent;
    }
    
    public ArrayList<Category> getChildren() {
        return this.m_children;
    }
    
    public ArrayList<Achievement> getAchievements() {
        return this.m_achievements;
    }
    
    void addChildCategory(final Category category) {
        if (this.m_children == null) {
            this.m_children = new ArrayList<Category>();
        }
        if (!this.m_children.contains(category)) {
            this.m_children.add(category);
        }
    }
    
    void addAchievement(final Achievement achievement) {
        if (this.m_achievements == null) {
            this.m_achievements = new ArrayList<Achievement>();
        }
        if (!this.m_achievements.contains(achievement)) {
            this.m_achievements.add(achievement);
        }
    }
    
    public boolean hasParentInHierarchy(final int categoryId) {
        return categoryId == this.m_id || (this.m_parent != null && this.m_parent.hasParentInHierarchy(categoryId));
    }
}
