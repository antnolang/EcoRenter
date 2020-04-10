function validateEcoTruki() {
	var errorFiles = checkEcoTrukiFiles();
	
	return errorFiles.length == 0;
}

function checkEcoTrukiFiles() {
	var ls_files = document.getElementById("file");
	var files = ls_files.files;
	var sixeMax = 5 * 1024 * 1024;
	var error = "";
	var result;
	
	var n = files.length;
	if (n > 0) {
		var i;
		for (i=0;i<n;i++) {
			result = files.item(i).size > sixeMax;
			
			if (result) {
				error = "Existe una o varias imágenes seleccionadas que superan los 5MB";
				break;
			}
		}
	}
	
	document.getElementById("ecoTrukifilesError").innerHTML = error;
	
	return error;
}