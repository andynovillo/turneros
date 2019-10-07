function generateJSON() {

	var obj = new Object();

	$('.answer').each(
			function() {

				switch (this.type) {
				case 'radio': // Radio
					if ($(this).is(':checked')) {
						obj[$(this).attr('name')] = $(this).val();
					} else {

					}
					break;
				case 'text': // Input
					if ($(this).val())
						obj[$(this).data('name')] = $(this).val();
					else
						obj[$(this).data('name')] = "";
					break;
				case 'textarea': // Input
					if ($(this).val())
						obj[$(this).data('name')] = $(this).val();
					else
						obj[$(this).data('name')] = "";
					break;
				default:
					switch ($(this).data('type')) {
					case 'date-time': // PrimeFaces Calendar
						if ($(this).find('input').val())
							obj[$(this).data('name')] = $(this).find('input')
									.val();
						else
							obj[$(this).data('name')] = "";
						break;
					case 'select-single':
						if ($(this).find('option:selected').text()) {
							obj[$(this).data('name')] = $(this).find(
									'option:selected').text();
							obj[$(this).data('name') + "-select-value"] = $(
									this).val();
						} else {
							obj[$(this).data('name')] = '';
							obj[$(this).data('name') + "-select-value"] = '';
						}
						break;
					default:
						// Ignore
					}
				}

			});

	var jsonString = JSON.stringify(obj);

	$('.json-value').val(jsonString);

}

function loadJSON() {

	var obj = new Object();

	try {

		if ($('.json-value').data('value')) {
			obj = $('.json-value').data('value');
		} else {
			obj = JSON.parse($('.json-value').val());
		}

		// console.log(obj);

		$('*[class^="value"]').text('');

		for ( var key in obj) {
			if (obj.hasOwnProperty(key)) {

				$(".value-" + key).text(obj[key]);
				// $(".value-" + key).val(obj[key]);
				// console.log(".value-" + key);

			}
		}

		var date = new Date();
		var day = date.getDate();
		var monthIndex = date.getMonth();
		var year = date.getFullYear();

		var text = "Guayaquil, " + day + " de " + monthNames[monthIndex]
				+ " del " + year;

		$('.value-today-date-time').text(text);

	} catch (exception) {
		// console.log(exception);
		// can't load data
	}

	/*
	 * if (key.startsWith('date-time')) {
	 * 
	 * //formatDate(key, obj[key] + ''); } else {
	 * 
	 * $(".value-" + key).text(obj[key]); }
	 */

	function formatDate(key, dateTime) {

		var suffix;

		if (key === 'date-time') {
			suffix = '';
		} else {
			suffix = key.replace('date-time', '');
		}

		var dateText = dateTime.split(' ')[0].split('/');

		day = dateText[0];
		monthIndex = dateText[1];
		year = dateText[2];

		var timeText = dateTime.split(' ')[1].split(':');

		var hour = timeText[0];
		var minute = timeText[1];

		date = new Date(year, monthIndex - 1, day, hour, minute, 0, 0);

		day = date.getDate();
		monthIndex = date.getMonth();
		year = date.getFullYear();

		dateText = day + " de " + monthNames[monthIndex] + " del " + year;

		$('.value-date' + suffix).text(dateText);
		$('.value-time' + suffix).text(dateTime.split(' ')[1]);

	}

}

function loadJSONByIDOrDataName() {

	var obj = new Object();

	try {

		if ($('.json-value').data('value'))
			obj = $('.json-value').data('value');
		else
			obj = JSON.parse($('.json-value').val());

		$('*[class="answer"]').text('');

		var element = null;

		for ( var key in obj) {
			if (obj.hasOwnProperty(key)) {

				/*
				 * if ($("input#" + key).length == 0) {
				 * 
				 * element = $("input[data-name='" + key + "']");
				 * 
				 * element.text(obj[key]); element.val(obj[key]); } else {
				 * 
				 * element = $("input#" + key);
				 * 
				 * element.text(obj[key]); element.val(obj[key]); }
				 */

				if (key.includes('select-value')) {

					element = $('select').filter(
							'[data-name=' + key.replace('-select-value', '')
									+ ']');

					if (element.length != 0) {

						element.val(obj[key]);

					}

				}

				if ($('input, span, textarea').filter('[id=' + key + ']').length == 0) {

					element = $('input, span, textarea').filter(
							'[data-name=' + key + ']');

					if (element.length != 0) {

						element.text(obj[key]);
						element.val(obj[key]);

					}
					
					

				} else {

					element = $('input, span, textarea').filter(
							'[id=' + key + ']');

					if (element.length != 0) {

						element.text(obj[key]);
						element.val(obj[key]);

					}

				}
				
				//console.log(element[0].id, obj[key]);

			}
		}

	} catch (exception) {
		console.log(exception);
		// can't load data
	}

}

