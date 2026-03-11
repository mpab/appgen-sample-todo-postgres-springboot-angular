package com.api.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskReferences {
    public List<AssigneeEnum> assignee_enum;
    public List<StatusEnum> status_enum;

    public TaskReferences() {
        this.assignee_enum = new ArrayList<>();
        this.status_enum = new ArrayList<>();
    }
}
