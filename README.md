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
response => username and token in harder 


3. user can validate the session
4. session are also handled
5. logout api is included
6. using decreapt algo for password and implemented the matcher as well
