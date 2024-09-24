package com.example.spring_batch.batch.customizedItemProcessor;

import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomizedItemProcessor implements ItemProcessor<String, Integer> {
    private int processItemNumber = 0;

    @Override
    public Integer process(String item) {
        processItemNumber++;
        System.out.println("processing item#" + processItemNumber);
        char lastChar = item.charAt(item.length() - 1);
        return Integer.parseInt(String.valueOf(lastChar));
    }

    @BeforeProcess
    public void beforeProcess(){
        System.out.println("log before process");
    }

    @AfterProcess
    public void afterProcess(){
        System.out.println("log after process item");
        System.out.println("-------------------------");
    }

}
