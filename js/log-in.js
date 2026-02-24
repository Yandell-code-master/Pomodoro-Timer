const singUpEmailButon = document.getElementById("sing-up-email-buton");

singUpEmailButon.addEventListener("click", async () => {
    event.preventDefault();
    const URL_BACKEND = ENV.API_URL + "users/log-in-email";
    const emailInput = document.getElementById("email-input");
    const passwordInput = document.getElementById("password-input");
    const logInDTO = {
        email: emailInput.value,
        password: passwordInput.value
    }

    try {
        let logInFetch = await fetch(URL_BACKEND, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(logInDTO)
        })

        if (!logInFetch.ok) {
            throw new Error("SERVER_ERROR: " + logInFetch.status);
        }

        logInUser(await logInFetch.json());
    } catch (error) {
        if (error.message.startsWith("SERVER_ERROR")) {
            alert("Somthing went wrong trying to log in the user");
        } else {
            alert("The server didn't respond.")
            throw new Error("The server didn't respond: " + error.message);
        }
    }
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
    const BACKEND_ENDPOINT = ENV.API_URL + "users/log-in-google"
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
    window.location.href = ENV.URL_FRONTEND + "index.html";
}

function renderGoogleButton() {
    const container = document.getElementById('google-button-container');
    const wrapper = document.querySelector('.btn-google-wrapper');

    if (container && wrapper) {
        let currentWidth = wrapper.offsetWidth;
        container.innerHTML = "";

        const isSmall = currentWidth < 210;

        // Definimos configuraciones distintas para evitar conflictos
        const options = isSmall ? {
            type: "icon",           // Modo cuadrado/círculo
            shape: "square",        // 'square' suele cargar mejor el logo en iconos
            theme: "outline",
            size: "large"
            // IMPORTANTE: En modo icon NO enviamos width, text ni logo_alignment
        } : {
            type: "standard",       // Modo largo con texto
            shape: "rectangular",
            theme: "outline",
            text: "signin_with",
            size: "large",
            logo_alignment: "left",
            width: currentWidth     // Solo aquí enviamos el ancho
        };

        google.accounts.id.renderButton(container, options);
    }
}

// Usamos una sola declaración para el resize con un pequeño retraso (debounce)
let resizeTimeout;
window.addEventListener('resize', () => {
    clearTimeout(resizeTimeout);
    resizeTimeout = setTimeout(renderGoogleButton, 10);
});

// Además de cargar el script, llamamos a la función cuando Google Identity esté listo
window.onload = () => {
    // Si usas el script async, es mejor esperar un segundo a que google.accounts esté disponible
    setTimeout(renderGoogleButton, 10);
};