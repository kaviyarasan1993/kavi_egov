package org.egov.lams.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentCycleEnum {
	
	 MONTH("Month"),QUARTER("Quarter"),HALFYEAR("HalfYear"),ANNUAL("Annual");

	 private String value;
	 
	 PaymentCycleEnum(String value) {
	     this.value = value;
	 }
	 
	 @Override
	 @JsonValue
	 public String toString() {
	    return String.valueOf(value);
	 }

	 @JsonCreator
	    public static PaymentCycleEnum fromValue(String text) {
	      for (PaymentCycleEnum b : PaymentCycleEnum.values()) {
	        if (String.valueOf(b.value).equals(text)) {
	          return b;
	        }
	      }
	      return null;
	    }
}
