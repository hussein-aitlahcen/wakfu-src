package org.apache.tools.ant.types;

import java.util.*;

public class FilterSetCollection
{
    private List<FilterSet> filterSets;
    
    public FilterSetCollection() {
        super();
        this.filterSets = new ArrayList<FilterSet>();
    }
    
    public FilterSetCollection(final FilterSet filterSet) {
        super();
        this.filterSets = new ArrayList<FilterSet>();
        this.addFilterSet(filterSet);
    }
    
    public void addFilterSet(final FilterSet filterSet) {
        this.filterSets.add(filterSet);
    }
    
    public String replaceTokens(final String line) {
        String replacedLine = line;
        for (final FilterSet filterSet : this.filterSets) {
            replacedLine = filterSet.replaceTokens(replacedLine);
        }
        return replacedLine;
    }
    
    public boolean hasFilters() {
        for (final FilterSet filterSet : this.filterSets) {
            if (filterSet.hasFilters()) {
                return true;
            }
        }
        return false;
    }
}
