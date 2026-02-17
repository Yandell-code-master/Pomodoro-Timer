document.addEventListener("DOMContentLoaded", event => {
    event.preventDefault();

    const form = document.getElementById("email-form");
    const mailInput = document.getElementById("email-input");

    form.addEventListener("submit", () => {
        event.preventDefault();
        const URL = "http://localhost:8080/users/save-user-email";
        const email = mailInput.value;
        mailInput.value = "";

        const userFromEmail = {
            name: "newUser",
            passwordHash: "",
            email: email,
            status: "PENDING"
        }

        fetch(URL, {
            method: 'POST',
            body: JSON.stringify(userFromEmail),
            headers: {
                'Content-Type': 'application/json'
            }
        });

        window.location.href = "http://localhost:8000/activate-page.html";
    });
});