Environment: Java, SpringBoot, MySQL

In order to receive access to URLs with necessary authentication:
  1) Firstly create customer with URL http://localhost:8081/api/customers, method POST and required JSON + password field.
  2) Then authenticate with URL http://localhost:8081/api/customers/authenticate, method POST and JSON with email and password.
  3) Add received previously JWT to web headers: header = "Authorization", value = "Bearer " + token you received from authentication.

Database schema creates automatically so you do not need to create it manually. Simply change database properties in "application.properties" file.

JSON examples:
* to create customer:
{
  "fullName" : "Pavlo Nilson",
  "email" : "pavlo.nilson@gmail.com",
  "phone" : "+380666174899", // optional field
  "password" : "Paha123"
}

* to authenticate:
{
  "email" : "pavlo.nilson@gmail.com",
  "password" : "Paha123"
}

* to update customer
{
  "id" : "1",
  "fullName" : "Pavlo Povilson",
  "phone" : "+380985989898"
}
