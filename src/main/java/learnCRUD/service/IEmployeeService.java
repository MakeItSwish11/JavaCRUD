package learnCRUD.service;

import java.util.List;

import learnCRUD.model.Employee;

public interface IEmployeeService {
	
	Employee addEmp(Employee newEmp);
	List<Employee> listOfEmp();
	Employee detailOfEmp (Integer empID);
	Employee updateEmp (Employee putEmp);
}
