package com.ankamagames.baseImpl.graphics.alea.display;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.worldElements.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.apache.tools.ant.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.util.*;
import gnu.trove.*;

public class ScreenMap
{
    private static final Logger m_logger;
    public static final int MAP_WIDTH = 1024;
    public static final int MAP_HEIGHT = 576;
    private static final TLongArrayList m_indexSortingList;
    private static final ArrayList<ScreenElement> m_sortedElementList;
    final ArrayList<ScreenElement> m_elements;
    final short m_x;
    final short m_y;
    int m_minX;
    int m_minY;
    int m_maxX;
    int m_maxY;
    private int m_coordMinX;
    private int m_coordMinY;
    private short m_coordMinZ;
    private int m_coordMaxX;
    private int m_coordMaxY;
    private short m_coordMaxZ;
    
    public ScreenMap() {
        this(0, 0);
    }
    
    public ScreenMap(final int x, final int y) {
        super();
        this.m_coordMinX = Integer.MAX_VALUE;
        this.m_coordMinY = Integer.MAX_VALUE;
        this.m_coordMinZ = 32767;
        this.m_coordMaxX = Integer.MIN_VALUE;
        this.m_coordMaxY = Integer.MIN_VALUE;
        this.m_coordMaxZ = -32768;
        this.m_elements = new ArrayList<ScreenElement>(1024);
        this.m_minX = Integer.MAX_VALUE;
        this.m_maxX = Integer.MIN_VALUE;
        this.m_minY = Integer.MAX_VALUE;
        this.m_maxY = Integer.MIN_VALUE;
        assert x > -32768 && x < 32767;
        assert y > -32768 && y < 32767;
        this.m_x = (short)x;
        this.m_y = (short)y;
    }
    
    public final void clear() {
        for (int numElements = this.m_elements.size(), i = 0; i < numElements; ++i) {
            final ScreenElement element = this.m_elements.get(i);
            element.removeReference();
        }
        this.m_elements.clear();
    }
    
    public final int getElementsCountForVisibilityMask(final byte visibilityMask) {
        int count = 0;
        for (int i = 0, size = this.m_elements.size(); i < size; ++i) {
            final ScreenElement screenElement = this.m_elements.get(i);
            final byte elementVisibilityMask = screenElement.getCommonProperties().getVisibilityMask();
            if ((elementVisibilityMask & visibilityMask) == elementVisibilityMask) {
                ++count;
            }
        }
        return count;
    }
    
    public final ArrayList<ScreenElement> getElements() {
        return this.m_elements;
    }
    
    public final short getX() {
        return this.m_x;
    }
    
    public final short getY() {
        return this.m_y;
    }
    
    public boolean load(final String path) throws IOException {
        final String mapName = this.getMapName(path);
        final ExtendedDataInputStream bitStream = ExtendedDataInputStream.wrap(WorldMapFileHelper.readFile(mapName));
        this.m_coordMinX = bitStream.readInt();
        this.m_coordMinY = bitStream.readInt();
        this.m_coordMinZ = bitStream.readShort();
        this.m_coordMaxX = bitStream.readInt();
        this.m_coordMaxY = bitStream.readInt();
        this.m_coordMaxZ = bitStream.readShort();
        final int count = bitStream.readShort() & 0xFFFF;
        final int[] groupKeys = new int[count];
        final byte[] layerIndexes = new byte[count];
        final int[] groupIds = new int[count];
        for (int i = 0; i < count; ++i) {
            groupKeys[i] = bitStream.readInt();
            layerIndexes[i] = bitStream.readByte();
            groupIds[i] = bitStream.readInt();
        }
        final float[][] colors = this.readColorTable(bitStream);
        boolean loadOK = true;
        final int mapX = bitStream.readInt();
        final int mapY = bitStream.readInt();
        for (int numRects = bitStream.readShort() & 0xFFFF, j = 0; j < numRects; ++j) {
            final int minX = mapX + (bitStream.readByte() & 0xFF);
            final int maxX = mapX + (bitStream.readByte() & 0xFF);
            final int minY = mapY + (bitStream.readByte() & 0xFF);
            final int maxY = mapY + (bitStream.readByte() & 0xFF);
            for (int cx = minX; cx < maxX; ++cx) {
                for (int cy = minY; cy < maxY; ++cy) {
                    for (int numElements = bitStream.readByte() & 0xFF, elementIndex = 0; elementIndex < numElements; ++elementIndex) {
                        final ScreenElement element = ScreenElement.Factory.newPooledInstance();
                        element.m_cellX = cx;
                        element.m_cellY = cy;
                        loadOK &= element.load(bitStream);
                        final int groupIndex = bitStream.readShort() & 0xFFFF;
                        element.m_groupKey = groupKeys[groupIndex];
                        element.m_layerIndex = layerIndexes[groupIndex];
                        element.m_groupId = groupIds[groupIndex];
                        final int colorIndex = bitStream.readShort() & 0xFFFF;
                        element.m_colors = colors[colorIndex];
                        final ElementProperties properties = element.m_commonData;
                        if (properties != null) {
                            element.computeLocation();
                            element.computeHashCode();
                            this.m_elements.add(element);
                        }
                    }
                }
            }
        }
        bitStream.close();
        this.onAllElementAdded();
        return loadOK;
    }
    
