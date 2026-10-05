package com.erp.hr.service;

import com.erp.hr.dto.AttendanceRequest;
import com.erp.hr.dto.AttendanceResponse;
import com.erp.hr.entity.Attendance;
import com.erp.hr.entity.Employee;
import com.erp.hr.repository.AttendanceRepository;
import com.erp.hr.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            EmployeeRepository employeeRepository) {

        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public AttendanceResponse createAttendance(AttendanceRequest request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (attendanceRepository.existsByEmployeeIdAndAttendanceDate(
                request.getEmployeeId(),
                request.getAttendanceDate())) {

            throw new RuntimeException(
                    "Attendance already exists for this employee on this date"
            );
        }

        Attendance attendance = new Attendance();

        attendance.setEmployee(employee);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setStatus(request.getStatus());
        attendance.setRemarks(request.getRemarks());

        Attendance savedAttendance = attendanceRepository.save(attendance);

        return toResponse(savedAttendance);
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAllAttendance() {

        return attendanceRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AttendanceResponse getAttendanceById(Long id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        return toResponse(attendance);
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendanceByEmployee(Long employeeId) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found");
        }

        return attendanceRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendanceByDate(LocalDate date) {

        return attendanceRepository.findByAttendanceDate(date)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AttendanceResponse updateAttendance(
            Long id,
            AttendanceRequest request) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        boolean employeeChanged =
                !attendance.getEmployee().getId().equals(request.getEmployeeId());

        boolean dateChanged =
                !attendance.getAttendanceDate()
                        .equals(request.getAttendanceDate());

        if ((employeeChanged || dateChanged)
                && attendanceRepository.existsByEmployeeIdAndAttendanceDate(
                        request.getEmployeeId(),
                        request.getAttendanceDate())) {

            throw new RuntimeException(
                    "Attendance already exists for this employee on this date"
            );
        }

        attendance.setEmployee(employee);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setStatus(request.getStatus());
        attendance.setRemarks(request.getRemarks());

        Attendance updatedAttendance =
                attendanceRepository.save(attendance);

        return toResponse(updatedAttendance);
    }

    @Transactional
    public void deleteAttendance(Long id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        attendanceRepository.delete(attendance);
    }

    private AttendanceResponse toResponse(Attendance attendance) {

        Employee employee = attendance.getEmployee();

        String employeeName =
                employee.getFirstName() + " " + employee.getLastName();

        return new AttendanceResponse(
                attendance.getId(),
                employee.getId(),
                employee.getEmployeeCode(),
                employeeName,
                attendance.getAttendanceDate(),
                attendance.getStatus(),
                attendance.getRemarks()
        );
    }
}