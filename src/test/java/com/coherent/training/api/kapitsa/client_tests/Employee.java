package com.coherent.training.api.kapitsa.client_tests;

import lombok.Getter;

@Getter
public class Employee {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private int employeeId;
    private String address;

    private Employee(EmployeeBuilder eb) {
        this.firstName = eb.firstName;
        this.lastName = eb.lastName;
        this.dateOfBirth = eb.dateOfBirth;
        this.employeeId = eb.employeeId;
        this.address = eb.address;
    }

    public static class EmployeeBuilder{
        private String firstName;
        private String lastName;
        private String dateOfBirth;
        private int employeeId;
        private String address;

        public EmployeeBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return new EmployeeBuilder();
        }

        public EmployeeBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return new EmployeeBuilder();
        }

        public EmployeeBuilder setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return new EmployeeBuilder();
        }

        public EmployeeBuilder setEmployeeId(int employeeId) {
            this.employeeId = employeeId;
            return new EmployeeBuilder();
        }

        public EmployeeBuilder setAddress(String address) {
            this.address = address;
            return new EmployeeBuilder();
        }

        public Employee build() {
            return new Employee(this);
        }
    }
}
