document.addEventListener("DOMContentLoaded", () => {
    const logOutDropDownItem = document.getElementById("log-out-dropdown-item");

    logOutDropDownItem.addEventListener("click", () => {
        localStorage.removeItem("userData");
        localStorage.removeItem("isLoged");
        window.location.href = ENV.URL_FRONTEND + "index.html";
    });
});