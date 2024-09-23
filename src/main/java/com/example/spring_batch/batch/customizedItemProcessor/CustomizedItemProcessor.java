package com.example.spring_batch.batch.customizedItemProcessor;

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
}
