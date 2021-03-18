package com.db.service;

import com.db.model.request.ReasonForCorrespondenceRequest;
import com.db.model.request.TemplateRdtRequest;
import com.db.model.response.ReasonForCorrespondenceResponse;
import com.db.model.response.TemplateRdtResponse;
import java.util.List;

public interface DmnService {
    void loadDecision();
    List<ReasonForCorrespondenceResponse> decideReason(ReasonForCorrespondenceRequest request) throws Exception;
    List<TemplateRdtResponse> decideTemplateRdt(TemplateRdtRequest request) throws Exception;
}
