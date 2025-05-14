package com.rafanegrette.books.services.langchain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SystemPrompts {

    private static final Map<String, DayPrompt> dayPrompts;
    public static final int HOUR_AFTERNOON_LIMIT = 16;

    static class DayPrompt {
        String morning;
        String noon;
        String afternoon;

        public String getMorning() {
            return morning;
        }

        public void setMorning(String morning) {
            this.morning = morning;
        }

        public String getNoon() {
            return noon;
        }

        public void setNoon(String noon) {
            this.noon = noon;
        }

        public String getAfternoon() {
            return afternoon;
        }

        public void setAfternoon(String afternoon) {
            this.afternoon = afternoon;
        }
    }

    static {
        Map<String, DayPrompt> tempPrompts;
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            var resource = new ClassPathResource("month1.json");
            var fileStream = resource.getInputStream();
            TypeReference<Map<String, DayPrompt>> typeRef = new TypeReference<>(){};
            tempPrompts = (Map<String, DayPrompt>) objectMapper.readValue(fileStream, typeRef);
        } catch (IOException e) {
            log.error("Loading resource files", e);
            tempPrompts = new HashMap<>();
        }

        dayPrompts = tempPrompts;
    }

    public String getDayPrompts(LocalDateTime currentDate) {
        var dayName = currentDate.getDayOfWeek().name().toLowerCase();

        var dayPrompt = dayPrompts.get(dayName);

        if (currentDate.toLocalTime().isBefore(LocalTime.NOON)) {
            return dayPrompt.morning;
        } else if(currentDate.toLocalTime().isAfter(LocalTime.NOON) && currentDate.toLocalTime().isBefore(LocalTime.of(HOUR_AFTERNOON_LIMIT, 0)) ){
            return dayPrompt.noon;
        }

        return dayPrompt.afternoon;
    }



}
