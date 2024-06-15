package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        String email=student.getEmail();
        if(studentRepository.findStudentByEmail(email).isPresent()){
            throw new IllegalStateException("email taken");
        }
        else
        {
            studentRepository.save(student);
        }

    }

    public void deleteStudent(Long id)
    {
        boolean exists=studentRepository.existsById(id);
        if (!exists)
        {
            throw new IllegalStateException("Student with id "+id+" does not exist");
        }
        studentRepository.deleteById(id);

    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email)
    {
        Student student=studentRepository.findById(studentId).orElseThrow(()->new IllegalStateException("student with id: "+studentId+" does not exist"));
        if (name!=null && name.length()>0
        && !Objects.equals(name,student.getName()))
        {
            student.setName(name);
        }
        //the same for email
        if (email!=null && email.length()>0
            && !Objects.equals(email,student.getEmail()))
        {
            //check if email is taken
            if(studentRepository.findStudentByEmail(email).isPresent())
            {
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }

    }


}
