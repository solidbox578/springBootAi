package com.vibhu.openai.Tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TimeTools {

    private final Logger logger  = LoggerFactory.getLogger(TimeTools.class);

    @Tool(name = "getCurrentLocalTime", description = "Get the current local time in the user's timezone")
    public String getCurrentLocalTime(){
        logger.debug("Returning the current time in the user's timezone");
        return LocalDateTime.now().toString();
    }

    @Tool(name = "getCurrentTime"
        , description = "Get the current time in a specific timezone")
    public String getCurrentLocalTime(@ToolParam(description = "Value representing the timezone") String timezone){
        logger.debug("Returning the current time in the timezone: {}", timezone);
        return LocalDateTime.now(ZoneId.of(timezone)).toString();
    }
}
