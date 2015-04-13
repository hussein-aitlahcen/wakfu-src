package org.apache.tools.ant.taskdefs.condition;

import java.util.*;
import org.apache.tools.ant.*;

public class Os implements Condition
{
    private static final String OS_NAME;
    private static final String OS_ARCH;
    private static final String OS_VERSION;
    private static final String PATH_SEP;
    private String family;
    private String name;
    private String version;
    private String arch;
    public static final String FAMILY_WINDOWS = "windows";
    public static final String FAMILY_9X = "win9x";
    public static final String FAMILY_NT = "winnt";
    public static final String FAMILY_OS2 = "os/2";
    public static final String FAMILY_NETWARE = "netware";
    public static final String FAMILY_DOS = "dos";
    public static final String FAMILY_MAC = "mac";
    public static final String FAMILY_TANDEM = "tandem";
    public static final String FAMILY_UNIX = "unix";
    public static final String FAMILY_VMS = "openvms";
    public static final String FAMILY_ZOS = "z/os";
    public static final String FAMILY_OS400 = "os/400";
    private static final String DARWIN = "darwin";
    
    public Os() {
        super();
    }
    
    public Os(final String family) {
        super();
        this.setFamily(family);
    }
    
    public void setFamily(final String f) {
        this.family = f.toLowerCase(Locale.ENGLISH);
    }
    
    public void setName(final String name) {
        this.name = name.toLowerCase(Locale.ENGLISH);
    }
    
    public void setArch(final String arch) {
        this.arch = arch.toLowerCase(Locale.ENGLISH);
    }
    
    public void setVersion(final String version) {
        this.version = version.toLowerCase(Locale.ENGLISH);
    }
    
    public boolean eval() throws BuildException {
        return isOs(this.family, this.name, this.arch, this.version);
    }
    
    public static boolean isFamily(final String family) {
        return isOs(family, null, null, null);
    }
    
    public static boolean isName(final String name) {
        return isOs(null, name, null, null);
    }
    
    public static boolean isArch(final String arch) {
        return isOs(null, null, arch, null);
    }
    
    public static boolean isVersion(final String version) {
        return isOs(null, null, null, version);
    }
    
    public static boolean isOs(final String family, final String name, final String arch, final String version) {
        boolean retValue = false;
        if (family != null || name != null || arch != null || version != null) {
            boolean isFamily = true;
            boolean isName = true;
            boolean isArch = true;
            boolean isVersion = true;
            if (family != null) {
                final boolean isWindows = Os.OS_NAME.indexOf("windows") > -1;
                boolean is9x = false;
                boolean isNT = false;
                if (isWindows) {
                    is9x = (Os.OS_NAME.indexOf("95") >= 0 || Os.OS_NAME.indexOf("98") >= 0 || Os.OS_NAME.indexOf("me") >= 0 || Os.OS_NAME.indexOf("ce") >= 0);
                    isNT = !is9x;
                }
                if (family.equals("windows")) {
                    isFamily = isWindows;
                }
                else if (family.equals("win9x")) {
                    isFamily = (isWindows && is9x);
                }
                else if (family.equals("winnt")) {
                    isFamily = (isWindows && isNT);
                }
                else if (family.equals("os/2")) {
                    isFamily = (Os.OS_NAME.indexOf("os/2") > -1);
                }
                else if (family.equals("netware")) {
                    isFamily = (Os.OS_NAME.indexOf("netware") > -1);
                }
                else if (family.equals("dos")) {
                    isFamily = (Os.PATH_SEP.equals(";") && !isFamily("netware"));
                }
                else if (family.equals("mac")) {
                    isFamily = (Os.OS_NAME.indexOf("mac") > -1 || Os.OS_NAME.indexOf("darwin") > -1);
                }
                else if (family.equals("tandem")) {
                    isFamily = (Os.OS_NAME.indexOf("nonstop_kernel") > -1);
                }
                else if (family.equals("unix")) {
                    isFamily = (Os.PATH_SEP.equals(":") && !isFamily("openvms") && (!isFamily("mac") || Os.OS_NAME.endsWith("x") || Os.OS_NAME.indexOf("darwin") > -1));
                }
                else if (family.equals("z/os")) {
                    isFamily = (Os.OS_NAME.indexOf("z/os") > -1 || Os.OS_NAME.indexOf("os/390") > -1);
                }
                else if (family.equals("os/400")) {
                    isFamily = (Os.OS_NAME.indexOf("os/400") > -1);
                }
                else {
                    if (!family.equals("openvms")) {
                        throw new BuildException("Don't know how to detect os family \"" + family + "\"");
                    }
                    isFamily = (Os.OS_NAME.indexOf("openvms") > -1);
                }
            }
            if (name != null) {
                isName = name.equals(Os.OS_NAME);
            }
            if (arch != null) {
                isArch = arch.equals(Os.OS_ARCH);
            }
            if (version != null) {
                isVersion = version.equals(Os.OS_VERSION);
            }
            retValue = (isFamily && isName && isArch && isVersion);
        }
        return retValue;
    }
    
    static {
        OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);
        OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.ENGLISH);
        PATH_SEP = System.getProperty("path.separator");
    }
}
