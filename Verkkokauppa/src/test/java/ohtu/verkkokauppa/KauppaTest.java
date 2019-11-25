package ohtu.verkkokauppa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KauppaTest {
    Pankki pankki;
    Viitegeneraattori viite;
    Varasto varasto;

    @Before
    public void setUp() {
        pankki = mock(Pankki.class);
        viite = mock(Viitegeneraattori.class);
        varasto = mock(Varasto.class);
    }
    
    @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan() {
        when(viite.uusi()).thenReturn(42);
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

        Kauppa k = new Kauppa(varasto, pankki, viite);              

        k.aloitaAsiointi();
        k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
        k.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto(eq("pekka"), eq(42), eq("12345"), anyString(), eq(5));
    }
    
    @Test
    public void tilinSiirtoSuoritetaanOikealtaAsiakkaaltaJaOikeallaSummallaKahdellaEriTuotteella() {
        when(viite.uusi()).thenReturn(432);
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        
        when(varasto.saldo(2)).thenReturn(15);
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "juusto", 12));
        
        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(2);
        k.tilimaksu("maija", "13579");
        
        verify(pankki).tilisiirto(eq("maija"), eq(432), eq("13579"), anyString(), eq(17));
    }
    
    @Test
    public void tilinSiirtoSuoritetaanOikealtaAsiakkaaltaJaOikeallaSummallaKahdellaSamallaTuotteella() {
        when(viite.uusi()).thenReturn(65);
        when(varasto.saldo(2)).thenReturn(15);
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "juusto", 12));
        
        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(2);
        k.lisaaKoriin(2);
        k.tilimaksu("maija", "13579");
        
        verify(pankki).tilisiirto(eq("maija"), eq(65), eq("13579"), anyString(), eq(24));
    }
    
    @Test
    public void tilinSiirtoSuoritetaanOikealtaAsiakkaaltaJaOikeallaSummallaTuotteellaJotaOnJaTuotteellaJotaEiOle() {
        when(viite.uusi()).thenReturn(653);
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        
        when(varasto.saldo(2)).thenReturn(0);
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "juusto", 12));
        
        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(2);
        k.tilimaksu("petteri", "98765");
        
        verify(pankki).tilisiirto(eq("petteri"), eq(653), eq("98765"), anyString(), eq(5));
    }
    
    @Test
    public void aloitaAsiointiNollaaEdellisenOstoksenTiedot() {
        when(viite.uusi()).thenReturn(5).thenReturn(6);
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        
        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(1);
        k.tilimaksu("petteri", "98765");
        
        verify(pankki).tilisiirto(eq("petteri"), eq(5), eq("98765"), anyString(), eq(10));
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.tilimaksu("maija", "13579");
        
        verify(pankki).tilisiirto(eq("maija"), eq(6), eq("13579"), anyString(), eq(5));
    }
    
    @Test
    public void jokaiselleMaksuTapahtumalleHaetaanUusiViite() {
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        
        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.tilimaksu("petteri", "98765");
        
        verify(viite, times(1)).uusi();
    }
    
    @Test
    public void yhdenTuotteenOttoJaPalautusEiMaksaMitaan() {
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        
        Kauppa k = new Kauppa(varasto, pankki, viite);
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.poistaKorista(1);
        k.tilimaksu("petteri", "98765");
        
        verify(pankki).tilisiirto(eq("petteri"), anyInt(), eq("98765"), anyString(), eq(0));
    }
}

