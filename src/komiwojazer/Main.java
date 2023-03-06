package komiwojazer;

import java.util.*;

import static java.util.Collections.shuffle;

public class Main {
    static final int EPOKI = 10;
    // Kolorowy terminal
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    //Selekcja rankingowa
    public static int[][] selekcjaRank(int[][] population, double[] decimals, boolean minimum){
        double tmp;
        int tmpInt;
        // Sortowanie tablicy
        if(!minimum) {
            for (int i = 0; i < decimals.length; i++) {
                for(int j = i; j < decimals.length; j++){
                    if (decimals[i] < decimals[j]) {
                        tmp = decimals[i];
                        decimals[i] = decimals[j];
                        decimals[j] = tmp;
                        for(int k = 0; k < decimals.length; k++){
                            tmpInt = population[i][k];
                            population[i][k] = population[j][k];
                            population[j][k] = tmpInt;
                        }
                    }
                }
            }
        }
        if(minimum) {
            for (int i = 0; i < decimals.length; i++) {
                for(int j = i; j < decimals.length; j++){
                    if (decimals[i] > decimals[j]) {
                        tmp = decimals[i];
                        decimals[i] = decimals[j];
                        decimals[j] = tmp;
                        for(int k = 0; k < decimals.length; k++){
                            tmpInt = population[i][k];
                            population[i][k] = population[j][k];
                            population[j][k] = tmpInt;
                        }
                    }
                }
            }
        }

        // Podwojne losowanie n razy
        int[][] newPopulation = new int[decimals.length][decimals.length];
        int indeks, tmpIndeks;
        for(int i = 0; i < decimals.length; i++) {
            tmpIndeks = new Random().nextInt(decimals.length) + 1;
            indeks = new Random().nextInt(tmpIndeks);
            System.arraycopy(population[indeks], 0, newPopulation[i], 0, decimals.length);
        }
        return newPopulation;
    }

    ///Selekcja turniejowa
    public static int[][] selekcjaTurn(int[][] population, double[] decimals, boolean minimum){
        Random rn = new Random();

        int nr_os1; //pierwsza trasa z pary turniejowej
        int nr_os2; //druga trasa z pary turniejowej
        int [] wygranetrasy = new int [decimals.length];
        double [][] pary = new double [decimals.length][2]; //Tablica sum długości tras losowanych Par Tras
        int [][] paryin = new int [decimals.length][2]; //Tablica indeksów losowanych Par Tras

        System.out.println("Turniej - wylosowane pary:\n");
        for(int i = 0; i < decimals.length; i++) {//Wybieramy n grup turniejowych
            //Losujemy parę
            nr_os1 = rn.nextInt(decimals.length);
            nr_os2 = rn.nextInt(decimals.length);
            // Wypisujemy wylosowana pare
            System.out.println("Para nr " + i + ":");
            System.out.println(decimals[nr_os1]);
            System.out.println(decimals[nr_os2]);

            for (int j = 0; j < population.length; j++) {
                System.out.print(population[nr_os1][j] + " ");
            }
            System.out.format(": " + "%.2f\n", decimals[nr_os1]);

            for (int j = 0; j < population.length; j++) {
                System.out.print(population[nr_os2][j] + " ");
            }
            System.out.format(": " + "%.2f\n", decimals[nr_os2]);

            //Sumy długości trasy wylosowanych tras
            pary[i][0] = decimals[nr_os1];
            pary[i][1] = decimals[nr_os2];
            //indexy wylosowanych tras
            paryin[i][0] = nr_os1;
            paryin[i][1] = nr_os2;
        }

        System.out.println("\nTurniej - wygrane trasy:");
        System.out.println();
        //Wybieramy najlepszego osobnika z pary
        for(int i = 0; i < decimals.length; i++) {
            if(!minimum) {
                if (pary[i][0] > pary[i][1]) {
                    wygranetrasy[i] = paryin[i][0];
                } else {
                    wygranetrasy[i] = paryin[i][1];
                }
            }
            else{
                if (pary[i][0] < pary[i][1]) {
                    wygranetrasy[i] = paryin[i][0];
                } else {
                    wygranetrasy[i] = paryin[i][1];
                }
            }
            System.out.println("Para nr " + i + ":");
            System.out.println("->index nr: " + wygranetrasy[i]);
            System.out.println();
        }

        int[][] newPopulation = new int[decimals.length][decimals.length];

        for(int i = 0; i < decimals.length; i++) {
            for(int j = 0; j < decimals.length; j++) {
                newPopulation[i][j] = population[wygranetrasy[i]][j];
            }
        }

        return newPopulation;
    }

