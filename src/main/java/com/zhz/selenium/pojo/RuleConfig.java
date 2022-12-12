package com.zhz.selenium.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.signature.qual.FullyQualifiedName;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;

@Data
public class RuleConfig {
    private Integer id;
    private String keyName;
    private String remark;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;
    private String delState;
    private String condition;
}
