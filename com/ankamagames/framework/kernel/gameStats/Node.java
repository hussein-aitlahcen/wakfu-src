package com.ankamagames.framework.kernel.gameStats;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.regex.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public abstract class Node
{
    public static boolean DEBUG;
    protected static final Logger m_logger;
    private static final Pattern SUBPACKAGE_PATTERN;
    private final String m_name;
    protected final Node m_parent;
    protected final MergeMode m_mergeMode;
    private boolean m_dirty;
    private final String m_fullName;
    private final byte[] m_encodedFullName;
    
    Node(final String name, final Node parent, final MergeMode mergeMode) {
        super();
        this.m_name = name;
        this.m_parent = parent;
        this.m_mergeMode = mergeMode;
        this.m_dirty = true;
        if (this.m_parent != null) {
            this.m_fullName = this.m_parent.getFullName() + "." + this.m_name;
        }
        else {
            this.m_fullName = name;
        }
        this.m_encodedFullName = StringUtils.toUTF8(this.m_fullName);
    }
    
    public final String getName() {
        return this.m_name;
    }
    
    public final String getFullName() {
        return this.m_fullName;
    }
    
    public final byte[] getEncodedFullName() {
        return this.m_encodedFullName;
    }
    
    public final Node getParent() {
        return this.m_parent;
    }
    
    public final MergeMode getMergeMode() {
        return this.m_mergeMode;
    }
    
    protected final void touch() {
        this.m_dirty = true;
        if (this.m_parent != null) {
            this.m_parent.touch();
        }
    }
    
    public boolean isDirty() {
        return this.m_dirty;
    }
    
    public abstract void clear();
    
    public void update() {
    }
    
    public abstract boolean hasValue();
    
    public abstract long getValue();
    
    public abstract Map<String, Node> getDirectChildren();
    
    public final Node getDirectChild(final String childName) {
        return this.getDirectChildren().get(childName);
    }
    
    public final NodeSet select(final String path) {
        if (path == null) {
            throw new RuntimeException("Path invalide : " + path);
        }
        final Matcher matcher = Node.SUBPACKAGE_PATTERN.matcher(path);
        if (matcher.matches()) {
            final String[] packages = path.split("\\.");
            NodeSet potentialNodes = NodeSet.singleton(this);
            String level = "";
            for (final String aPackage : packages) {
                level += aPackage;
                if (aPackage.equals("?")) {
                    final NodeSet subNodes = new NodeSet();
                    for (final Node potentialNode : potentialNodes.getAllNodes()) {
                        subNodes.addAll(potentialNode.getDirectChildren().values());
                    }
                    if (subNodes.size() == 0) {
                        if (Node.DEBUG) {
                            Node.m_logger.error((Object)("Aucun noeud au niveau " + level));
                        }
                        return NodeSet.EMPTY;
                    }
                    potentialNodes = subNodes;
                }
                else if (aPackage.equals("*")) {
                    final LinkedList<Node> toAdd = new LinkedList<Node>();
                    for (final Node node : potentialNodes.getAllNodes()) {
                        toAdd.add(node);
                    }
                    final NodeSet subNodes2 = new NodeSet();
                    while (!toAdd.isEmpty()) {
                        final Node node = toAdd.poll();
                        toAdd.addAll(node.getDirectChildren().values());
                        subNodes2.add(node);
                    }
                    if (subNodes2.size() == 0) {
                        Node.m_logger.error((Object)("Aucun noeud au niveau " + level));
                        return NodeSet.EMPTY;
                    }
                    potentialNodes = subNodes2;
                }
                else {
                    final NodeSet subNodes = new NodeSet();
                    for (final Node potentialNode : potentialNodes.getAllNodes()) {
                        final Node subNode = potentialNode.getDirectChild(aPackage);
                        if (subNode != null) {
                            subNodes.add(subNode);
                        }
                    }
                    if (subNodes.size() == 0) {
                        if (Node.DEBUG) {
                            Node.m_logger.error((Object)("Aucun noeud " + aPackage + " au niveau " + level));
                        }
                        return NodeSet.EMPTY;
                    }
                    potentialNodes = subNodes;
                }
                level += ".";
            }
            return potentialNodes;
        }
        throw new RuntimeException("Path invalide : " + path);
    }
    
    public final Node getChild(final String path) {
        final List<Node> allNodes = this.select(path).getAllNodes();
        if (allNodes.size() == 1) {
            return allNodes.get(0);
        }
        return null;
    }
    
    public final ContainerNode getContainer(final String path) {
        final List<ContainerNode> packageNodes = this.select(path).getContainerNodes();
        if (packageNodes.size() == 1) {
            return packageNodes.get(0);
        }
        return null;
    }
    
    public final SimplePropertyNode getProperty(final String path) {
        try {
            final List<SimplePropertyNode> propertyNodes = this.select(path).getPropertyNodes();
            if (propertyNodes.size() == 1) {
                return propertyNodes.get(0);
            }
        }
        catch (Exception e) {
            Node.m_logger.error((Object)"Exception lev\u00e9e lors de la r\u00e9cup\u00e9ration des noeuds", (Throwable)e);
        }
        return null;
    }
    
    public abstract void serialize(final ByteArray p0);
    
    protected void serializeBase(final ByteArray buffer, final NodeType unserializeAs) {
        final byte[] nodeName = StringUtils.toUTF8(this.getName());
        buffer.put((byte)nodeName.length);
        buffer.put(nodeName);
        buffer.put((byte)unserializeAs.ordinal());
        buffer.put((byte)this.getMergeMode().ordinal());
    }
    
    public abstract void unserialize(final ByteBuffer p0, final String p1);
    
    @Override
    public String toString() {
        return this.getFullName();
    }
    
    static {
        Node.DEBUG = true;
        m_logger = Logger.getLogger((Class)Node.class);
        SUBPACKAGE_PATTERN = Pattern.compile("\\A(([\\w\\d-]+|\\?)\\.)*([\\w\\d-]+|\\?|\\*)?(\\.([\\w\\d-]+|\\?))*\\z");
    }
}
