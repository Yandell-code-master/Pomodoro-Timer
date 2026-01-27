document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("email-form");
    const mailInput = document.getElementById("email-input");

    form.addEventListener("submit", () => {
        event.preventDefault();
        const URL = "http://localhost:8080/users/save-user-email";
        const email = mailInput.value;

        const userFromEmail = {
            name : "newUser",
            passwordHash : "PENDING",
            email : email,
            status : "PENDING"
        }

        fetch(URL, {
            method : 'POST',
            body : JSON.stringify(userFromEmail),
            headers : {
                'Content-Type' : 'application/json'
            }
        });
    });
});