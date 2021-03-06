/*
 * PaperFood v0.1
 * 
 * Author: Kushal Pandya < https://github.com/kushalpandya >
 * License: GPLv3.
 */

var regExAlphabet = /^\D\w*$/;
var regExEmail = /^(.+)@(.+)$/;
var regExPassword = /^.{8,}$/;

var shelfTemplate = Handlebars.compile($("#shelf-items").html());
var bookinfoTemplate = Handlebars.compile($("#book-info").html());
var cartinfoTemplate = Handlebars.compile($("#cart-info").html());

var dlgBookInfo = $("#dlgBookInfo");
var dlgCartInfo = $("#dlgCartInfo");

var itemCount = $(".item-count");

var loginmsg = $("#loginmsg");
var loginloader = $(".login-loader");

var hashShelf = "bookshelf";
var hashSearch = "search?";

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
	$("#loginform").slideToggle(150, function() {
		$(this).find("#loginEmail").focus();
	});
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
	
	var btnsignin = $(this);
	
	var loginmail = $("#loginEmail");
	var loginpass = $("#loginPass");
	
	
	if(loginmail.val() === "" || loginpass === "")
	{
		loginmsg.removeClass("pass-valid");
		loginmsg.addClass("pass-invalid");
	}
	else
	{
		loginmsg.removeClass("pass-invalid");
		loginmsg.addClass("pass-valid");
		
		loginloader.show();
		btnsignin.addClass("disabled").attr("disabled", "disabled");
		$.post("Authenticate", {
			type : "login",
			loginEmail : $("#loginEmail").val().trim(),
			loginPass : $("#loginPass").val().trim(),
			loginRemember : document.loginform.loginRemember.checked
		},
		function(data) {
			loginloader.hide();
			btnsignin.removeClass("disabled").removeAttr("disabled");
			if(data.status === "success")
			{
				$("#loginform").slideToggle(150, function() {
					$("#login, #signup").hide();
					$("#logout, #cart").show();
				});
			}
			else if(data.status === "invalid")
			{
				loginmsg.removeClass("pass-valid");
				loginmsg.addClass("pass-invalid");
			}
			else if(data.status === "unavailable")
				alert("Cannot connect to PaperFood, try again later.");
			else
				alert("Error occurred while logging you in.");
		});
	}
});

//Logout User
$("#logout").live("click", function(e) {
	e.preventDefault();
	$.post("Authenticate", {
		type : "logout"
	},
	function(data) {
		if(data.status === "success")
		{
			$("#logout, #cart").hide();
			$("#login, #signup").show();
			alert("You've logged out successfully!");
		}
		else
			alert("Error occurred while logging you out.");
	});
});

//Show Cart Items Dialog
$("#cart").live("click", function(e) {
	e.preventDefault();
	$.post("CartManager", {
		type : "cartinfo"
	},
	function(data) {
		if(data.status === "success")
			showCheckoutDialog(data.cartinfo);
	});
});

//Perform Checkout.
function showCheckoutDialog(cartinfo)
{
	dlgCartInfo.html(cartinfoTemplate(cartinfo));
	dlgCartInfo.dialog({
		modal: true,
		resizable: false,
		title: "Checkout",
		height: 350,
		width: 600,
		show: "fade",
		hide: "fade",
		buttons: {
			"Checkout" : function() {
				$.post("CartManager", {
					type : "checkout"
				},
				function(data) {
					console.log(data);
					if(data.status === "success")
					{
						alert("Your order has been placed");
						dlgCartInfo.dialog("close");
					}
				});
			},
		}
	});
}

//Clear errors on changing invalid info in registration form.
$("input[type='text'],input[type='password'],input[name!='txtSearchKey']").on("keydown", function() {
	$(this).parents().eq(1).removeClass("error");
	$(this).parents().eq(1).removeClass("success");
	if($(this).attr("name") === "txtEmail")
		$(this).parent().find(".help-block").remove();
});

//Register User.
$("#btnSubmit").on("click", function(e) {
	e.preventDefault();
	if($(".registration-form").find(".error").length <= 0) //Prevent registration for invalid information, this is risky due to client-side validation if code is not obfuscated.
	{
		$.post("RegisterUser", $("#registerform").jsonify(), function(data) {
			if(data.status === "success")
			{
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
				if(emailblock.parent().find(".help-block").length <= 0)
					emailblock.after("<p class='help-block'>Email already registered, try using different Email address.</p>");
				emailblock.parents().eq(1).removeClass("success");
				emailblock.parents().eq(1).addClass("error");
				emailblock.focus();
			}
		});
	}
	else
	{
		alert("Your registration information is invalid!");
		$(".registration-form").find(".error").children().find("input").focus();
	}
});

//Clear Registration Form.
$("#btnReset").on("click", function(e) {
	var formcontrols = $("#registerform").find(".control-group");
	formcontrols.each(function() {
		$(this).removeClass("success");
		$(this).removeClass("error");
	});
	formcontrols.find("input[name='txtEmail']").siblings(".help-block").remove();
});

//Show entire Book Shelf.
$("#btnShowBookshelf").on("click", function() {
	$("#searcharea").slideLeftHide(function() {
		$("#loading").fadeIn();
		window.location.hash = hashShelf;
		$.post("BooksCatalog", {
			type : "none"
		},
		function(data) {
			$("#loading").fadeOut(function() {
				if(data.type === "catalog")
				{
					$("#bookshelf").fadeIn().html(shelfTemplate(data.catalog));
					console.log(data.catalog);
				}
				else if(data.type === "fail")
					$("#loading").parent().append("<h1 align='center' style='color: white; text-align: center; margin-top: 10%;'>Oops! something went wrong while connecting PaperFood, try again later.</h1>");
			});
		});
	});
});

