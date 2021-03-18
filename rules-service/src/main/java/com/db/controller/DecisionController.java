package com.db.controller;

import com.db.model.request.ReasonForCorrespondenceRequest;
import com.db.model.request.TemplateRdtRequest;
import com.db.model.response.ReasonForCorrespondenceResponse;
import com.db.model.response.TemplateRdtResponse;
import com.db.service.DmnService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/correspondence")
@Slf4j
public class DecisionController {

    private DmnService dmnService;

    public DecisionController(DmnService dmnService) {
        this.dmnService = dmnService;
    }

    @PostMapping(value = "/reason")
    public List<ReasonForCorrespondenceResponse> queryReasonForCorrespondence(
            @RequestBody ReasonForCorrespondenceRequest request) throws Exception {
        return dmnService.decideReason(request);
    }

    @PostMapping(value = "/template")
    public List<TemplateRdtResponse> queryTemplateRdt(
        @RequestBody TemplateRdtRequest request) throws Exception {
        return dmnService.decideTemplateRdt(request);
    }
}
