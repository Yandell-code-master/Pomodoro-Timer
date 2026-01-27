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
                password : passwordTextField.value
            }

            fetch (URL, {
                method : 'POST'
                body : 
            })
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
});

