package com.flipkart.fdp.migration.distftp;

import com.flipkart.fdp.migration.distcp.config.HostConfig;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sushil.s on 28/08/15.
 */
@Getter
@Setter
public class DistFTPClient {

    private FTPClient ftpClient;

    public DistFTPClient(){
        ftpClient = new FTPClient();
    }

    public boolean connect(HostConfig hostConfig){
        try {
            ftpClient.connect(hostConfig.getHost(),hostConfig.getPort());
            ftpClient.login(hostConfig.getUserName(),hostConfig.getUserPassword());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean transferFile(String path,InputStream in) throws IOException {
        return ftpClient.storeFile(path,in);
    }

    public void closeConnection() throws IOException {
        if(ftpClient.isConnected()){
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }


    public long getFileSize(String destPath) throws IOException {
        String[] pathTree = destPath.split("/");
        String dirPath = null;
        for(int i = 0 ; i < (destPath.length()-1); i++)
            dirPath += pathTree[i]+"/";

        FTPFile[] ftpFiles = ftpClient.listFiles(dirPath);
        for( FTPFile ftpFile : ftpFiles )
            if ( ftpFile.getName().equals(pathTree[pathTree.length-1]) )
                ftpFile.getSize();
        return -1l;
    }

    public String getHostName() {
        return ftpClient
                  .getRemoteAddress()
                    .toString();
    }
}
