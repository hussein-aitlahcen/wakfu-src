package org.apache.tools.ant.util;

import java.util.*;

public class CompositeMapper extends ContainerMapper
{
    public String[] mapFileName(final String sourceFileName) {
        final LinkedHashSet results = new LinkedHashSet();
        FileNameMapper mapper = null;
        final Iterator mIter = this.getMappers().iterator();
        while (mIter.hasNext()) {
            mapper = mIter.next();
            if (mapper != null) {
                final String[] mapped = mapper.mapFileName(sourceFileName);
                if (mapped == null) {
                    continue;
                }
                for (int i = 0; i < mapped.length; ++i) {
                    results.add(mapped[i]);
                }
            }
        }
        return (String[])((results.size() == 0) ? null : ((String[])results.toArray(new String[results.size()])));
    }
}
