package komiwojazer;

import java.util.ArrayList;
import java.util.Arrays;

public class Trasa {
    int [] trasa; //trasa
    double tras_sum; // suma trasy

    public Trasa(int[] trasa, double tras_sum) {
        this.trasa = trasa;
        this.tras_sum = tras_sum;
    }

    public int[] getTrasa() {
        return trasa;
    }

    public void setTrasa(int[] trasa) {
        this.trasa = trasa;
    }

    public double getTras_sum() {
        return tras_sum;
    }

    public void setTras_sum(double tras_sum) {
        this.tras_sum = tras_sum;
    }

    @Override
    public String toString() {
        return "Trasa{" +
                "trasa=" + Arrays.toString(trasa) +
                ", tras_sum=" + tras_sum +
                '}';
    }

}
