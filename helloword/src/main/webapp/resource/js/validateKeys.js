/* Faz a validacao dos campos para que 
 * recebam apenas os valores corretos.
 */

// Deixa digitar somemte numeros evento : onkeypress
function onlyNumbers(e) {
	var tecla = (window.event) ? event.keyCode : e.which;
	if ((tecla > 47 && tecla < 58))
		return true;
	else {
		if (tecla == 8 || tecla == 0)
			return true;
		else
			return false;
	}
}

// deixa digitar numeros e / (barra)
function onlyDate(e) {
	var tecla = (window.event) ? event.keyCode : e.which;
	if ((tecla > 46 && tecla < 58))
		return true;
	else {
		if (tecla == 8 || tecla == 0)
			return true;
		else
			return false;
	}
}

// Deixa digitar somemte letras evento : onkeypress
function onlyLetters(e) {
	var tecla = (window.event) ? event.keyCode : e.which;
	if (tecla >= 33 && tecla <= 64 || tecla >= 91 && tecla <= 93
			|| tecla >= 123 && tecla <= 159 || tecla >= 162 && tecla <= 191) {
		return false;
	} else {
		return true;
	}
}

// Deixa digitar somemte numeros e ponto (para numeros float) evento :
// onkeypress
function onlyFloat(e) {
	var tecla = (window.event) ? event.keyCode : e.which;
	if ((tecla > 47 && tecla < 58))
		return true;
	else {
		if (tecla == 8 || tecla == 0 || tecla == 46)
			return true;
		else
			return false;
	}
}

function normal(e) {
	return true;
}

function up(lstr, e) {
	var str = lstr.value; // obtem o valor
	lstr.value = str.toUpperCase(); // converte as strings e retorna ao campo
}
