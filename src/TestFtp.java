import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import sun.misc.IOUtils;

import java.io.*;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class TestFtp {
    private final FTPClient ftp = new FTPClient();

    public static void main(String[] args) {
        TestFtp testFtp = new TestFtp();
        // testFtp.open();

//       InputStream is =  testFtp.getFtpFile("/uploads/TESTFILE.txt");
//       if(is != null){
//           System.out.println("File found");
//           try {
//               byte[] buffer = new byte[is.available()];
//               is.read(buffer);
//
//               File targetFile = new File("TEST_FROM_SERVER.txt");
//               OutputStream outStream = new FileOutputStream(targetFile);
//               outStream.write(buffer);
//           } catch (IOException e) {
//               e.printStackTrace();
//           }
//       }else {
//           System.out.println("File not found");
//       }
//        boolean sent = testFtp.send(new File("TEST_FILE.txt"));
//        System.out.println(sent);

        //  testFtp.copyFilesToLocalInbox();

//        File file = new File("error.txt");
//        try {
//            testFtp.moveFile(file.getCanonicalPath(), "error/" + file.getName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        System.out.println(testFtp.send(new File("REMOTE_FILE.xml")));
    }

    private boolean authenticate() {
        try {
            String hostName = "";
            ftp.connect(hostName, 21);
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("Exception in connecting to FTP Server!");
                close();
            }

            System.out.println("Authenticating FTP connection");
            boolean authenticated = ftp.login("testftp", "testftp123");
            if (authenticated) {
                System.out.println("FTP connection authenticated");
            } else {
                System.out.println("User authentication failed");
            }
            return authenticated;
        } catch (IOException e) {
            //System.out.println("Error connecting to FTP Server: ", e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean send(File file) {
        boolean sent = false;
        try {
            if (authenticate()) {
                System.out.println("Attempting to send file to remote server");
                ftp.storeFile("/uploads/" + file.getName(), new FileInputStream(file));
                sent = true;
                System.out.println("File successfully sent to remote server");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending file to remote server.");
        }
        return sent;
    }


    private void copyFilesToLocalInbox() {

        try {
            if (authenticate()) {
                System.out.println("Checking for xml files on remote inbox directory...");
                FTPFile[] files = ftp.listFiles("/uploads/*.xml");

                if (files.length <= 0) {
                    System.out.println("No XML files found in remote directory");
                }

                for (FTPFile file : files) {
                    System.out.println("Copying file: {} to local inbox. " + file.getName());
                    ftp.retrieveFile("/uploads/" + file.getName(), new FileOutputStream(file.getName()));
                    System.out.println("File: {} successfully transferred to local inbox " + file.getName());
                    System.out.println("Deleting remote file " + file.getName());
                    ftp.deleteFile("/uploads/" + file.getName());
                    System.out.println("Remote file Deleted!");
                }
            }
            close();
        } catch (IOException e) {
            // System.out.println("Error connecting to FTP Server: ", e);
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        System.out.println("Trying to close FTP Connection.");
        ftp.disconnect();
        System.out.println("FTP connection closed.");
    }


    public InputStream getFtpFile(String fileName) {
        InputStream inputStream = null;
        System.out.println("Attempting to retrieve single file " + fileName);
        try {
            inputStream = ftp.retrieveFileStream(fileName);
            System.out.println("File {} successfully copied to local directory " + fileName);
        } catch (IOException e) {
            System.out.println("Error retrieving file from FTP folder: {}" + fileName);
        }
        return inputStream;
    }

    private void moveFile(String sourceLoc, String newLoc) {
        Path source = Paths.get(sourceLoc);
        Path target = Paths.get(newLoc);
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error moving file");
            e.printStackTrace();
        }
    }
}
