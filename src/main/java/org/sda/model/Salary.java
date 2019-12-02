package org.sda.model;

import java.util.Date;

public class Salary {
    private Long employeeNumber;
    private Long salary;
    private Date fromDate;
    private Date toDate;

    public Salary() {
    }

    public Salary(Long employeeNumber, Long salary, Date fromDate, Date toDate) {
        this.employeeNumber = employeeNumber;
        this.salary = salary;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
