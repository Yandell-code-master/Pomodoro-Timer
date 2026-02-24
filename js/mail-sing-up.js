document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("email-form");
    const mailInput = document.getElementById("email-input");

    form.addEventListener("submit", async event => {
        event.preventDefault();
        const URL = ENV.API_URL + "users/save-user-email";
        const email = mailInput.value;

        const userFromEmail = {
            name: "newUser",
            passwordHash: "",
            email: email,
            status: "PENDING"
        }

        try {
            const response = await fetch(URL, {
                method: 'POST',
                body: JSON.stringify(userFromEmail),
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error("SERVER_ERROR: " + response.status);
            }
        } catch (error) {
            if (error.message.startsWith("SERVER_ERROR")) {
                alert("Something went wrong trying to sign up (The server responded with an error).");
            } else {
                alert("The server didn't respond.");
                throw new Error("The server didn't respond: " + error);
            }
        }

        mailInput.value = "";
        window.location.href = "http://localhost:8000/activate-page.html";
    });
});