package intermediate;

import com.sandwich.koan.Koan;

import java.io.*;
import java.util.logging.Logger;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutFileIO {

    @Koan
    public void fileObjectDoesntCreateFile() {
        File f = new File("i-never.exist");
        assertEquals(f.exists(), __);
    }

    @Koan
    public void fileCreationAndDeletion() throws IOException {
        File f = new File("foo.txt");
        f.createNewFile();
        assertEquals(f.exists(), __);
        f.delete();
        assertEquals(f.exists(), __);
    }

    @Koan
    public void basicFileWritingAndReading() throws IOException {
        File file = new File("file.txt");
        FileWriter fw = new FileWriter(file);
        String data = "First line\nSecond line";
        fw.write(data);
        fw.flush();
        fw.close();

        char[] in = new char[data.length()];
        int size = 0;
        FileReader fr = new FileReader(file);
        size = fr.read(in);
        // No flush necessary!
        fr.close();
        assertEquals(size, __);
        String expected = new String(in);
        assertEquals(expected.length(), __);
        assertEquals(expected, __);
        file.delete();
    }

    @Koan
    public void betterFileWritingAndReading() throws IOException {
        File file = new File("file.txt");
        file.deleteOnExit();
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);
        pw.println("First line");
        pw.println("Second line");
        pw.close();

        FileReader fr = new FileReader(file);
        BufferedReader br = null;
        try {
            br = new BufferedReader(fr);
            assertEquals(br.readLine(), __); // first line
            assertEquals(br.readLine(), __); // second line
            assertEquals(br.readLine(), __); // what now?
        } finally {
            // anytime you open access to a file, you should close it or you may
            // lock it from other processes (ie frustrate people)
            closeStream(br);
        }
    }

    private void closeStream(BufferedReader br) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException x) {
                Logger.getAnonymousLogger().severe("Unable to close reader.");
            }
        }
    }

    @Koan
    public void directChainingForReadingAndWriting() throws IOException {
        File file = new File("file.txt");
        PrintWriter pw = new PrintWriter(file);
        pw.println("1. line");
        pw.println("2. line");
        pw.close();

        StringBuffer sb = new StringBuffer();
        // Add the loop to go through the file line by line and add the line
        // to the StringBuffer
        assertEquals(sb.toString(), "1. line\n2. line");
    }
}

