var signupSym = $("#signup").find(".btn-symbol");

$("#signup").on("click", function() {
	$(".registration-form").slideToggle(500, function() {
		if($(this).is(':visible'))
			signupSym.html("&#11014;");
		else
			signupSym.html("&#11015;");
	});
});