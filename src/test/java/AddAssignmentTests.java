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
        // Test case for trying to add an already existing student
        String id = "2";
        String desc = "descriere tema";
        int startWeek = 5;
        int endWeek = 6;

        service.saveTema(id, desc, endWeek, startWeek); // Save the student once

        int result = service.saveTema(id, desc, endWeek, startWeek); // Attempt to save the student again

        assertEquals(1, result);
    }
}