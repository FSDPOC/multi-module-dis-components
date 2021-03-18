package com.db.service.impl;

import com.db.config.Constants;
import com.db.model.request.ReasonForCorrespondenceRequest;
import com.db.model.request.TemplateRdtRequest;
import com.db.model.response.ReasonForCorrespondenceResponse;
import com.db.model.response.TemplateRdtResponse;
import com.db.persistence.entity.DmnTemplate;
import com.db.persistence.repository.DmnTemplateRepository;
import com.db.service.DmnService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DmnServiceImpl implements DmnService {

    private DmnEngine dmnEngine;
    private DmnDecision decision;
    private DmnTemplateRepository dmnTemplateRepository;
    private ConcurrentMap<String, DmnDecision> decisionMap;

    public DmnServiceImpl(DmnTemplateRepository dmnTemplateRepository) {
        this.dmnTemplateRepository = dmnTemplateRepository;
        this.decisionMap = new ConcurrentHashMap<>();
    }

    @PostConstruct
    @Override
    public void loadDecision() {
        dmnEngine = DmnEngineConfiguration.createDefaultDmnEngineConfiguration().buildEngine();
        List<DmnTemplate> templateList = dmnTemplateRepository.findAll();
        templateList.forEach(
            t -> {
                DmnModelInstance dmnModelInstance =
                    Dmn.readModelFromStream(
                        IOUtils.toInputStream(t.getTemplate()));
                decisionMap.put(t.getType(),
                    dmnEngine.parseDecision(t.getType(), dmnModelInstance));
            }
        );
    }

    public Long decideQuarter(Integer month) {
        if (decision == null) loadDecision();

        VariableMap variables = Variables.createVariables().putValue("month", month);
        Long quarter =
                dmnEngine
                        .evaluateDecision(decision, variables)
                        .getSingleResult()
                        .<Long>getEntry("quarter");
        log.info("quarter is {}", quarter);
        return quarter;
    }

    @Override
    public List<ReasonForCorrespondenceResponse> decideReason(ReasonForCorrespondenceRequest request) throws Exception {
        if (!decisionMap.containsKey(Constants.DMN_RULE_TYPE_REASON_FOR_CORRESPONDENCE))
            throw new Exception("No rules setup for " + Constants.DMN_RULE_TYPE_REASON_FOR_CORRESPONDENCE);

        DmnDecision decision = decisionMap.get(Constants.DMN_RULE_TYPE_REASON_FOR_CORRESPONDENCE);

        VariableMap variables = Variables
            .createVariables()
            .putValue("ProcessProfile", request.getProcessProfile())
            .putValue("OrderType", request.getOrderType())
            .putValue("ProductGroup", request.getProductGroup())
            .putValue("GBM", request.getGbm())
            .putValue("InputChannel", request.getInputChannel())
            .putValue("CommissioningParty", request.getCommissioningParty())
            .putValue("ProcessType", request.getProcessType())
            .putValue("ProcessStep", request.getProcessStep())
            .putValue("NonCustomerFlag", request.getNonCustomerFlag())
            .putValue("CorrespondenceType", request.getCorrespondenceType());

        List<Map<String, Object>> resultList = dmnEngine
            .evaluateDecision(decision, variables)
            .getResultList();

        return resultList.stream()
            .map(m -> ReasonForCorrespondenceResponse
                .builder()
                .categories((String)m.get("Categories"))
                .reasons((String)m.get("Reasons"))
                .build()
            ).collect(Collectors.toList());
    }

    @Override
    public List<TemplateRdtResponse> decideTemplateRdt(TemplateRdtRequest request)
        throws Exception {
        if (!decisionMap.containsKey(Constants.DMN_RULE_TYPE_TEMPLATE_RDT))
            throw new Exception("No rules setup for " + Constants.DMN_RULE_TYPE_TEMPLATE_RDT);

        DmnDecision decision = decisionMap.get(Constants.DMN_RULE_TYPE_TEMPLATE_RDT);
        VariableMap variables = Variables
            .createVariables()
            .putValue("Categories", request.getCategories())
            .putValue("Reasons", request.getReasons());

        List<Map<String, Object>> resultList = dmnEngine
            .evaluateDecision(decision, variables)
            .getResultList();

        return resultList.stream()
            .map(m -> TemplateRdtResponse
                .builder()
                .templateName((String)m.get("TemplateName"))
                .textBlockNames((String)m.get("TextblockNames"))
                .rdtId((String)m.get("RDRID"))
                .build()
            ).collect(Collectors.toList());
    }
}
