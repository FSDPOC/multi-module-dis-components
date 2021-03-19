package com.db.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpiryInformationRequest {
  private String categories;
  private String reasons;
  private String priority;
  private String inputChannel;
}
