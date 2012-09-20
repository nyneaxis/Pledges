/**
 *
 * @author Brandon Wilson <brandon@nyneaxis.com>
 * @date Aug 26, 2012
 * @email exdox77@gmail.com
 * 
 * Changes: 
 * 9/2/2012: JAVADOC comments created.
 *
 * Beginning of Java Class FileExecution
 */
package pledges;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Writer;
import static java.lang.System.*;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import javax.swing.JOptionPane;

public class FileExecution {

    InputStream inputStream;
    BufferedReader bufferedReader = null;
    File file = null;
    //default file location
    private String DEFAULT_FILE_LOCATION = getProperty("user.home") + getProperty("file.separator") + "charity.txt";

    /**
     * Delete's file by overwriting with random bytes to the file. Securely
     * removing the specified file from the computer.
     *
     * @param file Location of file
     * @return true if the file was successfully and securely removed, false
     * otherwise.
     * @throws IOException if the file is not found or cannot be overwritten.
     */
    public Boolean secureDelete(File file) throws IOException {
        if (file.exists()) { //checks if file exists
            long length = file.length(); //gets length of file
            SecureRandom random = new SecureRandom(); //creates random number
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws"); //accesses the file randomly
            randomAccessFile.seek(0); //sets file pointer offset to 0
            randomAccessFile.getFilePointer();
            byte[] data = new byte[64]; //creates new byte array
            int position = 0;
            while (position < length) { //loops through file length and writes random data to file
                random.nextBytes(data);
                randomAccessFile.write(data); //writes random data to file
                position += data.length; //increment position.
            }
            randomAccessFile.close(); //closes randomAccessFile 
            file.delete(); //deletes file
            return true;
        } else { //if file does not exists return false
            return false;
        }
    }

    /**
     * Closes file that is currently open in the bufferedReader and inputStream.
     *
     * @return true - operation is successful. false - otherwise.
     */
    public Boolean closeFile() {
        try {
            bufferedReader.close(); //closes buffered reader
            inputStream.close(); //closes input stream
            return true;
        } catch (IOException ex) {
            out.printf("Error closing file:\r\n", ex);
            return false;
        }
    }

    /**
     * Deletes the file in the DEFAULT_FILE_LOCATION.
     *
     * @return true - if the file was deleted successfully. false - otherwise.
     */
    public Boolean deleteFile() {
        file = new File(DEFAULT_FILE_LOCATION); //get new files from DEFAULT_FILE_LOCATION
        try {
            if (file.exists()) { //checks if file exists
                if (closeFile()) { //calls closeFile()
                    if (secureDelete(file)) { //once file is closed we securely delete it
                        out.printf("Deleted file at %s", DEFAULT_FILE_LOCATION);
                        return true; //returns true if it is securely deleted
                    } else {
                        out.printf("Error while deleting file: %s\r\n", DEFAULT_FILE_LOCATION);
                        return false;
                    }
                } else {
                    out.printf("Error while closing file: %s\r\n", DEFAULT_FILE_LOCATION);
                    return false;
                }
            } else {
                out.printf("File: %s doesn't exists\r\n", DEFAULT_FILE_LOCATION);
                return false;
            }
        } catch (Exception e) {
            out.printf("Could not delete file %s", e);
            return false;
        }

    }

    /**
     * Reads file from DEFAULT_FILE_LOCATION.
     *
     * @return BufferedReader - with file contents. NULL otherwise.
     */
    public BufferedReader readFile() {
        //readFile method return BufferedReader
        try {
            //tries to get file from location
            file = new File(DEFAULT_FILE_LOCATION); //opens new file from DEFAULT_FILE_LOCATION
            if (file.isFile() && file.canRead()) { //makes sure file is there and can be read
                inputStream = new FileInputStream(file); //places new file in new inputstream
                bufferedReader = new BufferedReader(
                        new InputStreamReader(inputStream, Charset.forName("UTF-8"))); //places input stream into buffer
            }
        } catch (IOException e) {
            out.printf("Error reading file: %s\r\n%s", DEFAULT_FILE_LOCATION, e);
        }
        //return file contents
        return bufferedReader;
    }

    /**
     * Writes line to file from the DEFAULT_FILE_LOCATION.
     *
     * @param name Specifies name of charity donor
     * @param charity Specifies name of charity
     * @param amount Specifies amount of pledge
     * @return true - if line was written to file. false - otherwise.
     */
    public boolean writeFile(String name, String charity, String amount) {
        //writes file if conidtions are true
        try {
            //creates file in directory
            Writer fileWriter = new BufferedWriter(new FileWriter(DEFAULT_FILE_LOCATION, true));
            fileWriter.append(String.format("%s|%s|%s\r\n", name, charity, amount)); //formats the line with delimiters
            fileWriter.close(); //closes file
            out.printf("Your file was saved in %s\r\n", DEFAULT_FILE_LOCATION);
            return true;

        } catch (IOException ex) {
            //catches in IO exceptions and advises user
            JOptionPane.showMessageDialog(null, "An error occurred while trying to create file.");
            out.println(String.format("Error: %s", ex.getMessage()));
            return false;
        }
    }
}

/**
 *
 *End of Java Class FileExecution
 *
 */