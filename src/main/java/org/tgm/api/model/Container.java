package org.tgm.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Container {
    private String containerId;
    private String containerName;
    private String status; // running, stopped, paused
    private Long userId;
}

