package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.framework.graphics.opengl.*;
import com.ankamagames.wakfu.client.*;
import java.io.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;

public class TakeScreenShotCommand implements Command
{
    private static final Logger m_logger;
    private static final String FILE_EXTENSION = ".png";
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final ShortcutScreenShotHandler handler = new ShortcutScreenShotHandler();
        try {
            if (!this.createFile(handler)) {
                handler.onScreenShotError(new RuntimeException("Internal error"));
                return;
            }
        }
        catch (IOException e) {
            handler.onScreenShotError(e);
            return;
        }
        final boolean requested = WakfuClientInstance.getInstance().getRenderer().requestScreenShot(handler);
        if (!requested) {
            TakeScreenShotCommand.m_logger.error((Object)"Impossible de faire une requ\u00eate de screenShot aupr\u00e8s du Renderer");
        }
    }
    
    private boolean createFile(final ShortcutScreenShotHandler handler) throws IOException {
        final String path = WakfuClient.INSTANCE.getScreenshotsPath();
        final File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Unable to create directory : path=" + path);
        }
        handler.m_file = new File(dir, System.currentTimeMillis() + ".png");
        return handler.m_file.createNewFile();
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TakeScreenShotCommand.class);
    }
    
    private static final class ShortcutScreenShotHandler implements ScreenShotHandler
    {
        private File m_file;
        
        @NotNull
        @Override
        public File getOutputFile() {
            return this.m_file;
        }
        
        @Override
        public void onScreenShotTook() {
            String absoluteFileName;
            try {
                absoluteFileName = this.m_file.getCanonicalPath();
            }
            catch (IOException e) {
                absoluteFileName = this.m_file.getAbsolutePath();
            }
            absoluteFileName = absoluteFileName.replaceAll("\\\\", "\\\\\\\\");
            final String msg = WakfuTranslator.getInstance().getString("screenshot.took", absoluteFileName);
            ChatManager.getInstance().pushMessage(msg, 4);
        }
        
        @Override
        public void onScreenShotError(final Exception e) {
            final String msg = WakfuTranslator.getInstance().getString("screenshot.fail");
            ChatManager.getInstance().pushMessage(msg, 4);
            TakeScreenShotCommand.m_logger.error((Object)"Erreur lors de la prise de screenShot", (Throwable)e);
        }
    }
}
