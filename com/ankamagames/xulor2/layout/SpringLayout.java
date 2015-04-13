package com.ankamagames.xulor2.layout;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.*;
import java.awt.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;

public class SpringLayout extends AbstractLayoutManager
{
    public static final String TAG = "SpringLayout";
    public static final String SHORT_TAG = "SPL";
    private HashMap<Widget, ArrayList<SpringLayoutData>> m_constraints;
    
    public SpringLayout() {
        super();
        this.m_constraints = new HashMap<Widget, ArrayList<SpringLayoutData>>();
    }
    
    @Override
    public String getTag() {
        return "SpringLayout";
    }
    
    public SpringLayoutData getConstraint(final Widget w) {
        final ArrayList<SpringLayoutData> list = this.m_constraints.get(w);
        return (list != null && list.size() != 0) ? list.get(0) : null;
    }
    
    private Widget getWidgetByConstraint(final Container parent, final SpringLayoutData data) {
        for (final Widget w : parent.getWidgetChildren()) {
            if (w.getLayoutData() instanceof SpringLayoutData) {
                final SpringLayoutData spld = (SpringLayoutData)w.getLayoutData();
                if (spld.equals(data)) {
                    return w;
                }
                continue;
            }
        }
        return null;
    }
    
    private void cleanUpUnusedWidgets(final ArrayList<Widget> list) {
        final Set<Widget> set = this.m_constraints.keySet();
        for (int i = list.size() - 1; i >= 0; --i) {
            final Widget w = list.get(i);
            if (!set.contains(w)) {
                this.m_constraints.remove(w);
            }
        }
    }
    
    private void ensureWidgetHasSpringLayoutData(final Widget w) {
        if (w.getLayoutData() instanceof SpringLayoutData) {
            return;
        }
        ArrayList<SpringLayoutData> allConstraints = this.m_constraints.get(w);
        if (allConstraints == null) {
            allConstraints = new ArrayList<SpringLayoutData>();
            this.m_constraints.put(w, allConstraints);
        }
        if (allConstraints.size() == 0) {
            final SpringLayoutData constraints = SpringLayoutData.defaultConstraints(this, w);
            allConstraints.add(constraints);
        }
    }
    
    @Override
    public boolean canComputeContentSize() {
        return false;
    }
    
    @Override
    public Dimension getContentMinSize(final Container container) {
        this.cleanUpUnusedWidgets(container.getWidgetChildren());
        final Rectangle rect = new Rectangle();
        for (final Widget w : container.getWidgetChildren()) {
            SpringLayoutData constraints = null;
            this.ensureWidgetHasSpringLayoutData(w);
            constraints = this.m_constraints.get(w).get(0);
            rect.union(new Rectangle(constraints.getX().getValue(), constraints.getY().getValue(), constraints.getWidth().getValue(), constraints.getHeight().getValue()));
        }
        return new Dimension((int)rect.getWidth(), (int)rect.getHeight());
    }
    
    @Override
    public Dimension getContentPreferedSize(final Container container) {
        final ArrayList<Widget> children = container.getWidgetChildren();
        this.cleanUpUnusedWidgets(children);
        final Rectangle rect = new Rectangle();
        for (int i = children.size() - 1; i >= 0; --i) {
            final Widget w = children.get(i);
            SpringLayoutData constraints = null;
            this.ensureWidgetHasSpringLayoutData(w);
            constraints = this.m_constraints.get(w).get(0);
            rect.union(new Rectangle(constraints.getX().getValue(), constraints.getY().getValue(), constraints.getWidth().getValue(), constraints.getHeight().getValue()));
        }
        return new Dimension((int)rect.getWidth(), (int)rect.getHeight());
    }
    
    public static void doSpringLayout(final Container container, final List<Widget> list) {
    }
    
    @Override
    public void layoutContainer(final Container container) {
        final ArrayList<Widget> list = container.getWidgetChildren();
        if (list == null) {
            return;
        }
        for (int i = list.size() - 1; i >= 0; --i) {
            final Widget w = list.get(i);
            StaticLayout.doLayoutStatic(container, w);
            this.ensureWidgetHasSpringLayoutData(w);
        }
        for (int i = list.size() - 1; i >= 0; --i) {
            final Widget w = list.get(i);
            if (w.getLayoutData() instanceof SpringLayoutData) {
                final SpringLayoutData constraints = this.m_constraints.get(w).get(0);
                if (constraints != null) {
                    final int x = constraints.getX().getValue();
                    final int y = constraints.getY().getValue();
                    final int width = constraints.getWidth().getValue();
                    final int height = constraints.getHeight().getValue();
                    w.setPosition(x, y);
                    w.setSize(new Dimension(width, height));
                }
            }
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_constraints.clear();
    }
    
    @Override
    public SpringLayout clone() {
        final SpringLayout l = new SpringLayout();
        l.onCheckOut();
        this.copyElement(l);
        return l;
    }
    
    private boolean isFullyDisplayed(final Container c, final SpringLayoutData spld) {
        return spld.getX().getValue() >= 0 && spld.getY().getValue() >= 0 && spld.getX().getValue() + spld.getWidth().getValue() <= c.getWidth() && spld.getY().getValue() + spld.getHeight().getValue() <= c.getHeight();
    }
    
    public void putConstraint(final Widget w1, final SpringLayoutData constraints) {
        if (constraints == null) {
            return;
        }
        ArrayList<SpringLayoutData> constraintsList = this.m_constraints.get(w1);
        if (constraintsList == null) {
            constraintsList = new ArrayList<SpringLayoutData>();
            this.m_constraints.put(w1, constraintsList);
        }
        else {
            constraintsList.clear();
        }
        constraintsList.add(constraints);
    }
    
    public void removeWidgetData(final Widget widget) {
        this.m_constraints.remove(widget);
    }
}
