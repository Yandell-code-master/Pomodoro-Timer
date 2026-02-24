document.addEventListener("DOMContentLoaded", () => {
    const btnSendPassword = document.getElementById("btn-send-password");

    btnSendPassword.addEventListener("click", async () => {
        const passwordTextField = document.getElementById("password-textfield");
        const passwordConfirmationTextField = document.getElementById("password-confirm-textfield");
        const warningText = document.getElementById("warning-text");
        const URL = ENV.API_URL + "users/set-password";
        const data = {
            password: passwordTextField.value,
            token: getToken()
        }


        if (!verifyPassword(passwordTextField.value)) {
            alert("The password is short try another one")
            cleanPasswordFields();
            return;
        }

        if (!confirmPasswordEquality(passwordTextField.value, passwordConfirmationTextField.value)) {
            warningText.innerText = "Passwords are different";
            warningText.style.display = "block";
            return;
        }

        try {
            let response = await fetch(URL, {
                method: 'POST',
                body: JSON.stringify(data),
                headers: {
                    'Content-Type': 'application/json'
                }
            });


            if (!response.ok) {
                throw new Error("SERVER_ERROR: " + response.status);
            }

            window.location.href = ENV.API_URL + "log-in.html";
        } catch (error) {
            if (error.message.startsWith("SERVER_ERROR")) {
                alert("Something went wrong trying to set the new password");
            } else {
                alert("The server didn't respond");
            }
        }
    });

    function verifyPassword(password) {
        if (password.length >= 8) {
            return true;
        }

        return false;
    }

    function confirmPasswordEquality(password, confirmationPassword) {
        if (password == confirmationPassword) {
            return true;
        }

        return false;
    }

    function getToken() {
        const queryParams = window.location.search;
        const urlParams = new URLSearchParams(queryParams);
        const token = urlParams.get('token');

        return token;
    }

    function cleanPasswordFields() {
        passwordTextField.value = "";
        passwordConfirmationTextField = "";
    }
});

