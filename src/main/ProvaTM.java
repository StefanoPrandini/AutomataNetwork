package main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.isNull;
class Threaddino implements Runnable{
    @Override
    public void run() {

    }


}

public class ProvaTM {


    public static void main(String[] args) {


        Date ora = new Date();
       SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
       System.out.println(format.format(ora).trim().replace(' ', '_'));

        /**

        ArrayList<Thread> trs = new ArrayList<>();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Scanner s = new Scanner(System.in);

        System.out.println("inserisci");
        while (Integer.parseInt(s.next()) != 5){
            System.out.println("non zero");
        }
        int res = 0;
        for (int j = 0; j < Integer.parseInt(s.next()); j++) {
            int nome = j;
            ProvaT x = new ProvaT(j+10, nome);

            try {

                //submit produce un Future<Integer>
                res = res + exec.submit(x).get();
            }catch (InterruptedException ie){
                System.out.println(ie);
            }catch (ExecutionException ee){
                System.out.println(ee);
            }


        }


        System.out.println(res);


        exec.shutdown();

**/
    }
}
