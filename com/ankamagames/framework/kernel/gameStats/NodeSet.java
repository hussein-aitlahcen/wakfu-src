package com.ankamagames.framework.kernel.gameStats;

import org.apache.log4j.*;
import java.util.*;
import java.nio.*;

public final class NodeSet
{
    public static final NodeSet EMPTY;
    protected static final Logger m_logger;
    private static final Comparator<Node> NODE_COMPARATOR;
    private final List<Node> m_allNodes;
    private final List<SimplePropertyNode> m_propertyNodes;
    private final List<ContainerNode> m_containerNodes;
    
    static NodeSet singleton(final Node node) {
        final NodeSet set = new NodeSet();
        set.add(node);
        return set;
    }
    
    NodeSet() {
        super();
        this.m_allNodes = new ArrayList<Node>();
        this.m_propertyNodes = new ArrayList<SimplePropertyNode>();
        this.m_containerNodes = new ArrayList<ContainerNode>();
    }
    
    final void add(final Node node) {
        this.m_allNodes.add(node);
        if (node.hasValue()) {
            this.m_propertyNodes.add((SimplePropertyNode)node);
        }
        else {
            this.m_containerNodes.add((ContainerNode)node);
        }
    }
    
    final void addAll(final Collection<Node> nodes) {
        for (final Node node : nodes) {
            this.add(node);
        }
    }
    
    final void remove(final Node node) {
        this.m_allNodes.remove(node);
        if (node.hasValue()) {
            this.m_propertyNodes.remove(node);
        }
        else {
            this.m_containerNodes.remove(node);
        }
    }
    
    public final int size() {
        return this.m_allNodes.size();
    }
    
    public List<Node> getAllNodes(final boolean sorted) {
        if (sorted) {
            Collections.sort(this.m_allNodes, NodeSet.NODE_COMPARATOR);
        }
        return this.m_allNodes;
    }
    
    public List<Node> getAllNodes() {
        return this.getAllNodes(false);
    }
    
    public List<SimplePropertyNode> getPropertyNodes(final boolean sorted) {
        if (sorted) {
            Collections.sort(this.m_propertyNodes, NodeSet.NODE_COMPARATOR);
        }
        return this.m_propertyNodes;
    }
    
    public List<SimplePropertyNode> getPropertyNodes() {
        return this.getPropertyNodes(false);
    }
    
    public List<ContainerNode> getContainerNodes() {
        return this.m_containerNodes;
    }
    
    public boolean isEmpty() {
        return this.m_allNodes.isEmpty();
    }
    
    public boolean hasDirtyNode() {
        for (int i = 0, size = this.m_allNodes.size(); i < size; ++i) {
            final Node node = this.m_allNodes.get(i);
            if (node.isDirty()) {
                return true;
            }
        }
        return false;
    }
    
    public void clear() {
        this.m_allNodes.clear();
        this.m_propertyNodes.clear();
        this.m_containerNodes.clear();
    }
    
    public final byte[] serializePropertyNodes() {
        int totalSize = 2;
        for (int i = 0, size = this.m_propertyNodes.size(); i < size; ++i) {
            final SimplePropertyNode node = this.m_propertyNodes.get(i);
            totalSize += 2 + node.getEncodedFullName().length + 8;
        }
        final ByteBuffer buffer = ByteBuffer.allocate(totalSize);
        buffer.putShort((short)this.m_propertyNodes.size());
        for (int j = 0, size2 = this.m_propertyNodes.size(); j < size2; ++j) {
            final SimplePropertyNode node2 = this.m_propertyNodes.get(j);
            final byte[] encodedName = node2.getEncodedFullName();
            buffer.putShort((short)encodedName.length);
            buffer.put(encodedName);
            buffer.putLong(node2.getValue());
        }
        assert buffer.remaining() == 0 : "Buffer non-rempli totalement";
        return buffer.array();
    }
    
    public final List<byte[]> serializePropertyNodes(final int dataBlockMaxSize) {
        int totalSize = 2;
        final ArrayList<SimplePropertyNode> nodes = new ArrayList<SimplePropertyNode>();
        final ArrayList<byte[]> data = new ArrayList<byte[]>();
        for (int i = 0, size = this.m_propertyNodes.size(); i < size; ++i) {
            final SimplePropertyNode node = this.m_propertyNodes.get(i);
            final int propSize = 2 + node.getEncodedFullName().length + 8;
            if (totalSize + propSize >= dataBlockMaxSize) {
                final byte[] serializedData = this.serializePropertyNodes(nodes, totalSize);
                data.add(serializedData);
                nodes.clear();
                totalSize = 2;
            }
            totalSize += propSize;
            nodes.add(node);
        }
        if (!nodes.isEmpty()) {
            final byte[] serializedData2 = this.serializePropertyNodes(nodes, totalSize);
            data.add(serializedData2);
        }
        return data;
    }
    
    private byte[] serializePropertyNodes(final List<SimplePropertyNode> nodes, final int totalSize) {
        final ByteBuffer buffer = ByteBuffer.allocate(totalSize);
        buffer.putShort((short)nodes.size());
        for (int i = 0, size = nodes.size(); i < size; ++i) {
            final SimplePropertyNode node = nodes.get(i);
            final byte[] encodedName = node.getEncodedFullName();
            buffer.putShort((short)encodedName.length);
            buffer.put(encodedName);
            buffer.putLong(node.getValue());
        }
        assert buffer.remaining() == 0 : "Buffer non-rempli totalement";
        return buffer.array();
    }
    
    static {
        EMPTY = new NodeSet();
        m_logger = Logger.getLogger((Class)NodeSet.class);
        NODE_COMPARATOR = new Comparator<Node>() {
            @Override
            public int compare(final Node o1, final Node o2) {
                if (o1 == null || o1.getFullName() == null) {
                    return -1;
                }
                if (o2 == null || o2.getFullName() == null) {
                    return 1;
                }
                return o1.getFullName().compareTo(o2.getFullName());
            }
        };
    }
}
