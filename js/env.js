window.ENV = {
    API_URL: window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
        ? 'http://localhost:8080/'
        : 'https://backend-pomodoro-timer-production.up.railway.app/',
    URL_FRONTEND : window.localStorage.hostname === "localhost" || window.location.hostname === "127.0.0.1"
        ? "http://localhost:800"
        : "http://yandell-code-master/Pomodoro-Timer"
};