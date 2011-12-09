/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SampleFrame.java
 *
 * Created on Oct 8, 2011, 11:41:32 PM
 */
package websiteschema.analyzer.sample;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import websiteschema.analyzer.context.BrowserContext;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.cluster.DocUnits;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.model.domain.cluster.Unit;
import websiteschema.persistence.hbase.SampleMapper;
import websiteschema.persistence.hbase.WebsiteschemaMapper;
import websiteschema.utils.DateUtil;
import websiteschema.utils.UrlLinkUtil;

/**
 *
 * @author ray
 */
public class SampleFrame extends javax.swing.JFrame {

    String siteId;

    /** Creates new form SampleFrame */
    public SampleFrame() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contentDialog = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        contentTextArea = new javax.swing.JTextArea();
        addDialog = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        siteIdField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        urlField = new javax.swing.JTextField();
        addSampleButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        sampleTable = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        fetchButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        viewButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();

        contentDialog.setMinimumSize(new java.awt.Dimension(800, 600));

        contentTextArea.setColumns(20);
        contentTextArea.setRows(5);
        jScrollPane2.setViewportView(contentTextArea);

        javax.swing.GroupLayout contentDialogLayout = new javax.swing.GroupLayout(contentDialog.getContentPane());
        contentDialog.getContentPane().setLayout(contentDialogLayout);
        contentDialogLayout.setHorizontalGroup(
            contentDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        contentDialogLayout.setVerticalGroup(
            contentDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        addDialog.setMinimumSize(new java.awt.Dimension(280, 119));
        addDialog.setResizable(false);

        jLabel1.setText("SiteId :");

        siteIdField.setEditable(false);

        jLabel2.setText("URL :");

        addSampleButton.setText("保存");
        addSampleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSampleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addDialogLayout = new javax.swing.GroupLayout(addDialog.getContentPane());
        addDialog.getContentPane().setLayout(addDialogLayout);
        addDialogLayout.setHorizontalGroup(
            addDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDialogLayout.createSequentialGroup()
                .addGroup(addDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(addDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addDialogLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(siteIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(addDialogLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(urlField, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))))
                    .addGroup(addDialogLayout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(addSampleButton)))
                .addContainerGap())
        );
        addDialogLayout.setVerticalGroup(
            addDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(siteIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(urlField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addSampleButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setMinimumSize(new java.awt.Dimension(400, 300));

        sampleTable.setAutoCreateRowSorter(true);
        sampleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "样本", "URL", "HTTP状态", "上次更新"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(sampleTable);
        sampleTable.getColumnModel().getColumn(0).setMaxWidth(40);
        sampleTable.getColumnModel().getColumn(1).setMaxWidth(300);
        sampleTable.getColumnModel().getColumn(3).setMaxWidth(90);
        sampleTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        sampleTable.getColumnModel().getColumn(4).setMaxWidth(200);

        jToolBar1.setRollover(true);

        addButton.setText("新增样本");
        addButton.setFocusable(false);
        addButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(addButton);

        deleteButton.setText("删除选定行");
        deleteButton.setFocusable(false);
        deleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(deleteButton);

        fetchButton.setText("采集样本");
        fetchButton.setFocusable(false);
        fetchButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fetchButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        fetchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fetchButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(fetchButton);

        refreshButton.setText("刷新");
        refreshButton.setFocusable(false);
        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(refreshButton);

        viewButton.setText("查看内容");
        viewButton.setFocusable(false);
        viewButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        viewButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        viewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(viewButton);

        statusLabel.setText("Status:");
        jToolBar1.add(statusLabel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        this.loadData();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        int selectRows = sampleTable.getSelectedRows().length;// 取得用户所选行的行数
        DefaultTableModel tableModel = (DefaultTableModel) sampleTable.getModel();

        if (selectRows > 0) {
            int[] selRowIndexs = sampleTable.getSelectedRows();// 用户所选行的序列

            for (int i = 0; i < selRowIndexs.length; i++) {
                // 用tableModel.getValueAt(row, column)取单元格数据
                String rowKey = (String) tableModel.getValueAt(selRowIndexs[i], 1);
                System.out.println(rowKey);
                SampleMapper mapper = BrowserContext.getSpringContext().getBean("sampleMapper", SampleMapper.class);
                mapper.delete(rowKey);
            }
            for (int i = 0; i < selRowIndexs.length; i++) {
                tableModel.removeRow(selRowIndexs[0]);
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void addSampleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSampleButtonActionPerformed
        // TODO add your handling code here:
        String url = this.urlField.getText().trim();
        if (!"".equals(url)) {
            try {
                URI uri = new URI(url);
                String rowKey = UrlLinkUtil.getInstance().convertUriToRowKey(uri, getSiteId());
                Sample sample = new Sample();
                sample.setRowKey(rowKey);
                sample.setUrl(url);
                sample.setCreateTime(new Date());
                sample.setLastUpdateTime(sample.getCreateTime());
                SampleMapper mapper = BrowserContext.getSpringContext().getBean("sampleMapper", SampleMapper.class);
                mapper.put(sample);
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
        this.addDialog.setVisible(false);
    }//GEN-LAST:event_addSampleButtonActionPerformed

    private void viewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButtonActionPerformed
        // TODO add your handling code here:
        SampleMapper mapper = BrowserContext.getSpringContext().getBean("sampleMapper", SampleMapper.class);
        int selectRows = sampleTable.getSelectedRows().length;// 取得用户所选行的行数
        DefaultTableModel tableModel = (DefaultTableModel) sampleTable.getModel();
        if (selectRows == 1) {
            this.contentTextArea.setText("");
            int rowId = sampleTable.getSelectedRow();
            String rowKey = (String) tableModel.getValueAt(rowId, 1);
            Sample sample = mapper.get(rowKey);
            DocUnits docUnits = sample.getContent();
            Unit[] units = docUnits.getUnits();
            if (null != units) {
                this.contentDialog.setTitle(getSiteId() + " : " + sample.getUrl());
                this.contentDialog.setVisible(true);
                for (Unit unit : units) {
                    this.contentTextArea.append(unit.xpath + " -> " + unit.text.trim() + "\n");
                }
            }
        }
    }//GEN-LAST:event_viewButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
        this.siteIdField.setText(getSiteId());
        this.addDialog.setVisible(true);
    }//GEN-LAST:event_addButtonActionPerformed

    private void fetchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fetchButtonActionPerformed
        // TODO add your handling code here:
        final SampleMapper mapper = BrowserContext.getSpringContext().getBean("sampleMapper", SampleMapper.class);
        final WebsiteschemaMapper wMapper = BrowserContext.getSpringContext().getBean("websiteschemaMapper", WebsiteschemaMapper.class);
        Websiteschema w = wMapper.get(getSiteId());
        final SampleCrawler sc = new SampleCrawler();
        sc.setXPathAttributes(w.getXpathAttr());

        int selectRows = sampleTable.getSelectedRows().length;// 取得用户所选行的行数
        final DefaultTableModel tableModel = (DefaultTableModel) sampleTable.getModel();
        if (selectRows == 1) {
            final int rowId = sampleTable.getSelectedRow();
            String rowKey = (String) tableModel.getValueAt(rowId, 1);
            final Sample sample = mapper.get(rowKey);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    sc.fetch(sample);
                    tableModel.setValueAt(sample.getHttpStatus(), rowId, 3);
                    tableModel.setValueAt(DateUtil.getDateTime(sample.getLastUpdateTime()), rowId, 4);
                }
            }).start();
        }
    }//GEN-LAST:event_fetchButtonActionPerformed

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
        this.setTitle(siteId);
    }

    private Vector convert(int id, Sample sample) {
        Vector vect = new Vector();
        vect.add(id);
        vect.add(sample.getRowKey());
        vect.add(sample.getUrl());
        vect.add(sample.getHttpStatus());
        vect.add(DateUtil.getDateTime(sample.getLastUpdateTime()));
        return vect;
    }

    public void loadData() {
        new DataLoader().start();
    }

    private synchronized void load() {
        this.statusLabel.setText("Status: Loading...");
        final SampleMapper mapper = BrowserContext.getSpringContext().getBean("sampleMapper", SampleMapper.class);
        String now = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");
        String end = getSiteId() + "+" + now;
        List<Sample> samples = mapper.getList(getSiteId(), end);
        DefaultTableModel tableModel = (DefaultTableModel) sampleTable.getModel();
        int rows = sampleTable.getRowCount();
        for (int i = 0; i < rows; i++) {
            tableModel.removeRow(0);
        }
        int i = 0;
        if (null != samples && !samples.isEmpty()) {
            for (Sample sample : samples) {
                tableModel.addRow(convert(++i, sample));
            }
            this.statusLabel.setText("Status: Completed.");
        } else {
            this.statusLabel.setText("Status: No Samples in Database.");
        }
    }

    class DataLoader extends Thread {

        @Override
        public void run() {
            load();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JDialog addDialog;
    private javax.swing.JButton addSampleButton;
    private javax.swing.JDialog contentDialog;
    private javax.swing.JTextArea contentTextArea;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton fetchButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTable sampleTable;
    private javax.swing.JTextField siteIdField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField urlField;
    private javax.swing.JButton viewButton;
    // End of variables declaration//GEN-END:variables
}
