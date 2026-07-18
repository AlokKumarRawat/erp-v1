package com.example.sms;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.security.auth.Subject;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class NewControllers {

    UsersRepository repo;
    StudentRepository studentRepository;
    TeacherRepository teacherRepository;
    ScheduleRepository scheduleRepository;
    MarksRepository marksRepository;
    AttendanceRepository attendanceRepository;

    NewControllers(UsersRepository repo, StudentRepository studentRepository, AttendanceRepository attendanceRepository , MarksRepository marksRepository, TeacherRepository teacherRepository, ScheduleRepository scheduleRepository) {
        this.repo = repo;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.scheduleRepository = scheduleRepository;
        this.marksRepository = marksRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }


    @GetMapping("/addS")
    public String addStudent(HttpSession session) {
        Users user = (Users) session.getAttribute("loggedUser");

        if (user == null || !user.getRole().equals("Admin")) {
            return "redirect:/login";
        }
        return "addStudent";
    }


    @GetMapping("/addT")
    public String addTeacher(HttpSession session) {
        Users user = (Users) session.getAttribute("loggedUser");

        if (user == null || !user.getRole().equals("Admin")) {
            return "redirect:/login";
        }
        return "addTeacher";
    }

    @GetMapping("/sch")
    public String View(HttpSession session) {
        Users user = (Users) session.getAttribute("loggedUser");

        if (user == null || !user.getRole().equals("Admin")) {
            return "redirect:/login";
        }
        return "Schedule";
    }


    @GetMapping("/Admin")
    public String Admin(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedUser");

        if (user == null || !user.getRole().equals("Admin")) {
            return "redirect:/login";
        }

        System.out.println("Dashboard");
        List<Schedule> schedules = scheduleRepository.findAll();
        model.addAttribute("schedules", schedules);
        List<Student> allStudents = studentRepository.findAll();
        model.addAttribute("allStudents", allStudents);
        List<Teacher> allTeachers = teacherRepository.findAll();
        model.addAttribute("allTeachers", allTeachers);

        model.addAttribute("studentCount", allStudents.size());
        model.addAttribute("teacherCount", allTeachers.size());
        return "Admin";
    }


    @GetMapping("/login")
    public String Login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String userid,
            @RequestParam String password,
            @RequestParam String role,
            Model model,
            HttpSession session
    ) {

        Users user = repo.findByUseridAndPasswordAndRole(userid, password, role);

        if (user != null) {

            session.setAttribute("loggedUser", user);
            if (role.equals("Student"))
                return "Student";

            if (role.equals("Teacher"))
                return "Teacher";

            if (role.equals("Admin"))
                return "redirect:/Admin";

        }
        model.addAttribute("error", "Invalid User ID, Password or Role");
        return "login";
    }

    @GetMapping("/dashboard")
    public String showSchedule(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedUser");

        if (user == null || !user.getRole().equals("Admin")) {
            return "redirect:/login";
        }
        System.out.println("Dashboard");
        List<Schedule> schedules = scheduleRepository.findAll();
        model.addAttribute("schedules", schedules);
        List<Student> allStudents = studentRepository.findAll();
        model.addAttribute("allStudents", allStudents);
        List<Teacher> allTeachers = teacherRepository.findAll();
        model.addAttribute("allTeachers", allTeachers);

        model.addAttribute("studentCount", allStudents.size());
        model.addAttribute("teacherCount", allTeachers.size());

        return "Admin";

    }

    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        studentRepository.save(student);

        Users user = new Users();
        user.setUserid(student.getUname());
        user.setPassword(student.getPassword());
        user.setRole("Student");

        repo.save(user);
        redirectAttributes.addFlashAttribute("message", "Student Record Added");
        return "redirect:/dashboard";
    }

    @PostMapping("/addTeacher")
    public String addTeacher(@ModelAttribute Teacher teacher, RedirectAttributes redirectAttributes) {
        teacherRepository.save(teacher);
        Users user = new Users();
        user.setUserid(teacher.getUserid());
        user.setPassword(teacher.getPassword());
        user.setRole("Teacher");

        repo.save(user);
        redirectAttributes.addFlashAttribute("message", "Teacher Record Added");
        return "redirect:/dashboard";
    }


    @PostMapping("/addSchedule")
    public String addSchedule(@ModelAttribute Schedule schedule, RedirectAttributes redirectAttributes) {
        scheduleRepository.save(schedule);
        redirectAttributes.addFlashAttribute("message", "Teacher Record Added");
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


    @GetMapping("/dash")
    public String tdashboard(Model model, HttpSession session) {

        Users user = (Users) session.getAttribute("loggedUser");

        if (user == null || !"Teacher".equals(user.getRole())) {
            return "redirect:/login";
        }

        List<Student> students = studentRepository.findAll();

        model.addAttribute("students", students);

        return "Teacher";
    }

    @GetMapping("/result")
    public String result(Model model, HttpSession session) {

        Users user = (Users) session.getAttribute("loggedUser");

        if (user == null || !"Teacher".equals(user.getRole())) {
            return "redirect:/login";
        }

        List<Student> allStudents = studentRepository.findAll();

        model.addAttribute("allStudents", allStudents);

        System.out.println(allStudents.size());

        model.addAttribute("allStudents", allStudents);

        return "result";
    }
    @PostMapping("/marks/save")
    public String saveMarks(@ModelAttribute com.example.sms.dto.MarksDTO form) {

        marksRepository.saveAll(form.getMarksList());

        return "redirect:/result";
    }
    @GetMapping("/student/results")
    public String results(
            HttpSession session,
            Model model) {

        Users user =
                (Users) session.getAttribute("loggedUser");

        List<Marks> marks =
                marksRepository.findByStudentId(
                        user.getId());

        model.addAttribute("marks", marks);

        return "studentResult";
    }
    @GetMapping("/attendance")
    public String attendancePage(Model model, HttpSession session) {

        Users user = (Users) session.getAttribute("loggedUser");

        if (user == null || !user.getRole().equals("Teacher")) {
            return "redirect:/login";
        }

        List<Student> allStudents = studentRepository.findAll();

        System.out.println(allStudents.size());

        model.addAttribute("allStudents", allStudents);

        return "Attendance";
    }
    @PostMapping("/attendance/save")
    public String saveAttendance(@ModelAttribute com.example.sms.dto.AttendanceDTO dto) {

        for (Attendance a : dto.getAttendanceList()) {
            a.setDate(LocalDate.now());
        }

        attendanceRepository.saveAll(dto.getAttendanceList());

        return "redirect:/dash";
    }
    @GetMapping("/student/dashboard")
    public String dashboard(Model model, @SessionAttribute("studentId") Long studentId) {

        List<Marks> marks = marksRepository.findByStudentId(studentId);
        List<Attendance> attendance = attendanceRepository.findByStudentId(studentId);

        model.addAttribute("marks", marks);
        model.addAttribute("attendance", attendance);

        return "student-dashboard";
    }
}