    ///Selekcja ruletka (działa tylko dla maksimum)
    public static int[][] selekcjaRuletka(int[][] population, double[] decimals, boolean minimum){
        minimum = false;
        Random rn = new Random();
        double F = 0; //wartość funkcji przystosowania
        for(int i = 0; i < decimals.length; i++) {//obliczam F
            F += decimals[i];
        }
        double [] qtab = new double [decimals.length]; //Tablica dystrybuant

        double p; //prawdopowdobieństwo wyboru i-tej trasy
        double q = 0.0;
        for(int i = 0; i < decimals.length; i++){//pętla po wierszach
            p = decimals[i] / F;
            q += p;
            qtab[i] = q;
            System.out.print("q nr." + i + " = " + qtab[i]);
            System.out.println();
        }

        int [] wygranetrasy = new int [decimals.length];
        System.out.println("Ruletka - wybrane trasy: ");
        double r;
        for(int i = 0; i < decimals.length; i++){ //Obrót ruletką n-razy
            r = rn.nextDouble()*1.0; //losujemu n-razy liczbę rzeczywistą z przedziału [0,1]
            System.out.println("r = " + r);
            if(!minimum) {
                if (r <= qtab[0]) {
                    System.out.print("Trasa nr " + i + ": ");
                    wygranetrasy[i] = 0;
                } else {
                    for (int j = 1; j < decimals.length; j++) {
                        if (qtab[j - 1] < r && r <= qtab[j]) {
                            System.out.print("Trasa nr " + j + ": ");
                            wygranetrasy[i] = j;
                            break;
                        }
                    }
                }
            }
            else{
                if (r <= qtab[0]) {
                    System.out.print("Trasa nr " + i + ": ");
                    wygranetrasy[i] = 0;
                } else {
                    for (int j = 1; j < decimals.length; j++) {
                        if (qtab[j - 1] > r && r >= qtab[j]) {
                            System.out.print("Trasa nr " + j + ": ");
                            wygranetrasy[i] = j;
                            break;
                        }
                    }
                }
            }
//            System.out.println("iter=" + i);
//            System.out.println();
        }

        int[][] newPopulation = new int[decimals.length][decimals.length];

        for(int i = 0; i < decimals.length; i++) {
            for(int j = 0; j < decimals.length; j++) {
                newPopulation[i][j] = population[wygranetrasy[i]][j];
            }
        }
        return newPopulation;
    }

    public static int[][] mutacja(int[][] population, int m, double pm) {
        Random random = new Random();

        for (int i = 0; i < population.length; i++) {
            for(int j = 0; j < m; j++) {
                if (random.nextDouble() < pm) {
                    // Mutuj gen poprzez zamianę miejscami z innym losowym genenem
                    int indexToSwap = random.nextInt(population.length);
                    int temp = population[i][j];
                    population[i][j] = population[i][indexToSwap];
                    population[i][indexToSwap] = temp;
                }
            }
        }

        return population;
    }

    public static double ocena(int[] trasa, double[][] odleglosci){
        // double[] trasySUM = new double[trasa.length];
        double odleglosc = 0;

        for (int i = 0; i < trasa.length-1; i++)
        {
            odleglosc += odleglosci[trasa[i]][trasa[i+1]];
        }
        return odleglosc;
    }

