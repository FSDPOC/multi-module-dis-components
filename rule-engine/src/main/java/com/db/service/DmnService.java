package com.db.service;

public interface DmnService {
    void initDmnEngine();

    Long decideQuarter(Integer month);
}
