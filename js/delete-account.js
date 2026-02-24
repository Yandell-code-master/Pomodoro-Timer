document.addEventListener("DOMContentLoaded", () => {
    const deleteAccountButon = document.getElementById("delete-account-dropdown-item");

    deleteAccountButon.addEventListener("click", async () => {
        const user = JSON.parse(localStorage.getItem("userData"));
        const URL_BACKEND = ENV.API_URL + `users/${user.id}`;

        if (!checkServerStatus()) {
            alert("You can use this feature because the server is offline");
            throw new Error("The server is offline");
        }

        const delitingUserFetch = await fetch(URL_BACKEND, {
            method: 'DELETE'
        })

        if (!delitingUserFetch.ok) {
            throw new Error("There was an error trying to delete the user");
        }

        localStorage.removeItem("userData");
        localStorage.removeItem("isLoged");
        window.location.href = "http://localhost:8000/index.html";
    });
});