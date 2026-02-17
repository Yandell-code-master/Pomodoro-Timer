const singUpEmailButon = document.getElementById("sing-up-email-buton");

singUpEmailButon.addEventListener("click", async () => {
    event.preventDefault();
    const URL_BACKEND = "http://localhost:8080/users/log-in-email";
    const emailInput = document.getElementById("email-input");
    const passwordInput = document.getElementById("password-input");
    const logInDTO = {
        email : emailInput.value,
        password : passwordInput.value
    }

    let response = await fetch(URL_BACKEND, {
        method : 'POST',
        headers : {
            'Content-Type' : 'application/json'
        },
        body : JSON.stringify(logInDTO)
    }) 

    if (!response.ok) {
        throw new Error("Something went wrong trying to log in");
    }
    
    

    logInUser(await response.json());
});

function decodeJWT(token) {

    let base64Url = token.split(".")[1];
    let base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    let jsonPayload = decodeURIComponent(
        atob(base64)
            .split("")
            .map(function (c) {
                return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
            })
            .join("")
    );
    return JSON.parse(jsonPayload);
}

async function handleCredentialResponse(userToken) {
    const BACKEND_ENDPOINT = "http://localhost:8080/users/log-in-google"
    let user = decodeJWT(userToken.credential);

    let logInGoogleDTO = {
        jsonWebToken: {
            jwt: userToken.credential,
            isValid: false
        },
        userFromGoogle: {
            name: user.name,
            email: user.email,
            googleId: user.sub
        }
    }

    const response = await fetch(BACKEND_ENDPOINT, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(logInGoogleDTO)
    });



    if (!response.ok) {
        throw new Error("Something went wrong trying to log in");
    }

    logInUser(user);
}

function logInUser(userData) {
    localStorage.setItem("isLoged", "true");
    localStorage.setItem("userData", JSON.stringify(userData))
    window.location.href = "http://localhost:8000/index.html";
}

function getTasksUser() {

}