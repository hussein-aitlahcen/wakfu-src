package com.ankamagames.wakfu.client.sound;

import com.ankamagames.framework.sound.group.music.*;
import com.ankamagames.framework.sound.group.defaultSound.*;
import com.ankamagames.framework.sound.group.field.*;
import com.ankamagames.framework.sound.group.*;
import org.apache.commons.lang3.*;

public enum GameSoundGroup
{
    MUSIC((byte)0, new DefaultSourceGroup("music", (byte)1), new MusicGroup("music", (byte)0), new FieldSourceGroup("ambiance_music", (byte)0), new long[] { 200L, 210L, 220L, 230L, 290L, 702L }, false), 
    SOUND_AMB_2D((byte)1, new DefaultSourceGroup("sound_amb_2d", (byte)1), new MusicGroup("sound_amb_2d", (byte)1, (byte)1), new FieldSourceGroup("sound_amb_2d", (byte)1), new long[] { 700L, 701L, 703L, 704L, 705L, 706L, 707L, 710L, 720L }, true), 
    SOUND_AMB_3D((byte)2, (DefaultSourceGroup)null, (MusicGroup)null, new FieldSourceGroup("sound_amb_3d", (byte)3, (byte)2), new long[] { 800L }, true), 
    SOUND_FIGHT((byte)3, new DefaultSourceGroup("sound_fight", (byte)2, (byte)3), (MusicGroup)null, new FieldSourceGroup("soundFight3D", (byte)2, (byte)3), new long[] { 100L, 300L }, true), 
    GUI((byte)4, new DefaultSourceGroup("gui", (byte)0, (byte)4), (MusicGroup)null, (FieldSourceGroup)null, new long[] { 600L }, false), 
    VOICES((byte)5, new DefaultSourceGroup("voices", (byte)4, (byte)5), (MusicGroup)null, new FieldSourceGroup("voices3D", (byte)4, (byte)5), new long[] { 900L, 910L, 920L, 930L }, true), 
    SFX((byte)6, new DefaultSourceGroup("sfx", (byte)2, (byte)6), (MusicGroup)null, new FieldSourceGroup("sfx3d", (byte)2, (byte)6), new long[] { 501L, 502L, 530L }, true), 
    PARTICLES((byte)7, new DefaultSourceGroup("particles2D", (byte)1, (byte)7), (MusicGroup)null, new FieldSourceGroup("particles", (byte)1, (byte)7), new long[] { 400L, 410L, 420L }, true), 
    FOLEYS((byte)8, new DefaultSourceGroup("foleys2D", (byte)2, (byte)8), (MusicGroup)null, new FieldSourceGroup("foleys", (byte)2, (byte)8), new long[] { 110L, 111L, 120L, 130L, 310L, 320L, 330L }, true);
    
    private final byte m_groupId;
    private final long[] m_idPrefix;
    private final MusicGroup m_musicGroup;
    private final DefaultSourceGroup m_defaultGroup;
    private final FieldSourceGroup m_fieldSourceGroup;
    private final boolean m_worldSoundGroup;
    
    private GameSoundGroup(final byte groupId, final DefaultSourceGroup defaultGroup, final MusicGroup musicGroup, final FieldSourceGroup fieldSourceGroup, final long[] idPrefix, final boolean worldSoundGroup) {
        this.m_groupId = groupId;
        this.m_idPrefix = idPrefix;
        this.m_musicGroup = musicGroup;
        this.m_defaultGroup = defaultGroup;
        this.m_fieldSourceGroup = fieldSourceGroup;
        this.m_worldSoundGroup = worldSoundGroup;
    }
    
    public byte getGroupId() {
        return this.m_groupId;
    }
    
    public MusicGroup getMusicGroup() {
        return this.m_musicGroup;
    }
    
    public DefaultSourceGroup getDefaultGroup() {
        return this.m_defaultGroup;
    }
    
    public FieldSourceGroup getFieldSourceGroup() {
        return this.m_fieldSourceGroup;
    }
    
    public boolean hasPrefixId(final long prefix) {
        for (final long prefixId : this.m_idPrefix) {
            if (prefix == prefixId) {
                return true;
            }
        }
        return false;
    }
    
    public static GameSoundGroup fromId(final byte id) {
        for (final GameSoundGroup group : values()) {
            if (group.m_groupId == id) {
                return group;
            }
        }
        return null;
    }
    
    public static GameSoundGroup fromSourceGroup(final AudioSourceGroup asg) {
        for (final GameSoundGroup group : values()) {
            if (group.m_defaultGroup == asg || group.m_fieldSourceGroup == asg || group.m_musicGroup == asg) {
                return group;
            }
        }
        return null;
    }
    
    public static GameSoundGroup getGameSourceGroupFromPrefix(final long id) {
        for (final GameSoundGroup group : values()) {
            if (ArrayUtils.contains(group.m_idPrefix, id)) {
                return group;
            }
        }
        return null;
    }
    
    public boolean isWorldSoundGroup() {
        return this.m_worldSoundGroup;
    }
    
    public void onEnterWorld() {
        this.setEnabled(true);
    }
    
    public void onBackToLogin() {
        this.setEnabled(false);
    }
    
    private void setEnabled(final boolean enabled) {
        if (this.m_worldSoundGroup) {
            if (this.m_defaultGroup != null) {
                this.m_defaultGroup.setEnabled(enabled);
            }
            if (this.m_musicGroup != null) {
                this.m_musicGroup.setEnabled(enabled);
            }
            if (this.m_fieldSourceGroup != null) {
                this.m_fieldSourceGroup.setEnabled(enabled);
            }
        }
    }
}
