package org.apache.tools.ant.input;

import java.util.*;

public class MultipleChoiceInputRequest extends InputRequest
{
    private final LinkedHashSet<String> choices;
    
    public MultipleChoiceInputRequest(final String prompt, final Vector<String> choices) {
        super(prompt);
        if (choices == null) {
            throw new IllegalArgumentException("choices must not be null");
        }
        this.choices = new LinkedHashSet<String>(choices);
    }
    
    public Vector<String> getChoices() {
        return new Vector<String>(this.choices);
    }
    
    public boolean isInputValid() {
        return this.choices.contains(this.getInput()) || ("".equals(this.getInput()) && this.getDefaultValue() != null);
    }
}
