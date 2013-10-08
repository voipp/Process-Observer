package com.company;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main implements Runnable {

    static int period;

    Date date;

    ProcessBuilder pb = new ProcessBuilder("tasklist","/FO","CSV");

    private class processInfo
    {
        StringBuilder process_name;
        StringBuilder memory;


        public processInfo(StringBuilder process_name , StringBuilder memory)
        {
            this.process_name = process_name;this.memory = memory;
        }

        public String getProcessName()
        {
         return this.process_name.toString();
        }

        public String getMemory()
        {
            return this.memory.toString();
        }
    }

    public static void main(String[] args) {

        Properties prop = new Properties();

        try {

            prop.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        period = Integer.valueOf(prop.getProperty("time_period"));
        Main myMain = new Main();
        Thread observerThread = new Thread(myMain);
        observerThread.start();

    }

    public void run() {

        while(true)
        {

            try {
                //---------------------------------time and file
                OutputStreamWriter fileOutputStream = new OutputStreamWriter(new BufferedOutputStream( new FileOutputStream("process.xml")));

                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                fileOutputStream.write("date:  " + df.format( Calendar.getInstance().getTime() ) + "\n");
                //---------------------------------time and file


                //---------------------------------process console reading and writing to file
                BufferedReader isr;
                isr = new BufferedReader( new InputStreamReader( pb.start().getInputStream() ) );
                isr.readLine();
                for (String str = isr.readLine() ; str!=null ; str = isr.readLine())
                {
                String[] strs = str.split(",");
                String s = new String(String.valueOf("<process memory = " + (new StringBuilder(strs[4])).deleteCharAt(strs[4].length() - 1).deleteCharAt(0).toString()
                        + "> " + (new StringBuilder(strs[0])).deleteCharAt(strs[0].length() - 1).deleteCharAt(0) + "<\\process> \n").getBytes(),"Cp866");
                fileOutputStream.write(new String(s.getBytes("Cp1251")));//.getBytes("Cp1251").toString());
                };
                //---------------------------------process console reading and writing to file

                fileOutputStream.close();

                try {
                    Thread.sleep(1000*period);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } ;

            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }


    }

    }

