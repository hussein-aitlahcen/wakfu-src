package com.ankamagames.xulor2.component.tournament;

import java.util.*;
import java.awt.geom.*;

public class TournamentData
{
    private ArrayList<TournamentItem> m_items;
    private ArrayList<Line2D> m_lines;
    private int m_rowCount;
    private int m_columnCount;
    
    public TournamentData(final ArrayList<TournamentItem> items, final ArrayList<Line2D> lines, final int rowCount, final int columnCount) {
        super();
        this.m_rowCount = rowCount;
        this.m_columnCount = columnCount;
        this.m_items = items;
        this.m_lines = lines;
    }
    
    public ArrayList<TournamentItem> getItems() {
        return this.m_items;
    }
    
    public void setItems(final ArrayList<TournamentItem> items) {
        this.m_items = items;
    }
    
    public int getRowCount() {
        return this.m_rowCount;
    }
    
    public void setRowCount(final int rowCount) {
        this.m_rowCount = rowCount;
    }
    
    public int getColumnCount() {
        return this.m_columnCount;
    }
    
    public void setColumnCount(final int columnCount) {
        this.m_columnCount = columnCount;
    }
    
    public ArrayList<Line2D> getLines() {
        return this.m_lines;
    }
    
    public void setLines(final ArrayList<Line2D> lines) {
        this.m_lines = lines;
    }
}
