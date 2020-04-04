function validateCreditCard() {
	var errorHolderName = checkHolderName();
	var errorBrandName = checkBrandName();
	var errorNumber = checkNumber();
	var errorMonth = checkExpirationMonth();
	var errorYear = checkExpirationYear();
	var errorCVVCode = checkCVVCode();
	
	return errorHolderName.length == 0 &&
	 	   errorBrandName.length == 0 &&
	 	  errorNumber.length == 0 &&
	 	   errorMonth.length == 0 &&
	 	   errorYear.length == 0 &&
	 	   errorCVVCode.length == 0;
}


function checkHolderName() {
	var input = document.getElementById("holderName");
	var holderName = input.value.trim();
	var regExp = /^[^0-9]{1,}$/;
	var error = "";
	
	var valid = regExp.test(holderName);
	
	if (!valid) {
		error = "Debe rellenar este campo con un valor válido";
	}
	
	document.getElementById("holderNameError").innerHTML = error;
	
	return error;
}

function checkBrandName() {
	var input = document.getElementById("brandName");
	var brandName = input.value.trim();
	var regExp = /^[^0-9]{1,}$/;
	var error = "";
	
	var valid = regExp.test(brandName);
	
	if (!valid) {
		error = "Debe rellenar este campo con un valor válido";
	}
	
	document.getElementById("brandNameError").innerHTML = error;
	
	return error;
}

function checkNumber() {
	var input = document.getElementById("number");
	var number = input.value.trim();
	var regExp = /^[0-9]{16}$/;
	var error = "";
	
	var valid = regExp.test(number);
	
	if (!valid) {
		error = "Debe rellenar este campo con un valor válido";
	}
	
	document.getElementById("numberError").innerHTML = error;
	
	return error;
}

function checkExpirationMonth() {
	var input = document.getElementById("expirationMonth");
	var strExpirationMonth = input.value.trim();
	var expirationMonth = Number.parseInt(strExpirationMonth);
	var error = "";
	
	
	var valid = !isNaN(expirationMonth) && expirationMonth >=1 && expirationMonth <= 12;
	
	if (!valid) {
		error = "Debe introducir un valor comprendido entre 1 y 12";
	}
	
	document.getElementById("expirationMonthError").innerHTML = error;
	
	return error;
}

function checkExpirationYear() {
	var input = document.getElementById("expirationYear");
	var expirationYear = input.value.trim();
	var regExp = /^[0-9]{2}$/;
	var error = "";
	
	var valid = regExp.test(expirationYear);
	
	if (!valid) {
		error = "Debe introducir los 2 últimos dígitos del año";
	}
	
	document.getElementById("expirationYearError").innerHTML = error;
	
	return error;
}

function checkCVVCode() {
	var input = document.getElementById("cvvCode");
	var strCVVCode = input.value.trim();
	var cvvCode = Number.parseInt(strCVVCode);
	var error = "";
	
	var valid = !isNaN(cvvCode) && cvvCode >= 100 && cvvCode <= 999;
	
	if (!valid) {
		error = "Debe introducir un valor comprendido entre 100 y 999";
	}
	
	document.getElementById("cvvCodeError").innerHTML = error;
	
	return error;
}
