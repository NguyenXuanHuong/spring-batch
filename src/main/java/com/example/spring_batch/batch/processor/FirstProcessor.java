package com.example.spring_batch.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstProcessor implements ItemProcessor<String, Integer> {
    @Override
    public Integer process(String item) throws Exception {
        System.out.println("start process in the FirstProcessor with input value of: " + item);
        return 1;
    }
}
