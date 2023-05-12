import domain.Student;
import domain.Tema;
import org.junit.Before;
import org.junit.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IntegrationTest {
    private Service service;
    private StudentXMLRepository studentMockRepo;
    private TemaXMLRepository assignmentMockRepo;
    private Service mockService;

    @Before
    public void setUp() {
        // Normal declarations
        TemaXMLRepository assignmentRepository = new TemaXMLRepository(new TemaValidator(), "tema-tests.xml");
        StudentXMLRepository studentRepository = new StudentXMLRepository(new StudentValidator(), "students-tests.xml");
        NotaXMLRepository gradeRepository = new NotaXMLRepository(new NotaValidator(), "note-tests.xml");
        service = new Service(studentRepository, assignmentRepository, gradeRepository);
        
        // Mock Student Repo
        studentMockRepo = mock();
        when(studentMockRepo.save(new Student("1", "Luana Duca", 933))).thenReturn(null);
        when(studentMockRepo.findOne("1")).thenReturn(new Student("1", "Luana Duca", 933));

        // Mock Nota Repo
        assignmentMockRepo = mock();
        when(assignmentMockRepo.save(new Tema("id", "description", 2, 1))).thenReturn(null);
        when(assignmentMockRepo.findOne("id")).thenReturn(new Tema("id", "description", 2, 1));

        // Mock Service
        mockService = new Service(studentMockRepo, assignmentMockRepo, gradeRepository);

    }

    @Test
    public void testAddStudentSuccess() {
        service.saveStudent("1", "Luana Duca", 933);
        var students = service.findAllStudents();
        assert students.iterator().hasNext();
    }

    @Test
    public void testAddTemaSuccess() {
        service.saveTema("idTema", "description", 2, 1);
        var assignments = service.findAllTeme();
        assert assignments.iterator().hasNext();
    }

    @Test
    public void testAddNotaSuccess() {
        int retval = service.saveNota("1", "id", 9.00, 2, "feedback");
        assert retval == -1;
    }

    @Test
    public void integrationTest() {
        // Save student
        service.saveStudent("1", "Luana Duca", 933);
        var students = service.findAllStudents();
        assert students.iterator().hasNext();

        // Save tema
        service.saveTema("idTema", "description", 2, 1);
        var assignments = service.findAllTeme();
        assert assignments.iterator().hasNext();

        // Save nota
        int return_value = service.saveNota("1", "idTema", 9.00, 2, "feedback");
        assert return_value != -1;

    }
    @Test
    public void testAddStudentMock() {
        assert mockService.saveStudent("1", "Luana Duca", 933) == 1;
        assert studentMockRepo.findOne("1").equals(new Student("1", "Luana Duca", 933));
    }

    @Test
    public void testAddTemaMock() {
        assert mockService.saveTema("id", "description", 2, 1) == 1;
        assert assignmentMockRepo.findOne("id").equals(new Tema("id", "description", 2, 1));
    }

    @Test
    public void integrationTestAddStudentAndTema() {
        // Add student
        service.saveStudent("1", "gigel", 933);
        var students = service.findAllStudents();
        assert students.iterator().hasNext();

        // Add tema
        service.saveTema("idTema", "description", 2, 1);
        var assignments = service.findAllTeme();
        assert assignments.iterator().hasNext();
    }

    @Test
    public void integrationTestAddStudentAndTemaMock() {
        // Add student mock
        assert mockService.saveStudent("1", "Luana Duca", 933) == 1;
        assert studentMockRepo.findOne("1").equals(new Student("1", "Luana Duca", 933));

        // Add tema mock
        assert mockService.saveTema("id", "description", 2, 1) == 1;
        assert assignmentMockRepo.findOne("id").equals(new Tema("id", "description", 2, 1));
    }

    @Test
    public void integrationTestAddGradeMock() {
        // Add student mock
        mockService.saveStudent("1", "Luana Duca", 933);
        assert studentMockRepo.findOne("1").equals(new Student("1", "Luana Duca", 933));

        // Add tema mock
        mockService.saveTema("id", "description", 2, 1);
        assert assignmentMockRepo.findOne("id").equals(new Tema("id", "description", 2, 1));

        // Add nota
        int returnValue = mockService.saveNota("1", "id", 9.00, 2, "feedback");
        assert returnValue != -1;
    }
}