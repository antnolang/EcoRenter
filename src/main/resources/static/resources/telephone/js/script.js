function checkTelephoneNumber() {
	var telephone = document.getElementById("telephoneNumber").value;
	var regExp = /^((\+[0-9]{1,3}\ )?([0-9]{1,3}\ )?([0-9]{4,})?)$/;
	var error = "";
		
	var result = regExp.test(telephone);
		
	if (!result) {
		error = "Teléfono inválido";
	}
	
	document.getElementById("telephoneNumberError").innerHTML = error;
	
	return result;
}