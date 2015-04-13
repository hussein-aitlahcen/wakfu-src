package com.ankamagames.wakfu.client.core.preferences.checker;

import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.baseImpl.graphics.core.*;

public class SetPropertyAction extends AbstractPropertyAction
{
    private static final String VALUE = "value";
    private String m_value;
    
    @Override
    public void load(final DocumentEntry node) {
        super.load(node);
        final DocumentEntry valueParam = node.getParameterByName("value");
        if (valueParam != null) {
            this.m_value = valueParam.getStringValue();
        }
    }
    
    @Override
    public CheckerAction newInstance() {
        return new SetPropertyAction();
    }
    
    @Override
    protected void doExecute(final KeyInterface key, final GamePreferences preferences) {
        preferences.setValue(key, this.m_value);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SetPropertyAction");
        sb.append("{m_name='").append(this.m_name).append('\'');
        sb.append("{m_value='").append(this.m_value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
