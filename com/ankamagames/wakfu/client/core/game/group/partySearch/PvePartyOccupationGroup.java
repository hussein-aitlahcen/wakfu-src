package com.ankamagames.wakfu.client.core.game.group.partySearch;

import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;

public class PvePartyOccupationGroup extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String DUNGEON = "dungeon";
    public static final String MONSTER = "monster";
    private final int m_familyId;
    private final DungeonPartyOccupationView m_dungeon;
    private final MonsterPartyOccupationView m_monster;
    
    public PvePartyOccupationGroup(final int familyId, final Iterable<PvePartyOccupation> occupations, final short level, @Nullable final PartyRequester partyRequester, final boolean registration) {
        super();
        this.m_familyId = familyId;
        DungeonPartyOccupationView dungeon = null;
        MonsterPartyOccupationView monster = null;
        for (final PvePartyOccupation occupation : occupations) {
            if (occupation.getOccupationType() == PartyOccupationType.DUNGEON) {
                dungeon = new DungeonPartyOccupationView(this, occupation, level, registration);
                if (partyRequester == null || !partyRequester.hasOccupation(occupation)) {
                    continue;
                }
                dungeon.setSelected(true);
            }
            else {
                if (occupation.getOccupationType() != PartyOccupationType.MONSTER) {
                    continue;
                }
                monster = new MonsterPartyOccupationView(this, occupation, level, registration);
                if (partyRequester == null || !partyRequester.hasOccupation(occupation)) {
                    continue;
                }
                monster.setSelected(true);
            }
        }
        this.m_dungeon = dungeon;
        this.m_monster = monster;
    }
    
    public int getFamilyId() {
        return this.m_familyId;
    }
    
    public short getLevel() {
        if (this.m_dungeon == null) {
            return this.m_monster.getOccupation().getLevel();
        }
        if (this.m_monster == null) {
            return this.m_dungeon.getOccupation().getLevel();
        }
        return MathHelper.minShort(this.m_monster.getOccupation().getLevel(), this.m_dungeon.getOccupation().getLevel());
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(38, this.m_familyId, new Object[0]);
    }
    
    public DungeonPartyOccupationView getDungeon() {
        return this.m_dungeon;
    }
    
    public MonsterPartyOccupationView getMonster() {
        return this.m_monster;
    }
    
    @Override
    public String[] getFields() {
        return PvePartyOccupationGroup.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("name".equals(fieldName)) {
            return WakfuTranslator.getInstance().getString(38, this.m_familyId, new Object[0]);
        }
        if ("dungeon".equals(fieldName)) {
            return this.m_dungeon;
        }
        if ("monster".equals(fieldName)) {
            return this.m_monster;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "PvePartyOccupationGroup{m_familyId=" + this.m_familyId + ", m_dungeon=" + this.m_dungeon + ", m_monster=" + this.m_monster + '}';
    }
    
    public boolean containsName(final String searchString) {
        if (searchString == null || searchString.isEmpty()) {
            return true;
        }
        final String string = searchString.toLowerCase();
        return (this.m_dungeon != null && this.m_dungeon.getName().toLowerCase().contains(string)) || (this.m_monster != null && this.m_monster.getName().toLowerCase().contains(string)) || this.getName().toLowerCase().contains(string);
    }
    
    public boolean hasOccupation(final PartyOccupation occupation) {
        return (this.m_monster != null && this.m_monster.getOccupation() == occupation) || (this.m_dungeon != null && this.m_dungeon.getOccupation() == occupation);
    }
    
    public void computeActivationByLevel(final short maxLevel) {
        if (this.m_monster != null && this.m_monster.getLevel() > maxLevel) {
            this.m_monster.m_enabled = false;
        }
        else if (this.m_monster != null) {
            this.m_monster.m_enabled = true;
        }
        if (this.m_dungeon != null && this.m_dungeon.getLevel() > maxLevel) {
            this.m_dungeon.m_enabled = false;
        }
        else if (this.m_dungeon != null) {
            this.m_dungeon.m_enabled = true;
        }
    }
    
    public boolean hasSelection() {
        final boolean monsterIsSelected = this.m_monster != null && this.m_monster.isSelected();
        final boolean dungeonIsSelected = this.m_dungeon != null && this.m_dungeon.isSelected();
        return monsterIsSelected || dungeonIsSelected;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PvePartyOccupationGroup that = (PvePartyOccupationGroup)o;
        return this.m_familyId == that.m_familyId;
    }
    
    public boolean isLevelValid(final short occupationMinLevel, final short occupationMaxLevel) {
        return (this.m_dungeon != null && this.m_dungeon.getLevel() >= occupationMinLevel && this.m_dungeon.getLevel() <= occupationMaxLevel) || (this.m_monster != null && this.m_monster.getLevel() >= occupationMinLevel && this.m_monster.getLevel() <= occupationMaxLevel);
    }
}
