package ru.skypro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.RecordInfo;

@RestController
public class InfoController {
    @GetMapping
    public String shop() {
        return "Онлайн магазин запущен";
    }

    @GetMapping("/info")
    public RecordInfo info() {
        return new RecordInfo("Седов Алексей ", "Онлайн магазин по продаже носков");
    }
}
