package komiwojazer;

import static java.lang.Math.*;
import static java.lang.Math.pow;

public class Miasto {
    String nazwa;
    int x;
    int y;

    public Miasto(String nazwa)
    {
        this.nazwa = nazwa;
        x = (int) (Math.random()*1000);
        y = (int) (Math.random()*1000);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public String getNazwa()
    {
        return nazwa;
    }

    public double obliczOdleglosc(Miasto miasto) //zwraca odległość od wybranego miasta
    {
        int odleglosc_x = abs(getX() - miasto.getX());
        int odleglosc_y = abs(getY() - miasto.getY());

        double odleglosc = sqrt(pow(odleglosc_x, 2) + pow(odleglosc_y, 2));

        return odleglosc;
    }

    @Override
    public String toString() {
        return "Miasto{" +
                "nazwa=" + nazwa +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
