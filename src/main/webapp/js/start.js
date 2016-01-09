function startInit(){
	$("#user").text(mUser.getLogin());
	$("#user_logout").click(function(){
		mUser.clear();
		nav(1);
	});
}