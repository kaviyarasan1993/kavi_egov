package org.egov.lams.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NatureOfAllotmentEnum {
	
	TENDER("tender"),DIRECT("direct"),AUCTION("auction");

    private String value;

    NatureOfAllotmentEnum(String value) {
      this.value = value;
    }
    
    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
    
    @JsonCreator
    public static NatureOfAllotmentEnum fromValue(String text) {
      for (NatureOfAllotmentEnum b : NatureOfAllotmentEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

}
