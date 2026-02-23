btnClicked = null;
btnIniciar = document.getElementById("btn-iniciar");
btnOmitir = document.getElementById("btn-omitir");

document.addEventListener("DOMContentLoaded", function () {
    btn1 = document.getElementById("btn-pomodoro");
    btn2 = document.getElementById("btn-sbreak");
    btn3 = document.getElementById("btn-lbreak");

    btn1.addEventListener("click", () => manejarClick(btn1));
    btn2.addEventListener("click", () => manejarClick(btn2));
    btn3.addEventListener("click", () => manejarClick(btn3));
});

// Cambia el estilo de los botones, simulando que fuera un combobox
function cambiarPresionadoEstilo(btn) {
    btn.classList.add("btn-contenedor-reloj-pressed")
    btn.classList.remove("btn-contenedor-noPressed");
}


function cambiarNoPresionadoEstilo(btn) {
    btn.classList.add("btn-contenedor-reloj-noPressed")
    btn.classList.remove("btn-contenedor-reloj-pressed");
}

// Manje la logica que debe suceder cuando se presiona una de los botones
function manejarClick(btn) {
    if (btnClicked) {
        cambiarNoPresionadoEstilo(btnClicked);
        cambiarPresionadoEstilo(btn);
    } else {
        cambiarPresionadoEstilo(btn);
    }

    btnClicked = btn;
}

// Cambia el estilo del boton para iniciar
function cambiarEstiloNoIniciado() {
    btnIniciar.innerText = "START"

    btnOmitir.classList.add("btn-invisible");
    btnOmitir.classList.remove("btn-visible")

    btnIniciar.classList.add("btn-iniciar-desactivado");
    btnIniciar.classList.remove("btn-iniciar-activado");
}