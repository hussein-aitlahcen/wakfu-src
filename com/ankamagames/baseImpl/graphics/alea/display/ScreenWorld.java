package com.ankamagames.baseImpl.graphics.alea.display;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.io.*;
import java.util.*;

public class ScreenWorld
{
    public static final int CELL_WIDTH = 86;
    public static final int CELL_HEIGHT = 43;
    public static final int ELEVATION_UNIT = 10;
    private static final int WIDTH_MAX = 5;
    private static final int HEIGHT_MAX = 7;
    private static final int CACHE_SIZE = 35;
    private static final float SCREEN_WIDTH_RATIO = 9.765625E-4f;
    private static final float SCREEN_HEIGHT_RATIO = 0.0017361111f;
    private static final Logger m_logger;
    final IntObjectLightWeightMap<ScreenMap> m_maps;
    private final ArrayList<ScreenMap> m_mapsUsed;
    protected final ScreenWorldHelper m_cache;
    private float m_centerX;
    private float m_centerY;
    private final BoundHelper m_boundHelper;
    
    public ScreenWorld() {
        super();
        this.m_maps = new IntObjectLightWeightMap<ScreenMap>();
        this.m_mapsUsed = new ArrayList<ScreenMap>();
        this.m_cache = new ScreenWorldHelper();
        this.m_boundHelper = new BoundHelper();
    }
    
    public void initialize(final int numMaps) {
        this.m_maps.ensureCapacity(numMaps);
    }
    
    public void clear() {
        for (final ScreenMap map : this.m_maps) {
            if (map != null) {
                map.clear();
            }
        }
        this.m_maps.clear();
        this.m_mapsUsed.clear();
        this.m_cache.clear();
        this.m_boundHelper.reset();
    }
    
    boolean boundChanged() {
        return this.m_boundHelper.m_boundChanged;
    }
    
    float getCenterX() {
        return this.m_centerX;
    }
    
    float getCenterY() {
        return this.m_centerY;
    }
    
    public int mapCount() {
        return this.m_maps.size();
    }
    
    void loadMaps(final String path, int top, int left, int bottom, int right, @NotNull final PartitionExistValidator mapsCoordinates) {
        top = MathHelper.fastCeil(top * 0.0017361111f);
        bottom = MathHelper.fastFloor(bottom * 0.0017361111f);
        left = MathHelper.fastFloor(left * 9.765625E-4f);
        right = MathHelper.fastCeil(right * 9.765625E-4f);
        if (!this.m_boundHelper.boundChanged(top, bottom, left, right)) {
            this.m_cache.compact(this.m_centerX, this.m_centerY, 35, this.m_mapsUsed);
            return;
        }
        this.m_centerX = (left + right) / 2.0f;
        this.m_centerY = (top + bottom) / 2.0f;
        this.m_mapsUsed.clear();
        if (left <= -32768 || right >= 32767 || bottom <= -32768 || top >= 32767) {
            ScreenWorld.m_logger.error((Object)("on ne devrait pas \u00eatre l\u00e0!! chargement de la map: " + top + "," + left));
        }
        else {
            final int w = right - left;
            if (w > 5) {
                final int offset = (w - 5) / 2;
                right -= offset;
                left += offset;
            }
            final int h = top - bottom;
            if (h > 7) {
                final int offset2 = (h - 7) / 2;
                top -= offset2;
                bottom += offset2;
            }
            for (int y = bottom; y <= top; ++y) {
                for (int x = left; x <= right; ++x) {
                    final int hashCode = MathHelper.getIntFromTwoInt(x, y);
                    if (mapsCoordinates.partitionExists(x, y)) {
                        ScreenMap map = this.m_cache.getMap(hashCode);
                        if (map == null) {
                            map = this.loadMap(path, x, y);
                            this.m_cache.put(hashCode, map);
                        }
                        this.m_mapsUsed.add(map);
                    }
                }
            }
        }
        this.m_maps.clear();
        for (int i = this.m_mapsUsed.size() - 1; i >= 0; --i) {
            try {
                final ScreenMap map2 = this.m_mapsUsed.get(i);
                final int hashCode2 = MathHelper.getIntFromTwoShort(map2.m_x, map2.m_y);
                this.m_maps.put(hashCode2, map2);
            }
            catch (Exception e) {
                ScreenWorld.m_logger.error((Object)"Probl\u00e8me avec la map ", (Throwable)e);
            }
        }
    }
    
    protected ScreenMap loadMap(final String path, final int x, final int y) {
        final ScreenMap map = new ScreenMap(x, y);
        try {
            map.load(path);
        }
        catch (FileNotFoundException ex) {
            ScreenWorld.m_logger.error((Object)("file not found to load map (" + x + "; " + y + ")"));
        }
        catch (IOException e) {
            ScreenWorld.m_logger.error((Object)("Unable to load map (" + x + "; " + y + ")"), (Throwable)e);
        }
        return map;
    }
    
