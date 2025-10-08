tiempo = document.getElementById("tiempo");
btnIniciar = document.getElementById("btn-iniciar");
btnPomodoro = document.getElementById("btn-pomodoro");
btnSBreak = document.getElementById("btn-sbreak");
btnLBreak = document.getElementById("btn-lbreak");

active = false;

document.addEventListener("DOMContentLoaded", function () {
    interval = null;

    // Cuando se aprieta el boton se activa el cronometro o se desactiva
    btnIniciar.addEventListener("click", function () {
        if (!active) {
            interval = setInterval(restarTiempo, 100); // Se ejecuta cada 1 segundo
            active = true;
        } else {
            active = false;
            clearInterval(interval);
        }
    });

    btnPomodoro.addEventListener("click", () => {
        clearInterval(interval);
        setCantidadTiempo("25");
    });

    btnSBreak.addEventListener("click", function() {
        clearInterval(interval);
        setCantidadTiempo("05");   
    });

    btnLBreak.addEventListener("click", () => {
        clearInterval(interval);
        setCantidadTiempo("15");
    });
});

function setCantidadTiempo(minutos) {
    tiempo.innerText = minutos + ":00";
}

// Le resta un segundo al cronometro y lo cambia en el texto que sale en la página
function restarTiempo() {
    tiempo = document.getElementById("tiempo");
    minutos = tiempo.innerText.substring(0, 2);
    segundos = tiempo.innerText.substring(3, 5);

    if (segundos == "00") {
        segundos = "59";
        minutos = String(Number(minutos) - 1);

        [minutos, segundos] = agregarCeros(minutos, segundos);
    } else {
        segundos = String(Number(segundos) - 1);

        [minutos, segundos] = agregarCeros(minutos, segundos);
    }

    tiempo.innerText = minutos + ":" + segundos;
}


// No introducir null en esta función como parámetros
// Se le pasa los minutos y los segundos en forma de string y si está en unidad se le pone un cero 
// A la izquierda
function agregarCeros(minutos, segundos) {
    if ((minutos != null) && (minutos.length == 1)) {
        minutos = "0" + minutos;
    } 

    if ((segundos != null) && (segundos.length == 1)) {
        segundos = "0" + segundos;
    }

    return [minutos, segundos];
}