    //Krzyżowanie PMX
    public static List<List<Integer>> KrzyzowaniePMX(ArrayList<Integer> rodzic1, ArrayList<Integer> rodzic2){
        List<List<Integer>> wynik_krzyzowania = new ArrayList<>();

        ArrayList<Integer> potomek1 = new ArrayList<>();
        ArrayList<Integer> potomek2 = new ArrayList<>();

        ArrayList<Integer> tmp1 = new ArrayList<>();
        ArrayList<Integer> tmp2 = new ArrayList<>();

        Random rand = new Random();

        int pktPrzeciecia1 = rand.nextInt(rodzic1.size());
        int pktPrzeciecia2 = rand.nextInt(rodzic1.size());

        int pktPrzecieciaTmp;

        if (pktPrzeciecia1 > pktPrzeciecia2)
        {
            pktPrzecieciaTmp = pktPrzeciecia1;
            pktPrzeciecia1 = pktPrzeciecia2;
            pktPrzeciecia2 = pktPrzecieciaTmp;
        }

        System.out.println("Punkt przeciecia 1: " +pktPrzeciecia1);
        System.out.println("Punkt przeciecia 2: " +pktPrzeciecia2);
        System.out.println("Rodzice: ");

        //Wypisanie rodzic1
        System.out.print("Rodzic 1: ");
        for (int i=0; i<rodzic1.size(); i++)
        {
            if (i == pktPrzeciecia1)
            {
                System.out.print("| ");
            }

            if (i == pktPrzeciecia2)
            {
                System.out.print("| ");
            }

            System.out.print(rodzic1.get(i) +" ");
        }

        System.out.println();

        //Wypisanie rodzic2
        System.out.print("Rodzic 2: ");
        for (int i=0; i<rodzic2.size(); i++)
        {
            if (i == pktPrzeciecia1)
            {
                System.out.print("| ");
            }

            if (i == pktPrzeciecia2)
            {
                System.out.print("| ");
            }

            System.out.print(rodzic2.get(i) +" ");
        }

        System.out.println();

        //Etap 1
        //Wymiana środków (środki rodziców są wymieniane między soboą i trafiają do potomków)
        for (int i=pktPrzeciecia1; i<pktPrzeciecia2; i++)
        {
            potomek1.add(rodzic2.get(i));
            potomek2.add(rodzic1.get(i));

            tmp1.add(rodzic2.get(i));
            tmp2.add(rodzic1.get(i));
        }

        System.out.println("Potomkowie po 1 etapie: ");
        System.out.println("Potomek 1: " +potomek1);
        System.out.println("Potomek 2: " +potomek2);
        System.out.println("Tmp po 1 etapie: ");
        System.out.println("Tmp 1: " +tmp1);
        System.out.println("Tmp 2: " +tmp2);

//        etap 2 – wstawiamy od rodziców początkowych te
//        miasta dla których nie zachodzi konflikt
//        Etap 3 – uzupełniamy resztę wykorzystując
//        odwzorowania ze środków

        //Etap 2 i 3 dla cyfr po lewej stronie pkt przeciecia 1
        for (int i=0; i<pktPrzeciecia1; i++)
        {
            System.out.println("\niter = " + i);
            //Sprawdzenie czy potomek 1 nie posiada cyfr z rodzica1
            //Etap 2. - jesli nie posiada (nie zachodzi konflikt)
            if(!potomek1.contains(rodzic1.get(i)))
            {
                System.out.println("potomek1 nie posiada cyfry " + rodzic1.get(i) + " z rodzica1, więc wstawiamy w miejsce " + i);
                potomek1.add(i, rodzic1.get(i));
                System.out.println("Potomek 1: " +potomek1);
            }

            //Etap 3. - jesli posiada (zachodzi konflikt)
            else
            {
                System.out.println("potomek1 posiada cyfrę " + rodzic1.get(i) + " z rodzica1, więc zachodzi konflikt w miejscu " + i);
                for (int j=0; j<tmp1.size(); j++) //pętla po rodzic2
                {
                    if (rodzic1.get(i).equals(tmp1.get(j)))
                    {
                        System.out.println("rodzic1 posiada cyfrę " + rodzic1.get(i) + " z tmp1(śroedek rodzic2), z miejsca " + j + " dla tmp1 , więc wstawiamy liczbę z tego miejsca z tmp2, czyli " + tmp2.get(j) + " w miejsce "  + i + " w potomek1");
                        potomek1.add(i, tmp2.get(j));
                        System.out.println("Potomek 1: " +potomek1);
                    }
                }
            }

            //Sprawdzenie czy potomek 2 nie posiada cyfr z rodzica1
            //Etap 2. - jesli nie posiada
            if (!potomek2.contains(rodzic2.get(i)))
            {
                potomek2.add(i, rodzic2.get(i));
            }

            //Etap 3. - jesli posiada
            else
            {
                for (int j=0; j<tmp1.size(); j++)
                {
                    if (rodzic2.get(i).equals(tmp2.get(j)))
                    {
                        potomek2.add(i, tmp1.get(j));
                    }
                }
            }
        }

        //Etap 2 i 3 dla cyfr po prawej stronie pkt przeciecia 2
        for (int i=pktPrzeciecia2; i<rodzic1.size(); i++)
        {
            //Sprawdzenie czy potomek 1 nie posiada cyfr z rodzica1
            //Etap 2. - jesli posiada
            if (!potomek1.contains(rodzic1.get(i)))
            {
                potomek1.add(i, rodzic1.get(i));
            }

            //Etap 3. - jesli nie posiada
            else
            {
                for (int j=0; j<tmp1.size(); j++)
                {
                    if (rodzic1.get(i).equals(tmp1.get(j)))
                    {
                        potomek1.add(i, tmp2.get(j));
                    }
                }
            }

            //Sprawdzenie czy potomek 2 nie posiada cyfr z rodzica1
            //Etap 2. - jesli posiada
            if (!potomek2.contains(rodzic2.get(i)))
            {
                potomek2.add(i, rodzic2.get(i));
            }

            //Etap 3. - jesli nie posiada
            else
            {
                for (int j=0; j<tmp1.size(); j++)
                {
                    if (rodzic2.get(i).equals(tmp2.get(j)))
                    {
                        potomek2.add(i, tmp1.get(j));
                    }
                }
            }
        }

        System.out.println("Potomkowie:");
        System.out.println("Potomek 1: " +potomek1);
        System.out.println("Potomek 2: " +potomek2);
        wynik_krzyzowania.add(potomek1);
        wynik_krzyzowania.add(potomek2);

        return wynik_krzyzowania;
    }

