/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.tools;

/**
 *
 * @author mgd
 */
public class UnitHelpDialog extends javax.swing.JDialog {

    /**
     * Creates new form UnitHelpDialog
     */
    public UnitHelpDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.initComponents();
        this.jTextArea.setLineWrap(true);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("用例");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("unitHelpDlg");

        jTextArea.setColumns(20);
        jTextArea.setRows(5);
        jTextArea.setText("1、假设您当前分析的页面的URL为：http://guba.sina.com.cn\n\n2、则在Unit Path栏中输入（需通过分析待抽取页面的结构得到）：\nhtml/body/div/div/div/div/div/div/table/tbody/tr\n\n3、在上方文本区中输入要抽取的信息的配置（需通过分析待抽取页面的结构得到）：\n[{\"regex\":\"(\\\\d+)\",\"name\":\"点击\",\"path\":\"td[1]/text()\"},{\"name\":\"回复\",\"path\":\"td[2]/text()\"},{\"name\":\"标题\",\"path\":\"td[3]/a[1]/text()\"},{\"name\":\"链接\",\"path\":\"td[3]/a[1]/@href\"},{\"name\":\"来源\",\"path\":\"td[3]/a[2]/text()\"},{\"name\":\"作者\",\"path\":\"td[4]/div/a/text()\"}]\n\n4、点击按钮“开始抽取”");
        jScrollPane1.setViewportView(jTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea;
    // End of variables declaration//GEN-END:variables
}