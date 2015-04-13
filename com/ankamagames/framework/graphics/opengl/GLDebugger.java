package com.ankamagames.framework.graphics.opengl;

import org.apache.log4j.*;
import javax.media.opengl.*;
import java.util.*;
import com.sun.opengl.util.*;
import java.nio.*;
import java.io.*;

public class GLDebugger
{
    protected static final Logger m_logger;
    private static final GLDebugger m_instance;
    private Version m_version;
    
    public static GLDebugger getInstance() {
        return GLDebugger.m_instance;
    }
    
    public void readVersion(final GL gl) {
        this.m_version = new Version(gl.glGetString(7938));
    }
    
    public Version getVersion() {
        return this.m_version;
    }
    
    public boolean isVersionLowerThan(final int major, final int minor) {
        if (this.m_version == null) {
            GLDebugger.m_logger.error((Object)"Il faut appeler la fonction readVersion(GL)");
        }
        return this.m_version.compareTo(new Version(major, minor, 0)) < 0;
    }
    
    public static void getGLInformations(final GL gl, final HashMap<String, String> infos) {
        infos.put("Renderer", gl.glGetString(7937));
        infos.put("Vendor", gl.glGetString(7936));
        infos.put("Version", gl.glGetString(7938));
        infos.put("Extensions", gl.glGetString(7939));
    }
    
    public static void dump(final GL gl) {
        GLDebugger.m_logger.info((Object)"------- GL dump ---------");
        final IntBuffer buffer = BufferUtil.newIntBuffer(4);
        buffer.rewind();
        final boolean depthTest = gl.glIsEnabled(2929);
        final boolean scissorTest = gl.glIsEnabled(3089);
        final boolean alphaTest = gl.glIsEnabled(3008);
        final boolean stencilTest = gl.glIsEnabled(2960);
        final boolean cullingTest = gl.glIsEnabled(2884);
        GLDebugger.m_logger.info((Object)("GL_RENDERER : " + gl.glGetString(7937)));
        GLDebugger.m_logger.info((Object)("GL_VENDOR : " + gl.glGetString(7936)));
        GLDebugger.m_logger.info((Object)("GL_VERSION : " + gl.glGetString(7938)));
        GLDebugger.m_logger.info((Object)("GL_DEPTH_TEST = " + depthTest));
        GLDebugger.m_logger.info((Object)("GL_SCISSOR_TEST = " + scissorTest));
        GLDebugger.m_logger.info((Object)("GL_ALPHA_TEST = " + alphaTest));
        gl.glGetIntegerv(3415, buffer);
        GLDebugger.m_logger.info((Object)("GL_STENCIL_TEST = " + stencilTest + ";  Stencil depth = " + buffer.get(0) + " bits"));
        GLDebugger.m_logger.info((Object)("GL_CULL_FACE = " + cullingTest));
        gl.glGetIntegerv(2978, buffer);
        GLDebugger.m_logger.info((Object)("GL_VIEWPORT = [" + buffer.get(0) + ";" + buffer.get(1) + "] - [" + buffer.get(2) + ";" + buffer.get(3) + "]"));
        gl.glGetIntegerv(3088, buffer);
        GLDebugger.m_logger.info((Object)("GL_SCISSOR_BOX = [" + buffer.get(0) + ";" + buffer.get(1) + "] - [" + buffer.get(2) + ";" + buffer.get(3) + "]"));
        gl.glGetIntegerv(33001, buffer);
        GLDebugger.m_logger.info((Object)("GL_MAX_ELEMENTS_INDICES = " + buffer.get(0)));
        gl.glGetIntegerv(33000, buffer);
        GLDebugger.m_logger.info((Object)("GL_MAX_ELEMENTS_VERTICES = " + buffer.get(0)));
    }
    
