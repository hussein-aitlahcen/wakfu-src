package com.ankamagames.wakfu.client.core.game.tutorial;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.events.actions.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class TutorialEntryView extends ImmutableFieldProvider implements UITutorialFrame.TutorialEventListener
{
    public static final String NAME_FIELD = "name";
    public static final String IS_NEW_FIELD = "isNew";
    public static final String IS_LAUNCHED_FIELD = "isLaunched";
    private final ClientEventActionLaunchTutorial m_clientEventActionLaunchTutorial;
    private boolean m_isNew;
    
    public TutorialEntryView(final ClientEventActionLaunchTutorial cea) {
        super();
        this.m_clientEventActionLaunchTutorial = cea;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("isNew")) {
            return this.m_isNew;
        }
        if (fieldName.equals("isLaunched")) {
            return this.isLaunched();
        }
        return null;
    }
    
    public String getName() {
        return this.m_clientEventActionLaunchTutorial.getTitle();
    }
    
    public void setNew(final boolean aNew) {
        this.m_isNew = aNew;
    }
    
    public void launch() {
        UITutorialFrame.getInstance().add(this);
        this.m_clientEventActionLaunchTutorial.execute();
    }
    
    public boolean isLaunched() {
        final TutorialView tutorialView = (TutorialView)PropertiesProvider.getInstance().getObjectProperty("tutorialMessageView");
        return this.matches(tutorialView);
    }
    
    private boolean matches(final TutorialView tutorialView) {
        return tutorialView != null && tutorialView.getTitle().equals(this.getName());
    }
    
    @Override
    public void onTutorialRemoved(final TutorialView tutorialView) {
        if (!this.matches(tutorialView)) {
            return;
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isLaunched");
        UITutorialFrame.getInstance().remove(this);
    }
    
    @Override
    public void onTutorialLaunched(final TutorialView tutorialView) {
        if (!this.matches(tutorialView)) {
            return;
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isLaunched");
    }
}
