import domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.StudentXMLRepository;
import service.Service;
import validation.StudentValidator;
import validation.Validator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddStudentTests {

    private Service service;
    private StudentXMLRepository studentXmlRepo;
    private StudentValidator studentValidator;
    private String studentXMLFilePath;

    @BeforeEach
    void setUp() {
        studentXMLFilePath = "students-tests.xml";
        studentValidator = new StudentValidator();
        studentXmlRepo = new StudentXMLRepository(studentValidator, studentXMLFilePath);
        service = new Service(studentXmlRepo, null, null); // Only StudentXMLRepository is required for these tests
    }

    @Test
    void testSaveStudentSuccess() {
        // test case for a valid student
        Random rand = new Random(System.currentTimeMillis());
        int idRandom = rand.nextInt(1000);
        String id = Integer.toString(idRandom);
        String name = "John Doe";
        int group = 933;

        int result = service.saveStudent(id, name, group);

        assertEquals(0, result);
        Student savedStudent = studentXmlRepo.findOne(id);
        assertEquals(id, savedStudent.getID());
        assertEquals(name, savedStudent.getNume());
        assertEquals(group, savedStudent.getGrupa());
    }

    @Test
    void testSaveStudentDuplicate() {
        // Test case for trying to add an already existing student
        String id = "2";
        String name = "Jane Doe";
        int group = 933;

        service.saveStudent(id, name, group); // Save the student once

        int result = service.saveStudent(id, name, group); // Attempt to save the student again

        assertEquals(1, result);
    }

    @Test
    void testSaveStudent_emptyId() {
        // Test case for a student with an empty ID
        int result = service.saveStudent("", "John Doe", 101);
        assertEquals(1, result, "Expected saveStudent to return 0 for a student with an empty ID");
    }

    @Test
    void testSaveStudent_nullId() {
        // Test case for a student with a null ID
        int result = service.saveStudent(null, "John Doe", 101);
        assertEquals(1, result, "Expected saveStudent to return 0 for a student with a null ID");
    }

    @Test
    void testSaveStudent_emptyName() {
        // Test case for a student with an empty name
        int result = service.saveStudent("1", "", 101);
        assertEquals(1, result, "Expected saveStudent to return 0 for a student with an empty name");
    }

    @Test
    void testSaveStudent_nullName() {
        // Test case for a student with a null name
        int result = service.saveStudent("1", null, 101);
        assertEquals(1, result, "Expected saveStudent to return 0 for a student with a null name");
    }

    @Test
    void testSaveStudent_invalidGroup() {
        //Test case for a student with an invalid group number (e.g., negative)
        int result = service.saveStudent("1", "John Doe", -1);
        assertEquals(1, result, "Expected saveStudent to return 0 for a student with an invalid group number");
    }

    @Test
    void testSaveStudent_invalidGroupAbove() {
        //Test case for a student with an invalid group number (e.g., >= 1000)
        int result = service.saveStudent("1", "John Doe", 1000);
        assertEquals(1, result, "Expected saveStudent to return 0 for a student with an invalid group number");
    }
}