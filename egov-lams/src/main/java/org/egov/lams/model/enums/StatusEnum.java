package org.egov.lams.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusEnum {

	ACTIVE("active"),CANCELLED("cancelled"),EVICTED("evicted");
	
	private String value;
	
	StatusEnum(String value) {
	      this.value = value;
	}
	
	@Override
	@JsonValue
	public String toString() {
	    return String.valueOf(value);
	}
	@JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
}
