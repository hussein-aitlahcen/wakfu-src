package com.ankamagames.baseImpl.common.clientAndServer.game.pathfind;

public class PathFinderNode
{
    public byte m_cellElementsCount;
    public short m_firstElementIndex;
    public byte m_nodeElementIndex;
    public float m_g;
    public float m_h;
    public float m_f;
    public PathFinderNode m_parentNode;
    public byte m_incomingDirection;
    public boolean m_opened;
    public byte m_currentPathLength;
    
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{Node f : ").append(this.m_f).append("}");
        return sb.toString();
    }
}
