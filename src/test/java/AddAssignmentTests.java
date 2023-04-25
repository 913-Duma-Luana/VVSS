import domain.Tema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.Validator;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddAssignmentTests {

    private Service service;
    private TemaXMLRepository temaXmlRepo;
    private Validator<Tema> temaValidator;
    private String temaXMLFilePath;

    @BeforeEach
    void setUp() {
        temaXMLFilePath = "tema-tests.xml";
        temaValidator = new TemaValidator();
        temaXmlRepo = new TemaXMLRepository(temaValidator, temaXMLFilePath);
        service = new Service(null, temaXmlRepo, null); // Only temaXmlRepo is required for these tests
    }

    @Test
    void testSaveTemaSuccess() {
        // test case for a valid Tema
        Random rand = new Random(System.currentTimeMillis());
        int idRandom = rand.nextInt(1000);

        String id = Integer.toString(idRandom);
        String desc = "descriere tema";
        int startWeek = 1;
        int endWeek = 2;

        int result = service.saveTema(id, desc, endWeek, startWeek);

        assertEquals(0, result);
        Tema savedTema = temaXmlRepo.findOne(id);
        assertEquals(id, savedTema.getID());
        assertEquals(desc, savedTema.getDescriere());
        assertEquals(startWeek, savedTema.getStartline());
        assertEquals(endWeek, savedTema.getDeadline());
    }

    @Test
    void testSaveTemaDuplicate() {
        // Test case for trying to add an already existing tema
        String id = "2";
        String desc = "descriere tema";
        int startWeek = 5;
        int endWeek = 6;

        service.saveTema(id, desc, endWeek, startWeek); // Save the tema once

        int result = service.saveTema(id, desc, endWeek, startWeek); // Attempt to save the tema again

        assertEquals(1, result);
    }

    @Test
    void testSaveTema_emptyId() {
        // Test case for an assignment with an empty ID
        int result = service.saveTema("", "Descriere Tema", 10, 8);
        assertEquals(1, result, "Expected saveTema to return 1 for an assignment with an empty ID");
    }

    @Test
    void testSaveTema_nullId() {
        // Test case for an assignment with a null ID
        int result = service.saveTema(null, "Descriere Tema", 10, 8);
        assertEquals(1, result, "Expected saveTema to return 1 for an assignment with a null ID");
    }

    @Test
    void testSaveTema_emptyDescriere() {
        // Test case for an assignment with an empty description
        int result = service.saveTema("1", "", 10, 8);
        assertEquals(1, result, "Expected saveTema to return 1 for an assignment with an empty description");
    }

    @Test
    void testSaveTema_nullDescriere() {
        // Test case for an assignment with a null description
        int result = service.saveTema("1", null, 10, 8);
        assertEquals(1, result, "Expected saveTema to return 1 for an assignment with a null description");
    }

    @Test
    void testSaveTema_invalidDeadlineIsZero() {
        // Test case for an assignment with an invalid deadline - deadline = 0 (should be >= 1)
        int result = service.saveTema("1", "Descriere Tema", 0, 8);
        assertEquals(1, result, "Expected saveTema to return 1 for an assignment with an invalid deadline (< 1)");
    }

    @Test
    void testSaveTema_invalidDeadlineAboveLimit() {
        // Test case for an assignment with an invalid deadline - deadline = 15 (should be <= 14)
        int result = service.saveTema("1", "Descriere Tema", 15, 14);
        assertEquals(1, result, "Expected saveTema to return 1 for an assignment with an invalid deadline (> 14)");
    }

    @Test
    void testSaveTema_invalidDeadlineNegative() {
        // Test case for an assignment with an invalid deadline - deadline = -1 (should be >= 1)
        int result = service.saveTema("1", "Descriere Tema", -1, 14);
        assertEquals(1, result, "Expected saveTema to return 0 for an assignment with an invalid deadline (negative)");
    }

    @Test
    void testSaveTema_invalidStartlineIsZero() {
        // Test case for an assignment with an invalid startline (e.g., negative)
        int result = service.saveTema("1", "Descriere Tema", 10, 0);
        assertEquals(1, result, "Expected saveTema to return 0 for an assignment with an invalid startline (< 1)");
    }

    @Test
    void testSaveTema_invalidStartlineAboveLimit() {
        //Test case for an assignment with an invalid startline (e.g., >= 1000)
        int result = service.saveTema("1", "Descriere Tema", 14, 15);
        assertEquals(1, result, "Expected saveTema to return 0 for an assignment with an invalid startline (> 14)");
    }

    @Test
    void testSaveTema_invalidStartlineNegative() {
        //Test case for an assignment with an invalid startline (e.g., >= 1000)
        int result = service.saveTema("1", "Descriere Tema", 10, -1);
        assertEquals(1, result, "Expected saveTema to return 0 for an assignment with an invalid startline (negative)");
    }

    @Test
    void testSaveTema_invalidStartlineMoreThanDeadline() {
        //Test case for an assignment with an invalid deadline and startline combination (startline > deadline)
        int result = service.saveTema("1", "Descriere Tema", 10, 11);
        assertEquals(1, result, "Expected saveTema to return 0 for an assignment with a startline bigger than the deadline");
    }
}