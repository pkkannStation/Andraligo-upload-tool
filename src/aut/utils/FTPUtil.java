package aut.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamAdapter;

/**
 *
 * @author Patrick
 */
public class FTPUtil {

    private static String host = "194.255.32.68";
    private static int port = 21;
    private static String user = "patrick";
    private static String pass = "Allo14vino";

    private static FTPClient client;

    public static boolean uploadFile(File from, String to, CopyStreamAdapter streamListener, String filetype) {
        client = new FTPClient();
        client.setCopyStreamListener(streamListener);
        client.setBufferSize(99999);

        try {
            client.connect(host, port);
            System.out.println("Is connected");
            client.login(user, pass);
            System.out.println("Are logged in");
            client.enterLocalPassiveMode();
            System.out.println("Passive mode entered");
            client.setFileType(FTP.BINARY_FILE_TYPE);
            System.out.println("Binary file type chosen");

            InputStream inputStream = new FileInputStream(from);
            System.out.println("Uploading file...");
            boolean done = client.storeFile(to + "." + filetype, inputStream);
            if (done) {
                System.out.println("File uploaded");
            } else {
                System.out.println("File not uploaded!");
            }
            inputStream.close();

            return true;
        } catch (IOException ex) {
            return false;
        } finally {
            if (client.isConnected()) {
                System.out.println("Logging out and disconnecting");
                try {
                    client.logout();
                    client.disconnect();
                } catch (IOException ex) {
                    Logger.getLogger(FTPUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
