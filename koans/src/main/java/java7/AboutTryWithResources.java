package java7;

import com.sandwich.koan.Koan;

import java.io.*;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutTryWithResources {

    class AutoClosableResource implements AutoCloseable{
        public void foo() throws WorkException{
            throw new WorkException("Exception thrown while working");
        }
        public void close() throws CloseException{
            throw new CloseException("Exception thrown while closing");
        }
    }

    class WorkException extends Exception {
        public WorkException(String message) {
            super(message);
        }
    }

    class CloseException extends Exception {
        public CloseException(String message) {
            super(message);
        }
    }

    @Koan
    public void lookMaNoClose() {
        String str = "first line"
                + System.lineSeparator()
                + "second line";
        InputStream is = new ByteArrayInputStream(str.getBytes());
        String line;
        /* BufferedReader implementing @see java.lang.AutoCloseable interface */
        try (BufferedReader br =
                     new BufferedReader(
                             new InputStreamReader(is))) {
            line = br.readLine();
            //br guaranteed to be closed
        } catch (IOException e) {
            line = "error";
        }
        assertEquals(line, __);
    }

    @Koan
    public void lookMaNoCloseWithException() throws IOException {
        String line = "no need to close readers";
        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader("I do not exist!"))) {
            line = br.readLine();
        }catch(FileNotFoundException e){
            line = "no more leaking!";
        }
        assertEquals(line, __);
    }

    @Koan
    public void lookMaNoCloseWithMultipleResources() throws IOException {
        String str = "first line"
                + System.lineSeparator()
                + "second line";
        InputStream is = new ByteArrayInputStream(str.getBytes());
        String line;
        //multiple resources in the same try declaration
        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader("I do not exist!"));
             BufferedReader brFromString =
                     new BufferedReader(
                             new InputStreamReader(is))
        ) {
            line = br.readLine();
            line += brFromString.readLine();
        }catch (IOException e) {
            line = "error";
        }
        assertEquals(line, __);
    }

    @Koan
    public void supressException(){
        String message = "";
        try {
            bar();
        } catch (WorkException e) {
            message += e.getMessage() + " " + e.getSuppressed()[0].getMessage();
        } catch (CloseException e) {
            message += e.getMessage();
        }
        assertEquals(message, __);
    }


    public void bar() throws CloseException, WorkException {
        try (AutoClosableResource autoClosableResource =
                     new AutoClosableResource()) {
            autoClosableResource.foo();
        }
    }
}