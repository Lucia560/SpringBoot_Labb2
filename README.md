**Title: GitHub OAuth2 Configuration for IntelliJ Run Configuration**

**Objective:**  
To set up GitHub OAuth2 authentication for logging in via GitHub in a local environment using IntelliJ IDEA.

**Creating a GitHub client id**

To use GitHub’s OAuth 2.0 authentication system for login, you must first [Add a new GitHub app](https://github.com/settings/developers).

Select "New OAuth App" and then the "Register a new OAuth application" page is presented. Enter an app name and description. Then, enter your app’s home page, which should be http://localhost:8080, in this case. Finally, indicate the Authorization callback URL as http://localhost:8080/login/oauth2/code/github and click Register Application.


**IntelliJ Instructions:**

1. **Open IntelliJ IDEA:** Launch your IntelliJ IDEA IDE.

2. **Navigate to Run Configuration:**
    - Go to the top menu and select "Run".
    - From the dropdown menu, choose "Edit Configurations..."

3. **Select your Application Configuration:**
    - From the left-hand side, select the configuration you use to run your Spring Boot application.

4. **Add VM Options:**
    - In the "Run/Debug Configurations" dialog, locate the "VM Options" field.
    - Add the following lines to the VM options:
      ```
      -Dspring.security.oauth2.client.registration.github.client-id=<Your client id>
      -Dspring.security.oauth2.client.registration.github.client-secret=<Your client secret>
      ```

5. **Save Configuration:**
    - Once you've added the VM options, click on the "Apply" button to save the changes.
    - Click "OK" to close the dialog.

6. **Run the Application:**
    - Now, run your Spring Boot application as usual.

7. **Verify GitHub OAuth2 Integration:**
    - Once the application is running, navigate to the login page where GitHub authentication is implemented.
    - You should now be able to log in via GitHub using the provided credentials.

**Additional Notes:**
- Ensure that the client ID and client secret provided are correct and up-to-date.
- Double-check the configuration setup to avoid any errors.
- This setup is intended for local development environments only. Make sure not to expose sensitive information in production environments.