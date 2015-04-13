package com.ankamagames.xulor2.decorator;

import org.apache.log4j.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.decorator.mesh.*;

public class GradientBackground extends PlainBackground
{
    private static Logger m_logger;
    public static final String TAG = "GradientBackground";
    private GradientBackgroundMesh m_mesh;
    
    @Override
    public String getTag() {
        return "GradientBackground";
    }
    
    @Override
    public void setColor(final ColorElement c) {
        this.setColor(c.getColor(), c.getPosition());
    }
    
    public void setColor(final Color color, final GradientBackgroundMesh.GradientBackgroundColorAlign align) {
        this.getMesh().setColor(color, align);
    }
    
    @Override
    public GradientBackgroundMesh getMesh() {
        return this.m_mesh;
    }
    
    @Override
    public boolean isValidAdd(final BasicElement e) {
        if (e instanceof ColorElement && ((ColorElement)e).getPosition() == null) {
            GradientBackground.m_logger.error((Object)"Tentative d'ajout d'un ColorElement sans position");
            return false;
        }
        return super.isValidAdd(e);
    }
    
    @Override
    protected void cleanColorsInChildren(final ColorElement c) {
        if (this.m_children == null) {
            return;
        }
        for (int i = this.m_children.size() - 1; i >= 0; --i) {
            final EventDispatcher e = this.m_children.get(i);
            if (e instanceof ColorElement && ((ColorElement)e).getPosition().equals(c.getPosition())) {
                this.destroy(e);
            }
        }
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        (this.m_mesh = new GradientBackgroundMesh()).onCheckOut();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_mesh.onCheckIn();
    }
    
    static {
        GradientBackground.m_logger = Logger.getLogger((Class)GradientBackground.class);
    }
}
