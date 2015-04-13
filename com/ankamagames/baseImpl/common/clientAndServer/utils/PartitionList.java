package com.ankamagames.baseImpl.common.clientAndServer.utils;

import org.apache.log4j.*;
import org.jdom.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.nio.*;
import gnu.trove.*;

public class PartitionList
{
    private static final Logger m_logger;
    protected final TIntHashSet m_maps;
    
    public PartitionList() {
        super();
        this.m_maps = new TIntHashSet();
    }
    
    public PartitionList(final TIntHashSet maps) {
        super();
        this.m_maps = maps;
    }
    
    public final boolean isEmpty() {
        return this.m_maps.isEmpty();
    }
    
    public void add(final PartitionList partitionList) {
        final TIntIterator iter = partitionList.m_maps.iterator();
        while (iter.hasNext()) {
            this.m_maps.add(iter.next());
        }
    }
    
    public final void add(final int x, final int y) {
        this.m_maps.add(hash(x, y));
    }
    
    public final boolean contains(final int x, final int y) {
        return this.m_maps.contains(hash(x, y));
    }
    
    public final boolean removeAll(final PartitionList partitions) {
        return this.m_maps.removeAll(partitions.m_maps.toArray());
    }
    
    public final boolean remove(final int x, final int y) {
        return this.m_maps.remove(hash(x, y));
    }
    
    public TIntHashSet list() {
        return this.m_maps;
    }
    
    public Element writeXml() {
        final Element node = new Element("partitions");
        for (final int value : this.m_maps) {
            final int x = MathHelper.getFirstShortFromInt(value);
            final int y = MathHelper.getSecondShortFromInt(value);
            final Element subNode = new Element("partition");
            subNode.setAttribute("x", String.valueOf(x));
            subNode.setAttribute("y", String.valueOf(y));
            node.addContent(subNode);
        }
        return node;
    }
    
    public void readFromXml(final Element node) {
        assert node.getName().equals("partitions");
        final List children = node.getChildren("partition");
        for (int i = 0; i < children.size(); ++i) {
            final Element partition = children.get(i);
            final int x = Integer.parseInt(partition.getAttributeValue("x"));
            final int y = Integer.parseInt(partition.getAttributeValue("y"));
            this.add(x, y);
        }
        this.m_maps.compact();
    }
    
    public void writeToStream(final OutputBitStream output) throws IOException {
        final int count = this.m_maps.size();
        output.writeInt(count);
        final TIntIterator iter = this.m_maps.iterator();
        for (int i = 0; i < count; ++i) {
            output.writeInt(iter.next());
        }
    }
    
    public void readFrom(final ByteBuffer buffer) {
        for (int count = buffer.getInt(), i = 0; i < count; ++i) {
            this.m_maps.add(buffer.getInt());
        }
        this.m_maps.compact();
    }
    
    protected static int hash(final int x, final int y) {
        return MathHelper.getIntFromTwoInt(x, y);
    }
    
    static {
        m_logger = Logger.getLogger((Class)PartitionList.class);
    }
}
