package com.db.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReasonForCorrespondenceRequest {
  private String processProfile;
  private String orderType;
  private String productGroup;
  private String gbm;
  private String inputChannel;
  private String commissioningParty;
  private String processType;
  private String processStep;
  private String nonCustomerFlag;
  private String correspondenceType;
}