    public static String dumpText(final GL gl) {
        final StringBuilder result = new StringBuilder();
        final IntBuffer buffer = BufferUtil.newIntBuffer(4);
        buffer.rewind();
        final boolean depthTest = gl.glIsEnabled(2929);
        final boolean scissorTest = gl.glIsEnabled(3089);
        final boolean alphaTest = gl.glIsEnabled(3008);
        final boolean stencilTest = gl.glIsEnabled(2960);
        final boolean cullingTest = gl.glIsEnabled(2884);
        result.append("\tGL_RENDERER : ").append(gl.glGetString(7937));
        result.append("\n\tGL_VENDOR : ").append(gl.glGetString(7936));
        result.append("\n\tGL_VERSION : ").append(gl.glGetString(7938));
        result.append("\n\tGL_DEPTH_TEST = ").append(depthTest);
        result.append("\n\tGL_SCISSOR_TEST = ").append(scissorTest);
        result.append("\n\tGL_ALPHA_TEST = ").append(alphaTest);
        gl.glGetIntegerv(3415, buffer);
        result.append("\n\tGL_STENCIL_TEST = ").append(stencilTest).append("  ").append(buffer.get(0)).append("bits");
        result.append("\n\tGL_CULL_FACE = ").append(cullingTest);
        gl.glGetIntegerv(2978, buffer);
        result.append("\n\tGL_VIEWPORT = [").append(buffer.get(0)).append(";").append(buffer.get(1)).append("] - [").append(buffer.get(2)).append(";").append(buffer.get(3)).append("]");
        gl.glGetIntegerv(3088, buffer);
        result.append("\n\tGL_SCISSOR_BOX = [").append(buffer.get(0)).append(";").append(buffer.get(1)).append("] - [").append(buffer.get(2)).append(";").append(buffer.get(3)).append("]");
        return result.toString();
    }
    
    public static String matrixToString(final FloatBuffer m) {
        String ret = "\n";
        for (int i = 0; i < 4; ++i) {
            ret += "\t";
            for (int j = 0; j < 4; ++j) {
                ret = ret + "[" + m.get(i * 4 + j) + "] ";
            }
            ret += "\n";
        }
        return ret;
    }
    
    public static void convertGLFlags() {
        try {
            final File f = new File("GLFlags.txt");
            if (!f.exists()) {
                GLDebugger.m_logger.error((Object)("cannot open file " + f.getName()));
                return;
            }
            final BufferedReader reader = new BufferedReader(new FileReader(f));
            final BufferedWriter writer = new BufferedWriter(new FileWriter("GLFlags2.txt"));
            String in;
            while ((in = reader.readLine()) != null) {
                writer.write("m_flags.add( new GLFlagDesc( " + in + ", \"" + in + "\" ) );\n");
            }
            writer.close();
            reader.close();
        }
        catch (FileNotFoundException e) {
            GLDebugger.m_logger.error((Object)"Exception", (Throwable)e);
        }
        catch (IOException e2) {
            GLDebugger.m_logger.error((Object)"Exception", (Throwable)e2);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GLDebugger.class);
        m_instance = new GLDebugger();
    }
    
    public class Version
    {
        private int m_major;
        private int m_minor;
        private int m_build;
        
        public Version(final int major, final int minor, final int build) {
            super();
            this.m_major = major;
            this.m_minor = minor;
            this.m_build = build;
        }
        
        public Version(final String version) {
            super();
            this.m_major = 0;
            this.m_minor = 0;
            this.m_build = 0;
            if (version != null && version.length() > 0) {
                final String[] v = version.split("(\\s|\\.)");
                try {
                    this.m_major = Integer.valueOf(v[0]);
                }
                catch (NumberFormatException e) {
                    this.m_major = 0;
                }
                if (v.length >= 2) {
                    try {
                        this.m_minor = Integer.valueOf(v[1]);
                    }
                    catch (NumberFormatException e) {
                        this.m_minor = 0;
                    }
                }
                if (v.length >= 3) {
                    try {
                        this.m_build = Integer.valueOf(v[2]);
                    }
                    catch (NumberFormatException e) {
                        this.m_build = 0;
                    }
                }
            }
        }
        
        public int getBuild() {
            return this.m_build;
        }
        
        public int getMajor() {
            return this.m_major;
        }
        
        public int getMinor() {
            return this.m_minor;
        }
        
        public int compareTo(final Version version, final boolean compareBuild) {
            int compare = this.m_major - version.getMajor();
            if (compare == 0) {
                compare = this.m_minor - version.getMinor();
                if (compare == 0 && compareBuild) {
                    compare = this.m_build - version.getBuild();
                }
            }
            return compare;
        }
        
        public int compareTo(final Version version) {
            return this.compareTo(version, false);
        }
    }
    
    static class GLFlagDesc
    {
        long value;
        String descr;
        
        public GLFlagDesc(final long value, final String descr) {
            super();
            this.value = value;
            this.descr = descr;
        }
        
        public long getValue() {
            return this.value;
        }
        
        public String getDescr() {
            return this.descr;
        }
    }
}