    public void save(final String exportPath) throws IOException {
        final Iterator<ScreenMap> iterator = this.m_maps.iterator();
        final int[] statsResult = new int[4];
        final int[] maxResult = new int[statsResult.length];
        final int[] totalResult = new int[statsResult.length];
        final int[] minResult = new int[statsResult.length];
        Arrays.fill(minResult, Integer.MAX_VALUE);
        while (iterator.hasNext()) {
            final ScreenMap map = iterator.next();
            statsResult[3] = map.save(exportPath, statsResult);
            statsResult[0] = map.m_elements.size();
            for (int i = 0; i < statsResult.length; ++i) {
                final int[] array = totalResult;
                final int n = i;
                array[n] += statsResult[i];
                if (statsResult[i] < minResult[i]) {
                    minResult[i] = statsResult[i];
                }
                if (statsResult[i] > maxResult[i]) {
                    maxResult[i] = statsResult[i];
                }
            }
        }
        final float mapCount = this.m_maps.size();
        ScreenWorld.m_logger.info((Object)("exportPath = " + exportPath + " mapCount=" + mapCount));
        ScreenWorld.m_logger.info((Object)("Num elements = " + totalResult[0] + "\tsize= " + totalResult[3] + "octets"));
        ScreenWorld.m_logger.info((Object)("Avg elements by maps = " + totalResult[0] / mapCount + "\tsize = " + totalResult[3] / mapCount + "\tNum Groupe = " + totalResult[1] / mapCount + "\tnumColors = " + totalResult[2] / mapCount));
        ScreenWorld.m_logger.info((Object)("Max elements by maps = " + maxResult[0] + "\tsize = " + maxResult[3] + "\tNum Groupe = " + maxResult[1] + "\tnumColors = " + maxResult[2]));
        ScreenWorld.m_logger.info((Object)("Min elements by maps = " + minResult[0] + "\tsize = " + minResult[3] + "\tNum Groupe = " + minResult[1] + "\tnumColors = " + minResult[2]));
    }
    
    public static int isoToScreenX(final int isoLocalX, final int isoLocalY) {
        return (int)((isoLocalX - isoLocalY) * 86 * 0.5f);
    }
    
    public static int isoToScreenY(final int isoLocalX, final int isoLocalY, final int isoAltitude) {
        return (int)(-(isoLocalX + isoLocalY) * 21.5f) + isoAltitude * 10;
    }
    
    public static float isoToScreenXf(final int isoLocalX, final int isoLocalY) {
        return MathHelper.fastFloor((isoLocalX - isoLocalY) * 86 * 0.5f);
    }
    
    public static float isoToScreenYf(final int isoLocalX, final int isoLocalY, final int isoAltitude) {
        final float offsetY = (Math.abs(isoLocalX) % 2 == Math.abs(isoLocalY) % 2) ? 0.0f : 0.5f;
        return MathHelper.fastFloor(-(isoLocalX + isoLocalY) * 21.5f) + isoAltitude * 10 + offsetY;
    }
    
    public void addElementToMap(final int x, final int y, final ScreenElement screenElement) {
        addElementToMap(this.m_maps, x, y, screenElement);
    }
    
    protected static ScreenMap addElementToMap(final IntObjectLightWeightMap<ScreenMap> maps, final int x, final int y, final ScreenElement screenElement) {
        final int hashCode = MathHelper.getIntFromTwoInt(x, y);
        ScreenMap map = maps.get(hashCode);
        if (map == null) {
            map = new ScreenMap(x, y);
            maps.put(hashCode, map);
        }
        map.addElement(screenElement);
        return map;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ScreenWorld.class);
    }
    
    private static class BoundHelper
    {
        private int m_top;
        private int m_bottom;
        private int m_left;
        private int m_right;
        private boolean m_boundChanged;
        
        private BoundHelper() {
            super();
            this.m_boundChanged = true;
            this.reset();
        }
        
        private void reset() {
            final int n = Integer.MAX_VALUE;
            this.m_right = n;
            this.m_left = n;
            this.m_bottom = n;
            this.m_top = n;
            this.m_boundChanged = true;
        }
        
        public boolean boundChanged(final int top, final int bottom, final int left, final int right) {
            this.m_boundChanged = (this.m_top != top || this.m_bottom != bottom || this.m_left != left || this.m_right != right);
            if (this.m_boundChanged) {
                this.m_top = top;
                this.m_bottom = bottom;
                this.m_left = left;
                this.m_right = right;
            }
            return this.m_boundChanged;
        }
    }
}