function loadSchema(pnl, cpt) {

	try {

		$('.' + pnl).empty();

		var data = JSON.parse($('.' + cpt).val());
		var obj = new Object();

		for ( var item in data.schema) {

			obj = data.schema[item];

			if (obj.hasOwnProperty('type')) {
				switch (obj.type) {
				case 'text':
					$('.' + pnl).append(

					$('<div/>', {
						'class' : 'form-group'
					}).append($('<label/>', {
						'class' : 'col-sm-2 control-label',
						'text' : obj.label
					}), $('<div/>', {
						'class' : 'col-sm-4'
					}).append($('<input/>', obj.attrs))));

					break;
				case 'datepicker':
					$('.' + pnl).append(

					$('<div/>', {
						'class' : 'form-group'
					}).append($('<label/>', {
						'class' : 'col-sm-2 control-label',
						'text' : obj.label
					}), $('<div/>', {
						'class' : 'col-sm-4'
					}).append($('<input/>', obj.attrs))));

					break;
				default:
					console.log('not implemented ' + obj.type);

				}
			}

		}

	} catch (exception) {

		// console.log(exception);

		$('.' + pnl).empty();

		// can't load data
	}

}

function createJSONData(cpt, trg) {

	var obj = new Object();

	$(cpt).find('.answer').each(
			function() {

				switch (this.type) {
				case 'radio': // Radio
					if ($(this).is(':checked')) {
						obj[$(this).attr('name')] = $(this).val();
					} else {

					}
					break;
				case 'text': // Input
					if ($(this).val())
						obj[$(this).data('name')] = $(this).val();
					else
						obj[$(this).data('name')] = "";
					break;
				case 'time': // Input
					if ($(this).val())
						obj[$(this).data('name')] = $(this).val();
					else
						obj[$(this).data('name')] = "";
					break;
				case 'textarea': // Input
					if ($(this).val())
						obj[$(this).data('name')] = $(this).val();
					else
						obj[$(this).data('name')] = "";
					break;
				default:
					switch ($(this).data('type')) {
					case 'date-time': // PrimeFaces Calendar
						if ($(this).find('input').val())
							obj[$(this).data('name')] = $(this).find('input')
									.val();
						else
							obj[$(this).data('name')] = "";
						break;
					case 'select-single':
						if ($(this).find('option:selected').text()) {
							obj[$(this).data('name')] = $(this).find(
									'option:selected').text();
							obj[$(this).data('name') + "-select-value"] = $(
									this).val();
						} else {
							obj[$(this).data('name')] = '';
							obj[$(this).data('name') + "-select-value"] = '';
						}
						break;
					default:
						// Ignore
					}
				}

			});

	var jsonString = JSON.stringify(obj);

	console.log(jsonString);

	$(trg).val(jsonString);

}

function loadJSONData(src) {

	var obj = new Object();

	try {

		obj = $(src).data('value');

		var element = null;

		for ( var key in obj) {
			if (obj.hasOwnProperty(key)) {

				if (key.includes('select-value')) {

					elements = $('select').filter(
							'[data-name=' + key.replace('-select-value', '')
									+ ']');

					elements.each(function() {

						if ($(this).length != 0) {

							$(this).val(obj[key]);

						}

					});

				}

				if ($('input, span, textarea').filter('[id=' + key + ']').length == 0) {

					elements = $('input, span, textarea').filter(
							'[data-name=' + key + ']');

					elements.each(function() {

						if ($(this).data('type') == 'radio') {

							if ($(this).val() == obj[key]) {

								$(this).prop('checked', true);

							}

						} else if ($(this).hasClass('hasDatepicker')) {

							$(this).val(obj[key]);

						} else if ($(this).length != 0) {

							$(this).text(obj[key]);
							$(this).val(obj[key]);

						} else {

						}

					});

				} else {

					elements = $('input, span, textarea').filter(
							'[id=' + key + ']');

					elements.each(function() {

						if ($(this).data('type') == 'radio') {

							if ($(this).val() == obj[key]) {

								$(this).prop('checked', true);

							}

						} else if ($(this).hasClass('hasDatepicker')) {

							$(this).val(obj[key]);

						} else if ($(this).length != 0) {

							$(this).text(obj[key]);
							$(this).val(obj[key]);

						} else {

						}

					});

				}

			}
		}

	} catch (exception) {
		console.log(exception);
		// can't load data
	}

}
