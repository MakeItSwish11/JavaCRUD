package learnCRUD.midas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import learnCRUD.model.Employee;
import learnCRUD.service.EmployeeService;

@WebServlet("/employee")
public class EmployeeMIDAS extends HttpServlet {

	private static final long serialVersionUID = 1L;// Dibungkus jadi binary

	private Gson gson = new Gson();// declare class awal json atau inisiasi objt
	private GsonBuilder gsonBuilder = new GsonBuilder();// inisiasi
	private static final String EMP_NAME = "nameEmp";
	private static final String EMP_POSITION = "empPos";

	private EmployeeService empService;

	public EmployeeMIDAS() {
		super();
		empService = new EmployeeService();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idx = null;
		idx = request.getParameter("idx");
		PrintWriter out = response.getWriter();
		if (idx == null) {
			List<Employee> empList = empService.listOfEmp();
			Gson gson = gsonBuilder.create();
			String listingCpy = gson.toJson(empList);
			response.setContentType("application/json");
			out.print(listingCpy);
			out.flush();
		} else {
			Integer findIndex = Integer.parseInt(idx);
			Employee empDtl = empService.detailOfEmp(findIndex);
			String dtlEmp = this.gson.toJson(empDtl);
			JSONObject obj = new JSONObject(dtlEmp);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(obj);
			out.flush();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Integer id = Integer.parseInt(request.getParameter("id"));
		String empName = request.getParameter("nameEmp");
		String empPos = request.getParameter("empPos");

		Employee emp = new Employee();
		emp.setEmpNo(id);
		emp.setPosition(empPos);
		emp.setEmpName(empName);
		empService.addEmp(emp);

		if (empService != null) {
			out.println("Success Create New Employee");
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Integer idNew = Integer.parseInt(request.getParameter("id"));
		Employee emp;
		String result = new BufferedReader(new InputStreamReader(request.getInputStream())).lines().parallel()
				.collect(Collectors.joining("\n"));

		StringBuffer strBuffer = new StringBuffer(result);

		// split by new line, trim and filter empty line
		List<String> lines = Arrays.stream(result.split("\\n")).map(x -> x.trim()).collect(Collectors.toList());

		List<Employee> empServ = empService.listOfEmp();
		Optional<Employee> optEmp = empServ.stream().filter(employee -> employee.getEmpNo().compareTo(idNew) == 0)
				.findFirst();
		if (optEmp.isPresent()) {
			emp = optEmp.get();

			String key = "";
			for (String line : lines) {
				Pattern pattern = Pattern.compile("[^A-Za-z]");
				Matcher match = pattern.matcher(line);
				boolean val = match.find();

				if (line.indexOf("=") > 0) {
					String[] temp = line.split("=" + "", line.length() - 1);
					System.out.println(temp.length);
					System.out.println(temp[1].substring(1, temp[1].length() - 1));
					key = temp[1].substring(1, temp[1].length() - 1);
				}

				if (!val) {
					if (line.length() > 0 && key.equals(EMP_NAME)) {
						emp.setEmpName(line.trim());
						key = "";
					} else if (line.length() > 0 && key.equals(EMP_POSITION)) {
						emp.setPosition(line.trim());
						key = "";
					}
				}
			}

			empService.updateEmp(emp);
		}

	}

	private static int retrieveUserid(HttpServletRequest req) {
		String pathInfo = req.getPathInfo();
		if (pathInfo.startsWith("/")) {
			pathInfo = pathInfo.substring(1);
		}
		return Integer.parseInt(pathInfo);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

	protected void doDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		

	}
}
