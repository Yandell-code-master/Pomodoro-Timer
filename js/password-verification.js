document.addEventListener("DOMContentLoaded", () => {
    const btnSendPassword = document.getElementById("btn-send-password");

    btnSendPassword.addEventListener("click", () => {
        const passwordTextField = document.getElementById("password-textfield");
        const passwordConfirmationTextField = document.getElementById("password-confirm-textfield");
        const warningText = document.getElementById("warning-text");


        if (!verifyPassword(passwordTextField.value)) {
            warningText.style.display = "block";
        } else {

            if (!confirmPasswordEquality(passwordTextField.value, passwordConfirmationTextField.value)) {
                warningText.innerText = "Passwords are different";
                warningText.style.display = "block"
            }

            const URL = "http://localhost:8080/users/set-password"

            const data = {
                password : passwordTextField.value,
                token : getToken()
            }

            console.log(getToken());

            fetch(URL, {
                method: 'POST',
                body: JSON.stringify(data),
                headers : {
                    'Content-Type' : 'application/json'
                }
            });
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
});