//Perform search on Enter Key Press.
$("input[name='txtSearchKey']").on("keydown", function(e) {
	if(e.keyCode === 13)
		$("#btnSearch").trigger("click");
});

//Request for search results.
$("#btnSearch").on("click", function() {
	var key = $("input[name='txtSearchKey']").val();
	if(key !== "")
		showSearchResults(key);
});

//Request for Book info.
$("#bookshelf ul li a").live("click", function(e) {
	e.preventDefault();
	var referer = $(this);
	//$("body").mask("Loading...");
	$.post("BooksCatalog", {
		type : "isbn",
		value : referer.attr("rel")
	},
	function(data) {
		//$("body").unmask();
		if(data.type === "book")
			showBookDialog(data.book, data.activeLogin);
	});
});

//Show individual book info.
function showBookDialog(JSONBook, loginstatus)
{
	dlgBookInfo.html(bookinfoTemplate(JSONBook));
	dlgBookInfo.dialog({
		modal: true,
		resizable: false,
		//draggable: false,
		height: 350,
		width: 600,
		show: "fade",
		hide: "fade",
		buttons: {
			"Add to Cart" : function() {
				$.post("CartManager", {
					type : "addtocart",
					isbn : dlgBookInfo.find("img").data("isbn")
				},
				function(data) {
					if(data.status === "success")
					{
						itemCount.text(data.count);
						dlgBookInfo.dialog("close");
					}
				});
			},
		}
	});
	$(".ui-dialog-buttonpane button:contains('Add to Cart')").attr("disabled", !loginstatus).addClass("disabled");
}

//Perform Cookie or Session based autologin.
function doAutoLogin()
{
	var cookieval = getCookie("paperfood");
	if(cookieval != null)
	{
		$.post("Authenticate", {
			type : "cookielogin",
			loginEmail : cookieval.split("\"")[1]
		},
		function(data) {
			if(data.status === "success")
			{
				$("#login, #signup").hide();
				$("#logout, #cart").show();
			}
			else
				alert("Error occurred while logging you in.");
		});
	}
	else
	{
		$.post("CartManager", {
			type : "lastcartinfo",
			isbn : dlgBookInfo.find("img").data("isbn")
		},
		function(data) {
			if(data.status === "success")
				itemCount.text(data.count);
		});
		
		$.post("Authenticate", {
			type : "sessionlogin"
		},
		function(data) {
			if(data.status === "success")
			{
				$("#login, #signup").hide();
				$("#logout, #cart").show();
			}
		});
	}
}

//Perform Search.
function showSearchResults(key)
{
	$.post("BooksCatalog", {
		type : "search",
		searchKey : key
	},
	function(data) {
		if(data.type === "result")
		{
			window.location.hash = hashSearch+key;
			$("#searcharea").slideLeftHide(function() {
				$("#bookshelf").fadeIn().html(shelfTemplate(data.result));
			});
		}
		else if(data.type === "empty")
		{
			window.location.hash = hashSearch+key;
			$("#searcharea").slideLeftHide(function() {
				var msgelement = $("#loading").parent();
				if(msgelement.find("h1").length <= 0) 	//Need this to guard against multiple messages due to window.hash.change event execution of showSearchResults(key).
					$("#loading").parent().append("<h1 align='center' style='color: white; text-align: center; margin-top: 10%;'>No items match with your query!</h1>");
			});
		}
		else
		{
			window.location.hash = hashSearch+key;
			$("#searcharea").slideLeftHide(function() {
				$("#loading").parent().append("<h1 align='center' style='color: white; text-align: center; margin-top: 10%;'>Oops! something went wrong while connecting PaperFood, try again later.</h1>");
			});
		}
	});
}

//Get Hash from the URL.
function getLocationHash() {
	  return window.location.hash.substring(1);
}

//Get Cookie from Browser
function getCookie( check_name ) {
    // first we'll split this cookie up into name/value pairs
    // note: document.cookie only returns name=value, not the other components
    var a_all_cookies = document.cookie.split( ';' );
    var a_temp_cookie = '';
    var cookie_name = '';
    var cookie_value = '';
    var b_cookie_found = false; // set boolean t/f default f

    for ( i = 0; i < a_all_cookies.length; i++ )
    {
        // now we'll split apart each name=value pair
        a_temp_cookie = a_all_cookies[i].split( '=' );


        // and trim left/right whitespace while we're at it
        cookie_name = a_temp_cookie[0].replace(/^\s+|\s+$/g, '');

        // if the extracted name matches passed check_name
        if ( cookie_name == check_name )
        {
            b_cookie_found = true;
            // we need to handle case where cookie has no value but exists (no = sign, that is):
            if ( a_temp_cookie.length > 1 )
            {
                cookie_value = unescape( a_temp_cookie[1].replace(/^\s+|\s+$/g, '') );
            }
            // note that in cases where cookie is initialized but no value, null is returned
            return cookie_value;
            break;
        }
        a_temp_cookie = null;
        cookie_name = '';
    }
    if ( !b_cookie_found )
    {
        return null;
    }
}

//Window Hash Change Event Handler.
window.onhashchange = function(e) {
	if(getLocationHash() === "")
	{
		$("#bookshelf").fadeOut(function() {
			$("#searcharea").fadeIn();
			$("#loading").parent().find("h1").remove();
		});
	}
	else if(getLocationHash() === "bookshelf")
	{
		$("#btnShowBookshelf").trigger("click");
	}
	else if(getLocationHash().split("?")[0] === "search")
	{
		showSearchResults(getLocationHash().split("?")[1]);
	}
};

