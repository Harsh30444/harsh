function getSubCategories(hospitalBranchId){
var appname=window.location.pathname.substr(0, window.location.pathname.lastIndexOf('/uploadDoctorProfile'));
document.getElementById('objdoctor').action = appname+"/uploadDoctorProfile?getQry=getSubsCatagory&hospitalBranchId="+hospitalBranchId;

document.getElementById('objdoctor').submit();
}

function selectBranch(serviceName){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectBranch'));
	/*var doctorId=document.getElementById('doctorId').value;*/
	document.getElementById('doc').action=appname+"/selectBranch?getQry=selectBranch&serviceName="+serviceName;
	document.getElementById('doc').submit();
}

function selectedService(serviceName){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectedService'));
	document.getElementById('doc').action=appname+"/selectedService?getQry=selectedService&serviceName="+serviceName;
	document.getElementById('doc').submit();
}

function selectedBranches(branchId){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectedBranches'));
	document.getElementById('doc').action=appname+"/selectedBranches?getQry=selectedService&branchId="+branchId;
	document.getElementById('doc').submit();
}  
function selectedBranchAdmin(branchId){
	alert("hello");
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/Appointment'));
	document.getElementById('AdminAppointment').action=appname+"Appointment?getQry=selectedBranchAdmin&hospitalBranchId="+branchId;
	document.getElementById('AdminAppointment').submit();
}  

function selectedDoctors(doctorId){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectedDoctors'));
	var hospitalBranchId=document.getElementById('BranchId').value;
	/*var patientId=document.getElementById('patient').value;*/
	document.getElementById('doc').action=appname+"/selectedDoctors?getQry=selectedDoctors&hospitalBranchId="+hospitalBranchId+"&doctorId="+doctorId;
	document.getElementById('doc').submit();
}
function DropDoctors(doctorId){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectedDoctors'));
	var hospitalBranchId=document.getElementById('BranchId').value;
	/*var patientId=document.getElementById('patient').value;*/
	document.getElementById('doc').action=appname+"/selectedDoctors?getQry=selectedDoctors&hospitalBranchId="+hospitalBranchId+"&doctorId="+doctorId;
	document.getElementById('doc').submit();
}


function selectService(serviceName){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectDoc'));
	/*var doctorId=document.getElementById('doctorId').value;*/
	document.getElementById('appointmentObj').action=appname+"/selectDoc?getQry=selectService&serviceName="+serviceName;
	document.getElementById('appointmentObj').submit();
}

function doctorAvailable(interviewerId){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectDoc'));
	/*var doctorId=document.getElementById('doctorId').value;*/
	document.getElementById('appointmentObj').action=appname+"/selectDoc?getQryDoc=doctorAvailable&doctorId="+doctorId;
	document.getElementById('appointmentObj').submit();
}

function dateAvailable(date){
var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/ihgufyd'));
var interviewerId=document.getElementById('interviewerId').value;
var hospitalBranchId=document.getElementById('hospitalBranch').value;
document.getElementById('appointmentObj').action=appname+"/selectDoc?getQryDate=dateAvailable&Date="+date+"&doctorId="+interviewerId+"&hospitalBranchId="+hospitalBranchId;
document.getElementById('appointmentObj').submit();
}


function offlineappointmentform(){
var doctorId=document.getElementById('doctor').value;
var pid=document.getElementById('patient').value;
document.appointment.submit();

var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/ihgufyd'));
}

function handler(e){
 
  var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/ihgufyd'));
  var hospitalName=document.getElementById('HospitalName').value;
  var DoctorId=document.getElementById('DoctorId').value;
 
}



function selectedDate(date){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/ihgufyd'));
	//var doctorId=document.getElementById('interviewerId').value;
	//var pid=document.getElementById('interviewerAvailabilityId').value;
	//var hospitalBranchid=document.getElementById('hbid').value;
	alert("selectedDateMethod")
	//var interviewerId=document.getElementById('interviewerId').value;
	
	document.getElementById('doc').action=appname+"/Viewdoctors?getQryDate=selectDate&Date="+date+"&interviewerId="+interviewerId;
	document.getElementById('doc').submit();
}