    private float[][] readColorTable(final ExtendedDataInputStream bitStream) throws IOException {
        final int colorCount = bitStream.readShort() & 0xFFFF;
        final float[][] colors = new float[colorCount][];
        for (int i = 0; i < colorCount; ++i) {
            final byte type = bitStream.readByte();
            ScreenElement.readColors(colors[i] = ScreenElement.newColorsFromType(type), type, bitStream);
        }
        return colors;
    }
    
    public final void onAllElementAdded() {
        sortElements(this.m_elements);
        this.updateMapBounds();
    }
    
    private void updateMapBounds() {
        for (int i = 0, numElements = this.m_elements.size(); i < numElements; ++i) {
            final ScreenElement element = this.m_elements.get(i);
            if (element.m_left < this.m_minX) {
                this.m_minX = element.m_left;
            }
            final int imgWidth = element.m_commonData.getImgWidth();
            if (element.m_left + imgWidth > this.m_maxX) {
                this.m_maxX = element.m_left + imgWidth;
            }
            if (element.m_top > this.m_maxY) {
                this.m_maxY = element.m_top;
            }
            final int imgHeight = element.m_commonData.getImgHeight();
            if (element.m_top - imgHeight < this.m_minY) {
                this.m_minY = element.m_top - imgHeight;
            }
        }
    }
    
    private static void sortElements(final ArrayList<ScreenElement> elements) {
        ScreenMap.m_indexSortingList.reset();
        final int numElements = elements.size();
        for (int i = 0; i < numElements; ++i) {
            final ScreenElement screenElement = elements.get(i);
            ScreenMap.m_indexSortingList.add((screenElement.m_hashCode << 14) + i);
        }
        ScreenMap.m_indexSortingList.sort();
        ScreenMap.m_sortedElementList.clear();
        ScreenMap.m_sortedElementList.ensureCapacity(numElements);
        int posToInsert = 0;
        for (int j = 0; j < numElements; ++j) {
            final long code = ScreenMap.m_indexSortingList.getQuick(j);
            final int index = (int)(code & 0x3FFFL);
            if (code < 0L) {
                ScreenMap.m_sortedElementList.add(elements.get(index));
            }
            else {
                ScreenMap.m_sortedElementList.add(posToInsert, elements.get(index));
                ++posToInsert;
            }
        }
        elements.clear();
        elements.addAll(ScreenMap.m_sortedElementList);
    }
    
    @Override
    public final String toString() {
        return "ScreenMap {" + this.m_x + ", " + this.m_y + "}";
    }
    
