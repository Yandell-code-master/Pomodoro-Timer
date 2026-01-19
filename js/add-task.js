document.addEventListener("DOMContentLoaded", function() {
    btnAddTask = document.getElementById("btn-add-task");
    createTaskContainer = document.getElementById("create-task");
    btnSaveTask = document.getElementById("save-task");
    btnCacelTask = document.getElementById("cancel-task");
    btnUp = document.querySelector(".btn-up");
    btnDown = document.querySelector(".btn-down");
    numberInput = document.querySelector(".quantity-pomodoros-input");

    btnAddTask.addEventListener("click", function() {
        btnAddTask.style.display = "none";
        createTaskContainer.style.display = "block";
    });

    btnSaveTask.addEventListener("click", function() {
        let taskName = document.querySelector('.add-task input[type="text"]').value.trim();
        let pomodoros = numberInput.value;
        if (taskName !== '') {
            // Create task display container
            let taskContainer = document.createElement('div');
            taskContainer.className = 'task-display';
            taskContainer.innerHTML = `<p class="task-name">${taskName}</p><p class="task-pomodoros">Pomodoros: ${pomodoros}</p>`;
            // Insert after .linea-task
            let lineaTask = document.querySelector('.linea-task');
            lineaTask.insertAdjacentElement('afterend', taskContainer);
            // Hide form, show button
            createTaskContainer.style.display = "none";
            btnAddTask.style.display = "block";
            // Clear inputs
            document.querySelector('.add-task input[type="text"]').value = '';
            numberInput.value = '1';
        } else {
            alert('Please enter a task name.');
        }
    });

    btnCacelTask.addEventListener("click", function() {
        createTaskContainer.style.display = "none";
        btnAddTask.style.display = "block";
    });

    btnUp.addEventListener("click", function() {
        let current = parseInt(numberInput.value) || 1;
        numberInput.value = current + 1;
    });

    btnDown.addEventListener("click", function() {
        let current = parseInt(numberInput.value) || 1;
        if (current > 1) {
            numberInput.value = current - 1;
        }
    });
});