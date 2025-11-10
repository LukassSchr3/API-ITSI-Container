package org.tgm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContainerDTO {
    private int containerId;
    private String containerName;
    private String status;
    private int userId;
    private int aufgabenId;
    private String ipAddress;
    private Integer port;
}

