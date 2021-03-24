package com.db.model.dmn;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DecisionTableModel {
  private String decisionId;
  private List<Object> inputList;
  private List<Object> outputList;
  private List<Rule> ruleList;

  @Getter
  @Setter
  public static class Rule {
    private List<Object> inputList;
    private List<Object> outputList;
  }
}
