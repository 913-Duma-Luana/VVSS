import domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.StudentXMLRepository;
import service.Service;
import validation.StudentValidator;
import validation.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddStudentTests {

    private Service service;
    private StudentXMLRepository studentXmlRepo;
    private StudentValidator studentValidator;

    @BeforeEach
    void setUp() {
        String studentXMLUrl = "E:\\UBB\\VVSS\\Project\\VVSS\\MaxPointsParticipants\\src\\test\\java\\test-assets\\students-tests.xml";
        studentValidator = new StudentValidator();
        studentXmlRepo = new StudentXMLRepository(studentValidator, studentXMLUrl);
        service = new Service(studentXmlRepo, null, null); // Only StudentXMLRepository is required for these tests
    }

    @Test
    void testSaveStudentSuccess() {
        String id = "1";
        String name = "John Doe";
        int group = 933;

        int result = service.saveStudent(id, name, group);

        assertEquals(1, result);
        Student savedStudent = studentXmlRepo.findOne(id);
        assertEquals(id, savedStudent.getID());
        assertEquals(name, savedStudent.getNume());
        assertEquals(group, savedStudent.getGrupa());
    }

    @Test
    void testSaveStudentDuplicate() {
        String id = "2";
        String name = "Jane Doe";
        int group = 933;

        service.saveStudent(id, name, group); // Save the student once

        int result = service.saveStudent(id, name, group); // Attempt to save the student again

        assertEquals(0, result);
    }
}