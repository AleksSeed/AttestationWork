package ru.skypro.service;

import ru.skypro.exception.MessageException;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public interface FilesService {
    String readSocksFromFile() throws MessageException;
    boolean saveSocksToFile(String json);
    void cleanFile(String fileName) throws FileNotFoundException, MessageException;
    Path createTempFile(String suffix);
    File getFileSock();
    void cleanSocksFile() throws MessageException, FileNotFoundException;
}
