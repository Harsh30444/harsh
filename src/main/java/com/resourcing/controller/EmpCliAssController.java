/**
 * @author Nikhil Gundla
 *
 */

package com.resourcing.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.resourcing.beans.Client;
import com.resourcing.beans.Employee;
import com.resourcing.beans.EmployeeClientAssociation;
import com.resourcing.beans.JobDescription;
import com.resourcing.service.ClientService;
import com.resourcing.service.EmployeeClientAssociationService;
import com.resourcing.service.EmployeeService;
import com.resourcing.service.JobDescriptionService;

@Controller
@RequestMapping(value = "/eca")
public class EmpCliAssController {

	static Logger LOGGER = Logger.getLogger(ClientController.class);

	@Autowired
	ClientService clientService;

	@Autowired
	JobDescriptionService jobDescriptionService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeClientAssociationService employeeClientAssociationService;

	// Get The Entire List of Employee Client Associations
	@GetMapping("/ecaListAll")
	public String getJdList(Model model) {
		List<EmployeeClientAssociation> ecaList = employeeClientAssociationService.getAllEmployeeClientAssociations();
		model.addAttribute("ecaList", ecaList);
		return "ecaList";
	}

	// To Inactivate a Particular Emp- Client Asociation
	@GetMapping("/InactivateEca/{id}")
	public String inactivateJd(Model model, @PathVariable("id") int ecaId, EmployeeClientAssociation objEca) {
		LOGGER.debug("inside inactivateEca id:::" + ecaId);
		EmployeeClientAssociation eca = employeeClientAssociationService.findEcaById(ecaId);
		LOGGER.debug("inside inactivateEca id:::" + ecaId);
		if (eca.getIsActive() == 'N') {
			eca.setIsActive('Y');
			employeeClientAssociationService.updateEca(eca);
		} else if (eca.getIsActive() == 'Y') {
			eca.setIsActive('N');
			employeeClientAssociationService.updateEca(eca);
		}
		return "redirect:/eca/ecaListAll";
	}

	// Employee List For a Single Client Those who associated with Him.
	@GetMapping("/ecaListForSingleClient/{id}")
	public String getEmps(Model model, @PathVariable("id") int id) {
		Client objClient = clientService.getClientById(id);
		LOGGER.debug("The Client Id came to the Method is:::::::::::::" + id);
		List<EmployeeClientAssociation> ecaList = employeeClientAssociationService.findEmpListByClient(objClient);
		model.addAttribute("empList", ecaList);
		return "client_dashboard_employee_list";
	}

	// Client List For a Single Employee Those who assoicated with Him.
	@GetMapping("/ecaListForSingleEmployee/{id}")
	public String getEcaListForASingleEmployee(Model model, @PathVariable("id") int employeeId,
			@ModelAttribute("employee") Employee employee, EmployeeClientAssociation eca) {
		List<EmployeeClientAssociation> ClientListForEmployee = employeeClientAssociationService
				.findClientsByEmployeeId(employee);
		model.addAttribute("clientList", ClientListForEmployee);
		return "client-myClientList";
	}

	// To Get Total Client List with Job Descriptions Links to an Employee
	@GetMapping(value = "/clientListForEmployee")
	public String getClientListWithJDLink(Model model) {
		List<Client> clientList = clientService.getAllClients();
		model.addAttribute("clientList", clientList);
		return "clientListForEmployee";
	}

	// Total Active&Inactive Clients List for the Administrator(User)
	@RequestMapping(value = "/clientList", method = RequestMethod.GET)
	public String getClientList(Model model) {
		List<Client> clientList = clientService.getAllClients();
		model.addAttribute("clientList", clientList);
		return "clientListForUser";
	}

	// Active JobList To show to Employee
	@GetMapping("/JdList/{id}")
	public String getJdList(Model model, @PathVariable("id") int clientId, JobDescription jobDescription) {
		Client client = clientService.getClientById(clientId);
		List<JobDescription> jdList = jobDescriptionService.findAllActiveJobDescriptionByClient(client);
		model.addAttribute("jdList", jdList);
		return "client_dashboard_jd_list.html";
	}

}
