package com.ankamagames.wakfu.common.game.inventory.moderation;

import javax.swing.table.*;
import java.util.*;

public class ItemTrackerViewerTableModel extends AbstractTableModel
{
    private ArrayList<String[]> m_data;
    private final String[] m_header;
    
    public ItemTrackerViewerTableModel() {
        super();
        this.m_data = new ArrayList<String[]>();
        this.m_header = new String[] { "logLevel", "serverId", "date", "accountFrom", "characterFrom", "accountTo", "characterTo", "ipFrom", "ipTo", "type", "externalId", "instanceId", "itemRefId", "itemFromUid", "itemToUId", "quantity", "kamas" };
    }
    
    @Override
    public int getRowCount() {
        return this.m_data.size();
    }
    
    @Override
    public int getColumnCount() {
        return this.m_header.length;
    }
    
    @Override
    public String getColumnName(final int columnIndex) {
        return this.m_header[columnIndex];
    }
    
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        return this.m_data.get(rowIndex)[columnIndex];
    }
    
    public void addData(final String[] data) {
        this.m_data.add(data);
        this.fireTableRowsInserted(this.m_data.size() - 1, this.m_data.size() - 1);
    }
    
    public void removeData(final int rowIndex) {
        this.m_data.remove(rowIndex);
        this.fireTableRowsDeleted(rowIndex, rowIndex);
    }
    
    public void clearData() {
        if (this.m_data.isEmpty()) {
            return;
        }
        final int size = this.m_data.size();
        this.m_data.clear();
        this.fireTableRowsDeleted(0, size - 1);
    }
}
