package de.curse.allround.core.cloud.cli;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class Command {
    private final String name;
    private String description = "";
    private String[] alias = {};

    public abstract void execute(String[] args);
}
