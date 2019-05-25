package ua.com.nc.service.impl;


import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ua.com.nc.exceptions.LogicException;
import ua.com.nc.service.FileTransferService;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
@Log4j2
@Service
@PropertySource("classpath:sql_queries.properties")
public class FiletransferServiceImpl implements FileTransferService {

    @Value("${spring.ftp.host}")
    private String server;
    @Value("${spring.ftp.port}")
    private int port;
    @Value("${spring.ftp.login}")
    private String user;
    @Value("${spring.ftp.password}")
    private String pass;

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                log.info("SERVER: " + aReply);
            }
        }
    }
    @Override
    public void uploadFileToServer(String path, String name, InputStream stream) {
        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.error("Operation failed. Server reply code: " + replyCode);
                throw new LogicException("Error while connecting to server");
            }
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!success) {
                log.error("Could not login to the server");
                throw new LogicException("Error while login to server");
            }
            // Creates a directory if needed
            FTPFile[] remoteFiles = ftpClient.listFiles(path);
            if (remoteFiles.length == 0) {
                success = ftpClient.makeDirectory(path);
                showServerReply(ftpClient);
                if (success) {
                    log.info("Successfully created directory: " + path);
                } else {
                    log.error("Failed to create directory.");
                }
            }
            //Creates file
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);

            success = ftpClient.storeFile(path+"/"+ name, stream);
            showServerReply(ftpClient);
            if (success) {
                log.info("Successfully created file: " + path+"/"+ name);
            } else {
                log.error("Failed to create file.");
            }

            // logs out
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    @Override
    public InputStream downloadFileFromServer(String path) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.error("Operation failed. Server reply code: " + replyCode);
            }
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!success) {
                log.error("Could not login to the server");
            }
            return ftpClient.retrieveFileStream(path);
        }
        catch (IOException ex){
            log.error(ex);

        }
        return null;
    }
}
