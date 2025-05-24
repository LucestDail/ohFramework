package io.framework.oh.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Code {
    private Long id;
    private String code;
    private String name;
    private String description;
    private boolean isActive;
    private CodeGroup codeGroup;
    private Code parentCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getParentId() {
        return parentCode != null ? parentCode.getId() : null;
    }
} 