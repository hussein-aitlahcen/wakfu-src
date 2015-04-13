package com.ankamagames.wakfu.client.console.command.debug.anim;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.script.function.context.*;

public class AnmDebuggerFieldProvider implements FieldProvider
{
    private static final AnmDebuggerFieldProvider m_instance;
    public static final String LINKAGES = "linkages";
    public static final String SELECTED_LINKAGE = "selectedLinkage";
    public static final String TYPE = "type";
    private ArrayList<String> m_linkages;
    private String m_selectedLinkage;
    private String m_type;
    
    private AnmDebuggerFieldProvider() {
        super();
        this.m_linkages = new ArrayList<String>();
        this.m_type = "players";
    }
    
    public static AnmDebuggerFieldProvider getInstance() {
        return AnmDebuggerFieldProvider.m_instance;
    }
    
    public void updateLinkages() {
        this.m_linkages.clear();
        final ArrayList<String> linkages = new ArrayList<String>();
        final CharacterActor actor = WakfuGameEntity.getInstance().getLocalPlayer().getActor();
        actor.onAnmLoaded(new Runnable() {
            @Override
            public void run() {
                actor.getAnmInstance().getAnimationList(linkages);
                for (int i = 0, size = linkages.size(); i < size; ++i) {
                    final String linkage = linkages.get(i);
                    final int end = -1;
                    final String toAdd = linkage.substring(2, (end == -1) ? linkage.length() : end);
                    if (!AnmDebuggerFieldProvider.this.m_linkages.contains(toAdd)) {
                        AnmDebuggerFieldProvider.this.m_linkages.add(toAdd);
                    }
                }
                Collections.sort((List<Comparable>)AnmDebuggerFieldProvider.this.m_linkages);
                PropertiesProvider.getInstance().firePropertyValueChanged(AnmDebuggerFieldProvider.this, "linkages");
                actor.getAnmInstance().forceUpdate();
                if (AnmDebuggerFieldProvider.this.m_linkages.size() > 0) {
                    AnmDebuggerFieldProvider.this.setSelectedLinkage(AnmDebuggerFieldProvider.this.m_linkages.get(0));
                }
            }
        });
    }
    
    public void setType(final String type) {
        this.m_type = type;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "type");
    }
    
    public void setSkin(final String skinId) {
        this.setSkin(this.m_type, skinId);
    }
    
    public void setSkin(final String type, final String skinId) {
        final CharacterActor actor = WakfuGameEntity.getInstance().getLocalPlayer().getActor();
        try {
            final String gfxFile = CreateAnimatedElement.getFilename(type, skinId);
            actor.load(gfxFile, true);
        }
        catch (Exception ex) {}
        this.updateLinkages();
        if (this.m_linkages.size() > 0) {
            this.setSelectedLinkage(this.m_linkages.get(0));
        }
    }
    
    public void setSelectedLinkage(final String selected) {
        this.m_selectedLinkage = selected;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedLinkage");
        WakfuGameEntity.getInstance().getLocalPlayer().getActor().setAnimation(selected);
        this.play();
    }
    
    public void play() {
        if (this.m_selectedLinkage != null) {
            WakfuGameEntity.getInstance().getLocalPlayer().getActor().setAnimation(this.m_selectedLinkage);
        }
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("linkages")) {
            return this.m_linkages;
        }
        if (fieldName.equals("selectedLinkage")) {
            return this.m_selectedLinkage;
        }
        if (fieldName.equals("type")) {
            return this.m_type;
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    static {
        m_instance = new AnmDebuggerFieldProvider();
    }
}
