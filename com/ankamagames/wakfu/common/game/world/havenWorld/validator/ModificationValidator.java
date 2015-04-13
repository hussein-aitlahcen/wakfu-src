package com.ankamagames.wakfu.common.game.world.havenWorld.validator;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict.*;
import java.util.*;

public abstract class ModificationValidator<T>
{
    private static final Logger m_logger;
    private final ArrayList<ConstructionError> m_errors;
    protected final T m_dataProvider;
    
    protected ModificationValidator(final T dataProvider) {
        super();
        this.m_errors = new ArrayList<ConstructionError>();
        this.m_dataProvider = dataProvider;
    }
    
    protected final void addConflict(final ModificationItem item) {
        this.addError(new ModificationError(item));
    }
    
    protected final void addError(final ConstructionError item) {
        for (final ConstructionError modif : this.m_errors) {
            if (modif.equals(item)) {
                return;
            }
        }
        this.m_errors.add(item);
    }
    
    public final ArrayList<ConstructionError> getErrors() {
        return this.m_errors;
    }
    
    public final ConstructionError getError(final ConstructionError.Type type) {
        for (int i = 0; i < this.m_errors.size(); ++i) {
            if (this.m_errors.get(i).getType() == type) {
                return this.m_errors.get(i);
            }
        }
        return null;
    }
    
    public final boolean hasErrors() {
        return !this.m_errors.isEmpty();
    }
    
    public final boolean hasError(final ConstructionError.Type type) {
        for (int i = 0; i < this.m_errors.size(); ++i) {
            if (this.m_errors.get(i).getType() == type) {
                return true;
            }
        }
        return false;
    }
    
    protected void removeAllErrors() {
        this.m_errors.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ModificationValidator.class);
    }
}
