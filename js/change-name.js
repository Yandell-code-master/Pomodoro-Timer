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

        try {
            const patchingUserResponse = await fetch(URL_BACKEND, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userInfoToUpdate)
            })

            if (!patchingUserResponse.ok) {
                throw new Error("SERVER_ERROR :" + patchingUserResponse.status);
            }
            
            const userUpdated = await patchingUserResponse.text();

            localStorage.removeItem("userData");
            localStorage.setItem("userData", userUpdated);
            alert("The changes have been saved succefully")
        } catch (error) {
            if (error.message.startsWith("SERVER_ERROR")) {
                alert("Something went wrong trying to change the name (The server responded with an error).");
            } else {
                alert("The server didn't respond.");
                throw new Error("The server didn't respond: " + error);
            }
        }
    })
});