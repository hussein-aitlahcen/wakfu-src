package com.ankamagames.wakfu.client.core.game.background.gazette;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.background.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;

public class GazetteManager
{
    private static final Logger m_logger;
    public static final GazetteManager INSTANCE;
    private static final int VERSION = 0;
    private final TIntObjectHashMap<Boolean> m_gazettes;
    private final ArrayList<GazetteView> m_views;
    private int m_unreadGazettes;
    
    private GazetteManager() {
        super();
        this.m_gazettes = new TIntObjectHashMap<Boolean>();
        this.m_views = new ArrayList<GazetteView>();
        this.m_unreadGazettes = 0;
    }
    
    public void init() {
        this.m_gazettes.clear();
        this.m_views.clear();
        final ArrayList<BackgroundDisplayData> gazettes = BackgroundDisplayManager.INSTANCE.getBackgroundDisplayDataOfType(BackgroundDisplayType.GAZETTE);
        for (int i = 0, size = gazettes.size(); i < size; ++i) {
            this.m_gazettes.put(gazettes.get(i).getId(), false);
        }
        this.m_unreadGazettes = gazettes.size();
        this.loadGazettes();
        this.createViews();
        PropertiesProvider.getInstance().setPropertyValue("gazettes", this.m_views);
        PropertiesProvider.getInstance().setPropertyValue("gazettes.notRead", this.m_unreadGazettes);
    }
    
    public boolean isRead(final int gazetteId) {
        final Boolean read = this.m_gazettes.get(gazetteId);
        return read != null && read;
    }
    
    public void readGazette(final int gazetteId) {
        if (!this.m_gazettes.contains(gazetteId) || this.isRead(gazetteId)) {
            return;
        }
        this.m_gazettes.put(gazetteId, true);
        this.saveGazettes();
        --this.m_unreadGazettes;
        final GazetteView view = this.getView(gazetteId);
        view.refreshUI();
        PropertiesProvider.getInstance().setPropertyValue("gazettes.notRead", this.m_unreadGazettes);
    }
    
    private void loadGazettes() {
        final String path = getFile();
        try {
            final byte[] data = ContentFileHelper.readFile(path);
            final ExtendedDataInputStream istream = ExtendedDataInputStream.wrap(data);
            final int version = istream.readInt();
            for (int numReadGazettes = istream.readInt(), i = 0; i < numReadGazettes; ++i) {
                final int gazetteId = istream.readInt();
                final boolean read = istream.readBooleanBit();
                if (this.m_gazettes.contains(gazetteId)) {
                    this.m_gazettes.put(gazetteId, read);
                    if (read) {
                        --this.m_unreadGazettes;
                    }
                }
            }
            istream.close();
        }
        catch (IOException e) {
            GazetteManager.m_logger.warn((Object)"Impossible de charger le fichier de gazettes");
        }
    }
    
    private void saveGazettes() {
        final String path = getFile();
        try {
            final FileOutputStream fileOutputStream = FileHelper.createFileOutputStream(path);
            final OutputBitStream ostream = new OutputBitStream(fileOutputStream);
            ostream.writeInt(0);
            ostream.writeInt(this.m_gazettes.size());
            final TIntObjectIterator<Boolean> it = this.m_gazettes.iterator();
            while (it.hasNext()) {
                it.advance();
                final int gazetteId = it.key();
                final boolean read = it.value();
                ostream.writeInt(gazetteId);
                ostream.writeBooleanBit(read);
            }
            ostream.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            GazetteManager.m_logger.warn((Object)"Impossible de sauver le fichier de gazettes");
        }
    }
    
    private void createViews() {
        final TIntObjectIterator<Boolean> it = this.m_gazettes.iterator();
        while (it.hasNext()) {
            it.advance();
            final BackgroundDisplayData data = BackgroundDisplayManager.INSTANCE.getBackgroundDisplayData(it.key());
            final GazetteView view = new GazetteView(data, GazetteConstants.getUnlockDate(data.getId()));
            this.m_views.add(view);
        }
        Collections.sort(this.m_views, GazetteComparator.ID_COMPARATOR_INDIRECT);
        int i = this.m_views.size() - 1;
        final int size = this.m_views.size();
        while (i >= 0) {
            this.m_views.get(i).setIssueNumber(size - i);
            --i;
        }
    }
    
    private GazetteView getView(final int id) {
        for (int i = this.m_views.size() - 1; i >= 0; --i) {
            final GazetteView view = this.m_views.get(i);
            if (view.getId() == id) {
                return view;
            }
        }
        return null;
    }
    
    private static String getFile() {
        return WakfuClientConfigurationManager.getInstance().getAccountDirectory() + "/gazettes.dat";
    }
    
    static {
        m_logger = Logger.getLogger((Class)GazetteManager.class);
        INSTANCE = new GazetteManager();
    }
}
