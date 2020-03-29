function validateActor() {
	var errorTelephone = checkTelephoneNumber();
	var errorSizeFile = checkSizeFile();
	
	return errorTelephone.length == 0 && errorSizeFile.length == 0;
}

function checkTelephoneNumber() {
	var telephone = document.getElementById("telephoneNumber").value;
	var regExp = /^((\+[0-9]{1,3}\ )?([0-9]{1,3}\ )?([0-9]{4,})?)$/;
	var error = "";
		
	var result = regExp.test(telephone);
		
	if (!result) {
		error = "Teléfono inválido";
	}
	
	document.getElementById("telephoneNumberError").innerHTML = error;
	
	return error;
}

function checkSizeFile() {
	var ls_files = document.getElementById("file");
	var files = ls_files.files;
	var sixeMax = 5 * 1024 * 1024;
	var error = "";
	var result;
	
	if (files.length > 0) {
		result = files[0].size > sixeMax;
		
		if (result) {
			error = "La imagen seleccionada supera los 5MB";
		}
	}
	
	document.getElementById("fileError").innerHTML = error;
	
	return error;
}