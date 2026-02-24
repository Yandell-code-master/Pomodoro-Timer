document.addEventListener("DOMContentLoaded", () => {
    const profileImage = document.getElementById("card-information-img");
    const inputFile = document.getElementById("input-file");

    profileImage.addEventListener("click", () => {
        inputFile.click();
    });


    // Aquí lo que hacemos es utilizar el evento change, que sirve para detectar cuando un input fue cambiado de estado osea que le agregaron algun dato por lo que nos sirve
    // para detectar cuando el usuario agrega la foto
    inputFile.addEventListener("change", () => {
        if (inputFile.files.length > 0) {
            const file = inputFile.files[0];

            // esto es basicamente un objeto especial que maneja toda la logica del tipo de dato multipart y bounderies, por lo que no tenemos que hacerlo nosotros
            const formData = new FormData();
            formData.append('userPhoto', file);

            uploadToServer(formData);
        }
    });

    async function uploadToServer(formData) {
        const user = JSON.parse(localStorage.getItem("userData"));
        const FETCH_PHOTO_URL = ENV.API_URL + `users/${user.id}/photo`;
        const FETCH_USER_URL = ENV.API_URL + `users/email-type/${user.id}`;

        // Cuando nosotros le pasamos un form data a un fetch en el body este sobreentiendo que el tipo de body es multipart entonces lo que hace es manejar todo la lógica 
        // de poner en el header el content type y menjar los bounderies que es el formato por el cual se separan los diferentes datos que van en el form data
        // hay que tener en cuenta que es bueno mandarlo de esta form ya que el form data está diseñado para especificamente poder transportar imagenes en binario sin 
        // corromperlas

        try {
            const fetchingPhoto = await fetch(FETCH_PHOTO_URL, {
                method: 'POST',
                body: formData
            });

            // Putting the photo in src image
            profileImage.src = await fetchingPhoto.text();

            const fetchingUser = await fetch(FETCH_USER_URL, {
                method: 'GET'
            });

            if (!fetchingPhoto.ok) {
                throw new Error("SERVER_ERROR_PHOTO: " + fetchingPhoto.status);
            }

            if (!fetchingUser.ok) {
                throw new Error("SERVER_ERROR_USER: " + fetchingUser.status);
            }

            localStorage.removeItem("userData")
            localStorage.setItem("userData", JSON.stringify(await fetchingUser.json()));
        } catch (error) {
            if (error.name.startsWith("SERVER_ERROR_PHOTO")) {
                alert("Something went wrong trying to save your photo");
            } else if (error.name.startsWith("SERVER_ERROR_USER")) {
                alert("Something went wrong tyring to get the new user");
            } else {
                alert("The server didn't respond");
            }
        }
    }
});