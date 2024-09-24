package com.example.spring_batch.batch.customizedItemReader;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class CustomItemReader implements ItemReader<String> {
    private List<String> inputList
            = new ArrayList<>(Arrays.asList("item1", "item2", "item3", "item4", "item5", "item6"));
    private int itemNumber = 0;

    @Override
    public String read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
        itemNumber ++;
        try {
            String removedItem = inputList.remove(0);
            System.out.println("read item number " + itemNumber + " and return value: " + removedItem);
            return removedItem;
        }catch (Exception exception){
            System.out.println("all items are read so null value is returned");
            return null;
        }
    }
    @BeforeRead
    public void beforeItemReader(){
        System.out.println("log before read");
    }
    @AfterRead
    public void afterItemReader(){
        System.out.println("log after read");
        System.out.println("-------------------------");
    }
}
