document.addEventListener("DOMContentLoaded", () => {
    const deleteAccountButon = document.getElementById("delete-account-dropdown-item");

    deleteAccountButon.addEventListener("click", async () => {
        const user = JSON.parse(localStorage.getItem("userData"));
        const URL_BACKEND = ENV.API_URL + `users/${user.id}`;

        try {
            const delitingUserFetch = await fetch(URL_BACKEND, {
                method: 'DELETE'
            });

            if (!delitingUserFetch.ok) {
                throw new Error("There was an error trying to delete the user: " + delitingUserFetch.status);
            }

            localStorage.removeItem("userData");
            localStorage.removeItem("isLoged");
            window.location.href = "http://localhost:8000/index.html";
        } catch {
            if (error.message.startsWith("SERVER_ERROR")) {
                alert("Something went wrong trying to sign up (The server responded with an error).");
            } else {
                alert("The server didn't respond.");
                throw new Error("The server didn't respond: " + error);
            }
        }
    });
});