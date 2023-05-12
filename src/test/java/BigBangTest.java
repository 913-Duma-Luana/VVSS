import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.Validator;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BigBangTest {

    private Service service;
    private StudentXMLRepository studentXmlRepo;
    private TemaXMLRepository temaXmlRepo;
    private NotaXMLRepository notaXmlRepo;
    private Validator<Student> studentValidator;
    private Validator<Tema> temaValidator;
    private Validator<Nota> notaValidator;
    private String temaXMLFilePath;
    private String studentXMLFilePath;
    private String noteXMLFilePath;
    private String lastAssignmentId;
    private String lastStudentId;
    private String lastGradeId;
    private Random rand;

    @BeforeEach
    void setUp() {
        rand = new Random(System.currentTimeMillis());
        studentXMLFilePath = "students-tests.xml";
        temaXMLFilePath = "tema-tests.xml";
        noteXMLFilePath = "note-tests.xml";

        studentValidator = new StudentValidator();
        temaValidator = new TemaValidator();
        notaValidator = new NotaValidator();

        temaXmlRepo = new TemaXMLRepository(temaValidator, temaXMLFilePath);
        studentXmlRepo = new StudentXMLRepository(studentValidator, studentXMLFilePath);
        notaXmlRepo = new NotaXMLRepository(notaValidator, noteXMLFilePath);

        service = new Service(studentXmlRepo, temaXmlRepo, notaXmlRepo);

        lastStudentId = Integer.toString(rand.nextInt(1000));
        lastAssignmentId = Integer.toString(rand.nextInt(1000));
    }

    @Test
    void testBigBang() {
        // test case for a valid student
        String id = lastStudentId;
        String name = "John Doe";
        int group = 933;

        int result = service.saveStudent(id, name, group);

        assertEquals(0, result);
        Student savedStudent = studentXmlRepo.findOne(id);
        assertEquals(id, savedStudent.getID());
        assertEquals(name, savedStudent.getNume());
        assertEquals(group, savedStudent.getGrupa());


        String idd = lastAssignmentId;
        String desc = "descriere tema";
        int startWeek = 1;
        int endWeek = 2;

        result = service.saveTema(idd, desc, endWeek, startWeek);

        assertEquals(0, result);
        Tema savedTema = temaXmlRepo.findOne(idd);
        assertEquals(idd, savedTema.getID());
        assertEquals(desc, savedTema.getDescriere());
        assertEquals(startWeek, savedTema.getStartline());
        assertEquals(endWeek, savedTema.getDeadline());


        double valNota = rand.nextInt()%10 + rand.nextDouble();
        if (valNota < 5) valNota += 5;
        int deliveryWeek = savedTema.getDeadline();

        String sid = lastStudentId;
        String aid = lastAssignmentId;
        result = service.saveNota(sid, aid, valNota, deliveryWeek, "feedback");

        assertEquals(0, result);
        Nota savedNota = notaXmlRepo.findOne(new Pair(lastStudentId, lastAssignmentId));
        assertEquals(sid, savedNota.getID().getObject1());
        assertEquals(aid, savedNota.getID().getObject2());
        assertEquals("feedback", savedNota.getFeedback());
        assertEquals(deliveryWeek, savedNota.getSaptamanaPredare());
        assertEquals(valNota, savedNota.getNota());
    }

    public void testSaveNotaSuccess() {
        // test case for a valid Nota
        double valNota = rand.nextInt()%10 + rand.nextDouble();
        int deliveryWeek = rand.nextInt()%14 + 1;

//        String sid = Integer.toString(lastStudentId);
//        String aid = Integer.toString(lastAssignmentId);
        String sid = lastStudentId;
        String aid = lastAssignmentId;
        int result = service.saveNota(sid, aid, valNota, deliveryWeek, "feedback");

        assertEquals(0, result);
        Nota savedNota = notaXmlRepo.findOne(new Pair(lastStudentId, lastAssignmentId));
        assertEquals(sid, savedNota.getID().getObject1());
        assertEquals(aid, savedNota.getID().getObject2());
        assertEquals("feedback", savedNota.getFeedback());
        assertEquals(deliveryWeek, savedNota.getSaptamanaPredare());
        assertEquals(valNota, savedNota.getNota());
    }
}
