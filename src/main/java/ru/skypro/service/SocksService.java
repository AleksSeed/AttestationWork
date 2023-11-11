package ru.skypro.service;

import org.webjars.NotFoundException;
import ru.skypro.characteristic.Socks;

import java.io.File;


public interface SocksService {
    void addSocks(Socks socks);
    Socks editSocks(long id , Socks newSock);
    Socks getSocks(long id);
    boolean deleteSock(long id);

    File createSocksTxtFile() throws NotFoundException;

}