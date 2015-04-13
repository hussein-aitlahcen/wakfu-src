package com.ankamagames.baseImpl.graphics.alea.environment;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import gnu.trove.*;

public class AmbienceMap
{
    protected static final Logger m_logger;
    public TIntObjectHashMap<TIntHashSet> m_ambienceMap;
    
    public void init() {
        this.m_ambienceMap = new TIntObjectHashMap<TIntHashSet>();
    }
    
    public void reset() {
        if (this.m_ambienceMap != null) {
            this.m_ambienceMap.clear();
        }
    }
    
    public void add(final int ambienceId, final int x, final int y) {
        TIntHashSet list = this.m_ambienceMap.get(ambienceId);
        if (list == null) {
            list = new TIntHashSet();
            this.m_ambienceMap.put(ambienceId, list);
        }
        final int val = getHash(x, y);
        if (!list.contains(val)) {
            list.add(val);
        }
    }
    
    public TIntHashSet getMapsFromAmbienceId(final int ambienceId) {
        if (this.m_ambienceMap != null) {
            return this.m_ambienceMap.get(ambienceId);
        }
        return null;
    }
    
    public static int getHash(final int x, final int y) {
        return MathHelper.getIntFromTwoInt(x, y);
    }
    
    public static short getX(final int hash) {
        return MathHelper.getFirstShortFromInt(hash);
    }
    
    public static short getY(final int hash) {
        return MathHelper.getSecondShortFromInt(hash);
    }
    
    public void load(final ExtendedDataInputStream is) {
        final int ambienceSize = is.readInt();
        this.m_ambienceMap = new TIntObjectHashMap<TIntHashSet>(ambienceSize);
        for (int i = 0; i < ambienceSize; ++i) {
            final int ambienceId = is.readInt();
            final int listSize = is.readInt();
            if (listSize != 0) {
                final TIntHashSet list = new TIntHashSet(listSize);
                this.m_ambienceMap.put(ambienceId, list);
                for (int j = 0; j < listSize; ++j) {
                    list.add(is.readInt());
                }
            }
        }
    }
    
    public void save(final OutputBitStream os) {
        try {
            os.writeInt(this.m_ambienceMap.size());
            if (!this.m_ambienceMap.isEmpty()) {
                this.m_ambienceMap.forEachEntry(new TIntObjectProcedure<TIntHashSet>() {
                    @Override
                    public boolean execute(final int a, final TIntHashSet b) {
                        try {
                            os.writeInt(a);
                            os.writeInt(b.size());
                            final TIntIterator it = b.iterator();
                            while (it.hasNext()) {
                                os.writeInt(it.next());
                            }
                        }
                        catch (IOException e) {
                            AmbienceMap.m_logger.error((Object)"Exception", (Throwable)e);
                            return false;
                        }
                        return true;
                    }
                });
            }
        }
        catch (IOException e) {
            AmbienceMap.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AmbienceMap.class);
    }
    
    public interface AmbianceMapProcedure
    {
        void addMap(int p0, short p1, short p2);
    }
}