    int save(final String path, final int[] statsResult) throws IOException {
        final String mapName = this.getMapName(path);
        final OutputBitStream bitStream = new OutputBitStream();
        this.computeBounds();
        final HashSet<GroupInfo> groupInfos = new HashSet<GroupInfo>();
        final HashSet<ColorInfo> colorInfos = new HashSet<ColorInfo>();
        final int numElements = this.m_elements.size();
        for (int i = 0; i < numElements; ++i) {
            final ScreenElement element = this.m_elements.get(i);
            groupInfos.add(new GroupInfo(element));
            colorInfos.add(new ColorInfo(element));
        }
        bitStream.writeInt(this.m_coordMinX);
        bitStream.writeInt(this.m_coordMinY);
        bitStream.writeShort(this.m_coordMinZ);
        bitStream.writeInt(this.m_coordMaxX);
        bitStream.writeInt(this.m_coordMaxY);
        bitStream.writeShort(this.m_coordMaxZ);
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        final TIntObjectHashMap<ArrayList<ScreenElement>> elementsByCells = new TIntObjectHashMap<ArrayList<ScreenElement>>(512);
        for (int j = 0; j < numElements; ++j) {
            final ScreenElement element2 = this.m_elements.get(j);
            final int x = element2.m_cellX;
            final int y = element2.m_cellY;
            if (x < minX) {
                minX = x;
            }
            if (x > maxX) {
                maxX = x;
            }
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
            final int hashCode = MathHelper.getIntFromTwoInt(x, y);
            TroveUtils.insert(elementsByCells, hashCode, element2);
        }
        final ArrayList<GroupInfo> groupInfoList = new ArrayList<GroupInfo>(groupInfos);
        bitStream.writeShort((short)groupInfoList.size());
        for (int k = 0; k < groupInfoList.size(); ++k) {
            groupInfoList.get(k).write(bitStream);
        }
        statsResult[1] = groupInfoList.size();
        final ArrayList<ColorInfo> colorInfoList = new ArrayList<ColorInfo>(colorInfos);
        bitStream.writeShort((short)colorInfoList.size());
        for (int l = 0; l < colorInfoList.size(); ++l) {
            colorInfoList.get(l).write(bitStream);
        }
        statsResult[2] = colorInfoList.size();
        assert maxX - minX <= 255;
        assert maxY - minY <= 255;
        bitStream.writeInt(minX);
        bitStream.writeInt(minY);
        final int sizeX = maxX - minX + 1;
        final int sizeY = maxY - minY + 1;
        final ArrayList<Rect> rectList = new ArrayList<Rect>(sizeX * sizeY);
        final byte[] cells = new byte[sizeY * sizeX];
        final TIntObjectIterator<ArrayList<ScreenElement>> iterator = elementsByCells.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            final ArrayList<ScreenElement> elementList = iterator.value();
            final int numElementsByCell = elementList.size();
            final int hashCode2 = iterator.key();
            final int x2 = MathHelper.getFirstShortFromInt(hashCode2);
            final int y2 = MathHelper.getSecondShortFromInt(hashCode2);
            cells[(y2 - minY) * sizeX + (x2 - minX)] = (byte)numElementsByCell;
        }
        for (int y3 = 0; y3 < sizeY; ++y3) {
            for (int x3 = 0; x3 < sizeX; ++x3) {
                if (cells[y3 * sizeX + x3] != 0) {
                    int rectSizeX = 1;
                    int rectSizeY = 1;
                    for (int cy = y3 + 1; cy < sizeY && cells[cy * sizeX + x3] != 0; ++cy) {
                        ++rectSizeY;
                    }
                    for (int cx = x3 + 1; cx < sizeX; ++cx) {
                        boolean isOk = true;
                        for (int cy2 = y3; cy2 < y3 + rectSizeY; ++cy2) {
                            if (cells[cy2 * sizeX + cx] == 0) {
                                isOk = false;
                                break;
                            }
                        }
                        if (!isOk) {
                            break;
                        }
                        ++rectSizeX;
                    }
                    for (int cy = y3; cy < y3 + rectSizeY; ++cy) {
                        for (int cx2 = x3; cx2 < x3 + rectSizeX; ++cx2) {
                            cells[cy * sizeX + cx2] = 0;
                        }
                    }
                    rectList.add(new Rect(x3, x3 + rectSizeX, y3, y3 + rectSizeY));
                }
            }
        }
        final int numRects = rectList.size();
        bitStream.writeShort((short)numRects);
        for (int m = 0; m < numRects; ++m) {
            final Rect rect = rectList.get(m);
            bitStream.writeByte((byte)rect.m_xMin);
            bitStream.writeByte((byte)rect.m_xMax);
            bitStream.writeByte((byte)rect.m_yMin);
            bitStream.writeByte((byte)rect.m_yMax);
            for (int cx3 = rect.m_xMin; cx3 < rect.m_xMax; ++cx3) {
                for (int cy = rect.m_yMin; cy < rect.m_yMax; ++cy) {
                    final int x4 = cx3 + minX;
                    final int y4 = cy + minY;
                    final int hashCode3 = MathHelper.getIntFromTwoInt(x4, y4);
                    final ArrayList<ScreenElement> elementList2 = elementsByCells.get(hashCode3);
                    final int numElementsInRect = elementList2.size();
                    bitStream.writeByte((byte)numElementsInRect);
                    for (int elementIndex = 0; elementIndex < numElementsInRect; ++elementIndex) {
                        final ScreenElement element3 = elementList2.get(elementIndex);
                        element3.save(bitStream);
                        final int groupIndex = groupInfoList.indexOf(new GroupInfo(element3));
                        if (groupIndex == -1 || groupIndex > 32767) {
                            throw new BuildException("pas d'indexage de groupe");
                        }
                        bitStream.writeShort((short)groupIndex);
                        final int colorIndex = colorInfoList.indexOf(new ColorInfo(element3));
                        if (colorIndex == -1 || colorIndex > 32767) {
                            throw new BuildException("pas d'indexage de couleur");
                        }
                        bitStream.writeShort((short)colorIndex);
                    }
                }
            }
        }
        final FileOutputStream stream = FileHelper.createFileOutputStream(mapName);
        final byte[] data = bitStream.getData();
        stream.write(data);
        bitStream.close();
        stream.close();
        return data.length;
    }
    
    public void computeBounds() {
        for (int numElements = this.m_elements.size(), i = 0; i < numElements; ++i) {
            final ScreenElement element = this.m_elements.get(i);
            if (element.m_cellX < this.m_coordMinX) {
                this.m_coordMinX = element.m_cellX;
            }
            if (element.m_cellX > this.m_coordMaxX) {
                this.m_coordMaxX = element.m_cellX;
            }
            if (element.m_cellY < this.m_coordMinY) {
                this.m_coordMinY = element.m_cellY;
            }
            if (element.m_cellY > this.m_coordMaxY) {
                this.m_coordMaxY = element.m_cellY;
            }
            if (element.m_cellZ < this.m_coordMinZ) {
                this.m_coordMinZ = element.m_cellZ;
            }
            if (element.m_cellZ > this.m_coordMaxZ) {
                this.m_coordMaxZ = element.m_cellZ;
            }
        }
    }
    
    void addElement(final ScreenElement element) {
        this.m_elements.add(element);
    }
    
    boolean isInMap(final int x, final int y) {
        return x >= this.m_coordMinX && x <= this.m_coordMaxX && y >= this.m_coordMinY && y <= this.m_coordMaxY;
    }
    
    boolean isInMap(final int x, final int y, final short z) {
        return x >= this.m_coordMinX && x <= this.m_coordMaxX && y >= this.m_coordMinY && y <= this.m_coordMaxY && z >= this.m_coordMinZ && z <= this.m_coordMaxZ;
    }
    
    private String getMapName(final String path) {
        return path + this.m_x + "_" + this.m_y;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ScreenMap.class);
        m_indexSortingList = new TLongArrayList(2048);
        m_sortedElementList = new ArrayList<ScreenElement>();
    }
    
    private static class GroupInfo
    {
        final int m_groupKey;
        final short m_layerIndex;
        final int m_groupId;
        
        GroupInfo(final ScreenElement element) {
            super();
            this.m_groupKey = element.m_groupKey;
            this.m_layerIndex = element.m_layerIndex;
            this.m_groupId = element.m_groupId;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GroupInfo)) {
                return false;
            }
            final GroupInfo groupInfo = (GroupInfo)o;
            return this.m_groupId == groupInfo.m_groupId && this.m_groupKey == groupInfo.m_groupKey && this.m_layerIndex == groupInfo.m_layerIndex;
        }
        
        @Override
        public int hashCode() {
            int result = this.m_groupKey;
            result = 31 * result + this.m_layerIndex;
            result = 31 * result + this.m_groupId;
            return result;
        }
        
        public void write(final OutputBitStream bitStream) throws IOException {
            bitStream.writeInt(this.m_groupKey);
            bitStream.writeByte((byte)this.m_layerIndex);
            bitStream.writeInt(this.m_groupId);
        }
    }
    
    private static class ColorInfo
    {
        final float[] m_colors;
        final byte m_type;
        
        ColorInfo(final ScreenElement element) {
            super();
            this.m_colors = element.m_colors;
            this.m_type = element.m_type;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ColorInfo)) {
                return false;
            }
            final ColorInfo colorInfo = (ColorInfo)o;
            return this.m_type == colorInfo.m_type && Arrays.equals(this.m_colors, colorInfo.m_colors);
        }
        
        @Override
        public int hashCode() {
            int result = Arrays.hashCode(this.m_colors);
            result = 31 * result + this.m_type;
            return result;
        }
        
        public void write(final OutputBitStream bitStream) throws IOException {
            bitStream.writeByte(this.m_type);
            for (int i = 0; i < this.m_colors.length; ++i) {
                bitStream.writeByte((byte)this.m_colors[i]);
            }
        }
    }
}
