package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.nodes.*;

public interface Construct
{
    Object construct(Node p0);
    
    void construct2ndStep(Node p0, Object p1);
}
