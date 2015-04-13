package com.ankamagames.framework.kernel.gameStats;

import com.ankamagames.framework.kernel.gameStats.intelligentNodes.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public final class PackageNode extends ContainerNode
{
    PackageNode(final String name, final Node parent, final MergeMode mergeMode) {
        super(name, parent, mergeMode);
    }
    
    public NodeType getType() {
        return NodeType.PACKAGE;
    }
    
    public final PackageNode getOrCreateSubPackage(final String packageName) {
        final Node n = this.getDirectChildren().get(packageName);
        if (n != null && n instanceof PackageNode) {
            return (PackageNode)n;
        }
        return this.addChild(new PackageNode(packageName, this, MergeMode.REPLACE));
    }
    
    public final PackageNode createSubPackage(final String packageName) {
        return this.addChild(new PackageNode(packageName, this, MergeMode.REPLACE));
    }
    
    public final SimplePropertyNode getOrCreateSimpleProperty(final String childName, final MergeMode mergeMode) {
        final Node n = this.getDirectChildren().get(childName);
        if (n != null && n instanceof SimplePropertyNode) {
            return (SimplePropertyNode)n;
        }
        return this.addChild(new SimplePropertyNode(childName, this, mergeMode));
    }
    
    public final SimplePropertyNode createSimpleProperty(final String childName, final MergeMode mergeMode) {
        return this.addChild(new SimplePropertyNode(childName, this, mergeMode));
    }
    
    public final CountOverTimePropertyNode createCountOverTimeProperty(final String childName, final MergeMode mergeMode) {
        return this.addChild(new CountOverTimePropertyNode(childName, this, mergeMode));
    }
    
    public final MultipleValuesPropertyNode createMultipleValuesProperty(final String childName, final MergeMode mergeMode) {
        return this.addChild(new MultipleValuesPropertyNode(childName, this, mergeMode));
    }
    
    public final TrackMaxPropertyNode createTrackMaxProperty(final String childName, final MergeMode mergeMode) {
        return this.addChild(new TrackMaxPropertyNode(childName, this, mergeMode));
    }
    
    public final TrackQuotaPropertyNode createTrackQuotaProperty(final String childName, final MergeMode mergeMode) {
        return this.addChild(new TrackQuotaPropertyNode(childName, this, mergeMode));
    }
    
    @Override
    public void serialize(final ByteArray buffer) {
        this.serializeBase(buffer, NodeType.PACKAGE);
        buffer.putShort((short)this.getDirectChildren().size());
        for (final Node child : this.getDirectChildren().values()) {
            child.serialize(buffer);
        }
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final String source) {
        for (int nChildren = buffer.getShort() & 0xFFFF, i = 0; i < nChildren; ++i) {
            final byte[] encodedName = new byte[buffer.get() & 0xFF];
            buffer.get(encodedName);
            final String name = StringUtils.fromUTF8(encodedName);
            final int nodeTypeValue = buffer.get() & 0xFF;
            final int mergeModeValue = buffer.get() & 0xFF;
            final NodeType nodeType = NodeType.values()[nodeTypeValue];
            final MergeMode mergeMode = MergeMode.values()[mergeModeValue];
            final Node currentChild = this.getDirectChild(name);
            if (currentChild != null) {
                if (currentChild.getMergeMode() == mergeMode) {
                    currentChild.unserialize(buffer, source);
                }
                else {
                    PackageNode.m_logger.error((Object)("Impossible de fusionner des nodes de mergeMode diff\u00e9rents : " + currentChild.getMergeMode() + " != " + mergeMode));
                }
            }
            else {
                final Node node = this.addChild(nodeType.getNodeFactory().makeNode(name, this, mergeMode));
                node.unserialize(buffer, source);
            }
        }
    }
}
