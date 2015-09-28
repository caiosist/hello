/* Faz a validacao das Dialogs para que
 * quando elas estiverem com informacoes erradas
 * nao fechem, e sim facam o efeito Shake;
 */

function validaCliente(xhr, status, args) {
	if (!args.sucesso) {
		/*
		 * $(PrimeFaces.escapeClientId('form:dmCliente')).effect("shake", {
		 * times : 5 }, 100);
		 */
	} else {
		PF('dManterCliente').hide();
	}
}

function validaVeiculo(xhr, status, args) {
	if (!args.sucesso) {
		/*
		 * $(PrimeFaces.escapeClientId('form:dmCliente')).effect("shake", {
		 * times : 5 }, 100);
		 */
	} else {
		PF('dManterVeiculo').hide();
	}
}

function validaSenha(xhr, status, args) {
	if (!args.sucesso) {
		/*
		 * $(PrimeFaces.escapeClientId('form:dmCliente')).effect("shake", {
		 * times : 5 }, 100);
		 */
	} else {
		PF('dAlteraSenha').show();
	}
}

function validaPlano(xhr, status, args) {
	if (!args.sucesso) {
		/*
		 * $(PrimeFaces.escapeClientId('form:dmCliente')).effect("shake", {
		 * times : 5 }, 100);
		 */
	} else {
		PF('dEditarPlano').hide();
		// window.top.location.href = "http://localhost:8080/helloword/";
	}
}
