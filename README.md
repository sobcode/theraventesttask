**Environment:** Java, SpringBoot, MySQL
</br>
</br>
**In order to receive access to URLs with necessary authentication:**
  1) Firstly create customer with URL http://localhost:8081/api/customers, method POST and required JSON + password field.
  2) Then authenticate with URL http://localhost:8081/api/customers/authenticate, method POST and JSON with email and password.
  3) Add received previously JWT to web headers: header = "Authorization", value = "Bearer " + token you received from authentication.
</br>
Database schema creates automatically so you do not need to create it manually. Simply change database properties in "application.properties" file.
</br>
</br>
**Endpoints/JSON examples**:
</br>
1) Create customer: (POST) http://localhost:8081/api/customers

request body (JSON):

{

"fullName" : "{full name}",

"email" : "{username}@{domain}",

"phone" : "{plus symbol(+) and 5-13 digits}", // optional field

"password" : "{customer's password}"

}

2) Read customers: (GET) http://localhost:8081/api/customers

    2.1) Read customers by full name: (GET) http://localhost:8081/api/customers?fullName={searched part of the full name}

    2.2) Read customers by email: (GET) http://localhost:8081/api/customers?email={searched part of the email}

    2.3) Read customers phone number: (GET) http://localhost:8081/api/customers?phone={searched part of the phone number}

    *You can combine filtering parameters

    *ADDITIONAL PAGINATION AND SORTING: ?size={number}&page={number}&sort={field name},{ASC/DESC}

3) Read customer by id: (GET) http://localhost:8081/api/customer/{id}

4) Delete customer: (DELETE) http://localhost:8081/api/customer/{id}

5) Update customer with all fields specified: (PUT) http://localhost:8081/api/customer/{id}

request body (JSON):

{

"fullName" : "{full name}",

"phone" : "{plus symbol(+) and 5-13 digits}",

}

6) Update customer with one/some specified fields: (PATCH) http://localhost:8081/api/customer/{id}

request body (JSON):

{

"fullName" : "{full name}", // optional field

"phone" : "{plus symbol(+) and 5-13 digits}", // optional field

}
