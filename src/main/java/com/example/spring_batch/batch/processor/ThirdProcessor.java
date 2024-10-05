package com.example.spring_batch.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ThirdProcessor implements ItemProcessor<String, Integer> {
    @Override
    public Integer process(String item) {
        System.out.println("start Process of the ThirdProcessor with input value: " + item);
        return 3;
    }
}
