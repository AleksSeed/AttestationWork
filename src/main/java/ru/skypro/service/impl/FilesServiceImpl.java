package ru.skypro.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.skypro.exception.MessageException;
import ru.skypro.service.FilesService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FilesServiceImpl implements FilesService {
    @Value("${path.to.data.file}")
    private String dataFilePath;
    @Value("socks.json")
    private String socksFileName;

    @PostConstruct
    private void init() {
        try {
            if (!Files.exists(Path.of(dataFilePath, socksFileName))) {
                Files.createFile(Path.of(dataFilePath, socksFileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Файл не создан");
        }
    }

    @Override
    public Path createTempFile(String suffix) {
        try {
            return Files.createTempFile(Path.of(dataFilePath), "tempfile", suffix);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания временного файла");
        }
    }

    @Override
    public File getFileSock() {
        return new File(dataFilePath + "/" + socksFileName);
    }

    @Override
    public void cleanSocksFile() throws MessageException, NotFoundException, FileNotFoundException {
        cleanFile(socksFileName);
    }

    private boolean saveToFile(String fileName, String json) {
        try {
            cleanFile(fileName);
            Files.writeString(Path.of(dataFilePath, fileName), json);
            return true;
        } catch (IOException | MessageException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String readFromFile(String fileName) throws MessageException {
        try {
            return Files.readString(Path.of(dataFilePath, fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new MessageException("Ошибка чтения файла");
        }
    }

    @Override
    public boolean saveSocksToFile(String json) {
        return false;
    }

    @Override
    public String readSocksFromFile() throws MessageException {
        return readFromFile(socksFileName);
    }

    @Override
    public void cleanFile(String fileName) throws FileNotFoundException, MessageException {
        try {
            Files.deleteIfExists(Path.of(dataFilePath, fileName));
            Files.createFile(Path.of(dataFilePath, fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new MessageException("Ошибка очистки файла");
        }
    }

}