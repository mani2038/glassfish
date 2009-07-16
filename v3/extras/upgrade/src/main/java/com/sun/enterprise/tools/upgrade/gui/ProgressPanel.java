/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.enterprise.tools.upgrade.gui;

import com.sun.enterprise.tools.upgrade.common.CommonInfoModel;
import com.sun.enterprise.tools.upgrade.logging.LogService;
import com.sun.enterprise.util.i18n.StringManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

/**
 * Panel to show the progress and results of an upgrade.
 * For information on how results get to the text area in
 * the GUI, see the comments in UpgradeWorker.java.
 *
 * @author Bobby Bissett
 */
public class ProgressPanel extends javax.swing.JPanel {

    private static final Logger logger = LogService.getLogger();
    private StringManager stringManager =
        StringManager.getManager(MainFrame.class);
    private static final CommonInfoModel commonInfoModel =
        CommonInfoModel.getInstance();
    
    /** Creates new form DataCollectionPanel */
    public ProgressPanel() {
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

        javax.swing.JLabel headerLabel = new javax.swing.JLabel();
        separator = new javax.swing.JSeparator();
        javax.swing.JLabel contentLabel = new javax.swing.JLabel();
        resultsLabel = new javax.swing.JLabel();
        resultTextAreaScrollPane = new javax.swing.JScrollPane();
        resultTextArea = new javax.swing.JTextArea();
        progressLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        headerLabel.setText(stringManager.getString("upgrade.gui.progresspanel.headerPanel"));

        contentLabel.setForeground(java.awt.Color.blue);
        contentLabel.setText(stringManager.getString("upgrade.gui.progresspanel.contentLabel"));

        resultsLabel.setForeground(java.awt.Color.blue);
        resultsLabel.setLabelFor(resultTextArea);
        resultsLabel.setText(stringManager.getString("upgrade.gui.progresspanel.textAreaText"));

        resultTextArea.setColumns(20);
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);
        resultTextArea.setRows(5);
        resultTextArea.setFocusable(false);
        resultTextArea.setRequestFocusEnabled(false);
        resultTextAreaScrollPane.setViewportView(resultTextArea);

        progressLabel.setForeground(java.awt.Color.blue);
        progressLabel.setLabelFor(progressBar);
        progressLabel.setText(stringManager.getString("upgrade.gui.progresspanel.progressLabel"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(resultTextAreaScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                    .addComponent(separator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                    .addComponent(headerLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                    .addComponent(progressLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contentLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(resultsLabel, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(contentLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(resultsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultTextAreaScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(progressLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JTextArea resultTextArea;
    private javax.swing.JScrollPane resultTextAreaScrollPane;
    private javax.swing.JLabel resultsLabel;
    private javax.swing.JSeparator separator;
    // End of variables declaration//GEN-END:variables

    // used by main frame and/or worker thread to set the state
    JProgressBar getProgressBar() {
        return progressBar;
    }

    /*
     * This method is used to append text to JTextArea. This method
     * is thread safe because JTextArea is thread safe. If the implementation
     * of the GUI changes, change this method as needed to remain
     * thread safe as the callers expect it to be.
     *
     * The level param is optional, but can be passed in so that the
     * progress panel can use differentiate message types in an
     * implementation-specifc way.
     *
     * todo: do we want red/yellow colors for different levels?
     */
    void appendResultString(String res, Level level) {
        resultTextArea.append(res);
        resultTextArea.append("\n");
    }
    
}
