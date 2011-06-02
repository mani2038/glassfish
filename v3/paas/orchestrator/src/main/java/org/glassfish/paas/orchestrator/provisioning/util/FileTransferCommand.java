/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
package org.glassfish.paas.orchestrator.provisioning.util;

import org.glassfish.api.ActionReport;
import org.glassfish.api.Param;
import org.glassfish.api.admin.*;
import org.glassfish.cluster.ssh.launcher.SSHLauncher;
import org.glassfish.cluster.ssh.sftp.SFTPClient;
import org.glassfish.config.support.CommandTarget;
import org.glassfish.config.support.TargetType;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.PerLookup;

import java.io.*;
import java.util.logging.Logger;

/**
 * @author Jagadish Ramu
 */
@Service(name = "_file-transfer")
@Scoped(PerLookup.class)
@CommandLock(CommandLock.LockType.NONE)
@ExecuteOn(value = {RuntimeType.DAS})
@TargetType(value = {CommandTarget.DAS})
public class FileTransferCommand implements AdminCommand {

    @Param(name = "target", defaultValue = "server", optional = true)
    private String target;

    @Param(name = "username", optional = false)
    private String user;

    @Param(name = "keyfile", optional = false)
    private String keyfile;

    @Param(name = "hostname", optional = false)
    private String hostname;

    @Param(name = "localfilename", optional = false)
    private String fileName;

    @Param(name = "remotefilename", optional = false)
    private String remoteFileName;

    @Param(name = "upload", defaultValue = "true", optional = true)
    private Boolean upload;

    @Inject
    private SSHLauncher sshLauncher;

    @Inject
    private Logger logger;

    public void execute(AdminCommandContext context) {
        final ActionReport report = context.getActionReport();

        sshLauncher.init(user, hostname, 0, null, keyfile, null, logger);
        try {
            SFTPClient ftpClient = sshLauncher.getSFTPClient();
            if (upload) {
                writeToFile(ftpClient, remoteFileName, new FileInputStream(fileName));
            } else {
                readFromFile(ftpClient, remoteFileName, fileName);
            }
        } catch (IOException e) {
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            report.setFailureCause(e);
        }
    }

    private void readFromFile(SFTPClient ftpClient, String remoteFileName, String localFileName) throws IOException {
        InputStream content = ftpClient.read(remoteFileName);
        FileOutputStream os = new FileOutputStream(localFileName);
        int bytesRead;
        try {
            final byte[] buffer = new byte[1024];
            while ((bytesRead = content.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } finally {
            os.close();
        }
    }

    private void writeToFile(SFTPClient ftpClient, final String path, final InputStream content) throws IOException {
        final OutputStream os = new BufferedOutputStream(ftpClient.writeToFile(path));
        int bytesRead;
        try {
            final byte[] buffer = new byte[1024];
            while ((bytesRead = content.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } finally {
            os.close();
        }
    }
}
