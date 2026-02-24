function getHostName() {
    return location.hostname;
}

// This is a function that make sure that the server is online in a very fast way
async function checkServerStatus() {
    const url = `${window.ENV.API_URL}ping`;

    try {
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 5000);

        const response = await fetch(url, { signal: controller.signal });
        clearTimeout(timeoutId);

        return true;
    } catch (error) {
        return false;
    }
}