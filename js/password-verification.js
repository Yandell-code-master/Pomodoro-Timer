document.addEventListener("DOMContentLoaded", () => {
    const btnSendPassword = document.getElementById("btn-send-password");

    btnSendPassword.addEventListener("click", async () => {
        const passwordTextField = document.getElementById("password-textfield");
        const passwordConfirmationTextField = document.getElementById("password-confirm-textfield");
        const warningText = document.getElementById("warning-text");
        const URL = "http://localhost:8080/users/set-password";
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

        if (getHostName() != "localhost") {
            alert("To use this feature you have to download the full version");
            throw new Error("To use this feature download the full version");
        }

        let response = await fetch(URL, {
            method: 'POST',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error("Something went wrong trying to change the password " + response);
        }

        window.location.href = "http://localhost:8000/index.html";
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

