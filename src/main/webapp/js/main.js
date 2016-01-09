$(document).ready(function(){
	nav();
});

var mUser = {
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
		}
}