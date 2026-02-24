document.addEventListener("DOMContentLoaded", event => {
    let tokenClient;

    const initGoogle = () => {
        if (typeof google !== 'undefined' && google.accounts) {

            tokenClient = google.accounts.oauth2.initTokenClient({
                client_id: "913993571162-hirk98knfjtejttd92udhug2v0kqrp3r.apps.googleusercontent.com",
                scope: "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email",
                callback: (tokenResponse) => {
                    if (tokenResponse && tokenResponse.access_token) {
                        handleCredentialResponse(tokenResponse.access_token);
                    }
                }
            });

            const buton = document.getElementById("btn-google");

            buton.onclick = () => {
                tokenClient.requestAccessToken();
            };
        } else {
            setTimeout(initGoogle, 100);
        }
    };

    initGoogle();
});


async function handleCredentialResponse(accessToken) {
    const URL_GOOGLE = 'https://www.googleapis.com/oauth2/v3/userinfo';
    const URL_BACKEND = ENV.API_URL + 'users/save-user-google';

    try {
        let googleResponse = await fetch(URL_GOOGLE, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        });

        if (!googleResponse.ok) {
            throw new Error("GOOGLE_API_ERROR: " + googleResponse.status);
        }

        let data = await googleResponse.json();

        let userFromGoogle = {
            name: data.name,
            email: data.email,
            googleId: data.sub
        }

        let savingUserFetch = await fetch(URL_BACKEND, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userFromGoogle)
        })

        if (!savingUserFetch.ok) {
            throw new ("SERVER_ERROR: " + savingUserFetch.status);
        }
    } catch (error) {
        if (error.message.startsWith("GOOGLE_API_ERROR") === "GOOGLE_API_ERROR") {
            alert("Something went wrong trying to fetch de identity from the google API.");
        } else if (error.message.startsWith("SERVER_ERROR") === "SERVER_ERROR") {
            alert("Something went wrong tyring to save the new user.")
        } else {
            alert("The server didn't respond.");
        }
    }
}