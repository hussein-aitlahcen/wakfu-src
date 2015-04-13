package com.ankamagames.framework.kernel.core.common.debug.analyzer;

import java.util.regex.*;
import javax.swing.table.*;
import com.codahale.metrics.*;
import java.util.concurrent.*;
import com.intellij.uiDesigner.core.*;
import java.awt.*;
import javax.swing.*;

public class MonitoredPoolAnalyzer extends JFrame
{
    private static final Pattern SPLIT_PATTERN;
    public JTable m_table;
    public JPanel m_panel;
    public JScrollPane m_scrollPane;
    private final DefaultTableModel m_model;
    
    public MonitoredPoolAnalyzer() {
        super();
        this.$$$setupUI$$$();
        this.add(this.m_panel);
        this.m_model = new DefaultTableModel();
        this.m_table.setModel(this.m_model);
        this.m_model.addColumn("object");
        this.m_model.addColumn("count");
        this.m_model.addColumn("mean rate");
        this.m_model.addColumn("1-minute rate");
        this.m_model.addColumn("5-minute rate");
        this.m_model.addColumn("15-minute rate");
        this.m_model.addColumn("min");
        this.m_model.addColumn("max");
        this.m_model.addColumn("mean");
        this.m_model.addColumn("stddev");
        this.m_model.addColumn("median");
        this.m_model.addColumn("75%");
        this.m_model.addColumn("95%");
        this.m_model.addColumn("98%");
        this.m_model.addColumn("99%");
        this.m_model.addColumn("99.9%");
    }
    
    public final void startMonitoring() {
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
            }
            
            private boolean updateRow(final String name, final Timer timer, final Snapshot snapshot) {
                for (int i = 0; i < MonitoredPoolAnalyzer.this.m_model.getRowCount(); ++i) {
                    final String rowName = (String)MonitoredPoolAnalyzer.this.m_model.getValueAt(i, 0);
                    if (name.equals(rowName)) {
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(name, i, 0);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(timer.getCount(), i, 1);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(timer.getMeanRate(), i, 2);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(timer.getOneMinuteRate(), i, 3);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(timer.getFiveMinuteRate(), i, 4);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(timer.getFifteenMinuteRate(), i, 5);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(snapshot.getMin(), i, 6);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(snapshot.getMax(), i, 7);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(snapshot.getMean(), i, 8);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(snapshot.getStdDev(), i, 9);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(snapshot.getMedian(), i, 10);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(snapshot.get75thPercentile(), i, 11);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(snapshot.get95thPercentile(), i, 12);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(snapshot.get98thPercentile(), i, 13);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(snapshot.get99thPercentile(), i, 14);
                        MonitoredPoolAnalyzer.this.m_model.setValueAt(snapshot.get999thPercentile(), i, 15);
                        return true;
                    }
                }
                return false;
            }
        }, 1L, 1L, TimeUnit.SECONDS);
    }
    
    public static void main(final String... args) {
        final MonitoredPoolAnalyzer frame = new MonitoredPoolAnalyzer();
        frame.setTitle("MonitoredPool Analyzer");
        frame.setDefaultCloseOperation(3);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.startMonitoring();
    }
    
    private void $$$setupUI$$$() {
        (this.m_panel = new JPanel()).setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        this.m_scrollPane = new JScrollPane();
        this.m_panel.add(this.m_scrollPane, new GridConstraints(0, 0, 1, 1, 0, 3, 5, 5, null, null, null, 0, false));
        (this.m_table = new JTable()).setAutoCreateRowSorter(true);
        this.m_scrollPane.setViewportView(this.m_table);
    }
    
    public JComponent $$$getRootComponent$$$() {
        return this.m_panel;
    }
    
    static {
        SPLIT_PATTERN = Pattern.compile("\\.");
    }
}
