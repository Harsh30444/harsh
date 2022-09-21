function getPositions(deptName) {
	alert(deptName);
	var appname = window.location.pathname.substr(0, window.location.pathname.lastIndexOf('/hjhkjh'));
	document.getElementById('jobDescription').action = appname + "/client/saveJdGoToManage?getQry=getposition&deptName=" + deptName;
	document.getElementById('jobDescription').submit();
}

/* When the user clicks on the button,
toggle between hiding and showing the dropdown content */
function myFunction() {
  document.getElementById("myDropdown").classList.toggle("show");
}



