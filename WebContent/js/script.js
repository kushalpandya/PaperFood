var regExAlphabet = /^\D\w*$/;
var regExEmail = /^(.+)@(.+)$/;
var regExPassword = /^.{8,}$/;
var shelfTemplate = Handlebars.compile($("#shelf-items").html());

var hashShelf = "bookshelf";

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
	if(currfield.attr("name") === "txtFname" || currfield.attr("name") === "txtLname")
	{
		if(!regExAlphabet.test(currfield.val()))
			currfield.parents().eq(1).addClass("error");
		else
		{
			currfield.parents().eq(1).removeClass("error");
			currfield.parents().eq(1).addClass("success");
		}
	}
	else if(currfield.attr("name") === "txtEmail")
	{
		if(!regExEmail.test(currfield.val()))
			currfield.parents().eq(1).addClass("error");
		else
		{
			currfield.parents().eq(1).removeClass("error");
			currfield.parents().eq(1).addClass("success");
		}
	}
	else if(currfield.attr("name") === "txtPass")
	{
		if(!regExPassword.test(currfield.val()))
			currfield.parents().eq(1).addClass("error");
		else
		{
			currfield.parents().eq(1).removeClass("error");
			currfield.parents().eq(1).addClass("success");
		}
	}
	else if(currfield.attr("name") === "txtCPass")
	{
		if(currfield.val().length <= 0 || currfield.val() !== $("input[name='txtPass']").val())
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
	if($(this).attr("name") === "txtEmail")
		$(this).parent().find(".help-block").remove();
});

$("#btnSubmit").on("click", function(e) {
	e.preventDefault();
	$.post("RegisterUser", $("#registerform").jsonify(), function(data) {
		if(data.status === "success")
		{
			//$("#registerform").fadeTo(300,0).after("<h3 class='span6'>Registration Completed!<h3>");
			alert("Registration Completed. Login using your email and password");
			$("#btnReset").trigger("click");
			$(".registration-form").slideToggle(500, function() {
				$("#loginform").slideToggle(150, function() {
					$("#loginEmail").focus();
				});
			});
		}
		else if(data.status === "invalid")
		{
			var emailblock = $("input[name='txtEmail']");
			emailblock.after("<p class='help-block'>Email already registered, try using different Email address.</p>");
			emailblock.parents().eq(1).removeClass("success");
			emailblock.parents().eq(1).addClass("error");
			emailblock.focus();
		}
	});
});

$("#btnReset").on("click", function(e) {
	var formcontrols = $("#registerform").find(".control-group");
	formcontrols.each(function() {
		$(this).removeClass("success");
		$(this).removeClass("error");
	});
	formcontrols.find("input[name='txtEmail']").siblings(".help-block").remove();
});

$("#btnShowBookshelf").on("click", function() {
	$("#searcharea").slideLeftHide(function() {
		$("#loading").fadeIn();
		window.location.hash = hashShelf;
		$.post("BooksCatalog", {
			type : "none"
		},
		function(data) {
			$("#loading").fadeOut(function() {
				//if(data.type === "catalog")
					$("#bookshelf").fadeIn().html(shelfTemplate(data.catalog));
					console.log(data.catalog);
				//$("#bookshelf").fadeIn();
			});
		});
	});
});

function getLocationHash() {
	  return window.location.hash.substring(1);
}

window.onhashchange = function(e) {
	switch(getLocationHash())
	{
		case "": //No Hash
			$("#bookshelf").fadeOut(function() {
				$("#searcharea").fadeIn();
			});
			break;
		case "bookshelf":
			$("#btnShowBookshelf").trigger("click");
			break;
	    default: 
	    	break;
	}
};

