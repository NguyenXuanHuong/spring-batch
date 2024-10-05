package com.example.spring_batch.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class SecondProcessor implements ItemProcessor<Integer, String> {
    @Override
    public String process(Integer item) throws Exception {
        System.out.println("start process in the SecondProcessor with input value: " + item);
        return "output of Second Processor";
    }
}
