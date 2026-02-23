document.addEventListener("DOMContentLoaded", async () => {
    const inputName = document.getElementById("user-name");
    const saveChangesButton = document.getElementById("save-changes-buton");

    saveChangesButton.addEventListener("click", async e => {
        e.preventDefault();
        const user = JSON.parse(localStorage.getItem("userData"));
        const URL_BACKEND = `http://localhost:8080/users/${user.id}`;

        const userInfoToUpdate = {
            name: inputName.value
        }

        if (getHostName() != "localhost") {
            alert("To use this feature you have to download the full version");
            throw new Error("To use this feature download the full version");
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