package com.db.service.impl;

import com.db.config.Constants;
import com.db.model.dmn.DecisionTableModel;
import com.db.model.dmn.DecisionTableModel.Rule;
import com.db.persistence.entity.DmnTemplate;
import com.db.persistence.repository.DmnTemplateRepository;
import com.db.service.DmnService;
import com.db.service.DmnTemplateService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class DmnTemplateServiceImpl implements DmnTemplateService {

    private DmnTemplateRepository dmnTemplateRepository;
    private DmnService dmnService;
    private Configuration configuration;
    private Template template;

    public DmnTemplateServiceImpl(DmnTemplateRepository dmnTemplateRepository, DmnService dmnService) {
        this.dmnService = dmnService;
        this.dmnTemplateRepository = dmnTemplateRepository;
    }

    @PostConstruct
    public void initFreeMarker() throws IOException {
        this.configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");
        template = configuration.getTemplate("dmnTemplate.ftl");
    }

    @Override
    public void convertDmnTemplate(InputStream inputStream) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Iterator<Sheet> it = workbook.sheetIterator();
            while (it.hasNext()) {
                Sheet sheet = it.next();
                DecisionTableModel model = convertDecisionTable(sheet);
                String templateXml = parseDmnTemplate(model);
                log.info(templateXml);

                // Save to database
                DmnTemplate dmnTemplate;
                Optional<DmnTemplate> dmnTemplateOptional =
                    dmnTemplateRepository.findByType(sheet.getSheetName());
                if (!dmnTemplateOptional.isPresent()) {
                    dmnTemplate =
                        DmnTemplate.builder()
                            .type(sheet.getSheetName())
                            .status(Constants.DMN_RULE_STATUS_ACTIVE)
                            .build();
                }
                else
                    dmnTemplate = dmnTemplateOptional.get();
                dmnTemplate.setTemplate(templateXml);
                dmnTemplateRepository.save(dmnTemplate);
            }
            dmnService.loadDecision();
        }
    }

    private DecisionTableModel convertDecisionTable(Sheet sheet) {
        // Get header type - INPUTS/OUTPUTS
        int inputHeaderIndex = 0, outputHeaderIndex = 1;
        int inputHeaderNumCells = 1, outputHeaderNumCells = 1;
        // Get merged header size
        if (sheet.getNumMergedRegions() > 0) {
            inputHeaderIndex = sheet.getMergedRegion(0).getFirstColumn();
            inputHeaderNumCells = sheet.getMergedRegion(0).getNumberOfCells();
        }
        if (sheet.getNumMergedRegions() > 1) {
            outputHeaderIndex = sheet.getMergedRegion(1).getFirstColumn();
            outputHeaderNumCells = sheet.getMergedRegion(1).getNumberOfCells();
        }
        log.info("Input column index - start {} count {}", inputHeaderIndex, inputHeaderNumCells);
        log.info("Output column index - start {} count {}", outputHeaderIndex, outputHeaderNumCells);

        Iterator<Row> it = sheet.rowIterator();
        Row headerTypeRow = it.next();
        Assert.isTrue("INPUTS".equalsIgnoreCase(headerTypeRow.getCell(inputHeaderIndex).getStringCellValue()),
            "No INPUTS header section can be found");
        Assert.isTrue("OUTPUTS".equalsIgnoreCase(headerTypeRow.getCell(outputHeaderIndex).getStringCellValue()),
            "No OUTPUTS header section can be found");

        // Get headers
        DecisionTableModel model = new DecisionTableModel();
        model.setDecisionId(sheet.getSheetName());
        Row headerRow = it.next();
        log.info("Processing header columns");
        model.setInputList(getList(headerRow, 0, inputHeaderNumCells));
        model.setOutputList(getList(headerRow, outputHeaderIndex, outputHeaderNumCells));

        // Get rules
        List<Rule> ruleList = new ArrayList<>();
        log.info("processing ruleset rows");
        while (it.hasNext()) {
            Row row = it.next();
            Rule rule = new Rule();
            rule.setInputList(getList(row, 0, inputHeaderNumCells));
            rule.setOutputList(getList(row, outputHeaderIndex, outputHeaderNumCells));
            ruleList.add(rule);
        }
        model.setRuleList(ruleList);

        return model;
    }

    private List<Object> getList(Row row, int start, int count) {
        List<Object> list = new ArrayList<>();
        int end = start + count;
        try {
            for (int i = start; i < end; i++) {
                Cell cell = row.getCell(i);
                switch (cell.getCellType()) {
                    case STRING:
                        list.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        list.add(String.format("%.0f", cell.getNumericCellValue()));
                        break;
                    case BOOLEAN:
                        list.add(cell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        list.add(cell.getCellFormula());
                        break;
                    case BLANK:
                        list.add("");
                        break;
                    default:
                        log.warn("Unknown cell type at row {} column {}", row.getRowNum(), i);
                }
            }
        }
        catch (Exception ex) {
            log.error("Error: {}", ex.getMessage());
        }
        return list;
    }

    private String parseDmnTemplate(DecisionTableModel model) throws Exception {
        try (StringWriter writer = new StringWriter()) {
            Map<String, Object> map = new HashMap<>();
            map.put("model", model);
            template.process(map, writer);
            writer.flush();
            return writer.getBuffer().toString();
        }
        catch (Exception ex) {
            log.error("Failed to convert template: {}", ex.getMessage());
            throw ex;
        }
    }

    /*private void convertExcelSheet(Sheet sheet) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet newSheet = workbook.createSheet(sheet.getSheetName());
        ExcelOperationUtil.copySheet(sheet, newSheet);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            convert(sheet.getSheetName(), in);
        }
    }

    private void convert(String name, InputStream inputStream) throws IOException {
        XlsxConverter converter = new XlsxConverter();
        DmnModelInstance dmnModelInstance = converter.convert(inputStream);
        DmnTemplate dmnTemplate;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Dmn.writeModelToStream(outputStream, dmnModelInstance);
            String template = new String(outputStream.toByteArray());
            log.info("{} template: {}", name, template);

            Optional<DmnTemplate> dmnTemplateOptional =
                dmnTemplateRepository.findByTypeAndStatus(
                    name, Constants.DMN_RULE_STATUS_ACTIVE);
            if (!dmnTemplateOptional.isPresent()) {
                dmnTemplate =
                    DmnTemplate.builder()
                        .type(name)
                        .status(Constants.DMN_RULE_STATUS_ACTIVE)
                        .build();
            } else dmnTemplate = dmnTemplateOptional.get();
            dmnTemplate.setTemplate(template);
            dmnTemplateRepository.save(dmnTemplate);
        }
    }*/
}
