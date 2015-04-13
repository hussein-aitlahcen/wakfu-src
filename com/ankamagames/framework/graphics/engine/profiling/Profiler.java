package com.ankamagames.framework.graphics.engine.profiling;

import gnu.trove.*;
import com.ankamagames.framework.graphics.engine.*;
import java.util.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class Profiler
{
    static final double NANO = 1000000.0;
    static final int HISTORY_SIZE = 500;
    static final float WIDTH = 2.0f;
    static final float HEIGHT = 200.0f;
    static final float OFFSET_Y = 300.0f;
    static final Comparator<Node> COMPARATOR_TIME;
    static final Comparator<Node> COMPARATOR_NAME;
    private static final THashMap<String, ProfileData> m_profileData;
    private static final THashMap<String, ColoredHistory> m_history;
    private static final Node m_root;
    private static Node m_current;
    private static ScreenText m_textRenderer;
    private static boolean m_activated;
    static long m_frameCount;
    
    public static boolean isActivated() {
        return Profiler.m_activated && Profiler.m_textRenderer != null;
    }
    
    public static void setActivated(final boolean activated) {
        Profiler.m_activated = activated;
        reset();
    }
    
    public static void initialize(final String fontName, final int style, final int size) {
        Profiler.m_textRenderer = new ScreenText(fontName, style, size);
    }
    
    public static void inc(final String name, final int value, final float r, final float g, final float b, final float a, final double maxValue) {
        if (!isActivated()) {
            return;
        }
        ColoredHistory data = Profiler.m_history.get(name);
        if (data == null) {
            data = new ColoredHistory();
            Profiler.m_history.put(name, data);
        }
        data.m_color[0] = r;
        data.m_color[1] = g;
        data.m_color[2] = b;
        data.m_color[3] = a;
        data.m_heightFactor = 200.0 / maxValue;
        data.incValue(value);
    }
    
    public static void start(final String name, final float r, final float g, final float b) {
        start(name, new float[] { r, g, b, 0.8f });
    }
    
    public static void start(final String name) {
        start(name, null);
    }
    
    private static void start(final String name, final float[] color) {
        if (!isActivated()) {
            return;
        }
        ProfileData data = Profiler.m_profileData.get(name);
        if (data == null) {
            data = new ProfileData(name);
            Profiler.m_profileData.put(name, data);
        }
        data.m_lastFrameUsage = Profiler.m_frameCount;
        data.m_color = color;
        Node o = null;
        for (int i = 0, size = Profiler.m_current.m_children.size(); i < size; ++i) {
            final Node node = Profiler.m_current.m_children.get(i);
            if (node.m_data == data) {
                o = node;
                break;
            }
        }
        if (o == null) {
            o = new Node(data, Profiler.m_current);
            Profiler.m_current.m_children.add(o);
        }
        Profiler.m_current = o;
        data.start();
    }
    
    public static void newFrame() {
        if (!isActivated()) {
            return;
        }
        Profiler.m_current = Profiler.m_root;
        ++Profiler.m_frameCount;
        final ArrayList<String> toremove = new ArrayList<String>();
        Profiler.m_profileData.forEachValue(new TObjectProcedure<ProfileData>() {
            @Override
            public boolean execute(final ProfileData object) {
                object.newFrame(Profiler.m_frameCount);
                if (object.m_lastFrameUsage < Profiler.m_frameCount - 500L) {
                    toremove.add(object.m_name);
                }
                return true;
            }
        });
        for (int i = 0, size = toremove.size(); i < size; ++i) {
            final ProfileData data = Profiler.m_profileData.remove(toremove.get(i));
            remove(Profiler.m_root, data);
        }
        Profiler.m_history.forEachValue(new TObjectProcedure<ColoredHistory>() {
            @Override
            public boolean execute(final ColoredHistory object) {
                object.newFrame(Profiler.m_frameCount);
                return true;
            }
        });
    }
    
    private static boolean remove(final Node node, final ProfileData data) {
        for (int i = 0; i < node.m_children.size(); ++i) {
            if (node.m_children.get(i).m_data == data) {
                node.m_children.remove(i);
                return true;
            }
        }
        for (int i = 0; i < node.m_children.size(); ++i) {
            if (remove(node.m_children.get(i), data)) {
                return true;
            }
        }
        return false;
    }
    
    private static void reset() {
        Profiler.m_frameCount = 0L;
        Profiler.m_profileData.clear();
        Profiler.m_history.clear();
        Profiler.m_current = Profiler.m_root;
        Profiler.m_root.m_children.clear();
    }
    
    public static void end() {
        if (!isActivated()) {
            return;
        }
        final long time = System.nanoTime();
        Profiler.m_profileData.get(Profiler.m_current.m_data.m_name).end(time);
        Profiler.m_current = Profiler.m_current.m_parent;
    }
    
    public static void render(final Renderer renderer) {
        if (!isActivated()) {
            return;
        }
        drawCurve(renderer);
        Profiler.m_textRenderer.prepare(renderer, 8, 0);
        Profiler.m_textRenderer.drawLine(String.format("%15s|%6s|%6s|%6s|%6s", "", "avg/call", "avg", "min", "max"));
        final Node[] sorted = new Node[Profiler.m_root.m_children.size()];
        Profiler.m_root.m_children.toArray(sorted);
        Arrays.sort(sorted, Profiler.COMPARATOR_TIME);
        for (final Node n : sorted) {
            displayProfileTime(n, 0);
        }
        Profiler.m_textRenderer.end();
    }
    
    private static void drawCurve(final Renderer renderer) {
        final GL gl = renderer.getDevice();
        RenderStateManager.getInstance().enableTextures(false);
        RenderStateManager.getInstance().applyStates(renderer);
        renderer.setWorldMatrix(Matrix44.IDENTITY);
        renderer.setCameraMatrix(Matrix44.IDENTITY);
        renderer.updateMatrix();
        final float[] axisColor = { 0.2f, 0.2f, 0.2f, 0.9f };
        gl.glColor4fv(axisColor, 0);
        gl.glBegin(1);
        final float x = -500.0f;
        final float y = -300.0f;
        gl.glVertex2f(-500.0f, -300.0f);
        gl.glVertex2f(500.0f, -300.0f);
        gl.glVertex2f(-500.0f, -300.0f);
        gl.glVertex2f(-500.0f, -100.0f);
        gl.glEnd();
        Profiler.m_profileData.forEachValue(new TObjectProcedure<ProfileData>() {
            @Override
            public boolean execute(final ProfileData object) {
                object.drawCurve(gl);
                return true;
            }
        });
        Profiler.m_history.forEachValue(new TObjectProcedure<ColoredHistory>() {
            @Override
            public boolean execute(final ColoredHistory object) {
                ProfileData.drawHistory(gl, object, object.m_color, object.m_heightFactor);
                return true;
            }
        });
        Profiler.m_textRenderer.prepare(renderer, 0, (int)(renderer.getViewportHeight() * 0.5f - 200.0f - 16.0f - 300.0f));
        Profiler.m_textRenderer.drawLine("10 ms", axisColor);
        Profiler.m_textRenderer.end();
    }
    
    private static void displayProfileTime(final Node node, final int offset) {
        String s = "";
        for (int i = 0; i < offset; ++i) {
            s += "_";
        }
        if (node.m_data.m_lastFrameUsage != Profiler.m_frameCount) {
            s += "#";
        }
        if (node.m_data.m_duration.m_avg / 1000000.0 > 0.01 || offset >= 2 || offset == 0) {
            if (node.m_data.m_duration.m_avg / 1000000.0 > 0.01 || node.m_data.m_name.startsWith("*")) {
                Profiler.m_textRenderer.drawLine(node.m_data.toString(s), node.m_data.m_color);
            }
            final Node[] sorted = new Node[node.m_children.size()];
            node.m_children.toArray(sorted);
            Arrays.sort(sorted, Profiler.COMPARATOR_TIME);
            for (final Node n : sorted) {
                displayProfileTime(n, offset + 1);
            }
        }
    }
    
    static {
        COMPARATOR_TIME = new Comparator<Node>() {
            @Override
            public int compare(final Node o1, final Node o2) {
                return o2.m_data.m_duration.m_avg - o1.m_data.m_duration.m_avg;
            }
        };
        COMPARATOR_NAME = new Comparator<Node>() {
            @Override
            public int compare(final Node o1, final Node o2) {
                return o1.m_data.m_name.compareTo(o2.m_data.m_name);
            }
        };
        m_profileData = new THashMap<String, ProfileData>(10);
        m_history = new THashMap<String, ColoredHistory>(10);
        m_root = new Node((ProfileData)null, (Node)null);
        Profiler.m_current = Profiler.m_root;
        Profiler.m_activated = false;
        Profiler.m_frameCount = 0L;
    }
    
    private static class Node
    {
        final ProfileData m_data;
        final Node m_parent;
        ArrayList<Node> m_children;
        
        private Node(final ProfileData data, final Node parent) {
            super();
            this.m_children = new ArrayList<Node>();
            this.m_data = data;
            this.m_parent = parent;
        }
    }
    
    private static class ColoredHistory extends History
    {
        final float[] m_color;
        double m_heightFactor;
        
        private ColoredHistory() {
            super();
            this.m_color = new float[4];
            this.m_heightFactor = 1.0;
        }
    }
}
