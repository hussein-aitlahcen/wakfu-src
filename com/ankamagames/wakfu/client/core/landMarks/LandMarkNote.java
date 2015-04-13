package com.ankamagames.wakfu.client.core.landMarks;

import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class LandMarkNote
{
    private static final byte CURRENT_VERSION = 2;
    private int m_id;
    private int m_x;
    private int m_y;
    private int m_gfxId;
    private String m_note;
    
    LandMarkNote() {
        super();
    }
    
    public LandMarkNote(final int id, final int x, final int y, final String note, final int gfxId) {
        super();
        this.m_id = id;
        this.m_x = x;
        this.m_y = y;
        this.m_gfxId = gfxId;
        this.m_note = note;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public void setId(final int id) {
        this.m_id = id;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public void setX(final int x) {
        this.m_x = x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public void setY(final int y) {
        this.m_y = y;
    }
    
    public String getNote() {
        return this.m_note;
    }
    
    public void setNote(final String note) {
        this.m_note = note;
    }
    
    public void setGfxId(final int gfxId) {
        this.m_gfxId = gfxId;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public void load(final ExtendedDataInputStream is, final int version) throws IOException {
        this.m_x = is.readInt();
        this.m_y = is.readInt();
        this.m_note = is.readString();
        if (version > -1) {
            this.m_gfxId = is.readInt();
        }
    }
    
    public void save(final OutputBitStream os, final int version) throws IOException {
        os.writeInt(this.m_x);
        os.writeInt(this.m_y);
        os.writeString(this.m_note);
        if (version > -1) {
            os.writeInt(this.m_gfxId);
        }
    }
    
    @Override
    public String toString() {
        return this.m_note;
    }
}
