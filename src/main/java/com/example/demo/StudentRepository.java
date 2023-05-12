package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true )
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s from Student s where s.email = ?1")
    Optional<Student> findStudentsByEmail(String email);

    @Query("SELECT s FROM Student  s where s.firstName= ?1 and s.age = ?2")
    List<Student> findStudentsByFirstNameEqualsAndAgeEquals(String firstName, Integer age);

    @Query("SELECT s from Student s where s.firstName = ?1 and s.age> ?2")
    List<Student> findStudentsByFirstNameEqualsAndAgeIsGreaterThan(String firstName, Integer age);

    @Query("SELECT s from Student s where s.firstName = ?1 and s.age >= ?2")
    List<Student> findStudentsByFirstNameEqualsAndAgeIsGreaterThanEqual(String firstName, Integer age);

    @Query("SELECT s from Student s where s.firstName = ?1 and s.age >= ?2")
    List<Student> selectStudentsByFirstNameEqualsAndAgeIsGreaterThanEqual(String firstName, Integer age);

    @Query(value = "SELECT * from student where first_name = ?1 and age >= ?2", nativeQuery = true)
    List<Student> selectStudentsByFirstNameEqualsAndAgeIsGreaterThanEqualNative(String firstName, Integer age);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * from student where first_name = :firstName and age >= :age",
            nativeQuery = true)
    List<Student> selectStudentsByFirstNameEqualsAndAgeIsGreaterThanEqualNativeNamedParameter(@Param("firstName") String firstName,@Param("age") Integer age);

    @Modifying
    @Transactional
    @Query("DELETE FROM Student u where u.id = ?1")
    int deleteStudentById(Long id);



}
