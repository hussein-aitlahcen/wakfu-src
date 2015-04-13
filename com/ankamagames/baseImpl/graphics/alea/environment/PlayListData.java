package com.ankamagames.baseImpl.graphics.alea.environment;

import com.ankamagames.framework.sound.group.music.*;
import java.util.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class PlayListData
{
    public static final byte DAY_PLAY_LIST = 1;
    public static final byte NIGHT_PLAY_LIST = 2;
    public static final byte FIGHT_PLAY_LIST = 3;
    private short m_playListId;
    private ArrayList<MusicData> m_dayMusics;
    private ArrayList<MusicData> m_nightMusics;
    private MusicData m_dayAmbience;
    private MusicData m_nightAmbience;
    private MusicData m_fight;
    private MusicData m_bossFight;
    private boolean m_loopPlayList;
    
    public void addMusicData(final int type, final long id, final long alternativeId, final byte volume, final short silence, final byte order, final int duration) {
        this.addMusicData(type, new MusicData(id, alternativeId, volume, silence, order, duration));
    }
    
    private void addMusicData(final int type, final MusicData data) {
        switch (data.getOrder()) {
            case 0: {
                this.setAmbience(type, data);
                break;
            }
            case -1: {
                this.setFight(type, data);
                break;
            }
            case -2: {
                this.setBossFight(type, data);
                break;
            }
            default: {
                this.addMusic(type, data);
                break;
            }
        }
    }
    
    private void addMusic(final int type, final MusicData data) {
        ArrayList<MusicData> musics;
        if (type == 1) {
            if (this.m_dayMusics == null) {
                this.m_dayMusics = new ArrayList<MusicData>();
            }
            musics = this.m_dayMusics;
        }
        else {
            if (type != 2) {
                return;
            }
            if (this.m_nightMusics == null) {
                this.m_nightMusics = new ArrayList<MusicData>();
            }
            musics = this.m_nightMusics;
        }
        musics.add(data);
        Collections.sort(musics, MusicDataOrderComparator.COMPARATOR);
    }
    
    private void setAmbience(final int type, final MusicData data) {
        if (type == 1) {
            this.m_dayAmbience = data;
        }
        else if (type == 2) {
            this.m_nightAmbience = data;
        }
    }
    
    private void setFight(final int type, final MusicData data) {
        this.m_fight = data;
    }
    
    private void setBossFight(final int type, final MusicData data) {
        this.m_bossFight = data;
    }
    
    public ArrayList<MusicData> getDayMusics() {
        return this.m_dayMusics;
    }
    
    public ArrayList<MusicData> getNightMusics() {
        return this.m_nightMusics;
    }
    
    public MusicData getDayAmbience() {
        return this.m_dayAmbience;
    }
    
    public MusicData getNightAmbience() {
        return this.m_nightAmbience;
    }
    
    public MusicData getFight() {
        return this.m_fight;
    }
    
    public MusicData getBossFight() {
        return this.m_bossFight;
    }
    
    public short getPlayListId() {
        return this.m_playListId;
    }
    
    public void setPlayListId(final short playListId) {
        this.m_playListId = playListId;
    }
    
    public void setLoopPlaylist(final boolean loop) {
        this.m_loopPlayList = loop;
    }
    
    public boolean isLoopPlaylist() {
        return this.m_loopPlayList;
    }
    
    final void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        this.m_playListId = istream.readShort();
        short musicNumber = istream.readShort();
        for (int i = 0; i < musicNumber; ++i) {
            final MusicData musicData = new MusicData();
            musicData.load(istream);
            this.addMusicData(1, musicData);
        }
        musicNumber = istream.readShort();
        for (int i = 0; i < musicNumber; ++i) {
            final MusicData musicData = new MusicData();
            musicData.load(istream);
            this.addMusicData(2, musicData);
        }
        musicNumber = istream.readShort();
        for (int i = 0; i < musicNumber; ++i) {
            final MusicData musicData = new MusicData();
            musicData.load(istream);
            this.addMusicData(3, musicData);
        }
        this.m_loopPlayList = istream.readBooleanBit();
    }
    
    final void save(@NotNull final OutputBitStream ostream) throws IOException {
        ostream.writeShort(this.m_playListId);
        short num = 0;
        if (this.m_dayMusics != null) {
            num += (short)this.m_dayMusics.size();
        }
        if (this.m_dayAmbience != null) {
            ++num;
        }
        ostream.writeShort(num);
        if (this.m_dayMusics != null) {
            for (int i = 0, size = this.m_dayMusics.size(); i < size; ++i) {
                this.m_dayMusics.get(i).save(ostream);
            }
        }
        if (this.m_dayAmbience != null) {
            this.m_dayAmbience.save(ostream);
        }
        num = 0;
        if (this.m_nightMusics != null) {
            num += (short)this.m_nightMusics.size();
        }
        if (this.m_nightAmbience != null) {
            ++num;
        }
        ostream.writeShort(num);
        if (this.m_nightMusics != null) {
            for (int i = 0, size = this.m_nightMusics.size(); i < size; ++i) {
                this.m_nightMusics.get(i).save(ostream);
            }
        }
        if (this.m_nightAmbience != null) {
            this.m_nightAmbience.save(ostream);
        }
        num = (short)(((this.m_fight != null) ? 1 : 0) + ((this.m_bossFight != null) ? 1 : 0));
        ostream.writeShort(num);
        if (this.m_fight != null) {
            this.m_fight.save(ostream);
        }
        if (this.m_bossFight != null) {
            this.m_bossFight.save(ostream);
        }
        ostream.writeBooleanBit(this.m_loopPlayList);
    }
    
    public boolean isEqualTo(final PlayListData data) {
        if (data == this) {
            return true;
        }
        if (data.m_dayMusics != null && this.m_dayMusics != null) {
            if (data.m_dayMusics.size() != this.m_dayMusics.size()) {
                return false;
            }
            for (int i = 0, size = this.m_dayMusics.size(); i < size; ++i) {
                if (!this.m_dayMusics.get(i).equals(data.m_dayMusics.get(i))) {
                    return false;
                }
            }
        }
        else if (data.m_dayMusics != null || this.m_dayMusics != null) {
            return false;
        }
        if (data.m_nightMusics != null && this.m_nightMusics != null) {
            if (data.m_nightMusics.size() != this.m_nightMusics.size()) {
                return false;
            }
            for (int i = 0, size = this.m_nightMusics.size(); i < size; ++i) {
                if (!this.m_nightMusics.get(i).equals(data.m_nightMusics.get(i))) {
                    return false;
                }
            }
        }
        else if (data.m_nightMusics != null || this.m_nightMusics != null) {
            return false;
        }
        if (this.m_dayAmbience != null && data.m_dayAmbience != null) {
            if (!this.m_dayAmbience.equals(data.m_dayAmbience)) {
                return false;
            }
        }
        else if (this.m_dayAmbience != null || data.m_dayAmbience != null) {
            return false;
        }
        if (this.m_nightAmbience != null && data.m_nightAmbience != null) {
            if (!this.m_nightAmbience.equals(data.m_nightAmbience)) {
                return false;
            }
        }
        else if (this.m_nightAmbience != null || data.m_nightAmbience != null) {
            return false;
        }
        if (this.m_fight != null && data.m_fight != null) {
            if (!this.m_fight.equals(data.m_fight)) {
                return false;
            }
        }
        else if (this.m_fight != null || data.m_fight != null) {
            return false;
        }
        if (this.m_bossFight != null && data.m_bossFight != null) {
            if (!this.m_bossFight.equals(data.m_bossFight)) {
                return false;
            }
        }
        else if (this.m_bossFight != null || data.m_bossFight != null) {
            return false;
        }
        return this.m_loopPlayList == data.m_loopPlayList;
    }
    
    private static class MusicDataOrderComparator implements Comparator<MusicData>
    {
        private static final MusicDataOrderComparator COMPARATOR;
        
        @Override
        public int compare(final MusicData o1, final MusicData o2) {
            return o1.getOrder() - o2.getOrder();
        }
        
        static {
            COMPARATOR = new MusicDataOrderComparator();
        }
    }
}
