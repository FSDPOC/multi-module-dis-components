package com.db.service;

import java.io.InputStream;

public interface DmnTemplateService {
  void convertDmnTemplate(InputStream inputStream) throws Exception;
}
