document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("email-form");
    const mailInput = document.getElementById("email-input");

    form.addEventListener("submit", async event => {
        event.preventDefault();
        const URL = "http://localhost:8080/users/save-user-email";
        const email = mailInput.value;

        const userFromEmail = {
            name: "newUser",
            passwordHash: "",
            email: email,
            status: "PENDING"
        }

        const response = await fetch(URL, {
            method: 'POST',
            body: JSON.stringify(userFromEmail),
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error("Something went wrong trying to sing up");
        }

        mailInput.value = "";
        window.location.href = "http://localhost:8000/activate-page.html";
    });
});