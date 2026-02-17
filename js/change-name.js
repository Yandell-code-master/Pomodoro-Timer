document.addEventListener("DOMContentLoaded", async () => {
    const user = JSON.parse(localStorage.getItem("userData"));
    const URL_BACKEND = "http://localhost:8080/users/{}";
    const inputName = document.getElementById("input-name");

    inputName.addEventListener("click", async e => {
        e.preventDefault();

        const userInfoToUpdate = {
            name: inputName.value
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
    })
});