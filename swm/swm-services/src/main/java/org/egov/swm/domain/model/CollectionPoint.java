package org.egov.swm.domain.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionPoint {

    @JsonProperty("code")
    private String code = null;

    @NotNull
    @Length(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @Size(min = 1, max = 128)
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("location")
    private Boundary location = null;

    @Valid
    @JsonProperty("binDetails")
    private List<BinDetails> binDetails = null;

    @Valid
    @JsonProperty("collectionPointDetails")
    private List<CollectionPointDetails> collectionPointDetails = null;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
