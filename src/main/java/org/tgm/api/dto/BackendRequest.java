package org.tgm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackendRequest<T> {
    private String action; // start, stop, list, delete, etc.
    private T payload;
}

