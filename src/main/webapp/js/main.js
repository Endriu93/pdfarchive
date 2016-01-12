$(document).ready(function(){
	nav();
});

mUser = {
		id:"",
		login:"",
		getLogin: function(){
			return this.login;
		},
		getId: function(){
			return this.id;
		},
		isLogged: function(){
			if(this.login.trim().length()===0) return false;
			else return true;
		},
		set: function(id,login){
			this.id = id;
			this.login = login;
		},
		clear:function(){
			this.id = "";
			this.login = "";
		},
		makeUserJson: function(){
			return {json: JSON.stringify({ userID: this.id.toString()})};
		},
		makeUserJsonString: function(){
			return JSON.stringify({json: JSON.stringify({ userID: this.id.toString()})})
		}
};

//values will be set after.
mAnimationSpeed = 400;

mSearchUtil = {
		makeUserWordJson: function(word){
			return {json: JSON.stringify({ userID: this.id.toString(), word: word.toString()})};
		}	
};

mDownloadUtil = {
		performRequest: function(title){
			$('#form_userID').val(mUser.getId().toString());
			$('#form_title').val(title.toString());
			$('#download_form').submit();
		}
}

