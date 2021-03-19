package com.db.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpiryInformationResponse {
  private String expireinDays;
  private String expirationChangeFlag;
  private String expirationAction;
}