    public static void main(String[] args) {

        double pm, pk; // prawdopodbienstwo mutacji
        pm = 0.05;
        pk = 0.50;
        boolean minimum = false;
        int ilosc_miast = 10;

        //// Komiwojażer
        // Lista miast
        ArrayList<Miasto> listaMiast = new ArrayList<>();
        // Dodaję miasta do listy - każde miasto ma losowo wygenerowane współżędne x,y
        listaMiast.add(new Miasto("Białystok"));
        listaMiast.add(new Miasto("Warszawa"));
        listaMiast.add(new Miasto("Augustów"));
        listaMiast.add(new Miasto("Mońki"));
        listaMiast.add(new Miasto("Suwałki"));
        listaMiast.add(new Miasto("Białystok"));
        listaMiast.add(new Miasto("Warszawa"));
        listaMiast.add(new Miasto("Augustów"));
        listaMiast.add(new Miasto("Mońki"));
        listaMiast.add(new Miasto("Suwałki"));
        // ewentualnie można bez nazw miast i wtedy dodawać je w pętli

        int[][] miasta = new int[listaMiast.size()][listaMiast.size()]; // miasta przedstawione w formie liczbowej w tablicy
        double[][] odleglosci = new double[listaMiast.size()][listaMiast.size()]; //odległości pomiędzy konkretnymi miastami
        double[] trasySUM = new double[listaMiast.size()];

        //Wypisanie wszystkich miast
        System.out.println("Miasta: ");
        for (int i = 0; i < listaMiast.size(); i++) {
            System.out.println(i + 1 + ": " + listaMiast.get(i));
        }

        ArrayList<Integer> trasa = new ArrayList<>();
        for (int i = 0; i < listaMiast.size(); i++) {
            trasa.add(i);
        }

        for (int i = 0; i < listaMiast.size(); i++) {
            shuffle(trasa); //losowanie trasy
            for (int j = 0; j < listaMiast.size(); j++) {
                miasta[i][j] = trasa.get(j);
                odleglosci[i][j] = listaMiast.get(i).obliczOdleglosc(listaMiast.get(j));
            }
        }

        System.out.println(ANSI_CYAN + "-----------------------" + ANSI_RESET);
        System.out.println("Odległości pomiędzy konkretnymi miastami (w przybliżeniu do 2 miejsc po przecinku): ");
        for (int i = 0; i < listaMiast.size(); i++) {
            for (int j = 0; j < listaMiast.size(); j++) {
                System.out.format("%.2f\t", odleglosci[i][j]);
//               System.out.print(odleglosci[i][j] +"\t\t ");
            }
            System.out.println();
        }


        System.out.println(ANSI_CYAN + "-----------------------" + ANSI_RESET);
        System.out.println("Kombinacje tras: ");
        for (int i = 0; i < listaMiast.size(); i++) {
            for (int j = 0; j < listaMiast.size(); j++) {
                System.out.print(miasta[i][j]/*+1*/ + " ");
            }
            System.out.println();
        }
        System.out.println(ANSI_CYAN + "-----------------------" + ANSI_RESET);

        //trasySUM = ocena(miasta, odleglosci);
        for (int i = 0; i < miasta.length; i++) {
            trasySUM[i] = ocena(miasta[i], odleglosci);
        }

        System.out.println("Suma odległości poszczególnych tras (w przybliżeniu do 2 miejsc po przecinku):");
        for (int i = 0; i < listaMiast.size(); i++) {
            for (int j = 0; j < listaMiast.size(); j++) {
                System.out.print(miasta[i][j] + " ");
            }
            System.out.format(": " + "%.2f\n", trasySUM[i]);
        }
        System.out.println(ANSI_CYAN + "-----------------------" + ANSI_RESET);


        // ZMIENNE WTYKORZYSTANE W PETLI ALGORYTMU
        int z, n, dolosowany, buff1, nros1, nros2, z1;
        int[] tras;
        int[] tmpTrasa;
        double r, min=0, max=1;
        double[] tmpOdleglosci;
        List<List<Integer>> wynik_krzyzowania = new ArrayList<>();
        ArrayList<Trasa> trasy = new ArrayList<>(); //lista wszystkich tras
        ArrayList<Trasa> krzyzowane = new ArrayList<>(); //lista krzyżowanych tras
        ArrayList<Trasa> niekrzyzowane = new ArrayList<>(); //lista niekrzyżowanych tras
        ArrayList<Integer> krzyz1 = new ArrayList<>();
        ArrayList<Integer> krzyz2 = new ArrayList<>();
        Random rn = new Random();
        double localAvg, avg=0;

        // START PETLI ALGORYTMU
        for(int iter = 0; iter < EPOKI; iter++){
            localAvg = 0;

            // Wyczyszczenie list po kazdej iteracji
            wynik_krzyzowania.clear(); trasy.clear(); krzyzowane.clear(); niekrzyzowane.clear(); krzyz1.clear(); krzyz2.clear();

            ///SELEKCJA
            String selekcja;
            miasta = selekcjaRank(miasta, trasySUM, minimum); selekcja="rankingowej";
        //  miasta = selekcjaTurn(miasta, trasySUM, minimum); selekcja="turniejowej";
         //   miasta = selekcjaRuletka(miasta, trasySUM, minimum); selekcja="ruletka";
            for (int i = 0; i < miasta.length; i++) {
                trasySUM[i] = ocena(miasta[i], odleglosci);
            }
            System.out.println("Trasy po wykorzystaniu algorytmu selekcji " + selekcja + ":");
            for (int i = 0; i < listaMiast.size(); i++) {
                for (int j = 0; j < listaMiast.size(); j++) {
                    System.out.print(miasta[i][j] + " ");
                }
                System.out.format(": " + "%.2f\n", trasySUM[i]);
            }

//            // Selekcja - Turniej
//            miasta = selekcja2(miasta, trasySUM, minimum);
//            for (int i = 0; i < miasta.length; i++) {
//                trasySUM[i] = ocena(miasta[i], odleglosci);
//            }
//            System.out.println("Trasy po wykorzystaniu algorytmu selekcji turniejowej:");
//            for (int i = 0; i < listaMiast.size(); i++) {
//                for (int j = 0; j < listaMiast.size(); j++) {
//                    System.out.print(miasta[i][j] + " ");
//                }
//                System.out.format(": " + "%.2f\n", trasySUM[i]);
//            }

//            // Selekcja - Ruletka
//            miasta = selekcja3(miasta, trasySUM, minimum);
//            for (int i = 0; i < miasta.length; i++) {
//                trasySUM[i] = ocena(miasta[i], odleglosci);
//            }
//            System.out.println("Trasy po wykorzystaniu algorytmu selekcji - ruletki:");
//            for (int i = 0; i < listaMiast.size(); i++) {
//                for (int j = 0; j < listaMiast.size(); j++) {
//                    System.out.print(miasta[i][j] + " ");
//                }
//                System.out.format(": " + "%.2f\n", trasySUM[i]);
//            }



            // Mutacja
            miasta = mutacja(miasta, listaMiast.size(), pm);
            for (int i = 0; i < miasta.length; i++) {
                trasySUM[i] = ocena(miasta[i], odleglosci);
            }
            System.out.println("Trasy po wykorzystaniu mutacji:");
            for (int i = 0; i < listaMiast.size(); i++) {
                for (int j = 0; j < listaMiast.size(); j++) {
                    System.out.print(miasta[i][j] + " ");
                }
                System.out.format(": " + "%.2f\n", trasySUM[i]);
            }

            // Krzyżowanie
            System.out.println(ANSI_CYAN + "-----------------------" + ANSI_RESET);
            System.out.println("Krzyżowanie: ");
            System.out.println();

            System.out.println("Wszystkie trasy przed krzyżowaniem");
            for (int i = 0; i < listaMiast.size(); i++) {
                System.out.print("Nr " + i + ": ");
                trasy.add(new Trasa(miasta[i], trasySUM[i])); // dodaję trasę do listy
                System.out.println(trasy.get(i));
            }

            z = 0; // licznik do sprawdzania parzystości

            for (Trasa value : trasy) { //losowanie krzyżowanych tras
                r = min + (new Random().nextDouble() * (max - min));
                if (r < pk) {
                    krzyzowane.add(value);
                    z++;
                } else niekrzyzowane.add(value);
            }

            if (z == 0) {
                System.out.println(ANSI_RED + "Brak krzyżowanych tras! (nieprzeprowadzono krzyżowania)" + ANSI_RESET);
            }
            else System.out.println(ANSI_BLUE_BACKGROUND + "Krzyżowaniu ulegną trasy: " + ANSI_RESET);

            if (z % 2 != 0) {
                // jak nieparzysta liczba krzyżowanych osobników i istnieją jakieś niekrzyżowane  to wylosuj kolejnego
                if (niekrzyzowane.size() > 0) {
                    dolosowany = rn.nextInt(niekrzyzowane.size());
                    System.out.println("Nieparzysta liczba krzyżowanych tras, dolosowano trasę nr: " + dolosowany);
                    krzyzowane.add(niekrzyzowane.get(dolosowany));
                    niekrzyzowane.remove(dolosowany);
                    z++;
                }
                // jak nieparzysta liczba krzyżowanych osobników i nieistnieją niekrzyżowane to usuń losowego
                else {
                    dolosowany = rn.nextInt(krzyzowane.size());
                    System.out.println("Nieparzysta liczba krzyżowanych tras, usunięto trasę nr: " + dolosowany);
                    krzyzowane.remove(dolosowany);
                    z--;
                }
            }

            // Wypisanie krzyżowanych osobników
            for (Trasa value : krzyzowane) {
                System.out.println(value);
            }


            z1 = z / 2;
            // Losowanie par do krzyżowania i przeprowadzanie krzyżowania
            for (int i = 0; i < z1; i++) {
                // losujemy pary krzyżowanych osobników
                nros1 = rn.nextInt(z);
                nros2 = rn.nextInt(z);
                while (nros1 == nros2) { // zabezbieczenie aby nie wylosować dwóch tych samych osobników
                    nros2 = rn.nextInt(z);
                }
                if (nros2 > nros1) { // jeżeli punkt2 jest większy to zamieniam zmienne miejscami
                    buff1 = nros2;
                    nros2 = nros1;
                    nros1 = buff1;
                }
                z = z - 2;

                System.out.println();
                System.out.println("Rodzice nr " + i + ":");
                System.out.println(krzyzowane.get(nros1));
                System.out.println(krzyzowane.get(nros2));

                for (int j = 0; j < krzyzowane.get(nros1).getTrasa().length; j++) {
                    krzyz1.add(krzyzowane.get(nros1).trasa[j]);
                }
                for (int j = 0; j < krzyzowane.get(nros2).getTrasa().length; j++) {
                    krzyz2.add(krzyzowane.get(nros2).trasa[j]);
                }

                wynik_krzyzowania = KrzyzowaniePMX(krzyz1, krzyz2);

                // usunięcie już skrzyżowanych par tras z listy do krzyżowania
                krzyzowane.remove(nros1);
                krzyzowane.remove(nros2);
                krzyz1.clear();
                krzyz2.clear();


                tras = new int[listaMiast.size()];
                for (int j = 0; j < wynik_krzyzowania.get(0).size(); j++) {
                    tras[j] = wynik_krzyzowania.get(0).get(j);
                }
                trasy.add(new Trasa(tras, 0)); // dodatnie pierwszego potomka

                tras = new int[listaMiast.size()];
                for (int j = 0; j < wynik_krzyzowania.get(1).size(); j++) {
                    tras[j] = wynik_krzyzowania.get(1).get(j);
                }
                trasy.add(new Trasa(tras, 0));// dodatnie drugiego potomka
            }

            System.out.println(ANSI_CYAN + "-----------------------" + ANSI_RESET);
            // Obliczenie tras
            System.out.println("Wszystkie trasy po krzyżowaniu:");
            tmpOdleglosci = new double[trasy.size()];

            for (int i = 0; i < trasy.size(); i++) {
                tmpTrasa = trasy.get(i).trasa;
                tmpOdleglosci[i] = 0;
                for (int j = 0; j < tmpTrasa.length - 1; j++) {
                    tmpOdleglosci[i] += odleglosci[tmpTrasa[j]][tmpTrasa[j + 1]];
                }
                trasy.get(i).tras_sum = tmpOdleglosci[i];
            }
            // Wypisanie tras po krzyzowaniu
            trasy.forEach(System.out::println);

            // Posortowanie tras
            Trasa temp;
            if (minimum) {
                n = trasy.size();
                for (int i = 0; i < n; i++) {
                    for (int j = 1; j < (n - i); j++) {
                        if (trasy.get(j - 1).tras_sum < trasy.get(j).tras_sum) {
                            temp = trasy.get(j - 1);
                            trasy.set(j - 1, trasy.get(j));
                            trasy.set(j, temp);
                        }
                    }
                }
            }
            if (!minimum) {
                n = trasy.size();
                for (int i = 0; i < n; i++) {
                    for (int j = 1; j < (n - i); j++) {
                        if (trasy.get(j - 1).tras_sum > trasy.get(j).tras_sum) {
                            temp = trasy.get(j - 1);
                            trasy.set(j - 1, trasy.get(j));
                            trasy.set(j, temp);
                        }
                    }
                }
            }

            System.out.println(ANSI_CYAN + "-----------------------" + ANSI_RESET);
            // Wypisanie posortowanych tras
            System.out.println("Posortowane trasy:");
            trasy.forEach(System.out::println);

            // Wybranie 10 osobnikow do populacji
            while (trasy.size() != listaMiast.size()) {
                trasy.remove(trasy.size() - 1);
            }
            System.out.println(ANSI_CYAN + "-----------------------" + ANSI_RESET);
            System.out.println("Wybrana populacja (10 najlepszych tras):");
            trasy.forEach(System.out::println);

            for(int i = 0; i < trasy.size(); i++){
                localAvg += trasy.get(i).tras_sum;
            }
//            localAvg /= trasy.size();
            avg += localAvg;
            System.out.println("\nŚrednia sumy tras iteracji " + iter +" = " + localAvg);
        }
        avg/=(EPOKI*listaMiast.size());
        System.out.println("\nŚrednia sum tras z " + EPOKI + " epok = " + Math.round(avg*100.0)/100.0);
    }
}