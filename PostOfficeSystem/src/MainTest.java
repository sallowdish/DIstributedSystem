import java.io.BufferedInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;

import static org.junit.Assert.*;

/**
 * Created by Ray on 15-06-21.
 */
public class MainTest {
    String root = "/Users/Ray/Desktop/early_testcases/";

    @org.junit.After
    public void tearDown() throws Exception {
        Files.deleteIfExists((new File("log_master.txt")).toPath());
        Files.deleteIfExists((new File("log_front.txt")).toPath());
        PostOffice.cleanUp();
    }

    @org.junit.Test
    public void testMain0() throws Exception {
        String path = root + "0/";
        String args[] = {path + "offices.txt", path + "wanted.txt", path + "commands.txt"};
        Main.main(args);
        String masterLog = FileUtils.readFileToString(new File("log_master.txt"));
        String sampleMastlog = FileUtils.readFileToString(new File(path + "log_master.txt"));
        String frontLog = FileUtils.readFileToString(new File("log_front.txt"));
        String sampleFrontLog = FileUtils.readFileToString(new File(path + "log_front.txt"));
        assertEquals("master logs do not match", masterLog, sampleMastlog);
        assertEquals("front logs do not match", frontLog, sampleFrontLog);
    }

    @org.junit.Test
    public void testMain1() throws Exception {
        String path = root + "1/";
        String args[] = {path + "offices.txt", path + "wanted.txt", path + "commands.txt"};
        Main.main(args);
        String masterLog = FileUtils.readFileToString(new File("log_master.txt"));
        String sampleMastlog = FileUtils.readFileToString(new File(path + "log_master.txt"));
        String frontLog = FileUtils.readFileToString(new File("log_front.txt"));
        String sampleFrontLog = FileUtils.readFileToString(new File(path + "log_front.txt"));
        String berlinLog = FileUtils.readFileToString(new File("log_Berlin.txt"));
        String sampleBerlinLog = FileUtils.readFileToString(new File(path + "log_Berlin.txt"));
        assertEquals( "master logs do not match", masterLog, sampleMastlog);
        assertEquals("front logs do not match", frontLog, sampleFrontLog);
        assertEquals("Berlin logs don't match", berlinLog, sampleBerlinLog);
    }

    @org.junit.Test
    public void testMain2() throws Exception {
        String path = root + "2/";
        String args[] = {path + "offices.txt", path + "wanted.txt", path + "commands.txt"};
        Main.main(args);
        String masterLog = FileUtils.readFileToString(new File("log_master.txt"));
        String sampleMastlog = FileUtils.readFileToString(new File(path + "log_master.txt"));
        String frontLog = FileUtils.readFileToString(new File("log_front.txt"));
        String sampleFrontLog = FileUtils.readFileToString(new File(path + "log_front.txt"));
        String berlinLog = FileUtils.readFileToString(new File("log_Berlin.txt"));
        String sampleBerlinLog = FileUtils.readFileToString(new File(path + "log_Berlin.txt"));
        assertEquals( "master logs do not match", masterLog, sampleMastlog);
        assertEquals("front logs do not match", frontLog, sampleFrontLog);
        assertEquals("Berlin logs don't match", berlinLog, sampleBerlinLog);
    }

    @org.junit.Test
    public void testMain3() throws Exception {
        String path = root + "3/";
        String args[] = {path + "offices.txt", path + "wanted.txt", path + "commands.txt"};
        Main.main(args);
        String masterLog = FileUtils.readFileToString(new File("log_master.txt"));
        String sampleMastlog = FileUtils.readFileToString(new File(path + "log_master.txt"));
        String frontLog = FileUtils.readFileToString(new File("log_front.txt"));
        String sampleFrontLog = FileUtils.readFileToString(new File(path + "log_front.txt"));
        String berlinLog = FileUtils.readFileToString(new File("log_Berlin.txt"));
        String sampleBerlinLog = FileUtils.readFileToString(new File(path + "log_Berlin.txt"));
        assertEquals( "master logs do not match", masterLog, sampleMastlog);
        assertEquals("front logs do not match", frontLog, sampleFrontLog);
        assertEquals("Berlin logs don't match", berlinLog, sampleBerlinLog);
    }

    @org.junit.Test
    public void testMain4() throws Exception {
        String path = root + "4/";
        String args[] = {path + "offices.txt", path + "wanted.txt", path + "commands.txt"};
        Main.main(args);
        String masterLog = FileUtils.readFileToString(new File("log_master.txt"));
        String sampleMastlog = FileUtils.readFileToString(new File(path + "log_master.txt"));
        String frontLog = FileUtils.readFileToString(new File("log_front.txt"));
        String sampleFrontLog = FileUtils.readFileToString(new File(path + "log_front.txt"));
        String berlinLog = FileUtils.readFileToString(new File("log_Berlin.txt"));
        String sampleBerlinLog = FileUtils.readFileToString(new File(path + "log_Berlin.txt"));
        assertEquals( "master logs do not match", masterLog, sampleMastlog);
        assertEquals("front logs do not match", frontLog, sampleFrontLog);
        assertEquals("Berlin logs don't match", berlinLog, sampleBerlinLog);
    }

