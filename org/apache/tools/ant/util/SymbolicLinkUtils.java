package org.apache.tools.ant.util;

import org.apache.tools.ant.*;
import java.io.*;
import org.apache.tools.ant.taskdefs.*;

public class SymbolicLinkUtils
{
    private static final FileUtils FILE_UTILS;
    private static final SymbolicLinkUtils PRIMARY_INSTANCE;
    
    public static SymbolicLinkUtils getSymbolicLinkUtils() {
        return SymbolicLinkUtils.PRIMARY_INSTANCE;
    }
    
    public boolean isSymbolicLink(final File file) throws IOException {
        return this.isSymbolicLink(file.getParentFile(), file.getName());
    }
    
    public boolean isSymbolicLink(final String name) throws IOException {
        return this.isSymbolicLink(new File(name));
    }
    
    public boolean isSymbolicLink(final File parent, final String name) throws IOException {
        final File toTest = (parent != null) ? new File(parent.getCanonicalPath(), name) : new File(name);
        return !toTest.getAbsolutePath().equals(toTest.getCanonicalPath());
    }
    
    public boolean isDanglingSymbolicLink(final String name) throws IOException {
        return this.isDanglingSymbolicLink(new File(name));
    }
    
    public boolean isDanglingSymbolicLink(final File file) throws IOException {
        return this.isDanglingSymbolicLink(file.getParentFile(), file.getName());
    }
    
    public boolean isDanglingSymbolicLink(final File parent, final String name) throws IOException {
        final File f = new File(parent, name);
        if (!f.exists()) {
            final String localName = f.getName();
            final String[] c = parent.list(new FilenameFilter() {
                public boolean accept(final File d, final String n) {
                    return localName.equals(n);
                }
            });
            return c != null && c.length > 0;
        }
        return false;
    }
    
    public void deleteSymbolicLink(File link, final Task task) throws IOException {
        if (this.isDanglingSymbolicLink(link)) {
            if (!link.delete()) {
                throw new IOException("failed to remove dangling symbolic link " + link);
            }
        }
        else {
            if (!this.isSymbolicLink(link)) {
                return;
            }
            if (!link.exists()) {
                throw new FileNotFoundException("No such symbolic link: " + link);
            }
            final File target = link.getCanonicalFile();
            if (task == null || target.getParentFile().canWrite()) {
                final File temp = SymbolicLinkUtils.FILE_UTILS.createTempFile("symlink", ".tmp", target.getParentFile(), false, false);
                if (SymbolicLinkUtils.FILE_UTILS.isLeadingPath(target, link)) {
                    link = new File(temp, SymbolicLinkUtils.FILE_UTILS.removeLeadingPath(target, link));
                }
                boolean renamedTarget = false;
                try {
                    try {
                        SymbolicLinkUtils.FILE_UTILS.rename(target, temp);
                        renamedTarget = true;
                    }
                    catch (IOException e) {
                        throw new IOException("Couldn't rename resource when attempting to delete '" + link + "'.  Reason: " + e.getMessage());
                    }
                    if (!link.delete()) {
                        throw new IOException("Couldn't delete symlink: " + link + " (was it a real file? is this " + "not a UNIX system?)");
                    }
                }
                finally {
                    if (renamedTarget) {
                        try {
                            SymbolicLinkUtils.FILE_UTILS.rename(temp, target);
                        }
                        catch (IOException e2) {
                            throw new IOException("Couldn't return resource " + temp + " to its original name: " + target.getAbsolutePath() + ". Reason: " + e2.getMessage() + "\n THE RESOURCE'S NAME ON DISK" + " HAS BEEN CHANGED BY THIS" + " ERROR!\n");
                        }
                    }
                }
            }
            else {
                Execute.runCommand(task, new String[] { "rm", link.getAbsolutePath() });
            }
        }
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
        PRIMARY_INSTANCE = new SymbolicLinkUtils();
    }
}
