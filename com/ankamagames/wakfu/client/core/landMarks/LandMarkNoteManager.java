package com.ankamagames.wakfu.client.core.landMarks;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.core.world.*;
import gnu.trove.*;

public class LandMarkNoteManager
{
    private static final Logger m_logger;
    private static final LandMarkNoteManager INSTANCE;
    private final TIntObjectHashMap<LandMarkNote> m_notes;
    private static final int CURRENT_VERSION = 1;
    private int m_guid;
    
    private LandMarkNoteManager() {
        super();
        this.m_notes = new TIntObjectHashMap<LandMarkNote>();
        this.m_guid = 0;
    }
    
    public static LandMarkNoteManager getInstance() {
        return LandMarkNoteManager.INSTANCE;
    }
    
    private void addNote(final LandMarkNote note, final TIntObjectHashMap<LandMarkNote> notes) {
        note.setId(this.m_guid++);
        notes.put(note.getId(), note);
    }
    
    public LandMarkNote addNote(final int x, final int y, final String note, final int gfxId) {
        final LandMarkNote noteObj = new LandMarkNote(this.m_guid++, x, y, note, gfxId);
        this.addNote(noteObj, this.m_notes);
        this.saveNotes();
        return noteObj;
    }
    
    public boolean removeNote(final LandMarkNote note) {
        final boolean removed = this.m_notes.remove(note.getId()) != null;
        if (removed) {
            this.saveNotes();
        }
        return removed;
    }
    
    public boolean removeNote(final int id) {
        final boolean removed = this.m_notes.remove(id) != null;
        if (removed) {
            this.saveNotes();
        }
        return removed;
    }
    
    public TIntObjectHashMap<LandMarkNote> getDisplayedNotes(final short instanceId) {
        final short paperMapId = this.getPaperMapId(instanceId);
        if (paperMapId == this.getPaperMapId()) {
            return this.m_notes;
        }
        final TIntObjectHashMap<LandMarkNote> notes = new TIntObjectHashMap<LandMarkNote>();
        this.loadNotes(paperMapId, notes);
        return notes;
    }
    
    public void loadNotes() {
        this.loadNotes(this.getPaperMapId(), this.m_notes);
    }
    
    public void saveNotes() {
        saveNotes(this.getPaperMapId(), this.m_notes);
    }
    
    private void loadNotes(final short instanceId, final TIntObjectHashMap<LandMarkNote> notes) {
        notes.clear();
        if (instanceId == 0) {
            return;
        }
        final String path = getInstanceFile(instanceId);
        try {
            final ExtendedDataInputStream istream = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(path));
            int version = -1;
            if (istream.readInt() == -1) {
                version = istream.readInt();
            }
            else {
                istream.setOffset(0);
            }
            for (int numNotes = istream.readInt(), i = 0; i < numNotes; ++i) {
                final LandMarkNote note = new LandMarkNote();
                note.load(istream, version);
                this.addNote(note, notes);
            }
        }
        catch (IOException e) {
            LandMarkNoteManager.m_logger.warn((Object)("probl\u00e8me \u00e0 l'ouverture des notes de carte : " + e.getMessage()));
        }
    }
    
    private static void saveNotes(final short instanceId, final TIntObjectHashMap<LandMarkNote> notes) {
        final String path = getInstanceFile(instanceId);
        try {
            final FileOutputStream fileOutputStream = FileHelper.createFileOutputStream(path);
            final OutputBitStream ostream = new OutputBitStream(fileOutputStream);
            ostream.writeInt(-1);
            ostream.writeInt(1);
            ostream.writeInt(notes.size());
            final TIntObjectIterator<LandMarkNote> it = notes.iterator();
            while (it.hasNext()) {
                it.advance();
                it.value().save(ostream, 1);
            }
            ostream.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            LandMarkNoteManager.m_logger.warn((Object)("probl\u00e8me \u00e0 la sauvegarde des notes de carte :" + e.getMessage()));
        }
    }
    
    private static String getInstanceFile(final short instanceId) {
        return WakfuClientConfigurationManager.getInstance().getInstanceDirectory(instanceId) + "/notes.dat";
    }
    
    public TIntObjectIterator<LandMarkNote> iterator() {
        return this.m_notes.iterator();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LandMarkNoteManager");
        sb.append("{}");
        return sb.toString();
    }
    
    private short getPaperMapId() {
        final short mapId = MapManagerHelper.getWorldId();
        if (MapManager.getInstance().isHavenWorld()) {
            return mapId;
        }
        return this.getPaperMapId(mapId);
    }
    
    private short getPaperMapId(final short mapId) {
        final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo(mapId);
        return (worldInfo == null) ? mapId : worldInfo.m_papermapId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)LandMarkNoteManager.class);
        INSTANCE = new LandMarkNoteManager();
    }
}
