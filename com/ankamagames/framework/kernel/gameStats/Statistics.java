package com.ankamagames.framework.kernel.gameStats;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;

public class Statistics
{
    public static final boolean DEBUG = false;
    protected static final Logger m_logger;
    private static final Statistics m_instance;
    private PackageNode m_rootPackage;
    private final ByteArray m_buffer;
    
    public static Statistics getInstance() {
        return Statistics.m_instance;
    }
    
    private Statistics() {
        super();
        this.m_rootPackage = new PackageNode("root", null, MergeMode.REPLACE);
        this.m_buffer = new ByteArray();
    }
    
    public PackageNode getRootPackage() {
        return this.m_rootPackage;
    }
    
    public final void clear() {
        this.m_rootPackage.clear();
    }
    
    public final byte[] serializeTree() {
        this.m_buffer.clear();
        this.getRootPackage().serialize(this.m_buffer);
        return this.m_buffer.toArray();
    }
    
    public final void unserializeTree(final byte[] data, final String source) {
        final ByteBuffer buffer = ByteBuffer.wrap(data);
        final byte[] encodedName = new byte[buffer.get() & 0xFF];
        buffer.get(encodedName);
        final String name = StringUtils.fromUTF8(encodedName);
        buffer.get();
        buffer.get();
        if (!name.equals(this.getRootPackage().getName())) {
            Statistics.m_logger.error((Object)("Synchronisation depuis un root package diff\u00e9rent : " + name + " au lieu de " + this.getRootPackage().getName()));
            return;
        }
        this.getRootPackage().unserialize(buffer, source);
    }
    
    public final void unserializeNodeSet(final byte[] data) {
        final ByteBuffer buffer = ByteBuffer.wrap(data);
        for (int nNodes = buffer.getShort() & 0xFFFF, i = 0; i < nNodes; ++i) {
            final byte[] encodedName = new byte[buffer.getShort() & 0xFFFF];
            buffer.get(encodedName);
            final String fullName = StringUtils.fromUTF8(encodedName);
            final long value = buffer.getLong();
            final Node node = this.addNode(fullName, value);
            if (node == null) {
                Statistics.m_logger.error((Object)("Impossible de cr\u00e9er le node " + fullName));
            }
        }
    }
    
    private SimplePropertyNode addNode(final String fullName, final long value) {
        final String[] packages = fullName.split("\\.");
        PackageNode currentPackage = null;
        for (int j = 0; j < packages.length; ++j) {
            final String aPackage = packages[j];
            if (j == 0) {
                if (!aPackage.equals(this.m_rootPackage.getName())) {
                    Statistics.m_logger.error((Object)("Le root package ne s'appelle pas " + aPackage));
                    return null;
                }
                currentPackage = this.m_rootPackage;
            }
            else if (j == packages.length - 1) {
                final Node subNode = currentPackage.getDirectChild(aPackage);
                if (subNode == null) {
                    final SimplePropertyNode propertyNode = currentPackage.createSimpleProperty(aPackage, MergeMode.REPLACE);
                    propertyNode.setValue(value);
                    return propertyNode;
                }
                if (subNode instanceof SimplePropertyNode) {
                    final SimplePropertyNode propertyNode = (SimplePropertyNode)subNode;
                    propertyNode.setValue(value);
                    return propertyNode;
                }
                Statistics.m_logger.error((Object)("Impossible de d\u00e9finir une valeur au neoud " + subNode + " qui n'est pas une propri\u00e9t\u00e9"));
            }
            else {
                final Node subNode = currentPackage.getDirectChild(aPackage);
                if (subNode == null) {
                    currentPackage = currentPackage.createSubPackage(aPackage);
                }
                else {
                    if (!(subNode instanceof PackageNode)) {
                        Statistics.m_logger.error((Object)("Impossible de cr\u00e9er un fils \u00e0 " + subNode + " qui n'est pas un package"));
                        return null;
                    }
                    currentPackage = (PackageNode)subNode;
                }
            }
        }
        return null;
    }
    
    public void printAllStats() {
        Statistics.m_logger.info((Object)"All statistics:");
        for (final SimplePropertyNode propertyNode : this.getRootPackage().select("*").getPropertyNodes()) {
            Statistics.m_logger.info((Object)("  " + propertyNode.getFullName() + " = " + propertyNode.getValue()));
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)Statistics.class);
        m_instance = new Statistics();
    }
}
