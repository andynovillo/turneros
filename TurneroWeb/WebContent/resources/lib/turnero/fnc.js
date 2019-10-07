$(function() {

	initPage();
	initVideo();
	testService();

});

var audio;
var tbl;
var tblHead;
var tblBody;
var voiceEnabled = false;

function initPage() {

	// Inicio
	// Limpieza de turnos

	// Limpiar todos los días cuando arranca el televisor
	// Si la fecha es diferente al día de hoy, borra todo el contenido

	var dateObj = new Date();
	var month = dateObj.getUTCMonth() + 1; 
	var day = dateObj.getUTCDate();
	var year = dateObj.getUTCFullYear();

	today = year + "/" + month + "/" + day;

	if (localStorage.getItem("today") != today) {
		localStorage.clear();
		localStorage.setItem("today", today);
	}

	// Fin
	// Limpieza de turnos

	audio = document.getElementById('bellSound');

	if (voiceEnabled)
		responsiveVoice.setDefaultVoice('Spanish Latin American Female');

	tbl = $('#form\\:tbl');
	tblHead = $('#form\\:tbl_head');
	tblHead.empty();
	tblBody = $('#form\\:tbl_data');
	tblBody.empty();

	var content = localStorage.getItem("calledTicketList");

	if (content != null && content.length > 0) {
		tbl.html(content);

	}

	tbl = $('#form\\:tbl');
	tblHead = $('#form\\:tbl_head');
	tblBody = $('#form\\:tbl_data');

}

function testService() {

	var link = location.protocol + '//' + location.hostname
			+ (location.port ? ':' + location.port : '') + location.pathname;

	$.ajax({
		type : 'HEAD',
		url : link,
		success : function() {
			updateTicketTblRmtCmd();
			loadFontSize();
			loadVideoInfo();
			modifyPanels();
			setTimeout(function() {
				testService();
			}, 2000);
		},
		error : function() {
			setTimeout(function() {
				testService();
			}, 2000);
		}
	});

}

function addRow(data) {

	if (data) {

		var obj = null;

		try {

			obj = JSON.parse(data);

			var patientClinicHistory = obj.patientClinicHistory;
			var doctorName = obj.doctorName;
			var patientName = obj.patientName;
			var ticket = obj.ticket;
			var module = obj.module;
			var infoProtected = obj.infoProtected;
			var rowClass = obj.rowClass;

			if (infoProtected) {

				if (tblHead.children().length == 0) {

					var trHd = jQuery('<tr/>', {
						'title' : ''
					});

					var nameHd = jQuery('<td/>', {
						'text' : 'DOCTOR'
					}).css({
						'text-align' : 'center',
						'width' : '50%'
					});

					var ticketHd = jQuery('<td/>', {
						'text' : 'TURNO'
					}).css({
						'text-align' : 'center',
						'width' : '70%'
					});

					var moduleHd = jQuery('<td/>', {
						'text' : 'MÓD.'
					}).css({
						'text-align' : 'center',
						'width' : '30%'
					});

					// trHd.append(nameHd);
					trHd.append(ticketHd);
					trHd.append(moduleHd);

					tblHead.append(trHd);

				}

			} else {

				if (tblHead.children().length == 0) {

					var trHd = jQuery('<tr/>', {
						'title' : ''
					});

					var nameHd = jQuery('<td/>', {
						'text' : 'PACIENTE'
					}).css({
						'text-align' : 'center',
						'width' : '80%'
					});

					var moduleHd = jQuery('<td/>', {
						'text' : 'MÓD.'
					}).css({
						'text-align' : 'center',
						'width' : '20%'
					});

					trHd.append(nameHd);
					trHd.append(moduleHd);

					tblHead.append(trHd);

				}

			}

			if (tblBody.find('tr').length > 15) {

				tblBody.find('tr').last().remove();

			}

			tblBody.find('tr[data-id="' + patientClinicHistory + '"]').remove();

			var tr = jQuery('<tr/>', {
				'data-id' : patientClinicHistory,
				'class' : rowClass
			});

			if (infoProtected) {

				var nameCol = jQuery('<td/>', {
					'class' : 'shineAnimation'
				}).css({
					'text-align' : 'left',
					'width' : '50%'
				}).append(jQuery('<div/>', {
					'class' : 'ellipsis',
					'text' : doctorName
				}));

				var ticketCol = jQuery('<td/>', {
					'text' : ticket,
					'class' : 'shineAnimation'
				}).css({
					'width' : '70%'
				});

				var moduleCol = jQuery('<td/>', {
					'text' : module,
					'class' : 'shineAnimation'
				}).css({
					'width' : '30%'
				});

				// tr.append(nameCol);
				tr.append(ticketCol);
				tr.append(moduleCol);

			} else {

				var nameCol = jQuery('<td/>', {
					'class' : 'shineAnimation'
				}).css({
					'text-align' : 'left',
					'width' : '80%'
				}).append(jQuery('<div/>', {
					'class' : 'ellipsis',
					'text' : patientName
				}));

				var moduleCol = jQuery('<td/>', {
					'text' : module,
					'class' : 'shineAnimation'
				}).css({
					'width' : '20%'
				});

				tr.append(nameCol);
				tr.append(moduleCol);

			}

			tblBody.prepend(tr);

			audio.load();
			audio.play();

			if (voiceEnabled)
				responsiveVoice.speak(obj.ticket);

			localStorage.setItem("calledTicketList", tbl.html());

		} catch (err) {

			console.log(err);

		}

	}

}