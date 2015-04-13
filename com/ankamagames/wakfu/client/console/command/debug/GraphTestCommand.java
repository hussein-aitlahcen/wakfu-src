package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;

public class GraphTestCommand implements Command
{
    protected static final Logger m_logger;
    private static Runnable m_process;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final String dialogId = "graphDialog";
        if (Xulor.getInstance().isLoaded("graphDialog")) {
            Xulor.getInstance().unload("graphDialog");
            return;
        }
        Xulor.getInstance().load("graphDialog", Dialogs.getDialogPath("graphDialog"), (short)10000);
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("graphDialog");
        final int[] indices = new int[5];
        final Color[] colors = { new Color(1.0f, 0.8f, 0.3f, 1.0f), new Color(0.0f, 0.5f, 1.0f, 1.0f) };
        final Color[] colors2 = { new Color(1.0f, 0.8f, 0.3f, 0.3f), new Color(0.0f, 0.5f, 1.0f, 0.3f) };
        final GraphMesh.GraphLine line1 = new GraphMesh.GraphLine();
        line1.setColors(colors, indices);
        line1.setValues(new float[] { 12.0f, 8.5f, 9.0f, 10.0f, 15.0f });
        final int[] indices2 = { 0, 1, 0, 0, 0 };
        final GraphMesh.GraphLine line2 = new GraphMesh.GraphLine();
        line2.setColors(colors, indices2);
        line2.setValues(new float[] { 11.0f, -5.0f, 3.0f, 8.0f, 7.0f });
        final int[] indices3 = new int[10];
        indices3[3] = 1;
        final GraphMesh.GraphZone graphZone = new GraphMesh.GraphZone();
        graphZone.setColors(colors2, indices3);
        graphZone.addGraphLine(line1);
        graphZone.addGraphLine(line2);
        final GraphMesh.GraphElement winterZone = new GraphMesh.GraphElement();
        winterZone.setBackgroundPixmap(null);
        winterZone.setModulationColor(new Color(0.9f, 0.9f, 1.0f, 1.0f));
        final GraphMesh.GraphElement springZone = new GraphMesh.GraphElement();
        springZone.setBackgroundPixmap(null);
        springZone.setModulationColor(new Color(0.9f, 1.0f, 0.9f, 1.0f));
        final Graph graph = (Graph)map.getElement("graph");
        final Graph.GraphData data = new Graph.GraphData();
        data.addGraphLine(line1);
        data.addGraphLine(line2);
        data.addGraphZone(graphZone);
        for (int i = 0; i < 3; ++i) {
            data.addGraphElement(winterZone);
        }
        for (int i = 0; i < 2; ++i) {
            data.addGraphElement(springZone);
        }
        data.setMinValue(-8.0f);
        data.setMaxValue(22.0f);
        graph.setContent(data);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GraphTestCommand.class);
        GraphTestCommand.m_process = null;
    }
}
