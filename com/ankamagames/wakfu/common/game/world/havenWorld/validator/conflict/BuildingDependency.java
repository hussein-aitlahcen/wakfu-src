package com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class BuildingDependency extends ConstructionError
{
    private static final Logger m_logger;
    private final ConstructionError m_constructionError;
    
    public BuildingDependency(final ModificationItem item, final ConstructionError constructionError) {
        super(item);
        this.m_constructionError = constructionError;
    }
    
    public ConstructionError getConstructionError() {
        return this.m_constructionError;
    }
    
    @Override
    public boolean equals(final ConstructionError error) {
        if (this == error) {
            return true;
        }
        if (error == null || this.getClass() != error.getClass()) {
            return false;
        }
        final BuildingDependency that = (BuildingDependency)error;
        return this.m_item.equals(that.m_item);
    }
    
    @Override
    public String toString() {
        return "BuildingDependency{m_item=" + this.m_item + ", m_constructionError=" + this.m_constructionError + '}';
    }
    
    @Override
    public final Type getType() {
        return Type.BuildingDependency;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BuildingDependency.class);
    }
}
