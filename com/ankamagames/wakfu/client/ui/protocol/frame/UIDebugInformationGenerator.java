package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import java.util.regex.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.game.steam.*;
import com.ankamagames.steam.wrapper.*;
import com.ankamagames.framework.net.http.*;
import com.ankamagames.xulor2.core.messagebox.*;
import java.util.zip.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.proxy.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.core.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import com.ankamagames.framework.graphics.opengl.*;
import java.util.*;
import javax.media.opengl.*;
import java.lang.management.*;
import com.ankamagames.framework.kernel.*;
import java.awt.*;

public final class UIDebugInformationGenerator implements MessageFrame
{
    private static final Logger m_logger;
    private static final UIDebugInformationGenerator m_instance;
    private static final Pattern m_patternLogFile;
    private static BufferedImage m_screenBuffer;
    
    public static UIDebugInformationGenerator getInstance() {
        return UIDebugInformationGenerator.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16440: {
                final String path = WakfuClient.INSTANCE.getReportPath();
                final File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                final Date date = new Date();
                String fileName = "" + (date.getYear() + 1900) + ((date.getMonth() < 9) ? ("0" + (date.getMonth() + 1)) : (date.getMonth() + 1)) + ((date.getDate() < 10) ? ("0" + date.getDate()) : date.getDate()) + "_" + ((date.getHours() < 10) ? ("0" + date.getHours()) : date.getHours()) + ((date.getMinutes() < 10) ? ("0" + date.getMinutes()) : date.getMinutes()) + ((date.getSeconds() < 10) ? ("0" + date.getSeconds()) : date.getSeconds());
                fileName += ".zip";
                final File file = new File(dir, fileName);
                if (file.exists()) {
                    file.delete();
                }
                if (this.generateBugArchive(file)) {
                    String absoluteFileName = null;
                    try {
                        absoluteFileName = file.getCanonicalPath();
                    }
                    catch (IOException e) {
                        absoluteFileName = file.getAbsolutePath();
                    }
                    absoluteFileName = absoluteFileName.replace("\\", "\\\\");
                    final String langKey = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage().toLowerCase();
                    final String linkSupport = String.format(WakfuConfiguration.getInstance().getString("bugReport.url", ""), langKey);
                    final String msg = WakfuTranslator.getInstance().getString("bug.report.message", "<b>" + file.getName() + "</b>", "<b>" + absoluteFileName + "</b>", "<b>" + linkSupport + "</b>");
                    final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(msg, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 10265L, 102, 1);
                    messageBoxControler.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                if (SteamClientContext.INSTANCE.isInit()) {
                                    SteamApi.SteamFriends().ActivateGameOverlayToWebPage(linkSupport);
                                }
                                else if (!BrowserManager.openUrlInBrowser(linkSupport)) {
                                    Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("bug.report.message.error"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 515L, 102, 1);
                                }
                            }
                        }
                    });
                }
                else {
                    Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("bug.report.message.error"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 515L, 102, 1);
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean generateBugArchive(final File file) {
        try {
            final ZipOutputStream zipfile = new ZipOutputStream(new FileOutputStream(file));
            final DataOutputStream out = new DataOutputStream(zipfile);
            zipfile.putNextEntry(new ZipEntry("account.txt"));
            this.writeAccountInformation(out);
            out.flush();
            zipfile.closeEntry();
            zipfile.putNextEntry(new ZipEntry("system.txt"));
            this.writeSystemInformation(out);
            out.flush();
            zipfile.closeEntry();
            this.writeClientLog(zipfile, out);
            if (UIDebugInformationGenerator.m_screenBuffer != null) {
                zipfile.putNextEntry(new ZipEntry("screenshot.jpg"));
                this.writeScreenShot(out);
                out.flush();
                zipfile.closeEntry();
            }
            out.close();
            zipfile.close();
        }
        catch (Exception e) {
            UIDebugInformationGenerator.m_logger.error((Object)("Error during creating bug report file at location: " + file.getAbsolutePath()), (Throwable)e);
            return false;
        }
        return true;
    }
    
    private void writeAccountInformation(final DataOutputStream out) throws Exception {
        String serverName = "";
        long accountId = 0L;
        String accountNickname = "";
        WakfuClientInstance.getInstance();
        final WakfuGameEntity gameEntity = WakfuClientInstance.getGameEntity();
        if (gameEntity != null) {
            final ProxyGroup world = gameEntity.getProxyGroup();
            if (world != null) {
                serverName = world.getName();
            }
            final LocalAccountInformations localAccount = gameEntity.getLocalAccount();
            if (localAccount != null) {
                accountId = localAccount.getAccountId();
                accountNickname = localAccount.getAccountNickName();
            }
        }
        long characterId = 0L;
        String characterName = "";
        int x = 0;
        int y = 0;
        short z = 0;
        short instanceId = 0;
        final LocalPlayerCharacter localplayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localplayer != null) {
            characterId = localplayer.getId();
            characterName = localplayer.getControllerName();
            x = localplayer.getWorldCellX();
            y = localplayer.getWorldCellY();
            z = localplayer.getWorldCellAltitude();
            instanceId = localplayer.getInstanceId();
        }
        final GamePreferences gamePreference = WakfuClientInstance.getInstance().getGamePreferences();
        final StringBuilder report = new StringBuilder();
        report.append("==========================\n");
        report.append("= Informations de compte =\n");
        report.append("==========================\n");
        report.append("\n");
        report.append("Compte Ankama Games : \n");
        report.append("\tCompte : ").append(accountId).append("\n");
        report.append("\tNom de compte : ").append(accountNickname).append("\n");
        report.append("\n");
        report.append("Compte Wakfu : \n");
        report.append("\tServeur : ").append(serverName).append("\n");
        report.append("\tPersonnage : ").append(characterId).append("\n");
        report.append("\tNom du personnage : ").append(characterName).append("\n");
        report.append("\tCoordonn\u00e9es : ").append("(" + x + "," + y + "," + z + ")").append(" @" + instanceId).append("\n");
        report.append("\n");
        report.append("Client : \n");
        report.append("\tVersion : ").append(Version.READABLE_DATED_VERSION).append("\n");
        report.append("\tResolution : ").append(WakfuClientInstance.getInstance().getAppUI().getResolution()).append("\n");
        out.writeBytes(report.toString());
    }
    
    private void writeScreenShot(final DataOutputStream out) throws Exception {
        ImageIO.write(UIDebugInformationGenerator.m_screenBuffer, "jpeg", out);
        UIDebugInformationGenerator.m_screenBuffer = null;
    }
    
    private void writeClientLog(final ZipOutputStream zipfile, final DataOutputStream out) throws Exception {
        final File logDir = this.getLogsDirectory();
        if (!logDir.exists() || !logDir.isDirectory()) {
            zipfile.putNextEntry(new ZipEntry("logs/"));
            zipfile.closeEntry();
            return;
        }
        final File[] arr$;
        final File[] logFiles = arr$ = logDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return UIDebugInformationGenerator.m_patternLogFile.matcher(name).matches();
            }
        });
        for (final File f : arr$) {
            zipfile.putNextEntry(new ZipEntry("logs/" + f.getName()));
            this.writeFile(f, out);
            out.flush();
            zipfile.closeEntry();
        }
    }
    
    private File getLogsDirectory() {
        final String logPath = WakfuClient.INSTANCE.getLogPath();
        return new File(logPath, "logs");
    }
    
    private void writeFile(final File f, final DataOutputStream out) throws Exception {
        final FileInputStream in = new FileInputStream(f);
        final byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
    }
    
    private void writeSystemInformation(final DataOutputStream out) throws Exception {
        out.writeBytes(this.generateConfig());
    }
    
    private String generateConfig() {
        final StringBuilder config = new StringBuilder();
        config.append("=========================\n");
        config.append("= Configuration systeme =\n");
        config.append("=========================\n");
        config.append("\n");
        config.append("[Carte graphique] : \n\n");
        try {
            final Renderer renderer = WakfuClientInstance.getInstance().getRenderer();
            final HashMap<String, String> deviceInformations = renderer.getDeviceInformations();
            for (final Map.Entry<String, String> confLine : deviceInformations.entrySet()) {
                config.append("\t").append(confLine.getKey()).append(" = ").append(confLine.getValue()).append("\n");
            }
        }
        catch (Exception e) {
            config.append("\tImpossible d'envoyer la config openGL : GL non r\u00e9cup\u00e9rable\n");
        }
        config.append("\n");
        config.append("[GL DUMP] : \n\n");
        final GL gl = WakfuClientInstance.getInstance().getRenderer().getGl();
        config.append(GLDebugger.dumpText(gl)).append("\n");
        config.append("\n");
        config.append("[Syst\u00e8me d'exploitation] : \n\n");
        config.append("\tArchitecture : ").append(System.getProperty("os.arch")).append("\n");
        config.append("\tNom : ").append(System.getProperty("os.name")).append("\n");
        config.append("\tVersion : ").append(System.getProperty("os.version")).append("\n");
        config.append("\tPatch Level : ").append(System.getProperty("sun.os.patch.level")).append("\n");
        config.append("\n");
        final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        config.append("[M\u00e9moire Syst\u00e8me]\n\n");
        config.append("\tTotal : ").append(Runtime.getRuntime().totalMemory()).append("\n");
        config.append("\tMax : ").append(Runtime.getRuntime().maxMemory()).append("\n");
        config.append("\tFree : ").append(Runtime.getRuntime().freeMemory()).append("\n");
        config.append("\n");
        config.append("[Java Virtual Machine]\n\n");
        config.append("\tConstructeur : ").append(System.getProperty("java.vm.vendor")).append("\n");
        config.append("\tNom : ").append(System.getProperty("java.vm.name")).append("\n");
        config.append("\tVersion : ").append(System.getProperty("java.vm.version")).append("\n");
        config.append("\n");
        final RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeBean.getUptime();
        if (uptime == 0L) {
            uptime = 1L;
        }
        config.append("[Java Runtime]\n\n");
        config.append("\tNom : ").append(System.getProperty("java.runtime.name")).append("\n");
        config.append("\tVersion : ").append(System.getProperty("java.runtime.version")).append("\n");
        config.append("\n");
        final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        final long[] threadIds = threadBean.getAllThreadIds();
        long userTotal = 0L;
        long cpuTotal = 0L;
        config.append("[Runtime Threads]\n\n");
        for (final long threadId : threadIds) {
            final ThreadInfo threadInfo = threadBean.getThreadInfo(threadId);
            if (threadInfo != null) {
                final long userTime = threadBean.isThreadCpuTimeSupported() ? threadBean.getThreadUserTime(threadId) : 0L;
                final long cpuTime = threadBean.isThreadCpuTimeSupported() ? threadBean.getThreadCpuTime(threadId) : -1L;
                userTotal += userTime;
                cpuTotal += cpuTotal;
                config.append("\t[[Thread ").append(threadId).append("]]\n");
                config.append("\t\tNom : ").append(threadInfo.getThreadName()).append("\n");
                config.append("\t\tUser Time : ").append(userTime).append(" (").append(userTime / (uptime * 10000L)).append("%)\n");
                config.append("\t\tCPU : ").append(cpuTime).append(" (").append(cpuTime / (uptime * 10000L)).append("%)\n");
            }
        }
        config.append("[Thread Total]\n");
        config.append("\tUser Time : ").append(userTotal).append(" (").append(userTotal / (uptime * 10000L)).append("%)\n");
        config.append("\tCPU : ").append(cpuTotal).append(" (").append(cpuTotal / (uptime * 10000L)).append("%)\n");
        config.append("\n");
        return config.toString();
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    public static void takeScreenShot() {
        try {
            final Rectangle glComponentRect = WakfuClientInstance.getInstance().getAppUI().getGLComponentRect();
            final Robot robot = new Robot();
            UIDebugInformationGenerator.m_screenBuffer = robot.createScreenCapture(glComponentRect);
        }
        catch (AWTException e) {
            UIDebugInformationGenerator.m_screenBuffer = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIDebugInformationGenerator.class);
        m_instance = new UIDebugInformationGenerator();
        m_patternLogFile = Pattern.compile("(.*)\\.log");
    }
}
