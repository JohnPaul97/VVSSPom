package biblioteca.control;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import biblioteca.model.Carte;
import biblioteca.repository.repo.CartiRepoFile;
import biblioteca.repository.repoMock.CartiRepoMock;
import biblioteca.view.Consola;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TopDownTesting {

    private static final String TITLE = "Titlu";
    private static final int YEAR = 1997;

    private static String adaugaCarte, cautaCarti, afisareCarti, exit;

    private static String TITLU, AN, AUTOR, EDITURA, CUVANT;

    static {
        TITLU = "bla";
        AN = "1997";
        AUTOR = "gogu";
        EDITURA = "bla";
        CUVANT = "mda";

        adaugaCarte = "1"
                + System.lineSeparator()
                + TITLU
                + System.lineSeparator()
                + AN
                + System.lineSeparator()
                + "1"
                + System.lineSeparator()
                + AUTOR
                + System.lineSeparator()
                + EDITURA
                + System.lineSeparator()
                + "1"
                + System.lineSeparator()
                + CUVANT
                + System.lineSeparator();

        cautaCarti = "2"
                + System.lineSeparator()
                + AUTOR
                + System.lineSeparator();

        afisareCarti = "3"
                + System.lineSeparator()
                + AN
                + System.lineSeparator();

        exit = "0";
    }

    private Carte carte = new Carte();

    private BibliotecaCtrl bibliotecaCtrl = new BibliotecaCtrl(new CartiRepoFile());

    private Consola consola;

    @Before
    public void setup() {
        carte.setTitlu(TITLE);
        carte.setAnAparitie(YEAR);
        carte.setReferenti(Collections.singletonList("Autor"));
        carte.setEditura("Humanitas");
        carte.setCuvinteCheie(Collections.singletonList("cuvant cheie"));

        bibliotecaCtrl = new BibliotecaCtrl(new CartiRepoMock());
        consola = new Consola(bibliotecaCtrl);
    }

    @After
    public void tearDown() throws Exception {
        bibliotecaCtrl.stergeCarte(carte);
    }

    //F01
    @Test
    public void givenValidTitleAndYear_whenAddingCarte_carteIsSuccesufullyAdded() throws Exception {
        bibliotecaCtrl.adaugaCarte(carte);

        List<Carte> carti = bibliotecaCtrl.cautaCarte(carte.getReferenti().get(0));

        assertBooksEquals(carti.get(carti.size() - 1), carte);
    }

    //F02
    @Test
    public void givenExistentAutor_whenRetrievingCarti_thenOk() throws Exception {
        bibliotecaCtrl.adaugaCarte(carte);

        List<Carte> carti = bibliotecaCtrl.cautaCarte("Autor");

        assertBooksEquals(carti.get(0), carte);
    }

    //F03
    @Test
    public void givenExistentYear_whenRetrieveingCArtiFromASpecificYear_thenNotNullListIsReturned() throws Exception {
        bibliotecaCtrl.adaugaCarte(carte);
        bibliotecaCtrl.adaugaCarte(carte);
        List<Carte> carti = bibliotecaCtrl.getCartiOrdonateDinAnul("1997");
        assertTrue(carti.size() >= 0);

        for (int i = 0; i > carti.size() - 1; ++i) {
            Carte currentBook = carti.get(i);
            Carte nextBook = carti.get(i + 1);

            if (!currentBook.getTitlu().equals(nextBook.getTitlu())) {
                assertTrue(currentBook.getReferenti().get(0).compareTo(nextBook.getReferenti().get(0)) <= 0);
            } else {
                assertTrue(currentBook.getTitlu().compareTo(nextBook.getTitlu()) < 0);
            }
        }
    }

    @Test
    public void integratePWithA() throws Exception {

        String testCmd = adaugaCarte + exit;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testCmd.getBytes());

        System.setIn(inputStream);

        consola.executa();

        carte.setTitlu(TITLU);
        carte.setAnAparitie(Integer.parseInt(AN));
        carte.setReferenti(Collections.singletonList(AUTOR));
        carte.setEditura(EDITURA);
        carte.setCuvinteCheie(Collections.singletonList(CUVANT));

        List<Carte> carti = bibliotecaCtrl.cautaCarte(AUTOR);
        assertBooksEquals(carti.get(carti.size() - 1), carte);

        System.setIn(System.in);
    }

    @Test
    public void integratePAWithB() throws Exception {
        String testCmd = adaugaCarte + cautaCarti + exit;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testCmd.getBytes());

        System.setIn(inputStream);

        consola.executa();

        carte.setTitlu(TITLU);
        carte.setAnAparitie(Integer.parseInt(AN));
        carte.setReferenti(Collections.singletonList(AUTOR));
        carte.setEditura(EDITURA);
        carte.setCuvinteCheie(Collections.singletonList(CUVANT));

        List<Carte> carti = bibliotecaCtrl.cautaCarte(AUTOR);
        assertBooksEquals(carti.get(carti.size() - 1), carte);

        System.setIn(System.in);
    }

    @Test
    public void integratePABWithC() throws Exception {
        String testCmd = adaugaCarte + cautaCarti + afisareCarti + exit;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testCmd.getBytes());

        System.setIn(inputStream);

        consola.executa();

        carte.setTitlu(TITLU);
        carte.setAnAparitie(Integer.parseInt(AN));
        carte.setReferenti(Collections.singletonList(AUTOR));
        carte.setEditura(EDITURA);
        carte.setCuvinteCheie(Collections.singletonList(CUVANT));

        List<Carte> carti = bibliotecaCtrl.cautaCarte(AUTOR);
        assertBooksEquals(carti.get(carti.size() - 1), carte);

        System.setIn(System.in);
    }

    public void assertBooksEquals(Carte b1, Carte b2) {
        assertEquals(b1.getTitlu(), b2.getTitlu());
        assertEquals(b1.getEditura(), b2.getEditura());
        assertEquals(b1.getReferenti(), b2.getReferenti());
        assertEquals(b1.getAnAparitie(), b2.getAnAparitie());
        assertEquals(b1.getCuvinteCheie(), b2.getCuvinteCheie());
    }

}