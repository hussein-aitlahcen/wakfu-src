package com.ankamagames.wakfu.client.sound.ambiance2D.exporter;

import java.util.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class SoundContainerExporter
{
    private final ArrayList<RawSoundStrata> m_stratas;
    private final ArrayList<RawSoundContainer> m_rawSoundContainers;
    
    public SoundContainerExporter() {
        super();
        this.m_stratas = new ArrayList<RawSoundStrata>();
        this.m_rawSoundContainers = new ArrayList<RawSoundContainer>();
    }
    
    public ArrayList<RawSoundStrata> getSoundStratas() {
        return this.m_stratas;
    }
    
    public void addSoundStrata(final RawSoundStrata rss) {
        this.m_stratas.add(rss);
    }
    
    public void removeSoundStrata(final RawSoundStrata strata) {
        this.m_stratas.remove(strata);
    }
    
    public ArrayList<RawSoundContainer> getSoundContainers() {
        return this.m_rawSoundContainers;
    }
    
    public void addSoundContainer(final RawSoundContainer rsc) {
        this.m_rawSoundContainers.add(rsc);
    }
    
    public void removeSoundContainer(final RawSoundContainer rsc) {
        this.m_rawSoundContainers.remove(rsc);
    }
    
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        int size = istream.readInt();
        this.m_stratas.ensureCapacity(size);
        for (int i = 0; i < size; ++i) {
            final RawSoundStrata strata = new RawSoundStrata();
            strata.load(istream);
            this.m_stratas.add(strata);
        }
        size = istream.readInt();
        this.m_rawSoundContainers.ensureCapacity(size);
        for (int i = 0; i < size; ++i) {
            final RawSoundContainer container = new RawSoundContainer();
            container.load(istream);
            this.m_rawSoundContainers.add(container);
        }
    }
    
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        ostream.writeInt(this.m_stratas.size());
        for (int i = 0, size = this.m_stratas.size(); i < size; ++i) {
            this.m_stratas.get(i).save(ostream);
        }
        ostream.writeInt(this.m_rawSoundContainers.size());
        for (int i = 0, size = this.m_rawSoundContainers.size(); i < size; ++i) {
            this.m_rawSoundContainers.get(i).save(ostream);
        }
    }
    
    public void loadAsProperties(@NotNull final ExtendedDataInputStream istream) throws IOException {
        int size = istream.readInt();
        this.m_stratas.ensureCapacity(size);
        for (int i = 0; i < size; ++i) {
            final RawSoundStrata strata = new RawSoundStrata();
            strata.loadAsProperties(istream);
            this.m_stratas.add(strata);
        }
        size = istream.readInt();
        this.m_rawSoundContainers.ensureCapacity(size);
        for (int i = 0; i < size; ++i) {
            final RawSoundContainer container = new RawSoundContainer();
            container.loadAsProperties(istream);
            this.m_rawSoundContainers.add(container);
        }
    }
    
    public void saveAsProperties(@NotNull final OutputBitStream ostream) throws IOException {
        ostream.writeInt(this.m_stratas.size());
        for (int i = 0, size = this.m_stratas.size(); i < size; ++i) {
            this.m_stratas.get(i).saveAsProperties(ostream);
        }
        ostream.writeInt(this.m_rawSoundContainers.size());
        for (int i = 0, size = this.m_rawSoundContainers.size(); i < size; ++i) {
            this.m_rawSoundContainers.get(i).saveAsProperties(ostream);
        }
    }
    
    public RawSoundContainer getRawSoundContainerById(final int id) {
        for (int i = this.m_rawSoundContainers.size() - 1; i >= 0; --i) {
            final RawSoundContainer rsc = this.m_rawSoundContainers.get(i);
            if (rsc.m_id == id) {
                return rsc;
            }
        }
        return null;
    }
    
    public RawSoundStrata getRawSoundStrataById(final int id) {
        for (int i = this.m_stratas.size() - 1; i >= 0; --i) {
            final RawSoundStrata rsc = this.m_stratas.get(i);
            if (rsc.m_id == id) {
                return rsc;
            }
        }
        return null;
    }
}
