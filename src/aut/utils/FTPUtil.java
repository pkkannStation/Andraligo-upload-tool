package aut.utils;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

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

    private IntegerProperty progress;
    private double fileSize;
    private double transfered;

    public FTPUtil() {
        progress = new SimpleIntegerProperty(0);
        this.client = new FTPClient();
        this.client.setCompressionEnabled(true);
        this.client.setAutoNoopTimeout(30000);
        this.client.setType(FTPClient.TYPE_BINARY);
    }
    
    public IntegerProperty getProgressProperty() {
        return this.progress;
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
//            System.out.println("Loggin out from ftp server");
//            client.logout();

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
        } catch (IOException | FTPIllegalReplyException ex) {
            Logger.getLogger(FTPUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean uploadFile(File from, String to, String filetype) {
        if (this.client.isConnected()) {
            if (this.client.isAuthenticated()) {
                try {
                    if (!this.client.isPassive()) {
                        this.client.setPassive(true);
                    }
                    this.fileSize = from.length();
                    TransferListener tl = new TransferListener();
                    this.client.upload(to, new FileInputStream(from), 1000, 0, tl);
                } catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException ex) {
                }
            }
        }
        return false;
    }

    public class TransferListener implements FTPDataTransferListener {

        @Override
        public void started() {
            System.out.println("FILESIZE: " + FTPUtil.this.fileSize);
        }

        @Override
        public void transferred(int length) {
            FTPUtil.this.transfered += length;
            double pro = (FTPUtil.this.transfered / FTPUtil.this.fileSize) * 100;
            FTPUtil.this.progress.set((int)pro);
        }

        @Override
        public void completed() {

        }

        @Override
        public void aborted() {

        }

        @Override
        public void failed() {

        }

    }

}
