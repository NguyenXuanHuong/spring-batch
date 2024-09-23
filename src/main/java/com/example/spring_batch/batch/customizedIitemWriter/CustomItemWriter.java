package com.example.spring_batch.batch.customizedIitemWriter;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomItemWriter implements ItemWriter<Integer> {
    @Override
    public void write(Chunk<? extends Integer> chunk) throws Exception {
        List<? extends Integer> writeItemList = chunk.getItems();
        StringBuilder outputString = new StringBuilder("list output item to be written: ");
        for (Integer integer : writeItemList) {
            outputString.append(" item#").append(integer);
        }
        System.out.println(outputString);
        System.out.println("-----------------------");
    }
}
