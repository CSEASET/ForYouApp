function check(evt) {
	var a = $(evt).val();
	a = a.replace(/\s+/g, '');
	$(evt).val(a.toUpperCase());
}

function check2(evt) {

}

function check3(evt, no) {
	$('#u' + no + 'Title').val(evt.options[evt.selectedIndex].value.trim());
}

function check4(evt) {
	var a = $(evt).val();
	a = a.split('\n').join(' ').trim();
	$(evt).val(a);
}

function submit() {
	var paperCode = $('#paperCode').val();
	var ptit = $('#paperTitle').val();
	var u1t = $('#u1Title').val();
	var u2t = $('#u2Title').val();
	var u3t = $('#u3Title').val();
	var u4t = $('#u4Title').val();
	var u1d = $('#u1Details').val();
	var u2d = $('#u2Details').val();
	var u3d = $('#u3Details').val();
	var u4d = $('#u4Details').val();

	var obj = {
		paperCode : [{
			"paperTitle" : ptit,
			"paperCredits" : 3,
			"paperUnits" : [{
				"unit" : {
					"unitTitle" : u1t,
					"unitDetails" : u1d
				}
			}, {
				"unit" : {
					"unitTitle" : u2t,
					"unitDetails" : u2d
				}
			}, {
				"unit" : {
					"unitTitle" : u3t,
					"unitDetails" : u3d
				}
			}, {
				"unit" : {
					"unitTitle" : u4t,
					"unitDetails" : u4d
				}
			}]
		}]
	};
	var json = JSON.stringify(obj);
	json = json.replace("paperCode", paperCode);
	$('#json').val(json);
	$.post( "save.php", { "fileName" : paperCode + ".txt", "fileContent" : json} );
}