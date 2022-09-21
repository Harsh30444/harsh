package com.resourcing.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.resourcing.beans.Client;
import com.resourcing.beans.Department;
import com.resourcing.beans.Position;
import com.resourcing.service.ClientService;
import com.resourcing.service.DepartmentService;
import com.resourcing.service.PositionService;

@Controller
@RequestMapping("/dept")
public class DepartmentController {

	static Logger LOGGER = Logger.getLogger(CandidateController.class);
	
	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private PositionService positionService;

	@Autowired
	private ClientService clientService;

	@GetMapping("/addDept")
	public String getAddDeptPage(Model model, Department dept, HttpSession session) {
		model.addAttribute("objDept", dept);		
		return "client_add_new_department";
	}

	@PostMapping("/saveDept")
	public String saveDept(Model model, @ModelAttribute("objDept") Department dept, Position post,
			HttpSession session) {
		Department department = departmentService.findByDepartmentName(dept.getDeptName());
		if (department == null) {
			LOGGER.debug("Inside Save A Department Page::::::" + dept);
			dept.setDeptName(dept.getDeptName().toUpperCase());
			departmentService.addDepartment(dept);
			List<Department> deptList = departmentService.getAllDepartment();
			model.addAttribute("deptList", deptList);
			model.addAttribute("sucessMessage", dept.getDeptName() + " is added in Department List");
			return "client_dashboard_dept_list";
		} else {
			LOGGER.debug("else::::::::");
			model.addAttribute("objDept", dept);
			model.addAttribute("sucessMessage", dept.getDeptName() + " is Already added in Department");
			return "client_add_new_department";
		}
	}

	@GetMapping("/addDeptAndPost/{id}")
	public String GetCreateDeptPagenow(Model model, @PathVariable("id") int clientId, Position post, Department dept,
			Client client, HttpSession session) {
		// Client objClient = clientService.getClientById(clientId);
		// session.setAttribute("clientDetails", objClient);
		// session.getAttribute("")
		List<Position> pstnList = positionService.getAllPositions();
		model.addAttribute("pstnList", pstnList);
		model.addAttribute("post", post);
		Client sessionClient = (Client) session.getAttribute("clientDetails");
		model.addAttribute("clientDetails", sessionClient);
		model.addAttribute("editClientDetails", sessionClient);
		LOGGER.debug("::::::::::::::::::::::::" + sessionClient);
		return "client_add_new_department";
	}

	@GetMapping("/deptList")
	public String getDeptList(Model model, Department dept, HttpSession session) {
		List<Department> deptList = departmentService.getAllDepartment();
		model.addAttribute("deptList", deptList);
		return "client_dashboard_dept_list";
	}

	@GetMapping("/positionList")
	public String positionList(Model model) {
		List<Position> positionList = positionService.getAllPositions();
		model.addAttribute("positionList", positionList);
		return "client_dashboard_add_new";
	}
	
	@GetMapping("/positionList/{departmentId}")
	public String positionListByDepartmentId(@PathVariable int departmentId,Model model) {
		LOGGER.debug("invoked::::");
		List<Position> positionList = positionService.getAllPositions();
		List<Position> positions = positionList.stream().filter(dept -> dept.getDeptId()==departmentId).collect(Collectors.toList());
		LOGGER.debug("count:::::"+positions.size());
		model.addAttribute("positionList", positions);
		return "client_dashboard_add_new";
	}
	
	
	@GetMapping("/addNewPost/{id}")
	public String getAddPostPage(Model model, @PathVariable("id") int deptId, Department dept, Position post,
			HttpSession session) {
		Department objDept = departmentService.getDepartmentById(deptId);
		model.addAttribute("objDept", objDept);
		model.addAttribute("post", post);
		model.addAttribute("deptName", objDept.getDeptName());
		List<Position> pstnList = positionService.getAllPositions();
		LOGGER.debug(pstnList);
		// model.addAttribute("dept", dept);
		return "client_dashboard_add_new_post";
	}

	@PostMapping("/savePost/{id}")
	public String saveNewDeptPost(Model model, @ModelAttribute("post") Position post,
			@PathVariable int id,HttpSession session) {
		LOGGER.debug("before ifffffffffffff");
		LOGGER.debug("position name:::"+post.getPostName());
		LOGGER.debug("dept name::"+post.getDeptName());
		//positionService.findByPostionIgnoreCaseAndDepartmentIgnoreCase(post.getDeptName(), post.getPostName());
		if(positionService.findByPostionIgnoreCaseAndDepartmentIgnoreCase(post.getDeptName(), post.getPostName()) == null) {
		LOGGER.debug("iffffffffffff");
		model.addAttribute("post", post);
		LOGGER.debug("Inside Add Save A Post Page::::::" + post);
		post.setIsActive('Y');
		post.setPostName(post.getPostName().toUpperCase());
		positionService.addPosition(post);
		List<Position> positionList = positionService.getAllPositions();
		List<Position> positions = positionList.stream().filter(dept -> dept.getDeptId()==id).collect(Collectors.toList());
		model.addAttribute("positionList", positions);
		Client sessionClient = (Client) session.getAttribute("clientDetails");
		model.addAttribute("clientDetails", sessionClient);
		model.addAttribute("editClientDetails", sessionClient);
		return "client_dashboard_add_new";
	}
		else {
			LOGGER.debug("else::::::::::");
			Department objDept = departmentService.getDepartmentById(id);
			model.addAttribute("objDept", objDept);
			model.addAttribute("post", post);
			Client sessionClient = (Client) session.getAttribute("clientDetails");
			model.addAttribute("clientDetails", sessionClient);
			model.addAttribute("editClientDetails", sessionClient);
			LOGGER.debug("::::::::::::::::::::::::" + sessionClient);
			model.addAttribute("deptName", objDept.getDeptName());
			model.addAttribute("message", post.getPostName()+" is already added to "+post.getDeptName());
			return "client_dashboard_add_new_post";
		}
	}

	@GetMapping("/InactivatePost/{id}")
	public String inactivatePost(Model model, @PathVariable("id") int postId, HttpSession session) {
		LOGGER.debug("inside inactivateJd id:::" + postId);
		Position post = positionService.getPositionById(postId);
		if (post.getIsActive() == 'N') {
			post.setIsActive('Y');
			positionService.updatePosition(post);
		} else if (post.getIsActive() == 'Y') {
			post.setIsActive('N');
			positionService.updatePosition(post);
		}
		List<Position> pstnList = positionService.getAllPositions();
		Client sessionClient = (Client) session.getAttribute("clientDetails");
		model.addAttribute("clientDetails", sessionClient);
		model.addAttribute("editClientDetails", sessionClient);
		model.addAttribute("pstnList", pstnList);
		return "client_dashboard_add_new";
	}

}
