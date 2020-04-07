function checkSizeFiles() {
	var ls_files = document.getElementById("file");
	var files = ls_files.files;
	var sizeMax = 5 * 1024 * 1024;
	var error = "";
	var result;
	
	for (const i = 0; i <= files.length - 1; i++) {
		result = files.item(i).size > sizeMax;
		if(result){
			error = "La imagen seleccionada supera los 5MB";
			$("#Guarda").prop("disabled",true);
			break;
		} else {
			$("#Guarda").prop("disabled",false);
			$("#fileError").text("");
		}
	}
	
	document.getElementById("fileError").innerHTML = error;

	return error;
}