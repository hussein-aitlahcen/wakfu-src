package com.ankamagames.wakfu.common.game.inventory.moderation;

import org.apache.log4j.*;
import java.awt.event.*;
import org.apache.log4j.spi.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.awt.*;

public class ItemTrackerViewer extends AppenderSkeleton
{
    private final JTable m_table;
    final ItemTrackerViewerTableModel m_model;
    private static final int MESSAGES_MAX_LINE_COUNT = 3000;
    private final JScrollPane m_scrollPane;
    private final JFrame m_frame;
    private final JLabel m_statutLabel;
    private final JCheckBox m_autoScrollCheckBox;
    
    public ItemTrackerViewer(final String name) throws HeadlessException {
        super();
        this.m_model = new ItemTrackerViewerTableModel();
        final String appenderName = getFullName(name);
        this.setName(appenderName);
        this.m_frame = new JFrame(appenderName);
        this.m_table = new JTable(this.m_model);
        (this.m_scrollPane = new JScrollPane(this.m_table)).setSize(new Dimension(1024, 768));
        this.m_frame.getContentPane().add(this.m_scrollPane, "Center");
        final JPanel panel = new JPanel();
        panel.add(this.m_statutLabel = new JLabel());
        (this.m_autoScrollCheckBox = new JCheckBox("AutoScroll enabled")).setSelected(true);
        panel.add(this.m_autoScrollCheckBox);
        final JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ItemTrackerViewer.this.m_model.clearData();
            }
        });
        panel.add(clearButton);
        this.m_frame.getContentPane().add(panel, "North");
        this.reflowView();
        this.m_frame.pack();
        this.m_frame.setVisible(true);
        this.m_frame.setDefaultCloseOperation(2);
    }
    
    public static String getFullName(final String localName) {
        return "ItemTrackerViewer" + localName;
    }
    
    protected void append(final LoggingEvent event) {
        final String message = (String)event.getMessage();
        final String[] split = message.split(",");
        final String[] data = new String[split.length + 1];
        data[0] = event.getLevel().toString();
        System.arraycopy(split, 0, data, 1, split.length);
        if (this.m_model.getRowCount() == 3000) {
            this.m_model.removeData(0);
        }
        this.m_model.addData(data);
        this.reflowView();
    }
    
    private void reflowView() {
        int col = 0;
        final int droiteMax = 0;
        int larg = 0;
        int largTotal = 0;
        int row = 0;
        final int tableX = 0;
        int width = 0;
        final JTableHeader header = this.m_table.getTableHeader();
        final Enumeration columns = this.m_table.getColumnModel().getColumns();
        this.m_table.setAutoResizeMode(0);
        while (columns.hasMoreElements()) {
            final TableColumn column = columns.nextElement();
            col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            width = (int)this.m_table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(this.m_table, column.getIdentifier(), false, false, -1, col).getPreferredSize().getWidth();
            for (row = 0; row < this.m_table.getRowCount(); ++row) {
                final int preferedWidth = (int)this.m_table.getCellRenderer(row, col).getTableCellRendererComponent(this.m_table, this.m_table.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column);
            larg = width + this.m_table.getIntercellSpacing().width;
            larg += 10;
            largTotal += larg;
            column.setWidth(larg);
        }
        this.m_statutLabel.setText(this.m_model.getRowCount() + " line(s)" + ((this.m_model.getRowCount() == 3000) ? " (limit reached ! rolling...)" : ""));
        final Point location = this.m_frame.getLocation();
        this.m_scrollPane.setPreferredSize(new Dimension(largTotal + 30, this.m_scrollPane.getHeight()));
        if (this.m_autoScrollCheckBox.isSelected()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ItemTrackerViewer.this.m_table.scrollRectToVisible(ItemTrackerViewer.this.m_table.getCellRect(ItemTrackerViewer.this.m_table.getRowCount() - 1, 0, false));
                    ItemTrackerViewer.this.m_table.repaint();
                }
            });
        }
        this.m_frame.pack();
        this.m_frame.setLocation(location);
    }
    
    public void close() {
        this.m_frame.dispose();
    }
    
    public boolean requiresLayout() {
        return false;
    }
}
