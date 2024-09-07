package com.example.spring_batch.batch.faulttolerance;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
@Slf4j
public class CustomSkipPolicy implements SkipPolicy {
    private static final Logger log = LogManager.getLogger(CustomSkipPolicy.class);

    private final Integer skipLimit = 10;

    @Override
    public boolean shouldSkip(Throwable exception, long skipCount) throws SkipLimitExceededException {
        if (exception instanceof FileNotFoundException){
            return Boolean.FALSE;
        }else if ((exception instanceof FlatFileParseException) && (skipCount <= skipLimit) ){
            FlatFileParseException fileParseException = (FlatFileParseException) exception;
            String input = fileParseException.getInput();
            int lineNumber = fileParseException.getLineNumber();
            log.error("The line with error is: {}",input);
            log.error("The line number with error is: {}",lineNumber);
            //write into a file
            //send into kafka topic or any Message broker
            return Boolean.TRUE;
        }else if ((exception instanceof IllegalArgumentException) && (skipCount <= skipLimit) ){
            log.warn(exception.getMessage());
            return Boolean.TRUE;
        }
        return false;
    }
}
