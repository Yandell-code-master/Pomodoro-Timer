document.addEventListener("DOMContentLoaded", async () => {
    const inputName = document.getElementById("user-name");
    const saveChangesButton = document.getElementById("save-changes-buton");

    saveChangesButton.addEventListener("click", async e => {
        e.preventDefault();
        const user = JSON.parse(localStorage.getItem("userData"));
        const URL_BACKEND = ENV.API_URL + `users/${user.id}`;

        const userInfoToUpdate = {
            name: inputName.value
        }

        if (!checkServerStatus()) {
            alert("You can use this feature because the server is offline");
            throw new Error("The server is offline");
        }

        const patchingUserResponse = await fetch(URL_BACKEND, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userInfoToUpdate)
        })

        if (!patchingUserResponse.ok) {
            throw new Error("There was an error trying to update the user");
        }

        const userUpdated = await patchingUserResponse.text();

        localStorage.removeItem("userData");
        localStorage.setItem("userData", userUpdated);
        alert("The changes have been saved succefully")
    })
});