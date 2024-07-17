package org.example.catcafereservation;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({ValidationGroups.NotBlankGroup.class, ValidationGroups.PatternGroup.class, Default.class})
public interface ValidationOrder {
}
