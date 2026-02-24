document.addEventListener("DOMContentLoaded", () => {
    if (verifyIsUserLoged()) {
        loadUserInformation();
    } else {
        loadDefaultIndex();
    }
});

const listBtnNav = document.getElementById("list-btn-nav");
const singInItem = document.getElementById("sing-in-item");
const logInItem = document.getElementById("log-in-dropdown-item");
const logOutDropDownItem = document.getElementById("log-out-dropdown-item");
const deleteAccountDropDownItem = document.getElementById("delete-account-dropdown-item");
const threeDotsIcon = document.getElementById("three-dots-icon");
const userImage = document.getElementById("user-image");
const accountDropdownItem = document.getElementById("account-dropdown-item");
const threeDotBtn = document.getElementById("three-dot-btn");
const cardInformationImg = document.getElementById("card-information-img");
const inputUserName = document.getElementById("user-name");

function verifyIsUserLoged() {
    return localStorage.getItem("isLoged");
}

async function loadUserInformation() {
    const user = JSON.parse(localStorage.getItem("userData"));

    // UI de navegación (tu código original)
    listBtnNav.style.marginLeft = "auto";
    listBtnNav.style.marginRight = "auto";

    singInItem.style.display = "none";
    logInItem.style.display = "none";
    threeDotsIcon.style.display = "none";
    logOutDropDownItem.style.display = "block";
    deleteAccountDropDownItem.style.display = "block";
    accountDropdownItem.style.display = "block";
    threeDotBtn.style.padding = "4px";
    inputUserName.value = user.name;

    if (user.picture) {
        userImage.src = user.picture;
        cardInformationImg.src = user.picture;
    } else {
        cardInformationImg.src = "./resources/user-big-black.png";
        userImage.src = "./resources/user-big-black.png";
    }
    userImage.style.display = "block";

    // NUEVO: Cargar las tareas del usuario desde el servidor
    await fetchAndRenderTasks(user.id);
}

// Función para traer y pintar las tareas
async function fetchAndRenderTasks(userId) {
    const URL_TASKS = ENV.API_URL + `tasks/${userId}`;

    try {
        const response = await fetch(URL_TASKS);
        if (!response.ok) throw new Error("Error al obtener tareas");

        const tasks = await response.json();
        const lineaTask = document.querySelector('.linea-task');

        // Limpiar tareas previas para no duplicar si se recarga
        document.querySelectorAll('.task-display').forEach(el => el.remove());

        tasks.forEach(task => {
            renderTask(task.id, task.name, task.pomodoros, lineaTask);
        });
    } catch (error) {
        console.error("The server didn't respond.");
    }
}

function renderTask(id, name, pomodoros, referenceElement) {
    let taskContainer = document.createElement('div');
    taskContainer.className = 'task-display';

    // Agregamos el ID a los datos del elemento (buena práctica)
    taskContainer.dataset.id = id;

    taskContainer.innerHTML = `
        <div class="task-display-content">
            <p class="task-name">${name}</p>
            <p class="task-pomodoros">Pomodoros: ${pomodoros}</p>
        </div>
        <button class="btn-delete-task" title="Eliminar tarea">
            <i class="bi bi-trash"></i>
        </button>
    `;

    // CORRECCIÓN: El selector debe coincidir con la clase del botón arriba
    const deleteBtn = taskContainer.querySelector('.btn-delete-task');

    deleteBtn.addEventListener('click', async () => {
        // Llamamos a la función que borra en el servidor
        const fueBorrado = await deleteTask(id);
        if (fueBorrado) {
            taskContainer.remove(); // Quitamos de la vista
        }
    });

    referenceElement.insertAdjacentElement('afterend', taskContainer);
}

async function deleteTask(id) {
    const URL_BACKEND = ENV.API_URL + `tasks/${parseInt(id)}`;

    try {
        const deletingTaskResponse = await fetch(URL_BACKEND, {
            method: 'DELETE'
        })

        if (!deletingTaskResponse.ok) {
            return false;
        }

        return true;
    } catch (error) {
        alert("The server didn't respond.");
    }
}

function loadDefaultIndex() {
    listBtnNav.style.marginLeft = "110px";
    singInItem.style.display = "block";
    logInItem.style.display = "block";
    threeDotsIcon.style.display = "block";
    logOutDropDownItem.style.display = "none";
    deleteAccountDropDownItem.style.display = "none";
    accountDropdownItem.style.display = "none";
    userImage.src = "./resources/user-big-black.png";
}