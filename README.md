# UserManagement

It has APIs for signin, singup, to get userinfo, setroles
1. Singup -> by providing below details a user can signup and all the details are saved in db
   request =>  {    "userName":"Vimlesh11",
	"email":"vimlesh11@gmail.com",
    "password":"12345"}

 response => {
    "userName": "Vimlesh11",
    "signupMessage": "successful login"
}

2. Signin -> by providing the below details a user can login in
  request =>
   {
    "email":"vimleshgmail.com",
    "password":"12345"
}
response => username and token in hearder 
