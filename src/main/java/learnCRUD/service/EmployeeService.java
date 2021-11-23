package learnCRUD.service;

import java.util.ArrayList;
import java.util.List;

import learnCRUD.model.Employee;

public class EmployeeService implements IEmployeeService {

	private List<Employee> listEmployees = new ArrayList<Employee>();

	@Override
	public Employee addEmp(Employee newEmp) {
		listEmployees.add(newEmp);
		return newEmp;
	}

	@Override
	public List<Employee> listOfEmp() {
		// TODO Auto-generated method stub
		return listEmployees;
	}

	@Override
	public Employee detailOfEmp(Integer empID) {
		// TODO Auto-generated method stub
		Employee findEmp = listEmployees.get(empID);
		return findEmp;
	}

	@Override
	public Employee updateEmp(Employee putEmp) {
		// TODO Auto-generated method stub
		Integer idEmp = putEmp.getEmpNo();
		
		listEmployees.set(idEmp , putEmp);
		return putEmp;
	}
	
	
	
	
}
