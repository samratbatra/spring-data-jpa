package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner1(
            StudentRepository studentRepository,
            StudentIDCardRepository studentIDCardRepository) {
        return args -> {
//            jpaBasicCRUDOperations(studentRepository);
//            generateRandomStudents(studentRepository);
//            sorting(studentRepository);
//            pagination(studentRepository);
//            oneToOneMappingDemo(studentRepository, studentIDCardRepository);
//            manyToOneMappingDemo(studentRepository);
//            manyToManyMappingDemo(studentRepository);

        };

    }

    private static void manyToManyMappingDemo(StudentRepository studentRepository) {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "." + lastName + "@gmail.com";
        Student student = new Student(firstName,
                lastName,
                email,
                faker.number().numberBetween(17, 55));

        student.addBook(new Book("Book1", LocalDateTime.now().minusDays(4)));
        student.addBook(new Book("Book2", LocalDateTime.now()));
        student.addBook(new Book("Book3", LocalDateTime.now().minusYears(1)));
        StudentIdCard studentIdCard = new StudentIdCard(
                "123456789",
                student);
        student.setStudentIdCard(studentIdCard);

//            student.enrollToCourses(new Course("Computer Science", "IT"));
//            student.enrollToCourses(new Course("AmigosCode SpringData JPA", "IT"));

        student.addEnrollment(
                new Enrollment(
                        new EnrollmentId(1L,1L),
                        student,
                        new Course("Computer Science", "IT"),LocalDateTime.now()));
        student.addEnrollment(
                new Enrollment(
                        new EnrollmentId(1L,2L),
                        student,
                        new Course("AmigosCode SpringData JPA", "IT"),LocalDateTime.now().minusDays(18) ));

        studentRepository.save(student);

        studentRepository.findById(1L).ifPresent(s -> {
            System.out.println("Fetch Lazy");
            List<Book> books = student.getBooks();
            books.forEach(book -> {
                System.out.println(s.getFirstName() + " borrowed " + book.getBookName());
            });
        });
    }

    private static void manyToOneMappingDemo(StudentRepository studentRepository) {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "." + lastName + "@gmail.com";
        Student student = new Student(firstName,
                lastName,
                email,
                faker.number().numberBetween(17, 55));

        student.addBook(new Book("Book1", LocalDateTime.now().minusDays(4)));
        student.addBook(new Book("Book2", LocalDateTime.now()));
        student.addBook(new Book("Book3", LocalDateTime.now().minusYears(1)));
        StudentIdCard studentIdCard = new StudentIdCard(
                "123456789",
                student);
        student.setStudentIdCard(studentIdCard);
        studentRepository.save(student);

        studentRepository.findById(1L).ifPresent(s -> {
            System.out.println("Fetch Lazy");
            List<Book> books = student.getBooks();
            books.forEach(book -> {
                System.out.println(s.getFirstName() + " borrowed " + book.getBookName());
            });
        });
    }

    private static void oneToOneMappingDemo(StudentRepository studentRepository, StudentIDCardRepository studentIDCardRepository) {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "." + lastName + "@gmail.com";

        Student student = new Student(firstName,
                lastName,
                email,
                faker.number().numberBetween(17, 55));
        StudentIdCard studentIdCard = new StudentIdCard(
                "123456789",
                student);
        studentIDCardRepository.save(studentIdCard);

        studentRepository.findById(1L).ifPresent(System.out::println);

        studentIDCardRepository.findById(1L)
                .ifPresent(System.out::println);

        studentRepository.deleteById(1L);
    }

    private static void pagination(StudentRepository studentRepository) {
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC,
                "firstName"));
        Page<Student> page = studentRepository.findAll(pageRequest);
        System.out.println(page);
    }

    private static void jpaBasicCRUDOperations(StudentRepository studentRepository) {
        Student maria = new Student("Maria", "Jones", "mariajonesgmail.com", 32);
        Student maria2 = new Student("Maria", "Jones2", "mariajonesgmail2.com", 18);
        Student maria3 = new Student("Maria", "Jones3", "mariajonesgmail3.com", 20);
        Student ahmed = new Student("Ahmed", "Ali", "ahmedali@gmail.com.com", 21);

        System.out.println("Adding Ahmed and Maria");
        studentRepository.saveAll(List.of(maria, ahmed, maria2, maria3));

        System.out.println("Number of students");
        System.out.println(studentRepository.count());

        studentRepository.findById(2L).ifPresentOrElse(System.out::println, () -> System.out.println("Student not found"));

        studentRepository.findById(3L).ifPresentOrElse(System.out::println, () -> System.out.println("Student not found"));

        System.out.println("Select all students");
        studentRepository.findAll().forEach(System.out::println);

        System.out.println("Delete maria");
        studentRepository.deleteById(1L);

        System.out.println("Number of students");
        System.out.println(studentRepository.count());

        //            Custom methods of JPARepository
        studentRepository.findStudentsByEmail("ahmedali@gmail.com.com")
                .ifPresentOrElse(System.out::println,
                        () -> System.out.println("Student not found"));

        studentRepository.findStudentsByFirstNameEqualsAndAgeEquals("Maria", 18)
                .forEach(System.out::println);

        studentRepository.findStudentsByFirstNameEqualsAndAgeIsGreaterThan
                ("Maria", 18).forEach(System.out::println);

        studentRepository.findStudentsByFirstNameEqualsAndAgeIsGreaterThanEqual
                ("Maria", 18).forEach(System.out::println);

        //            Query
        studentRepository.selectStudentsByFirstNameEqualsAndAgeIsGreaterThanEqual
                ("Maria", 18).forEach(System.out::println);

        //            NativeQuery
        studentRepository.selectStudentsByFirstNameEqualsAndAgeIsGreaterThanEqualNative
                ("Maria", 18).forEach(System.out::println);

//            Named Parameters
        studentRepository.selectStudentsByFirstNameEqualsAndAgeIsGreaterThanEqualNative
                ("Maria", 18).forEach(System.out::println);

//            Delete student
        System.out.println(studentRepository.deleteStudentById(3L));
    }


    private static void sorting(StudentRepository studentRepository) {
        Sort sort = Sort.by(Sort.Direction.DESC, "firstName");

        Sort sort2 = Sort.by("firstName").ascending()
                .and(Sort.by("age").descending());

        studentRepository.findAll(sort)
                .forEach(student -> System.out.println(student.getFirstName()));

        studentRepository.findAll(sort2)
                .forEach(student -> System.out.println(student.getFirstName() + " " + student.getAge()));
    }

    private static void generateRandomStudents(StudentRepository studentRepository) {
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = firstName + "." + lastName + "@gmail.com";
//                email = String.format("%s.%s@gmail.com",firstName,lastName);

            Student student = new Student(firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55));
            studentRepository.save(student);
        }
    }

}
