document.addEventListener("DOMContentLoaded", () => {
    const deleteAccountButon = document.getElementById("delete-account-dropdown-item");

    deleteAccountButon.addEventListener("click", async () => {
        

        const user = JSON.parse(localStorage.getItem("userData"));
        const URL_BACKEND = `http://localhost:8080/users/${user.id}`;

        const delitingUserFetch = await fetch(URL_BACKEND, {
            method : 'DELETE'
        })

        if (!delitingUserFetch.ok) {
            throw new Error("There was an error trying to delete the user");
        }

        localStorage.removeItem("userData");
        localStorage.removeItem("isLoged");
        window.location.href = "http://localhost:8000/index.html";
    });
});