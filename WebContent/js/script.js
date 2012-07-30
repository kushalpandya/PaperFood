var regExAlphabet = /^\D\w*$/;
var regExEmail = /^(.+)@(.+)$/;
var regExPassword = /^.{8,}$/;

var signupSym = $("#signup").find(".btn-symbol");

//Registration Form Drawer
$("#signup").on("click", function(e) {
	e.preventDefault();
	$(".registration-form").slideToggle(500, function() {
		if($(this).is(':visible'))
			signupSym.html("&#11014;");
		else
			signupSym.html("&#11015;");
	});
});

//Login Form Drawer
$("#login").on("click", function(e) {
	e.preventDefault();
	$("#loginform").slideToggle(150);
});

//Registration Form Validator
$("input[type='text'],input[type='password'],input[name!='txtSearchKey']").on("blur", function() {
	var currfield = $(this);
	if(currfield.attr("id") === "txtFname" || currfield.attr("id") === "txtLname")
	{
		if(!regExAlphabet.test(currfield.val()))
			currfield.parents().eq(1).addClass("error");
		else
		{
			currfield.parents().eq(1).removeClass("error");
			currfield.parents().eq(1).addClass("success");
		}
	}
	else if(currfield.attr("id") === "txtEmail")
	{
		if(!regExEmail.test(currfield.val()))
			currfield.parents().eq(1).addClass("error");
		else
		{
			currfield.parents().eq(1).removeClass("error");
			currfield.parents().eq(1).addClass("success");
		}
	}
	else if(currfield.attr("id") === "txtPass")
	{
		if(!regExPassword.test(currfield.val()))
			currfield.parents().eq(1).addClass("error");
		else
		{
			currfield.parents().eq(1).removeClass("error");
			currfield.parents().eq(1).addClass("success");
		}
	}
	else if(currfield.attr("id") === "txtCPass")
	{
		if(currfield.val().length <= 0 || currfield.val() !== $("#txtPass").val())
			currfield.parents().eq(1).addClass("error");
		else
		{
			currfield.parents().eq(1).removeClass("error");
			currfield.parents().eq(1).addClass("success");
		}
	}
});

//Login Validator
$("#btnLogin").on("click", function(e) {
	e.preventDefault();
	
	var loginmail = $("#loginEmail");
	var loginpass = $("#loginPass");
	
	var loginmsg = $("#loginmsg");
	
	if(loginmail.val() === "" || loginpass === "")
	{
		loginmsg.removeClass("pass-valid");
		loginmsg.addClass("pass-invalid");
	}
	else
	{
		loginmsg.removeClass("pass-invalid");
		loginmsg.addClass("pass-valid");
	}
});

$("input[type='text'],input[type='password'],input[name!='txtSearchKey']").on("keydown", function() {
	$(this).parents().eq(1).removeClass("error");
	$(this).parents().eq(1).removeClass("success");
});



