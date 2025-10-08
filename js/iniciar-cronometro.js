activo = false;
btnIniciar = document.getElementById("btn-iniciar");
btnOmitir = document.getElementById("btn-omitir")

document.addEventListener("DOMContentLoaded", function() {
    console.log("DOM listo 2");

    btnIniciar.addEventListener("click", function() {
        buttonSound = new Audio('resources/ui-click-43196.mp3');
        buttonSound.play();

        if (activo) {
            cambiarEstiloNoIniciado();
            activo = false;
        } else {
            cambiarEstiloIniciado();
            activo = true;
        }
    });
});

function cambiarEstiloIniciado() {
    btnOmitir.classList.add("btn-visible");
    btnOmitir.classList.remove("btn-invisible");

    btnIniciar.innerText = "PAUSE";

    btnIniciar.classList.remove("btn-iniciar-desactivado");
    btnIniciar.classList.add("btn-iniciar-activado");
}


function cambiarEstiloNoIniciado() {
    btnIniciar.innerText = "START"

    btnOmitir.classList.add("btn-invisible");
    btnOmitir.classList.remove("btn-visible")

    btnIniciar.classList.add("btn-iniciar-desactivado");
    btnIniciar.classList.remove("btn-iniciar-activado");
}
