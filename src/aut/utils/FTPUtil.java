package aut.utils;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patrick
 */
public class FTPUtil {

    private String host = "194.255.32.68";
    private int port = 21;
    private String user = "patrick";
    private String pass = "Allo14vino";

    private FTPClient client;
    
    public FTPUtil() {
        this.client = new FTPClient();
    }
    
    public boolean connect() {
        System.out.println("Connecting to ftp server");
        try {
            String[] message = client.connect(host);
            System.out.println("FTP server says: " + message);
            
            client.login(user, pass);
            return true;
        } catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException ex) {
            Logger.getLogger(FTPUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public boolean disconnect() {
        
        try {
            System.out.println("Loggin out from ftp server");
            client.logout();
            
            System.out.println("Disconnecting from ftp server");
            client.disconnect(false);
            return true;
        } catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException ex) {
        }
        
        return false;
    }
    
    public boolean abort() {
        System.out.println("Aborting all current ftp transfers, exceptions might occur");
        try {
            this.client.abortCurrentDataTransfer(true);
        } catch (IOException ex) {
            Logger.getLogger(FTPUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FTPIllegalReplyException ex) {
            Logger.getLogger(FTPUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean uploadFile(File from, String to, String filetype) {
        if(this.client.isConnected()) {
            if(this.client.isAuthenticated()) {
                try {
                    if(!this.client.isPassive()) {
                        this.client.setPassive(true);
                    }
                    this.client.upload(to, new FileInputStream(from), 1000, 0, null);
                } catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException ex) {
                }
            }
        }
        return false;
    }
    
//    public boolean abortUpload() {
//        try {
//            this.client.disconnect();
//        } catch (IOException ex) {
//            Logger.getLogger(FTPUtil.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
//
//    public boolean uploadFile(File from, String to, CopyStreamAdapter streamListener, String filetype) {
//        client = new FTPClient();
//        client.setCopyStreamListener(streamListener);
//        client.setBufferSize(99999);
//
//        try {
//            client.connect(host, port);
//            System.out.println("Is connected");
//            client.login(user, pass);
//            System.out.println("Are logged in");
//            client.enterLocalPassiveMode();
//            System.out.println("Passive mode entered");
//            client.setFileType(FTP.BINARY_FILE_TYPE);
//            System.out.println("Binary file type chosen");
//
//            InputStream inputStream = new FileInputStream(from);
//            System.out.println("Uploading file...");
//            boolean done = client.storeFile(to + "." + filetype, inputStream);
//            if (done) {
//                System.out.println("File uploaded");
//            } else {
//                System.out.println("File not uploaded!");
//            }
//            inputStream.close();
//
//            return true;
//        } catch (IOException ex) {
//            return false;
//        } finally {
//            if (client.isConnected()) {
//                System.out.println("Logging out and disconnecting");
//                try {
//                    client.logout();
//                    client.disconnect();
//                } catch (IOException ex) {
//                    Logger.getLogger(FTPUtil.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//    }

}
