package com.ankamagames.wakfu.client.sound.ambiance2D.exporter;

import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class RawSoundStrata
{
    public int m_id;
    public String m_name;
    
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        this.m_id = istream.readInt();
        this.m_name = istream.readString();
    }
    
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        ostream.writeInt(this.m_id);
        ostream.writeString(this.m_name);
    }
    
    public void loadAsProperties(@NotNull final ExtendedDataInputStream istream) throws IOException {
        this.load(istream);
    }
    
    public void saveAsProperties(@NotNull final OutputBitStream ostream) throws IOException {
        this.save(ostream);
    }
    
    @Override
    public String toString() {
        return this.m_name + " (id=" + this.m_id + ")";
    }
}
