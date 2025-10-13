document.addEventListener("DOMContentLoaded", function() {
    btnAddTask = document.getElementById("btn-add-task");
    createTaskContainer = document.getElementById("create-task");
    btnSaveTask = document.getElementById("save-task");
    btnCacelTask = document.getElementById("cancel-task");
    

    btnAddTask.addEventListener("click", function() {
        btnAddTask.style.display = "none";
        createTaskContainer.style.display = "block";
    });
});