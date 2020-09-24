package main;

import java.math.BigInteger;
import java.util.concurrent.Callable;

public class ProvaT implements Callable<Integer> {

    int i;
    int id;
    String tabs = "\t";

    public ProvaT(int i, int id) {
        this.i = i;
        this.id = id;
        for (int j = 0; j < id; j++) {
            tabs += "\t\t\t";
        }
    }

    private String stato(){

        return tabs + id + " --> " + i;
    }

    @Override
    public Integer call() {
        while (i <10){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return 0;
        /**long inizio = System.currentTimeMillis();
        System.out.println(tabs + "Running " + id);
        while (i-->0){
            System.out.println(stato());
        }
        Thread.yield();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long fine  = System.currentTimeMillis();
        long diff = fine - inizio;

        System.out.println(tabs + "Tempo esecuzione: " + inizio + " --> " + fine);
        return (int)diff;**/
    }


}
