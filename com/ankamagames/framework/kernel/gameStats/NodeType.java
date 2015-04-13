package com.ankamagames.framework.kernel.gameStats;

import com.ankamagames.framework.kernel.gameStats.intelligentNodes.*;

public enum NodeType
{
    SIMPLE_PROPERTY((NodeFactory)new NodeFactory() {
        @Override
        public Node makeNode(final String nodeName, final Node parent, final MergeMode mergeMode) {
            return new SimplePropertyNode(nodeName, parent, mergeMode);
        }
    }), 
    FUSIONING_SIMPLE_PROPERTY((NodeFactory)new NodeFactory() {
        @Override
        public Node makeNode(final String nodeName, final Node parent, final MergeMode mergeMode) {
            return new FusioningSimplePropertyNode(nodeName, parent, mergeMode);
        }
    }), 
    LINKED_PROPERTY((NodeFactory)new NodeFactory() {
        @Override
        public Node makeNode(final String nodeName, final Node parent, final MergeMode mergeMode) {
            return new LinkedPropertyNode(nodeName, parent, mergeMode);
        }
    }), 
    PACKAGE((NodeFactory)new NodeFactory() {
        @Override
        public Node makeNode(final String nodeName, final Node parent, final MergeMode mergeMode) {
            return new PackageNode(nodeName, parent, mergeMode);
        }
    }), 
    MULTIPLE_VALUES((NodeFactory)new NodeFactory() {
        @Override
        public Node makeNode(final String nodeName, final Node parent, final MergeMode mergeMode) {
            return new MultipleValuesPropertyNode(nodeName, parent, mergeMode);
        }
    }), 
    TRACK_MAX((NodeFactory)new NodeFactory() {
        @Override
        public Node makeNode(final String nodeName, final Node parent, final MergeMode mergeMode) {
            return new TrackMaxPropertyNode(nodeName, parent, mergeMode);
        }
    }), 
    FUSIONING_TRACK_MAX((NodeFactory)new NodeFactory() {
        @Override
        public Node makeNode(final String nodeName, final Node parent, final MergeMode mergeMode) {
            return new FusioningTrackMaxPropertyNode(nodeName, parent, mergeMode);
        }
    }), 
    TRACK_QUOTA((NodeFactory)new NodeFactory() {
        @Override
        public Node makeNode(final String nodeName, final Node parent, final MergeMode mergeMode) {
            return new TrackQuotaPropertyNode(nodeName, parent, mergeMode);
        }
    }), 
    FUSIONING_TRACK_QUOTA((NodeFactory)new NodeFactory() {
        @Override
        public Node makeNode(final String nodeName, final Node parent, final MergeMode mergeMode) {
            return new FusioningTrackQuotaPropertyNode(nodeName, parent, mergeMode);
        }
    }), 
    COUNT_OVER_TIME((NodeFactory)new NodeFactory() {
        @Override
        public Node makeNode(final String nodeName, final Node parent, final MergeMode mergeMode) {
            return new CountOverTimePropertyNode(nodeName, parent, mergeMode);
        }
    }), 
    FUSIONING_COUNT_OVER_TIME((NodeFactory)new NodeFactory() {
        @Override
        public Node makeNode(final String nodeName, final Node parent, final MergeMode mergeMode) {
            return new FusioningCountOverTimePropertyNode(nodeName, parent, mergeMode);
        }
    });
    
    private final NodeFactory m_nodeFactory;
    
    private NodeType(final NodeFactory nodeFactory) {
        this.m_nodeFactory = nodeFactory;
    }
    
    public NodeFactory getNodeFactory() {
        return this.m_nodeFactory;
    }
}
