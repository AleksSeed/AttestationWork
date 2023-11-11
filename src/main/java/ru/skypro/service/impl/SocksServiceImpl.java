package ru.skypro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.skypro.characteristic.Socks;
import ru.skypro.exception.MessageException;
import ru.skypro.service.FilesService;
import ru.skypro.service.SocksService;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.TreeMap;

@Service
public class SocksServiceImpl implements SocksService {
    private final FilesService fileService;
    private static Map<Long, Socks> socks = new TreeMap<>();
    private static long lastId = 0;

    public SocksServiceImpl(FilesService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void addSocks(Socks socks) {
        socks.put(lastId++, socks);
    }

    @Override
    public Socks getSocks(long id) {
        if (socks.containsKey(id)) {
            return socks.get(id);
        }
        return null;
    }

    @Override
    public Socks editSocks(long id, Socks newSocks) {
        if (socks.containsKey(id)) {
            socks.put(id, newSocks);
            return newSocks;
        }
        return null;
    }

    @Override
    public boolean deleteSock(long id) {
        if (socks.containsKey(id)) {
            socks.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public File createSocksTxtFile() throws NotFoundException {
        Path path = fileService.createTempFile("Socks");
        try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            for (Socks socks : socks.values()) {
                writer.append(socks.toString());
                writer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка: txt файл не создан.");
        }
        return path.toFile();
    }

    private void readFromFile() throws MessageException {
        try {
            String json = fileService.readSocksFromFile();
            if (!json.isBlank()) {
                socks = new ObjectMapper().readValue(json, new TypeReference<>() {
                });
                lastId = socks.size();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка: невозможно прочитать файл.");
        }
    }


    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socks);
            fileService.saveSocksToFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка: невозможно сохранить файл.");
        }
    }

}