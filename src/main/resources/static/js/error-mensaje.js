window.onload = function () {
    let error = document.getElementById("error").textContent;
    let mensaje = document.getElementById("mensaje").textContent;

    if (error.trim() !== "") {
        alert(error);
    }

    if (success.trim() !== "") {
        alert(mensaje);
    }
};