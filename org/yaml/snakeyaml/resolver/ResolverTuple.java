package org.yaml.snakeyaml.resolver;

import org.yaml.snakeyaml.nodes.*;
import java.util.regex.*;

final class ResolverTuple
{
    private final Tag tag;
    private final Pattern regexp;
    
    public ResolverTuple(final Tag tag, final Pattern regexp) {
        super();
        this.tag = tag;
        this.regexp = regexp;
    }
    
    public Tag getTag() {
        return this.tag;
    }
    
    public Pattern getRegexp() {
        return this.regexp;
    }
    
    public String toString() {
        return "Tuple tag=" + this.tag + " regexp=" + this.regexp;
    }
}
