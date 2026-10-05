package com.erp.hr.service;

import com.erp.hr.dto.LeaveRequest;
import com.erp.hr.dto.LeaveResponse;
import com.erp.hr.entity.Employee;
import com.erp.hr.entity.Leave;
import com.erp.hr.repository.EmployeeRepository;
import com.erp.hr.repository.LeaveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveService(
            LeaveRepository leaveRepository,
            EmployeeRepository employeeRepository) {

        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public LeaveResponse createLeave(LeaveRequest request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        validateDates(request);

        Leave leave = new Leave();

        leave.setEmployee(employee);
        leave.setLeaveType(request.getLeaveType());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setReason(request.getReason());
        leave.setStatus(request.getStatus());

        Leave savedLeave = leaveRepository.save(leave);

        return toResponse(savedLeave);
    }

    @Transactional(readOnly = true)
    public List<LeaveResponse> getAllLeaves() {

        return leaveRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LeaveResponse getLeaveById(Long id) {

        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        return toResponse(leave);
    }

    @Transactional(readOnly = true)
    public List<LeaveResponse> getLeavesByEmployee(Long employeeId) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found");
        }

        return leaveRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LeaveResponse> getLeavesByStatus(String status) {

        return leaveRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public LeaveResponse updateLeave(Long id, LeaveRequest request) {

        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        validateDates(request);

        leave.setEmployee(employee);
        leave.setLeaveType(request.getLeaveType());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setReason(request.getReason());
        leave.setStatus(request.getStatus());

        Leave updatedLeave = leaveRepository.save(leave);

        return toResponse(updatedLeave);
    }

    @Transactional
    public void deleteLeave(Long id) {

        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leaveRepository.delete(leave);
    }

    private void validateDates(LeaveRequest request) {

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException(
                    "End date cannot be before start date"
            );
        }
    }

    private LeaveResponse toResponse(Leave leave) {

        Employee employee = leave.getEmployee();

        String employeeName =
                employee.getFirstName() + " " + employee.getLastName();

        return new LeaveResponse(
                leave.getId(),
                employee.getId(),
                employee.getEmployeeCode(),
                employeeName,
                leave.getLeaveType(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getReason(),
                leave.getStatus()
        );
    }
}