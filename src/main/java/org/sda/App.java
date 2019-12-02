package org.sda;

import org.sda.model.Employee;
import org.sda.model.GenderEnum;
import org.sda.model.Salary;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class App {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employees";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "admin";

    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);

//        String sql = "select * from departments";
//
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(sql);
//
//        while (resultSet.next()) {
//            String departmentNo = resultSet.getString("dept_no");
//            String departmentName = resultSet.getString("dept_name");
//
//            System.out.print(departmentNo);
//            System.out.print(" ");
//            System.out.println(departmentName);
//        }
//
//        resultSet.close();
//        statement.close();

//        sql = "insert into departments(dept_no, dept_name) values ('d010', 'Leadership')";
//
//        statement = connection.createStatement();
//
//        statement.executeUpdate(sql);
//        statement.close();

//        sql = "select * from departments where dept_no='d010'";
//        statement = connection.createStatement();
//        resultSet = statement.executeQuery(sql);
//
//        while (resultSet.next()) {
//            String departmentNo = resultSet.getString("dept_no");
//            String departmentName = resultSet.getString("dept_name");
//
//            System.out.println(departmentNo + " " + departmentName);
//        }

        getDepartmentByDepartmentNo("d010");
        List<Employee> employees = getEmployeesByGenderAndSalaryRange("F", 50000, 51000);
        printEmployees(employees);

        Employee employee = new Employee(11L, new Date(), "First Testing", "transaction", GenderEnum.F, new Date());
        Salary salary = new Salary(employee.getNumber(), 100000L, new Date(), new Date());

        saveEmployeeAndSalary(employee, salary);

        connection.close();
    }

    private static void getDepartmentByDepartmentNo(String departmentNo) throws SQLException {
        String sql = "select * from departments where dept_no=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, departmentNo);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String departNo = resultSet.getString("dept_no");
            String departmentName = resultSet.getString("dept_name");

            System.out.println(departNo + " " + departmentName);
        }

        resultSet.close();
        statement.close();

    }

    private static List<Employee> getEmployeesByGenderAndSalaryRange(String gender, int minSalary, int maxSalary) throws SQLException {
        String sql = "select e.* from employees as e inner join salaries as s on" +
                " e.emp_no = s.emp_no where gender = ? and salary between ? and ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, gender);
        statement.setInt(2, minSalary);
        statement.setInt(3, maxSalary);

        ResultSet resultSet = statement.executeQuery();

        List<Employee> employees = new ArrayList<>();

        while (resultSet.next()) {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
//            int salary = resultSet.getInt("salary");

//            System.out.println(firstName + " " + lastName + " " + salary);
            Date birthDate = resultSet.getDate("birth_date");
            Date hireDate = resultSet.getDate("hire_date");
            long employeeNo = resultSet.getLong("emp_no");
            String empGender = resultSet.getString("gender");

            Employee employee = new Employee();
            employee.setNumber(employeeNo);
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setBirthDate(birthDate);
            employee.setHireDate(hireDate);
            employee.setGender(GenderEnum.getGenderByValue(empGender));

            employees.add(employee);

        }

        resultSet.close();
        statement.close();

        return employees;
    }

    private static void printEmployees(List<Employee> employees) {
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    private static void saveEmployeeAndSalary(Employee employee, Salary salary) {
        PreparedStatement statement;
        try {
            connection.setAutoCommit(false);

            String insertEmployeeSql = "insert into employees(emp_no, birth_date, first_name, last_name, gender, hire_date) " +
                    "values(?,?,?,?,?,?)";

            statement = connection.prepareStatement(insertEmployeeSql);
            statement.setLong(1, employee.getNumber());
            statement.setDate(2, new java.sql.Date(employee.getBirthDate().getTime()));
            statement.setString(3, employee.getFirstName());
            statement.setString(4, employee.getLastName());
            statement.setString(5, employee.getGender().name());
            statement.setDate(6, new java.sql.Date(employee.getHireDate().getTime()));

            statement.executeUpdate();

            String insertSalarySql = "insert into salaries(emp_no, salary, from_date, to_date) " +
                    "values(?,?,?,?)";
            PreparedStatement salaryStatement = connection.prepareStatement(insertSalarySql);
            salaryStatement.setLong(1, salary.getEmployeeNumber());
            salaryStatement.setLong(2, salary.getSalary());
            salaryStatement.setDate(3, new java.sql.Date(salary.getFromDate().getTime()));
//            salaryStatement.setNull(4, Types.DATE);
            salaryStatement.setDate(4, new java.sql.Date(salary.getToDate().getTime()));

            salaryStatement.executeUpdate();

            connection.commit();

            statement.close();
            salaryStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
