<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/DMN/20151101/dmn.xsd" id="definitions" name="definitions" namespace="http://camunda.org/schema/1.0/dmn">
  <decision id="${model.decisionId}" name="${model.decisionId}">
    <decisionTable id="decisionTable" hitPolicy="COLLECT">
      <#list model.inputList as input>
        <input id="${input}">
          <inputExpression id="input-${input?index}" typeRef="string">
            <text>${input}</text>
          </inputExpression>
        </input>
      </#list>
      <#list model.outputList as output>
        <output id="output-${output?index}" name="${output}" typeRef="string"/>
      </#list>

      <#list model.ruleList as rule>
        <rule id="rule-${rule?index}">
          <#list rule.inputList as input>
            <inputEntry id="input-${rule?index}-${input?index}">
              <text>"${input}"</text>
            </inputEntry>
          </#list>
          <#list rule.outputList as output>
            <outputEntry id="output-${rule?index}-${output?index}">
              <text>"${output}"</text>
            </outputEntry>
          </#list>
        </rule>
      </#list>
    </decisionTable>
  </decision>
</definitions>