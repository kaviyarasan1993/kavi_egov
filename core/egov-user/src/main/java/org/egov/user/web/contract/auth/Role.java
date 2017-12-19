package org.egov.user.web.contract.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
//This class is serialized to Redis
public class Role implements Serializable {
    private static final long serialVersionUID = 2090518436085399889L;
    private Long id;
    private String name;
    private String code;
    private String tenantId;

    public Role(org.egov.user.domain.model.Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.code= role.getCode();
        this.tenantId=role.getTenantId();
    }
}
