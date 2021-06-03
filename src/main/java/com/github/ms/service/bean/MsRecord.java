package com.github.ms.service.bean;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MsRecord {
    private String id;
    private long timestamp;
    private String from;
    private List<String> toList;
    private Map<String, String> extHeader;
    private String content;
}