    @org.junit.Test
    public void testMain5() throws Exception {
        String path = root + "5/";
        String args[] = {path + "offices.txt", path + "wanted.txt", path + "commands.txt"};
        Main.main(args);
        String masterLog = FileUtils.readFileToString(new File("log_master.txt"));
        String sampleMastlog = FileUtils.readFileToString(new File(path + "log_master.txt"));
        String frontLog = FileUtils.readFileToString(new File("log_front.txt"));
        String sampleFrontLog = FileUtils.readFileToString(new File(path + "log_front.txt"));
        String berlinLog = FileUtils.readFileToString(new File("log_Berlin.txt"));
        String sampleBerlinLog = FileUtils.readFileToString(new File(path + "log_Berlin.txt"));
        String nilrebLog = FileUtils.readFileToString(new File("log_Nilreb.txt"));
        String sampleNilrebLog = FileUtils.readFileToString(new File(path + "log_Nilreb.txt"));
        assertEquals( "master logs do not match", masterLog, sampleMastlog);
        assertEquals("front logs do not match", frontLog, sampleFrontLog);
        assertEquals("Berlin logs don't match", berlinLog, sampleBerlinLog);
        assertEquals("Nilreb logs don't match", nilrebLog, sampleNilrebLog);
    }

    @org.junit.Test
    public void testMain6() throws Exception {
        String path = root + "6/";
        String args[] = {path + "offices.txt", path + "wanted.txt", path + "commands.txt"};
        Main.main(args);
        String masterLog = FileUtils.readFileToString(new File("log_master.txt"));
        String sampleMastlog = FileUtils.readFileToString(new File(path + "log_master.txt"));
//        String frontLog = FileUtils.readFileToString(new File("log_front.txt"));
//        String sampleFrontLog = FileUtils.readFileToString(new File(path + "log_front.txt"));
        String berlinLog = FileUtils.readFileToString(new File("log_Berlin.txt"));
        String sampleBerlinLog = FileUtils.readFileToString(new File(path + "log_Berlin.txt"));
        String nilrebLog = FileUtils.readFileToString(new File("log_Nilreb.txt"));
        String sampleNilrebLog = FileUtils.readFileToString(new File(path + "log_Nilreb.txt"));
        assertEquals( "master logs do not match", masterLog, sampleMastlog);
//        assertEquals("front logs do not match", frontLog, sampleFrontLog);
        assertEquals("Berlin logs don't match", berlinLog, sampleBerlinLog);
        assertEquals("Nilreb logs don't match", nilrebLog, sampleNilrebLog);
    }

    @org.junit.Test
    public void testMain7() throws Exception {
        String path = root + "7/";
        String args[] = {path + "offices.txt", path + "wanted.txt", path + "commands.txt"};
        Main.main(args);
        String masterLog = FileUtils.readFileToString(new File("log_master.txt"));
        String sampleMastlog = FileUtils.readFileToString(new File(path + "log_master.txt"));
        String frontLog = FileUtils.readFileToString(new File("log_front.txt"));
        String sampleFrontLog = FileUtils.readFileToString(new File(path + "log_front.txt"));
        String berlinLog = FileUtils.readFileToString(new File("log_Berlin.txt"));
        String sampleBerlinLog = FileUtils.readFileToString(new File(path + "log_Berlin.txt"));
        String nilrebLog = FileUtils.readFileToString(new File("log_Nilreb.txt"));
        String sampleNilrebLog = FileUtils.readFileToString(new File(path + "log_Nilreb.txt"));
        assertEquals( "master logs do not match", masterLog, sampleMastlog);
        assertEquals("front logs do not match", frontLog, sampleFrontLog);
        assertEquals("Berlin logs don't match", berlinLog, sampleBerlinLog);
        assertEquals("Nilreb logs don't match", nilrebLog, sampleNilrebLog);
    }

    @org.junit.Test
    public void testMain8() throws Exception {
        String path = root + "8/";
        String args[] = {path + "offices.txt", path + "wanted.txt", path + "commands.txt"};
        Main.main(args);
        String masterLog = FileUtils.readFileToString(new File("log_master.txt"));
        String sampleMastlog = FileUtils.readFileToString(new File(path + "log_master.txt"));
        String frontLog = FileUtils.readFileToString(new File("log_front.txt"));
        String sampleFrontLog = FileUtils.readFileToString(new File(path + "log_front.txt"));
        String berlinLog = FileUtils.readFileToString(new File("log_Berlin.txt"));
        String sampleBerlinLog = FileUtils.readFileToString(new File(path + "log_Berlin.txt"));
        String nilrebLog = FileUtils.readFileToString(new File("log_Nilreb.txt"));
        String sampleNilrebLog = FileUtils.readFileToString(new File(path + "log_Nilreb.txt"));
        assertEquals( "master logs do not match", masterLog, sampleMastlog);
        assertEquals("front logs do not match", frontLog, sampleFrontLog);
        assertEquals("Berlin logs don't match", berlinLog, sampleBerlinLog);
        assertEquals("Nilreb logs don't match", nilrebLog, sampleNilrebLog);
    }
}