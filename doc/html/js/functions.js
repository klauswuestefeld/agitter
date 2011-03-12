$(document).ready(function() {
	$(".lamp_on").focus(function() {
		if($(this).val() == $(this).attr("alt")){
			$(this).val('');
			$(this).removeClass("lamp_off").addClass("lamp_on");	
		}
	}).blur(function() {
		if($(this).val() == ''){
			$(this).val($(this).attr("alt"));
			$(this).removeClass("lamp_on").addClass("lamp_off");
		}
	}).each(function() {
		if($(this).val($(this).attr("alt"))){
			$(this).removeClass("lamp_on").addClass("lamp_off");
		}
	});
});