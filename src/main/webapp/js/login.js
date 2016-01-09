function loginInit(){
$("#login_button").click(function(){
var email = $("#email").val();
var password = $("#password").val();
// Checking for blank fields.
if( email =='' || password ==''){
$('input[type="text"],input[type="password"]').css("border","2px solid red");
$('input[type="text"],input[type="password"]').css("box-shadow","0 0 3px red");
alert("Please fill all fields...!!!!!!");
}else {
$.post("http://pdfarchive-wfiisaw.rhcloud.com/LoginServlet",{json: JSON.stringify({ email1: email, password1:password})},
function(data) {
if(data=='InvalidLogin') {
$('input[type="text"]').css({"border":"2px solid red","box-shadow":"0 0 3px red"});
$('input[type="password"]').css({"border":"2px solid #00F5FF","box-shadow":"0 0 5px #00F5FF"});
alert("Użytkownik o podanej nazwie nie istnieje");
}else if(data=='InvalidPassword'){
$('input[type="text"],input[type="password"]').css({"border":"2px solid red","box-shadow":"0 0 3px red"});
alert("Niepoprawne hasło");
} else {
var loginData = jQuery.parseJSON(data);
$("form")[0].reset();
$('input[type="text"],input[type="password"]').css({"border":"2px solid #00F5FF","box-shadow":"0 0 5px #00F5FF"});
mUser.set(loginData.id, loginData.login);
alert("Witaj "+ loginData.login);
} 
});
}
});
}