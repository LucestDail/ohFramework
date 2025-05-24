package io.framework.oh.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class NewsCompany {
    private Long id;
    private String newsCompanyCode;
    private String newsCompanyName;
    private String etc1;
    private String etc2;
    private String etc3;
    private String etc4;
    private String etc5;
    private LocalDateTime newsCompanyCreateDt;
    private LocalDateTime newsCompanyUpdateDt;
} 