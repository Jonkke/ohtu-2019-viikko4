
package ohtu.intjoukkosovellus;

public class IntJoukko {

    public final static int KAPASITEETTI = 5, // aloitustalukon koko
                            OLETUSKASVATUS = 5;  // luotava uusi taulukko on 
    // näin paljon isompi kuin vanha
    private int kasvatuskoko;     // Uusi taulukko on tämän verran vanhaa suurempi.
    private int[] lukujono;      // Joukon luvut säilytetään taulukon alkupäässä. 
    private int alkioidenLkm;    // Tyhjässä joukossa alkioiden_määrä on nolla. 

    public IntJoukko() {
        lukujono = new int[KAPASITEETTI];
        for (int indeksi = 0; indeksi < lukujono.length; indeksi++) {
            lukujono[indeksi] = 0;
        }
        alkioidenLkm = 0;
        this.kasvatuskoko = OLETUSKASVATUS;
    }

    public IntJoukko(int kapasiteetti) {
        if (!kapasiteettiOnValidi(kapasiteetti)) return;
        
        lukujono = new int[kapasiteetti];
        for (int indeksi = 0; indeksi < lukujono.length; indeksi++) {
            lukujono[indeksi] = 0;
        }
        alkioidenLkm = 0;
        this.kasvatuskoko = OLETUSKASVATUS;

    }
    
    
    public IntJoukko(int kapasiteetti, int kasvatuskoko) {
        if (!kapasiteettiOnValidi(kapasiteetti)) return;
        if (!kasvatuskokoOnValidi(kasvatuskoko)) return;
        lukujono = new int[kapasiteetti];
        for (int indeksi = 0; indeksi < lukujono.length; indeksi++) {
            lukujono[indeksi] = 0;
        }
        alkioidenLkm = 0;
        this.kasvatuskoko = kasvatuskoko;

    }

    public void lisaaJoukkoon(int luku) {

        if (alkioidenLkm == 0) {
            lukujono[0] = luku;
            alkioidenLkm++;
        } else {
        }
        if (!kuuluuJoukkoon(luku)) {
            lukujono[alkioidenLkm] = luku;
            alkioidenLkm++;
            if (alkioidenLkm % lukujono.length == 0) {
                int[] taulukkoOld = new int[lukujono.length];
                taulukkoOld = lukujono;
                kopioiTaulukko(lukujono, taulukkoOld);
                lukujono = new int[alkioidenLkm + kasvatuskoko];
                kopioiTaulukko(taulukkoOld, lukujono);
            }
        }
    }

    public boolean kuuluuJoukkoon(int luku) {
        for (int indeksi = 0; indeksi < alkioidenLkm; indeksi++) {
            if (luku == lukujono[indeksi]) {
                return true;
            }
        }
        return false;
    }

    public void poistaJoukosta(int luku) {
        int kohta = -1;
        int apu;
        for (int indeksi = 0; indeksi < alkioidenLkm; indeksi++) {
            if (luku == lukujono[indeksi]) {
                kohta = indeksi; //siis luku löytyy tuosta kohdasta :D
                lukujono[kohta] = 0;
                break;
            }
        }
        if (kohta != -1) {
            for (int indeksi = kohta; indeksi < alkioidenLkm - 1; indeksi++) {
                apu = lukujono[indeksi];
                lukujono[indeksi] = lukujono[indeksi + 1];
                lukujono[indeksi + 1] = apu;
            }
            alkioidenLkm--;
        }
    }

    private void kopioiTaulukko(int[] vanha, int[] uusi) {
        for (int indeksi = 0; indeksi < vanha.length; indeksi++) {
            uusi[indeksi] = vanha[indeksi];
        }

    }

    public int mahtavuus() {
        return alkioidenLkm;
    }


    @Override
    public String toString() {
        if (alkioidenLkm == 0) {
            return "{}";
        } else if (alkioidenLkm == 1) {
            return "{" + lukujono[0] + "}";
        } else {
            String tuotos = "{";
            for (int indeksi = 0; indeksi < alkioidenLkm - 1; indeksi++) {
                tuotos += lukujono[indeksi];
                tuotos += ", ";
            }
            tuotos += lukujono[alkioidenLkm - 1];
            tuotos += "}";
            return tuotos;
        }
    }

    public int[] toIntArray() {
        int[] taulu = new int[alkioidenLkm];
        for (int indeksi = 0; indeksi < taulu.length; indeksi++) {
            taulu[indeksi] = lukujono[indeksi];
        }
        return taulu;
    }
   

    public static IntJoukko yhdiste(IntJoukko a, IntJoukko b) {
        IntJoukko x = new IntJoukko();
        int[] aTaulu = a.toIntArray();
        int[] bTaulu = b.toIntArray();
        for (int indeksi = 0; indeksi < aTaulu.length; indeksi++) {
            x.lisaaJoukkoon(aTaulu[indeksi]);
        }
        for (int i = 0; i < bTaulu.length; i++) {
            x.lisaaJoukkoon(bTaulu[i]);
        }
        return x;
    }

    public static IntJoukko leikkaus(IntJoukko a, IntJoukko b) {
        IntJoukko y = new IntJoukko();
        int[] aTaulu = a.toIntArray();
        int[] bTaulu = b.toIntArray();
        for (int indeksi1 = 0; indeksi1 < aTaulu.length; indeksi1++) {
            for (int indeksi2 = 0; indeksi2 < bTaulu.length; indeksi2++) {
                if (aTaulu[indeksi1] == bTaulu[indeksi2]) {
                    y.lisaaJoukkoon(bTaulu[indeksi2]);
                }
            }
        }
        return y;

    }
    
    public static IntJoukko erotus ( IntJoukko a, IntJoukko b) {
        IntJoukko z = new IntJoukko();
        int[] aTaulu = a.toIntArray();
        int[] bTaulu = b.toIntArray();
        for (int indeksi = 0; indeksi < aTaulu.length; indeksi++) {
            z.lisaaJoukkoon(aTaulu[indeksi]);
        }
        for (int indeksi = 0; indeksi < bTaulu.length; indeksi++) {
            z.poistaJoukosta(bTaulu[indeksi]);
        }
 
        return z;
    }
    
    private boolean kapasiteettiOnValidi(int kapasiteetti) {
        return kapasiteetti >= 0;
    }
    
    private boolean kasvatuskokoOnValidi(int kasvatuskoko) {
        return kasvatuskoko >= 0;
    }
        
}
