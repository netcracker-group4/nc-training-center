package ua.com.nc.service;

import org.apache.commons.net.ftp.FTPClient;

import java.io.InputStream;

public interface FileTransferService {
    int uploadFileToServer(String path, String name, InputStream stream);
    InputStream downloadFileFromServer(String path);
}
