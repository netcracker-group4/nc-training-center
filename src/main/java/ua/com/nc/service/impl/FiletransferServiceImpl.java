package ua.com.nc.service.impl;


import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;
import ua.com.nc.service.FileTransferService;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
@Log4j2
@Service
public class FiletransferServiceImpl implements FileTransferService {

    private String server = "45.66.10.81";
    private int port = 21;
    private String user = "Administrator";
    private String pass = "2Y36N72Db3K7";

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                log.info("SERVER: " + aReply);
            }
        }
    }
    @Override
    public int uploadFileToServer(String path, String name, InputStream stream) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.info("Operation failed. Server reply code: " + replyCode);
                return 1;
            }
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!success) {
                log.info("Could not login to the server");
                return 1;
            }
            // Creates a directory if needed
            FTPFile[] remoteFiles = ftpClient.listFiles(path);
            if (remoteFiles.length == 0) {
                success = ftpClient.makeDirectory(path);
                showServerReply(ftpClient);
                if (success) {
                    log.info("Successfully created directory: " + path);
                } else {
                    log.info("Failed to create directory. See server reply.");
                }
            }
            //Creates file
            success = ftpClient.storeFile(path+"/"+ name, stream);
            showServerReply(ftpClient);
            if (success) {
                log.info("Successfully created file: " + path);
            } else {
                log.info("Failed to create file. See server reply.");
            }

            // logs out
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            log.info("Oops! Something wrong happened");
            log.info(ex);
        }
        return 0;
    }

    @Override
    public InputStream downloadFileFromServer(String path) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.info("Operation failed. Server reply code: " + replyCode);
            }
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!success) {
                log.info("Could not login to the server");
            }
            return ftpClient.retrieveFileStream(path);
        }
        catch (IOException ex){
            log.info("Oops! Something wrong happened");
            log.info(ex);

        }
        return null;
    }
}
