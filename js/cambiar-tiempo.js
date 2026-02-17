let tiempo = document.getElementById("tiempo");
let btnIniciar = document.getElementById("btn-iniciar");
let btnPomodoro = document.getElementById("btn-pomodoro");
let btnSBreak = document.getElementById("btn-sbreak");
let btnLBreak = document.getElementById("btn-lbreak");

let active = false;
let interval = null;
let tiempoInicial = "25:00"; // Guardamos el valor para resetear al terminar

document.addEventListener("DOMContentLoaded", function () {
    btnIniciar.addEventListener("click", function () {
        if (!active) {
            // Cambiamos a 1000ms (1 segundo real)
            interval = setInterval(restarTiempo, 1000);
            active = true;
        } else {
            pausarCronometro();
        }
    });

    btnPomodoro.addEventListener("click", () => configuracionInicial("25:00"));
    btnSBreak.addEventListener("click", () => configuracionInicial("05:00"));
    btnLBreak.addEventListener("click", () => configuracionInicial("15:00"));
});

function configuracionInicial(valor) {
    pausarCronometro();
    tiempoInicial = valor; // Actualizamos el valor de reset
    tiempo.innerText = valor;
}

function pausarCronometro() {
    active = false;
    clearInterval(interval);
}

function restarTiempo() {
    let tiempoTexto = tiempo.innerText;
    let minutos = parseInt(tiempoTexto.substring(0, 2));
    let segundos = parseInt(tiempoTexto.substring(3, 5));

    // Lógica de término
    if (minutos === 0 && segundos === 0) {
        pausarCronometro();
        tiempo.innerText = tiempoInicial;
        cambiarNoPresionadoEstilo(btnIniciar);
        return;
    }

    if (segundos === 0) {
        segundos = 59;
        minutos--;
    } else {
        segundos--;
    }

    // Formateo con ceros usando padStart (más limpio que la función manual)
    let minStr = String(minutos).padStart(2, '0');
    let segStr = String(segundos).padStart(2, '0');

    tiempo.innerText = `${minStr}:${segStr}`;
}