document.addEventListener("DOMContentLoaded", function () {
    btnAddTask = document.getElementById("btn-add-task");
    createTaskContainer = document.getElementById("create-task");
    btnSaveTask = document.getElementById("save-task");
    btnCacelTask = document.getElementById("cancel-task");
    btnUp = document.querySelector(".btn-up");
    btnDown = document.querySelector(".btn-down");
    numberInput = document.querySelector(".quantity-pomodoros-input");

    btnAddTask.addEventListener("click", function () {
        if (!localStorage.getItem("isLoged")) {
            alert("You have to sing up first");
            return;
        }

        btnAddTask.style.display = "none";
        createTaskContainer.style.display = "block";
    });

    btnSaveTask.addEventListener("click", async function () {
        const URL_BACKEND = ENV.API_URL + "tasks";
        let taskName = document.querySelector('.add-task input[type="text"]').value.trim();
        let pomodoros = numberInput.value;
        let jsonUser = localStorage.getItem("userData");
        let task = {
            name: taskName,
            pomodoros: pomodoros,
            user: JSON.parse(jsonUser)
        }

        if (taskName == '') {
            alert('Please enter a task name.');
            return;
        }

        try {
            const savingTaskResponse = await fetch(URL_BACKEND, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(task)
            })

            if (!savingTaskResponse.ok) {
                throw new Error("SERVER_ERROR: " + savingTaskResponse.status);
            }

            const taskSaved = await savingTaskResponse.json();
            const lineaTask = document.querySelector('.linea-task');

            renderTask(taskSaved.id, taskSaved.name, taskSaved.pomodoros, lineaTask);
        } catch (error) {
            if (error.message.startsWith("SERVER_ERROR")) {
                alert("Something went wrong trying to save the task (The server responded with an error).");
            } else {
                alert("The server didn't respond.");
                throw new Error("The server didn't respond: " + error);
            }
        }
    });

    btnCacelTask.addEventListener("click", function () {
        createTaskContainer.style.display = "none";
        btnAddTask.style.display = "block";
    });

    btnUp.addEventListener("click", function () {
        let current = parseInt(numberInput.value) || 1;
        numberInput.value = current + 1;
    });

    btnDown.addEventListener("click", function () {
        let current = parseInt(numberInput.value) || 1;
        if (current > 1) {
            numberInput.value = current - 1;
        }
    });
});