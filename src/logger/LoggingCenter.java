/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logger;

import controllers.SimulatorSystem;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Hao
 */
public class LoggingCenter {
    
    private static LoggingCenter INSTANCE = null;
    
    public synchronized static LoggingCenter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoggingCenter();
        }
        return INSTANCE;
    }
    
    public LoggingCenter() {
        logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {

            // This block configure the logger with handler and formatter  
            fh = new FileHandler("MyLogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages  
            //logger.info("My first log");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//    public static enum Type {
//
//        NONE,
//        INFO,
//        ERR,
//        WARNNING
//    }

//    public static void getLog(Object o, int type, String m) {
//        try {
//            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("myfile.txt", true)));
//            out.println("the text");
//            out.close();
//        } catch (IOException e) {
//        }
//    }
    public static Logger logger;

    public static void writeLog() {

    }
}
